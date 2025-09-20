package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.EventoSessao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface EventoSessaoDao extends GenericDao<EventoSessao, Long> {

	public List<EventoSessao> pesquisar( Long idSessao ) throws DaoException;
	
}
