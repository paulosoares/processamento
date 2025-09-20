package br.gov.stf.estf.usuario.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.entidade.usuario.Pessoa;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface PessoaDao extends GenericDao<Pessoa, Long> {
	public List<Pessoa> recuperarApenasPartesIntegradas(List<Parte> partes) throws DaoException;
}
