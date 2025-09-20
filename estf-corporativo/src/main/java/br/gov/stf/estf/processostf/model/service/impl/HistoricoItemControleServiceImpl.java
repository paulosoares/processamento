/**
 * 
 */
package br.gov.stf.estf.processostf.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.HistoricoItemControle;
import br.gov.stf.estf.processostf.model.dataaccess.HistoricoItemControleDao;
import br.gov.stf.estf.processostf.model.service.HistoricoItemControleService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author paulo.estevao
 * @since 29.06.2012
 */
@Service("historicoItemControleService")
public class HistoricoItemControleServiceImpl extends GenericServiceImpl<HistoricoItemControle, Long, HistoricoItemControleDao> implements
		HistoricoItemControleService {

	protected HistoricoItemControleServiceImpl(HistoricoItemControleDao dao) {
		super(dao);
	}

}
