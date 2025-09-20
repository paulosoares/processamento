package br.gov.stf.estf.publicacao.model.util;

import br.gov.stf.estf.model.util.IConsultaBasicaDeProcesso;

public interface IConsultaDeDadosDePublicacao extends IConsultaBasicaDeProcesso {

	public Long getSequencialDoArquivoEletronico();

	public Integer getMateriaCodigoCapitulo();

	public Integer getProcessoCodigoCapitulo();
}
