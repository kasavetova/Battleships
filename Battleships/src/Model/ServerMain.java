package Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerMain extends Thread {
    public static void main(String[] args) throws IOException {

        int portNumber = 4445;

        ServerSocket serverSocket = new ServerSocket(portNumber);
        System.out.println("Connecting players");

        //Creates a thread for any new players

        while (true) {
            Socket clientSocket = serverSocket.accept();
            ServerThread.addToList(new ServerThread(clientSocket));
        }

    }
}
