package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.SituacaoJulgamento;
import br.gov.stf.estf.julgamento.model.dataaccess.SituacaoJulgamentoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class SituacaoJulgamentoDaoHibernate extends GenericHibernateDao<SituacaoJulgamento, Long> implements SituacaoJulgamentoDao{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8346493655099690360L;

	public SituacaoJulgamentoDaoHibernate() {
		super(SituacaoJulgamento.class);
	}

	@SuppressWarnings("unchecked")
	public List<SituacaoJulgamento> pesquisar(Long idJulgamentoProcesso)
			throws DaoException {
		
		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(SituacaoJulgamento.class);
		criteria.add(Restrictions.eq("julgamentoProcesso.id", idJulgamentoProcesso));
		
		return criteria.list();
	}

}
