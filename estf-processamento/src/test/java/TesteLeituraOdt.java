import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;

@Ignore
public class TesteLeituraOdt {
	public static void main(String[] args) throws IOException {
		String str = "áàãéíóü!!!";
		String anotacaoObservacao = "Texto de revisão único para seção do óculos.";

		StringBuilder texto = new StringBuilder();

		File file = new File("D:\\testeRtf1.odt");
		FileReader reader = new FileReader(file);

		// \{\\\*\\bkmkstart OBS\}\s*\{\\\*\\bkmkend OBS\}(.*)\}
		Pattern p = Pattern.compile("\bselecionarDestinatario\b");

		// Carrega o arquivo para a memoria
		while (true) {
			int charAtual = reader.read();
			if (charAtual == -1)
				break;
			texto.append((char) charAtual);
		}
		// Procura por um bookmark no arquivo
		Matcher m = p.matcher(texto.toString());
		if (m.find()) {
			texto.replace(m.start(), m.end(), String.format("{\\*\\bkmkstart OBS}{\\*\\bkmkend OBS}%s", escapeRTFString(anotacaoObservacao)));
		} else {
			inserirBookmark(texto, anotacaoObservacao);
		}

		try {
			File f = new File("D:\\resultRTF1.rtf");

			if (f.exists()) {
				f.delete();
				f.createNewFile();
			}

			FileOutputStream arquivoSaida = new FileOutputStream(f, false);
			try {
				arquivoSaida.write(Charset.forName("windows-1252").encode(texto.toString()).array());
				arquivoSaida.flush();
				arquivoSaida.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void inserirBookmark(StringBuilder texto, String obs) {
		StringBuilder tmp = new StringBuilder();
		boolean finalRTF = false;
		int posFinalRTF = -1;
		int charAtual = 0;

		for (int pos = 0; pos < texto.length(); pos++) {
			charAtual = texto.charAt(pos);

			// caso ache o final do arquivo
			if (charAtual == -1)
				break;

			/*
			 * Maquina de estados responsavel por identificar o final do arquivo RTF.
			 */
			if (finalRTF) {
				// se a busca achou um caractere } porém possui caracteres whiteSpace '\n', '\r' e etc
				// logo em seguida, continua a busca até o final do arquivo
				if (Character.isWhitespace(charAtual)) {
					continue;
				} else if (charAtual == '}') {
					posFinalRTF = pos;
				}
				// se a busca encontrou um caractere } mas em seguida possui outros caracteres
				// ele retornará false e continuará buscando um novo '}'
				else {
					finalRTF = false;
					posFinalRTF = -1;
				}
			}
			// caso a busca ache um caractere '}'
			else {
				if (charAtual == '}') {
					finalRTF = true;
					posFinalRTF = pos;
				}
			}
		}

		if (posFinalRTF == -1)
			throw new RuntimeException("Arquivo RTF mal formatado.");

		tmp.append(texto.substring(0, posFinalRTF));
		tmp.append(String.format("\\par{\\*\\bkmkstart OBS}{\\*\\bkmkend OBS}%s}", escapeRTFString(obs)));
		tmp.append(texto.substring(posFinalRTF));

		texto.delete(0, texto.length());
		texto.append(tmp.toString());
	}

	public static String escapeRTFString(String s) {
		StringBuilder ret = null;
		Charset charset = Charset.forName("windows-1252");
		CharsetEncoder encoder = charset.newEncoder();
		CharsetDecoder decoder = charset.newDecoder();

		try {
			ByteBuffer bbuff = encoder.encode(CharBuffer.wrap(s));
			CharBuffer cbuff = decoder.decode(bbuff);
			String sCp1252 = cbuff.toString();

			ret = new StringBuilder();

			for (int i = 0; i < sCp1252.length(); i++) {
				char c = sCp1252.charAt(i);
				if (c < 0x20)
					continue;
				if (c > 0x7e)
					ret.append(String.format("\\'%02x", (int) c));
				else
					ret.append(c);
			}
		} catch (CharacterCodingException e) {
			throw new RuntimeException(e);
		}

		return ret.toString();
	}

	public static String parseRTFString(String s) {
		// Localiza caracteres especiais no formato RTF.
		Pattern p = Pattern.compile("\\\\\'(([0-9]|[a-e]|[A-E]){2})");
		Matcher m = p.matcher(s);
		while (m.find()) {
			String charCode = m.group(1);
			// Recupera codigo exta do caractere e limpa qualquer lixo de conversao
			int c = Integer.parseInt(charCode, 16) & 0xff;
			// Converte o codigo hexa em um caracter no charset windows-1252
			Charset charset = Charset.forName("windows-1252");
			char ch = ' ';
			CharsetDecoder decoder = charset.newDecoder();
			try {
				ch = decoder.decode((ByteBuffer) ByteBuffer.allocate(1).put((byte) c).flip()).get();
			} catch (CharacterCodingException e) {
				e.printStackTrace();
			}

			// Substitui a expressao pelo caractere convertido
			s = s.replace(m.group(0), new String(new char[] { ch }));
			// Reinicia a busca
			m = p.matcher(s);
		}

		return s;
	}
}
