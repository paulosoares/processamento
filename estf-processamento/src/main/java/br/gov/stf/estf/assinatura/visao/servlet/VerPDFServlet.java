package br.gov.stf.estf.assinatura.visao.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.processostf.TipoConfidencialidade;
import br.gov.stf.framework.util.ApplicationFactory;

public class VerPDFServlet extends VerPDFBaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 957713431287593402L;
	
	public static final String PARAM_SEQ_DOCUMENTO_ELETRONICO = "seqDocumentoEletronico";
	public static final String PARAM_NOME_DOCUMENTO = "nomeDocumento";
	public static final String PARAM_CONFIDENCIALIDADE = "confidencialidadeComunicacao";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		OutputStream os = resp.getOutputStream();
		byte[] conteudo = null;
		String nomeArquivo = req.getParameter(PARAM_NOME_DOCUMENTO);
		try {
			DocumentoEletronicoService documentoEletronicoService = (DocumentoEletronicoService) ApplicationFactory.getInstance().getServiceLocator().getService("documentoEletronicoService");
			
			String sSeqDocumentoEletronico = req.getParameter(PARAM_SEQ_DOCUMENTO_ELETRONICO);
			if ( sSeqDocumentoEletronico!=null ) {
				Long seqDocumentoEletronico = new Long ( sSeqDocumentoEletronico );
				DocumentoEletronico documentoEletronico = documentoEletronicoService.recuperarPorId(seqDocumentoEletronico);
				conteudo = documentoEletronico.getArquivo();
				String confidencialidade = req.getParameter(PARAM_CONFIDENCIALIDADE);
				if (TipoConfidencialidade.SEGREDO_JUSTICA.getDescricao().replaceAll(" ","_").replaceAll("ç","c")
					.equals(confidencialidade))
					conteudo = incluirTagConfidencialidade(conteudo);
					
			} 
			
			//resp.setContentType("application/octet-stream");
			resp.setContentType("application/x-pdf");
			resp.setHeader("Content-disposition","attachment; filename=" + nomeArquivo + ".pdf");	
			resp.setHeader("cache-control", "no-cache");			
			resp.setBufferSize(1024);
			os.write(conteudo);		
			
			
		} catch ( Exception e ) {
			throw new ServletException(e);
		} finally {
			os.flush();
			resp.flushBuffer();
		}
	}
}
