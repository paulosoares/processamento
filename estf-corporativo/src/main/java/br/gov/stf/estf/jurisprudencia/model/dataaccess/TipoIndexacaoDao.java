/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import br.gov.stf.estf.entidade.jurisprudencia.TipoIndexacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 10.07.2012
 */
public interface TipoIndexacaoDao extends GenericDao<TipoIndexacao, Long> {

	TipoIndexacao recuperarPorSigla(String sigla) throws DaoException;

}
