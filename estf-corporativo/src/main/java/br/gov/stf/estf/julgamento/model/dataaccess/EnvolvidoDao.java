package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.Envolvido;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface EnvolvidoDao extends GenericDao<Envolvido, Long>{

	List<Envolvido> pesquisar(String sugestaoNome) throws DaoException;

}
