/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.IncidenteAnalise;
import br.gov.stf.estf.entidade.jurisprudencia.ObservacaoLivreJurisprudencia;
import br.gov.stf.estf.entidade.jurisprudencia.TipoOrdenacaoObservacaoJurisprudencia;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.ObservacaoLivreJurisprudenciaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 08.10.2012
 */
@Repository
public class ObservacaoLivreJurisprudenciaDaoHibernate extends GenericHibernateDao<ObservacaoLivreJurisprudencia, Long> implements
		ObservacaoLivreJurisprudenciaDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -175013514534298427L;

	public ObservacaoLivreJurisprudenciaDaoHibernate() {
		super(ObservacaoLivreJurisprudencia.class);
	}

	@Override
	public ObservacaoLivreJurisprudencia recuperar(IncidenteAnalise incidenteAnalise,
			TipoOrdenacaoObservacaoJurisprudencia tipoOrdenacao) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ObservacaoLivreJurisprudencia.class);
			c.add(Restrictions.eq("incidenteAnalise", incidenteAnalise));
			c.add(Restrictions.eq("tipoOrdenacao", tipoOrdenacao));
			
			return (ObservacaoLivreJurisprudencia) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public ObservacaoLivreJurisprudencia alterar(ObservacaoLivreJurisprudencia observacaoLivreJurisprudencia) throws DaoException {
		Session session = retrieveSession();
		try {
			session.saveOrUpdate(observacaoLivreJurisprudencia);
		} catch (Exception e) {
			throw new DaoException(e.getMessage(), e);
		}
		return observacaoLivreJurisprudencia;
	}
}
