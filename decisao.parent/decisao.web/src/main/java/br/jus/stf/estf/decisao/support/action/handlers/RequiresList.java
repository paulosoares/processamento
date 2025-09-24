package br.jus.stf.estf.decisao.support.action.handlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica que a��o requer que uma lista tenha sido selecionada
 * no formul�rio de pesquisa avan�ada.
 * 
 * @author Rodrigo Barreiros
 * @see 27.05.2010
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequiresList {

}
