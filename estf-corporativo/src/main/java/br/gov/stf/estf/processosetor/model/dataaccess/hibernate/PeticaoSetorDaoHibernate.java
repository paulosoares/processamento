package br.gov.stf.estf.processosetor.model.dataaccess.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processosetor.PeticaoSetor;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.model.util.TipoOrdem;
import br.gov.stf.estf.model.util.TipoOrdemProcesso;
import br.gov.stf.estf.processosetor.model.dataaccess.PeticaoSetorDao;
import br.gov.stf.estf.processosetor.model.util.PeticaoSetorSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.DateTimeHelper;
import br.gov.stf.framework.util.FWConfig;

@Repository
public class PeticaoSetorDaoHibernate extends GenericHibernateDao<PeticaoSetor, Long> implements PeticaoSetorDao {

	private static final long serialVersionUID = 7178146921683645756L;

	public PeticaoSetorDaoHibernate() {
		super(PeticaoSetor.class);
	}

	public PeticaoSetor recuperarPeticaoSetor(Long id) throws DaoException {

		PeticaoSetor peticaoSetorRecuperada = null;

		try {

			Session session = retrieveSession();

			Criteria criteria = session.createCriteria(PeticaoSetor.class);

			criteria.add(Restrictions.eq("id", id));

			peticaoSetorRecuperada = (PeticaoSetor) criteria.uniqueResult();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return peticaoSetorRecuperada;
	}

	@Override
	@Deprecated
	@SuppressWarnings("unchecked")
	public Long pesquisarQuantidadePeticaoSetor(Long numeroPeticao, Short anoPeticao, Long idSetor, String siglaClasseProcessual, Long numeroProcesso, Short codigoRecurso,
			Date dataEntradaInicial, Date dataEntradaFinal, Date dataRemessaInicial, Date dataRemessaFinal, Date dataRecebimentoInicial, Date dataRecebimentoFinal,
			Boolean juntado, Boolean tratado, Boolean vinculadoProcesso, Boolean semLocalizacao, String numeroSala, String numeroArmario, String numeroEstante,
			String numeroPrateleira, String numeroColuna, Boolean deslocamentoPeticao, Long idSecaoUltimoDeslocamento, Boolean localizadoNoSetor, String tipoMeioProcesso, Boolean peticoesSemDeslocamento)
			throws DaoException {

		PeticaoSetorSearchData searchData = criarSearchData(numeroPeticao, anoPeticao, idSetor, siglaClasseProcessual, numeroProcesso, codigoRecurso, dataEntradaInicial,
				dataEntradaFinal, dataRemessaInicial, dataRemessaFinal, dataRecebimentoInicial, dataRecebimentoFinal, juntado, tratado, vinculadoProcesso, semLocalizacao,
				numeroSala, numeroArmario, numeroEstante, numeroPrateleira, numeroColuna, deslocamentoPeticao, idSecaoUltimoDeslocamento, localizadoNoSetor, tipoMeioProcesso,
				null, null, null, null, null, null,peticoesSemDeslocamento);
		return pesquisarQuantidadePeticaoSetor(searchData);
	}

	@Override
	public Long pesquisarQuantidadePeticaoSetor(PeticaoSetorSearchData peticaoSetorSearchData) throws DaoException {
		Long quantidade;

		try {
			Query query = createPeticaoSetorQuery(peticaoSetorSearchData, true);
			quantidade = (Long) query.uniqueResult();
			
			if(peticaoSetorSearchData.peticoesSemDeslocamento){
				Query querySemDeslocamento = createPeticaoSetorQuerySemDeslocamento(peticaoSetorSearchData, Boolean.TRUE);
				Long qnt = (Long) querySemDeslocamento.uniqueResult();
				quantidade += qnt;
			}
			
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return quantidade;
	}

	@Override
	@Deprecated
	@SuppressWarnings("unchecked")
	public List<PeticaoSetor> pesquisarPeticaoSetor(Long numeroPeticao, Short anoPeticao, Long idSetor, String siglaClasseProcessual, Long numeroProcesso, Short codigoRecurso,
			Date dataEntradaInicial, Date dataEntradaFinal, Date dataRemessaInicial, Date dataRemessaFinal, Date dataRecebimentoInicial, Date dataRecebimentoFinal,
			Boolean juntado, Boolean tratado, Boolean vinculadoProcesso, Boolean semLocalizacao, String numeroSala, String numeroArmario, String numeroEstante,
			String numeroPrateleira, String numeroColuna, Boolean deslocamentoPeticao, Long idSecaoUltimoDeslocamento, Boolean localizadoNoSetor, String tipoMeioProcesso,
			Boolean orderByPeticao, Boolean orderByProcesso, Boolean orderByDataEntrada, Boolean orderByCrescente, Boolean readOnlyQuery, Boolean limitarPesquisa, Boolean peticoesSemDeslocamento)
			throws DaoException {

		List<PeticaoSetor> listaPeticaoSetor = null;
		List<Peticao> listaPeticaoSetorSemDeslocamento = null;

		try {
			PeticaoSetorSearchData searchData = criarSearchData(numeroPeticao, anoPeticao, idSetor, siglaClasseProcessual, numeroProcesso, codigoRecurso, dataEntradaInicial,
					dataEntradaFinal, dataRemessaInicial, dataRemessaFinal, dataRecebimentoInicial, dataRecebimentoFinal, juntado, tratado, vinculadoProcesso, semLocalizacao,
					numeroSala, numeroArmario, numeroEstante, numeroPrateleira, numeroColuna, deslocamentoPeticao, idSecaoUltimoDeslocamento, localizadoNoSetor, tipoMeioProcesso,
					orderByPeticao, orderByProcesso, orderByDataEntrada, orderByCrescente, readOnlyQuery, limitarPesquisa,peticoesSemDeslocamento);

			Query query = createPeticaoSetorQuery(searchData, false);

			listaPeticaoSetor = query.list();
			
			
			if(searchData.peticoesSemDeslocamento){
				Query querySemDeslocamento = createPeticaoSetorQuerySemDeslocamento(searchData, false);
				listaPeticaoSetorSemDeslocamento = querySemDeslocamento.list();
				
				for (Peticao peticao : listaPeticaoSetorSemDeslocamento){
					PeticaoSetor ps = new PeticaoSetor();
					ps.setPeticao(peticao);
					listaPeticaoSetor.add(ps);
				}
				if (searchData.tipoOrdemProcesso == TipoOrdemProcesso.PETICAO){
					if( PeticaoSetorSearchData.maiorQueZero(searchData.anoPeticao) || PeticaoSetorSearchData.maiorQueZero(searchData.numeroPeticao) || (searchData.juntado != null)) {
						if (searchData.tipoOrdem == TipoOrdem.CRESCENTE)
							Collections.sort(listaPeticaoSetor, new PeticaoComparatorCrescenteAnoNumero());
						else 
							Collections.sort(listaPeticaoSetor, new PeticaoComparatorDecrescenteAnoNumero());
					}
				}
				
			}

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return listaPeticaoSetor;
	}

	@Override
	public List<PeticaoSetor> pesquisarPeticaoSetor(PeticaoSetorSearchData peticaoSetorSearchData) throws DaoException {
		List<PeticaoSetor> listaPeticaoSetor = null;

		try {
			Query query = createPeticaoSetorQuery(peticaoSetorSearchData, false);
			listaPeticaoSetor = query.list();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return listaPeticaoSetor;
	}

	private PeticaoSetorSearchData criarSearchData(Long numeroPeticao, Short anoPeticao, Long idSetor, String siglaClasseProcessual, Long numeroProcesso, Short codigoRecurso,
			Date dataEntradaInicial, Date dataEntradaFinal, Date dataRemessaInicial, Date dataRemessaFinal, Date dataRecebimentoInicial, Date dataRecebimentoFinal,
			Boolean juntado, Boolean tratado, Boolean vinculadoProcesso, Boolean semLocalizacao, String numeroSala, String numeroArmario, String numeroEstante,
			String numeroPrateleira, String numeroColuna, Boolean deslocamentoPeticao, Long idSecaoUltimoDeslocamento, Boolean localizadoNoSetor, String tipoMeioProcesso,
			Boolean orderByPeticao, Boolean orderByProcesso, Boolean orderByDataEntrada, Boolean orderByCrescente, Boolean readOnlyQuery, Boolean limitarPesquisa, Boolean peticoesSemDeslocamento) {

		TipoOrdem tipoOrdem = PeticaoSetorSearchData.isTrue(orderByCrescente) ? TipoOrdem.CRESCENTE : PeticaoSetorSearchData.isFalse(orderByCrescente) ? TipoOrdem.DECRESCENTE
				: null;
		TipoOrdemProcesso tipoOrdemProcesso = null;
		if (PeticaoSetorSearchData.isTrue(orderByProcesso)) {
			tipoOrdemProcesso = TipoOrdemProcesso.PROCESSO;
		} else if (PeticaoSetorSearchData.isTrue(orderByPeticao)) {
			tipoOrdemProcesso = TipoOrdemProcesso.PETICAO;
		} else if (PeticaoSetorSearchData.isTrue(orderByDataEntrada)) {
			tipoOrdemProcesso = TipoOrdemProcesso.DT_ENTRADA;
		}

		PeticaoSetorSearchData searchData = new PeticaoSetorSearchData(limitarPesquisa, readOnlyQuery, tipoOrdem, numeroPeticao, anoPeticao, idSetor, siglaClasseProcessual,
				numeroProcesso, codigoRecurso, dataEntradaInicial, dataEntradaFinal, dataRemessaInicial, dataRemessaFinal, dataRecebimentoInicial, dataRecebimentoFinal, juntado,
				tratado, vinculadoProcesso, semLocalizacao, numeroSala, numeroArmario, numeroEstante, numeroPrateleira, numeroColuna, deslocamentoPeticao,
				idSecaoUltimoDeslocamento, localizadoNoSetor, tipoMeioProcesso, tipoOrdemProcesso,peticoesSemDeslocamento);
		return searchData;
	}

	private Query createPeticaoSetorQuery(PeticaoSetorSearchData peticaoSetorSearchData, Boolean quantidade) throws DaoException {
		try {

			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer("");

			if (quantidade != null && quantidade)
				hql.append("SELECT count(ps.id) ");
			else
				hql.append("SELECT ps ");

			hql.append(" FROM PeticaoSetor ps ");
			hql.append(" JOIN ps.peticao pt LEFT JOIN pt.objetoIncidenteVinculado oiv ");

			if (peticaoSetorSearchData.dataRemessaInicial != null || peticaoSetorSearchData.dataRemessaFinal != null || peticaoSetorSearchData.dataRecebimentoFinal != null
					&& peticaoSetorSearchData.dataRecebimentoInicial != null || (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.numeroSala))
					|| (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.numeroArmario))
					|| (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.numeroEstante))
					|| (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.numeroPrateleira))
					|| (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.numeroColuna))
					|| (PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.idSecaoUltimoDeslocamento))) {

				if (PeticaoSetorSearchData.isTrue(peticaoSetorSearchData.deslocamentoPeticao)) {
					hql.append(" INNER JOIN ps.deslocamentoAtual da ");
				} else {
					hql.append(" ,ProcessoSetor ps2 ,ps2.deslocamentoAtual da ");
				}
			}

			hql.append(" WHERE ");

			hql.append(" ps.peticao.id = pt.id ");

			if (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.siglaClasseProcessual) || (PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.numeroProcesso))
					|| (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.tipoMeioProcesso))) {

				hql.append(" AND EXISTS ( SELECT pr.id FROM Processo pr WHERE oiv.id = pr.id ");

				if (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.siglaClasseProcessual))
					hql.append(" AND pr.siglaClasseProcessual = :siglaClasseProcessual ");

				if (PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.numeroProcesso))
					hql.append(" AND pr.numeroProcessual = :numeroProcesso ");

				if (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.tipoMeioProcesso))
					hql.append(" AND pr.tipoMeioProcesso = :tipoMeioProcesso");

				hql.append(" ) ");
			}

			if (PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.anoPeticao))
				hql.append(" AND pt.anoPeticao = :anoPeticao ");

			if (PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.numeroPeticao))
				hql.append(" AND pt.numeroPeticao = :numeroPeticao ");

			/*
			 * Com a entrada do eJUD em produção, a FLG_JUNTADA_PROCESSO deixou de ser alimentada, o sistema verifica que existe incidente vinculado se existir,
			 * foi juntada, se não existir não foi juntada.
			 * 
			 * Código anterior: if( juntado != null ){ if(juntado) hql.append(" AND pt.juntadaProcesso = 'S'"); else
			 * hql.append(" AND pt.juntadaProcesso = 'N'");}
			 */

			/*
			 * if( juntado != null ){ if(juntado) hql.append(" AND pt.objetoIncidenteVinculado is not null"); else
			 * hql.append(" AND pt.objetoIncidenteVinculado is null"); }
			 */
			if (PeticaoSetorSearchData.isTrue(peticaoSetorSearchData.juntado)) {
				// Se a pesquisa for por petições juntadas a regra é a
				// seguinte:
				// - petições fisicas com o andamento 8245 ou petições de
				// processos eletrônicas (Definição Presidência);
				hql.append(" AND ((exists(select ap FROM AndamentoPeticao ap " + "WHERE ap.objetoIncidente.id = pt.id "
						+ "AND ap.tipoAndamento.id = 8245) AND EXISTS (SELECT pr.id FROM Processo pr WHERE oiv.id = pr.id AND pr.tipoMeioProcesso <> :tipoEletronico)) "
						+ "OR EXISTS (SELECT pr.id FROM Processo pr WHERE oiv.id = pr.id AND pr.tipoMeioProcesso = :tipoEletronico))");
			} else if (PeticaoSetorSearchData.isFalse(peticaoSetorSearchData.juntado)) {
				// Se a pesquisa for por petições não juntadas ele traz
				// somente as fisicas, pois as eletrônicas já estão juntadas
				// (definição da Presidência)
				hql.append(" AND (not exists(SELECT ap FROM AndamentoPeticao ap WHERE ap.objetoIncidente.id = pt.id AND ap.tipoAndamento.id = 8245) ").append(
						" AND EXISTS (SELECT pr.id FROM Processo pr WHERE oiv.id = pr.id AND pr.tipoMeioProcesso <> :tipoEletronico)) ");
			}

			if (PeticaoSetorSearchData.isTrue(peticaoSetorSearchData.tratado)) {
				hql.append(" AND ps.dataFimTramite IS NOT NULL ");
			} else if (PeticaoSetorSearchData.isFalse(peticaoSetorSearchData.tratado)) {
				hql.append(" AND ps.dataFimTramite IS NULL ");
			}

			if (PeticaoSetorSearchData.isTrue(peticaoSetorSearchData.vinculadoProcesso)) {
				hql.append(" AND oiv IS NOT NULL AND oiv.tipoObjetoIncidente = 'PR' ");
			} else if (PeticaoSetorSearchData.isFalse(peticaoSetorSearchData.vinculadoProcesso)) {
				hql.append(" AND oiv IS NULL OR oiv.tipoObjetoIncidente <> 'PR' ");
			}

			if (peticaoSetorSearchData.idSetor != null)
				hql.append(" AND ps.setor.id = :idSetor ");

			// if( codigoRecurso != null )
			// hql.append(" AND rp.codigoRecurso = :codigoRecurso");

			if (peticaoSetorSearchData.dataEntradaInicial != null)
				hql.append(" AND ps.dataEntradaSetor >= to_date(:dataEntradaInicial, 'DD/MM/YYYY HH24:MI:SS') ");

			if (peticaoSetorSearchData.dataEntradaFinal != null)
				hql.append(" AND ps.dataEntradaSetor <= to_date(:dataEntradaFinal, 'DD/MM/YYYY HH24:MI:SS') ");

			if (PeticaoSetorSearchData.isTrue(peticaoSetorSearchData.semLocalizacao)) {
				hql.append(" AND ps.deslocamentoAtual IS NULL ");

			} else {

				if (PeticaoSetorSearchData.isFalse(peticaoSetorSearchData.semLocalizacao))
					hql.append(" AND ps.deslocamentoAtual IS NOT NULL ");

				if (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.numeroSala) || PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.numeroArmario)
						|| PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.numeroEstante)
						|| (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.numeroPrateleira))
						|| PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.numeroColuna)) {
					hql.append(" AND contains(da.flagAtualizado,' ");
				}

				if (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.numeroSala))
					hql.append(" (%" + peticaoSetorSearchData.numeroSala + "% WITHIN NUM_SALA) ");

				if (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.numeroArmario)) {

					if (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.numeroSala))
						hql.append(" AND (%" + peticaoSetorSearchData.numeroArmario + "% WITHIN NUM_ARMARIO) ");
					else
						hql.append(" (%" + peticaoSetorSearchData.numeroArmario + "% WITHIN NUM_ARMARIO) ");

				}

				if (peticaoSetorSearchData.numeroEstante != null && peticaoSetorSearchData.numeroEstante.trim().length() > 0) {

					if ((peticaoSetorSearchData.numeroSala != null && peticaoSetorSearchData.numeroSala.trim().length() > 0)
							|| (peticaoSetorSearchData.numeroArmario != null && peticaoSetorSearchData.numeroArmario.trim().length() > 0))
						hql.append(" AND (%" + peticaoSetorSearchData.numeroEstante + "% WITHIN NUM_ESTANTE) ");
					else
						hql.append(" (%" + peticaoSetorSearchData.numeroSala + "% WITHIN NUM_ESTANTE) ");

				}

				if (peticaoSetorSearchData.numeroPrateleira != null && peticaoSetorSearchData.numeroPrateleira.trim().length() > 0)

					if ((peticaoSetorSearchData.numeroSala != null && peticaoSetorSearchData.numeroSala.trim().length() > 0)
							|| (peticaoSetorSearchData.numeroArmario != null && peticaoSetorSearchData.numeroArmario.trim().length() > 0)
							|| (peticaoSetorSearchData.numeroEstante != null && peticaoSetorSearchData.numeroEstante.trim().length() > 0))
						hql.append(" AND (%" + peticaoSetorSearchData.numeroPrateleira + "% WITHIN NUM_PRATELEIRA) ");
					else
						hql.append(" (%" + peticaoSetorSearchData.numeroPrateleira + "% WITHIN NUM_PRATELEIRA) ");

				if (peticaoSetorSearchData.numeroColuna != null && peticaoSetorSearchData.numeroColuna.trim().length() > 0)

					if ((peticaoSetorSearchData.numeroSala != null && peticaoSetorSearchData.numeroSala.trim().length() > 0)
							|| (peticaoSetorSearchData.numeroArmario != null && peticaoSetorSearchData.numeroArmario.trim().length() > 0)
							|| (peticaoSetorSearchData.numeroEstante != null && peticaoSetorSearchData.numeroEstante.trim().length() > 0)
							|| (peticaoSetorSearchData.numeroPrateleira != null && peticaoSetorSearchData.numeroPrateleira.trim().length() > 0))
						hql.append(" AND (%" + peticaoSetorSearchData.numeroColuna + "% WITHIN NUM_COLUNA) ");
					else
						hql.append(" (%" + peticaoSetorSearchData.numeroColuna + "% WITHIN NUM_COLUNA) ");

				if ((peticaoSetorSearchData.numeroSala != null && peticaoSetorSearchData.numeroSala.trim().length() > 0)
						|| (peticaoSetorSearchData.numeroArmario != null && peticaoSetorSearchData.numeroArmario.trim().length() > 0)
						|| (peticaoSetorSearchData.numeroEstante != null && peticaoSetorSearchData.numeroEstante.trim().length() > 0)
						|| (peticaoSetorSearchData.numeroPrateleira != null && peticaoSetorSearchData.numeroPrateleira.trim().length() > 0)
						|| (peticaoSetorSearchData.numeroColuna != null && peticaoSetorSearchData.numeroColuna.trim().length() > 0)) {
					hql.append("') > 0 ");
				}

				if (peticaoSetorSearchData.dataRemessaInicial != null)
					hql.append(" AND da.dataRemessa >= to_date(:dataRemessaInicial, 'DD/MM/YYYY HH24:MI:SS')  ");

				if (peticaoSetorSearchData.dataRemessaFinal != null)
					hql.append(" AND da.dataRemessa <= to_date(:dataRemessaFinal, 'DD/MM/YYYY HH24:MI:SS')  ");

				if (peticaoSetorSearchData.dataRecebimentoInicial != null)
					hql.append(" AND da.dataRecebimento >= to_date(:dataRecebimentoInicial, 'DD/MM/YYYY HH24:MI:SS')  ");

				if (peticaoSetorSearchData.dataRecebimentoFinal != null)
					hql.append(" AND da.dataRecebimento <= to_date(:dataRecebimentoFinal, 'DD/MM/YYYY HH24:MI:SS')  ");

				if (peticaoSetorSearchData.idSecaoUltimoDeslocamento != null && peticaoSetorSearchData.idSecaoUltimoDeslocamento > 0)
					hql.append(" AND da.secaoDestino.id = :idSecaoUltimoDeslocamento ");

			}

			if (peticaoSetorSearchData.localizadoNoSetor != null) {
				if (peticaoSetorSearchData.localizadoNoSetor)
					hql.append(" AND ps.dataSaidaSetor IS NULL ");
				else
					hql.append(" AND ps.dataSaidaSetor IS NOT NULL ");
			}

			if (quantidade == null || (quantidade != null && !quantidade)) {

				if (peticaoSetorSearchData.tipoOrdemProcesso == TipoOrdemProcesso.DT_ENTRADA) {
					if (peticaoSetorSearchData.tipoOrdem == TipoOrdem.CRESCENTE)
						hql.append(" ORDER BY ps.dataEntradaSetor ASC");
					else
						hql.append(" ORDER BY ps.dataEntradaSetor DESC");
				} else if (peticaoSetorSearchData.tipoOrdemProcesso == TipoOrdemProcesso.PETICAO && PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.anoPeticao)
						|| PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.numeroPeticao) || (peticaoSetorSearchData.juntado != null)) {
					if (peticaoSetorSearchData.tipoOrdem == TipoOrdem.CRESCENTE)
						hql.append(" ORDER BY pt.anoPeticao ASC, pt.numeroPeticao ASC ");
					else
						hql.append(" ORDER BY pt.anoPeticao DESC, pt.numeroPeticao DESC ");
				}
				// if( OderByProcesso != null && OderByProcesso ){
				// if( orderByCrescente != null &&
				// orderByCrescente.booleanValue() )
				// hql.append(" ORDER BY pr.siglaClasseProcessual ASC, pr.numeroProcessual ASC ");
				// else
				// hql.append(" ORDER BY pr.siglaClasseProcessual DESC, pr.numeroProcessual DESC ");
				// }
			}

			/*
			 * ---------------------------------- QUERY & PARAMETERS ---------------------------------
			 */

			Query query = session.createQuery(hql.toString());

			if (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.siglaClasseProcessual))
				query.setString("siglaClasseProcessual", peticaoSetorSearchData.siglaClasseProcessual);

			if (PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.numeroProcesso))
				query.setLong("numeroProcesso", peticaoSetorSearchData.numeroProcesso);

			if (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.tipoMeioProcesso))
				query.setString("tipoMeioProcesso", peticaoSetorSearchData.tipoMeioProcesso);

			if (PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.numeroPeticao))
				query.setLong("numeroPeticao", peticaoSetorSearchData.numeroPeticao);

			if (PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.anoPeticao))
				query.setShort("anoPeticao", peticaoSetorSearchData.anoPeticao);

			if (peticaoSetorSearchData.idSetor != null)
				query.setLong("idSetor", peticaoSetorSearchData.idSetor);

			// if( codigoRecurso != null ) {
			// if( codigoRecurso >= 0 )
			// query.setShort("codigoRecurso", codigoRecurso);
			// }

			if (peticaoSetorSearchData.semLocalizacao == null || PeticaoSetorSearchData.isFalse(peticaoSetorSearchData.semLocalizacao)) {
				if (peticaoSetorSearchData.dataRecebimentoInicial != null)
					query.setString("dataRecebimentoInicial", DateTimeHelper.getDataString(peticaoSetorSearchData.dataRecebimentoInicial) + " 00:00:00");

				if (peticaoSetorSearchData.dataRecebimentoFinal != null)
					query.setString("dataRecebimentoFinal", DateTimeHelper.getDataString(peticaoSetorSearchData.dataRecebimentoFinal) + " 23:59:59");

				if (peticaoSetorSearchData.dataRemessaInicial != null)
					query.setString("dataRemessaInicial", DateTimeHelper.getDataString(peticaoSetorSearchData.dataRemessaInicial) + " 00:00:00");

				if (peticaoSetorSearchData.dataRemessaFinal != null)
					query.setString("dataRemessaFinal", DateTimeHelper.getDataString(peticaoSetorSearchData.dataRemessaFinal) + " 23:59:59");

				if (PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.idSecaoUltimoDeslocamento))
					query.setLong("idSecaoUltimoDeslocamento", peticaoSetorSearchData.idSecaoUltimoDeslocamento);
			}
			if (peticaoSetorSearchData.dataEntradaInicial != null)
				query.setString("dataEntradaInicial", DateTimeHelper.getDataString(peticaoSetorSearchData.dataEntradaInicial) + " 00:00:00");

			if (peticaoSetorSearchData.dataEntradaFinal != null)
				query.setString("dataEntradaFinal", DateTimeHelper.getDataString(peticaoSetorSearchData.dataEntradaFinal) + " 23:59:59");

			if (PeticaoSetorSearchData.isTrue(peticaoSetorSearchData.limitarPesquisa)) {
				if (FWConfig.getInstance().getMaxQueryResult() > 0)
					query.setMaxResults(FWConfig.getInstance().getMaxQueryResult());
			}

			if (PeticaoSetorSearchData.isTrue(peticaoSetorSearchData.readOnlyQuery))
				query.setReadOnly(true);

			if (peticaoSetorSearchData.juntado != null) {
				query.setString("tipoEletronico", "E");
			}

			return query;

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}
	
	private Query createPeticaoSetorQuerySemDeslocamento(PeticaoSetorSearchData peticaoSetorSearchData, Boolean quantidade) throws DaoException {
		
		try {

			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer("");
			
			if (quantidade != null && quantidade){
				hql.append(" SELECT count(pe.id) ");
			} else {						
				hql.append(" SELECT pe  ");				
			}	    
			hql.append(" FROM Peticao pe INNER JOIN pe.objetoIncidenteVinculado oiv, ProcessoSetor ps ");
			hql.append(" WHERE oiv.principal = ps.objetoIncidente ");
			
			hql.append(" AND ps.dataSaida IS NULL ");
			if (peticaoSetorSearchData.idSetor != null){ 
				    hql.append(" AND ps.setor = :idSetor ");
			}			
			//hql.append(" AND pe.objetoIncidente NOT IN (SELECT pss.peticao.objetoIncidente FROM PeticaoSetor pss WHERE pss.setor = ps.setor) ");
			hql.append(" AND NOT EXISTS (SELECT pss FROM PeticaoSetor pss WHERE pss.setor = ps.setor  AND pss.peticao.objetoIncidente = pe.objetoIncidente) ");
			
			if (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.siglaClasseProcessual) || (PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.numeroProcesso))
				    || (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.tipoMeioProcesso))) {
					hql.append(" AND EXISTS ( SELECT pr.id FROM Processo pr WHERE pr.id = oiv.principal.id");				   
				if (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.siglaClasseProcessual)) 
					hql.append(" AND pr.siglaClasseProcessual = :siglaClasseProcessual ");				
				if (PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.numeroProcesso))
					hql.append(" AND pr.numeroProcessual = :numeroProcesso ");				    
				if (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.tipoMeioProcesso))    
					hql.append(" AND pr.tipoMeioProcesso = :tipoMeioProcesso");				
				hql.append("                 ) ");
			}
				
			if (PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.anoPeticao)) 
			    hql.append(" AND pe.anoPeticao = :anoPeticao ");
			if (PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.numeroPeticao)) 
			    hql.append(" AND pe.numeroPeticao = :numeroPeticao ");
				
			if (PeticaoSetorSearchData.isTrue(peticaoSetorSearchData.juntado)) {
				hql.append(" AND ((EXISTS (SELECT ap FROM AndamentoPeticao ap WHERE ap.objetoIncidente.id = pe.id AND ap.tipoAndamento.id = 8245) ");
				hql.append(" AND EXISTS (SELECT pr.id FROM Processo pr WHERE pe.principal.id = pr.id AND pr.tipoMeioProcesso <> 'E')) ");
				hql.append(" OR EXISTS (SELECT pr.id FROM Processo pr WHERE pe.principal.id = pr.id AND pr.tipoMeioProcesso = 'E'))");
			} else if (PeticaoSetorSearchData.isFalse(peticaoSetorSearchData.juntado)) {      
				hql.append(" AND (NOT EXISTS(SELECT ap FROM AndamentoPeticao ap WHERE ap.objetoIncidente.id = pe.id AND ap.tipoAndamento.id = 8245) ");
				hql.append(" AND EXISTS (SELECT pr.id FROM Processo pr WHERE pe.principal.id = pr.id AND pr.tipoMeioProcesso <> 'E')) ");			
			}
			
			if (PeticaoSetorSearchData.isTrue(peticaoSetorSearchData.vinculadoProcesso)) {
				hql.append(" AND (oiv IS NOT NULL AND oiv.tipoObjetoIncidente = 'PR') ");
			} else if (PeticaoSetorSearchData.isFalse(peticaoSetorSearchData.vinculadoProcesso)) {
				hql.append(" AND (oiv IS NULL OR oiv.tipoObjetoIncidente <> 'PR') ");
			}
			
			if (quantidade == null || (quantidade != null && !quantidade)) {
				if (peticaoSetorSearchData.tipoOrdemProcesso == TipoOrdemProcesso.PETICAO){
					if(PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.anoPeticao) || PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.numeroPeticao) || (peticaoSetorSearchData.juntado != null)) {
						if (peticaoSetorSearchData.tipoOrdem == TipoOrdem.CRESCENTE)
							hql.append(" ORDER BY pe.anoPeticao ASC, pe.numeroPeticao ASC ");
						else 
							hql.append(" ORDER BY pe.anoPeticao DESC, pe.numeroPeticao DESC ");
					}
				}				
			}	

			/*
			 * ---------------------------------- QUERY & PARAMETERS ---------------------------------
			 */
			
			
			Query query = session.createQuery(hql.toString());
			

			if (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.siglaClasseProcessual))
				query.setString("siglaClasseProcessual", peticaoSetorSearchData.siglaClasseProcessual);

			if (PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.numeroProcesso))
				query.setLong("numeroProcesso", peticaoSetorSearchData.numeroProcesso);

			if (PeticaoSetorSearchData.stringNotEmpty(peticaoSetorSearchData.tipoMeioProcesso))
				query.setString("tipoMeioProcesso", peticaoSetorSearchData.tipoMeioProcesso);

			if (PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.numeroPeticao))
				query.setLong("numeroPeticao", peticaoSetorSearchData.numeroPeticao);

			if (PeticaoSetorSearchData.maiorQueZero(peticaoSetorSearchData.anoPeticao))
				query.setShort("anoPeticao", peticaoSetorSearchData.anoPeticao);
			
		   if (peticaoSetorSearchData.idSetor != null)
				query.setLong("idSetor", peticaoSetorSearchData.idSetor);

			query.setReadOnly(true);

			return query;
			
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}

	public Long estatisticaPeticaoSetor(Long idSetor, Date dataEntradaInicial, Date dataEntradaFinal, Boolean localizadoNoSetor, String tipoMeioProcesso) throws DaoException {

		Long quantidade;

		try {

			Session session = retrieveSession();
			/*
			 * StringBuffer hql = new StringBuffer("");
			 * 
			 * hql.append("SELECT count(ps.id) ");
			 * 
			 * hql.append(" FROM PeticaoSetor ps ");
			 * 
			 * hql.append(" , ObjetoIncidente oi "); // hql.append(" JOIN ps.objetoIncidente oips ");
			 * 
			 * hql.append(" WHERE ");
			 * 
			 * hql.append(" ps.peticao = oi.anterior "); // hql.append(" ps.objetoIncidente = oips.id ");
			 * 
			 * if( tipoMeioProcesso != null && tipoMeioProcesso.trim().length() > 0 ) { hql.append(
			 * " AND ( EXISTS ( SELECT pr FROM Processo pr WHERE pr.id = oi.principal.id " ); hql.append(" AND pr.tipoMeioProcesso = :tipoMeioProcesso ) ");
			 * 
			 * hql.append( " OR EXISTS ( SELECT rp.id FROM RecursoProcesso rp, Processo rpp WHERE rp.id = oi.id AND rp.principal = rpp " );
			 * hql.append(" AND rpp.tipoMeioProcesso = :tipoMeioProcesso ) ");
			 * 
			 * hql.append( " OR EXISTS ( SELECT ij.id FROM IncidenteJulgamento ij, Processo rpp2 WHERE ij.id = oi.id AND ij.principal = rpp2  " );
			 * hql.append(" AND rpp2.tipoMeioProcesso = :tipoMeioProcesso ) ");
			 * 
			 * hql.append(" ) "); }
			 * 
			 * // hql.append( " (SELECT pt.id FROM Peticao pt WHERE pt.id = ps.objetoIncidente.id AND pt.id = oi.anterior " );
			 * 
			 * if( idSetor != null ) hql.append(" AND ps.setor.id = :idSetor ");
			 * 
			 * if( dataEntradaInicial != null ) hql.append( " AND ps.dataEntradaSetor >= to_date(:dataEntradaInicial, 'DD/MM/YYYY HH24:MI:SS') " );
			 * 
			 * if( dataEntradaFinal != null ) hql.append( " AND ps.dataEntradaSetor <= to_date(:dataEntradaFinal, 'DD/MM/YYYY HH24:MI:SS') " );
			 * 
			 * if( localizadoNoSetor != null ){ if( localizadoNoSetor) hql.append(" AND ps.dataSaidaSetor IS NULL "); else
			 * hql.append(" AND ps.dataSaidaSetor IS NOT NULL "); }
			 */
			/*
			 * ---------------------------------- QUERY & PARAMETERS ---------------------------------
			 */
			/*
			 * Query query = session.createQuery(hql.toString());
			 * 
			 * if( idSetor != null ) query.setLong("idSetor", idSetor);
			 * 
			 * if(dataEntradaInicial != null) query.setString("dataEntradaInicial", DateTimeHelper.getDataString(dataEntradaInicial) + " 00:00:00");
			 * 
			 * if(dataEntradaFinal != null) query.setString("dataEntradaFinal", DateTimeHelper.getDataString(dataEntradaFinal) + " 23:59:59");
			 * 
			 * if( tipoMeioProcesso != null && tipoMeioProcesso.trim().length() > 0 ) query.setString("tipoMeioProcesso", tipoMeioProcesso);
			 */
			PeticaoSetorSearchData searchData = criarSearchData(null, null, idSetor, null, null, null, dataEntradaInicial, dataEntradaFinal, null, null, null, null, null, null,
					null, null, null, null, null, null, null, null, null, localizadoNoSetor, tipoMeioProcesso, null, null, null, null, null, null,false);

			searchData.localizadoNoSetor = localizadoNoSetor;
			searchData.tipoMeioProcesso = tipoMeioProcesso;

			Query query = createPeticaoSetorQuery(searchData, Boolean.TRUE);
			quantidade = (Long) query.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return quantidade;

	}

	public Boolean persistirPeticaoSetor(PeticaoSetor peticaoSetor) throws DaoException {

		Boolean alterado = Boolean.FALSE;

		Session session = retrieveSession();

		try {

			session.persist(peticaoSetor);
			session.flush();

			alterado = Boolean.TRUE;

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return alterado;
	}

	@Override
	public Boolean registrarSituacaoTratada(PeticaoSetor peticaoSetor) throws DaoException {
		Boolean alterado = Boolean.FALSE;

		try {
			Connection connection = retrieveSession().connection();
			StringBuilder sql = new StringBuilder();

			sql.append(" UPDATE egab.peticao_setor ");
			sql.append(" SET dat_fim_tramite = ? ");
			sql.append(" WHERE seq_peticao_setor = ?");

			PreparedStatement statement = connection.prepareStatement(sql.toString());

			java.sql.Date dataFimTramite = null;
			if (peticaoSetor.isTratada()) {
				dataFimTramite = new java.sql.Date(peticaoSetor.getDataFimTramite().getTime());
				statement.setDate(1, dataFimTramite);
			} else {
				statement.setNull(1, Types.DATE);
			}

			statement.setLong(2, peticaoSetor.getId());

			statement.executeUpdate();
			alterado = Boolean.TRUE;
			statement.close();
		} catch (Exception exception) {
			throw new DaoException(exception);
		}

		return alterado;
	}

	private static class PeticaoComparatorCrescente implements Comparator, Serializable {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof PeticaoSetor) || !(obj2 instanceof PeticaoSetor))
				return 0;

			PeticaoSetor doc1 = (PeticaoSetor) obj1;
			PeticaoSetor doc2 = (PeticaoSetor) obj2;

			if ((doc1 == null || doc2 == null) && (doc1.getPeticao() == null || doc2.getPeticao() == null))
				return 0;

			return doc1.getPeticao().getIdentificacao().compareTo(doc2.getPeticao().getIdentificacao());

		}
	}

	private static class PeticaoComparatorDecrescente implements Comparator, Serializable {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof PeticaoSetor) || !(obj2 instanceof PeticaoSetor))
				return 0;

			PeticaoSetor doc1 = (PeticaoSetor) obj1;
			PeticaoSetor doc2 = (PeticaoSetor) obj2;

			if ((doc1 == null || doc2 == null) && (doc1.getPeticao() == null || doc2.getPeticao() == null))
				return 0;

			return doc2.getPeticao().getIdentificacao().compareTo(doc1.getPeticao().getIdentificacao());
		}
	}
	
	private static class PeticaoComparatorCrescenteAnoNumero implements Comparator, Serializable {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof PeticaoSetor) || !(obj2 instanceof PeticaoSetor))
				return 0;

			PeticaoSetor doc1 = (PeticaoSetor) obj1;
			PeticaoSetor doc2 = (PeticaoSetor) obj2;

			if ((doc1 == null || doc2 == null) && (doc1.getPeticao() == null || doc2.getPeticao() == null))
				return 0;
			
			if ((doc1.getPeticao().getAnoPeticao() == null || doc2.getPeticao().getAnoPeticao() == null) && (doc1.getPeticao().getNumeroPeticao() == null || doc2.getPeticao().getNumeroPeticao() == null))
				return 0;
			
			int sComp = doc1.getPeticao().getAnoPeticao().compareTo(doc2.getPeticao().getAnoPeticao());

            if (sComp != 0) {
               return sComp;
            } else {
              return doc1.getPeticao().getNumeroPeticao().compareTo(doc2.getPeticao().getNumeroPeticao());
            }
		}
	}

	private static class PeticaoComparatorDecrescenteAnoNumero implements Comparator, Serializable {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof PeticaoSetor) || !(obj2 instanceof PeticaoSetor))
				return 0;

			PeticaoSetor doc1 = (PeticaoSetor) obj1;
			PeticaoSetor doc2 = (PeticaoSetor) obj2;

			if ((doc1 == null || doc2 == null) && (doc1.getPeticao() == null || doc2.getPeticao() == null))
				return 0;
			
			if ((doc1.getPeticao().getAnoPeticao() == null || doc2.getPeticao().getAnoPeticao() == null) && (doc1.getPeticao().getNumeroPeticao() == null || doc2.getPeticao().getNumeroPeticao() == null))
				return 0;
			
			int sComp = doc2.getPeticao().getAnoPeticao().compareTo(doc1.getPeticao().getAnoPeticao());

            if (sComp != 0) {
               return sComp;
            } else {
              return doc2.getPeticao().getNumeroPeticao().compareTo(doc1.getPeticao().getNumeroPeticao());
            }
		}
	}

	@Override
	public Boolean isPeticaoPendenteTratamento(Processo processo) throws DaoException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("	select COUNT(*) from JUDICIARIO.PROCESSO pr, ");
			sql.append("            JUDICIARIO.PETICAO pt, ");
			sql.append("            EGAB.PETICAO_SETOR PS");
			sql.append("	where PT.SEQ_OBJETO_INCIDENTE_VINC = PR.SEQ_OBJETO_INCIDENTE (+)");
			sql.append("	AND PR.SEQ_OBJETO_INCIDENTE = " + processo.getId());
			sql.append("	AND PS.DAT_FIM_TRAMITE IS NULL");
			sql.append("	and PS.DAT_SAIDA_SETOR IS NULL");
			sql.append("	AND PS.SEQ_OBJETO_INCIDENTE = PT.SEQ_OBJETO_INCIDENTE");
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString());
			if (sqlQuery.uniqueResult() == null){
				return false;
			} else {
				Long resultado = NumberUtils.createLong(sqlQuery.uniqueResult().toString());
				if ( resultado >= (new Long(1)) ) {
					return true;
				} else {
					return false;
				}
			}
			
//			Criteria criteria = session.createCriteria(PeticaoSetor.class);
//			criteria.add(Restrictions.eq("peticao.objetoIncidenteVinculado", processo.getId()));
//			criteria.add(Restrictions.isNull("dataFimTramite"));
//			if (criteria.list() == null) {
//				return false;
//			}
//			if (criteria.list().size() == 0) {
//				return false;
//			} else {
//				return true;
	//		}

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}
}