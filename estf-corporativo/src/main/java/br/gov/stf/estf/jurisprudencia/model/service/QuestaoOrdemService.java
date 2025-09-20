/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service;


import br.gov.stf.estf.entidade.jurisprudencia.QuestaoOrdem;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.QuestaoOrdemDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 20.08.2012
 */
public interface QuestaoOrdemService extends GenericService<QuestaoOrdem, Long, QuestaoOrdemDao> {

	QuestaoOrdem recuperarPorObjetoIncidente(ObjetoIncidente<?> oi) throws ServiceException;	

}
