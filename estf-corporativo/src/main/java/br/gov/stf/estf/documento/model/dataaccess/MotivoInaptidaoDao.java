package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.MotivoInaptidao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface MotivoInaptidaoDao extends GenericDao<MotivoInaptidao, Long> {

	public List<MotivoInaptidao> pesquisarTodos() throws DaoException;

}
