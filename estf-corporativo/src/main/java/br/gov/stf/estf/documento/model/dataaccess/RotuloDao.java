/**
 * 
 */
package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.Rotulo;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 28.11.2013
 */
public interface RotuloDao extends GenericDao<Rotulo, Long> {

	List<Rotulo> pesquisarRotulos(ObjetoIncidente<?> objetoIncidente, Setor setor) throws DaoException;

}
