package br.gov.stf.estf.ministro.model.dataaccess.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.ministro.MinistroPresidente;
import br.gov.stf.estf.entidade.ministro.Ocorrencia;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.ministro.model.dataaccess.MinistroDao;
import br.gov.stf.estf.usuario.model.util.TipoOcorrenciaMinistro;
import br.gov.stf.estf.usuario.model.util.TipoTurma;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.SearchData;

@Repository
public class MinistroDaoHibernate extends GenericHibernateDao<Ministro, Long> implements MinistroDao {

	private static final long serialVersionUID = -3302650624233995092L;

	public MinistroDaoHibernate() {
		super(Ministro.class);
	}

	public Ministro recuperarMinistro(Setor setor) throws DaoException {
		try {
			Criteria c = retrieveSession().createCriteria(Ministro.class);
			c.add(Restrictions.eq("setor.id", setor.getId()));
			c.createAlias("setor", "s", Criteria.LEFT_JOIN);

			return (Ministro) c.uniqueResult();
		} catch (HibernateException e) {
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Ministro> pesquisarMinistros(Boolean ativo, Boolean incluirGabinetePresidencia, MinistroPresidente ministroPresidente, Boolean primeiraTurma,
			Boolean segundaTurma, Boolean sessaoPlenaria) throws DaoException {
		List<Ministro> resultado = null;
		Session session = retrieveSession();

		try {
			/*
			DetachedCriteria subQuery = DetachedCriteria.forClass(MinistroPresidente.class, "mp");			
			subQuery.add( Restrictions.isNull("mp.dataAfastamento") );
			subQuery.add( Restrictions.eq("mp.tipoOcorrencia",TipoOcorrenciaMinistro.MP) );
			subQuery.setProjection( Projections.property("mp.id.ministro") );
			*/

			Criteria c = session.createCriteria(Ministro.class, "m");

			if (ativo != null && ativo.booleanValue())
				c.add(Restrictions.isNull("m.dataAfastamento"));

			if (primeiraTurma != null && primeiraTurma.booleanValue())
				c.add(Restrictions.eq("m.codigoTurma", Ministro.CODIGO_PRIMEIRA_TURMA));

			if (segundaTurma != null && segundaTurma.booleanValue())
				c.add(Restrictions.eq("m.codigoTurma", Ministro.CODIGO_SEGUNDA_TURMA));

			if (sessaoPlenaria != null && sessaoPlenaria.booleanValue())
				c.add(Restrictions.isNotNull("m.codigoTurma"));

			if (incluirGabinetePresidencia != null) {
				if (!incluirGabinetePresidencia.booleanValue()) {
					c.add(Restrictions.ne("m.id", new Long(1)));
					// c.add( Restrictions.isNull("m.codigoTurma"));
					/*
					c = c.createAlias("m.ministroPresidente", "mmp", CriteriaSpecification.LEFT_JOIN).
																		setFetchMode("m", FetchMode.JOIN);					
					c.add( Subqueries.ne("mmp.id.ministro.id", subQuery) );
					*/
				} else if (incluirGabinetePresidencia.booleanValue()) {
					//c.add(Restrictions.eq("m.id", new Long(1)));
				}
			}

			// Elimina o presidente do tribunal das turmas devido inconsistência de dados
			if ((primeiraTurma != null && primeiraTurma.booleanValue()) || (segundaTurma != null && segundaTurma.booleanValue()))
				c.add(Restrictions.ne("m.id", ministroPresidente.getId().getMinistro().getId()));

			c.addOrder(Order.asc("m.dataPosse"));

			resultado = c.list();

		} catch (HibernateException e) {
			throw new DaoException(e);
		}
		return resultado;
	}

	public Ministro recuperarPresidente(Boolean incluirGabinetePresidencia, Boolean primeiraTurma, Boolean segundaTurma, Boolean sessaoPlenaria)
			throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();

		try {

			hql.append(" SELECT m FROM Ministro m ");
			hql.append(" WHERE ");
			hql.append(" m.dataPosse = ( ");
			hql.append(" SELECT MIN(m2.dataPosse) FROM Ministro m2 ");
			hql.append(" where ");
			hql.append(" m2.dataAfastamento IS NULL ");

			if (primeiraTurma != null && primeiraTurma.booleanValue()) {
				hql.append(" AND m2.codigoTurma = ");
				hql.append(Ministro.CODIGO_PRIMEIRA_TURMA);
			}

			if (segundaTurma != null && segundaTurma.booleanValue()) {
				hql.append(" AND m2.codigoTurma = ");
				hql.append(Ministro.CODIGO_SEGUNDA_TURMA);
			}

			if (sessaoPlenaria != null && sessaoPlenaria.booleanValue())
				hql.append(" AND m2.codigoTurma IS NOT NULL ");

			if (incluirGabinetePresidencia != null) {
				if (!incluirGabinetePresidencia.booleanValue()) {
					hql.append(" AND m2.id <> ");
					hql.append(1L);
				} else if (incluirGabinetePresidencia.booleanValue()) {
					hql.append(" AND m2.id = ");
					hql.append(1L);
				}
			}
			hql.append(" ) ");

			Query q = session.createQuery(hql.toString());
			return (Ministro) q.uniqueResult();

		} catch (HibernateException e) {
			throw new DaoException(e);
		}
	}

	public Ministro recuperarMinistro(String nome, Long id) throws DaoException {

		Session session = retrieveSession();

		try {
			Criteria c = session.createCriteria(Ministro.class);
			if (nome != null && nome.trim().length() > 0)
				c.add(Restrictions.like("nome", "%" + nome.toUpperCase() + "%"));

			if (id != null && id > 0)
				c.add(Restrictions.eq("id", id));

			return (Ministro) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Ministro> pesquisarMinistrosAtivos() throws DaoException {
		List<Ministro> ministros = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Ministro.class);
			c.add(Restrictions.isNull("dataAfastamento"));
			ministros = c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return ministros;
	}

	@SuppressWarnings("unchecked")
	public List<Ministro> pesquisarMinistros(String nomeMinistro, Boolean ativo) throws DaoException {
		List<Ministro> ministros = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Ministro.class);
			if (SearchData.stringNotEmpty(nomeMinistro)) {
				nomeMinistro = nomeMinistro.replace("|", "\\|");
				nomeMinistro = nomeMinistro.replace('%', ' ');
				c.add(Restrictions.ilike("nome", nomeMinistro.trim(), MatchMode.ANYWHERE));
			}

			if (ativo != null) {
				if (ativo)
					c.add(Restrictions.isNull("dataAfastamento"));
				else
					c.add(Restrictions.isNotNull("dataAfastamento"));
			}

			c.addOrder(Order.asc("dataPosse"));
			ministros = c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return ministros;
	}

	public List<Ministro> pesquisarMinistro(Long codigoMinistro, String sigla, String nome, Long codigoSetor, TipoTurma tipoTurma, Boolean ativo,
			Boolean semMinistroPresidente) throws DaoException {
		Session session = retrieveSession();

		List<Ministro> usuarios = null;

		try {

			StringBuffer hql = new StringBuffer(" SELECT m " + " FROM Ministro m " + " WHERE 1 = 1 ");

			if (codigoMinistro != null)
				hql.append(" AND m.id = " + codigoMinistro);

			if (sigla != null && sigla.trim().length() > 0)
				hql.append(" AND m.sigla = '" + sigla.toUpperCase() + "'");

			if (SearchData.stringNotEmpty(nome)) {
				nome = nome.replace("|", "\\|");
				nome = nome.replace('%', ' ');
				hql.append(" AND m.nome = '" + nome.trim().toUpperCase() + "'");
			}

			if (codigoSetor != null)
				hql.append(" AND m.setor.id = " + codigoSetor);

			if (tipoTurma != null)
				hql.append(" AND m.tipoTurma = '" + tipoTurma.getCodigo() + "'");

			if (semMinistroPresidente != null && semMinistroPresidente)
				hql.append(" AND m.id <> 1");

			if (ativo != null) {

				if (ativo.booleanValue())
					hql.append(" AND m.dataAfastamento IS NULL");
				else
					hql.append(" AND m.dataAfastamento IS NOT NULL");

			}

			hql.append(" ORDER BY m.nome, m.dataPosse");

			Query query = session.createQuery(hql.toString());

			usuarios = query.list();

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return usuarios;
	}

	public Ministro recuperarMinistroRelator(ObjetoIncidente objetoIncidente) throws DaoException {
		Ministro relator = null;
		try {
			StringBuffer hql = new StringBuffer();

			hql.append(" SELECT smp.ministroRelator ");
			hql.append("   FROM ObjetoIncidente oi, ");
			hql.append("        ObjetoIncidente ppai, ");
			hql.append("        SituacaoMinistroProcesso smp ");
			hql.append("  WHERE oi.principal = ppai.principal ");
			hql.append("    AND ppai = smp.objetoIncidente ");
			hql.append("    AND smp.relatorAtual = :atual ");
			hql.append("    AND oi.id = :objetoIncidente ");

			Query q = retrieveSession().createQuery(hql.toString());
			q.setParameter("objetoIncidente", objetoIncidente.getId());
			q.setString("atual", "S");

			relator = (Ministro) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return relator;
	}
	
	@Override
	public Ministro recuperarRedatorAcordao(ObjetoIncidente objetoIncidente) throws DaoException {

		Ministro relator = null;
		try {
			StringBuffer hql = new StringBuffer();

			List<String> listaOcorrenciaRedator = new ArrayList<String>();

			listaOcorrenciaRedator.add(Ocorrencia.REDATOR_ACORDAO.getCodigo());
			listaOcorrenciaRedator.add(Ocorrencia.REDATOR_ACORDAO_RISTF.getCodigo());
			listaOcorrenciaRedator.add(Ocorrencia.SUBSTITUICAO_TEMPORARIA.getCodigo());

			hql.append(" SELECT smp.ministroRelator ");
			hql.append("   FROM SituacaoMinistroProcesso smp ");
			hql.append("  WHERE smp.ocorrencia IN (:ocorrencia) ");
			hql.append("    AND smp.objetoIncidente.id = :objetoIncidente ");
			hql.append("    AND smp.dataOcorrencia = ");
			hql.append("           (SELECT MAX (smp2.dataOcorrencia) ");
			hql.append("              FROM SituacaoMinistroProcesso smp2 ");
			hql.append("             WHERE smp2.ocorrencia = smp.ocorrencia ");
			hql.append("               AND smp2.objetoIncidente = smp.objetoIncidente) ");

			Query q = retrieveSession().createQuery(hql.toString());
			q.setParameter("objetoIncidente", objetoIncidente.getId());
			q.setParameterList("ocorrencia", listaOcorrenciaRedator);

			relator = (Ministro) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return relator;
	}	

	public Ministro recuperarRelatorAcordao(ObjetoIncidente objetoIncidente) throws DaoException {

		Ministro relator = null;
		try {
			StringBuffer hql = new StringBuffer();

			List<String> listaOcorrenciaRedator = new ArrayList<String>();

			listaOcorrenciaRedator.add(Ocorrencia.REDATOR_ACORDAO.getCodigo());
			listaOcorrenciaRedator.add(Ocorrencia.REDATOR_ACORDAO_RISTF.getCodigo());
			listaOcorrenciaRedator.add(Ocorrencia.SUBSTITUICAO_TEMPORARIA.getCodigo());

			hql.append(" SELECT smp.ministroRelator ");
			hql.append("   FROM SituacaoMinistroProcesso smp ");
			hql.append("  WHERE smp.ocorrencia IN (:ocorrencia) ");
			hql.append("    AND smp.objetoIncidente.id = :objetoIncidente ");
			hql.append("    AND smp.dataOcorrencia = ");
			hql.append("           (SELECT MAX (smp2.dataOcorrencia) ");
			hql.append("              FROM SituacaoMinistroProcesso smp2 ");
			hql.append("             WHERE smp2.ocorrencia = smp.ocorrencia ");
			hql.append("               AND smp2.objetoIncidente = smp.objetoIncidente) ");

			Query q = retrieveSession().createQuery(hql.toString());
			q.setParameter("objetoIncidente", objetoIncidente.getId());
			q.setParameterList("ocorrencia", listaOcorrenciaRedator);

			relator = (Ministro) q.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return relator;
	}

	@Override
	public Ministro recuperarMinistroRelatorIncidente(ObjetoIncidente<?> objetoIncidente) throws DaoException {
		Ministro relator = null;
		try {
			StringBuffer sql = new StringBuffer();

			sql.append(" SELECT * FROM STF.MINISTROS m ");
			sql.append(" WHERE m.COD_MINISTRO = JUDICIARIO.PKG_RELATORIA.FNC_RECUPERA_RELATOR(:idObjetoIncidente)");

			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString()).addEntity(Ministro.class);
			sqlQuery.setLong("idObjetoIncidente", objetoIncidente.getId());

			relator = (Ministro) sqlQuery.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return relator;
	}
	
	@Override
	public Ministro recuperarMinistroRelatorIncidenteDataJulgamento(ObjetoIncidente<?> objetoIncidente) throws DaoException {
		Ministro relator = null;
		try {
			StringBuffer sql = new StringBuffer();

			sql.append("SELECT * ");
			sql.append(" FROM stf.ministros m ");
			sql.append(" WHERE m.cod_ministro = (SELECT cod_ministro ");
			sql.append(" FROM (SELECT y.cod_ministro, ");
			sql.append(" MAX (y.dat_ocorrencia) dat_ocorrencia ");
			sql.append(" FROM (SELECT sm.seq_objeto_incidente, sm.cod_ministro, x.nivel, sm.dat_ocorrencia ");
			sql.append(" FROM (SELECT seq_objeto_incidente, ");
			sql.append(" LEVEL nivel ");
			sql.append(" FROM judiciario.objeto_incidente oi ");
			sql.append(" START WITH oi.seq_objeto_incidente = :idObjetoIncidente ");
			sql.append(" CONNECT BY PRIOR oi.seq_objeto_incidente_pai = oi.seq_objeto_incidente) x, stf.sit_min_processos sm ");
			sql.append(" WHERE sm.cod_ocorrencia IN ('RE', 'SU', 'RG') ");
			sql.append(" AND x.seq_objeto_incidente = sm.seq_objeto_incidente) y ");
			sql.append(" WHERE y.nivel = (SELECT MIN (z.nivel) ");
			sql.append(" FROM (SELECT sm.dat_ocorrencia, sm.seq_objeto_incidente, sm.cod_ministro, x.nivel ");
			sql.append(" FROM (SELECT seq_objeto_incidente, ");
			sql.append(" LEVEL nivel ");
			sql.append(" FROM judiciario.objeto_incidente oi ");
			sql.append(" START WITH oi.seq_objeto_incidente = :idObjetoIncidente ");
			sql.append(" CONNECT BY PRIOR oi.seq_objeto_incidente_pai = oi.seq_objeto_incidente) x, stf.sit_min_processos sm ");
			sql.append(" WHERE x.seq_objeto_incidente = sm.seq_objeto_incidente ");
			sql.append(" AND sm.cod_ocorrencia IN ('RE', 'SU', 'RG') ");
			sql.append(" AND sm.dat_ocorrencia <= TRUNC ((SELECT TRUNC (t.dat_sessao) ");
			sql.append(" FROM stf.textos t ");
			sql.append(" WHERE t.seq_objeto_incidente = :idObjetoIncidente ");
			sql.append(" AND t.cod_tipo_texto = 80 ");
			sql.append(" AND t.flg_publico = 'S') + 1) - 1 / (24 * 60 * 60)) z) ");
			sql.append(" AND y.dat_ocorrencia <= TRUNC ((SELECT TRUNC (t.dat_sessao) ");
			sql.append(" FROM stf.textos t ");
			sql.append(" WHERE t.seq_objeto_incidente = :idObjetoIncidente ");
			sql.append(" AND t.cod_tipo_texto = 80 ");
			sql.append(" AND t.flg_publico = 'S') + 1) - 1 / (24 * 60 * 60) ");
			sql.append(" GROUP BY y.cod_ministro ");
			sql.append(" ORDER BY dat_ocorrencia DESC) ");
			sql.append(" WHERE ROWNUM = 1) ");
			
			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString()).addEntity(Ministro.class);
			sqlQuery.setLong("idObjetoIncidente", objetoIncidente.getId());

			relator = (Ministro) sqlQuery.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return relator;
	}

	@Override
	public Ministro recuperarMinistroRevisorIncidente(Long idObjetoIncidente) throws DaoException {
		Ministro revisor = null;
		try {
			StringBuffer sql = new StringBuffer();

			sql.append(" SELECT * FROM STF.MINISTROS m ");
			sql.append(" WHERE m.COD_MINISTRO = JUDICIARIO.PKG_RELATORIA.FNC_RECUPERA_REVISOR(:idObjetoIncidente)");

			SQLQuery sqlQuery = retrieveSession().createSQLQuery(sql.toString()).addEntity(Ministro.class);
			sqlQuery.setLong("idObjetoIncidente", idObjetoIncidente);

			revisor = (Ministro) sqlQuery.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return revisor;
	}

	@Override
	public Ministro recuperarMinistroPresidente(Date data) throws DaoException {
		try {
			StringBuffer hql = new StringBuffer();

			hql.append("SELECT ministro FROM MinistroPresidente mp ");
			hql.append("JOIN mp.id.ministro ministro ");
			hql.append("WHERE mp.tipoOcorrencia = :tipoOcorrencia ");
			hql.append("AND (mp.dataAfastamento IS NULL OR mp.dataAfastamento >= :data) ");
			hql.append("AND mp.id.dataPosse <= :data ");
			hql.append("ORDER BY mp.id.dataPosse DESC ");

			Query q = retrieveSession().createQuery(hql.toString());
			q.setString("tipoOcorrencia", TipoOcorrenciaMinistro.MP.getSigla());
			q.setDate("data", data);

			List<Ministro> ministros = q.list();
			if (ministros != null && ministros.size() > 0) {
				return ministros.get(0);
			} else {
				return null;
			}
			
		} catch (HibernateException e) {
			throw new DaoException(e);
		}
	}

}
