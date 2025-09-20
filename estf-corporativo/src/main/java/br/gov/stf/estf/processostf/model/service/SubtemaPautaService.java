/**
 * 
 */
package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.SubtemaPauta;
import br.gov.stf.estf.entidade.processostf.TemaPauta;
import br.gov.stf.estf.processostf.model.dataaccess.SubtemaPautaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 08.08.2011
 */
public interface SubtemaPautaService extends GenericService<SubtemaPauta, Long, SubtemaPautaDao> {
	
	List<SubtemaPauta> pesquisar(TemaPauta temaPauta) throws ServiceException;

}
