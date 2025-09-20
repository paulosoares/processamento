/**
 * 
 */
package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.processostf.ProtocoloTaquigrafia;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Almir.Oliveira
 * @since 14.12.2011 */
public interface ProtocoloTaquigrafiaDao extends GenericDao<ProtocoloTaquigrafia, Long> {

	List<ProtocoloTaquigrafia> pesquisar(ListaJulgamento listaJulgamento) throws DaoException;
}
