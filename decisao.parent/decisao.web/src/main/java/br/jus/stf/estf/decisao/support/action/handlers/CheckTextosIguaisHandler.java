package br.jus.stf.estf.decisao.support.action.handlers;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;

/**
 * Trata ações marcadas com {@link CheckTextosIguais}
 * 
 * <p>Executa validação para determinar se uma ação pode ser executada, 
 * dados os textos selecionados.
 * 
 * <p>A ação só deve ser apresentada se os textos selecionados 
 * estiverem em lista de textos iguais.
 * 
 * @author Rodrigo Barreiros
 * @since 22.07.2010
 */
@Component
public class CheckTextosIguaisHandler implements ActionConditionHandler<CheckTextosIguais>{
	
	/**
	 * @see br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler#matches(java.lang.Object, java.util.Set, java.lang.Class, java.util.Map)
	 */
	@Override
	public <T> boolean matches(CheckTextosIguais annotation, Set<T> resources, Class<T> resourceClass, Map<?, ?> options) {
		if (resourceClass.isAssignableFrom(TextoDto.class)) {
			// Verifica se todos os recursos selecionados pertencem à lista de textos iguais
			for (T resource : resources) {
				TextoDto texto = (TextoDto) resource;
				// Se algum recurso não estiver associado à lista de textos iguais, retornar false.
				if (!texto.isTextosIguais()) {
					return false;
				}
			}
			// Se todos os recurso estiverem associados à lista de textos iguais, retornar true.
			return true;
		}
		return false;
	}
	
	/**
	 * @see br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler#getAnnotation()
	 */
	@Override
	public Class<CheckTextosIguais> getAnnotation() {
		return CheckTextosIguais.class;
	}

}
