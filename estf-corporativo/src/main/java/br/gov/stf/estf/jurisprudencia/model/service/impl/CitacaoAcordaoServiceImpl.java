/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.CitacaoAcordao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.CitacaoAcordaoDao;
import br.gov.stf.estf.jurisprudencia.model.service.CitacaoAcordaoService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 17.10.2012
 */
@Service("citacaoAcordaoService")
public class CitacaoAcordaoServiceImpl extends GenericServiceImpl<CitacaoAcordao, Long, CitacaoAcordaoDao> implements CitacaoAcordaoService {

	protected CitacaoAcordaoServiceImpl(CitacaoAcordaoDao dao) {
		super(dao);
	}

}
