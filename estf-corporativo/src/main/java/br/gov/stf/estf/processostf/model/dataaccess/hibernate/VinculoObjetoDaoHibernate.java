/**
 * 
 */
package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.TipoVinculoObjeto;
import br.gov.stf.estf.entidade.processostf.VinculoObjeto;
import br.gov.stf.estf.processostf.model.dataaccess.VinculoObjetoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 27.07.2011
 */
@Repository
public class VinculoObjetoDaoHibernate extends GenericHibernateDao<VinculoObjeto, Long>
		implements VinculoObjetoDao {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2567882004581954054L;

	public VinculoObjetoDaoHibernate() {
		super(VinculoObjeto.class);
	}

	@Override
	public List<VinculoObjeto> pesquisarPorVinculador(
			ObjetoIncidente<?> vinculador, TipoVinculoObjeto tipoVinculoObjeto) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(VinculoObjeto.class);
			c.setFetchMode("objetoIncidenteVinculador", FetchMode.JOIN);
			c.setFetchMode("objetoIncidente", FetchMode.JOIN);
			c.add(Restrictions.eq("objetoIncidenteVinculador.id", vinculador.getId()));
			if (tipoVinculoObjeto != null) {
				c.add(Restrictions.eq("tipoVinculoObjeto", tipoVinculoObjeto));
			}
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<VinculoObjeto> pesquisarPorVinculado(
			ObjetoIncidente<?> vinculado, TipoVinculoObjeto tipoVinculoObjeto) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(VinculoObjeto.class);
			c.setFetchMode("objetoIncidenteVinculador", FetchMode.JOIN);
			c.setFetchMode("objetoIncidente", FetchMode.JOIN);
			c.add(Restrictions.eq("objetoIncidente.id", vinculado.getId()));
			if (tipoVinculoObjeto != null) {
				c.add(Restrictions.eq("tipoVinculoObjeto", tipoVinculoObjeto));
			}
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<ObjetoIncidente<?>> recuperarObjetosIncidenteVinculados(
			ObjetoIncidente<?> vinculador, TipoVinculoObjeto tipoVinculoObjeto)
			throws DaoException {
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer(" SELECT oi FROM VinculoObjeto vo, ");
			hql.append(" ObjetoIncidente oi ");
			hql.append(" join fetch oi.principal ");
			hql.append(" WHERE vo.objetoIncidente.id = oi.id ");
			if (vinculador != null) {
				hql.append(" AND vo.objetoIncidenteVinculador.id = :seqObjetoIncidente ");
			}
			if (tipoVinculoObjeto != null) {
				hql.append(" AND vo.tipoVinculoObjeto = :tipoVinculoObjeto ");
			}
			
			Query q = session.createQuery(hql.toString());
			
			if (vinculador != null) {
				q.setLong("seqObjetoIncidente", vinculador.getId());
			}
			if (tipoVinculoObjeto != null) {
				q.setLong("tipoVinculoObjeto", tipoVinculoObjeto.getCodigo());
			}
			return (List<ObjetoIncidente<?>>) q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
