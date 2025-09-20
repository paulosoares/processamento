package br.gov.stf.estf.documento.model.util;

import br.gov.stf.estf.entidade.documento.Texto;

public class ConsultaDePecaProcessoEletronicoDoTextoAdapter implements IConsultaPecaProcessoEletronicoDoTexto {
	private Texto texto;

	private Texto getTexto() {
		return texto;
	}

	public ConsultaDePecaProcessoEletronicoDoTextoAdapter(Texto texto) {
		this.texto = texto;
	}

	public Long getSequencialArquivoEletronico() {
		return getTexto().getArquivoEletronico().getId();
	}

	public Long getSequencialObjetoIncidente() {
		return getTexto().getObjetoIncidente().getId();
	}

}
