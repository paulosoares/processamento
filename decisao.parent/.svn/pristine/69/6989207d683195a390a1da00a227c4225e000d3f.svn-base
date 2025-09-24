/**
 * 
 */
package br.jus.stf.estf.decisao.texto.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.documento.model.service.ListaTextosService;
import br.gov.stf.estf.documento.model.service.TextoListaTextoService;
import br.gov.stf.estf.entidade.documento.ListaTextos;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TextoListaTexto;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckMinisterId;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRestrictions;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresList;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionCallback;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.texto.service.TextoService;

/**
 * @author Paulo.Estevao
 * @since 14.07.2010
 */
@Action(id="excluirTextosActionFacesBean", 
		name="Excluir Textos da Lista", 
		view="/acoes/texto/excluirTextos.xhtml")
@Restrict({ActionIdentification.EXCLUIR_TEXTO_DE_UMA_LISTA})
@RequiresResources(Mode.Many)
@RequiresList
@CheckMinisterId
@CheckRestrictions
public class ExcluirTextosActionFacesBean extends ActionSupport<TextoDto> {

	@Autowired
	private ListaTextosService listaTextosService;

	@Autowired
	private TextoListaTextoService textoListaTextoService;
	
	@Qualifier("textoServiceLocal") 
	@Autowired 
	private TextoService textoService;
	
	public void execute() {
		try {
			final ListaTextos listaTexto = listaTextosService.recuperarPorId((Long) getOptions().get(ListaTextos.class));
			if(listaTexto != null) {
				execute(new ActionCallback<TextoDto>() {
		    		public void doInAction(TextoDto texto) throws Exception {
		    			TextoListaTexto textoListaTexto = textoListaTextoService.recuperar(listaTexto, (Texto) textoService.recuperarTextoPorId(texto.getId()));
		    			textoListaTextoService.excluir(textoListaTexto);
		    		}
		    	});
			} else {
				addError("Lista não encontrada.");
			}
		} catch(ServiceException e) {
			addError(e.getMessage());
		} catch(Exception e) {
			addError(e.getMessage());
		}
		
		setRefresh(true);
		
		if (!hasMessages()) {
			sendToConfirmation();
		} else {
			sendToErrors();
		}
	}
	
}
