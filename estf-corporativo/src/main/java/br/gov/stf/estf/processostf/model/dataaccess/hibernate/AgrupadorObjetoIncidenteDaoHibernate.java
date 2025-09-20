package br.gov.stf.estf.processostf.model.dataaccess.hibernate;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.entidade.processostf.AgrupadorObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.AgrupadorObjetoIncidenteDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class AgrupadorObjetoIncidenteDaoHibernate extends GenericHibernateDao<AgrupadorObjetoIncidente, Long>
		implements AgrupadorObjetoIncidenteDao {

	private static final long serialVersionUID = 1L;

	public AgrupadorObjetoIncidenteDaoHibernate() {
		super(AgrupadorObjetoIncidente.class);
	}

	@Override
	public List<AgrupadorObjetoIncidente> pesquisarAgrupadorObjetoIncidente(Agrupador categoria, ObjetoIncidente<?> objetoIncidente) throws DaoException {
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			
			hql.append(" FROM AgrupadorObjetoIncidente a ");
			hql.append(" WHERE 1=1");
			
			if (categoria != null)
				hql.append(" and (a.categoria.id=:idCategoria) ");
			
			if (objetoIncidente != null)
				hql.append(" and (a.objetoIncidente.id = :idObjetoIncidente)");
			
			Query q = session.createQuery(hql.toString());
			
			if (categoria != null)
				q.setLong("idCategoria", categoria.getId());
			
			if (objetoIncidente != null)
				q.setLong("idObjetoIncidente", objetoIncidente.getId());
			
			return q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}