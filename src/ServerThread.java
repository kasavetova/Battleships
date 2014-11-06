import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;


public class ServerThread extends Thread {
    private static ArrayList<ServerThread> serverThreads = new ArrayList<ServerThread>();
    private Socket clientSocket;
    private static int threadInstances = 0;
    private String username;
    private int playerNumber;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean inGame;
    private Board gameBoard;
    private ArrayList<String> lobbyList;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        playerNumber = threadInstances++;
        System.out.println("Player " + playerNumber + " connected");
        inGame = false;
        this.start();
    }

    public String getPlayerName() {
        return username;
    }

    public static void addToList(ServerThread s) {
        serverThreads.add(s);
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

                } else if (input.getActionType().equals("RandomGameRequest")) {

                    int activePlayersCount = 0;
                    for (ServerThread st : serverThreads) {
                        if (st.inGame == false && !st.getPlayerName().equals(input.getOrigin())) {
                            activePlayersCount++;
                        }
                    }

                    if (activePlayersCount == 0) {
                        messageAll(new Request("RandomGameRequestFail", "SERVER", input.getOrigin()));
                    } else {
                        int index = new Random().nextInt(serverThreads.size());
                        if (serverThreads.get(index).inGame == false && !serverThreads.get(index).getPlayerName().equals(input.getOrigin())) {
                            for (ServerThread st : serverThreads) {
                                if (!st.getPlayerName().equals(input.getOrigin())) {
                                    st.messageAll(new Request("GameRequest", input.getOrigin(), serverThreads.get(index).getPlayerName()));
                                    break;
                                }
                            }
                        }
                    }
                }
                //Handles closing connections
                else if (input.getActionType().equals("UserClosed")) {
                    serverThreads.remove(this);
                    messageAll(new Request("UserLeftLobby", "SERVER", "ALL", username));
                    System.out.println(username + " has exited.");
                    out.close();
                    in.close();
                    interrupt();
                } else if (input.getActionType().startsWith("UserLeftGame")) {
                    System.out.println("Sending" + input);
                    for (ServerThread st : serverThreads) {
                        if (st.getPlayerName().equals(input.getDestination())) {
                            st.message(input);
                            st.setInGame(false);
                            break;
                        }
                    }

                    serverThreads.remove(this);
                    messageAll(new Request("UserLeftLobby", "SERVER", "ALL", username));
                    System.out.println(username + " has exited.");
                    out.close();
                    in.close();
                    interrupt();
                } else if (input.getActionType().equals("GameBoard")) {
                    gameBoard = (Board) input.getObject();
                } else if (input.getActionType().equals("Move")) {
                    /*
                	GameMove gm = (GameMove) input.getObject();
                	
                	Point coordinates = gm.getMoveCoordinates();
                	String playerName = gm.getPlayerName();
                	String outcome = gameBoard.completeMove(coordinates);
                	GameMove gmToSend = new GameMove((Point) coordinates, playerName, outcome);
                	
                	messageAll(new Request("MoveResult", "SERVER", input.getOrigin(), gmToSend));
                	messageAll(new Request("MoveResult", "SERVER", input.getDestination(), gmToSend));
                	*/
                    if (!input.getDestination().equals(username)) {

                        for (int i = 0; i < serverThreads.size(); i++) {
                            if (serverThreads.get(i).getPlayerName().equals(input.getDestination())) {
                                serverThreads.get(i).completeMove(input);
                                break;
                            }
                        }
                    }

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
