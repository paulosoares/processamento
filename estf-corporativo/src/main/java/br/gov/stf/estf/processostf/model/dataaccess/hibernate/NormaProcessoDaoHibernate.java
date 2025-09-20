package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.NormaProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.NormaProcessoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class NormaProcessoDaoHibernate 
	extends GenericHibernateDao<NormaProcesso, Long> 
	implements NormaProcessoDao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -721862077458179337L;

	public NormaProcessoDaoHibernate() {
		super(NormaProcesso.class);
	}
	
	public List<NormaProcesso> pesquisarNormasProcesso(Long codigo, String descricao, Short ano, String normaJurisprudencia)
	throws DaoException {
		Session session = retrieveSession();
		
		List<NormaProcesso> resultado = null;
		
		try {
			Criteria criteria = session.createCriteria(NormaProcesso.class);
			
			if( codigo != null ){
				criteria.add( Restrictions.eq("id", codigo) );
			}
			
			if( descricao != null && descricao.trim().length() > 0 ) {
				criteria.add( Restrictions.eq("descricao", descricao) );
			}
			
			if( ano != null ){
				criteria.add( Restrictions.eq("ano", ano) );
			}
			
			if( normaJurisprudencia != null && normaJurisprudencia.trim().length() > 0 ) {
				criteria.add( Restrictions.eq("normaJurisprudencia", normaJurisprudencia) );
			}
			
			criteria.addOrder(Order.asc("descricao"));
			
			resultado = criteria.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		
		return resultado;
	}

}
