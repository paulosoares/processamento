package br.gov.stf.estf.processostf.model.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Oferece serviços de validação, parsing e recuperação de informações dada
 * a idenfificação de um processo.
 * 
 * <p>A idenfificação de um processo é dada no formato "<SIGLA> <NUMERO>".
 * Esse resolver valida esse formato, identificando quais informações
 * estão presentes no idenficador informado. Por exemplo: na indentificação
 * "87423" não há sigla, apenas o número. O resolver também desconsidera
 * informações estranhas ao formato, como por exemplo: "RE-87423///".
 * 
 * <p>Será inválida a identificação que só forneça a sigla.
 * 
 * @author Rodrigo Barreiros
 * @since 05.05.2010
 */
@Component
public class IdentificacaoProcessoResolver {
	
	private enum TipoIdentificacao {SIGLA, NUMERO}
	
	/**
	 * Verifica se a identificação é válida. Será inválida a identificação que só forneça a sigla.
	 * 
	 * @param identificacao a identificação do processo
	 * @return true, se válida, false, caso contrário
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
	 * Retorna a sigla do processo presente na identificação.
	 * 
	 * @param identificacao a identificação do processo
	 * @return a sigla do processo
	 */
	public String getSigla(String identificacao) {
		return (String) parseIdentificacao(identificacao, TipoIdentificacao.SIGLA);
	}
	
	/**
	 * Retorna o número do processo presente na identificação.
	 * 
	 * @param identificacao a identificação do processo
	 * @return o número do processo
	 */
	public Long getNumero(String identificacao) {
		return (Long) parseIdentificacao(identificacao, TipoIdentificacao.NUMERO);
	}

	/**
	 * Faz o parse da identificação do processo, recuperando a informação desejada: sigla ou tipo.
	 * 
	 * @param identificacao a identificação de entrada 
	 * @param tipoIdentificacao o tipo de informação que se deseja
	 * @return sigla ou número do processo
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
		// TODO: Implementar código da versão anterior.
		return classe;
	}
}
