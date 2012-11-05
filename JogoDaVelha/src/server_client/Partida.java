package server_client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
  
public class Partida {

	private Jogador[] tabuleiro = { null, null, null, null, null, null, null,
			null, null };

	public boolean regraDoJogo() {
		return (tabuleiro[0] != null && tabuleiro[0] == tabuleiro[1] && tabuleiro[0] == tabuleiro[2])
				|| (tabuleiro[3] != null && tabuleiro[3] == tabuleiro[4] && tabuleiro[3] == tabuleiro[5])
				|| (tabuleiro[6] != null && tabuleiro[6] == tabuleiro[7] && tabuleiro[6] == tabuleiro[8])
				|| (tabuleiro[0] != null && tabuleiro[0] == tabuleiro[3] && tabuleiro[0] == tabuleiro[6])
				|| (tabuleiro[1] != null && tabuleiro[1] == tabuleiro[4] && tabuleiro[1] == tabuleiro[7])
				|| (tabuleiro[2] != null && tabuleiro[2] == tabuleiro[5] && tabuleiro[2] == tabuleiro[8])
				|| (tabuleiro[0] != null && tabuleiro[0] == tabuleiro[4] && tabuleiro[0] == tabuleiro[8])
				|| (tabuleiro[2] != null && tabuleiro[2] == tabuleiro[4] && tabuleiro[2] == tabuleiro[6]);
	}

	private Jogador jogadorAtual;

	public boolean foiPreenchido() {
		for (int i = 0; i < tabuleiro.length; i++) {
			if (tabuleiro[i] == null) {
				return false;
			}
		}
		return true;
	}

	public synchronized boolean movimentoPermitido(int indice, Jogador jogador) {
		if (jogador == jogadorAtual && tabuleiro[indice] == null) {
			tabuleiro[indice] = jogadorAtual;
			jogadorAtual = jogadorAtual.oponente;
			jogadorAtual.movimentoOponente(indice);
			return true;
		}
		return false;
	}

	class Jogador extends Thread {

		private static final String COMECAR = "COMECAR ";
		private static final String INFO = "INFO ";
		private static final String MOVIMENTO_OPONENTE = "MOVIMENTO_OPONENTE ";
		private static final String VITORIA = "VITORIA ";
		private static final String EMPATE = "EMPATE ";
		private static final String SAIR = "SAIR ";
		private static final String DERROTA = "DERROTA ";
		private static final String MOVIMENTO_VALIDO = "MOVIMENTO_VALIDO ";
		private static final String MOVER = "MOVER ";

		private char tipo;
		private Jogador oponente;
		private Socket socket;
		private BufferedReader entrada;
		private PrintWriter saida; 

		public Jogador(Socket socket, char tipo) {
			this.socket = socket;
			this.tipo = tipo;
			try {
				entrada = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				saida = new PrintWriter(socket.getOutputStream(), true);
				saida.println(COMECAR + tipo);
				saida.println(INFO + "Aguarde o oponente conectar");
			} catch (IOException e) {
				System.out.println("Jogador perdeu: " + e);
			}
		}

		public void setOponente(Jogador oponente) {
			this.oponente = oponente;
		}

		public void movimentoOponente(int indice) {
			saida.println(MOVIMENTO_OPONENTE + indice);
			saida.println(regraDoJogo() ? DERROTA : foiPreenchido() ? EMPATE
					: "");
		}

		public void run() {
			try {
				saida.println(INFO + "Todos os jogadores conectados");

				if (tipo == 'X') {
					saida.println(INFO + "Seu movimento?");
				}
				while (true) {
					String comandos = entrada.readLine();
					if (comandos.startsWith(MOVER)) {
						int indice = Integer.parseInt(comandos.substring(6));
						if (movimentoPermitido(indice, this)) {
							saida.println(MOVIMENTO_VALIDO);
							saida.println(regraDoJogo() ? VITORIA
									: foiPreenchido() ? EMPATE : "");
						} else {
							saida.println(INFO + "Movimento inválido?");
						}
					} else if (comandos.startsWith(SAIR)) {
						return;
					}
				}
			} catch (IOException e) {
				System.out.println("Jogador perdeu: " + e);
			} finally {
				try {
					if(socket != null){
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setJogadores(Jogador jogadorX, Jogador jogadorO) {
		jogadorX.setOponente(jogadorO);
		jogadorO.setOponente(jogadorX);
		this.jogadorAtual = jogadorX;
		jogadorX.start();
		jogadorO.start();

	}
}
