package br.gov.stf.estf.documento.model.service;

import java.util.Date;

import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.framework.model.service.ServiceException;

public interface PdfService {
	public void salvarExtratoAta ( Long objetoIncidente, Date dataAta, byte[] pdf  ) throws ServiceException;
	public void salvarCertidaoJulgamento ( Long objetoIncidente, Date dataAta, byte[] pdf  ) throws ServiceException;
	
	public void salvarDocumentoTextoAssinado(
			Long documentoTexto, Long documentoEletronico, byte[] pdfAssinado,
			byte[] assinatura, byte[] carimboTempo, Date dataCarimboTempo)
			throws ServiceException;
	
	public void salvarPDFPadrao( DocumentoEletronico documentoEletronico, String nomeSistema, byte[] pdf) throws ServiceException;
	
}
