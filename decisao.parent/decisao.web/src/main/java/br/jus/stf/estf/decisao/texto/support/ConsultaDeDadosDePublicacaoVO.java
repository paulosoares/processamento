package br.jus.stf.estf.decisao.texto.support;

import br.gov.stf.estf.publicacao.model.util.IConsultaDeDadosDePublicacao;

public class ConsultaDeDadosDePublicacaoVO implements IConsultaDeDadosDePublicacao {

	private Integer materiaCodigoCapitulo;
	private Integer processoCodigoCapitulo;
	private Long sequencialDoArquivoEletronico;
	private Long sequencialObjetoIncidente;

	public Long getSequencialObjetoIncidente() {
		return sequencialObjetoIncidente;
	}

	public void setSequencialObjetoIncidente(Long sequencialObjetoIncidente) {
		this.sequencialObjetoIncidente = sequencialObjetoIncidente;
	}

	public Integer getMateriaCodigoCapitulo() {
		return materiaCodigoCapitulo;
	}

	public void setMateriaCodigoCapitulo(Integer materiaCodigoCapitulo) {
		this.materiaCodigoCapitulo = materiaCodigoCapitulo;
	}

	public Integer getProcessoCodigoCapitulo() {
		return processoCodigoCapitulo;
	}

	public void setProcessoCodigoCapitulo(Integer processoCodigoCapitulo) {
		this.processoCodigoCapitulo = processoCodigoCapitulo;
	}

	public Long getSequencialDoArquivoEletronico() {
		return sequencialDoArquivoEletronico;
	}

	public void setSequencialDoArquivoEletronico(Long sequencialDoArquivoEletronico) {
		this.sequencialDoArquivoEletronico = sequencialDoArquivoEletronico;
	}

}
