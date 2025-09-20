package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.gov.stf.estf.processostf.model.dataaccess.ListaProcessosDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.SearchData;

@Repository
public class ListaProcessosDaoHibernate extends GenericHibernateDao<ListaProcessos, Long> implements ListaProcessosDao {

	private static final long serialVersionUID = -7011235029521755493L;

	public ListaProcessosDaoHibernate() {
		super(ListaProcessos.class);
	}

	public ListaProcessos recuperarPorNome(String nome) throws DaoException {
		Criteria criteria = retrieveSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("nome", nome));
		
		return (ListaProcessos) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<ListaProcessos> pesquisarListaProcessos(String nome, Boolean ativo,Long idSetor) throws DaoException {
		List<ListaProcessos> lista = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ListaProcessos.class);
			if( SearchData.stringNotEmpty(nome) ){
				nome = nome.replace("|", "\\|");
				nome = nome.replace('%', ' ');
				c.add(Restrictions.ilike("nome", nome.trim(), MatchMode.ANYWHERE));
			}

			if( ativo != null ){
				c.add( Restrictions.eq("ativa", ativo) );
			}
			
			if( idSetor != null ){
				c.add( Restrictions.eq("setor.id", idSetor) );
			}
			c.addOrder(Order.asc("nome"));
			lista = c.list();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return lista;
	}

}
