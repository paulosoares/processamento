package br.gov.stf.estf.configuracao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.configuracao.model.dataaccess.ConfiguracaoSistemaDao;
import br.gov.stf.estf.entidade.configuracao.ConfiguracaoSistema;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ConfiguracaoSistemaDaoHibernate extends GenericHibernateDao<ConfiguracaoSistema, Long> implements ConfiguracaoSistemaDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfiguracaoSistemaDaoHibernate() {
		super(ConfiguracaoSistema.class);
	}
	
	@Override
	public String recuperarValor(String chave) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ConfiguracaoSistema.class)
		    	.add( Restrictions.eq("chave", chave));
			List<ConfiguracaoSistema> lstConfig = c.list();
			if (lstConfig == null || lstConfig.isEmpty()) {
				return null;
			}
			ConfiguracaoSistema configSistema = lstConfig.get(0);
			if (configSistema != null) {
				return configSistema.getValor();
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	
	@Override
	public ConfiguracaoSistema recuperarValor(String siglaSistema, String chave) throws DaoException {
		if(siglaSistema != null && !siglaSistema.isEmpty() && chave != null && !chave.isEmpty()){
			try {
				
				Session session = retrieveSession();
	
				StringBuilder hql = new StringBuilder(" select cs from ConfiguracaoSistema cs ");
				hql.append(" WHERE cs.siglaSistema = :siglaSistema ");
				hql.append(" AND cs.chave = :chave ");	
	
				Query q = session.createQuery(hql.toString());
				
				q.setString("siglaSistema", siglaSistema);
				q.setString("chave", chave);
				
				return (ConfiguracaoSistema) q.uniqueResult();
	
			} catch (Exception e) {
				throw new DaoException (e);
			}	
		}
		return null;		
		
	}



}
