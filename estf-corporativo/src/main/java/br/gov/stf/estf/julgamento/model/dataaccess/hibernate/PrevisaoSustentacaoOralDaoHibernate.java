/**
 * 
 */
package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.julgamento.PrevisaoSustentacaoOral;
import br.gov.stf.estf.julgamento.model.dataaccess.PrevisaoSustentacaoOralDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 05.07.2011
 */
@Repository
public class PrevisaoSustentacaoOralDaoHibernate extends
		GenericHibernateDao<PrevisaoSustentacaoOral, Long> implements PrevisaoSustentacaoOralDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1444658196246219314L;

	public PrevisaoSustentacaoOralDaoHibernate() {
		super(PrevisaoSustentacaoOral.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PrevisaoSustentacaoOral> pesquisar(
			InformacaoPautaProcesso informacaoPautaProcesso)
			throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(PrevisaoSustentacaoOral.class);
			c.add(Restrictions.eq("informacaoPautaProcesso.id", informacaoPautaProcesso.getId()));
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
