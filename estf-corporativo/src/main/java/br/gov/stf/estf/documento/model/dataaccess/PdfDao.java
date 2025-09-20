package br.gov.stf.estf.documento.model.dataaccess;

import java.util.Date;

import org.hibernate.HibernateException;

import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.framework.model.dataaccess.DaoException;

public interface PdfDao {
	public void salvarPdf (Long objetoIncidente,
			Date dataAta, byte[] pdf, Long codigoTipoDocumentoTexto) throws DaoException;
	
	public void salvarDocumentoTextoAssinado(
			Long documentoTexto, Long documentoEletronico, byte[] pdfAssinado,
			byte[] assinatura, byte[] carimboTempo, Date dataCarimboTempo)
			throws DaoException;
	
	public void salvarPDFPadrao( DocumentoEletronico documentoEletronico, String nomeSistema, byte[] pdf) throws DaoException;
	
	public Long getSeqTextos(Long objetoIncidente, Date dataAta) throws HibernateException, DaoException;
}
