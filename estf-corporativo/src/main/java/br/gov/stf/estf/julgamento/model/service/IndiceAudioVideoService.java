package br.gov.stf.estf.julgamento.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.processostf.IndiceAudioVideo;
import br.gov.stf.estf.entidade.processostf.SessaoAudioEVideo;
import br.gov.stf.estf.julgamento.model.dataaccess.IndiceAudioVideoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;


/**
 * @author Almir.Oliveira
 * @since 14.12.2011 */
public interface IndiceAudioVideoService extends GenericService<IndiceAudioVideo, Long, IndiceAudioVideoDao>{

	List<IndiceAudioVideo> pesquisar(ListaJulgamento listaJulgamento) throws ServiceException;

	List<IndiceAudioVideo> pesquisar(SessaoAudioEVideo sessaoAudioEVideo) throws ServiceException;

}
