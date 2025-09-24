package br.jus.stf.estf.decisao.support.action.handlers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;

/**
 * Trata ação marcadas com {@link CheckNotForIdTipoTexto}
 * 
 * @author Paulo.Estevao
 * @see 14.09.2011
 */
@Component
public class CheckNotForIdTipoTextoHandler implements ActionConditionHandler<CheckNotForIdTipoTexto> {
	
	/**
	 * @see br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler#matches(java.lang.Object, java.util.Set, java.lang.Class, java.util.Map)
	 */
	@Override
	public <T> boolean matches(CheckNotForIdTipoTexto annotation, Set<T> resources, Class<T> resourceClass, Map<?, ?> options) {
		if (resourceClass.equals(TextoDto.class)) {
			long[] idsTipoTexto = annotation.value();
			Set<Long> tiposTexto = new HashSet<Long>();
			for (Long id : idsTipoTexto) {
				tiposTexto.add(id);
			}
			for (Object resource : resources) {
				if (tiposTexto.contains(((TextoDto) resource).getTipoTexto().getCodigo())) {
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
	public Class<CheckNotForIdTipoTexto> getAnnotation() {
		return CheckNotForIdTipoTexto.class;
	}

}
