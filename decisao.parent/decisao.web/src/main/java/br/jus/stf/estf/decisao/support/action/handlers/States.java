package br.jus.stf.estf.decisao.support.action.handlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;

/**
 * Indica que ação só será exibida para determinados estados do texto.
 * 
 * @author Rodrigo Barreiros
 * @see 27.05.2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface States {
	
	/**
	 * Os estados do textos para os quais a ação deve ser exibida.
	 * 
	 * @return os estados do texto
	 */
	FaseTexto[] value();

}
