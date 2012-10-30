package inetJava;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NakovChatServer {
	public static final int LISTENING_PORT = 2002;

	public static void main(String[] args) {
		// Open server socket for listening
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(LISTENING_PORT);
			System.out.println("NakovChatServer started on port " + LISTENING_PORT);
		} catch (IOException se) {
			System.err.println("Can not start listening on port " + LISTENING_PORT);
			se.printStackTrace();
			System.exit(-1);
		}

		// Start ServerDispatcher thread
		ServerDispatcher serverDispatcher = new ServerDispatcher();
		serverDispatcher.start();

		// Accept and handle client connections
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				ClientInfo clientInfo = new ClientInfo();
				clientInfo.clientSocket = socket;
				ClientListener clientListener = new ClientListener(clientInfo, serverDispatcher);
				ClientSender clientSender = new ClientSender(clientInfo, serverDispatcher);
				clientInfo.clientOuvinte = clientListener;
				clientInfo.clientRemetente = clientSender;
				clientListener.start();
				clientSender.start();
				serverDispatcher.addClient(clientInfo);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

}