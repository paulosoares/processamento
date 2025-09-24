package br.jus.stf.estf.decisao.documento.support.assinador;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfSignatureAppearance;

public final class ContextoAssinatura implements Serializable {

	private static final long serialVersionUID = -4801807132705368529L;

	private final String idContexto;
	private final String idDocumento;
	private PdfSignatureAppearance appearance;
	private byte primeiroHash[];
	private String pdfPath;
	private PdfPKCS7 pdfPKCS7;
	private Calendar signDate;
	private int tamanhoEstimado;

	public ContextoAssinatura(String idDocumento) {
		this.idDocumento = idDocumento;
		this.idContexto = gerarIdContexto(this.idDocumento);
	}
	
	private String gerarIdContexto(String idDocumento) {
		String uniqueID = UUID.randomUUID().toString();
		String id = uniqueID + "#" + idDocumento;
		return id;
	}

	public String getIdContexto() {
		return idContexto;
	}

	public String getIdDocumento() {
		return idDocumento;
	}

	public PdfSignatureAppearance getAppearance() {
		return appearance;
	}

	public void setAppearance(PdfSignatureAppearance appearance) {
		this.appearance = appearance;
	}

	public byte[] getPrimeiroHash() {
		return primeiroHash;
	}

	public void setPrimeiroHash(byte[] primeiroHash) {
		this.primeiroHash = primeiroHash;
	}

	public String getPdfPath() {
		return pdfPath;
	}

	public void setPdfPath(String pdfPath) {
		this.pdfPath = pdfPath;
	}

	public PdfPKCS7 getPdfPKCS7() {
		return pdfPKCS7;
	}

	public void setPdfPKCS7(PdfPKCS7 pdfPKCS7) {
		this.pdfPKCS7 = pdfPKCS7;
	}

	public Calendar getSignDate() {
		return signDate;
	}

	public void setSignDate(Calendar signDate) {
		this.signDate = signDate;
	}

	public int getTamanhoEstimado() {
		return tamanhoEstimado;
	}

	public void setTamanhoEstimado(int tamanhoEstimado) {
		this.tamanhoEstimado = tamanhoEstimado;
	}
	
}