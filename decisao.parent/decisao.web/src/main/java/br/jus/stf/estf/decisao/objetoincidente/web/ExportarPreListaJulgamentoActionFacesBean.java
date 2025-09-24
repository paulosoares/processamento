package br.jus.stf.estf.decisao.objetoincidente.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.jboss.seam.Component;
import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.cabecalho.model.CabecalhosObjetoIncidente.CabecalhoObjetoIncidente;
import br.gov.stf.estf.cabecalho.service.CabecalhoObjetoIncidenteService;
import br.gov.stf.estf.converter.DocumentConversionException;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.ProcessoListaJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.julgamento.model.service.ProcessoListaJulgamentoService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.PreListaJulgamentoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.support.ListaGenericaReport;
import br.jus.stf.estf.decisao.objetoincidente.support.PreListaJulgamentoReport;
import br.jus.stf.estf.decisao.objetoincidente.support.PreListaJulgamentoReportSupport;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.service.ConfiguracaoSistemaService;
import br.jus.stf.estf.decisao.support.service.ConverterService;
import br.jus.stf.estf.decisao.support.service.UsuarioLogadoService;
import br.jus.stf.estf.decisao.support.util.FormatoArquivo;
import br.jus.stf.estf.decisao.support.util.ReportUtils;
import br.jus.stf.estf.decisao.support.util.VelocityBuilder;

@Action(id = "exportarPreListaProcessosActionFacesBean", name = "Exportar Pré Lista de Processos", view = "/acoes/texto/exportarPreListaJulgamento.xhtml", height = 300, width = 560)
@Restrict({ ActionIdentification.EXPORTAR_LISTA_PROCESSOS })
@RequiresResources(Mode.One)
public class ExportarPreListaJulgamentoActionFacesBean extends ActionSupport<PreListaJulgamento> {

	protected static final String MSG_ERRO_GERAR_INFORMACOES_RELATORIO = "Erro gerarInformacoesRelatorio";
	protected static final String MSG_ERRO_GERAR      = "A Lista não tem processos.";
	protected static final String MSG_ERRO_GERAR_HTML = "Não foi possível gerar HTML para a Lista selecionada.";
	protected static final String MSG_ERRO_CABECALHO  = "Não foi possível recuperar o Cabeçalho do Objeto Incidente.";
	protected static final String MSG_ERRO_LISTA_NAO_RECUPERADA = "Não foi possível recuperar a Lista de Processos selecionada.";
	protected final static String ESPACO_BRANCO = " ";
	protected final static String SUPREMO_TRIBUNAL_FEDERAL = "Supremo Tribunal Federal";
	protected final static FormatoArquivo FORMATO_RELATORIO_PADRAO = FormatoArquivo.DOC;
	
	final static Long idMinistroRobertoBarro = (long) 48;
	static final String MIN_ROBERTO_BARROSO = "Ministro Luís Roberto Barroso";
	
	static final String TIPO_PRE_LISTA = "JUDICIARIO.LISTA";
	static final String TIPO_LISTA = "JULGAMENTO.LISTA_JULGAMENTO";

	private List<ObjetoIncidente<?>> listaProcessos;
	private FormatoArquivo formatoRelatorio = FORMATO_RELATORIO_PADRAO;
	private ByteArrayInputStream inputRelatorio;
	private ByteArrayOutputStream outputRelatorio;
	private List<PreListaJulgamentoReport> preListaJulgamentoReport;
	private Boolean listaProcessada;
	private PreListaJulgamento preLista;
	private Boolean mostrarEmenta = false;
	private Boolean mostrarObservacao = false;
	private Boolean mostrarPartes = true;
	private Boolean mostrarVotoVista = false;
	
	public PreListaJulgamento getPreLista() {
		return preLista;
	}

	public void setPreLista(PreListaJulgamento preLista) {
		this.preLista = preLista;
	}

	public Boolean getListaVazia() {
		return listaVazia;
	}

	public void setListaVazia(Boolean listaVazia) {
		this.listaVazia = listaVazia;
	}

	private Boolean listaVazia;	
	
	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Autowired
	private PreListaJulgamentoService preListaJulgamentoService;

	@Autowired
	private CabecalhoObjetoIncidenteService cabecalhoObjetoIncidenteService;
	
	@Autowired
	private ConverterService converterService;
	
	@Autowired
	private VelocityBuilder velocityBuilder;
	
	@Autowired
	private TextoService textoService;
	
	@Autowired
	private UsuarioLogadoService usuarioLogadoService;
	
	@Autowired
	private ConfiguracaoSistemaService configuracaoSistemaService;
	
	@Autowired
	private ExportarListaProcessosActionFacesBean exportarListaProcessosActionFacesBean;
	
	@Autowired
	private ProcessoListaJulgamentoService processoListaJulgamentoService;

	@Override
	public void load() {
		try {
			this.preListaJulgamentoReport = new ArrayList<PreListaJulgamentoReport>();
			long idLista = ((PreListaJulgamento) getResources().iterator().next()).getId();
			this.listaProcessos = convertePrelistaParaListaProcessos(idLista);
		} catch (ServiceException e) {
			addError(MSG_ERRO_LISTA_NAO_RECUPERADA + e.getMessage());
			logger.error(MSG_ERRO_LISTA_NAO_RECUPERADA, e);
		}
	}
	
	protected List<ObjetoIncidente<?>> convertePrelistaParaListaProcessos(long idLista) throws ServiceException{
		List<ObjetoIncidente<?>> listaProcessosRetorno = new ArrayList<ObjetoIncidente<?>>();
		PreListaJulgamento preLista = preListaJulgamentoService.recuperarPorId(idLista);
		setPreLista(preLista);		
		listaProcessosRetorno = convertePrelistaParaListaProcessos(preLista);		
		return listaProcessosRetorno;
	}

	
	@SuppressWarnings("unchecked")
	protected List<ObjetoIncidente<?>> convertePrelistaParaListaProcessos(PreListaJulgamento preLista) throws ServiceException{
		List<ObjetoIncidente<?>> listaProcessosRetorno = new ArrayList<ObjetoIncidente<?>>();
		boolean ordenacaoNumerica = configuracaoSistemaService.isOrdenacaoNumerica();		
		List<PreListaJulgamentoObjetoIncidente> preListJulgObjetoIncidentes = preLista.getObjetosIncidentes(ordenacaoNumerica);
		setListaVazia(preListJulgObjetoIncidentes.size()<1);
		
		if (getListaVazia()) {
			listaProcessada = Boolean.FALSE;
			throw new ServiceException(MSG_ERRO_GERAR);			
		}else{
			listaProcessada = Boolean.TRUE;
			
			for (PreListaJulgamentoObjetoIncidente rel : preListJulgObjetoIncidentes) {
				if (rel.getRevisado().equals(Boolean.TRUE)) {
					listaProcessosRetorno.add((ObjetoIncidente<Processo>) rel.getObjetoIncidente());
				}
			}
		}
		
		return listaProcessosRetorno;
	}
	
	
	public void execute() {
		try {
			if(getListaVazia()){
				listaProcessada = Boolean.FALSE;
				throw new ServiceException(MSG_ERRO_GERAR);				
			}else{
				
				// Salva o cabeçalho
				preListaJulgamentoService.salvar(preLista);
				
				preListaJulgamentoReport.clear();
				outputRelatorio = new ByteArrayOutputStream();				
				
				PreListaJulgamentoReportSupport preListaJulgamentoReportSupport = carregaPreListaJulgamentoReportSupport();				
				this.inputRelatorio = gerarRelatorioEmHTML(preListaJulgamentoReportSupport);
				
				if (logger.isInfoEnabled()) {
					logger.info("Gerando relatório de " + preListaJulgamentoReport.size() + " processo(s) no formato " + formatoRelatorio + ".");
				}
				outputRelatorio = gerarArquivoSaida(this.formatoRelatorio,this.inputRelatorio,this.outputRelatorio);
				gerarRelatorioDOCParaUsuario(new ByteArrayInputStream(outputRelatorio.toByteArray()), preLista.getNome());
				listaProcessada = Boolean.TRUE;
			}
		}catch (ServiceException e) {
			addWarning(e.getMessage());
		}catch (Exception e) {
			addError("Erro ao Recuperar a Lista selecionada. " + e.getMessage());
			logger.error("Erro ao Recuperar a Lista selecionada.", e);
			e.printStackTrace();
		}
		
		if (hasErrors()) {
			sendToErrors();
		} else {
			sendToConfirmation();
		}
	}

	protected ByteArrayOutputStream gerarArquivoSaida(FormatoArquivo formatoRelatorio,ByteArrayInputStream inputRelatorio,ByteArrayOutputStream outputRelatorio) throws IOException,DocumentConversionException {
		
		switch (formatoRelatorio) {
		case PDF:
			converterService.converterHtmlParaPDF(inputRelatorio, outputRelatorio);
			gerarRelatorioPDFParaUsuario(new ByteArrayInputStream(outputRelatorio.toByteArray()));
			break;
		case RTF:
			converterService.converterHtmlParaRTF(inputRelatorio, outputRelatorio);
			//gerarRelatorioRTFParaUsuario(new ByteArrayInputStream(outputRelatorio.toByteArray()));
			break;
		case DOC:
			converterService.converterHtmlParaDOCX(inputRelatorio, outputRelatorio);
			break;
		default:
			addWarning("Favor selecionar o formato desejado.");
			return null;
		}
		
		return outputRelatorio;
	}

	protected PreListaJulgamentoReportSupport carregaPreListaJulgamentoReportSupport() {
		String cabecalho = preLista.getCabecalho();
		String ministro = this.recuperarMinistroDaLista();
		String numeroLista = "";
		String sessao = "";
		
		PreListaJulgamentoReportSupport preListaJulgamentoReportSupport= new PreListaJulgamentoReportSupport();
		preListaJulgamentoReportSupport.setCabecalho(cabecalho);
		preListaJulgamentoReportSupport.setListaProcessos(this.listaProcessos);
		preListaJulgamentoReportSupport.setMinistro(ministro);
		preListaJulgamentoReportSupport.setMostrarEmenta(this.mostrarEmenta);
		preListaJulgamentoReportSupport.setMostrarObservacao(this.mostrarObservacao);
		preListaJulgamentoReportSupport.setMostrarPartes(this.mostrarPartes);
		preListaJulgamentoReportSupport.setMostrarVotoVista(this.mostrarVotoVista);
		preListaJulgamentoReportSupport.setNumeroLista(numeroLista);
		preListaJulgamentoReportSupport.setSessao(sessao);
		preListaJulgamentoReportSupport.setTipoLista(TIPO_PRE_LISTA);
		return preListaJulgamentoReportSupport;
	}

	protected void gerarRelatorioPDFParaUsuario(ByteArrayInputStream documento) {
		ReportUtils.report(documento, FormatoArquivo.PDF);
	}

	protected void gerarRelatorioRTFParaUsuario(ByteArrayInputStream documento, String nomeArquivo) {
		ReportUtils.report(documento, nomeArquivo, FormatoArquivo.RTF);
	}
	
	protected void gerarRelatorioDOCParaUsuario(ByteArrayInputStream documento, String nomeArquivo) {
		ReportUtils.report(documento, nomeArquivo, FormatoArquivo.DOC);
	}

	public ByteArrayInputStream gerarRelatorioEmHTML(PreListaJulgamentoReportSupport preListaJulgamentoReportSupport) throws Exception {
		String retorno = new String();
		try {
			
			String cabecalho = preListaJulgamentoReportSupport.getCabecalho();
			String cabecalhoVistor = preListaJulgamentoReportSupport.getCabecalhoVistor();
			String ministro = preListaJulgamentoReportSupport.getMinistro();
			String ministroVistor = preListaJulgamentoReportSupport.getMinistroVistor();
			String numeroLista = preListaJulgamentoReportSupport.getNumeroLista();
			String sessao = preListaJulgamentoReportSupport.getSessao();
			String descricaoTipo = preListaJulgamentoReportSupport.getDescricaoTipoListaJulgamento();

			List<PreListaJulgamentoReport> listaProcessosReport = gerarInformacoesRelatorio(preListaJulgamentoReportSupport);		
			
			Map<String, Object> mapaParametros = new HashMap<String, Object>();
			mapaParametros.put(PreListaJulgamentoReport.CABECALHO_HTML, cabecalho);
			mapaParametros.put(PreListaJulgamentoReport.CABECALHO_HTML_VISTOR, cabecalhoVistor);
			mapaParametros.put(PreListaJulgamentoReport.LISTA_PROCESSOS, listaProcessosReport);
			mapaParametros.put(PreListaJulgamentoReport.MINISTRO,ministro);
			mapaParametros.put(PreListaJulgamentoReport.MINISTRO_VISTOR,ministroVistor);
			mapaParametros.put(PreListaJulgamentoReport.NUMERO_LISTA,numeroLista);
			mapaParametros.put(PreListaJulgamentoReport.SESSAO,sessao);
			mapaParametros.put(PreListaJulgamentoReport.DESCRICAO_TIPO_LISTA, descricaoTipo);
			retorno = velocityBuilder.substituiVariaveisDoTemplate(PreListaJulgamentoReport.TEMPLATE_RELATORIO, mapaParametros);
		} catch (Exception e) {
			addError(MSG_ERRO_GERAR_HTML + e.getMessage());
			logger.error(MSG_ERRO_GERAR_HTML, e);
		}
		ByteArrayInputStream inputRelatorio = new ByteArrayInputStream(retorno.getBytes());		
		return inputRelatorio;
	}

	public List<PreListaJulgamentoReport> gerarInformacoesRelatorio(PreListaJulgamentoReportSupport preListaJulgamentoReportSupport) throws ServiceException {
		List<PreListaJulgamentoReport> retorno = new ArrayList<PreListaJulgamentoReport>();
		try{
			List<ObjetoIncidente<?>> listaProcessos = preListaJulgamentoReportSupport.getListaProcessos();
			Hibernate.initialize(listaProcessos);
			for (ObjetoIncidente<?> objetoIncidente : listaProcessos) {
				PreListaJulgamentoReport informacoesObjInc = gerarInformacoesObjetoIncidente(objetoIncidente,preListaJulgamentoReportSupport);
				Integer numItemLista = this.getNumItemLista(preListaJulgamentoReportSupport,listaProcessos, objetoIncidente);
				
				if (numItemLista != null)
					informacoesObjInc.setNumItemLista(numItemLista.toString());
				else
					informacoesObjInc.setNumItemLista("");
				
				retorno.add(informacoesObjInc);
			}
		} catch (Exception e) {
			throw new ServiceException(MSG_ERRO_GERAR_INFORMACOES_RELATORIO, e);
		}
		return retorno;
	}

	protected Integer getNumItemLista(PreListaJulgamentoReportSupport preListaJulgamentoReportSupport,List<ObjetoIncidente<?>> listaProcessos,ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		Integer numItemLista = null;
		
		if( TIPO_LISTA.equals(preListaJulgamentoReportSupport.getTipoLista()) ){
			Long idListaJulgamento = preListaJulgamentoReportSupport.getIdListaJulgamento();
			ProcessoListaJulgamento processoListaJulgamento = recuperarOrdemDoItemNaLista(idListaJulgamento, objetoIncidente);
			numItemLista = processoListaJulgamento.getOrdemNaLista();
		}else{
			numItemLista =  listaProcessos.indexOf(objetoIncidente);
			numItemLista = numItemLista + 1;
		}
		return numItemLista;
	}

	ProcessoListaJulgamento recuperarOrdemDoItemNaLista(Long idListaJulgamento, ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		ListaJulgamento listaJulgamento = new ListaJulgamento();
		listaJulgamento.setId(idListaJulgamento);
		List<ProcessoListaJulgamento> lista = processoListaJulgamentoService.listarProcessos(listaJulgamento);
		
		for (ProcessoListaJulgamento processoLista : lista)
			if (processoLista.getObjetoIncidente().equals(objetoIncidente))
				return processoLista;
		
		return null;
	}

	private PreListaJulgamentoReport gerarInformacoesObjetoIncidente(ObjetoIncidente<?> objetoIncidente,PreListaJulgamentoReportSupport preListaJulgamentoReportSupport) {
		PreListaJulgamentoReport preListaProcessosReport = new PreListaJulgamentoReport();
		CabecalhoObjetoIncidente cabecalhoOi;
		
		try {
			cabecalhoOi = cabecalhoObjetoIncidenteService.recuperarCabecalho(objetoIncidente.getId());
			if (cabecalhoOi != null) {
				preListaProcessosReport.setIdentificacaoCompletaProcesso(gerarIdentificacaoCompletaProcesso(cabecalhoOi));
				preListaProcessosReport.getInformacoesProcesso().addAll(gerarInformacoesOcorrenciaMinistro(cabecalhoOi));
				if(preListaJulgamentoReportSupport.getMostrarPartes()){
					preListaProcessosReport.getInformacoesProcesso().addAll(gerarInformacoesParte(cabecalhoOi));
				}
				if(preListaJulgamentoReportSupport.getMostrarEmenta()){
					String texto = getTextoEmenta(objetoIncidente);
					texto = limpaTexto(texto);					
					preListaProcessosReport.setTextoEmenta(texto);
				}
				if(preListaJulgamentoReportSupport.getMostrarObservacao()){
					String tipoLista = preListaJulgamentoReportSupport.getTipoLista();
					String texto = getTextoObservacao(preListaJulgamentoReportSupport, objetoIncidente,tipoLista);
					texto = limpaTexto(texto);
					preListaProcessosReport.setTextoObservacao(texto);
				}
				if(preListaJulgamentoReportSupport.getMostrarVotoVista()){
					if (preListaJulgamentoReportSupport.getIdMinistroVistor()!=null){
						String texto = getTextoVotoVista(objetoIncidente, preListaJulgamentoReportSupport.getIdMinistroVistor());
						texto = limpaTexto(texto);
						preListaProcessosReport.setTextoVotoVista(texto);
					}
				}
			}
		} catch (ServiceException e) {
			addError(MSG_ERRO_CABECALHO + e.getMessage());
			logger.error(MSG_ERRO_CABECALHO, e);
		}
		return preListaProcessosReport;
	}

	String limpaTexto(String texto) {
		String textoRetorno = null;
		boolean condicao = "".equals(texto) || " ".equals(texto);
		if (!condicao){
			textoRetorno = texto;
		}
		return textoRetorno;
	}

	private String gerarIdentificacaoCompletaProcesso(CabecalhoObjetoIncidente cabecalhoOi) {
		String retorno = exportarListaProcessosActionFacesBean.gerarIdentificacaoCompletaProcesso(cabecalhoOi);
		return retorno;
	}

	private List<ListaGenericaReport> gerarInformacoesOcorrenciaMinistro(CabecalhoObjetoIncidente cabecalhoOi) {
		List<ListaGenericaReport> informacoesOcorrencia = exportarListaProcessosActionFacesBean.gerarInformacoesOcorrenciaMinistro(cabecalhoOi);
		return informacoesOcorrencia;
	}

	private List<ListaGenericaReport> gerarInformacoesParte(CabecalhoObjetoIncidente cabecalhoOi) {
		List<ListaGenericaReport> informacoesParte = exportarListaProcessosActionFacesBean.gerarInformacoesParte(cabecalhoOi);
		return informacoesParte;
	}
	
	@SuppressWarnings("rawtypes")
	String getTextoObservacao(PreListaJulgamentoReportSupport preListaJulgamentoReportSupport, ObjetoIncidente<?> objetoIncidente, String tipoLista) throws ServiceException {
		String textoRetorno = null;
		Long idObjetoIncidente = objetoIncidente.getId();
		ObjetoIncidente oi = objetoIncidenteService.recuperarPorId(idObjetoIncidente);
		if(TIPO_LISTA.equals(tipoLista) ){
			Long idListaJulgamento = preListaJulgamentoReportSupport.getIdListaJulgamento();
			ProcessoListaJulgamento incidenteLista = recuperarOrdemDoItemNaLista(idListaJulgamento, objetoIncidente);
			
			if(incidenteLista != null){
				textoRetorno =  incidenteLista.getObservacao();
			}
		}else{
			PreListaJulgamentoObjetoIncidente incidentePreLista = preListaJulgamentoService.recuperarPreListaJulgamentoObjetoIncidente(oi);
			if(incidentePreLista != null){
				textoRetorno =  incidentePreLista.getObservacao();
			}
		}
		return textoRetorno;
	}
	
	private String getTextoEmenta(ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		String textoRetorno = null;
		long idMinistro = usuarioLogadoService.getMinistro().getId();
		Texto ementa = textoService.recuperar(objetoIncidente, TipoTexto.EMENTA, idMinistro);
		textoRetorno = this.loadConteudo(ementa);
		if (textoRetorno == null){
			textoRetorno = "";
		}
		return textoRetorno;
	}
	
	private String getTextoVotoVista(ObjetoIncidente<?> objetoIncidente, Long idMinistroVistor) throws ServiceException {
		return this.loadConteudo( textoService.recuperar(objetoIncidente, TipoTexto.VOTO_VISTA, idMinistroVistor) );
	}
	
	public String loadConteudo(Texto texto) throws ServiceException {
		RevisarListasFacesBean revisarListasFacesBean = (RevisarListasFacesBean)Component.getInstance(RevisarListasFacesBean.class, true);
		return revisarListasFacesBean.loadConteudo(texto);
	}

	protected String recuperarMinistroDaLista() {
		String retorno = null;
		if (usuarioLogadoService.getMinistro() != null){
			retorno = usuarioLogadoService.getMinistro().getNomeMinistroCapsulado(true, false);
			
			//DECISAO-2142
			Long idMinistro = usuarioLogadoService.getMinistro().getId();
			if(idMinistroRobertoBarro.equals(idMinistro)){
				retorno = MIN_ROBERTO_BARROSO;
			}
		}
		return retorno;
	}
	
	protected String recuperarMinistroRelatorDaLista(ListaJulgamento listaJulgamento) {
			String retorno = null;
			if (listaJulgamento.getMinistro() != null){
				//DECISAO-2142
				Long idMinistro = listaJulgamento.getMinistro().getId();
				if(idMinistroRobertoBarro.equals(idMinistro)){
					retorno = MIN_ROBERTO_BARROSO;
				} else {
					retorno = listaJulgamento.getMinistro().getNomeMinistroCapsulado(true, false);
				}
			}
			return retorno;
		}
	
	protected String recuperarMinistroVistorDaLista(ListaJulgamento listaJulgamento) {
		String retorno = null;
		if (listaJulgamento.getMinistroVistor() != null){
			//DECISAO-2142
			Long idMinistro = listaJulgamento.getMinistroVistor().getId();
			if(idMinistroRobertoBarro.equals(idMinistro)){
				retorno = MIN_ROBERTO_BARROSO;
			} else {
				retorno = listaJulgamento.getMinistroVistor().getNomeMinistroCapsulado(true, false);
			}
		}
		return retorno;
	}

	@Override
	public String getErrorTitle() {
		return "Erro ao gerar o arquivo PDF da Lista.";
	}

	public void voltar() {
		getDefinition().setFacet("principal");
	}

	public FormatoArquivo getFormatoRelatorio() {
		return formatoRelatorio;
	}

	public void setFormatoRelatorio(FormatoArquivo formatoRelatorio) {
		this.formatoRelatorio = formatoRelatorio;
	}

	public Boolean getListaProcessada() {
		return listaProcessada;
	}

	public void setListaProcessada(Boolean listaProcessada) {
		this.listaProcessada = listaProcessada;
	}

	public void setListaProcessosReport(List<PreListaJulgamentoReport> prelistaJulgamentoReport) {
		this.preListaJulgamentoReport = prelistaJulgamentoReport;
	}

	public Boolean getMostrarEmenta() {
		return mostrarEmenta;
	}

	public void setMostrarEmenta(Boolean mostrarEmenta) {
		this.mostrarEmenta = mostrarEmenta;
	}

	public Boolean getMostrarObservacao() {
		return mostrarObservacao;
	}

	public void setMostrarObservacao(Boolean mostrarObservacao) {
		this.mostrarObservacao = mostrarObservacao;
	}

	public Boolean getMostrarPartes() {
		return mostrarPartes;
	}

	public void setMostrarPartes(Boolean mostrarPartes) {
		this.mostrarPartes = mostrarPartes;
	}

}