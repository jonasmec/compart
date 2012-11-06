package cliente;

import java.io.IOException;
import java.net.UnknownHostException;

import swing.Tela;

/**
 * @author jonas, guilherme
 *
 */
public class AppJogar {

	/**
	 * @param args
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {

		String endereco = (args.length == 0) ? "localhost" : args[0];

		while (true) {
			// envia o endereÃ§o para a classe cliente abrir conexao com servidor
			Cliente cliente = new Cliente(endereco);
			// cria a interface e insere um cliente
			Tela tela = new Tela(cliente);
			// verifica se o jogador quer continuar jogando
			if (!tela.isJogarNovamente()) {
				break;
			}
		}
	}
}
