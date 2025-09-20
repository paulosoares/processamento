package br.gov.stf.estf.julgamento.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.processostf.SessaoAudioEVideo;
import br.gov.stf.estf.julgamento.model.dataaccess.SessaoAudioEVideoDao;
import br.gov.stf.estf.julgamento.model.service.SessaoAudioEVideoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;


@Service("sessaoAudioEVideoService")
public class SessaoAudioEVideoServiceImpl extends GenericServiceImpl<SessaoAudioEVideo, Long, SessaoAudioEVideoDao> implements SessaoAudioEVideoService {

	protected SessaoAudioEVideoServiceImpl(SessaoAudioEVideoDao dao) {
		super( dao );
	}

	@Override
	public SessaoAudioEVideo recuperar(Sessao sessao) throws ServiceException {
		try {
			return dao.recuperar(sessao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
}
