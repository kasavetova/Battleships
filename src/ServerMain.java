import java.net.ServerSocket;
import java.net.Socket;

/**
 * Creates instances of {@link ServerThread}, for every new connection to the socket.
 *
 * @author Team 1-O
 * 
 * @see Thread
 * @see ServerSocket
 * @see Socket
 */

public class ServerMain extends Thread {
	/**
	 * Runs the server
	 * @param args
	 * @throws Exception When an error has occurred trying to connect
	 */
	public static void main(String[] args) {

		try{
			int portNumber = 4444;
			ServerSocket serverSocket = new ServerSocket(portNumber);

			System.out.println("Connecting players");

			serverSocket.setReuseAddress(true);

			while (true) {
				Socket clientSocket = serverSocket.accept();
				clientSocket.setReuseAddress(true);
				new ServerThread(clientSocket);
			}


		} catch (Exception e){
			System.out.println("The following error has occurred: \n" + e.getMessage());
		}

	}
}
