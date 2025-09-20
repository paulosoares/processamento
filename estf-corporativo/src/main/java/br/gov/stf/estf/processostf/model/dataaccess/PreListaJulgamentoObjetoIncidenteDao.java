package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface PreListaJulgamentoObjetoIncidenteDao extends GenericDao<PreListaJulgamentoObjetoIncidente, Long> {

	PreListaJulgamentoObjetoIncidente pesquisarPorObjetoIncidente(ObjetoIncidente<?> objetoIncidente);

	List<PreListaJulgamentoObjetoIncidente> pesquisarProcessoEmLista(ObjetoIncidente<?> objetoIncidente,
			PreListaJulgamento preListaJulgamento);

}
