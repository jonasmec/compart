package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Partida {

	private Jogador jogadorAtivo;
	// celulas com os tipo de jogador X/O
	private Jogador[] celulas = { null, null, null, null, null, null, null,	null, null };
	// verifica as possibilidade de vitória
	public boolean isVitoria() {
		return (celulas[0] != null && celulas[0] == celulas[1] && celulas[0] == celulas[2])
				|| (celulas[3] != null && celulas[3] == celulas[4] && celulas[3] == celulas[5])
				|| (celulas[6] != null && celulas[6] == celulas[7] && celulas[6] == celulas[8])
				|| (celulas[0] != null && celulas[0] == celulas[3] && celulas[0] == celulas[6])
				|| (celulas[1] != null && celulas[1] == celulas[4] && celulas[1] == celulas[7])
				|| (celulas[2] != null && celulas[2] == celulas[5] && celulas[2] == celulas[8])
				|| (celulas[0] != null && celulas[0] == celulas[4] && celulas[0] == celulas[8])
				|| (celulas[2] != null && celulas[2] == celulas[4] && celulas[2] == celulas[6]);
	}
	// jogadores atribuidos a celula
	public boolean isPreenchido() {
		for (int i = 0; i < celulas.length; i++) {
			if (celulas[i] == null) {
				return false;
			}
		}
		return true;
	}
	// verefica o movimento do jogador e atribui o movimento a celula, sinchronized para
	// impedir que dois jogadores se movimente simultaneamente
	public synchronized boolean isMovimentoPermitido(int indice, Jogador jogador) {
		if (jogador == jogadorAtivo && celulas[indice] == null) {
			celulas[indice] = jogadorAtivo;
			jogadorAtivo = jogadorAtivo.oponente;
			jogadorAtivo.movimentoOponente(indice);
			return true;
		}
		return false;
	}

	class Jogador extends Thread {
		// comando enviados por printwriter
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
				entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				saida = new PrintWriter(socket.getOutputStream(), true);
				saida.println(COMECAR + tipo);
				saida.println(INFO + "AGUARDE O OPONENTE SE CONECTAR");
				
			} catch (IOException e) {
				System.out.println("JOGADOR SAIU: " + e);
			}
		}

		public void setOponente(Jogador oponente) {
			this.oponente = oponente;
		}

		public void movimentoOponente(int indice) {
			saida.println(MOVIMENTO_OPONENTE + indice);
			saida.println(isVitoria() ? DERROTA : isPreenchido() ? EMPATE : "");
		}

		public void run() {
			try {
				saida.println(INFO + "TODOS OS JOGADORES CONECTADOS, AGUARDE O MOVIMENTO");

				if (tipo == 'X') {
					saida.println(INFO + "SEU MOVIMENTO?");
				}
				while (true) {
					String comandos = entrada.readLine();
					if (comandos.startsWith(MOVER)) {
						int indice = Integer.parseInt(comandos.substring(6));
						if (isMovimentoPermitido(indice, this)) {
							saida.println(MOVIMENTO_VALIDO);
							saida.println(isVitoria() ? VITORIA : isPreenchido() ? EMPATE : "");
						} else {
							saida.println(INFO + "MOVIMENTO INVÁLIDO?");
						}
					} else if (comandos.startsWith(SAIR)) {
						return;
					}
				}
			} catch (IOException e) {
				System.out.println("JOGADOR SAIU: " + e);
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
	// atribui os jogadores na partida, e declara o X como principal/ativo
	public void setJogadores(Jogador jogadorX, Jogador jogadorO) {
		jogadorX.setOponente(jogadorO);
		jogadorO.setOponente(jogadorX);
		jogadorAtivo = jogadorX;
		jogadorX.start();
		jogadorO.start();

	}
}
