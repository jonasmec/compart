package inetJava;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class ClientSender extends Thread {
	private Vector<String> filaMenssage = new Vector<String>();

	private ServerDispatcher mServerDispatcher;
	private ClientInfo infoClient;
	private PrintWriter ourPrint;

	public ClientSender(ClientInfo aClientInfo,	ServerDispatcher aServerDispatcher) throws IOException {
		infoClient = aClientInfo;
		mServerDispatcher = aServerDispatcher;
		Socket socket = aClientInfo.clientSocket;
		ourPrint = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	/**
	 * Adds given message to the message queue and notifies this thread
	 * (actually getNextMessageFromQueue method) that a message is arrived.
	 * sendMessage is called by other threads (ServeDispatcher).
	 */
	public synchronized void sendMessage(String aMessage) {
		filaMenssage.add(aMessage);
		notify();
	}

	/**
	 * @return and deletes the next message from the message queue. If the queue
	 *         is empty, falls in sleep until notified for message arrival by
	 *         sendMessage method.
	 */
	private synchronized String getNextMessageFromQueue()
			throws InterruptedException {
		while (filaMenssage.size() == 0)
			wait();
		String message = (String) filaMenssage.get(0);
		filaMenssage.removeElementAt(0);
		return message;
	}

	/**
	 * Sends given message to the client's socket.
	 */
	private void sendMessageToClient(String aMessage) {
		ourPrint.println(aMessage);
		ourPrint.flush();
	}

	/**
	 * Until interrupted, reads messages from the message queue and sends them
	 * to the client's socket.
	 */
	public void run() {
		try {
			while (!isInterrupted()) {
				String message = getNextMessageFromQueue();
				sendMessageToClient(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Communication is broken. Interrupt both listener and sender threads
		infoClient.clientOuvinte.interrupt();
		mServerDispatcher.deleteClient(infoClient);
	}

}
