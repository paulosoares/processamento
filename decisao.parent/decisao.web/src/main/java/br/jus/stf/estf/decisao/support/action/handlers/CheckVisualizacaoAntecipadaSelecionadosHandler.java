package br.jus.stf.estf.decisao.support.action.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckVisualizacaoAntecipadaSelecionados.BooleanMode;

/**
 * Trata ações marcadas com {@link CheckVisualizacaoAntecipadaSelecionados}
 * 
 * <p>Executa validação para determinar se uma ação pode ser executada, 
 * dados os textos selecionados.
 * 
 * <p>A ação só deve ser apresentada se TODOS os textos selecionados 
 * possuem a flag visualizacao antecipada setada ou se nenhum possui a flag
 * 
 */
@Component
public class CheckVisualizacaoAntecipadaSelecionadosHandler implements ActionConditionHandler<CheckVisualizacaoAntecipadaSelecionados>{
	
	@Override
	public <T> boolean matches(CheckVisualizacaoAntecipadaSelecionados annotation, Set<T> resources, Class<T> resourceClass, Map<?, ?> options) {
		if(resourceClass.equals(TextoDto.class)) {
			List<TextoDto> textosLiberados = new ArrayList<TextoDto>();
			List<TextoDto> textosNaoLiberados = new ArrayList<TextoDto>();
			
			for (Object resource : resources) {
				TextoDto textoDto = (TextoDto) resource;
				
				if(Boolean.TRUE.equals(textoDto.getLiberacaoAntecipada()))
					textosLiberados.add(textoDto);
				else
					textosNaoLiberados.add(textoDto);
			}
			
			boolean temTextoLiberado = !textosLiberados.isEmpty();
			boolean temTextoNaoLiberado = !textosNaoLiberados.isEmpty();
			
			if(!(temTextoLiberado^temTextoNaoLiberado)) // O sinal ^ é o XOR (ou exclusivo)
				return false;
			
			boolean modoTextoLiberado = annotation.value() == BooleanMode.SIM;
			boolean modoTextoNaoLiberado = annotation.value() == BooleanMode.NAO;
			
			return (temTextoLiberado&modoTextoLiberado)|(temTextoNaoLiberado&modoTextoNaoLiberado);
		}
		
		return false;
	}

	@Override
	public Class<CheckVisualizacaoAntecipadaSelecionados> getAnnotation() {
		return CheckVisualizacaoAntecipadaSelecionados.class;
	}

}
