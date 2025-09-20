package br.gov.stf.estf.assinatura.visao.jsf.beans.consultardocumentosexterno;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;

public class BeanConsultarPecas extends AssinadorBaseBean {

	private static final long serialVersionUID = -4282469608474738969L;

	public static final String VISUALIZADOR_PECAS = "http://sistemas.stf.jus.br/estfvisualizador/jsp/consultarprocessoeletronico/ConsultarProcessoEletronico.jsf?seqobjetoincidente=";
	public static final String SEQ_OBJETO_INCIDENTE_PROCESSO = "seqObjetoIncidenteConsultaExterna";

	private Long seqObjetoIncidente;

	public BeanConsultarPecas() {
	}
	public String consultarPecas() {
		
		setSessionValue(SEQ_OBJETO_INCIDENTE_PROCESSO, seqObjetoIncidente);
		
		return "consultarPecas";
	}

	// ######################### GETTERS & SETTERS #########################

	public Long getSeqObjetoIncidente() {
		return seqObjetoIncidente;
	}

	public void setSeqObjetoIncidente(Long seqObjetoIncidente) {
		this.seqObjetoIncidente = seqObjetoIncidente;
	}
}
