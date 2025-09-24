package br.jus.stf.estf.decisao.support.action.handlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;

/**
 * Indica que a��o deve ser validada com rela��o a seguran�a e s� ser�
 * exibida se o usu�rio possuir o perfil exigido pela a��o.
 *  
 * @author Rodrigo Barreiros
 * @see 27.05.2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Restrict {
	
	/**
	 * O identificador da a��o para valida��o de seguran�a
	 * 
	 * @return o identificador
	 */
	ActionIdentification[] value();
	
}
