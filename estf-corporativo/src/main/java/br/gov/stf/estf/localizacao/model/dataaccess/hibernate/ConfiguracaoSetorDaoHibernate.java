package br.gov.stf.estf.localizacao.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.ConfiguracaoSetor;
import br.gov.stf.estf.localizacao.model.dataaccess.ConfiguracaoSetorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ConfiguracaoSetorDaoHibernate 
	extends GenericHibernateDao<ConfiguracaoSetor, Long> 
	implements ConfiguracaoSetorDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfiguracaoSetorDaoHibernate() {
		super(ConfiguracaoSetor.class);
	}
	
	public ConfiguracaoSetor pesquisarConfiguracaoSetor(Long codigoSetor, Long codigoTipoConfiguracaoSetor) 
		throws DaoException {
        
        Session session = retrieveSession();        
        ConfiguracaoSetor configuracaoSetor = null;

        try {        
                Criteria criteria = session.createCriteria(ConfiguracaoSetor.class);                
                criteria.add(Restrictions.eq("setor.id", codigoSetor));
                criteria.add(Restrictions.eq("tipoConfiguracaoSetor.id", codigoTipoConfiguracaoSetor));
                configuracaoSetor = (ConfiguracaoSetor) criteria.uniqueResult();
        }
        catch(HibernateException e) {
            throw new DaoException("HibernateException",
                    SessionFactoryUtils.convertHibernateAccessException(e));
        }
        catch( RuntimeException e ) {
            throw new DaoException("RuntimeException", e);
        }    
        return configuracaoSetor;        
    }
	
	public ConfiguracaoSetor recuperarConfiguracaoSetor(Long codigoSetor, String siglaTipoConfiguracaoSetor) throws DaoException{
		Session session = retrieveSession();        
        ConfiguracaoSetor configuracaoSetor = null;

        try {        
                Criteria criteria = session.createCriteria(ConfiguracaoSetor.class, "cs");                
                if( codigoSetor != null ){
                	criteria.add(Restrictions.eq("cs.setor.id", codigoSetor));
                }
                if( siglaTipoConfiguracaoSetor != null && siglaTipoConfiguracaoSetor.trim().length() > 0 ){
                	criteria.createCriteria("cs.tipoConfiguracaoSetor", "tp");
                	criteria.add(Restrictions.eq("tp.sigla", siglaTipoConfiguracaoSetor));
                }
                configuracaoSetor = (ConfiguracaoSetor) criteria.uniqueResult();
        }
        catch(HibernateException e) {
            throw new DaoException("HibernateException",
                    SessionFactoryUtils.convertHibernateAccessException(e));
        }
        catch( RuntimeException e ) {
            throw new DaoException("RuntimeException", e);
        }    
        return configuracaoSetor;        

	}
}