package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.TipoRecursoProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.TipoRecursoProcessoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoRecursoProcessoDaoHibernate extends GenericHibernateDao<TipoRecursoProcesso, Long> implements TipoRecursoProcessoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7608075532364089136L;

	public TipoRecursoProcessoDaoHibernate() {
		super(TipoRecursoProcesso.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoRecursoProcesso> pesquisar(Boolean ativo) throws DaoException {
		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(TipoRecursoProcesso.class);
		
		if(ativo != null)
			criteria.add(Restrictions.eq("ativo", ativo));
		
		return criteria.list();
	}
	
}
