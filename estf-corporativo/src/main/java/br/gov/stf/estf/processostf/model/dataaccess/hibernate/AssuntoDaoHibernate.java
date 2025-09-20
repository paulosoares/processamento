package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.Assunto;
import br.gov.stf.estf.processostf.model.dataaccess.AssuntoDao;
import br.gov.stf.estf.processostf.model.util.AssuntoSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.entity.Flag;
import br.gov.stf.framework.util.SearchData;
import br.gov.stf.framework.util.SearchResult;

@Repository
public class AssuntoDaoHibernate extends GenericHibernateDao<Assunto, String>
		implements AssuntoDao {
	
	public AssuntoDaoHibernate() {
		super(Assunto.class);
	}

	private static final long serialVersionUID = -6996835210272663629L;

	public SearchResult<Assunto> pesquisarPorDescricao(AssuntoSearchData sd) {
		try {
			Criteria c = retrieveSession().createCriteria(Assunto.class);
			
			if( SearchData.stringNotEmpty(sd.descricaoCompleta) ){
				c.add(Restrictions.sqlRestriction("contains(DSC_ASSUNTO_COMPLETO, ?) > 0", parseQuery(sd), new StringType()));
			}
			
			if( sd.ativo != null ){
				c.add(Restrictions.eq("ativo", sd.ativo));
			}
			
			c.addOrder(Order.asc("descricaoCompleta"));
			
			return pesquisarComPaginacaoCriteria(sd, c);
		} catch (DaoException e) {
			throw new RuntimeException(e);
		}
	}

	public SearchResult<Assunto> pesquisar(AssuntoSearchData sd)
		throws DaoException {
		
		try {
			Session session = retrieveSession();
			
			Criteria c = session.createCriteria(Assunto.class);
			
			if( SearchData.stringNotEmpty(sd.descricaoCompleta) ){
				sd.descricaoCompleta = sd.descricaoCompleta.replace('%', ' ');
				c.add(Restrictions.ilike("descricaoCompleta", sd.descricaoCompleta.trim(), MatchMode.ANYWHERE));
			}
			
			if( sd.ativo != null ){
				c.add(Restrictions.eq("ativo", sd.ativo));
			}
			
			c.addOrder(Order.asc("descricaoCompleta"));
			
			return pesquisarComPaginacaoCriteria(sd, c);
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
	}

	public List<Assunto> pesquisarAssuntoHierarquicoProcesso(
			String codigoAssuntoIncompleto, String codigoAssunto, String siglaClasseProcessual,
			Long numeroProcessual) throws DaoException {

		Session session = retrieveSession();

		List<Assunto> assuntos = null;

		try {

			StringBuffer hql = new StringBuffer(" SELECT a " +
					" FROM Processo p" +
					" JOIN p.assunto a" +
			" WHERE (1=1)	");

			if( codigoAssuntoIncompleto != null ) {
				hql.append(" AND a.codigo LIKE '%"+codigoAssuntoIncompleto+"%'");
			}

			if(codigoAssunto!=null){
				hql.append(" AND a.codigo = "+codigoAssunto);
			}

			if(siglaClasseProcessual != null){
				hql.append(" AND p.siglaClasseProcessual = '"+siglaClasseProcessual+"'");
			}

			if(numeroProcessual != null){            	
				hql.append(" AND p.numeroProcessual = "+numeroProcessual);

			}
			hql.append(" ORDER BY a.codigo ");

			Query q = session.createQuery(hql.toString());
			assuntos = q.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		return assuntos;    	
	}

	private Query createAssuntoHierarquicoCriteria(String codigoAssunto, String descricao, 
			Boolean ativo,Boolean pesquisarInicio, Boolean pesquisarTodosNiveis,Boolean quantidade) throws DaoException {

		Session session = retrieveSession();
		StringBuffer hql = null;
		if( quantidade != null && quantidade )
			hql = new StringBuffer(" SELECT COUNT(a.id) FROM Assunto a ");
		else
			hql = new StringBuffer(" SELECT a FROM Assunto a ");

		hql.append(" WHERE 1=1 ");
		
		if(codigoAssunto!=null){
			hql.append(" AND a.id = "+ codigoAssunto);
		}


		if( descricao != null && descricao.trim().length() >= 3 ) {
			descricao = descricao.replace("|", "\\|");
			descricao = descricao.replace('%', ' ');
			hql.append(" AND contains(a.descricaoCompleta,'"+descricao.trim()+"%') > 1");
		}	


		if( ativo != null ) {
			if( ativo ){
				hql.append(" AND a.ativo = '"+ Flag.SIM+"'");
			}else{
				hql.append(" AND a.ativo = '"+ Flag.NAO+"'");
			}
		}

		if( quantidade == null || !quantidade )
			hql.append(" ORDER BY a.descricaoCompleta");

		Query query = session.createQuery(hql.toString());
		/*if( pesquisarTodosNiveis != null && !pesquisarTodosNiveis.booleanValue() ){
    		if( pesquisarInicio!=null && pesquisarInicio.booleanValue() ){
				if( descricao != null && descricao.trim().length() >= 3 ) {
					criteria.add(Restrictions.ilike("descricao", descricao.trim(), MatchMode.START));*/
		/*
					criteria.add(Restrictions.or(
							Restrictions.ilike("descricao", descricao, MatchMode.START),
							Restrictions.or(
							  Restrictions.ilike("descricaoAvo", descricao, MatchMode.START),
							  Restrictions.ilike("descricaoPai", descricao, MatchMode.START)
							)
					));
		 */
		/*		}
	        }
	        else if(pesquisarInicio!=null&&!pesquisarInicio.booleanValue()){
	        	//descricao = descricao.trim().replaceAll(" ", "%");
	        	criteria.add(Restrictions.ilike("descricao", descricao, MatchMode.ANYWHERE));
	        	//System.out.println(" descricao "+descricao);
	        }
    	}
    	else if( pesquisarTodosNiveis != null && pesquisarTodosNiveis.booleanValue() ){
    		if( pesquisarInicio!=null && pesquisarInicio.booleanValue() ){
				if( descricao != null && descricao.trim().length() >= 3 ) {										
					criteria.add(Restrictions.or(
									Restrictions.ilike("descricao", descricao, MatchMode.START),
									Restrictions.or(
										Restrictions.ilike("descricaoPai", descricao, MatchMode.START),
										Restrictions.ilike("descricaoAvo", descricao, MatchMode.START)
													)
												)
								);					
				}
	        }
	        else if(pesquisarInicio!=null&&!pesquisarInicio.booleanValue()){
	        	//descricao = descricao.trim().replaceAll(" ", "%");
	        	if( descricao != null && descricao.trim().length() >= 3 ) {										
					criteria.add(Restrictions.or(
									Restrictions.ilike("descricao", descricao, MatchMode.ANYWHERE),
									Restrictions.or(
										Restrictions.ilike("descricaoPai", descricao, MatchMode.ANYWHERE),
										Restrictions.ilike("descricaoAvo", descricao, MatchMode.ANYWHERE)
													)
												)
								);					
				}
	        }
    	}*/



		return query;
	}
	
	

	public List<Assunto> pesquisarAssuntoHierarquico(
			String codigoAssunto, String descricao, Boolean ativo, Boolean pesquisarInicio, 
			Short numeroMaximoDeResultados, Boolean pesquisarTodosNiveis) 
			throws DaoException {

		List<Assunto> assuntos = null;

		try {
			Query query = createAssuntoHierarquicoCriteria(codigoAssunto, descricao, ativo, pesquisarInicio, pesquisarTodosNiveis,false);


			if( numeroMaximoDeResultados != null )
				query.setMaxResults(numeroMaximoDeResultados);

			assuntos = query.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return assuntos;    	
	}	

	public Integer pesquisarQuantidadeAssuntoHierarquico(
			String codigoAssunto, String descricao, Boolean ativo,Boolean pesquisarInicio, Boolean pesquisarTodosNiveis) throws DaoException {

		Long quantidade = null;

		try {
			Query q = createAssuntoHierarquicoCriteria(codigoAssunto, descricao, ativo,pesquisarInicio, pesquisarTodosNiveis,true);

			quantidade = (Long) q.uniqueResult();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return quantidade.intValue();  		
	}

	public List<Assunto> pesquisarAssunto(
			String codigoAssunto, String descricao, Boolean ativo, 
			Short numeroMaximoDeResultados) throws DaoException {

		Session session = retrieveSession();

		List<Assunto> assuntos = null;

		try {
			Criteria criteria = session.createCriteria(Assunto.class);

			if(codigoAssunto!=null){
				criteria.add(Restrictions.eq("codigo", codigoAssunto));
			}

			if( descricao != null && descricao.trim().length() >= 3 ) {
				criteria.add(Restrictions.ilike("descricao", descricao, MatchMode.START) );
			}

			if( ativo != null ) {
				criteria.add(Restrictions.eq("ativo", ativo));
			}	    	

			criteria.addOrder(Order.asc("descricao"));

			if( numeroMaximoDeResultados != null )
				criteria.setMaxResults(numeroMaximoDeResultados);

			assuntos = criteria.list();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		return assuntos;    	
	}
	
	public Assunto recuperarAssunto(String codigoAssunto) throws DaoException {
		
		Assunto assuntoRecuperado = null;
		
		Session session = retrieveSession();
		
		try {
			
			Criteria criteria = session.createCriteria(Assunto.class);
			
			criteria.add(Restrictions.eq("codigo", codigoAssunto));
			
			assuntoRecuperado = (Assunto) criteria.uniqueResult();
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch(RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
			
		return assuntoRecuperado;
	}

	private String parseQuery(AssuntoSearchData sd) {
		List<String> strings = new ArrayList<String>();
		for (String string : StringUtils.split(sd.descricaoCompleta)) {
			strings.add(MessageFormat.format("%{0}%", string));
		}
		return StringUtils.join(strings, " AND ");
	}
	
}
