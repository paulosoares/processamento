package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.estf.processostf.model.dataaccess.ProtocoloDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ProtocoloDaoHibernate extends GenericHibernateDao<Protocolo, Long> implements ProtocoloDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5950477702941244152L;

	public ProtocoloDaoHibernate() {
		super(Protocolo.class);
	}

	public void removerObjetoSessaoHibernate(Object objeto, boolean executarFlush) {

		Session session;
		try {
			session = retrieveSession();

			if (executarFlush)
				session.flush();

			session.evict(objeto);
		} catch (DaoException e) {
			e.printStackTrace();
		}

	}

	public void persistirProtocolo(Protocolo protocolo) throws DaoException {
		Session session = retrieveSession();

		try {
			session.save(protocolo);
			session.flush();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

	}

	/**
	 * @see br.gov.stf.estf.processostf.model.dataaccess.IDaoProtocolo#gerarProtocoloAnoNumero()
	 */
	public Protocolo gerarProtocoloAnoNumero() throws DaoException {
		String sql = "{? = call stf.FNC_RETORNA_ANO_NUM_PETICAO(?,?)}";
		Session session = retrieveSession();
		Connection con = session.connection();
		Protocolo protocolo = new Protocolo();
		try {
			CallableStatement cs = con.prepareCall(sql);
			java.util.Date data = new java.util.Date();
			cs.registerOutParameter(1, java.sql.Types.NUMERIC);
			cs.setDate(2, new java.sql.Date(data.getTime()));
			cs.setString(3, "PI"); // PETIÇÃO INICIAL - Protocolo
			cs.execute();
			String retorno = cs.getString(1);
			protocolo.setAnoProtocolo(Short.valueOf(retorno.substring(0, 4)));
			protocolo.setNumeroProtocolo(Long.valueOf(retorno.substring(4)));
			cs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException("Exceção ao retornar o número do Protocolo: " + e.getMessage() + ", número erro: "
					+ e.getErrorCode());
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return protocolo;
	}

	public Protocolo recuperarProtocolo(Long numero, Short ano) throws DaoException {
		Session session = retrieveSession();

		Protocolo protocolo = null;

		try {
			Criteria criteria = session.createCriteria(Protocolo.class);
			criteria.add(Restrictions.eq("numero", numero));
			criteria.add(Restrictions.eq("ano", ano));

			protocolo = (Protocolo) criteria.uniqueResult();
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return protocolo;
	}

	/**
	 * @see br.gov.stf.estf.processostf.model.dataaccess.IDaoProtocolo#recuperarProtocolo(java.lang.String,
	 *      java.lang.String, java.lang.Integer)
	 */
	public Protocolo recuperarProtocolo(String siglaClasseProcedencia, String numeroProcessoProcedencia, Integer codigoOrigem)
			throws DaoException {

		Session session = retrieveSession();

		Protocolo protocolo = null;

		StringBuffer st = new StringBuffer();
		st.append(" FROM Protocolo p ");
		st.append(" WHERE (1=1) ");

		if (numeroProcessoProcedencia != null && numeroProcessoProcedencia.trim().length() > 0) {
			st.append(" AND p.numeroProcessoProcedencia = '" + numeroProcessoProcedencia + "'");
		}

		if (siglaClasseProcedencia != null && siglaClasseProcedencia.trim().length() > 0) {
			st.append(" AND p.siglaClasseProcedencia = '" + siglaClasseProcedencia + "'");
		}

		if (codigoOrigem != null && codigoOrigem > -1) {
			st.append(" AND p.codigoOrigem = " + codigoOrigem);
		}

		st.append(" ORDER BY DAT_ENTRADA_PROTOC DESC");

		try {
			List lista = session.createQuery(st.toString()).list();

			if (lista != null && lista.size() > 0) {
				protocolo = (Protocolo) lista.get(0);
			}
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return protocolo;
	}

	public List<Protocolo> recuperarProtocolo(Integer codigoOrigem, String siglaClasseProcedencia,
			String numeroProcessoProcedencia, String siglaClasse, Long numeroProcesso, Long numeroProtocolo, Short anoProtocolo,
			String tipoMeioFisico, Date dataInicial, Date dataFinal) throws DaoException {

		try {
			Session session = retrieveSession();
			Criteria criteria = session.createCriteria(Protocolo.class);

			criteria.setMaxResults(50);

			criteria.add(Restrictions.eq("sigiloso", false));

			if (tipoMeioFisico != null) {
				criteria.add(Restrictions.eq("tipoMeio", tipoMeioFisico));
			}

			if (codigoOrigem != null) {
				criteria.add(Restrictions.eq("codigoOrigem", codigoOrigem));
			}

			if (siglaClasseProcedencia != null) {
				criteria.add(Restrictions.eq("siglaClasseProcedencia", siglaClasseProcedencia));
			}

			if (numeroProcessoProcedencia != null) {
				criteria.add(Restrictions.eq("numeroProcessoProcedencia", numeroProcessoProcedencia));
			}

			Criteria criteriaProcesso = criteria;
			criteriaProcesso = criteria.createCriteria("processo");
			if (siglaClasse != null) {

				criteriaProcesso.createCriteria("classeProcessual").add(Restrictions.eq("siglaClasse", siglaClasse));

			}
			if (numeroProcesso != null) {
				criteriaProcesso.add(Restrictions.eq("numeroProcessual", numeroProcesso));

			}

			if (numeroProtocolo != null) {
				criteria.add(Restrictions.eq("numero", numeroProtocolo));
			}

			if (anoProtocolo != null) {
				criteria.add(Restrictions.eq("ano", anoProtocolo));
			}

			if (dataInicial != null || dataFinal != null) {

				String dataInicialFormatada;
				if (dataInicial != null) {
					dataInicialFormatada = new SimpleDateFormat("dd/MM/yyyy").format(dataInicial);
					Date d1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicialFormatada + " 00:00:00");
					criteria.add(Restrictions.ge("dataEntrada", d1));
				}

				String dataFinalFormatada;
				if (dataFinal != null) {
					dataFinalFormatada = new SimpleDateFormat("dd/MM/yyyy").format(dataFinal);
					Date d2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFinalFormatada + " 23:59:59");
					criteria.add(Restrictions.le("dataEntrada", d2));

				}
			}
			List<Protocolo> protocolos = criteria.list();

			return protocolos;

		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (Exception e) {
			throw new DaoException("RuntimeException", e);
		}
	}
	
	public List<Protocolo> recuperar(String tipoMeioProcesso) throws DaoException {
		List<Protocolo> lista = null;
		Session session = retrieveSession();
		
		try {
			
			Criteria c = session.createCriteria(Protocolo.class, "pt");
			c.add(Restrictions.eq("pt.tipoMeioProcesso", tipoMeioProcesso));
			lista = c.list();
			
		} catch (HibernateException e) {
			throw new DaoException("HibernateException", SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (Exception e) {
			throw new DaoException("RuntimeException", e);
		}
		
		return lista;
	}
}