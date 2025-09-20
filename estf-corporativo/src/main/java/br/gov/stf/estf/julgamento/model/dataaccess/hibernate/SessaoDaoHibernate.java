package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoAmbienteConstante;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoJulgamentoVirtual;
import br.gov.stf.estf.entidade.julgamento.Sessao.TipoSessaoConstante;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.julgamento.model.dataaccess.SessaoDao;
import br.gov.stf.estf.util.DataUtil;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.DateTimeHelper;

@Repository
public class SessaoDaoHibernate extends GenericHibernateDao<Sessao, Long> implements SessaoDao {

	private static final long serialVersionUID = 7858577141797523618L;

	public SessaoDaoHibernate() {
		super(Sessao.class);
	}

	public Boolean excluirSessao(Sessao sessao) throws DaoException {

		Session session = retrieveSession();
		Boolean alterado = Boolean.FALSE;

		try {
			session.delete(sessao);
			session.flush();

			alterado = Boolean.TRUE;

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return alterado;
	}

	@SuppressWarnings("unchecked")
	public List<Sessao> pesquisarSessaoSQL(Date dataInicio, Date dataFim, Date dataPrevistaInicio, Date dataPrevistaFim,
			Short anoSessao, Long numeroSessao, String tipoSessao, String tipoColegiado, String tipoAmbiente,
			Boolean numeroAnoPreenchido) throws DaoException {

		List<Object[]> sessoesObject = null;
		List<Sessao> listSessoes = null;
		try {
			Session session = retrieveSession();

			StringBuffer sql = new StringBuffer();
			sql.append("SELECT                     ");
			sql.append("   SEQ_SESSAO              ");
			sql.append("  ,NUM_SESSAO              ");
			sql.append("  ,DAT_INICIO              ");
			sql.append("  ,DAT_FIM                 ");
			sql.append("  ,DAT_PREVISTA_INICIO     ");
			sql.append("  ,DAT_PREVISTA_FIM        ");
			sql.append("  ,TIP_AMBIENTE_SESSAO     ");
			sql.append("  ,TIP_SESSAO              ");
			sql.append("  ,DAT_INCLUSAO            ");
			sql.append("  ,USU_INCLUSAO            ");
			sql.append("  ,DAT_ALTERACAO           ");
			sql.append("  ,USU_ALTERACAO           ");
			sql.append("  ,ANO_SESSAO              ");
			sql.append("  ,FLG_DISPONIVEL_INTERNET ");
			sql.append("  ,COD_COLEGIADO           ");
			sql.append("  ,TXT_OBSERVACAO          ");
			sql.append("from JULGAMENTO.SESSAO S   ");
			sql.append("WHERE 1=1                  ");
			if (dataInicio != null) {
				String dataInicial = DataUtil.date2String(dataInicio, false);
				sql.append(" AND to_char(S.DAT_INICIO, 'DD/MM/YYYY') ='" + dataInicial + "'");
			}
			if (dataFim != null) {
				String dataFimString = DataUtil.date2String(dataFim, false);
				sql.append(" AND to_char(S.DAT_FIM, 'DD/MM/YYYY') ='" + dataFimString + "'");
			}
			if (dataPrevistaInicio != null) {
				String dataPrevisaoInicial = DataUtil.date2String(dataPrevistaInicio, false);
				sql.append(" AND to_char(S.DAT_PREVISTA_INICIO, 'DD/MM/YYYY') ='" + dataPrevisaoInicial + "'");
			}
			if (dataPrevistaFim != null) {
				String dataPrevisaoFim = DataUtil.date2String(dataPrevistaFim, false);
				sql.append(" AND to_char(S.DAT_PREVISTA_FIM, 'DD/MM/YYYY') ='" + dataPrevisaoFim + "'");
			}
			if (tipoAmbiente != null) {
				sql.append(" AND TIP_AMBIENTE_SESSAO='" + tipoAmbiente + "'");
			}
			if (tipoSessao != null) {
				sql.append(" AND TIP_SESSAO='" + tipoSessao + "'");
			}
			if (anoSessao != null) {
				sql.append(" AND ANO_SESSAO='" + anoSessao + "'");
			}
			if (tipoColegiado != null) {
				sql.append(" AND COD_COLEGIADO='" + tipoColegiado + "'");
			}

			Query q = session.createSQLQuery(sql.toString());
			sessoesObject = q.list();
			listSessoes = this.Objetc2Feriado(sessoesObject);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return listSessoes;
	}

	private List<Sessao> Objetc2Feriado(List<Object[]> listObject) {
		List<Sessao> listSessoes = new ArrayList<Sessao>();
		if (listObject != null) {
			for (Object[] object : listObject) {
				Sessao sessao = new Sessao();
				sessao.setId(object[0] != null ? ((Number) object[0]).longValue() : null);
				sessao.setNumero(object[1] != null ? ((Number) object[1]).longValue() : null);
				sessao.setTipoAmbiente(object[6] != null ? (String) object[6] : null);
				sessao.setTipoSessao(object[7] != null ? (String) object[7] : null);
				listSessoes.add(sessao);
			}
		}
		return listSessoes;
	}

	@SuppressWarnings("unchecked")
	public List<Sessao> pesquisarSessao(Date dataInicio, Date dataFim, Date dataPrevistaInicio, Date dataPrevistaFim,
			Short ano, Long numero, String tipoSessao, String tipoColegiado, String tipoAmbiente,
			Boolean numeroAnoPreenchido) throws DaoException {

		Session session = retrieveSession();
		List<Sessao> resultado = null;
		StringBuffer hql = new StringBuffer();

		try {
			hql.append(" SELECT s FROM Sessao s ");
			hql.append(" WHERE 1 = 1 ");

			if (ano != null && ano > 0)
				hql.append(" AND s.ano = :anoSessao ");

			if (numero != null && numero > 0)
				hql.append(" AND s.numero = :numeroSessao ");

			if (tipoSessao != null && tipoSessao.trim().length() > 0)
				hql.append(" AND s.tipoSessao = :tipoSessaoValor ");

			if (tipoColegiado != null && tipoColegiado.trim().length() > 0)
				hql.append(" AND s.colegiado.id = :tipoColegiadoSessao ");

			if (tipoAmbiente != null && tipoAmbiente.trim().length() > 0)
				hql.append(" AND s.tipoAmbiente = :tipoAmbienteValor ");

			if (dataInicio != null) {
				hql.append(" AND s.dataInicio >= TO_DATE(:dataInicioSessaoComeco, 'DD/MM/YYYY HH24:MI:SS')");
				hql.append(" AND s.dataInicio <= TO_DATE(:dataInicioSessaoFim, 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (dataFim != null) {
				hql.append(" AND s.dataFim >= TO_DATE(:dataFimSessaoComeco, 'DD/MM/YYYY HH24:MI:SS')");
				hql.append(" AND s.dataFim <= TO_DATE(:dataFimSessaoFim, 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (dataPrevistaInicio != null) {
				hql.append(
						" AND s.dataPrevistaInicio >= TO_DATE(:dataPrevistaInicioSessaoComeco, 'DD/MM/YYYY HH24:MI:SS')");
				hql.append(
						" AND s.dataPrevistaInicio <= TO_DATE(:dataPrevistaInicioSessaoFim, 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (dataPrevistaFim != null) {
				hql.append(" AND s.dataPrevistaFim >= TO_DATE(:dataPrevistaFimSessaoComeco, 'DD/MM/YYYY HH24:MI:SS')");
				hql.append(" AND s.dataPrevistaFim <= TO_DATE(:dataPrevistaFimSessaoFim, 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (numeroAnoPreenchido != null && numeroAnoPreenchido.booleanValue()) {
				hql.append(" AND s.numero IS NOT NULL ");
				hql.append(" AND s.ano IS NOT NULL ");
			}

			hql.append(" AND s.exclusivoDigital ='N' ");

			Query q = session.createQuery(hql.toString());

			if (ano != null && ano > 0)
				q.setShort("anoSessao", ano);

			if (numero != null && numero > 0)
				q.setLong("numeroSessao", numero);

			if (tipoSessao != null && tipoSessao.trim().length() > 0)
				q.setString("tipoSessaoValor", tipoSessao);

			if (tipoColegiado != null && tipoColegiado.trim().length() > 0)
				q.setString("tipoColegiadoSessao", tipoColegiado);

			if (tipoAmbiente != null && tipoAmbiente.trim().length() > 0)
				q.setString("tipoAmbienteValor", tipoAmbiente);

			if (dataInicio != null) {
				q.setString("dataInicioSessaoComeco", DateTimeHelper.getDataString(dataInicio) + "00:00:00");
				q.setString("dataInicioSessaoFim", DateTimeHelper.getDataString(dataInicio) + " 23:59:59");
			}

			if (dataFim != null) {
				q.setString("dataFimSessaoComeco", DateTimeHelper.getDataString(dataFim) + "00:00:00");
				q.setString("dataFimSessaoFim", DateTimeHelper.getDataString(dataFim) + " 23:59:59");
			}

			if (dataPrevistaInicio != null) {
				q.setString("dataPrevistaInicioSessaoComeco",
						DateTimeHelper.getDataString(dataPrevistaInicio) + "00:00:00");
				q.setString("dataPrevistaInicioSessaoFim",
						DateTimeHelper.getDataString(dataPrevistaInicio) + " 23:59:59");
			}

			if (dataPrevistaFim != null) {
				q.setString("dataPrevistaFimSessaoComeco", DateTimeHelper.getDataString(dataPrevistaFim) + "00:00:00");
				q.setString("dataPrevistaFimSessaoFim", DateTimeHelper.getDataString(dataPrevistaFim) + " 23:59:59");
			}

			resultado = q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Sessao> pesquisarSessoesVirtuaisDeListaNaoIniciadas() throws DaoException {

		Session session = retrieveSession();
		List<Sessao> resultado = null;
		StringBuffer hql = new StringBuffer();

		try {
			hql.append(" SELECT s FROM Sessao s ");
			hql.append(" WHERE s.exclusivoDigital = 'N' AND s.tipoAmbiente = 'V' AND "
					+ " s.dataInicio IS NULL AND s.dataPrevistaInicio < SYSDATE ");

			Query q = session.createQuery(hql.toString());

			resultado = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return resultado;
	}

	public List<Sessao> pesquisarSessoesPrevistas(Date dataBase, boolean maiorDataBase) throws DaoException {

		List<Sessao> resultado = null;
		try {
			Session session = retrieveSession();

			Criteria c = session.createCriteria(Sessao.class, "s");
			if (maiorDataBase)
				c.add(Restrictions.ge("s.dataPrevistaInicio", dataBase));
			else
				c.add(Restrictions.eq("s.dataPrevistaInicio", dataBase));
			c.add(Restrictions.isNull("s.dataFim"));
			c.add(Restrictions.eq("disponibilizadoInternet", true));
			c.add(Restrictions.eq("tipoAmbiente", "F"));
			c.add(Restrictions.eq("exclusivoDigital", false));
			c.addOrder(Order.desc("s.dataPrevistaInicio"));

			resultado = (List<Sessao>) c.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return resultado;
	}

	public Date recuperarDataSessaoJulgamento(String siglaClasse, Long numeroProcesso, Long recurso,
			String tipoJulgamento) throws DaoException {

		Session session = retrieveSession();
		Date resultado = null;
		StringBuffer hql = new StringBuffer();

		try {
			hql.append(" SELECT s.dataFim FROM Sessao s ");
			hql.append(" WHERE 1 = 1 ");
			hql.append(" AND s.exclusivoDigital ='N'  ");
			hql.append(" AND s.id IN ( ");

			hql.append(" SELECT jp.sessao.id FROM JulgamentoProcesso jp ");
			hql.append(" WHERE 1 = 1 ");

			if (siglaClasse != null && siglaClasse.trim().length() > 0) {
				hql.append(" AND jp.processo.siglaClasseProcessual = '");
				hql.append(siglaClasse);
				hql.append("'");
			}

			if (numeroProcesso != null) {
				hql.append(" AND jp.processo.numeroProcessual = ");
				hql.append(numeroProcesso);
			}

			if (recurso != null) {
				hql.append(" AND jp.tipoRecurso = ");
				hql.append(recurso);
			}

			if (tipoJulgamento != null && tipoJulgamento.trim().length() > 0) {
				hql.append(" AND jp.tipoJulgamento.id = '");
				hql.append(tipoJulgamento);
				hql.append("' ");
			}

			hql.append(" ) ");

			Query q = session.createQuery(hql.toString());

			resultado = (Date) q.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return resultado;
	}

	public Long recuperarMaiorNumeroSessao() throws DaoException {
		Session session = retrieveSession();

		try {
			Criteria c = session.createCriteria(Sessao.class, "s");
			c.setProjection(Projections.max("s.numero"));

			return (Long) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	public Long recuperarMaiorNumeroSessaoVirtual(Colegiado colegiado, short ano) throws DaoException {
		Session session = retrieveSession();

		try {
			Criteria c = session.createCriteria(Sessao.class, "s");
			c.setProjection(Projections.max("s.numero"));
			c.add(Restrictions.eq("s.tipoAmbiente", TipoAmbienteConstante.VIRTUAL.getSigla()));
			c.add(Restrictions.eq("s.colegiado.id", colegiado.getId()));
			c.add(Restrictions.eq("s.ano", ano));

			return (Long) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Sessao> pesquisarSessao(Date dataInicioSessao, Date dataFimSessao, TipoAmbienteConstante tipoAmbiente,
			TipoSessaoConstante tipoSessao, String colegiado) throws DaoException {
		Session session = retrieveSession();
		List<Sessao> resultado = null;
		StringBuffer hql = new StringBuffer();
		try {
			hql.append(" SELECT s " + "  FROM Sessao s " + " WHERE 1 = 1 ");

			if (dataInicioSessao != null) {
				hql.append(" AND s.dataInicio >= TO_DATE(:dataInicioSessaoComeco, 'DD/MM/YYYY HH24:MI:SS')");
				hql.append(" AND s.dataInicio <= TO_DATE(:dataInicioSessaoFim, 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (dataFimSessao != null) {
				hql.append(" AND s.dataFim >= TO_DATE(:dataFimSessaoComeco, 'DD/MM/YYYY HH24:MI:SS')");
				hql.append(" AND s.dataFim <= TO_DATE(:dataFimSessaoFim, 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (tipoAmbiente != null) {
				hql.append(" AND s.tipoAmbiente = :tipoAmbiente ");
			}

			if (tipoSessao != null) {
				hql.append(" AND s.tipoSessao = :tipoSessao ");
			}

			if (colegiado != null && colegiado.trim().length() > 0) {
				hql.append(" AND s.colegiado.id = :colegiado ");
			}

			Query q = session.createQuery(hql.toString());

			if (dataInicioSessao != null) {
				q.setString("dataInicioSessaoComeco", DateTimeHelper.getDataString(dataInicioSessao) + "00:00:00");
				q.setString("dataInicioSessaoFim", DateTimeHelper.getDataString(dataInicioSessao) + " 23:59:59");
			}

			if (dataFimSessao != null) {
				q.setString("dataFimSessaoComeco", DateTimeHelper.getDataString(dataFimSessao) + "00:00:00");
				q.setString("dataFimSessaoFim", DateTimeHelper.getDataString(dataFimSessao) + " 23:59:59");
			}

			if (tipoAmbiente != null) {
				q.setString("tipoAmbiente", tipoAmbiente.getSigla());
			}

			if (tipoSessao != null) {
				q.setString("tipoSessao", tipoSessao.getSigla());
			}

			if (colegiado != null && colegiado.trim().length() > 0) {
				q.setString("colegiado", colegiado);
			}

			resultado = q.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return resultado;
	}

	@SuppressWarnings("unchecked")
	public List<Sessao> pesquisarSessaoVirtual(Date dataInicioSessao, Date dataFimSessao, TipoAmbienteConstante tipoAmbiente,
			TipoSessaoConstante tipoSessao, String colegiado) throws DaoException {
		Session session = retrieveSession();
		List<Sessao> resultado = null;
		StringBuffer hql = new StringBuffer();
		try {
			hql.append(" SELECT s " + "  FROM Sessao s " + " WHERE 1 = 1 ");

			if (dataInicioSessao != null) {
				hql.append(" AND s.dataPrevistaInicio = TO_DATE(:dataInicioSessaoComeco, 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (dataFimSessao != null) {
				hql.append(" AND s.dataPrevistaFim = TO_DATE(:dataFimSessaoComeco, 'DD/MM/YYYY HH24:MI:SS')");
			}

			if (tipoAmbiente != null) {
				hql.append(" AND s.tipoAmbiente = :tipoAmbiente ");
			}

			if (tipoSessao != null) {
				hql.append(" AND s.tipoSessao = :tipoSessao ");
			}

			if (colegiado != null && colegiado.trim().length() > 0) {
				hql.append(" AND s.colegiado.id = :colegiado ");
			}

			Query q = session.createQuery(hql.toString());

			if (dataInicioSessao != null) {
				q.setString("dataInicioSessaoComeco", DateTimeHelper.getDataHoraString(dataInicioSessao));
			}

			if (dataFimSessao != null) {
				q.setString("dataFimSessaoComeco", DateTimeHelper.getDataHoraString(dataFimSessao));
			}

			if (tipoAmbiente != null) {
				q.setString("tipoAmbiente", tipoAmbiente.getSigla());
			}

			if (tipoSessao != null) {
				q.setString("tipoSessao", tipoSessao.getSigla());
			}

			if (colegiado != null && colegiado.trim().length() > 0) {
				q.setString("colegiado", colegiado);
			}

			resultado = q.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return resultado;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Sessao pesquisarSessao(Date dataInicioSessao, TipoAmbienteConstante tipoAmbiente,
			TipoSessaoConstante tipoSessao, String colegiado) throws DaoException {
		Session session = retrieveSession();
		List<Sessao> resultado = null;
		StringBuffer hql = new StringBuffer();
		try {
			hql.append(" SELECT s " + "  FROM Sessao s " + " WHERE 1 = 1 ");
			hql.append(" AND s.exclusivoDigital ='N'  ");

			if (dataInicioSessao != null) {
				hql.append(" AND TRUNC(s.dataInicio) = TO_DATE(:dataInicioSessaoComeco, 'DD/MM/YYYY') ");
			}

			if (tipoAmbiente != null) {
				hql.append(" AND s.tipoAmbiente = :tipoAmbiente ");
			}

			if (tipoSessao != null) {
				hql.append(" AND s.tipoSessao = :tipoSessao ");
			}

			if (colegiado != null && colegiado.trim().length() > 0) {
				hql.append(" AND s.colegiado.id = :colegiado ");
			}

			Query q = session.createQuery(hql.toString());

			if (dataInicioSessao != null) {
				q.setString("dataInicioSessaoComeco", DateTimeHelper.getDataString(dataInicioSessao));
			}

			if (tipoAmbiente != null) {
				q.setString("tipoAmbiente", tipoAmbiente.getSigla());
			}

			if (tipoSessao != null) {
				q.setString("tipoSessao", tipoSessao.getSigla());
			}

			if (colegiado != null && colegiado.trim().length() > 0) {
				q.setString("colegiado", colegiado);
			}

			resultado = q.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}
		return (resultado != null && resultado.size() > 0) ? resultado.get(0) : null;
	}

	public Sessao recuperar(Long seqObjetoIncidente) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		try {
			hql.append("SELECT s FROM Sessao s JOIN s.listaJulgamentoProcesso jps ");
			hql.append("WHERE ");

			if (seqObjetoIncidente != null && seqObjetoIncidente.longValue() > 0)
				hql.append("jps.objetoIncidente.id = :seqObjetoIncidente ");

			hql.append(" AND s.exclusivoDigital ='N'  ");

			Query q = session.createQuery(hql.toString());

			if (seqObjetoIncidente != null && seqObjetoIncidente.longValue() > 0)
				q.setLong("seqObjetoIncidente", seqObjetoIncidente);

			return (Sessao) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	public List<Sessao> recuperarExclusivoDigital(Long seqObjetoIncidente) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		try {
			hql.append("SELECT s FROM Sessao s JOIN s.listaJulgamentoProcesso jps ");
			hql.append("WHERE s.exclusivoDigital ='S' AND s.dataFim is null AND ");

			if (seqObjetoIncidente != null && seqObjetoIncidente.longValue() > 0)
				hql.append("jps.objetoIncidente.id = :seqObjetoIncidente ");

			Query q = session.createQuery(hql.toString());

			if (seqObjetoIncidente != null && seqObjetoIncidente.longValue() > 0)
				q.setLong("seqObjetoIncidente", seqObjetoIncidente);

			return (List<Sessao>) q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	public Sessao recuperar(ObjetoIncidente<?> objetoIncidente) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		try {
			hql.append("SELECT s FROM Sessao s JOIN s.listaJulgamentoProcesso jps ");
			hql.append("WHERE ");

			hql.append("s.dataFim IS null ");
			hql.append(" AND s.exclusivoDigital ='N'  ");

			if (objetoIncidente != null && objetoIncidente.getId().longValue() > 0) {
				hql.append("AND jps.objetoIncidente.id = :seqObjetoIncidente ");
			}

			Query q = session.createQuery(hql.toString());

			if (objetoIncidente != null && objetoIncidente.getId().longValue() > 0) {
				q.setLong("seqObjetoIncidente", objetoIncidente.getId().longValue());
			}

			return (Sessao) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> pesquisarSessao(TipoColegiadoConstante colegiado, TipoAmbienteConstante tipoAmbienteSessao,
			String tipoSessao, Date dataBase) throws DaoException {
		Session session = retrieveSession();
		List<Object[]> resultado = null;
		StringBuffer hql = new StringBuffer();

		try {
			hql.append(
					" SELECT s, count(distinct jp.objetoIncidente), count(distinct lj) FROM Sessao s LEFT JOIN s.listaJulgamentoProcesso jp ");
			hql.append(" LEFT JOIN s.listasJulgamento lj ");
			hql.append(" WHERE 1 = 1 ");
			hql.append(" AND s.exclusivoDigital ='N'  ");
			if (colegiado != null) {
				hql.append(" AND s.colegiado = :colegiado ");
			}
			if (tipoAmbienteSessao != null) {
				hql.append(" AND s.tipoAmbiente = :tipoAmbiente ");
			}
			if (tipoSessao != null && tipoSessao.length() > 0) {
				hql.append(" AND s.tipoSessao = :tipoSessao ");
			}
			if (dataBase != null) {
				hql.append(" AND s.dataInicio >= TO_DATE(:dataBase, 'DD/MM/YYYY HH24:MI:SS')");
			}

			hql.append(" AND s.dataFim IS NULL ");

			hql.append(" GROUP BY s.id, s.numero, s.ano, s.dataInicio, s.dataFim, ");
			hql.append(
					"s.dataPrevistaInicio, s.dataPrevistaFim, s.tipoSessao, s.colegiado, s.tipoAmbiente, s.disponibilizadoInternet, s.observacao ");

			hql.append(" ORDER BY s.dataInicio, s.colegiado ");

			Query q = session.createQuery(hql.toString());

			if (colegiado != null) {
				q.setString("colegiado", colegiado.getSigla());
			}
			if (tipoAmbienteSessao != null) {
				q.setString("tipoAmbiente", tipoAmbienteSessao.getSigla());
			}
			if (tipoSessao != null && tipoSessao.length() > 0) {
				q.setString("tipoSessao", tipoSessao);
			}
			if (dataBase != null) {
				q.setString("dataBase", DateTimeHelper.getDataString(dataBase) + " 00:00:00");
			}

			resultado = q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
		return resultado;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> pesquisarSessao(String colegiado, TipoAmbienteConstante tipoAmbienteSessao, String tipoSessao,
			Date dataBase) throws DaoException {
		Session session = retrieveSession();
		List<Object[]> resultado = null;
		StringBuffer hql = new StringBuffer();
		try {
			hql.append(" SELECT s, count(jp.objetoIncidente) FROM Sessao s LEFT JOIN s.listaJulgamentoProcesso jp ");
			hql.append(" WHERE 1 = 1 ");
			hql.append(" AND s.exclusivoDigital ='N'  ");

			if (colegiado != null) {
				hql.append(" AND s.colegiado = :colegiado ");
			}
			if (tipoAmbienteSessao != null) {
				hql.append(" AND s.tipoAmbiente = :tipoAmbiente ");
			}
			if (tipoSessao != null && tipoSessao.length() > 0) {
				hql.append(" AND s.tipoSessao = :tipoSessao ");
			}
			if (dataBase != null) {
				hql.append(" AND s.dataInicio >= TO_DATE(:dataBase, 'DD/MM/YYYY HH24:MI:SS')");
			}

			hql.append(" GROUP BY s.id, s.numero, s.ano, s.dataInicio, s.dataFim, ");
			hql.append(
					"s.dataPrevistaInicio, s.dataPrevistaFim, s.tipoSessao, s.colegiado, s.tipoAmbiente, s.disponibilizadoInternet, s.observacao ");

			hql.append(" ORDER BY s.dataInicio, s.colegiado ");

			Query q = session.createQuery(hql.toString());

			if (colegiado != null) {
				q.setString("colegiado", colegiado);
			}
			if (tipoAmbienteSessao != null) {
				q.setString("tipoAmbiente", tipoAmbienteSessao.getSigla());
			}
			if (tipoSessao != null && tipoSessao.length() > 0) {
				q.setString("tipoSessao", tipoSessao);
			}
			if (dataBase != null) {
				q.setString("dataBase", DateTimeHelper.getDataString(dataBase) + " 00:00:00");
			}

			resultado = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return resultado;
	}

	@Override
	public void refresh(Sessao sessao) throws DaoException {
		retrieveSession().refresh(sessao);
	}

	@Override
	public List<Sessao> pesquisar(TipoColegiadoConstante colegiado) throws DaoException {
		return this.pesquisar(colegiado, TipoAmbienteConstante.PRESENCIAL);
	}

	public List<Sessao> pesquisar(TipoColegiadoConstante colegiado, TipoAmbienteConstante ambiente)
			throws DaoException {
		return pesquisar(colegiado, ambiente, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sessao> pesquisar(TipoColegiadoConstante colegiado, TipoAmbienteConstante ambiente,
			Boolean sessaoVirtualExtraordinaria) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Sessao.class);
			c.add(Restrictions.eq("colegiado.id", colegiado.getSigla()));
			c.add(Restrictions.isNull("dataFim"));
			c.add(Restrictions.eq("exclusivoDigital", false));
			if (ambiente == null) {
				c.add(Restrictions.eq("tipoAmbiente", TipoAmbienteConstante.PRESENCIAL.getSigla()));
			} else {
				c.add(Restrictions.eq("tipoAmbiente", ambiente.getSigla()));
			}

			if (sessaoVirtualExtraordinaria != null) {
				if (sessaoVirtualExtraordinaria) {
					c.add(Restrictions.eq("tipoSessao", TipoSessaoConstante.EXTRAORDINARIA.getSigla()));
				} else {
					c.add(Restrictions.eq("tipoSessao", TipoSessaoConstante.ORDINARIA.getSigla()));
				}
			}

			c.addOrder(Order.asc("dataInicio"));
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Long recuperarUltimoNumeroSessao(Sessao novaSessao) throws DaoException {
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer("SELECT max(s.numero) FROM Sessao s ");
			hql.append(" WHERE 1 = 1 ");
			hql.append(" AND s.exclusivoDigital ='N'  ");
			if (novaSessao.getColegiado() != null) {
				hql.append(" AND s.colegiado.id = :colegiado ");
			}
			if (novaSessao.getTipoSessao() != null) {
				hql.append(" AND s.tipoSessao = :tipoSessao ");
			}
			if (novaSessao.getAno() != null) {
				hql.append(" AND s.ano = :ano");
			}
			if (novaSessao.getTipoAmbiente() != null) {
				hql.append(" AND s.tipoAmbiente = :tipoAmbiente ");
			}

			Query q = session.createQuery(hql.toString());

			if (novaSessao.getColegiado() != null) {
				q.setParameter("colegiado", novaSessao.getColegiado().getId());
			}
			if (novaSessao.getTipoSessao() != null) {
				q.setString("tipoSessao", novaSessao.getTipoSessao());
			}
			if (novaSessao.getAno() != null) {
				q.setShort("ano", new Short(novaSessao.getAno().shortValue()));
			}
			if (novaSessao.getTipoAmbiente() != null) {
				q.setString("tipoAmbiente", new String(novaSessao.getTipoAmbiente()));
			}

			return (Long) q.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sessao> pesquisarSessaoNaoEncerrada(TipoColegiadoConstante colegiado,
			TipoAmbienteConstante tipoAmbienteSessao, TipoSessaoConstante tipoSessaoConstante,
			TipoJulgamentoVirtual tipoJulgamentoVirtual) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		try {
			hql.append(" SELECT s FROM Sessao s ");
			hql.append(" WHERE 1 = 1 ");
			hql.append(" AND s.exclusivoDigital <> 'S' ");

			if (colegiado != null) {
				hql.append(" AND s.colegiado = :colegiado ");
			}
			if (tipoAmbienteSessao != null) {
				hql.append(" AND s.tipoAmbiente = :tipoAmbiente ");
			}

			if (tipoSessaoConstante != null) {
				hql.append(" AND s.tipoSessao = :tipoSessao ");
			}

			if (tipoJulgamentoVirtual != null)
				hql.append(" AND s.tipoJulgamentoVirtual = :tipoJulgamentoVirtual ");

			hql.append(" AND s.dataFim IS NULL ");

			hql.append(" GROUP BY s.id, s.numero, s.ano, s.dataInicio, s.dataFim, ");
			hql.append(
					"s.dataPrevistaInicio, s.dataPrevistaFim, s.tipoSessao, s.colegiado, s.tipoAmbiente, s.disponibilizadoInternet, s.observacao, s.tipoJulgamentoVirtual, s.exclusivoDigital  ");

			hql.append(" ORDER BY s.dataInicio, s.dataPrevistaInicio, s.colegiado, s.id  ");

			Query q = session.createQuery(hql.toString());

			if (colegiado != null) {
				q.setString("colegiado", colegiado.getSigla());
			}
			if (tipoAmbienteSessao != null) {
				q.setString("tipoAmbiente", tipoAmbienteSessao.getSigla());
			}
			if (tipoSessaoConstante != null) {
				q.setString("tipoSessao", tipoSessaoConstante.getSigla());
			}

			if (tipoJulgamentoVirtual != null)
				q.setInteger("tipoJulgamentoVirtual", tipoJulgamentoVirtual.getId());

			return q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Long pesquisarQuantidadeProcessosNaSessao(Sessao sessao) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		try {
			hql.append(" SELECT count(distinct jp.objetoIncidente) FROM JulgamentoProcesso jp ");
			hql.append(" WHERE 1 = 1 ");

			if (sessao != null) {
				hql.append(" AND jp.sessao.id = :sessao ");
			}

			Query q = session.createQuery(hql.toString());

			if (sessao != null) {
				q.setParameter("sessao", sessao.getId());
			}

			return (Long) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Long pesquisarQuantidadeProcessosDasListas(Sessao sessao) throws DaoException {
		Session session = retrieveSession();
		StringBuffer sql = new StringBuffer();
		try {
			sql.append(
					" SELECT count(plj.SEQ_PROCESSO_LISTA_JULG) FROM JULGAMENTO.LISTA_JULGAMENTO lj,JULGAMENTO.PROCESSO_LISTA_JULG plj ");
			sql.append(" WHERE 1 = 1 ");
			sql.append(" AND lj.SEQ_LISTA_JULGAMENTO = plj.SEQ_LISTA_JULGAMENTO ");
			sql.append(" AND NOT REGEXP_LIKE (lj.DSC_LISTA_PROCESSO, '[a-z]$', 'i') ");

			if (sessao != null) {
				sql.append(" AND lj.SEQ_SESSAO = :idSessao ");
			}

			SQLQuery q = session.createSQLQuery(sql.toString());

			if (sessao != null) {
				q.setLong("idSessao", sessao.getId());
			}

			BigDecimal resultado = (BigDecimal) q.uniqueResult();

			if (resultado != null) {
				return new Long(resultado.longValue());
			}

			return null;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Long pesquisarQuantidadeListasNaSessao(Sessao sessao) throws DaoException {
		Session session = retrieveSession();
		StringBuffer sql = new StringBuffer();
		try {
			sql.append(" SELECT count(distinct lj.SEQ_LISTA_JULGAMENTO) FROM JULGAMENTO.LISTA_JULGAMENTO lj ");
			sql.append(" WHERE 1 = 1 ");
			sql.append(" AND NOT REGEXP_LIKE (lj.DSC_LISTA_PROCESSO, '[a-z]$', 'i') ");

			if (sessao != null) {
				sql.append(" AND lj.SEQ_SESSAO = :idSessao ");
			}

			SQLQuery q = session.createSQLQuery(sql.toString());

			if (sessao != null) {
				q.setLong("idSessao", sessao.getId());
			}

			BigDecimal resultado = (BigDecimal) q.uniqueResult();

			if (resultado != null) {
				return new Long(resultado.longValue());
			}

			return null;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public Sessao recuperarUltimaSessaoEncerrada(Colegiado colegiado) throws DaoException {
		try {
			Session session = retrieveSession();

			StringBuffer hql = new StringBuffer();
			hql.append("SELECT s2 FROM Sessao s2 WHERE s2.dataFim = ");
			hql.append("(SELECT max(s1.dataFim) FROM Sessao s1 ");
			hql.append("WHERE s1.colegiado.id = :colegiado ");
			hql.append(" AND s1.exclusivoDigital ='N'  ");
			hql.append("AND s1.dataFim IS NOT NULL ");
			hql.append("AND s1.tipoAmbiente = :tipoAmbiente");
			hql.append(") AND s2.tipoAmbiente = :tipoAmbiente");

			Query query = session.createQuery(hql.toString());

			query.setParameter("colegiado", colegiado.getId());
			query.setString("tipoAmbiente", TipoAmbienteConstante.PRESENCIAL.getSigla());

			return (Sessao) query.uniqueResult();

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Sessao> pesquisarSessoesVirtuais(TipoColegiadoConstante colegiado, Boolean possuiListaJulgamento,
			Boolean iniciada, Boolean finalizada) throws DaoException {
		Session session = retrieveSession();
		List<Sessao> resultado = null;
		StringBuffer hql = new StringBuffer();

		try {
			hql.append(" SELECT s FROM Sessao s WHERE  s.tipoAmbiente = 'V' ");
			hql.append(" AND s.exclusivoDigital ='N'  ");
			if (iniciada != null) {
				hql.append(" AND s.dataInicio ");
				if (iniciada)
					hql.append(" IS NOT NULL ");
				else
					hql.append(" IS NULL ");
			}
			if (finalizada != null) {
				hql.append(" AND s.dataFim ");
				if (finalizada)
					hql.append(" IS NOT NULL ");
				else
					hql.append(" IS NULL ");
			}
			hql.append(" AND s.tipoJulgamentoVirtual = " + TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO.getId() + " ");

			if (possuiListaJulgamento != null && possuiListaJulgamento)
				hql.append(
						" AND exists (select lj from ListaJulgamento lj where lj.sessao.id = s.id AND lj.publicado = 'N') ");

			if (null != colegiado)
				hql.append(" AND s.colegiado.id = :colegiado ");

			hql.append(" ORDER BY s.dataPrevistaInicio DESC ");

			Query q = session.createQuery(hql.toString());

			if (null != colegiado)
				q.setString("colegiado", colegiado.getSigla());

			q.setMaxResults(10);

			resultado = q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return resultado;

	}

	@SuppressWarnings("unchecked")
	public List<Sessao> pesquisarSessoesVirtuaisIndependePublicado(TipoColegiadoConstante colegiado,
			Boolean possuiListaJulgamento, Boolean iniciada, Boolean finalizada) throws DaoException {
		Session session = retrieveSession();
		List<Sessao> resultado = null;
		StringBuffer hql = new StringBuffer();

		try {
			hql.append(" SELECT s FROM Sessao s WHERE  s.tipoAmbiente = 'V' ");
			if (iniciada != null) {
				hql.append(" AND s.dataInicio ");
				if (iniciada)
					hql.append(" IS NOT NULL ");
				else
					hql.append(" IS NULL ");
			}
			if (finalizada != null) {
				hql.append(" AND s.dataFim ");
				if (finalizada)
					hql.append(" IS NOT NULL ");
				else
					hql.append(" IS NULL ");
			}
			hql.append(" AND s.tipoJulgamentoVirtual = " + TipoJulgamentoVirtual.LISTAS_DE_JULGAMENTO.getId() + " ");

			if (possuiListaJulgamento != null && possuiListaJulgamento)
				hql.append(" AND exists (select lj from ListaJulgamento lj where lj.sessao.id = s.id) ");

			if (null != colegiado)
				hql.append(" AND s.colegiado.id = :colegiado ");

			hql.append(" AND s.exclusivoDigital  ='N' ");

			hql.append(" ORDER BY s.dataPrevistaInicio DESC ");

			Query q = session.createQuery(hql.toString());

			if (null != colegiado)
				q.setString("colegiado", colegiado.getSigla());

			q.setMaxResults(4);

			resultado = q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}

		return resultado;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isProducao() throws HibernateException, SQLException, DaoException {
		return retrieveSession().connection().getMetaData().getURL().toLowerCase().contains("stfp");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sessao> pesquisarSessoesEncerradas(Date dataLimite, String tipoAmbiente) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();

		try {
			hql.append(" SELECT s FROM Sessao s JOIN FETCH s.listaJulgamentoProcesso jp JOIN FETCH jp.textos t JOIN FETCH jp.objetoIncidente.principal p JOIN FETCH jp.processoListaJulgamento plj JOIN FETCH plj.objetoIncidente oi ");
			hql.append(" WHERE s.dataFim IS NULL AND s.exclusivoDigital ='N' ");

			if (tipoAmbiente != null && tipoAmbiente.trim().length() > 0)
				hql.append(" AND s.tipoAmbiente = :tipoAmbienteValor ");

			if (dataLimite != null)
				hql.append(" AND s.dataPrevistaFim < TO_DATE(:dataLimite, 'DD/MM/YYYY HH24:MI:SS')");

			Query q = session.createQuery(hql.toString());

			if (tipoAmbiente != null && tipoAmbiente.trim().length() > 0)
				q.setString("tipoAmbienteValor", tipoAmbiente);

			if (dataLimite != null)
				q.setString("dataLimite", DataUtil.date2String(dataLimite, true));

			return q.list();

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public boolean isExclusivoDigital(ObjetoIncidente oi) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		try {
			hql.append("SELECT s FROM Sessao s JOIN s.listaJulgamentoProcesso jps ");
			hql.append("WHERE s.exclusivoDigital ='S' AND ");

			if (oi != null && oi.getId() != null)
				hql.append("jps.objetoIncidente = :oi ");

			Query q = session.createQuery(hql.toString());

			if (oi != null && oi.getId() != null)
				q.setEntity("oi", oi);

			return !q.list().isEmpty();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
