package br.jus.stf.estf.decisao.support.action.handlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica que a ação só deve ser apresentada se o 
 * texto pertencer à lista de textos iguais.
 * 
 * @author Rodrigo Barreiros
 * @see 22.07.2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CheckTextosIguais {
	
}
