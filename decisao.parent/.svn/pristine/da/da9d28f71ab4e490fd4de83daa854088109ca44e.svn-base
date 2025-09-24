package br.jus.stf.estf.decisao.objetoincidente.web;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidenteListaProcessos;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidenteListaProcessos.ObjetoIncidenteListaProcessosId;
import br.gov.stf.estf.processostf.model.service.ListaProcessosService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteListaProcessosService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.SearchData;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionCallback;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;

@Action(id="adicionarProcessosActionFacesBean", 
		name="Adicionar Processos à Lista", 
		view="/acoes/objetoincidente/adicionarProcessos.xhtml")
@Restrict({ActionIdentification.ADICIONAR_PROCESSO_A_UMA_LISTA})
@RequiresResources(Mode.Many)
public class AdicionarProcessosActionFacesBean extends ActionSupport<ObjetoIncidenteDto> {

	@Autowired
	private ListaProcessosService listaProcessosService;
	
	@Autowired
	private ObjetoIncidenteListaProcessosService objetoIncidenteListaProcessosService;
	
	@Qualifier("objetoIncidenteServiceLocal") 
	@Autowired 
	private ObjetoIncidenteService objetoIncidenteService;
	
	private Long idListaProcessos;
	
	private String nomeNovaListaProcessos;
	
	private String informationMenssage;
	

	/**
     * Executa as regras para adição de processo.
     */
    public void execute() {
    	try {
	    	ListaProcessos listaProcessos = null;
	    	
	    	// Se o ID da lista selecionada for -1, significa que o usuário selecionou a opções criar nova lista. Caso contrário, 
			// o usuário selecionou uma lista já existente. Neste caso, será informado o ID da lista selecionada.
	    	if(idListaProcessos == -1L) {
	    		// Criação de uma nova lista.
	    		if(nomeNovaListaProcessos != null && nomeNovaListaProcessos.trim().length() > 0) {
	    			List<ListaProcessos> listas = listaProcessosService.pesquisarListaProcessos(nomeNovaListaProcessos, Boolean.TRUE, getSetorMinistro().getId());
					
					if (listas != null && listas.size() > 0) {
						addInformation("Já existe uma lista para este setor com este nome. Tente criar uma lista com um nome diferente.");
						sendToInformations();
						return;
					} else {
			    		listaProcessos = new ListaProcessos();
			    		listaProcessos.setSetor(getSetorMinistro());
			    		listaProcessos.setNome(nomeNovaListaProcessos);
			    		listaProcessos.setAtiva(true);
			    		listaProcessos.setElementos(new LinkedHashSet<ObjetoIncidente<?>>());		    		
			    		listaProcessosService.incluir(listaProcessos);
			    		
			    		adicionarProcessos(listaProcessos);
					}
	    		} else {
	    			// Erro caso o usuário tente criar uma lista sem nome.
	    			addInformation("Por favor, informe o nome da nova lista.");
					sendToInformations();
	    			return;
	    		}
	    	} else {
	    		// Recupera a lista caso o usuário selecione uma lista existente. 
	    		listaProcessos = listaProcessosService.recuperarPorId(idListaProcessos);
    			
	    		adicionarProcessos(listaProcessos);
    		
    	    	if ( SearchData.stringNotEmpty(informationMenssage)) {
    	    		addInformation(informationMenssage);
					sendToInformations();
	    			return;
    	    	}
    	    	
	    	}
    	} catch(ServiceException e) {
    		addError(e.getMessage());
    		sendToErrors();
    	} catch(Exception e) {
			addError(e.getMessage());
			sendToErrors();
    	}
    	
    	setRefresh(true);
    	
    }
    
    public void adicionarProcessos(final ListaProcessos listaProcessos) {
    	execute(new ActionCallback<ObjetoIncidenteDto>() {
    		public void doInAction(ObjetoIncidenteDto objetoIncidente) throws Exception {
    			ObjetoIncidente<?> oi = (ObjetoIncidente<?>) objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidente.getId());
    			
    			ObjetoIncidenteListaProcessos existente = objetoIncidenteListaProcessosService.recuperar(listaProcessos, oi);
    			if (existente != null) {
    				addError("[" + objetoIncidente.getIdentificacao() + "] - Processo já pertence à lista.");
    			} else {
    				ObjetoIncidenteListaProcessos objetoIncidenteListaProcessos = new ObjetoIncidenteListaProcessos();
	    			ObjetoIncidenteListaProcessosId objetoIncidenteListaProcessosId = new ObjetoIncidenteListaProcessosId();
	    			objetoIncidenteListaProcessosId.setObjetoIncidente(oi);
	    			objetoIncidenteListaProcessosId.setListaProcessos(listaProcessos);
	    			objetoIncidenteListaProcessos.setId(objetoIncidenteListaProcessosId);
	    			
	    			objetoIncidenteListaProcessosService.incluir(objetoIncidenteListaProcessos); 
    			}
    		}
    	});
    }
    
    public void voltar() {
    	getDefinition().setFacet("principal");
    }
	
	public List<SelectItem> getListasDisponiveis() throws ServiceException {
		List<SelectItem> itens = new LinkedList<SelectItem>();
		List<ListaProcessos> listas = listaProcessosService.pesquisarListaProcessos(null, null, getSetorMinistro().getId());
		itens.add(new SelectItem(-1, "Nova Lista de Processos"));
		for (ListaProcessos lista : listas) {
			itens.add(new SelectItem(lista.getId(), lista.getNome()));
		}
		idListaProcessos = -1L;
		return itens;
	}

	public void setIdListaProcessos(Long idListaProcessos) {
		this.idListaProcessos = idListaProcessos;
	}

	public Long getIdListaProcessos() {
		return idListaProcessos;
	}

	public void setNomeNovaListaProcessos(String nomeNovaListaProcessos) {
		this.nomeNovaListaProcessos = nomeNovaListaProcessos;
	}

	public String getNomeNovaListaProcessos() {
		return nomeNovaListaProcessos;
	}
	
	public String getInformationMenssage() {
		return informationMenssage;
	}

	public void setInformationMenssage(String informationMenssage) {
		this.informationMenssage = informationMenssage;
	}
}
