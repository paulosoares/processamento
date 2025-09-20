/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.AcordaoJurisprudencia;
import br.gov.stf.estf.entidade.jurisprudencia.PublicacaoAcordao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 20.08.2012
 */
public interface PublicacaoAcordaoDao extends GenericDao<PublicacaoAcordao, Long> {

	List<PublicacaoAcordao> pesquisarPorAcordaoJurisprudencia(
			AcordaoJurisprudencia acordaoJurisprudencia) throws DaoException;

}
