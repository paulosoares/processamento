/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.TipoLegislacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.TipoLegislacaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 11.08.2012
 */
@Repository
public class TipoLegislacaoDaoHibernate extends GenericHibernateDao<TipoLegislacao, Long> implements TipoLegislacaoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3498110003365056637L;

	public TipoLegislacaoDaoHibernate() {
		super(TipoLegislacao.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoLegislacao> pesquisarTiposLegislacaoPrincipais(String sugestao) throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TipoLegislacao.class);
			Criteria c2 = session.createCriteria(TipoLegislacao.class);
			String[] siglas = {"CF", "CIB"};
			c.add(Restrictions.or(Restrictions.eq("tipoLegislacao", "2"), Restrictions.in("sigla", siglas)));
			c2.add(Restrictions.or(Restrictions.eq("tipoLegislacao", "2"), Restrictions.in("sigla", siglas)));
			
			if (StringUtils.isNotBlank(sugestao)) {
				c.add(Restrictions.ilike("sigla", sugestao, MatchMode.START));
				c2.add(Restrictions.like("descricao", sugestao.toUpperCase(), MatchMode.START));
			}
			c.addOrder(Order.asc("sigla"));
			c2.addOrder(Order.asc("descricao"));
			
			List<TipoLegislacao> lista = c.list();
			lista.addAll(ListUtils.subtract(c2.list(), lista));
			
			return lista;
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
