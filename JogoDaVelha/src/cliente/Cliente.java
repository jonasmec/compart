package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
	
	public  static final String MOVER = "MOVER ";
	public static final String SAIR = "SAIR ";


	private static final int PORTA = 5000;
	private Socket socket;
	private BufferedReader entrada;
	private PrintWriter saida;

	public Cliente(String endereco) throws UnknownHostException, IOException {

		socket = new Socket(endereco, PORTA);
		entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		saida = new PrintWriter(socket.getOutputStream(), true);
	}

	public Socket getSocket() {
		return socket;
	}

	public BufferedReader getEntrada() {
		return entrada;
	}

	public PrintWriter getSaida() {
		return saida;
	}

	public void mover(int j) {
		getSaida().println(MOVER + j);

	}

	public void sair() {
		getSaida().println(SAIR);
		
	}
}
