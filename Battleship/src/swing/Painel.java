package swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import conexao.ConectarServer;

@SuppressWarnings("serial")
public class Painel extends JPanel {

	public Painel() {
		setLayout(new BorderLayout());
		add(new Tabuleiro(), BorderLayout.CENTER);
	}

	private class Tabuleiro extends JPanel {

		private Tabuleiro() {
			super.setPreferredSize(new Dimension(800, 250));
			setBorder(new TitledBorder("BATALHA"));

		}
	}
}
