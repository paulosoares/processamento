package br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.andamento;

import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.MaskFormatter;

import org.apache.commons.lang.StringUtils;

public class StringUtil extends StringUtils {

	private static final Integer TAMANHO_NUMERO_UNICO = 20;
	public static final String TOKEN = ",";
	

	/**
	 * <pre>
	 * StringUtils.toString(null)      = ""
	 * StringUtils.toString("")        = ""
	 * StringUtils.toString(" ")       = " "
	 * StringUtils.toString(10L)     = "10"
	 * StringUtils.toString(1065.68) = "1065.68"
	 * </pre>
	 * 
	 * @param qualquer
	 *            objecto
	 * @return string
	 */
	public static String toString(Object o) {
		if (o == null) {
			return StringUtils.EMPTY;
		}
		return o.toString();
	}

	public static String extrairNumeroUnicoDoProtocolo(String protocolo) {
		if (protocolo.length() < TAMANHO_NUMERO_UNICO) {
			return null;
		}
		return protocolo.substring(protocolo.length() - TAMANHO_NUMERO_UNICO, protocolo.length());
	}

	public static <T> T coalesce(T... items) {
		for (T i : items)
			if (i != null)
				return i;
		return null;
	}

	public static String formatNumeroUnico(String value) {
		try {
			if (!isNumeric(value) || value.length() < 20)
				return value;
			MaskFormatter mask;
			mask = new MaskFormatter("#######-##.####.#.##.####");
			mask.setValueContainsLiteralCharacters(false);
			return mask.valueToString(value);
		} catch (ParseException e) {
			return value;
		}
	}

	public static String formatNumAviso(String value) {
		try {
			if (!isNumeric(value))
				return value;
			MaskFormatter mask;
			mask = new MaskFormatter("#####");
			mask.setValueContainsLiteralCharacters(false);
			return mask.valueToString(value);
		} catch (ParseException e) {
			return value;
		}
	}

	public static String formatCPFCNPJ(String value) {
		try {
			if (!isNumeric(value))
				return value;
			MaskFormatter mask;
			if (value.length() <= 11) {
				mask = new MaskFormatter("###.###.###-##");
			} else {
				mask = new MaskFormatter("##.###.###/####-##");
			}
			mask.setValueContainsLiteralCharacters(false);
			return mask.valueToString(value);
		} catch (ParseException e) {
			return value;
		}
	}

	public static String removeTypeException(String message) {
		if (message.contains("]:")) {
			return message.substring(message.indexOf("]:") + 2, message.length());
		}
		return message;
	}

	public static String toStringList(List<?> list) {
		StringBuilder str = new StringBuilder();
		Integer n = 1;
		for (Object object : list) {
			str.append(String.format("[%d] %s; ", n++, object));
		}
		return str.toString();
	}
	
	public static String recuperaPrimeiroGrupoEmPesquisaRegex(String texto, String expressaoRegex) {
		if (StringUtil.isEmpty(texto))
			return "Sem SOAP";
		Pattern r = Pattern.compile(expressaoRegex, Pattern.MULTILINE + Pattern.DOTALL);
		Matcher m = r.matcher(texto);
		
		if(m.find()){
			return m.group(0);
		}
		return texto;
	}

	public static String listaEmString(List<String> lista) {
		StringBuilder str = new StringBuilder();
		for (String elemento : lista) {
			str.append(elemento).append(", ");
		}
		if (str.length()>2)
			return str.toString().substring( 0, str.length() - 2);
		return str.toString();
	}

}
