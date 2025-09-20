/**
 * 
 */
package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoObjetoIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.PreListaJulgamentoObjetoIncidenteDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class PreListaJulgamentoObjetoIncidenteDaoHibernate extends GenericHibernateDao<PreListaJulgamentoObjetoIncidente, Long>
		implements PreListaJulgamentoObjetoIncidenteDao {

	private static final long serialVersionUID = -1284591112480055967L;

	public PreListaJulgamentoObjetoIncidenteDaoHibernate() {
		super(PreListaJulgamentoObjetoIncidente.class);
	}

	@Override
	public PreListaJulgamentoObjetoIncidente pesquisarPorObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		PreListaJulgamentoObjetoIncidente result = null;

		Session session;
		try {
			session = retrieveSession();

			StringBuilder hql = new StringBuilder(" SELECT c FROM PreListaJulgamentoObjetoIncidente c ");
			hql.append(" JOIN c.preListaJulgamento p");
			hql.append(" WHERE rownum =1 ");

			if (objetoIncidente != null && objetoIncidente.getId() != null) {
				hql.append(" AND c.objetoIncidente.id = " + objetoIncidente.getId());
			}

			Query q = session.createQuery(hql.toString());
			result = (PreListaJulgamentoObjetoIncidente) q.uniqueResult();

		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public List<PreListaJulgamentoObjetoIncidente> pesquisarProcessoEmLista(ObjetoIncidente<?> objetoIncidente,
			PreListaJulgamento preListaJulgamento) {
		List<PreListaJulgamentoObjetoIncidente> listaRetorno = null;

		Session session;
		try {
			session = retrieveSession();

			StringBuilder hql = new StringBuilder(" SELECT c FROM PreListaJulgamentoObjetoIncidente c ");
			hql.append(" JOIN c.preListaJulgamento p");
			hql.append(" WHERE 1=1 ");

			if (objetoIncidente != null && objetoIncidente.getId() != null) {
				hql.append(" AND c.objetoIncidente.id = " + objetoIncidente.getId());
			}
			
			if (preListaJulgamento != null && preListaJulgamento.getId() != null) {
				hql.append(" AND c.preListaJulgamento.id = " + preListaJulgamento.getId());
			}

			Query q = session.createQuery(hql.toString());
			listaRetorno = q.list();

		} catch (DaoException e) {
			e.printStackTrace();
		}

		return listaRetorno;
	}

}
