package br.gov.stf.estf.documento.model.util;

import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.model.util.IConsultaBasicaDeProcesso;

public interface IConsultaDadosDoTexto extends IConsultaBasicaDeProcesso {

	public TipoTexto getTipoDeTexto();

	public Long getCodigoDoMinistro();

	public Long getSequencialDoArquivoEletronico();

	boolean isIncluirPresidencia();

	boolean isIncluirVicePresidencia();

	TipoTexto[] getTiposDeTextoParaExcluir();

}
