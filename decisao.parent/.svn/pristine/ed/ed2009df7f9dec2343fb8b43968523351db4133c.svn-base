package br.jus.stf.estf.decisao.support.action.handlers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.gov.stf.estf.entidade.documento.tipofase.FaseTexto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;

/**
 * Trata ação marcadas com {@link States}
 * 
 * @author Rodrigo Barreiros
 * @see 27.05.2010
 */
@Component
public class StatesHandler implements ActionConditionHandler<States> {
	
	/**
	 * @see br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler#matches(java.lang.Object, java.util.Set, java.lang.Class, java.util.Map)
	 */
	@Override
	public <T> boolean matches(States annotation, Set<T> resources, Class<T> resourceClass, Map<?, ?> options) {
		if (resourceClass.equals(TextoDto.class)) {
			List<FaseTexto> fases = Arrays.asList(annotation.value());
			for (Object resource : resources) {
				if (!fases.contains(((TextoDto) resource).getFase())) {
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
	public Class<States> getAnnotation() {
		return States.class;
	}

}
