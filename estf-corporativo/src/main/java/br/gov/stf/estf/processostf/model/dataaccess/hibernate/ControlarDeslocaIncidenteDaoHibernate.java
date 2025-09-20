package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.ControlarDeslocaIncidente;
import br.gov.stf.estf.processostf.model.dataaccess.ControlarDeslocaIncidenteDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ControlarDeslocaIncidenteDaoHibernate extends GenericHibernateDao<ControlarDeslocaIncidente, Long> implements ControlarDeslocaIncidenteDao {

	public ControlarDeslocaIncidenteDaoHibernate() {
		super(ControlarDeslocaIncidente.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// grava um objeto incidente na tabela temporária que servirá como insumo
	// para a pkg_desloca_incidente realizar o deslocamento.
	@Override
	public void insereObjetoIncidente(ControlarDeslocaIncidente controlarDeslocaIncidente) throws DaoException {

		String sql = "INSERT INTO JUDICIARIO.CONTROLAR_DESLOCA_INCIDENTE (SEQ_OBJETO_INCIDENTE) VALUES (:idObjetoIncidente)";
		Session session = retrieveSession();
		Query query = session.createSQLQuery(sql);
		query.setLong("idObjetoIncidente", controlarDeslocaIncidente.getId());
		query.executeUpdate();
	}
}
