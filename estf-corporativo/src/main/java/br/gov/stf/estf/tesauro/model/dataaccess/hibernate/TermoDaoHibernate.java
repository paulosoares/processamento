/**
 * 
 */
package br.gov.stf.estf.tesauro.model.dataaccess.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.tesauro.Termo;
import br.gov.stf.estf.tesauro.model.dataaccess.TermoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 19.07.2012
 */
@Repository
public class TermoDaoHibernate extends GenericHibernateDao<Termo, Long> implements TermoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 699166096940153141L;

	public TermoDaoHibernate() {
		super(Termo.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Termo> pesquisarPorDescricao(String suggestion, boolean termoExato) throws DaoException {
		try {
			Session session = retrieveSession();
			
			if (termoExato) {
				Criteria c = session.createCriteria(Termo.class);
			
				c.add(Restrictions.like("descricao", suggestion.toUpperCase(), MatchMode.ANYWHERE));
				c.addOrder(Order.asc("descricao"));
				
				return c.list();
			} else {
				List<Termo> listaTermos = new ArrayList<Termo>();
				
				StringBuffer sql1 = new StringBuffer();
				sql1.append("SELECT * FROM TESAURO.TERMO termo ");
				sql1.append("WHERE TRANSLATE(termo.DSC_TERMO, 'ÁÇÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕËÜáçéíóúàèìòùâêîôûãõëü', 'ACEIOUAEIOUAEIOUAOEUaceiouaeiouaeiouaoeu') ");
				sql1.append("LIKE (TRANSLATE(:suggestion, 'ÁÇÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕËÜáçéíóúàèìòùâêîôûãõëü', 'ACEIOUAEIOUAEIOUAOEUaceiouaeiouaeiouaoeu') || '%') ");
				sql1.append("ORDER BY termo.DSC_TERMO ");
				
				SQLQuery query1 = session.createSQLQuery(sql1.toString());
				query1.addEntity(Termo.class);
				query1.setString("suggestion", suggestion.toUpperCase());
				
				StringBuffer sql2 = new StringBuffer();
				sql2.append("SELECT * FROM TESAURO.TERMO termo ");
				sql2.append("WHERE TRANSLATE(termo.DSC_TERMO, 'ÁÇÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕËÜáçéíóúàèìòùâêîôûãõëü', 'ACEIOUAEIOUAEIOUAOEUaceiouaeiouaeiouaoeu') ");
				sql2.append("LIKE ('%' || TRANSLATE(:suggestion, 'ÁÇÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕËÜáçéíóúàèìòùâêîôûãõëü', 'ACEIOUAEIOUAEIOUAOEUaceiouaeiouaeiouaoeu') || '%') ");
				sql2.append("AND TRANSLATE(termo.DSC_TERMO, 'ÁÇÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕËÜáçéíóúàèìòùâêîôûãõëü', 'ACEIOUAEIOUAEIOUAOEUaceiouaeiouaeiouaoeu') ");
				sql2.append("NOT LIKE (TRANSLATE(:suggestion, 'ÁÇÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕËÜáçéíóúàèìòùâêîôûãõëü', 'ACEIOUAEIOUAEIOUAOEUaceiouaeiouaeiouaoeu') || '%') ");
				sql2.append("ORDER BY termo.DSC_TERMO ");
				
				SQLQuery query2 = session.createSQLQuery(sql2.toString());
				query2.addEntity(Termo.class);
				query2.setString("suggestion", suggestion.toUpperCase());
				
				listaTermos.addAll(query1.list());
				listaTermos.addAll(query2.list());
				
				return listaTermos;
			}
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Termo> pesquisarPorDescricaoExata(String suggestion, boolean termoExato) throws DaoException {
		try {
			Session session = retrieveSession();
			
			if (termoExato) {
				Criteria c = session.createCriteria(Termo.class);
				c.add(Restrictions.eq("descricao", suggestion.toUpperCase()));
				c.addOrder(Order.asc("descricao"));
				
				return c.list();
			} else {
				List<Termo> listaTermos = new ArrayList<Termo>();
				
				StringBuffer sql1 = new StringBuffer();
				sql1.append("SELECT * FROM TESAURO.TERMO termo ");
				sql1.append("WHERE TRANSLATE(termo.DSC_TERMO, 'ÁÇÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕËÜáçéíóúàèìòùâêîôûãõëü', 'ACEIOUAEIOUAEIOUAOEUaceiouaeiouaeiouaoeu') ");
				sql1.append("= (TRANSLATE(:suggestion, 'ÁÇÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕËÜáçéíóúàèìòùâêîôûãõëü', 'ACEIOUAEIOUAEIOUAOEUaceiouaeiouaeiouaoeu')) ");
				sql1.append("ORDER BY termo.DSC_TERMO ");
				
				SQLQuery query1 = session.createSQLQuery(sql1.toString());
				query1.addEntity(Termo.class);
				query1.setString("suggestion", suggestion.toUpperCase());
				
				StringBuffer sql2 = new StringBuffer();
				sql2.append("SELECT * FROM TESAURO.TERMO termo ");
				sql2.append("WHERE TRANSLATE(termo.DSC_TERMO, 'ÁÇÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕËÜáçéíóúàèìòùâêîôûãõëü', 'ACEIOUAEIOUAEIOUAOEUaceiouaeiouaeiouaoeu') ");
				sql2.append("LIKE ('%' || TRANSLATE(:suggestion, 'ÁÇÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕËÜáçéíóúàèìòùâêîôûãõëü', 'ACEIOUAEIOUAEIOUAOEUaceiouaeiouaeiouaoeu') || '%') ");
				sql2.append("AND TRANSLATE(termo.DSC_TERMO, 'ÁÇÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕËÜáçéíóúàèìòùâêîôûãõëü', 'ACEIOUAEIOUAEIOUAOEUaceiouaeiouaeiouaoeu') ");
				sql2.append("NOT LIKE (TRANSLATE(:suggestion, 'ÁÇÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕËÜáçéíóúàèìòùâêîôûãõëü', 'ACEIOUAEIOUAEIOUAOEUaceiouaeiouaeiouaoeu') || '%') ");
				sql2.append("ORDER BY termo.DSC_TERMO ");
				
				SQLQuery query2 = session.createSQLQuery(sql2.toString());
				query2.addEntity(Termo.class);
				query2.setString("suggestion", suggestion.toUpperCase());
				
				listaTermos.addAll(query1.list());
				listaTermos.addAll(query2.list());
				
				return listaTermos;
			}
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
