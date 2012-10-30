package inetJava;

import java.net.Socket;
import java.util.Vector;

public class ServerDispatcher extends Thread {
	
	private Vector<String> filaMenssage = new Vector<String>();
	private Vector<ClientInfo> clients = new Vector<ClientInfo>();

	/**
	 * Adds given client to the server's client list.
	 */
	public synchronized void addClient(ClientInfo aClientInfo) {
		clients.add(aClientInfo);
	}

	/**
	 * Deletes given client from the server's client list if the client is in
	 * the list.
	 */
	public synchronized void deleteClient(ClientInfo aClientInfo) {
		int clientIndex = clients.indexOf(aClientInfo);
		if (clientIndex != -1)
			clients.removeElementAt(clientIndex);
	}

	/**
	 * Adds given message to the dispatcher's message queue and notifies this
	 * thread to wake up the message queue reader (getNextMessageFromQueue
	 * method). dispatchMessage method is called by other threads
	 * (ClientListener) when a message is arrived.
	 */
	public synchronized void dispatchMessage(ClientInfo aClientInfo, String aMessage) {
		Socket socket = aClientInfo.clientSocket;
		String senderIP = socket.getInetAddress().getHostAddress();
		String senderPort = "" + socket.getPort();
		aMessage = senderIP + ":" + senderPort + " : " + aMessage;
		filaMenssage.add(aMessage);
		notify();
	}

	/**
	 * @return and deletes the next message from the message queue. If there is
	 *         no messages in the queue, falls in sleep until notified by
	 *         dispatchMessage method.
	 */
	private synchronized String getNextMessageFromQueue() throws InterruptedException {
		while (filaMenssage.size() == 0)
			wait();
		String message = (String) filaMenssage.get(0);
		filaMenssage.removeElementAt(0);
		return message;
	}

	/**
	 * Sends given message to all clients in the client list. Actually the
	 * message is added to the client sender thread's message queue and this
	 * client sender thread is notified.
	 */
	private synchronized void sendMessageToAllClients(String aMessage) {
		for (int i = 0; i < clients.size(); i++) {
			ClientInfo clientInfo = (ClientInfo) clients.get(i);
			clientInfo.clientRemetente.sendMessage(aMessage);
		}
	}

	/**
	 * Infinitely reads messages from the queue and dispatch them to all clients
	 * connected to the server.
	 */
	public void run() {
		try {
			while (true) {
				String message = getNextMessageFromQueue();
				sendMessageToAllClients(message);
			}
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

}