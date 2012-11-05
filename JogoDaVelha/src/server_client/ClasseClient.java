package server_client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



/** 
 * @author jonas, guilherme
 *
 */
public class ClasseClient {

	// titulo da tela
	private static final String TITULO = "JoGo da VeLha";
	// constantes dos icones
	private static final String ICONE_X = "X.png";
	private static final String ICONE_O = "O.png";
	// porta padrão como 5000
	private static final int PORTA = 5000;
	// constantes de comando para envio via printWriter
	private static final String COMECAR = "COMECAR ";
	private static final String INFO = "INFO ";
	private static final String MOVIMENTO_OPONENTE = "MOVIMENTO_OPONENTE ";
	private static final String VITORIA = "VITORIA ";
	private static final String EMPATE = "EMPATE ";
	private static final String SAIR = "SAIR ";
	private static final String DERROTA = "DERROTA ";
	private static final String MOVIMENTO_VALIDO = "MOVIMENTO_VALIDO ";
	private static final String MOVER = "MOVER ";
	// cria a tela
	private JFrame frame = new JFrame(TITULO);
	private JLabel mensagemLabel = new JLabel();
	private ImageIcon icone;
	private ImageIcon iconeOponente;
	// prepara o tabuleiro com a classe Celula (JPanel)
	private Celula[] tabuleiro = new Celula[9];
	private Celula tabuleiroAtual;
	// socket para conexão
	private Socket socket;
	// para a entrada de dados
	private BufferedReader entrada;
	// para saida de dados
	private PrintWriter saida;

	/**
	 * ClasseClient
	 * @param endereco 
	 */
	public ClasseClient(String endereco) {

		try {
			socket = new Socket(endereco, PORTA);
			entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			saida = new PrintWriter(socket.getOutputStream(), true);
			
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		mensagemLabel.setForeground(Color.RED);
		frame.getContentPane().add(mensagemLabel, BorderLayout.SOUTH);

		JPanel base = new JPanel();
		base.setBackground(Color.black);
		base.setLayout(new GridLayout(3, 3, 4, 4));

		for (int i = 0; i < tabuleiro.length; i++) {
			final int j = i;
			tabuleiro[i] = new Celula();
			tabuleiro[i].addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					tabuleiroAtual = tabuleiro[j];
					saida.println(MOVER + j);
				}
			});
			base.add(tabuleiro[i]);
		}
		frame.getContentPane().add(base, BorderLayout.CENTER);
	}

	public void jogar() {
		String resposta;
		try {
			resposta = entrada.readLine();
			
			if (resposta.startsWith(COMECAR)) {
				char tipo = resposta.charAt(8);
				icone = new ImageIcon(tipo == 'X' ? ICONE_X : ICONE_O);
				iconeOponente = new ImageIcon(tipo == 'X' ? ICONE_O : ICONE_X);
				frame.setTitle(TITULO + " : " + tipo);
			}
			
			while (true) {
				resposta = entrada.readLine();
				
				if (resposta.startsWith(MOVIMENTO_VALIDO)) {
					mensagemLabel.setForeground(Color.RED);
					mensagemLabel.setText("Movimento do opanente, aguarde...");
					tabuleiroAtual.setIcon(icone);
					tabuleiroAtual.repaint();
				} else if (resposta.startsWith(MOVIMENTO_OPONENTE)) {
					int indice = Integer.parseInt(resposta.substring(19));
					tabuleiro[indice].setIcon(iconeOponente);
					tabuleiro[indice].repaint();
					mensagemLabel.setForeground(Color.GREEN);
					mensagemLabel.setText("Oponente moveu, sua vez...");
				} else if (resposta.startsWith(VITORIA)) {
					mensagemLabel.setText("<< Você venceu >>");
					break;
				} else if (resposta.startsWith(DERROTA)) {
					mensagemLabel.setText("<< Você perdeu >>");
					break;
				} else if (resposta.startsWith(EMPATE)) {
					mensagemLabel.setText("<< Empate >>");
					break;
				} else if (resposta.startsWith(INFO)) {
					mensagemLabel.setText(resposta.substring(5));
				}
			}
			saida.println(SAIR);
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}

	/**
	 * jogarNovamente
	 * @return boolean 
	 */
	private boolean jogarNovamente() {
		int resposta = JOptionPane.showConfirmDialog(frame, "Jogar novamente?", TITULO, JOptionPane.YES_NO_OPTION);
		// libera o frame da memoria
		frame.dispose();
		// retorna true se a resposta for sim (zero)
		return resposta == JOptionPane.YES_OPTION;
	}

	class Celula extends JPanel {
		private static final long serialVersionUID = 1L;
		
		JLabel label = new JLabel();

		public Celula() {
			setBackground(Color.white);
			add(label);
		}

		public void setIcon(Icon icon) {
			label.setIcon(icon);
		}
	}

	/**
	 * @param String[] args
	 */
	public static void main(String[] args) {
		// verefica se recebeu o endereço por linha de comando
		String endereco = (args.length == 0) ? "localhost" : args[0];

		while (true) {
			// envia o endereço para a classe client
			ClasseClient cliente = new ClasseClient(endereco);
			// cria a interface
			cliente.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			cliente.frame.setSize(460, 460);
			cliente.frame.setVisible(true);
			cliente.frame.setResizable(false);
			cliente.frame.setLocationRelativeTo(null);
			cliente.jogar();
			// verifica se o jigador que continuar jogando
			if (!cliente.jogarNovamente()) {
				break;
			}
		}
	}
}
