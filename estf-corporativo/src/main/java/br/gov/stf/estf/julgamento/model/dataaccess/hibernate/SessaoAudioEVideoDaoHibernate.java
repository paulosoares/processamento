package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.processostf.SessaoAudioEVideo;
import br.gov.stf.estf.julgamento.model.dataaccess.SessaoAudioEVideoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class SessaoAudioEVideoDaoHibernate extends GenericHibernateDao<SessaoAudioEVideo, Long> implements SessaoAudioEVideoDao {

	private static final long serialVersionUID = 7858577141797523619L;
	
	public SessaoAudioEVideoDaoHibernate() {
		super(SessaoAudioEVideo.class);
	}

	@Override
	public SessaoAudioEVideo recuperar(Sessao sessao) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(SessaoAudioEVideo.class);
			c.add(Restrictions.eq("sessao", sessao));
			return (SessaoAudioEVideo) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
