package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


public class ServerThread extends Thread {

    private static ArrayList<ServerThread> serverThreads = new ArrayList<ServerThread>();
    private static int threadInstances = 0;

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private String name;
    private int playerNum;
    private boolean inGame;
    private static Request r = null;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        playerNum = threadInstances++;
        System.out.println("Player " + playerNum + " connected");
        inGame = false;
        this.start();
    }

    public String getPlayerName() {
        return name;
    }

    public static void addToList(ServerThread s) {
        serverThreads.add(s);
    }

    public static void removeFromList(ServerThread s) {
        serverThreads.remove(s);
        updateLists();
    }

    public void run() {
        try {

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //message each player there player number
            message("Player " + playerNum);
            String input;
            while ((input = in.readLine()) != null) {
                //Gets name request from player
                if (input.startsWith("Name is:")) {
                    name = input.substring(9);
                    System.out.println(name);
                    updateLists();
                    break;
                }
            }
            while ((input = in.readLine()) != null) {
                //Routes the game requests to other player
                if (input.startsWith("Request")) {
                    String s = input.substring(8);
                    for (ServerThread st : serverThreads) {
                        if (s.equals(st.getPlayerName())) {
                            r = new Request(this, st);
                            break;
                        }
                    }

                }
                //Handles closing connections
                else if (input.startsWith("Closing")) {
                    removeFromList(this);
                    System.out.println(playerNum + " closed");
                    out.close();
                    in.close();
                    interrupt();
                } else if (input.startsWith("YES")) {
                    System.out.println("YES");
                    r.answer(1);
                } else if (input.startsWith("NO")) {
                    //System.out.println("NO");
                    r.answer(0);
                    r = null;
                } else {
                    System.out.println(input);
                }

            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    public void message(String x) {
        out.println(x);
    }

    public static void updateLists() {
        //send new list of active players out to each player

        String x = "Update";

        for (ServerThread st : serverThreads) {
            if (!st.inGame) {
                x = x + " " + st.getPlayerName();
            }
            st.message(x);
        }

    }

    public void setInGame(Boolean x) {
        inGame = x;
        System.out.println(inGame);
        updateLists();
    }
}
