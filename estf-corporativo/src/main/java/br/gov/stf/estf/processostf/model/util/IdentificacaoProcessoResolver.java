package br.gov.stf.estf.processostf.model.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Oferece servi�os de valida��o, parsing e recupera��o de informa��es dada
 * a idenfifica��o de um processo.
 * 
 * <p>A idenfifica��o de um processo � dada no formato "<SIGLA> <NUMERO>".
 * Esse resolver valida esse formato, identificando quais informa��es
 * est�o presentes no idenficador informado. Por exemplo: na indentifica��o
 * "87423" n�o h� sigla, apenas o n�mero. O resolver tamb�m desconsidera
 * informa��es estranhas ao formato, como por exemplo: "RE-87423///".
 * 
 * <p>Ser� inv�lida a identifica��o que s� forne�a a sigla.
 * 
 * @author Rodrigo Barreiros
 * @since 05.05.2010
 */
@Component
public class IdentificacaoProcessoResolver {
	
	private enum TipoIdentificacao {SIGLA, NUMERO}
	
	/**
	 * Verifica se a identifica��o � v�lida. Ser� inv�lida a identifica��o que s� forne�a a sigla.
	 * 
	 * @param identificacao a identifica��o do processo
	 * @return true, se v�lida, false, caso contr�rio
	 */
	public boolean isValid(String identificacao) {
		if (StringUtils.isNotBlank(identificacao)) {
			for (int i = 0; i < identificacao.length(); i++) {
				if (Character.isDigit(identificacao.charAt(i))) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Retorna a sigla do processo presente na identifica��o.
	 * 
	 * @param identificacao a identifica��o do processo
	 * @return a sigla do processo
	 */
	public String getSigla(String identificacao) {
		return (String) parseIdentificacao(identificacao, TipoIdentificacao.SIGLA);
	}
	
	/**
	 * Retorna o n�mero do processo presente na identifica��o.
	 * 
	 * @param identificacao a identifica��o do processo
	 * @return o n�mero do processo
	 */
	public Long getNumero(String identificacao) {
		return (Long) parseIdentificacao(identificacao, TipoIdentificacao.NUMERO);
	}

	/**
	 * Faz o parse da identifica��o do processo, recuperando a informa��o desejada: sigla ou tipo.
	 * 
	 * @param identificacao a identifica��o de entrada 
	 * @param tipoIdentificacao o tipo de informa��o que se deseja
	 * @return sigla ou n�mero do processo
	 */
	private Object parseIdentificacao(String identificacao, TipoIdentificacao tipoIdentificacao) {
		if (StringUtils.isNotBlank(identificacao)) {

			Pattern p = Pattern.compile("([a-zA-Z]*)[\\s]*([0-9]*)[-]?([a-zA-Z]*)");
			Matcher matcher = p.matcher(identificacao.replaceAll("\\.", ""));
	
			if (matcher.find()) {
				if (tipoIdentificacao.equals(TipoIdentificacao.NUMERO) && StringUtils.isNotBlank(matcher.group(2))) {
					return new Long(matcher.group(2));
				}
				if (tipoIdentificacao.equals(TipoIdentificacao.SIGLA)) {
					return convertClasseProcessual(matcher.group(1));
				}
			}
			
		}
		return null;
	}

	private String convertClasseProcessual(String classe) {
		// TODO: Implementar c�digo da vers�o anterior.
		return classe;
	}
}
