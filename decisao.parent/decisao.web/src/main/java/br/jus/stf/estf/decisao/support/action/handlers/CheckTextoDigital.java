package br.jus.stf.estf.decisao.support.action.handlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.gov.stf.estf.entidade.documento.Texto.TipoRestricao;

/**
 * Indica que a��o deve ser validada com rela��o � restri��es
 * a textos produzidos no sistema digital
 * 
 * @author Edvaldo Oliveira
 * @see 24.09.2024
 * 
 * @see TipoRestricao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CheckTextoDigital {
	
}
