package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.Orgao;
import br.gov.stf.estf.processostf.model.dataaccess.OrgaoDao;
import br.gov.stf.estf.processostf.model.service.OrgaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("orgaoService")
public class OrgaoServiceImpl extends GenericServiceImpl<Orgao, Long, OrgaoDao> implements OrgaoService {

	public OrgaoServiceImpl(OrgaoDao dao) {
		super(dao);
	}

	@Override
	public List<Orgao> pesquisarOrgaosAtivos() throws ServiceException {
		try {
			return dao.pesquisarOrgaosAtivos();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Orgao> pesquisarPelaDescricaoOrgaosAtivos(String descricao) throws ServiceException{
			try {
			return dao.pesquisarPelaDescricaoOrgaosAtivos(descricao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	
	
	
}
