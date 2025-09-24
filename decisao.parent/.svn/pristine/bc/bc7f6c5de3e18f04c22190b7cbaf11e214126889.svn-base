package br.jus.stf.estf.decisao.objetoincidente.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.configuracao.model.service.ConfiguracaoSistemaService;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoSessaoConstante;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.julgamento.model.service.SessaoService;
import br.gov.stf.estf.processostf.model.service.ListaProcessosService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.objetoincidente.support.DadosAgendamentoDto;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoNaoPodeSerAgendadoException;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoPrecisaDeConfirmacaoException;
import br.jus.stf.estf.decisao.objetoincidente.support.TipoAgendamento;
import br.jus.stf.estf.decisao.objetoincidente.support.TipoColegiadoAgendamento;
import br.jus.stf.estf.decisao.objetoincidente.support.ValidacaoLiberacaoParaJulgamentoException;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaIncidentesDto;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.ActionCallback;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.util.NestedRuntimeException;

/**
 * @author Paulo.Estevao
 * @since 17.10.2011
 */
//DECISAO-2392
//@Action(id = "liberarListaParaJulgamentoActionFacesBean", name = "Liberar Lista para Julgamento", view = "/acoes/objetoincidente/liberarListaParaJulgamento.xhtml")
@Restrict({ActionIdentification.LIBERAR_PARA_JULGAMENTO})
@RequiresResources(Mode.Many)
public class LiberarListaParaJulgamentoActionFacesBean extends ActionSupport<ListaIncidentesDto> {

	@Autowired
	private ListaProcessosService listaProcessosService;
	
	@Qualifier("objetoIncidenteServiceLocal")
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Autowired
	private SessaoService sessaoService;
	
	private TipoColegiadoConstante colegiadoMinistro;
	
	private Date dataJulgamento;
	private String idTipoColegiadoAgendamento;
	private ListaJulgamento listaJulgamento;
	private List<SelectItem> sessoes;
	private List<Sessao> sessoesEmAberto;
	private Long idSessao;
	private Boolean confirmarListasPendentes = false;
	private Boolean existeListaLiberada = false;
	private Boolean existeListaNaoLiberada = false;
	private Boolean sessaoMinistroDiferente = false;
	
	private Set<ListaIncidentesDto> listasInvalidas = new HashSet<ListaIncidentesDto>();
	private Set<ListaIncidentesDto> listasParaConfirmacao = new HashSet<ListaIncidentesDto>();
	
	public void carregarSessoes() {
		try {
			sessoes = new ArrayList<SelectItem>();
			TipoColegiadoConstante colegiado = null;
			colegiado = defineColegiado(idTipoColegiadoAgendamento);
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			sessoesEmAberto = new ArrayList<Sessao>();
			List<Sessao> sessoesAbertasPresencias = sessaoService.pesquisar(colegiado,TipoAmbienteConstante.PRESENCIAL);
			for(Sessao sessao : sessoesAbertasPresencias) {
				String label = formatter.format(sessao.getDataInicio()) + " - " + TipoColegiadoConstante.valueOfSigla(sessao.getColegiado().getId()).getDescricao() + " - " + TipoSessaoConstante.valueOfSigla(sessao.getTipoSessao()).getDescricao();
				sessoes.add(new SelectItem(sessao.getId(), label));
				sessoesEmAberto.add(sessao);
			}
			if (!idTipoColegiadoAgendamento.equalsIgnoreCase("P")){
				if (!colegiadoMinistro.getSigla().equalsIgnoreCase(idTipoColegiadoAgendamento)){
					setSessaoMinistroDiferente(true);;
				}
				else{
					setSessaoMinistroDiferente(false);
				}
			}else{
				setSessaoMinistroDiferente(false);
			}
		} catch (ServiceException e) {
			addError("Erro ao carregar sessões.");
			logger.error("Erro ao carregar sessões.", e);
		}		
	}
	

	/**
	 * @return
	 */
	private TipoColegiadoConstante defineColegiado(String idTipoColegiadoAgendamento) {
		TipoColegiadoConstante colegiado;
		colegiadoMinistro = colegiado = TipoColegiadoConstante.valueOfCodigoCapitulo(
				objetoIncidenteService.defineCodigoDaTurmaDoMinistro(getMinistro(), null));
		
		if (TipoColegiadoAgendamento.PLENARIO.getId().equalsIgnoreCase(idTipoColegiadoAgendamento)) {
			colegiado = TipoColegiadoConstante.SESSAO_PLENARIA;
		} else if (TipoColegiadoAgendamento.PT.getId().equalsIgnoreCase(idTipoColegiadoAgendamento)){
			colegiado = TipoColegiadoConstante.PRIMEIRA_TURMA;
		} else{
			colegiado = TipoColegiadoConstante.SEGUNDA_TURMA;
		}
		return colegiado;
	}

	public void validateAndExecute() {
		for (ListaIncidentesDto dto : getResources()) {
			validarLiberacaoListaParaJulgamento(dto);
		}
		if (hasMessages()) {
			sendToInformations();
		} else {
			execute();
		}
	}
	
	private void validarLiberacaoListaParaJulgamento(ListaIncidentesDto dto) {
		List<String> mensagensProcessosNaoPodemSerAgendados = new ArrayList<String>();
		List<String> mensagensConfirmacao = new ArrayList<String>();
		try {
			ListaProcessos listaProcessos = listaProcessosService.recuperarPorId(dto.getId());
			for (ObjetoIncidente<?> oi : listaProcessos.getElementos()) {
				try {
					validaLiberacaoProcessoParaJulgamento(oi);
				} catch (ValidacaoLiberacaoParaJulgamentoException e) {
					mensagensProcessosNaoPodemSerAgendados.add(
							String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, ObjetoIncidenteDto.valueOf(oi).getIdentificacao())
							+ ": " + e.getMessage() + "\n");
				} catch (ProcessoPrecisaDeConfirmacaoException e) {
					mensagensConfirmacao.add(
							String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, ObjetoIncidenteDto.valueOf(oi).getIdentificacao())
							+ ": " + e.getMessage() + "\n");
				} catch (ProcessoNaoPodeSerAgendadoException e) {
					mensagensProcessosNaoPodemSerAgendados.add(
							String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, ObjetoIncidenteDto.valueOf(oi).getIdentificacao())
							+ ": " + e.getMessage() + "\n");
				} catch (ServiceException e) {
					mensagensProcessosNaoPodemSerAgendados.add(
							String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, ObjetoIncidenteDto.valueOf(oi).getIdentificacao())
							+ ": " + "Erro ao validar liberar para julgamento do processo. " + e.getMessage() + "\n");
				}
			}
			
			if (mensagensProcessosNaoPodemSerAgendados.size() > 0) {
				StringBuffer sb = new StringBuffer();
				for (String mensagem : mensagensProcessosNaoPodemSerAgendados) {
					sb.append(mensagem);
				}
				addError(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, dto.getNome()) + ": Os processos seguintes pertencentes à lista não puderam ser liberados para julgamento: " + sb.toString());
				listasInvalidas.add(dto);
			} else if (mensagensConfirmacao.size() > 0) {
				StringBuffer sb = new StringBuffer();
				for (String mensagem : mensagensConfirmacao) {
					sb.append(mensagem);
				}
				addWarning(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, dto.getNome()) + ": Os processos seguintes pertencentes à lista precisam de confirmação para serem liberados para julgamento: " + sb.toString());
				listasParaConfirmacao.add(dto);
			}
		} catch (ServiceException e) {
			addError(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO, dto.getNome()) + ": Erro ao validar liberação de lista para julgamento.");
			listasInvalidas.add(dto);
		}
	}

	private void validaLiberacaoProcessoParaJulgamento(ObjetoIncidente<?> oi) throws ServiceException, ValidacaoLiberacaoParaJulgamentoException, ProcessoPrecisaDeConfirmacaoException, ProcessoNaoPodeSerAgendadoException {
		objetoIncidenteService.verificaAgendamentoProcesso(oi,
				defineColegiado(idTipoColegiadoAgendamento),
				Agendamento.COD_MATERIA_AGENDAMENTO_JULGAMENTO);
		objetoIncidenteService.verificaProcessoEmSessaoPrevista(oi);
		objetoIncidenteService.verificaProcessoEmListaJulgamentoPrevista(oi);
		objetoIncidenteService.validarProcessoParaAgendamento(montaDadosDoAgendamento(ObjetoIncidenteDto.valueOf(oi)));
		objetoIncidenteService.validarMinistroRelatorOuVistor(oi, getMinistro());
	}
	
	public void execute() {
		if (existeListaSelecionada()) {
			// Limpa as mensagens mostradas anteriormente.
			cleanMessages();
			// Retira as listas inválidas e pendentes de confirmação dos
			// recursos selecionados.
			getResources().removeAll(listasInvalidas);
			// Se os processos pendentes não forem confirmados, devem ser
			// retirados
			// do processamento
			if (!getConfirmarListasPendentes()) {
				getResources().removeAll(listasParaConfirmacao);
			}
			
			execute(new ActionCallback<ListaIncidentesDto>() {
				public void doInAction(ListaIncidentesDto lista) throws Exception {
					try {
						ListaJulgamento listaJulgamento = objetoIncidenteService.liberarListaParaJulgamento(montaDadosDoAgendamento(lista));
						addInformation("A lista de processos ["	+ lista.getNome() + "] foi liberada para julgamento em "
								+ new SimpleDateFormat("dd/MM/yyyy").format(sessaoService.recuperarPorId(idSessao).getDataInicio())
								+ " com o nome de [" + listaJulgamento.getNome() + "].");
						existeListaLiberada = true;
					} catch (ServiceException e) {
						addError(e.getMessage());
						throw new NestedRuntimeException(e);
					}
				}
			});
			
			existeListaNaoLiberada = hasErrors();
			finalizar();
		} else {
			getDefinition().setFacet("nenhumaListaSelecionada");
			getDefinition().setHeight(180);
		}
	}
	
	private boolean existeListaSelecionada() {
		int tamanhoDosRecursos = getResources().size();
		int tamanhoDeExcluidos = listasInvalidas.size();
		if (!getConfirmarListasPendentes()) {
			tamanhoDeExcluidos += listasParaConfirmacao.size();
		}
		return tamanhoDosRecursos > tamanhoDeExcluidos;
	}
	
	public void voltar() {
		getDefinition().setFacet("principal");
	}
	
	public void finalizar() {
		getDefinition().setFacet("final");
	}
	
	private DadosAgendamentoDto montaDadosDoAgendamento(ListaIncidentesDto lista) throws ServiceException {
		DadosAgendamentoDto dadosAgendamento = new DadosAgendamentoDto();
		dadosAgendamento.setMinistro(getMinistro());
		dadosAgendamento.setListaIncidentesDto(lista);
		dadosAgendamento.setTipoAgendamento(TipoAgendamento.INDICE);
		dadosAgendamento.setUsuario(getUsuario());
		dadosAgendamento.setSetorDoUsuario(getSetorMinistro());
		dadosAgendamento.setTipoColegiadoAgendamento(TipoColegiadoAgendamento.getById(getIdTipoColegiadoAgendamento()));
		dadosAgendamento.setSessao(sessaoService.recuperarPorId(idSessao));
		dadosAgendamento.setSessoesEmAberto(sessoesEmAberto);
		return dadosAgendamento;
	}
	
	protected DadosAgendamentoDto montaDadosDoAgendamento(ObjetoIncidenteDto processo) {
		DadosAgendamentoDto dadosAgendamento = new DadosAgendamentoDto();
		dadosAgendamento.setMinistro(getMinistro());
		dadosAgendamento.setObjetoIncidenteDto(processo);
		dadosAgendamento.setTipoAgendamento(TipoAgendamento.INDICE);
		dadosAgendamento.setUsuario(getUsuario());
		dadosAgendamento.setSetorDoUsuario(getSetorMinistro());
		dadosAgendamento.setTipoColegiadoAgendamento(TipoColegiadoAgendamento.getById(getIdTipoColegiadoAgendamento()));
		return dadosAgendamento;
	}
	
	public TipoColegiadoAgendamento[] getTiposColegiadoAgendamento() {
		return TipoColegiadoAgendamento.values();
	}

	public Date getDataJulgamento() {
		return dataJulgamento;
	}

	public void setDataJulgamento(Date dataJulgamento) {
		this.dataJulgamento = dataJulgamento;
	}

	public String getIdTipoColegiadoAgendamento() {
		return idTipoColegiadoAgendamento;
	}

	public void setIdTipoColegiadoAgendamento(String idTipoColegiadoAgendamento) {
		this.idTipoColegiadoAgendamento = idTipoColegiadoAgendamento;
	}
	
	public ListaJulgamento getListaJulgamento() {
		return listaJulgamento;
	}

	public List<SelectItem> getSessoes() {
		return sessoes;
	}
	
	public void setSessoes(List<SelectItem> sessoes) {
		this.sessoes = sessoes;
	}
	
	public Long getIdSessao() {
		return idSessao;
	}
	
	public void setIdSessao(Long idSessao) {
		this.idSessao = idSessao;
	}
	
	public Boolean getConfirmarListasPendentes() {
		return confirmarListasPendentes;
	}
	
	public void setConfirmarListasPendentes(Boolean confirmarListasPendentes) {
		this.confirmarListasPendentes = confirmarListasPendentes;
	}
	
	public Set<ListaIncidentesDto> getListasParaConfirmacao() {
		return listasParaConfirmacao;
	}
	
	public void setListasParaConfirmacao(
			Set<ListaIncidentesDto> listasParaConfirmacao) {
		this.listasParaConfirmacao = listasParaConfirmacao;
	}
	
	public Set<ListaIncidentesDto> getListasInvalidas() {
		return listasInvalidas;
	}
	
	public void setListasInvalidas(Set<ListaIncidentesDto> listasInvalidas) {
		this.listasInvalidas = listasInvalidas;
	}
	
	public Boolean getExisteListaLiberada() {
		return existeListaLiberada;
	}
	
	public void setExisteListaLiberada(Boolean existeListaLiberada) {
		this.existeListaLiberada = existeListaLiberada;
	}
	
	public Boolean getExisteListaNaoLiberada() {
		return existeListaNaoLiberada;
	}
	
	public void setExisteListaNaoLiberada(Boolean existeListaNaoLiberada) {
		this.existeListaNaoLiberada = existeListaNaoLiberada;
	}

	public TipoColegiadoConstante getColegiadoMinistro() {
		return colegiadoMinistro;
	}

	public void setColegiadoMinistro(TipoColegiadoConstante colegiadoMinistro) {
		this.colegiadoMinistro = colegiadoMinistro;
	}

	public Boolean getSessaoMinistroDiferente() {
		return sessaoMinistroDiferente;
	}

	public void setSessaoMinistroDiferente(Boolean sessaoMinistroDiferente) {
		this.sessaoMinistroDiferente = sessaoMinistroDiferente;
	}
}
