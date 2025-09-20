package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.ProcessoListaJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.julgamento.model.dataaccess.ProcessoListaJulgamentoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ProcessoListaJulgamentoDaoHibernate extends GenericHibernateDao<ProcessoListaJulgamento, Long> implements ProcessoListaJulgamentoDao {

	private static final long serialVersionUID = -1284591112480055967L;

	public ProcessoListaJulgamentoDaoHibernate() {
		super(ProcessoListaJulgamento.class);
	}

	public ProcessoListaJulgamento recuperar(ObjetoIncidente<?> objetoIncidente) throws DaoException {
		ProcessoListaJulgamento retorno = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ProcessoListaJulgamento.class);
			if (objetoIncidente != null) {
				c.add(Restrictions.eq("objetoIncidente", objetoIncidente));
			}
			retorno = (ProcessoListaJulgamento) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return retorno;
	}

	@Override
	public ProcessoListaJulgamento recuperarProcessoListaJulgamento(ObjetoIncidente<?> incidente, ListaJulgamento listaJulgamento) throws DaoException {
		Session session = retrieveSession();
		StringBuilder hql = new StringBuilder();

		hql.append(" Select plj FROM ProcessoListaJulgamento plj ");
		hql.append(" WHERE plj.listaJulgamento.id  = " + listaJulgamento.getId() + " ");
		hql.append(" AND plj.objetoIncidente.id = " + incidente.getId());

		Query q = session.createQuery(hql.toString());

		return (ProcessoListaJulgamento) q.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProcessoListaJulgamento> listarProcessos(ListaJulgamento listaJulgamento) throws DaoException {
		List<ProcessoListaJulgamento> listaRetorno = null;

		Session session;
		try {
			session = retrieveSession();

			StringBuilder hql = new StringBuilder("SELECT c FROM ProcessoListaJulgamento c, ObjetoIncidente oi ");
			hql.append(" WHERE c.objetoIncidente.id = oi.id and c.listaJulgamento = " + listaJulgamento.getId());
			hql.append(" ORDER BY c.ordemNaLista");

			Query q = session.createQuery(hql.toString());
			listaRetorno = (List<ProcessoListaJulgamento>) q.list();
			
			for (ProcessoListaJulgamento plj : listaRetorno)
				Hibernate.initialize(plj.getManifestacoes());

		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listaRetorno;
	}
}
