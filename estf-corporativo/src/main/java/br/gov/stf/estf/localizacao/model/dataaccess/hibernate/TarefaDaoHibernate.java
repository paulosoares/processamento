package br.gov.stf.estf.localizacao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.Secao;
import br.gov.stf.estf.entidade.localizacao.Tarefa;
import br.gov.stf.estf.localizacao.model.dataaccess.TarefaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TarefaDaoHibernate
	extends GenericHibernateDao<Tarefa, Long>
	implements TarefaDao {

    public TarefaDaoHibernate() {
    	super(Tarefa.class);
    } 
    
    public Secao recuperarTarefa(Long id) 
    throws DaoException {
    	
        Session session = retrieveSession();
        
        Secao secao = null;

        try {
        	
        	Criteria criteria = session.createCriteria(Secao.class);
        	
        	criteria.add(Restrictions.idEq(id));

        	secao = (Secao) criteria.uniqueResult();
        }
        catch(HibernateException e) {
            throw new DaoException("HibernateException",
                    SessionFactoryUtils.convertHibernateAccessException(e));
        }
        catch( RuntimeException e ) {
            throw new DaoException("RuntimeException", e);
        }
        
        return secao;
    }
    public List<Tarefa> verificarUnicidade(Long id,String descricao) throws DaoException{
    	Session session = retrieveSession();
        
        try {
            StringBuffer hql = new StringBuffer(
            " SELECT t FROM Tarefa t" +
            " WHERE (1=1)"); 
            
	            if(id!= null){
	            	hql.append(" AND t.id = " + id);
	            }

                if(descricao!= null && !descricao.equals("")){
                      hql.append(" AND t.descricao = '" + descricao.toUpperCase()+"'");
                }
                        
            hql.append(" ORDER BY t.descricao");       
                        
            Query query = session.createQuery(hql.toString());
            List result = query.list();
    
            return result;
        }
        catch (HibernateException e) {
            throw new DaoException(
                    SessionFactoryUtils.convertHibernateAccessException(e));
        }
        catch (RuntimeException e) {
            throw new DaoException(e);
        }

    }
    public List<Tarefa> pesquisarTarefa(Long id,String descricao, Long idSecao, Long idSetor,boolean localizacaoIgual) throws DaoException {

        Session session = retrieveSession();
    
        try {
            StringBuffer hql = new StringBuffer("");
            if(idSetor==null){
            	hql.append(" SELECT t FROM Tarefa t");
            }else{
            	hql.append(" SELECT t FROM SecaoSetor ss " +
                " INNER JOIN ss.tarefas t");
            }
            
            hql.append(" WHERE (1=1)"); 
            
	            if(id!= null){
	            	hql.append(" AND t.id = " + id);
	            }

                if(descricao!= null && !descricao.equals("")){
                      hql.append(" AND t.descricao like '%" + descricao.toUpperCase()+"%'");
                }
                        
                if(idSetor!= null){
                	
                	if(idSecao!= null){
                        hql.append(" AND ss.secao.id = " + idSecao);
                    }
                	
                	if(localizacaoIgual){
                		hql.append(" AND ss.setor.id = " + idSetor);
                	}else{
                		hql.append(" AND ss.setor.id <> " + idSetor);
                	}
                }
                
                
                        
            hql.append(" ORDER BY t.descricao");       
                        
            Query query = session.createQuery(hql.toString());
            List result = query.list();
    
            return result;
        }
        catch (HibernateException e) {
            throw new DaoException(
                    SessionFactoryUtils.convertHibernateAccessException(e));
        }
        catch (RuntimeException e) {
            throw new DaoException(e);
        }
    }
    
   public Boolean excluirTarefa(Tarefa tarefa) throws DaoException {
	   Boolean excluido = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.delete(tarefa);
			session.flush();

			excluido = Boolean.TRUE;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return excluido;
	}

	public Boolean persistirTarefa(Tarefa tarefa) throws DaoException {
		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.persist(tarefa);
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
