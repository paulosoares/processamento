package br.jus.stf.estf.decisao.support.action.handlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica que ação só será exibida para determinados tipos de texto.
 * 
 * @author Paulo.Estevao
 * @see 11.09.2011
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CheckIdTipoTexto {
	
	/**
	 * Os tipos de texto para os quais a ação deve ser exibida.
	 * 
	 * @return os tipos de texto
	 */
	long[] value();

}
