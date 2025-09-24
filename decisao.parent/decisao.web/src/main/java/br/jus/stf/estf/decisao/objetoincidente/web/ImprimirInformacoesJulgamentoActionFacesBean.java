package br.jus.stf.estf.decisao.objetoincidente.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.documento.model.service.ControleVistaService;
import br.gov.stf.estf.entidade.documento.ControleVista;
import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.julgamento.model.service.InformacaoPautaProcessoService;
import br.gov.stf.estf.julgamento.model.service.JulgamentoProcessoService;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.objetoincidente.support.InformacoesJulgamentoReport;
import br.jus.stf.estf.decisao.objetoincidente.support.InformacoesJulgamentoReport.ObjetoIncidenteReport;
import br.jus.stf.estf.decisao.objetoincidente.support.InformacoesJulgamentoReport.ObjetoIncidenteReportComparator;
import br.jus.stf.estf.decisao.objetoincidente.support.InformacoesJulgamentoReport.SessaoReport;
import br.jus.stf.estf.decisao.objetoincidente.support.InformacoesJulgamentoReport.SessaoReportComparator;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ReportAction;
import br.jus.stf.estf.decisao.support.service.ConverterService;
import br.jus.stf.estf.decisao.support.util.FormatoArquivo;
import br.jus.stf.estf.decisao.support.util.VelocityBuilder;

/**
 * @author Paulo.Estevao
 * @since 18.02.2013 */
@Action(id = "imprimirInformacoesJulgamentoActionFacesBean", name = "Imprimir Informações de Julgamento", view = "/acoes/objetoincidente/imprimirInformacoesJulgamento.xhtml")
@RequiresResources(Mode.Many)
public class ImprimirInformacoesJulgamentoActionFacesBean extends ReportAction<ObjetoIncidenteDto> {

	@Autowired
	private InformacaoPautaProcessoService informacaoPautaProcessoService;

	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Autowired
	private ConverterService converterService;
	
	@Autowired
	private JulgamentoProcessoService julgamentoProcessoService;
	
	@Autowired
	private MinistroService ministroService;
	
	@Autowired
	private ControleVistaService controleVistaService;
	
	@Autowired
	private VelocityBuilder velocityBuilder;
	
	private Boolean agruparRelatorioPorSessao = Boolean.FALSE;
	
	private byte[] gerarRelatorioEmHTML(InformacoesJulgamentoReport informacoesJulgamentoReport) throws Exception {
		String retorno = new String();
		try {
			Map<String, Object> mapaParametros = new HashMap<String, Object>();
			mapaParametros.put("informacoesJulgamentoReport", informacoesJulgamentoReport);
			retorno = velocityBuilder.substituiVariaveisDoTemplate(InformacoesJulgamentoReport.TEMPLATE_RELATORIO, mapaParametros);
		} catch (Exception e) {
			addError("Não foi possível gerar HTML para o Processo." + e.getMessage());
			logger.error("Não foi possível gerar HTML para o Processo.", e);
			throw e;
		}
		return retorno.getBytes();
	}

	private InformacoesJulgamentoReport gerarInformacoesRelatorio(Set<ObjetoIncidenteDto> resources) throws ServiceException {
		InformacoesJulgamentoReport ijr = new InformacoesJulgamentoReport();
		
		if (agruparRelatorioPorSessao) {
			for (ObjetoIncidenteDto objetoIncidenteDto : resources) {
				ObjetoIncidente<?> oi = objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidenteDto.getId());
				JulgamentoProcesso jp = julgamentoProcessoService.pesquisaSessaoNaoFinalizada(oi, TipoAmbienteConstante.PRESENCIAL);
				InformacaoPautaProcesso ipp = informacaoPautaProcessoService.recuperar(oi);
				Ministro relator = ministroService.recuperarPorId(objetoIncidenteDto.getIdRelator());
				if (jp != null) {
					SessaoReport sessaoReport = ijr.getSessaoReport(jp.getSessao());
					if (sessaoReport == null) {
						sessaoReport = new SessaoReport();
						sessaoReport.setSessao(jp.getSessao());
						sessaoReport.getObjetosIncidente().add(instanciarObjetoIncidenteReport(oi, relator, ipp));
						ijr.getSessoes().add(sessaoReport);
					} else {
						sessaoReport.getObjetosIncidente().add(instanciarObjetoIncidenteReport(oi, relator, ipp));
					}
				} else {
					ijr.getObjetosIncidenteSemSessao().add(instanciarObjetoIncidenteReport(oi, relator, ipp));
				}
			}
		} else {
			for (ObjetoIncidenteDto objetoIncidenteDto : resources) {
				ObjetoIncidente<?> oi = objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidenteDto.getId());
				InformacaoPautaProcesso ipp = informacaoPautaProcessoService.recuperar(oi);
				Ministro relator = ministroService.recuperarPorId(objetoIncidenteDto.getIdRelator());
				ObjetoIncidenteReport oir = instanciarObjetoIncidenteReport(oi, relator, ipp);
				ijr.getObjetosIncidente().add(oir);
			}
		}
		ordenarRelatorio(ijr);
		return ijr;
	}

	private void ordenarRelatorio(InformacoesJulgamentoReport ijr) {
		for (SessaoReport sessao : ijr.getSessoes()) {
			Collections.sort(sessao.getObjetosIncidente(), new ObjetoIncidenteReportComparator());
		}
		Collections.sort(ijr.getSessoes(), new SessaoReportComparator());
		Collections.sort(ijr.getObjetosIncidente(), new ObjetoIncidenteReportComparator());
		Collections.sort(ijr.getObjetosIncidenteSemSessao(), new ObjetoIncidenteReportComparator());
	}

	private ObjetoIncidenteReport instanciarObjetoIncidenteReport(ObjetoIncidente<?> objetoIncidente, Ministro relator,
			InformacaoPautaProcesso ipp) throws ServiceException {
		ObjetoIncidenteReport oir = new ObjetoIncidenteReport();
		oir.setObjetoIncidente(objetoIncidente);
		oir.setInformacaoPautaProcesso(ipp);
		oir.setRelator(relator);
		adicionarInformacaoDeVista(objetoIncidente, oir);
		return oir;
	}

	private void adicionarInformacaoDeVista(ObjetoIncidente<?> objetoIncidente, ObjetoIncidenteReport oir)
			throws ServiceException {
		List<ControleVista> vistasAoProcesso = controleVistaService.recuperar(((Processo) objetoIncidente.getPrincipal()).getSiglaClasseProcessual(), 
                ((Processo) objetoIncidente.getPrincipal()).getNumeroProcessual());
		List<Ministro> ministrosVista = new ArrayList<Ministro>();
		ControleVista controleVistaMaisRecente = null;
		for (ControleVista cv : vistasAoProcesso) {
			if (controleVistaMaisRecente == null) {
				controleVistaMaisRecente = cv;
			} else if(controleVistaMaisRecente.getDataInicio() != null && cv.getDataInicio() != null) {
				if (controleVistaMaisRecente.getDataInicio().compareTo(cv.getDataInicio()) < 0) {
					controleVistaMaisRecente = cv;
				}
			} else if (cv.getDataInicio() != null){
				controleVistaMaisRecente = cv;
			}
		}
		
		if (controleVistaMaisRecente != null && controleVistaMaisRecente.getCodigoMinistro() != null) {
			ministrosVista.add(ministroService.recuperarPorId(controleVistaMaisRecente.getCodigoMinistro()));
		}
		oir.setMinistrosVista(ministrosVista);
	}

	@Override
	public String getErrorTitle() {
		return "Erro ao gerar o relatório.";
	}

	public void voltar() {
		getDefinition().setFacet("principal");
	}
	
	public Boolean getAgruparRelatorioPorSessao() {
		return agruparRelatorioPorSessao;
	}
	
	public void setAgruparRelatorioPorSessao(Boolean agruparRelatorioPorSessao) {
		this.agruparRelatorioPorSessao = agruparRelatorioPorSessao;
	}

	@Override
	protected InputStream doReport(Set<ObjetoIncidenteDto> resources) throws IOException {
		ByteArrayOutputStream outputRelatorio = new ByteArrayOutputStream();
		try {
			InformacoesJulgamentoReport report = gerarInformacoesRelatorio(getResources());
			converterService.converterHtmlParaPDF(gerarRelatorioEmHTML(report), outputRelatorio);
			return new ByteArrayInputStream(outputRelatorio.toByteArray());
		} catch (Exception e) {
			addError("Erro ao Imprimir Informações de Julgamento. " + e.getMessage());
			logger.error("Erro ao Imprimir Informações de Julgamento.", e);
		}
		
		return null;
	}
	
	@Override
	protected FormatoArquivo getFormatoArquivo() {
		return FormatoArquivo.PDF;
	}
}