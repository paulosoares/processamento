/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import org.springframework.stereotype.Service;
import br.gov.stf.estf.entidade.jurisprudencia.QuestaoOrdem;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.QuestaoOrdemDao;
import br.gov.stf.estf.jurisprudencia.model.service.QuestaoOrdemService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;


@Service("questaoOrdemService")
public class QuestaoOrdemServiceImpl extends GenericServiceImpl<QuestaoOrdem, Long, QuestaoOrdemDao> implements
	QuestaoOrdemService {
	
	protected QuestaoOrdemServiceImpl(QuestaoOrdemDao dao) {
		super(dao);
	}

	
	@Override
	public QuestaoOrdem recuperarPorObjetoIncidente(
			ObjetoIncidente<?> oi) throws ServiceException {

		try {
			return this.dao.recuperarPorObjetoIncidente(oi);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("Não foi possível recuperar a Questao de Ordem com base no Objeto Incidente Informado.", e);
		}
	}

	
}