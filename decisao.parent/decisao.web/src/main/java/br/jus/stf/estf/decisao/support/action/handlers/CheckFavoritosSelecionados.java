package br.jus.stf.estf.decisao.support.action.handlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica que a ação só deve ser apresentada se os 
 * textos forem todos favoritos ou todos não-favoritos.
 * 
 * @author Hertony.Morais
 * @since 31.03.2015
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CheckFavoritosSelecionados {
	public enum Mode {Favoritados, Desfavoritados}
	
	Mode value();
}