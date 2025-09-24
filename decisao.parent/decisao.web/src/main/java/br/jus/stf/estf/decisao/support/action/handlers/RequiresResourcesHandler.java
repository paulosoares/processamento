package br.jus.stf.estf.decisao.support.action.handlers;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;

/**
 * Trata ação marcadas com {@link RequiresResources}
 * 
 * @author Rodrigo Barreiros
 * @since 19.05.2010
 */
@Component
public class RequiresResourcesHandler implements ActionConditionHandler<RequiresResources> {

	/**
	 * @see br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler#matches(java.lang.Object, java.util.Set, java.lang.Class, java.util.Map)
	 */
	@Override
	public <T> boolean matches(RequiresResources annotation, Set<T> resources, Class<T> resourceClass, Map<?, ?> options) {
		if (annotation.value().equals(Mode.Many)) {
			return (resources.size() > 0);
		}
		if (annotation.value().equals(Mode.One)) {
			return resources.size() == 1;
		}
		return false;
	}

	/**
	 * @see br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler#getAnnotation()
	 */
	@Override
	public Class<RequiresResources> getAnnotation() {
		return RequiresResources.class;
	}

}
