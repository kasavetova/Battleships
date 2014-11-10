import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Creates instances of ServerThread, for every new connection to the socket
 *
 * @author Team 1-O
 */

public class ServerMain extends Thread {
    public static void main(String[] args) throws IOException {

        int portNumber = 4446;
        ServerSocket serverSocket = new ServerSocket(portNumber);
        
        System.out.println("Connecting players");
        
        serverSocket.setReuseAddress(true);
        
        while (true) {
            Socket clientSocket = serverSocket.accept();
            clientSocket.setReuseAddress(true);
            new ServerThread(clientSocket);
        }

    }
}
