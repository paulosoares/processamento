/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.jurisprudencia.ItemLegislacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Paulo.Estevao
 * @since 12.08.2012
 */
public interface ItemLegislacaoDao extends GenericDao<ItemLegislacao, Long> {

	List<ItemLegislacao> recuperarFilhos(ItemLegislacao entity) throws DaoException;

}
