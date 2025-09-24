package br.jus.stf.estf.decisao.support.action.handlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica que a ação marcada com essa anaotação requer que um ou vários
 * recursos tenha sido selecionados na tela.
 * 
 * @author Rodrigo Barreiros
 * @see 27.05.2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequiresResources {
	
	public enum Mode {One, Many}
	
	/**
	 * Retorna o número de recursos solicitados: um ou muitos
	 * 
	 * @return o número de recurso
	 */
	Mode value();

}
