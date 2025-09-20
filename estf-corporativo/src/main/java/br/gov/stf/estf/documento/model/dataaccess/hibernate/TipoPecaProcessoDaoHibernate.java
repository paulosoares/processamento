package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.TipoPecaProcessoDao;
import br.gov.stf.estf.entidade.documento.TipoPecaProcesso;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoPecaProcessoDaoHibernate extends
		GenericHibernateDao<TipoPecaProcesso, Long> implements
		TipoPecaProcessoDao {
	
	public TipoPecaProcessoDaoHibernate() {
		super(TipoPecaProcesso.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8655451147504599425L;

	public TipoPecaProcesso recuperar(String sigla) throws DaoException {
		TipoPecaProcesso tipoPecaProcesso = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TipoPecaProcesso.class);
			c.add(Restrictions.eq("sigla", sigla));
			tipoPecaProcesso = (TipoPecaProcesso) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return tipoPecaProcesso;
	}



	
    public List pesquisarTipoPecaProcessoEletronico() 
    throws DaoException {
	
    Session session = retrieveSession();        
    List lista = null;        
    try {
        lista = session.createQuery("FROM TipoPecaProcessoEletronico t ORDER BY t.id ").list();
    } catch(HibernateException e) {
        throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
    } catch( RuntimeException e ) {
        throw new DaoException("RuntimeException", e);
    }        
    return lista;
}


// WARN: Método utilizado no digitalizador de peças do e-STF
public List<TipoPecaProcesso> pesquisarTipoPecaProcessoEletronicoOrdenadoDescricao() 
    throws DaoException {
	
    Session session = retrieveSession();        
    List<TipoPecaProcesso> lista = null;        
    try {
        lista = session.createQuery("FROM TipoPecaProcessoEletronico t ORDER BY t.descricaoPeca ").list();
    } catch(HibernateException e) {
        throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
    } catch( RuntimeException e ) {
        throw new DaoException("RuntimeException", e);
    }        
    return lista;
}

// WARN: Método utilizado no digitalizador de peças do e-STF
public TipoPecaProcesso recuperarTipoPecaProcessoEletronico(Long id) 
    throws DaoException {
    
    Session session = retrieveSession();        
    TipoPecaProcesso tipoPeca = null;

    try {        
            Criteria criteria = session.createCriteria(TipoPecaProcesso.class);                
            criteria.add(Restrictions.idEq(id));            
            tipoPeca = (TipoPecaProcesso) criteria.uniqueResult();
    }
    catch(HibernateException e) {
        throw new DaoException("HibernateException",
                SessionFactoryUtils.convertHibernateAccessException(e));
    }
    catch( RuntimeException e ) {
        throw new DaoException("RuntimeException", e);
    }    
    return tipoPeca; 
}

}
