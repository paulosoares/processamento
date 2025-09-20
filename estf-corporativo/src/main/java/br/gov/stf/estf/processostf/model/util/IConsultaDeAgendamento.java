package br.gov.stf.estf.processostf.model.util;

import java.util.Collection;

import br.gov.stf.estf.model.util.IConsultaBasicaDeProcesso;

public interface IConsultaDeAgendamento extends IConsultaBasicaDeProcesso {

	Long getCodigoDoMinistro();

	Collection<Long> getSequenciaisObjetosIncidentes();

}
