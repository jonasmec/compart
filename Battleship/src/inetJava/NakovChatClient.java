package inetJava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class NakovChatClient {
	public static final String SERVER_HOSTNAME = "192.168.0.104";
	public static final int SERVER_PORT = 2002;

	public static void main(String[] args) {
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			// Connect to Nakov Chat Server
			Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			System.out.printf("Connected to server %s %s ", SERVER_HOSTNAME, SERVER_PORT);
			// Create and start Sender thread
			Sender sender = new Sender(out);
			sender.setDaemon(true);
			sender.start();
			
			// Read messages from the server and print them
			String message;
			while ((message = in.readLine()) != null) {
				System.out.println(message);
			}

		} catch (IOException ioe) {
			System.err.printf("Can not establish connection to %s %s ",
					SERVER_HOSTNAME, SERVER_PORT);
			ioe.printStackTrace();
		}
	}
}

class Sender extends Thread {
	private PrintWriter outPrint;

	public Sender(PrintWriter aOut) {
		outPrint = aOut;
	}

	/**
	 * Until interrupted reads messages from the standard input (keyboard) and
	 * sends them to the chat server through the socket.
	 */
	public void run() {
		System.out.println("ok");
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			while (!isInterrupted()) {
				String message = in.readLine();
				outPrint.println(message);
				System.out.println(message);
				System.out.flush();
				outPrint.flush();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
