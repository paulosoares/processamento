/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.gov.stf.estf.processostf.model.service.ListaProcessosService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaIncidentesDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;

/**
 * @author Paulo.Estevao
 * @since 29.09.2010
 */
@Action(id="alterarListaProcessosActionFacesBean", name="Alterar Lista", view="/acoes/objetoincidente/alterarLista.xhtml", height=210, width=500)
@Restrict({ActionIdentification.EDITAR_LISTA_DE_PROCESSOS})
@RequiresResources(Mode.One)
public class AlterarListaProcessosActionFacesBean extends ActionSupport<ListaIncidentesDto> {
	
	@Autowired
	private ListaProcessosService listaProcessosService;
	
	private String nomeLista;
	
	private boolean ativa;
	
	private ListaProcessos listaProcessos;
	
	@Override
	public void load() {
		// Recuperando e setando novos dados da lista...
		try {
			listaProcessos = listaProcessosService.recuperarPorId(getResources().iterator().next().getId());
			nomeLista = listaProcessos.getNome();
			ativa = listaProcessos.getAtiva();
		} catch (ServiceException e) {
			addError(e.getMessage());
			logger.error(e.getMessage(), e);
			sendToErrors();
		}
	}
	
	public void execute() {
		try {
			if (nomeLista == null || nomeLista.trim().length() == 0 ) {
				// Enviando mensagem padrão...
				addInformation("Por favor, informe o novo nome da lista.");
			} else {
				List<ListaProcessos> listas = listaProcessosService.pesquisarListaProcessos(nomeLista, Boolean.TRUE, getSetorMinistro().getId());
				
				if (listas != null && listas.size() > 0) {
					addInformation("Já existe uma lista para este setor com este nome. Tente criar uma lista com um nome diferente.");
					sendToInformations();
				} else {
					listaProcessos.setNome(nomeLista);
					listaProcessos.setAtiva(ativa);
					// Persistindo a lista...
					listaProcessosService.alterar(listaProcessos);
					sendToConfirmation();
				}
			}
		} catch (ServiceException e) {
			addError(e.getMessage());
			logger.error(e.getMessage(), e);
			sendToErrors();
		}
		setRefresh(true);
	}

	public String getNomeNovaLista() {
		return nomeLista;
	}

	public void setNomeNovaLista(String nomeLista) {
		this.nomeLista = nomeLista;
	}

	public boolean isAtiva() {
		return ativa;
	}

	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}
	
	public ListaProcessos getLista() {
		return listaProcessos;
	}
	
	@Override
	public String getErrorTitle() {
		return "Erro ao alterar lista de processos";
	}
	
	public void voltar() {
    	getDefinition().setFacet("principal");
    }
}
