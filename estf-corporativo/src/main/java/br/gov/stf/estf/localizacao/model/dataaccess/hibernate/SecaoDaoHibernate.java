package br.gov.stf.estf.localizacao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.ParametroSecao;
import br.gov.stf.estf.entidade.localizacao.Secao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.localizacao.model.dataaccess.SecaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class SecaoDaoHibernate
	extends GenericHibernateDao<Secao, Long>
	implements SecaoDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SecaoDaoHibernate() {
    	super(Secao.class);
    } 
    
    public Secao recuperarSecao(Long id) 
    throws DaoException {
    	
        Session session = retrieveSession();
        limparContexto();
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
    
    public Secao recuperarSecao(Long id, String descricao, String sigla) throws DaoException{
    	Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);

        try {
            StringBuffer hql = new StringBuffer(" SELECT s FROM Secao s WHERE (1=1)");

            if (id != null) {
                hql.append(" AND s.id = " + id);
            }

            if (descricao != null) {
                descricao = descricao.trim().replace(' ', '%');

                hql.append(" AND s.descricao = '" + descricao + "'");
            }

            if (sigla != null) {
                sigla = sigla.trim();
                
               hql.append(" AND s.sigla = '" + sigla + "'");

            }    
            
            Secao secao = (Secao)session.createQuery(hql.toString()).uniqueResult();
            return secao;
        }
        catch(HibernateException e) {
            throw new DaoException(
                    SessionFactoryUtils.convertHibernateAccessException(e));
        }
        catch( RuntimeException e ) {
            throw new DaoException(e);
        }

    }
    
    public List pesquisarSecao(Long id, String descricao, String sigla, Setor localizacao, Boolean ativo)
    throws DaoException {

        Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);
        //limparContextoPersistencia();
        try {
            StringBuffer hql = new StringBuffer(" SELECT s FROM Secao s");
            if (localizacao != null) {
            	hql.append(" JOIN s.secoesSetor ss ");
            }
            
            hql.append(" WHERE (1=1) ");

            if (id != null) {
                hql.append(" AND s.id = " + id);
            }

            if (descricao != null) {
                descricao = descricao.trim().replace(' ', '%');

                hql.append(" AND s.descricao LIKE '%" + descricao + "%'");
            }

            if (sigla != null) {
                sigla = sigla.trim();
                
               hql.append(" AND s.sigla = '" + sigla + "'");
            }    
            
            if (localizacao != null) {
                hql.append(" AND ss.setor.id = " + localizacao.getId());
            }
            
            if( ativo != null ) {
            	if( ativo.booleanValue() )
            		hql.append(" AND ss.ativo = 'S' ");
            	else
            		hql.append(" AND ss.ativo = 'N' ");
            }
                        
            hql.append(" ORDER BY s.descricao ");
            Query query = session.createQuery(hql.toString());
            List result = query.list();
            return result;
        }
        catch(HibernateException e) {
            throw new DaoException(
                    SessionFactoryUtils.convertHibernateAccessException(e));
        }
        catch( RuntimeException e ) {
            throw new DaoException(e);
        }
    }
    
    public String verificarDependencia(Long idSecao, Long idSetor) throws DaoException{
    	Session session = retrieveSession();
    	String dependencia = null;
    	try{
    		StringBuffer hql = new StringBuffer(" SELECT ss FROM SecaoSetor ss WHERE (1=1)");
            
    		if (idSecao != null) {
            	hql.append(" AND ss.secao.id = "+idSecao);
            }
    		
    		Query query = session.createQuery(hql.toString());
            List listaDepenciaSetor = query.list();
            
            hql = new StringBuffer(" SELECT hd FROM HistoricoDeslocamento hd ");            
            hql.append("                                     join hd.processoSetor ps ");            
            hql.append(" WHERE 1=1 ");
            
            if( idSetor != null )
            	hql.append(" AND hd.setor.id = " + idSetor );
            
    		if (idSecao != null) {
            	hql.append(" AND ((hd.secaoOrigem.id = "+idSecao+
            			   "        OR   hd.secaoDestino.id = " +idSecao+")) ");
            } 			  
 		   
    		query = session.createQuery(hql.toString());
            List listaDepenciaHistoriDeslocamento = query.list();
            
            hql = new StringBuffer(" SELECT hdp FROM HistoricoDeslocamentoPeticao hdp ");
            hql.append("                                     join hdp.peticaoSetor ps ");            
            hql.append(" WHERE 1=1 ");
            
            if( idSetor != null )
            	hql.append(" AND ps.setor.id = " + idSetor );
            
    		if (idSecao != null) {
            	hql.append(" AND ((hdp.secaoOrigem.id = "+idSecao+
            			   "        OR   hdp.secaoDestino.id = " +idSecao+")) ");
            } 			  
 		   
    		query = session.createQuery(hql.toString());
            List listaDepenciaHistoriDeslocamentoPeticao = query.list();
            
            if(listaDepenciaSetor!=null&&listaDepenciaSetor.size()>0){
            	dependencia = " possui dependência com um ou mais localizacaoes";
            }
            
            if(listaDepenciaHistoriDeslocamento!=null&&listaDepenciaHistoriDeslocamento.size()>0){
            	if(dependencia!=null&&dependencia.trim().length()>0){
            		dependencia = dependencia + ", e com deslocamentos de processo/protocolo";
            	}else{
            		dependencia = " possui dependência com deslocamentos de processo/protocolo";
            	}
            }
            
            if( listaDepenciaHistoriDeslocamentoPeticao != null && listaDepenciaHistoriDeslocamentoPeticao.size() > 0 ) {
            	if( dependencia != null && dependencia.trim().length() > 0 ) {
            		dependencia = dependencia + ", e com deslocamentos de petição";
            	}else{
            		dependencia = " possui dependência com deslocamentos de petição";
            	}
            }
    		    		
    	}catch(HibernateException e) {
            throw new DaoException(
                    SessionFactoryUtils.convertHibernateAccessException(e));
        }
        catch( RuntimeException e ) {
            throw new DaoException(e);
        }
        
        return dependencia;
    }
    
    
    public Long incluirSecao(Secao secao) 
    throws DaoException {
    	
    	Long id = null;
    	
        Session session = retrieveSession();
        
        try {
        	
        	id = (Long) session.save(secao);
        	session.flush();

        }
        catch(HibernateException e) {
            throw new DaoException("HibernateException",
                    SessionFactoryUtils.convertHibernateAccessException(e));
        }
        catch( RuntimeException e ) {
            throw new DaoException("RuntimeException", e);
        }    	
    	
    	return id;
    }
    
    public Boolean alterarSecao(Secao secao) 
    throws DaoException {
    	
    	Boolean alterado = Boolean.FALSE;
    	
        Session session = retrieveSession();
        limparContexto();
        
        try {
        	
        	session.update(secao);
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
    
    public Boolean excluirSecao(Secao secao) throws DaoException{
    	
    	Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);
        
        try {
        	
        	session.delete(secao);
        	session.flush();
        	return Boolean.TRUE;

        }
        catch(HibernateException e) {
            throw new DaoException("HibernateException",
                    SessionFactoryUtils.convertHibernateAccessException(e));
        }
        catch( RuntimeException e ) {
            throw new DaoException("RuntimeException", e);
        }    	
    }

    public Boolean persistirParametroSecao(ParametroSecao parametro) throws DaoException{
    	Session session = retrieveSession();
    	
    	Boolean alterado = Boolean.FALSE;
		try {

			session.persist(parametro);
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