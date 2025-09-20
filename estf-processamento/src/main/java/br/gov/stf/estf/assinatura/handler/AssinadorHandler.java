package br.gov.stf.estf.assinatura.handler;

import br.gov.stf.estf.cabecalho.service.CabecalhoObjetoIncidenteService;
import br.gov.stf.estf.converter.DocumentConverterService;
import br.gov.stf.estf.documento.model.service.ArquivoEletronicoService;
import br.gov.stf.estf.documento.model.service.ArquivoProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.ComunicacaoIncidenteService;
import br.gov.stf.estf.documento.model.service.ControleVotoService;
import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.DocumentoTextoService;
import br.gov.stf.estf.documento.model.service.FaseComunicacaoService;
import br.gov.stf.estf.documento.model.service.PdfService;
import br.gov.stf.estf.documento.model.service.TextoDiversoService;
import br.gov.stf.estf.documento.model.service.TextoService;
import br.gov.stf.estf.processostf.model.service.HistoricoProcessoOrigemService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.processostf.model.service.ProcessoService;
import br.gov.stf.estf.processostf.model.service.SumulaService;
import br.gov.stf.estf.publicacao.model.service.ConteudoPublicacaoService;
import br.gov.stf.estf.publicacao.model.service.EstruturaPublicacaoService;
import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.gov.stf.framework.util.ApplicationFactory;
import br.gov.stf.framework.util.IServiceLocator;
import br.jus.stf.estf.montadortexto.OpenOfficeMontadorTextoService;


public class AssinadorHandler {
	
	protected IServiceLocator locator;
	protected TextoService textoService;
	protected ConteudoPublicacaoService conteudoPublicacaoService;
	protected EstruturaPublicacaoService estruturaPublicacaoService;
	protected ProcessoService processoService;
	protected DocumentoEletronicoService documentoEletronicoService;
	protected DocumentoTextoService documentoTextoService;
	protected PdfService pdfService;
	protected ArquivoEletronicoService arquivoEletronicoService;
	protected TextoDiversoService textoDiversoService;
	protected SumulaService sumulaService;
	protected ArquivoProcessoEletronicoService arquivoProcessoEletronicoService;
	protected ObjetoIncidenteService objetoIncidenteService;
	protected UsuarioService usuarioService;
	protected DocumentConverterService converterService;
	protected DocumentoComunicacaoService documentoComunicacaoService;
	protected ControleVotoService controleVotoService;
	protected CabecalhoObjetoIncidenteService cabecalhoObjetoIncidenteService;
	protected HistoricoProcessoOrigemService historicoProcessoOrigemService;
	protected OpenOfficeMontadorTextoService openOfficeMontadorTextoService;
	protected FaseComunicacaoService faseComunicacaoService;
	protected ComunicacaoIncidenteService comunicacaoIncidenteService;

	
	public AssinadorHandler () {		
		locator = ApplicationFactory.getInstance().getServiceLocator();
		textoService = (TextoService) locator.getService("textoService");
		conteudoPublicacaoService = (ConteudoPublicacaoService) locator.getService("conteudoPublicacaoService");
		estruturaPublicacaoService = (EstruturaPublicacaoService) locator.getService("estruturaPublicacaoService");
		processoService = (ProcessoService) locator.getService("processoService");
		documentoEletronicoService = (DocumentoEletronicoService) locator.getService("documentoEletronicoService");
		documentoTextoService = (DocumentoTextoService) locator.getService("documentoTextoService");
		pdfService = (PdfService) locator.getService("pdfService");
		arquivoEletronicoService = (ArquivoEletronicoService) locator.getService("arquivoEletronicoService");
		textoDiversoService = (TextoDiversoService) locator.getService("textoDiversoService");
		sumulaService = (SumulaService) locator.getService("sumulaService");
		arquivoProcessoEletronicoService = (ArquivoProcessoEletronicoService) locator.getService("arquivoProcessoEletronicoService");
		objetoIncidenteService = (ObjetoIncidenteService) locator.getService("objetoIncidenteService");
		usuarioService = (UsuarioService) locator.getService("usuarioService");
		converterService = (DocumentConverterService) locator.getService("converterService");
		documentoComunicacaoService = (DocumentoComunicacaoService) locator.getService("documentoComunicacaoService");
		controleVotoService = (ControleVotoService) locator.getService("controleVotoService");
		cabecalhoObjetoIncidenteService = (CabecalhoObjetoIncidenteService) locator.getService("cabecalhoObjetoIncidenteService");
		historicoProcessoOrigemService = (HistoricoProcessoOrigemService) locator.getService("historicoProcessoOrigemService");	
		openOfficeMontadorTextoService = (OpenOfficeMontadorTextoService) locator.getService("openOfficeMontadorTextoService");	
		faseComunicacaoService = (FaseComunicacaoService) locator.getService("faseComunicacaoService");	
		comunicacaoIncidenteService = (ComunicacaoIncidenteService) locator.getService("comunicacaoIncidenteService");		
	}

}
