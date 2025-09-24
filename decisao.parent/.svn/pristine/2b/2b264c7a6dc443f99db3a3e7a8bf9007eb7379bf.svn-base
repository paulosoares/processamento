package br.jus.stf.estf.decisao.objetoincidente.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.cabecalho.model.CabecalhosObjetoIncidente.CabecalhoObjetoIncidente;
import br.gov.stf.estf.cabecalho.model.InformacoesParte;
import br.gov.stf.estf.cabecalho.model.OcorrenciasMinistro;
import br.gov.stf.estf.cabecalho.service.CabecalhoObjetoIncidenteService;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.julgamento.model.service.InformacaoPautaProcessoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.objetoincidente.service.ObjetoIncidenteService;
import br.jus.stf.estf.decisao.objetoincidente.support.EspelhoReport;
import br.jus.stf.estf.decisao.objetoincidente.support.EspelhoReport.ItemParte;
import br.jus.stf.estf.decisao.pesquisa.domain.ObjetoIncidenteDto;
import br.jus.stf.estf.decisao.pesquisa.web.incidente.IncidenteFacesBean.ItemEspelho;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources;
import br.jus.stf.estf.decisao.support.action.handlers.RequiresResources.Mode;
import br.jus.stf.estf.decisao.support.action.support.Action;
import br.jus.stf.estf.decisao.support.action.support.ReportAction;
import br.jus.stf.estf.decisao.support.service.ConverterService;
import br.jus.stf.estf.decisao.support.util.TextoUtils;
import br.jus.stf.estf.decisao.support.util.VelocityBuilder;
import br.jus.stf.estf.decisao.texto.service.TextoService;

/**
 * @author Paulo.Estevao
 * @since 07.02.2013 */
@Action(id = "imprimirEspelhoActionFacesBean", name = "Imprimir Espelho", report = true)
@RequiresResources(Mode.Many)
public class ImprimirEspelhoActionFacesBean extends ReportAction<ObjetoIncidenteDto> {

	@Autowired
	private InformacaoPautaProcessoService informacaoPautaProcessoService;

	@Autowired
	private ObjetoIncidenteService objetoIncidenteService;
	
	@Autowired
	private ConverterService converterService;
	
	@Autowired
	private TextoService textoService;
	
	@Autowired
	private VelocityBuilder velocityBuilder;
	
	@Autowired
	private CabecalhoObjetoIncidenteService cabecalhoObjetoIncidenteService;
	
	/* Classes que apresentarão o Campo "Parecer da AGU" */
	private static final String[] CLASSE_AGU = {"ADI", "ADC", "ADPF","ADO"};
	
	private byte[] gerarRelatorioEmHTML(EspelhoReport espelhoReport) throws Exception {
		String retorno = new String();
		try {
			Map<String, Object> mapaParametros = new HashMap<String, Object>();
			mapaParametros.put("espelhoReport", espelhoReport);
			mapaParametros.put("itemEspelhoImpressaoDecisoes", "Informações");
			retorno = velocityBuilder.substituiVariaveisDoTemplate(EspelhoReport.TEMPLATE_RELATORIO, mapaParametros);
		} catch (Exception e) {
			addError("Não foi possível gerar HTML para o Processo." + e.getMessage());
			logger.error("Não foi possível gerar HTML para o Processo.", e);
			throw e;
		}
		return retorno.getBytes();
	}
	
	private List<ItemEspelho> carregarItensEspelho(InformacaoPautaProcesso informacaoPautaProcesso, boolean possuiDecisoes) {
		List<ItemEspelho> itensEspelho = new ArrayList<ItemEspelho>();

		// Tema
		if (informacaoPautaProcesso.getTemaEspelho() != null && informacaoPautaProcesso.getTemaEspelho().length() > 0) {
			itensEspelho.add(geraItemEspelho("Tema", informacaoPautaProcesso.getTemaEspelho()));
		}
		// Tese
		if (informacaoPautaProcesso.getTeseEspelho() != null && informacaoPautaProcesso.getTeseEspelho().length() > 0) {
			itensEspelho.add(geraItemEspelho("Tese", informacaoPautaProcesso.getTeseEspelho()));
		}
		// Parecer da AGU
		if ( informacaoPautaProcesso.getObjetoIncidente() != null ){
			if ( Arrays.asList( CLASSE_AGU ).contains( ( (Processo) informacaoPautaProcesso.getObjetoIncidente().getPrincipal()).getSiglaClasseProcessual()))  {
				if (informacaoPautaProcesso.getParecerAGUEspelho() != null && informacaoPautaProcesso.getParecerAGUEspelho().length() > 0) {
					itensEspelho.add(geraItemEspelho("Parecer da AGU", informacaoPautaProcesso.getParecerAGUEspelho()));
				}
			}
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
				|| possuiDecisoes ) {
			if ( informacaoPautaProcesso.getInformacoesEspelho() != null ){
				itensEspelho.add(geraItemEspelho("Informações", informacaoPautaProcesso.getInformacoesEspelho()));
			}
		}
		
		return itensEspelho;
	}
	
	private ItemEspelho geraItemEspelho(String nome, String conteudo) {
		ItemEspelho itemEspelho = new ItemEspelho();
		itemEspelho.setNome(nome);
		itemEspelho.setConteudo( conteudo.replaceAll("\\n", "<br />") );
		return itemEspelho;
	}
	
	/** 
	 * Carrega os Textos de Decisão de um processo
	 * @param objetoIncidente O Processo do qual se pretende buscar as decisões. */
	private List<String> carregarDecisoesDoProcesso(ObjetoIncidenteDto objetoIncidenteDto)throws ServiceException {
		List<String> decisoes = new ArrayList<String>();
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
		return decisoes;
	}

	private EspelhoReport gerarInformacoesRelatorio(ObjetoIncidenteDto objetoIncidenteDto) throws ServiceException {
		EspelhoReport ippr = new EspelhoReport();
		ippr.setInformacaoPautaProcesso(informacaoPautaProcessoService.recuperar(objetoIncidenteService.recuperarObjetoIncidentePorId(objetoIncidenteDto.getId())));
		if (ippr.getInformacaoPautaProcesso() == null) {
			return null;
		}
		carregarDadosCabecalhoObjetoIncidente(objetoIncidenteDto, ippr);
		ippr.setDecisoes(carregarDecisoesDoProcesso(objetoIncidenteDto));
		ippr.setItensEspelho(carregarItensEspelho(ippr.getInformacaoPautaProcesso(), ippr.getDecisoes().size() > 0));
		return ippr;
	}

	private void carregarDadosCabecalhoObjetoIncidente(ObjetoIncidenteDto objetoIncidenteDto, EspelhoReport ippr)
			throws ServiceException {
		CabecalhoObjetoIncidente cabecalhoObjetoIncidente = cabecalhoObjetoIncidenteService.recuperarCabecalho(objetoIncidenteDto.getId());
		if (cabecalhoObjetoIncidente != null) {
			if (cabecalhoObjetoIncidente.getOcorrenciasMinistro() != null
				&& cabecalhoObjetoIncidente.getOcorrenciasMinistro().getOcorrenciaMinistro().size() > 0) {
				for (OcorrenciasMinistro.OcorrenciaMinistro ministro : cabecalhoObjetoIncidente
						.getOcorrenciasMinistro().getOcorrenciaMinistro()) {
					if (ministro.getCategoriaMinistro().equals("RELATOR") || ministro.getCategoriaMinistro().equals("RELATOR DO INCIDENTE")) {
						ippr.setSufixoRelator("RELATOR");
						ippr.setNomeRelator(ministro.getApresentacaoMinistro());
					} else if (ministro.getCategoriaMinistro().equals("RELATORA") || ministro.getCategoriaMinistro().equals("RELATORA DO INCIDENTE")) {
						ippr.setSufixoRelator("RELATORA");
						ippr.setNomeRelator(ministro.getApresentacaoMinistro());
					}
				}
			}
			
			if (cabecalhoObjetoIncidente.getInformacoesParte() != null
					&& cabecalhoObjetoIncidente.getInformacoesParte().getInformacaoParte().size() > 0) {
				for (InformacoesParte.InformacaoParte parte : cabecalhoObjetoIncidente
						.getInformacoesParte().getInformacaoParte()) {
					ippr.getPartes().add(new ItemParte(parte.getCategoriaParte(), parte.getApresentacaoParte()));
				}
			}
		}
	}

	@Override
	public String getErrorTitle() {
		return "Erro ao gerar o relatório.";
	}

	public void voltar() {
		getDefinition().setFacet("principal");
	}

	@Override
	protected InputStream doReport(Set<ObjetoIncidenteDto> resources) throws IOException {
		ByteArrayOutputStream relatorioCompleto = new ByteArrayOutputStream();
		ByteArrayOutputStream outputRelatorio = new ByteArrayOutputStream();
		try {
			for (ObjetoIncidenteDto objetoIncidenteDto : resources) {
				EspelhoReport espelhoReport = gerarInformacoesRelatorio(objetoIncidenteDto);
				if (espelhoReport != null) {
					relatorioCompleto.write(gerarRelatorioEmHTML(espelhoReport));
				}
			}
			converterService.converterHtmlParaPDF(relatorioCompleto.toByteArray(), outputRelatorio);
			return new ByteArrayInputStream(outputRelatorio.toByteArray());
		} catch (Exception e) {
			addError("Erro ao Imprimir Espelho. " + e.getMessage());
			logger.error("Erro ao Imprimir Espelho.", e);
		}
		
		return null;

	}
}