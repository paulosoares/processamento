/**
 * 
 */
package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.TipoVinculoObjeto;
import br.gov.stf.estf.entidade.processostf.VinculoObjeto;
import br.gov.stf.estf.processostf.model.dataaccess.VinculoObjetoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 27.07.2011
 */
public interface VinculoObjetoService extends GenericService<VinculoObjeto, Long, VinculoObjetoDao> {

	List<VinculoObjeto> pesquisarPorVinculador(ObjetoIncidente<?> vinculador, TipoVinculoObjeto tipoVinculoObjeto) throws ServiceException;

	List<VinculoObjeto> pesquisarPorVinculado(ObjetoIncidente<?> vinculado, TipoVinculoObjeto tipoVinculoObjeto) throws ServiceException;
	
	List<ObjetoIncidente<?>> recuperarObjetosIncidenteVinculados(ObjetoIncidente<?> vinculador, TipoVinculoObjeto tipoVinculoObjeto) throws ServiceException;
}
