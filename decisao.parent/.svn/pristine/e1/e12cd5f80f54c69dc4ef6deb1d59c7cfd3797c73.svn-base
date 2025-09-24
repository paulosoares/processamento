package br.jus.stf.estf.decisao.texto.support;

import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;

public class TextoComSituacaoDaPublicacaoVO implements
		Comparable<TextoComSituacaoDaPublicacaoVO> {
	private Texto texto;
	private SituacaoDoTextoParaPublicacao situacaoDaPublicacaoDoTexto;

	public TextoComSituacaoDaPublicacaoVO(Texto texto,
			SituacaoDoTextoParaPublicacao situacaoDaPublicacaoDoTexto) {
		this.texto = texto;
		this.situacaoDaPublicacaoDoTexto = situacaoDaPublicacaoDoTexto;
	}

	public Texto getTexto() {
		return texto;
	}

	public void setTexto(Texto texto) {
		this.texto = texto;
	}

	public SituacaoDoTextoParaPublicacao getSituacaoDaPublicacaoDoTexto() {
		return situacaoDaPublicacaoDoTexto;
	}

	public void setSituacaoDaPublicacaoDoTexto(
			SituacaoDoTextoParaPublicacao situacaoDaPublicacaoDoTexto) {
		this.situacaoDaPublicacaoDoTexto = situacaoDaPublicacaoDoTexto;
	}

	
	public int compareTo(TextoComSituacaoDaPublicacaoVO outroTextoComSituacao) {
		int comparador = getSituacaoDaPublicacaoDoTexto().compareTo(
				outroTextoComSituacao.getSituacaoDaPublicacaoDoTexto());
		if (comparador == 0) {
			Processo processoTexto = ObjetoIncidenteUtil.getProcesso(getTexto().getObjetoIncidente());
			Processo processoOutroTexto = ObjetoIncidenteUtil.getProcesso(outroTextoComSituacao.getTexto().getObjetoIncidente());
			
			comparador = processoTexto.getClasseProcessual().getId().compareTo(
					processoOutroTexto.getClasseProcessual().getId());
			if (comparador == 0) {
				comparador = processoTexto.getNumeroProcessual().compareTo(
						processoOutroTexto.getNumeroProcessual());
			}
		}
		return comparador;
	}

}
