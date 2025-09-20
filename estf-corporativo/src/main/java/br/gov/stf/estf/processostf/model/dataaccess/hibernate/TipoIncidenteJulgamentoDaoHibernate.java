package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.processostf.model.dataaccess.TipoIncidenteJulgamentoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.OrderCriterion;

@Repository
public class TipoIncidenteJulgamentoDaoHibernate extends GenericHibernateDao<TipoIncidenteJulgamento, Long>
	implements TipoIncidenteJulgamentoDao {

	public TipoIncidenteJulgamentoDaoHibernate() {
		super(TipoIncidenteJulgamento.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2601934893219065384L;

	public TipoIncidenteJulgamento recuperar(String sigla) throws DaoException {
		TipoIncidenteJulgamento tij = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TipoIncidenteJulgamento.class);
			c.add( Restrictions.eq("sigla", sigla) );
			tij = (TipoIncidenteJulgamento) c.uniqueResult();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return tij;
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoIncidenteJulgamento> pesquisar(Boolean ativo) throws DaoException {
		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(TipoIncidenteJulgamento.class);
		
		if(ativo != null)
			criteria.add(Restrictions.eq("ativo", ativo));
		
		return criteria.list();
	}	
	
	public List<TipoIncidenteJulgamento> pesquisarTipoJulgamento(String sigla, String descricao, Boolean ativo)	throws DaoException	{
		Session session = retrieveSession();
		
		List<TipoIncidenteJulgamento> tiposJulgamento = null;
		
		Criteria criteria = session.createCriteria(TipoIncidenteJulgamento.class);
		
		if(sigla != null && !sigla.trim().equals(""))
			criteria.add(Restrictions.eq("sigla", sigla));
		
		if(descricao != null && !descricao.trim().equals(""))
			criteria.add(Restrictions.eq("descricao", descricao));
		
		if(ativo != null)
			criteria.add(Restrictions.eq("ativo", ativo));
		
        criteria.addOrder(Order.asc("descricao"));
        
		tiposJulgamento = criteria.list();
		
		return tiposJulgamento;
	}

	@SuppressWarnings("unchecked")
	public List<TipoIncidenteJulgamento> pesquisarTodosTiposJulgamento(OrderCriterion... ordenacoes) throws DaoException	{
		Criteria criteria = retrieveSession().createCriteria( TipoIncidenteJulgamento.class, "tr" );
		criteria.add( Restrictions.eq( "ativo", true) );
		criteria.add( Restrictions.eq( "permiteCriacaoIncidenteGabinete", true ) );
		adicionaOrdenamentosAoCriteria( criteria, ordenacoes );
		criteria.setFetchMode("classes", FetchMode.JOIN);
		return criteria.list();
	}

	@Override
	public List<TipoIncidenteJulgamento> pesquisarTiposJulgamento(Classe classeProcessual) throws DaoException {
		Criteria criteria = retrieveSession().createCriteria( TipoIncidenteJulgamento.class, "tr" );
		criteria.add( Restrictions.eq( "ativo", true) );
		criteria.add( Restrictions.eq( "permiteCriacaoIncidenteGabinete", true ) );
		return criteria.list();
	}	

}
