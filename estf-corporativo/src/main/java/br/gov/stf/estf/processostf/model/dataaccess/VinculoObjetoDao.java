/**
 * 
 */
package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.TipoVinculoObjeto;
import br.gov.stf.estf.entidade.processostf.VinculoObjeto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 27.07.2011
 */
public interface VinculoObjetoDao extends GenericDao<VinculoObjeto, Long> {

	List<VinculoObjeto> pesquisarPorVinculador(ObjetoIncidente<?> vinculador, TipoVinculoObjeto tipoVinculoObjeto) throws DaoException;

	List<VinculoObjeto> pesquisarPorVinculado(ObjetoIncidente<?> vinculado, TipoVinculoObjeto tipoVinculoObjeto) throws DaoException;

	List<ObjetoIncidente<?>> recuperarObjetosIncidenteVinculados(
			ObjetoIncidente<?> vinculador, TipoVinculoObjeto tipoVinculoObjeto) throws DaoException;
}
