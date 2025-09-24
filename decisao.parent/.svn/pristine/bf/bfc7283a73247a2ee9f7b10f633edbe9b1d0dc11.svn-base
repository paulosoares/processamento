/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.web;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.service.ListaProcessosService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresList;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionCallback;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;

/**
 * @author Paulo.Estevao
 * @since 13.07.2010
 */
@Action(id="excluirProcessosActionFacesBean", 
		name="Excluir Processos da Lista", 
		view="/acoes/objetoincidente/excluirProcessos.xhtml")
@Restrict({ActionIdentification.EXCLUIR_PROCESSO_DE_UMA_LISTA})
@RequiresResources(Mode.Many)
@RequiresList
public class ExcluirProcessosActionFacesBean extends ActionSupport<ObjetoIncidenteDto> {

	@Autowired
	private ListaProcessosService listaProcessosService;
	
	@Qualifier("objetoIncidenteServiceLocal") 
	@Autowired 
	private ObjetoIncidenteService objetoIncidenteService;
	
	public void execute() {
		try {
			final ListaProcessos listaProcessos = listaProcessosService.recuperarPorId((Long) getOptions().get(ListaProcessos.class));
			if(listaProcessos != null) {
				Hibernate.initialize(listaProcessos.getElementos());
				execute(new ActionCallback<ObjetoIncidenteDto>() {
		    		public void doInAction(ObjetoIncidenteDto objetoIncidente) throws Exception {
		    			listaProcessos.getElementos().remove((ObjetoIncidente<?>) objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidente.getId()));
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
		
		if (!hasMessages()) {
			sendToConfirmation();
		} else {
			sendToErrors();
		}
		
		setRefresh(true);
	}
	
}
