package inetJava;

import java.net.Socket;

public class ClientInfo {
	public Socket clientSocket = null;
	public ClientListener clientOuvinte = null;
	public ClientSender clientRemetente = null;
}
