import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Ignore;

@Ignore
public class TrocaEspacosEmBranco {

	public static void main(String args[]) {
		String padrao = "\\s";
		Pattern regPat = Pattern.compile(padrao);
		String frase = "Esta frase cont�m alguns espa�os";
		Matcher matcher = regPat.matcher(frase);
		String res = matcher.replaceAll("_");
		System.out.println(res);
	}
}
