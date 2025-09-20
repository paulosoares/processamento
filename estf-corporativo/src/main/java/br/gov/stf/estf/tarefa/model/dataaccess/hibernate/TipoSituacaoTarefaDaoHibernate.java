package br.gov.stf.estf.tarefa.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.tarefa.TipoSituacaoTarefa;
import br.gov.stf.estf.tarefa.model.dataaccess.TipoSituacaoTarefaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoSituacaoTarefaDaoHibernate
	extends GenericHibernateDao<TipoSituacaoTarefa, Long>
	implements TipoSituacaoTarefaDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7586506486768434266L;

	public TipoSituacaoTarefaDaoHibernate() {
    	super(TipoSituacaoTarefa.class);
    }

    public List<TipoSituacaoTarefa> pesquisarTipoSituacaoTarefa(Long Id,String Descricao,
			Long idSetor,Boolean ativo,Boolean semSetor)
    throws DaoException {
    	
    	Session session = retrieveSession();
        
	    List<TipoSituacaoTarefa> listaSituacao = null;
        
	    try {
	    	Criteria criteria = session.createCriteria(TipoSituacaoTarefa.class);

	    	if(Id!=null){
                criteria.add(Restrictions.eq("id", Id));
            }
            
            if(Descricao != null&&Descricao.trim().length()>0){            	
                criteria.add(Restrictions.eq("descricao", Descricao));
            }
            
            if(idSetor != null){            	
            	if(semSetor != null&&semSetor){                
                    criteria.add(Restrictions.or(Restrictions.eq("setor.id", idSetor), Restrictions.isNull("setor.id")));
                }else{
                	criteria.add(Restrictions.eq("setor.id", idSetor));
                }
            	
            }
            
            if(ativo != null){                
                criteria.add(Restrictions.eq("ativo", ativo));
            }            
            
                        
            criteria.addOrder(Order.desc("descricao"));
            
            listaSituacao = (List<TipoSituacaoTarefa>) criteria.list();
	    }
	    catch(HibernateException e) {
	        throw new DaoException("HibernateException",
	                SessionFactoryUtils.convertHibernateAccessException(e));
	    }
	    catch( RuntimeException e ) {
	        throw new DaoException("RuntimeException", e);
	    }
	    return listaSituacao;    	
    }
    
    public TipoSituacaoTarefa recuperarTipoSituacaoTarefa(Long id)throws DaoException{
    	Session session = retrieveSession();
        
	    TipoSituacaoTarefa tipo = null;
        
	    try {
	    	Criteria criteria = session.createCriteria(TipoSituacaoTarefa.class);

	    	if(id!=null){
                criteria.add(Restrictions.eq("id", id));
            }
            
            tipo = (TipoSituacaoTarefa) criteria.uniqueResult();
	    }
	    catch(HibernateException e) {
	        throw new DaoException("HibernateException",
	                SessionFactoryUtils.convertHibernateAccessException(e));
	    }
	    catch( RuntimeException e ) {
	        throw new DaoException("RuntimeException", e);
	    }
	    return tipo;    	
    }
    
    
    public Boolean persistirTipoSituacaoTarefa(TipoSituacaoTarefa tipoSituacaoTarefa) throws DaoException{
		Session session = retrieveSession();
		Boolean alterado = Boolean.FALSE;
		try {
			session.persist(tipoSituacaoTarefa);
			session.flush();

			alterado = Boolean.TRUE;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return alterado;

    }
    
    public Boolean excluirTipoSituacaoTarefa(TipoSituacaoTarefa tipoSituacaoTarefa) throws DaoException{
		Session session = retrieveSession();
		Boolean alterado = Boolean.FALSE;
		try {
			session.delete(tipoSituacaoTarefa);
			session.flush();

			alterado = Boolean.TRUE;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return alterado;

    }
    
}