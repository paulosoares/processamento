/**
 * 
 */
package br.gov.stf.estf.processostf.model.service;

import java.util.List;
import java.util.SortedMap;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ItemControle;
import br.gov.stf.estf.entidade.processostf.TipoControle;
import br.gov.stf.estf.processostf.model.dataaccess.ItemControleDao;
import br.gov.stf.estf.processostf.model.util.ItemControleResult;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ItemControleService extends GenericService<ItemControle, Long, ItemControleDao> {

	public SortedMap<TipoControle, List<ItemControleResult>> buscaListaDeItemUsuario(String sigUsuario, Setor setor, 
			boolean permissaoGabineteSEJ, boolean permissaoManterGrupos) throws ServiceException;
	

	public void chamaPackageItemControle() throws ServiceException;


	public boolean isItemControleRepublicacao(Long itemControleId) throws ServiceException;
	
}
