import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


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

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
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
                if (input.getActionType().equals("UserJoinedLobby")) {
                    username = input.getOrigin();
                    messageAllActive(new Request("UserJoinedLobby", "SERVER", "ALL", username));
                } else if (input.getActionType().equals("UserLeftLobby")) {
                    messageAllActive(new Request("UserLeftLobby", "SERVER", "ALL", input.getOrigin()));
                } else if (input.getActionType().equals("SendMessage")) {

                    messageAll(new Request("ReceiveMessage", input.getOrigin(), input.getDestination(), input.getObject()));
                } else if (input.getActionType().startsWith("GameRequest")) {
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
                } else if (input.getActionType().equals("RetrieveLobby")) {
                    lobbyList = new ArrayList<String>();
                    for (ServerThread st : serverThreads) {
                        if (st.inGame == false) {
                            lobbyList.add(st.getPlayerName());
                        }
                    }
                    messageAllActive(new Request("RetrieveLobby", "SERVER", input.getOrigin(), lobbyList));

                }
                //Handles closing connections
                else if (input.getActionType().equals("UserClosed")) {
                    //If user closes on lobby screen
                    serverThreads.remove(this);
                    messageAll(new Request("UserLeftLobby", "SERVER", "ALL", username));
                    System.out.println(username + " has exited.");
                    out.close();
                    in.close();
                    clientSocket.close();
                    interrupt();
                } else if (input.getActionType().startsWith("UserLeftGame")) {
                    //if user closes during shipselection/gameui
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
                    in.close();
                    clientSocket.close();
                    interrupt();
                } else if (input.getActionType().equals("UserWentBackToLobby")) {
                    for (ServerThread st : serverThreads) {
                        if (st.getPlayerName().equals(input.getDestination())) {
                            st.setInGame(false);
                            st.setPlayerStatus(false);
                            st.message(input);
                            setInGame(false);
                            setPlayerStatus(false);
                            break;

                        }
                    }
                } else if (input.getActionType().equals("GameBoard")) {
                    gameBoard = (Board) input.getObject();
                } else if (input.getActionType().equals("Move")) {
                    if (!input.getDestination().equals(username)) {
                        for (int i = 0; i < serverThreads.size(); i++) {
                            if (serverThreads.get(i).getPlayerName().equals(input.getDestination())) {
                                serverThreads.get(i).completeMove(input);
                                break;
                            }
                        }
                    }

                } else if (input.getActionType().equals("PlayerReady")) {
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
                } else if (input.getActionType().equals("PlayerBusy")) {
                    messageAll(input);
                } else {
                    System.out.println(input);
                }
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + " or listening for a connection");
            System.out.println("CATCH FROM MAIN THREAD" + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("CATCH FROM MAIN THREAD2" + e.getMessage());
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
            if (st.inGame == false) {
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
