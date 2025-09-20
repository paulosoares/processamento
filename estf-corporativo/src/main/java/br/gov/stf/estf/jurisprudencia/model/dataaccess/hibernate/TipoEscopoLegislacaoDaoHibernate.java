/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.TipoEscopoLegislacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.TipoEscopoLegislacaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 11.08.2012
 */
@Repository
public class TipoEscopoLegislacaoDaoHibernate extends GenericHibernateDao<TipoEscopoLegislacao, Long> implements
		TipoEscopoLegislacaoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3862342276634911922L;

	public TipoEscopoLegislacaoDaoHibernate() {
		super(TipoEscopoLegislacao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoEscopoLegislacao> pesquisarTodos() throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TipoEscopoLegislacao.class);
			c.addOrder(Order.asc("ordem"));
			c.addOrder(Order.asc("descricao"));
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoEscopoLegislacao> pesquisarTiposEscopoLegislacao(String sugestao) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TipoEscopoLegislacao.class);
			if (StringUtils.isNotBlank(sugestao)) {
				c.add(Restrictions.ilike("descricao", sugestao, MatchMode.ANYWHERE));
			}
			c.addOrder(Order.asc("ordem"));
			c.addOrder(Order.asc("descricao"));
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
