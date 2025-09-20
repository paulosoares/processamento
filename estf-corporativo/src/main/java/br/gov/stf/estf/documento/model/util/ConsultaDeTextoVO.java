package br.gov.stf.estf.documento.model.util;

public class ConsultaDeTextoVO implements IConsultaDeTexto {
	private Long idObjetoIncidente;
	private Long tipoDocumentoTexto;
	private Long tipoTexto;

	public void setIdObjetoIncidente(Long idObjetoIncidente) {
		this.idObjetoIncidente = idObjetoIncidente;
	}

	public void setTipoDocumentoTexto(Long tipoDocumentoTexto) {
		this.tipoDocumentoTexto = tipoDocumentoTexto;
	}

	public void setTipoTexto(Long tipoTexto) {
		this.tipoTexto = tipoTexto;
	}

	public Long getIdObjetoIncidente() {
		return idObjetoIncidente;
	}

	public Long getTipoDocumentoTexto() {
		return tipoDocumentoTexto;
	}

	public Long getTipoTexto() {
		return tipoTexto;
	}

}
