package br.gov.stf.estf.configuracao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.configuracao.model.dataaccess.AlertaSistemaDao;
import br.gov.stf.estf.entidade.configuracao.AlertaSistema;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class AlertaSistemaDaoHibernate extends GenericHibernateDao<AlertaSistema, Long> implements AlertaSistemaDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AlertaSistemaDaoHibernate() {
		super(AlertaSistema.class);
	}
	
	@Override
	public List<AlertaSistema> recuperarValor(String siglaSistema, String chave) throws DaoException {
		if(siglaSistema != null && !siglaSistema.isEmpty() && chave != null && !chave.isEmpty()){
			try {
				
				Session session = retrieveSession();
	
				StringBuilder hql = new StringBuilder(" select als from AlertaSistema als ");
				hql.append(" WHERE als.siglaSistema = :siglaSistema ");
				hql.append(" AND als.chave = :chave ");	
	
				Query q = session.createQuery(hql.toString());
				
				q.setString("siglaSistema", siglaSistema);
				q.setString("chave", chave);
				
				return q.list();
	
			} catch (Exception e) {
				throw new DaoException (e);
			}	
		}
		return null;		
		
	}



}
