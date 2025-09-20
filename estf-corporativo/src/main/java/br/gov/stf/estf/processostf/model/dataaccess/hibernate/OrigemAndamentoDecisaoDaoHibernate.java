package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.OrigemAndamentoDecisao;
import br.gov.stf.estf.processostf.model.dataaccess.OrigemAndamentoDecisaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class OrigemAndamentoDecisaoDaoHibernate extends GenericHibernateDao<OrigemAndamentoDecisao, Long> implements OrigemAndamentoDecisaoDao { 
    /**
	 * 
	 */
	private static final long serialVersionUID = -295743668645051025L;


    public OrigemAndamentoDecisaoDaoHibernate() {
		super(OrigemAndamentoDecisao.class);
	}


	@Override
	public OrigemAndamentoDecisao pesquisarOrigemDecisao(Setor setor) throws DaoException {
		
		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(OrigemAndamentoDecisao.class);
		criteria.add(Restrictions.eq("setor", setor));
		
		return (OrigemAndamentoDecisao) criteria.uniqueResult();
	}

	//@Override
	public List<OrigemAndamentoDecisao> pesquisarOrigensSemMinistro() throws DaoException {

		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(OrigemAndamentoDecisao.class);
		criteria.add(Restrictions.isNotNull("setor"));
		criteria.add(Restrictions.isNull("ministro"));
		criteria.addOrder(Order.asc("descricao"));
		
		return criteria.list();
	}
	
	@Override
	public List<OrigemAndamentoDecisao> pesquisarOrigensComMinistroAtivo() throws DaoException {
		
		Session session = retrieveSession();
		
		String sql = "select o from OrigemAndamentoDecisao o where o.ministro in (select m.id from Ministro m where m.dataAfastamento is null) order by o.descricao";
		Query query = session.createQuery(sql);
		List<OrigemAndamentoDecisao> origens = (List<OrigemAndamentoDecisao>)query.list();

		return origens;
	}
	
	@Override
	public List<OrigemAndamentoDecisao> pesquisarOrigensDecisao(List<Long> idsOrigem) throws DaoException {
		
		Session session = retrieveSession();

		Criteria criteria = session.createCriteria(OrigemAndamentoDecisao.class);
		
		criteria.add(Restrictions.in("id", idsOrigem));
		criteria.addOrder(Order.asc("descricao"));
		
		return criteria.list();
	}
}
