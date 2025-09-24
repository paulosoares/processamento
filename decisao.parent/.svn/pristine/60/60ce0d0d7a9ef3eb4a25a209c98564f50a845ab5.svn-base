package br.jus.stf.estf.decisao.objetoincidente.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Embeddable;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.gov.stf.estf.entidade.julgamento.CategoriaEnvolvido;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.Envolvido;
import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.julgamento.PrevisaoSustentacaoOral;
import br.gov.stf.estf.entidade.julgamento.Tema;
import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.Assunto;
import br.gov.stf.estf.entidade.processostf.IncidentePreferencia;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.SituacaoMinistroProcesso;
import br.gov.stf.estf.entidade.processostf.TipoVinculoObjeto;
import br.gov.stf.estf.entidade.processostf.VinculoObjeto;
import br.gov.stf.estf.julgamento.model.service.CategoriaEnvolvidoService;
import br.gov.stf.estf.julgamento.model.service.EnvolvidoService;
import br.gov.stf.estf.julgamento.model.service.InformacaoPautaProcessoService;
import br.gov.stf.estf.julgamento.model.service.TemaService;
import br.gov.stf.estf.jurisdicionado.model.service.JurisdicionadoService;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.service.AgendamentoService;
import br.gov.stf.estf.processostf.model.service.ParteService;
import br.gov.stf.estf.processostf.model.service.VinculoObjetoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.objetoincidente.support.DadosAgendamentoDto;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoNaoPodeSerAgendadoException;
import br.jus.stf.estf.decisao.objetoincidente.support.ProcessoPrecisaDeConfirmacaoException;
import br.jus.stf.estf.decisao.objetoincidente.support.TipoAgendamento;
import br.jus.stf.estf.decisao.objetoincidente.support.TipoColegiadoAgendamento;
import br.jus.stf.estf.decisao.objetoincidente.support.ValidacaoLiberacaoParaJulgamentoException;
import br.jus.stf.estf.decisao.objetoincidente.web.LiberarParaJulgamentoActionFacesBean.ProcessoVinculadoDto.TipoVinculacao;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.CheckRelatorOrRevisorId;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionCallback;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.query.Dto;

/**
 * @author Rodrigo Barreiros
 * @since 26.05.2010
 */
@Action(id = "liberarParaJulgamentoActionFacesBean", name = "Liberar para Julgamento", view = "/acoes/objetoincidente/liberarParaJulgamento.xhtml", height = 600, width = 730)
@Restrict({ActionIdentification.LIBERAR_PARA_JULGAMENTO})
@RequiresResources(Mode.Many)
@CheckRelatorOrRevisorId
public class LiberarParaJulgamentoActionFacesBean extends ActionSupport<ObjetoIncidenteDto> {

	private String idTipoAgendamento;
	private String idTipoColegiadoAgendamento;
	private String observacao;
	private Boolean confirmarProcessosPendentes = false;
	private Boolean sessaoMinistroDiferente = false;
	private Set<ObjetoIncidenteDto> processosInvalidos = new HashSet<ObjetoIncidenteDto>();
	private Set<ObjetoIncidenteDto> processosParaConfirmacao = new HashSet<ObjetoIncidenteDto>();

	// Novos campos
	private Date dataJulgamento;
	private String observacaoDataJulgamento;
	private List<Assunto> assuntos;
	private String identificacaoProcessoVinculado;
	private ObjetoIncidenteDto novoProcessoVinculado;
	private TipoVinculacao tipoVinculacao;
	private List<ProcessoVinculadoDto> processosVinculados;
	private Long idParteSustentacaoOral;
	private List<Parte> partesProcesso;
	private String nomeAdvogado;
	private AdvogadoSustentacaoOral advogadoSustentacaoOral;
	private String observacaoSustentacaoOral;
	private List<PrevisaoSustentacaoOralDto> sustentacoesOrais;
	private Boolean repercussaoGeral;
	private String temaRepercussaoGeral;
	private Date dataLiberacao;
	private Date dataDistribuicao;
	private String preferencias;
	private InformacaoPautaProcesso informacaoPautaProcesso;
	private ObjetoIncidente<?> objetoIncidente;
	private Ministro ministroRelator;
	private Boolean liberarVariosProcessos;
	private Boolean pautaExtra;

	@Qualifier("objetoIncidenteServiceLocal")
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Autowired
	private ParteService parteService;
	
	@Autowired
	private InformacaoPautaProcessoService informacaoPautaProcessoService;
	
	@Autowired
	private AgendamentoService agendamentoService;
	
	@Autowired
	private TemaService temaService;
	
	@Autowired
	private JurisdicionadoService jurisdicionadoService;
	
	@Autowired
	private EnvolvidoService envolvidoService;
	
	@Autowired
	private CategoriaEnvolvidoService categoriaEnvolvidoService;
	
	@Autowired
	private MinistroService ministroService;
	
	@Autowired
	private VinculoObjetoService vinculoObjetoService;
	
	@Override
	public void load() {
		if ( existeProcessoSelecionado() ){
			try {
				// Validações
				for (ObjetoIncidenteDto dto : getResources()) {
					ObjetoIncidente<?> oi = objetoIncidenteService.recuperarObjetoIncidentePorId(dto.getId());
					validaMinistro(oi);
					verificarAgendamento(oi);
					validaExigenciaDeRelatorio(oi);
				}
				
				if (getResources().size() <= 1) {
					objetoIncidente = objetoIncidenteService.recuperarObjetoIncidentePorId(((ObjetoIncidenteDto) getResources().iterator().next()).getId());
					Hibernate.initialize(objetoIncidente.getPrincipal());
					Hibernate.initialize(((Processo) objetoIncidente.getPrincipal()).getAssuntos());
					assuntos = ((Processo) objetoIncidente.getPrincipal()).getAssuntos();
					repercussaoGeral = Boolean.FALSE;
					
					setIdentificacaoProcessoVinculado(null);
					setNovoProcessoVinculado(null);
					setAdvogadoSustentacaoOral(null);
					setIdParteSustentacaoOral(null);
					setNomeAdvogado(null);
					setObservacao(null);
					setObservacaoDataJulgamento(null);
					setObservacaoSustentacaoOral(null);
					setTipoVinculacao(TipoVinculacao.JULGAMENTO_CONJUNTO);
					
					// Recupera informação pauta processo
					informacaoPautaProcesso = informacaoPautaProcessoService.recuperar(objetoIncidente);
					
					if (informacaoPautaProcesso != null) {
						// Preenche os campos do formulário referentes ao controle de pauta
						dataJulgamento = informacaoPautaProcesso.getDataJulgamentoSugerida();
						observacaoDataJulgamento = informacaoPautaProcesso.getObservacaoDataSugerida();
						observacao = informacaoPautaProcesso.getObservacao();
						sustentacoesOrais = montarListaSustentacoesOrais(informacaoPautaProcesso);
						repercussaoGeral = informacaoPautaProcesso.getRepercussaoGeral();
						pautaExtra = informacaoPautaProcesso.getPautaExtra();
					}
					
					// Recupera informações de repercussão geral
					Tema tema = temaService.recuperarTemas(objetoIncidente.getId());
					if (tema != null) {
						repercussaoGeral = Boolean.TRUE;
						temaRepercussaoGeral = tema.getTituloTema() + " - " + tema.getDescricao();
					}
					carregarPreferencias();
					carregarDataDistribuicao();
					carregarProcessosVinculados(objetoIncidente);
				}	
				
			} catch (Exception e) {
				addError(e.getMessage());
				logger.error(e.getMessage(), e);
			}
		} else {
			getDefinition().setFacet("nenhumProcessoSelecionado");
			getDefinition().setHeight(180);
			getDefinition().setWidth(300);
		}
		
		if ( hasWarnings() ) {
			sendToValidacao();
		}
		
		if ( hasErrors() ) {
			sendToErrors();
		}
	}

	private void sendToValidacao() {
		getDefinition().setFacet("validacao");
		getDefinition().setHeight(215);
		cleanMessages();
	}

	private void validaExigenciaDeRelatorio(ObjetoIncidente<?> objetoIncidente) {
		try {
			objetoIncidenteService.verificaExigenciaDeRelatorioVoto(objetoIncidente, getMinistro(), false);
		} catch (ProcessoNaoPodeSerAgendadoException e) {
			addWarning(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO + ": %s ",
					ObjetoIncidenteDto.valueOf(objetoIncidente).getIdentificacao(), 
					e.getMessage()));
		}
		
	}

	private void carregarProcessosVinculados(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		List<ObjetoIncidente<?>> julgamentoConjunto = informacaoPautaProcessoService.recuperarProcessosJulgamentoConjunto(objetoIncidente, false);
		List<VinculoObjeto> vinculosDependencia = vinculoObjetoService.pesquisarPorVinculador(objetoIncidente, TipoVinculoObjeto.DEPENDE_DO_JULGAMENTO);
		
		for (ObjetoIncidente<?> oi : julgamentoConjunto) {
			ProcessoVinculadoDto pv = new ProcessoVinculadoDto();
			InformacaoPautaProcesso ipp = informacaoPautaProcessoService.recuperar(oi);
			pv.setIdListaJulgamentoConjunto(ipp.getSeqListaJulgamentoConjunto());
			pv.setVinculacao(TipoVinculacao.JULGAMENTO_CONJUNTO);
			pv.setObjetoIncidente(ObjetoIncidenteDto.valueOf(oi));
			getProcessosVinculados().add(pv);
		}
		
		for (VinculoObjeto vinculo : vinculosDependencia) {
			ProcessoVinculadoDto pv = new ProcessoVinculadoDto();
			pv.setObjetoIncidente(ObjetoIncidenteDto.valueOf(vinculo.getObjetoIncidente()));
			pv.setVinculacao(TipoVinculacao.DEPENDE_DO_JULGAMENTO);
			pv.setIdVinculoObjeto(vinculo.getId());
			getProcessosVinculados().add(pv);
		}
		
		Collections.sort(getProcessosVinculados(), new Comparator<ProcessoVinculadoDto>() {

			@Override
			public int compare(ProcessoVinculadoDto o1, ProcessoVinculadoDto o2) {
				return o1.getObjetoIncidente().getIdentificacao().compareToIgnoreCase(o2.getObjetoIncidente().getIdentificacao());
			}
			
		});
	}

	private void carregarDataDistribuicao() {
		SituacaoMinistroProcesso distribuicao;
		try {
			distribuicao = objetoIncidenteService.recuperarDistribuicaoProcesso(objetoIncidente.getPrincipal());
			if ( distribuicao != null ) {
				dataDistribuicao = distribuicao.getDataOcorrencia();
			}
		} catch (ServiceException e) {
			logger.warn(e.getMessage(), e);
		}
	}

	private List<PrevisaoSustentacaoOralDto> montarListaSustentacoesOrais(
			InformacaoPautaProcesso informacaoPautaProcesso) {
		List<PrevisaoSustentacaoOralDto> lista = new ArrayList<PrevisaoSustentacaoOralDto>();
		if (informacaoPautaProcesso.getSustentacoesOrais() != null) {
			for (PrevisaoSustentacaoOral pso : informacaoPautaProcesso.getSustentacoesOrais()) {
				lista.add(PrevisaoSustentacaoOralDto.valueOf(pso));
			}
		}
		return lista;
	}

	private void carregarPreferencias() {
		// Preferências
		List<IncidentePreferencia> listaPreferencias = ((Processo) objetoIncidente.getPrincipal()).getIncidentePreferencia();
		StringBuffer pref = new StringBuffer();
		for (IncidentePreferencia preferencia : listaPreferencias) {
			if (listaPreferencias.indexOf(preferencia) > 0) {
				if (listaPreferencias.indexOf(preferencia) == listaPreferencias.size() - 1) {
					pref.append(" e ");
				} else {
					pref.append(", ");
				}
			}
			pref.append(preferencia.getTipoPreferencia().getDescricao());
		}
		preferencias = pref.toString();
	}

	private void verificarAgendamento(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		List<Agendamento> agendamentos = agendamentoService.pesquisar(objetoIncidente);
		if (agendamentos != null && agendamentos.size() > 0) {
			addWarning(String.format(
					MENSAGEM_ERRO_EXECUCAO_ACAO + ": %s ", 
					ObjetoIncidenteDto.valueOf(objetoIncidente).getIdentificacao(),
					"Processo já está liberado para julgamento. Efetue o cancelamento da liberação para julgamento antes de fazer nova liberação."));
		}
	}

	private void validaMinistro(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		// Verifica se o ministro que está executando a ação é o ministro relator do processo/incidente
		// Modificar para buscar o relator do incidente
		ministroRelator = objetoIncidenteService.recuperarMinistroRelatorIncidente(objetoIncidente);
		Ministro ministroRevisor = objetoIncidenteService.recuperarMinistroRevisorIncidente(objetoIncidente);
		if (ministroRelator != null && !ministroRelator.getId().equals(getMinistro().getId())
				&& ministroRevisor != null && !ministroRevisor.getId().equals(getMinistro().getId())) {
			addWarning(String.format(
					MENSAGEM_ERRO_EXECUCAO_ACAO + ": %s ", 
					ObjetoIncidenteDto.valueOf(objetoIncidente).getIdentificacao(),
					"Ministro do setor não é relator ou revisor do processo."));
		}
	}

	/**
	 * Grava informações de pauta
	 */
	public void gravarInformacaoPauta() {
		try {
			if (informacaoPautaProcesso != null) {
				informacaoPautaProcesso = informacaoPautaProcessoService.recuperarPorId(informacaoPautaProcesso.getId());
			} else {
				informacaoPautaProcesso = new InformacaoPautaProcesso();
				informacaoPautaProcesso.setObjetoIncidente(objetoIncidente);
			}
			
			informacaoPautaProcesso.setObservacao(getObservacao());
			informacaoPautaProcesso.setRepercussaoGeral(getRepercussaoGeral());
			informacaoPautaProcesso.setDataJulgamentoSugerida(getDataJulgamento());
			
			informacaoPautaProcesso = informacaoPautaProcessoService.salvar(informacaoPautaProcesso);
			
			objetoIncidenteService.gravarSustentacoesOrais(informacaoPautaProcesso, getSustentacoesOrais());
		} catch (ServiceException e) {
			addError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		
		if (hasErrors()) {
			sendToErrors();
		}
	}

	/**
	 * Executa as regras para agendamento de processos.
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
			execute(new ActionCallback<ObjetoIncidenteDto>() {
				public void doInAction(ObjetoIncidenteDto objetoIncidente) throws Exception {
					objetoIncidenteService.salvarAgendamentoProcesso(montaDadosDoAgendamento(objetoIncidente));
				}
			});
		} else {
			getDefinition().setFacet("nenhumProcessoSelecionado");
			getDefinition().setHeight(180);
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
	
	/**
	 * 
	 */
	public void incluirProcessoVinculado() {
		try {
			ProcessoVinculadoDto dto = new ProcessoVinculadoDto();
			if (getNovoProcessoVinculado() != null && getNovoProcessoVinculado().getIdentificacao().equals(getIdentificacaoProcessoVinculado().trim())) {
				Ministro relatorIncidente = ministroService.recuperarMinistroRelatorIncidente(objetoIncidenteService.recuperarObjetoIncidentePorId(getNovoProcessoVinculado().getId()));
				
				// Verifica se o usuário está tentando vincular um processo a ele mesmo
				if (!getNovoProcessoVinculado().getId().equals(objetoIncidente.getId())) {
					boolean processoJaVinculado = isProcessoVinculado(getNovoProcessoVinculado(), getProcessosVinculados());
					
					// Verifica se o processo já encontra-se na lista de processos vinculados
					if (!processoJaVinculado) {
						
						// Verifica se o setor que está realizando a vinculação é o relator do processo que está sendo vinculado
						if (getMinistro().getId().equals(relatorIncidente.getId())) {
							dto.setObjetoIncidente(getNovoProcessoVinculado());
							dto.setVinculacao(getTipoVinculacao());
							getProcessosVinculados().add(dto);
							if (TipoVinculacao.JULGAMENTO_CONJUNTO.equals(getTipoVinculacao())) {
								List<ObjetoIncidenteDto> processosJulgamentoConjunto = objetoIncidenteService.recuperarProcessosJulgamentoConjunto(getNovoProcessoVinculado());
								
								for (ObjetoIncidenteDto oi : processosJulgamentoConjunto) {
									if (!isProcessoVinculado(oi, getProcessosVinculados())) {
										ProcessoVinculadoDto pv = new ProcessoVinculadoDto();
										pv.setObjetoIncidente(oi);
										pv.setVinculacao(TipoVinculacao.JULGAMENTO_CONJUNTO);
										pv.setIdListaJulgamentoConjunto(informacaoPautaProcessoService.recuperar(objetoIncidenteService.recuperarObjetoIncidentePorId(oi.getId())).getSeqListaJulgamentoConjunto());
										getProcessosVinculados().add(pv);
										addInformation("O processo "
												+ oi.getIdentificacao()
												+ " está sendo vinculado como julgamento conjunto porque possui o mesmo tipo de vínculo com o processo "
												+ getNovoProcessoVinculado().getIdentificacao()
												+ ".");
									}
								}
								
//								List<VinculoObjeto> listaVinculosObjetoGravados = vinculoObjetoService.pesquisarPorVinculador(
//												objetoIncidenteService.recuperarObjetoIncidentePorId(getNovoProcessoVinculado().getId()),
//												TipoVinculoObjeto.DEPENDE_DO_JULGAMENTO);
//								for (VinculoObjeto vinculo : listaVinculosObjetoGravados) {
//									Hibernate.initialize(vinculo.getObjetoIncidente());
//									ObjetoIncidenteDto oi = ObjetoIncidenteDto.valueOf(vinculo.getObjetoIncidente());
//									if (!isProcessoVinculado(oi, getProcessosVinculados())) {
//										ProcessoVinculadoDto pv = new ProcessoVinculadoDto();
//										pv.setObjetoIncidente(oi);
//										pv.setVinculacao(TipoVinculacao.DEPENDE_DO_JULGAMENTO);
//										getProcessosVinculados().add(pv);
//										addInformation("O processo "
//												+ oi.getIdentificacao()
//												+ " está sendo vinculado como depende do julgamento porque possui o mesmo tipo de vínculo com o processo "
//												+ getNovoProcessoVinculado().getIdentificacao()
//												+ ".");
//									}
//								}
								

							}
							setNovoProcessoVinculado(null);
							setIdentificacaoProcessoVinculado(null);
						} else {
							addWarning("Relator do processo não é ministro do gabinete.");
						}
					} else {
						addWarning("O processo selecionado já encontra-se na lista de processos vinculados.");
					}
				} else {
					addWarning("Não é possível vincular um processo a ele mesmo.");
				}
			} else {
				addWarning("É necessário selecionar um processo para vinculação.");
			} 
		} catch (ServiceException e) {
			addError("Erro ao recuperar relator do processo.");
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @return
	 */
	private boolean isProcessoVinculado(ObjetoIncidenteDto objetoIncidente, List<ProcessoVinculadoDto> processosVinculados) {
		boolean processoJaVinculado = false;
		for (ProcessoVinculadoDto pv : processosVinculados) {
			if (pv.getObjetoIncidente().equals(objetoIncidente)) {
				processoJaVinculado = true;
			}
		}
		return processoJaVinculado;
	}
	
	/**
	 * Exclui os processos vinculados selecionados da lista de processos vinculados.
	 */
	public void excluirProcessosVinculados() {
		Collection<ProcessoVinculadoDto> paraRetirar = new ArrayList<ProcessoVinculadoDto>();
		for (ProcessoVinculadoDto dto : getProcessosVinculados()) {
			if (dto.isSelected()) {
				paraRetirar.add(dto);
			}
		}
		getProcessosVinculados().removeAll(paraRetirar);
	}
	
	/**
	 * Verifica se o colegiado selecionado para liberar o processo é diferente do 
	 * ministro cujo o gabinete o usuário está lotado. Se for o sistema exibirá 
	 * uma mensagem de alerta. Se o cliente selecionar plenário, a mensagem não
	 * será exibida.
	 */
	public void verificarSessaoMinistro(){
		TipoColegiadoConstante colegiado;
		colegiado = TipoColegiadoConstante.valueOfCodigoCapitulo(
				objetoIncidenteService.defineCodigoDaTurmaDoMinistro(getMinistro(), null));
		if (!idTipoColegiadoAgendamento.equalsIgnoreCase("P")){
			if (!colegiado.getSigla().equalsIgnoreCase(idTipoColegiadoAgendamento)){
				setSessaoMinistroDiferente(true);
			}
			else{
				setSessaoMinistroDiferente(false);
			}			
		}else{
			setSessaoMinistroDiferente(false);
		}
	}
	
	/**
	 * Seleciona todos os processos vinculados da lista.
	 */
	public void selectAllProcessosVinculados() {
		boolean check = !allChecked(getProcessosVinculados());
		for (ProcessoVinculadoDto dto : getProcessosVinculados()) {
			dto.setSelected(check);
		}
	}
	
	/**
	 * Inclui uma sustentação oral prevista para o julgamento do processo.
	 */
	public void incluirSustentacaoOral() {
		try {
			PrevisaoSustentacaoOralDto dto = new PrevisaoSustentacaoOralDto();
			if (getAdvogadoSustentacaoOral() != null) {
				if (getAdvogadoSustentacaoOral().getJurisdicionado() != null) {
					dto.setIdAdvogado(getAdvogadoSustentacaoOral().getJurisdicionado().getId());
					dto.setNomeAdvogado(getAdvogadoSustentacaoOral().getJurisdicionado().getNome());
				}
				if (getAdvogadoSustentacaoOral().getEnvolvido() != null) {
					dto.setIdEnvolvido(getAdvogadoSustentacaoOral().getEnvolvido().getId());
					dto.setNomeAdvogado(getAdvogadoSustentacaoOral().getEnvolvido().getNome());
				}
			} else {
				Envolvido envolvido = inserirEnvolvido(getNomeAdvogado());
				dto.setIdEnvolvido(envolvido.getId());
				dto.setNomeAdvogado(envolvido.getNome());
			}
			dto.setIdParte(getIdParteSustentacaoOral());
			dto.setNomeParte(jurisdicionadoService.recuperarPorId(getIdParteSustentacaoOral()).getNome());
			dto.setObservacao(getObservacaoSustentacaoOral());
			getSustentacoesOrais().add(dto);
			setObservacaoSustentacaoOral(null);
			setAdvogadoSustentacaoOral(null);
			setIdParteSustentacaoOral(null);
			setNomeAdvogado(null);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			addError(e.getMessage());
		}
		
	}
	
	/**
	 * Insere um novo Envolvido.
	 * @param nomeAdvogado Nome do advogado que será inserido na tabela de envolvidos.
	 * @return Entidade Envolvido inserida.
	 * @throws ServiceException
	 */
	private Envolvido inserirEnvolvido(String nomeAdvogado) throws ServiceException {
		Envolvido entidade = new Envolvido();
		entidade.setCategoriaEnvolvido(categoriaEnvolvidoService.recuperarPorId(CategoriaEnvolvido.ADVOGADO));
		entidade.setNome(nomeAdvogado.toUpperCase());
		entidade = envolvidoService.incluir(entidade);
		return entidade;
	}

	/**
	 * Excluir as sustentações orais selecionadas da lista de sustentações orais previstas para o julgamento do processo.
	 */
	public void excluirSustentacaoOral() {
		Collection<PrevisaoSustentacaoOralDto> paraRetirar = new ArrayList<PrevisaoSustentacaoOralDto>();
		for (PrevisaoSustentacaoOralDto dto : getSustentacoesOrais()) {
			if (dto.isSelected()) {
				paraRetirar.add(dto);
			}
		}
		getSustentacoesOrais().removeAll(paraRetirar);
		
	}
	
	/**
	 * Seleciona todas as sustentações orais previstas para o julgamento do processo.
	 */
	public void selectAllSustentacoesOrais() {
		boolean check = !allChecked(getSustentacoesOrais());
		for (PrevisaoSustentacaoOralDto dto : getSustentacoesOrais()) {
			dto.setSelected(check);
		}
	}
	
	/**
	 * Verifica se todos os itens da lista de dtos estão selecionados.
	 * @param lista
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private boolean allChecked(List lista) {
    	for (Object dto : lista) {
    		if (!((Dto) dto).isSelected()) {
    			return false;
    		}
    	}
    	return true;
    }
	
	/**
	 * Busca os advogados (Jurisdicionados e Envolvidos), pelo nome, a partir de uma sugestão.
	 * @param suggestion
	 * @return
	 */
	public List<AdvogadoSustentacaoOral> searchAdvogado(Object suggestion) {
		try {
			return objetoIncidenteService.pesquisarAdvogados(suggestion.toString());
		} catch (ServiceException e) {
			addError(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		return new ArrayList<AdvogadoSustentacaoOral>();
	}

	public TipoAgendamento[] getTiposDeAgendamento() {
		return TipoAgendamento.values();
	}
	
	public TipoVinculacao[] getTiposVinculacao() {
		return TipoVinculacao.values();
	}

	public TipoColegiadoAgendamento[] getTiposColegiadoAgendamento() {
		return TipoColegiadoAgendamento.values();
	}

	public void validateAndExecute() {
		if (idTipoAgendamento == null || idTipoAgendamento.length() == 0 || idTipoColegiadoAgendamento == null || idTipoColegiadoAgendamento.length() == 0) {
			if (idTipoAgendamento == null || idTipoAgendamento.length() == 0) {
				addWarning("É necessário selecionar o Destino.");
			}
			if (idTipoColegiadoAgendamento == null || idTipoColegiadoAgendamento.length() == 0) {
				addWarning("É necessário selecionar o Colegiado.");
			}
		} else {
			for (ObjetoIncidenteDto processo : getResources()) {
				validaProcessoParaAgendamento(processo);
			}
			// Valida processos vinculados para julgamento conjunto
			for (ProcessoVinculadoDto pv : getProcessosVinculados()) {
				if (TipoVinculacao.JULGAMENTO_CONJUNTO.equals(pv.getVinculacao())) {
					validaProcessoParaAgendamento(pv.getObjetoIncidente());
				}
			}
			if (precisaApresentarConfirmacao()) {
				sendToInformations();
				if (processosInvalidos.size() > 0 && processosParaConfirmacao.size() > 0) {
					getDefinition().setHeight(450);
				} else {
					getDefinition().setHeight(280);
				}
			} else {
				execute();
			}
		}
	}

	protected void validaProcessoParaAgendamento(ObjetoIncidenteDto processo) {
		try {
			ObjetoIncidente<?> oi = objetoIncidenteService.recuperarObjetoIncidentePorId(processo.getId());
			objetoIncidenteService.verificaAgendamentoProcesso(oi,
					defineColegiado(idTipoColegiadoAgendamento),
					TipoAgendamento.getById(idTipoAgendamento).getCodigoMateria());
			objetoIncidenteService.verificaProcessoEmSessaoPrevista(oi);
			objetoIncidenteService.verificaProcessoEmListaJulgamentoPrevista(oi);
			DadosAgendamentoDto dadosAgendamento = montaDadosDoAgendamento(processo);
			objetoIncidenteService.validarProcessoParaAgendamento(dadosAgendamento);
		} catch (ProcessoPrecisaDeConfirmacaoException e) {
			insereProcessoPendenteDeValidacao(processo, e);
		} catch (ProcessoNaoPodeSerAgendadoException e) {
			insereProcessoInvalidoParaAgendamento(processo, e);
		} catch (ValidacaoLiberacaoParaJulgamentoException e) {
			insereProcessoInvalidoParaAgendamento(processo, e);
		} catch (Exception e) {
			logger.error(String.format("Erro ao validar liberação para julgamento do processo [%s]", processo.getIdentificacao()),
					e);
			insereProcessoInvalidoParaAgendamento(processo, MENSAGEM_ERRO_NAO_ESPECIFICADO);
		}
	}
	
	/**
	 * @return
	 */
	private TipoColegiadoConstante defineColegiado(String idTipoColegiadoAgendamento) {
		TipoColegiadoConstante colegiado;
		if (TipoColegiadoAgendamento.PLENARIO.getId().equalsIgnoreCase(idTipoColegiadoAgendamento)) {
			colegiado = TipoColegiadoConstante.SESSAO_PLENARIA;
		} else {
			colegiado = TipoColegiadoConstante.valueOfCodigoCapitulo(
					objetoIncidenteService.defineCodigoDaTurmaDoMinistro(getMinistro(), null));
		}
		return colegiado;
	}

	protected DadosAgendamentoDto montaDadosDoAgendamento(ObjetoIncidenteDto processo) {
		DadosAgendamentoDto dadosAgendamento = new DadosAgendamentoDto();
		dadosAgendamento.setMinistro(getMinistro());
		dadosAgendamento.setObjetoIncidenteDto(processo);
		dadosAgendamento.setTipoAgendamento(TipoAgendamento.getById(getIdTipoAgendamento()));
		dadosAgendamento.setUsuario(getUsuario());
		dadosAgendamento.setSetorDoUsuario(getSetorMinistro());
		dadosAgendamento.setObservacao(getObservacao());
		dadosAgendamento.setTipoColegiadoAgendamento(TipoColegiadoAgendamento.getById(getIdTipoColegiadoAgendamento()));
		if (getProcessosVinculados().size() > 0) {
			List<ObjetoIncidenteDto> julgamentoConjunto = new ArrayList<ObjetoIncidenteDto>();
			// Precedentes são os vinculados do tipo Depende de
			// O vinculador é o objeto dependente
			List<ObjetoIncidenteDto> precedentes = new ArrayList<ObjetoIncidenteDto>();
			for (ProcessoVinculadoDto pv : getProcessosVinculados()) {
				if (TipoVinculacao.JULGAMENTO_CONJUNTO.equals(pv.getVinculacao())) {
					julgamentoConjunto.add(pv.getObjetoIncidente());
				} else {
					precedentes.add(pv.getObjetoIncidente());
				}
			}
			dadosAgendamento.setJulgamentoConjunto(julgamentoConjunto);
			dadosAgendamento.setPrecedentes(precedentes);
		}
		dadosAgendamento.setSustentacoesOrais(getSustentacoesOrais());
		dadosAgendamento.setDataJulgamentoSugerida(getDataJulgamento());
		dadosAgendamento.setObservacaoDataJulgamento(getObservacaoDataJulgamento());
		dadosAgendamento.setRepercussaoGeral(getRepercussaoGeral());
		dadosAgendamento.setPautaExtra(getPautaExtra());
		return dadosAgendamento;
	}

	private boolean precisaApresentarConfirmacao() {
		return processosInvalidos.size() > 0 || processosParaConfirmacao.size() > 0;
	}

	private void insereProcessoInvalidoParaAgendamento(ObjetoIncidenteDto processo,
			ProcessoNaoPodeSerAgendadoException e) {
		insereProcessoInvalidoParaAgendamento(processo, e.getMessage());
	}
	
	private void insereProcessoInvalidoParaAgendamento(ObjetoIncidenteDto processo,
			ValidacaoLiberacaoParaJulgamentoException e) {
		insereProcessoInvalidoParaAgendamento(processo, e.getMessage());
	}

	private void insereProcessoInvalidoParaAgendamento(ObjetoIncidenteDto processo, String motivo) {
		processosInvalidos.add(processo);
		addError(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO + ": %s ", processo.getIdentificacao(), motivo));
	}

	private void insereProcessoPendenteDeValidacao(ObjetoIncidenteDto processo, ProcessoPrecisaDeConfirmacaoException e) {
		addWarning(String.format(MENSAGEM_ERRO_EXECUCAO_ACAO + ": %s ", processo.getIdentificacao(), e.getMessage()));
		processosParaConfirmacao.add(processo);
	}

	public void voltar() {
		getDefinition().setFacet("principal");
		getDefinition().setHeight(300);
		limpaListasDeProcessos();
	}
	

	private void limpaListasDeProcessos() {
		processosInvalidos.clear();
		processosParaConfirmacao.clear();
	}

	@Override
	protected String getErrorTitle() {
		return "Os processos abaixo apresentaram problemas de processamento, e a operação não foi realizada para eles:";
	}

	/**
	 * Verifica se os processos selecionados são maiores do que a quantidade de processos que será excluída pela validação.
	 * @return True se houver algum recurso para ser processado. False caso contrário.
	 */
	public Boolean getMostrarBotaoDeConfirmacao() {
		int tamanhoDosRecursos = getResources().size();
		int tamanhoDeExcluidos = processosInvalidos.size();
		if (!getConfirmarProcessosPendentes()) {
			// Caso não marque os agendamentos pendentes, eles serão retirados.
			tamanhoDeExcluidos += processosParaConfirmacao.size();
		}
		return tamanhoDosRecursos > tamanhoDeExcluidos;
	}
	
	public Date getDataJulgamento() {
		return dataJulgamento;
	}

	public void setDataJulgamento(Date dataJulgamento) {
		this.dataJulgamento = dataJulgamento;
	}

	public String getObservacaoDataJulgamento() {
		return observacaoDataJulgamento;
	}

	public void setObservacaoDataJulgamento(String observacaoDataJulgamento) {
		this.observacaoDataJulgamento = observacaoDataJulgamento;
	}

	public List<Assunto> getAssuntos() {
		return assuntos;
	}

	public void setAssuntos(List<Assunto> assuntos) {
		this.assuntos = assuntos;
	}

	public String getIdentificacaoProcessoVinculado() {
		return identificacaoProcessoVinculado;
	}

	public void setIdentificacaoProcessoVinculado(String identificacaoProcessoVinculado) {
		this.identificacaoProcessoVinculado = identificacaoProcessoVinculado;
	}

	public ObjetoIncidenteDto getNovoProcessoVinculado() {
		return novoProcessoVinculado;
	}

	public void setNovoProcessoVinculado(ObjetoIncidenteDto novoProcessoVinculado) {
		this.novoProcessoVinculado = novoProcessoVinculado;
	}

	public TipoVinculacao getTipoVinculacao() {
		return tipoVinculacao;
	}

	public void setTipoVinculacao(TipoVinculacao tipoVinculacao) {
		this.tipoVinculacao = tipoVinculacao;
	}

	public List<ProcessoVinculadoDto> getProcessosVinculados() {
		if (processosVinculados == null) {
			processosVinculados = new ArrayList<ProcessoVinculadoDto>();
		}
		return processosVinculados;
	}

	public void setProcessosVinculados(
			List<ProcessoVinculadoDto> processosVinculados) {
		this.processosVinculados = processosVinculados;
	}

	public Long getIdParteSustentacaoOral() {
		return idParteSustentacaoOral;
	}

	public void setIdParteSustentacaoOral(Long idParteSustentacaoOral) {
		this.idParteSustentacaoOral = idParteSustentacaoOral;
	}

	public List<Parte> getPartesProcesso() {
		if (partesProcesso == null) {
			try {
				partesProcesso = parteService.pesquisarPartes(((ObjetoIncidenteDto) getResources().iterator().next()).getId());
			} catch (ServiceException e) {
				addError("Erro ao pesquisar partes do processo.");
			}
		}
		return partesProcesso;
	}

	public void setPartesProcesso(List<Parte> partesProcesso) {
		this.partesProcesso = partesProcesso;
	}

	public String getNomeAdvogado() {
		return nomeAdvogado;
	}

	public void setNomeAdvogado(String nomeAdvogado) {
		this.nomeAdvogado = nomeAdvogado;
	}

	public AdvogadoSustentacaoOral getAdvogadoSustentacaoOral() {
		return advogadoSustentacaoOral;
	}

	public void setAdvogadoSustentacaoOral(AdvogadoSustentacaoOral advogadoSustentacaoOral) {
		this.advogadoSustentacaoOral = advogadoSustentacaoOral;
	}

	public String getObservacaoSustentacaoOral() {
		return observacaoSustentacaoOral;
	}

	public void setObservacaoSustentacaoOral(String observacaoSustentacaoOral) {
		this.observacaoSustentacaoOral = observacaoSustentacaoOral;
	}

	public List<PrevisaoSustentacaoOralDto> getSustentacoesOrais() {
		if (sustentacoesOrais == null) {
			sustentacoesOrais = new ArrayList<PrevisaoSustentacaoOralDto>();
		}
		return sustentacoesOrais;
	}

	public void setSustentacoesOrais(List<PrevisaoSustentacaoOralDto> sustentacoesOrais) {
		this.sustentacoesOrais = sustentacoesOrais;
	}

	public Date getDataLiberacao() {
		return dataLiberacao;
	}

	public void setDataLiberacao(Date dataLiberacao) {
		this.dataLiberacao = dataLiberacao;
	}

	public Date getDataDistribuicao() {
		return dataDistribuicao;
	}

	public void setDataDistribuicao(Date dataDistribuicao) {
		this.dataDistribuicao = dataDistribuicao;
	}

	public String getPreferencias() {
		return preferencias;
	}

	public void setPreferencias(String preferencias) {
		this.preferencias = preferencias;
	}

	public Set<ObjetoIncidenteDto> getProcessosParaConfirmacao() {
		return processosParaConfirmacao;
	}

	public void setProcessosParaConfirmacao(Set<ObjetoIncidenteDto> textosParaConfirmacao) {
		this.processosParaConfirmacao = textosParaConfirmacao;
	}

	public Set<ObjetoIncidenteDto> getProcessosInvalidos() {
		return processosInvalidos;
	}

	public void setProcessosInvalidos(Set<ObjetoIncidenteDto> textosInvalidos) {
		this.processosInvalidos = textosInvalidos;
	}

	public Boolean getConfirmarProcessosPendentes() {
		return confirmarProcessosPendentes;
	}

	public void setConfirmarProcessosPendentes(Boolean confirmarProcessosPendentes) {
		this.confirmarProcessosPendentes = confirmarProcessosPendentes;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getIdTipoColegiadoAgendamento() {
		return idTipoColegiadoAgendamento;
	}

	public void setIdTipoColegiadoAgendamento(String idTipoColegiadoAgendamento) {
		this.idTipoColegiadoAgendamento = idTipoColegiadoAgendamento;
	}

	public String getIdTipoAgendamento() {
		return idTipoAgendamento;
	}

	public void setIdTipoAgendamento(String idTipoAgendamento) {
		this.idTipoAgendamento = idTipoAgendamento;
	}

	public Boolean getRepercussaoGeral() {
		if (repercussaoGeral == null) {
			repercussaoGeral = Boolean.FALSE;
		}
		return repercussaoGeral;
	}

	public void setRepercussaoGeral(Boolean repercussaoGeral) {
		this.repercussaoGeral = repercussaoGeral;
	}
	
	public Boolean getPautaExtra() {
		if (pautaExtra == null) {
			pautaExtra = Boolean.FALSE;
		}
		return pautaExtra;
	}
	
	public void setPautaExtra(Boolean pautaExtra) {
		this.pautaExtra = pautaExtra;
	}

	public String getTemaRepercussaoGeral() {
		return temaRepercussaoGeral;
	}

	public void setTemaRepercussaoGeral(String temaRepercussaoGeral) {
		this.temaRepercussaoGeral = temaRepercussaoGeral;
	}
	
	public InformacaoPautaProcesso getInformacaoPautaProcesso() {
		return informacaoPautaProcesso;
	}
	
	public Ministro getMinistroRelator() {
		return ministroRelator;
	}

	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}
	
	public Boolean getLiberarVariosProcessos() {
		if ( getResources() != null && getResources().size() > 1 ) {
			liberarVariosProcessos = Boolean.TRUE;
			getDefinition().setFacet( "principal" );
			getDefinition().setHeight( 200 );
		} else {
			liberarVariosProcessos = Boolean.FALSE;
			getDefinition().setFacet( "principal" );
			getDefinition().setHeight( 500 );
		}
		return liberarVariosProcessos;
	}
	
	public void setLiberarVariosProcessos(Boolean liberarVariosProcessos) {
		this.liberarVariosProcessos = liberarVariosProcessos;
	}

	public Boolean getSessaoMinistroDiferente() {
		return sessaoMinistroDiferente;
	}

	public void setSessaoMinistroDiferente(Boolean sessaoMinistroDiferente) {
		this.sessaoMinistroDiferente = sessaoMinistroDiferente;
	}

	@Embeddable
	public static class ProcessoVinculadoDto implements br.jus.stf.estf.decisao.support.query.Dto {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 4361640450675610856L;
		
		private boolean selected;
		private ObjetoIncidenteDto objetoIncidente;
		private TipoVinculacao vinculacao;
		private Long idListaJulgamentoConjunto;
		private Long idVinculoObjeto;

		@Override
		public boolean isSelected() {
			return selected;
		}

		@Override
		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		@Override
		public Long getId() {
			return null;
		}

		@Override
		public boolean isFake() {
			return false;
		}

		public ObjetoIncidenteDto getObjetoIncidente() {
			return objetoIncidente;
		}

		public void setObjetoIncidente(ObjetoIncidenteDto objetoIncidente) {
			this.objetoIncidente = objetoIncidente;
		}

		public TipoVinculacao getVinculacao() {
			return vinculacao;
		}

		public void setVinculacao(TipoVinculacao vinculacao) {
			this.vinculacao = vinculacao;
		}
		
		public Long getIdListaJulgamentoConjunto() {
			return idListaJulgamentoConjunto;
		}

		public void setIdListaJulgamentoConjunto(Long idListaJulgamentoConjunto) {
			this.idListaJulgamentoConjunto = idListaJulgamentoConjunto;
		}

		public Long getIdVinculoObjeto() {
			return idVinculoObjeto;
		}

		public void setIdVinculoObjeto(Long idVinculoObjeto) {
			this.idVinculoObjeto = idVinculoObjeto;
		}

		public static enum TipoVinculacao {
			DEPENDE_DO_JULGAMENTO ("Depende do julgamento"), JULGAMENTO_CONJUNTO("Julgamento conjunto");
			
			private String descricao;
			
			private TipoVinculacao (String descricao) {
				this.descricao = descricao;
			}

			public String getDescricao() {
				return descricao;
			}

			public void setDescricao(String descricao) {
				this.descricao = descricao;
			}
		}
	}

	@Embeddable
	public static class PrevisaoSustentacaoOralDto implements Dto {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7386749501315947160L;
		
		private Long id;
		private boolean selected;
		private Long idEnvolvido;
		private Long idAdvogado;
		private Long idParte;
		private String nomeAdvogado;
		private String nomeParte;
		private String observacao;
		private Long idInformacaoPautaProcesso;
		
		@Override
		public boolean isSelected() {
			return selected;
		}

		@Override
		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		@Override
		public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}

		@Override
		public boolean isFake() {
			return false;
		}

		public Long getIdEnvolvido() {
			return idEnvolvido;
		}

		public void setIdEnvolvido(Long idEnvolvido) {
			this.idEnvolvido = idEnvolvido;
		}

		public Long getIdAdvogado() {
			return idAdvogado;
		}

		public void setIdAdvogado(Long idAdvogado) {
			this.idAdvogado = idAdvogado;
		}

		public Long getIdParte() {
			return idParte;
		}

		public void setIdParte(Long idParte) {
			this.idParte = idParte;
		}

		public String getNomeAdvogado() {
			return nomeAdvogado;
		}

		public void setNomeAdvogado(String nomeAdvogado) {
			this.nomeAdvogado = nomeAdvogado;
		}

		public String getNomeParte() {
			return nomeParte;
		}

		public void setNomeParte(String nomeParte) {
			this.nomeParte = nomeParte;
		}

		public String getObservacao() {
			return observacao;
		}

		public void setObservacao(String observacao) {
			this.observacao = observacao;
		}
		
		public Long getIdInformacaoPautaProcesso() {
			return idInformacaoPautaProcesso;
		}

		public void setIdInformacaoPautaProcesso(Long idInformacaoPautaProcesso) {
			this.idInformacaoPautaProcesso = idInformacaoPautaProcesso;
		}

		public static PrevisaoSustentacaoOralDto valueOf(PrevisaoSustentacaoOral previsaoSustentacaoOral) {
			PrevisaoSustentacaoOralDto dto = new PrevisaoSustentacaoOralDto();
			dto.setId(previsaoSustentacaoOral.getId());
			dto.setIdParte(previsaoSustentacaoOral.getRepresentado().getId());
			dto.setIdAdvogado(previsaoSustentacaoOral.getJurisdicionado() == null ? null : previsaoSustentacaoOral.getJurisdicionado().getId());
			dto.setIdEnvolvido(previsaoSustentacaoOral.getEnvolvido() == null ? null : previsaoSustentacaoOral.getEnvolvido().getId());
			dto.setNomeParte(previsaoSustentacaoOral.getRepresentado().getNome());
			dto.setNomeAdvogado(previsaoSustentacaoOral.getJurisdicionado() == null ? previsaoSustentacaoOral.getEnvolvido().getNome() : previsaoSustentacaoOral.getJurisdicionado().getNome());
			dto.setObservacao(previsaoSustentacaoOral.getObservacao());
			dto.setIdInformacaoPautaProcesso(previsaoSustentacaoOral.getInformacaoPautaProcesso().getId());
			return dto;
		}
	}

	@Embeddable
	public static class AdvogadoSustentacaoOral implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -5696738223491276956L;
		
		private Envolvido envolvido;
		private Jurisdicionado jurisdicionado;
		
		public Envolvido getEnvolvido() {
			return envolvido;
		}
		
		public void setEnvolvido(Envolvido envolvido) {
			this.envolvido = envolvido;
		}
		
		public Jurisdicionado getJurisdicionado() {
			return jurisdicionado;
		}
		
		public void setJurisdicionado(Jurisdicionado jurisdicionado) {
			this.jurisdicionado = jurisdicionado;
		}
		
		public String getNome() {
			if (envolvido != null) {
				return envolvido.getNome();
			}
			if (jurisdicionado != null) {
				return jurisdicionado.getNome();
			}
			return null;
		}
	}
}
