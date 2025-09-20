package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.ministro.Ocorrencia;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.SituacaoMinistroProcesso;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoDao;
import br.gov.stf.estf.processostf.model.util.ProcessoSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.SearchResult;

@Repository
public class ProcessoDaoHibernate extends GenericHibernateDao<Processo, Long> implements ProcessoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4500258460084749041L;

	public ProcessoDaoHibernate() {
		super(Processo.class);
	}

	public Processo recuperarProcesso(String classeProcessual, Long numeroProcesso) throws DaoException {

		Processo resp = null;

		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Processo.class);

			if (classeProcessual != null && classeProcessual.trim().length() > 0) {
				c.add(Restrictions.eq("classeProcessual.id", classeProcessual).ignoreCase());
			}

			if (numeroProcesso != null) {
				c.add(Restrictions.eq("numeroProcessual", numeroProcesso));
			}

			resp = (Processo) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return resp;
	}

	@SuppressWarnings("unchecked")
	public List<Processo> pesquisarProcessos(Long numero, Boolean isFisico) throws DaoException {

		Session session = retrieveSession();

		try {
			Criteria criteria = session.createCriteria(Processo.class, "p");

			criteria.add(Restrictions.eq("p.numeroProcessual", numero));
			
			if (isFisico != null) {
				if (isFisico) {
					criteria.add(Restrictions.eq("p.tipoMeioProcesso", TipoMeioProcesso.FISICO));
				} else {
					criteria.add(Restrictions.eq("p.tipoMeioProcesso", TipoMeioProcesso.ELETRONICO));
				}
			}

			return (List<Processo>) criteria.list();

		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Processo> pesquisarProcessos(Long numero) throws DaoException {

		Session session = retrieveSession();

		try {
			Criteria criteria = session.createCriteria(Processo.class);

			criteria.add(Restrictions.eq("numeroProcessual", numero));

			return (List<Processo>) criteria.list();

		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	public Processo recuperarProcessoSTF(String sigla, Long numero) throws DaoException {

		Session session = retrieveSession();

		Processo processoSTF = null;

		try {
			/*
			 * Processo processo = new Processo();
			 * processo.setNumeroProcessual(numero); Classe classe = new
			 * Classe(); classe.setId(sigla);
			 * processo.setClasseProcessual(classe);
			 */
			Criteria criteria = session.createCriteria(Processo.class);

			criteria.add(Restrictions.eq("numeroProcessual", numero));

			criteria.add(Restrictions.eq("classeProcessual.id", sigla));

			// criteria.add(Restrictions.idEq(processoId));

			processoSTF = (Processo) criteria.uniqueResult();

		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return processoSTF;
	}

	public Processo recuperarProcessoSTF(Long numeroProtocolo, Short anoProtocolo) throws DaoException {

		Session session = retrieveSession();
		Processo processoSTF = null;
		StringBuffer hql = new StringBuffer();

		try {

			hql.append(" SELECT pr FROM Processo pr, ObjetoIncidente oi, Protocolo pt WHERE ");
			hql.append(" pr.id = oi.id ");
			hql.append(" AND oi.anterior.id = pt.id ");
			hql.append(" AND oi.tipoObjetoIncidente = 'PR' ");
			
			if( numeroProtocolo != null && numeroProtocolo > 0 )
				hql.append(" AND pt.numeroProtocolo = :numeroProtocolo ");
			
			if( anoProtocolo != null && anoProtocolo > 0 )
				hql.append(" AND pt.anoProtocolo = :anoProtocolo ");
			
			Query q = session.createQuery(hql.toString());
			
			if( numeroProtocolo != null && numeroProtocolo > 0 )
				q.setLong("numeroProtocolo", numeroProtocolo);
			
			if( anoProtocolo != null && anoProtocolo > 0 )
				q.setShort("anoProtocolo", anoProtocolo);
			
			processoSTF = (Processo) q.uniqueResult();

		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return processoSTF;
	}

	public Processo recuperarProcessoSTF(Long codigoOrigem, String siglaClasseProcedencia, String numeroProcessoProcedencia)
			throws DaoException {
		Session session = retrieveSession();

		Processo processoSTF = null;

		try {

			Criteria criteria = session.createCriteria(Processo.class);
			if (codigoOrigem != null)
				criteria.add(Restrictions.eq("origem.id", codigoOrigem));
			if (siglaClasseProcedencia != null)
				criteria.add(Restrictions.eq("siglaClasseProcedencia", siglaClasseProcedencia));
			if (numeroProcessoProcedencia != null)
				criteria.add(Restrictions.eq("numeroProcessoProcedencia", numeroProcessoProcedencia));

			/*
			 * criteria.add(Restrictions.eq("siglaClasse", sigla));
			 * criteria.add(Restrictions.eq("numero", numero));
			 */

			processoSTF = (Processo) criteria.uniqueResult();

			// Query query = session.createQuery();
			// List result = query.list();
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return processoSTF;
	}
	
	public SituacaoMinistroProcesso recuperarDataDistribuicaoSTF(Long seqObjetoIncidente, String sigla, Long numeroProcesso, Long codigoMinistroRelator)
			throws DaoException {

		SituacaoMinistroProcesso dataDistribuicaoSTF = null;
		Session session = retrieveSession();

		try {

			StringBuffer hql = new StringBuffer();

			hql.append(" FROM SituacaoMinistroProcesso smp ");
			hql.append(" WHERE ");
//			hql.append(" smp.ocorrencia IN ('RE' , 'SU' , 'RG') ");
			hql.append(" smp.ocorrencia IN ( :ocorrencia ) ");
			hql.append(" AND smp.relatorAtual = :sim ");
			
			if( seqObjetoIncidente != null && seqObjetoIncidente > 0 )
				hql.append(" AND smp.objetoIncidente.id = :seqObjetoIncidente ");

//			if (codigoMinistroRelator != null)
//				hql.append(" AND smp.ministroRelator.id = " + codigoMinistroRelator);
				
//			if (sigla != null && sigla.trim().length() > 0)
//				hql.append(" AND ps.siglaClasseProcessual = '" + sigla + "' ");
//
//			if (numeroProcesso != null)
//				hql.append(" AND ps.numeroProcessual = " + numeroProcesso);

//			hql.append(") ");

			Query query = (Query) session.createQuery(hql.toString());
			
//			query.setParameterList("ocorrencia", new String[]{Ocorrencia.RELATOR.getCodigo(), Ocorrencia.RELATOR_SUBSTITUTO.getCodigo(), Ocorrencia.REGISTRADO.getCodigo()});
			query.setParameterList("ocorrencia", new Ocorrencia[]{Ocorrencia.RELATOR, Ocorrencia.RELATOR_SUBSTITUTO, Ocorrencia.REGISTRADO});
			query.setString("sim", "S");
			
			if( seqObjetoIncidente != null && seqObjetoIncidente > 0 )
				query.setLong("seqObjetoIncidente", seqObjetoIncidente);
			
			dataDistribuicaoSTF = (SituacaoMinistroProcesso) query.uniqueResult();
			
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return dataDistribuicaoSTF;
	}

	@SuppressWarnings("unchecked")
	public List<SituacaoMinistroProcesso> pesquisarSituacaoMinistroProcesso(Processo processo) throws DaoException {

		if (processo.getNumeroProcessual() == null) {
			throw new DaoException("O número do processo não pode ser nulo.");
		}

		if (processo.getClasseProcessual().getId() == null || processo.getClasseProcessual().getId().length() <= 0) {
			throw new DaoException("A classe do processo não pode ser nula.");
		}

		List<SituacaoMinistroProcesso> listaSituacaoMinistroProcesso = null;

		Session sessao = retrieveSession();
		try {
			StringBuffer hql = new StringBuffer();

			hql.append(" SELECT DISTINCT smp FROM SituacaoMinistroProcesso smp ");
			hql.append(" WHERE 1=1 ");
			hql.append(" AND smp.numeroProcessual = " + processo.getNumeroProcessual());
			hql.append(" AND smp.siglaClasseProcessual = '" + processo.getClasseProcessual().getId() + "'");
			hql.append(" AND smp.codigoOcorrencia IN ('RE', 'SU', 'RG') ");

			hql.append(" ORDER BY smp.dataOcorrencia desc ");

			Query query = sessao.createQuery(hql.toString());
			listaSituacaoMinistroProcesso = query.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", e);
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return listaSituacaoMinistroProcesso;

	}
	
	@Override
	public Ministro pesquisarRelatorAtual(ObjetoIncidente objetoIncidente) throws DaoException {

		List<SituacaoMinistroProcesso> listaSituacaoMinistroProcesso = null;

		Session sessao = retrieveSession();
		try {
			StringBuffer hql = new StringBuffer();

			hql.append(" SELECT DISTINCT smp FROM SituacaoMinistroProcesso smp ");
			hql.append(" WHERE 1=1 ");
			hql.append(" AND smp.objetoIncidente.id = '" + objetoIncidente.getId() + "'");
			hql.append(" AND smp.ocorrencia IN ('RE', 'SU', 'RG') ");
			hql.append(" ORDER BY smp.dataOcorrencia desc ");

			Query query = sessao.createQuery(hql.toString());
			listaSituacaoMinistroProcesso = query.list();
			if ( listaSituacaoMinistroProcesso != null && ! listaSituacaoMinistroProcesso.isEmpty())
				return listaSituacaoMinistroProcesso.get(0).getMinistroRelator();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", e);
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return null;
	}

	public SearchResult<Processo> pesquisarProcesso(ProcessoSearchData processoSearchData) throws DaoException {
		Criteria criteria = retrieveSession().createCriteria(Processo.class);
			if(processoSearchData.siglaClasseProcesso != null){
				criteria.add(Restrictions.eq("classeProcessual.id", processoSearchData.siglaClasseProcesso));
			}
			if(processoSearchData.numProcesso != null){
				criteria.add(Restrictions.eq("numeroProcessual", processoSearchData.numProcesso));
			}
			if(processoSearchData.baixa != null){
				criteria.add(Restrictions.eq("baixa", processoSearchData.baixa));
			}
		
//			Session session = retrieveSession();
//		
//			StringBuffer hql = new StringBuffer("SELECT p");
//			
//			StringBuffer hqlCount = new StringBuffer("SELECT count(p)");
//		
//			StringBuffer hqlFrom = new StringBuffer();
//			hqlFrom.append(" FROM Processo p, SituacaoMinistroProcesso smp WHERE");
//			hqlFrom.append(" smp.objetoIncidente.principal.id = p.id");
//			if(processoSearchData.siglaClasseProcesso != null){
//				hqlFrom.append(" AND p.classeProcessual.id = :siglaClasseProcesso");
//			}
//			if(processoSearchData.numProcesso != null){
//				hqlFrom.append(" AND p.numeroProcessual = :numProcesso");
//			}
//			if(processoSearchData.baixa != null){
//				hqlFrom.append(" AND p.baixa = :baixa");
//			}
//			
//			hqlCount.append(hqlFrom);
//			hqlCount.append(" group by p");
//			
//			Query query =  session.createQuery(hql.append(hqlFrom).toString());
//			Query queryCount = session.createQuery(hqlCount.toString());
//			
//			if(processoSearchData.siglaClasseProcesso != null){
//				query.setString("siglaClasseProcesso", processoSearchData.siglaClasseProcesso);
//				queryCount.setString("siglaClasseProcesso", processoSearchData.siglaClasseProcesso);
//			}
//			if(processoSearchData.numProcesso != null){
//				query.setLong("numProcesso", processoSearchData.numProcesso);
//				queryCount.setLong("numProcesso", processoSearchData.numProcesso);
//			}
//			if(processoSearchData.baixa != null){				
//				query.setString("baixa", processoSearchData.baixa?"S":"N");
//				queryCount.setString("baixa", processoSearchData.baixa?"S":"N");
//			}
			
//		return pesquisarComPaginacaoQuery(processoSearchData, queryCount, query);
			
		return pesquisarComPaginacaoCriteria(processoSearchData, criteria);
	}
	
	public Processo recuperar(Peticao peticao) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		Processo processo = null;
		
		try {
			
			hql.append(" SELECT p FROM Processo p, ObjetoIncidente oi ");
			hql.append(" WHERE ");
			hql.append(" oi.principal.id = p.id ");
			hql.append(" AND EXISTS (SELECT ps FROM PeticaoSetor ps WHERE ps.peticao.id = oi.anterior.id ");
		        	
        	if( peticao != null && peticao.getId() > 0 )
        		hql.append(" AND ps.peticao.id = :id ) ");
        	
        	Query q = session.createQuery(hql.toString());
        	
        	if( peticao != null && peticao.getId() > 0 )
        		q.setLong("id", peticao.getId());
        	
        	processo = (Processo) q.uniqueResult();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
		return processo;
	}



	public void alterarBaixaProcesso(Processo processo) throws DaoException {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE judiciario.processo p");
			sql.append("   SET p.flg_baixa = :baixa");
			sql.append(" WHERE p.seq_objeto_incidente = :processo");
			
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery( sql.toString() );
			sqlQuery.setString("baixa","N");
			sqlQuery.setLong("processo", processo.getId());
			
			sqlQuery.executeUpdate();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
	}
	
	public void alterarBaixaProcesso(Processo processo, Boolean flag) throws DaoException {
			try {
				StringBuffer sql = new StringBuffer();
				sql.append("UPDATE judiciario.processo p");
				sql.append("   SET p.flg_baixa = :baixa");
				sql.append(" WHERE p.seq_objeto_incidente = :processo");
				
				SQLQuery sqlQuery = retrieveSession().createSQLQuery( sql.toString() );
				if (flag) {
					sqlQuery.setString("baixa","S");
				} else {
					sqlQuery.setString("baixa","N");
				}
				sqlQuery.setLong("processo", processo.getId());
				
				sqlQuery.executeUpdate();
				
				retrieveSession().clear();

			} catch ( Exception e ) {
				throw new DaoException(e);
			}
		
	}

	@SuppressWarnings("rawtypes")
	public boolean isProcessoFindo(Processo processo) throws DaoException {
		
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT COUNT (1) ");
			sql.append("FROM stf.andamento_processos ap ");
			sql.append("WHERE ((cod_andamento = 7106) or (cod_andamento = 2309)) ");
			sql.append("and sig_classe_proces = :classeProcesso ");
			sql.append("and num_processo = :numeroProcesso ");
			sql.append("AND NOT EXISTS ( ");
			sql.append("SELECT 1 ");
			sql.append("FROM stf.andamento_processos ap2 ");
			sql.append("WHERE ap2.num_seq_errado = ap.num_sequencia ");
			sql.append("AND ap2.seq_objeto_incidente = ap.seq_objeto_incidente) ");
			sql.append("GROUP BY sig_classe_proces, num_processo");
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery( sql.toString() );
			sqlQuery.setString("classeProcesso", processo.getClasseProcessual().getId());
			sqlQuery.setLong("numeroProcesso", processo.getNumeroProcessual());
			
			List lista = sqlQuery.list();
			
			return lista.size() == 0 ? false : ((BigDecimal)lista.get(0)).intValue() > 0;
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
	}

	@Override
	public boolean isProcessoDistribuido(Processo processo) throws DaoException {
		
		Session session = retrieveSession();
		
		String sql = "select count(1) from stf.sit_min_processos s where s.sig_classe_proces = :siglaProcesso and s.num_processo = :numeroProcesso and " +
				"s.cod_ocorrencia = :codigoOcorrencia";
		
		Query query = session.createSQLQuery(sql);
		
		query.setString("siglaProcesso", processo.getSiglaClasseProcessual());
		query.setLong("numeroProcesso", processo.getNumeroProcessual());
		query.setString("codigoOcorrencia", Ocorrencia.RELATOR.getCodigo());
		
		Number number = (Number) query.uniqueResult();
		
		return number != null && number.intValue() > 0;
	}

	// verifica se o processo está bloqueado para baixa segunda a regra:
	// se a flag no banco for null, então não está bloqueado
	// se a flag for "S" está; se a flag for "N" não está.
	@Override
	public boolean isBloqueadoBaixa(Processo processo) throws DaoException {
		Session session = retrieveSession();
		String sql = "select p.flg_baixa from judiciario.processo p " +
				     "where p.sig_classe_proces = :siglaProcesso and p.num_processo = :numeroProcesso ";
		
		Query query = session.createSQLQuery(sql);
		
		query.setString("siglaProcesso", processo.getSiglaClasseProcessual());
		query.setLong("numeroProcesso", processo.getNumeroProcessual());
		
		String flagBaixa = (String) query.uniqueResult();

		if (flagBaixa == null) {
			return false;
		} 
		return flagBaixa.equals("N");
	}

}
