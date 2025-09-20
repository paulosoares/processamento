package br.gov.stf.estf.jurisdicionado.model.dataaccess;


import br.gov.stf.estf.entidade.jurisdicionado.TipoIdentificacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoIdentificacaoDao extends GenericDao<TipoIdentificacao, Long> {

	TipoIdentificacao pesquisaPelaSigla(String sigla) throws DaoException;

}
