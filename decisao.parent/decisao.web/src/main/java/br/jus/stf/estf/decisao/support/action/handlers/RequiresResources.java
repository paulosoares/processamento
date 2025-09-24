package br.jus.stf.estf.decisao.support.action.handlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica que a a��o marcada com essa anaota��o requer que um ou v�rios
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
	 * Retorna o n�mero de recursos solicitados: um ou muitos
	 * 
	 * @return o n�mero de recurso
	 */
	Mode value();

}
