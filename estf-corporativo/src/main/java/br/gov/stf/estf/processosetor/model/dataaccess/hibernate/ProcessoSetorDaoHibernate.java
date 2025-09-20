package br.gov.stf.estf.processosetor.model.dataaccess.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.localizacao.TipoFaseSetor;
import br.gov.stf.estf.entidade.processosetor.EstatisticaProcessoSetor;
import br.gov.stf.estf.entidade.processosetor.ProcessoSetor;
import br.gov.stf.estf.entidade.processosetor.RelatorioAnaliticoProcessoSetor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoIncidentePreferencia;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.estf.model.util.TipoOrdem;
import br.gov.stf.estf.model.util.TipoOrdemProcesso;
import br.gov.stf.estf.processosetor.model.dataaccess.ProcessoSetorDao;
import br.gov.stf.estf.processosetor.model.service.impl.ProcessoSetorEletronicoSearchData;
import br.gov.stf.estf.processosetor.model.service.impl.ProcessoSetorEletronicoSearchData.TipoQTD;
import br.gov.stf.estf.processosetor.model.util.ProcessoSetorSearchData;
import br.gov.stf.estf.processosetor.model.util.TipoGroupBy;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.DateTimeHelper;
import br.gov.stf.framework.util.FWConfig;
import br.gov.stf.framework.util.SearchData;
import br.gov.stf.framework.util.SearchResult;

@Repository
public class ProcessoSetorDaoHibernate extends
		GenericHibernateDao<ProcessoSetor, Long> implements ProcessoSetorDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6224414527819136772L;

	public ProcessoSetorDaoHibernate() {
		super(ProcessoSetor.class);
	}

	public ProcessoSetor recuperarProcessoSetor(String sigla, Long numero,
			Short recurso, Long idSetor) throws DaoException {

		Session session = retrieveSession();

		ProcessoSetor processo = null;

		try {

			Criteria criteria = session.createCriteria(ProcessoSetor.class);

			if (sigla != null && !sigla.equals("")) {
				criteria.add(Restrictions.eq("siglaClasseProcessual", sigla));
			}

			if (numero != null) {
				criteria.add(Restrictions.eq("numeroProcessual", numero));
			}

			if (recurso != null) {
				criteria.add(Restrictions.eq("codigoRecurso", recurso));
			}

			if (idSetor != null) {
				criteria.add(Restrictions.eq("setor.id", idSetor));
			}

			List<ProcessoSetor> listaProcesso = (List) criteria.list();
			if (listaProcesso != null && listaProcesso.size() > 0) {
				processo = listaProcesso.get(0);
			}

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return processo;
	}

	public ProcessoSetor recuperarProcessoSetor(Long numeroProtocolo,
			Short anoProtocolo, Long idSetor) throws DaoException {
		Session session = retrieveSession();

		ProcessoSetor processo = null;

		try {

			Criteria criteria = session.createCriteria(ProcessoSetor.class);

			criteria.add(Restrictions.eq("numeroProtocolo", numeroProtocolo));
			criteria.add(Restrictions.eq("anoProtocolo", anoProtocolo));
			criteria.add(Restrictions.eq("setor.id", idSetor));

			processo = (ProcessoSetor) criteria.uniqueResult();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return processo;
	}

	public Boolean persistirProcessoSetor(ProcessoSetor processoSetor)
			throws DaoException {

		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {
			processoSetor = salvar(processoSetor);
			session.flush();

		} catch (SQLGrammarException e) {
			if (e.getSQL().indexOf("update JUDICIARIO.OBJETO_INCIDENTE") == -1) {
				throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
			}
			else {
				logger.warn("Hibernate tentou alterar o objeto incidente.");
			}
			
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		
		alterado = Boolean.TRUE;

		return alterado;
	}

	public Boolean alterarProcessoSetor(ProcessoSetor processoSetor)
			throws DaoException {

		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();
		try {

			session.merge(processoSetor);
			session.flush();

			alterado = Boolean.TRUE;

		} catch (HibernateException e) {
			throw new DaoException(e.getMessage(),
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException(e.getMessage(), e);
		}

		return alterado;
	}

	private boolean isPesquisarComProtocolo(Short anoProtocolo,
			Long numeroProtocolo, Boolean orderByProtocolo,
			 Boolean protocoloNaoAutuado) {
		return (anoProtocolo != null && anoProtocolo > 0)
				|| (numeroProtocolo != null && numeroProtocolo > 0)
				|| (orderByProtocolo != null && orderByProtocolo)
				|| (protocoloNaoAutuado != null && protocoloNaoAutuado);
	}

	public List<ProcessoSetor> pesquisarProcessoSetor(String siglaUsuario)
			throws DaoException {

		Session session = retrieveSession();
		List<ProcessoSetor> result = null;

		try {
			StringBuffer hql = new StringBuffer();

			hql.append("SELECT ps FROM ProcessoSetor ps" + " WHERE 1=1");

			if (siglaUsuario != null && !"".equals(siglaUsuario)) {
				hql.append(" AND ps.distribuicaoAtual.usuario.id = '"
						+ siglaUsuario.toUpperCase() + "'");
			}

			Query query = session.createQuery(hql.toString());

			result = query.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return result;
	}

	public List<EstatisticaProcessoSetor> pesquisarProcessoSetor(
			Short anoProtocolo, Long numeroProtocolo, String siglasClassesProcessuaisAgrupadas, String sigla,
			Long numeroProcesso, Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada,
			String siglaTipoJulgamento, String codigoTipoMeioProcesso,
			Long numeroPeticao, String codigoAssunto, String descricaoAssunto,
			String complementoAssunto, Long codigoMinistroRelator,
			String numeroSala, String numeroArmario, String numeroEstante,
			String numeroPrateleira, String numeroColuna,
			String obsDeslocamento,
			Boolean pesquisarAssuntoEmTodosNiveis, Boolean pesquisarInicio,
			Long idSecaoUltimoDeslocamento, String siglaUsuarioDistribuicao,
			Long idGrupoProcessoSetor, 
			Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal,
			Date dataDistribuicaoInicial,
			Date dataDistribuicaoFinal, Date dataFaseInicial,
			Date dataFaseFinal, Date dataRemessaInicial, Date dataRemessaFinal,
			Date dataRecebimentoInicial, Date dataRecebimentoFinal,
			Date dataEntradaInicial, Date dataEntradaFinal, Date dataSaidaInicial, Date dataSaidaFinal, Long idSetor,
			Long idTipoUltimaFaseSetor, Long idTipoUltimoStatusSetor,
			Boolean faseAtualProcesso, Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox,
			Boolean semLocalizacao, Boolean semFase, Boolean semDistribuicao,
			Boolean semVista, 
            Long idCategoriaPartePesquisa,
            String nomeParte,
			Long idTipoTarefa, Boolean localizadosNoSetor,
			Boolean emTramiteNoSetor, Boolean possuiLiminar,
			Boolean possuiPreferencia, Boolean sobrestado, Boolean julgado,
			List<Andamento> listaIncluirAndamentos,
			Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos,
			List<Andamento> listaNaoIncluirAndamentos,
			Date dataInicialNaoIncluirAndamentos,
			Date dataFinalNaoIncluirAndamentos, Boolean groupByFase,
			Boolean groupByFaseStatus, Boolean groupByDistribuicao,
			Boolean groupByDeslocamento, Boolean groupByAssunto)
			throws DaoException {

		List<EstatisticaProcessoSetor> result = null;

		try {
			Session session = retrieveSession();
			Query query = createProcessoSetorQuery(anoProtocolo,
					numeroProtocolo, siglasClassesProcessuaisAgrupadas, sigla, numeroProcesso, recurso, possuiRecurso,
					siglaRecursoUnificada, siglaTipoJulgamento,
					codigoTipoMeioProcesso, numeroPeticao, codigoAssunto,
					descricaoAssunto, complementoAssunto,
					codigoMinistroRelator, numeroSala, numeroArmario,
					numeroEstante, numeroPrateleira, numeroColuna,
					obsDeslocamento,
					pesquisarAssuntoEmTodosNiveis, pesquisarInicio,
					idSecaoUltimoDeslocamento, siglaUsuarioDistribuicao,
					idGrupoProcessoSetor, 
					dataDistribuicaoMinistroInicial, dataDistribuicaoMinistroFinal,
					dataDistribuicaoInicial,
					dataDistribuicaoFinal, dataFaseInicial, dataFaseFinal,
					dataRemessaInicial, dataRemessaFinal,
					dataRecebimentoInicial, dataRecebimentoFinal,
					dataEntradaInicial, dataEntradaFinal, dataSaidaInicial, dataSaidaFinal, idSetor,
					idTipoUltimaFaseSetor, idTipoUltimoStatusSetor,
					faseAtualProcesso, repercussaoGeralCheckbox, protocoloNaoAutuadoCheckbox,
					semLocalizacao, semFase, semDistribuicao, semVista,
					idCategoriaPartePesquisa, nomeParte,
					idTipoTarefa, localizadosNoSetor, emTramiteNoSetor,
					possuiLiminar, possuiPreferencia, sobrestado, julgado,
					false, true, false, groupByFase, groupByFaseStatus,
					groupByDistribuicao, groupByDeslocamento, groupByAssunto,
					null, null, null, null, null, null, listaIncluirAndamentos,
					dataInicialIncluirAndamentos, dataFinalIncluirAndamentos,
					listaNaoIncluirAndamentos, dataInicialNaoIncluirAndamentos,
					dataFinalNaoIncluirAndamentos);

			result = query.list();

			session.clear();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (DaoException e) {
			throw new DaoException("RuntimeException", e);
		}

		return result;
	}
	
	protected Query createProcessoSetorQuery(Short anoProtocolo,
			Long numeroProtocolo, String siglasClassesProcessuaisAgrupadas, String sigla, Long numeroProcesso,
			Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada,
			String siglaTipoJulgamento, String codigoTipoMeioProcesso,
			Long numeroPeticao, String codigoAssunto, String descricaoAssunto,
			String complementoAssunto, Long codigoMinistroRelator,
			String numeroSala, String numeroArmario, String numeroEstante,
			String numeroPrateleira, String numeroColuna,
			String obsDeslocamento,
			Boolean pesquisarAssuntoEmTodosNiveis, Boolean pesquisarInicio,
			Long idSecaoUltimoDeslocamento, String siglaUsuarioDistribuicao,
			Long idGrupoProcessoSetor, 
			Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal,
			Date dataDistribuicaoInicial,
			Date dataDistribuicaoFinal, Date dataFaseInicial,
			Date dataFaseFinal, Date dataRemessaInicial, Date dataRemessaFinal,
			Date dataRecebimentoInicial, Date dataRecebimentoFinal,
			Date dataEntradaInicial, Date dataEntradaFinal, Date dataSaidaInicial, Date dataSaidaFinal, Long idSetor,
			Long idTipoUltimaFaseSetor, Long idTipoUltimoStatusSetor,
			Boolean faseProcessualAtual, Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox,
			Boolean semLocalizacao, Boolean semFase, Boolean semDistribuicao,
			Boolean semVista, 
            Long idCategoriaPartePesquisa,
            String nomeParte,
			Long idTipoTarefa, Boolean localizadosNoSetor,
			Boolean emTramiteNoSetor, Boolean possuiLiminar,
			Boolean possuiPreferencia, Boolean sobrestado, Boolean julgado,
			Boolean preFetchAssunto, Boolean readOnlyQuery, Boolean count,
			Boolean groupByFase, Boolean groupByFaseStatus,
			Boolean groupByDistribuicao, Boolean groupByDeslocamento,
			Boolean groupByAssunto, Boolean orderByProcesso,
			Boolean orderByProtocolo, Boolean orderByValorGut,
			Boolean orderByDataEntrada, Boolean orderByAssunto,
			Boolean orderByCrescente,
			List<Andamento> listaIncluirAndamentos,
			Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos,
			List<Andamento> listaNaoIncluirAndamentos,
			Date dataInicialNaoIncluirAndamentos,
			Date dataFinalNaoIncluirAndamentos) throws DaoException {

		boolean buscaPorAssunto = false;

		if ((descricaoAssunto != null && descricaoAssunto.trim().length() >= 3)	|| (codigoAssunto != null))
			buscaPorAssunto = true;

		StringBuffer hql = new StringBuffer();

		/*
		 * ------------------------------------- SELECT & JOINS ----------------------------------
		 */

//		boolean groupBy = false;
//		if (groupByFase || groupByDistribuicao || groupByDeslocamento
//				|| groupByAssunto)
//			groupBy = true;
//
//		if (groupBy) {
//			if (groupByFase && (semFase == null || !semFase.booleanValue())) {
//				if (groupByFaseStatus) {
//					/*hql.append(" SELECT new br.gov.stf.estf.processosetor.modelo.EstatisticaProcessoSetor("
//									+ "   ps.faseAtual.tipoFaseSetor.descricao, ps.faseAtual.tipoStatusSetor.descricao, COUNT(distinct ps) "
//									+ " ) " + " FROM ProcessoSetor ps ");*/
//					
//					hql.append(" SELECT new br.gov.stf.estf.entidade.processosetor.EstatisticaProcessoSetor("
//							+ "   ps.faseAtual.tipoFaseSetor.descricao, tssGru.descricao, COUNT(distinct ps) "
//							+ " ) " + " FROM ProcessoSetor ps JOIN FETCH ps.objetoIncidente oips JOIN FETCH ps.objetoIncidente.principal oiprinc JOIN FETCH ps.objetoIncidente.principal.anterior oiant " +
//									" LEFT JOIN ps.faseAtual.tipoStatusSetor tssGru");
//					
//				} else {
//					hql.append(" SELECT new br.gov.stf.estf.entidade.processosetor.EstatisticaProcessoSetor("
//									+ "   ps.faseAtual.tipoFaseSetor.descricao, COUNT(distinct ps) "
//									+ " ) " + " FROM ProcessoSetor ps JOIN FETCH ps.objetoIncidente oips JOIN FETCH ps.objetoIncidente.principal oiprinc JOIN FETCH ps.objetoIncidente.principal.anterior oiant ");
//				}			
//
//			} else if (groupByDistribuicao
//					&& (semDistribuicao == null || !semDistribuicao.booleanValue())) {
//				hql.append(" SELECT new br.gov.stf.estf.entidade.processosetor.EstatisticaProcessoSetor("
//								+ " ps.distribuicaoAtual.usuario.nome, COUNT(distinct ps) "
//								+ " ) " + " FROM ProcessoSetor ps JOIN FETCH ps.objetoIncidente oips JOIN FETCH ps.objetoIncidente.principal oiprinc JOIN FETCH ps.objetoIncidente.principal.anterior oiant ");
//
//			} else if (groupByDeslocamento
//					&& (semLocalizacao == null || !semLocalizacao.booleanValue())) {
//
//				hql.append(" SELECT new br.gov.stf.estf.entidade.processosetor.EstatisticaProcessoSetor("
//								+ "   sd.descricao, COUNT(distinct ps) "
//								+ " ) " + " FROM ProcessoSetor ps JOIN FETCH ps.objetoIncidente oips JOIN FETCH ps.objetoIncidente.principal oiprinc JOIN FETCH ps.objetoIncidente.principal.anterior oiant ");
//
//				hql.append(" JOIN ps.deslocamentoAtual da LEFT JOIN da.secaoDestino sd ");
//
//			} else if (groupByAssunto != null && groupByAssunto.booleanValue()) {
//				hql.append(" SELECT new br.gov.stf.estf.entidade.processosetor.EstatisticaProcessoSetor("
//								+ "   ah.descricaoCompleta, COUNT(distinct ps) "
//								+ " ) " + " FROM ProcessoSetor ps JOIN FETCH ps.objetoIncidente oips JOIN FETCH ps.objetoIncidente.principal oiprinc JOIN FETCH ps.objetoIncidente.principal.anterior oiant ");				
//
//			} else if ((semLocalizacao != null && semLocalizacao.booleanValue())
//					|| (semFase != null && semFase.booleanValue())
//					|| (semDistribuicao != null && semDistribuicao.booleanValue())) {
//				hql.append(" SELECT new br.gov.stf.estf.entidade.processosetor.EstatisticaProcessoSetor("
//								+ "   COUNT(distinct ps) "
//								+ " ) "
//								+ " FROM ProcessoSetor ps JOIN FETCH ps.objetoIncidente oips JOIN FETCH ps.objetoIncidente.principal oiprinc JOIN FETCH ps.objetoIncidente.principal.anterior oiant ");
//
//			}

//		} else 
		hql.append(" SELECT ");
		
		if (count != null && count.booleanValue()) {
			if ( (idCategoriaPartePesquisa != null && idCategoriaPartePesquisa > 0) || (nomeParte != null && nomeParte.trim().length() > 0) 
					|| (dataDistribuicaoMinistroInicial != null || dataDistribuicaoMinistroFinal != null))
				hql.append(" COUNT(DISTINCT ps) ");
			else
				hql.append(" COUNT(ps) ");
			
		} else {
			if ( (idCategoriaPartePesquisa != null && idCategoriaPartePesquisa > 0) || (nomeParte != null && nomeParte.trim().length() > 0) 
					|| (dataDistribuicaoMinistroInicial != null || dataDistribuicaoMinistroFinal != null))
				hql.append(" DISTINCT ");
			
			hql.append(" ps  ");
		}
		
		hql.append(" FROM ProcessoSetor ps JOIN ps.objetoIncidente oips ");
		//hql.append(" FROM ProcessoSetor ps JOIN FETCH ps.objetoIncidente oips ");
		
		if(protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox)
			hql.append(" LEFT JOIN ps.objetoIncidente.principal oiprinc LEFT JOIN ps.objetoIncidente.principal.anterior oiant ");
//			hql.append(" JOIN FETCH ps.objetoIncidente.principal oiprinc JOIN FETCH ps.objetoIncidente.principal.anterior oiant ");

		if (idGrupoProcessoSetor != null && idGrupoProcessoSetor > 0)
			hql.append(" JOIN ps.grupos gr ");

		if (semVista != null)
			hql.append(" , Agendamento a ");

		if ( dataDistribuicaoMinistroInicial != null || dataDistribuicaoMinistroFinal != null /*|| 
				(codigoMinistroRelator != null && codigoMinistroRelator > 0)*/ )
			hql.append(" , SituacaoMinistroProcesso smp ");

		if ( (idCategoriaPartePesquisa != null && idCategoriaPartePesquisa > 0) || (nomeParte != null && nomeParte.trim().length() > 0) )
			hql.append(" , Parte pv ");
		
		if( SearchData.stringNotEmpty(codigoAssunto) || SearchData.stringNotEmpty(descricaoAssunto) || 
				(preFetchAssunto != null && preFetchAssunto.booleanValue()) || buscaPorAssunto || 
				//(groupByAssunto != null && groupByAssunto.booleanValue()) ||
				(orderByAssunto != null && orderByAssunto.booleanValue()) )
			hql.append(" ,Assunto ah, AssuntoProcesso ahproc ");		

//		if( possuiLiminar != null )
//			hql.append(" ,IncidentePreferencia ip ");
		
		
//		if (siglaRecursoUnificada != null)
//			hql.append(" JOIN FETCH ps.classeProcessualSTFUnificada ");
//		else
//			hql.append(" LEFT JOIN FETCH ps.classeProcessualSTFUnificada ");		
		
		if( (anoProtocolo != null && anoProtocolo > 0) || 
				(numeroProtocolo != null && numeroProtocolo > 0) ||
				(orderByProtocolo != null && orderByProtocolo) ||
				(orderByAssunto != null && orderByAssunto.booleanValue()) ||
				SearchData.stringNotEmpty(codigoTipoMeioProcesso) )
			hql.append(" ,Protocolo protocoloSTF ");
		
		if( protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox ) {
			if ( (SearchData.stringNotEmpty(siglasClassesProcessuaisAgrupadas)) || (SearchData.stringNotEmpty(siglasClassesProcessuaisAgrupadas) && 
					siglasClassesProcessuaisAgrupadas.equals(Classe.SIGLAS_CLASSES_PROCESSUAIS_ORIGINARIOS)) )
				hql.append(" , Classe cp ");	
			
			if( recurso != null && recurso > 0 )
				hql.append(" ,RecursoProcesso rp ");
			
			if( (SearchData.stringNotEmpty(siglasClassesProcessuaisAgrupadas)) ||
					(numeroProcesso != null) ||
					(SearchData.stringNotEmpty(sigla)) ||
					(recurso != null) ||
					(SearchData.stringNotEmpty(codigoTipoMeioProcesso)) ||
					(repercussaoGeralCheckbox != null && repercussaoGeralCheckbox) ||					
					//(protocoloNaoAutuadoCheckbox != null) ||
					(idTipoTarefa != null) ||
					(idCategoriaPartePesquisa != null && idCategoriaPartePesquisa > 0) ||
					(SearchData.stringNotEmpty(nomeParte)) ||
					(orderByProcesso != null) ||
					(dataDistribuicaoMinistroInicial != null || dataDistribuicaoMinistroFinal != null) ||
					(SearchData.stringNotEmpty(codigoAssunto) || SearchData.stringNotEmpty(descricaoAssunto)) ||
					(SearchData.stringNotEmpty(siglaRecursoUnificada)) ||
					//(groupByAssunto != null && groupByAssunto.booleanValue()) || 
					(localizadosNoSetor != null && localizadosNoSetor != true ) || 
					(orderByAssunto != null && orderByAssunto.booleanValue()) )
				hql.append(" ,Processo processoSTF ");
			
			if( (SearchData.stringNotEmpty(siglaTipoJulgamento) && !siglaTipoJulgamento.equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla())) /*|| (semVista != null)*/ )
				hql.append(" ,IncidenteJulgamento ij ");
			
			
			if (idTipoTarefa != null && idTipoTarefa > 0)
				hql.append(" , TarefaSetor ts LEFT JOIN ts.processos pts ");
		}

		/*
		 * ---------------------------------------- WHERE ---------------------------------------
		 */
		hql.append(" WHERE 1=1 ");	
		
		hql.append(" AND CASE WHEN (ps.objetoIncidente.tipoObjetoIncidente = 'RC') THEN (select COUNT(rp1) from RecursoProcesso rp1 where rp1 = ps.objetoIncidente.id AND rp1.ativo = 'S') ");
		hql.append(" WHEN (ps.objetoIncidente.tipoObjetoIncidente = 'IJ') THEN (select COUNT(ij1) from IncidenteJulgamento ij1 where ij1.id = ps.objetoIncidente.id AND ij1.ativo = 'S') ");
		hql.append(" WHEN (ps.objetoIncidente.tipoObjetoIncidente = 'PR') THEN 1 ELSE 0 END = 1 ");
	
		
		//-------------------- JOINS ------------------
		if( (anoProtocolo != null && anoProtocolo > 0) || 
				(numeroProtocolo != null && numeroProtocolo > 0) ||
				(orderByProtocolo != null && orderByProtocolo) ||
				(orderByAssunto != null && orderByAssunto.booleanValue()) ||
				SearchData.stringNotEmpty(codigoTipoMeioProcesso) ) {
			if(protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox)
				hql.append(" AND protocoloSTF.id = oiant.id ");
			else if(protocoloNaoAutuadoCheckbox != null && protocoloNaoAutuadoCheckbox)
				hql.append(" AND protocoloSTF.id = oips.id ");
		}
		
//		if( possuiLiminar != null ) {
//			hql.append(" AND ip.objetoIncidente.id = oips.id ");
//			hqlQtd.append(" AND ip.objetoIncidente.id = oips.id ");
//		}		
		
		if( SearchData.stringNotEmpty(codigoAssunto) || SearchData.stringNotEmpty(descricaoAssunto) || 
				(preFetchAssunto != null && preFetchAssunto.booleanValue()) || buscaPorAssunto || 
				//(groupByAssunto != null && groupByAssunto.booleanValue()) ||
				(orderByAssunto != null && orderByAssunto.booleanValue()))
			if(protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox)
				hql.append(" AND ah.id = ahproc.assunto.id AND oiprinc.id = ahproc.objetoIncidente.id ");	
			else
				hql.append(" AND ah.id = ahproc.assunto.id AND oips.id = ahproc.objetoIncidente.id ");
		
		if ( (idCategoriaPartePesquisa != null && idCategoriaPartePesquisa > 0) || SearchData.stringNotEmpty(nomeParte) )
			hql.append(" AND pv.objetoIncidente.id = oips.id ");
		
		if ( semVista != null)
			hql.append(" AND a.id.objetoIncidente = oips.id ");
			
		if( dataDistribuicaoMinistroInicial != null || dataDistribuicaoMinistroFinal != null /*|| 
				(codigoMinistroRelator != null && codigoMinistroRelator > 0)*/ )
			hql.append(" AND smp.objetoIncidente.id = oips.id ");
		
		if( protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox ) {
			if( recurso != null && recurso > 0 )
				hql.append(" AND rp.principal.id = oiprinc.id ");
			
			if( ((SearchData.stringNotEmpty(siglasClassesProcessuaisAgrupadas)) ||
					(numeroProcesso != null) ||
					(SearchData.stringNotEmpty(sigla)) ||
					(recurso != null) ||
					(SearchData.stringNotEmpty(codigoTipoMeioProcesso)) ||
					(repercussaoGeralCheckbox != null && repercussaoGeralCheckbox) ||
					//(protocoloNaoAutuadoCheckbox != null) ||
					(idTipoTarefa != null) ||
					(idCategoriaPartePesquisa != null && idCategoriaPartePesquisa > 0) ||
					(SearchData.stringNotEmpty(nomeParte)) ||
					(orderByProcesso != null) ||
					(dataDistribuicaoMinistroInicial != null || dataDistribuicaoMinistroFinal != null) ||
					(SearchData.stringNotEmpty(codigoAssunto) || SearchData.stringNotEmpty(descricaoAssunto)) ||
					(SearchData.stringNotEmpty(siglaRecursoUnificada)) ||
					//(groupByAssunto != null && groupByAssunto.booleanValue()) ||
					(orderByAssunto != null && orderByAssunto.booleanValue())) && 
					(protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox) )
				hql.append(" AND processoSTF.id = oiprinc.id ");
		
			if( (SearchData.stringNotEmpty(siglaTipoJulgamento) && !siglaTipoJulgamento.equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla())) /*|| (semVista != null)*/ )
				hql.append(" AND ij.id = oips.id ");
		}		
		
		//----------------------------------------------	
		
		if( protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox ) {
			if ( SearchData.stringNotEmpty(siglasClassesProcessuaisAgrupadas) ) {								
				hql.append(" AND processoSTF.siglaClasseProcessual = cp.id ");
				if( siglasClassesProcessuaisAgrupadas.equals(Classe.SIGLAS_CLASSES_PROCESSUAIS_AI_RE) ) {
					hql.append(" AND processoSTF.siglaClasseProcessual in ('RE', 'AI') ");
				} else if( siglasClassesProcessuaisAgrupadas.equals(Classe.SIGLAS_CLASSES_PROCESSUAIS_ORIGINARIOS) ) {					
					hql.append(" AND cp.originario = 'S' ");
				}
			} else if ( SearchData.stringNotEmpty(sigla)) {
				hql.append(" AND processoSTF.siglaClasseProcessual = :sigla ");
			}

			if (numeroProcesso != null && numeroProcesso > 0)
				hql.append(" AND processoSTF.numeroProcessual = :numeroProcesso ");
			
			if ( possuiRecurso != null ){			
				if ( possuiRecurso ) 
					hql.append(" AND ");
				else if ( !possuiRecurso )
					hql.append(" AND NOT ");

				hql.append(" EXISTS (SELECT rp FROM RecursoProcesso rp WHERE rp.id = oips.id ) ");
			}
			
			if ( recurso != null && recurso >0 )
				hql.append(" AND rp.codigoRecurso = :recurso ");

			if (siglaRecursoUnificada != null && siglaRecursoUnificada.trim().length() > 0)
				hql.append(" AND EXISTS (SELECT rp FROM RecursoProcesso rp WHERE  oips.id = rp.id AND rp.siglaCadeiaIncidente LIKE :siglaRecursoUnificada) ");
			

			if (SearchData.stringNotEmpty(siglaTipoJulgamento)) {
				if( !siglaTipoJulgamento.equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla()))
					hql.append(" AND ij.tipoJulgamento.sigla = :siglaTipoJulgamento ");
				else
					hql.append(" AND NOT EXISTS (SELECT ij FROM IncidenteJulgamento ij WHERE oips.id = ij.id) ");
			}			
			
			if (codigoTipoMeioProcesso != null && codigoTipoMeioProcesso.trim().length() > 0)
				hql.append(" AND processoSTF.tipoMeioProcesso = :codigoTipoMeioProcesso ");
			

			// Repercussão Geral = S
			if (repercussaoGeralCheckbox != null && repercussaoGeralCheckbox.booleanValue()) 
				hql.append(" AND processoSTF.repercussaoGeral = 'S' ");
			
			// tarefa
			if (idTipoTarefa != null && idTipoTarefa > 0)
				hql.append(" AND ts.tipoTarefa.id = :idTipoTarefa "
								+ " AND processoSTF.siglaClasseProcessual = pts.siglaClasseProcessual "
								+ " AND processoSTF.numeroProcessual = pts.numeroProcessual ");
			
			// Parâmetros de Processos por ministro Relator
			if ( codigoMinistroRelator != null && codigoMinistroRelator > 0 ) {
				hql.append(" AND processoSTF.ministroRelatorAtual.id = :codigoMinistroRelator ");
			}
			
		} else if(protocoloNaoAutuadoCheckbox != null && protocoloNaoAutuadoCheckbox) {
			// protocolo não autuado
			hql.append(" AND oips.tipoObjetoIncidente = 'PI' " +
						" AND NOT EXISTS (SELECT oiptna FROM ObjetoIncidente oiptna WHERE oiptna.anterior.id = oips.id) ");
			
			if (codigoTipoMeioProcesso != null && codigoTipoMeioProcesso.trim().length() > 0)
				hql.append(" AND protocoloSTF.tipoMeioProcesso = :codigoTipoMeioProcesso ");
		}
		

		if (anoProtocolo != null && anoProtocolo > 0)
			hql.append(" AND protocoloSTF.anoProtocolo = :anoProtocolo ");

		if (numeroProtocolo != null && numeroProtocolo > 0)
			hql.append(" AND protocoloSTF.numeroProtocolo = :numeroProtocolo ");
	
		if (idSecaoUltimoDeslocamento != null && idSecaoUltimoDeslocamento > 0 )
			hql.append(" AND ps.deslocamentoAtual.secaoDestino.id = :idSecaoUltimoDeslocamento ");

		// Parâmetros de fase
		if (semFase != null && semFase.booleanValue()) {
			hql.append(" AND ps.faseAtual IS NULL ");
		} else {
			if (semFase != null && !semFase.booleanValue())
				hql.append(" AND ps.faseAtual IS NOT NULL ");

			if (idTipoUltimaFaseSetor != null && idTipoUltimaFaseSetor > 0 && (semFase == null || !semFase) )
				hql.append(" AND ps.faseAtual.tipoFaseSetor.id = :idTipoUltimaFaseSetor ");

			if (idTipoUltimoStatusSetor != null && idTipoUltimoStatusSetor > 0)
				hql.append(" AND ps.faseAtual.tipoStatusSetor.id = :idTipoUltimoStatusSetor ");

			if (dataFaseInicial != null)
				hql.append(" AND ps.faseAtual.dataFase >= to_date(:dataFaseInicial, 'DD/MM/YYYY HH24:MI:SS')  ");

			if (dataFaseFinal != null)
				hql.append(" AND ps.faseAtual.dataFase <= to_date(:dataFaseFinal, 'DD/MM/YYYY HH24:MI:SS')  ");
		}

		// Parâmetros de distribuição
		if (semDistribuicao != null && semDistribuicao.booleanValue()) {
			hql.append(" AND ps.distribuicaoAtual IS NULL ");
			dataDistribuicaoFinal = null;
			dataDistribuicaoInicial = null;
		} else {
			if (semDistribuicao != null && !semDistribuicao.booleanValue())
				hql.append(" AND ps.distribuicaoAtual IS NOT NULL ");

			if ( SearchData.stringNotEmpty(siglaUsuarioDistribuicao) && (semDistribuicao == null || !semDistribuicao) )
				hql.append(" AND ps.distribuicaoAtual.usuario.id = :siglaUsuarioDistribuicao ");

			if (dataDistribuicaoInicial != null)
				hql.append(" AND ps.distribuicaoAtual.dataDistribuicao >= to_date(:dataDistribuicaoInicial, 'DD/MM/YYYY HH24:MI:SS')  ");

			if (dataDistribuicaoFinal != null)
				hql.append(" AND ps.distribuicaoAtual.dataDistribuicao <= to_date(:dataDistribuicaoFinal, 'DD/MM/YYYY HH24:MI:SS')  ");
		}
		
		// Distribuição para o Relator
		if( dataDistribuicaoMinistroInicial != null || dataDistribuicaoMinistroFinal != null ) {
			if( dataDistribuicaoMinistroInicial != null )
				hql.append(" AND smp.dataOcorrencia >= to_date(:dataDistribuicaoMinistroInicial, 'DD/MM/YYYY HH24:MI:SS')  ");
				
			if( dataDistribuicaoMinistroFinal != null )
				hql.append(" AND smp.dataOcorrencia <= to_date(:dataDistribuicaoMinistroFinal, 'DD/MM/YYYY HH24:MI:SS')  ");					
				
			hql.append(" AND smp.ocorrencia IN ('RE', 'SU', 'RG') ");
			hql.append(" AND smp.relatorAtual = 'S' ");				
		}
		
		// Última situação do processo
		if (faseProcessualAtual != null && faseProcessualAtual.booleanValue()) {
			hql.append(" AND ( EXISTS ( SELECT rpfpa.id FROM RecursoProcesso rpfpa, Processo prp1 " +
					"	                    WHERE prp1.id = rpfpa.principal.id AND rpfpa.dataInterposicao = " +
					"											(SELECT MAX(rpfpa2.dataInterposicao) FROM RecursoProcesso rpfpa2, Processo prp2 " +
					"															WHERE prp2.id = rpfpa2.principal.id " +
					"																  AND prp1.siglaClasseProcessual = prp2.siglaClasseProcessual " +
					"																  AND prp1.numeroProcessual = prp2.numeroProcessual ) " +
					"   AND rpfpa.id = oips.id)	" +
					"          OR EXISTS (SELECT pfpa.id FROM Processo pfpa " +
					"						  WHERE pfpa.id = oips.id " +
					"						        AND NOT EXISTS (SELECT oifpap.id FROM ObjetoIncidente oifpap " +
					"													WHERE oifpap.principal = pfpa.id AND oifpap.tipoObjetoIncidente = 'RC')" +
					"                     ) " +
					"        )");
		}
		
		// Parâmetros de deslocamento
		if (semLocalizacao != null && semLocalizacao.booleanValue()) {
			hql.append(" AND ps.deslocamentoAtual IS NULL ");
		} else {
			if (groupByDeslocamento) {
				hql.append(" AND ps.deslocamentoAtual IS NOT NULL ");

				if ((numeroSala != null && numeroSala.trim().length() > 0)
						|| (numeroArmario != null && numeroArmario.trim()
								.length() > 0)
						|| (numeroEstante != null && numeroEstante.trim()
								.length() > 0)
						|| (numeroPrateleira != null && numeroPrateleira.trim()
								.length() > 0)
						|| (numeroColuna != null && numeroColuna.trim()
								.length() > 0)) {
					hql.append(" AND contains (ps.deslocamentoAtual.flagAtualizado,' ");

					if (numeroSala != null && numeroSala.trim().length() > 0)
						hql.append(" (%" + numeroSala
								+ "% within NUM_SALA) AND ");

					if (numeroArmario != null
							&& numeroArmario.trim().length() > 0)
						hql.append(" (%" + numeroArmario
								+ "% within NUM_ARMARIO) AND ");

					if (numeroEstante != null
							&& numeroEstante.trim().length() > 0)
						hql.append(" (%" + numeroEstante
								+ "% within NUM_ESTANTE) AND ");

					if (numeroPrateleira != null
							&& numeroPrateleira.trim().length() > 0)
						hql.append(" (%" + numeroPrateleira
								+ "% within NUM_PRATELEIRA) AND ");

					if (numeroColuna != null
							&& numeroColuna.trim().length() > 0)
						hql.append(" (%" + numeroColuna
								+ "% within NUM_COLUNA) ");

					// verifica se existe AND por último, caso exista retira o mesmo
					if (hql.substring(hql.length() - 6, hql.length()).equals(
							") AND ")) {
						StringBuffer hqlTemp = new StringBuffer(hql.substring(
								0, hql.length() - 4));
						hql.delete(0, hql.length());
						hql.append(hqlTemp);
					}

					hql.append(" ' ) > 0 ");

				}

				if (dataRemessaInicial != null && (semLocalizacao == null ||  !semLocalizacao))
					hql.append(" AND ps.deslocamentoAtual.dataRemessa >= to_date(:dataRemessaInicial, 'DD/MM/YYYY HH24:MI:SS')  ");

				if (dataRemessaFinal != null && (semLocalizacao == null ||  !semLocalizacao))
					hql.append(" AND ps.deslocamentoAtual.dataRemessa <= to_date(:dataRemessaFinal, 'DD/MM/YYYY HH24:MI:SS')  ");

				if (dataRecebimentoInicial != null && (semLocalizacao == null ||  !semLocalizacao))
					hql.append(" AND ps.deslocamentoAtual.dataRecebimento >= to_date(:dataRecebimentoInicial, 'DD/MM/YYYY HH24:MI:SS')  ");

				if (dataRecebimentoFinal != null && (semLocalizacao == null ||  !semLocalizacao))
					hql.append(" AND ps.deslocamentoAtual.dataRecebimento <= to_date(:dataRecebimentoFinal, 'DD/MM/YYYY HH24:MI:SS')  ");
			} else {
				if (semLocalizacao != null && !semLocalizacao.booleanValue())
					hql.append(" AND ps.deslocamentoAtual IS NOT NULL ");

				if ((numeroSala != null && numeroSala.trim().length() > 0)
						|| (numeroArmario != null && numeroArmario.trim()
								.length() > 0)
						|| (numeroEstante != null && numeroEstante.trim()
								.length() > 0)
						|| (numeroPrateleira != null && numeroPrateleira.trim()
								.length() > 0)
						|| (numeroColuna != null && numeroColuna.trim()
								.length() > 0)) {
					hql.append(" AND contains(ps.deslocamentoAtual.flagAtualizado,' ");

					if (numeroSala != null && numeroSala.trim().length() > 0)
						hql.append(" (%" + numeroSala
								+ "% within NUM_SALA) AND ");

					if (numeroArmario != null
							&& numeroArmario.trim().length() > 0)
						hql.append(" (%" + numeroArmario
								+ "% within NUM_ARMARIO) AND ");

					if (numeroEstante != null
							&& numeroEstante.trim().length() > 0)
						hql.append(" (%" + numeroEstante
								+ "% within NUM_ESTANTE) AND ");

					if (numeroPrateleira != null
							&& numeroPrateleira.trim().length() > 0)
						hql.append(" (%" + numeroPrateleira
								+ "% within NUM_PRATELEIRA) AND ");

					if (numeroColuna != null
							&& numeroColuna.trim().length() > 0)
						hql.append(" (%" + numeroColuna
								+ "% within NUM_COLUNA) ");

					// verifica se existe AND por último, caso exista retira o mesmo
					if (hql.substring(hql.length() - 6, hql.length()).equals(
							") AND ")) {
						StringBuffer hqlTemp = new StringBuffer(hql.substring(
								0, hql.length() - 4));
						hql.delete(0, hql.length());
						hql.append(hqlTemp);
					}

					hql.append(" ' ) > 0 ");

				}

				if (dataRemessaInicial != null && (semLocalizacao == null ||  !semLocalizacao))
					hql.append(" AND ps.deslocamentoAtual.dataRemessa >= to_date(:dataRemessaInicial, 'DD/MM/YYYY HH24:MI:SS')  ");

				if (dataRemessaFinal != null && (semLocalizacao == null ||  !semLocalizacao))
					hql.append(" AND ps.deslocamentoAtual.dataRemessa <= to_date(:dataRemessaFinal, 'DD/MM/YYYY HH24:MI:SS')  ");

				if (dataRecebimentoInicial != null && (semLocalizacao == null ||  !semLocalizacao))
					hql.append(" AND ps.deslocamentoAtual.dataRecebimento >= to_date(:dataRecebimentoInicial, 'DD/MM/YYYY HH24:MI:SS')  ");

				if (dataRecebimentoFinal != null && (semLocalizacao == null ||  !semLocalizacao))
					hql.append(" AND ps.deslocamentoAtual.dataRecebimento <= to_date(:dataRecebimentoFinal, 'DD/MM/YYYY HH24:MI:SS')  ");
			}
		}

		//observacao contida no deslocamento atual
		if( obsDeslocamento!= null && obsDeslocamento.trim().length() > 0) {
			//obsDeslocamento = "%" + obsDeslocamento.replace(' ', '%') + "%";
			obsDeslocamento = obsDeslocamento.replace('%', ' ');
			hql.append(" AND CONTAINS(ps.deslocamentoAtual.observacao, '" + obsDeslocamento + "') > 1 ");
		}
		
		// Parâmetros de vista
		if (semVista != null) {
			hql.append(" AND a.vista IS NOT NULL ");			

			hql.append(" AND a.ministro.id = (SELECT m.id FROM Ministro m WHERE m.setor.id = :idSetor AND m.dataAfastamento IS NULL) ");

//			hql.append(" AND a.objetoIncidente.tipoJulgamento.id = ij.tipoIncidenteJulgamento.id ");

			if (semVista)
				hql.append(" AND a.vista = 'S' ");
			else
				hql.append(" AND a.vista = 'N' ");
		}

		//categorias da parte
		if ( idCategoriaPartePesquisa != null && idCategoriaPartePesquisa > 0 )
			hql.append(" AND pv.categoria.id = :idCategoriaPartePesquisa ");
		
		//nome da parte
		if ( nomeParte != null && nomeParte.trim().length() > 0 ) {
			nomeParte = nomeParte.replace('%', ' ');
//			hql.append(" AND CONTAINS(pv.nomeJurisdicionado, '" + sdProcessoSetor.nomeParte + "') > 1 ");
			hql.append(" AND pv.nomeJurisdicionado LIKE :nomeParte ");
		}

		if (dataEntradaInicial != null)
			hql.append(" AND ps.dataEntrada >= to_date(:dataEntradaInicial, 'DD/MM/YYYY HH24:MI:SS') ");

		if (dataEntradaFinal != null)
			hql.append(" AND ps.dataEntrada <= to_date(:dataEntradaFinal, 'DD/MM/YYYY HH24:MI:SS') ");
		
		if (dataSaidaInicial != null)
			hql.append(" AND ps.dataSaida >= to_date(:dataSaidaInicial, 'DD/MM/YYYY HH24:MI:SS') ");

		if (dataSaidaFinal != null)
			hql.append(" AND ps.dataSaida <= to_date(:dataSaidaFinal, 'DD/MM/YYYY HH24:MI:SS') ");

		if (idSetor != null)
			hql.append(" AND ps.setor.id = :idSetor ");

		if (localizadosNoSetor != null)
			if (localizadosNoSetor.booleanValue())
				hql.append(" AND ps.dataSaida IS NULL ");
			else
				hql.append(" AND ps.dataSaida IS NOT NULL ");

		if (emTramiteNoSetor != null)
			if (emTramiteNoSetor.booleanValue())
				hql.append(" AND ps.dataFimTramite IS NULL ");
			else
				hql.append(" AND ps.dataFimTramite IS NOT NULL ");

		if (possuiLiminar != null) {
			hql.append(" AND ");
			if (possuiLiminar.booleanValue())
				hql.append(" EXISTS ");
			else
				hql.append(" NOT EXISTS ");
			
			hql.append(" (SELECT ip FROM IncidentePreferencia ip WHERE ip.objetoIncidente.id = oips.id AND ip.tipoPreferencia.sigla = '" + TipoIncidentePreferencia.TipoPreferenciaContante.MEDIDA_LIMINAR.getSigla() + "' )");
		}
		
		if (possuiPreferencia != null)
			if (possuiPreferencia.booleanValue())
				hql.append(" AND ps.preferencia = 'S' ");
			else
				hql.append(" AND ps.preferencia = 'N' ");

		if (sobrestado != null)
			if (sobrestado.booleanValue())
				hql.append(" AND ps.sobrestado = 'N' ");
			else
				hql.append(" AND ps.sobrestado = 'S' ");

		if (julgado != null) {
			hql.append("AND ");
			if (!julgado) {
				hql.append("NOT ");
			}

			hql.append(" EXISTS (SELECT cvt "
					+ " FROM ControleVoto cvt "
					+ " WHERE cvt.tipoTexto = " + TipoTexto.EMENTA.getCodigo()
					+ " AND cvt.ministro.id = cvt.codigoMinistroRelator "
					+ " AND cvt.objetoIncidente.id = oips.id) ");
		}

		if (idGrupoProcessoSetor != null && idGrupoProcessoSetor > 0)
			hql.append(" AND gr.id = :idGrupoProcessoSetor ");

		if (descricaoAssunto != null && descricaoAssunto.trim().length() >= 3) {
			descricaoAssunto = descricaoAssunto.replace("|", "\\|");
			descricaoAssunto = descricaoAssunto.replace('%', ' ');
			hql.append(" AND CONTAINS(ah.descricaoCompleta, '" + descricaoAssunto +"') > 1 ");
		}

		// if( codigoAssunto != null && codigoAssunto.trim().length() == 9 ) {
		if ( SearchData.stringNotEmpty(codigoAssunto) ) {
			hql.append(" AND ah.id = :codigoAssunto ");
		}

		if ( SearchData.stringNotEmpty(complementoAssunto) ) {
			complementoAssunto = "%" + complementoAssunto.replace(' ', '%').toUpperCase()
					+ "%";
			hql.append(" AND UPPER(ps.complementoAssunto) LIKE :complementoAssunto ");
		}

		if (listaIncluirAndamentos != null && listaIncluirAndamentos.size() > 0) {
			hql.append(" AND EXISTS( SELECT ap1.descricaoObservacaoAndamento "
					+ " FROM AndamentoProcesso ap1"
					+ " WHERE ap1.tipoAndamento.id IN( ");
			int ivalor = 1;
			for (Andamento tipoAndamento : listaIncluirAndamentos) {
				if (ivalor < listaIncluirAndamentos.size()) {
					hql.append(tipoAndamento.getId() + ",");
				} else {
					hql.append(tipoAndamento.getId() + ")");
				}
				ivalor++;
			}

			if (dataInicialIncluirAndamentos != null)
				hql.append(" AND ap1.dataAndamento >= to_date(:dataInicialIncluirAndamentos, 'DD/MM/YYYY HH24:MI:SS') ");

			if (dataFinalIncluirAndamentos != null)
				hql.append(" AND ap1.dataAndamento <= to_date(:dataFinalIncluirAndamentos, 'DD/MM/YYYY HH24:MI:SS') ");

			hql.append(" AND ap1.objetoIncidente.id = oips.id ) ");

		}

		if (listaNaoIncluirAndamentos != null
				&& listaNaoIncluirAndamentos.size() > 0) {
			hql.append(" AND NOT EXISTS( SELECT ap2.descricaoObservacaoAndamento "
					+ " FROM AndamentoProcesso ap2"
					+ " WHERE ap2.tipoAndamento.id IN( ");
			int ivalor = 1;
			for (Andamento tipoAndamento : listaNaoIncluirAndamentos) {
				if (ivalor < listaNaoIncluirAndamentos.size()) {
					hql.append(tipoAndamento.getId() + ",");
				} else {
					hql.append(tipoAndamento.getId() + ")");
				}
				ivalor++;
			}

			if (dataInicialNaoIncluirAndamentos != null)
				hql.append(" AND ap2.dataAndamento >= to_date(:dataInicialNaoIncluirAndamentos, 'DD/MM/YYYY HH24:MI:SS') ");

			if (dataFinalNaoIncluirAndamentos != null)
				hql.append(" AND ap2.dataAndamento <= to_date(:dataFinalNaoIncluirAndamentos, 'DD/MM/YYYY HH24:MI:SS') ");

			hql.append(" AND ap2.objetoIncidente = oips.id ) ");
		}

//		if (groupBy) {
//
//			if (groupByFase) {
//				if (semFase == null || !semFase.booleanValue()) {
//					if (groupByFaseStatus) {
//						hql.append(" GROUP BY ps.faseAtual.tipoFaseSetor.descricao, ps.faseAtual.tipoStatusSetor.descricao ");
//					} else {
//						hql.append(" GROUP BY ps.faseAtual.tipoFaseSetor.descricao ");
//					}
//					hql.append(" ORDER BY ps.faseAtual.tipoFaseSetor.descricao ASC ");
//				}
//
//			} else if (groupByDistribuicao) {
//				if (semDistribuicao == null || !semDistribuicao.booleanValue()) {
//					hql.append(" GROUP BY ps.distribuicaoAtual.usuario.nome "
//							+ " ORDER BY ps.distribuicaoAtual.usuario.nome ");
//				}
//			} else if (groupByDeslocamento) {
//				if (semLocalizacao == null || !semLocalizacao.booleanValue()) {
//
//					hql.append(" GROUP BY sd.descricao "
//							+ " ORDER BY sd.descricao ");
//
//				}
//			} else if (groupByAssunto != null && groupByAssunto.booleanValue()) {
//
//				hql.append(" GROUP BY ah.descricaoCompleta ");
//								//+ " ORDER BY ps.processoSTF.assunto.descricaoCompleta ");
//			}
//		} else {
		
		
		
		
		
			if (count == null || !count.booleanValue()
					&& ((idCategoriaPartePesquisa == null || idCategoriaPartePesquisa <= 0) && (nomeParte == null || nomeParte.trim().length() <= 0) 
							&& (dataDistribuicaoMinistroInicial == null && dataDistribuicaoMinistroFinal == null)) 
			) {
				if ( (orderByProcesso != null && orderByProcesso.booleanValue()) && (protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox) ) {
					if (orderByCrescente != null && !orderByCrescente.booleanValue()) {
						hql.append(" ORDER BY processoSTF.siglaClasseProcessual DESC , processoSTF.numeroProcessual DESC ");
					} else {
						hql.append(" ORDER BY processoSTF.siglaClasseProcessual , processoSTF.numeroProcessual ");
					}
				} else if (orderByProtocolo != null	&& orderByProtocolo.booleanValue()) {
					if (orderByCrescente != null && !orderByCrescente.booleanValue()) {
						hql.append(" ORDER BY protocoloSTF.anoProtocolo DESC , protocoloSTF.numeroProtocolo DESC ");
					} else {
						hql.append(" ORDER BY protocoloSTF.anoProtocolo , protocoloSTF.numeroProtocolo ");
					}
				} else if (orderByValorGut != null && orderByValorGut.booleanValue()) {
					if (orderByCrescente != null && !orderByCrescente.booleanValue()) {
						hql.append(" ORDER BY NVL((ps.valorGravidade * ps.valorTendencia * ps.valorUrgencia),0) DESC ");
					} else {
						hql.append(" ORDER BY NVL((ps.valorGravidade * ps.valorTendencia * ps.valorUrgencia),0) ");
					}
				} else if (orderByDataEntrada != null
						&& orderByDataEntrada.booleanValue()) {
					if (orderByCrescente != null && !orderByCrescente.booleanValue()) {
						hql.append(" ORDER BY ps.dataEntrada DESC ");
					} else {
						hql.append(" ORDER BY ps.dataEntrada ");
					}
				} else if (orderByAssunto != null && orderByAssunto.booleanValue()) {

					if (orderByCrescente != null && !orderByCrescente.booleanValue()) {
						hql.append(" ORDER BY ah.descricaoCompleta DESC, "
										//+ " processoSTF.siglaClasseProcessual DESC , processoSTF.numeroProcessual DESC, "
										+ " protocoloSTF.anoProtocolo DESC , protocoloSTF.numeroProtocolo DESC ");
					} else {
						hql.append(" ORDER BY ah.descricaoCompleta, "
								//+ " processoSTF.siglaClasseProcessual, processoSTF.numeroProcessual, "
								+ " protocoloSTF.anoProtocolo , protocoloSTF.numeroProtocolo ");

					}
				}
			}
//		}

		/*
		 * ---------------------------------- QUERY & PARAMETERS
		 * ---------------------------------
		 */

		Session session = retrieveSession();
		Query query = session.createQuery(hql.toString());
//		System.out.println("SQL HIBERNATE: " + hql.toString());
		
		if( protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox.booleanValue() ) {
			if( siglasClassesProcessuaisAgrupadas == null || siglasClassesProcessuaisAgrupadas.trim().length() <= 0 ) {
				if (sigla != null && !sigla.equals(""))
					query.setString("sigla", sigla);
			}
	
			if (numeroProcesso != null && numeroProcesso > 0)
				query.setLong("numeroProcesso", numeroProcesso);
			
			if (recurso != null) {
				if (recurso > 0)
					query.setShort("recurso", recurso);
			}

			if (siglaRecursoUnificada != null
					&& siglaRecursoUnificada.trim().length() > 0)
				query.setString("siglaRecursoUnificada", siglaRecursoUnificada);

			if (siglaTipoJulgamento != null && siglaTipoJulgamento.trim().length() > 0 && !siglaTipoJulgamento.equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla()))
				query.setString("siglaTipoJulgamento", siglaTipoJulgamento);
			

			if (idTipoTarefa != null && idTipoTarefa > 0)
				query.setLong("idTipoTarefa", idTipoTarefa);
			
			if (codigoMinistroRelator != null && codigoMinistroRelator > 0)
				query.setLong("codigoMinistroRelator", codigoMinistroRelator);			
		}

		if (anoProtocolo != null && anoProtocolo > 0)
			query.setShort("anoProtocolo", anoProtocolo);

		if (numeroProtocolo != null && numeroProtocolo > 0)
			query.setLong("numeroProtocolo", numeroProtocolo);

		if (codigoTipoMeioProcesso != null
				&& codigoTipoMeioProcesso.trim().length() > 0)
			query.setString("codigoTipoMeioProcesso", codigoTipoMeioProcesso);

		if (siglaUsuarioDistribuicao != null
				&& siglaUsuarioDistribuicao.trim().length() > 0)
			query.setString("siglaUsuarioDistribuicao",
					siglaUsuarioDistribuicao);

		if (idSetor != null)
			query.setLong("idSetor", idSetor);
		
		if (dataDistribuicaoMinistroInicial != null)
			query.setString("dataDistribuicaoMinistroInicial", DateTimeHelper
					.getDataString(dataDistribuicaoMinistroInicial)
					+ " 00:00:00");

		if (dataDistribuicaoMinistroFinal != null)
			query.setString("dataDistribuicaoMinistroFinal", DateTimeHelper
					.getDataString(dataDistribuicaoMinistroFinal)
					+ " 23:59:59");

		if (dataDistribuicaoInicial != null)
			query.setString("dataDistribuicaoInicial", DateTimeHelper
					.getDataString(dataDistribuicaoInicial)
					+ " 00:00:00");

		if (dataDistribuicaoFinal != null)
			query.setString("dataDistribuicaoFinal", DateTimeHelper
					.getDataString(dataDistribuicaoFinal)
					+ " 23:59:59");

		if (dataFaseInicial != null)
			query.setString("dataFaseInicial", DateTimeHelper
					.getDataString(dataFaseInicial)
					+ " 00:00:00");

		if (dataFaseFinal != null)
			query.setString("dataFaseFinal", DateTimeHelper
					.getDataString(dataFaseFinal)
					+ " 23:59:59");

		if (dataRecebimentoInicial != null)
			query.setString("dataRecebimentoInicial", DateTimeHelper
					.getDataString(dataRecebimentoInicial)
					+ " 00:00:00");

		if (dataRecebimentoFinal != null)
			query.setString("dataRecebimentoFinal", DateTimeHelper
					.getDataString(dataRecebimentoFinal)
					+ " 23:59:59");

		if (dataRemessaInicial != null)
			query.setString("dataRemessaInicial", DateTimeHelper
					.getDataString(dataRemessaInicial)
					+ " 00:00:00");

		if (dataRemessaFinal != null)
			query.setString("dataRemessaFinal", DateTimeHelper
					.getDataString(dataRemessaFinal)
					+ " 23:59:59");

		if (dataEntradaInicial != null)
			query.setString("dataEntradaInicial", DateTimeHelper
					.getDataString(dataEntradaInicial)
					+ " 00:00:00");

		if (dataEntradaFinal != null)
			query.setString("dataEntradaFinal", DateTimeHelper
					.getDataString(dataEntradaFinal)
					+ " 23:59:59");

		if (dataSaidaInicial != null)
			query.setString("dataSaidaInicial", DateTimeHelper
					.getDataString(dataSaidaInicial)
					+ "00:00:00");
		
		if (dataSaidaFinal != null)
			query.setString("dataSaidaFinal", DateTimeHelper
					.getDataString(dataSaidaFinal)
					+ "23:59:59");
		
		if ( idCategoriaPartePesquisa != null && idCategoriaPartePesquisa > 0 )
			query.setLong("idCategoriaPartePesquisa", idCategoriaPartePesquisa);

		if (idGrupoProcessoSetor != null && idGrupoProcessoSetor > 0)
			query.setLong("idGrupoProcessoSetor", idGrupoProcessoSetor);

		if (idTipoUltimaFaseSetor != null && idTipoUltimaFaseSetor > 0)
			query.setLong("idTipoUltimaFaseSetor", idTipoUltimaFaseSetor);

		if (idTipoUltimoStatusSetor != null && idTipoUltimoStatusSetor > 0)
			query.setLong("idTipoUltimoStatusSetor", idTipoUltimoStatusSetor);

		if (idSecaoUltimoDeslocamento != null && idSecaoUltimoDeslocamento > 0)
			query.setLong("idSecaoUltimoDeslocamento",
					idSecaoUltimoDeslocamento);

		/*if (descricaoAssunto != null && descricaoAssunto.trim().length() >= 3)
			query.setString("descricaoAssunto", descricaoAssunto);*/

		// if( codigoAssunto != null && codigoAssunto.trim().length() == 9 )
		if (codigoAssunto != null)
			query.setString("codigoAssunto", codigoAssunto);

		if (complementoAssunto != null
				&& complementoAssunto.trim().length() > 0) {
			complementoAssunto = "%" + complementoAssunto.replace(' ', '%')
					+ "%";
			query.setString("complementoAssunto", complementoAssunto);
		}

		if(SearchData.stringNotEmpty(nomeParte)) {
			nomeParte = nomeParte.replace('%', ' ');
			query.setString("nomeParte", nomeParte);
		}
		
		if (dataInicialIncluirAndamentos != null
				&& listaIncluirAndamentos != null)
			query.setString("dataInicialIncluirAndamentos", DateTimeHelper
					.getDataString(dataInicialIncluirAndamentos)
					+ " 00:00:00");

		if (dataFinalIncluirAndamentos != null
				&& listaIncluirAndamentos != null)
			query.setString("dataFinalIncluirAndamentos", DateTimeHelper
					.getDataString(dataFinalIncluirAndamentos)
					+ " 23:59:59");

		if (dataInicialNaoIncluirAndamentos != null
				&& listaNaoIncluirAndamentos != null)
			query.setString("dataInicialNaoIncluirAndamentos", DateTimeHelper
					.getDataString(dataInicialNaoIncluirAndamentos)
					+ " 00:00:00");

		if (dataFinalNaoIncluirAndamentos != null
				&& listaNaoIncluirAndamentos != null)
			query.setString("dataFinalNaoIncluirAndamentos", DateTimeHelper
					.getDataString(dataFinalNaoIncluirAndamentos)
					+ " 23:59:59");

		if (readOnlyQuery != null && readOnlyQuery.booleanValue())
			query.setReadOnly(true);

		// if( groupBy )
		// query.setResultTransformer(Transformers.aliasToBean(EstatisticaProcessoSetor.class));
		// query = session.getNamedQuery("sutuacaoAtual");
		return query;
		
	}
	
	public List<ProcessoSetor> pesquisarProcessoSetor(String sigla, Long idSetor)
			throws DaoException {

		List<ProcessoSetor> processos = new LinkedList<ProcessoSetor>();

		PreparedStatement pstmt = null;

		ResultSet rs = null;

		try {
			Session session = retrieveSession();

			StringBuffer sql = new StringBuffer();

			sql.append(" SELECT  ps.sig_classe_proces, ps.dat_entrada_setor , "
					+ "         ps.num_processo, ps.tip_julgamento, ps.cod_recurso, "
					+ "         rp.cod_recurso, rp.dat_interposicao "
					+ "   FROM  egab.processo_setor ps LEFT JOIN stf.recurso_processos rp "
					+ "		   ON (ps.num_processo = rp.num_processo "
					+ "				    AND ps.sig_classe_proces = rp.sig_classe_proces "
					+ "				    AND ps.cod_recurso = rp.cod_recurso)"
					+ "   WHERE ps.cod_setor = " + idSetor
					+ "   	   AND ps.tip_julgamento = 'M' ");

			if (sigla != null && !sigla.trim().equals("")) {
				sql.append(" AND ps.sig_classe_proces =" + sigla);
			}
			sql.append("         AND ps.dat_saida_setor IS NULL "
					+ "         AND ps.dat_saida_setor IS NULL  "
					+ "		   AND ( "
					+ "	             (rp.dat_interposicao = ( "
					+ "	              SELECT MAX(dat_interposicao) "
					+ "	                FROM stf.recurso_processos "
					+ "	               WHERE sig_classe_proces = ps.sig_classe_proces "
					+ "	                     AND num_processo = ps.num_processo) "
					+ "				) "
					+ "				OR "
					+ "	            ( NOT EXISTS ( "
					+ "	               SELECT sig_classe_proces, num_processo "
					+ "	                 FROM stf.recurso_processos "
					+ "	                WHERE sig_classe_proces = ps.sig_classe_proces "
					+ "	                  AND num_processo = ps.num_processo) "
					+ "	            ) "
					+ "	         ) "
					+ "	       order by ps.sig_classe_proces, ps.dat_entrada_setor ");

			pstmt = session.connection().prepareStatement(sql.toString());

			rs = pstmt.executeQuery();

			while (rs.next()) {
				processos.add(montarListaProcessoSetor(rs));
			}

			session.clear();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (SQLException ex) {
			throw new DaoException("Erro ao executar o comando SQL", ex);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DaoException("SQLException Finally", e);
			}
		}

		return processos;

	}

	@SuppressWarnings("unchecked")
	private ProcessoSetor montarListaProcessoSetor(ResultSet rs) {

		ProcessoSetor processo = new ProcessoSetor();
		try {

			// processo.setNumeroProcessual(rs.getLong("num_processo"));
			// processo.setSiglaClasseProcessual(rs.getString("sig_classe_proces"));
			// processo.setCodigoRecurso(rs.getShort("cod_recurso"));
			processo.getObjetoIncidente().setId(
					rs.getLong("seq_objeto_incidente"));
			processo.setDataEntrada(rs.getDate("dat_entrada_setor"));

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return processo;
	}

	public List<EstatisticaProcessoSetor> pesquisarProcessoSetorEstatistica(
			String sigla, Long idSetor) throws DaoException {

		List<EstatisticaProcessoSetor> processos = new LinkedList<EstatisticaProcessoSetor>();

		PreparedStatement pstmt = null;

		ResultSet rs = null;

		try {
			Session session = retrieveSession();

			StringBuffer sql = new StringBuffer();

			sql.append(" SELECT  ps.sig_classe_proces, count(*) as count "
					+ "   FROM  egab.processo_setor ps LEFT JOIN stf.recurso_processos rp "
					+ "		  ON (ps.num_processo = rp.num_processo "
					+ "				    AND ps.sig_classe_proces = rp.sig_classe_proces "
					+ "				    AND ps.cod_recurso = rp.cod_recurso)"
					+ "   WHERE ps.cod_setor = " + idSetor
					+ "   	   AND ps.tip_julgamento = 'M' ");

			if (sigla != null && !sigla.trim().equals("")) {
				sql.append(" AND ps.sig_classe_proces =" + sigla);
			}
			sql.append("         AND ps.dat_saida_setor IS NULL "
					+ "         AND ps.dat_saida_setor IS NULL  "
					+ "		  AND ( "
					+ "	             (rp.dat_interposicao = ( "
					+ "	              SELECT MAX(dat_interposicao) "
					+ "	                FROM stf.recurso_processos "
					+ "	               WHERE sig_classe_proces = ps.sig_classe_proces "
					+ "	                     AND num_processo = ps.num_processo) "
					+ "				) "
					+ "				OR "
					+ "	            ( NOT EXISTS ( "
					+ "	               SELECT sig_classe_proces, num_processo "
					+ "	                 FROM stf.recurso_processos "
					+ "	                WHERE sig_classe_proces = ps.sig_classe_proces "
					+ "	                  AND num_processo = ps.num_processo) "
					+ "	            ) " + "	         ) "
					+ "          group by ps.sig_classe_proces "
					+ "	       order by ps.sig_classe_proces ");

			Connection con = session.connection();
			pstmt = con.prepareStatement(sql.toString());

			rs = pstmt.executeQuery();

			while (rs.next()) {
				processos.add(montarListaProcessoSetorEstatistica(rs));
			}

			session.clear();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (SQLException ex) {
			throw new DaoException("Erro ao executar o comando SQL", ex);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DaoException("SQLException Finally", e);
			}
		}

		return processos;

	}

	private EstatisticaProcessoSetor montarListaProcessoSetorEstatistica(
			ResultSet rs) {

		EstatisticaProcessoSetor estatistica = new EstatisticaProcessoSetor();
		try {

			estatistica.setDescricao1(rs.getString("sig_classe_process"));
			estatistica.setQuantidade(rs.getLong("count"));

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return estatistica;

	}

	public Boolean isProcessoProtocoloPosseUsuario(Long idProcessoSetor,
			String siglaUsuario, Long idSetorUsuario) throws DaoException {

		Boolean isProcessoPosseUsuario = null;

		try {

			Session sessao = retrieveSession();

			StringBuffer hql = new StringBuffer();

			hql.append("SELECT ps FROM ProcessoSetor ps" + " WHERE 1=1");

			hql.append(" AND ps.id = " + idProcessoSetor);

			hql.append(" AND (ps.distribuicaoAtual.usuario.id = '"
					+ siglaUsuario + "'");

			hql.append(" OR ps.deslocamentoAtual.secaoDestino.id = (SELECT se.id FROM Secao se"
					+ " LEFT JOIN se.secoesSetor ss"
					+ " LEFT JOIN ss.usuarios us"
					+ " WHERE us.sigla = + '"
					+ siglaUsuario
					+ "'"
					+ " AND ss.setor.id = "
					+ idSetorUsuario + "))");

			Query query = sessao.createQuery(hql.toString());

			ProcessoSetor processoSetor = (ProcessoSetor) query.uniqueResult();

			if (processoSetor != null)
				isProcessoPosseUsuario = Boolean.TRUE;
			else
				isProcessoPosseUsuario = Boolean.FALSE;
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return isProcessoPosseUsuario;
	}

	@SuppressWarnings("unused")
	private Boolean possuiGroupBy(TipoGroupBy tipo) {
		Boolean possui = Boolean.FALSE;

		if (tipo != null
				&& (tipo.equals(TipoGroupBy.FASE)
						|| tipo.equals(TipoGroupBy.FASE_STATUS)
						|| tipo.equals(TipoGroupBy.DISTRIBUICAO)
						|| tipo.equals(TipoGroupBy.DESLOCAMENTO) || tipo
						.equals(TipoGroupBy.ASSUNTO))) {
			possui = Boolean.TRUE;
		}

		return possui;
	}

	private StringBuffer createQuerySQLNativo(ProcessoSetorSearchData pssd)
			throws DaoException {

		try {

			StringBuffer sql = new StringBuffer();
			Boolean group = possuiGroupBy(pssd.tipoGroupBy);

			sql.append(" SELECT ");
			if (group) {
				if (pssd.tipoGroupBy.equals(TipoGroupBy.DISTRIBUICAO)) {
					sql.append(" usu.nom_usuario AS usu_distribuicao, COUNT(DISTINCT ps.seq_processo_setor) quantidade ");
				} else if ((pssd.tipoGroupBy.equals(TipoGroupBy.FASE) || pssd.tipoGroupBy
						.equals(TipoGroupBy.FASE_STATUS))
						&& (pssd.semFase == null || !pssd.semFase)) {
					if (pssd.tipoGroupBy.equals(TipoGroupBy.FASE_STATUS))
						sql.append(" tfs.dsc_tipo_fase_setor AS descricao_tipo_fase_setor, tss.dsc_Tipo_status_setor AS descricao_tipo_status_setor, "
								+ " COUNT(DISTINCT ps.seq_processo_setor) quantidade");
					else
						sql.append(" tfs.dsc_tipo_fase_setor AS descricao_tipo_fase_setor, COUNT(DISTINCT ps.seq_processo_setor) quantidade ");
				} else if (pssd.tipoGroupBy.equals(TipoGroupBy.DESLOCAMENTO)
						&& (pssd.semLocalizacao == null || !pssd.semLocalizacao)) {
					sql.append(" s.dsc_secao AS descricao_secao_destino, COUNT(DISTINCT ps.seq_processo_setor) quantidade ");
				} else if (pssd.tipoGroupBy.equals(TipoGroupBy.ASSUNTO)) {
					sql.append(" ass.dsc_assunto_completo AS descricao_assunto_completo, COUNT(DISTINCT ps.seq_processo_setor) quantidade ");
				}
			} else {
				sql.append(" DISTINCT ");

				sql.append(" protocolo.num_protocolo AS numero_protocolo, ");
				sql.append(" protocolo.ano_protocolo AS ano_protocolo, ");
				sql.append(" ps.dsc_complemento_assunto AS assunto_egab, ");
				sql.append(" ps.seq_processo_setor AS seq_processo_setor, ");
				sql.append(" ps.seq_objeto_incidente AS seq_objeto_incidente, ");
				sql.append(" oips.tip_objeto_incidente AS tip_objeto_incidente, ");
				sql.append(" TO_CHAR ( ps.dat_entrada_setor ,'DD/MM/YYYY') AS data_entrada_setor, ");
				sql.append(" ps.num_indicador_gravidade, ");
				sql.append(" ps.num_indicador_tendencia, ");
				sql.append(" ps.num_indicador_urgencia ");

				if (pssd.protocoloNaoAutuado == null
						|| !pssd.protocoloNaoAutuado) {
					sql.append(" , processoSTF.sig_classe_proces AS sigla_classe, ");
					sql.append(" processoSTF.num_processo AS numero_processo, ");
					sql.append(" (select rpsig.sig_cadeia_incidente from judiciario.recurso_processo rpsig "
							+ " where	rpsig.seq_objeto_incidente = oips.seq_objeto_incidente) as sig_cadeia_incidente_rp, ");
					sql.append(" (select ijsig.sig_cadeia_incidente from judiciario.incidente_julgamento ijsig "
							+ " where	ijsig.seq_objeto_incidente = oips.seq_objeto_incidente) as sig_cadeia_incidente_ij, ");
					sql.append(" tr.sig_tipo_recurso AS tipo_julgamento, ");
					sql.append(" processoSTF.tip_meio_processo AS tipo_processo ");
				}

				if ((pssd.tipoRelatorio != null)
						&& (pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_ASSUNTO)
								|| pssd.tipoRelatorio
										.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DESLOCAMENTO) || pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))) {
					// sql.append(" , ass.dsc_assunto_completo AS assunto_completo, ");
					if (pssd.protocoloNaoAutuado == null
							|| !pssd.protocoloNaoAutuado)
						sql.append(" ,ta.quantidade_assunto_processo ");

					sql.append(" ,ta.quantidade_assunto_protocolo ");
				}

				if (pssd.tipoRelatorio != null
						&& (pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DESLOCAMENTO) || pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))) {
					sql.append(" , ( SELECT s.dsc_secao FROM egab.secao s WHERE seq_secao = hd.seq_secao_origem ) AS secao_origem, ");
					sql.append(" ( SELECT s.dsc_secao FROM egab.secao s WHERE seq_secao = hd.seq_secao_destino ) AS secao_destino, ");
					sql.append(" TO_CHAR( hd.dat_remessa, 'DD/MM/YYYY' ) AS data_remessa, ");
					sql.append(" TO_CHAR( hd.dat_recebimento, 'DD/MM/YYYY' ) AS data_recebimento, ");
					sql.append(" hd.num_sala AS numero_sala, ");
					sql.append(" hd.num_armario AS numero_armario, ");
					sql.append(" hd.num_estante AS numero_estante, ");
					sql.append(" hd.num_prateleira AS numero_prateleira, ");
					sql.append(" hd.num_coluna AS numero_coluna, ");
					sql.append(" hd.obs_deslocamento AS obs_deslocamento, ");
					sql.append(" ps.dsc_complemento_assunto AS assunto_interno,");
					sql.append(" ps.obs_processo_setor AS observacao_processo");
				}

				if (pssd.tipoRelatorio != null
						&& (pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DISTRIBUICAO) || pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))) {
					sql.append(" , usu.nom_usuario AS usu_distribuicao, ");
					sql.append(" grupos_usuario.grupo_usuario, ");
					sql.append(" TO_CHAR( hdist.dat_distribuicao, 'DD/MM/YYYY' ) AS data_distribuicao ");
				}

				if (pssd.tipoRelatorio != null
						&& (pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_FASE) || pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))) {
					sql.append(" , tfs.dsc_tipo_fase_setor AS descricao_tipo_fase_setor, ");
					sql.append(" tss.dsc_Tipo_status_setor AS descricao_tipo_status_setor, ");
					sql.append(" usu.sig_usuario AS sigla_usu_distribuicao, ");
					sql.append(" hf.dsc_observacao AS obs_fase ");
				}

				if (pssd.tipoRelatorio != null
						&& pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)) {
					sql.append(" , tgp.quantidade_grupo_processo AS quantidade_grupo_processo ");
				}
				
				if (pssd.tipoRelatorio != null && pssd.tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)) {
					if (pssd.origem != null && pssd.origem > 0) {
						sql.append(", ori.dsc_origem "); 
					} else{
						sql.append(", (select dsc_origem from judiciario.origem ori "); 
						sql.append(" where ori.cod_origem = (CASE ");
						sql.append("                      WHEN (SELECT COUNT (*) ");
						sql.append("                              FROM judiciario.historico_processo_origem hpoaux ");
						sql.append("                              WHERE hpoaux.seq_objeto_incidente = oips.seq_objeto_incidente_principal ");
						sql.append("                              AND hpoaux.tip_historico = 'O' ");
						sql.append("                              AND hpoaux.flg_principal = 'S') > 0 ");
						sql.append("                      THEN ");
						sql.append("                             (SELECT MIN (hpoaux.cod_origem) ");
						sql.append("                              FROM judiciario.historico_processo_origem hpoaux ");
						sql.append("                              WHERE hpoaux.seq_objeto_incidente = oips.seq_objeto_incidente_principal ");
						sql.append("                              AND hpoaux.tip_historico = 'O' ");
					    sql.append("                              AND hpoaux.flg_principal = 'S') ");
					    sql.append("                      WHEN (SELECT COUNT (*) ");
					    sql.append("                              FROM judiciario.historico_processo_origem hpoaux ");
				        sql.append("                              WHERE hpoaux.seq_objeto_incidente = oips.seq_objeto_incidente_principal ");
				  	    sql.append("                              AND hpoaux.tip_historico = 'O' ");
					    sql.append("                              AND hpoaux.flg_principal = 'N') > 0 ");
					    sql.append("                      THEN ");
					    sql.append("                           (SELECT MIN (hpoaux.cod_origem) ");
					    sql.append("                            FROM judiciario.historico_processo_origem hpoaux ");
					    sql.append("                            WHERE hpoaux.seq_objeto_incidente = oips.seq_objeto_incidente_principal ");
					    sql.append("                            AND hpoaux.tip_historico = 'O' ");
					    sql.append("                            AND hpoaux.flg_principal = 'N') ");
					    sql.append("                      ELSE ");
					    sql.append("                           NULL ");
					    sql.append("                      END)) as dsc_origem ");
					}
					
					String ordemTema = "ASC";
					if (pssd.tipoOrderProcesso.equals(TipoOrdemProcesso.TEMA) && pssd.tipoOrdem.equals(TipoOrdem.DECRESCENTE))
						ordemTema = "DESC";
						
					// Adiciona o tema
					sql.append(" , (SELECT LISTAGG('Tema: ' || t.NUM_TEMA, '; ') WITHIN GROUP (ORDER BY t.dsc_tema "+ordemTema+") FROM julgamento.processo_tema pt INNER JOIN judiciario.tema t ON t.SEQ_TEMA = pt.SEQ_TEMA WHERE pt.seq_objeto_incidente = oips.seq_objeto_incidente_principal) as dsc_tema ");
					
					String ordemMotivo = "ASC";
					if (pssd.tipoOrderProcesso.equals(TipoOrdemProcesso.TEMA) && pssd.tipoOrdem.equals(TipoOrdem.DECRESCENTE))
						ordemMotivo = "DESC";
					
					// Adiciona os motivos de inaptidão
					sql.append(" , (SELECT LISTAGG(mi.DSC_MOTIVO_INAPTIDAO, '; ') WITHIN GROUP (ORDER BY mi.DSC_MOTIVO_INAPTIDAO "+ordemMotivo+") FROM JUDICIARIO.PROCESSO_MOTIVO_INAPTIDAO pmi INNER JOIN JUDICIARIO.MOTIVO_INAPTIDAO mi ON mi.COD_MOTIVO_INAPTIDAO = pmi.COD_MOTIVO_INAPTIDAO WHERE pmi.SEQ_OBJETO_INCIDENTE = oips.seq_objeto_incidente_principal) as dsc_motivo_inaptidao ");
				}
			}

			sql.append(" FROM egab.processo_setor ps ");
			sql.append(" INNER JOIN judiciario.objeto_incidente oips ON ps.seq_objeto_incidente = oips.seq_objeto_incidente ");
			sql.append(" INNER JOIN judiciario.processo proc ON oips.seq_objeto_incidente_principal = proc.seq_objeto_incidente ");
			
			if (pssd.tipoRelatorio != null && pssd.tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)) {
				if (pssd.origem != null && pssd.origem > 0) {
					sql.append(" LEFT JOIN judiciario.historico_processo_origem hpo ON hpo.seq_objeto_incidente = oips.seq_objeto_incidente_principal ");
					sql.append(" LEFT JOIN judiciario.origem ori ON hpo.cod_origem = ori.cod_origem ");
				}
			}
			if (pssd.protocoloNaoAutuado == null || !pssd.protocoloNaoAutuado) {
				sql.append(" LEFT JOIN judiciario.objeto_incidente oipsprinc ON oipsprinc.seq_objeto_incidente = oips.seq_objeto_incidente_principal ");
				sql.append(" LEFT JOIN judiciario.objeto_incidente oipsant ON oipsant.seq_objeto_incidente = oipsprinc.seq_objeto_incidente_anterior ");
				sql.append(" LEFT JOIN judiciario.vw_processo_relator processoSTF ON oipsprinc.seq_objeto_Incidente = processoSTF.seq_objeto_Incidente ");
				sql.append(" LEFT JOIN judiciario.objeto_incidente oirp ON oipsprinc.seq_objeto_incidente = oirp.seq_objeto_incidente_principal ");
				sql.append(" LEFT JOIN judiciario.recurso_processo rp ON oirp.seq_objeto_Incidente = rp.seq_objeto_Incidente ");
				sql.append(" LEFT JOIN judiciario.incidente_julgamento ij ON oips.seq_objeto_Incidente = ij.seq_objeto_Incidente ");
				sql.append(" LEFT JOIN judiciario.tipo_recurso tr ON ij.seq_tipo_recurso = tr.seq_tipo_recurso ");

				if (pssd.idTipoTarefa != null && pssd.idTipoTarefa > 0) {
					sql.append(" LEFT JOIN egab.tarefa_atribuida_processo tap on processoSTF.seq_objeto_incidente = tap.seq_objeto_incidente ");
					sql.append(" LEFT JOIN egab.tarefa_setor ts on ts.seq_tarefa_setor = tap.seq_tarefa_setor ");
				}

				if (pssd.siglasClassesProcessuaisAgrupadas != null
						&& pssd.siglasClassesProcessuaisAgrupadas.trim()
								.length() > 0) {
					sql.append(" LEFT JOIN judiciario.classe clp ON ps.sig_classe_proces = clp.sig_classe ");
				}
			}

			if (pssd.protocoloNaoAutuado == null || !pssd.protocoloNaoAutuado)
				sql.append(" LEFT JOIN stf.processo_protocolados protocolo ON oipsant.seq_objeto_Incidente = protocolo.seq_objeto_Incidente ");
			else
				sql.append(" LEFT JOIN stf.processo_protocolados protocolo ON oips.seq_objeto_Incidente = protocolo.seq_objeto_Incidente ");

			if ((pssd.tipoRelatorio != null && (pssd.tipoRelatorio
					.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DESLOCAMENTO)
					|| pssd.tipoRelatorio
							.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_DESLOCAMENTO) || pssd.tipoRelatorio
					.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)))
					|| (pssd.idSecaoUltimoDeslocamento != null)
					|| (SearchData.stringNotEmpty(pssd.numeroSala))
					|| (SearchData.stringNotEmpty(pssd.numeroArmario))
					|| (SearchData.stringNotEmpty(pssd.numeroEstante))
					|| (SearchData.stringNotEmpty(pssd.numeroPrateleira))
					|| (SearchData.stringNotEmpty(pssd.numeroColuna))
					|| (SearchData.hasDate(pssd.dataRemessa))
					|| (SearchData.hasDate(pssd.dataRecebimento))
					|| (SearchData.stringNotEmpty(pssd.obsDeslocamento))) {
				sql.append(" LEFT JOIN egab.historico_deslocamento hd ON ps.seq_deslocamento_atual = hd.seq_historico_deslocamento ");
			}

			if ((pssd.tipoRelatorio != null && (pssd.tipoRelatorio
					.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_FASE)
					|| pssd.tipoRelatorio
							.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_FASE) || pssd.tipoRelatorio
					.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)))
					|| (pssd.idTipoUltimaFaseSetor != null)
					|| (SearchData.hasDate(pssd.dataFase))
					|| (pssd.semFase != null)) {
				sql.append(" LEFT JOIN egab.historico_fase hf ON ps.seq_fase_atual = hf.seq_historico_fase AND ps.seq_processo_setor = hf.seq_processo_setor ");
			}

			if ((pssd.tipoRelatorio != null && (pssd.tipoRelatorio
					.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_FASE)
					|| pssd.tipoRelatorio
							.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_FASE) || pssd.tipoRelatorio
					.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)))
					|| (pssd.idTipoUltimaFaseSetor != null)) {
				sql.append(" LEFT JOIN egab.tipo_fase_setor tfs ON hf.seq_tipo_fase_setor = tfs.seq_tipo_fase_setor ");
			}

			if ((pssd.tipoRelatorio != null && (pssd.tipoRelatorio
					.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_FASE)
					|| pssd.tipoRelatorio
							.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_FASE) || pssd.tipoRelatorio
					.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)))
					|| (pssd.idTipoUltimoStatusSetor != null)) {
				sql.append(" LEFT JOIN egab.tipo_status_setor tss ON hf.seq_tipo_status_setor = tss.seq_tipo_status_setor ");
			}

			if ((pssd.tipoRelatorio != null && (pssd.tipoRelatorio
					.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DISTRIBUICAO)
					|| pssd.tipoRelatorio
							.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_DISTRIBUICAO)
					|| pssd.tipoRelatorio
							.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO) || pssd.tipoRelatorio
					.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_FASE)))
					|| (SearchData.hasDate(pssd.dataDistribuicao))
					|| (SearchData
							.stringNotEmpty(pssd.siglaUsuarioDistribuicao))																																	
							|| (pssd.processosDistribuidosForaDoSetor != null && pssd.processosDistribuidosForaDoSetor.booleanValue())
							|| (pssd.processosDistribuidosInativos  != null && pssd.processosDistribuidosInativos.booleanValue())) {
				sql.append(" LEFT JOIN egab.historico_distribuicao hdist ON ps.seq_distribuicao_atual = hdist.seq_historico_distribuicao ");
			}
	
			if ((pssd.tipoRelatorio != null && (pssd.tipoRelatorio
					.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DISTRIBUICAO)
					|| pssd.tipoRelatorio
							.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_DISTRIBUICAO)
					|| pssd.tipoRelatorio
							.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_FASE) || pssd.tipoRelatorio
					.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)))
					|| (SearchData
							.stringNotEmpty(pssd.siglaUsuarioDistribuicao))							
			|| (pssd.processosDistribuidosInativos  != null && pssd.processosDistribuidosInativos.booleanValue())
			) {
				sql.append(" LEFT JOIN stf.usuarios usu ON hdist.sig_usuario_analise = usu.sig_usuario ");
			}

			if (pssd.tipoRelatorio != null
					&& (pssd.tipoRelatorio
							.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DISTRIBUICAO)
							|| pssd.tipoRelatorio
									.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_DISTRIBUICAO) || pssd.tipoRelatorio
							.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))) {
				sql.append(" LEFT JOIN (SELECT LTRIM "
						+ " (MAX (SYS_CONNECT_BY_PATH (dsc_grupo, '\\'))KEEP (DENSE_RANK LAST ORDER BY curr), ', ' "
						+ " ) AS grupo_usuario, des.SIG_USUARIO "
						+ " FROM (SELECT a.seq_grupo_usuario, sig_usuario, dsc_grupo, "
						+ " ROW_NUMBER () OVER (PARTITION BY sig_usuario ORDER BY a.seq_grupo_usuario DESC) AS curr, "
						+ " ROW_NUMBER () OVER (PARTITION BY sig_usuario ORDER BY a.seq_grupo_usuario DESC) - 1 AS prev "
						+ " FROM egab.USUARIO_grupo a "
						+ " JOIN egab.grupo_usuario gu on gu.seq_grupo_usuario = a.seq_grupo_usuario) des "
						+ " GROUP BY SIG_USUARIO "
						+ " CONNECT BY prev = PRIOR curr "
						+ " AND SIG_USUARIO = PRIOR SIG_USUARIO "
						+ " START WITH curr = 1) grupos_usuario ON grupos_usuario.sig_usuario = usu.sig_usuario ");
			}

			if (pssd.codigosAssuntosVirgula != null
					|| (pssd.descricaoAssunto != null && pssd.descricaoAssunto
							.trim().length() >= 3)
					|| (pssd.tipoRelatorio != null && pssd.tipoRelatorio
							.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_ASSUNTO))) {
				if (pssd.protocoloNaoAutuado == null
						|| !pssd.protocoloNaoAutuado)
					sql.append(" LEFT JOIN stf.assunto_processo ap ON oipsprinc.seq_objeto_incidente = ap.seq_objeto_incidente ");
				else
					sql.append(" LEFT JOIN stf.assunto_processo ap ON oips.seq_objeto_incidente = ap.seq_objeto_incidente ");
				sql.append(" LEFT JOIN stf.assuntos ass ON ass.cod_assunto = ap.cod_assunto ");
			}

			if ((pssd.tipoRelatorio != null && (pssd.tipoRelatorio
					.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_ASSUNTO)
					|| pssd.tipoRelatorio
							.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_ASSUNTO)
					|| pssd.tipoRelatorio
							.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DESLOCAMENTO)
					|| pssd.tipoRelatorio
							.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_DESLOCAMENTO) || pssd.tipoRelatorio
					.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)))
					|| (pssd.tipoOrderProcesso
							.equals(TipoOrdemProcesso.ASSUNTO))
					|| (pssd.codigosAssuntosVirgula != null)
					|| (pssd.descricaoAssunto != null && pssd.descricaoAssunto
							.trim().length() >= 3)) {
				if (pssd.protocoloNaoAutuado == null
						|| !pssd.protocoloNaoAutuado)
					sql.append(" LEFT JOIN (SELECT sig_classe_proces, num_processo, COUNT(*) AS quantidade_assunto_processo "
							+ " FROM stf.assunto_processo GROUP BY sig_classe_proces, num_processo) ta ON ta.num_processo = processoSTF.num_processo  "
							+ "AND ta.sig_classe_proces = processoSTF.sig_classe_proces ");
				sql.append(" LEFT JOIN (SELECT ano_protocolo, num_protocolo, COUNT(*) AS quantidade_assunto_protocolo "
						+ " FROM stf.assunto_processo GROUP BY ano_protocolo, num_protocolo) ta ON ta.num_protocolo = protocolo.num_protocolo  AND "
						+ "ta.ano_protocolo = protocolo.ano_protocolo ");
			}

			if (pssd.idGrupoProcessoSetor != null
					&& pssd.idGrupoProcessoSetor > 0) {
				sql.append(" LEFT JOIN egab.processo_setor_grupo psg ON ps.seq_objeto_incidente = psg.seq_objeto_incidente ");
				sql.append(" LEFT JOIN egab.grupo_processo_setor gps ON ps.cod_setor = gps.cod_setor AND gps.seq_grupo_processo_setor = psg.seq_grupo_processo_setor ");
			}

			if (pssd.tipoRelatorio != null
					&& pssd.tipoRelatorio
							.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)) {
				sql.append(" LEFT JOIN (SELECT seq_objeto_incidente, COUNT (*) AS quantidade_grupo_processo "
						+ "                  FROM egab.processo_setor_grupo psgqtd "
						+ "                  GROUP BY seq_objeto_incidente) tgp ON ps.seq_objeto_incidente = tgp.seq_objeto_incidente ");
			}

			if ((pssd.idSecaoUltimoDeslocamento != null && pssd.idSecaoUltimoDeslocamento > 0)
					|| (pssd.tipoRelatorio != null && pssd.tipoRelatorio
							.equals(ProcessoSetor.RELATORIO_SINTETICO_DESLOCAMENTO))) {
				sql.append(" LEFT JOIN egab.secao s ON hd.seq_secao_destino = s.seq_secao ");
			}

			if (SearchData.hasDate(pssd.dataDistribuicaoMinistro)) {
				sql.append(" LEFT JOIN stf.sit_min_processos smp ON oips.seq_objeto_incidente = smp.seq_objeto_incidente ");
			}

			if (pssd.semVista != null) {
				sql.append(" LEFT JOIN stf.ministros mns ON ps.cod_setor = mns.cod_setor ");
				sql.append(" LEFT JOIN stf.agendamentos ag ON oips.seq_objeto_incidente = ag.seq_objeto_incidente ");
			}

			if ((pssd.idCategoriaPartePesquisa != null && pssd.idCategoriaPartePesquisa > 0)
					|| SearchData.stringNotEmpty(pssd.nomeParte)) {
				sql.append(" LEFT JOIN judiciario.vw_jurisdicionado_incidente cp ON oips.seq_objeto_incidente = cp.seq_objeto_incidente ");
			}

			if (pssd.idCategoriaPartePesquisa != null
					&& pssd.idCategoriaPartePesquisa > 0) {
				sql.append(" LEFT JOIN judiciario.categoria c ON cp.cod_categoria = c.cod_categoria ");
			}

			if (pssd.getPesquisaLegislacao()) {
				sql.append(" LEFT JOIN stf.legislacao_processo lp ON oips.seq_objeto_incidente = lp.seq_objeto_incidente ");
			}
			
			if (TipoOrdemProcesso.TEMA.equals(pssd.tipoOrderProcesso)) {
				sql.append(" LEFT JOIN JULGAMENTO.PROCESSO_TEMA processoTema ON processoTema.seq_objeto_incidente = ps.seq_objeto_incidente ");
				sql.append(" LEFT JOIN JUDICIARIO.TEMA tema ON tema.seq_tema = processoTema.seq_tema ");
			}
			
			if (TipoOrdemProcesso.MOTIVO_INAPTIDAO.equals(pssd.tipoOrderProcesso) || (pssd.motivoInaptidao != null && pssd.motivoInaptidao.size() > 0)) {
				sql.append(" LEFT JOIN JUDICIARIO.PROCESSO_MOTIVO_INAPTIDAO processoMotivoInaptidao ON processoMotivoInaptidao.seq_objeto_incidente = ps.seq_objeto_incidente ");
				sql.append(" LEFT JOIN JUDICIARIO.MOTIVO_INAPTIDAO mi ON mi.COD_MOTIVO_INAPTIDAO = processoMotivoInaptidao.COD_MOTIVO_INAPTIDAO ");
			}
			
			sql.append(" WHERE ( 1 = 1 ) ");
			
			if (TipoOrdemProcesso.TEMA.equals(pssd.tipoOrderProcesso)) {
				if (TipoOrdem.CRESCENTE.equals(pssd.tipoOrdem))
					sql.append(" AND (tema.NUM_TEMA = (SELECT min(t.NUM_TEMA) FROM JULGAMENTO.PROCESSO_TEMA pt INNER JOIN JUDICIARIO.TEMA t on t.seq_tema = pt.seq_tema WHERE pt.seq_objeto_incidente = ps.seq_objeto_incidente AND t.COD_TIPO_TEMA = 1) OR tema.NUM_TEMA IS NULL) ");
				else
					sql.append(" AND (tema.NUM_TEMA = (SELECT max(t.NUM_TEMA) FROM JULGAMENTO.PROCESSO_TEMA pt INNER JOIN JUDICIARIO.TEMA t on t.seq_tema = pt.seq_tema WHERE pt.seq_objeto_incidente = ps.seq_objeto_incidente AND t.COD_TIPO_TEMA = 1) OR tema.NUM_TEMA IS NULL) ");
			}
			
			if (TipoOrdemProcesso.MOTIVO_INAPTIDAO.equals(pssd.tipoOrderProcesso)) {
				if (TipoOrdem.CRESCENTE.equals(pssd.tipoOrdem))
					sql.append(" AND (mi.COD_MOTIVO_INAPTIDAO = (SELECT min(mip.COD_MOTIVO_INAPTIDAO) FROM JUDICIARIO.PROCESSO_MOTIVO_INAPTIDAO mip WHERE mip.SEQ_OBJETO_INCIDENTE = ps.SEQ_OBJETO_INCIDENTE) OR mi.COD_MOTIVO_INAPTIDAO IS NULL) ");
				else
					sql.append(" AND (mi.COD_MOTIVO_INAPTIDAO = (SELECT max(mip.COD_MOTIVO_INAPTIDAO) FROM JUDICIARIO.PROCESSO_MOTIVO_INAPTIDAO mip WHERE mip.SEQ_OBJETO_INCIDENTE = ps.SEQ_OBJETO_INCIDENTE) OR mi.COD_MOTIVO_INAPTIDAO IS NULL) ");
			}
			
			if (pssd.motivoInaptidao != null && pssd.motivoInaptidao.size() > 0) {
				
				sql.append(" AND mi.COD_MOTIVO_INAPTIDAO IN("+StringUtils.join(pssd.motivoInaptidao, ',')+") ");
				sql.append(" AND NOT EXISTS (SELECT mip.COD_MOTIVO_INAPTIDAO FROM JUDICIARIO.PROCESSO_MOTIVO_INAPTIDAO mip WHERE mip.seq_objeto_incidente = ps.seq_objeto_incidente AND mip.COD_MOTIVO_INAPTIDAO NOT IN ("+StringUtils.join(pssd.motivoInaptidao, ',')+"))");
			}
			
			sql.append(" AND CASE WHEN (oips.tip_objeto_incidente = 'RC') THEN (select COUNT(rp1.seq_objeto_incidente) from judiciario.recurso_processo rp1 where rp1.seq_objeto_incidente = oips.seq_objeto_incidente AND rp1.flg_ativo = 'S') ");
			sql.append(" WHEN (oips.tip_objeto_incidente = 'IJ') THEN (select COUNT(ij1.seq_objeto_incidente) from judiciario.incidente_julgamento ij1 where ij1.seq_objeto_incidente = oips.seq_objeto_incidente AND ij1.flg_ativo = 'S') ");
			sql.append(" WHEN (oips.tip_objeto_incidente = 'PR') THEN 1 ELSE 0 END = 1 ");
			
			
			if (pssd.tipoRelatorio != null && pssd.tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)) {
				if (pssd.origem != null && pssd.origem > 0) {
					sql.append(" AND hpo.cod_origem = " + pssd.origem);
					sql.append(" AND hpo.seq_historico_processo_origem = (CASE ");
					sql.append("                      WHEN (SELECT COUNT (*) ");
					sql.append("                              FROM judiciario.historico_processo_origem hpoaux ");
					sql.append("                              WHERE hpoaux.seq_objeto_incidente = oips.seq_objeto_incidente_principal ");
					sql.append("                              AND hpoaux.tip_historico = 'O' ");
					sql.append("                              AND hpoaux.flg_principal = 'S') > 0 ");
					sql.append("                      THEN ");
					sql.append("                             (SELECT MIN (hpoaux.seq_historico_processo_origem) ");
					sql.append("                              FROM judiciario.historico_processo_origem hpoaux ");
					sql.append("                              WHERE hpoaux.seq_objeto_incidente = oips.seq_objeto_incidente_principal ");
					sql.append("                              AND hpoaux.tip_historico = 'O' ");
				    sql.append("                              AND hpoaux.flg_principal = 'S') ");
				    sql.append("                      WHEN (SELECT COUNT (*) ");
				    sql.append("                              FROM judiciario.historico_processo_origem hpoaux ");
			        sql.append("                              WHERE hpoaux.seq_objeto_incidente = oips.seq_objeto_incidente_principal ");
			  	    sql.append("                              AND hpoaux.tip_historico = 'O' ");
				    sql.append("                              AND hpoaux.flg_principal = 'N') > 0 ");
				    sql.append("                      THEN ");
				    sql.append("                           (SELECT MIN (hpoaux.seq_historico_processo_origem) ");
				    sql.append("                            FROM judiciario.historico_processo_origem hpoaux ");
				    sql.append("                            WHERE hpoaux.seq_objeto_incidente = oips.seq_objeto_incidente_principal ");
				    sql.append("                            AND hpoaux.tip_historico = 'O' ");
				    sql.append("                            AND hpoaux.flg_principal = 'N') ");
				    sql.append("                      ELSE ");
				    sql.append("                           NULL ");
				    sql.append("                      END) ");
				}
			}
			
			if (pssd.mostraProcessoReautuadoRejeitado == null 
					|| !pssd.mostraProcessoReautuadoRejeitado.booleanValue())
				sql.append(" AND (select cod_situacao from judiciario.processo where seq_objeto_incidente = oips.seq_objeto_incidente_principal) not in ('J','W','K')  ");
		
			if (pssd.protocoloNaoAutuado == null
					|| !pssd.protocoloNaoAutuado.booleanValue()) {

				if (pssd.siglasClassesProcessuaisAgrupadas != null
						&& pssd.siglasClassesProcessuaisAgrupadas.trim()
								.length() > 0) {
					if (pssd.siglasClassesProcessuaisAgrupadas
							.equals(Classe.SIGLAS_CLASSES_PROCESSUAIS_AI_RE)) {
						sql.append(" AND processoSTF.sig_classe_proces IN ('RE', 'AI') ");
					} else if (pssd.siglasClassesProcessuaisAgrupadas
							.equals(Classe.SIGLAS_CLASSES_PROCESSUAIS_ORIGINARIOS)) {
						sql.append(" AND clp.flg_originario = 'S' ");
					} else if (pssd.siglasClassesProcessuaisAgrupadas
							.equals(Classe.SIGLAS_CLASSES_PROCESSUAIS_ADI_ADC_ADO_ADPF)) {
						sql.append(" AND processoSTF.sig_classe_proces IN ('ADI', 'ADC', 'ADO', 'ADPF') ");
					}
				} else if (SearchData.stringNotEmpty(pssd.sigla))
					sql.append(" AND processoSTF.sig_classe_proces = '"
							+ pssd.sigla + "' ");

				if (SearchData
						.stringNotEmpty(pssd.classesProcessuaisPorVirgula)) {
					sql.append(" AND processoSTF.sig_classe_proces in ('"
							+ pssd.classesProcessuaisPorVirgula.replace(",",
									"','").replace(" ", "") + "')");
				}

				if (pssd.numeroProcesso != null && pssd.numeroProcesso > 0)
					sql.append(" AND processoSTF.num_processo = "
							+ pssd.numeroProcesso);

				if (pssd.possuiRecurso != null) {
					if (pssd.possuiRecurso) {
						sql.append(" AND ");
					} else if (!pssd.possuiRecurso) {
						sql.append(" AND NOT ");
					}
					sql.append(" EXISTS (SELECT rp2.seq_objeto_incidente FROM judiciario.recurso_processo rp2 WHERE rp2.seq_objeto_incidente = oips.seq_objeto_incidente ) ");
				}

				if (pssd.recurso != null) {
					sql.append(" AND rp.cod_recurso = " + pssd.recurso);
				}

				if (SearchData.stringNotEmpty(pssd.siglaRecursoUnificada))
					sql.append(" AND EXISTS (SELECT rp3.seq_objeto_incidente FROM judiciario.recurso_processo rp3 WHERE  "
							+ "oips.seq_objeto_incidente = rp3.seq_objeto_incidente AND rp3.sig_cadeia_incidente LIKE '%"
							+ pssd.siglaRecursoUnificada + "%' ) ");

				if (SearchData.stringNotEmpty(pssd.siglaTipoJulgamento)) {
					if (!pssd.siglaTipoJulgamento
							.equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO
									.getSigla()))
						sql.append(" AND ij.tip_julgamento = '"
								+ pssd.siglaTipoJulgamento + "' ");
					else
						sql.append(" AND NOT EXISTS (SELECT ij2.seq_objeto_incidente FROM judiciario.incidente_julgamento ij2 "
								+ " WHERE oips.seq_objeto_incidente = ij2.seq_objeto_incidente) ");
				}

				if (SearchData.stringNotEmpty(pssd.codigoTipoMeioProcesso))
					sql.append(" AND processoSTF.tip_meio_processo = '"
							+ pssd.codigoTipoMeioProcesso + "' ");

				// Repercussão Geral = S
				if (pssd.repercussaoGeral != null
						&& pssd.repercussaoGeral.booleanValue()) {
					sql.append(" AND processoSTF.flg_repercussao_geral = 'S' ");
				}
				
				// Processos Distribuidos a usuários fora do setor
				if (pssd.processosDistribuidosForaDoSetor != null && pssd.processosDistribuidosForaDoSetor.booleanValue())					
					sql.append (" AND hdist.sig_usuario_analise NOT IN (select ush.sig_usuario from egab.usuario_setor ush where ush.cod_setor ="+ pssd.idSetor+") ");							
				
				// Processos Distribuidos a usuários inativos
				if (pssd.processosDistribuidosInativos  != null && pssd.processosDistribuidosInativos.booleanValue())
					sql.append (" AND usu.flg_ativo = 'N'");
					
					


				// tarefa
				if (pssd.idTipoTarefa != null && pssd.idTipoTarefa > 0) {
					sql.append(" AND ts.seq_tipo_tarefa = " + pssd.idTipoTarefa);
				}

				// ministro relator
				if (pssd.codigoMinistroRelator != null
						&& pssd.codigoMinistroRelator > 0) {
					sql.append(" AND processoSTF.cod_relator_atual = "
							+ pssd.codigoMinistroRelator);
				}

			} else if (pssd.protocoloNaoAutuado != null
					&& pssd.protocoloNaoAutuado) {

				// protocolo não autuado
				if (pssd.protocoloNaoAutuado != null
						&& pssd.protocoloNaoAutuado.booleanValue()) {
					sql.append(" AND oips.tip_objeto_incidente = 'PI' "
							+ " AND NOT EXISTS (SELECT oiptna.seq_objeto_incidente FROM judiciario.objeto_incidente oiptna WHERE "
							+ " oiptna.seq_objeto_incidente_anterior = oips.seq_objeto_incidente) ");
				}

				if (SearchData.stringNotEmpty(pssd.codigoTipoMeioProcesso))
					sql.append(" AND protocolo.tip_meio_processo = '"
							+ pssd.codigoTipoMeioProcesso + "' ");

			}

			if (pssd.anoProtocolo != null && pssd.anoProtocolo > 0)
				sql.append(" AND protocolo.ano_protocolo = "
						+ pssd.anoProtocolo);

			if (pssd.numeroProtocolo != null && pssd.numeroProtocolo > 0)
				sql.append(" AND protocolo.num_protocolo = "
						+ pssd.numeroProtocolo);

			// codigoSetor
			if (pssd.idSetor != null)
				sql.append(" AND ps.cod_setor = " + pssd.idSetor);

			// idGrupoProcessoSetor
			if (pssd.idGrupoProcessoSetor != null
					&& pssd.idGrupoProcessoSetor > 0)
				sql.append(" AND gps.seq_grupo_processo_setor = "
						+ pssd.idGrupoProcessoSetor);

			// idSecaoUltimoDeslocamento
			if (pssd.idSecaoUltimoDeslocamento != null
					&& pssd.idSecaoUltimoDeslocamento > 0)
				sql.append(" AND s.seq_secao = "
						+ pssd.idSecaoUltimoDeslocamento);

			// dataEntradaInicial
			if (SearchData.hasDate(pssd.dataEntrada)) {
				if (pssd.dataEntrada.getInitialDate() != null)
					sql.append(" AND ps.dat_entrada_setor >= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(pssd.dataEntrada.getInitialDate())
							+ " 00:00:00" + "', 'DD/MM/YYYY HH24:MI:SS') ");

				// dataEntradaFinal
				if (pssd.dataEntrada.getFinalDate() != null)
					sql.append(" AND ps.dat_entrada_setor <= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(pssd.dataEntrada.getFinalDate())
							+ " 23:59:59" + "', 'DD/MM/YYYY HH24:MI:SS') ");
			}

			// dataSaidaInicial
			if (SearchData.hasDate(pssd.dataSaida)) {

				if (pssd.dataSaida.getInitialDate() != null)
					sql.append("AND ps.dat_saida_setor >= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(pssd.dataSaida.getInitialDate())
							+ "00:00:00" + "', 'DD/MM/YYYY HH24:MI:SS')");

				// dataSaidaFinal
				if (pssd.dataSaida.getFinalDate() != null)
					sql.append("AND ps.dat_saida_setor <= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(pssd.dataSaida.getFinalDate())
							+ "23:59:59" + "', 'DD/MM/YYYY HH24:MI:SS')");
			}

			// localizados no setor
			if (pssd.localizadosNoSetor != null) {
				if (pssd.localizadosNoSetor.booleanValue())
					sql.append(" AND ps.dat_saida_setor IS NULL ");
				else
					sql.append(" AND ps.dat_saida_setor is NOT NULL AND (proc.flg_tramitacao = 'S'  or proc.flg_tramitacao is null) ");
			}else{
				sql.append(" AND ( proc.flg_tramitacao = 'S' or proc.flg_tramitacao is null )  "); 
			}

			// emTramiteNoSetor
			if (pssd.emTramiteNoSetor != null)
				if (pssd.emTramiteNoSetor.booleanValue())
					sql.append(" AND ps.dat_fim_tramite IS NULL ");
				else
					sql.append(" AND ps.dat_fim_tramite IS NOT NULL ");

			// possuiLiminar
			if (pssd.possuiLiminar != null)
				if (pssd.possuiLiminar.booleanValue())
					sql.append(" AND ps.flg_liminar = 'S' ");
				else
					sql.append(" AND ps.flg_liminar = 'N' ");

			// possuiPreferencia
			if (pssd.possuiPreferencia != null)
				if (pssd.possuiPreferencia.booleanValue())
					sql.append(" AND ps.flg_preferencia = 'S' ");
				else
					sql.append(" AND ps.flg_preferencia = 'N' ");

			// sobrestado
			if (pssd.sobrestado != null)
				if (pssd.sobrestado.booleanValue())
					sql.append(" AND ps.flg_sobrestado = 'N' ");
				else
					sql.append(" AND ps.flg_sobrestado = 'S' ");

			// assunto
			if (pssd.descricaoAssunto != null
					&& pssd.descricaoAssunto.trim().length() >= 3) {
				pssd.descricaoAssunto = pssd.descricaoAssunto.replace("|",
						"\\|");
				pssd.descricaoAssunto = pssd.descricaoAssunto.replace('%', ' ');
				pssd.descricaoAssunto = pssd.descricaoAssunto.replace('$', ' ');
				sql.append(" AND CONTAINS(ass.dsc_assunto_completo, '"
						+ pssd.descricaoAssunto + "') > 1 ");
			}
			
			if (!pssd.codigosAssuntosVirgula.isEmpty()) {
				sql.append(" AND ap.cod_assunto IN (");
				String assuntos = pssd.codigosAssuntosVirgula.trim();
				String[] codAssuntos = assuntos.split(",");				
				if (codAssuntos.length != 1){
					int i;
					for ( i = 0; i < codAssuntos.length - 1; i++ ){
						sql.append("'" + codAssuntos[i].trim() + "',");
					}
					sql.append("'" + codAssuntos[i++].trim() + "')");
				} else {
					sql.append("'" + codAssuntos[0].trim() + "')");
				}
			}
			
			/*
			if (SearchData.stringNotEmpty(pssd.codigosAssuntosVirgula)) {
				sql.append(" AND ass.cod_assunto = '" + pssd.codigosAssuntosVirgula
						+ "' ");
			} */

			if (SearchData.stringNotEmpty(pssd.complementoAssunto)) {
				pssd.complementoAssunto = "%"
						+ pssd.complementoAssunto.replace(' ', '%') + "%";
				sql.append(" AND ps.dsc_complemento_assunto LIKE '"
						+ pssd.complementoAssunto + "' ");
			}

			// Parâmetros de fase
			if (pssd.semFase != null && pssd.semFase.booleanValue()) {
				sql.append(" AND ps.seq_fase_atual IS NULL ");
			} else {
				if (pssd.semFase != null && !pssd.semFase.booleanValue())
					sql.append(" AND ps.seq_fase_atual IS NOT NULL ");

				if (pssd.idTipoUltimaFaseSetor != null
						&& pssd.idTipoUltimaFaseSetor > 0)
					sql.append(" AND tfs.seq_tipo_fase_setor = "
							+ pssd.idTipoUltimaFaseSetor);

				if (pssd.idTipoUltimoStatusSetor != null
						&& pssd.idTipoUltimoStatusSetor > 0)
					sql.append(" AND tss.seq_tipo_status_setor = "
							+ pssd.idTipoUltimoStatusSetor);

				if (SearchData.hasDate(pssd.dataFase)) {
					if (pssd.dataFase.getInitialDate() != null)
						sql.append(" AND hf.dat_historico_fase >= to_date('"
								+ new SimpleDateFormat("dd/MM/yyyy")
										.format(pssd.dataFase.getInitialDate())
								+ " 00:00:00" + "', 'DD/MM/YYYY HH24:MI:SS') ");

					if (pssd.dataFase.getFinalDate() != null)
						sql.append(" AND hf.dat_historico_fase <= to_date('"
								+ new SimpleDateFormat("dd/MM/yyyy")
										.format(pssd.dataFase.getFinalDate())
								+ " 23:59:59" + "', 'DD/MM/YYYY HH24:MI:SS') ");
				}
			}

			// Parâmetros de distribuição
			if (pssd.semDistribuicao != null
					&& pssd.semDistribuicao.booleanValue()) {
				sql.append(" AND ps.seq_distribuicao_atual IS NULL ");
			} else {
				if (pssd.semDistribuicao != null
						&& !pssd.semDistribuicao.booleanValue())
					sql.append(" AND ps.seq_distribuicao_atual IS NOT NULL ");

				if (SearchData.stringNotEmpty(pssd.siglaUsuarioDistribuicao))
					sql.append(" AND usu.sig_usuario = '"
							+ pssd.siglaUsuarioDistribuicao + "' ");

				if (SearchData.hasDate(pssd.dataDistribuicao)) {
					if (pssd.dataDistribuicao.getInitialDate() != null)
						sql.append(" AND hdist.dat_distribuicao >= to_date('"
								+ new SimpleDateFormat("dd/MM/yyyy")
										.format(pssd.dataDistribuicao
												.getInitialDate())
								+ " 00:00:00" + "', 'DD/MM/YYYY HH24:MI:SS') ");

					if (pssd.dataDistribuicao.getFinalDate() != null)
						sql.append(" AND hdist.dat_distribuicao <= to_date('"
								+ new SimpleDateFormat("dd/MM/yyyy")
										.format(pssd.dataDistribuicao
												.getFinalDate()) + " 23:59:59"
								+ "', 'DD/MM/YYYY HH24:MI:SS') ");
				}
			}

			// Distribuição para o Relator
			if (SearchData.hasDate(pssd.dataDistribuicaoMinistro)) {
				if (pssd.dataDistribuicaoMinistro.getInitialDate() != null)
					sql.append(" AND smp.DAT_OCORRENCIA >= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(pssd.dataDistribuicaoMinistro
											.getInitialDate()) + " 00:00:00"
							+ "', 'DD/MM/YYYY HH24:MI:SS') ");

				if (pssd.dataDistribuicaoMinistro.getFinalDate() != null)
					sql.append(" AND smp.DAT_OCORRENCIA <= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(pssd.dataDistribuicaoMinistro
											.getFinalDate()) + " 23:59:59"
							+ "', 'DD/MM/YYYY HH24:MI:SS') ");

				sql.append(" AND smp.COD_OCORRENCIA IN ('RE', 'SU', 'RG') ");
				sql.append(" AND smp.flg_relator_atual = 'S' ");
			}

			// Última situação do processo
			if (pssd.faseProcessualAtual != null && pssd.faseProcessualAtual) {
				sql.append(" AND ( EXISTS ( SELECT rpfpa.seq_objeto_incidente FROM judiciario.recurso_processo rpfpa "
						+ "                    WHERE rpfpa.dat_interposicao = ( SELECT MAX(rpfpa2.dat_interposicao) "
						+ "                                                          FROM judiciario.recurso_processo rpfpa2 "
						+ "                                                          WHERE  rpfpa2.sig_classe_proces = rpfpa.sig_classe_proces"
						+ "																  		 AND rpfpa2.num_processo = rpfpa.num_processo ) "
						+ "                          AND rpfpa.seq_objeto_incidente = ps.seq_objeto_incidente "
						+ "               ) "
						+ "        OR EXISTS ( SELECT pfpa.seq_objeto_incidente FROM judiciario.processo pfpa "
						+ "                         WHERE pfpa.seq_objeto_incidente = oips.seq_objeto_incidente "
						+ "                               AND NOT EXISTS ( SELECT oifpa.seq_objeto_incidente "
						+ "                                                    FROM judiciario.objeto_incidente oifpa "
						+ "              			                               WHERE oifpa.seq_objeto_incidente_principal = pfpa.seq_objeto_incidente "
						+ "                                                             AND oifpa.tip_objeto_incidente = 'RC') "
						+ "                   ) " + "    ) ");
			}

			// categorias da parte
			if (pssd.idCategoriaPartePesquisa != null
					&& pssd.idCategoriaPartePesquisa > 0)
				sql.append(" AND cp.cod_categoria = "
						+ pssd.idCategoriaPartePesquisa);

			// nome da parte 
			/*
			if (SearchData.stringNotEmpty(pssd.nomeParte)) {
				nomeParte = '%' + nomeParte.replace(' ', '%') + '%';
				// nomeParte = nomeParte.toUpperCase();
				// sql.append(" AND cpp.nom_parte LIKE '" + nomeParte + "' ");
				pssd.nomeParte = pssd.nomeParte.replace('%', ' ');
				sql.append(" AND CONTAINS(cp.nom_jurisdicionado, '"
						+ pssd.nomeParte + "') > 1 ");
			}*/
			
			// nome da parte
			if (SearchData.stringNotEmpty(pssd.nomeParte)) {
				pssd.nomeParte = "%"
						+ pssd.nomeParte.replace(' ', '%')
								.toUpperCase() + "%";
				sql.append(" AND UPPER(cp.nom_jurisdicionado) LIKE '" + pssd.nomeParte + "' ");
			}

			// Parâmetros de deslocamento
			if (pssd.semLocalizacao != null
					&& pssd.semLocalizacao.booleanValue()) {
				sql.append(" AND ps.seq_deslocamento_atual IS NULL ");
			} else {
				if (pssd.semLocalizacao != null
						&& !pssd.semLocalizacao.booleanValue())
					sql.append(" AND ps.seq_deslocamento_atual IS NOT NULL ");

				if ((SearchData.stringNotEmpty(pssd.numeroSala))
						|| (SearchData.stringNotEmpty(pssd.numeroArmario))
						|| (SearchData.stringNotEmpty(pssd.numeroEstante))
						|| (SearchData.stringNotEmpty(pssd.numeroPrateleira))
						|| (SearchData.stringNotEmpty(pssd.numeroColuna))) {
					sql.append(" AND CONTAINS (hd.flg_atualizado,' ");

					if (SearchData.stringNotEmpty(pssd.numeroSala))
						sql.append(" (%" + pssd.numeroSala
								+ "% within NUM_SALA) AND ");

					if (SearchData.stringNotEmpty(pssd.numeroArmario))
						sql.append(" (%" + pssd.numeroArmario
								+ "% within NUM_ARMARIO) AND ");

					if (SearchData.stringNotEmpty(pssd.numeroEstante))
						sql.append(" (%" + pssd.numeroEstante
								+ "% within NUM_ESTANTE) AND ");

					if (SearchData.stringNotEmpty(pssd.numeroPrateleira))
						sql.append(" (%" + pssd.numeroPrateleira
								+ "% within NUM_PRATELEIRA) AND ");

					if (SearchData.stringNotEmpty(pssd.numeroColuna))
						sql.append(" (%" + pssd.numeroColuna
								+ "% within NUM_COLUNA) ");

					// verifica se existe AND por último, caso exista retira o
					// mesmo
					if (sql.substring(sql.length() - 6, sql.length()).equals(
							") AND ")) {
						StringBuffer sqlTemp = new StringBuffer(sql.substring(
								0, sql.length() - 4));
						sql.delete(0, sql.length());
						sql.append(sqlTemp);
					}

					sql.append(" ' ) > 0 ");

				}

				if (SearchData.hasDate(pssd.dataRemessa)) {
					if (pssd.dataRemessa.getInitialDate() != null)
						sql.append(" AND hd.dat_remessa >= to_date('"
								+ new SimpleDateFormat("dd/MM/yyyy")
										.format(pssd.dataRemessa
												.getInitialDate())
								+ " 00:00:00" + "', 'DD/MM/YYYY HH24:MI:SS') ");

					if (pssd.dataRemessa.getFinalDate() != null)
						sql.append(" AND hd.dat_remessa <= to_date('"
								+ new SimpleDateFormat("dd/MM/yyyy")
										.format(pssd.dataRemessa.getFinalDate())
								+ " 23:59:59" + "', 'DD/MM/YYYY HH24:MI:SS') ");
				}

				if (SearchData.hasDate(pssd.dataRecebimento)) {
					if (pssd.dataRecebimento.getInitialDate() != null)
						sql.append(" AND hd.dat_recebimento >= to_date('"
								+ new SimpleDateFormat("dd/MM/yyyy")
										.format(pssd.dataRecebimento
												.getInitialDate())
								+ " 00:00:00" + "', 'DD/MM/YYYY HH24:MI:SS') ");

					if (pssd.dataRecebimento.getFinalDate() != null)
						sql.append(" AND hd.dat_recebimento <= to_date('"
								+ new SimpleDateFormat("dd/MM/yyyy")
										.format(pssd.dataRecebimento
												.getFinalDate()) + " 23:59:59"
								+ "', 'DD/MM/YYYY HH24:MI:SS') ");
				}
			}

			// julgado
			if (pssd.julgado != null) {
				sql.append(" AND ");
				if (!pssd.julgado.booleanValue()) {
					sql.append(" NOT ");
				}

				sql.append(" (EXISTS (SELECT cvt.seq_objeto_incidente "
						+ " FROM stf.controle_votos cvt "
						+ " WHERE cvt.cod_tipo_texto = "
						+ TipoTexto.EMENTA.getCodigo()
						+ " AND cvt.cod_ministro = ( select s.cod_ministro from stf.sit_min_processos s "
						+ " where s.seq_objeto_incidente = cvt.seq_objeto_incidente and "
						+ " s.cod_ocorrencia in ('RE','SU','RG') and s.FLG_RELATOR_ATUAL = 'S' ) "
						+ " AND cvt.seq_objeto_incidente = ps.seq_objeto_incidente)) ");
			}

			// observacao contida no deslocamento atual
			if (SearchData.stringNotEmpty(pssd.obsDeslocamento)) {
				// obsDeslocamento = "%" + obsDeslocamento.replace(' ', '%') +
				// "%";
				pssd.obsDeslocamento = pssd.obsDeslocamento.replace('%', ' ');
				sql.append(" AND CONTAINS(hd.obs_deslocamento, '"
						+ pssd.obsDeslocamento + "') > 1 ");
			}

			// vista
			if (pssd.semVista != null) {
				sql.append(" AND ag.flg_vistas IS NOT NULL ");

				sql.append(" AND mns.cod_ministro = (SELECT m.cod_ministro FROM stf.ministros m WHERE m.cod_setor = "
						+ pssd.idSetor + " AND m.dat_afast_ministro IS NULL) ");

				if (pssd.semVista.booleanValue())
					sql.append(" AND ag.flg_vistas = 'S' ");
				else
					sql.append(" AND ag.flg_vistas = 'N' ");
			}

			// inicio Filtro andamentos
			if (SearchData.stringNotEmpty(pssd.andamentosProcessuais)) {
				sql.append(" AND EXISTS( SELECT ap1.DSC_OBSER_AND "
						+ " FROM stf.ANDAMENTO_PROCESSOS ap1 "
						+ " LEFT JOIN stf.ANDAMENTOS an1 ON ap1.cod_andamento = an1.cod_andamento "
						+ " WHERE an1.cod_andamento IN("
						+ pssd.andamentosProcessuais + ")");

				if (SearchData.hasDate(pssd.dataAndamentos)) {
					if (pssd.dataAndamentos.getInitialDate() != null) {
						sql.append(" AND ap1.dat_andamento >= to_date('"
								+ new SimpleDateFormat("dd/MM/yyyy")
										.format(pssd.dataAndamentos
												.getInitialDate())
								+ " 00:00:00" + "', 'DD/MM/YYYY HH24:MI:SS') ");
					}

					if (pssd.dataAndamentos.getFinalDate() != null) {
						sql.append(" AND ap1.dat_andamento <= to_date('"
								+ new SimpleDateFormat("dd/MM/yyyy")
										.format(pssd.dataAndamentos
												.getFinalDate()) + " 23:59:59"
								+ "', 'DD/MM/YYYY HH24:MI:SS') ");
					}
				}

				sql.append(" AND ap1.seq_objeto_incidente = ps.seq_objeto_incidente ) ");

			}

			// início: legislação
			if (pssd.normaProcesso != null && pssd.normaProcesso > 0) {
				sql.append(" AND lp.cod_norma = " + pssd.normaProcesso + " ");
			}

			if (pssd.numeroAno != null && pssd.numeroAno > 0) {
				sql.append(" AND lp.ano_legislacao = " + pssd.numeroAno + " ");
			}

			if (pssd.numeroLegislacao != null && pssd.numeroLegislacao > 0) {
				sql.append(" AND lp.num_legislacao = " + pssd.numeroLegislacao
						+ " ");
			}

			if (SearchData.stringNotEmpty(pssd.numeroArtigo)) {
				sql.append(" AND lp.artigo = '" + pssd.numeroArtigo + "' ");
			}

			if (SearchData.stringNotEmpty(pssd.numeroInciso)) {
				sql.append(" AND lp.inciso = '" + pssd.numeroInciso + "' ");
			}

			if (SearchData.stringNotEmpty(pssd.numeroParagrafo)) {
				sql.append(" AND lp.paragrafo = '" + pssd.numeroParagrafo
						+ "' ");
			}

			if (SearchData.stringNotEmpty(pssd.numeroAlinea)) {
				sql.append(" AND lp.alinea = '" + pssd.numeroAlinea + "' ");
			}
			// fim: legislação

			if (group) {
				if (pssd.tipoGroupBy.equals(TipoGroupBy.DISTRIBUICAO)) {
					sql.append(" AND usu.nom_usuario IS NOT NULL GROUP BY usu.nom_usuario ORDER BY usu.nom_usuario ");
				} else if ((pssd.tipoGroupBy.equals(TipoGroupBy.FASE) || pssd.tipoGroupBy
						.equals(TipoGroupBy.FASE_STATUS))
						&& (pssd.semFase == null || !pssd.semFase)) {
					if (pssd.tipoGroupBy.equals(TipoGroupBy.FASE_STATUS)) {
						sql.append(" AND tfs.dsc_tipo_fase_setor IS NOT NULL GROUP BY tfs.dsc_tipo_fase_setor, tss.dsc_Tipo_status_setor");
					} else {
						sql.append(" AND tfs.dsc_tipo_fase_setor IS NOT NULL GROUP BY tfs.dsc_tipo_fase_setor");
					}

					sql.append(" ORDER BY tfs.dsc_tipo_fase_setor ASC ");
				} else if (pssd.tipoGroupBy.equals(TipoGroupBy.DESLOCAMENTO)
						&& (pssd.semLocalizacao == null || !pssd.semLocalizacao)) {
					sql.append(" AND s.dsc_secao IS NOT NULL GROUP BY s.dsc_secao ORDER BY s.dsc_secao ");
				} else if (pssd.tipoGroupBy.equals(TipoGroupBy.ASSUNTO)) {
					sql.append(" AND ass.dsc_assunto_completo IS NOT NULL GROUP BY ass.dsc_assunto_completo ORDER BY ass.dsc_assunto_completo ");
				}

			} else if (pssd.tipoOrderProcesso
					.equals(TipoOrdemProcesso.PROCESSO)
					&& (pssd.protocoloNaoAutuado == null || !pssd.protocoloNaoAutuado)) {
				if (pssd.tipoOrdem.equals(TipoOrdem.DECRESCENTE)) {
					sql.append(" ORDER BY sigla_classe DESC, numero_processo DESC ");
				} else {
					sql.append(" ORDER BY sigla_classe, numero_processo ");
				}
			} else if (pssd.tipoOrderProcesso
					.equals(TipoOrdemProcesso.PROTOCOLO)) {
				if (pssd.tipoOrdem.equals(TipoOrdem.DECRESCENTE)) {
					sql.append(" ORDER BY protocolo.ano_protocolo DESC, protocolo.num_protocolo DESC ");
				} else {
					sql.append(" ORDER BY protocolo.ano_protocolo, protocolo.num_protocolo ");
				}
			} else if (pssd.tipoOrderProcesso
					.equals(TipoOrdemProcesso.VALOR_GUT)) {
				if (pssd.tipoOrdem.equals(TipoOrdem.DECRESCENTE)) {
					sql.append(" ORDER BY NVL((ps.num_indicador_gravidade * ps.num_indicador_tendencia * ps.num_indicador_urgencia),0) DESC ");
				} else {
					sql.append(" ORDER BY NVL((ps.num_indicador_gravidade * ps.num_indicador_tendencia * ps.num_indicador_urgencia),0) ");
				}
			} else if (pssd.tipoOrderProcesso
					.equals(TipoOrdemProcesso.DT_ENTRADA)) {
				if (pssd.tipoOrdem.equals(TipoOrdem.DECRESCENTE)) {
					sql.append(" ORDER BY data_entrada_setor DESC ");
				} else {
					sql.append(" ORDER BY data_entrada_setor ");
				}

			}

			return sql;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public List<EstatisticaProcessoSetor> pesquisarRelatorioSinteticoProcessoSetor(
			ProcessoSetorSearchData pssd) throws DaoException {

		List<EstatisticaProcessoSetor> listaRelatorio = new LinkedList<EstatisticaProcessoSetor>();
		Boolean group = possuiGroupBy(pssd.tipoGroupBy);

		StringBuffer sql = null;

		Statement stmt = null;
		ResultSet rs = null;

		try {

			sql = createQuerySQLNativo(pssd);

			Session sessao = retrieveSession();

			stmt = sessao.connection().createStatement();
			stmt.executeQuery(sql.toString());
			rs = stmt.getResultSet();
			int cont = 0;
			while (rs.next()) {
				EstatisticaProcessoSetor rel = new EstatisticaProcessoSetor();

				if (group) {
					if (pssd.tipoGroupBy.equals(TipoGroupBy.DISTRIBUICAO)) {
						rel.setDescricao1(rs.getString("usu_distribuicao"));
						rel.setQuantidade(rs.getLong("quantidade"));
					} else if ((pssd.tipoGroupBy.equals(TipoGroupBy.FASE) || pssd.tipoGroupBy
							.equals(TipoGroupBy.FASE_STATUS))
							&& (pssd.semFase == null || !pssd.semFase)) {
						if (pssd.tipoGroupBy.equals(TipoGroupBy.FASE_STATUS)) {
							rel.setDescricao1(rs
									.getString("descricao_tipo_fase_setor"));
							rel.setDescricao2(rs
									.getString("descricao_tipo_status_setor"));
							rel.setQuantidade(rs.getLong("quantidade"));
						} else {
							rel.setDescricao1(rs
									.getString("descricao_tipo_fase_setor"));
							rel.setQuantidade(rs.getLong("quantidade"));
						}
					} else if (pssd.tipoGroupBy
							.equals(TipoGroupBy.DESLOCAMENTO)
							&& (pssd.semLocalizacao == null || !pssd.semLocalizacao)) {
						rel.setDescricao1(rs
								.getString("descricao_secao_destino"));
						rel.setQuantidade(rs.getLong("quantidade"));
					} else if (pssd.tipoGroupBy.equals(TipoGroupBy.ASSUNTO)) {
						rel.setDescricao1(rs
								.getString("descricao_assunto_completo"));
						rel.setQuantidade(rs.getLong("quantidade"));
					}
				}

				listaRelatorio.add(rel);
			}

		}

		catch (SQLException e) {
			throw new DaoException("Erro ao executar o comando SQL", e);
		} finally {

			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DaoException("Erro ao executar o comando SQL", e);
			}

		}

		return listaRelatorio;
	}

	public List<RelatorioAnaliticoProcessoSetor> pesquisarRelatorioAnaliticoProcessoSetor(
			ProcessoSetorSearchData pssd) throws DaoException {

		List<RelatorioAnaliticoProcessoSetor> listaRelatorio = new LinkedList<RelatorioAnaliticoProcessoSetor>();

		StringBuffer sql = null;

		Statement stmt = null;
		ResultSet rs = null;

		try {

			sql = createQuerySQLNativo(pssd);

			Session sessao = retrieveSession();

			stmt = sessao.connection().createStatement();
			stmt.executeQuery(sql.toString());
			rs = stmt.getResultSet();
			int cont = 0;
			while (rs.next()) {
				RelatorioAnaliticoProcessoSetor rel = new RelatorioAnaliticoProcessoSetor();
				// rel.setIdentificacaoProcessual( rs.getString( "processo" ) );

				rel.setTipoObjetoIncidente(rs.getString("tip_objeto_incidente"));
				rel.setNumeroProtocolo(rs.getString("numero_protocolo"));
				rel.setAnoProtocolo(rs.getString("ano_protocolo"));
				rel.setAssuntoEGAB(rs.getString("assunto_egab"));
				rel.setSeqObjetoIncidente(rs.getString("seq_objeto_incidente"));
				rel.setDataEntrada(rs.getString("data_entrada_setor"));

				if (rel.getTipoObjetoIncidente().equals(
						TipoObjetoIncidente.PROCESSO.getCodigo())) {
					rel.setSiglaClasseUnificada(rs.getString("sigla_classe"));
					rel.setNumeroProcessual(rs.getString("numero_processo"));
					rel.setTipoJulgamento(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO
							.getSigla());
					rel.setTipoMeioProcesso(rs.getString("tipo_processo"));
				} else if (rel.getTipoObjetoIncidente().equals(
						TipoObjetoIncidente.RECURSO.getCodigo())) {
					rel.setSiglaClasseUnificada(rs.getString("sigla_classe"));
					rel.setSiglaCadeiaIncidente(rs
							.getString("sig_cadeia_incidente_rp"));
					rel.setNumeroProcessual(rs.getString("numero_processo"));
					rel.setTipoJulgamento(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO
							.getSigla());
					rel.setTipoMeioProcesso(rs.getString("tipo_processo"));
				} else if (rel.getTipoObjetoIncidente().equals(
						TipoObjetoIncidente.INCIDENTE_JULGAMENTO.getCodigo())) {
					rel.setSiglaClasseUnificada(rs.getString("sigla_classe"));
					rel.setSiglaCadeiaIncidente(rs
							.getString("sig_cadeia_incidente_ij"));
					rel.setNumeroProcessual(rs.getString("numero_processo"));
					rel.setTipoJulgamento(rs.getString("tipo_julgamento"));
					rel.setTipoMeioProcesso(rs.getString("tipo_processo"));
				}

				if (pssd.tipoRelatorio != null
						&& (pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_ASSUNTO)
								|| pssd.tipoRelatorio
										.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DESLOCAMENTO) || pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))) {
					String stringTotalAssuntos = null;

					if ((pssd.protocoloNaoAutuado == null || !pssd.protocoloNaoAutuado)
							&& SearchData.stringNotEmpty(rs
									.getString("quantidade_assunto_processo"))) {
						stringTotalAssuntos = rs
								.getString("quantidade_assunto_processo");
					} else if (SearchData.stringNotEmpty(rs
							.getString("quantidade_assunto_protocolo"))) {
						stringTotalAssuntos = rs
								.getString("quantidade_assunto_protocolo");
					}

					if (stringTotalAssuntos == null
							|| stringTotalAssuntos.trim().length() <= 0)
						stringTotalAssuntos = "0";

					Integer totalAssuntos = Integer
							.parseInt(stringTotalAssuntos);
					if (totalAssuntos != null && totalAssuntos > 0
							&& rel.getSeqObjetoIncidente() != null
							&& rel.getSeqObjetoIncidente().trim().length() > 0) {
						cont = cont + 1;
						rel.setDescricaoAssuntoCompletoSTF(recuperarTodosAssuntosProcessoSetor(
								rel.getSeqObjetoIncidente(),
								rel.getTipoObjetoIncidente()));
					}
					/*
					 * else rel.setDescricaoAssuntoCompletoSTF(rs.getString(
					 * "assunto_completo")); // assunto
					 */

				}

				if (pssd.tipoRelatorio != null
						&& (pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DESLOCAMENTO) || pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))) {
					rel.setSecaoOrigem(rs.getString("secao_origem"));
					rel.setSecaoDestino(rs.getString("secao_destino"));
					rel.setDataRemessa(rs.getString("data_remessa"));
					rel.setDataRecebimento(rs.getString("data_recebimento"));
					// dados de localização

					rel.setSala(rs.getString("numero_sala"));
					rel.setArmario(rs.getString("numero_armario"));
					rel.setEstante(rs.getString("numero_estante"));
					rel.setPrateleira(rs.getString("numero_prateleira"));
					rel.setColuna(rs.getString("numero_coluna"));

					rel.setObservacaoDeslocamento(rs
							.getString("obs_deslocamento")); // relatoriodeslocamentoanaliticolocalizacao
					rel.setAssuntoInterno(rs.getString("assunto_interno"));
					rel.setObservacaoProcessoSetor(rs
							.getString("observacao_processo"));

				}

				if (pssd.tipoRelatorio != null
						&& (pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DISTRIBUICAO) || pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))) {
					rel.setUsuarioDistribuicao(rs.getString("usu_distribuicao"));
					rel.setGrupoUsuarioDistribuicao(rs
							.getString("grupo_usuario"));
					rel.setDataDistribuicao(rs.getString("data_distribuicao"));
				}

				if (pssd.tipoRelatorio != null
						&& (pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_FASE) || pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))) {
					rel.setDescricaoTipoFaseSetor(rs
							.getString("descricao_tipo_fase_setor"));
					rel.setDescricaoTipoStatusSetor(rs
							.getString("descricao_tipo_status_setor"));
					rel.setSiglaUsuarioDistribuicao(rs
							.getString("sigla_usu_distribuicao"));
					rel.setObservacaoFase(rs.getString("obs_fase"));
				}

				if (pssd.tipoRelatorio != null
						&& pssd.tipoRelatorio
								.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)) {
					String stringTotalGruposProcesso = null;

					if (SearchData.stringNotEmpty(rs
							.getString("quantidade_grupo_processo"))) {
						stringTotalGruposProcesso = rs
								.getString("quantidade_assunto_processo");
					}
					
					if (SearchData.stringNotEmpty(rs.getString("dsc_origem"))) {
						rel.setOrigem(rs.getString("dsc_origem"));
					}
					
					if (SearchData.stringNotEmpty(rs.getString("dsc_tema"))) {
						rel.setTema(rs.getString("dsc_tema"));
					}
					
					if (SearchData.stringNotEmpty(rs.getString("dsc_motivo_inaptidao"))) {
						rel.setMotivoInaptidao(rs.getString("dsc_motivo_inaptidao"));
					}

					if (stringTotalGruposProcesso == null
							|| stringTotalGruposProcesso.trim().length() <= 0)
						stringTotalGruposProcesso = "0";

					Integer totalGruposProcesso = Integer
							.parseInt(stringTotalGruposProcesso);
					if ((totalGruposProcesso != null && totalGruposProcesso > 0)
							&& rel.getSeqObjetoIncidente().trim().length() > 0) {
						cont = cont + 1;
						rel.setNomeGrupoProcesso(recuperarGruposProcesso(
								pssd.idSetor,
								Long.parseLong(rel.getSeqObjetoIncidente())));
					}
				}

				listaRelatorio.add(rel);
			}
			if (pssd.tipoOrderProcesso.equals(TipoOrdemProcesso.ASSUNTO)) {
				if (pssd.tipoOrdem.equals(TipoOrdem.DECRESCENTE)) {
					Collections.sort(listaRelatorio,
							new AssuntoComparatorDecrescente());
				} else {
					Collections.sort(listaRelatorio,
							new AssuntoComparatorCrescente());
				}
			}

		}

		catch (SQLException e) {
			throw new DaoException("Erro ao executar o comando SQL", e);
		} finally {

			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DaoException("Erro ao executar o comando SQL", e);
			}

		}

		return listaRelatorio;
	}

	private static class AssuntoComparatorDecrescente implements Comparator,
			Serializable {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof RelatorioAnaliticoProcessoSetor)
					|| !(obj2 instanceof RelatorioAnaliticoProcessoSetor))
				return 0;

			RelatorioAnaliticoProcessoSetor doc1 = (RelatorioAnaliticoProcessoSetor) obj1;
			RelatorioAnaliticoProcessoSetor doc2 = (RelatorioAnaliticoProcessoSetor) obj2;

			if (doc1 == null || doc2 == null)
				return 0;

			return doc2.getDescricaoAssuntoCompletoSTF().compareTo(
					doc1.getDescricaoAssuntoCompletoSTF());
		}
	}

	private static class AssuntoComparatorCrescente implements Comparator,
			Serializable {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof RelatorioAnaliticoProcessoSetor)
					|| !(obj2 instanceof RelatorioAnaliticoProcessoSetor))
				return 0;

			RelatorioAnaliticoProcessoSetor doc1 = (RelatorioAnaliticoProcessoSetor) obj1;
			RelatorioAnaliticoProcessoSetor doc2 = (RelatorioAnaliticoProcessoSetor) obj2;

			if (doc1 == null || doc2 == null)
				return 0;

			return doc1.getDescricaoAssuntoCompletoSTF().compareTo(
					doc2.getDescricaoAssuntoCompletoSTF());
		}
	}

	private static class ProcessoComparatorCrescente implements Comparator,
			Serializable {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof ProcessoSetor)
					|| !(obj2 instanceof ProcessoSetor))
				return 0;

			ProcessoSetor doc1 = (ProcessoSetor) obj1;
			ProcessoSetor doc2 = (ProcessoSetor) obj2;

			if ((doc1 == null || doc2 == null)
					&& (doc1.getProcesso() == null || doc2.getProcesso() == null))
				return 0;

			return doc1.getProcesso().getIdentificacao()
					.compareTo(doc2.getProcesso().getIdentificacao());

		}
	}

	private static class ProcessoComparatorDecrescente implements Comparator,
			Serializable {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof ProcessoSetor)
					|| !(obj2 instanceof ProcessoSetor))
				return 0;

			ProcessoSetor doc1 = (ProcessoSetor) obj1;
			ProcessoSetor doc2 = (ProcessoSetor) obj2;

			if ((doc1 == null || doc2 == null)
					&& (doc1.getProcesso() == null || doc2.getProcesso() == null))
				return 0;

			return doc2.getProcesso().getIdentificacao()
					.compareTo(doc1.getProcesso().getIdentificacao());

		}
	}

	private static class ProtocoloComparatorCrescente implements Comparator,
			Serializable {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof ProcessoSetor)
					|| !(obj2 instanceof ProcessoSetor))
				return 0;

			ProcessoSetor doc1 = (ProcessoSetor) obj1;
			ProcessoSetor doc2 = (ProcessoSetor) obj2;

			if ((doc1 == null || doc2 == null)
					&& (doc1.getProtocolo() == null || doc2.getProtocolo() == null))
				return 0;

			return doc1.getProtocolo().getIdentificacao()
					.compareTo(doc2.getProtocolo().getIdentificacao());
		}
	}

	private static class ProtocoloComparatorDecrescente implements Comparator,
			Serializable {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof ProcessoSetor)
					|| !(obj2 instanceof ProcessoSetor))
				return 0;

			ProcessoSetor doc1 = (ProcessoSetor) obj1;
			ProcessoSetor doc2 = (ProcessoSetor) obj2;

			if ((doc1 == null || doc2 == null)
					&& (doc1.getProtocolo() == null || doc2.getProtocolo() == null))
				return 0;

			return doc2.getProtocolo().getIdentificacao()
					.compareTo(doc1.getProtocolo().getIdentificacao());
		}
	}

	public String recuperarGruposProcesso(Long idSetor,
			Long seq_objeto_incidente) throws DaoException {
		Session sessao = retrieveSession();
		String nomeGruposProcessoConcatenados = "";

		Statement stmtGruposProcesso = null;
		ResultSet rsGruposProcesso = null;

		try {
			StringBuffer sqlGrupoProcesso = new StringBuffer();
			sqlGrupoProcesso
					.append(" SELECT DISTINCT gps.nom_grupo_processo_setor AS nome_grupo_processo, "
							+ "                 gps.seq_grupo_processo_setor AS seq_grupo_processo_setor "
							+ " FROM egab.processo_setor ps, egab.processo_setor_grupo psg, egab.grupo_processo_setor gps "
							+ " WHERE "
							+ " gps.nom_grupo_processo_setor IS NOT NULL "
							+ " AND ps.seq_objeto_incidente = psg.seq_objeto_incidente "
							+ " AND ps.cod_setor = gps.cod_setor  "
							+ " AND gps.seq_grupo_processo_setor = psg.seq_grupo_processo_setor "
							+ " AND ps.cod_setor = "
							+ idSetor
							+ " AND ps.seq_objeto_incidente = "
							+ seq_objeto_incidente
							+ " ORDER BY gps.nom_grupo_processo_setor ");
			
			// System.out.println("--------SQL NATIVO GRUPOS PROCESSO:  "+sqlGrupoProcesso.toString());
			
			stmtGruposProcesso = sessao.connection().createStatement();
			stmtGruposProcesso.executeQuery(sqlGrupoProcesso.toString());
			rsGruposProcesso = stmtGruposProcesso.getResultSet();

			while (rsGruposProcesso.next()) {
				nomeGruposProcessoConcatenados = nomeGruposProcessoConcatenados
						+ rsGruposProcesso.getString("nome_grupo_processo")
						+ "\n";
			}
			
			// retira o \n do fim da string
			if (nomeGruposProcessoConcatenados.trim().length() >= 2)
				nomeGruposProcessoConcatenados = nomeGruposProcessoConcatenados
						.substring(0,
								nomeGruposProcessoConcatenados.length() - 2);

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				if (stmtGruposProcesso != null)
					stmtGruposProcesso.close();
				if (rsGruposProcesso != null)
					rsGruposProcesso.close();
			} catch (SQLException e) {
				throw new DaoException("Erro ao executar o comando SQL", e);
			}

		}

		return nomeGruposProcessoConcatenados;
	}

	public String recuperarTodosAssuntosProcessoSetor(
			String seqObjetoIncidente, String tipoObjetoIncidente)
			throws DaoException {
		Session sessao = retrieveSession();
		String assuntosConcatenados = "";
		
		Statement stmt = null;
		ResultSet rs = null;

		try {
			StringBuffer sql = new StringBuffer();

			if (!tipoObjetoIncidente.equals(TipoObjetoIncidente.PROCESSO
					.getCodigo())) {
				sql.append(
						" SELECT oi.seq_objeto_incidente_principal AS seq_processo FROM judiciario.objeto_incidente oi WHERE oi.seq_objeto_incidente = ")
						.append(seqObjetoIncidente);

				// System.out.println("------------- SQL NATIVO PARA RECUPERAR A SEQ_OBJETO_INCIDENTE_PAI (DO PROCESSO): "+sql.toString());
				
				stmt = sessao.connection().createStatement();
				stmt.executeQuery(sql.toString());
				rs = stmt.getResultSet();
				rs.next();
				if (SearchData.stringNotEmpty(rs.getString("seq_processo")))
					seqObjetoIncidente = rs.getString("seq_processo");

				sql = new StringBuffer();
				rs.close();
				stmt.close();
			}

			sql.append(" SELECT ass.dsc_assunto_completo "
					+ " FROM stf.assunto_processo ap, stf.assuntos ass "
					+ " WHERE ap.cod_assunto = ass.cod_assunto(+) "
					+ " AND ap.seq_objeto_incidente = " + seqObjetoIncidente
					+ " ORDER BY ap.num_ordem ");

			// System.out.println("--------SQL NATIVO ASSUNTOS:  "+sql.toString());

			stmt = sessao.connection().createStatement();
			stmt.executeQuery(sql.toString());
			rs = stmt.getResultSet();

			while (rs.next()) {
				assuntosConcatenados = assuntosConcatenados
						+ rs.getString("dsc_assunto_completo") + "\n";
			}
			
			// retira o \n do fim da string
			if (assuntosConcatenados.trim().length() >= 2)
				assuntosConcatenados = assuntosConcatenados.substring(0,
						assuntosConcatenados.length() - 2);

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DaoException("Erro ao executar o comando SQL", e);
			}

		}

		return assuntosConcatenados;
	}

	public List<ProcessoSetor> pesquisarProcessoSetor(TipoFaseSetor faseSetor)
			throws DaoException {

		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		List<ProcessoSetor> listaProcessos = null;

		try {

			hql.append(" select ps from ProcessoSetor ps "
					+ " where ps.faseAtual.id in ( "
					+ "     select hf.id from HistoricoFase hf "
					+ "        where hf.id = ps.faseAtual.id "
					+ "			and hf.tipoFaseSetor = " + faseSetor.getId() + " ) ");

			Query q = session.createQuery(hql.toString());
			listaProcessos = q.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return listaProcessos;
	}
	
	public List<EstatisticaProcessoSetor> pesquisarRelatorioSinteticoProcessoSetor(
			Short anoProtocolo, Long numeroProtocolo, String siglasClassesProcessuaisAgrupadas, String sigla,
			Long numeroProcesso, Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada,
			String siglaTipoJulgamento, String codigoTipoMeioProcesso,
			Long numeroPeticao, String codigoAssunto, String descricaoAssunto,
			String complementoAssunto, Long codigoMinistroRelator,
			String numeroSala, String numeroArmario, String numeroEstante,
			String numeroPrateleira, String numeroColuna,
			String obsDeslocamento,
			Boolean pesquisarAssuntoEmTodosNiveis, Boolean pesquisarInicio,
			Long idSecaoUltimoDeslocamento, String siglaUsuarioDistribuicao,
			Long idGrupoProcessoSetor, 
			Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal,
			Date dataDistribuicaoInicial,
			Date dataDistribuicaoFinal, Date dataFaseInicial,
			Date dataFaseFinal, Date dataRemessaInicial, Date dataRemessaFinal,
			Date dataRecebimentoInicial, Date dataRecebimentoFinal,
			Date dataEntradaInicial, Date dataEntradaFinal, Date dataSaidaInicial, Date dataSaidaFinal, Long idSetor,
			Long idTipoUltimaFaseSetor, Long idTipoUltimoStatusSetor,
			Boolean faseProcessualAtual, Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox,
			Boolean semLocalizacao, Boolean semFase, Boolean semDistribuicao,
			Boolean semVista, 
            Long idCategoriaPartePesquisa,
            String nomeParte,
			Long idTipoTarefa, Boolean localizadosNoSetor,
			Boolean emTramiteNoSetor, Boolean possuiLiminar,
			Boolean possuiPreferencia, Boolean sobrestado, Boolean julgado,
			Boolean preFetchAssunto, Boolean readOnlyQuery,
			Boolean limitarPesquisa, String tipoRelatorio,
			Boolean groupByFase, Boolean groupByFaseStatus,	Boolean groupByDistribuicao, Boolean groupByDeslocamento, Boolean groupByAssunto,
			Boolean orderByProcesso, Boolean orderByProtocolo,
			Boolean orderByValorGut, Boolean orderByDataEntrada,
			Boolean orderByAssunto, Boolean orderByCrescente,
			List<Andamento> listaIncluirAndamentos,
			Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos,
			List<Andamento> listaNaoIncluirAndamentos,
			Date dataInicialNaoIncluirAndamentos,
			Date dataFinalNaoIncluirAndamentos) throws DaoException {

		List<EstatisticaProcessoSetor> listaRelatorio = new LinkedList<EstatisticaProcessoSetor>();
		Boolean group = possuiGroupBy(groupByFase, groupByFaseStatus, groupByDistribuicao, groupByDeslocamento, groupByAssunto);
		
		StringBuffer sql = null;
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			
			sql = createQuerySQLNativo(anoProtocolo, numeroProtocolo, 
								siglasClassesProcessuaisAgrupadas, sigla, numeroProcesso, 
								recurso, possuiRecurso, siglaRecursoUnificada, siglaTipoJulgamento, codigoTipoMeioProcesso, 
								numeroPeticao, codigoAssunto, descricaoAssunto, complementoAssunto, codigoMinistroRelator, 
								numeroSala, numeroArmario, numeroEstante, numeroPrateleira, numeroColuna, obsDeslocamento, 
								pesquisarAssuntoEmTodosNiveis, pesquisarInicio, idSecaoUltimoDeslocamento, siglaUsuarioDistribuicao, idGrupoProcessoSetor, 
								dataDistribuicaoMinistroInicial, dataDistribuicaoMinistroFinal, dataDistribuicaoInicial, dataDistribuicaoFinal, 
								dataFaseInicial, dataFaseFinal, dataRemessaInicial, dataRemessaFinal, dataRecebimentoInicial, dataRecebimentoFinal, 
								dataEntradaInicial, dataEntradaFinal, dataSaidaInicial, dataSaidaFinal, idSetor, idTipoUltimaFaseSetor, 
								idTipoUltimoStatusSetor, faseProcessualAtual, repercussaoGeralCheckbox, protocoloNaoAutuadoCheckbox, 
								semLocalizacao, semFase, semDistribuicao, semVista, idCategoriaPartePesquisa, nomeParte, idTipoTarefa, localizadosNoSetor, 
								emTramiteNoSetor, possuiLiminar, possuiPreferencia, sobrestado, julgado, preFetchAssunto, readOnlyQuery, limitarPesquisa, 
								tipoRelatorio, groupByFase, groupByFaseStatus, groupByDistribuicao, groupByDeslocamento, groupByAssunto, orderByProcesso, 
								orderByProtocolo, orderByValorGut, orderByDataEntrada, orderByAssunto, orderByCrescente, listaIncluirAndamentos, 
								dataInicialIncluirAndamentos, dataFinalIncluirAndamentos, listaNaoIncluirAndamentos, dataInicialNaoIncluirAndamentos, 
								dataFinalNaoIncluirAndamentos);
			
	
			Session sessao = retrieveSession();
		
			stmt = sessao.connection().createStatement();
			stmt.executeQuery(sql.toString());
			rs = stmt.getResultSet();			
			int cont = 0;
			while (rs.next()) {				
				EstatisticaProcessoSetor rel = new EstatisticaProcessoSetor();
				
				if( group ) {
					if( groupByDistribuicao ) {
						rel.setDescricao1(rs.getString("usu_distribuicao"));
						rel.setQuantidade(rs.getLong("quantidade"));
					} else if( groupByFase && (semFase == null || !semFase) ) {
						if( groupByFaseStatus )	{
							rel.setDescricao1(rs.getString("descricao_tipo_fase_setor"));
							rel.setDescricao2(rs.getString("descricao_tipo_status_setor"));
							rel.setQuantidade(rs.getLong("quantidade"));
						} else {
							rel.setDescricao1(rs.getString("descricao_tipo_fase_setor"));
							rel.setQuantidade(rs.getLong("quantidade"));
						}
					} else if( groupByDeslocamento && (semLocalizacao == null || !semLocalizacao) ) {
						rel.setDescricao1(rs.getString("descricao_secao_destino"));
						rel.setQuantidade(rs.getLong("quantidade"));
					} else if( groupByAssunto != null && groupByAssunto.booleanValue() ) {
						rel.setDescricao1(rs.getString("descricao_assunto_completo"));
						rel.setQuantidade(rs.getLong("quantidade"));
					}
				}
				
				listaRelatorio.add(rel);
			}

		}

		catch (SQLException e) {
			throw new DaoException("Erro ao executar o comando SQL", e);
		}finally{
			
			try{
				if( stmt != null )
					stmt.close();
				if( rs != null )
					rs.close();
			}catch(SQLException e ){
				throw new DaoException("Erro ao executar o comando SQL", e);
			}
			
		}

		return listaRelatorio;
	}

	public List<RelatorioAnaliticoProcessoSetor> pesquisarRelatorioAnaliticoProcessoSetor(
			Short anoProtocolo, Long numeroProtocolo, String siglasClassesProcessuaisAgrupadas, String sigla,
			Long numeroProcesso, Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada,
			String siglaTipoJulgamento, String codigoTipoMeioProcesso,
			Long numeroPeticao, String codigoAssunto, String descricaoAssunto,
			String complementoAssunto, Long codigoMinistroRelator,
			String numeroSala, String numeroArmario, String numeroEstante,
			String numeroPrateleira, String numeroColuna,
			String obsDeslocamento,
			Boolean pesquisarAssuntoEmTodosNiveis, Boolean pesquisarInicio,
			Long idSecaoUltimoDeslocamento, String siglaUsuarioDistribuicao,
			Long idGrupoProcessoSetor, 
			Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal,
			Date dataDistribuicaoInicial,
			Date dataDistribuicaoFinal, Date dataFaseInicial,
			Date dataFaseFinal, Date dataRemessaInicial, Date dataRemessaFinal,
			Date dataRecebimentoInicial, Date dataRecebimentoFinal,
			Date dataEntradaInicial, Date dataEntradaFinal, Date dataSaidaInicial, Date dataSaidaFinal, Long idSetor,
			Long idTipoUltimaFaseSetor, Long idTipoUltimoStatusSetor,
			Boolean faseProcessualAtual, Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox,
			Boolean semLocalizacao, Boolean semFase, Boolean semDistribuicao,
			Boolean semVista, 
            Long idCategoriaPartePesquisa,
            String nomeParte,
			Long idTipoTarefa, Boolean localizadosNoSetor,
			Boolean emTramiteNoSetor, Boolean possuiLiminar,
			Boolean possuiPreferencia, Boolean sobrestado, Boolean julgado,
			Boolean preFetchAssunto, Boolean readOnlyQuery,
			Boolean limitarPesquisa, String tipoRelatorio,
			Boolean groupByFase, Boolean groupByFaseStatus,	Boolean groupByDistribuicao, Boolean groupByDeslocamento, Boolean groupByAssunto,
			Boolean orderByProcesso, Boolean orderByProtocolo,
			Boolean orderByValorGut, Boolean orderByDataEntrada,
			Boolean orderByAssunto, Boolean orderByCrescente,
			List<Andamento> listaIncluirAndamentos,
			Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos,
			List<Andamento> listaNaoIncluirAndamentos,
			Date dataInicialNaoIncluirAndamentos,
			Date dataFinalNaoIncluirAndamentos) throws DaoException {

		List<RelatorioAnaliticoProcessoSetor> listaRelatorio = new LinkedList<RelatorioAnaliticoProcessoSetor>();
		
		StringBuffer sql = null;
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			
			sql = createQuerySQLNativo(anoProtocolo, numeroProtocolo, 
								siglasClassesProcessuaisAgrupadas, sigla, numeroProcesso, 
								recurso, possuiRecurso, siglaRecursoUnificada, siglaTipoJulgamento, codigoTipoMeioProcesso, 
								numeroPeticao, codigoAssunto, descricaoAssunto, complementoAssunto, codigoMinistroRelator, 
								numeroSala, numeroArmario, numeroEstante, numeroPrateleira, numeroColuna, obsDeslocamento, 
								pesquisarAssuntoEmTodosNiveis, pesquisarInicio, idSecaoUltimoDeslocamento, siglaUsuarioDistribuicao, idGrupoProcessoSetor, 
								dataDistribuicaoMinistroInicial, dataDistribuicaoMinistroFinal, dataDistribuicaoInicial, dataDistribuicaoFinal, 
								dataFaseInicial, dataFaseFinal, dataRemessaInicial, dataRemessaFinal, dataRecebimentoInicial, dataRecebimentoFinal, 
								dataEntradaInicial, dataEntradaFinal, dataSaidaInicial, dataSaidaFinal, idSetor, idTipoUltimaFaseSetor, 
								idTipoUltimoStatusSetor, faseProcessualAtual, repercussaoGeralCheckbox, protocoloNaoAutuadoCheckbox, 
								semLocalizacao, semFase, semDistribuicao, semVista, idCategoriaPartePesquisa, nomeParte, idTipoTarefa, localizadosNoSetor, 
								emTramiteNoSetor, possuiLiminar, possuiPreferencia, sobrestado, julgado, preFetchAssunto, readOnlyQuery, limitarPesquisa, 
								tipoRelatorio, groupByFase, groupByFaseStatus, groupByDistribuicao, groupByDeslocamento, groupByAssunto, orderByProcesso, 
								orderByProtocolo, orderByValorGut, orderByDataEntrada, orderByAssunto, orderByCrescente, listaIncluirAndamentos, 
								dataInicialIncluirAndamentos, dataFinalIncluirAndamentos, listaNaoIncluirAndamentos, dataInicialNaoIncluirAndamentos, 
								dataFinalNaoIncluirAndamentos);
			
	
			Session sessao = retrieveSession();
		
			stmt = sessao.connection().createStatement();
			stmt.executeQuery(sql.toString());
			rs = stmt.getResultSet();			
			int cont = 0;
			while (rs.next()) {				
				RelatorioAnaliticoProcessoSetor rel = new RelatorioAnaliticoProcessoSetor();
				// rel.setIdentificacaoProcessual( rs.getString( "processo" ) );
				
				if( rs.getString("tip_objeto_incidente").equals(TipoObjetoIncidente.PROCESSO.getCodigo()) ) {
					rel.setSiglaClasseUnificada(rs.getString("sigla_classe"));
					rel.setNumeroProcessual(rs.getString("numero_processo"));
					rel.setTipoJulgamento(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla());
					rel.setTipoMeioProcesso(rs.getString("tipo_processo"));
				} else if( rs.getString("tip_objeto_incidente").equals(TipoObjetoIncidente.RECURSO.getCodigo()) ) {
//					rel.setSiglaRecurso(rs.getString("sig_cadeia_incidente"));
//					rel.setSiglaClasseUnificada(rs.getString("sigla_classe"));
					rel.setSiglaClasseUnificada(rs.getString("sig_cadeia_incidente"));
					rel.setNumeroProcessual(rs.getString("numero_processo"));
					rel.setTipoJulgamento(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla());
					rel.setTipoMeioProcesso(rs.getString("tipo_processo"));
				} else if( rs.getString("tip_objeto_incidente").equals(TipoObjetoIncidente.INCIDENTE_JULGAMENTO.getCodigo()) ) {
					rel.setSiglaClasseUnificada(rs.getString("sigla_classe"));
					rel.setNumeroProcessual(rs.getString("numero_processo"));
					rel.setTipoJulgamento(rs.getString("tipo_julgamento"));
					rel.setTipoMeioProcesso(rs.getString("tipo_processo"));
				}
				
				// rel.setIdentificacaoProtocolo( rs.getString( "protocolo" ) );
				
//				if( rs.getString("tip_objeto_incidente").equals(TipoObjetoIncidente.PROTOCOLO.getCodigo()) ) {
//					rel.setNumeroProtocolo(rs.getString("numero_protocolo"));
//					rel.setAnoProtocolo(rs.getString("ano_protocolo"));
//				} 
//				else if( rs.getString("tip_objeto_incidente").equals(TipoObjetoIncidente.PROCESSO.getCodigo()) ) {
//					rel.setNumeroProtocolo(rs.getString("num_protocolo_processo"));
//					rel.setAnoProtocolo(rs.getString("ano_protocolo_processo"));
//				}
				
				rel.setNumeroProtocolo(rs.getString("numero_protocolo"));
				rel.setAnoProtocolo(rs.getString("ano_protocolo"));

				rel.setAssuntoEGAB(rs.getString("assunto_egab")); // assunto EGAB

				if (tipoRelatorio != null && (tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_ASSUNTO)
							|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DESLOCAMENTO) 
							|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))
				) {										
					String stringTotalAssuntos = null;
					
					if( (protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox) && SearchData.stringNotEmpty(rs.getString("quantidade_assunto_processo")) ) {
						stringTotalAssuntos = rs.getString("quantidade_assunto_processo");
					} else if( SearchData.stringNotEmpty(rs.getString("quantidade_assunto_protocolo")) ) {
						stringTotalAssuntos = rs.getString("quantidade_assunto_protocolo");
					}
										
					if( stringTotalAssuntos == null || stringTotalAssuntos.trim().length() <= 0 )
						stringTotalAssuntos = "0";
					
					Integer totalAssuntos = Integer.parseInt(stringTotalAssuntos);
					if (totalAssuntos != null && totalAssuntos > 0
							&& rel.getNumeroProtocolo() != null && rel.getNumeroProtocolo().trim().length() > 0
							&& rel.getAnoProtocolo() != null && rel.getAnoProtocolo().trim().length() > 0
					) {												
						cont = cont + 1;
						rel.setDescricaoAssuntoCompletoSTF(recuperarTodosAssuntosProcessoSetor(
																rel.getNumeroProtocolo(),  rel.getAnoProtocolo()));
					}
					/*else
						rel.setDescricaoAssuntoCompletoSTF(rs.getString("assunto_completo")); // assunto*/
					
				}

				if (tipoRelatorio != null && (tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DESLOCAMENTO) 
						|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))) {
					rel.setSecaoOrigem(rs.getString("secao_origem"));
					rel.setSecaoDestino(rs.getString("secao_destino"));
					rel.setDataRemessa(rs.getString("data_remessa"));
					rel.setDataRecebimento(rs.getString("data_recebimento"));
					// dados de localização

					rel.setSala(rs.getString("numero_sala"));
					rel.setArmario(rs.getString("numero_armario"));
					rel.setEstante(rs.getString("numero_estante"));
					rel.setPrateleira(rs.getString("numero_prateleira"));
					rel.setColuna(rs.getString("numero_coluna"));

					/*
					 * String localizacao = "";
					 * 
					 * if( numSala != null && numSala.trim().length() > 0 ){
					 * localizacao = localizacao + " Sala: " + numSala; } if(
					 * numArmario != null && numArmario.trim().length() > 0 ){
					 * localizacao = localizacao + " Armário: " + numArmario; }
					 * if( numEstante != null && numEstante.trim().length() > 0 ){
					 * localizacao = localizacao + " Estante: " + numEstante; }
					 * if( numPrateleira != null &&
					 * numPrateleira.trim().length() > 0 ){ localizacao =
					 * localizacao + " Prateleria: " + numPrateleira; } if(
					 * numColuna != null && numColuna.trim().length() > 0 ){
					 * localizacao = localizacao + " Coluna: " + numColuna; }
					 * rel.setLocalizacao( localizacao );
					 */
					rel.setObservacaoDeslocamento(rs.getString("obs_deslocamento")); // relatoriodeslocamentoanaliticolocalizacao
					rel.setAssuntoInterno(rs.getString("assunto_interno"));
					rel.setObservacaoProcessoSetor(rs.getString("observacao_processo"));

				}

				if (tipoRelatorio != null && (tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DISTRIBUICAO) 
						|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))) {
					rel.setUsuarioDistribuicao(rs.getString("usu_distribuicao"));
					rel.setGrupoUsuarioDistribuicao(rs.getString("grupo_usuario"));
					rel.setDataDistribuicao(rs.getString("data_distribuicao"));
				}

				if (tipoRelatorio != null && (tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_FASE) 
						|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))) {
					rel.setDescricaoTipoFaseSetor(rs.getString("descricao_tipo_fase_setor"));
					rel.setDescricaoTipoStatusSetor(rs.getString("descricao_tipo_status_setor"));
					rel.setSiglaUsuarioDistribuicao(rs.getString("sigla_usu_distribuicao"));
					rel.setObservacaoFase(rs.getString("obs_fase"));
				}
				
				
				listaRelatorio.add(rel);
			}
			if (orderByAssunto != null && orderByAssunto.booleanValue()) {
				if (orderByCrescente != null
						&& !orderByCrescente.booleanValue()) {
					Collections.sort(listaRelatorio, new AssuntoComparatorCrescente());
				} else {
					Collections.sort(listaRelatorio, new AssuntoComparatorDecrescente());
				}
			}

		}

		catch (SQLException e) {
			throw new DaoException("Erro ao executar o comando SQL", e);
		}finally{
			
			try{
				if( stmt != null )
					stmt.close();
				if( rs != null )
					rs.close();
			}catch(SQLException e ){
				throw new DaoException("Erro ao executar o comando SQL", e);
			}
			
		}

		return listaRelatorio;
	}

	private StringBuffer createQuerySQLNativo(
			Short anoProtocolo, Long numeroProtocolo, String siglasClassesProcessuaisAgrupadas, String sigla,
			Long numeroProcesso, Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada,
			String siglaTipoJulgamento, String codigoTipoMeioProcesso,
			Long numeroPeticao, String codigoAssunto, String descricaoAssunto,
			String complementoAssunto, Long codigoMinistroRelator,
			String numeroSala, String numeroArmario, String numeroEstante,
			String numeroPrateleira, String numeroColuna,
			String obsDeslocamento,
			Boolean pesquisarAssuntoEmTodosNiveis, Boolean pesquisarInicio,
			Long idSecaoUltimoDeslocamento, String siglaUsuarioDistribuicao,
			Long idGrupoProcessoSetor, 
			Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal,
			Date dataDistribuicaoInicial,
			Date dataDistribuicaoFinal, Date dataFaseInicial,
			Date dataFaseFinal, Date dataRemessaInicial, Date dataRemessaFinal,
			Date dataRecebimentoInicial, Date dataRecebimentoFinal,
			Date dataEntradaInicial, Date dataEntradaFinal, Date dataSaidaInicial, Date dataSaidaFinal, Long idSetor,
			Long idTipoUltimaFaseSetor, Long idTipoUltimoStatusSetor,
			Boolean faseProcessualAtual, Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox,
			Boolean semLocalizacao, Boolean semFase, Boolean semDistribuicao,
			Boolean semVista, 
            Long idCategoriaPartePesquisa,
            String nomeParte,
			Long idTipoTarefa, Boolean localizadosNoSetor,
			Boolean emTramiteNoSetor, Boolean possuiLiminar,
			Boolean possuiPreferencia, Boolean sobrestado, Boolean julgado,
			Boolean preFetchAssunto, Boolean readOnlyQuery,
			Boolean limitarPesquisa, String tipoRelatorio,
			Boolean groupByFase, Boolean groupByFaseStatus,	Boolean groupByDistribuicao, Boolean groupByDeslocamento, Boolean groupByAssunto,
			Boolean orderByProcesso, Boolean orderByProtocolo,
			Boolean orderByValorGut, Boolean orderByDataEntrada,
			Boolean orderByAssunto, Boolean orderByCrescente,
			List<Andamento> listaIncluirAndamentos,
			Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos,
			List<Andamento> listaNaoIncluirAndamentos,
			Date dataInicialNaoIncluirAndamentos,
			Date dataFinalNaoIncluirAndamentos) throws DaoException {
		
		try {
			
			/*
			 * falta incluir aqui, pois estamos colocando novos campos na nova pesquisa, mas não na antiga,
			 * que é aonde gera os relatórios, então falta colocsdProcessoSetor.possuiRecurso
			 * 
			 * hql.append(" JOIN ps.processoSTF.legislacaoProcessos lp ");		
			 * sd.numeroLegislacao =  (Long) ProcessoSetorSearchData.verificaObjetoMaiorQZero(numeroLegislacao);
			 * sd.numeroAno =  (Short) ProcessoSetorSearchData.verificaObjetoMaiorQZero(numeroAno);
			 * sd.numeroArtigo = numeroArtigo;
			 * sd.numeroInciso = numeroInciso;
			 * sd.numeroParagrafo = numeroParagrafo;
			 * sd.numeroAlinea = numeroAlinea;
			 * sd.normaProcesso = normaProcesso;
			 */

			StringBuffer sql = new StringBuffer();
			Boolean group = possuiGroupBy(groupByFase, groupByFaseStatus, groupByDistribuicao, groupByDeslocamento, groupByAssunto);

			sql.append(" SELECT ");
			if( group ) {
				if( groupByDistribuicao ) {
					sql.append(" usu.nom_usuario AS usu_distribuicao, COUNT(DISTINCT ps.seq_processo_setor) quantidade ");
				} else if( groupByFase && (semFase == null || !semFase) ) {
					if( groupByFaseStatus )	
						sql.append(" tfs.dsc_tipo_fase_setor AS descricao_tipo_fase_setor, tss.dsc_Tipo_status_setor AS descricao_tipo_status_setor, " +
								" COUNT(DISTINCT ps.seq_processo_setor) quantidade");
					else
						sql.append(" tfs.dsc_tipo_fase_setor AS descricao_tipo_fase_setor, COUNT(DISTINCT ps.seq_processo_setor) quantidade ");
				} else if( groupByDeslocamento && (semLocalizacao == null || !semLocalizacao) ) {
					sql.append(" s.dsc_secao AS descricao_secao_destino, COUNT(DISTINCT ps.seq_processo_setor) quantidade ");
				} else if( groupByAssunto != null && groupByAssunto.booleanValue() ) {
					sql.append(" ass.dsc_assunto_completo AS descricao_assunto_completo, COUNT(DISTINCT ps.seq_processo_setor) quantidade ");
				}
			} else {
				sql.append(" DISTINCT ");
				
//				if ( (idCategoriaPartePesquisa != null && idCategoriaPartePesquisa > 0) || (nomeParte != null && nomeParte.trim().length() > 0) 
//						|| (dataDistribuicaoMinistroInicial != null || dataDistribuicaoMinistroFinal != null)) {
//					sql.append(" DISTINCT ");
//				}
		

				sql.append(" protocolo.num_protocolo AS numero_protocolo, ");
				sql.append(" protocolo.ano_protocolo AS ano_protocolo, ");
				sql.append(" ps.dsc_complemento_assunto AS assunto_egab, ");
				sql.append(" ps.seq_processo_setor AS seq_processo_setor, ");
				sql.append(" oips.tip_objeto_incidente AS tip_objeto_incidente, ");
				sql.append(" ps.dat_entrada_setor AS data_entrada_setor, ");
				sql.append(" ps.num_indicador_gravidade, ");
				sql.append(" ps.num_indicador_tendencia, ");
				sql.append(" ps.num_indicador_urgencia ");
				
				if(protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox) {
					sql.append(" , processoSTF.sig_classe_proces AS sigla_classe, ");
					sql.append(" processoSTF.num_processo AS numero_processo, ");
					sql.append(" (select rpsig.sig_cadeia_incidente from judiciario.recurso_processo rpsig " +
								" where	rpsig.seq_objeto_incidente = oips.seq_objeto_incidente) as sig_cadeia_incidente, ");
					/*sql.append(" rp.sig_classe_proces AS sigla_classe_rp, ");
					sql.append(" rp.num_processo AS numero_processo_rp, ");
					sql.append(" rp.sig_cadeia_incidente AS sig_cadeia_incidente, ");
					sql.append(" ij.sig_classe_proces AS sigla_classe_ij, ");
					sql.append(" ij.num_processo AS numero_processo_ij, ");*/
					sql.append(" tr.sig_tipo_recurso AS tipo_julgamento, ");				
					sql.append(" processoSTF.tip_meio_processo AS tipo_processo ");					
				}
		
				if( (tipoRelatorio != null) && (tipoRelatorio.equalsIgnoreCase( ProcessoSetor.RELATORIO_ANALITICO_ASSUNTO )
						|| tipoRelatorio.equalsIgnoreCase( ProcessoSetor.RELATORIO_ANALITICO_DESLOCAMENTO )
						|| tipoRelatorio.equalsIgnoreCase( ProcessoSetor.RELATORIO_ANALITICO_COMPLETO ) ) 
				){	
					//sql.append(" , ass.dsc_assunto_completo AS assunto_completo, ");
					if(protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox) 
						sql.append(" ,ta.quantidade_assunto_processo ");
					
					sql.append(" ,ta.quantidade_assunto_protocolo ");
				}
		
				if (tipoRelatorio != null && (tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DESLOCAMENTO) 
						|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))) {
					sql.append(" , ( SELECT s.dsc_secao FROM egab.secao s WHERE seq_secao = hd.seq_secao_origem ) AS secao_origem, ");
					sql.append(" ( SELECT s.dsc_secao FROM egab.secao s WHERE seq_secao = hd.seq_secao_destino ) AS secao_destino, ");
					sql.append(" TO_CHAR( hd.dat_remessa, 'DD/MM/YYYY' ) AS data_remessa, ");
					sql.append(" TO_CHAR( hd.dat_recebimento, 'DD/MM/YYYY' ) AS data_recebimento, ");
					sql.append(" hd.num_sala AS numero_sala, ");
					sql.append(" hd.num_armario AS numero_armario, ");
					sql.append(" hd.num_estante AS numero_estante, ");
					sql.append(" hd.num_prateleira AS numero_prateleira, ");
					sql.append(" hd.num_coluna AS numero_coluna, ");
					sql.append(" hd.obs_deslocamento AS obs_deslocamento, ");
					sql.append(" ps.dsc_complemento_assunto AS assunto_interno,");
					sql.append(" ps.obs_processo_setor AS observacao_processo");
				}
		
				if (tipoRelatorio != null && (tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DISTRIBUICAO) 
						|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))) {
					sql.append(" , usu.nom_usuario AS usu_distribuicao, ");
					sql.append(" grupos_usuario.grupo_usuario, ");
					sql.append(" TO_CHAR( hdist.dat_distribuicao, 'DD/MM/YYYY' ) AS data_distribuicao ");
				}
		
				if (tipoRelatorio != null && (tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_FASE) 
						|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))) {
					sql.append(" , tfs.dsc_tipo_fase_setor AS descricao_tipo_fase_setor, ");
					sql.append(" tss.dsc_Tipo_status_setor AS descricao_tipo_status_setor, ");
					sql.append(" usu.sig_usuario AS sigla_usu_distribuicao, ");
					sql.append(" hf.dsc_observacao AS obs_fase ");
				}
				
			}
			
			sql.append(" FROM egab.processo_setor ps ");			
			sql.append(" INNER JOIN judiciario.objeto_incidente oips ON ps.seq_objeto_incidente = oips.seq_objeto_incidente ");
			
			if(protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox) {
				sql.append(" LEFT JOIN judiciario.objeto_incidente oipsprinc ON oipsprinc.seq_objeto_incidente = oips.seq_objeto_incidente_principal ");
				sql.append(" LEFT JOIN judiciario.objeto_incidente oipsant ON oipsant.seq_objeto_incidente = oipsprinc.seq_objeto_incidente_anterior ");
				sql.append(" LEFT JOIN judiciario.vw_processo_relator processoSTF ON oipsprinc.seq_objeto_Incidente = processoSTF.seq_objeto_Incidente ");
				sql.append(" LEFT JOIN judiciario.objeto_incidente oirp ON oipsprinc.seq_objeto_incidente = oirp.seq_objeto_incidente_principal ");
				sql.append(" LEFT JOIN judiciario.recurso_processo rp ON oirp.seq_objeto_Incidente = rp.seq_objeto_Incidente ");
				sql.append(" LEFT JOIN judiciario.incidente_julgamento ij ON oips.seq_objeto_Incidente = ij.seq_objeto_Incidente ");
				sql.append(" LEFT JOIN judiciario.tipo_recurso tr ON ij.seq_tipo_recurso = tr.seq_tipo_recurso ");
				
				if (idTipoTarefa != null && idTipoTarefa > 0) {
					sql.append(" LEFT JOIN egab.tarefa_atribuida_processo tap on processoSTF.seq_objeto_incidente = tap.seq_objeto_incidente ");
					sql.append(" LEFT JOIN egab.tarefa_setor ts on ts.seq_tarefa_setor = tap.seq_tarefa_setor ");
				}
				
				if( siglasClassesProcessuaisAgrupadas != null && siglasClassesProcessuaisAgrupadas.trim().length() > 0 ) {
					sql.append(" LEFT JOIN stf.classes clp ON ps.sig_classe_proces = clp.sig_classe ");
				}	
			}
			
			if(protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox)
				sql.append(" LEFT JOIN stf.processo_protocolados protocolo ON oipsant.seq_objeto_Incidente = protocolo.seq_objeto_Incidente ");
			else
				sql.append(" LEFT JOIN stf.processo_protocolados protocolo ON oips.seq_objeto_Incidente = protocolo.seq_objeto_Incidente ");
				


//			sql.append(" LEFT JOIN stf.classe_unif cuinf ON ps.cod_recurso = cuinf.cod_recurso AND ps.sig_classe_proces = cuinf.sig_classe AND ps.tip_julgamento = cuinf.tip_julgamento ");

			if ((tipoRelatorio != null && (tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DESLOCAMENTO) 
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_DESLOCAMENTO) 
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)))
					|| (idSecaoUltimoDeslocamento != null)
					|| (numeroSala != null && numeroSala.trim().length() > 0)
					|| (numeroArmario != null && numeroArmario.trim().length() > 0)
					|| (numeroEstante != null && numeroEstante.trim().length() > 0)
					|| (numeroPrateleira != null && numeroPrateleira.trim().length() > 0)
					|| (numeroColuna != null && numeroColuna.trim().length() > 0)
					|| (dataRemessaInicial != null)
					|| (dataRemessaFinal != null)
					|| (dataRecebimentoInicial != null)
					|| (dataRecebimentoFinal != null)
					|| (obsDeslocamento != null && obsDeslocamento.trim().length() > 0)
			) {
				sql.append(" LEFT JOIN egab.historico_deslocamento hd ON ps.seq_deslocamento_atual = hd.seq_historico_deslocamento ");
			}

			if ((tipoRelatorio != null && (tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_FASE) 
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_FASE)
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)))
					|| (idTipoUltimaFaseSetor != null)
					|| (dataFaseInicial != null)
					|| (dataFaseFinal != null)
					|| (semFase != null)) {
				sql.append(" LEFT JOIN egab.historico_fase hf ON ps.seq_fase_atual = hf.seq_historico_fase AND ps.seq_processo_setor = hf.seq_processo_setor ");
			}

			if ((tipoRelatorio != null && (tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_FASE) 
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_FASE)
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)))
					|| (idTipoUltimaFaseSetor != null)) {
				sql.append(" LEFT JOIN egab.tipo_fase_setor tfs ON hf.seq_tipo_fase_setor = tfs.seq_tipo_fase_setor ");
			}

			if ((tipoRelatorio != null && (tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_FASE) 
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_FASE)
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)))
					|| (idTipoUltimoStatusSetor != null)) {
				sql.append(" LEFT JOIN egab.tipo_status_setor tss ON hf.seq_tipo_status_setor = tss.seq_tipo_status_setor ");
			}

			if ((tipoRelatorio != null && (tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DISTRIBUICAO)
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_DISTRIBUICAO)
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO) 
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_FASE)))
					|| (dataDistribuicaoInicial != null)
					|| (dataDistribuicaoFinal != null)
					|| (siglaUsuarioDistribuicao != null && siglaUsuarioDistribuicao
							.trim().length() > 0)) {
				sql.append(" LEFT JOIN egab.historico_distribuicao hdist ON ps.seq_distribuicao_atual = hdist.seq_historico_distribuicao ");
			}

			if ((tipoRelatorio != null && (tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DISTRIBUICAO)
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_DISTRIBUICAO)
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_FASE) 
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)))
					|| (siglaUsuarioDistribuicao != null && siglaUsuarioDistribuicao.trim().length() > 0)) {
				sql.append(" LEFT JOIN stf.usuarios usu ON hdist.sig_usuario_analise = usu.sig_usuario ");
			}

			if (tipoRelatorio != null && (tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DISTRIBUICAO) 
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_DISTRIBUICAO)
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))) {
				sql.append(" LEFT JOIN (SELECT LTRIM "
								+ " (MAX (SYS_CONNECT_BY_PATH (dsc_grupo, '\\'))KEEP (DENSE_RANK LAST ORDER BY curr), ', ' "
								+ " ) AS grupo_usuario, des.SIG_USUARIO "
								+ " FROM (SELECT a.seq_grupo_usuario, sig_usuario, dsc_grupo, "
								+ " ROW_NUMBER () OVER (PARTITION BY sig_usuario ORDER BY a.seq_grupo_usuario DESC) AS curr, "
								+ " ROW_NUMBER () OVER (PARTITION BY sig_usuario ORDER BY a.seq_grupo_usuario DESC) - 1 AS prev "
								+ " FROM egab.USUARIO_grupo a "
								+ " JOIN egab.grupo_usuario gu on gu.seq_grupo_usuario = a.seq_grupo_usuario) des "
								+ " GROUP BY SIG_USUARIO "
								+ " CONNECT BY prev = PRIOR curr "
								+ " AND SIG_USUARIO = PRIOR SIG_USUARIO "
								+ " START WITH curr = 1) grupos_usuario ON grupos_usuario.sig_usuario = usu.sig_usuario ");
			}

//			if ( (repercussaoGeralCheckbox != null && repercussaoGeralCheckbox.booleanValue())
//					|| (idTipoTarefa != null)
//					//|| (codigoAssunto != null && codigoAssunto.trim().length() > 0)
//					|| ((tipoRelatorio != null) && ((tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_ASSUNTO)
//					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_ASSUNTO)
//					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DESLOCAMENTO) 
//					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO))))
//					|| (idCategoriaPartePesquisa != null && idCategoriaPartePesquisa > 0)
//					|| (nomeParte != null && nomeParte.trim().length() > 0)
//					|| (codigoMinistroRelator != null && codigoMinistroRelator > 0)
//			) {
//				sql.append(" LEFT JOIN stf.processos p ON ps.sig_classe_proces = p.sig_classe_proces AND ps.num_processo = p.num_processo ");
//			}	
			
			if ( codigoAssunto != null || (descricaoAssunto != null && descricaoAssunto.trim().length() >= 3) 
					|| (tipoRelatorio != null && tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_ASSUNTO)) ) {			
				if(protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox)
					sql.append(" LEFT JOIN stf.assunto_processo ap ON oipsprinc.seq_objeto_incidente = ap.seq_objeto_incidente ");
				else
					sql.append(" LEFT JOIN stf.assunto_processo ap ON oips.seq_objeto_incidente = ap.seq_objeto_incidente ");
				sql.append(" LEFT JOIN stf.assuntos ass ON ass.cod_assunto = ap.cod_assunto ");		
			}

			if ((tipoRelatorio != null && (tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_ASSUNTO)
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_ASSUNTO)
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_DESLOCAMENTO)
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_DESLOCAMENTO) 
					|| tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_ANALITICO_COMPLETO)))
					|| (orderByAssunto != null && orderByAssunto.booleanValue())
					|| (codigoAssunto != null)
					|| (descricaoAssunto != null && descricaoAssunto.trim()
							.length() >= 3)
					|| (orderByAssunto != null && orderByAssunto.booleanValue())) {
				if(protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox) 
					sql.append(" LEFT JOIN (SELECT sig_classe_proces, num_processo, COUNT(*) AS quantidade_assunto_processo "
							+ " FROM stf.assunto_processo GROUP BY sig_classe_proces, num_processo) ta ON ta.num_processo = processoSTF.num_processo  " +
									"AND ta.sig_classe_proces = processoSTF.sig_classe_proces ");
				sql.append(" LEFT JOIN (SELECT ano_protocolo, num_protocolo, COUNT(*) AS quantidade_assunto_protocolo "
						+ " FROM stf.assunto_processo GROUP BY ano_protocolo, num_protocolo) ta ON ta.num_protocolo = protocolo.num_protocolo  AND " +
								"ta.ano_protocolo = protocolo.ano_protocolo ");
			}

			if (idGrupoProcessoSetor != null && idGrupoProcessoSetor > 0) {
				sql.append(" LEFT JOIN egab.processo_setor_grupo psg ON ps.seq_objeto_incidente = psg.seq_objeto_incidente ");
				sql.append(" LEFT JOIN egab.grupo_processo_setor gps ON ps.cod_setor = gps.cod_setor AND gps.seq_grupo_processo_setor = psg.seq_grupo_processo_setor ");
			}

			if ( (idSecaoUltimoDeslocamento != null && idSecaoUltimoDeslocamento > 0) || 
					(tipoRelatorio != null && tipoRelatorio.equals(ProcessoSetor.RELATORIO_SINTETICO_DESLOCAMENTO)) ) {
				sql.append(" LEFT JOIN egab.secao s ON hd.seq_secao_destino = s.seq_secao ");
			}
			
			if (dataDistribuicaoMinistroInicial != null || dataDistribuicaoMinistroFinal != null) {
				sql.append(" LEFT JOIN stf.sit_min_processos smp ON oips.seq_objeto_incidente = smp.seq_objeto_incidente ");
			}
			
			if (semVista != null) {
				sql.append(" LEFT JOIN stf.ministros mns ON ps.cod_setor = mns.cod_setor ");
				sql.append(" LEFT JOIN stf.agendamentos ag ON oips.seq_objeto_incidente = ag.seq_objeto_incidente ");
			}

			
//			if ( (idCategoriaPartePesquisa != null && idCategoriaPartePesquisa > 0) || (nomeParte != null && nomeParte.trim().length() > 0) ) {
//		        sql.append(" LEFT JOIN stf.categoria_partes cp ON p.sig_classe_proces = cp.sig_classe_proces AND p.num_processo = cp.num_processo ");
//			}
			
			if ( (idCategoriaPartePesquisa != null && idCategoriaPartePesquisa > 0) || SearchData.stringNotEmpty(nomeParte) ) {
				sql.append(" LEFT JOIN judiciario.vw_jurisdicionado_incidente cp ON oips.seq_objeto_incidente = cp.seq_objeto_incidente ");
			}
			
			if ( idCategoriaPartePesquisa != null && idCategoriaPartePesquisa > 0 ) {
				sql.append(" LEFT JOIN judiciario.categoria c ON cp.cod_categoria = c.cod_categoria ");
			}
			
//			if ( nomeParte != null && nomeParte.trim().length() > 0 ) {
//				sql.append(" LEFT JOIN stf.partes cpp ON cp.cod_parte = cpp.cod_parte ");
//			}
			
		
			
			sql.append(" WHERE ( 1 = 1 ) ");
			
			sql.append(" AND CASE WHEN (oips.tip_objeto_incidente = 'RC') THEN (select COUNT(rp1.seq_objeto_incidente) from judiciario.recurso_processo rp1 where rp1.seq_objeto_incidente = oips.seq_objeto_incidente AND rp1.flg_ativo = 'S') ");
			sql.append(" WHEN (oips.tip_objeto_incidente = 'IJ') THEN (select COUNT(ij1.seq_objeto_incidente) from judiciario.incidente_julgamento ij1 where ij1.seq_objeto_incidente = oips.seq_objeto_incidente AND ij1.flg_ativo = 'S') ");
			sql.append(" WHEN (oips.tip_objeto_incidente = 'PR') THEN 1 ELSE 0 END = 1 ");

			if( protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox.booleanValue() ) {
				
				if( siglasClassesProcessuaisAgrupadas != null && siglasClassesProcessuaisAgrupadas.trim().length() > 0 ) {
					if( siglasClassesProcessuaisAgrupadas.equals(Classe.SIGLAS_CLASSES_PROCESSUAIS_AI_RE) ) {
						sql.append(" AND processoSTF.sig_classe_proces IN ('RE', 'AI') ");
					} else if( siglasClassesProcessuaisAgrupadas.equals(Classe.SIGLAS_CLASSES_PROCESSUAIS_ORIGINARIOS) ) {
						sql.append(" AND clp.flg_originario = 'S' ");
					}
				} else if(sigla != null && sigla.trim().length() > 0)
					sql.append(" AND processoSTF.sig_classe_proces = '" + sigla + "' ");
//						sql.append(" AND (processoSTF.sig_classe_proces = '" + sigla + "' OR rp.sig_classe_proces = '" + sigla + "' OR ij.sig_classe_proces = '" + sigla + "' )");
	
				if (numeroProcesso != null && numeroProcesso > 0)
					sql.append(" AND processoSTF.num_processo = " + numeroProcesso);
//					sql.append(" AND (processoSTF.num_processo = " + numeroProcesso + " OR rp.num_processo = " + numeroProcesso + " OR ij.num_processo = " + numeroProcesso + ") ");
				
				if ( possuiRecurso != null ){			
					if ( possuiRecurso ) {
						sql.append(" AND ");
					} else if ( !possuiRecurso ){ 
						sql.append(" AND NOT ");
					}
					sql.append(" EXISTS (SELECT rp2.seq_objeto_incidente FROM judiciario.recurso_processo rp2 WHERE rp2.seq_objeto_incidente = oips.seq_objeto_incidente ) ");
				}
				
				if ( recurso != null) {
					sql.append(" AND rp.cod_recurso = " + recurso );
				}

				if (SearchData.stringNotEmpty(siglaRecursoUnificada))
					sql.append(" AND EXISTS (SELECT rp3.seq_objeto_incidente FROM judiciario.recurso_processo rp3 WHERE  oips.seq_objeto_incidente = rp3.seq_objeto_incidente AND rp3.sig_cadeia_incidente LIKE '%" + siglaRecursoUnificada + "%' ) ");

				if (SearchData.stringNotEmpty(siglaTipoJulgamento)) {
					if( !siglaTipoJulgamento.equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla()) )
						sql.append(" AND ij.tip_julgamento = '"+ siglaTipoJulgamento + "' ");
					else
						sql.append(" AND NOT EXISTS (SELECT ij2.seq_objeto_incidente FROM judiciario.incidente_julgamento ij2 " +
								" WHERE oips.seq_objeto_incidente = ij2.seq_objeto_incidente) ");
				}

				if (codigoTipoMeioProcesso != null && codigoTipoMeioProcesso.trim().length() > 0)
					sql.append(" AND processoSTF.tip_meio_processo = '"	+ codigoTipoMeioProcesso + "' ");
			
				// Repercussão Geral = S
				if (repercussaoGeralCheckbox != null && repercussaoGeralCheckbox.booleanValue()) {
					sql.append(" AND processoSTF.flg_repercussao_geral = 'S' ");
				}

				// tarefa
				if (idTipoTarefa != null && idTipoTarefa > 0) {
					sql.append(" AND ts.seq_tipo_tarefa = " + idTipoTarefa);
				}	
				
				// ministro relator
				if (codigoMinistroRelator != null && codigoMinistroRelator > 0) {
					sql.append(" AND processoSTF.cod_relator_atual = " + codigoMinistroRelator);
				}
			
			} else if(protocoloNaoAutuadoCheckbox != null && protocoloNaoAutuadoCheckbox) {

				// protocolo não autuado
				if( protocoloNaoAutuadoCheckbox != null && protocoloNaoAutuadoCheckbox.booleanValue() ) {
					sql.append(" AND oips.tip_objeto_incidente = 'PI' " +
							" AND NOT EXISTS (SELECT oiptna.seq_objeto_incidente FROM judiciario.objeto_incidente oiptna WHERE " +
							" oiptna.seq_objeto_incidente_anterior = oips.seq_objeto_incidente) ");
				}

				if (codigoTipoMeioProcesso != null && codigoTipoMeioProcesso.trim().length() > 0)
					sql.append(" AND protocolo.tip_meio_processo = '"	+ codigoTipoMeioProcesso + "' ");
				
			}
			
			if (anoProtocolo != null && anoProtocolo > 0)
				sql.append(" AND protocolo.ano_protocolo = " + anoProtocolo);

			if (numeroProtocolo != null && numeroProtocolo > 0)
				sql.append(" AND protocolo.num_protocolo = " + numeroProtocolo);

			// codigoSetor
			if (idSetor != null)
				sql.append(" AND ps.cod_setor = " + idSetor);

			// idGrupoProcessoSetor
			if (idGrupoProcessoSetor != null && idGrupoProcessoSetor > 0)
				sql.append(" AND gps.seq_grupo_processo_setor = "
						+ idGrupoProcessoSetor);

			// idSecaoUltimoDeslocamento
			if (idSecaoUltimoDeslocamento != null && idSecaoUltimoDeslocamento > 0)
				sql.append(" AND s.seq_secao = " + idSecaoUltimoDeslocamento);

			// dataEntradaInicial
			if (dataEntradaInicial != null)
				sql.append(" AND ps.dat_entrada_setor >= to_date('"
						+ new SimpleDateFormat("dd/MM/yyyy")
								.format(dataEntradaInicial) + " 00:00:00"
						+ "', 'DD/MM/YYYY HH24:MI:SS') ");

			// dataEntradaFinal
			if (dataEntradaFinal != null)
				sql.append(" AND ps.dat_entrada_setor <= to_date('"
						+ new SimpleDateFormat("dd/MM/yyyy")
								.format(dataEntradaFinal) + " 23:59:59"
						+ "', 'DD/MM/YYYY HH24:MI:SS') ");

			//dataSaidaInicial
			if (dataSaidaInicial != null)
				sql.append("AND ps.dat_saida_setor >= to_date('" 
						+ new SimpleDateFormat("dd/MM/yyyy")
								.format(dataSaidaInicial) + "00:00:00"
						+ "', 'DD/MM/YYYY HH24:MI:SS')");
			
			//dataSaidaFinal
			if (dataSaidaFinal != null)
				sql.append("AND ps.dat_saida_setor <= to_date('" 
						+ new SimpleDateFormat("dd/MM/yyyy")
								.format(dataSaidaFinal) + "23:59:59"
						+ "', 'DD/MM/YYYY HH24:MI:SS')");
			
			// localizados no setor
			if (localizadosNoSetor != null) {
				if (localizadosNoSetor.booleanValue())
					sql.append(" AND ps.dat_saida_setor IS NULL ");
				else
					sql.append(" AND ps.dat_saida_setor is NOT NULL AND processoSTF.filtroEmTramitacao = 'S' ");
			}else{
				sql.append(" AND processoSTF.filtroEmTramitacao = 'S' "); 
			}

			// emTramiteNoSetor
			if (emTramiteNoSetor != null)
				if (emTramiteNoSetor.booleanValue())
					sql.append(" AND ps.dat_fim_tramite IS NULL ");
				else
					sql.append(" AND ps.dat_fim_tramite IS NOT NULL ");

			// possuiLiminar
			if (possuiLiminar != null)
				if (possuiLiminar.booleanValue())
					sql.append(" AND ps.flg_liminar = 'S' ");
				else
					sql.append(" AND ps.flg_liminar = 'N' ");

			// possuiPreferencia
			if (possuiPreferencia != null)
				if (possuiPreferencia.booleanValue())
					sql.append(" AND ps.flg_preferencia = 'S' ");
				else
					sql.append(" AND ps.flg_preferencia = 'N' ");

			// sobrestado
			if (sobrestado != null)
				if (sobrestado.booleanValue())
					sql.append(" AND ps.flg_sobrestado = 'N' ");
				else
					sql.append(" AND ps.flg_sobrestado = 'S' ");

			// assunto
			if (descricaoAssunto != null && descricaoAssunto.trim().length() >= 3) {
				descricaoAssunto = descricaoAssunto.replace("|", "\\|");
				descricaoAssunto = descricaoAssunto.replace('%', ' ');
				sql.append(" AND CONTAINS(ass.dsc_assunto_completo, '" + descricaoAssunto + "') > 1 ");
			}

			// if( codigoAssunto != null && codigoAssunto.trim().length() == 9 )
			// {
			if (codigoAssunto != null) {
				sql.append(" AND ass.cod_assunto = '" + codigoAssunto + "' ");
			}

			if (complementoAssunto != null
					&& complementoAssunto.trim().length() > 0) {
				complementoAssunto = "%" + complementoAssunto.replace(' ', '%') + "%";
				sql.append(" AND ps.dsc_complemento_assunto LIKE '"
						+ complementoAssunto + "' ");
			}

			// Parâmetros de fase
			if (semFase != null && semFase.booleanValue()) {
				sql.append(" AND ps.seq_fase_atual IS NULL ");
			} else {
				if (semFase != null && !semFase.booleanValue())
					sql.append(" AND ps.seq_fase_atual IS NOT NULL ");

				if (idTipoUltimaFaseSetor != null && idTipoUltimaFaseSetor > 0)
					sql.append(" AND tfs.seq_tipo_fase_setor = "
							+ idTipoUltimaFaseSetor);

				if (idTipoUltimoStatusSetor != null && idTipoUltimoStatusSetor > 0)
					sql.append(" AND tss.seq_tipo_status_setor = "
							+ idTipoUltimoStatusSetor);

				if (dataFaseInicial != null)
					sql.append(" AND hf.dat_historico_fase >= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(dataFaseInicial) + " 00:00:00"
							+ "', 'DD/MM/YYYY HH24:MI:SS') ");

				if (dataFaseFinal != null)
					sql.append(" AND hf.dat_historico_fase <= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(dataFaseFinal) + " 23:59:59"
							+ "', 'DD/MM/YYYY HH24:MI:SS') ");
			}

			// Parâmetros de distribuição
			if (semDistribuicao != null && semDistribuicao.booleanValue()) {
				sql.append(" AND ps.seq_distribuicao_atual IS NULL ");
			} else {
				if (semDistribuicao != null && !semDistribuicao.booleanValue())
					sql.append(" AND ps.seq_distribuicao_atual IS NOT NULL ");

				if (siglaUsuarioDistribuicao != null
						&& siglaUsuarioDistribuicao.trim().length() > 0)
					sql.append(" AND usu.sig_usuario = '"
							+ siglaUsuarioDistribuicao + "' ");

				if (dataDistribuicaoInicial != null)
					sql.append(" AND hdist.dat_distribuicao >= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(dataDistribuicaoInicial)
							+ " 00:00:00" + "', 'DD/MM/YYYY HH24:MI:SS') ");

				if (dataDistribuicaoFinal != null)
					sql.append(" AND hdist.dat_distribuicao <= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(dataDistribuicaoFinal)
							+ " 23:59:59" + "', 'DD/MM/YYYY HH24:MI:SS') ");
			}	

			// Distribuição para o Relator
			if( dataDistribuicaoMinistroInicial != null || dataDistribuicaoMinistroFinal != null ) {
				if( dataDistribuicaoMinistroInicial != null )
					sql.append(" AND smp.DAT_OCORRENCIA >= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(dataDistribuicaoMinistroInicial)
							+ " 00:00:00" + "', 'DD/MM/YYYY HH24:MI:SS') ");
					
				if( dataDistribuicaoMinistroFinal != null )
					sql.append(" AND smp.DAT_OCORRENCIA <= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(dataDistribuicaoMinistroFinal)
							+ " 23:59:59" + "', 'DD/MM/YYYY HH24:MI:SS') ");					
					
				sql.append(" AND smp.COD_OCORRENCIA IN ('RE', 'SU', 'RG') ");
				sql.append(" AND smp.flg_relator_atual = 'S' ");			
			}				
															
			// Última situação do processo
			if( faseProcessualAtual != null && faseProcessualAtual ) {
				sql.append(" AND ( EXISTS ( SELECT rpfpa.seq_objeto_incidente FROM judiciario.recurso_processo rpfpa " +
			               "                    WHERE rpfpa.dat_interposicao = ( SELECT MAX(rpfpa2.dat_interposicao) " +
			               "                                                          FROM judiciario.recurso_processo rpfpa2 " +
			               "                                                          WHERE  rpfpa2.sig_classe_proces = rpfpa.sig_classe_proces" +
					"																  		 AND rpfpa2.num_processo = rpfpa.num_processo ) " +
			               "                          AND rpfpa.seq_objeto_incidente = ps.seq_objeto_incidente " +
			               "               ) " +
			               "        OR EXISTS ( SELECT pfpa.seq_objeto_incidente FROM judiciario.processo pfpa " +
			               "                         WHERE pfpa.seq_objeto_incidente = oips.seq_objeto_incidente " +
			               "                               AND NOT EXISTS ( SELECT oifpa.seq_objeto_incidente " +
			               "                                                    FROM judiciario.objeto_incidente oifpa " +
			               "              			                               WHERE oifpa.seq_objeto_incidente_principal = pfpa.seq_objeto_incidente " +
			               "                                                             AND oifpa.tip_objeto_incidente = 'RC') " +
			               "                   ) " +
			               "    ) ");
			}
			


			//categorias da parte
			if ( idCategoriaPartePesquisa != null && idCategoriaPartePesquisa > 0 )
				sql.append(" AND cp.cod_categoria = " + idCategoriaPartePesquisa );
			
			//nome da parte
			if ( nomeParte != null && nomeParte.trim().length() > 0 ) {
				//nomeParte = '%' + nomeParte.replace(' ', '%') + '%';
				//nomeParte = nomeParte.toUpperCase();
				//sql.append(" AND cpp.nom_parte LIKE '" + nomeParte + "' ");
				nomeParte = nomeParte.replace('%', ' ');
				sql.append(" AND CONTAINS(cp.nom_jurisdicionado, '" + nomeParte + "') > 1 ");
			}			
			
			// Parâmetros de deslocamento
			if (semLocalizacao != null && semLocalizacao.booleanValue()) {
				sql.append(" AND ps.seq_deslocamento_atual IS NULL ");
			} else {
				if (semLocalizacao != null && !semLocalizacao.booleanValue())
					sql.append(" AND ps.seq_deslocamento_atual IS NOT NULL ");

				if ((numeroSala != null && numeroSala.trim().length() > 0)
						|| (numeroArmario != null && numeroArmario.trim()
								.length() > 0)
						|| (numeroEstante != null && numeroEstante.trim()
								.length() > 0)
						|| (numeroPrateleira != null && numeroPrateleira.trim()
								.length() > 0)
						|| (numeroColuna != null && numeroColuna.trim()
								.length() > 0)) {
					sql.append(" AND CONTAINS (hd.flg_atualizado,' ");

					if (numeroSala != null && numeroSala.trim().length() > 0)
						sql.append(" (%" + numeroSala
								+ "% within NUM_SALA) AND ");

					if (numeroArmario != null
							&& numeroArmario.trim().length() > 0)
						sql.append(" (%" + numeroArmario
								+ "% within NUM_ARMARIO) AND ");

					if (numeroEstante != null
							&& numeroEstante.trim().length() > 0)
						sql.append(" (%" + numeroEstante
								+ "% within NUM_ESTANTE) AND ");

					if (numeroPrateleira != null
							&& numeroPrateleira.trim().length() > 0)
						sql.append(" (%" + numeroPrateleira
								+ "% within NUM_PRATELEIRA) AND ");

					if (numeroColuna != null
							&& numeroColuna.trim().length() > 0)
						sql.append(" (%" + numeroColuna
								+ "% within NUM_COLUNA) ");

					// verifica se existe AND por último, caso exista retira o mesmo
					if (sql.substring(sql.length() - 6, sql.length()).equals(
							") AND ")) {
						StringBuffer sqlTemp = new StringBuffer(sql.substring(
								0, sql.length() - 4));
						sql.delete(0, sql.length());
						sql.append(sqlTemp);
					}

					sql.append(" ' ) > 0 ");

				}

				if (dataRemessaInicial != null)
					sql.append(" AND hd.dat_remessa >= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(dataRemessaInicial) + " 00:00:00"
							+ "', 'DD/MM/YYYY HH24:MI:SS') ");

				if (dataRemessaFinal != null)
					sql.append(" AND hd.dat_remessa <= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(dataRemessaFinal) + " 23:59:59"
							+ "', 'DD/MM/YYYY HH24:MI:SS') ");

				if (dataRecebimentoInicial != null)
					sql.append(" AND hd.dat_recebimento >= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(dataRecebimentoInicial)
							+ " 00:00:00" + "', 'DD/MM/YYYY HH24:MI:SS') ");

				if (dataRecebimentoFinal != null)
					sql.append(" AND hd.dat_recebimento <= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(dataRecebimentoFinal) + " 23:59:59"
							+ "', 'DD/MM/YYYY HH24:MI:SS') ");
			}

			// julgado
			if (julgado != null) {
				sql.append(" AND ");
				if (!julgado.booleanValue()) {
					sql.append(" NOT ");
				}

				sql.append(" (EXISTS (SELECT cvt.seq_objeto_incidente "
								+ " FROM stf.controle_votos cvt "
								+ " WHERE cvt.cod_tipo_texto = " + TipoTexto.EMENTA.getCodigo()
								+ " AND cvt.cod_ministro = ( select s.cod_ministro from stf.sit_min_processos s " +
																" where s.seq_objeto_incidente = cvt.seq_objeto_incidente and " + 
																	  " s.cod_ocorrencia in ('RE','SU','RG') and s.FLG_RELATOR_ATUAL = 'S' ) "
								+ " AND cvt.seq_objeto_incidente = ps.seq_objeto_incidente)) ");
			}

			//observacao contida no deslocamento atual
			if( obsDeslocamento!= null && obsDeslocamento.trim().length() > 0) {
				//obsDeslocamento = "%" + obsDeslocamento.replace(' ', '%') + "%";
				obsDeslocamento = obsDeslocamento.replace('%', ' ');
				sql.append(" AND CONTAINS(hd.obs_deslocamento, '" + obsDeslocamento + "') > 1 ");
			}

			// vista
			if (semVista != null) {
				sql.append(" AND ag.flg_vistas IS NOT NULL ");

				sql.append(" AND mns.cod_ministro = (SELECT m.cod_ministro FROM stf.ministros m WHERE m.cod_setor = "
								+ idSetor
								+ " AND m.dat_afast_ministro IS NULL) ");

				if (semVista.booleanValue())
					sql.append(" AND ag.flg_vistas = 'S' ");
				else
					sql.append(" AND ag.flg_vistas = 'N' ");
			}
			
			// andamentos
			if (listaIncluirAndamentos != null && listaIncluirAndamentos.size() > 0) {
				sql.append(" AND EXISTS( SELECT ap1.DSC_OBSER_AND "
								+ " FROM stf.ANDAMENTO_PROCESSOS ap1 "
								+ " LEFT JOIN stf.ANDAMENTOS an1 ON ap1.cod_andamento = an1.cod_andamento "
								+ " WHERE an1.cod_andamento IN(");

				int ivalor = 1;
				for (Andamento tipoAndamento : listaIncluirAndamentos) {
					if (ivalor < listaIncluirAndamentos.size()) {
						sql.append(tipoAndamento.getId() + ", ");
					} else {
						sql.append(tipoAndamento.getId() + ") ");
					}
					ivalor++;
				}

				if (dataInicialIncluirAndamentos != null) {
					sql.append(" AND ap1.dat_andamento >= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(dataInicialIncluirAndamentos)
							+ " 00:00:00" + "', 'DD/MM/YYYY HH24:MI:SS') ");
				}

				if (dataFinalIncluirAndamentos != null) {
					sql.append(" AND ap1.dat_andamento <= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(dataFinalIncluirAndamentos)
							+ " 23:59:59" + "', 'DD/MM/YYYY HH24:MI:SS') ");
				}

				sql.append(" AND ap1.seq_objeto_incidente = ps.seq_objeto_incidente ) ");

			}
			
			if (listaNaoIncluirAndamentos != null && listaNaoIncluirAndamentos.size() > 0) {
				sql.append(" AND NOT EXISTS( SELECT ap2.DSC_OBSER_AND "
								+ " FROM stf.ANDAMENTO_PROCESSOS ap2 "
								+ " LEFT JOIN stf.ANDAMENTOS an1 ON ap2.cod_andamento = an1.cod_andamento "
								+ " WHERE an1.cod_andamento IN(");

				int ivalor = 1;
				for (Andamento tipoAndamento : listaNaoIncluirAndamentos) {
					if (ivalor < listaNaoIncluirAndamentos.size()) {
						sql.append(tipoAndamento.getId() + ", ");
					} else {
						sql.append(tipoAndamento.getId() + ") ");
					}
					ivalor++;
				}

				if (dataInicialNaoIncluirAndamentos != null) {
					sql.append(" AND ap2.dat_andamento >= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(dataInicialNaoIncluirAndamentos)
							+ " 00:00:00" + "', 'DD/MM/YYYY HH24:MI:SS') ");
				}

				if (dataFinalNaoIncluirAndamentos != null) {
					sql.append(" AND ap2.dat_andamento <= to_date('"
							+ new SimpleDateFormat("dd/MM/yyyy")
									.format(dataFinalNaoIncluirAndamentos)
							+ " 23:59:59" + "', 'DD/MM/YYYY HH24:MI:SS') ");
				}

				sql.append(" AND ap2.seq_objeto_incidente = ps.seq_objeto_incidente ) ");

			}
			
//			if( tipoRelatorio != null && tipoRelatorio.equalsIgnoreCase(ProcessoSetor.RELATORIO_SINTETICO_ASSUNTO) )
//				sql.append(" AND ap.num_ordem = 1 ");

			if( group ) {
				if( groupByDistribuicao ) {
					sql.append(" AND usu.nom_usuario IS NOT NULL GROUP BY usu.nom_usuario ORDER BY usu.nom_usuario ");
				} else if( groupByFase ) {
					if( semFase == null || !semFase.booleanValue() ) {
						if( groupByFaseStatus ) {
							sql.append(" AND tfs.dsc_tipo_fase_setor IS NOT NULL GROUP BY tfs.dsc_tipo_fase_setor, tss.dsc_Tipo_status_setor");
						} else {
							sql.append(" AND tfs.dsc_tipo_fase_setor IS NOT NULL GROUP BY tfs.dsc_tipo_fase_setor");
						}
						
						sql.append(" ORDER BY tfs.dsc_tipo_fase_setor ASC ");
					}
				} else if( groupByDeslocamento && (semLocalizacao == null || !semLocalizacao) ) {
					sql.append(" AND s.dsc_secao IS NOT NULL GROUP BY s.dsc_secao ORDER BY s.dsc_secao ");
				} else if( groupByAssunto != null && groupByAssunto ) {
					sql.append(" AND ass.dsc_assunto_completo IS NOT NULL GROUP BY ass.dsc_assunto_completo ORDER BY ass.dsc_assunto_completo ");
				} 
				
			} else if (orderByProcesso != null && orderByProcesso.booleanValue() && (protocoloNaoAutuadoCheckbox == null || !protocoloNaoAutuadoCheckbox)) {
				if (orderByCrescente != null && !orderByCrescente.booleanValue()) {
					sql.append(" ORDER BY sigla_classe DESC, numero_processo DESC ");
				} else {
					sql.append(" ORDER BY sigla_classe, numero_processo ");
				}
			} else if (orderByProtocolo != null	&& orderByProtocolo.booleanValue()) {
				if (orderByCrescente != null
						&& !orderByCrescente.booleanValue()) {
					sql.append(" ORDER BY protocolo.ano_protocolo DESC, protocolo.num_protocolo DESC ");
				} else {
					sql.append(" ORDER BY protocolo.ano_protocolo, protocolo.num_protocolo ");
				}
			} else if (orderByValorGut != null && orderByValorGut.booleanValue()) {
				if (orderByCrescente != null
						&& !orderByCrescente.booleanValue()) {
					sql.append(" ORDER BY NVL((ps.num_indicador_gravidade * ps.num_indicador_tendencia * ps.num_indicador_urgencia),0) DESC ");
				} else {
					sql.append(" ORDER BY NVL((ps.num_indicador_gravidade * ps.num_indicador_tendencia * ps.num_indicador_urgencia),0) ");
				}
			} else if (orderByDataEntrada != null
					&& orderByDataEntrada.booleanValue()) {
				if (orderByCrescente != null
						&& !orderByCrescente.booleanValue()) {
					sql.append(" ORDER BY ps.dat_entrada_setor DESC ");
				} else {
					sql.append(" ORDER BY ps.dat_entrada_setor ");
				}
			} else if (orderByAssunto != null && orderByAssunto.booleanValue()) {
				// implementado mais abaixo por comparator
				/*if (orderByCrescente != null
						&& !orderByCrescente.booleanValue()) {
					sql.append(" ORDER BY sigla_classe DESC, numero_processo DESC, "
							+ " ano_protocolo DESC, numero_protocolo DESC ");
				} else {
					sql.append(" ORDER BY sigla_classe, numero_processo, "
							 + " ano_protocolo, numero_protocolo ");
				}*/
			}	
			
//			System.out.println(" ---- Select Nativo Relatório: " + sql.toString());
			
			return sql;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	@SuppressWarnings("unused")
	private Boolean possuiGroupBy(Boolean groupByFase, Boolean groupByFaseStatus, Boolean groupByDistribuicao, 
								Boolean groupByDeslocamento, Boolean groupByAssunto) {
		Boolean possui = Boolean.FALSE;
		
		if( (groupByAssunto != null && groupByAssunto) || (groupByDeslocamento != null && groupByDeslocamento) || 
				(groupByDistribuicao != null && groupByDistribuicao) || (groupByFase != null && groupByFase) ||
				(groupByFaseStatus != null && groupByFaseStatus) ) {
			possui = Boolean.TRUE;
		}
		
		return possui;
	}
	
	public List<ProcessoSetor> pesquisarProcessoSetor(SecaoSetor secaoSetor,
			Long idSetor) throws DaoException {

		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		List<ProcessoSetor> listaProcessos = null;

		try {

			hql.append(" select ps from ProcessoSetor ps "
					+ " where ps.deslocamentoAtual.id in ( "
					+ "     select hd.id from HistoricoDeslocamento hd "
					+ "        where hd.id = ps.deslocamentoAtual.id ");

			if (secaoSetor.getSecao() != null) {
				hql.append("			 and ((hd.secaoOrigem = "
						+ secaoSetor.getSecao().getId()
						+ "		    	    or hd.secaoDestino = "
						+ secaoSetor.getSecao().getId() + "               )) ");
			}

			if (idSetor != null)
				hql.append(" and hd.setor.id = " + idSetor);

			hql.append(" ) ");

			if (idSetor != null)
				hql.append(" and ps.setor.id = " + idSetor);

			Query q = session.createQuery(hql.toString());
			listaProcessos = q.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return listaProcessos;
	}

	public void evictObjectSession(Object obj) throws DaoException {
		Session session = retrieveSession();
		session.evict(obj);
	}

	public void refreshObjectSession(Object obj) throws DaoException {
		Session session = retrieveSession();
		session.refresh(obj);
	}

	@SuppressWarnings("unchecked")
	public SearchResult pesquisarProcessoSetor(ProcessoSetorSearchData sdProcessoSetor) throws DaoException {
		try {

			if (sdProcessoSetor == null){
				return null;
			}

			StringBuffer hql = new StringBuffer();
			StringBuffer hqlSql = new StringBuffer();
			StringBuffer hqlQtd = new StringBuffer();

			/*
			 * ------------------------------------- SELECT & JOINS
			 * ----------------------------------
			 */

			if ((sdProcessoSetor.idCategoriaPartePesquisa != null && sdProcessoSetor.idCategoriaPartePesquisa > 0)
					|| SearchData.stringNotEmpty(sdProcessoSetor.nomeParte)
					|| SearchData.hasDate(sdProcessoSetor.dataDistribuicaoMinistro)
					|| SearchData.stringNotEmpty(sdProcessoSetor.codigosAssuntosVirgula)
					|| SearchData.stringNotEmpty(sdProcessoSetor.descricaoAssunto)
					|| (sdProcessoSetor.tipoOrderProcesso != null && sdProcessoSetor.tipoOrderProcesso.equals(TipoOrdemProcesso.ASSUNTO))) {
				hqlQtd.append(" SELECT COUNT(DISTINCT ps) ");
				if (sdProcessoSetor.getHasOnlyCount() == null || !sdProcessoSetor.getHasOnlyCount())
					hqlSql.append(" SELECT DISTINCT ps ");
			} else {
				hqlQtd.append(" SELECT COUNT(ps) ");
				if (sdProcessoSetor.getHasOnlyCount() == null || !sdProcessoSetor.getHasOnlyCount())
					hqlSql.append(" SELECT ps ");
			}

			hql.append(" FROM ProcessoSetor ps JOIN ps.objetoIncidente oips ");

			if (sdProcessoSetor.protocoloNaoAutuado == null || !sdProcessoSetor.protocoloNaoAutuado){
				hql.append(" LEFT JOIN ps.objetoIncidente.principal oiprinc LEFT JOIN ps.objetoIncidente.principal.anterior oiant ");
			}

			if (sdProcessoSetor.idGrupoProcessoSetor != null && sdProcessoSetor.idGrupoProcessoSetor > 0){
				hql.append(" JOIN ps.grupos gr ");
			}

			if (sdProcessoSetor.semVista != null){
				hql.append(" , Agendamento a ");
			}

			if (SearchData.hasDate(sdProcessoSetor.dataDistribuicaoMinistro)){
				hql.append(" , SituacaoMinistroProcesso smp ");
			}

			if ((sdProcessoSetor.idCategoriaPartePesquisa != null && sdProcessoSetor.idCategoriaPartePesquisa > 0) || SearchData.stringNotEmpty(sdProcessoSetor.nomeParte)){
				hql.append(" , Parte pv ");
			}

			if (SearchData.stringNotEmpty(sdProcessoSetor.codigosAssuntosVirgula)
					|| SearchData.stringNotEmpty(sdProcessoSetor.descricaoAssunto)
					|| (sdProcessoSetor.tipoOrderProcesso != null && sdProcessoSetor.tipoOrderProcesso.equals(TipoOrdemProcesso.ASSUNTO))){
				hql.append(" ,Assunto ah, AssuntoProcesso ahproc ");
			}

			boolean pesquisarPorProtocolo = sdProcessoSetor.tipoOrderProcesso != null && sdProcessoSetor.tipoOrderProcesso.equals(TipoOrdemProcesso.PROTOCOLO);		
		

			if (isPesquisarComProtocolo(sdProcessoSetor.anoProtocolo, sdProcessoSetor.numeroProtocolo, pesquisarPorProtocolo, sdProcessoSetor.protocoloNaoAutuado)){
				hql.append(" ,Protocolo protocoloSTF ");
			}
			
			if(sdProcessoSetor.origem != null && sdProcessoSetor.origem > 0){
				hql.append(" , HistoricoProcessoOrigem hpo ");
			}			

			if (sdProcessoSetor.protocoloNaoAutuado == null || !sdProcessoSetor.protocoloNaoAutuado) {
				if ((sdProcessoSetor.protocoloNaoAutuado == null || !sdProcessoSetor.protocoloNaoAutuado)
						&& ((SearchData.stringNotEmpty(sdProcessoSetor.siglasClassesProcessuaisAgrupadas)) 
								|| (SearchData.stringNotEmpty(sdProcessoSetor.siglasClassesProcessuaisAgrupadas) 
										&& sdProcessoSetor.siglasClassesProcessuaisAgrupadas.equals(Classe.SIGLAS_CLASSES_PROCESSUAIS_ORIGINARIOS)))){
					hql.append(" ,Classe cp ");
				}

				if (sdProcessoSetor.recurso != null){
					hql.append(" ,RecursoProcesso rp ");
				}

				if ((SearchData.stringNotEmpty(sdProcessoSetor.siglasClassesProcessuaisAgrupadas))
						|| sdProcessoSetor.numeroProcesso != null
						|| SearchData.stringNotEmpty(sdProcessoSetor.sigla)
						|| (sdProcessoSetor.recurso != null || sdProcessoSetor.possuiRecurso != null)
						|| SearchData.stringNotEmpty(sdProcessoSetor.codigoTipoMeioProcesso)
						|| (sdProcessoSetor.repercussaoGeral != null && sdProcessoSetor.repercussaoGeral)
						|| sdProcessoSetor.idTipoTarefa != null
						|| (sdProcessoSetor.idCategoriaPartePesquisa != null && sdProcessoSetor.idCategoriaPartePesquisa > 0)
						|| SearchData.stringNotEmpty(sdProcessoSetor.nomeParte)
						|| (sdProcessoSetor.tipoOrderProcesso != null && sdProcessoSetor.tipoOrderProcesso.equals(TipoOrdemProcesso.PROCESSO))
						|| SearchData.hasDate(sdProcessoSetor.dataDistribuicaoMinistro)
						|| sdProcessoSetor.getPesquisaLegislacao()
						|| (SearchData.stringNotEmpty(sdProcessoSetor.codigosAssuntosVirgula) || SearchData.stringNotEmpty(sdProcessoSetor.descricaoAssunto))
						|| (sdProcessoSetor.tipoOrderProcesso != null && sdProcessoSetor.tipoOrderProcesso.equals(TipoOrdemProcesso.ASSUNTO)) 
						|| (sdProcessoSetor.localizadosNoSetor != null && sdProcessoSetor.localizadosNoSetor != true )
						|| SearchData.stringNotEmpty(sdProcessoSetor.siglaRecursoUnificada)){
					hql.append(" ,Processo processoSTF ");
				}

				if ((SearchData.stringNotEmpty(sdProcessoSetor.siglaTipoJulgamento) && !sdProcessoSetor.siglaTipoJulgamento.equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla()))){
					hql.append(" ,IncidenteJulgamento ij ");	
				}

				if (sdProcessoSetor.idTipoTarefa != null){
					hql.append(" , TarefaSetor ts LEFT JOIN  ts.processos pts ");
				}

				if (sdProcessoSetor.getPesquisaLegislacao()){
					hql.append(" , LegislacaoProcesso lp ");
				}
			}
			
			if (TipoOrdemProcesso.TEMA.equals(sdProcessoSetor.tipoOrderProcesso))
				hql.append(" LEFT JOIN oips.tema tema ");

			if (TipoOrdemProcesso.MOTIVO_INAPTIDAO.equals(sdProcessoSetor.tipoOrderProcesso) || (sdProcessoSetor.motivoInaptidao != null && sdProcessoSetor.motivoInaptidao.size() > 0))
				hql.append(" LEFT JOIN oips.motivoInaptidao mi ");
			
			/*********************************************************
			 ********************************************************* 
			 ********************** WHERE ****************************
			 ********************************************************* 
			 ********************************************************/

			hql.append(" WHERE 1=1 ");
			
			if (TipoOrdemProcesso.TEMA.equals(sdProcessoSetor.tipoOrderProcesso)) {
				if (TipoOrdem.CRESCENTE.equals(sdProcessoSetor.tipoOrdem))
					hql.append(" AND (tema.numeroSequenciaTema = (SELECT min(vpt.tema.numeroSequenciaTema) FROM ProcessoTema vpt WHERE vpt.objetoIncidente = oips AND vpt.tema.tipoTema.id = 1) OR tema.numeroSequenciaTema IS NULL) ");
				else
					hql.append(" AND (tema.numeroSequenciaTema = (SELECT max(vpt.tema.numeroSequenciaTema) FROM ProcessoTema vpt WHERE vpt.objetoIncidente = oips AND vpt.tema.tipoTema.id = 1) OR tema.numeroSequenciaTema IS NULL) ");
			}
			
			if (TipoOrdemProcesso.MOTIVO_INAPTIDAO.equals(sdProcessoSetor.tipoOrderProcesso)) {
				if (TipoOrdem.CRESCENTE.equals(sdProcessoSetor.tipoOrdem))
					hql.append(" AND (mi = (SELECT min(mip.motivoInaptidao) FROM MotivoInaptidaoProcesso mip WHERE mip.objetoIncidente = oips) OR mi IS NULL) ");
				else
					hql.append(" AND (mi = (SELECT max(mip.motivoInaptidao) FROM MotivoInaptidaoProcesso mip WHERE mip.objetoIncidente = oips) OR mi IS NULL) ");
			}
			
			if (sdProcessoSetor.motivoInaptidao != null && sdProcessoSetor.motivoInaptidao.size() > 0) {
//				hql.append(" AND mi.id IN(:motivoInaptidao) ");
				hql.append(" AND mi NOT IN(SELECT mip.motivoInaptidao FROM MotivoInaptidaoProcesso mip WHERE mip.motivoInaptidao.id NOT IN (:motivoInaptidao))");
				hql.append(" AND NOT EXISTS (SELECT mip.id FROM MotivoInaptidaoProcesso mip WHERE mip.objetoIncidente.id = ps.objetoIncidente AND mip.motivoInaptidao.id NOT IN (:motivoInaptidao))");
			}
			
			hql.append(" AND CASE WHEN (ps.objetoIncidente.tipoObjetoIncidente = 'RC') THEN (select COUNT(rp1) from RecursoProcesso rp1 where rp1 = ps.objetoIncidente.id AND rp1.ativo = 'S') ");
			hql.append(" WHEN (ps.objetoIncidente.tipoObjetoIncidente = 'IJ') THEN (select COUNT(ij1) from IncidenteJulgamento ij1 where ij1.id = ps.objetoIncidente.id AND ij1.ativo = 'S') ");
			hql.append(" WHEN (ps.objetoIncidente.tipoObjetoIncidente = 'PR') THEN 1 ELSE 0 END = 1 ");
		
			// -------------------- JOINS ------------------
			if((sdProcessoSetor.origem != null && sdProcessoSetor.origem > 0)){
				hql.append(" AND hpo.objetoIncidente = oiprinc.id and hpo.principal = 'S' ");
			}	
			
			if (isPesquisarComProtocolo(sdProcessoSetor.anoProtocolo,sdProcessoSetor.numeroProtocolo, pesquisarPorProtocolo,sdProcessoSetor.protocoloNaoAutuado)) {
				if (sdProcessoSetor.protocoloNaoAutuado == null || !sdProcessoSetor.protocoloNaoAutuado){
					hql.append(" AND protocoloSTF.id = oiant.id ");
				}else if (sdProcessoSetor.protocoloNaoAutuado != null && sdProcessoSetor.protocoloNaoAutuado){
					hql.append(" AND protocoloSTF = oips.id ");
				}
			}

			/*
			if (SearchData.stringNotEmpty(sdProcessoSetor.classesProcessuaisPorVirgula)) {
				hql.append(" AND processoSTF.siglaClasseProcessual in ('" + sdProcessoSetor.classesProcessuaisPorVirgula.replace(",", "','") + "')");
			}*/
			
			if (SearchData.stringNotEmpty(sdProcessoSetor.classesProcessuaisPorVirgula)) {
				hql.append(" AND ps.siglaClasseProcessual in ('" + sdProcessoSetor.classesProcessuaisPorVirgula.replace(",", "','").replace(" ", "") + "')");
			}

			if (SearchData.stringNotEmpty(sdProcessoSetor.codigosAssuntosVirgula)
					|| SearchData.stringNotEmpty(sdProcessoSetor.descricaoAssunto)
					|| (sdProcessoSetor.tipoOrderProcesso != null && sdProcessoSetor.tipoOrderProcesso.equals(TipoOrdemProcesso.ASSUNTO))){
				if (sdProcessoSetor.protocoloNaoAutuado == null || !sdProcessoSetor.protocoloNaoAutuado){
					hql.append(" AND ah.id = ahproc.assunto.id AND oiprinc.id = ahproc.objetoIncidente.id ");
				}else{
					hql.append(" AND ah.id = ahproc.assunto.id AND oips.id = ahproc.objetoIncidente.id ");
				}
			}
			
			if (SearchData.stringNotEmpty(sdProcessoSetor.codigosAssuntosVirgula)) {
				hql.append(" AND  ah.id in ('" + sdProcessoSetor.codigosAssuntosVirgula.replace(",", "','") + "')");
			}

			if ((sdProcessoSetor.idCategoriaPartePesquisa != null && sdProcessoSetor.idCategoriaPartePesquisa > 0) || SearchData.stringNotEmpty(sdProcessoSetor.nomeParte)){
				hql.append(" AND pv.objetoIncidente.id = oips.id ");
			}

			if (sdProcessoSetor.semVista != null){
				hql.append(" AND a.id.objetoIncidente = oips.id ");
			}

			if (SearchData.hasDate(sdProcessoSetor.dataDistribuicaoMinistro)){
				hql.append(" AND smp.objetoIncidente.id = oips.id ");
			}

			if (sdProcessoSetor.getPesquisaLegislacao()){
				hql.append(" AND lp.objetoIncidente.id = oips.id ");
			}

			if (sdProcessoSetor.protocoloNaoAutuado == null || !sdProcessoSetor.protocoloNaoAutuado) {
				if (sdProcessoSetor.recurso != null){
					hql.append(" AND rp.principal.id = oiprinc.id ");
				}

				if ((SearchData.stringNotEmpty(sdProcessoSetor.siglasClassesProcessuaisAgrupadas))
						|| (sdProcessoSetor.numeroProcesso != null)
						|| (SearchData.stringNotEmpty(sdProcessoSetor.sigla))
						|| (sdProcessoSetor.recurso != null || sdProcessoSetor.possuiRecurso != null)
						|| (SearchData.stringNotEmpty(sdProcessoSetor.codigoTipoMeioProcesso))
						|| (sdProcessoSetor.repercussaoGeral != null && sdProcessoSetor.repercussaoGeral)
						|| (sdProcessoSetor.idTipoTarefa != null)
						|| (sdProcessoSetor.idCategoriaPartePesquisa != null && sdProcessoSetor.idCategoriaPartePesquisa > 0)
						|| (SearchData.stringNotEmpty(sdProcessoSetor.nomeParte))
						|| (sdProcessoSetor.tipoOrderProcesso != null && sdProcessoSetor.tipoOrderProcesso.equals(TipoOrdemProcesso.PROCESSO))
						|| (SearchData.hasDate(sdProcessoSetor.dataDistribuicaoMinistro))
						|| (sdProcessoSetor.getPesquisaLegislacao())
						|| (SearchData.stringNotEmpty(sdProcessoSetor.codigosAssuntosVirgula) || SearchData.stringNotEmpty(sdProcessoSetor.descricaoAssunto))
						|| (sdProcessoSetor.tipoOrderProcesso != null && sdProcessoSetor.tipoOrderProcesso.equals(TipoOrdemProcesso.ASSUNTO))
						|| (SearchData.stringNotEmpty(sdProcessoSetor.siglaRecursoUnificada))){
					hql.append(" AND processoSTF.id = oiprinc.id ");
				}

				if ((SearchData.stringNotEmpty(sdProcessoSetor.siglaTipoJulgamento) && !sdProcessoSetor.siglaTipoJulgamento.equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla()))){
					hql.append(" AND ij.id = oips.id ");
				}

			}
			
			if(sdProcessoSetor.origem != null && sdProcessoSetor.origem > 0 ){
				hql.append(" AND hpo.origem = :origem ");
				hql.append(" AND hpo.id = CASE ");
				hql.append("    WHEN (SELECT COUNT (*) ");
				hql.append(" 		   FROM HistoricoProcessoOrigem hpoaux ");
				hql.append(" 		  WHERE hpoaux.objetoIncidente = hpo.objetoIncidente ");
				hql.append(" 				AND hpoaux.tipoHistorico = 'O' ");
				hql.append(" 			AND hpoaux.principal = 'S') > 0 ");
				hql.append("   THEN ");
				hql.append(" 	   (SELECT MIN (hpoaux.id) ");
				hql.append(" 		  FROM HistoricoProcessoOrigem hpoaux ");
				hql.append(" 		 WHERE hpoaux.objetoIncidente = hpo.objetoIncidente ");
				hql.append(" 			   AND hpoaux.tipoHistorico = 'O' ");
				hql.append(" 			   AND hpoaux.principal = 'S') ");
				hql.append("   WHEN (SELECT COUNT (*) ");
				hql.append(" 		   FROM HistoricoProcessoOrigem hpoaux ");
				hql.append(" 		  WHERE hpoaux.objetoIncidente = hpo.objetoIncidente ");
				hql.append(" 				AND hpoaux.tipoHistorico = 'O' ");
				hql.append(" 				AND hpoaux.principal = 'N') > 0 ");
				hql.append("    THEN ");
				hql.append(" 	   (SELECT MIN (hpoaux.id) ");
				hql.append(" 		  FROM HistoricoProcessoOrigem hpoaux ");
				hql.append(" 		 WHERE hpoaux.objetoIncidente = hpo.objetoIncidente ");
				hql.append(" 			   AND hpoaux.tipoHistorico = 'O' ");
				hql.append(" 			   AND hpoaux.principal = 'N') ");
				hql.append("   ELSE ");
				hql.append(" 	   NULL ");
				hql.append(" END ");
			}

			if (sdProcessoSetor.protocoloNaoAutuado == null || !sdProcessoSetor.protocoloNaoAutuado) {
				if (SearchData.stringNotEmpty(sdProcessoSetor.siglasClassesProcessuaisAgrupadas)) {
					hql.append(" AND processoSTF.siglaClasseProcessual = cp.id ");
					if (sdProcessoSetor.siglasClassesProcessuaisAgrupadas.equals(Classe.SIGLAS_CLASSES_PROCESSUAIS_AI_RE)) {
						hql.append(" AND processoSTF.siglaClasseProcessual in ('RE', 'AI') ");
					} else if (sdProcessoSetor.siglasClassesProcessuaisAgrupadas.equals(Classe.SIGLAS_CLASSES_PROCESSUAIS_ORIGINARIOS)) {
						hql.append(" AND cp.originario = 'S' ");
					} else if (sdProcessoSetor.siglasClassesProcessuaisAgrupadas.equals(Classe.SIGLAS_CLASSES_PROCESSUAIS_ADI_ADC_ADO_ADPF)) {
						hql.append(" AND processoSTF.siglaClasseProcessual in ('ADI', 'ADC', 'ADO', 'ADPF') ");
					} else if (sdProcessoSetor.siglasClassesProcessuaisAgrupadas.equals(Classe.SIGLAS_CLASSES_PROCESSUAIS_AI_RE_ARE)) {
				    	hql.append(" AND processoSTF.siglaClasseProcessual in ('RE', 'AI', 'ARE') ");
				}

				} else if (SearchData.stringNotEmpty(sdProcessoSetor.sigla)) {
					hql.append(" AND processoSTF.siglaClasseProcessual = :sigla ");
				}

				if (sdProcessoSetor.numeroProcesso != null){
					hql.append(" AND processoSTF.numeroProcessual = :numeroProcesso ");
				}

				if (sdProcessoSetor.possuiRecurso != null) {
					if (sdProcessoSetor.possuiRecurso) {
						hql.append(" AND ");
					} else if (!sdProcessoSetor.possuiRecurso) {
						hql.append(" AND NOT ");
					}
					hql.append(" EXISTS (SELECT rp FROM RecursoProcesso rp WHERE rp.id = oips.id ) ");
				}

				if (sdProcessoSetor.recurso != null && sdProcessoSetor.recurso > 0){
					hql.append(" AND rp.codigoRecurso = :recurso ");
				}

				if (SearchData.stringNotEmpty(sdProcessoSetor.siglaRecursoUnificada)){
					hql.append(" AND EXISTS (SELECT rp FROM RecursoProcesso rp WHERE  oips.id = rp.id AND rp.siglaCadeiaIncidente LIKE :siglaRecursoUnificada) ");
				}

				if (SearchData.stringNotEmpty(sdProcessoSetor.siglaTipoJulgamento)) {
					if (!sdProcessoSetor.siglaTipoJulgamento.equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla())){
						hql.append(" AND ij.tipoJulgamento.sigla = :siglaTipoJulgamento ");
					}else{
						hql.append(" AND NOT EXISTS (SELECT ij FROM IncidenteJulgamento ij WHERE oips.id = ij.id) ");
					}
				}

				if (SearchData.stringNotEmpty(sdProcessoSetor.codigoTipoMeioProcesso)){
					hql.append(" AND processoSTF.tipoMeioProcesso = :codigoTipoMeioProcesso ");
				}

				// Repercussão Geral = S
				if (sdProcessoSetor.repercussaoGeral != null && sdProcessoSetor.repercussaoGeral.booleanValue()){
					hql.append(" AND processoSTF.repercussaoGeral = 'S' ");
				}
				
				
				// Processos Distribuidos a usuários fora do setor
				if (sdProcessoSetor.processosDistribuidosForaDoSetor != null && sdProcessoSetor.processosDistribuidosForaDoSetor.booleanValue()){
					hql.append(" AND ps.distribuicaoAtual.usuario.id NOT IN (select u.usuario.id from UsuarioEGab u where u.setor.id = :idSetor)");
				}
				
				// Processos Distribuidos a usuários inativos
				if (sdProcessoSetor.processosDistribuidosInativos  != null && sdProcessoSetor.processosDistribuidosInativos.booleanValue()){
					hql.append(" AND ps.distribuicaoAtual.usuario.ativo = 'N'");
				}
							

				// tarefa
				if (sdProcessoSetor.idTipoTarefa != null && sdProcessoSetor.idTipoTarefa > 0){
					hql.append(" AND ts.tipoTarefa.id = :idTipoTarefa  AND processoSTF.siglaClasseProcessual = pts.siglaClasseProcessual AND processoSTF.numeroProcessual = pts.numeroProcessual ");
				}
				
				// Parâmetros de Processos por ministro Relator
				if (sdProcessoSetor.codigoMinistroRelator != null && sdProcessoSetor.codigoMinistroRelator > 0) {
					hql.append(" AND processoSTF.ministroRelatorAtual.id = :codigoMinistroRelator ");
				}

			} else if (sdProcessoSetor.protocoloNaoAutuado != null && sdProcessoSetor.protocoloNaoAutuado) {
				// protocolo não autuado
				if (sdProcessoSetor.protocoloNaoAutuado != null && sdProcessoSetor.protocoloNaoAutuado){
					hql.append(" AND oips.tipoObjetoIncidente = 'PI' AND NOT EXISTS (SELECT oiptna FROM ObjetoIncidente oiptna WHERE oiptna.anterior.id = oips.id) ");
				}

				if (SearchData.stringNotEmpty(sdProcessoSetor.codigoTipoMeioProcesso)){
					hql.append(" AND protocoloSTF.tipoMeioProcesso = :codigoTipoMeioProcesso ");
				}

			}
			
			if (sdProcessoSetor.mostraProcessoReautuadoRejeitado == null || !sdProcessoSetor.mostraProcessoReautuadoRejeitado.booleanValue()){
				hql.append(" AND ps.objetoIncidente.principal.situacao not in ('J', 'W', 'K') ");
			}

			if (sdProcessoSetor.anoProtocolo != null && sdProcessoSetor.anoProtocolo > 0){
				hql.append(" AND protocoloSTF.anoProtocolo = :anoProtocolo ");
			}

			if (sdProcessoSetor.numeroProtocolo != null && sdProcessoSetor.numeroProtocolo > 0){
				hql.append(" AND protocoloSTF.numeroProtocolo = :numeroProtocolo ");
			}

			if (sdProcessoSetor.idSecaoUltimoDeslocamento != null){
				hql.append(" AND ps.deslocamentoAtual.secaoDestino.id = :idSecaoUltimoDeslocamento ");
			}

			// Parâmetros de fase
			if (sdProcessoSetor.semFase != null && sdProcessoSetor.semFase) {
				hql.append(" AND ps.faseAtual IS NULL ");
			} else {
				if (sdProcessoSetor.semFase != null && !sdProcessoSetor.semFase){
					hql.append(" AND ps.faseAtual IS NOT NULL ");
				}

				if (sdProcessoSetor.idTipoUltimaFaseSetor != null && (sdProcessoSetor.semFase == null || !sdProcessoSetor.semFase)){
					hql.append(" AND ps.faseAtual.tipoFaseSetor.id = :idTipoUltimaFaseSetor ");
				}

				if (sdProcessoSetor.idTipoUltimoStatusSetor != null){
					hql.append(" AND ps.faseAtual.tipoStatusSetor.id = :idTipoUltimoStatusSetor ");
				}

				if (SearchData.hasDate(sdProcessoSetor.dataFase)) {
					if (sdProcessoSetor.dataFase.getInitialDate() != null){
						hql.append(" AND ps.faseAtual.dataFase >= to_date(:dataFaseInicial, 'DD/MM/YYYY HH24:MI:SS')  ");
					}
					if (sdProcessoSetor.dataFase.getFinalDate() != null){
						hql.append(" AND ps.faseAtual.dataFase <= to_date(:dataFaseFinal, 'DD/MM/YYYY HH24:MI:SS')  ");
					}
				}
			}

			// Parâmetros de distribuição
			if (sdProcessoSetor.semDistribuicao != null && sdProcessoSetor.semDistribuicao.booleanValue()) {
				hql.append(" AND ps.distribuicaoAtual IS NULL ");
				sdProcessoSetor.dataDistribuicao = null;
			} else {
				if (sdProcessoSetor.semDistribuicao != null && !sdProcessoSetor.semDistribuicao){
					hql.append(" AND ps.distribuicaoAtual IS NOT NULL ");
				}

				if (SearchData.stringNotEmpty(sdProcessoSetor.siglaUsuarioDistribuicao) && (sdProcessoSetor.semDistribuicao == null || !sdProcessoSetor.semDistribuicao)){
					hql.append(" AND ps.distribuicaoAtual.usuario.id = :siglaUsuarioDistribuicao ");
				}

				if (SearchData.hasDate(sdProcessoSetor.dataDistribuicao)) {
					if (sdProcessoSetor.dataDistribuicao.getInitialDate() != null){
						hql.append(" AND ps.distribuicaoAtual.dataDistribuicao >= to_date(:dataDistribuicaoInicial, 'DD/MM/YYYY HH24:MI:SS')  ");
					}
					if (sdProcessoSetor.dataDistribuicao.getFinalDate() != null){
						hql.append(" AND ps.distribuicaoAtual.dataDistribuicao <= to_date(:dataDistribuicaoFinal, 'DD/MM/YYYY HH24:MI:SS')  ");
					}
				}

			}

			// Distribuição para o Relator
			if (SearchData.hasDate(sdProcessoSetor.dataDistribuicaoMinistro)) {
				if (sdProcessoSetor.dataDistribuicaoMinistro.getInitialDate() != null){
					hql.append(" AND smp.dataOcorrencia >= to_date(:dataDistribuicaoMinistroInicial, 'DD/MM/YYYY HH24:MI:SS')  ");
				}
				if (sdProcessoSetor.dataDistribuicaoMinistro.getFinalDate() != null){
					hql.append(" AND smp.dataOcorrencia <= to_date(:dataDistribuicaoMinistroFinal, 'DD/MM/YYYY HH24:MI:SS')  ");
				}

				hql.append(" AND smp.ocorrencia IN ('RE', 'SU', 'RG') ");
				hql.append(" AND smp.relatorAtual = 'S' ");
			}

			// Última situação do processo
			if (sdProcessoSetor.faseProcessualAtual != null && sdProcessoSetor.faseProcessualAtual.booleanValue()) {
				hql.append(" AND ( EXISTS ( SELECT rpfpa.id FROM RecursoProcesso rpfpa, Processo prp1 "
						+ "	                    WHERE prp1.id = rpfpa.principal.id AND rpfpa.dataInterposicao = "
						+ "											(SELECT MAX(rpfpa2.dataInterposicao) FROM RecursoProcesso rpfpa2, Processo prp2 "
						+ "															WHERE prp2.id = rpfpa2.principal.id "
						+ "																  AND prp1.siglaClasseProcessual = prp2.siglaClasseProcessual "
						+ "																  AND prp1.numeroProcessual = prp2.numeroProcessual ) "
						+ "   AND rpfpa.id = oips.id)	"
						+ "          OR EXISTS (SELECT pfpa.id FROM Processo pfpa "
						+ "						  WHERE pfpa.id = oips.id "
						+ "						        AND NOT EXISTS (SELECT oifpap.id FROM ObjetoIncidente oifpap "
						+ "													WHERE oifpap.principal = pfpa.id AND oifpap.tipoObjetoIncidente = 'RC')"
						+ "                     ) " + "        )");
			}

			// Parâmetros de deslocamento
			if (sdProcessoSetor.semLocalizacao != null && sdProcessoSetor.semLocalizacao) {
				hql.append(" AND ps.deslocamentoAtual IS NULL ");
			} else if (SearchData.stringNotEmpty(sdProcessoSetor.numeroSala)
					|| SearchData.stringNotEmpty(sdProcessoSetor.numeroArmario)
					|| SearchData.stringNotEmpty(sdProcessoSetor.numeroEstante)
					|| SearchData.stringNotEmpty(sdProcessoSetor.numeroPrateleira)
					|| SearchData.stringNotEmpty(sdProcessoSetor.numeroColuna)
					|| SearchData.hasDate(sdProcessoSetor.dataRemessa)
					|| SearchData.hasDate(sdProcessoSetor.dataRecebimento)
					|| SearchData.stringNotEmpty(sdProcessoSetor.obsDeslocamento)
					|| (sdProcessoSetor.semLocalizacao != null && !sdProcessoSetor.semLocalizacao)) {

				hql.append(" AND ps.deslocamentoAtual IS NOT NULL ");

				if (SearchData.stringNotEmpty(sdProcessoSetor.numeroSala)
						|| SearchData.stringNotEmpty(sdProcessoSetor.numeroArmario)
						|| SearchData.stringNotEmpty(sdProcessoSetor.numeroEstante)
						|| SearchData.stringNotEmpty(sdProcessoSetor.numeroPrateleira)
						|| SearchData.stringNotEmpty(sdProcessoSetor.numeroColuna)) {

					hql.append(" AND contains (ps.deslocamentoAtual.flagAtualizado,' ");

					if (SearchData.stringNotEmpty(sdProcessoSetor.numeroSala)){
						hql.append(" (%" + sdProcessoSetor.numeroSala+ "% within NUM_SALA) AND ");
					}

					if (SearchData.stringNotEmpty(sdProcessoSetor.numeroArmario)){
						hql.append(" (%" + sdProcessoSetor.numeroArmario + "% within NUM_ARMARIO) AND ");
					}

					if (SearchData.stringNotEmpty(sdProcessoSetor.numeroEstante)){
						hql.append(" (%" + sdProcessoSetor.numeroEstante + "% within NUM_ESTANTE) AND ");
					}

					if (SearchData.stringNotEmpty(sdProcessoSetor.numeroPrateleira)){
						hql.append(" (%" + sdProcessoSetor.numeroPrateleira	+ "% within NUM_PRATELEIRA) AND ");
					}

					if (SearchData.stringNotEmpty(sdProcessoSetor.numeroColuna)){
						hql.append(" (%" + sdProcessoSetor.numeroColuna	+ "% within NUM_COLUNA) ");
					}

					// verifica se existe AND por último, caso exista retira o
					// mesmo
					if (hql.substring(hql.length() - 6, hql.length()).equals(") AND ")) {
						StringBuffer hqlTemp = new StringBuffer(hql.substring(0, hql.length() - 4));
						hql.delete(0, hql.length());
						hql.append(hqlTemp);
					}

					hql.append(" ' ) > 0 ");

				}

				if (SearchData.hasDate(sdProcessoSetor.dataRemessa) && (sdProcessoSetor.semLocalizacao == null || !sdProcessoSetor.semLocalizacao)) {
					if (sdProcessoSetor.dataRemessa.getInitialDate() != null){
						hql.append(" AND ps.deslocamentoAtual.dataRemessa >= to_date(:dataRemessaInicial, 'DD/MM/YYYY HH24:MI:SS')  ");
					}
					if (sdProcessoSetor.dataRemessa.getFinalDate() != null){
						hql.append(" AND ps.deslocamentoAtual.dataRemessa <= to_date(:dataRemessaFinal, 'DD/MM/YYYY HH24:MI:SS')  ");
					}
				}

				if (SearchData.hasDate(sdProcessoSetor.dataRecebimento)	&& (sdProcessoSetor.semLocalizacao == null || !sdProcessoSetor.semLocalizacao)) {
					if (sdProcessoSetor.dataRecebimento.getInitialDate() != null){
						hql.append(" AND ps.deslocamentoAtual.dataRecebimento >= to_date(:dataRecebimentoInicial, 'DD/MM/YYYY HH24:MI:SS')  ");
					}
					if (sdProcessoSetor.dataRecebimento.getFinalDate() != null){
						hql.append(" AND ps.deslocamentoAtual.dataRecebimento <= to_date(:dataRecebimentoFinal, 'DD/MM/YYYY HH24:MI:SS')  ");
					}
				}

				if (SearchData.stringNotEmpty(sdProcessoSetor.obsDeslocamento)) {
					sdProcessoSetor.obsDeslocamento = sdProcessoSetor.obsDeslocamento.replace('%', ' ');
					hql.append(" AND CONTAINS(ps.deslocamentoAtual.observacao, '" + sdProcessoSetor.obsDeslocamento + "') > 1 ");
				}
			}

			// Parâmetros de vista
			if (sdProcessoSetor.semVista != null) {
				hql.append(" AND a.vista IS NOT NULL ");
				hql.append(" AND a.ministro.id = (SELECT m.id FROM Ministro m WHERE m.setor.id = :idSetor AND m.dataAfastamento IS NULL) ");

				if (sdProcessoSetor.semVista){
					hql.append(" AND a.vista = 'S' ");
				}else{
					hql.append(" AND a.vista = 'N' ");
				}
			}

			// categorias da parte
			if (sdProcessoSetor.idCategoriaPartePesquisa != null && sdProcessoSetor.idCategoriaPartePesquisa > 0){
				hql.append(" AND pv.categoria.id = :idCategoriaPartePesquisa ");
			}

			// nome da parte
			if (SearchData.stringNotEmpty(sdProcessoSetor.nomeParte)) {
				sdProcessoSetor.nomeParte = "%"	+ sdProcessoSetor.nomeParte.replace(' ', '%').toUpperCase() + "%";
				hql.append(" AND UPPER(pv.nomeJurisdicionado) LIKE :nomeParte ");
			}

			if (SearchData.hasDate(sdProcessoSetor.dataEntrada)) {
				if (sdProcessoSetor.dataEntrada.getInitialDate() != null){
					hql.append(" AND ps.dataEntrada >= to_date(:dataEntradaInicial, 'DD/MM/YYYY HH24:MI:SS') ");
				}
				if (sdProcessoSetor.dataEntrada.getFinalDate() != null){
					hql.append(" AND ps.dataEntrada <= to_date(:dataEntradaFinal, 'DD/MM/YYYY HH24:MI:SS') ");
				}

			}
			if (SearchData.hasDate(sdProcessoSetor.dataSaida)) {
				if (sdProcessoSetor.dataSaida.getInitialDate() != null){
					hql.append(" AND ps.dataSaida >= to_date(:dataSaidaInicial, 'DD/MM/YYYY HH24:MI:SS') ");
				}
				if (sdProcessoSetor.dataSaida.getFinalDate() != null){
					hql.append(" AND ps.dataSaida <= to_date(:dataSaidaFinal, 'DD/MM/YYYY HH24:MI:SS') ");
				}

			}

			if (sdProcessoSetor.idSetor != null){
				hql.append(" AND ps.setor.id = :idSetor ");
			}

			if (sdProcessoSetor.localizadosNoSetor != null) {
				if (sdProcessoSetor.localizadosNoSetor){
					hql.append(" AND ps.dataSaida IS NULL ");
				}else{
					hql.append(" AND ps.dataSaida IS NOT NULL AND ( processoSTF.filtroEmTramitacao = 'S' or processoSTF.filtroEmTramitacao is null)  ");
			}
				}else{
				hql.append(" AND (processoSTF.filtroEmTramitacao = 'S' or processoSTF.filtroEmTramitacao is null )  ");
			}

			if (sdProcessoSetor.emTramiteNoSetor != null) {
				if (sdProcessoSetor.emTramiteNoSetor){
					hql.append(" AND ps.dataFimTramite IS NULL ");
				}else{
					hql.append(" AND ps.dataFimTramite IS NOT NULL ");
				}
			}

			if (sdProcessoSetor.possuiLiminar != null) {
				hql.append(" AND ");
				if (sdProcessoSetor.possuiLiminar.booleanValue()){
					hql.append(" EXISTS ");
				}else{
					hql.append(" NOT EXISTS ");
				}

				hql.append(" (SELECT ip FROM IncidentePreferencia ip WHERE ip.objetoIncidente.id = oips.id AND ip.tipoPreferencia.sigla = '"
						+ TipoIncidentePreferencia.TipoPreferenciaContante.MEDIDA_LIMINAR.getSigla() + "' )");
			}

			if (sdProcessoSetor.possuiPreferencia != null) {
				if (sdProcessoSetor.possuiPreferencia){
					hql.append(" AND ps.preferencia = 'S' ");
				}else{
					hql.append(" AND ps.preferencia = 'N' ");
				}
			}

			if (sdProcessoSetor.sobrestado != null) {
				if (sdProcessoSetor.sobrestado.booleanValue()){
					hql.append(" AND ps.sobrestado = 'S' ");
				}else{
					hql.append(" AND ps.sobrestado = 'N' ");
				}
			}

			if (sdProcessoSetor.julgado != null) {
				hql.append("AND ");
				if (!sdProcessoSetor.julgado) {
					hql.append("NOT ");
				}

				hql.append(" EXISTS (SELECT cvt FROM ControleVoto cvt WHERE cvt.tipoTexto = " + TipoTexto.EMENTA.getCodigo() + " AND cvt.ministro.id = cvt.codigoMinistroRelator AND cvt.objetoIncidente.id = oips.id) ");
			}

			if (sdProcessoSetor.idGrupoProcessoSetor != null){
				hql.append(" AND gr.id = :idGrupoProcessoSetor ");
			}

			if (SearchData.stringNotEmpty(sdProcessoSetor.descricaoAssunto) && sdProcessoSetor.descricaoAssunto.trim().length() >= 3) {
				sdProcessoSetor.descricaoAssunto = sdProcessoSetor.descricaoAssunto.replace("|", "\\|");
				sdProcessoSetor.descricaoAssunto = sdProcessoSetor.descricaoAssunto.replace('%', ' ');
				sdProcessoSetor.descricaoAssunto = sdProcessoSetor.descricaoAssunto.replace('$', ' ');
				hql.append(" AND CONTAINS(ah.descricaoCompleta, '" + sdProcessoSetor.descricaoAssunto + "') > 1 ");
			}

			if (SearchData.stringNotEmpty(sdProcessoSetor.complementoAssunto)) {
				sdProcessoSetor.complementoAssunto = "%" + sdProcessoSetor.complementoAssunto.replace(' ', '%').toUpperCase() + "%";
				hql.append(" AND UPPER(ps.complementoAssunto) LIKE :complementoAssunto ");
			}

			if (sdProcessoSetor.listaIncluirAndamentos != null) {
				hql.append(" AND EXISTS( SELECT ap1.descricaoObservacaoAndamento FROM AndamentoProcesso ap1 WHERE ap1.tipoAndamento.id IN " + SearchData.inClause(sdProcessoSetor.listaIncluirAndamentos, false));

				if (SearchData.hasDate(sdProcessoSetor.dataIncluirAndamentos)) {
					if (sdProcessoSetor.dataIncluirAndamentos.getInitialDate() != null){
						hql.append(" AND ap1.dataAndamento >= to_date(:dataInicialIncluirAndamentos, 'DD/MM/YYYY HH24:MI:SS') ");
					}
					if (sdProcessoSetor.dataIncluirAndamentos.getFinalDate() != null){
						hql.append(" AND ap1.dataAndamento <= to_date(:dataFinalIncluirAndamentos, 'DD/MM/YYYY HH24:MI:SS') ");
					}
				}

				hql.append(" AND ap1.objetoIncidente = ps.objetoIncidente ) ");

			}

			if (sdProcessoSetor.listaNaoIncluirAndamentos != null && sdProcessoSetor.listaNaoIncluirAndamentos.size() > 0) {
				hql.append(" AND NOT EXISTS( SELECT ap2.descricaoObservacaoAndamento "
						+ " FROM AndamentoProcesso ap2"
						+ " WHERE ap2.tipoAndamento.id IN "
						+ SearchData.inClause(sdProcessoSetor.listaNaoIncluirAndamentos,false));

				if (SearchData.hasDate(sdProcessoSetor.dataNaoIncluirAndamentos)) {
					if (sdProcessoSetor.dataNaoIncluirAndamentos.getInitialDate() != null){
						hql.append(" AND ap2.dataAndamento >= to_date(:dataInicialNaoIncluirAndamentos, 'DD/MM/YYYY HH24:MI:SS') ");
					}
					if (sdProcessoSetor.dataNaoIncluirAndamentos.getFinalDate() != null){
						hql.append(" AND ap2.dataAndamento <= to_date(:dataFinalNaoIncluirAndamentos, 'DD/MM/YYYY HH24:MI:SS') ");
					}
				}

				hql.append(" AND ap2.objetoIncidente = ps.objetoIncidente ) ");
			}

			// início: legislação
			// falta incluir na pesquisa antiga
			if (sdProcessoSetor.normaProcesso != null && sdProcessoSetor.normaProcesso > 0) {
				hql.append(" AND lp.normaProcesso.id = :normaProcesso");
			}

			if (sdProcessoSetor.numeroAno != null && sdProcessoSetor.numeroAno > 0) {
				hql.append(" AND lp.anoLegislacao = :numeroAno");
			}

			if (sdProcessoSetor.numeroLegislacao != null && sdProcessoSetor.numeroLegislacao > 0) {
				hql.append(" AND lp.numeroLegislacao = :numeroLegislacao");
			}

			if (SearchData.stringNotEmpty(sdProcessoSetor.numeroArtigo)) {
				hql.append(" AND lp.artigo = :numeroArtigo");
			}

			if (SearchData.stringNotEmpty(sdProcessoSetor.numeroInciso)) {
				hql.append(" AND lp.inciso = :numeroInciso");
			}

			if (SearchData.stringNotEmpty(sdProcessoSetor.numeroParagrafo)) {
				hql.append(" AND lp.paragrafo = :numeroParagrafo");
			}

			if (SearchData.stringNotEmpty(sdProcessoSetor.numeroAlinea)) {
				hql.append(" AND lp.alinea = :numeroAlinea");
			}

			// Filtro Afastamentos
			if (SearchData.stringNotEmpty(sdProcessoSetor.andamentosProcessuais)) {

				hql.append(" AND EXISTS( SELECT ap1.descricaoObservacaoAndamento "
						+ " FROM AndamentoProcesso ap1"
						+ " WHERE ap1.tipoAndamento.id IN ("
						+ sdProcessoSetor.andamentosProcessuais + ")");

				if (SearchData.hasDate(sdProcessoSetor.dataAndamentos)) {
					if (sdProcessoSetor.dataAndamentos.getInitialDate() != null){
						hql.append(" AND ap1.dataAndamento >= to_date(:dataAndamentosInicial, 'DD/MM/YYYY HH24:MI:SS') ");
					}
					if (sdProcessoSetor.dataAndamentos.getFinalDate() != null){
						hql.append(" AND ap1.dataAndamento <= to_date(:dataAndamentosFinal, 'DD/MM/YYYY HH24:MI:SS') ");
					}
				}
				
				if(sdProcessoSetor.todosAndamentos) {
					hql.append(" AND ap1.numProcesso = ps.numeroProcessual and ap1.sigClasseProces = ps.siglaClasseProcessual ) ");
				}else {
					hql.append(" AND ap1.objetoIncidente = ps.objetoIncidente ) ");	
				}
				
			}
			
			// Filtro Peças Geradas pelo Sistema Digital 
			if (sdProcessoSetor.decisaoDigital != null && sdProcessoSetor.decisaoOutrosSistemas != null && (sdProcessoSetor.decisaoDigital || sdProcessoSetor.decisaoOutrosSistemas)) {
				hql.append(" AND ps.possuiDecisaoDigital in ( ");
				if(sdProcessoSetor.decisaoDigital) {
					hql.append(" 'Sim' ");	
				}
				if(sdProcessoSetor.decisaoDigital && sdProcessoSetor.decisaoOutrosSistemas) {
					hql.append(" , ");	
				}
				if(sdProcessoSetor.decisaoOutrosSistemas) {
					hql.append(" 'Não' ");	
				}
				hql.append(" ) ");
				
			} 
			
			if (sdProcessoSetor.decisaoDigitalTodos != null && sdProcessoSetor.decisaoOutrosSistemas != null && (sdProcessoSetor.decisaoDigitalTodos || sdProcessoSetor.decisaoOutrosSistemas)) {
				hql.append(" AND ps.possuiDecisaoDigitalTodos in ( ");
				if(sdProcessoSetor.decisaoDigitalTodos) {
					hql.append(" 'Sim' ");	
				}
				if(sdProcessoSetor.decisaoDigitalTodos && sdProcessoSetor.decisaoOutrosSistemas) {
					hql.append(" , ");	
				}
				if(sdProcessoSetor.decisaoOutrosSistemas) {
					hql.append(" 'Não' ");	
				}
				hql.append(" ) ");
				
			} 
			
			StringBuffer hqlOrderby = new StringBuffer("");
			if (sdProcessoSetor.tipoOrdem != null
					&& sdProcessoSetor.tipoOrderProcesso != null
					&& ((sdProcessoSetor.idCategoriaPartePesquisa == null || sdProcessoSetor.idCategoriaPartePesquisa <= 0)
					&& !SearchData.stringNotEmpty(sdProcessoSetor.nomeParte)
					&& !SearchData.hasDate(sdProcessoSetor.dataDistribuicaoMinistro)
					&& !SearchData.stringNotEmpty(sdProcessoSetor.codigosAssuntosVirgula) 
					&& !SearchData.stringNotEmpty(sdProcessoSetor.descricaoAssunto))) {
				// esta parte da condição foi colocada para não ordenar os processos, 
				// pois está dando erro de hibernate, no DISTINCT qdo os processos se repetem

				if ((sdProcessoSetor.tipoOrderProcesso != null 
						&& sdProcessoSetor.tipoOrderProcesso.equals(TipoOrdemProcesso.PROCESSO))
						&& (sdProcessoSetor.protocoloNaoAutuado == null || !sdProcessoSetor.protocoloNaoAutuado)) {
					if (sdProcessoSetor.tipoOrdem != null && sdProcessoSetor.tipoOrdem.equals(TipoOrdem.DECRESCENTE)) {
						hqlOrderby.append(" ORDER BY processoSTF.siglaClasseProcessual DESC , processoSTF.numeroProcessual DESC ");
					} else {
						hqlOrderby.append(" ORDER BY processoSTF.siglaClasseProcessual , processoSTF.numeroProcessual ");
					}
				} else if (sdProcessoSetor.tipoOrderProcesso != null && sdProcessoSetor.tipoOrderProcesso.equals(TipoOrdemProcesso.PROTOCOLO)) {
					if (sdProcessoSetor.tipoOrdem != null && sdProcessoSetor.tipoOrdem.equals(TipoOrdem.DECRESCENTE)) {
						hqlOrderby.append(" ORDER BY protocoloSTF.anoProtocolo DESC , protocoloSTF.numeroProtocolo DESC ");
					} else {
						hqlOrderby.append(" ORDER BY protocoloSTF.anoProtocolo , protocoloSTF.numeroProtocolo ");
					}
				} else if (sdProcessoSetor.tipoOrderProcesso != null && sdProcessoSetor.tipoOrderProcesso.equals(TipoOrdemProcesso.VALOR_GUT)) {
					if (sdProcessoSetor.tipoOrdem != null && sdProcessoSetor.tipoOrdem.equals(TipoOrdem.DECRESCENTE)) {
						hqlOrderby.append(" ORDER BY NVL((ps.valorGravidade * ps.valorTendencia * ps.valorUrgencia),0) DESC ");
					} else {
						hqlOrderby.append(" ORDER BY NVL((ps.valorGravidade * ps.valorTendencia * ps.valorUrgencia),0) ");
					}
				} else if (sdProcessoSetor.tipoOrderProcesso != null && sdProcessoSetor.tipoOrderProcesso.equals(TipoOrdemProcesso.DT_ENTRADA)) {
					if (sdProcessoSetor.tipoOrdem != null && sdProcessoSetor.tipoOrdem.equals(TipoOrdem.DECRESCENTE)) {
						hqlOrderby.append(" ORDER BY ps.dataEntrada DESC ");
					} else {
						hqlOrderby.append(" ORDER BY ps.dataEntrada ");
					}
				} else if (sdProcessoSetor.tipoOrderProcesso != null && sdProcessoSetor.tipoOrderProcesso.equals(TipoOrdemProcesso.ASSUNTO)) {
					if (sdProcessoSetor.tipoOrdem != null && sdProcessoSetor.tipoOrdem.equals(TipoOrdem.DECRESCENTE)) {
						hqlOrderby.append(" ORDER BY ps.siglaClasseProcessual DESC ");
					} else {
						hqlOrderby.append(" ORDER BY  ps.siglaClasseProcessual ASC");

					}					
				} else if (sdProcessoSetor.tipoOrderProcesso != null && sdProcessoSetor.tipoOrderProcesso.equals(TipoOrdemProcesso.ORIGEM)) {
					if (sdProcessoSetor.tipoOrdem != null && sdProcessoSetor.tipoOrdem.equals(TipoOrdem.DECRESCENTE)) {					
						hqlOrderby.append(" ORDER BY nvl(ps.descricaoOrigemAtual,' ') DESC ");
					} else {
						hqlOrderby.append(" ORDER BY ps.descricaoOrigemAtual ");
					}
				} else if (TipoOrdemProcesso.TEMA.equals(sdProcessoSetor.tipoOrderProcesso)) {
					hqlOrderby.append(" ORDER BY tema.numeroSequenciaTema ");
					
					if (TipoOrdem.DECRESCENTE.equals(sdProcessoSetor.tipoOrdem))
						hqlOrderby.append(" DESC ");
					
					hqlOrderby.append(" NULLS LAST ");
				} else if (TipoOrdemProcesso.MOTIVO_INAPTIDAO.equals(sdProcessoSetor.tipoOrderProcesso)) {
					hqlOrderby.append(" ORDER BY mi ");
					
					if (TipoOrdem.DECRESCENTE.equals(sdProcessoSetor.tipoOrdem))
						hqlOrderby.append(" DESC ");
					
					hqlOrderby.append(" NULLS LAST ");
				}
			} else{
				hqlOrderby.append(" ORDER BY 1,3 ");	
			}
			
			Session session = retrieveSession();
			Query countQuery = session.createQuery(hqlQtd.toString() + " " + hql.toString());
			Query collectionQuery = session.createQuery(hqlSql.toString() 	+ hql.toString() + " " + hqlOrderby.toString());

			if(sdProcessoSetor.origem != null && sdProcessoSetor.origem > 0 ){
				countQuery.setLong("origem", sdProcessoSetor.origem);
				collectionQuery.setLong("origem", sdProcessoSetor.origem);
			}
			
			if (sdProcessoSetor.protocoloNaoAutuado == null || !sdProcessoSetor.protocoloNaoAutuado) {
				if (SearchData.stringNotEmpty(sdProcessoSetor.sigla)) {
					countQuery.setString("sigla", sdProcessoSetor.sigla);
					collectionQuery.setString("sigla", sdProcessoSetor.sigla);
				}

				if (sdProcessoSetor.numeroProcesso != null) {
					countQuery.setLong("numeroProcesso", sdProcessoSetor.numeroProcesso);
					collectionQuery.setLong("numeroProcesso", sdProcessoSetor.numeroProcesso);
				}

				if (sdProcessoSetor.recurso != null && sdProcessoSetor.recurso > 0) {
					countQuery.setShort("recurso", sdProcessoSetor.recurso);
					collectionQuery.setShort("recurso", sdProcessoSetor.recurso);
				}

				if (SearchData.stringNotEmpty(sdProcessoSetor.siglaRecursoUnificada) && !sdProcessoSetor.siglaRecursoUnificada.equals("SEM RECURSO")) {
					countQuery.setString("siglaRecursoUnificada", "%" + sdProcessoSetor.siglaRecursoUnificada + "%");
					collectionQuery.setString("siglaRecursoUnificada", "%" + sdProcessoSetor.siglaRecursoUnificada + "%");
				}

				if (SearchData.stringNotEmpty(sdProcessoSetor.siglaTipoJulgamento) && !sdProcessoSetor.siglaTipoJulgamento.equals(ProcessoSetor.TipoIncidenteJulgamentoConstante.MERITO.getSigla())) {
					countQuery.setString("siglaTipoJulgamento",	sdProcessoSetor.siglaTipoJulgamento);
					collectionQuery.setString("siglaTipoJulgamento", sdProcessoSetor.siglaTipoJulgamento);
				}

				if (sdProcessoSetor.idTipoTarefa != null) {
					countQuery.setLong("idTipoTarefa", sdProcessoSetor.idTipoTarefa);
					collectionQuery.setLong("idTipoTarefa", sdProcessoSetor.idTipoTarefa);
				}

				if (sdProcessoSetor.codigoMinistroRelator != null && sdProcessoSetor.codigoMinistroRelator > 0) {
					countQuery.setLong("codigoMinistroRelator",	sdProcessoSetor.codigoMinistroRelator);
					collectionQuery.setLong("codigoMinistroRelator", sdProcessoSetor.codigoMinistroRelator);
				}

			}

			if (sdProcessoSetor.anoProtocolo != null && sdProcessoSetor.anoProtocolo > 0) {
				collectionQuery.setShort("anoProtocolo", sdProcessoSetor.anoProtocolo);
				countQuery.setShort("anoProtocolo", sdProcessoSetor.anoProtocolo);
			}

			if (sdProcessoSetor.numeroProtocolo != null && sdProcessoSetor.numeroProtocolo > 0) {
				countQuery.setLong("numeroProtocolo",sdProcessoSetor.numeroProtocolo);
				collectionQuery.setLong("numeroProtocolo",sdProcessoSetor.numeroProtocolo);
			}
			
			if (sdProcessoSetor.motivoInaptidao != null && sdProcessoSetor.motivoInaptidao.size() > 0) {
				countQuery.setParameterList("motivoInaptidao", sdProcessoSetor.motivoInaptidao);
				collectionQuery.setParameterList("motivoInaptidao", sdProcessoSetor.motivoInaptidao);
			}

			if (SearchData.stringNotEmpty(sdProcessoSetor.codigoTipoMeioProcesso)) {
				countQuery.setString("codigoTipoMeioProcesso", sdProcessoSetor.codigoTipoMeioProcesso);
				collectionQuery.setString("codigoTipoMeioProcesso", sdProcessoSetor.codigoTipoMeioProcesso);
			}

			if (SearchData.stringNotEmpty(sdProcessoSetor.siglaUsuarioDistribuicao)
					&& (sdProcessoSetor.semDistribuicao == null || !sdProcessoSetor.semDistribuicao)) {
				countQuery.setString("siglaUsuarioDistribuicao", sdProcessoSetor.siglaUsuarioDistribuicao);
				collectionQuery.setString("siglaUsuarioDistribuicao", sdProcessoSetor.siglaUsuarioDistribuicao);
			}

			if (sdProcessoSetor.idSetor != null) {
				countQuery.setLong("idSetor", sdProcessoSetor.idSetor);
				collectionQuery.setLong("idSetor", sdProcessoSetor.idSetor);
			}

			if (SearchData.stringNotEmpty(sdProcessoSetor.nomeParte)) {
				sdProcessoSetor.nomeParte = "%" + sdProcessoSetor.nomeParte.replace(' ', '%') + "%";
				countQuery.setString("nomeParte", sdProcessoSetor.nomeParte);
				collectionQuery.setString("nomeParte", sdProcessoSetor.nomeParte);
			}

			if (SearchData.hasDate(sdProcessoSetor.dataDistribuicao)) {
				if (sdProcessoSetor.dataDistribuicao.getInitialDate() != null){
					countQuery.setString("dataDistribuicaoInicial",	sdProcessoSetor.dataDistribuicao.getDateHourInitialString());
				}
				if (sdProcessoSetor.dataDistribuicao.getFinalDate() != null){
					countQuery.setString("dataDistribuicaoFinal", sdProcessoSetor.dataDistribuicao.getDateHourFinalString());
				}
				if (sdProcessoSetor.dataDistribuicao.getInitialDate() != null){
					collectionQuery.setString("dataDistribuicaoInicial", sdProcessoSetor.dataDistribuicao.getDateHourInitialString());
				}
				if (sdProcessoSetor.dataDistribuicao.getFinalDate() != null){
					collectionQuery.setString("dataDistribuicaoFinal", sdProcessoSetor.dataDistribuicao.getDateHourFinalString());
				}
			}

			if (SearchData.hasDate(sdProcessoSetor.dataDistribuicaoMinistro)) {
				if (sdProcessoSetor.dataDistribuicaoMinistro.getInitialDate() != null){
					countQuery.setString("dataDistribuicaoMinistroInicial", sdProcessoSetor.dataDistribuicaoMinistro.getDateHourInitialString());
				}
				if (sdProcessoSetor.dataDistribuicaoMinistro.getFinalDate() != null){
					countQuery.setString("dataDistribuicaoMinistroFinal", sdProcessoSetor.dataDistribuicaoMinistro.getDateHourFinalString());
				}
				if (sdProcessoSetor.dataDistribuicaoMinistro.getInitialDate() != null){
					collectionQuery.setString("dataDistribuicaoMinistroInicial", sdProcessoSetor.dataDistribuicaoMinistro.getDateHourInitialString());
				}
				if (sdProcessoSetor.dataDistribuicaoMinistro.getFinalDate() != null){
					collectionQuery.setString("dataDistribuicaoMinistroFinal", sdProcessoSetor.dataDistribuicaoMinistro.getDateHourFinalString());
				}
			}

			if (SearchData.hasDate(sdProcessoSetor.dataFase)) {
				if (sdProcessoSetor.dataFase.getInitialDate() != null){
					countQuery.setString("dataFaseInicial", sdProcessoSetor.dataFase.getDateHourInitialString());
				}
				if (sdProcessoSetor.dataFase.getFinalDate() != null){
					countQuery.setString("dataFaseFinal", sdProcessoSetor.dataFase.getDateHourFinalString());
				}
				if (sdProcessoSetor.dataFase.getInitialDate() != null){
					collectionQuery.setString("dataFaseInicial", sdProcessoSetor.dataFase.getDateHourInitialString());
				}
				if (sdProcessoSetor.dataFase.getFinalDate() != null){
					collectionQuery.setString("dataFaseFinal", sdProcessoSetor.dataFase.getDateHourFinalString());
				}
			}

			if (SearchData.hasDate(sdProcessoSetor.dataRecebimento) && (sdProcessoSetor.semLocalizacao == null || !sdProcessoSetor.semLocalizacao)) {
				if (sdProcessoSetor.dataRecebimento.getInitialDate() != null){
					countQuery.setString("dataRecebimentoInicial", sdProcessoSetor.dataRecebimento.getDateHourInitialString());
				}
				if (sdProcessoSetor.dataRecebimento.getFinalDate() != null){
					countQuery.setString("dataRecebimentoFinal", sdProcessoSetor.dataRecebimento.getDateHourFinalString());
				}
				if (sdProcessoSetor.dataRecebimento.getInitialDate() != null){
					collectionQuery.setString("dataRecebimentoInicial", sdProcessoSetor.dataRecebimento.getDateHourInitialString());
				}
				if (sdProcessoSetor.dataRecebimento.getFinalDate() != null){
					collectionQuery.setString("dataRecebimentoFinal", sdProcessoSetor.dataRecebimento.getDateHourFinalString());
				}
			}

			if (SearchData.hasDate(sdProcessoSetor.dataRemessa)	&& (sdProcessoSetor.semLocalizacao == null || !sdProcessoSetor.semLocalizacao)) {
				if (sdProcessoSetor.dataRemessa.getInitialDate() != null){
					countQuery.setString("dataRemessaInicial", sdProcessoSetor.dataRemessa.getDateHourInitialString());
				}
				if (sdProcessoSetor.dataRemessa.getFinalDate() != null){
					countQuery.setString("dataRemessaFinal", sdProcessoSetor.dataRemessa.getDateHourFinalString());
				}
				if (sdProcessoSetor.dataRemessa.getInitialDate() != null){
					collectionQuery.setString("dataRemessaInicial", sdProcessoSetor.dataRemessa.getDateHourInitialString());
				}
				if (sdProcessoSetor.dataRemessa.getFinalDate() != null){
					collectionQuery.setString("dataRemessaFinal", sdProcessoSetor.dataRemessa.getDateHourFinalString());
				}
			}

			if (SearchData.hasDate(sdProcessoSetor.dataEntrada)) {
				if (sdProcessoSetor.dataEntrada.getInitialDate() != null){
					countQuery.setString("dataEntradaInicial", sdProcessoSetor.dataEntrada.getDateHourInitialString());
				}
				if (sdProcessoSetor.dataEntrada.getFinalDate() != null){
					countQuery.setString("dataEntradaFinal", sdProcessoSetor.dataEntrada.getDateHourFinalString());
				}
				if (sdProcessoSetor.dataEntrada.getInitialDate() != null){
					collectionQuery.setString("dataEntradaInicial", sdProcessoSetor.dataEntrada.getDateHourInitialString());
				}
				if (sdProcessoSetor.dataEntrada.getFinalDate() != null){
					collectionQuery.setString("dataEntradaFinal", sdProcessoSetor.dataEntrada.getDateHourFinalString());
				}
			}

			if (SearchData.hasDate(sdProcessoSetor.dataSaida)) {
				if (sdProcessoSetor.dataSaida.getInitialDate() != null){
					countQuery.setString("dataSaidaInicial", sdProcessoSetor.dataSaida.getDateHourInitialString());
				}
				if (sdProcessoSetor.dataSaida.getFinalDate() != null){
					countQuery.setString("dataSaidaFinal", sdProcessoSetor.dataSaida.getDateHourFinalString());
				}
				if (sdProcessoSetor.dataSaida.getInitialDate() != null){
					collectionQuery.setString("dataSaidaInicial", sdProcessoSetor.dataSaida.getDateHourInitialString());
				}
				if (sdProcessoSetor.dataSaida.getFinalDate() != null){
					collectionQuery.setString("dataSaidaFinal",	sdProcessoSetor.dataSaida.getDateHourFinalString());
				}
			}

			if (sdProcessoSetor.idCategoriaPartePesquisa != null && sdProcessoSetor.idCategoriaPartePesquisa > 0) {
				countQuery.setLong("idCategoriaPartePesquisa", sdProcessoSetor.idCategoriaPartePesquisa);
				collectionQuery.setLong("idCategoriaPartePesquisa", sdProcessoSetor.idCategoriaPartePesquisa);
			}

			if (sdProcessoSetor.idGrupoProcessoSetor != null) {
				countQuery.setLong("idGrupoProcessoSetor",sdProcessoSetor.idGrupoProcessoSetor);
				collectionQuery.setLong("idGrupoProcessoSetor",sdProcessoSetor.idGrupoProcessoSetor);

			}

			if (sdProcessoSetor.idTipoUltimaFaseSetor != null && (sdProcessoSetor.semFase == null || !sdProcessoSetor.semFase)) {
				countQuery.setLong("idTipoUltimaFaseSetor", sdProcessoSetor.idTipoUltimaFaseSetor);
				collectionQuery.setLong("idTipoUltimaFaseSetor", sdProcessoSetor.idTipoUltimaFaseSetor);

			}

			if (sdProcessoSetor.idTipoUltimoStatusSetor != null && (sdProcessoSetor.semFase == null || !sdProcessoSetor.semFase)) {
				countQuery.setLong("idTipoUltimoStatusSetor", sdProcessoSetor.idTipoUltimoStatusSetor);
				collectionQuery.setLong("idTipoUltimoStatusSetor", sdProcessoSetor.idTipoUltimoStatusSetor);
			}

			if (sdProcessoSetor.idSecaoUltimoDeslocamento != null) {
				countQuery.setLong("idSecaoUltimoDeslocamento", sdProcessoSetor.idSecaoUltimoDeslocamento);
				collectionQuery.setLong("idSecaoUltimoDeslocamento", sdProcessoSetor.idSecaoUltimoDeslocamento);

			}

			if (SearchData.stringNotEmpty(sdProcessoSetor.complementoAssunto)) {
				sdProcessoSetor.complementoAssunto = "%" + sdProcessoSetor.complementoAssunto.replace(' ', '%') + "%";
				countQuery.setString("complementoAssunto", sdProcessoSetor.complementoAssunto);
				collectionQuery.setString("complementoAssunto", sdProcessoSetor.complementoAssunto);
			}

			// início: legislação
			if (sdProcessoSetor.normaProcesso != null && sdProcessoSetor.normaProcesso > 0) {
				countQuery.setLong("normaProcesso", sdProcessoSetor.normaProcesso);
				collectionQuery.setLong("normaProcesso", sdProcessoSetor.normaProcesso);
			}

			if (sdProcessoSetor.numeroAno != null && sdProcessoSetor.numeroAno > 0) {
				countQuery.setShort("numeroAno", sdProcessoSetor.numeroAno);
				collectionQuery.setShort("numeroAno", sdProcessoSetor.numeroAno);
			}

			if (sdProcessoSetor.numeroLegislacao != null && sdProcessoSetor.numeroLegislacao > 0) {
				countQuery.setLong("numeroLegislacao", sdProcessoSetor.numeroLegislacao);
				collectionQuery.setLong("numeroLegislacao", sdProcessoSetor.numeroLegislacao);
			}

			if (SearchData.stringNotEmpty(sdProcessoSetor.numeroArtigo)) {
				countQuery.setString("numeroArtigo", sdProcessoSetor.numeroArtigo);
				collectionQuery.setString("numeroArtigo", sdProcessoSetor.numeroArtigo);
			}

			if (SearchData.stringNotEmpty(sdProcessoSetor.numeroInciso)) {
				countQuery.setString("numeroInciso", sdProcessoSetor.numeroInciso);
				collectionQuery.setString("numeroInciso", sdProcessoSetor.numeroInciso);
			}

			if (SearchData.stringNotEmpty(sdProcessoSetor.numeroParagrafo)) {
				countQuery.setString("numeroParagrafo", sdProcessoSetor.numeroParagrafo);
				collectionQuery.setString("numeroParagrafo", sdProcessoSetor.numeroParagrafo);
			}

			if (SearchData.stringNotEmpty(sdProcessoSetor.numeroAlinea)) {
				countQuery.setString("numeroAlinea", sdProcessoSetor.numeroAlinea);
				collectionQuery.setString("numeroAlinea", sdProcessoSetor.numeroAlinea);
			}
			// fim: legislação

			// Inicio Filtro Andamentos
			if (SearchData.hasDate(sdProcessoSetor.dataAndamentos)) {
				if (sdProcessoSetor.dataAndamentos.getInitialDate() != null){
					countQuery.setString("dataAndamentosInicial", sdProcessoSetor.dataAndamentos.getDateHourInitialString());
				}
				if (sdProcessoSetor.dataAndamentos.getFinalDate() != null){
					countQuery.setString("dataAndamentosFinal", sdProcessoSetor.dataAndamentos.getDateHourFinalString());
				}
				if (sdProcessoSetor.dataAndamentos.getInitialDate() != null){
					collectionQuery.setString("dataAndamentosInicial", sdProcessoSetor.dataAndamentos.getDateHourInitialString());
				}
				if (sdProcessoSetor.dataAndamentos.getFinalDate() != null){
					collectionQuery.setString("dataAndamentosFinal", sdProcessoSetor.dataAndamentos.getDateHourFinalString());
				}
			}

			Long totalSize = 0L;
			if (sdProcessoSetor.getTotalResult() != null && sdProcessoSetor.getTotalResult() > 0L) {
				totalSize = sdProcessoSetor.getTotalResult();
			} else {
				totalSize = (Long) countQuery.uniqueResult();
			}

			List<ProcessoSetor> result = null;

			if (totalSize > 0 && (sdProcessoSetor.getHasOnlyCount() == null || !sdProcessoSetor.getHasOnlyCount())) {
				if (totalSize > sdProcessoSetor.getPageData().getPageMaxResult()
						&& sdProcessoSetor.isPaging()
						&& (sdProcessoSetor.getReturnAllValues() == null || !sdProcessoSetor.getReturnAllValues().booleanValue())) {
					collectionQuery.setMaxResults(sdProcessoSetor.getPageData().getPageMaxResult());
					collectionQuery.setFirstResult(sdProcessoSetor.getPageData().getFirstResult());
				}

				result = collectionQuery.list();
			}
			return new SearchResult(sdProcessoSetor, totalSize, result);
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	public ProcessoSetor recuperarProcessoSetor(Long seqObjetoIncidente,
			Long idSetor) throws DaoException {
		Session session = retrieveSession();
		ProcessoSetor processoSetor = null;
		try {
			Criteria c = session.createCriteria(ProcessoSetor.class, "ps");
			c.add(Restrictions.eq("ps.objetoIncidente.id", seqObjetoIncidente));
			c.add(Restrictions.eq("ps.setor.id", idSetor));

			processoSetor = (ProcessoSetor) c.uniqueResult();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return processoSetor;
	}
	
	public List<ProcessoSetor> recuperarListaProcessoSetor(Long seqObjetoIncidente,
			Long idSetor) throws DaoException {
		Session session = retrieveSession();
		List<ProcessoSetor> processoSetor = null;
		try {
			Criteria c = session.createCriteria(ProcessoSetor.class, "ps");
			c.add(Restrictions.eq("ps.objetoIncidente.id", seqObjetoIncidente));
			c.add(Restrictions.eq("ps.setor.id", idSetor));

			processoSetor = c.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return processoSetor;
	}

	public List<String> pesquisarComplementoAssunto(String complementoAssunto,
			Long idSetor, Short numeroMaximoDeResultados) throws DaoException {
		Session session = retrieveSession();
		List<String> res = null;
		try {
			Criteria c = session.createCriteria(ProcessoSetor.class, "ps");
			c.setProjection(Projections.property("ps.complementoAssunto"));

			if (complementoAssunto != null
					&& SearchData.stringNotEmpty(complementoAssunto)) {
				complementoAssunto = complementoAssunto.replace('%', ' ');
				c.add(Restrictions.ilike("ps.complementoAssunto",
						complementoAssunto.trim(), MatchMode.ANYWHERE));
			}

			if (idSetor != null && idSetor > 0)
				c.add(Restrictions.eq("ps.setor.id", idSetor));

			c.addOrder(Order.asc("ps.complementoAssunto"));

			if (numeroMaximoDeResultados != null
					&& numeroMaximoDeResultados > 0)
				c.setMaxResults(numeroMaximoDeResultados);

			res = c.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return res;
	}

	public Integer pesquisarQuantidadeComplementoAssunto(
			String complementoAssunto, Long idSetor) throws DaoException {
		Session session = retrieveSession();
		Integer res = null;
		try {
			Criteria c = session.createCriteria(ProcessoSetor.class, "ps");
			c.setProjection(Projections.rowCount());

			if (complementoAssunto != null
					&& SearchData.stringNotEmpty(complementoAssunto)) {
				complementoAssunto = complementoAssunto.replace('%', ' ');
				c.add(Restrictions.ilike("ps.complementoAssunto",
						complementoAssunto.trim(), MatchMode.ANYWHERE));
			}

			if (idSetor != null && idSetor > 0)
				c.add(Restrictions.eq("ps.setor.id", idSetor));

			res = (Integer) c.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return res;
	}

	public Integer pesquisarQuantidadeProcessoSetorEletronico(
			ProcessoSetorEletronicoSearchData sd) throws DaoException {
		Session sessao = retrieveSession();

		Statement stmt = null;
		ResultSet rs = null;

		try {
			StringBuffer sql = new StringBuffer("select count(*) as qtd "
					+ " from egab.processo_setor ps,"
					+ "     judiciario.processo p ");

			if (sd.tipoQTD.equals(TipoQTD.QTD_PROCESSO)) {
				sql.append("where ps.seq_objeto_incidente = p.seq_objeto_incidente ");
			} else if (sd.tipoQTD.equals(TipoQTD.QTD_MC)) {
				sql.append(",judiciario.incidente_julgamento ij, "
						+ "judiciario.objeto_incidente oi "
						+ " where ps.seq_objeto_incidente = ij.seq_objeto_incidente "
						+ "  and ij.seq_objeto_incidente = oi.seq_objeto_incidente "
						+ "  and oi.seq_objeto_incidente_principal = p.seq_objeto_incidente");

			} else if (sd.tipoQTD.equals(TipoQTD.QTD_RECURSO)) {
				sql.append(",judiciario.recurso_processo rp, "
						+ "judiciario.objeto_incidente oi "
						+ " where ps.seq_objeto_incidente = rp.seq_objeto_incidente "
						+ " and rp.seq_objeto_incidente = oi.seq_objeto_incidente "
						+ " and oi.seq_objeto_incidente_principal = p.seq_objeto_incidente ");

			} else if (sd.tipoQTD.equals(TipoQTD.QTD_SEM_RESPONSAVEL)) {
				sql.append(",judiciario.objeto_incidente oi "
						+ " where ps.seq_objeto_incidente = oi.seq_objeto_incidente "
						+ "  and oi.seq_objeto_incidente_principal = p.seq_objeto_incidente "
						+ "  and ps.seq_distribuicao_atual is null");

			} else if (sd.tipoQTD.equals(TipoQTD.QTD_SEM_FASE)) {
				sql.append(",judiciario.objeto_incidente oi "
						+ " where ps.seq_objeto_incidente = oi.seq_objeto_incidente "
						+ "  and oi.seq_objeto_incidente_principal = p.seq_objeto_incidente "
						+ "  and p.cod_situacao not in ('J', 'W', 'K') "
						+ "  and ps.seq_fase_atual is null");

			} else if (sd.tipoQTD.equals(TipoQTD.QTD_INCIDENTE_USUARIO)) {
				sql.append(",egab.historico_distribuicao hd, "
						+ " judiciario.objeto_incidente oi "
						+ " where ps.seq_objeto_incidente = oi.seq_objeto_incidente "
						+ "  and oi.seq_objeto_incidente_principal = p.seq_objeto_incidente "
						+ "  and hd.seq_historico_distribuicao = ps.seq_distribuicao_atual "
						+ "  and hd.sig_usuario_analise = '"
						+ sd.idUsuarioAutenticado + "'");
			} else if (sd.tipoQTD.equals(TipoQTD.QTD_PROCESSO_INATIVO)) {
				sql.append(",judiciario.objeto_incidente oi "
						+ " where ps.seq_objeto_incidente = oi.seq_objeto_incidente "
						+ "  and oi.seq_objeto_incidente_principal = p.seq_objeto_incidente "
						+ "  and p.cod_situacao in ('J', 'W', 'K') ");
			}
			
			sql.append(" AND CASE WHEN ((select oi.tip_objeto_incidente from judiciario.objeto_incidente oi where oi.seq_objeto_incidente = ps.seq_objeto_incidente) = 'RC') THEN (select COUNT(rp1.seq_objeto_incidente) from judiciario.recurso_processo rp1 where rp1.seq_objeto_incidente = ps.seq_objeto_incidente AND rp1.flg_ativo = 'S') ");
			sql.append(" WHEN ((select oi.tip_objeto_incidente from judiciario.objeto_incidente oi where oi.seq_objeto_incidente = ps.seq_objeto_incidente) = 'IJ') THEN (select COUNT(ij1.seq_objeto_incidente) from judiciario.incidente_julgamento ij1 where ij1.seq_objeto_incidente = ps.seq_objeto_incidente AND ij1.flg_ativo = 'S') ");
			sql.append(" WHEN ((select oi.tip_objeto_incidente from judiciario.objeto_incidente oi where oi.seq_objeto_incidente = ps.seq_objeto_incidente) = 'PR') THEN 1 ELSE 0 END = 1 ");

			
			sql.append("  and p.tip_meio_processo = 'E' "
					+ "  and ps.cod_setor = " + sd.codSetor
					+ "  and ps.dat_saida_setor is null");

			stmt = sessao.connection().createStatement();
			stmt.executeQuery(sql.toString());
			rs = stmt.getResultSet();

			if (rs.next()) {
				return rs.getInt("qtd");
			}

		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new DaoException("Erro ao executar o comando SQL", e);
			}

		}

		return 0;
	}

	public SearchResult<ProcessoSetor> pesquisarProcessoSetorEletronico(
			ProcessoSetorEletronicoSearchData sd) throws DaoException {
		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer("select ps "
					+ " from ProcessoSetor ps "
					+ " join ps.objetoIncidente oi," + " Processo p ");

			if (sd.tipoQTD.equals(TipoQTD.QTD_PROCESSO)) {
				hql.append(" where p.id = oi.principal.id "
						+ " and oi.id = p.id");
			} else if (sd.tipoQTD.equals(TipoQTD.QTD_MC)) {
				hql.append(",IncidenteJulgamento ij "
						+ " where p.id = oi.principal.id"
						+ " and oi.id = ij.id ");

			} else if (sd.tipoQTD.equals(TipoQTD.QTD_RECURSO)) {
				hql.append(",RecursoProcesso rc "
						+ " where p.id = oi.principal.id"
						+ " and oi.id = rc.id ");

			} else if (sd.tipoQTD.equals(TipoQTD.QTD_SEM_RESPONSAVEL)) {
				hql.append(" where p.id = oi.principal.id "
						+ " and ps.distribuicaoAtual.id is null");

			} else if (sd.tipoQTD.equals(TipoQTD.QTD_SEM_FASE)) {
				hql.append(" where p.id = oi.principal.id "
						+ " and p.situacao not in ('J', 'W', 'K') "
						+ " and ps.faseAtual.id is null");

			} else if (sd.tipoQTD.equals(TipoQTD.QTD_INCIDENTE_USUARIO)) {
				hql.append(" where p.id = oi.principal.id "
						+ " and ps.distribuicaoAtual.usuario.id = :idUsuarioAutenticado");
				
			} 
			
			else if (sd.tipoQTD.equals(TipoQTD.QTD_PROCESSO_INATIVO)) {
				hql.append(" where p.id = oi.principal.id "); 
				hql.append("  and p.situacao in ('J', 'W', 'K') ");
			}
			
			else if (sd.tipoQTD.equals(TipoQTD.QTD_TOTAL)) {
				hql.append(" where p.id = oi.principal.id ");
			}
			hql.append("  and p.tipoMeioProcesso = :tipoMeioProcesso "
					+ "  and ps.setor.id = :codSetor"
					+ "  and ps.dataSaida is null");

			hql.append(" AND CASE WHEN (ps.objetoIncidente.tipoObjetoIncidente = 'RC') THEN (select COUNT(rp1) from RecursoProcesso rp1 where rp1 = ps.objetoIncidente.id AND rp1.ativo = 'S') ");
			hql.append(" WHEN (ps.objetoIncidente.tipoObjetoIncidente = 'IJ') THEN (select COUNT(ij1) from IncidenteJulgamento ij1 where ij1.id = ps.objetoIncidente.id AND ij1.ativo = 'S') ");
			hql.append(" WHEN (ps.objetoIncidente.tipoObjetoIncidente = 'PR') THEN 1 ELSE 0 END = 1 ");
		
			
			if (sd.tipoOrdem != null) {
				String ordem = sd.crescente ? "ASC" : "DESC";
				if (sd.tipoOrdem
						.equals(ProcessoSetorEletronicoSearchData.TipoOrdem.ID_PROCESSO)) {
					hql.append(" ORDER BY p.siglaClasseProcessual " + ordem
							+ " , p.numeroProcessual " + ordem);
				} else if (sd.tipoOrdem
						.equals(ProcessoSetorEletronicoSearchData.TipoOrdem.DT_ENTRADA)) {
					hql.append(" ORDER BY ps.dataEntrada " + ordem);
				}
			}

			Query collectionQuery = session.createQuery(hql.toString());

			collectionQuery.setLong("codSetor", sd.codSetor);
			collectionQuery.setString("tipoMeioProcesso",
					TipoMeioProcesso.ELETRONICO.getCodigo());

			if (sd.tipoQTD.equals(TipoQTD.QTD_INCIDENTE_USUARIO)) {
				collectionQuery.setString("idUsuarioAutenticado",
						sd.idUsuarioAutenticado);
			}

			List<Processo> result = null;

			if (sd.quantidade > 0) {

				if (sd.quantidade > sd.getPageData().getPageMaxResult()
						&& sd.isPaging()) {
					collectionQuery.setMaxResults(sd.getPageData()
							.getPageMaxResult());
					collectionQuery.setFirstResult(sd.getPageData()
							.getFirstResult());
				}

				result = collectionQuery.list();
			}

			Long qtd = sd.quantidade != null ? new Long(sd.quantidade) : null;

			return new SearchResult(sd, qtd, result);
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
	}

	public List<ProcessoSetor> pesquisarProcessoSetor(Short anoProtocolo,
			Long numeroProtocolo, String siglasClassesProcessuaisAgrupadas, String sigla, Long numeroProcesso,
			Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada,
			String siglaTipoJulgamento, String codigoTipoMeioProcesso,
			Long numeroPeticao, String codigoAssunto, String descricaoAssunto,
			String complementoAssunto, Long codigoMinistroRelator,
			String numeroSala, String numeroArmario, String numeroEstante,
			String numeroPrateleira, String numeroColuna,
			String obsDeslocamento,
			Boolean pesquisarAssuntoEmTodosNiveis, Boolean pesquisarInicio,
			Long idSecaoUltimoDeslocamento, String siglaUsuarioDistribuicao,
			Long idGrupoProcessoSetor, 
			Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal,
			Date dataDistribuicaoInicial,
			Date dataDistribuicaoFinal, Date dataFaseInicial,
			Date dataFaseFinal, Date dataRemessaInicial, Date dataRemessaFinal,
			Date dataRecebimentoInicial, Date dataRecebimentoFinal,
			Date dataEntradaInicial, Date dataEntradaFinal, Date dataSaidaInicial, Date dataSaidaFinal, Long idSetor,
			Long idTipoUltimaFaseSetor, Long idTipoUltimoStatusSetor,
			Boolean faseAtualProcesso, Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox,
			Boolean semLocalizacao, Boolean semFase, Boolean semDistribuicao,
			Boolean semVista, 
            Long idCategoriaPartePesquisa,
            String nomeParte,
			Long idTipoTarefa, Boolean localizadosNoSetor,
			Boolean emTramiteNoSetor, Boolean possuiLiminar,
			Boolean possuiPreferencia, Boolean sobrestado, Boolean julgado,
			Boolean preFetchAssunto, Boolean readOnlyQuery,
			Boolean limitarPesquisa, Boolean orderByProcesso,
			Boolean orderByProtocolo, Boolean orderByValorGut,
			Boolean orderByDataEntrada, Boolean orderByAssunto,
			Boolean orderByCrescente,
			List<Andamento> listaIncluirAndamentos,
			Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos,
			List<Andamento> listaNaoIncluirAndamentos,
			Date dataInicialNaoIncluirAndamentos,
			Date dataFinalNaoIncluirAndamentos) throws DaoException {

		List<ProcessoSetor> result = null;

		try {

			Session session = retrieveSession();
			Query query = createProcessoSetorQuery(anoProtocolo,
					numeroProtocolo, siglasClassesProcessuaisAgrupadas, sigla, numeroProcesso, recurso, possuiRecurso,
					siglaRecursoUnificada, siglaTipoJulgamento,
					codigoTipoMeioProcesso, numeroPeticao, codigoAssunto,
					descricaoAssunto, complementoAssunto,
					codigoMinistroRelator, numeroSala, numeroArmario,
					numeroEstante, numeroPrateleira, numeroColuna,
					obsDeslocamento,
					pesquisarAssuntoEmTodosNiveis, pesquisarInicio,
					idSecaoUltimoDeslocamento, siglaUsuarioDistribuicao,
					idGrupoProcessoSetor, 
					dataDistribuicaoMinistroInicial, dataDistribuicaoMinistroFinal,
					dataDistribuicaoInicial,
					dataDistribuicaoFinal, dataFaseInicial, dataFaseFinal,
					dataRemessaInicial, dataRemessaFinal,
					dataRecebimentoInicial, dataRecebimentoFinal,
					dataEntradaInicial, dataEntradaFinal, dataSaidaInicial, dataSaidaFinal, idSetor,
					idTipoUltimaFaseSetor, idTipoUltimoStatusSetor,
					faseAtualProcesso, repercussaoGeralCheckbox, protocoloNaoAutuadoCheckbox,
					semLocalizacao, semFase, semDistribuicao, semVista,
					idCategoriaPartePesquisa, nomeParte,
					idTipoTarefa, localizadosNoSetor, emTramiteNoSetor,
					possuiLiminar, possuiPreferencia, sobrestado, julgado,
					preFetchAssunto, readOnlyQuery, false, false, false, false,
					false, false, orderByProcesso, orderByProtocolo,
					orderByValorGut, orderByDataEntrada, orderByAssunto,
					orderByCrescente, listaIncluirAndamentos,
					dataInicialIncluirAndamentos, dataFinalIncluirAndamentos,
					listaNaoIncluirAndamentos, dataInicialNaoIncluirAndamentos,
					dataFinalNaoIncluirAndamentos);

			if (limitarPesquisa != null && limitarPesquisa.booleanValue()) {
				if (FWConfig.getInstance().getMaxQueryResult() > 0)
					query.setMaxResults(FWConfig.getInstance()
							.getMaxQueryResult());

			}

			result = query.list();
			
			/*
			 * if(limitarPesquisa==null || !limitarPesquisa.booleanValue()){
			 * session.clear(); }
			 */
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		} catch (DaoException e) {
			throw new DaoException("RuntimeException", e);
		}

		return result;
	}
	

}
