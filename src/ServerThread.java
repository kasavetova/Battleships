import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ListIterator;
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
                if(input.getActionType().equals("UserJoinedLobby")){
                    username = input.getOrigin();
                    messageAllActive(new Request("UserJoinedLobby","SERVER", "ALL", username));
                } else if(input.getActionType().equals("UserLeftLobby")) {
                    messageAllActive(new Request("UserLeftLobby","SERVER", "ALL", input.getOrigin()));
                } else if(input.getActionType().equals("SendMessage")) {
                    messageAll(new Request("ReceiveMessage", input.getOrigin(), input.getDestination(), input.getObject()));
                	
                }

                else if (input.getActionType().startsWith("GameRequest")) {
                    messageAllActive(input);
                    if(input.getActionType().equals("GameRequestAnswer") && input.getObject().equals("Yes")) {
                        for (ServerThread st : serverThreads) {
                            if(st.getPlayerName().equals(input.getOrigin())
                                    || st.getPlayerName().equals(input.getDestination())) {
                                st.setInGame(true);
                                messageAllActive(new Request("UserLeftLobby", "SERVER", "ALL", st.getPlayerName()));
                            }
                        }
                    }
                } else if(input.getActionType().equals("RetrieveLobby")) {
                    lobbyList = new ArrayList<String>();
                    for (ServerThread st : serverThreads) {
                        if (st.inGame == false) {
                            lobbyList.add(st.getPlayerName());
                        }
                    }
                    messageAllActive(new Request("RetrieveLobby", "SERVER", input.getOrigin(), lobbyList));

                } else if(input.getActionType().equals("RandomGameRequest")) {
                    /*boolean search = true;
                    while(search) {

                        ListIterator<ServerThread>  it = serverThreads.listIterator();
                        if(it.hasNext()) {
                            ServerThread st = it.next();
                            System.out.println(st.getPlayerName());
                            if(st.inGame==false && !st.getPlayerName().equals(input.getOrigin())) {
                                st.message(new Request("GameRequest", input.getOrigin(), st.getPlayerName()));
                            }
                        }

                        int index = new Random().nextInt(serverThreads.size());
                        if(serverThreads.get(index).inGame==false && !serverThreads.get(index).equals(input.getOrigin())) {
                            for(ServerThread st: serverThreads) {
                                if(!st.getPlayerName().equals(input.getOrigin())){
                                    st.message(new Request("GameRequest", input.getOrigin(), serverThreads.get(index).getPlayerName()));
                                }
                            }
                            //break;
                        }
                    }*/
                    int activePlayersCount = 0;
                    for(ServerThread st: serverThreads) {
                        if(st.inGame==false && !st.getPlayerName().equals(input.getOrigin())){
                           activePlayersCount++;
                        }
                    }

                    if(activePlayersCount==0) {
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
                	System.out.println("Recieved");
                	if(input.getDestination().equals(username)){
                		System.out.println("recieved" + input);
                		setInGame(false);
                		messageAllActive(new Request("UserJoinedLobby","SERVER", "ALL", username));
                	}
                	else{
                		System.out.println("Sending"+input);
                		for (ServerThread st : serverThreads) {
                            if (st.getPlayerName().equals(input.getDestination())) {
                            	st.message(input);
                            }
                        }
                	}
                    
                    
                    serverThreads.remove(this);
                    messageAll(new Request("UserLeftLobby", "SERVER", "ALL", username));
                    System.out.println(username + " has exited.");
                    out.close();
                    in.close();
                    interrupt();
                }
                else {
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
        for(ServerThread st : serverThreads){
            st.message(r);
        }
    }

    public void messageAllActive (Request r ) throws IOException {
        for (ServerThread st : serverThreads) {
            if(st.inGame==false) {
                st.message(r);
            }
        }
    }
    public void setInGame(Boolean x) throws IOException {
        inGame = x;
    }

    
}
