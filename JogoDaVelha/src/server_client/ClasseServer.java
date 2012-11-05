package server_client;

import java.net.ServerSocket;



public class ClasseServer {

	private static final String TITULO = "JoGo da VeLha";
	private static final int PORTA = 5000;

	public static void main(String[] args) throws Exception {
		int porta = (args.length == 0 ? PORTA : Integer.parseInt(args[0]));
		 
		ServerSocket conexao = new ServerSocket(porta);
		
		System.out.println(TITULO + " server ligado");
		
		try {
			while (true) {
				Partida partida = new Partida();
				partida.setJogadores(partida.new Jogador(conexao.accept(), 'X'), partida.new Jogador(conexao.accept(), 'O'));
			}
		} finally {
			if(conexao != null){
				conexao.close();
			}
		}
	}
}


