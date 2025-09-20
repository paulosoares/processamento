package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.AndamentoPeticao;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.processostf.model.dataaccess.AndamentoPeticaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class AndamentoPeticaoDaoHibernate extends GenericHibernateDao<AndamentoPeticao, Long> implements AndamentoPeticaoDao {

	private static final long serialVersionUID = 6465448990664903314L;

	public AndamentoPeticaoDaoHibernate() {
		super(AndamentoPeticao.class);
	}

	@Override
	public AndamentoPeticao pesquisar(Andamento andamento, Peticao peticao) throws DaoException {
		
		Session session = retrieveSession();
		try {
			Criteria criteria = session.createCriteria(AndamentoPeticao.class);

			criteria.add(Restrictions.eq("tipoAndamento", andamento));
			criteria.add(Restrictions.eq("objetoIncidente", peticao.getObjetoIncidente()));

			return (AndamentoPeticao) criteria.uniqueResult();
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	@Override
	public Long recuperarUltimaSequencia(ObjetoIncidente objetoIncidente) throws DaoException {
		
		Long seq = null;
		try {
			Session session = retrieveSession();
		
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT MAX(ap.numeroSequencia) ");
			hql.append("   FROM AndamentoPeticao ap ");
			hql.append("  WHERE ap.objetoIncidente.id = :oiid ");
		
			Query c = session.createQuery(hql.toString());
			c.setLong("oiid", objetoIncidente.getId());
		
			seq = (Long) c.uniqueResult();
		
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		return seq;
	}
	
}
