package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.EventoSessao;
import br.gov.stf.estf.julgamento.model.dataaccess.EventoSessaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class EventoSessaoDaoHibernate extends GenericHibernateDao<EventoSessao, Long> implements EventoSessaoDao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4644132384592065538L;

	public EventoSessaoDaoHibernate() {
		super(EventoSessao.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<EventoSessao> pesquisar( Long idSessao ) throws DaoException {
		Session session = retrieveSession();
		
		try {
			Criteria c = session.createCriteria( EventoSessao.class, "es" );
			
			c = c.createAlias("es.sessao", "s", CriteriaSpecification.INNER_JOIN).setFetchMode("s", FetchMode.JOIN);
			
			c.add( Restrictions.eq("s.id", idSessao) );
			
			return c.list();
		} catch ( Exception e ) {
			throw new DaoException ( e );
		}
	}

}
