package br.gov.stf.estf.assinatura.visao.util;
/**
 * @author Leandro.Andrade
 * */
public class PeticaoParser {
	
	public static Long getNumeroPeticao(String valor){
		StringBuilder strInvertida = retiraBarraPeticao(valor).reverse();
		String numeroInvertido = strInvertida.toString();
		numeroInvertido = numeroInvertido.substring(4, numeroInvertido.length());
		StringBuilder numero = new StringBuilder(numeroInvertido);
		numero = numero.reverse();
		return Long.parseLong(numero.toString());
		
	}
	
	public static Short getAnoPeticao(String valor){
		StringBuilder strInvertida = retiraBarraPeticao(valor).reverse();
		String anoInvertido = strInvertida.toString().trim();
		anoInvertido = anoInvertido.substring(0, 4);
		StringBuilder ano = new StringBuilder(anoInvertido);
		ano = ano.reverse();
		return Short.valueOf(ano.toString());
		
	}
	
	private static StringBuilder retiraBarraPeticao(String valor){
		StringBuilder strOriginal = new StringBuilder(valor.toString().replace("/", "").trim());
		return strOriginal;
	}

}
