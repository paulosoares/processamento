/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service;

import br.gov.stf.estf.entidade.jurisprudencia.BaseJurisprudenciaRepercussaoGeral;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.BaseJurisprudenciaRepercussaoGeralDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author henrique.bona
 *
 */
public interface BaseJurisprudenciaRepercussaoGeralService
		extends
		GenericService<BaseJurisprudenciaRepercussaoGeral, Long, BaseJurisprudenciaRepercussaoGeralDao> {
	
	/**
	 * Retorna a Repercussão Geral com base no ObjetoIncidente
	 * 
	 * @param oi
	 * @return
	 * @throws ServiceException 
	 */
	BaseJurisprudenciaRepercussaoGeral recuperarPorObjetoIncidente(ObjetoIncidente<?> oi) throws ServiceException;

}
