/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.ItemControlePublicacaoDj;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.ItemControlePublicacaoDjDao;
import br.gov.stf.estf.jurisprudencia.model.service.ItemControlePublicacaoDjService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 16.08.2012
 */
@Service("itemControlePublicacaoDjService")
public class ItemControlePublicacaoDjServiceImpl extends GenericServiceImpl<ItemControlePublicacaoDj, Long, ItemControlePublicacaoDjDao> implements
		ItemControlePublicacaoDjService {

	protected ItemControlePublicacaoDjServiceImpl(ItemControlePublicacaoDjDao dao) {
		super(dao);
	}

}
