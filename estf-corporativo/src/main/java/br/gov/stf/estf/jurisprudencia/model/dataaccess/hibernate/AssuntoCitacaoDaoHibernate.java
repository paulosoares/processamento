/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.AssuntoCitacao;
import br.gov.stf.estf.entidade.jurisprudencia.IncidenteAnalise;
import br.gov.stf.estf.entidade.jurisprudencia.TipoCitacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.AssuntoCitacaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 17.10.2012
 */
@Repository
public class AssuntoCitacaoDaoHibernate extends GenericHibernateDao<AssuntoCitacao, Long> implements AssuntoCitacaoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4720335583841872458L;

	public AssuntoCitacaoDaoHibernate() {
		super(AssuntoCitacao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AssuntoCitacao> pesquisar(IncidenteAnalise incidenteAnalise, TipoCitacao tipoCitacao)
			throws DaoException {
		try {
			Session session = retrieveSession();
			
			Criteria c = session.createCriteria(AssuntoCitacao.class);
			c.add(Restrictions.eq("incidenteAnalise", incidenteAnalise));
			c.add(Restrictions.eq("tipoCitacao", tipoCitacao));
			c.addOrder(Order.asc("ordem"));
			
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public AssuntoCitacao alterar(AssuntoCitacao assuntoCitacao) throws DaoException {
		Session session = retrieveSession();
		try {
			session.saveOrUpdate(assuntoCitacao);
		} catch (Exception e) {
			throw new DaoException(e.getMessage(), e);
		}
		return assuntoCitacao;
	}

}
