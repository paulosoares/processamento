package br.gov.stf.estf.processostf.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.processostf.IntegracaoOrgaoParte;
import br.gov.stf.estf.entidade.processostf.IntegracaoOrgaoParte.IntegracaoOrgaoParteId;
import br.gov.stf.estf.processostf.model.dataaccess.IntegracaoOrgaoParteDao;
import br.gov.stf.estf.processostf.model.service.IntegracaoOrgaoParteService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("integracaoOrgaoParteService")
public class IntegracaoOrgaoParteServiceImpl extends GenericServiceImpl<IntegracaoOrgaoParte, IntegracaoOrgaoParteId, IntegracaoOrgaoParteDao> 
	implements IntegracaoOrgaoParteService {
    public IntegracaoOrgaoParteServiceImpl(IntegracaoOrgaoParteDao dao) { super(dao); }

	public Origem recuperarOrigem(Long parte) throws ServiceException {
		Origem origem = null;
		
		try {
			IntegracaoOrgaoParte integracaoOrgaoParte = dao.recuperar(parte, null);
			if ( integracaoOrgaoParte!=null ) {
				origem = integracaoOrgaoParte.getId().getOrigem();
			}
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		
		return origem;
	}

}
