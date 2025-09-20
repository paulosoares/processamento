package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia.TipoProcessoDependenciaEnum;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoDependenciaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ProcessoDependenciaDaoHibernate extends GenericHibernateDao<ProcessoDependencia, Long> implements ProcessoDependenciaDao {

	private static final long serialVersionUID = 3444401287784692429L;

	public ProcessoDependenciaDaoHibernate() {
		super(ProcessoDependencia.class);
	}
	
	@Override
	public boolean isApenso(Processo processo) throws DaoException {
		Session session = retrieveSession();
		Long count = new Long(0);
		try {

			String hql = "select count(*) from ProcessoDependencia p " +
					     "where p.idObjetoIncidente = " + processo.getId() +
					     " and p.tipoDependenciaProcesso in (5,9)" +
					     " and p.dataFimDependencia is null";
			
			Query query = session.createQuery(hql.toString());
			if (query == null) {
				throw new DaoException("Erro ao verificar se o processo é um apenso");
			} else {
				count = (Long) query.uniqueResult();
			}
			if (count > 0) {
				return true;
			} else {
				return false;
			}

		} catch (HibernateException e) {
			throw new DaoException("Erro ao verificar se o processo é um apenso");
		}
	}

	@Override
	public List<ProcessoDependencia> pesquisarDependencias(AndamentoProcesso andamentoProcesso) throws DaoException {

		Session session = retrieveSession();

		try {
			Criteria criteria = session.createCriteria(ProcessoDependencia.class);

			criteria.add(Restrictions.eq("andamentoProcesso", andamentoProcesso.getId()));

			return criteria.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		}
	}

	// método que deverá retornar uma lista de pendencias apenas dos apensados,
	// ou seja do tipo "Apensado ao Processo nº" - cod_tipo_dependencia_processo = 5
	public List<ProcessoDependencia> recuperarApensos(Processo processo) throws DaoException {

		List<ProcessoDependencia> dependencias = Collections.emptyList();

		try {
			Session session = retrieveSession();
			/*			Collection
						Criteria criteria = session.createCriteria(ProcessoDependencia.class);
						criteria.add(Restrictions.eq("numeroProcessoVinculador", processo.getNumeroProcessual()));
						criteria.add(Restrictions.eq("classeProcessoVinculador", processo.getClasseProcessual()));
						criteria.add(Restrictions.in("tipoDependenciaProcesso", 5,9);
			*/
			// dependencias = (List<ProcessoDependencia>) criteria.list();

			SQLQuery query = session.createSQLQuery(
					"SELECT * " + "  FROM stf.processo_dependencia pd" + " WHERE     pd.cod_tipo_dependencia_processo in (5,9) "
							+ "       AND pd.sig_classe_vinculador = '" + processo.getSiglaClasseProcessual() + "'"
							+ "       AND pd.num_processo_vinculador = " + processo.getNumeroProcessual() + "       AND pd.dat_fim_dependencia IS NULL")
					.addEntity(ProcessoDependencia.class);
			dependencias = (List<ProcessoDependencia>) query.list();

		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e);
		}

		return dependencias;
	}

	@Override
	public List<ProcessoDependencia> recuperarTodosApensos(Processo processo) throws DaoException {

		List<Long> tiposDependencia = new ArrayList<Long>();
		tiposDependencia.add(TipoProcessoDependenciaEnum.APENSADO_AO_PROCESSO_NO.getCodigo());
		tiposDependencia.add(TipoProcessoDependenciaEnum.AGRAVO_DE_INSTRUMENTO_APENSADO_AO_RE_NO.getCodigo());

		Session session = retrieveSession();

		Criteria criteria = session.createCriteria(ProcessoDependencia.class);
		criteria.add(Restrictions.in("tipoDependenciaProcesso", tiposDependencia));
		criteria.add(Restrictions.eq("classeProcessoVinculador", processo.getSiglaClasseProcessual()));
		criteria.add(Restrictions.eq("numeroProcessoVinculador", processo.getNumeroProcessual()));
		criteria.add(Restrictions.isNull("dataFimDependencia"));

		return (List<ProcessoDependencia>) criteria.list();
	}

	@Override
	public List<ProcessoDependencia> recuperarTodosApensadosAo(Processo processo) throws DaoException {

		List<Long> tiposDependencia = new ArrayList<Long>();
		tiposDependencia.add(TipoProcessoDependenciaEnum.APENSADO_AO_PROCESSO_NO.getCodigo());
		tiposDependencia.add(TipoProcessoDependenciaEnum.AGRAVO_DE_INSTRUMENTO_APENSADO_AO_RE_NO.getCodigo());

		Session session = retrieveSession();

		Criteria criteria = session.createCriteria(ProcessoDependencia.class);
		criteria.add(Restrictions.in("tipoDependenciaProcesso", tiposDependencia));
		criteria.add(Restrictions.eq("classeProcesso", processo.getSiglaClasseProcessual()));
		criteria.add(Restrictions.eq("numeroProcesso", processo.getNumeroProcessual()));
		criteria.add(Restrictions.isNull("dataFimDependencia"));

		return (List<ProcessoDependencia>) criteria.list();
	}

	@Override
	public Integer getQuantidadeVinculados(Processo processo) throws DaoException {

		List<Long> tiposDependencia = new ArrayList<Long>();
		tiposDependencia.add(TipoProcessoDependenciaEnum.APENSADO_AO_PROCESSO_NO.getCodigo());
		tiposDependencia.add(TipoProcessoDependenciaEnum.AGRAVO_DE_INSTRUMENTO_APENSADO_AO_RE_NO.getCodigo());

		String sql = "select count(*) from stf.processo_dependencia dp " +
				"where dp.COD_TIPO_DEPENDENCIA_PROCESSO in (:tiposDependencia) " +
				"and dp.SIG_CLASSE_VINCULADOR = :classeProcessoVinculador " +
				"and dp.NUM_PROCESSO_VINCULADOR = :numeroProcessoVinculador " +
				"and dp.DAT_FIM_DEPENDENCIA is null";

		Session session = retrieveSession();
		Query query = session.createSQLQuery(sql);
		query.setParameterList("tiposDependencia", tiposDependencia);
		query.setParameter("classeProcessoVinculador", processo.getSiglaClasseProcessual());
		query.setParameter("numeroProcessoVinculador", processo.getNumeroProcessual());

		Number number = (Number) query.uniqueResult();

		return number != null ? number.intValue() : 0;
	}

	// este método está sendo utilizado para recuperar os apensos de um processo principal
	@Override
	public ProcessoDependencia getProcessoVinculador(Processo processo) throws DaoException {

		List<Long> tiposDependencia = new ArrayList<Long>();
		tiposDependencia.add(TipoProcessoDependenciaEnum.APENSADO_AO_PROCESSO_NO.getCodigo());
		tiposDependencia.add(TipoProcessoDependenciaEnum.AGRAVO_DE_INSTRUMENTO_APENSADO_AO_RE_NO.getCodigo());

		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(ProcessoDependencia.class);

		criteria.add(Restrictions.in("tipoDependenciaProcesso", tiposDependencia));
		criteria.add(Restrictions.eq("classeProcesso", processo.getSiglaClasseProcessual()));
		criteria.add(Restrictions.eq("numeroProcesso", processo.getNumeroProcessual()));
		criteria.add(Restrictions.isNull("dataFimDependencia"));
		if (criteria.list().size() == 0) {
			return null;
		} else {
			return (ProcessoDependencia) criteria.list().get(0);
		}
	}

	@Override
	public boolean isPeticaoJuntada(Processo processo, Peticao peticao) throws DaoException {

		Session session = retrieveSession();

		try {
			Criteria criteria = session.createCriteria(ProcessoDependencia.class);

			criteria.add(Restrictions.eq("tipoDependenciaProcesso", TipoProcessoDependenciaEnum.JUNTADA.getCodigo()));
			criteria.add(Restrictions.eq("idObjetoIncidente", processo.getId()));
			criteria.add(Restrictions.eq("idObjetoIncidenteVinculado", peticao.getId()));
			criteria.add(Restrictions.isNull("dataFimDependencia"));

			return !criteria.list().isEmpty();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		}
	}

	@Override
	public boolean isProcessoApensado(Processo processo, Processo processoPrincipal) throws DaoException {

		Session session = retrieveSession();

		try {
			Criteria criteria = session.createCriteria(ProcessoDependencia.class);

			List<Long> tiposDependencia = new ArrayList<Long>();
			tiposDependencia.add(TipoProcessoDependenciaEnum.APENSADO_AO_PROCESSO_NO.getCodigo());
			tiposDependencia.add(TipoProcessoDependenciaEnum.AGRAVO_DE_INSTRUMENTO_APENSADO_AO_RE_NO.getCodigo());
			criteria.add(Restrictions.in("tipoDependenciaProcesso", tiposDependencia));
			criteria.add(Restrictions.eq("idObjetoIncidente", processo.getId()));
			criteria.add(Restrictions.eq("idObjetoIncidenteVinculado", processoPrincipal.getId()));
			criteria.add(Restrictions.isNull("dataFimDependencia"));

			return !criteria.list().isEmpty();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		}
	}

	@Override
	public void finalizarApenso(Processo processo, Processo processoPrincipal) throws DaoException {

		Session session = retrieveSession();

		List<Long> tiposDependencia = new ArrayList<Long>();
		tiposDependencia.add(ProcessoDependencia.TipoProcessoDependenciaEnum.APENSADO_AO_PROCESSO_NO.getCodigo());
		tiposDependencia.add(ProcessoDependencia.TipoProcessoDependenciaEnum.AGRAVO_DE_INSTRUMENTO_APENSADO_AO_RE_NO.getCodigo());

		String sql = "update STF.PROCESSO_DEPENDENCIA dp set dp.DAT_FIM_DEPENDENCIA = :data where dp.sig_classe_proces = :classeProcesso and dp.num_processo = :numeroProcesso and "
				+ "dp.sig_classe_vinculador = :classeProcessoVinculador and dp.num_processo_vinculador = :numeroProcessoVinculador and dp.dat_fim_dependencia is null and "
				+ "dp.cod_tipo_dependencia_processo in (:tiposDependencia)";

		Query query = session.createSQLQuery(sql);
		query.setDate("data", new Date());
		query.setString("classeProcesso", processo.getSiglaClasseProcessual());
		query.setLong("numeroProcesso", processo.getNumeroProcessual());
		query.setString("classeProcessoVinculador", processoPrincipal.getSiglaClasseProcessual());
		query.setLong("numeroProcessoVinculador", processoPrincipal.getNumeroProcessual());
		query.setParameterList("tiposDependencia", tiposDependencia);

		int rows = query.executeUpdate();
	}

	@Override
	public void finalizarSobrestamentos(AndamentoProcesso andamentoProcessoVinculador) throws DaoException {

		Session session = retrieveSession();

		String sql = "UPDATE STF.PROCESSO_DEPENDENCIA pd set pd.DAT_FIM_DEPENDENCIA = :data, pd.SEQ_ANDAMENTO_DESVINCULADOR = :idAndamentoProcesso "
				+ "WHERE pd.SEQ_OBJETO_INCIDENTE_VINC = :idProcesso and pd.COD_TIPO_DEPENDENCIA_PROCESSO = :tipoSobrestamento and pd.DAT_FIM_DEPENDENCIA is null";

		Query query = session.createSQLQuery(sql);
		query.setDate("data", new Date());
		query.setLong("idAndamentoProcesso", andamentoProcessoVinculador.getId());
		query.setLong("idProcesso", andamentoProcessoVinculador.getObjetoIncidente().getId());
		query.setLong("tipoSobrestamento", ProcessoDependencia.TipoProcessoDependenciaEnum.SOBRESTADO.getCodigo());

		query.executeUpdate();
	}
}
