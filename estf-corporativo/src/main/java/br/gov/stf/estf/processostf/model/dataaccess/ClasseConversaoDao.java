package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ClasseConversao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ClasseConversaoDao extends GenericDao<ClasseConversao, String> {
	public List pesquisarClasseAntiga() throws DaoException;
}
