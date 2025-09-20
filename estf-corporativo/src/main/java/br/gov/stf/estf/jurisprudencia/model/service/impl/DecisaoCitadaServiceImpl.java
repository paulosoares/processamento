/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.DecisaoCitada;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.DecisaoCitadaDao;
import br.gov.stf.estf.jurisprudencia.model.service.DecisaoCitadaService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 17.10.2012
 */
@Service("decisaoCitadaService")
public class DecisaoCitadaServiceImpl extends GenericServiceImpl<DecisaoCitada, Long, DecisaoCitadaDao> implements DecisaoCitadaService {

	protected DecisaoCitadaServiceImpl(DecisaoCitadaDao dao) {
		super(dao);
	}

}
