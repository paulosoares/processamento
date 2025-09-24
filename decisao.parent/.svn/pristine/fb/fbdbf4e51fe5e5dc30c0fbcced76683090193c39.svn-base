/**
 * 
 */
package br.jus.stf.estf.decisao.texto.web;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.documento.model.exception.TextoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.texto.service.TextoService;

/**
 * @author Paulo.Estevao
 * @since 21.07.2010
 */
public abstract class AbstractExcluirTextoActionFacesBean<T> extends ActionSupport<TextoDto> {
	
	@Qualifier("textoServiceLocal") 
	@Autowired 
	private TextoService textoService;

	public void execute() {
		try {
			Collection<TextoDto> textosExcluidos = new ArrayList<TextoDto>();
			
			// Valida a exclusão dos textos marcados
			for (TextoDto texto : getResources()) {
				try {
					textoService.validaExclusaoTexto(texto);
					textosExcluidos.add(texto);
				} catch (TextoException e) {
					addError(montaMensagem(texto, e.getMessage()));
				}
			}
			
			// Remove os textos excluídos dos textos selecionados
			getResources().removeAll(textosExcluidos);
			
			if (textosExcluidos != null && textosExcluidos.size() > 0) {
				// Exclui os textos
				textoService.excluirTextos(textosExcluidos);
			}
		} catch (IllegalArgumentException e) {
			addError(e.getMessage());
		} catch (ServiceException e) {
			addError(e.getMessage());
		}
		 
		setRefresh(true);
		
		if(!hasMessages()) {
			sendToConfirmation();
		} else {
			sendToErrors();
		}
	}

	
	private String montaMensagem(TextoDto texto, String mensagem) {
		return texto.toString() + " - " + mensagem;
	}
}
