import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Handles the connection of an individual player
 *
 * @author Team 1-O
 */

public class ServerThread extends Thread {
    private static ArrayList<ServerThread> serverThreads = new ArrayList<ServerThread>();
    private Socket clientSocket;
    private static int threadInstances = 0;
    private String username;
    private int playerNumber;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean inGame;
    private boolean isReady;
    private Board gameBoard;
    private ArrayList<String> lobbyList;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        checkName();
    }

    public void checkName() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            lobbyList = new ArrayList<String>();
            for (ServerThread st : serverThreads) {
                if (st.getName() != null) {
                    lobbyList.add(st.getPlayerName());
                }
            }
            message(new Request("RetrieveLobby", "SERVER", "", lobbyList));
            in = new ObjectInputStream(clientSocket.getInputStream());
            Request input;
            while ((input = (Request) in.readObject()) != null) {
                if (input.getActionType().equals("Accepted")) {
                    createPlayerThread();
                    break;
                }
                if (input.getActionType().equals("Rejected")) {
                    out.close();
                    in.close();
                    clientSocket.close();
                    serverThreads.remove(this);
                    break;
                }

            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getPlayerName() {
        return username;
    }

    public void createPlayerThread() {
        playerNumber = threadInstances++;
        System.out.println("Player " + playerNumber + " connected");
        inGame = false;
        serverThreads.add(this);
        this.start();
    }


    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());
            Request input;
            while ((input = (Request) in.readObject()) != null) {
                String actionType = input.getActionType();
                switch (actionType) {
                    case "UserJoinedLobby":
                        username = input.getOrigin();
                        messageAllActive(new Request("UserJoinedLobby", "SERVER", "ALL", username));
                        break;
                    case "UserLeftLobby":
                        messageAllActive(new Request("UserLeftLobby", "SERVER", "ALL", input.getOrigin()));
                        break;
                    case "SendMessage":
                        messageAll(new Request("ReceiveMessage", input.getOrigin(), input.getDestination(), input.getObject()));
                        break;
                    case "RetrieveLobby":
                        lobbyList = new ArrayList<String>();
                        for (ServerThread st : serverThreads) {
                            if (!st.inGame) {
                                lobbyList.add(st.getPlayerName());
                            }
                        }
                        messageAllActive(new Request("RetrieveLobby", "SERVER", input.getOrigin(), lobbyList));
                        break;
                    case "UserWentBackToLobby":
                        System.out.println("backtolobby");
                        for (ServerThread st : serverThreads) {
                            if (st.getPlayerName().equals(input.getDestination())) {
                                messageAllActive(new Request("UserJoinedLobby", "SERVER", "ALL", username));
                                messageAllActive(new Request("UserJoinedLobby", "SERVER", "ALL", st.getPlayerName()));
                                st.setInGame(false);
                                st.setPlayerStatus(false);
                                st.message(input);
                                setInGame(false);
                                setPlayerStatus(false);
                                break;
                            }
                        }
                        break;
                    case "GameBoard":
                        gameBoard = (Board) input.getObject();
                        break;
                    case "Move":
                        if (!input.getDestination().equals(username)) {
                            for (ServerThread serverThread : serverThreads) {
                                if (serverThread.getPlayerName().equals(input.getDestination())) {
                                    serverThread.completeMove(input);
                                    break;
                                }
                            }
                        }
                        break;
                    case "MoveEnded":
                        messageAll(input);
                        break;
                    case "PlayerReady":
                        if (input.getOrigin().equals(username)) {
                            isReady = true;
                        }
                        for (ServerThread st : serverThreads) {
                            if (st.getPlayerName().equals(input.getDestination()) && st.getPlayerStatus()) {
                                message(new Request("GameStart", input.getDestination(), username));
                                st.message(new Request("GameStart", username, input.getDestination()));
                                break;
                            }
                        }
                        break;
                    case "PlayerBusy":
                        messageAll(input);
                        break;
                }
                if (input.getActionType().startsWith("GameRequest")) {
                    messageAllActive(input);
                    if (input.getActionType().equals("GameRequestAnswer") && input.getObject().equals("Yes")) {
                        for (ServerThread st : serverThreads) {
                            if (st.getPlayerName().equals(input.getOrigin())
                                    || st.getPlayerName().equals(input.getDestination())) {
                                st.setInGame(true);
                                messageAllActive(new Request("UserLeftLobby", "SERVER", "ALL", st.getPlayerName()));
                            }
                        }
                    }
                }
                //Handles closing connections
                else if (input.getActionType().equals("UserClosed")) {
                    //If user closes on lobby screen

                    serverThreads.remove(this);
                    messageAll(new Request("UserLeftLobby", "SERVER", "ALL", username));
                    System.out.println(username + " has exited.");
                    out.close();
                    break;
                } else if (input.getActionType().equals("UserLeftGame")) {
                    //If user closes during shipselection/gameui
                    System.out.println("Sending" + input);
                    for (ServerThread st : serverThreads) {
                        if (st.getPlayerName().equals(input.getDestination())) {
                            st.message(input);
                            st.setInGame(false);
                            st.setPlayerStatus(false);
                            break;
                        }
                    }
                    serverThreads.remove(this);
                    messageAll(new Request("UserLeftLobby", "SERVER", "ALL", username));
                    System.out.println(username + " has exited.");
                    out.close();
                    break;

                }
            }
            in.close();
            clientSocket.close();

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + " or listening for a connection");
        } catch (ClassNotFoundException e) {

        }
    }

    public void message(Request r) throws IOException {
        out.writeObject(r);
    }

    public void messageAll(Request r) throws IOException {
        for (ServerThread st : serverThreads) {
            st.message(r);
        }
    }

    public void messageAllActive(Request r) throws IOException {
        for (ServerThread st : serverThreads) {
            if (!st.inGame) {
                st.message(r);
            }
        }
    }

    public void setInGame(Boolean x) throws IOException {
        inGame = x;
    }

    public boolean getPlayerStatus() {
        return isReady;
    }

    public void setPlayerStatus(Boolean status) {
        isReady = status;
    }

    public void completeMove(Request input) {
        GameMove gm = (GameMove) input.getObject();
        String outcome = gameBoard.shoot(gm.getMoveCoordinates());
        gm.setMoveResult(outcome);
        try {
            message(new Request("MoveResult", input.getOrigin(), input.getDestination(), gm));
            messageAll(new Request("MoveResult", input.getDestination(), input.getOrigin(), gm));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
