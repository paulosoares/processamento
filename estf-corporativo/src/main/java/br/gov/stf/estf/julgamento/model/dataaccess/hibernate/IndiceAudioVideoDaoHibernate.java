package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.processostf.IndiceAudioVideo;
import br.gov.stf.estf.entidade.processostf.SessaoAudioEVideo;
import br.gov.stf.estf.julgamento.model.dataaccess.IndiceAudioVideoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Almir.Oliveira
 * @since 15.12.2011 */
@Repository
public class IndiceAudioVideoDaoHibernate extends GenericHibernateDao<IndiceAudioVideo, Long> implements IndiceAudioVideoDao {

	private static final long serialVersionUID = 7316838690339766942L;

	public IndiceAudioVideoDaoHibernate() {
		super(IndiceAudioVideo.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<IndiceAudioVideo> pesquisar(ListaJulgamento listaJulgamento) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(IndiceAudioVideo.class);
			c.add( Restrictions.eq( "listaJulgamento", listaJulgamento ) );
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}	
	}

	@Override
	public List<IndiceAudioVideo> pesquisar(SessaoAudioEVideo sessaoAudioEVideo) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(IndiceAudioVideo.class);
			c.add(Restrictions.eq("sessaoAudioEVideo", sessaoAudioEVideo));
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
