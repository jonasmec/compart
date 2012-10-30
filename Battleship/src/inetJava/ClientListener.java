package inetJava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientListener extends Thread {
	
	private ServerDispatcher serverDispatcher;
	private ClientInfo clientInfo;
	private BufferedReader inReader;

	public ClientListener(ClientInfo aClientInfo, ServerDispatcher aServerDispatcher) throws IOException {
		clientInfo = aClientInfo;
		serverDispatcher = aServerDispatcher;
		Socket socket = aClientInfo.clientSocket;
		inReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	/**
	 * Until interrupted, reads messages from the client socket, forwards them
	 * to the server dispatcher's queue and notifies the server dispatcher.
	 */
	public void run() {
		try {
			while (!isInterrupted()) {
				String message = inReader.readLine();
				if (message == null)
					break;
				serverDispatcher.dispatchMessage(clientInfo, message);
			}
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}

		// Communication is broken. Interrupt both listener and sender threads
		clientInfo.clientRemetente.interrupt();
		serverDispatcher.deleteClient(clientInfo);
	}

}
