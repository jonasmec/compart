package servidor;

import java.io.IOException;
import java.net.ServerSocket;

public class AppServidor {

	private static final String TITULO = "JoGo da VeLha";
	private static final int PORTA = 5000;

	public static void main(String[] args) {
		
		int porta = (args.length == 0 ? PORTA : Integer.parseInt(args[0]));

		ServerSocket conexao = null;
		
		try {
			conexao = new ServerSocket(porta);

			System.out.println(TITULO + " server ligado");

			while (true) {
				Partida partida = new Partida();
				partida.setJogadores(
						partida.new Jogador(conexao.accept(), 'X'),
						partida.new Jogador(conexao.accept(), 'O'));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conexao != null)
					conexao.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
