package br.jus.stf.estf.decisao.support.action.handlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;

/**
 * Indica que ação deve ser validada com relação a segurança e só será
 * exibida se o usuário possuir o perfil exigido pela ação.
 *  
 * @author Rodrigo Barreiros
 * @see 27.05.2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Restrict {
	
	/**
	 * O identificador da ação para validação de segurança
	 * 
	 * @return o identificador
	 */
	ActionIdentification[] value();
	
}
