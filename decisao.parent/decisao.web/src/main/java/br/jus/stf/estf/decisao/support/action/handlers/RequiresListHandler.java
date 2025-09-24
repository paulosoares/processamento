package br.jus.stf.estf.decisao.support.action.handlers;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.gov.stf.estf.entidade.documento.ListaTextos;
import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;

/**
 * Trata ação marcadas com {@link RequiresList}
 * 
 * @author Rodrigo Barreiros
 * @since 19.05.2010
 */
@Component
public class RequiresListHandler implements ActionConditionHandler<RequiresList> {

	/**
	 * @see br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler#matches(java.lang.Object, java.util.Set, java.lang.Class, java.util.Map)
	 */
	@Override
	public <T> boolean matches(RequiresList annotation, Set<T> resources, Class<T> resourceClass, Map<?, ?> options) {
		Class<?> key = resourceClass.isAssignableFrom(ObjetoIncidenteDto.class)? ListaProcessos.class:ListaTextos.class;
		if (options.get(key) != null) {
			return ((Long) options.get(key)).longValue() > 0;
		}
		return false;
	}

	/**
	 * @see br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler#getAnnotation()
	 */
	@Override
	public Class<RequiresList> getAnnotation() {
		return RequiresList.class;
	}

}
