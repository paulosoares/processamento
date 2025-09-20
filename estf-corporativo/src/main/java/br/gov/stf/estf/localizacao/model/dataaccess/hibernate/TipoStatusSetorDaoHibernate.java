package br.gov.stf.estf.localizacao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.TipoStatusSetor;
import br.gov.stf.estf.localizacao.model.dataaccess.TipoStatusSetorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoStatusSetorDaoHibernate
extends GenericHibernateDao<TipoStatusSetor, Long>
implements TipoStatusSetorDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7160200403992499222L;


	public TipoStatusSetorDaoHibernate() {
		super(TipoStatusSetor.class);
	}
	
	public List <TipoStatusSetor> pesquisarTipoStatusSetor(String descricao, Long idSetor, 
			Boolean comumEntreSetores, Boolean ativo)
		throws DaoException{
		
		Session sessao = retrieveSession();
		
		List<TipoStatusSetor> listaStatusSetor = null;
		
		try{
			StringBuffer hql = new StringBuffer("select f from TipoStatusSetor f " +
			"where 1=1 "); 
	
		if( descricao != null && !descricao.equals("")){
			hql.append(" and f.descricao like '%" + descricao.toUpperCase() + "%'");
		}
	
		if(idSetor != null){
			hql.append(" and");
			
			if(comumEntreSetores != null && comumEntreSetores.booleanValue())
				hql.append(" (");
			
			hql.append(" f.setor.id = " + idSetor);
			
			if(comumEntreSetores != null && comumEntreSetores.booleanValue())
				hql.append(" or f.setor.id is null)");
		}
		
		if(comumEntreSetores != null && idSetor == null)
		{
			if(comumEntreSetores.booleanValue())
				hql.append(" and f.setor.id is null");
			else
				hql.append(" and f.setor.id is not null");
		}
		
		if(ativo!=null){
			if(ativo.booleanValue()){
				hql.append(" and f.ativo = 'S'");
			}else{
				hql.append(" and f.ativo = 'N'");
			}	
		}
		
		hql.append(" ORDER BY f.descricao " );
	
		Query q = sessao.createQuery(hql.toString());
		listaStatusSetor = q.list();

		
		}catch(HibernateException ex){
			
			throw new DaoException( "HibernateException" ,
					SessionFactoryUtils.convertHibernateAccessException( ex ));
		}
		catch( RuntimeException ex ){
			throw new DaoException( "RuntimeException " , ex );
		}
			
		return listaStatusSetor;
		
	}
		
	public Boolean verificarUnicidade(String descricao,Long idSetor)throws DaoException{
		Session sessao = retrieveSession();
		
		Boolean possui =  Boolean.FALSE;
		
		try{
			StringBuffer hql = new StringBuffer("select f from TipoStatusSetor f " +
			"where 1=1 "); 
	
		if( descricao != null && !descricao.equals("")){
			hql.append(" and f.descricao = '" + descricao.toUpperCase() + "'");
		}
		
		if( idSetor !=null){
			hql.append("and ( f.setor.id = " + idSetor + 
					      " or f.setor.id is null)");
		}
		
		Query q = sessao.createQuery(hql.toString());
		List lista = q.list();
		 if(lista!=null&&lista.size()>0){
			 possui = Boolean.TRUE;
		 }else{
			 possui = Boolean.FALSE;
		 }

		
		}catch(HibernateException ex){
			
			throw new DaoException( "HibernateException" ,
					SessionFactoryUtils.convertHibernateAccessException( ex ));
		}
		catch( RuntimeException ex ){
			throw new DaoException( "RuntimeException " , ex );
		}
		return possui;	
	}
	public Boolean verificarDependecia(Long idTipoStatusSetor)throws DaoException{
		Session sessao = retrieveSession();
		
		Boolean possui =  Boolean.FALSE;
		
		try{
			StringBuffer hql = new StringBuffer("select h from HistoricoFase h " +
			"where 1=1 "); 
	
		if( idTipoStatusSetor != null && !idTipoStatusSetor.equals("")){
			hql.append(" and h.tipoStatusSetor.id = " + idTipoStatusSetor );
		}
		
		Query q = sessao.createQuery(hql.toString());
		List lista = q.list();
		 if(lista!=null&&lista.size()>0){
			 possui = Boolean.TRUE;
		 }else{
			 possui = Boolean.FALSE;
		 }

		
		}catch(HibernateException ex){
			throw new DaoException( "HibernateException" ,
					SessionFactoryUtils.convertHibernateAccessException( ex ));
		}
		catch( RuntimeException ex ){
			throw new DaoException( "RuntimeException " , ex );
		}
		return possui;	
	}

	public TipoStatusSetor recuperarTipoStatusSetor(Long idStatus)
		throws DaoException{
		
		Session sessao = retrieveSession();
		TipoStatusSetor statusSetor = null;
		
		try{
			
			if (idStatus != null){
				Criteria consulta = sessao.createCriteria( TipoStatusSetor.class );
				consulta.add(Restrictions.idEq( idStatus ));
				statusSetor = (TipoStatusSetor) consulta.uniqueResult();
				
			}
 		}catch(HibernateException ex ){
 			throw new DaoException( "HibernateException" ,
 					SessionFactoryUtils.convertHibernateAccessException( ex ));
 		}
 		catch( RuntimeException ex ){
 			throw new DaoException( "RuntimeException" , ex );
 		}
 		
 		return statusSetor;		
	}
	
	public Boolean persistirTipoStatusSetor( TipoStatusSetor status )
		throws DaoException{
		
		Session sessao = retrieveSession();
		Boolean sucesso = Boolean.FALSE;
		try{
			
			sessao.persist( status );
			sessao.flush();
			sucesso = Boolean.TRUE;
			
		}
		catch(HibernateException ex ){
			throw new DaoException( "HibernateException" , 
					SessionFactoryUtils.convertHibernateAccessException( ex ));
		}
		catch( RuntimeException ex ){
			throw new DaoException( "RuntimeException" , ex );
		}
		
		return sucesso;
		
	}
		
	
	public Boolean excluirTipoStatusSetor(TipoStatusSetor status)
		throws DaoException{
		
		Session sessao = retrieveSession();
		Boolean sucesso = Boolean.FALSE;
		try{
			sessao.delete( status );
			sucesso = Boolean.TRUE;
		}catch( HibernateException ex ){
			throw new DaoException( "HibernateException" ,
					SessionFactoryUtils.convertHibernateAccessException( ex ));
		}
		catch( RuntimeException ex ){
			throw new DaoException( "RuntimException" , ex );
		}
		
		return sucesso;
		
	}
		
	
		
	
	
	
	
}