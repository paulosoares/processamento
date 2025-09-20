/**
 * 
 */
package br.gov.stf.estf.julgamento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.processostf.IndiceAudioVideo;
import br.gov.stf.estf.entidade.processostf.SessaoAudioEVideo;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

/**
 * @author Almir.Oliveira
 * @since 14.12.2011 */
public interface IndiceAudioVideoDao extends GenericDao<IndiceAudioVideo, Long> {

	List<IndiceAudioVideo> pesquisar(ListaJulgamento listaJulgamento) throws DaoException;

	List<IndiceAudioVideo> pesquisar(SessaoAudioEVideo sessaoAudioEVideo) throws DaoException;
}
