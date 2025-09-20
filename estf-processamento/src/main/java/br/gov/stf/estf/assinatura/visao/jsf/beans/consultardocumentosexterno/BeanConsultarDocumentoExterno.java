package br.gov.stf.estf.assinatura.visao.jsf.beans.consultardocumentosexterno;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;

public class BeanConsultarDocumentoExterno extends AssinadorBaseBean {

	private static final long serialVersionUID = -7228734104478758301L;
	
	public static final String SEQ_OBJETO_INCIDENTE_CONSULTA_EXTERNA = "seqObjetoIncidenteConsultaExterna";
	public static final String PORTAL_STF = "http://www.stf.jus.br/portal/processo/verProcessoAndamento.asp?incidente=";
	

	private Long seqObjetoIncidente;

	public BeanConsultarDocumentoExterno() {

	}


	public String consultarProcessoInternet() {
		
		setSessionValue(SEQ_OBJETO_INCIDENTE_CONSULTA_EXTERNA,
				seqObjetoIncidente);
		return "consultarProcessoInternet";


	}


	
	// ######################### GETTERS & SETTERS #########################

	public Long getSeqObjetoIncidente() {
		return seqObjetoIncidente;
	}

	public void setSeqObjetoIncidente(Long seqObjetoIncidente) {
		this.seqObjetoIncidente = seqObjetoIncidente;
	}
}