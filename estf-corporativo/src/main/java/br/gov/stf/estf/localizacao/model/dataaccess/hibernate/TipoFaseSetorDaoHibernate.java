package br.gov.stf.estf.localizacao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.gov.stf.estf.entidade.localizacao.FluxoTipoFaseSetor;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.localizacao.TipoFaseSetor;
import br.gov.stf.estf.localizacao.model.dataaccess.TipoFaseSetorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoFaseSetorDaoHibernate 
extends GenericHibernateDao<TipoFaseSetor, Long>
implements TipoFaseSetorDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3832053404549068641L;

	public TipoFaseSetorDaoHibernate(){
		super(TipoFaseSetor.class);
	}

	public TipoFaseSetor recuperarTipoFaseSetor(String descricao)
	throws DaoException {

		Session session = retrieveSession();

		TipoFaseSetor faseSetor = null;

		try{

			Criteria criteria = session.createCriteria(TipoFaseSetor.class);

			if( descricao != null )
				criteria.add(Restrictions.like("descricao", descricao));

			faseSetor = (TipoFaseSetor) criteria.uniqueResult();


		}   catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return faseSetor;
	}

	public TipoFaseSetor recuperarTipoFaseSetor(Long idTipoFaseSetor) throws DaoException {

		Session session = retrieveSession();

		TipoFaseSetor faseSetor = null;

		try{

			Criteria criteria = session.createCriteria(TipoFaseSetor.class);

			if( idTipoFaseSetor != null )
				criteria.add(Restrictions.like("id", idTipoFaseSetor));

			faseSetor = (TipoFaseSetor) criteria.uniqueResult();


		}   catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return faseSetor;
	}

	public Boolean persistirTipoFaseSetor(TipoFaseSetor faseSetor)
	throws DaoException{

		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.persist(faseSetor);
			session.flush();

			alterado = Boolean.TRUE;

		}catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return alterado;		
	}

	public List<TipoFaseSetor> pesquisarTipoFaseSetor(String descricao, Long idSetor, 
			Boolean comumEntreSetores, Boolean soAtivo) 
	throws DaoException {
		
		Session session = retrieveSession();

		List<TipoFaseSetor> listaFaseSetor = null;

		try{
			StringBuffer hql = new StringBuffer("select f from TipoFaseSetor f " +
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
		
			if(soAtivo){
				hql.append(" and f.ativo = 'S' ");
			}
			
			hql.append(" ORDER BY f.descricao ");
			Query q = session.createQuery(hql.toString());
			listaFaseSetor = q.list();

		}catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return listaFaseSetor;
	}
	
	public List<FluxoTipoFaseSetor> pesquisarFluxoTipoFaseSetor(
			TipoFaseSetor tipoFaseSetorAntecessor,
			TipoFaseSetor tipoFaseSetorSucessor,
			Setor localizacao) 
    throws DaoException {
		Session session = retrieveSession();

		List<FluxoTipoFaseSetor> lista = null;

		try {
			Criteria criteria = session.createCriteria(FluxoTipoFaseSetor.class);
			
			if( localizacao != null ) {
				criteria.add(Restrictions.eq("setor.id", localizacao.getId()));
			}
			
			if( tipoFaseSetorAntecessor != null && tipoFaseSetorSucessor == null ) 
				criteria.add(Restrictions.eq("tipoFaseAntecessor.id", tipoFaseSetorAntecessor.getId()));
			else
				criteria.add(Restrictions.isNull("tipoFaseAntecessor.id"));
			
			if( tipoFaseSetorSucessor != null && tipoFaseSetorAntecessor == null ) 
				criteria.add(Restrictions.eq("tipoFaseSucessor.id", tipoFaseSetorSucessor.getId()));
			
			if( tipoFaseSetorAntecessor != null && tipoFaseSetorSucessor != null )
				criteria.add(
						Restrictions.or(
								Restrictions.eq("tipoFaseAntecessor.id", tipoFaseSetorAntecessor.getId()),
						Restrictions.eq("tipoFaseSucessor.id", tipoFaseSetorSucessor.getId())
						) );
				
			lista = criteria.list();

		}catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return lista;
	}	

	public Boolean verificarUnicidade(String descricao,Long idSetor) throws DaoException{
		Session sessao = retrieveSession();
		
		Boolean possui =  Boolean.FALSE;
		
		try{
			StringBuffer hql = new StringBuffer("select f from TipoFaseSetor f " +
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
	
	public String verificarDependencia(Long idFase) throws DaoException{
		Session sessao = retrieveSession();
		String possuiDependeciaCom = "";
		
		try{
			// verifica se a fase esta relacionada com algum processo localizacao
			StringBuffer hql = new StringBuffer("select count(*) from HistoricoFase h where  1=1 "); 
	
			if( idFase != null && !idFase.equals("")){
				hql.append(" and h.tipoFaseSetor.id = " + idFase );
			}
		
			Query q = sessao.createQuery(hql.toString());
			Long qtdHistoricoFase = (Long) q.list().get(0);
			// verifica se esta fase possui fases sucessoras
			hql = new StringBuffer("select count(*) from FluxoTipoFaseSetor h  where  rownum = 1 and  1=1 "); 
			String hql2 = "";
			String hql3 = "";
			if( idFase != null && !idFase.equals("")){
				hql2 = " and h.tipoFaseAntecessor.id = " + idFase;
				hql3 = " and h.tipoFaseSucessor.id = " + idFase;
				
			}
			
			q = sessao.createQuery(hql.toString()+hql2);
			Long qtdFaseSucessora = (Long) q.list().get(0);
			
			
			q = sessao.createQuery(hql.toString()+hql3);
			Long qtdFaseDependente= (Long) q.list().get(0);
			
			if(qtdHistoricoFase!=null&&qtdHistoricoFase >0L){
				possuiDependeciaCom=" Existe(m) "+ qtdHistoricoFase + " registros de históricos marcados com essa fase.";
			}
			
			if(qtdFaseSucessora!=null&&qtdFaseSucessora > 0L){
			    		possuiDependeciaCom = possuiDependeciaCom + " Possui fase sucessora.";
			}
			
			if(qtdFaseDependente!=null&&qtdFaseDependente > 0L){
				    possuiDependeciaCom= possuiDependeciaCom + " É uma fase sucessora.";
			}
			
		}catch(HibernateException ex){
			
			throw new DaoException( "HibernateException" ,
					SessionFactoryUtils.convertHibernateAccessException( ex ));
		}
		catch( RuntimeException ex ){
			throw new DaoException( "RuntimeException " , ex );
		}
		return possuiDependeciaCom;	
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Boolean excluirTipoFaseSetor(TipoFaseSetor faseSetor)throws  DaoException{

		Session session = retrieveSession();
		
		try {
		
			StringBuffer queryString = new StringBuffer();
			queryString.append(" update egab.processo_setor SET seq_fase_atual = null where seq_fase_atual IN (select seq_historico_fase FROM egab.historico_fase hf where hf.seq_tipo_fase_setor = :idFaseSetor )");
			SQLQuery query = session.createSQLQuery(queryString.toString());
			query.setParameter("idFaseSetor", faseSetor.getId());
			query.executeUpdate();
			
			StringBuffer queryString1 = new StringBuffer();
			queryString1.append(" delete from egab.historico_fase hf where hf.seq_tipo_fase_setor = :idFaseSetor ");
			SQLQuery query1 = session.createSQLQuery(queryString1.toString());
			query1.setParameter("idFaseSetor", faseSetor.getId());
			query1.executeUpdate();
			
			StringBuffer queryString2 = new StringBuffer();
			queryString2.append(" delete from egab.fluxo_processo_setor fp where fp.cod_setor = :idSetor and (fp.seq_tipo_fase_setor_anterior = :idFaseSetor or fp.seq_tipo_fase_setor_sucessor = :idFaseSetor) ");
			SQLQuery query2 = session.createSQLQuery(queryString2.toString());
			query2.setParameter("idFaseSetor", faseSetor.getId());
			query2.setParameter("idSetor", faseSetor.getSetor().getId());
			query2.executeUpdate();
			
			StringBuffer queryString3 = new StringBuffer();
			queryString3.append(" DELETE FROM egab.tipo_fase_setor tf ");
			queryString3.append(" where tf.seq_tipo_fase_setor = :idFaseSetor and  tf.cod_setor = :idSetor ");
			SQLQuery query3 = session.createSQLQuery(queryString3.toString());
			query3.setParameter("idFaseSetor", faseSetor.getId());
			query3.setParameter("idSetor", faseSetor.getSetor().getId());
			query3.executeUpdate();
			
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

    public Boolean persistirFluxoTipoFaseSetor(FluxoTipoFaseSetor fluxoTipoFaseSetor) throws DaoException {
		Boolean persistido = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.persist(fluxoTipoFaseSetor);
			session.flush();

			persistido = Boolean.TRUE;

		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}    	

		return persistido;	
    }
     
    public Boolean excluirFluxoTipoFaseSetor(FluxoTipoFaseSetor fluxoTipoFaseSetor)throws DaoException {
    	
    	Boolean excluido = Boolean.FALSE;
    	
		Session session = retrieveSession();

		try {
			session.delete(fluxoTipoFaseSetor);
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
}
