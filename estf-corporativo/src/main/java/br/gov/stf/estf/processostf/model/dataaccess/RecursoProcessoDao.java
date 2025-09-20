package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface RecursoProcessoDao extends GenericDao<RecursoProcesso, Long> {
	public List<RecursoProcesso> pesquisar(String siglaProcessual, Long numeroProcessual) throws DaoException;
	public Long pesquisar(Processo processo, Date data) throws DaoException;
}
