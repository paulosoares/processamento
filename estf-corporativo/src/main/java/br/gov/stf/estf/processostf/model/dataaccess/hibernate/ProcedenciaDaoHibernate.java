package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.Orgao;
import br.gov.stf.estf.entidade.processostf.Procedencia;
import br.gov.stf.estf.entidade.processostf.TipoHistorico;
import br.gov.stf.estf.processostf.model.dataaccess.ProcedenciaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ProcedenciaDaoHibernate extends GenericHibernateDao<Procedencia, Long> implements ProcedenciaDao {
	public ProcedenciaDaoHibernate() {
		super(Procedencia.class);
	}

	private static final long serialVersionUID = 1L;

	public List pesquisarProcedencia() throws DaoException {

		Session session = retrieveSession();

		List classes = null;

		try {

			Query query = (Query) session.createQuery("FROM Procedencia");
			classes = ((org.hibernate.Query) query).list();
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return classes;

	}

	public String pesquisarProcedencia(Long objetoIncidenteId) throws DaoException {

		Session session = retrieveSession();
		String sql = "SELECT pr.sig_procedencia as sig_procedencia " + "FROM judiciario.historico_processo_origem hpo, " + "judiciario.procedencia pr "
				+ "WHERE hpo.cod_procedencia = pr.cod_procedencia(+) " + "AND hpo.seq_objeto_incidente = :idObjetoIncidente " + "AND (hpo.tip_historico = '"
				+ TipoHistorico.ORIGEM.getCodigo() + "' OR hpo.flg_principal = '" + TipoHistorico.PROCESSO.getCodigo() + "') " + "ORDER BY flg_principal DESC";

		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("idObjetoIncidente", objetoIncidenteId);
		List procedencia = query.list();

		return procedencia.size() == 0 ? "" : (String) query.list().get(0);
	}

	@Override
	public List<Procedencia> pesquisarProcedenciasAtivas() throws DaoException {

		Session session = retrieveSession();

		Criteria criteria = session.createCriteria(Procedencia.class);
		criteria.add(Restrictions.eq("ativo", true));
		criteria.addOrder(Order.asc("siglaProcedencia"));

		return criteria.list();
	}

	@Override
	public List<Procedencia> pesquisarProcedenciasComOrigemAtiva(Orgao orgao) throws DaoException {

		Session session = retrieveSession();
		String hql = "select distinct p from Orgao o, RegiaoOrgao ro, RegiaoProcedencia rp,  Origem or, Procedencia p where " +
					 "o.id = :idOrgao and o.id = ro.orgao and ro.id = rp.regiaoOrgao and ro.id = or.regiaoOrgao " + 
					 " and or.ativo = 'S' and rp.procedencia = p.id order by p.siglaProcedencia";
		
		org.hibernate.Query query = session.createQuery(hql);
		query.setLong("idOrgao", orgao.getId());
		
		List<Procedencia> procedencias = query.list();  
		
		return procedencias;
	}


	@Override
	public List<Procedencia> pesquisarProcedenciasDescricaoAtivas(String descricao) throws DaoException {

		Session session = retrieveSession();

		Criteria criteria = session.createCriteria(Procedencia.class);
		criteria.add(Restrictions.eq("ativo", true));
		criteria.add(Restrictions.eq("descricao", descricao));
		criteria.addOrder(Order.asc("siglaProcedencia"));

		return criteria.list();
	}
}
