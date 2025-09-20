package br.gov.stf.estf.jurisdicionado.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisdicionado.ObjetoIncidenteEnderecoJurisdicionado;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.ObjetoIncidenteEnderecoJurisdicionadoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ObjetoIncidenteEnderecoJurisdicionadoDaoHibernate extends GenericHibernateDao<ObjetoIncidenteEnderecoJurisdicionado, Long>
	implements ObjetoIncidenteEnderecoJurisdicionadoDao {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ObjetoIncidenteEnderecoJurisdicionadoDaoHibernate() {
		super(ObjetoIncidenteEnderecoJurisdicionado.class);
	}
	
	@Override
	public List<ObjetoIncidenteEnderecoJurisdicionado> recuperarEnderecos(Long seqEnderecoJurisdicionado) throws DaoException {
		String hql = "from ObjetoIncidenteEnderecoJurisdicionado o where o.seqEnderecoJurisdicionado = " + seqEnderecoJurisdicionado;
		Session session = retrieveSession();
		return session.createQuery(hql).list();
	}


}
