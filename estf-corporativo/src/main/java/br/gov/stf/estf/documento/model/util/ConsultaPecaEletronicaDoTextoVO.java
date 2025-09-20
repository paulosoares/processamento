package br.gov.stf.estf.documento.model.util;

public class ConsultaPecaEletronicaDoTextoVO implements IConsultaPecaProcessoEletronicoDoTexto {

	private Long sequencialArquivoEletronico;
	private Long sequencialObjetoIncidente;

	public Long getSequencialObjetoIncidente() {
		return sequencialObjetoIncidente;
	}

	public void setSequencialObjetoIncidente(Long sequencialObjetoIncidente) {
		this.sequencialObjetoIncidente = sequencialObjetoIncidente;
	}

	public Long getSequencialArquivoEletronico() {
		return sequencialArquivoEletronico;
	}

	public void setSequencialArquivoEletronico(Long sequencialArquivoEletronico) {
		this.sequencialArquivoEletronico = sequencialArquivoEletronico;
	}

}
