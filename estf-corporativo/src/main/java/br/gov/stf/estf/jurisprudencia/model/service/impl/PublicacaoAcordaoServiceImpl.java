/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.AcordaoJurisprudencia;
import br.gov.stf.estf.entidade.jurisprudencia.PublicacaoAcordao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.PublicacaoAcordaoDao;
import br.gov.stf.estf.jurisprudencia.model.service.PublicacaoAcordaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;


@Service("publicacaoAcordaoService")
public class PublicacaoAcordaoServiceImpl extends GenericServiceImpl<PublicacaoAcordao, Long, PublicacaoAcordaoDao> implements
		PublicacaoAcordaoService {
	
	protected PublicacaoAcordaoServiceImpl(PublicacaoAcordaoDao dao) {
		super(dao);
	}

	@Override
	public List<PublicacaoAcordao> pesquisarPorAcordaoJurisprudencia(AcordaoJurisprudencia acordaoJurisprudencia) throws ServiceException {
		List<PublicacaoAcordao> result = null;
		try {
			result = dao.pesquisarPorAcordaoJurisprudencia(acordaoJurisprudencia);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return result;
	}


}
