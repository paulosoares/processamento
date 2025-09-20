package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.ControlePrazoIntimacao;
import br.gov.stf.estf.processostf.model.dataaccess.ControlePrazoIntimacaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ControlePrazoIntimacaoDaoHibernate extends GenericHibernateDao<ControlePrazoIntimacao, Long> implements ControlePrazoIntimacaoDao { 
    public ControlePrazoIntimacaoDaoHibernate() {
		super(ControlePrazoIntimacao.class);
	}

	private static final long serialVersionUID = 1L;
	
	public void persistirControlePrazoIntimacao(
			ControlePrazoIntimacao controlePrazoIntimacao)
			throws DaoException {

		Session session = retrieveSession();

		try {
			session.save(controlePrazoIntimacao);
			session.flush();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	/**
	 * Método que recupera os processos que receberam intimação e que estão
	 * pendentes de andamento intimado. recupera os processos com data de
	 * intimado nula.
	 * 
	 * @return List ControlePrazoIntimacao
	 */
	public List<ControlePrazoIntimacao> recuperarProcessoIntimadoPendente()
			throws DaoException {
		
		try {
		
			Session session = retrieveSession();
			Criteria criteria = session	.createCriteria(ControlePrazoIntimacao.class);

			criteria.add(Restrictions.isNull("dataIntimado"));

			List<ControlePrazoIntimacao> listaControlePrazoIntimacao = criteria.list();
			
			return listaControlePrazoIntimacao;

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}

	public void atualizaControlePrazoIntimacao(ControlePrazoIntimacao controlePrazoIntimacao) throws DaoException{
		Session session = retrieveSession();

		try {
			session.update(controlePrazoIntimacao);
			session.flush();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}
	
	// ************************************************************************************************
	public ControlePrazoIntimacao recuperarControlePrazoIntimacao(Long seqAndamentoProcesso)  throws DaoException{
		
		Session session = retrieveSession();
        
        ControlePrazoIntimacao controlePrazoIntimacao = null;

        try {
        	
        	Criteria criteria = session.createCriteria(ControlePrazoIntimacao.class);
        	
        	criteria.add(Restrictions.eq("seqAndamentoProcesso", seqAndamentoProcesso));
        	
        	controlePrazoIntimacao = (ControlePrazoIntimacao) criteria.uniqueResult();
        	
        	
        }
    
        
        catch(HibernateException e) {
            throw new DaoException("HibernateException",
                    SessionFactoryUtils.convertHibernateAccessException(e));
        }
        catch( RuntimeException e ) {
            throw new DaoException("RuntimeException", e);
        }
        return controlePrazoIntimacao;
		
	}
}
