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

import br.gov.stf.estf.entidade.jurisprudencia.TextoPadraoObservacaoJurisprudencia;
import br.gov.stf.estf.entidade.jurisprudencia.TipoOrdenacaoObservacaoJurisprudencia;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.TextoPadraoObservacaoJurisprudenciaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 08.10.2012
 */
@Repository
public class TextoPadraoObservacaoJurisprudenciaDaoHibernate extends
		GenericHibernateDao<TextoPadraoObservacaoJurisprudencia, Long> implements
		TextoPadraoObservacaoJurisprudenciaDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8157551285802502069L;

	public TextoPadraoObservacaoJurisprudenciaDaoHibernate() {
		super(TextoPadraoObservacaoJurisprudencia.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TextoPadraoObservacaoJurisprudencia> pesquisar(TipoOrdenacaoObservacaoJurisprudencia tipoOrdenacao)
			throws DaoException {
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(TextoPadraoObservacaoJurisprudencia.class);
			c.add(Restrictions.eq("tipoOrdenacao", tipoOrdenacao));
			c.addOrder(Order.asc("texto"));
			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
