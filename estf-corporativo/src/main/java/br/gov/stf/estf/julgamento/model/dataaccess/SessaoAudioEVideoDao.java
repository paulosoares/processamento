package br.gov.stf.estf.julgamento.model.dataaccess;

import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.processostf.SessaoAudioEVideo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface SessaoAudioEVideoDao extends GenericDao<SessaoAudioEVideo, Long> {

	SessaoAudioEVideo recuperar(Sessao sessao) throws DaoException;
	
}
