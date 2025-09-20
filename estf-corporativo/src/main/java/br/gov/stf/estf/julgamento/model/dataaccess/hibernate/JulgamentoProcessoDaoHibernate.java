package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.TipoSituacaoProcessoSessao;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoImagem;
import br.gov.stf.estf.entidade.processostf.TipoIncidenteJulgamento;
import br.gov.stf.estf.julgamento.model.dataaccess.JulgamentoProcessoDao;
import br.gov.stf.estf.julgamento.model.util.JulgamentoProcessoSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.SearchData;
import br.gov.stf.framework.util.SearchResult;

@Repository
public class JulgamentoProcessoDaoHibernate extends GenericHibernateDao<JulgamentoProcesso, Long> implements JulgamentoProcessoDao {

	private static final long serialVersionUID = -8009044434836319117L;
	
	public JulgamentoProcessoDaoHibernate() {
		super(JulgamentoProcesso.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<JulgamentoProcesso> pesquisarProcessosSessao(Long idSessao)	throws DaoException {
		Session session = retrieveSession();
		
		try {
			Criteria c = session.createCriteria( JulgamentoProcesso.class, "jp" );
			
			c = c.createAlias("jp.sessao", "s", CriteriaSpecification.INNER_JOIN).setFetchMode("s", FetchMode.JOIN);
			c = c.createAlias("jp.objetoIncidente", "oi", CriteriaSpecification.INNER_JOIN).setFetchMode("oi", FetchMode.JOIN);
			
			if( idSessao != null )
				c.add( Restrictions.eq("s.id", idSessao) );
			
			return c.list();
		} catch ( Exception e ) {
			throw new DaoException( e );
		}
	}
	
	public JulgamentoProcesso recuperarJulgamentoProcesso(Long id,  Long idObjetoIncidente,
			TipoAmbienteConstante tipoAmbienteSessao, String colegiado)
			throws DaoException {
		Session session = retrieveSession();
		JulgamentoProcesso resultado = null;
		try{
			
			Criteria criteria = session.createCriteria( JulgamentoProcesso.class,"jp");
			
			if( id != null )
				criteria.add( Restrictions.eq("jp.id", id) );
			
			if( idObjetoIncidente != null )
				criteria.add( Restrictions.eq("jp.objetoIncidente.id", idObjetoIncidente) );
			
			if( tipoAmbienteSessao != null || (colegiado != null && colegiado.trim().length() > 0) )
				criteria = criteria.createCriteria("jp.sessao", "s", CriteriaSpecification.INNER_JOIN);
			
			if( tipoAmbienteSessao != null )
				criteria.add( Restrictions.eq("s.tipoAmbiente", tipoAmbienteSessao.getSigla()) );
						
			if( colegiado != null && colegiado.trim().length() > 0)
				criteria.add( Restrictions.eq("s.colegiado.id", colegiado) );
			
			resultado = (JulgamentoProcesso) criteria.uniqueResult();
			
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return resultado;
	}
	
	public SearchResult<JulgamentoProcesso> pesquisarJulgamentoProcesso(
			JulgamentoProcessoSearchData searchData)
			throws DaoException {
		Session session = retrieveSession();
		
		try {
			
			StringBuffer hql = new StringBuffer(" FROM JulgamentoProcesso jp  ");
			
			if( searchData.tipoSituacaoJulgamento != null )
				hql.append("INNER JOIN jp.listaSituacaoJulgamento sj");
			
			hql.append(" WHERE (1=1) ");
			
			if( searchData.id != null )
				hql.append(" AND id = :id");
			
			if( SearchData.stringNotEmpty(searchData.siglaClasse) )
				hql.append(" AND jp.processo.siglaClasseProcessual = :siglaClasse");
				
			if( searchData.numeroProcesso != null )
				hql.append(" AND jp.processo.numeroProcessual = :numeroProcesso");
			
			if( searchData.codigoMinistroRelator != null )
				hql.append(" AND jp.processo.ministroRelator.codigo = :codigoMinistroRelator");
			
			if( searchData.numeroSessao != null )
				hql.append(" AND jp.sessao.numeroSessao = :numeroSessao");
			
			if( searchData.anoSessao !=null )
				hql.append(" AND jp.sessao.anoSessao = :anoSessao");
			
			if( searchData.dataInicioSessao  != null )
				hql.append(" AND jp.sessao.dataInicio = :dataInicioSessao");
			
			if( searchData.dataFimSessao != null )
				hql.append(" AND jp.sessao.dataFim = :dataFimSessao");
			
			if( searchData.dataPrevistaInicioSessao != null)
				hql.append(" AND jp.sessao.dataPrevistaInicio = :dataPrevistaInicioSessao");
			
			if( searchData.dataPrevistaFimSessao != null)
				hql.append(" AND jp.sessao.dataPrevistaFim = :dataPrevistaFimSessao");
			
			if( SearchData.hasDate(searchData.dataInicioSessaoDateRange))
				hql.append(" AND jp.sessao.dataInicio >= TO_DATE(:initialDateInicio,'DD/MM/YYYY HH24:MI:SS')"+
			               " AND jp.sessao.dataInicio <= TO_DATE(:finalDateInicio,'DD/MM/YYYY HH24:MI:SS')");
			
			if( SearchData.hasDate(searchData.dataFimSessaoDateRange ))
				hql.append(" AND jp.sessao.dataFim >= TO_DATE(:initialDateFim,'DD/MM/YYYY HH24:MI:SS')"+
	               		   " AND jp.sessao.dataFim <= TO_DATE(:finalDateFim,'DD/MM/YYYY HH24:MI:SS')");
			
			if( SearchData.hasDate(searchData.dataPrevistaFimSessaoDateRange ))
				hql.append(" AND jp.sessao.dataPrevistaFim >= TO_DATE(:initialDatePrevistaFim,'DD/MM/YYYY HH24:MI:SS')"+
        		   		   " AND jp.sessao.dataPrevistaFim <= TO_DATE(:finalDatePrevistaFim,'DD/MM/YYYY HH24:MI:SS')");
			
			if( SearchData.hasDate(searchData.dataPrevistaInicioSessaoDateRange ))
				hql.append(" AND jp.sessao.dataPrevistaInicio >= TO_DATE(:initialDatePrevistaInicio,'DD/MM/YYYY HH24:MI:SS')"+
		   		   		   " AND jp.sessao.dataPrevistaInicio <= TO_DATE(:finalDatePrevistaInicio,'DD/MM/YYYY HH24:MI:SS')");
			
			if( searchData.tipoSessao != null)
				hql.append(" AND jp.sessao.tipoSessao = :tipoSessao");
			
			if( SearchData.stringNotEmpty(searchData.colegiado))
				hql.append(" AND jp.sessao.colegiado.id = :colegiado");
			
			if( searchData.tipoAmbiente != null )
				hql.append(" AND jp.sessao.tipoAmbiente = :tipoAmbiente");
			
			if( searchData.tipoSituacaoJulgamento != null )
				hql.append(" AND sj.tipoSituacaoJulgamento.id = :tipoSituacaoJulgamento" +
						   " AND sj.atual = 'S'");
			
			
			
			//criando query
			Query countQuery = session.createQuery(" SELECT COUNT(jp) " + hql.toString());
			Query collectionQuery = session.createQuery(" SELECT jp " + hql.toString());
			if( searchData.id != null ){
				countQuery.setParameter("id", searchData.id);
                collectionQuery.setParameter("id", searchData.id);
			}
			
			if( SearchData.stringNotEmpty(searchData.siglaClasse) ){
				countQuery.setParameter("siglaClasse", searchData.siglaClasse);
                collectionQuery.setParameter("siglaClasse", searchData.siglaClasse);
			}
			
			if( searchData.numeroProcesso != null ){
				countQuery.setParameter("numeroProcesso", searchData.numeroProcesso);
                collectionQuery.setParameter("numeroProcesso", searchData.numeroProcesso);
			}
			
			if( searchData.codigoMinistroRelator != null ){
				countQuery.setParameter("codigoMinistroRelator", searchData.codigoMinistroRelator);
                collectionQuery.setParameter("codigoMinistroRelator", searchData.codigoMinistroRelator);
			}
			
			if( searchData.numeroSessao != null ){
				countQuery.setParameter("numeroSessao", searchData.numeroSessao);
                collectionQuery.setParameter("numeroSessao", searchData.numeroSessao);
			}
			
			if( searchData.anoSessao !=null ){
				countQuery.setParameter("anoSessao", searchData.anoSessao);
                collectionQuery.setParameter("anoSessao", searchData.anoSessao);
			}
			
			if( searchData.dataInicioSessao  != null ){
				countQuery.setParameter("dataInicioSessao", searchData.dataInicioSessao);
                collectionQuery.setParameter("dataInicioSessao", searchData.dataInicioSessao);
			}
			
			if( searchData.dataFimSessao != null ){
				countQuery.setParameter("dataFimSessao", searchData.dataFimSessao);
                collectionQuery.setParameter("dataFimSessao", searchData.dataFimSessao);
			}
			
			if( searchData.dataPrevistaInicioSessao != null){
				countQuery.setParameter("dataPrevistaInicioSessao", searchData.dataPrevistaInicioSessao);
                collectionQuery.setParameter("dataPrevistaInicioSessao", searchData.dataPrevistaInicioSessao);
			}
			
			if( searchData.dataPrevistaFimSessao != null){
				countQuery.setParameter("dataPrevistaFimSessao", searchData.dataPrevistaFimSessao);
                collectionQuery.setParameter("dataPrevistaFimSessao", searchData.dataPrevistaFimSessao);
			}
			
			if( SearchData.hasDate(searchData.dataInicioSessaoDateRange)){
				countQuery.setParameter("initialDateInicio", searchData.dataInicioSessaoDateRange.getDateHourInitialString());
				collectionQuery.setParameter("initialDateInicio", searchData.dataInicioSessaoDateRange.getDateHourInitialString());
				countQuery.setParameter("finalDateInicio", searchData.dataInicioSessaoDateRange.getDateHourFinalString());
                collectionQuery.setParameter("finalDateInicio", searchData.dataInicioSessaoDateRange.getDateHourFinalString());
			}
			
			if( SearchData.hasDate(searchData.dataFimSessaoDateRange )){
				countQuery.setParameter("initialDateFim", searchData.dataFimSessaoDateRange.getDateHourInitialString());
				collectionQuery.setParameter("initialDateFim", searchData.dataFimSessaoDateRange.getDateHourInitialString());
				countQuery.setParameter("finalDateFim", searchData.dataFimSessaoDateRange.getDateHourFinalString());
				collectionQuery.setParameter("finalDateFim", searchData.dataFimSessaoDateRange.getDateHourFinalString());
			}
			
			if( SearchData.hasDate(searchData.dataPrevistaFimSessaoDateRange )){
				countQuery.setParameter("initialDatePrevistaFim", searchData.dataPrevistaFimSessaoDateRange.getDateHourInitialString());
				collectionQuery.setParameter("initialDatePrevistaFim", searchData.dataPrevistaFimSessaoDateRange.getDateHourInitialString());
				countQuery.setParameter("finalDatePrevistaFim", searchData.dataPrevistaFimSessaoDateRange.getDateHourFinalString());
                collectionQuery.setParameter("finalDatePrevistaFim", searchData.dataPrevistaFimSessaoDateRange.getDateHourFinalString());
			}
			
			if( SearchData.hasDate(searchData.dataPrevistaInicioSessaoDateRange )){
				countQuery.setParameter("initialDatePrevistaInicio", searchData.dataPrevistaInicioSessaoDateRange.getDateHourInitialString());
				collectionQuery.setParameter("initialDatePrevistaInicio", searchData.dataPrevistaInicioSessaoDateRange.getDateHourInitialString());
				countQuery.setParameter("finalDatePrevistaInicio", searchData.dataPrevistaInicioSessaoDateRange.getDateHourFinalString());
                collectionQuery.setParameter("finalDatePrevistaInicio", searchData.dataPrevistaInicioSessaoDateRange.getDateHourFinalString());
			}
			
			if( searchData.tipoSessao != null){
				countQuery.setParameter("tipoSessao", searchData.tipoSessao.getSigla());
                collectionQuery.setParameter("tipoSessao", searchData.tipoSessao);
			}
			
			if( SearchData.stringNotEmpty(searchData.colegiado)){
				countQuery.setParameter("colegiado", searchData.colegiado);
                collectionQuery.setParameter("colegiado", searchData.colegiado);
			}
			
			if( searchData.tipoAmbiente != null ){
				countQuery.setParameter("tipoAmbiente", searchData.tipoAmbiente.getSigla());
                collectionQuery.setParameter("tipoAmbiente", searchData.tipoAmbiente.getSigla());
			}
			
			if( searchData.tipoSituacaoJulgamento != null ){
				countQuery.setParameter("tipoSituacaoJulgamento", searchData.tipoSituacaoJulgamento.getCodigo());
                collectionQuery.setParameter("tipoSituacaoJulgamento", searchData.tipoSituacaoJulgamento.getCodigo());
			}
			
			return pesquisarComPaginacaoQuery(searchData, countQuery, collectionQuery);
			
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<JulgamentoProcesso> pesquisarJulgamentoProcesso(
			Long idOpjetoIncidentePrincipal, Long idMinistro, String... sigTipoRecursos)
			throws DaoException {
		Session session = retrieveSession();

		try {

			StringBuffer hql = new StringBuffer();
			hql.append("SELECT jp ");
			hql.append(" FROM JulgamentoProcesso jp");					   
			hql.append(" WHERE (1=1)");
			
			if( idOpjetoIncidentePrincipal != null )
				hql.append(" AND (jp.objetoIncidente.principal = :idOpjetoIncidentePrincipal)");
								
			
			if( sigTipoRecursos != null && sigTipoRecursos.length > 0 )
				hql.append(" AND (jp.tipoJulgamento in(:sigTipoRecursos))");

			if (idMinistro != null){
				hql.append(" AND not exists(SELECT vjp.id FROM VotoJulgamentoProcesso vjp " +
					" WHERE vjp.ministro.id = :idMinistro" +
					" AND vjp.julgamentoProcesso.id = jp.id) ");
			}
			
			Query q = session.createQuery(hql.toString());
			
			if( idOpjetoIncidentePrincipal != null )
				q.setLong("idOpjetoIncidentePrincipal", idOpjetoIncidentePrincipal);
			
			if (idMinistro != null)
				q.setLong("idMinistro", idMinistro);
			
			if( sigTipoRecursos != null && sigTipoRecursos.length > 0)
				q.setParameterList("sigTipoRecursos", sigTipoRecursos);
			
			
			return q.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}

	
	@SuppressWarnings("unchecked")
	public List<JulgamentoProcesso> pesquisarJulgamentoProcessoRG(Long idOpjetoIncidentePrincipal, Long idMinistro, String... sigTipoRecursos) throws DaoException {
		Session session = retrieveSession();

		try {
			StringBuffer hql = new StringBuffer();
			hql.append("SELECT jp ");
			hql.append(" FROM JulgamentoProcesso jp INNER JOIN FETCH jp.objetoIncidente ");
			hql.append(" WHERE (1=1) ");

			if (idOpjetoIncidentePrincipal != null)
				hql.append(" AND (jp.objetoIncidente.principal = :idOpjetoIncidentePrincipal)");

//			if (sigTipoRecursos != null && sigTipoRecursos.length > 0)
//				hql.append(" AND (jp.tipoJulgamento in(:sigTipoRecursos))");

			if (sigTipoRecursos != null && sigTipoRecursos.length > 0)
				hql.append(" AND (jp.sessao.id in (select jp1.sessao.id from JulgamentoProcesso jp1 where jp1.objetoIncidente.principal = :idOpjetoIncidentePrincipal " + " AND jp1.tipoJulgamento in('"
						+ TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL + "','" + TipoIncidenteJulgamento.SIGLA_QUESTAO_ORDEM + "','" + TipoIncidenteJulgamento.SIGLA_QUESTAO_CONSTITUCIONAL + "', '"
						+ TipoIncidenteJulgamento.SIGLA_REPERCUSSAO_GERAL_SEGUNDO_JULGAMENTO + "', '" + TipoIncidenteJulgamento.SIGLA_QUESTAO_CONSTITUCIONAL_SEGUNDO_JULGAMENTO + "'))) ");

			if (idMinistro != null) {
				hql.append(" AND not exists(SELECT vjp.id FROM VotoJulgamentoProcesso vjp " + " WHERE vjp.ministro.id = :idMinistro" + " AND vjp.julgamentoProcesso.id = jp.id) ");
			}

			Query q = session.createQuery(hql.toString());

			if (idOpjetoIncidentePrincipal != null)
				q.setLong("idOpjetoIncidentePrincipal", idOpjetoIncidentePrincipal);

			if (idMinistro != null)
				q.setLong("idMinistro", idMinistro);

//			if (sigTipoRecursos != null && sigTipoRecursos.length > 0)
//				q.setParameterList("sigTipoRecursos", sigTipoRecursos);

			return q.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}
	
	@SuppressWarnings("unchecked")
	public List<ProcessoImagem> recuperarInteiroTeor(Long idObjetoIncidente,
			Processo processo) throws DaoException {
	
		Session session = retrieveSession();
		
		try {
			
			StringBuffer hql = new StringBuffer();
			
			hql.append("SELECT pimg ");
			hql.append(" FROM ProcessoImagem pimg, ");
			hql.append(" Processo pro, IncidenteJulgamento ij, ");
			hql.append(" ObjetoIncidente oijp, JulgamentoProcesso jp, ");
			hql.append(" ObjetoIncidente oipi ");
			hql.append(" WHERE pimg.objetoIncidente.id = oipi.id ");
			hql.append(" AND oipi.principal.id = pro.id ");
			hql.append(" AND oijp.principal.id = oipi.principal.id ");
			hql.append(" AND jp.objetoIncidente.id = oijp.id ");
			hql.append(" AND jp.objetoIncidente.id = ij.id ");
			hql.append(" AND pro.numeroProcessual = :numeroProcesso ");
			hql.append(" AND pro.siglaClasseProcessual = :siglaClasse ");
			hql.append(" AND pimg.flgLiberado = 'S' ");
			
			Query q = session.createQuery(hql.toString());
			
			q.setLong("numeroProcesso", processo.getNumeroProcessual());
			q.setString("siglaClasse", processo.getSiglaClasseProcessual());
			
			return q.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	public List<ProcessoImagem> recuperarInteiroTeorRG(Long idObjetoIncidente,
			Processo processo) throws DaoException {
		
		Session session = retrieveSession();
		Long id = 0L;
		
		try {
			
			CallableStatement statement = session.connection().prepareCall("{ ? = call internet.pkg_repercussao_geral.fnc_retorna_id_inteiro_teor(?, ?, ?)}");
			statement.registerOutParameter(1, Types.INTEGER);
			statement.setLong(2, processo.getNumeroProcessual());
			statement.setString(3, processo.getSiglaClasseProcessual());
			statement.setString(4, " ");
			statement.execute();
			id = statement.getLong(1);
			
			if( id != 0 ) {
				statement.close();
				
				StringBuffer hql = new StringBuffer();
				
				hql.append("SELECT pimg ");
				hql.append(" FROM ProcessoImagem pimg, ");
				hql.append(" Processo pro, IncidenteJulgamento ij, ");
				hql.append(" ObjetoIncidente oijp, JulgamentoProcesso jp, ");
				hql.append(" ObjetoIncidente oipi ");
				hql.append(" WHERE pimg.objetoIncidente.id = oipi.id ");
				hql.append(" AND oipi.principal.id = pro.id ");
				hql.append(" AND oijp.principal.id = oipi.principal.id ");
				hql.append(" AND jp.objetoIncidente.id = oijp.id ");
				hql.append(" AND jp.objetoIncidente.id = ij.id ");
				hql.append(" AND pro.numeroProcessual = :numeroProcesso ");
				hql.append(" AND pro.siglaClasseProcessual = :siglaClasse ");
				hql.append(" AND pimg.flgLiberado = 'S' ");
				hql.append(" AND pimg.seqInteiroTeor.id = " + id );
				
				Query q = session.createQuery(hql.toString());
				
				q.setLong("numeroProcesso", processo.getNumeroProcessual());
				q.setString("siglaClasse", processo.getSiglaClasseProcessual());
				
				return q.list();
				
				
			} else {
				
				statement = session.connection().prepareCall("{ ? = call internet.pkg_repercussao_geral.fnc_retorna_seq_doc_int_teor(?, ?, ?)}");
				statement.registerOutParameter(1, Types.INTEGER);
				statement.setLong(2, processo.getNumeroProcessual());
				statement.setString(3, processo.getSiglaClasseProcessual());
				statement.setString(4, " ");
				statement.execute();
				id = statement.getLong(1);
				
				if( id != 0 ) {
					statement.close();
					
					StringBuffer hql = new StringBuffer();
					
					hql.append("SELECT pimg ");
					hql.append(" FROM ProcessoImagem pimg, ");
					hql.append(" Processo pro, IncidenteJulgamento ij, ");
					hql.append(" ObjetoIncidente oijp, JulgamentoProcesso jp, ");
					hql.append(" ObjetoIncidente oipi ");
					hql.append(" WHERE pimg.objetoIncidente.id = oipi.id ");
					hql.append(" AND oipi.principal.id = pro.id ");
					hql.append(" AND oijp.principal.id = oipi.principal.id ");
					hql.append(" AND jp.objetoIncidente.id = oijp.id ");
					hql.append(" AND jp.objetoIncidente.id = ij.id ");
					hql.append(" AND pro.numeroProcessual = :numeroProcesso ");
					hql.append(" AND pro.siglaClasseProcessual = :siglaClasse ");
					hql.append(" AND pimg.flgLiberado = 'S' ");
					hql.append(" AND pimg.seqDocumento.id = " + id );
					hql.append(" AND ij.tipoJulgamento.sigla = 'RG' ");
					
					Query q = session.createQuery(hql.toString());
					
					q.setLong("numeroProcesso", processo.getNumeroProcessual());
					q.setString("siglaClasse", processo.getSiglaClasseProcessual());
					
					return q.list();
				}
				
			}
			
			statement.close();
			
			return null;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (Exception e) {
			throw new DaoException("Exception", e);
		}

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VotoJulgamentoProcesso> recuperarVotosProcesso(Long julgamentoProcessoId) throws DaoException {
			Session session = retrieveSession();
		
		Criteria criteria = session.createCriteria( VotoJulgamentoProcesso.class, "vjp");
		criteria.add( Restrictions.eq("vjp.julgamentoProcesso.id", julgamentoProcessoId));
		criteria.add(Restrictions.eq("vjp.tipoSituacaoVoto", "V"));
		criteria.addOrder(Order.asc("vjp.dataVoto"));
		List<VotoJulgamentoProcesso> votos = (List<VotoJulgamentoProcesso>) criteria.list();
		
		return votos;
	}
 
	@Override
	public JulgamentoProcesso recuperar(ObjetoIncidente<?> objetoIncidente,
			Sessao sessao) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(JulgamentoProcesso.class, "jp");
			c.createAlias("jp.sessao", "s");
			if (objetoIncidente != null) {
				c.add(Restrictions.eq("jp.objetoIncidente.id", objetoIncidente.getId()));
			}
			if (sessao != null) {
				c.add(Restrictions.eq("s.id", sessao.getId()));
			}
			return (JulgamentoProcesso) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public JulgamentoProcesso recuperar(ObjetoIncidente<?> objetoIncidente,
			Sessao sessao, Date dataBase) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(JulgamentoProcesso.class, "jp");
			c.createAlias("jp.sessao", "s");
			if (objetoIncidente != null) {
				c.add(Restrictions.eq("jp.objetoIncidente.id", objetoIncidente.getId()));
			}
			if (sessao != null) {
				c.add(Restrictions.eq("s.id", sessao.getId()));
			}
			if (dataBase != null) {
				c.add(Restrictions.ge("s.dataInicio", DateUtils.truncate(dataBase, Calendar.DATE)));
			}
			return (JulgamentoProcesso) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JulgamentoProcesso> pesquisarJulgamentoProcessoNaoJulgado(ObjetoIncidente<?> objetoIncidente, Sessao sessao)
			throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(JulgamentoProcesso.class, "jp");
			c.createAlias("jp.sessao", "s");
			c.add(Restrictions.eq("jp.objetoIncidente.id", objetoIncidente.getId()));
			c.add(Restrictions.ne("s.id", sessao.getId()));
			c.add(Restrictions.isNull("s.dataFim"));
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public JulgamentoProcesso pesquisaSessaoNaoFinalizada(
			ObjetoIncidente<?> objetoIncidente,
			TipoAmbienteConstante tipoAmbiente) throws DaoException {
		try { 
			Session session = retrieveSession();
			Criteria c = session.createCriteria(JulgamentoProcesso.class);
			c.createAlias("sessao", "s");
			c.add(Restrictions.eq("objetoIncidente.id", objetoIncidente.getId()));
			c.add(Restrictions.eq("s.tipoAmbiente", tipoAmbiente.getSigla()));
			c.add(Restrictions.isNull("s.dataFim"));
			return (JulgamentoProcesso) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);	
		}
	}

	@Override
	public Integer recuperarUltimaOrdemSessao(Sessao sessao)
			throws DaoException {
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer(" SELECT max(jp.ordemSessao) FROM JulgamentoProcesso jp ");
			hql.append(" WHERE jp.sessao.id = :idSessao ");
			Query q = session.createQuery(hql.toString());
			q.setLong("idSessao", sessao.getId());
			return (Integer) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void atualizarOrdenacaoProcessos(
			JulgamentoProcesso julgamentoProcessoBase, Integer ordemDestino) throws DaoException {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE julgamento.julgamento_processo jp ");
			if (ordemDestino == null || julgamentoProcessoBase.getOrdemSessao() < ordemDestino) {
				sql.append(" SET jp.num_ordem_sessao = jp.num_ordem_sessao - 1 ");
				sql.append(" WHERE jp.seq_sessao = :idSessao ");
				sql.append(" AND jp.num_ordem_sessao > :ordemBase ");
				if (ordemDestino != null) {
					sql.append(" AND jp.num_ordem_sessao <= :ordemDestino ");
				}
			} else if (julgamentoProcessoBase.getOrdemSessao() > ordemDestino){
				sql.append(" SET jp.num_ordem_sessao = jp.num_ordem_sessao + 1 ");
				sql.append(" WHERE jp.seq_sessao = :idSessao ");
				sql.append(" AND jp.num_ordem_sessao >= :ordemDestino ");
				sql.append(" AND jp.num_ordem_sessao < :ordemBase ");
			}
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery( sql.toString() );
			sqlQuery.setLong("idSessao", julgamentoProcessoBase.getSessao().getId());
			if (julgamentoProcessoBase.getOrdemSessao() != null){
				sqlQuery.setInteger("ordemBase", julgamentoProcessoBase.getOrdemSessao());}
			else{
				sqlQuery.setInteger("ordemBase", 9999);
			}
			if (ordemDestino != null) {
				sqlQuery.setInteger("ordemDestino", ordemDestino);
			}
			
			sqlQuery.executeUpdate();
		} catch(Exception e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public void refresh(JulgamentoProcesso julgamentoProcesso) throws DaoException {
		retrieveSession().refresh(julgamentoProcesso);
	}
	
	@Override
	public List<JulgamentoProcesso> pesquisarJulgamentoProcessoListaJulgamento(ListaJulgamento listaJulgamento) throws DaoException {
		
		Session session = retrieveSession();
		
		try {
			
			StringBuffer hql = new StringBuffer(" FROM JulgamentoProcesso jp  ");
			
			hql.append(" JOIN jp.sessao s ");
			hql.append(" , ListaJulgamento lj ");
			hql.append(" , ProcessoListaJulgamento pl ");
			
			hql.append(" WHERE (1=1) ");			
			hql.append(" and lj.sessao.id =  s.id");
			hql.append(" and pl.listaJulgamento.id =  lj.id");
			hql.append(" and pl.objetoIncidente.id =  jp.objetoIncidente.id");
			hql.append(" and lj.id = :id");
			
			
			Query countQuery = session.createQuery(" SELECT COUNT(jp) " + hql.toString());
			Query collectionQuery = session.createQuery(" SELECT jp " + hql.toString());
			
			countQuery.setParameter("id", listaJulgamento.getId());            
			collectionQuery.setParameter("id", listaJulgamento.getId());
			
			JulgamentoProcessoSearchData searchData = new JulgamentoProcessoSearchData();
			
			return (List<JulgamentoProcesso>)pesquisarComPaginacaoQuery(searchData, countQuery, collectionQuery).getResultCollection();
			
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}
	
	public JulgamentoProcesso pesquisaUltimoJulgamentoProcesso(ObjetoIncidente<?> oi, Ministro ministroDestaque, TipoSituacaoProcessoSessao situacaoProcessoSessao, Boolean destaqueCancelado) throws DaoException{

		try { 
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer(" FROM JulgamentoProcesso jp1 WHERE jp1.id = ");
			hql.append(" (SELECT max(jp.id) FROM JulgamentoProcesso jp, Sessao s");
			hql.append(" WHERE jp.sessao.id = s.id ");
			hql.append(" AND jp.objetoIncidente = :idObjetoIncidente ");
			
			if (ministroDestaque != null)
				hql.append(" AND jp.ministroDestaque = :ministroDestaque ");
			
			if (situacaoProcessoSessao != null)
				hql.append(" AND jp.situacaoProcessoSessao = :situacaoProcessoSessao");
			
			if (destaqueCancelado != null)
				hql.append(" AND jp.destaqueCancelado = :destaqueCancelado");
			
			hql.append(")");
			Query q = session.createQuery(hql.toString());
			q.setParameter("idObjetoIncidente", oi);
			
			if (ministroDestaque != null)
				q.setParameter("ministroDestaque", ministroDestaque);
			
			if (situacaoProcessoSessao != null)
				q.setParameter("situacaoProcessoSessao", situacaoProcessoSessao.getSigla());
			
			if (destaqueCancelado != null)
				if (Boolean.TRUE.equals(destaqueCancelado))
					q.setParameter("destaqueCancelado", "S");
				else
					q.setParameter("destaqueCancelado", "N");
						
			
			return  (JulgamentoProcesso) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);	
		}
	}
	
	public JulgamentoProcesso clonarJulgamentoProcesso(Long julgamentoProcessoId, Long idSessao, Boolean clonarRascunhos) throws DaoException{
		
		Session session = retrieveSession();

		//Clona o registro de JULGAMENTO_PROCESSO
		Long novoSeqJulgamentoProcesso = clonaRegsitroJulgamentoProcesso(julgamentoProcessoId, idSessao);
		
		//Recupera os votos atuais para serem clonados
		List<VotoJulgamentoProcesso> listaVotosAtuais = recuperarVotosAtuais(julgamentoProcessoId);
		
		//Clona um � um, mantendo um De -> Para para sabermos qual voto foi clonado para qual. 
		List<VotoDePara> listaVotosDePara = new ArrayList<VotoDePara>();
		for (VotoJulgamentoProcesso votoAtual : listaVotosAtuais) {
		
			if (!votoAtual.isRascunho() || Boolean.TRUE.equals(clonarRascunhos)) {
				Long votoProximaSeq = getProximaChavePrimaria();
				listaVotosDePara.add(new VotoDePara(votoAtual.getId(), votoProximaSeq));
	
				//Insere um novo registro baseado no votoAtual mudando o apontamento de JulgamentoProcesso
				clonaRegistroVotoJulgamentoProcesso(votoProximaSeq, novoSeqJulgamentoProcesso, votoAtual.getId());
			}
		}
		
		
		//Recupera os novos votos para atualizar a SEQ_VOTO_JULGAMENTO_PROC_VINC
		List<VotoJulgamentoProcesso> votosClonadosComVinculo = recuperarVotoJulgamentoProcessoComVinculo(novoSeqJulgamentoProcesso);
		for (VotoJulgamentoProcesso votoClonadoComVinculo : votosClonadosComVinculo) {
			
			//Busca o de -> para do vinculo do voto clonado
			Long seqVotoClonado = null; 
			for (VotoDePara votoDePara : listaVotosDePara) {
				if (votoDePara.getSeqVotoAtual().equals(votoClonadoComVinculo.getVotoAcompanhado().getId()))
					seqVotoClonado = votoDePara.getSeqVotoClonado(); 	
			}
			
			atualizarVotoClonado(seqVotoClonado, votoClonadoComVinculo.getId());
		}
		
		
		return this.findById(novoSeqJulgamentoProcesso, false);
	}
	
	
	private Long clonaRegsitroJulgamentoProcesso(Long julgamentoProcessoId, Long idSessao) throws DaoException{
		
		Session session = retrieveSession();
		
		//Clona a JULGAMENTO_PROCESSO, VOTO_JULGAMENTO_PROCESSO E SITUACAO_PROCESSO,
		String seqSql = "SELECT JULGAMENTO.SEQ_JULGAMENTO_PROCESSO.NEXTVAL FROM DUAL";
		SQLQuery query = session.createSQLQuery(seqSql);
		Long novoSeqJulgamentoProcesso = ((BigDecimal) query.uniqueResult()).longValue();
		
		StringBuffer queryString = new StringBuffer();
		queryString.append("INSERT INTO JULGAMENTO.JULGAMENTO_PROCESSO (SEQ_JULGAMENTO_PROCESSO, NUM_PROCESSO, COD_RECURSO, SEQ_SESSAO, SIG_CLASSE_PROCES, ");
		queryString.append("                                            TIPO_JULGAMENTO, SEQ_ANDAMENTO_PROCESSO, SEQ_OBJETO_INCIDENTE, NUM_ORDEM_SESSAO, ");
		queryString.append("                                            TIP_SITUACAO_PROC_SESSAO, TXT_OBSERVACAO, SEQ_PROCESSO_PUBLICADOS, COD_MINISTRO_DESTAQUE, ");
		queryString.append("                                            COD_MINISTRO_VISTA) ");
		queryString.append("            SELECT :novoSeqJulgamentoProcesso, NUM_PROCESSO, COD_RECURSO, SEQ_SESSAO, SIG_CLASSE_PROCES, ");
		queryString.append("                   TIPO_JULGAMENTO, SEQ_ANDAMENTO_PROCESSO, SEQ_OBJETO_INCIDENTE, NUM_ORDEM_SESSAO, ");
		queryString.append("                   'N', NULL, SEQ_PROCESSO_PUBLICADOS, NULL, ");
		queryString.append("                   NULL ");
		queryString.append("           FROM JULGAMENTO.JULGAMENTO_PROCESSO WHERE SEQ_JULGAMENTO_PROCESSO = :julgamentoProcessoId ");
		SQLQuery sqlQuery = session.createSQLQuery(queryString.toString());
		sqlQuery.setParameter("novoSeqJulgamentoProcesso", novoSeqJulgamentoProcesso);
		sqlQuery.setParameter("julgamentoProcessoId", julgamentoProcessoId);
		sqlQuery.executeUpdate();
		
		String queryUpdate = "UPDATE JULGAMENTO.JULGAMENTO_PROCESSO SET SEQ_SESSAO = :idSessao WHERE SEQ_JULGAMENTO_PROCESSO = :novoSeqJulgamentoProcesso";
		SQLQuery sqlQueryUpdate = session.createSQLQuery(queryUpdate.toString());
		sqlQueryUpdate.setParameter("idSessao", idSessao);
		sqlQueryUpdate.setParameter("novoSeqJulgamentoProcesso", novoSeqJulgamentoProcesso);
		sqlQueryUpdate.executeUpdate();
		
		return novoSeqJulgamentoProcesso;
				
	}
	
	private List<VotoJulgamentoProcesso> recuperarVotosAtuais(Long julgamentoProcessoId) throws DaoException{
		
		Session session = retrieveSession();
		
		Criteria criteria = session.createCriteria( VotoJulgamentoProcesso.class, "vjp");
		criteria.add( Restrictions.eq("vjp.julgamentoProcesso.id", julgamentoProcessoId) );
		List<VotoJulgamentoProcesso> listaVotosAtuais = (List<VotoJulgamentoProcesso>) criteria.list();
		
		return listaVotosAtuais;
	}
	
	private Long getProximaChavePrimaria() throws DaoException{
		Session session = retrieveSession();
		String seqVotoSql = "SELECT JULGAMENTO.SEQ_VOTO_JULGAMENTO_PROCESSO.NEXTVAL FROM DUAL";
		SQLQuery querySeqVoto = session.createSQLQuery(seqVotoSql);
		Long novoSeqVoto = ((BigDecimal) querySeqVoto.uniqueResult()).longValue();
		return novoSeqVoto;
	}
	
	private void clonaRegistroVotoJulgamentoProcesso(Long votoProximaSeq, Long novoSeqJulgamentoProcesso, Long votoAtual) throws DaoException{
		Session session = retrieveSession();
		StringBuffer votoClonadoBuffer = new StringBuffer();
		votoClonadoBuffer.append("INSERT INTO JULGAMENTO.VOTO_JULGAMENTO_PROCESSO (SEQ_VOTO_JULGAMENTO_PROCESSO, SEQ_JULGAMENTO_PROCESSO, COD_MINISTRO, ");
		votoClonadoBuffer.append("                                                 SEQ_TIPO_VOTO_JULGAMENTO, NUM_ORDEM_VOTO_SESSAO, TIP_SITUACAO_VOTO, DAT_VOTO, ");
		votoClonadoBuffer.append("                                                 TXT_OBSERVACAO, SEQ_VOTO_JULGAMENTO_PROC_VINC, COD_TIPO_VOTO) ");
		votoClonadoBuffer.append("            SELECT :novoSeqVoto, :novoSeqJulgamentoProcesso, COD_MINISTRO, SEQ_TIPO_VOTO_JULGAMENTO, ");
		votoClonadoBuffer.append("                   NUM_ORDEM_VOTO_SESSAO, TIP_SITUACAO_VOTO, DAT_VOTO, TXT_OBSERVACAO, SEQ_VOTO_JULGAMENTO_PROC_VINC, COD_TIPO_VOTO ");
		votoClonadoBuffer.append("            FROM JULGAMENTO.VOTO_JULGAMENTO_PROCESSO WHERE SEQ_VOTO_JULGAMENTO_PROCESSO = :seqVotoAtual");
		
		SQLQuery sqlVotosInsert = session.createSQLQuery(votoClonadoBuffer.toString());
		sqlVotosInsert.setParameter("novoSeqVoto", votoProximaSeq);
		sqlVotosInsert.setParameter("novoSeqJulgamentoProcesso", novoSeqJulgamentoProcesso);
		sqlVotosInsert.setParameter("seqVotoAtual", votoAtual);
		sqlVotosInsert.executeUpdate();
	}
	
	private List<VotoJulgamentoProcesso> recuperarVotoJulgamentoProcessoComVinculo(Long novoSeqJulgamentoProcesso) throws DaoException{
		Session session = retrieveSession();
		
		Criteria criteria = session.createCriteria( VotoJulgamentoProcesso.class, "vjp");
		criteria.add( Restrictions.eq("vjp.julgamentoProcesso.id", novoSeqJulgamentoProcesso) );
		criteria.add( Restrictions.isNotNull("vjp.votoAcompanhado") );
		List<VotoJulgamentoProcesso> listaVotosClonados = (List<VotoJulgamentoProcesso>) criteria.list();
		
		return listaVotosClonados;
	}
	
	private void atualizarVotoClonado(Long seqVotoClonado, Long seqVotoClonadoComVinculo) throws DaoException{
		Session session = retrieveSession();
		String queryVotoClonadoUpdate = "UPDATE JULGAMENTO.VOTO_JULGAMENTO_PROCESSO SET SEQ_VOTO_JULGAMENTO_PROC_VINC = :seqVotoClonado WHERE SEQ_VOTO_JULGAMENTO_PROCESSO = :seqVotoClonadoComVinculo";
		SQLQuery sqlQueryVotoClonadoUpdate = session.createSQLQuery(queryVotoClonadoUpdate);
		sqlQueryVotoClonadoUpdate.setParameter("seqVotoClonadoComVinculo", seqVotoClonadoComVinculo);
		sqlQueryVotoClonadoUpdate.setParameter("seqVotoClonado", seqVotoClonado);
		sqlQueryVotoClonadoUpdate.executeUpdate();
	}
	
	private class VotoDePara{
		private Long seqVotoAtual;
		private Long seqVotoClonado;
		
		public VotoDePara(Long seqVotoAtual, Long seqVotoClonado) {
			this.seqVotoAtual = seqVotoAtual;
			this.seqVotoClonado = seqVotoClonado;
		}
		public Long getSeqVotoAtual() {
			return seqVotoAtual;
		}
		public void setSeqVotoAtual(Long seqVotoAtual) {
			this.seqVotoAtual = seqVotoAtual;
		}
		public Long getSeqVotoClonado() {
			return seqVotoClonado;
		}
		public void setSeqVotoClonado(Long seqVotoClonado) {
			this.seqVotoClonado = seqVotoClonado;
		}
		
	}
}
