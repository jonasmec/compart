package swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import conexao.ConectarServer;

@SuppressWarnings("serial")
public class Tela extends JFrame {

	private JTextField areaEnviar;
	private JTextArea area;
	private JButton btn;
	
	public Tela() {
		super("BATALHA NAVAL");

		setJMenuBar(barraMenu());

		add(new Painel(), BorderLayout.CENTER);
		add(new Mural(), BorderLayout.SOUTH);

		getRootPane().setDefaultButton(btn);
		setResizable(false);
		setSize(800, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private JMenuBar barraMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu;
		JMenuItem item;

		menu = new JMenu("Arquivo");

		item = new JMenuItem("Configurar servidor");
		item.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TelaServer telaServer = new TelaServer();

				String serverHost = telaServer.getIp();
				String serverPort = telaServer.getPorta();
				String serverApelido = telaServer.getApelido();
				int port = (serverPort.equals("") ? 0 : Integer
						.valueOf(serverPort));
				new ConectarServer(serverHost, port);
			}
		});
		menu.add(item);

		item = new JMenuItem("Sair");
		item.addActionListener(new AbstractAction() {
			 

			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null, "Deseja sair do jogo?") == 0) {
					dispose();
//					if (JOptionPane.showConfirmDialog(null, "Tem certeza??") == 0) {
//						System.exit(0);
//					}
				}
			}
		});
		menu.add(item);
		menuBar.add(menu);

		return menuBar;

	}

	private class Mural extends JPanel {
		
		public Mural() {
			super.setPreferredSize(new Dimension(800, 120));
			setBorder(new TitledBorder("MURAL"));

			area = new JTextArea();
			area.setPreferredSize(new Dimension(764, 60));
			add(new JScrollPane(area), BorderLayout.NORTH);

			areaEnviar = new JTextField();
			areaEnviar.setPreferredSize(new Dimension(650, 20));
			add(new JScrollPane(areaEnviar), BorderLayout.SOUTH);
			btn = new JButton(new AbstractAction() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ConectarServer.enviar(areaEnviar.getText());
					areaEnviar.setText("");
					areaEnviar.requestFocus();
				}
			});

			btn.setText("Enviar");
			btn.setPreferredSize(new Dimension(110, 25));
			add(btn);
		}

		public void setTexto(String texto) {
			area.append(texto);
			
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Tela();
			}
		});
	}

}
