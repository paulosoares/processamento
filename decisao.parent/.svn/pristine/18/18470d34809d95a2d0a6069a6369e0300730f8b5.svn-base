package br.jus.stf.estf.decisao.objetoincidente.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.cabecalho.model.CabecalhosObjetoIncidente.CabecalhoObjetoIncidente;
import br.gov.stf.estf.cabecalho.model.InformacaoProcessoRecurso;
import br.gov.stf.estf.cabecalho.model.InformacoesParte;
import br.gov.stf.estf.cabecalho.model.OcorrenciasMinistro;
import br.gov.stf.estf.cabecalho.service.CabecalhoObjetoIncidenteService;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.service.ListaProcessosService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.support.ListaGenericaReport;
import br.jus.stf.estf.decisao.objetoincidente.support.ListaProcessosReport;
import br.jus.stf.estf.decisao.pesquisa.domain.ListaIncidentesDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.handlers.Restrict;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.action.support.ActionSupport;
import br.jus.stf.estf.decisao.support.service.ConverterService;
import br.jus.stf.estf.decisao.support.util.FormatoArquivo;
import br.jus.stf.estf.decisao.support.util.ReportUtils;
import br.jus.stf.estf.decisao.support.util.VelocityBuilder;

/**
 * @author Almir.Oliveira
 * @since 17.01.2012 */
@Action(id = "exportarListaProcessosActionFacesBean", name = "Exportar Lista de Processos", view = "/acoes/texto/exportarListaProcessos.xhtml", height = 300, width = 560)
@Restrict({ ActionIdentification.EXPORTAR_LISTA_PROCESSOS })
@RequiresResources(Mode.One)
public class ExportarListaProcessosActionFacesBean extends ActionSupport<ListaIncidentesDto> {

	private final static String ESPACO_BRANCO = " ";
	private final static String SUPREMO_TRIBUNAL_FEDERAL = "Supremo Tribunal Federal";
	private final static FormatoArquivo FORMATO_RELATORIO_PADRAO = FormatoArquivo.PDF;

	private ListaProcessos listaProcessos;
	private String cabecalhoLista;
	private FormatoArquivo formatoRelatorio = FORMATO_RELATORIO_PADRAO;
	private ByteArrayInputStream inputRelatorio;
	private ByteArrayOutputStream outputRelatorio;
	private List<ListaProcessosReport> listaProcessosReport;
	private Boolean listaProcessada;

	@Autowired
	private ListaProcessosService listaProcessosService;

	@Autowired
	private CabecalhoObjetoIncidenteService cabecalhoObjetoIncidenteService;
	
	@Autowired
	private ConverterService converterService;
	
	@Autowired
	private VelocityBuilder velocityBuilder;

	@Override
	public void load() {
		cabecalhoLista = new String();
		listaProcessada = new Boolean(Boolean.TRUE);
		listaProcessosReport = new ArrayList<ListaProcessosReport>();
		
		try {
			listaProcessos = listaProcessosService.recuperarPorId(((ListaIncidentesDto) getResources().iterator().next()).getId());
			Hibernate.initialize(listaProcessos.getSetor());
			Hibernate.initialize(listaProcessos.getElementos());
			// recuperarCabecalhoFixo();
		} catch (ServiceException e) {
			addError("Não foi possível recuperar a Lista de Processos selecionada." + e.getMessage());
			logger.error("Não foi possível recuperar a Lista de Processos selecionada.", e);
		}
	}

	public void execute() {
		try {
			listaProcessosReport.clear();
			outputRelatorio = new ByteArrayOutputStream();

			gerarInformacoesRelatorio();
			gerarRelatorioEmHTML();
			
			if (logger.isInfoEnabled()) {
				logger.info("Gerando relatório de " + listaProcessosReport.size() + " processo(s) no formato " + formatoRelatorio + ".");
			}
			
			switch (formatoRelatorio) {
			case PDF:
				converterService.converterHtmlParaPDF(inputRelatorio, outputRelatorio);
				gerarRelatorioPDFParaUsuario(new ByteArrayInputStream(outputRelatorio.toByteArray()));
				break;
			case RTF:
				converterService.converterHtmlParaRTF(inputRelatorio, outputRelatorio);
				gerarRelatorioRTFParaUsuario(new ByteArrayInputStream(outputRelatorio.toByteArray()));
				break;
			default:
				addWarning("Favor selecionar o formato desejado.");
				return;
			}

			listaProcessada = Boolean.TRUE;
		} catch (Exception e) {
			addError("Erro ao Recuperar a Lista selecionada. " + e.getMessage());
			logger.error("Erro ao Recuperar a Lista selecionada.", e);
		}
		if (hasErrors()) {
			sendToErrors();
		} else {
			sendToConfirmation();
		}
	}

	private InputStream gerarRelatorioEmHTML() throws Exception {
		String retorno = new String();
		try {
			Map<String, Object> mapaParametros = new HashMap<String, Object>();
			mapaParametros.put(ListaProcessosReport.CABECALHO_HTML, getCabecalhoLista());
			mapaParametros.put(ListaProcessosReport.LISTA_PROCESSOS, getListaProcessosReport());
			retorno = velocityBuilder.substituiVariaveisDoTemplate(ListaProcessosReport.TEMPLATE_RELATORIO, mapaParametros);
		} catch (Exception e) {
			addError("Não foi possível gerar HTML para a Lista selecionada." + e.getMessage());
			logger.error("Não foi possível gerar HTML para a Lista selecionada. ", e);
		}
		return inputRelatorio = new ByteArrayInputStream(retorno.getBytes());
	}

	private void gerarRelatorioPDFParaUsuario(ByteArrayInputStream documento) {
		ReportUtils.report(documento, FormatoArquivo.PDF);
	}

	private void gerarRelatorioRTFParaUsuario(ByteArrayInputStream documento) {
		ReportUtils.report(documento, FormatoArquivo.RTF);
	}

	public StringBuffer gerarRelatorioDaLista() throws FileNotFoundException {
		StringBuffer arquivoLista = new StringBuffer();
		for (Iterator<ObjetoIncidente<?>> oi = listaProcessos.getElementos().iterator(); oi.hasNext();) {
			arquivoLista.append(gerarInformacoesObjetoIncidente((ObjetoIncidente<?>) oi.next()));
		}
		return arquivoLista;
	}

	private void gerarInformacoesRelatorio() {
		for (Iterator<ObjetoIncidente<?>> oi = listaProcessos.getElementos().iterator(); oi.hasNext();) {
			getListaProcessosReport().add(gerarInformacoesObjetoIncidente((ObjetoIncidente<?>) oi.next()));
		}
	}

	private ListaProcessosReport gerarInformacoesObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		ListaProcessosReport listaProcessosReport = new ListaProcessosReport();
		CabecalhoObjetoIncidente cabecalhoOi;
		try {
			cabecalhoOi = cabecalhoObjetoIncidenteService.recuperarCabecalho(objetoIncidente.getId());
			if (cabecalhoOi != null) {
				listaProcessosReport.setIdentificacaoCompletaProcesso(gerarIdentificacaoCompletaProcesso(cabecalhoOi));
				listaProcessosReport.getInformacoesProcesso().addAll(gerarInformacoesOcorrenciaMinistro(cabecalhoOi));
				listaProcessosReport.getInformacoesProcesso().addAll(gerarInformacoesParte(cabecalhoOi));
			}
		} catch (ServiceException e) {
			addError("Não foi possível recuperar o Cabeçalho do Objeto Incidente." + e.getMessage());
			logger.error("Não foi possível recuperar o Cabeçalho do Objeto Incidente.", e);
		}
		return listaProcessosReport;
	}

	protected String gerarIdentificacaoCompletaProcesso(CabecalhoObjetoIncidente cabecalhoOi) {
		String retorno = new String();
		InformacaoProcessoRecurso informacaoProcessoRecurso = cabecalhoOi.getInformacaoProcessoRecurso();
		if (informacaoProcessoRecurso != null) {
			retorno = informacaoProcessoRecurso.getDescricaoCadeia()
					+ ESPACO_BRANCO
					+ String.valueOf(informacaoProcessoRecurso.getNumeroProcesso())
					+ ESPACO_BRANCO;
			if (cabecalhoOi.getInformacoesOrigem() != null && cabecalhoOi.getInformacoesOrigem().getInformacaoOrigem() != null) {
				retorno += cabecalhoOi.getInformacoesOrigem().getInformacaoOrigem().get(0).getInformacaoProcedencia().getDescricaoProcedencia();
			}
		}
		return retorno;
	}

	protected List<ListaGenericaReport> gerarInformacoesOcorrenciaMinistro(CabecalhoObjetoIncidente cabecalhoOi) {
		List<ListaGenericaReport> informacoesOcorrencia = new ArrayList<ListaGenericaReport>();
		if (cabecalhoOi.getOcorrenciasMinistro() != null && cabecalhoOi.getOcorrenciasMinistro().getOcorrenciaMinistro().size() > 0) {
			for (OcorrenciasMinistro.OcorrenciaMinistro ministro : cabecalhoOi.getOcorrenciasMinistro().getOcorrenciaMinistro()) {
				if (ministro.getCategoriaMinistro().equals("RELATOR") || ministro.getCategoriaMinistro().equals("RELATOR DO INCIDENTE")) {
					informacoesOcorrencia.add(new ListaGenericaReport("RELATOR", ministro.getApresentacaoMinistro()));
				} else if (ministro.getCategoriaMinistro().equals("RELATORA")
						|| ministro.getCategoriaMinistro().equals("RELATORA DO INCIDENTE")) {
					informacoesOcorrencia.add(new ListaGenericaReport("RELATORA", ministro.getApresentacaoMinistro()));
				} else if (ministro.getCategoriaMinistro().equals("REDATOR DO ACÓRDÃO")) {
					informacoesOcorrencia.add(new ListaGenericaReport("REDATOR DO ACÓRDÃO", ministro.getApresentacaoMinistro()));
				} else if (ministro.getCategoriaMinistro().equals("REDATORA DO ACÓRDÃO")) {
					informacoesOcorrencia.add(new ListaGenericaReport("REDATORA DO ACÓRDÃO", ministro.getApresentacaoMinistro()));
				}
			}
		}
		return informacoesOcorrencia;
	}

	protected List<ListaGenericaReport> gerarInformacoesParte(CabecalhoObjetoIncidente cabecalhoOi) {
		List<ListaGenericaReport> informacoesParte = new ArrayList<ListaGenericaReport>();
		if (cabecalhoOi.getInformacoesParte() != null) {
			for (InformacoesParte.InformacaoParte ip : cabecalhoOi.getInformacoesParte().getInformacaoParte()) {
				informacoesParte.add(new ListaGenericaReport(ip.getCategoriaParte(), ip.getApresentacaoParte()));
			}
		}
		return informacoesParte;
	}

	private void recuperarCabecalhoFixo() {
		setCabecalhoLista("<p style=\"text-align: center;\"><span style=\"font-size:20px;\">"
				+ SUPREMO_TRIBUNAL_FEDERAL.toUpperCase()
				+ "</span></p>"
				+ "<p style=\"text-align: center;\"><span style=\"font-size:18px;\">PRIMEIRA TURMA</span></p>"
				+ "<p style=\"text-align: center;\"><span style=\"font-size:18px;\">Relator: <strong>Ministro Dias Toffoli</strong></span></p>"
				+ "<p style=\"text-align: center;\"><span style=\"font-size:18px;\">Sess&atilde;o Ordin&aacute;ria do dia 08.02.2012</span></p>"
				+ "<p style=\"text-align: center;\"><span style=\"font-size:22px;\"><strong>" + listaProcessos.getNome()
				+ "</strong></span></p>");
	}

	private void recuperarCabecalhoDinamico() {
		setCabecalhoLista("<p style=\"text-align: center;\"><span style=\"font-size:24px;\">" + SUPREMO_TRIBUNAL_FEDERAL.toUpperCase()
				+ "</span></p>" + "<p style=\"text-align: center;\"><span style=\"font-size:18px;\">" + recuperarIdentificacaoSetor()
				+ "</span></p>" + "<p style=\"text-align: center;\"><span style=\"font-size:18px;\">" + recuperarColegiadoMinistro()
				+ "</span></p>" + "<p style=\"text-align: center;\"><span style=\"font-size:18px;\">Relator: <strong>"
				+ recuperarMinistroDaLista().getNomeMinistroCapsulado(true) + "</strong></span></p>"
				+ "<p style=\"text-align: center;\"><span style=\"font-size:18px;\">Sess&atilde;o xxx</span></p>"
				+ "<p style=\"text-align: center;\"><span style=\"font-size:24px;\"><strong>" + listaProcessos.getNome()
				+ "</strong></span></p>");
	}

	private String recuperarIdentificacaoSetor() {
		return listaProcessos.getSetor().getNome();
	}

	private Ministro recuperarMinistroDaLista() {
		Ministro retorno = new Ministro();
		for (Iterator<ObjetoIncidente<?>> oi = listaProcessos.getElementos().iterator(); oi.hasNext();) {
			ObjetoIncidente<?> obj = (ObjetoIncidente<?>) oi.next();
			if (obj.getSituacaoMinistroProcesso() != null) {
				Hibernate.initialize(obj.getSituacaoMinistroProcesso());
				if (obj.getSituacaoMinistroProcesso().getMinistroRelator() != null) {
					Hibernate.initialize(obj.getSituacaoMinistroProcesso().getMinistroRelator());
				}
			}
			retorno = obj.getSituacaoMinistroProcesso().getMinistroRelator();
			break;
		}
		return retorno;
	}

	private String recuperarColegiadoMinistro() {
		return recuperarMinistroDaLista().getTipoTurma().getDescricao();
	}

	@Override
	public String getErrorTitle() {
		return "Erro ao gerar o arquivo PDF da Lista.";
	}

	public void voltar() {
		getDefinition().setFacet("principal");
	}

	/** SETTER AND GETTER **/
	public String getCabecalhoLista() {
		if (cabecalhoLista == null) {
			cabecalhoLista = new String();
		}
		return cabecalhoLista;
	}

	public void setCabecalhoLista(String cabecalhoLista) {
		this.cabecalhoLista = cabecalhoLista;
	}

	public FormatoArquivo getFormatoRelatorio() {
		return formatoRelatorio;
	}

	public void setFormatoRelatorio(FormatoArquivo formatoRelatorio) {
		this.formatoRelatorio = formatoRelatorio;
	}

	public ListaProcessos getListaProcessos() {
		return listaProcessos;
	}

	public void setListaProcessos(ListaProcessos listaProcessos) {
		this.listaProcessos = listaProcessos;
	}

	public Boolean getListaProcessada() {
		return listaProcessada;
	}

	public void setListaProcessada(Boolean listaProcessada) {
		this.listaProcessada = listaProcessada;
	}

	public List<ListaProcessosReport> getListaProcessosReport() {
		if (listaProcessosReport == null) {
			listaProcessosReport = new ArrayList<ListaProcessosReport>();
		}
		return listaProcessosReport;
	}

	public void setListaProcessosReport(List<ListaProcessosReport> listaProcessosReport) {
		this.listaProcessosReport = listaProcessosReport;
	}

}