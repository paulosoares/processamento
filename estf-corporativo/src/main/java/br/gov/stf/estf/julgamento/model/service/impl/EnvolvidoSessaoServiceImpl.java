package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.EnvolvidoSessao;
import br.gov.stf.estf.julgamento.model.dataaccess.EnvolvidoSessaoDao;
import br.gov.stf.estf.julgamento.model.service.EnvolvidoSessaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("envolvidoSessaoService")
public class EnvolvidoSessaoServiceImpl extends GenericServiceImpl<EnvolvidoSessao, Long, EnvolvidoSessaoDao> implements EnvolvidoSessaoService {
    public EnvolvidoSessaoServiceImpl(EnvolvidoSessaoDao dao) { super(dao); } 

	public List<EnvolvidoSessao> pesquisar( String nomeEnvolvido, Boolean ministro, Boolean ministroSubstituto, Long idSessao, Long... codCompetencia ) throws ServiceException {		
		try {
			return dao.pesquisar( nomeEnvolvido, ministro, ministroSubstituto, idSessao, codCompetencia );
		} catch ( DaoException e ) {
			throw new ServiceException( e );
		}
	}
	
	public EnvolvidoSessao recuperar( Long idSessao, Long codTipoCompetenciaEnvolvido ) throws ServiceException {
		try {
			return dao.recuperar( idSessao, codTipoCompetenciaEnvolvido );
		} catch ( DaoException e ) {
			throw new ServiceException( e );
		}		
	}
	
}
