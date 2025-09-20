package br.gov.stf.estf.assinatura.stfoffice.editor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpSession;

import br.gov.stf.eprocesso.servidorpdf.servico.modelo.ExtensaoEnum;
import br.gov.stf.estf.assinatura.handler.AssinadorHandler;
import br.gov.stf.estf.assinatura.service.DocumentoHandlerServiceLocal;
import br.gov.stf.estf.assinatura.visao.util.RefreshController;
import br.gov.stf.estf.converter.DocumentConversionException;
import br.gov.stf.estf.converter.source.ByteArrayDocumentSource;
import br.gov.stf.estf.converter.target.ByteArrayDocumentTarget;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.stfoffice.editor.handler.IEditorHandler;
import br.gov.stf.stfoffice.handler.HandlerException;
import br.jus.stf.estf.montadortexto.MontadorTextoServiceException;

public class RequisicaoAbrirDocumentoHandler extends AssinadorHandler implements IEditorHandler {
	private RefreshController refreshController;
	private DocumentoHandlerServiceLocal documentoHandlerServiceLocal;

	public RequisicaoAbrirDocumentoHandler() {
		super();
		refreshController = (RefreshController) locator.getService("refreshController");
		documentoHandlerServiceLocal = (DocumentoHandlerServiceLocal) locator
				.getService("documentoHandlerServiceLocal");
	}

	private RequisicaoAbrirDocumento requisicaoAbrirModelo;

	public void setAtributos(RequisicaoAbrirDocumento requisicaoAbrirModelo, HttpSession session) {
		this.requisicaoAbrirModelo = requisicaoAbrirModelo;
	}

	public byte[] recuperarDocumento() throws HandlerException {
		byte[] odt = requisicaoAbrirModelo.getDocumento();
		return odt;
	}

	public void fecharDocumento() throws HandlerException {
		// TODO Auto-generated method stub

	}

	public void gerarPDF(byte[] pdf) throws HandlerException {

		Comunicacao comunicacao = requisicaoAbrirModelo.getComunicacao();

		documentoHandlerServiceLocal.gerarPdf(pdf, comunicacao, requisicaoAbrirModelo.getUser());
		refreshController.executarRefreshPagina();

	}

	public void salvarDocumento(byte[] odt) throws HandlerException {

		try {
			ArquivoEletronico arquivoEletronico = requisicaoAbrirModelo.getArquivoEletronico();
			arquivoEletronico.setConteudo(odt);
			arquivoEletronicoService.alterar(arquivoEletronico);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw new HandlerException("Erro ao atualizar ArquivoEletronico");
		}
	}

	public InputStream converteArquivo(byte[] source, ExtensaoEnum extensaoOriginal, ExtensaoEnum extensaoDestino)
			throws MontadorTextoServiceException, IOException {
		String formatoSource = extensaoOriginal.getContentType();
		String formatoTarget = extensaoDestino.getContentType();
		return converteArquivo(source, formatoSource, formatoTarget);

	}

	/**
	 * Converte o arquivo especificado para o formato solicitado
	 * @param source O arquivo para conversão
	 * @param formatoSource O mimetype do arquivo fonte
	 * @param formatoTarget O mimetype do arquivo de destino
	 * @return
	 * @throws MontadorTextoServiceException
	 */
	public InputStream converteArquivo(byte[] source, String formatoSource, String formatoTarget)
			throws MontadorTextoServiceException {
		ByteArrayDocumentTarget target1 = new ByteArrayDocumentTarget();

		try {
			converterService
					.convertDocument(new ByteArrayDocumentSource(source), formatoSource, target1, formatoTarget);
		} catch (DocumentConversionException e) {
			throw new MontadorTextoServiceException("Erro durante a conversão dos textos de RTF para PDF", e);
		}
		return new ByteArrayInputStream(target1.getByteArray());
	}

}
