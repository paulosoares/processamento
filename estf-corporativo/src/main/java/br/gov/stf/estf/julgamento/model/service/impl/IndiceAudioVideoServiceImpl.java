package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.processostf.IndiceAudioVideo;
import br.gov.stf.estf.entidade.processostf.SessaoAudioEVideo;
import br.gov.stf.estf.julgamento.model.dataaccess.IndiceAudioVideoDao;
import br.gov.stf.estf.julgamento.model.service.IndiceAudioVideoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Almir.Oliveira
 * @since 15.12.2011 */
@Service("indiceAudioVideoService")
public class IndiceAudioVideoServiceImpl extends GenericServiceImpl<IndiceAudioVideo, Long, IndiceAudioVideoDao> implements IndiceAudioVideoService {

	protected IndiceAudioVideoServiceImpl(IndiceAudioVideoDao dao) {
		super(dao);
	}

	@Override
	public List<IndiceAudioVideo> pesquisar(ListaJulgamento listaJulgamento) throws ServiceException {
		try {
			return dao.pesquisar( listaJulgamento );
		} catch(DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<IndiceAudioVideo> pesquisar(SessaoAudioEVideo sessaoAudioEVideo) throws ServiceException {
		try {
			return dao.pesquisar(sessaoAudioEVideo);
		} catch(DaoException e) {
			throw new ServiceException(e);
		}
	}

	
}
