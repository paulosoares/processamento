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
import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.localizacao.model.dataaccess.SecaoSetorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class SecaoSetorDaoHibernate
extends GenericHibernateDao<SecaoSetor, Long>
implements SecaoSetorDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SecaoSetorDaoHibernate() {
		super(SecaoSetor.class);
	} 

	public SecaoSetor recuperarSecaoSetor(Long id,Long idSecao,Long idSetor, Boolean ativo) 
	throws DaoException {

		Session session = retrieveSession();

		SecaoSetor secaoSetor = null;

		try {

			Criteria criteria = session.createCriteria(SecaoSetor.class);
			if(id!=null){
				criteria.add(Restrictions.idEq(id));
			}
			if(idSecao!=null){
				criteria.add(Restrictions.eq("secao.id",idSecao));
			}
			if(idSetor!=null){
				criteria.add(Restrictions.eq("setor.id",idSetor));
			}
			if( ativo != null ) {
				criteria.add(Restrictions.eq("ativo", ativo));
			}

			secaoSetor = (SecaoSetor) criteria.uniqueResult();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return secaoSetor;
	}

	public List<SecaoSetor> pesquisarSecaoSetor(Long id, String sigUsuario, String descricaoSecao, Long idSecao, Long idSetor, Boolean ativo) throws DaoException {
		Session session = retrieveSession();

		List<SecaoSetor> secoes = null;

		try {
			StringBuffer hql = new StringBuffer(" SELECT s FROM SecaoSetor s ");

			if( sigUsuario != null &&!sigUsuario.equals("")){
				hql.append(" JOIN s.usuarios u ");
			}
			
			hql.append(" WHERE (1=1)");

			if( id != null ){
				hql.append(" AND s.id ="+id);
			}	

			if( idSecao != null ){
				hql.append(" AND s.secao.id ="+idSecao);
			}

			if(descricaoSecao!=null&&!descricaoSecao.equals("")){
				hql.append(" AND s.secao.descricao LIKE UPPER('%"+descricaoSecao+"%')");
			}

			if( idSetor != null ){
				hql.append(" AND s.setor.id ="+idSetor);
			}

			if( sigUsuario != null &&!sigUsuario.equals("")){
				hql.append(" AND u.id = '"+sigUsuario.toUpperCase()+"'");
			}
			
			if( ativo != null ) {
				if( ativo.booleanValue() )
					hql.append(" AND s.ativo = 'S' ");
				else
					hql.append(" AND s.ativo = 'N' ");
			}

			/*
			 * Código comentado pelo Tiago Peixoto no dia 16/07/2007
			 * 
			if( idSetor==null && idSecao == null ) {
				hql.append(" AND s.id =" +
				" (SELECT MAX(ss.id) from SecaoSetor ss " +
				"  WHERE ss.secao = s.secao) ");

			}
            */

			hql.append(" ORDER BY s.secao.descricao ");
			Query q = session.createQuery(hql.toString());
			secoes = q.list();

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return secoes;
	}
	
    public List<Secao> pesquisarSecao(Long id, String sigUsuario, String descricaoSecao, Long idSetor, Boolean ativo) throws DaoException {
        Session session = retrieveSession();

        List<Secao> secoes = null;

        try {
            StringBuffer hql = new StringBuffer(" SELECT s FROM Secao s ");

            if( (sigUsuario != null && !sigUsuario.equals("")) ||
                (idSetor != null) ){
                hql.append(" JOIN s.secoesSetor ss ");
            }
            
            if( (sigUsuario != null && !sigUsuario.equals("")) ){
                hql.append(" JOIN ss.usuarios u ");
            }            
            
            hql.append(" WHERE (1=1)");

            if( id != null ){
                hql.append(" AND s.id ="+id);
            }   

            if(descricaoSecao!=null&&!descricaoSecao.equals("")){
                hql.append(" AND s.descricao LIKE UPPER('%"+descricaoSecao+"%')");
            }

            if( idSetor != null ){
                hql.append(" AND ss.setor.id ="+idSetor);
            }

            if( sigUsuario != null &&!sigUsuario.equals("")){
                hql.append(" AND u.id = '"+sigUsuario.toUpperCase()+"'");
            }
            
            if( ativo != null ) {
            	if( ativo.booleanValue() )
            		hql.append(" AND ss.ativo = 'S' ");
            	else
            		hql.append(" AND ss.ativo = 'N' ");
            }

            hql.append(" ORDER BY s.descricao ");
            Query q = session.createQuery(hql.toString());
            secoes = q.list();

        }
        catch(HibernateException e) {
            throw new DaoException("HibernateException",
                    SessionFactoryUtils.convertHibernateAccessException(e));
        }
        catch( RuntimeException e ) {
            throw new DaoException("RuntimeException", e);
        }

        return secoes;
    }
	

	public List<SecaoSetor> pesquisarTarefaSecao(Long id, Long idTarefa, String descricaoTarefa, String descricaoSecao, Long idSecao, Long idSetor) throws DaoException {
		Session session = retrieveSession();

		List<SecaoSetor> secoes = null;

		try {
			StringBuffer hql = new StringBuffer(" SELECT s FROM SecaoSetor s ");

			if( idTarefa != null ||(descricaoTarefa != null &&!descricaoTarefa.equals(""))){
				hql.append(" JOIN s.tarefas t ");
			}
			hql.append(" WHERE (1=1) ");

			if( id != null ){
				hql.append(" AND s.id ="+id);
			}	

			if( idSecao != null ){
				hql.append(" AND s.secao.id ="+idSecao);
			}

			if(descricaoSecao!=null&&!descricaoSecao.equals("")){
				hql.append(" AND s.secao.descricao LIKE UPPER('%"+descricaoSecao+"%')");
			}

			if( idSetor != null ){
				hql.append(" AND s.setor.id ="+idSetor);
			}

			if( idTarefa != null ){
				hql.append(" AND t.id ="+idTarefa);
			}

			if(descricaoTarefa != null &&!descricaoTarefa.equals("")){
				hql.append(" AND t.descricao LIKE UPPER('%"+descricaoSecao+"%') ");
			}

			if(idSetor==null&&idSecao == null){
				hql.append(" AND s.id =" +
				" (SELECT MAX(ss.id) FROM SecaoSetor ss " +
				"  WHERE ss.secao = s.secao )");

			}

			hql.append(" ORDER BY s.secao.descricao ");
			Query q = session.createQuery(hql.toString());
			secoes = q.list();

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return secoes;

	}


	public SecaoSetor recuperarSecaoSetor(Long id,Usuario usuario,Secao secao, Long idSetor, Boolean ativo)throws DaoException{
		Session session = retrieveSession();

		SecaoSetor secaoSetor = null;

		try {

			Criteria criteria = session.createCriteria(SecaoSetor.class, "ss");

			if( id != null && id > 0)
				criteria.add(Restrictions.idEq(id));

			if( secao != null )
				criteria.add(Restrictions.eq("ss.secao.id", secao.getId()));        	
            
            if( idSetor != null )
                criteria.add(Restrictions.eq("ss.setor.id", idSetor));   

			if( usuario != null )
				criteria = criteria.createCriteria("ss.usuarios", "u").add(Restrictions.eq("u.id", usuario.getId()));
			
			if( ativo != null ) {
				criteria.add(Restrictions.eq("u.ativo", ativo));
				criteria.add(Restrictions.eq("ss.ativo", ativo)); 
			}
			
			
			/*
            StringBuffer hql = 
            new StringBuffer(" SELECT ss FROM SecaoSetor ss ");
                if(usuario!=null && usuario.getSigla()!=null){
                        hql.append(" INNER JOIN ss.usuarios u ");                    
                }
                        hql.append(" WHERE 1=1 ");

            if (id!=null){
                hql.append(" ss.id = "+ id);
            }
            if (secao != null ) {
                if (secao.getId() != null ) {
                    hql.append(
                        " AND ss.secao.id =" + secao.getId());
                }else if (secao.getDescricao() != null ) {
                    hql.append(
                        " AND ss.secao.descricao like '%" + secao.getDescricao()+ "%'");
                } 
            }

            if (usuario != null &&usuario.getSigla()!=null ) {

                hql.append(
                        " AND u.sigla = '" + usuario.getSigla()+"'");

            }
			 */

			//Query query = session.createQuery(hql.toString());
			//secaoSetor = (SecaoSetor)query.uniqueResult();

			secaoSetor = (SecaoSetor) criteria.uniqueResult();

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return secaoSetor;
	}

	public Long incluirSecaoSetor(SecaoSetor secaoSetor) 
	throws DaoException {

		Long id = null;

		Session session = retrieveSession();

		try {
			id = (Long) session.save(secaoSetor);
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

	public Boolean alterarSecaoSetor(SecaoSetor secaoSetor) 
	throws DaoException {

		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.update(secaoSetor);
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

	public List pesquisarSecaoSetor(Long id,Secao secao, Boolean ativo) throws DaoException{
		Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);

		try {
			StringBuffer hql = new StringBuffer(" SELECT ss FROM SecaoSetor ss WHERE (1=1) ");

			if (id != null) {
				hql.append(" AND ss.id = " + id);
			}

			if (secao != null) {
				hql.append(" AND ss.secao.id = " + secao.getId());
			}
			
			if( ativo != null ) {
				if( ativo.booleanValue() )
					hql.append(" AND ss.ativo = 'S' ");
				else
					hql.append(" AND ss.ativo = 'N' ");
			}


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

	public Boolean persistirSecaoSetor(SecaoSetor secaoSetor) throws DaoException{
		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.persist(secaoSetor);
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

	public Boolean excluirSecaoSetor(SecaoSetor secaoSetor) throws DaoException {
		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.delete(secaoSetor);
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