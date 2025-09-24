package br.jus.stf.estf.decisao.objetoincidente.web;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.processostf.model.service.ListaProcessosService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaIncidentesDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionCallback;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;

@Action(id = "excluirListaProcessoActionFacesBean", 
		name = "Excluir lista de processos", 
		view = "/acoes/objetoincidente/excluirLista.xhtml", 
		height = 100, width = 500)
@Restrict({ActionIdentification.EXCLUIR_LISTA_DE_PROCESSOS})
@RequiresResources(Mode.Many)
public class ExcluirListaProcessoActionFacesBean extends ActionSupport<ListaIncidentesDto> {
	
	@Autowired
	private ListaProcessosService listaProcessosService;
	
	public void execute() {
		execute(new ActionCallback<ListaIncidentesDto>() {
			public void doInAction(ListaIncidentesDto listaProcessos) throws Exception {
				doExecute(listaProcessos);
			}
		});
		
		if(hasMessages()) {
			sendToErrors();
		} else {
			sendToConfirmation();
		}
		
		setRefresh(true);
	}
	
	protected void doExecute(ListaIncidentesDto listaProcessos) throws Exception {
		try {
			listaProcessosService.excluir(listaProcessosService.recuperarPorId(listaProcessos.getId()));
		} catch (ServiceException e) {
			addError(listaProcessos.getNome() + " - " + e.getMessage());
		}
	}
	
	@Override
	protected String getErrorTitle() {
		return "Erro ao excluir lista(s) de processos";
	}
}
