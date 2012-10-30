package swing;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TelaServer extends JDialog {

	private JTextField txtIp;
	private JTextField txtPorta;
	private JTextField txtApelido;
	private JLabel lIp;
	private JLabel lPorta;
	private JLabel lApelido;

	public TelaServer() {
		configServer();
		setSize(300, 150);
		setModal(true);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	private void configServer() {

		try {
			lIp = new JLabel("Endereço IP:");
			add(lIp);
			txtIp = new JTextField();
			txtIp.setText(InetAddress.getLocalHost().getHostAddress());
			add(txtIp);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		lPorta = new JLabel("Porta do servidor:");
		add(lPorta);
		txtPorta = new JTextField();
		txtPorta.setText("5000");
		add(txtPorta);

		lApelido = new JLabel("Seu usuário:");
		add(lApelido);
		txtApelido = new JTextField();
		add(txtApelido);

		JButton btn;
		btn = new JButton(new btnOk());
		add(btn);
		btn = new JButton(new btnCancelar());
		add(btn);
		setTitle("Configrações do servidor");
		setLayout(new GridLayout(4, 2, 10, 10));
	}

	private class btnOk extends AbstractAction {
		public btnOk() {
			super("OK");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Validar();
		}
	}

	private class btnCancelar extends AbstractAction {
		public btnCancelar() {
			super("Cancelar");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}

	public String getIp() {
		return txtIp.getText();
	}

	public String getPorta() {
		return txtPorta.getText();
	}

	public String getApelido() {
		return txtApelido.getText();
	}

	private void Validar() {
		String msg = "";

		if (txtPorta.getText().equals("")) {
			msg += "Favor informe a Porta\n";
		}
		if (txtIp.getText().equals("")) {
			msg += "Favor informe o IP\n";
		}
		if (!msg.equals("")) {
			msg = "Foram encontrados os seguintes erros:\n\n" + msg;
			JOptionPane.showMessageDialog(this, msg, "Informações do sitema",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		dispose();
	}
}
