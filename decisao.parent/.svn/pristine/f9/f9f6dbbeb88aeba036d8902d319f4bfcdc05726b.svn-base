/**
 * 
 */
package br.jus.stf.estf.decisao.objetoincidente.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.service.ListaProcessosService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.pesquisa.domain.AllResourcesDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.util.NestedRuntimeException;
import br.jus.stf.estf.decisao.texto.support.ProcessoInvalidoParaListaDeTextosException;

/**
 * @author Paulo.Estevao
 * @since 24.08.2010
 */
@Action(id="criarEditarListaProcessosActionFacesBean", name="Criar/Editar Lista de Processos", view="/acoes/objetoincidente/criarLista.xhtml", height=180, width=500)
@Restrict({ActionIdentification.NOVA_LISTA_DE_PROCESSOS})
public class CriarListaProcessosActionFacesBean extends ActionSupport<AllResourcesDto> {
	
	@Autowired
	private ListaProcessosService listaProcessosService;
	
	@Qualifier("objetoIncidenteServiceLocal") 
	@Autowired 
	private ObjetoIncidenteService objetoIncidenteService;
	
	private String nomeNovaLista;

	private Collection<ObjetoIncidenteDto> processosParaSelecao;
	
	private Long idListaProcessos;
	private String identificacaoProcesso;
	
	public void execute() {
		try {
			ListaProcessos listaProcessos = null;
			
			// Se o ID da lista selecionada for -1, significa que o usuário selecionou a opções criar nova lista. Caso contrário, 
			// o usuário selecionou uma lista já existente. Neste caso, será informado o ID da lista selecionada.
	    	if(idListaProcessos == -1L) {
	    		// Criação de uma nova lista.
	    		if(nomeNovaLista != null && nomeNovaLista.trim().length() > 0) {
	    			if (getMinistro() == null) {
		    			throw new NestedRuntimeException("Para criar uma lista é necessário selecionar um setor.");
		    		}
	    			List<ListaProcessos> listas = listaProcessosService.pesquisarListaProcessos(nomeNovaLista, Boolean.TRUE, getSetorMinistro().getId());
					
					if (listas != null && listas.size() > 0) {
						addInformation("Já existe uma lista para este setor com este nome. Tente criar uma lista com um nome diferente.");
						sendToInformations();
						return;
					} else {
			    		listaProcessos = new ListaProcessos();			    		
			    		listaProcessos.setSetor(getSetorMinistro());
			    		listaProcessos.setNome(nomeNovaLista);
			    		listaProcessos.setAtiva(true);
			    		listaProcessos.setElementos(new LinkedHashSet<ObjetoIncidente<?>>());
			    		
			    		adicionarProcessos(listaProcessos);
			    		
			    		listaProcessosService.incluir(listaProcessos);
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
    			Hibernate.initialize(listaProcessos.getElementos());
    			
    			removerProcessos(listaProcessos);
    			
    			adicionarProcessos(listaProcessos);
    		
    	    	listaProcessosService.alterar(listaProcessos);
	    	}
	    	sendToConfirmation();			
		} catch (Exception e) {
			// Enviando mensagem padrão...
			addError(e.getMessage());
			sendToErrors();
		}
		setRefresh(true);
	}
	
	public void removerProcessos(ListaProcessos listaProcessos) {
		Collection<ObjetoIncidente<?>> processosParaExclusao = new ArrayList<ObjetoIncidente<?>>(); 
		for (ObjetoIncidente<?> oi : listaProcessos.getElementos()) {
			if (!processosParaSelecao.contains(ObjetoIncidenteDto.valueOf(oi))) {
				processosParaExclusao.add(oi);
			}
		}
		listaProcessos.getElementos().removeAll(processosParaExclusao);
	}
	
	public void adicionarProcessos(ListaProcessos listaProcessos) {
    	for (ObjetoIncidenteDto oi : processosParaSelecao) {
			ObjetoIncidente<?> objetoIncidente = (ObjetoIncidente<?>) objetoIncidenteService.recuperarObjetoIncidentePorId(oi.getId());
			if (!listaProcessos.getElementos().contains(oi)) {
				listaProcessos.getElementos().add(objetoIncidente);
			} 
    	}
    }
	
	public void selectAll() {
		boolean check = !allChecked();
		for (ObjetoIncidenteDto processo : processosParaSelecao) {
			processo.setSelected(check);
		}
	}
	
	private boolean allChecked() {
    	for (ObjetoIncidenteDto dto : processosParaSelecao) {
    		if (!dto.isSelected()) {
    			return false;
    		}
    	}
    	return true;
    }
	
	public String getNomeNovaLista() {
		return nomeNovaLista;
	}

	public void setNomeNovaLista(String nomeNovaLista) {
		this.nomeNovaLista = nomeNovaLista;
	}	
	
	public Long getIdListaProcessos() {
		return idListaProcessos;
	}

	public void setIdListaProcessos(Long idListaProcessos) {
		this.idListaProcessos = idListaProcessos;
	}

	public String getIdentificacaoProcesso() {
		return identificacaoProcesso;
	}

	public void setIdentificacaoProcesso(String identificacaoProcesso) {
		this.identificacaoProcesso = identificacaoProcesso;
	}

	@Override
	protected String getErrorTitle() {
		return "Erro ao criar lista de processos";
	}
	
	public void voltar() {
    	getDefinition().setFacet("principal");
    }
	
	public void incluirProcessoSelecionado(ObjetoIncidenteDto objetoIncidenteSelecionado) {
		try {
			if (objetoIncidenteSelecionado == null) {
				throw new ProcessoInvalidoParaListaDeTextosException("Selecione um processo para inclusão na lista!");
			}
			if (isProcessoNaLista(objetoIncidenteSelecionado)) {
				addInformation("O processo selecionado já faz parte da lista!");
			} else {
				adicionaProcessoNaTabela(objetoIncidenteSelecionado);
			}
		} catch (ProcessoInvalidoParaListaDeTextosException e) {
			addInformation(e.getMessage());
		} catch (Exception e) {
			addError(e.getMessage());
		}	

		identificacaoProcesso = null;
	}
	
	public void excluirProcessosSelecionados() {
		Collection<ObjetoIncidenteDto> processos = getProcessosParaSelecao();
		if(processos != null && processos.size()> 0) {
			Collection<ObjetoIncidenteDto> processosParaRetirar = new ArrayList<ObjetoIncidenteDto>();
			for (ObjetoIncidenteDto processo : processos) {
				if (processo.isSelected()) {
					processosParaRetirar.add(processo);
				}
			}
			processos.removeAll(processosParaRetirar);
		}
	}
	
	private void adicionaProcessoNaTabela(ObjetoIncidenteDto objetoIncidente) throws ServiceException {
		getProcessosParaSelecao().add(objetoIncidente);
	}
	
	private boolean isProcessoNaLista(ObjetoIncidenteDto objetoIncidente) {
		if (objetoIncidente != null) {
			return isColecoesContemProcesso(objetoIncidente);
		}
		throw new RuntimeException("Ocorreu um erro ao recuperar o processo informado! Por favor, tente novamente!");
	}
	
	private boolean isColecoesContemProcesso(ObjetoIncidenteDto objetoIncidente) {
		return getProcessosParaSelecao().contains(objetoIncidente);
	}
	
	public Collection<ObjetoIncidenteDto> getProcessosParaSelecao() {
		if (processosParaSelecao == null) {
			processosParaSelecao = new ArrayList<ObjetoIncidenteDto>();
		}
		return processosParaSelecao;
	}
	
	public List<SelectItem> getListasDisponiveis() throws ServiceException {
		List<SelectItem> itens = new LinkedList<SelectItem>();
		List<ListaProcessos> listas = getMinistro() == null ? new ArrayList<ListaProcessos>() : listaProcessosService.pesquisarListaProcessos(null, null, getSetorMinistro().getId());
		itens.add(new SelectItem(-1, "Nova Lista de Processos"));
		for (ListaProcessos lista : listas) {
			itens.add(new SelectItem(lista.getId(), lista.getNome()));
		}
		idListaProcessos = -1L;
		return itens;
	}
	
	public void carregarListaProcessos() {
		try {
			if (idListaProcessos != null && idListaProcessos != -1L) {
				ListaProcessos listaProcessos = listaProcessosService.recuperarPorId(idListaProcessos);
				getProcessosParaSelecao().clear();
				for (ObjetoIncidente<?> oi : listaProcessos.getElementos()) {
					processosParaSelecao.add(ObjetoIncidenteDto.valueOf(oi));
				}
				Collections.sort((List<ObjetoIncidenteDto>) processosParaSelecao, new Comparator<ObjetoIncidenteDto>() {

					@Override
					public int compare(ObjetoIncidenteDto o1,
							ObjetoIncidenteDto o2) {
								return (o1.getSiglaProcesso().compareTo(o2.getSiglaProcesso()) != 0 ? 
										o1.getSiglaProcesso().compareTo(o2.getSiglaProcesso()) : (
												o1.getNumeroProcesso().compareTo(o2.getNumeroProcesso()) != 0 ? 
														o1.getNumeroProcesso().compareTo(o2.getNumeroProcesso()) : 
															o1.getCadeia().compareTo(o2.getCadeia())));
					}
					
				});
			}
		} catch (ServiceException e) {
			throw new NestedRuntimeException(e);
		}
	}
}
