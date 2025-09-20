package br.gov.stf.estf.assinatura.visao.jsf.beans.expediente.revisao;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.text.PDFTextStripper;
import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.security.UsuarioAssinatura;
import br.gov.stf.estf.assinatura.service.ComunicacaoServiceLocal;
import br.gov.stf.estf.assinatura.service.impl.CollectionUtils;
import br.gov.stf.estf.assinatura.stfoffice.editor.RequisicaoAbrirDocumento;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.jsf.beans.ComunicacaoDocumento;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.documento.model.util.FiltroPesquisarDocumentosAssinatura;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;
import br.gov.stf.stfoffice.editor.jnlp.AbrirDocumento.ArgumentosAbrirDocumento;
import br.gov.stf.stfoffice.web.requisicao.jnlp.RequisicaoJnlp;

public class BeanRevisao extends AssinadorBaseBean {

	private static final long serialVersionUID = -4971716149801262315L;
	@SuppressWarnings("unused")
	private static final Long COD_SETOR_GAB_SEJ = 600000627L;
	@SuppressWarnings("unused")
	private static final Long COD_SETOR_GAB_CELSO_MELO = 600000003L;
	private List<CheckableDataTableRowWrapper> documentosAguadandoRevisao;
	private CheckableDataTableRowWrapper documentoSelecionado;
	private HtmlDataTable tabelaAguardandoRevisao;
	private static final Log LOG = LogFactory.getLog(BeanRevisao.class);
	private String anotacaoCancelamento;
	private ComunicacaoDocumento comDocumentoTemporariaRowData;
	
	private String textoComunicacao;

	public BeanRevisao() {
		inicializar();
	}
	
	public void inicializar() {
		pesquisarDocumentosAguardandoRevisao();
		documentoSelecionado = null;
		anotacaoCancelamento = null;		
	}
	
	public void pesquisarDocumentosAguardandoRevisao() {

		try {
			documentosAguadandoRevisao = getCheckableDocumentoList(
					getComunicacaoServiceLocal().pesquisarDocumentos(TipoFaseComunicacao.EM_REVISAO.getCodigoFase(), getSetorUsuarioAutenticado()));
			
		} catch (Exception e) {
			
			reportarErro("Erro ao recuperar os documentos. " + e.getMessage());
			setDocumentosAguadandoRevisao(null);
			e.printStackTrace();
			return;
		}
	}

	public void marcarTodosTextos() {
		marcarOuDesmarcarTodas(documentosAguadandoRevisao);
	}
	
	public void atualizarDocumentoSelecionado() throws ServiceException {
		documentoSelecionado = (CheckableDataTableRowWrapper) tabelaAguardandoRevisao.getRowData();
	}

	public void retornarParaCorrecao() {
		UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();
		ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();

		try {
			comunicacaoServiceLocal.devolverParaSetorOrigem(((ComunicacaoDocumento)documentoSelecionado.getWrappedObject()), anotacaoCancelamento, usuarioAssinatura);
			reportarInformacao("Documento retornado para correção!");
			inicializar();
			
		} catch (RegraDeNegocioException exception) {
			
			reportarAviso(exception);
			
		} catch (ServiceLocalException exception) {
			
			reportarErro("Erro ao retornar documento para correção.", exception, LOG);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void finalizarRevisao() {
		List<ComunicacaoDocumento> documentosAFinalizar = retornarItensSelecionados(documentosAguadandoRevisao);
		
		List<Long> setoresEspeciais = new ArrayList<Long>();
		setoresEspeciais.add(Setor.CODIGO_GABINETE_SECRETARIA_JUDICIARIA);
		setoresEspeciais.add(Setor.CODIGO_SETOR_GABINETE_MINISTRO_CELSO_MELLO);
		setoresEspeciais.add(Setor.CODIGO_SETOR_GABINETE_MINISTRO_ROBERTO_BARROSO);
		setoresEspeciais.add(Setor.CODIGO_SETOR_GABINETE_MINISTRO_DIAS_TOFFOLI);
		setoresEspeciais.add(Setor.CODIGO_SETOR_GABINETE_MINISTRO_RICARDO_LEWANDOWSKI);
		setoresEspeciais.add(Setor.CODIGO_SETOR_PRESIDENCIA);
		
		
		try {
			if(setoresEspeciais.contains(getSetorUsuarioAutenticado().getId())){
				for( ComunicacaoDocumento docs : documentosAFinalizar){
					getFaseComunicacaoService().incluirFase(TipoFaseComunicacao.AGUARDANDO_ASSINATURA, docs.getComunicacao(), null, null);
				}
			}else{
				getComunicacaoServiceLocal().finalizarRevisao(documentosAFinalizar, (UsuarioAssinatura) getUser());
			}
			reportarInformacao("A revisão dos documentos foi finalizada com sucesso!");
			inicializar();
			
		} catch (ServiceLocalException e) {
			reportarErro("Erro ao finalizar revisão dos documentos.", e, LOG);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public void recuperaLinhaParaModalPanel(ActionEvent evt) {
		comDocumentoTemporariaRowData = (ComunicacaoDocumento) ((CheckableDataTableRowWrapper) tabelaAguardandoRevisao.getRowData()).getWrappedObject();
	}
	
	@SuppressWarnings("deprecation")
	public String editarDocumentos() {
		Comunicacao documento = comDocumentoTemporariaRowData.getComunicacao();

		try{
			documento = getComunicacaoService().recuperarPorId(documento.getId());
			/*List<ComunicacaoDocumentoResult> lista = getComunicacaoServiceLocal().pesquisarDocumentos(TipoFaseComunicacao.EM_REVISAO.getCodigoFase(),getSetorUsuarioAutenticado());
			for (ComunicacaoDocumentoResult c : lista){
				if (c.getComunicacao().getId().equals(documento.getId())){
						comDocumentoTemporariaRowData = new ComunicacaoDocumento(c);
						documento = comDocumentoTemporariaRowData.getComunicacao();
				}
			}*/
		}catch(Exception e){
			documento = comDocumentoTemporariaRowData.getComunicacao();
		}
		
		if (isFaseAssinado(documento)) {
			reportarAviso("Documentos assinados não podem ser editados!");
			return null;
		}

		UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();
		RequisicaoAbrirDocumento req = new RequisicaoAbrirDocumento();

		req.setArquivoEletronico(documento.getArquivoEletronico());
		req.setNomeDocumento(documento.getDscNomeDocumento());
		req.setComunicacao(documento);
		req.setUser(usuarioAssinatura.getUsername());
		req.setGerarPDF(true);

		if ((!isUsuarioEditorTextos()) && (!isUsuarioMaster())) {
			req.setReadOnly(true);
		} else {
			req.setReadOnly(false);
		}

		req.setTipoSalvar(ArgumentosAbrirDocumento.TIPO_ACAO_SALVAR_SERVIDOR);
		req.setDocumento(documento.getArquivoEletronico().getConteudo());

		setRequestValue(RequisicaoJnlp.REQUISICAO_JNLP, req);

		return "stfOfficeServlet";
	}
	
	public void abrirPdf(ActionEvent e){
		recuperaLinhaParaModalPanel(null);
		FiltroPesquisarDocumentosAssinatura filtro = new FiltroPesquisarDocumentosAssinatura();
		List<Long> id = new ArrayList<Long>();
		id.add(comDocumentoTemporariaRowData.getComunicacao().getId());
		filtro.setIds(id);				
		filtro.setSetor(getSetorUsuarioAutenticado());		
		filtro.setFaseDocumento(TipoFaseComunicacao.EM_REVISAO.getCodigoFase());		
		filtro.setCarregarFilhos(false);
		try{
			ComunicacaoDocumentoResult result = getComunicacaoServiceLocal().pesquisarDocumentos(filtro).getLista().get(0);
			comDocumentoTemporariaRowData = new ComunicacaoDocumento(result);
			String link = comDocumentoTemporariaRowData.getLinkPDF();
			link = link.replaceAll("../../", "/");
			HttpServletRequest httpRequest = (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
			StringBuffer requestURLStringBuffer = httpRequest.getRequestURL();
			int index = requestURLStringBuffer.indexOf("/", 8);
			String serverURL = requestURLStringBuffer.substring(0, index);
			String contextURL = serverURL + httpRequest.getContextPath();
	
			getFacesContext().getExternalContext().redirect(contextURL + link);
		}catch(ServiceException ee){
			reportarErro("Erro ao recuperar caminho do arquivo.");
		}catch(IOException ee){
			reportarErro("Erro ao recuperar caminho do arquivo.");
		}catch(RegraDeNegocioException ee){
			reportarErro("Erro ao recuperar caminho do arquivo.");
		} catch (ServiceLocalException e1) {
			reportarErro("Erro ao recuperar caminho do arquivo.");
		}
		
	}
	
	private boolean isFaseAssinado(Comunicacao documento) {
		return documento.getFaseAtual().equalsIgnoreCase(TipoFaseComunicacao.ASSINADO.getDescricao());
	}
	
	public void devolverDocumentosAction(ActionEvent evt) {
		devolverDocumentos();
	}
	
	public void devolverDocumentos() {
		ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();
		UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();

		try {
			comunicacaoServiceLocal.devolverParaSetorOrigem(comDocumentoTemporariaRowData, anotacaoCancelamento, usuarioAssinatura);
			reportarInformacao("Documento devolvido para correção!");
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao devolver o documento para correção.", exception, LOG);
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		}
	}
	
	public void imprimirDocumentosAction(ActionEvent e){
		imprimirDocumentos();		
		
	}
	
	private void imprimirDocumentos(){
		List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(documentosAguadandoRevisao);
		
		if (CollectionUtils.isVazia(selecionados)) {
			reportarAviso("É necessário selecionar pelo menos um documento.");				
		} else {
			List<CheckableDataTableRowWrapper> selecionadosCheckable = retornarItensCheckableSelecionados(documentosAguadandoRevisao);
			List<Long> idsDocumentos = new ArrayList<Long>();
			
			try{
				for(CheckableDataTableRowWrapper wrapper : selecionadosCheckable){
					URI uri = new URI(((ComunicacaoDocumento)wrapper.getWrappedObject()).getLinkPDF());
					String query = uri.getQuery();
					String idDoc  = (query.substring(0, query.indexOf('&')).split("="))[1];
					idsDocumentos.add(Long.parseLong(idDoc));					
				}
				byte[] conteudopdfs = getDocumentoEletronicoService().recuperarDocumentosPDF(idsDocumentos);
				setConteudoDocumentoDownload(conteudopdfs);
				downloadArquivoPdf();
								
			}catch(ServiceException e){
				reportarErro("Erro ao imprimir documentos");
			} catch (URISyntaxException e) {				// 
				reportarErro("Erro ao imprimir documentos");			
			}
		}
	}
		
	public List<CheckableDataTableRowWrapper> getDocumentosAguadandoRevisao() {
		return documentosAguadandoRevisao;
	}

	public void setDocumentosAguadandoRevisao(
			List<CheckableDataTableRowWrapper> documentosAguadandoRevisao) {
		this.documentosAguadandoRevisao = documentosAguadandoRevisao;
	}

	public String getAnotacaoCancelamento() {
		return anotacaoCancelamento;
	}

	public void setAnotacaoCancelamento(String anotacaoCancelamento) {
		this.anotacaoCancelamento = anotacaoCancelamento;
	}

	public HtmlDataTable getTabelaAguardandoRevisao() {
		return tabelaAguardandoRevisao;
	}

	public void setTabelaAguardandoRevisao(HtmlDataTable tabelaAguardandoRevisao) {
		this.tabelaAguardandoRevisao = tabelaAguardandoRevisao;
	}
	
	public String getRecuperarTextoComunicacaoAction(){
		
		return getRecuperarTextoDocumento(tabelaAguardandoRevisao);
		
	}
	
	public String getTextoComunicacao(){
		return this.textoComunicacao;
	}
}
