package br.gov.stf.estf.documento.model.util;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.model.util.IConsultaBasicaDeProcesso;

public class ConsultaDePecaProcessoEletronicoPendenteVisualizacaoAdapter implements IConsultaBasicaDeProcesso {
	private ObjetoIncidente<?> objetoIncidente;

	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public ConsultaDePecaProcessoEletronicoPendenteVisualizacaoAdapter(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	public Long getSequencialObjetoIncidente() {
		return getObjetoIncidente().getId();
	}

}
