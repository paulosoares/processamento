package br.jus.stf.estf.decisao.objetoincidente.web;

import static br.jus.stf.estf.decisao.support.util.ApplicationContextUtils.getApplicationContext;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.richfaces.event.DropEvent;
import org.richfaces.event.DropListener;
import org.springframework.security.context.SecurityContextHolder;

import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.entidade.documento.ArquivoEletronicoView;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoJulgamentoVirtual;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ObservacaoProcesso;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoMotivoAlteracao;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.enuns.SituacaoIncidenteJulgadoOuNao;
import br.gov.stf.estf.julgamento.model.dataaccess.hibernate.PreListaJulgamentoDaoHibernate;
import br.gov.stf.estf.julgamento.model.service.JulgamentoProcessoService;
import br.gov.stf.estf.julgamento.model.service.ListaJulgamentoService;
import br.gov.stf.estf.julgamento.model.service.SessaoService;
import br.gov.stf.estf.processostf.model.service.AgendamentoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.ObservacaoProcessoService;
import br.gov.stf.estf.processostf.model.service.PreListaJulgamentoObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.PreListaJulgamentoService;
import br.gov.stf.estf.processostf.model.service.exception.AgendamentoNaoDefinidoException;
import br.gov.stf.estf.publicacao.model.service.ConteudoPublicacaoService;
import br.gov.stf.estf.publicacao.model.service.FeriadoService;
import br.gov.stf.estf.util.DataUtil;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.support.ValidacaoLiberacaoParaJulgamentoException;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionInterface;
import br.jus.stf.estf.decisao.support.security.Principal;
import br.jus.stf.estf.decisao.support.service.ConfiguracaoSistemaService;
import br.jus.stf.estf.decisao.support.service.UsuarioLogadoService;
import br.jus.stf.estf.decisao.support.util.ColunaDragAndDrop;
import br.jus.stf.estf.decisao.support.util.GlobalFacesBean;
import br.jus.stf.estf.decisao.support.util.NestedRuntimeException;
import br.jus.stf.estf.decisao.support.util.ObjetoDragAndDrop;
import br.jus.stf.estf.decisao.support.util.TextoUtils;


/**
 * Gerencia a tela de agrupamento de objetos incidentes 
 * 
 * @author Gabriel Teles
 * @since 09.08.2015
 */
@Name("revisarListasFacesBean")
@Scope(ScopeType.CONVERSATION)
@Restrict({ActionIdentification.LIBERAR_PARA_JULGAMENTO})
public class RevisarListasFacesBean implements DropListener  {
	
	protected static final String MSG_PROCESSO_OBRIGATORIO       = "Você deve selecionar um processo para inserir.";
	protected static final String MSG_ERRO_NAO_ADICIONAR_LISTA   = "Não foi possível adicionar o processo. Este já está presente em uma lista.";
	protected static final String MSG_ERRO_PROCESSO_NAO_REVISADO = "Não foi possível marcar o processo como revisado.";
	protected static final String MSG_ERRO_OBSERVACAO_TAMANHO    = "Não é possível gravar mais que 500 caracteres.";
	@Logger
	private Log logger;
	/**
	 * @const Long SEM_LISTA_ID Identificador da lista "Sem Lista". Utilizado para identificar um drop nessa
	 * coluna e então remover a categoria do Objeto Incidente.
	 */
	final static Long SEM_LISTA_ID = -15L;
	private static final String MSG_ERRO_SISTEMA_ERRADO = "Incidentes de Repercussão Geral (RG) e Segundo Julgamento na Repercussão Geral (RG2JULG) devem ser incluídos no Plenário Virtual da Repercussão Geral.";
	
	/**
	 * @var ObjetoIncidente objIncidenteSelecionado Indica o objeto incidente selecionado
	 */
	private ObjetoIncidenteDto objIncidenteSelecionado = null;	
	/**
	 * @var Long idColunaSelecionada ID da coluna selecionada
	 */
	private Long idColunaSelecionada = null;

	/**
	 * @var boolean erro Indica a existência de erros no processamento
	 */
	private boolean erro;
	/**
	 * @var List<ColunaDragAndDrop<ObjetoIncidenteDto>> colunas Lista de colunas para Drag And Drop
	 */
	private LinkedList<ColunaDragAndDrop<ObjetoIncidenteDto>> colunas = null;
	/**
	 * @var ColunaDragAndDrop<ObjetoIncidenteDto> colunaSemLista Identifica a coluna "Sem Lista"
	 */
	private ColunaDragAndDrop<ObjetoIncidenteDto> colunaSemLista;
	/**
	 * @var Setor setorCarregado Indica o setor carregado no FacesBean
	 */
	private Setor setorCarregado;
	/**
	 * @var String remocaoAjaxId ID da coluna do item que será removido após clicar na lixeira próxima ao item
	 */
	private String remocaoAjaxId;
	/**
	 * @var String remocaoAjaxNome Nome do objeto que será removido após clicar na lixeira próxima ao item
	 */
	private String remocaoAjaxNome;
	/**
	 * @var String colunaMarcarTodosAjaxId ID da coluna que o usuário clicou em "Marcar Todos"
	 */
	private String colunaMarcarTodosAjaxId;
	
	
	private ObjetoIncidenteDto objetoIncidenteParaVisualizar;
	private ColunaDragAndDrop<ObjetoIncidenteDto> colunaObjetoIncidenteParaVisualizar;
	private String textoEmenta;
	private String textoRelatorio;
	private String textoVoto;
	private String observacao;
	
	private String Motivo;

	public String getMotivo() {
		return Motivo;
	}
	
	public void setMotivo(String motivo) {
		Motivo = motivo;
	}
	
	private Texto ementa = null;
	private Texto relatorio = null;
	private Texto voto = null;
	public Texto getEmenta() {
		return ementa;
	}

	public void setEmenta(Texto ementa) {
		this.ementa = ementa;
	}

	public Texto getRelatorio() {
		return relatorio;
	}

	public void setRelatorio(Texto relatorio) {
		this.relatorio = relatorio;
	}

	public Texto getVoto() {
		return voto;
	}

	public void setVoto(Texto voto) {
		this.voto = voto;
	}
	
	
	private boolean incidenteJulgado = false;
	
	@In(value="#{preListaJulgamentoService}")
	private PreListaJulgamentoService preListaJulgamentoService;
	
	@In(value="#{julgamentoProcessoService}")
	private JulgamentoProcessoService julgamentoProcessoService;
	
	@In(value="#{preListaJulgamentoObjetoIncidenteService}")
	private PreListaJulgamentoObjetoIncidenteService preListaJulgamentoObjetoIncidenteService;
	
	@In(value="#{usuarioLogadoService}")
	private UsuarioLogadoService usuarioLogadoService;
	
	@In(value = "#{objetoIncidenteService}")
	private ObjetoIncidenteService objetoIncidenteService;
	
	@In(value = "#{objetoIncidenteServiceLocal}")
	private br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService objetoIncidenteServiceLocal;
	
	@In(value = "#{agendamentoService}")
	private AgendamentoService agendamentoService;
	
	@In(value = "#{observacaoProcessoService}")
	private ObservacaoProcessoService observacaoProcessoService;
	
	@In(value="#{textoService}")
	private TextoService textoService;
	
	@In(value="#{textoServiceLocal}")
	private br.jus.stf.estf.decisao.texto.service.TextoService textoServiceLocal;
	
	@In(value = "globalFacesBean", create = true)
	private GlobalFacesBean globalFacesBean;
	
	@In(value = "#{configuracaoSistemaServiceLocal}")
	private ConfiguracaoSistemaService configuracaoSistemaService;
	
	private boolean ordenacaoNumerica;
	private List<PreListaJulgamento> preListasJulgamento;
	
	private Boolean reabrirModalListasLiberadas;
	
	private Date dataLiberacao = new Date();;
	
	private String dataInicioJulgamento;
	private String dataFimJulgamento;
	private String memoriaCalculo;

	@In(value = "#{liberarPreListaParaJulgamentoActionFacesBean}")
	private LiberarPreListaParaJulgamentoActionFacesBean liberarPreListaParaJulgamentoActionFacesBean;
	
	@In(value="#{listaJulgamentoService}")
	private ListaJulgamentoService listaJulgamentoService;
	
	private String idColegiado;
	
	@In(value="#{sessaoService}")
	public SessaoService sessaoService;
	
	@In(value="#{feriadoService}")
	public FeriadoService feriadoService;
	
	@In(value="#{conteudoPublicacaoService}")
	public ConteudoPublicacaoService conteudoPublicacaoService;
	
	/**
	 * Inicializa o RevisarListasFacesBean
	 */
	@Create
    public void init() {
		try {
			if (usuarioLogadoService.getMinistro() == null || usuarioLogadoService.getMinistro().getSetor() == null)
				return;
			
			reabrirModalListasLiberadas = false;
			setorCarregado = usuarioLogadoService.getMinistro().getSetor();
			ordenacaoNumerica = configuracaoSistemaService.isOrdenacaoNumerica();
			preListasJulgamento = preListaJulgamentoService.listarPreListasJulgamentoDoSetor(setorCarregado);
	        inicializarColunasDragAndDrop();
		} catch (Exception e) {
			e.printStackTrace();
			addError(CategorizarProcessoActionFacesBean.MSG_ERRO_NAO_FOI_POSSIVEL_CARREGAR_CAT_GABINETE);
		}
    }
	
    public void initListas() {
		try {
			if (usuarioLogadoService.getMinistro() == null || usuarioLogadoService.getMinistro().getSetor() == null)
				return;
			
			reabrirModalListasLiberadas = false;
			setorCarregado = usuarioLogadoService.getMinistro().getSetor();
			ordenacaoNumerica = configuracaoSistemaService.isOrdenacaoNumerica();
			preListasJulgamento = preListaJulgamentoService.listarPreListasJulgamentoDoSetor(setorCarregado);
		} catch (Exception e) {
			e.printStackTrace();
			addError(CategorizarProcessoActionFacesBean.MSG_ERRO_NAO_FOI_POSSIVEL_CARREGAR_CAT_GABINETE);
		}
    }
    
	/**
	 * Agrupa objetos incidentes em uma pré-lista (categoria/agrupamento)
	 */
	public void agruparObjetosIncidentes() {
		// Só realiza algum processamento caso a view seja a de agrupamento
		if (FacesContext.getCurrentInstance().getViewRoot().getViewId().equals("/revisarListas.xhtml")) {
			// Verifica mudança de setor
			Setor setorUsuario = null;
			
			if (usuarioLogadoService.getMinistro() != null && usuarioLogadoService.getMinistro().getSetor() != null)
				setorUsuario = usuarioLogadoService.getMinistro().getSetor();
				
			if ((setorUsuario != null && !setorUsuario.equals(setorCarregado)))
				init();
		}
	}

	/**
	 * Recarrega as listas
	 */
	public void regarregarListas() {
		inicializarColunasDragAndDrop();
	}
	
	/**
	 * Seleciona um objeto incidente a partir da caixa de sugestão
	 * @param ObjetoIncidenteDto incidente
	 */
	@SuppressWarnings("rawtypes")
	public void selecionarObjetoIncidente(ObjetoIncidenteDto objIncidente) {
		try {
			ObjetoIncidente oi = objetoIncidenteService.recuperarPorId(objIncidente.getId());
			setObjIncidenteSelecionado(ObjetoIncidenteDto.valueOf(oi));
			adicionarObjetoIncidente();
		} catch (ServiceException e) {
			
			addError(MSG_ERRO_NAO_ADICIONAR_LISTA);
			e.printStackTrace();
		}	
		
	}
	
	/**
	 * Recupera o controlador de mensagens.
	 * 
	 * @return o controlador
	 */
	public FacesMessages getFacesMessages() {
		return FacesMessages.instance();
	}
	
	/**
	 * Envia um mensagem de erro para apresentação pelo mecanismo de 
	 * mensagens.
	 * 
	 * @param error a mensagem de erro
	 */
	public void addError(String error) {
		getFacesMessages().add(Severity.ERROR, error);
		erro = true;
	}
	
	/**
	 * Persiste a configuração da revisão de listas
	 */
	@SuppressWarnings("rawtypes")
	public void processaCheckboxRevisao(ObjetoDragAndDrop<ObjetoIncidenteDto> objeto, ColunaDragAndDrop<ObjetoIncidenteDto> coluna) {
		try {
			ObjetoIncidente oi = new Processo();
			oi.setId(objeto.getInstancia().getId());
			
			PreListaJulgamento pj = new PreListaJulgamento();
			pj.setId(coluna.getId());
			
			Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			PreListaJulgamentoObjetoIncidente relacionamento = preListaJulgamentoService.alterarProcessoParaRevisado(oi, pj, objeto.getRevisado(), principal.getUsuario());
			
			if (Boolean.TRUE.equals(relacionamento.getRevisado())) {
				objeto.setRevisor(relacionamento.getUsuarioRevisor().getNome());
				objeto.setDataRevisao(relacionamento.getDataRevisao());
			}
			
			coluna.atualizarCheckboxRevisao();
		} catch (ServiceException e) {
			objeto.setRevisado(!objeto.getRevisado());
			addError(MSG_ERRO_PROCESSO_NAO_REVISADO);
			e.printStackTrace();
		}
	}
	
	/**
	 * Verifica a existência de um erro
	 * @return boolean
	 */
	public boolean hasError() {
		return erro;
	}

	/**
	 * Recupera o objeto incidente selecionado pela Rich Suggestion Box.
	 * @return ObjetoIncidenteDto
	 */
	public ObjetoIncidenteDto getObjIncidenteSelecionado() {
		return objIncidenteSelecionado;
	}

	/**
	 * Determina o objeto incidente selecionado
	 * @param ObjetoIncidenteDto objIncidenteSelecionado
	 */
	public void setObjIncidenteSelecionado(ObjetoIncidenteDto objIncidenteSelecionado) {
		this.objIncidenteSelecionado = objIncidenteSelecionado;
	}
	
	/**
	 * Recupera o ID da categoria que possui o elemento que será removido
	 * @return String ID da categoria
	 */
	public String getRemocaoAjaxId() {
		return remocaoAjaxId;
	}

	/**
	 * Determina o ID da categoria que possui o elemento que será removido
	 * @param remocaoAjaxId ID da categoria
	 */
	public void setRemocaoAjaxId(String remocaoAjaxId) {
		this.remocaoAjaxId = remocaoAjaxId;
	}

	/**
	 * Recupera o nome do objeto que será removido
	 * @return String  Nome do objeto
	 */
	public String getRemocaoAjaxNome() {
		return remocaoAjaxNome;
	}

	/**
	 * Determina o nome do objeto que será removido
	 * @param remocaoAjaxNome
	 */
	public void setRemocaoAjaxNome(String remocaoAjaxNome) {
		this.remocaoAjaxNome = remocaoAjaxNome;
	}
	
	/**
	 * Recupera a lista de colunas
	 * @return List<ColunaDragAndDrop<ObjetoIncidenteDto>>
	 */
	public List<ColunaDragAndDrop<ObjetoIncidenteDto>> getColunas() {
		return colunas;
	}
 
	public void setColunas(LinkedList<ColunaDragAndDrop<ObjetoIncidenteDto>> colunas) {
		this.colunas = colunas;
	}	
	
	/**
	 * Ação para adicionar um novo objeto incidente à lista de "Sem Lista"
	 */
	public void adicionarObjetoIncidente() {
		adicionarObjetoIncidente(true);
	}
	
	/**
	 * @return String Retorna o ID da coluna que o usuário clicou em "Marcar Todos"
	 */
	public String getColunaMarcarTodosAjaxId() {
		return colunaMarcarTodosAjaxId;
	}
	
	/**
	 * @param String colunaMarcarTodosAjaxId Define o ID da coluna que o usuário clicou em "Marcar Todos"
	 */
	public void setColunaMarcarTodosAjaxId(String colunaMarcarTodosAjaxId) {
		this.colunaMarcarTodosAjaxId = colunaMarcarTodosAjaxId;
	}
	
	
	/**
	 * Marca todos os incidentes de uma coluna como revisados
	 */
	public void processarTodosComoRevisadosAjax() {
		ColunaDragAndDrop<ObjetoIncidenteDto> coluna = null;
		int idColuna = Integer.parseInt(colunaMarcarTodosAjaxId);
		
		for (ColunaDragAndDrop<ObjetoIncidenteDto> _coluna : colunas) {
			if (_coluna.getId() == idColuna) {
				coluna = _coluna;
				break;
			}
		}
		
		if (coluna != null) {
			boolean revisado = !coluna.getTodosRevisados();
			for (ObjetoDragAndDrop<ObjetoIncidenteDto> objeto : coluna.getObjetos()) {
				objeto.setRevisado(revisado);
				processaCheckboxRevisao(objeto, coluna);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected void adicionarObjetoIncidente(boolean verificarSeFoiJulgado) {
		objetoIncidenteParaVisualizar = null;
		ObjetoIncidenteDto objetoIncidenteSelecionado = getObjIncidenteSelecionado();
		
		// Não selecionou um Objeto incidente
		if (objetoIncidenteSelecionado == null) {
			addError(MSG_PROCESSO_OBRIGATORIO);
		// Selecionou um incidente
		} else {
			try {
				// Não é possível inserir um objeto incidente que já está presente em uma categoria no gabinete
				ObjetoIncidente objetoIncidente = objetoIncidenteService.recuperarPorId(objetoIncidenteSelecionado.getId());
				PreListaJulgamento preListaAtiva = preListaJulgamentoService.recuperarPreListaJulgamentoAtiva(objetoIncidente);
				
				if ( preListaAtiva!= null) {
					addError(MSG_ERRO_NAO_ADICIONAR_LISTA);
				} else {
					
					if (objetoIncidente instanceof IncidenteJulgamento) {
						IncidenteJulgamento ij = (IncidenteJulgamento) objetoIncidente;
						List<String> tiposRG = Arrays.asList(TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL, TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO);
						
						if (tiposRG.contains(ij.getTipoJulgamento().getSigla())) {
							addError(MSG_ERRO_SISTEMA_ERRADO);
							return;
						}
					}
					
					validarMinistroRelatorOuVistor(objetoIncidente, usuarioLogadoService.getMinistro());
					
					if (verificarSeFoiJulgado) {
						if (isObjetoIncidenteJulgado(objetoIncidenteSelecionado)) {
							setIncidenteJulgado(true);
							return;
						} else {
							setIncidenteJulgado(false);
						}
					}
					
					PreListaJulgamentoMotivoAlteracao motivo = recuperaMotivoInclusao(objetoIncidenteSelecionado);
					ColunaDragAndDrop<ObjetoIncidenteDto> coluna = recuperaColunaDrapAndDropSelecionada();
					
					if (coluna.getId() != SEM_LISTA_ID && !verificarIndiceColunaProcesso(objetoIncidenteSelecionado, coluna) ||
							!verificarColegiadoMateriaProcessoLista(objetoIncidenteSelecionado, coluna))
						return;	
					
					String revisor = "";
					Date dataRevisao = null;
					String observacao = null;

					if (getPreListasJulgamento() != null) {
						for(PreListaJulgamento lista : getPreListasJulgamento()){
							
							if (lista.getObjetosIncidentes() != null && lista.getObjetosIncidentes().size() > 0) {
								for(PreListaJulgamentoObjetoIncidente relacionamento : lista.getObjetosIncidentes()){
									if (relacionamento.getObjetoIncidente().equals(objetoIncidente)) {
										
										if (relacionamento.getUsuarioRevisor() != null)
											revisor = relacionamento.getUsuarioRevisor().getNome();
										
										dataRevisao = relacionamento.getDataRevisao();
										motivo = relacionamento.getMotivo();
										
										for (ObservacaoProcesso op : relacionamento.getObjetoIncidente().getPrincipal().getObservacaoProcesso())
											if (op.getSetor().equals(usuarioLogadoService.getMinistro().getSetor()))
												observacao = op.getObservacao();
									}
								}
							}
						}
					}
					
					//Faz as validações para retorno de vistas
					if (!verificarProcessoComVistaJulgamentoVirtual(objetoIncidenteSelecionado, coluna))
						return;
					
					coluna.add(objetoIncidenteSelecionado, objetoIncidenteSelecionado.toString(), false, motivo, revisor, dataRevisao, observacao);
					persistirProcessoNaLista(coluna.getId(), objetoIncidenteSelecionado.getId(), motivo);
				}
			} catch (ServiceException e) {
				addError("Não foi possível adicionar o processo.");
			} catch (ValidacaoLiberacaoParaJulgamentoException e) {
				addError(e.getMessage());
			}
		}
	}

	void validarMinistroRelatorOuVistor(ObjetoIncidente objetoIncidente, Ministro ministro) throws ValidacaoLiberacaoParaJulgamentoException {
		objetoIncidenteServiceLocal.validarMinistroRelatorOuVistor(objetoIncidente, usuarioLogadoService.getMinistro());
	}

	protected ColunaDragAndDrop<ObjetoIncidenteDto> recuperaColunaDrapAndDropSelecionada() {
		ColunaDragAndDrop<ObjetoIncidenteDto> coluna;
		coluna = colunas.get(0); // Coluna "Sem Lista"		
		if (idColunaSelecionada != null) {
			for (ColunaDragAndDrop<ObjetoIncidenteDto> colunaItem : colunas){ 
				if (colunaItem.getId().equals(idColunaSelecionada)){
					coluna = colunaItem;
				}
			}
		}
		return coluna;
	}
	
	protected PreListaJulgamentoMotivoAlteracao recuperaMotivoInclusao(ObjetoIncidenteDto objetoIncidenteSelecionado) throws ServiceException {
		PreListaJulgamentoMotivoAlteracao motivo = null;
		ObjetoIncidente<?> oiRecuperado = objetoIncidenteService.recuperarPorId(objetoIncidenteSelecionado.getId());
		if (!temRelatorio(oiRecuperado)){
			motivo = PreListaJulgamentoMotivoAlteracao.SEM_RELATORIO;
		}else if(isObjetoIncidenteJulgado(objetoIncidenteSelecionado)){
			motivo = PreListaJulgamentoMotivoAlteracao.JA_JULGADO;
		}else if(!temEmenta(oiRecuperado)){
			motivo = PreListaJulgamentoMotivoAlteracao.SEM_EMENTA;
		}else if (!temVoto(oiRecuperado)){
			motivo = PreListaJulgamentoMotivoAlteracao.SEM_VOTO;
		}else{
			motivo = PreListaJulgamentoMotivoAlteracao.MANUAL;
		}
		return motivo;
	}

	public void confirmarInclusaoSelecionado() {
		adicionarObjetoIncidente(false);
	}
	
	protected boolean isObjetoIncidenteJulgado(ObjetoIncidenteDto incidente) throws ServiceException {
		return objetoIncidenteService.recuperarSituacaoJulgamentoIncidente(incidente.getId()).equals(SituacaoIncidenteJulgadoOuNao.JULGADO);
	}

	/**
	 * Processa Drag And Drop de um Objeto Incidente em uma coluna
	 */
	@Override
	public void processDrop(DropEvent e) {
		// identifica a coluna origem
		@SuppressWarnings("unchecked")
		ObjetoDragAndDrop<ObjetoIncidenteDto> colunaOrigem = (ObjetoDragAndDrop<ObjetoIncidenteDto>)e.getDragValue();
		ObjetoIncidenteDto objetoIncidenteDto = colunaOrigem.getInstancia();
		Long idObjetoIncidente = objetoIncidenteDto.getId();
		
		// Identifica a coluna destino
		@SuppressWarnings("unchecked")
		ColunaDragAndDrop<ObjetoIncidenteDto> colunaDestino = (ColunaDragAndDrop<ObjetoIncidenteDto>)e.getDropValue();				
		
		if ( colunaDestino.getId() != SEM_LISTA_ID &&
				colunaDestino.getId() < PreListaJulgamentoDaoHibernate.LIMITE_PRE_LISTA_DESTAQUES_CANCELADOS &&
				!verificarIndiceColunaProcesso(objetoIncidenteDto, colunaDestino) ||
				!verificarColegiadoMateriaProcessoLista(objetoIncidenteDto, colunaDestino) || 
				!verificarProcessoComVistaJulgamentoVirtual(objetoIncidenteDto, colunaDestino) )
			return;		
		
		// Remove o estado de revisado 
		colunaOrigem.setRevisado(false);
		
		// Remove o processo da lista de origem
		colunaOrigem.getColuna().remove(objetoIncidenteDto);
		//Long colunaAnterior = colunaOrigem.getColuna().getId();
		
		// Adiciona o processo na lista de destino
		Long colunaNova  = colunaDestino.getId();
		String nome      = colunaOrigem.getNome();
		Boolean revisado = colunaOrigem.getRevisado();
		String revisor   = colunaOrigem.getRevisor();
		Date dataRevisao = colunaOrigem.getDataRevisao();
		PreListaJulgamentoMotivoAlteracao motivo = colunaOrigem.getMotivo();
		String observacao = colunaOrigem.getObservacao();
		colunaDestino.add(objetoIncidenteDto, nome, revisado, motivo, revisor, dataRevisao, observacao);
		colunaOrigem.setColuna(colunaDestino);
		
		try {
			this.persistirProcessoNaLista(colunaNova, idObjetoIncidente, motivo);		
		} catch (ServiceException ex) {
			addError("Não foi possível associar o objeto incidente com a lista.");
		}
	}
	
	private Boolean verificarIndiceColunaProcesso(ObjetoIncidenteDto objetoIncidenteDto, ColunaDragAndDrop<ObjetoIncidenteDto> colunaDestino){
		if (colunaDestino.getObjetos() == null || colunaDestino.getObjetos().isEmpty())
			return Boolean.TRUE;
		ObjetoIncidenteDto objExemploLista = colunaDestino.getObjetos().get(0).getInstancia();

		try{
			ObjetoIncidente<?> objExemploAux = objetoIncidenteService.recuperarPorId(objExemploLista.getId());
			ObjetoIncidente<?> objAdicionado = objetoIncidenteService.recuperarPorId(objetoIncidenteDto.getId());
			Boolean listaComAgendamento = !agendamentoService.pesquisar(objExemploAux).isEmpty();
			if (listaComAgendamento != (!agendamentoService.pesquisar(objAdicionado).isEmpty())){
				if (listaComAgendamento)
					addError("Somente processos registrados no índice podem ser adicionados à essa lista.");
				else
					addError("Somente processos não registrados no índice podem ser adicionados à essa lista.");
				return Boolean.FALSE;
			}
		}catch(ServiceException ex){
			addError("Erro ao verificar o agendamento dos processos da lista");
			return Boolean.FALSE; 
		}
		return Boolean.TRUE;
				
	}
	
	private Boolean verificarColegiadoMateriaProcessoLista(ObjetoIncidenteDto dto, ColunaDragAndDrop<ObjetoIncidenteDto> colunaDestino){
		if (colunaDestino.getObjetos() == null || colunaDestino.getObjetos().isEmpty() || SEM_LISTA_ID.equals(colunaDestino.getId()))
			return Boolean.TRUE;
		ObjetoIncidenteDto objExemploLista = colunaDestino.getObjetos().get(0).getInstancia();
		try{
			ObjetoIncidente<?> objExemploAux = objetoIncidenteService.recuperarPorId(objExemploLista.getId());
			ObjetoIncidente<?> objAdicionado = objetoIncidenteService.recuperarPorId(dto.getId());
			List<Agendamento> agendamentoListaAux = agendamentoService.pesquisar(objExemploAux);
			if (agendamentoListaAux.isEmpty())
				return Boolean.TRUE;	
			Agendamento agendamentoAux = agendamentoListaAux.get(0);
			Agendamento agendamentoProcesso = agendamentoService.pesquisar(objAdicionado).get(0);			
			if (!agendamentoAux.getId().getCodigoCapitulo().equals(agendamentoProcesso.getId().getCodigoCapitulo())){
				addError("[" + objAdicionado.getIdentificacao() + "] O processo adicionado está pautado em um colegiado diferente dos processos inclusos na lista.");
				return Boolean.FALSE;
			}
			if (!agendamentoAux.getId().getCodigoMateria().equals(agendamentoProcesso.getId().getCodigoMateria())){
				addError("[" + objAdicionado.getIdentificacao() + "] O processo adicionado está pautado em uma matéria diferente dos processos inclusos na lista.");
				return Boolean.FALSE;
			} 
			
		}catch(ServiceException ex){
			addError("Erro ao verificar o agendamento dos processos da lista");
			return Boolean.FALSE; 
		}
		return Boolean.TRUE;						
	}
	
	//Verifica se a lista à qual o processo esta sendo adicionado possui somente processos com vista oriundos de julgamento virtual
	private Boolean verificarProcessoComVistaJulgamentoVirtual(ObjetoIncidenteDto objetoIncidenteAdicionadoDto, ColunaDragAndDrop<ObjetoIncidenteDto> colunaDestino){
		
		try{
			//Se é o primeiro processo da lista, não é necessário verificar.
			if (colunaDestino.getObjetos() == null || colunaDestino.getObjetos().isEmpty() || SEM_LISTA_ID.equals(colunaDestino.getId()))
				return Boolean.TRUE;

			/*****************************************************************************************************************/
			
			//Busca o objeto adicionado para verificar se possui vistas 
			ObjetoIncidente<?> objAdicionado = objetoIncidenteService.recuperarPorId(objetoIncidenteAdicionadoDto.getId());
			
			//Verfica se o objeto adicionado tem vistas.
			boolean objAdicionadoTemVista = false;
			try{
				Agendamento agendamento = objetoIncidenteServiceLocal.consultaAgendamentoCadastrado(objAdicionado);
				objAdicionadoTemVista = agendamento.getVista();
				
				//Recupera o tipo de ambiente, pode ser pela julgamento_processo ou processo_lista_julg.
				if (objetoIncidenteServiceLocal.isDevolucaoDeVistaPresencial(objAdicionado))
					objetoIncidenteAdicionadoDto.setTipoAmbiente(TipoAmbienteConstante.PRESENCIAL.getSigla());
				else 
					objetoIncidenteAdicionadoDto.setTipoAmbiente(TipoAmbienteConstante.VIRTUAL.getSigla());
			} catch (AgendamentoNaoDefinidoException e) {
			}
			objetoIncidenteAdicionadoDto.setTemVistas(objAdicionadoTemVista);
			
			//Recupera  a última lista
			ListaJulgamento ultimaListaObjetoAdicionado = objetoIncidenteServiceLocal.findUltimaListaJulgamento(objAdicionado);
			
			/*****************************************************************************************************************/
			//Busca o objeto exemplo da lista para verificar se é um processo com vistas. 
			//Somente processos com vista podem ser adicionados a listas com outros processos com vista.
			ObjetoIncidenteDto objetoExemploDto = colunaDestino.getObjetos().get(0).getInstancia();
			ObjetoIncidente<?> objExemploEntity = objetoIncidenteService.recuperarPorId(objetoExemploDto.getId());

			//Verifica se o objeto Exemplo tem vistas;
			boolean objExemploTemVista = false; 
			try{
				Agendamento agendamentoObjetoExemplo = objetoIncidenteServiceLocal.consultaAgendamentoCadastrado(objExemploEntity);
				objExemploTemVista = agendamentoObjetoExemplo.getVista();
				
				//Recupera o tipo de ambiente, pode ser pela julgamento_processo ou processo_lista_julg.
				if (objetoIncidenteServiceLocal.isDevolucaoDeVistaPresencial(objExemploEntity))
					objetoExemploDto.setTipoAmbiente(TipoAmbienteConstante.PRESENCIAL.getSigla());
				else 
					objetoExemploDto.setTipoAmbiente(TipoAmbienteConstante.VIRTUAL.getSigla());
			} catch (AgendamentoNaoDefinidoException e) {
			}
			objetoExemploDto.setTemVistas(objExemploTemVista);

			//Recupera  a última lista
			ListaJulgamento ultimaListaObjetoExemplo = objetoIncidenteServiceLocal.findUltimaListaJulgamento(objExemploEntity);
			
			/*****************************************************************************************************************/
			//Todos os processos de uma lista de devolução devem possuir vistas.
			if (objExemploTemVista){
				if (!objAdicionadoTemVista){
					addError("Somente processos que possuem vista pendente podem ser adicionados à essa lista.");
					return Boolean.FALSE;
				}
			} else {
				if (objAdicionadoTemVista){
					addError("Somente processos que não possuem vista pendente podem ser adicionados à essa lista.");
					return Boolean.FALSE;
				}
			}
			
			//Se ambos tem vista Verifica o tipo ambiente de origem. Processos retornando com vistas, se um é presencial e outro é virtual não permite;
			if (objExemploTemVista && objAdicionadoTemVista){
				if (!objetoExemploDto.getTipoAmbiente().equals(objetoIncidenteAdicionadoDto.getTipoAmbiente())){
					addError("Processo com retorno de vista do Presencial e do Virtual devem constar em listas separadas.");
					return Boolean.FALSE;
				}
				if (!ultimaListaObjetoAdicionado.getId().equals(ultimaListaObjetoExemplo.getId())){
					addError("Listas de devolução de vistas somente podem conter processos oriundos de uma mesma lista.");
					return Boolean.FALSE;
				}
			}
			
			//Para processos oriundos de listas, estas devem possuir nomes iguais. Só permite se existirem as duas listas com mesmo nome ou não existirem listas anteriores.
			if (ultimaListaObjetoAdicionado!=null && ultimaListaObjetoExemplo!=null){
				if (!ultimaListaObjetoAdicionado.getNome().equalsIgnoreCase(ultimaListaObjetoExemplo.getNome())){
					addError("Somente processos oriundos da mesma lista podem ser adicionados a essa lista.");
					return Boolean.FALSE;
				}
			} else {
				if ( ((ultimaListaObjetoAdicionado!=null) && (ultimaListaObjetoExemplo==null)) || ((ultimaListaObjetoAdicionado==null) && (ultimaListaObjetoExemplo!=null)) ) {
					addError("Somente processos oriundos da mesma lista podem ser adicionados a essa lista.");
					return Boolean.FALSE;
				}
			}
		}catch(ServiceException ex){
			addError("Erro ao verificar processos pendentes de vista.");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
				
	}


	@SuppressWarnings({ "rawtypes"})
	protected void persistirProcessoNaLista(Long idColuna, Long idObjetoIncidente, PreListaJulgamentoMotivoAlteracao motivo) throws ServiceException {
		ObjetoIncidente objetoIncidente = objetoIncidenteService.recuperarPorId(idObjetoIncidente);
		/*PreListaJulgamentoObjetoIncidente relacionamentoVelho = preListaJulgamentoService.recuperarPreListaJulgamentoObjetoIncidenteAtiva(objetoIncidente);
		
		if (relacionamentoVelho != null){
			preListaJulgamentoObjetoIncidenteService.excluir(relacionamentoVelho);
		}
		
		if (idColuna != SEM_LISTA_ID) {
			PreListaJulgamentoObjetoIncidente relacionamentoNovo = new PreListaJulgamentoObjetoIncidente();
			
			PreListaJulgamento novaPreLista = preListaJulgamentoService.recuperarPorId(idColuna);
			relacionamentoNovo.setPreListaJulgamento(novaPreLista);
			relacionamentoNovo.setObjetoIncidente(objetoIncidente);
			
			if (motivo == null)
				relacionamentoNovo.setMotivo(PreListaJulgamentoMotivoAlteracao.MANUAL);
			else
				relacionamentoNovo.setMotivo(motivo);
			
			relacionamentoNovo.setRevisado(false);
			preListaJulgamentoObjetoIncidenteService.salvar(relacionamentoNovo);
		}*/
		preListaJulgamentoObjetoIncidenteService.inserirObjetoIncidenteemPreListaJulgamento(idColuna,objetoIncidente, motivo);
	}
	
	/**
	 * Retorna o ID da lista "Sem lista"
	 */
	public Long getIdSemLista() {
		return SEM_LISTA_ID;
	}
	
	/**
	 * Processa a remoção de um objeto utilizando AJAX 
	 */
	@SuppressWarnings("rawtypes")
	public void processaRemocaoAjax() {
		long idColuna = Long.parseLong(getRemocaoAjaxId());
		String nomeObj  = getRemocaoAjaxNome();

		// Encontra, entre as colunas atuais, a lista referida
		ColunaDragAndDrop<ObjetoIncidenteDto> coluna = null;
		for(ColunaDragAndDrop<ObjetoIncidenteDto> _coluna : getColunas()) {
			if (_coluna.getId() == idColuna) {
				coluna = _coluna;
			}
		}
		
		if (coluna == null) {
			addError("Não foi possível remover o processo selecionado.");
		} else {
			ObjetoDragAndDrop<ObjetoIncidenteDto> objeto = null;

			for (ObjetoDragAndDrop<ObjetoIncidenteDto> _objeto : coluna.getObjetos()) {
				if (_objeto.getNome().equals(nomeObj))
					objeto = _objeto;
			}
			
			if (objeto == null) {
				addError("Não foi possível remover o processo selecionado.");				
			} else {
				try {
					// Remove o estado de revisado 
					objeto.setRevisado(false);
					
					// Remove da coluna de origem
					coluna.remove(objeto.getInstancia());
					
					// Adiciona na coluna de "Sem lista"
					ColunaDragAndDrop<ObjetoIncidenteDto> colunaSemLista = colunas.get(0);
					
					colunaSemLista.add(objeto.getInstancia(), objeto.getNome(), objeto.getRevisado(), objeto.getMotivo(), objeto.getRevisor(), objeto.getDataRevisao(), objeto.getObservacao());
					objeto.setColuna(colunaSemLista);
					
					// Persiste alteração
					
					ObjetoIncidente objetoIncidente = objetoIncidenteService.recuperarPorId(objeto.getInstancia().getId());
					PreListaJulgamentoObjetoIncidente relacionamento = preListaJulgamentoService.recuperarPreListaJulgamentoObjetoIncidente(objetoIncidente);
					
					if (relacionamento != null){
						preListaJulgamentoObjetoIncidenteService.excluir(relacionamento);
					}
						
				} catch (ServiceException e) {
					addError("Não foi possível remover o processo selecionado.");	
				}
			}
		}
	}
	
	/**
	 * Inicializa as colunas do drag and drop, recuperando as categorias do banco
	 */
	protected void inicializarColunasDragAndDrop() {
		colunas = new LinkedList<ColunaDragAndDrop<ObjetoIncidenteDto>>();
		
		// Cria a primeira lista (Sem lista)
		if (colunaSemLista == null)
			colunaSemLista = new ColunaDragAndDrop<ObjetoIncidenteDto>(SEM_LISTA_ID, "Sem Lista", ordenacaoNumerica);
		
		colunas.add(colunaSemLista);
		
		incluirColunasSequencialmente(getPreListasJulgamento());
	}

	public void selecionarObjetoIncidenteParaVisualizar(ObjetoIncidenteDto oi, ColunaDragAndDrop<ObjetoIncidenteDto> coluna) {
		carregarTextos(oi);
		carregarObservacao(oi);
		setObjetoIncidenteParaVisualizar(oi);
		setColunaObjetoIncidenteParaVisualizar(coluna);
	}

	@SuppressWarnings("rawtypes")
	public void salvarObservacaoIncidentePreLista() throws ServiceException {
		try {
			String textObservacao = this.getObservacao();
			if(textObservacao.length() > 500 ) {
				throw new ServiceException(MSG_ERRO_OBSERVACAO_TAMANHO);
			}else {
				Long idObjetoIncidente = objetoIncidenteParaVisualizar.getId();		
				ObjetoIncidente objetoIncidente = objetoIncidenteService.recuperarPorId(idObjetoIncidente);		
				PreListaJulgamentoObjetoIncidente incidentePreLista = preListaJulgamentoService.recuperarPreListaJulgamentoObjetoIncidente(objetoIncidente);				
				incidentePreLista.setObservacao(textObservacao);
				preListaJulgamentoObjetoIncidenteService.salvar(incidentePreLista);
			}
		} catch (ServiceException e) {
			addError(e.toString());
			//sendToErrors();
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void carregarObservacao(ObjetoIncidenteDto oi) {
		try {				
			Long idObjetoIncidente = oi.getId();		
			ObjetoIncidente objetoIncidente = objetoIncidenteService.recuperarPorId(idObjetoIncidente);		
			PreListaJulgamentoObjetoIncidente incidentePreLista = preListaJulgamentoService.recuperarPreListaJulgamentoObjetoIncidente(objetoIncidente);
			if (incidentePreLista != null ) {
					observacao = incidentePreLista.getObservacao();
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	private void carregarTextos(ObjetoIncidenteDto oi) {
		try {
			ObjetoIncidente<?> oiRecuperado = objetoIncidenteService.recuperarPorId(oi.getId());			
			if(temEmenta(oiRecuperado)) {
				try {
					objetoIncidenteService.registrarLogSistema(oi.getId(), "CONSULTA_TEXTO", "Visualizar Texto Ementa",getEmenta().getId(),"STF.TEXTOS");
				}catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			}
			if(temRelatorio(oiRecuperado)) {
				try {
					objetoIncidenteService.registrarLogSistema(oi.getId(), "CONSULTA_TEXTO", "Visualizar Texto Relatorio",getRelatorio().getId(),"STF.TEXTOS");
				}catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			}
			if(temVoto(oiRecuperado)) {
				try {
					objetoIncidenteService.registrarLogSistema(oi.getId(), "CONSULTA_TEXTO", "Visualizar Texto Voto",getVoto().getId(),"STF.TEXTOS");
				}catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	protected boolean temVoto(ObjetoIncidente<?> oiRecuperado) throws ServiceException {
		boolean retorno = false;
		Texto voto = textoService.recuperar(oiRecuperado, TipoTexto.VOTO, usuarioLogadoService.getMinistro().getId());
		if (voto != null) {
			setTextoVoto(loadConteudo(voto));
			setVoto(voto);
			retorno = true;
		} else {
			setTextoVoto(null);
		}
		return retorno;
	}

	protected boolean temRelatorio(ObjetoIncidente<?> oiRecuperado) throws ServiceException {
		boolean retorno = false;
		Texto relatorio = textoService.recuperar(oiRecuperado, TipoTexto.RELATORIO, usuarioLogadoService.getMinistro().getId());
		if (relatorio != null) {
			setTextoRelatorio(loadConteudo(relatorio));
			setRelatorio(relatorio);
			retorno = true;
		} else {
			setTextoRelatorio(null);
		}
		return retorno;
	}

	protected boolean temEmenta(ObjetoIncidente<?> oiRecuperado) throws ServiceException {
		boolean retorno = false;
		Texto ementa = textoService.recuperar(oiRecuperado, TipoTexto.EMENTA, usuarioLogadoService.getMinistro().getId());
		if (ementa != null) {
			setTextoEmenta(loadConteudo(ementa));
			setEmenta(ementa);
			retorno = true;
		} else {
			setTextoEmenta(null);
		}
		return retorno;
	}

	public String loadConteudo(Texto texto) throws ServiceException {
		String conteudo = null;
		TextoDto dto = null;
		String rtf = null;
		if (texto != null) {
			dto = TextoDto.valueOf(texto, true);
			// Carregar os dados de criação/alteração do arquivo eletronico
			ArquivoEletronicoView vwArquivoEletronico = textoServiceLocal.recuperarArquivoEletronicoViewPeloId(texto.getArquivoEletronico().getId());
			if (vwArquivoEletronico != null) {
				try {
					rtf = new String(texto.getArquivoEletronico().getConteudo(), "ISO-8859-1");
				} catch (UnsupportedEncodingException e) {
					throw new NestedRuntimeException(e);
				}
				conteudo = TextoUtils.convertRtfToHtml(rtf);
			}
		}
		textoServiceLocal.validaAcessoTextosRestritos(usuarioLogadoService.getPrincipal(), Arrays.asList(dto));
		return conteudo;
	}
	
	public void deselecionarObjetoIncidenteParaVisualizar() {
		setObjetoIncidenteParaVisualizar(null);
		setColunaObjetoIncidenteParaVisualizar(null);
	}
	
	public ObjetoIncidenteDto getObjetoIncidenteParaVisualizar() {
		return objetoIncidenteParaVisualizar;
	}

	public void setObjetoIncidenteParaVisualizar(ObjetoIncidenteDto objetoIncidenteParaVisualizar) {
		this.objetoIncidenteParaVisualizar = objetoIncidenteParaVisualizar;
	}

	public String getTextoEmenta() {
		return textoEmenta;
	}

	public void setTextoEmenta(String textoEmenta) {
		this.textoEmenta = textoEmenta;
	}

	public String getTextoRelatorio() {
		return textoRelatorio;
	}

	public void setTextoRelatorio(String textoRelatorio) {
		this.textoRelatorio = textoRelatorio;
	}

	public String getTextoVoto() {
		return textoVoto;
	}

	public void setTextoVoto(String textoVoto) {
		this.textoVoto = textoVoto;
	}
	
	public boolean getPossuiAlgumTexto() {
		boolean temTexto = textoEmenta != null || textoRelatorio != null || textoVoto != null;
		return temTexto;
	}

	public boolean getIncidenteJulgado() {
		return incidenteJulgado;
	}

	public void setIncidenteJulgado(boolean incidenteJulgado) {
		this.incidenteJulgado = incidenteJulgado;
	}
	
	@SuppressWarnings("rawtypes")
	public String recuperarUrlPecas(ObjetoIncidenteDto objetoIncidenteDto) throws ServiceException {		
		Long idObjetoIncidenteDto = objetoIncidenteDto.getId();
		ObjetoIncidente oi  = objetoIncidenteService.recuperarPorId(idObjetoIncidenteDto);
		Long idObjetoIncidente = oi.getPrincipal().getId();
		String urlPecasSupremo = globalFacesBean.getTipoAmbiente().getUrlPecasSupremo(idObjetoIncidente);
		return urlPecasSupremo;	
	}

	public Long getIdColunaSelecionada() {
		return idColunaSelecionada;
	}
	
	public boolean hasColunaSelecionada(){
		return idColunaSelecionada != null;
	}

	public void setIdColunaSelecionada(Long idColunaSelecionada) {
		if (idColunaSelecionada == 0)
			idColunaSelecionada = null;
		this.idColunaSelecionada = idColunaSelecionada;
	}
	public boolean exibirColuna(Long coluna){
		if (idColunaSelecionada == null)
			return true;
		return idColunaSelecionada.equals(coluna);
	}
	
	
	@SuppressWarnings("unchecked")
	public ActionInterface<PreListaJulgamento> getAcaoExportarPreListaJulgamento(Long idLista) {
		
		ActionInterface<PreListaJulgamento> newAction = (ActionInterface<PreListaJulgamento>) getApplicationContext().getBean("exportarPreListaProcessosActionFacesBean");

		PreListaJulgamento preLista = new PreListaJulgamento();
		preLista.setId(idLista);
		
		Set<PreListaJulgamento> lista = new HashSet<PreListaJulgamento>();
		lista.add(preLista);
						
		newAction.setResources(lista);
		return newAction;
	}
	
	@SuppressWarnings("unchecked")
	public ActionInterface<PreListaJulgamento> getAcaoGerenciarListasLiberadas() {
		
		ActionInterface<PreListaJulgamento> newAction = (ActionInterface<PreListaJulgamento>) getApplicationContext().getBean("gerenciarListasLiberadasActionFacesBean");
		
		PreListaJulgamento preLista = new PreListaJulgamento();
		preLista.setId((long) 1);

		Set<PreListaJulgamento> lista = new HashSet<PreListaJulgamento>();
		lista.add(preLista);
		
		newAction.setResources(lista);
		return newAction;
	}	
	
	@SuppressWarnings("unchecked")
	public ActionInterface<PreListaJulgamento> getAcaoLiberarParaJulgamento(Long idLista) {
		
		ActionInterface<PreListaJulgamento> newAction = (ActionInterface<PreListaJulgamento>) getApplicationContext().getBean("liberarPreListaParaJulgamentoActionFacesBean");
		
		PreListaJulgamento preLista = new PreListaJulgamento();
		preLista.setId(idLista);

		Set<PreListaJulgamento> lista = new HashSet<PreListaJulgamento>();
		lista.add(preLista);
		
		if (idLista <= PreListaJulgamentoDaoHibernate.LIMITE_PRE_LISTA_DESTAQUES_CANCELADOS)
			newAction = (ActionInterface<PreListaJulgamento>) getApplicationContext().getBean("liberarPreListaDestaquesCanceladosActionFacesBean");
		
		newAction.setResources(lista);
		return newAction;
	}
	
	@SuppressWarnings("unchecked")
	public ActionInterface<PreListaJulgamento> getAcaoCriarPreLista() {
		ActionInterface<PreListaJulgamento> newAction = (ActionInterface<PreListaJulgamento>)getApplicationContext().getBean("criarPreListaActionFacesBean");
		newAction.setResources(new HashSet<PreListaJulgamento>(Arrays.asList(new PreListaJulgamento())));
		return newAction;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public ColunaDragAndDrop<ObjetoIncidenteDto> getColunaObjetoIncidenteParaVisualizar() {
		return colunaObjetoIncidenteParaVisualizar;
	}

	public void setColunaObjetoIncidenteParaVisualizar(
			ColunaDragAndDrop<ObjetoIncidenteDto> colunaObjetoIncidenteParaVisualizar) {
		this.colunaObjetoIncidenteParaVisualizar = colunaObjetoIncidenteParaVisualizar;
	}

	public List<PreListaJulgamento> getPreListasJulgamento() {
		return preListasJulgamento;
	}
	
	
	private void incluirColunasSequencialmente(List<PreListaJulgamento> preListasJulgamentoPersistidas) {
		for (PreListaJulgamento preListaJulgamento : preListasJulgamentoPersistidas){
			ColunaDragAndDrop<ObjetoIncidenteDto> coluna = new ColunaDragAndDrop<ObjetoIncidenteDto>(preListaJulgamento.getId(), preListaJulgamento.getNome(), ordenacaoNumerica);
			colunas.add(coluna);

			List<PreListaJulgamentoObjetoIncidente> relacionamentos = preListaJulgamento.getObjetosIncidentes();
			String revisor = "";
			
			for (PreListaJulgamentoObjetoIncidente relacionamento : relacionamentos) {
				ObjetoIncidenteDto objIncidenteDto = ObjetoIncidenteDto.valueOf(relacionamento.getObjetoIncidente());
				
				if (relacionamento.getUsuarioRevisor() != null)
					revisor = relacionamento.getUsuarioRevisor().getNome();
				
				String observacao = null;
				
				for (ObservacaoProcesso op : relacionamento.getObjetoIncidente().getPrincipal().getObservacaoProcesso())
					if (op.getSetor().equals(usuarioLogadoService.getMinistro().getSetor()))
						observacao = op.getObservacao();
				
				coluna.add(objIncidenteDto, objIncidenteDto.toString(), relacionamento.getRevisado(), relacionamento.getMotivo(), revisor, relacionamento.getDataRevisao(), observacao);
			}
		}
	}

	
	public void incluirColuna(PreListaJulgamento preListaJulgamento) {
		ColunaDragAndDrop<ObjetoIncidenteDto> coluna = new ColunaDragAndDrop<ObjetoIncidenteDto>(preListaJulgamento.getId(), preListaJulgamento.getNome(), ordenacaoNumerica);
		colunas.add(1,coluna); // Quando incluir uma nova pré-lista, ela deve ser a primeira da tela
	}

	public void atualizarColuna(PreListaJulgamento preListaJulgamento) {
		for(PreListaJulgamento preLista : getPreListasJulgamento()) {
			if (preLista.getId().equals(preListaJulgamento.getId()))
				preLista.setObjetosIncidentes(preListaJulgamento.getObjetosIncidentes());
		}

		for (ColunaDragAndDrop<ObjetoIncidenteDto> coluna : colunas) {
			if (preListaJulgamento.getId().equals(coluna.getId())) {
				coluna.setNome(preListaJulgamento.getNome());
				
				coluna.setObjetos(new HashMap<ObjetoIncidenteDto,ObjetoDragAndDrop<ObjetoIncidenteDto>>());

				if (preListaJulgamento.getObjetosIncidentes()!=null)
					for (PreListaJulgamentoObjetoIncidente preListaOi : preListaJulgamento.getObjetosIncidentes()) {
						Long idObjetoIncidente = preListaOi.getObjetoIncidente().getId();
						preListaOi.setObjetoIncidente(objetoIncidenteServiceLocal.recuperarObjetoIncidentePorId(idObjetoIncidente));
					}
				
				List<PreListaJulgamentoObjetoIncidente> relacionamentos = preListaJulgamento.getObjetosIncidentes(ordenacaoNumerica);
				String revisor = "";
				
				if (relacionamentos != null) {
					String observacao = null;
					for (PreListaJulgamentoObjetoIncidente relacionamento : relacionamentos) {
						ObjetoIncidenteDto objIncidenteDto = ObjetoIncidenteDto.valueOf(relacionamento.getObjetoIncidente());
						if (relacionamento.getUsuarioRevisor() !=null )
							revisor = relacionamento.getUsuarioRevisor().getNome();
						
						for (ObservacaoProcesso observacaoProcesso : relacionamento.getObjetoIncidente().getPrincipal().getObservacaoProcesso())
							if (usuarioLogadoService.getMinistro().getSetor().equals(observacaoProcesso.getSetor()))
								observacao = observacaoProcesso.getObservacao();
						
						coluna.add(objIncidenteDto, objIncidenteDto.toString(), relacionamento.getRevisado(), relacionamento.getMotivo(), revisor, relacionamento.getDataRevisao(), observacao);						
					}
				}
			}
		}
	}

	public void removerColuna(PreListaJulgamento preListaJulgamento) {
		ColunaDragAndDrop<ObjetoIncidenteDto> colunaARemover = null;
		
		for (ColunaDragAndDrop<ObjetoIncidenteDto> coluna : colunas) {
			if (coluna.getId() == preListaJulgamento.getId()) {
				colunaARemover = coluna;
			}
		}
		
		colunas.remove(colunaARemover);
	}

	public Boolean getReabrirModalListasLiberadas() {
		return reabrirModalListasLiberadas;
	}

	public void setReabrirModalListasLiberadas(Boolean reabrirModal) {
		this.reabrirModalListasLiberadas = reabrirModal;
	}
	
	public Date getDataLiberacao() {
		return dataLiberacao;
	}

	public void setDataLiberacao(Date dataLiberacao) {
		this.dataLiberacao = dataLiberacao;
	}
	
	public String getDataInicioJulgamento() {
		return dataInicioJulgamento;
	}

	public void setDataInicioJulgamento(String dataJulgamento) {
		this.dataInicioJulgamento = dataJulgamento;
	}
	
	public void simularDataJulgamento() {
		if (idColegiado != null && dataLiberacao != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Colegiado colegiado = new Colegiado();
			colegiado.setId(idColegiado);
			
			try {
				Calendar dataLiberacaoCalendar = (Calendar) Calendar.getInstance().clone();
				dataLiberacaoCalendar.setTime(dataLiberacao);
				boolean ignorarCpc = false;
				
				Sessao sessao = sessaoService.recuperarSessao(dataLiberacaoCalendar, colegiado, ignorarCpc, TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO);

				setDataInicioJulgamento(sdf.format(sessao.getDataPrevistaInicio()));
				setDataFimJulgamento(sdf.format(sessao.getDataPrevistaFim()));
				setMemoriaCalculo(sessao.getMemoriaCalculo());
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		} else {
			dataInicioJulgamento = null;
		}
	}

	public String getIdColegiado() {
		return idColegiado;
	}

	public void setIdColegiado(String idColegiado) {
		this.idColegiado = idColegiado;
	}
	
	public boolean isPautaFechada() throws ServiceException {
		
		if (getIdColegiado() != null) {
			Colegiado colegiado = new Colegiado();
			colegiado.setId(getIdColegiado());
			return conteudoPublicacaoService.isPautaFechada(colegiado, DataUtil.date2Calendar(getDataLiberacao()));
		}
		
		return false;
	}

	public String getDataFimJulgamento() {
		return dataFimJulgamento;
	}

	public void setDataFimJulgamento(String dataFimJulgamento) {
		this.dataFimJulgamento = dataFimJulgamento;
	}

	public String getMemoriaCalculo() {
		return memoriaCalculo;
	}

	public void setMemoriaCalculo(String memoriaCalculo) {
		this.memoriaCalculo = memoriaCalculo;
	}
}