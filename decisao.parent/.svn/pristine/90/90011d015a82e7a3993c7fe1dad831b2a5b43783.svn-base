package br.jus.stf.estf.decisao.support.action.handlers;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.gov.stf.estf.entidade.documento.Texto.TipoRestricao;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;

/**
 * Trata ações marcadas com {@link CheckTextoDigital}
 * 
 * <p>
 * Executa validação para determinar se uma texto foi produzido no digital
 * caso tenha sido, não exibe o menu excluir texto.
 * 
 * @author Edvaldo Costa
 * @since 24.09.2024
 * 
 * @see TipoRestricao
 */
@Component
public class CheckTextoDigitalHandler implements ActionConditionHandler<CheckTextoDigital> {

	/**
	 * @see br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler#matches(java.lang.Object,
	 *      java.util.Set, java.lang.Class, java.util.Map)
	 */
	@Override
	public <T> boolean matches(CheckTextoDigital annotation, Set<T> resources, Class<T> resourceClass, Map<?, ?> options) {
		if (resourceClass.isAssignableFrom(TextoDto.class)) {
			for (T t : resources) {
				TextoDto textoDto = (TextoDto) t;
				
				if(textoDto.getOrigemDigital()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @see br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler#getAnnotation()
	 */
	@Override
	public Class<CheckTextoDigital> getAnnotation() {
		return CheckTextoDigital.class;
	}

}
