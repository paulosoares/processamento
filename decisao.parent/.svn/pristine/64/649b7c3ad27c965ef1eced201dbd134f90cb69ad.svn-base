package br.jus.stf.estf.decisao.support.action.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckFavoritosSelecionados.Mode;

/**
 * Trata ações marcadas com {@link CheckFavoritosSelecionados}
 * 
 * <p>Executa validação para determinar se uma ação pode ser executada, 
 * dados os textos selecionados.
 * 
 * <p>A ação só deve ser apresentada se os textos selecionados 
 * forem todos favoritos ou se nenhum texto selecionado for favorito.
 * 
 * @author Hertony.Morais
 * @since 31.03.2015
 */
@Component
public class CheckFavoritosSelecionadosHandler implements ActionConditionHandler<CheckFavoritosSelecionados>{
	
	/**
	 * @see br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler#matches(java.lang.Object, java.util.Set, java.lang.Class, java.util.Map)
	 */
	@Override
	public <T> boolean matches(CheckFavoritosSelecionados annotation, Set<T> resources, Class<T> resourceClass, Map<?, ?> options) {
		if(resourceClass.equals(TextoDto.class)) {
			List<TextoDto> textosFavoritados = new ArrayList<TextoDto>();
			List<TextoDto> textosDesfavoritados = new ArrayList<TextoDto>();
			
			for (Object resource : resources) {
				TextoDto textoDto = (TextoDto) resource;
				
				if(textoDto.isFavoritoNoGabinete()){
					textosFavoritados.add(textoDto);
					
					continue;
				}
				
				textosDesfavoritados.add(textoDto);
			}
			
			boolean temFavoritados = !textosFavoritados.isEmpty();
			boolean temDesfavoritados = !textosDesfavoritados.isEmpty();
			
			if(!(temFavoritados^temDesfavoritados)){
				return false;
			}
			
			boolean modoFavoritados = annotation.value() == Mode.Favoritados;
			boolean modoDesfavoritados = annotation.value() == Mode.Desfavoritados;
			
			return (temFavoritados&modoFavoritados)|(temDesfavoritados&modoDesfavoritados);
		}
		
		return false;
	}

	/**
	 * @see br.jus.stf.estf.decisao.support.action.handlers.ActionConditionHandler#getAnnotation()
	 */
	@Override
	public Class<CheckFavoritosSelecionados> getAnnotation() {
		return CheckFavoritosSelecionados.class;
	}

}
