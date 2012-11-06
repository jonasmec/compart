package swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cliente.Cliente;


public class Tela extends JFrame {

	private static final long serialVersionUID = 1L;
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
	private JLabel mensagem = new JLabel();
	private ImageIcon icone;
	private ImageIcon iconeOponente;
	// prepara o tabuleiro com a classe Celula (JPanel)
	private Celula[] celulas = new Celula[9];
	private Celula celulaAtiva;
	private Cliente cliente;
	
	public Tela(final Cliente cliente){
		
		this.cliente = cliente;
		
		mensagem.setForeground(Color.RED);
		getContentPane().add(mensagem, "South");
		// JPanel de fundo para criar as linhas
		JPanel base = new JPanel();
		base.setBackground(Color.black);
		base.setLayout(new GridLayout(3, 3, 4, 4));
		// declara/dispara os eventos de indice das celulas clicadas
		for (int i = 0; i < celulas.length; i++) {
			final int j = i;
			celulas[i] = new Celula();
			celulas[i].addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					celulaAtiva = celulas[j];
					cliente.getSaida().println(MOVER + j);
				}
			});
			base.add(celulas[i]);
		}
		getContentPane().add(base, "Center");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(460, 460);
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		
		jogar();
	}

	public void jogar() {
		
		String resposta;
		try {
			resposta = cliente.getEntrada().readLine();
			
			if (resposta.startsWith(COMECAR)) {
				char tipo = resposta.charAt(8);
				icone = new ImageIcon(tipo == 'X' ? ICONE_X : ICONE_O);
				iconeOponente = new ImageIcon(tipo == 'X' ? ICONE_O : ICONE_X);
				setTitle(TITULO + " : " + tipo);
			}
			
			while (true) {
				
				resposta = cliente.getEntrada().readLine();
				
				if (resposta.startsWith(MOVIMENTO_VALIDO)) {
					mensagem.setForeground(Color.RED);
					mensagem.setText("AGUARDE O MOVIMENTO DO OPONENTE...");
					celulaAtiva.setIcon(icone);
					celulaAtiva.repaint();
				} else if (resposta.startsWith(MOVIMENTO_OPONENTE)) {
					int indice = Integer.parseInt(resposta.substring(19));
					celulas[indice].setIcon(iconeOponente);
					celulas[indice].repaint();
					mensagem.setForeground(Color.GREEN);
					mensagem.setText("OPONENTE MOVEU, SUA VEZ...");
				} else if (resposta.startsWith(VITORIA)) {
					mensagem.setText("<< VOCÊ VENCEU >>");
					break;
				} else if (resposta.startsWith(DERROTA)) {
					mensagem.setText("<< VOCÊ PERDEU >>");
					break;
				} else if (resposta.startsWith(EMPATE)) {
					mensagem.setText("<< EMPATE >>");
					break;
				} else if (resposta.startsWith(INFO)) {
					mensagem.setText(resposta.substring(5));
				}
			}
			cliente.getSaida().println(SAIR);
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} finally {
			try {
				cliente.getSocket().close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}

	/**
	 * jogarNovamente
	 * @return boolean 
	 */
	public boolean isJogarNovamente() {
		int resposta = JOptionPane.showConfirmDialog(this, "JOGAR NOVAMENTE?", TITULO, JOptionPane.YES_NO_OPTION);
		// libera o frame da memoria
		dispose();
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
}
