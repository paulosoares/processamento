/**
 * 
 */
package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.TextoListaTextoDao;
import br.gov.stf.estf.entidade.documento.ListaTextos;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TextoListaTexto;
import br.gov.stf.estf.entidade.documento.TextoListaTexto.TextoListaTextoId;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 24.05.2011
 */
@Repository
public class TextoListaTextoDaoHibernate extends
		GenericHibernateDao<TextoListaTexto, TextoListaTextoId> implements TextoListaTextoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3643154325767763610L;

	public TextoListaTextoDaoHibernate() {
		super(TextoListaTexto.class);
	}

	@Override
	public TextoListaTexto recuperar(ListaTextos listaTextos, Texto texto)
			throws DaoException {
		Session session;
		
		try {
			session = retrieveSession();
			Criteria c = session.createCriteria(TextoListaTexto.class);
			c.add(Restrictions.eq("id.listaTexto.id", listaTextos.getId()));
			c.add(Restrictions.eq("id.texto.id", texto.getId()));
			
			return (TextoListaTexto) c.uniqueResult();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
