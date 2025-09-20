package br.gov.stf.estf.tarefa.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.tarefa.ConfiguracaoTipoTarefaSetor;
import br.gov.stf.estf.entidade.tarefa.TipoAtribuicaoTarefa;
import br.gov.stf.estf.entidade.tarefa.TipoCampoTarefa;
import br.gov.stf.estf.entidade.tarefa.TipoCampoTarefaValor;
import br.gov.stf.estf.entidade.tarefa.TipoTarefaSetor;
import br.gov.stf.estf.tarefa.model.dataaccess.TipoTarefaSetorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoTarefaSetorDaoHibernate
extends GenericHibernateDao<TipoTarefaSetor, Long>
implements TipoTarefaSetorDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8265846489343697725L;

	public TipoTarefaSetorDaoHibernate() {
		super(TipoTarefaSetor.class);
	}

	public TipoTarefaSetor recuperarTipoTarefaSetor(Long id) throws DaoException{

		Session session = retrieveSession();

		TipoTarefaSetor tipo = null;

		try {
			Criteria criteria = session.createCriteria(TipoTarefaSetor.class);

			if(id!=null){
				criteria.add(Restrictions.eq("id", id));
			}

			tipo = (TipoTarefaSetor) criteria.uniqueResult();
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
	
	public TipoCampoTarefa recuperarTipoCampoTarefa(Long id) throws DaoException{

		Session session = retrieveSession();

		TipoCampoTarefa tipo = null;

		try {
			Criteria criteria = session.createCriteria(TipoCampoTarefa.class);

			if(id!=null){
				criteria.add(Restrictions.eq("id", id));
			}

			tipo = (TipoCampoTarefa) criteria.uniqueResult();
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
	
	public List<TipoTarefaSetor> pesquisarTipoTarefaSetor(Long id,String descricao , Long idSetor, Boolean ativo) throws DaoException{

		Session session = retrieveSession();

		List<TipoTarefaSetor> listaTipoTarefa = null;

		try {
			Criteria criteria = session.createCriteria(TipoTarefaSetor.class);

			if(id!=null){
				criteria.add(Restrictions.eq("id", id));
			}

			if(descricao != null&&descricao.trim().length()>0){            	
				criteria.add(Restrictions.eq("descricao", descricao.toUpperCase()));
			}

			if(ativo != null ){            	
				criteria.add(Restrictions.eq("ativo", ativo ));
			}

			if(idSetor != null ){            				
				criteria.add(Restrictions.eq("setor.id", idSetor ));	
			}

			listaTipoTarefa = (List<TipoTarefaSetor>) criteria.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		return listaTipoTarefa;    	
	}

	public List<TipoTarefaSetor> pesquisarTipoTarefaSetor(Long id,String descricao , Long idSetor, Boolean ativo, Boolean cargaProgramada) throws DaoException{

		Session session = retrieveSession();

		List<TipoTarefaSetor> listaTipoTarefa = null;

		try {
			Criteria criteria = session.createCriteria(TipoTarefaSetor.class);

			if(id!=null){
				criteria.add(Restrictions.eq("id", id));
			}

			if(descricao != null&&descricao.trim().length()>0){            	
				criteria.add(Restrictions.eq("descricao", descricao.toUpperCase()));
			}

			if(ativo != null ){            	
				criteria.add(Restrictions.eq("ativo", ativo ));
			}

			Long idSetorSTF = 600000000L;
			
			if(idSetor != null ){            					
			if (cargaProgramada)			
			{
				criteria.add( Restrictions.or(Restrictions.eq("setor.id", idSetor), 
						Restrictions.eq("setor.id",idSetorSTF)));}
			else
			{	criteria.add(Restrictions.eq("setor.id", idSetor ));}	
						}

			listaTipoTarefa = (List<TipoTarefaSetor>) criteria.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		return listaTipoTarefa;    	
	}	
	
	public Boolean persistirTipoTarefaSetor(TipoTarefaSetor tipoTarefaSetor) throws DaoException{
		Session session = retrieveSession();
		Boolean alterado = Boolean.FALSE;
		try {
			session.persist(tipoTarefaSetor);
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

	public Boolean excluirTipoTarefaSetor(TipoTarefaSetor tipoTarefaSetor) throws DaoException{
		Session session = retrieveSession();
		Boolean alterado = Boolean.FALSE;
		try {
			session.delete(tipoTarefaSetor);
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

	public Boolean verificarUnicidade(String descricao,Long idSetor)throws DaoException{
		Session sessao = retrieveSession();

		Boolean possui =  Boolean.FALSE;

		try{
			StringBuffer hql = new StringBuffer("select tp from TipoTarefaSetor tp " +
			"where 1=1 "); 

			if( descricao != null && !descricao.equals("")){
				hql.append(" and tp.descricao = '" + descricao.toUpperCase() + "'");
			}

			if( idSetor !=null){
				hql.append("and ( tp.setor.id = " + idSetor + 
				" or tp.setor.id is null)");
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

	public TipoAtribuicaoTarefa recuperarTipoAtribuicaoTarefa(Long id,String sigla,String descricao) throws DaoException{
		Session session = retrieveSession();

		TipoAtribuicaoTarefa tipo = null;

		try {
			Criteria criteria = session.createCriteria(TipoAtribuicaoTarefa.class);

			if(id!=null){
				criteria.add(Restrictions.eq("id", id));
			}

			if(sigla!=null&&sigla.trim().length()>0){
				criteria.add(Restrictions.eq("sigla", sigla.toUpperCase()));
			}

			if(descricao!=null&&descricao.trim().length()>0){
				criteria.add(Restrictions.eq("descricao", descricao.toUpperCase()));
			}

			tipo = (TipoAtribuicaoTarefa) criteria.uniqueResult();
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

	public List<TipoAtribuicaoTarefa> pesquisarTipoAtribuicaoTarefa(Long id,String sigla,String descricao) throws DaoException{
		Session session = retrieveSession();

		List<TipoAtribuicaoTarefa> listaTipoTarefa = null;

		try {
			Criteria criteria = session.createCriteria(TipoAtribuicaoTarefa.class);

			if(id!=null){
				criteria.add(Restrictions.eq("id", id));
			}

			if(sigla!=null&&sigla.trim().length()>0){
				criteria.add(Restrictions.eq("sigla", sigla.toUpperCase()));
			}

			if(descricao!=null&&descricao.trim().length()>0){
				criteria.add(Restrictions.eq("descricao", descricao.toUpperCase()));
			}

			listaTipoTarefa = criteria.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		return listaTipoTarefa; 
	}

	public ConfiguracaoTipoTarefaSetor recuperarConfiguracaoTipoTarefaSetor(Long id,String sigla,String descricao) throws DaoException{
		Session session = retrieveSession();

		ConfiguracaoTipoTarefaSetor tipo = null;

		try {
			Criteria criteria = session.createCriteria(ConfiguracaoTipoTarefaSetor.class);

			if(id!=null){
				criteria.add(Restrictions.eq("id", id));
			}

			if(sigla!=null&&sigla.trim().length()>0){
				criteria.add(Restrictions.eq("sigla", sigla.toUpperCase()));
			}

			if(descricao!=null&&descricao.trim().length()>0){
				criteria.add(Restrictions.eq("descricao", descricao.toUpperCase()));
			}

			tipo = (ConfiguracaoTipoTarefaSetor) criteria.uniqueResult();
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
	public List<ConfiguracaoTipoTarefaSetor> pesquisarConfiguracaoTipoTarefaSetor(Long id,String sigla,String descricao) throws DaoException{
		Session session = retrieveSession();

		List<ConfiguracaoTipoTarefaSetor> lista = null;

		try {
			Criteria criteria = session.createCriteria(ConfiguracaoTipoTarefaSetor.class);

			if(id!=null){
				criteria.add(Restrictions.eq("id", id));
			}

			if(sigla!=null&&sigla.trim().length()>0){
				criteria.add(Restrictions.eq("sigla", sigla.toUpperCase()));
			}

			if(descricao!=null&&descricao.trim().length()>0){
				criteria.add(Restrictions.eq("descricao", descricao.toUpperCase()));
			}
			criteria.addOrder(Order.asc("descricao"));

			lista = criteria.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		return lista; 
	}
	
	public List<TipoCampoTarefaValor> pesquisarTipoCampoTarefaValor(Long id, Long idTipoCampoTarefa)throws DaoException{
		Session session = retrieveSession();

		List<TipoCampoTarefaValor> lista = null;

		try {
			Criteria criteria = session.createCriteria(TipoCampoTarefaValor.class);

			if( id != null ){
				criteria.add(Restrictions.eq("id", id));
			}

			if( idTipoCampoTarefa != null ){
				criteria.add(Restrictions.eq("tipoCampoTarefa.id", idTipoCampoTarefa));
			}

			//criteria.addOrder(Order.asc("id"));

			lista = criteria.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		return lista; 

	}



}