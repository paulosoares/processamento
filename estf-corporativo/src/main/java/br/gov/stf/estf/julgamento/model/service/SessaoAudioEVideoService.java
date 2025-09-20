package br.gov.stf.estf.julgamento.model.service;

import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.processostf.SessaoAudioEVideo;
import br.gov.stf.estf.julgamento.model.dataaccess.SessaoAudioEVideoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface SessaoAudioEVideoService extends GenericService<SessaoAudioEVideo, Long, SessaoAudioEVideoDao>{
	
	public SessaoAudioEVideo recuperar(Sessao sessao) throws ServiceException;
}
