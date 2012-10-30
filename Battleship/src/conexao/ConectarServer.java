package conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class ConectarServer {

	private BufferedReader in = null;
	private static PrintWriter out = null;
	private Socket socket;
	private Scanner scanner;

	public ConectarServer(String serverHost, int serverPort) {

		try {
			socket = new Socket(serverHost, serverPort);
			out = new PrintWriter(socket.getOutputStream());
			scanner = new Scanner(socket.getInputStream());
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null,
					"Não encontrou o host " + e.getMessage(), "Info ...",
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erro " + e.getMessage(),
					"Info ...", JOptionPane.ERROR_MESSAGE);
		}
		new Thread(new Escuta()).start();
		 
	}

	public static void enviar(String texto) {
		out.println(texto);
		out.flush();
	}

	private class Escuta implements Runnable {

		@Override
		public void run() {
			String s = null;
			try {
				while ((s = scanner.nextLine()) != null) {
					// FIXME
					// textArea.append(s + "\n");
				}
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null,
						"No Server " + e.getMessage());
			}
		}
	}
}
