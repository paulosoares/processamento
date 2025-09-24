package br.jus.stf.estf.decisao.pesquisa.web.incidente;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.springframework.security.context.SecurityContextHolder;

import br.gov.stf.estf.cabecalho.model.CabecalhosObjetoIncidente.CabecalhoObjetoIncidente;
import br.gov.stf.estf.cabecalho.model.OcorrenciasMinistro;
import br.gov.stf.estf.cabecalho.service.CabecalhoObjetoIncidenteService;
import br.gov.stf.estf.documento.model.service.RotuloService;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.ControleVoto;
import br.gov.stf.estf.entidade.documento.Rotulo;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoAdendo;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ManifestacaoRepresentante;
import br.gov.stf.estf.entidade.julgamento.PecaInformacaoPautaProcesso;
import br.gov.stf.estf.entidade.julgamento.PrevisaoImpedimentoMinistro;
import br.gov.stf.estf.entidade.julgamento.PrevisaoSustentacaoOral;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.Tema;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.ministro.MinistroPresidente;
import br.gov.stf.estf.entidade.processosetor.HistoricoDeslocamento;
import br.gov.stf.estf.entidade.processosetor.HistoricoDistribuicao;
import br.gov.stf.estf.entidade.processosetor.ProcessoSetor;
import br.gov.stf.estf.entidade.processostf.Agendamento;
import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.entidade.processostf.Categoria;
import br.gov.stf.estf.entidade.processostf.DeslocaProcesso;
import br.gov.stf.estf.entidade.processostf.IncidentePreferencia;
import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ObservacaoProcesso;
import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.entidade.processostf.PrescricaoReu;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ReferenciaPrescricao;
import br.gov.stf.estf.entidade.processostf.SituacaoMinistroProcesso;
import br.gov.stf.estf.entidade.processostf.TipoRecurso;
import br.gov.stf.estf.entidade.processostf.TipoVinculoObjeto;
import br.gov.stf.estf.entidade.processostf.enuns.SituacaoIncidenteJulgadoOuNao;
import br.gov.stf.estf.julgamento.model.service.InformacaoPautaProcessoService;
import br.gov.stf.estf.julgamento.model.service.JulgamentoProcessoService;
import br.gov.stf.estf.julgamento.model.service.ManifestacaoLeituraService;
import br.gov.stf.estf.julgamento.model.service.ManifestacaoRepresentanteService;
import br.gov.stf.estf.julgamento.model.service.PecaInformacaoPautaProcessoService;
import br.gov.stf.estf.julgamento.model.service.TemaService;
import br.gov.stf.estf.ministro.model.service.MinistroPresidenteService;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.service.AgendamentoService;
import br.gov.stf.estf.processostf.model.service.AgrupadorService;
import br.gov.stf.estf.processostf.model.service.CategoriaService;
import br.gov.stf.estf.processostf.model.service.DeslocaProcessoService;
import br.gov.stf.estf.processostf.model.service.ObservacaoProcessoService;
import br.gov.stf.estf.processostf.model.service.ParteService;
import br.gov.stf.estf.processostf.model.service.PrevisaoImpedimentoMinistroService;
import br.gov.stf.estf.processostf.model.service.VinculoObjetoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.objetoincidente.web.LiberarParaJulgamentoActionFacesBean.PrevisaoSustentacaoOralDto;
import br.jus.stf.estf.decisao.objetoincidente.web.LiberarParaJulgamentoActionFacesBean.ProcessoVinculadoDto;
import br.jus.stf.estf.decisao.objetoincidente.web.LiberarParaJulgamentoActionFacesBean.ProcessoVinculadoDto.TipoVinculacao;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.pesquisa.domain.Pesquisa;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;
import br.jus.stf.estf.decisao.pesquisa.service.PesquisaService;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.controller.context.FacesBean;
import br.jus.stf.estf.decisao.support.controller.faces.datamodel.DataModel;
import br.jus.stf.estf.decisao.support.controller.faces.datamodel.ListDataModel;
import br.jus.stf.estf.decisao.support.controller.faces.datamodel.PagedList;
import br.jus.stf.estf.decisao.support.security.PermissionChecker;
import br.jus.stf.estf.decisao.support.security.Principal;
import br.jus.stf.estf.decisao.support.util.NestedRuntimeException;
import br.jus.stf.estf.decisao.support.util.ReportUtils;
import br.jus.stf.estf.decisao.support.util.TextoUtils;
import br.jus.stf.estf.decisao.texto.service.TextoService;
import br.jus.stf.estf.montadortexto.impl.OpenOfficeMontadorTextoServiceImpl;

/**
 * Bean JSF (Seam Component) para controle e tratamento de eventos de tela associados a 
 * Objetos Incidente. Usado pelo mecanismo de pesquisa para recuperação e edição de informações.
 * 
 * <p>Implementação <code>FacesBean</code> para Objeto Incidente.
 * 
 * @author Rodrigo Barreiros
 * @since 30.04.2010
 */
@Name("incidenteFacesBean")
@Scope(ScopeType.CONVERSATION)
public class IncidenteFacesBean implements FacesBean<ObjetoIncidenteDto> {
	
	public enum TipoDeListaParaExibicao {TEXTOS_DO_MINISTRO, TODOS_TEXTOS}
	
    private TipoDeListaParaExibicao tipoDeListaParaExibicao = TipoDeListaParaExibicao.TEXTOS_DO_MINISTRO;
    
	private ObjetoIncidente<?> objetoIncidente;
	private ListDataModel<TextoDto> textos;
	private List<Ministro> ministros;
	private List<SelectItem> categoriasParte;
	private List<Parte> listaParte;
	private HistoricoDeslocamento deslocamentoAtual;
	private HistoricoDistribuicao distribuicaoAtual;
	private StringBuffer preferencias;
	private List<PrescricaoReu> prescricaoReu;
	private String ministroRelator;
	private String ministraRelatora;
	private String ministroRedatorAcordao;
	private String ministraRedatoraAcordao;
	private InformacaoPautaProcesso informacaoPautaProcesso;
	private Date dataDistribuicao;
	private List<ProcessoVinculadoDto> processosVinculados;
	private List<PrevisaoSustentacaoOralDto> sustentacoesOrais;
	private Agendamento agendamento;
	private String colegiadoConsultaPauta;
	private Date dataPrevistaJulgamento;
	private String temaRepercussaoGeral;
	private List<String> decisoes;
	private List<PecaInformacaoPautaProcesso> listaDocumentosVinculados;
	private List<PrevisaoImpedimentoMinistro> listaMinistrosImpedidos;
	private List<ItemEspelho> itensEspelho;
	private List<Rotulo> rotulos;
	private ObservacaoProcesso observacaoProcesso;
	private String situacaoJulgamento;
	private List<ManifestacaoRepresentante> arquivosDeJulgamento = new ArrayList<ManifestacaoRepresentante>();
	
	@In("#{permissionChecker}")
	private PermissionChecker permissionChecker;
	
	@In("#{ministroPresidenteService}")
	private MinistroPresidenteService ministroPresidenteService;
	
	public List<PrevisaoImpedimentoMinistro> getListaMinistrosImpedidos() {
		if ( listaMinistrosImpedidos == null ){
			listaMinistrosImpedidos = new ArrayList<PrevisaoImpedimentoMinistro>();
		}
		return listaMinistrosImpedidos;
	}

	public void setListaMinistrosImpedidos(List<PrevisaoImpedimentoMinistro> listaMinistrosImpedidos) {
		this.listaMinistrosImpedidos = listaMinistrosImpedidos;
	}

	public List<PecaInformacaoPautaProcesso> getListaDocumentosExternosVinculados() {
		if ( listaDocumentosVinculados == null ){
			listaDocumentosVinculados = new ArrayList<PecaInformacaoPautaProcesso>();
		}
		return listaDocumentosVinculados;
	}

	public void setListaDocumentosExternosVinculados(
			List<PecaInformacaoPautaProcesso> listaDocumentosVinculados) {
		this.listaDocumentosVinculados = listaDocumentosVinculados;
	}

	@In("#{objetoIncidenteServiceLocal}")
	private ObjetoIncidenteService objetoIncidenteService;
    
	@In("#{pesquisaService}")
	private PesquisaService pesquisaService;
	
	@In("#{textoServiceLocal}")
	private TextoService textoService;
	
	@In("#{ministroService}")
	private MinistroService ministroService;
	
	@In("#{categoriaService}")
	private CategoriaService categoriaService;
	
	@In("#{parteService}")
	private ParteService parteService;

	@In("#{cabecalhoObjetoIncidenteService}")
	private CabecalhoObjetoIncidenteService cabecalhoObjetoIncidenteService;

	@In("#{informacaoPautaProcessoService}")
	private InformacaoPautaProcessoService informacaoPautaProcessoService;

	@In("#{vinculoObjetoService}")
	private VinculoObjetoService vinculoObjetoService;

	@In("#{agendamentoService}")
	private AgendamentoService agendamentoService;

	@In
	private FacesMessages facesMessages;

	@In("#{julgamentoProcessoService}")
	private JulgamentoProcessoService julgamentoProcessoService;

	@In("#{temaService}")
	private TemaService temaService;
	
	@In("#{pecaInformacaoPautaProcessoService}")
	private PecaInformacaoPautaProcessoService pecaInformacaoPautaProcessoService;
	
	@In("#{previsaoImpedimentoMinistroService}")
	private PrevisaoImpedimentoMinistroService previsaoImpedimentoMinistroService;

	@In("#{rotuloService}")
	private RotuloService rotuloService;
	
	@In("#{observacaoProcessoService}")
	private ObservacaoProcessoService observacaoProcessoService;

	@In("#{agrupadorService}")
	private AgrupadorService agrupadorService;
	
	@Logger
	private Log logger;
	
	@In("#{deslocaProcessoService}")
	private DeslocaProcessoService deslocaProcessoService;
	
	@In("#{manifestacaoRepresentanteService}")
	private ManifestacaoRepresentanteService manifestacaoRepresentanteService;
	
	@In("#{manifestacaoLeituraService}")
	private ManifestacaoLeituraService manifestacaoLeituraService;
	
	/**
	 * Pesquisa os incidentes dado o identicador (sigla e número) do processo a
	 * que o objeto procurado está associado.
	 * 
	 * <p>
	 * A busca só será executada se o usuário informar o número ou parte dele.
	 * 
	 * @param suggest
	 *            o identificador do processo
	 * 
	 * @return a lista de objetos associados ao processo
	 */
	public List<ObjetoIncidenteDto> search(Object suggest) {
		return pesquisaService.pesquisarObjetosIncidente(suggest.toString());
	}
    
	public List<ObjetoIncidenteDto> searchWithoutFake(Object suggest) {
		return pesquisaService.pesquisarObjetosIncidentes(suggest.toString(), false);
	}
	
	private String categoria;
	public String getCategoria() {
		return categoria; 
	}

	public void setCategoria(String categoriaTxt) {
		this.categoria = categoriaTxt;
	}

	public List<Agrupador> recuperarCategoriasDoSetor(Object suggest) throws ServiceException {
		String termo = (String) suggest;
		List<Agrupador> l = new ArrayList<Agrupador>();
		
		if (suggest != null && getMinistro() != null && getMinistro().getSetor() != null && getMinistro().getSetor().getId() != null) {
			Long idSetor = this.getMinistro().getSetor().getId();
			
			if (termo.equals(""))
				 l = agrupadorService.recuperarCategoriasDoSetor(idSetor);
			else
				 l = agrupadorService.recuperarCategoriasDoSetor(idSetor, termo);
		}
		
		Agrupador a = new Agrupador();
		a.setDescricao("« Remover categoria »");
		a.setId(-1L);
		
		l.add(0, a);
		return l;
	}
	public void categorizarIncidente(Agrupador result){
		try {
			Long remover = result.getId();
			
			agrupadorService.categorizarIncidente(getObjetoIncidente(), result, getMinistro().getSetor());
			
			if (!remover.equals((long) -1))
				this.setCategoria(result.getDescricao());
			else
				this.setCategoria("");
		} catch (Exception e) {
			FacesMessages.instance().add(Severity.ERROR, e.getMessage());
		}
		
	}
	private void carregaCategoriaIncidente(){
		try {
			if (this.getMinistro() != null)
				setCategoria(
					agrupadorService.getCategoriaDoIncidente(
						getObjetoIncidente().getId(), 
						this.getMinistro().getSetor().getId()
					)
				);
		} catch (ServiceException e) {
			FacesMessages.instance().add(Severity.ERROR, e.getMessage());
		}
	}
	/**
	 * @see br.jus.stf.estf.decisao.support.controller.context.FacesBean#search(Pesquisa, int, int)
	 */
	@Override
	public PagedList<ObjetoIncidenteDto> search(Pesquisa pesquisa, int first, int max) {
		pesquisa.setFirstResult(first);
		pesquisa.setMaxResults(max);
		return pesquisaService.pesquisarObjetosIncidente(pesquisa);
	}
	
    /**
     * Carrega informações do Objeto Incidente, a lista de textos produzidos pelo ministro do
     * gabinete do usuário logado e a lista de textos produzidos por outros ministros.
     * 
     * @param objetoIncidenteDto o objeto incidente selecionado
     */
	@Override
	public ObjetoIncidenteDto load(ObjetoIncidenteDto objetoIncidenteDto) {
		try {
			// Carregando o objeto incidente selecionado no suggestion box da
			// pesquisa principal...
			logger.info("Carregando Objeto Incidente [#0]...", objetoIncidenteDto);
			this.objetoIncidente = objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidenteDto.getId());
			Hibernate.initialize(objetoIncidente.getPrincipal());
			Hibernate.initialize(((Processo) objetoIncidente.getPrincipal()).getAssuntos());
			Hibernate.initialize(((Processo) objetoIncidente.getPrincipal()).getMinistroRelatorAtual());
			Hibernate.initialize(objetoIncidente.getListasProcessos());
			Hibernate.initialize(objetoIncidente.getAnterior());
			objetoIncidenteDto = ObjetoIncidenteDto.valueOf(objetoIncidente);

			// Recuperando textos associados ao objeto incidente selecionado que
			// foram produzidos pelo
			// ministro do gabinete do usuário logado...
			if (getMinistro() != null) {
				this.textos = getTextosDoMinistro();
				this.setTipoDeListaParaExibicao(TipoDeListaParaExibicao.TEXTOS_DO_MINISTRO);
			} else {
				this.textos = getTodosTextos();
				this.setTipoDeListaParaExibicao(TipoDeListaParaExibicao.TODOS_TEXTOS);
			}

			carregarInformacoesEGab(objetoIncidenteDto);
			carregarPreferencias();
			carregarInformacoesPrescricao();
			carregarInformacoesRelator(objetoIncidenteDto);

			informacaoPautaProcesso = informacaoPautaProcessoService.recuperar(objetoIncidente);
			if (informacaoPautaProcesso != null) {
				if (informacaoPautaProcesso.getSustentacoesOrais() != null) {
					Hibernate.initialize(informacaoPautaProcesso.getSustentacoesOrais());
				}
				if (informacaoPautaProcesso.getSustentacoesOrais() != null
						&& informacaoPautaProcesso.getSustentacoesOrais().size() > 0) {
					for (PrevisaoSustentacaoOral pso : informacaoPautaProcesso.getSustentacoesOrais()) {
						Hibernate.initialize(pso.getRepresentado());
						Hibernate.initialize(pso.getEnvolvido());
						Hibernate.initialize(pso.getJurisdicionado());
					}
				}
				if (informacaoPautaProcesso.getSubtemaPauta() != null) {
					Hibernate.initialize(informacaoPautaProcesso.getSubtemaPauta());
					if (informacaoPautaProcesso.getSubtemaPauta().getTemaPauta() != null) {
						Hibernate.initialize(informacaoPautaProcesso.getSubtemaPauta().getTemaPauta());
						if (informacaoPautaProcesso.getSubtemaPauta().getTemaPauta().getPautaPlenario() != null) {
							Hibernate.initialize(informacaoPautaProcesso.getSubtemaPauta().getTemaPauta().getPautaPlenario());
						}
					}
				}
			}
			carregarDataDistribuicao();
			carregarRotulos();
			carregarObservacaoProcesso();
			carregarSituacaoJulgamento();
			carregaCategoriaIncidente();
			return objetoIncidenteDto;
		} catch (ServiceException e) {
			throw new NestedRuntimeException(e);
		}

	}
	private void carregarObservacaoProcesso() {
		if (getMinistro() != null){
			observacaoProcesso = observacaoProcessoService.pesquisarObservacaoProcesso(getObjetoIncidente(), getMinistro().getSetor());
			if (observacaoProcesso == null) {
				preencherNovaObservacaoProcessoVazia();
			}
		}
	}

	private void preencherNovaObservacaoProcessoVazia() {
		observacaoProcesso = new ObservacaoProcesso();
		observacaoProcesso.setObservacao("");
		observacaoProcesso.setTipoAdendo(TipoAdendo.OBSERVACAO);
		observacaoProcesso.setObjetoIncidente(getObjetoIncidente().getPrincipal());
		observacaoProcesso.setSetor(getMinistro().getSetor());
	}

	private void carregarRotulos() throws ServiceException {
		rotulos = rotuloService.pesquisarRotulos(getObjetoIncidente(), getMinistro() != null ? getMinistro().getSetor() : null);
	}
	
	private void carregarSituacaoJulgamento() throws ServiceException {
		try {
			situacaoJulgamento = objetoIncidenteService.recuperarSituacaoJulgamentoIncidente(objetoIncidente.getId()).getDescricao();
		} catch (ServiceException e) {
			logger.error("Erro ao carregar a situação de Julgamento do Processo.", e.getMessage());
			throw e;
		}
	}
	
	public String checkBrightness(String hexColor) {
		// remove hash character from string
		String rawColor = hexColor.substring(1, hexColor.length());

		// convert hex string to int
		int rgb = Integer.parseInt(rawColor, 16);

		Color c = new Color(rgb);

		float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);

		float brightness = hsb[2];

		if (brightness < 0.5) {
		   return "white";
		} else {
		   return "black";
		}
	}

	/**
	 * Carrega os dados da Pauta para o Processo
	 * @param objetoIncidenteDto
	 * @throws ServiceException */
	public void carregarInformacaoPautaProcesso(ObjetoIncidenteDto objetoIncidenteDto) throws ServiceException {
		/* Busca informações a respeito do Colegiado */
		carregarColegiadoDoProcesso( objetoIncidente );
		/* Carrega a data prevista para início do Julgamento do Processo */
		carregarDataPrevistaDoJulgamento( objetoIncidente );
		/* Carrega os Textos de Decisão de um processo */
		carregarDecisoesDoProcesso( objetoIncidenteDto );
		/* Carrega a lista de processos vínculados ao processo */
		carregarProcessosVinculados( objetoIncidente );
		/* Carrega as informações relacionadas a Sustentação Oral */
		carregarListaSustentacoesOrais( informacaoPautaProcesso );
		/* Carrega a lista de Ministros impedidos */
		carregarListaMinistroImpedido( objetoIncidente );
		/* Carrega os dados de Repercussao Geral */
		carregarDadosDaRepercussaoGeral( objetoIncidente );
		/* Carrega a lista de Documentos Externos vinculados */
		carregarListaDocumentoVinculado( informacaoPautaProcesso );
		/* Carrega o espelho */
		carregarItensEspelho( informacaoPautaProcesso );
	}
	
	/**
	 * Carrega o Colegiado de Julgamento do Processo 
	 * @param objetoIncidente O processo para se obter o Colegiado. */
	private void carregarColegiadoDoProcesso(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		colegiadoConsultaPauta = new String();
		try {
			List<Agendamento> agendamentos = agendamentoService.pesquisar( objetoIncidente );
			if ( agendamentos != null && agendamentos.size() > 0 ) {
				colegiadoConsultaPauta = TipoColegiadoConstante.valueOfCodigoCapitulo( agendamentos.get( 0 ).getId().getCodigoCapitulo() ).getDescricao();
			}
		} catch (ServiceException e) {
			logger.error("Erro ao carregar a informação sobre o colegiado do Processo.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Carrega a Data prevista de julgamento do processo
	 * @param objetoIncidente O processo para se obter a data do Julgamento. */
	public void carregarDataPrevistaDoJulgamento(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		dataPrevistaJulgamento = null;
		try {
			JulgamentoProcesso julgamentoProcesso = julgamentoProcessoService.pesquisaSessaoNaoFinalizada( objetoIncidente, TipoAmbienteConstante.PRESENCIAL );
			if (julgamentoProcesso != null) {
				dataPrevistaJulgamento = julgamentoProcesso.getSessao().getDataInicio();
			}
		} catch (ServiceException e) {
			logger.error("Erro ao carregar a data prevista de Julgamento do Processo.", e.getMessage());
			throw e;
		}
	}

	/** 
	 * Carrega os Textos de Decisão de um processo
	 * @param objetoIncidente O Processo do qual se pretende buscar as decisões. */
	private void carregarDecisoesDoProcesso(ObjetoIncidenteDto objetoIncidenteDto)throws ServiceException {
		decisoes = new ArrayList<String>();
		try {
			List<Texto> textosDecisoes = textoService.pesquisar(objetoIncidenteDto, TipoTexto.DECISAO, null);
			if (textosDecisoes != null && textosDecisoes.size() > 0) {
				for (Texto decisao : textosDecisoes) {
					String conteudo = new String(decisao.getArquivoEletronico().getConteudo(),"ISO-8859-1");
					conteudo = TextoUtils.converterRtfToString( conteudo.getBytes() );
					decisoes.add( conteudo );
				}
			}
		} catch (ServiceException e) {
			logger.error("Erro ao recuperar os Textos de Decisão.", e);
			throw e;
		} catch (UnsupportedEncodingException e) {
			logger.error("Erro ao criar a String com o Encoding ISO-8859-1.", e);
		}
	}
	
	/**
	 * Retorna o principal autenticado no sistema.
	 * 
	 * @return o principal de autenticação
	 */
	public Principal getPrincipal() {
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return principal;
	}
	
	/** 
	 * Carrega a lista de Vínculos para o Processo, seja Julgamento Conjunto 
	 * ou Depende do Julgamento. 
	 * @param objetoIncidente O objeto do qual se pretende carregar os vínculos. */
	private void carregarProcessosVinculados(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		processosVinculados = new ArrayList<ProcessoVinculadoDto>();
		/* Carrega os processos cujo vínculo é Julgamento Conjunto */
		List<ObjetoIncidente<?>> julgamentoConjunto = informacaoPautaProcessoService.recuperarProcessosJulgamentoConjunto(objetoIncidente, false);
		for (ObjetoIncidente<?> oi : julgamentoConjunto) {
			ProcessoVinculadoDto pv = new ProcessoVinculadoDto();
			InformacaoPautaProcesso ipp = informacaoPautaProcessoService.recuperar( oi );
			pv.setIdListaJulgamentoConjunto( ipp.getSeqListaJulgamentoConjunto() );
			pv.setVinculacao( TipoVinculacao.JULGAMENTO_CONJUNTO );
			pv.setObjetoIncidente( ObjetoIncidenteDto.valueOf( ipp.getObjetoIncidente() ) );
			processosVinculados.add( pv );
		}
		/* Carrega os processos cujo vínculo é Depende do Julgamento */
		List<ObjetoIncidente<?>> precedentes = vinculoObjetoService.recuperarObjetosIncidenteVinculados(objetoIncidente, TipoVinculoObjeto.DEPENDE_DO_JULGAMENTO);
		for (ObjetoIncidente<?> oi : precedentes) {
			Hibernate.initialize(oi.getPrincipal());
			ProcessoVinculadoDto pv = new ProcessoVinculadoDto();
			pv.setObjetoIncidente( ObjetoIncidenteDto.valueOf( oi ) );
			pv.setVinculacao( TipoVinculacao.DEPENDE_DO_JULGAMENTO );
			processosVinculados.add( pv );
		}
		/* Ordena a lista de Processos */
		Collections.sort(processosVinculados, new Comparator<ProcessoVinculadoDto>() {
			@Override
			public int compare(ProcessoVinculadoDto o1, ProcessoVinculadoDto o2) {
				return o1.getObjetoIncidente().getIdentificacao().compareToIgnoreCase( o2.getObjetoIncidente().getIdentificacao() );
			}
		});
	}

	/**
	 * Carrega a Lista de Sustentações Orais
	 * @param informacaoPautaProcesso A informação de pauta de um processo, de onde se obtém a lista de Sustentação Oral */
	private void carregarListaSustentacoesOrais(InformacaoPautaProcesso informacaoPautaProcesso) throws ServiceException {
		sustentacoesOrais = new ArrayList<PrevisaoSustentacaoOralDto>();
		if (informacaoPautaProcesso.getSustentacoesOrais() != null) {
			for (PrevisaoSustentacaoOral pso : informacaoPautaProcesso.getSustentacoesOrais()) {
				sustentacoesOrais.add( PrevisaoSustentacaoOralDto.valueOf( pso ) );
			}
		}
	}

	/**
	 * Carrega a Lista de Ministros Impedidos 
	 * @param objetoIncidente O processo do qual serão resgatado os Ministros impedidos do Julgamento. */
	public void carregarListaMinistroImpedido(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		listaMinistrosImpedidos = new ArrayList<PrevisaoImpedimentoMinistro>();
		if ( objetoIncidente != null ) {
			try {
				listaMinistrosImpedidos = previsaoImpedimentoMinistroService.recuperar( objetoIncidente );
			} catch (ServiceException e) {
				logger.error("Erro ao carregar os Ministros Impedidos.");
				throw e;
			}
		}
	}

	/** 
	 * Carrega os dados da Repercussão Geral
	 * @param objetoIncidente O Processo a se obter os dados da Repercussão Geral */
	private void carregarDadosDaRepercussaoGeral(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		temaRepercussaoGeral = new String();
		try {
			/* Carrega os dados de Repercussao Geral */
			Tema tema = temaService.recuperarTemas( objetoIncidente.getId() );
			if (tema != null) {
				temaRepercussaoGeral = tema.getTituloTema() + " - " + tema.getDescricao();
				if ( tema.getTituloTema() != null ){
					temaRepercussaoGeral = tema.getTituloTema() + " - " + tema.getDescricao();
				} else {
					temaRepercussaoGeral = tema.getDescricao();
				}
			}
		} catch (ServiceException e) {
			logger.error("Erro ao carregar os dados da Repercussão Geral.", e.getMessage());
			throw e;
		}
	}
	
  	/** 
  	 * Carrega as informações relacionada aos Documentos Externos Vinculados 
  	 * @param informacaoPautaProcesso*/
	public void carregarListaDocumentoVinculado(InformacaoPautaProcesso informacaoPautaProcesso) throws ServiceException {
		/* Reinicia a lista de documentos vinculados */
		try {
			listaDocumentosVinculados = pecaInformacaoPautaProcessoService.recuperar( informacaoPautaProcesso, false );
			
			for (PecaInformacaoPautaProcesso pipp : listaDocumentosVinculados) {
				if (pipp.getPecaProcessoEletronico() != null) {
					Hibernate.initialize(pipp.getPecaProcessoEletronico());
					Hibernate.initialize(pipp.getPecaProcessoEletronico().getDocumentos());
					for (ArquivoProcessoEletronico ape : pipp.getPecaProcessoEletronico().getDocumentos()){
						Hibernate.initialize(ape.getDocumentoEletronico());
					}
				}
			}
		} catch (ServiceException e){
			logger.error("Erro ao carregar a lista de Documentos Externos.", e.getMessage());
			throw e;
		}
	}
	
	public void carregarItensEspelho(InformacaoPautaProcesso informacaoPautaProcesso) {
		itensEspelho = new ArrayList<ItemEspelho>();

		// Tema
		if (informacaoPautaProcesso.getTemaEspelho() != null && informacaoPautaProcesso.getTemaEspelho().length() > 0) {
			itensEspelho.add(geraItemEspelho("Tema", informacaoPautaProcesso.getTemaEspelho()));
		}
		// Tese
		if (informacaoPautaProcesso.getTeseEspelho() != null && informacaoPautaProcesso.getTeseEspelho().length() > 0) {
			itensEspelho.add(geraItemEspelho("Tese", informacaoPautaProcesso.getTeseEspelho()));
		}
		// Parecer da AGU
		if (informacaoPautaProcesso.getParecerAGUEspelho() != null && informacaoPautaProcesso.getParecerAGUEspelho().length() > 0) {
			itensEspelho.add(geraItemEspelho("Parecer da AGU", informacaoPautaProcesso.getParecerAGUEspelho()));
		}
		// Parecer da PGR
		if (informacaoPautaProcesso.getParecerPGREspelho() != null && informacaoPautaProcesso.getParecerPGREspelho().length() > 0) {
			itensEspelho.add(geraItemEspelho("Parecer da PGR", informacaoPautaProcesso.getParecerPGREspelho()));
		}
		// Voto do Relator
		if (informacaoPautaProcesso.getVotoRelatorEspelho() != null && informacaoPautaProcesso.getVotoRelatorEspelho().length() > 0) {
			itensEspelho.add(geraItemEspelho("Voto do Relator", informacaoPautaProcesso.getVotoRelatorEspelho()));
		}
		// Votos
		if (informacaoPautaProcesso.getVotosEspelho() != null && informacaoPautaProcesso.getVotosEspelho().length() > 0) {
			itensEspelho.add(geraItemEspelho("Votos", informacaoPautaProcesso.getVotosEspelho()));
		}
		// Informações
		if ( (informacaoPautaProcesso.getInformacoesEspelho() != null && informacaoPautaProcesso.getInformacoesEspelho().length() > 0)
				|| (getDecisoes() != null && getDecisoes().size() > 0) ) {
			if ( informacaoPautaProcesso.getInformacoesEspelho() != null ){
				itensEspelho.add(geraItemEspelho("Informações", informacaoPautaProcesso.getInformacoesEspelho()));
			}
		}
	}
	
	private ItemEspelho geraItemEspelho(String nome, String conteudo) {
		ItemEspelho itemEspelho = new ItemEspelho();
		itemEspelho.setNome(nome);
		itemEspelho.setConteudo( conteudo.replaceAll("\\n", "<br/>") );
		return itemEspelho;
	}

	/**
	 * Abre o pdf da pecaInformacaoPauta
	 * @param pecaInformacaoPauta
	 */
	public void printPecaInformacaoPauta(PecaInformacaoPautaProcesso pecaInformacaoPauta) {
		if (pecaInformacaoPauta.getPecaProcessoEletronico() != null) {
			ArquivoProcessoEletronico ape = pecaInformacaoPauta.getPecaProcessoEletronico().getDocumentos().get(0);
			ReportUtils.report(new ByteArrayInputStream(ape.getDocumentoEletronico().getArquivo()));
		} else {
			ReportUtils.report(new ByteArrayInputStream(pecaInformacaoPauta.getArquivo()));
		}
	}
	
	/**
	 * Carrega as preferências para um determinado Objeto Incidente
	 */
	private void carregarPreferencias() {
		List<IncidentePreferencia> listaPreferencias = ((Processo) objetoIncidente.getPrincipal()).getIncidentePreferencia();
		preferencias = new StringBuffer();
		for (IncidentePreferencia preferencia : listaPreferencias) {
			if (listaPreferencias.indexOf(preferencia) > 0) {
				if (listaPreferencias.indexOf(preferencia) == listaPreferencias
						.size() - 1) {
					preferencias.append(" e ");
				} else {
					preferencias.append(", ");
				}
			}
			preferencias.append(preferencia.getTipoPreferencia().getDescricao());
		}
	}

	/**
	 * Carrega a data de distribuição para um processo. */
	private void carregarDataDistribuicao() {
		SituacaoMinistroProcesso distribuicao;
		try {
			distribuicao = objetoIncidenteService .recuperarDistribuicaoProcesso( objetoIncidente.getPrincipal() );
			if ( distribuicao != null ){
				setDataDistribuicao( distribuicao.getDataOcorrencia() );
			}
		} catch(ServiceException e) {
			logger.warn(e.getMessage(), e);
		}
	}


	/**
	 * @param objetoIncidenteDto
	 * @throws ServiceException
	 */
	private void carregarInformacoesEGab(ObjetoIncidenteDto objetoIncidenteDto)
			throws ServiceException {
		// Informações do eGab
		ProcessoSetor processoSetor = getMinistro() != null ? objetoIncidenteService
				.recuperarProcessoSetor(objetoIncidenteDto,
						objetoIncidenteService
								.consultarSetorPeloId(getMinistro().getSetor()
										.getId())) : null;
		deslocamentoAtual = null;
		distribuicaoAtual = null;
		if (processoSetor != null) {
			Hibernate.initialize(processoSetor.getDeslocamentoAtual());
			Hibernate.initialize(processoSetor.getDistribuicaoAtual());

			deslocamentoAtual = processoSetor.getDeslocamentoAtual();
			
			if (deslocamentoAtual != null) {
				Hibernate.initialize(deslocamentoAtual.getSecaoOrigem());
				Hibernate.initialize(deslocamentoAtual.getSecaoDestino());
				Hibernate.initialize(deslocamentoAtual.getUsuarioOrigem());
				Hibernate.initialize(deslocamentoAtual.getUsuarioDestino());
			}

			distribuicaoAtual = processoSetor.getDistribuicaoAtual();

			if (distribuicaoAtual != null) {
				Hibernate.initialize(distribuicaoAtual.getUsuario());
			}
		}
	}

	/**
	 * 
	 */
	private void carregarInformacoesPrescricao() {
		// Prescrição Réu
		prescricaoReu = null;
		Hibernate.initialize(((Processo) objetoIncidente.getPrincipal()).getReferenciaPrescricao());
		if (((Processo) objetoIncidente.getPrincipal()).getReferenciaPrescricao() != null && ((Processo) objetoIncidente.getPrincipal()).getReferenciaPrescricao().size() > 0) {
			Hibernate.initialize(((Processo) objetoIncidente.getPrincipal()).getReferenciaPrescricao().get(0).getPrescricaoReus());
			ReferenciaPrescricao referenciaPrescricao = ((Processo) objetoIncidente.getPrincipal()).getReferenciaPrescricao().get(0);
			prescricaoReu = referenciaPrescricao.getPrescricaoReus();
			for (PrescricaoReu pr : prescricaoReu) {
				Hibernate.initialize(pr.getJurisdicionado());
			}
		}
	}

	/**
	 * @param objetoIncidenteDto
	 * @throws ServiceException
	 */
	private void carregarInformacoesRelator(ObjetoIncidenteDto objetoIncidenteDto) throws ServiceException {
		// Carregando Relator a partir do componente cabeçalho
		ministroRelator = null;
		ministraRelatora = null;
		ministroRedatorAcordao = null;
		ministraRedatoraAcordao = null;
		CabecalhoObjetoIncidente cabecalhoObjetoIncidente = cabecalhoObjetoIncidenteService.recuperarCabecalho(objetoIncidenteDto.getId());
		if (cabecalhoObjetoIncidente != null && cabecalhoObjetoIncidente.getOcorrenciasMinistro() != null
				&& cabecalhoObjetoIncidente.getOcorrenciasMinistro().getOcorrenciaMinistro().size() > 0) {
			for (OcorrenciasMinistro.OcorrenciaMinistro ministro : cabecalhoObjetoIncidente
					.getOcorrenciasMinistro().getOcorrenciaMinistro()) {
				if (ministro.getCategoriaMinistro().equals("RELATOR") || ministro.getCategoriaMinistro().equals("RELATOR DO INCIDENTE")) {
					ministroRelator = ministro.getApresentacaoMinistro();
				} else if (ministro.getCategoriaMinistro().equals("RELATORA") || ministro.getCategoriaMinistro().equals("RELATORA DO INCIDENTE")) {
					ministraRelatora = ministro.getApresentacaoMinistro();
				} else if (ministro.getCategoriaMinistro().equals("REDATOR DO ACÓRDÃO")) {
					ministroRedatorAcordao = ministro.getApresentacaoMinistro();
				} else if (ministro.getCategoriaMinistro().equals("REDATORA DO ACÓRDÃO")) {
					ministraRedatoraAcordao = ministro.getApresentacaoMinistro();
				}
			}
		}
	}

	/**
	 * Altera a lista de textos exibida na tela.
	 * 
	 * @param event o evento de alteração
	 */
	public void changeTipoDeLista(ValueChangeEvent event) {
		if (event.getNewValue().equals(TipoDeListaParaExibicao.TEXTOS_DO_MINISTRO)) {
			textos = getTextosDoMinistro();
		} else {
			textos = getTodosTextos();
		}
	}
	
	/**
	 * Retorna os textos produzidos pelo ministro. 
	 * 
	 * @return os textos do ministros
	 */
	public ListDataModel<TextoDto> getTextosDoMinistro() {
		List<TextoDto> textosDoMinistro = null;
		try {
			textosDoMinistro = textoService.recuperarTextos(objetoIncidente, getMinistro(), getPrincipal());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(textosDoMinistro, new TextoComparator());
        return new ListDataModel<TextoDto>(textosDoMinistro); 
    }
	
	/**
	 * Retorna os textos produzidos por todos os ministros. 
	 * 
	 * @return os textos produzidos
	 */
	public ListDataModel<TextoDto> getTodosTextos() {
		List<TextoDto> todosTextos = textoService.recuperarTextos(objetoIncidente);
		Collections.sort(todosTextos, new TextoComparator());
        return new ListDataModel<TextoDto>(todosTextos);
	}
	
	private boolean existeTextoDeControleDeVotoNaoPublico(Ministro ministro, ControleVoto controleVoto) {
		return !controleVoto.getMinistro().equals(ministro) && controleVoto.getTexto() != null && !controleVoto.getTexto().getPublico();
	}

	/**
	 * Recupera os ministros ativos para apresentação na tela. Utilizado
	 * principalmente pela combos da pesquisa avançada.
	 * 
	 * @return a lista de ministros ativos
	 * @throws ServiceException em caos de problemas inesperados
	 */
	public List<Ministro> getMinistrosAtivos() throws ServiceException {
		if (ministros == null) {
			ministros = ministroService.pesquisarMinistrosAtivos();
			
			// --------------------------------------------------------------------------------------------
			// Comparator para ordenacao da lista...
			Comparator<Ministro> selectItemComparator = new Comparator<Ministro>() {
				public int compare(Ministro o1, Ministro o2) {
					//Jubé - Modificado em 10/9/2010, para atender ao Jira DECISAO-751: O Ministro presidente deve vir primeiro que os demais.
					if (Ministro.COD_MINISTRO_PRESIDENTE.equals(o1.getId())){
						return -1;
					}
					if (Ministro.COD_MINISTRO_PRESIDENTE.equals(o2.getId())){
						return 1;
					}
					return o1.getNome().compareTo(o2.getNome());
				}
			};
			
			Collections.sort(ministros, selectItemComparator);
		}
		
		return ministros;
	}
	
	/**
	 * Pesquisa os Ministros do Tribunal a partir de uma sugestão.
	 * @param suggest
	 * @return Lista de Ministros
	 * @throws ServiceException
	 */
	public List<Ministro> searchMinistros(Object suggest) throws ServiceException {
		List<Ministro> listaSugestaoMinistro = null;
		if (suggest != null && suggest.toString().trim().length() > 0) {
			listaSugestaoMinistro = ministroService.pesquisarMinistros(
					suggest.toString(), null);
		} else {
			listaSugestaoMinistro = getMinistrosAtivos();
		}

		return listaSugestaoMinistro;
	}
	
	/**
	 * Recupera o Ministro a cujo o Gabinete o usuário logado está associado.
	 *  
	 * @return o ministro
	 */
	public Ministro getMinistro() {
		Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		return principal.getMinistro();
	}
	
	public Ministro getMinistroLeitor() throws ServiceException {
		
		Ministro ministro = getMinistro();
		
		if (ministro == null || ministro.getId().equals(Ministro.COD_MINISTRO_PRESIDENTE)) {
			MinistroPresidente ministroPresidente = ministroPresidenteService.recuperarMinistroPresidenteAtual();
			ministro = ministroPresidente.getId().getMinistro();
		}
		
		return ministro;
	}
	
	/**
	 * Seta qual a lista de textos deve ser exibida.
	 * 
	 * @param tipoDeListaParaExibicao o tipo de lista
	 */
	public void setTipoDeListaParaExibicao(TipoDeListaParaExibicao tipoDeListaParaExibicao) {
		this.tipoDeListaParaExibicao = tipoDeListaParaExibicao;
	}
	
	/**
	 * Retorna o tipo de lista de texto que deve ser exibida.
	 * 
	 * @return o tipo de lista
	 */
	public TipoDeListaParaExibicao getTipoDeListaParaExibicao() {
		return tipoDeListaParaExibicao;
	}
	
	/**
	 * Indica se a lista de textos do ministros deve ser exibida.
	 * 
	 * @return true, se sim, false, caso contrário
	 */
	public boolean isListarTextosDoMinistro() {
		return tipoDeListaParaExibicao.equals(TipoDeListaParaExibicao.TEXTOS_DO_MINISTRO);
	}
	
	/**
	 * Indica se a lista de textos dos outros ministros deve ser exibida.
	 * 
	 * @return true, se sim, false, caso contrário
	 */
	public boolean isListarTodosTextos() {
		return tipoDeListaParaExibicao.equals(TipoDeListaParaExibicao.TODOS_TEXTOS);
	}
	
	/**
	 * Retorna o objeto incidente selecionado no suggestion box de processos da pesquisa principal.
	 * 
	 * @return o objeto incidente selecionado
	 */
	public ObjetoIncidente<?> getObjetoIncidente() {
        return objetoIncidente;
    }
	
	/**
	 * Recupera a lista de textos que deve ser apresentada na tela.
	 * 
	 * @return a lista de textos
	 */
	public DataModel<TextoDto> getTextos() {
		return textos;
	}

	/**
	 * Recupera a lista de categorias de parte
	 * 
	 * @return a lista de categorias de parte
	 */
	public List<SelectItem> getCategoriasParte() {
		if(categoriasParte == null) {
			try {
				List<SelectItem> itens = new ArrayList<SelectItem>();
				List<Categoria> categorias = categoriaService.pesquisar(null, null, true);
				for (Categoria categoria : categorias) {
					itens.add(new SelectItem(categoria.getId(), categoria.getDescricao()));
				}
				
				// --------------------------------------------------------------------------------------------
				// Comparator para ordenacao dos itens...
				Comparator<SelectItem> selectItemComparator = new Comparator<SelectItem>() {
					public int compare(SelectItem o1, SelectItem o2) {
						return o1.getLabel().compareTo(o2.getLabel());
					}
				};
	
				Collections.sort(itens, selectItemComparator);
				
				categoriasParte = itens;
				
			} catch (ServiceException e) {
				logger.error(e.getMessage(), e);
			}
			
		}
		return categoriasParte;
	}

	public List<Parte> getListaParte() {
		return listaParte;
	}

	public void setListaParte(List<Parte> listaParte) {
		this.listaParte = listaParte;
	}

	public HistoricoDeslocamento getDeslocamentoAtual() {
		return deslocamentoAtual;
	}

	public void setDeslocamentoAtual(HistoricoDeslocamento deslocamentoAtual) {
		this.deslocamentoAtual = deslocamentoAtual;
	}

	public HistoricoDistribuicao getDistribuicaoAtual() {
		return distribuicaoAtual;
	}

	public void setDistribuicaoAtual(HistoricoDistribuicao distribuicaoAtual) {
		this.distribuicaoAtual = distribuicaoAtual;
	}

	public void carregarListaParte(ObjetoIncidenteDto dto) {
		List<Parte> listaParte = null;
		
		try {
			listaParte = parteService.pesquisarPartes(dto.getId());
			for(Parte parte : listaParte) {
				Hibernate.initialize(parte.getCategoria());
				Hibernate.initialize(parte.getJurisdicionado());
				
				if (parte.getJurisdicionado() != null) {
					Hibernate.initialize(parte.getJurisdicionado().getOab());
					Hibernate.initialize(parte.getJurisdicionado().getIdentificadoresJurisdicionado());
				}
			}
			setListaParte(listaParte);
			
			if(getListaParte() == null || getListaParte().size() == 0) {
				facesMessages.add("Nenhuma parte encontrada.");
			}
		} catch (ServiceException e) {
			setListaParte(new ArrayList<Parte>());
			facesMessages.add(e.getMessage(), dto);
			logger.error(e.getMessage(), e);
		}
	}

	public List<ListaProcessos> getListasProcesso() {
		List<ListaProcessos> listas = objetoIncidente.getListasProcessos();
		return listas;
	}

	public List<TipoRecurso> getTiposIncidente() {
		List<TipoRecurso> lista = objetoIncidenteService.recuperarTiposRecurso(true);
		return lista;
	}

	public StringBuffer getPreferencias() {
		return preferencias;
	}
	
	public List<PrescricaoReu> getPrescricaoReu() {
		return prescricaoReu;
	}

	public String getMinistroRelator() {
		return ministroRelator;
	}

	public void setMinistroRelator(String ministroRelator) {
		this.ministroRelator = ministroRelator;
	}

	public String getMinistraRelatora() {
		return ministraRelatora;
	}

	public void setMinistraRelatora(String ministraRelatora) {
		this.ministraRelatora = ministraRelatora;
	}

	public String getMinistroRedatorAcordao() {
		return ministroRedatorAcordao;
	}

	public void setMinistroRedatorAcordao(String ministroRedatorAcordao) {
		this.ministroRedatorAcordao = ministroRedatorAcordao;
	}

	public String getMinistraRedatoraAcordao() {
		return ministraRedatoraAcordao;
	}

	public void setMinistraRedatoraAcordao(String ministraRedatoraAcordao) {
		this.ministraRedatoraAcordao = ministraRedatoraAcordao;
	}

	public InformacaoPautaProcesso getInformacaoPautaProcesso() {
		return informacaoPautaProcesso;
	}

	public void setInformacaoPautaProcesso(
			InformacaoPautaProcesso informacaoPautaProcesso) {
		this.informacaoPautaProcesso = informacaoPautaProcesso;
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

	public List<PrevisaoSustentacaoOralDto> getSustentacoesOrais() {
		if (sustentacoesOrais == null) {
			sustentacoesOrais = new ArrayList<PrevisaoSustentacaoOralDto>();
		}
		return sustentacoesOrais;
	}

	public void setSustentacoesOrais(
			List<PrevisaoSustentacaoOralDto> sustentacoesOrais) {
		this.sustentacoesOrais = sustentacoesOrais;
	}

	public Agendamento getAgendamento() {
		return agendamento;
	}

	public void setAgendamento(Agendamento agendamento) {
		this.agendamento = agendamento;
	}

	public String getColegiadoConsultaPauta() {
		return colegiadoConsultaPauta;
	}

	public void setColegiadoConsultaPauta(String colegiadoConsultaPauta) {
		this.colegiadoConsultaPauta = colegiadoConsultaPauta;
	}

	public void setDataPrevistaJulgamento(Date dataPrevistaJulgamento) {
		this.dataPrevistaJulgamento = dataPrevistaJulgamento;
	}

	public Date getDataPrevistaJulgamento() {
		return dataPrevistaJulgamento;
	}

	public void setDataDistribuicao(Date dataDistribuicao) {
		this.dataDistribuicao = dataDistribuicao;
	}

	public Date getDataDistribuicao() {
		return dataDistribuicao;
	}

	public void setTemaRepercussaoGeral(String temaRepercussaoGeral) {
		this.temaRepercussaoGeral = temaRepercussaoGeral;
	}

	public String getTemaRepercussaoGeral() {
		return temaRepercussaoGeral;
	}

	public List<String> getDecisoes() {
		return decisoes;
	}

	public void setDecisoes(List<String> decisoes) {
		this.decisoes = decisoes;
	}
	
	public List<ItemEspelho> getItensEspelho() {
		return itensEspelho;
	}
	
	public void setItensEspelho(List<ItemEspelho> itensEspelho) {
		this.itensEspelho = itensEspelho;
	}
	
	public List<Rotulo> getRotulos() {
		return rotulos;
	}
	
	public ObservacaoProcesso getObservacaoProcesso() {
		return observacaoProcesso;
	}

	public void setObservacaoProcesso(ObservacaoProcesso observacaoProcesso) {
		this.observacaoProcesso = observacaoProcesso;
	}

	public String getSituacaoJulgamento() {
		return situacaoJulgamento;
	}
	
	public SituacaoIncidenteJulgadoOuNao[] getTiposSituacaoJulgamento() {
		return SituacaoIncidenteJulgadoOuNao.values();
	}
	
	/**
	 * Comparator para ordenação de textos. Caso o texto não exista, não tenha sido criado ainda,
	 * e considerada a data 01 de janeiro de 1970.
	 * @author Paulo.Estevao
	 * @since 26.07.2010
	 */
	private class TextoComparator implements Comparator<TextoDto> {
		public int compare(TextoDto o1, TextoDto o2) {
			Date dataInicio1 = (o1.getDataInicio() == null ? new Date(0) : o1.getDataInicio());
			Date dataInicio2 = (o2.getDataInicio() == null ? new Date(0) : o2.getDataInicio());
			return dataInicio2.compareTo(dataInicio1);
		}
	}
	
	public static class ItemEspelho implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7349772336615969088L;
		private String nome;
		private String conteudo;
		
		public String getNome() {
			return nome;
		}
		
		public void setNome(String nome) {
			this.nome = nome;
		}
		
		public String getConteudo() {
			return conteudo;
		}
		
		public void setConteudo(String conteudo) {
			this.conteudo = conteudo;
		}
	}
	
	private void checarPermissaoEditarObservacao() throws ServiceException {
		if (!permissionChecker.hasPermission(getPrincipal(), ActionIdentification.EDITAR_OBSERVACAO_PROCESSO)) {
			throw new ServiceException("Usuário não possui permissão para editar observação!");
		}
	}
	
	public void salvarObservacaoProcesso() throws ServiceException {
		if (observacaoProcesso != null) {
			if (StringUtils.isEmpty(observacaoProcesso.getObservacao())) {
				if (observacaoProcesso.getId() != null) {
					observacaoProcessoService.excluir(observacaoProcesso);
					preencherNovaObservacaoProcessoVazia();
				}
			} else {
				try {
					if (observacaoProcesso.getObservacao().length() > 1000) throw new ServiceException("O campo observação tem tamanho máximo de 1000 caracteres.");
					
					checarPermissaoEditarObservacao();
					if (observacaoProcesso.getId() != null) {
						observacaoProcessoService.alterar(observacaoProcesso);
					} else {
						observacaoProcessoService.salvar(observacaoProcesso);
					}
					
					FacesMessages.instance().add(Severity.INFO, "Observação salva com sucesso.");
				} catch (ServiceException e) {
					carregarObservacaoProcesso();
					
					logger.error("Erro ao alterar a observação do processo.", e.getMessage());
					FacesMessages.instance().add(Severity.ERROR, e.getMessage());
				}
			}
		} 
	}
	
	public boolean temOABParaTodosOsRepresentantes() {
		return objetoIncidenteService.temOABParaTodosOsRepresentantes(objetoIncidente);
	}

	public String getRetornaLocalizacaoAtual() throws ServiceException{
		if (objetoIncidente != null &&  objetoIncidente.getPrincipal() != null){
			Processo processo = (Processo) objetoIncidente.getPrincipal();
			DeslocaProcesso ultimoDeslocamento = deslocaProcessoService.recuperarUltimoDeslocamentoProcesso(processo.getSiglaClasseProcessual(), processo.getNumeroProcessual());
			if (ultimoDeslocamento != null && ultimoDeslocamento.getCodigoOrgaoDestino().equals(Setor.CODIGO_ACERVO_ELETRONICO_INATIVO )){
				return "Este processo encontra-se na situação 'Inativo'";
			}
		}
		return " ";
	}

	public List<ManifestacaoRepresentante> getArquivosDeJulgamento() {
		return arquivosDeJulgamento;
	}

	public void setArquivosDeJulgamento(List<ManifestacaoRepresentante> arquivosDeJulgamento) {
		this.arquivosDeJulgamento = arquivosDeJulgamento;
	}
	
	public void carregarArquivosDeJulgamento() {
		try {
			this.arquivosDeJulgamento = manifestacaoRepresentanteService.listarManifestacoesPorIncidente(objetoIncidente);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public void baixarTodos() {
		if (getListaDocumentosExternosVinculados() != null && !getListaDocumentosExternosVinculados().isEmpty()) {
			List<InputStream> listaPDF = new ArrayList<InputStream>();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			for(PecaInformacaoPautaProcesso pecaInformacaoPauta : getListaDocumentosExternosVinculados()) {
				if (pecaInformacaoPauta.getPecaProcessoEletronico() != null) {
					ArquivoProcessoEletronico ape = pecaInformacaoPauta.getPecaProcessoEletronico().getDocumentos().get(0);
					listaPDF.add(new ByteArrayInputStream(ape.getDocumentoEletronico().getArquivo()));
				} else {
					listaPDF.add(new ByteArrayInputStream(pecaInformacaoPauta.getArquivo()));
				}
			}

			OpenOfficeMontadorTextoServiceImpl.concatPDFs(listaPDF, outputStream, false);
			ReportUtils.report(new ByteArrayInputStream(outputStream.toByteArray()));
		}
	}
	
	public void gravarLeituraSustentacaoOral(ManifestacaoRepresentante manifestacaoRepresentante) {
		try {
			manifestacaoLeituraService.gravarLeituraSustentacaoOral(manifestacaoRepresentante.getId(), getMinistroLeitor());
			carregarArquivosDeJulgamento();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}