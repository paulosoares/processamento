/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.Despacho;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.DespachoDao;
import br.gov.stf.estf.jurisprudencia.model.service.DespachoService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 20.08.2012
 */
@Service("despachoService")
public class DespachoServiceImpl extends GenericServiceImpl<Despacho, Long, DespachoDao> implements
DespachoService {
	
		
	protected DespachoServiceImpl(DespachoDao dao) {
		super(dao);
	}
}