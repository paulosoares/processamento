package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.ministro.Ocorrencia;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.SituacaoMinistroProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.SituacaoMinistroProcessoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class SituacaoMinistroProcessoDaoHibernate extends GenericHibernateDao<SituacaoMinistroProcesso, Long> implements SituacaoMinistroProcessoDao {

	public SituacaoMinistroProcessoDaoHibernate() {
		super(SituacaoMinistroProcesso.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5337516491251729601L;

	@SuppressWarnings("unchecked")
	public List<SituacaoMinistroProcesso> pesquisar(String[] codOcorrencia, String siglaProcesso, Long numProcesso, Long codRecurso, String tipoJulgamento,
			Boolean orderByDataOcorrenciaDesc) throws DaoException {
		List<SituacaoMinistroProcesso> res = null;

		try {
			Session session = retrieveSession();

			Criteria c = session.createCriteria(SituacaoMinistroProcesso.class);

			if (codOcorrencia != null && codOcorrencia.length > 0) {
				c.add(Restrictions.in("id.codigoOcorrencia", codOcorrencia));
			}
			if (siglaProcesso != null && siglaProcesso.trim().length() > 0) {
				c.add(Restrictions.eq("id.siglaClasseProcessual", siglaProcesso));
			}
			if (numProcesso != null) {
				c.add(Restrictions.eq("id.numeroProcessual", numProcesso));
			}
			if (codRecurso != null) {
				c.add(Restrictions.eq("id.codigoRecurso", codRecurso));
			}

			if (tipoJulgamento != null && tipoJulgamento.trim().length() > 0) {
				c.add(Restrictions.eq("tipoJulgamento", tipoJulgamento));
			}

			if (orderByDataOcorrenciaDesc) {
				c.addOrder(Order.desc("id.dataOcorrencia"));
			}

			res = c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return res;
	}

	public Ministro recuperarMinistroRelatorAtual(String siglaClasse, Long numeroProcesso) throws DaoException {
		try {
			Session session = retrieveSession();

			Criteria c = session.createCriteria(SituacaoMinistroProcesso.class);
			c.add(Restrictions.eq("classeProcessual.id", siglaClasse));
			c.add(Restrictions.eq("numeroProcessual", numeroProcesso));
			c.add(Restrictions.eq("relatorAtual", true));

			SituacaoMinistroProcesso situacaoMinistroProcesso = (SituacaoMinistroProcesso) c.uniqueResult();
			return situacaoMinistroProcesso.getMinistroRelator();

		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	public Ministro recuperarMinistroRelatorAtual(Processo processo) throws DaoException {
		Ministro relator = null;
		try {
			StringBuffer sql = new StringBuffer();

			sql.append(" SELECT * FROM STF.MINISTROS m ");
			sql.append(" WHERE m.COD_MINISTRO = JUDICIARIO.PKG_RELATORIA.FNC_RECUPERA_RELATOR(:idObjetoIncidente)");

			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString()).addEntity(Ministro.class);
			sqlQuery.setLong("idObjetoIncidente", processo.getId());

			relator = (Ministro) sqlQuery.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return relator;

	}

	public Ministro recuperarMinistroRelatorAtual(ObjetoIncidente<?> objetoIncidente) throws DaoException {
		StringBuffer hql = new StringBuffer();
		hql.append("SELECT m ");
		hql.append("FROM SituacaoMinistroProcesso smp, Processo p,Ministro m ");
		hql.append("WHERE  p.id = smp.objetoIncidente.principal.id ");
		hql.append("AND  smp.ministroRelator.id = m.id ");
		hql.append("AND  smp.relatorAtual = 'S' ");
		hql.append("AND p.id = " + objetoIncidente.getPrincipal().getId());

		Session session = retrieveSession();
		Query query = session.createQuery(hql.toString());

		return (Ministro) query.uniqueResult();

	}

	@Override
	public List<SituacaoMinistroProcesso> pesquisar(ObjetoIncidente<?> objetoIncidente, Ocorrencia ocorrencia) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(SituacaoMinistroProcesso.class);
			c.createAlias("objetoIncidente", "oi");
			
			if (ocorrencia != null)
				c.add(Restrictions.eq("ocorrencia", ocorrencia));
			
			c.add(Restrictions.eq("oi.principal.id", objetoIncidente.getPrincipal().getId()));
			c.addOrder(Order.asc("dataOcorrencia"));
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void remover(ObjetoIncidente<?> objetoIncidente) throws DaoException {
		Session session = retrieveSession();
		String sql = "DELETE FROM STF.SIT_MIN_PROCESSOS WHERE SEQ_OBJETO_INCIDENTE = :objetoIncidente";

		Query query = session.createSQLQuery(sql);
		query.setLong("objetoIncidente", objetoIncidente.getId());

		query.executeUpdate();
	}
}
