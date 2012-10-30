package conexao;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;

public class ServerApp {

	public static void main(String[] args) {
		new ServerApp().new Server();
	}

	public class Server {
		private Socket socket;
		private Set<PrintWriter> printWriter;

		public Server() {

			printWriter = new HashSet<PrintWriter>();

			try {
				int porta = 5000;
//						Integer.parseInt(JOptionPane.showInputDialog("Sua porta ex: 5000"));

				ServerSocket server = new ServerSocket(porta);

				JOptionPane.showMessageDialog(null, "Start server");
				while (true) {
					socket = server.accept();
					new Thread(new Escuta(socket)).start();
					PrintWriter p = new PrintWriter(socket.getOutputStream());
					printWriter.add(p);
				}

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Info ...",
						"Erro " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
			}
		}

		private void encaminhar(String texto) {
			if (texto.contains("lll")) {
				System.exit(0);
			}
			for (PrintWriter p : printWriter) {
				p.println(texto);
				p.flush();
			}
		}

		private class Escuta implements Runnable {

			Scanner scanner = null;

			public Escuta(Socket socket) {
				try {
					scanner = new Scanner(socket.getInputStream());
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Info ...",
							"User saiu " + e.getMessage(),
							JOptionPane.ERROR_MESSAGE);
				}
			}

			@Override
			public void run() {
				String s = null;
				try {
					while ((s = scanner.nextLine()) != null) {
						encaminhar(s);
					}
				} catch (NoSuchElementException e) {
					JOptionPane.showMessageDialog(
							null,
							"User down " + e.getMessage()
									+ socket.getInetAddress());
				}
			}
		}
	}
}

