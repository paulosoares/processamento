package br.gov.stf.estf.publicacao.model.util;

import br.gov.stf.estf.model.util.IConsultaBasicaDeProcesso;

public interface IConsultaDePautaDeJulgamento extends IConsultaBasicaDeProcesso {

	public Long getTipoJulgamento();

	public Integer getCodigoDaMateria();

}
