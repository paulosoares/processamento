import java.io.FileNotFoundException;

import org.junit.Ignore;

@Ignore
public class TAbrirArquivo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		TesteAbrirArquivo teste = new TesteAbrirArquivo();

		try {
			teste.teste();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
