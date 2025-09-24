package br.jus.stf.estf.decisao.objetoincidente.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.julgamento.model.service.ListaJulgamentoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.objetoincidente.support.DadosAgendamentoDto;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoNaoPodeSerAgendadoException;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoPrecisaDeConfirmacaoException;
import br.jus.stf.estf.decisao.objetoincidente.support.TipoAgendamento;
import br.jus.stf.estf.decisao.objetoincidente.support.TipoColegiadoAgendamento;
import br.jus.stf.estf.decisao.objetoincidente.support.ValidacaoLiberacaoParaJulgamentoException;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionCallback;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;

@Action(id = "adicionarProcessosListaJulgamentoActionFacesBean", name = "Adicionar Processos à Lista para Julgamento", view = "/acoes/objetoincidente/adicionarProcessosListaJulgamento.xhtml",width=600)
@Restrict({ ActionIdentification.LIBERAR_PARA_JULGAMENTO })
@RequiresResources(Mode.Many)
public class AdicionarProcessosListaJulgamentoActionFacesBean extends
		ActionSupport<ObjetoIncidenteDto> {

	@Qualifier("objetoIncidenteServiceLocal")
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;

	private Long idListaJulgamento;

	private String nomeNovaListaProcessos;

	private String informationMenssage;

	@Autowired
	private ListaJulgamentoService listaJulgamentoService;

	private Set<ObjetoIncidenteDto> processosParaConfirmacao = new HashSet<ObjetoIncidenteDto>();
	private Set<ObjetoIncidenteDto> processosInvalidos = new HashSet<ObjetoIncidenteDto>();
	private Boolean confirmarProcessosPendentes = false;

	/**
	 * Executa as regras para adição de processo.
	 */
	public void execute() {
		if (existeProcessoSelecionado()) {
			// Limpa as mensagens mostradas anteriormente.
			cleanMessages();
			// Retira os processos inválidos e pendentes de confirmação dos
			// recursos selecionados.
			getResources().removeAll(processosInvalidos);
			// Se os processos pendentes não forem confirmados, devem ser
			// retirados
			// do processamento
			if (!getConfirmarProcessosPendentes()) {
				getResources().removeAll(processosParaConfirmacao);
			}
			
			try {
				final ListaJulgamento listaJulgamento = idListaJulgamento != null && idListaJulgamento.intValue() > 0 
						? listaJulgamentoService.recuperarPorId(idListaJulgamento) : null;
	
				if (idListaJulgamento != null && idListaJulgamento.intValue() > 0) {
					execute(new ActionCallback<ObjetoIncidenteDto>() {
						public void doInAction(ObjetoIncidenteDto dto)
								throws Exception {
							ObjetoIncidente<?> objetoIncidente = objetoIncidenteService
									.recuperarObjetoIncidentePorId(dto.getId());
							objetoIncidenteService.incluirProcessoListaJulgamento(
									objetoIncidente, listaJulgamento,
									montaDadosDoAgendamento(listaJulgamento));
						}
					});
				}
			} catch (ServiceException e) {
				addError("Erro ao adicionar processos à lista de julgamento.");
			}
		} else {
			getDefinition().setFacet("nenhumProcessoSelecionado");
			getDefinition().setHeight(180);
		}
		
		if (hasMessages()) {
			sendToErrors();
		} else {
			sendToConfirmation();
		}
	}
	
	/**
	 * Verifica se os processos selecionados são maiores do que a quantidade de processos que será excluída pela validação.
	 * @return True se houver algum recurso para ser processado. False caso contrário.
	 */
	private boolean existeProcessoSelecionado() {
		int tamanhoDosRecursos = getResources().size();
		int tamanhoDeExcluidos = processosInvalidos.size();
		if (!getConfirmarProcessosPendentes()) {
			// Caso não marque os agendamentos pendentes, eles serão retirados.
			tamanhoDeExcluidos += processosParaConfirmacao.size();
		}
		return tamanhoDosRecursos > tamanhoDeExcluidos;
	}	
	
	public void validateAndExecute() {
		for (ObjetoIncidenteDto dto : getResources()) {
			try {
				ObjetoIncidente<?> objetoIncidente = objetoIncidenteService.recuperarObjetoIncidentePorId(dto.getId());
				objetoIncidenteService.verificaProcessoEmListaJulgamentoPrevista(objetoIncidente);
				ListaJulgamento listaJulgamento = listaJulgamentoService.recuperarPorId(idListaJulgamento);
				objetoIncidenteService.verificaAgendamentoProcesso(objetoIncidente, TipoColegiadoConstante.valueOfSigla(listaJulgamento.getSessao().getColegiado().getId()),Agendamento.COD_MATERIA_AGENDAMENTO_JULGAMENTO);
				objetoIncidenteService.validarProcessoParaAgendamento(montaDadosDoAgendamento(dto, listaJulgamento));
				
			} catch (ValidacaoLiberacaoParaJulgamentoException e) {
				addError(dto.getIdentificacao()	+ " - " + e.getMessage());
				processosInvalidos.add(dto);
			} catch (ServiceException e) {
				addError(dto.getIdentificacao()
						+ " - " + "Erro ao validar processo. - " + e.getMessage());
				processosInvalidos.add(dto);
				logger.error(dto.getIdentificacao()
						+ " - " + "Erro ao validar processo. - " + e.getMessage(), e);
			} catch (ProcessoPrecisaDeConfirmacaoException e) {
				addWarning(dto.getIdentificacao()+ " - " + e.getMessage());
				processosParaConfirmacao.add(dto);
			} catch (ProcessoNaoPodeSerAgendadoException e) {
				addError(dto.getIdentificacao()	+ " - " + e.getMessage());
				processosInvalidos.add(dto);
			}
		}
		if (hasMessages()) {
			sendToInformations();
		} else {
			execute();
		}
	}
	
	private DadosAgendamentoDto montaDadosDoAgendamento(ListaJulgamento listaJulgamento) {
		DadosAgendamentoDto dadosAgendamento = new DadosAgendamentoDto();
		dadosAgendamento.setMinistro(getMinistro());
		dadosAgendamento.setTipoAgendamento(TipoAgendamento.INDICE);
		dadosAgendamento.setUsuario(getUsuario());
		dadosAgendamento.setSetorDoUsuario(getSetorMinistro());		
		TipoColegiadoConstante colegiado = TipoColegiadoConstante.valueOfSigla(listaJulgamento.getSessao().getColegiado().getId());
		TipoColegiadoAgendamento colegiadoAgendamento;
		if (colegiado.equals(TipoColegiadoConstante.SESSAO_PLENARIA)){
			colegiadoAgendamento = TipoColegiadoAgendamento.PLENARIO;
		}else if (colegiado.equals((TipoColegiadoConstante.PRIMEIRA_TURMA))){
			colegiadoAgendamento = TipoColegiadoAgendamento.PT;
		}else{
			colegiadoAgendamento = TipoColegiadoAgendamento.ST;
		}
		dadosAgendamento.setTipoColegiadoAgendamento(colegiadoAgendamento);
		return dadosAgendamento;
	}

	private DadosAgendamentoDto montaDadosDoAgendamento(ObjetoIncidenteDto processo, ListaJulgamento listaJulgamento) {
		DadosAgendamentoDto dadosAgendamento = new DadosAgendamentoDto();
		dadosAgendamento.setMinistro(getMinistro());
		dadosAgendamento.setObjetoIncidenteDto(processo);
		dadosAgendamento.setTipoAgendamento(TipoAgendamento.INDICE);
		dadosAgendamento.setUsuario(getUsuario());
		dadosAgendamento.setSetorDoUsuario(getSetorMinistro());
		TipoColegiadoConstante colegiado = TipoColegiadoConstante.valueOfSigla(listaJulgamento.getSessao().getColegiado().getId());
		TipoColegiadoAgendamento colegiadoAgendamento;
		if (colegiado.equals(TipoColegiadoConstante.SESSAO_PLENARIA)){
			colegiadoAgendamento = TipoColegiadoAgendamento.PLENARIO;
		}else if (colegiado.equals((TipoColegiadoConstante.PRIMEIRA_TURMA))){
			colegiadoAgendamento = TipoColegiadoAgendamento.PT;
		}else{
			colegiadoAgendamento = TipoColegiadoAgendamento.ST;
		}
		dadosAgendamento.setTipoColegiadoAgendamento(colegiadoAgendamento);
		return dadosAgendamento;
	}
	
	public void voltar() {
		getDefinition().setFacet("principal");
	}

	public List<SelectItem> getListasDisponiveis() throws ServiceException {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		List<ListaJulgamento> listas = listaJulgamentoService
				.pesquisar(getMinistro());
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		for (ListaJulgamento lista : listas) {
			if (lista.getSessao().getDataInicio() != null) {
				String label = formatter.format(lista.getSessao().getDataInicio())
						+ " - " + lista.getNome();
				itens.add(new SelectItem(lista.getId(), label));
			}
		}
		idListaJulgamento = -1L;
		return itens;
	}

	public void setIdListaJulgamento(Long idListaJulgamento) {
		this.idListaJulgamento = idListaJulgamento;
	}

	public Long getIdListaJulgamento() {
		return idListaJulgamento;
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
	
	public Boolean getConfirmarProcessosPendentes() {
		return confirmarProcessosPendentes;
	}
	
	public void setConfirmarProcessosPendentes(
			Boolean confirmarProcessosPendentes) {
		this.confirmarProcessosPendentes = confirmarProcessosPendentes;
	}
	
	public Set<ObjetoIncidenteDto> getProcessosParaConfirmacao() {
		return processosParaConfirmacao;
	}
	
	public void setProcessosParaConfirmacao(
			Set<ObjetoIncidenteDto> processosParaConfirmacao) {
		this.processosParaConfirmacao = processosParaConfirmacao;
	}
	public Set<ObjetoIncidenteDto> getProcessosInvalidos() {
		return processosInvalidos;
	}
}
