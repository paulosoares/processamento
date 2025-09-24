/**
 * 
 */
package br.jus.stf.estf.decisao.texto.web;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.documento.model.service.ListaTextosService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaTextosDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionCallback;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;

/**
 * @author Paulo.Estevao
 * @since 26.08.2010
 */
@Action(id="excluirListaTextosActionFacesBean", name="Excluir Lista", view="/acoes/texto/excluirLista.xhtml", height=120, width=500)
@Restrict({ActionIdentification.EXCLUIR_LISTA_DE_TEXTOS})
@RequiresResources(Mode.Many)
public class ExcluirListaTextosActionFacesBean extends ActionSupport<ListaTextosDto> {
	
	@Autowired
	private ListaTextosService listaTextosService;
	
	public void execute() {
		execute(new ActionCallback<ListaTextosDto>() {
			public void doInAction(ListaTextosDto listaTextos) throws Exception {
				doExecute(listaTextos);
			}
		});
		setRefresh(true);
		if(hasMessages()) {
			sendToErrors();
		} else {
			sendToConfirmation();
		}
	}
	
	protected void doExecute(ListaTextosDto listaTextos) throws Exception {
		try {
			listaTextosService.excluir(listaTextosService.recuperarPorId(listaTextos.getId()));
		} catch (ServiceException e) {
			addError(listaTextos.getNome() + " - " + e.getMessage());
		}
	}
	
	@Override
	protected String getErrorTitle() {
		return "Erro ao excluir lista(s) de textos";
	}
}
