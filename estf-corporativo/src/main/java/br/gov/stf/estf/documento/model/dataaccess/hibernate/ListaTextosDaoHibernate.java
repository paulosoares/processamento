package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.ListaTextosDao;
import br.gov.stf.estf.entidade.documento.ListaTextos;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.SearchData;

@Repository
public class ListaTextosDaoHibernate extends GenericHibernateDao<ListaTextos, Long> implements ListaTextosDao {

	private static final long serialVersionUID = -7011235029521755493L;

	public ListaTextosDaoHibernate() {
		super(ListaTextos.class);
	}

	@SuppressWarnings("unchecked")
	public ListaTextos recuperarPorNome(String nome) throws DaoException {
		Criteria criteria = retrieveSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("nome", nome));

		return (ListaTextos) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<ListaTextos> pesquisarListaTextos(String nome, Boolean ativo, Long idSetor) throws DaoException {
		List<ListaTextos> lista = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(ListaTextos.class);
			if (SearchData.stringNotEmpty(nome)) {
				nome = nome.replace("|", "\\|");
				nome = nome.replace('%', ' ');
				c.add(Restrictions.ilike("nome", nome.trim(), MatchMode.ANYWHERE));
			}

			if (ativo != null) {
				c.add(Restrictions.eq("ativa", ativo));
			}

			if (idSetor != null) {
				c.add(Restrictions.eq("setor.id", idSetor));
			}

			c.addOrder(Order.asc("nome"));
			lista = c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return lista;
	}

	public List<ListaTextos> pesquisarListasDoTexto(Texto texto) throws DaoException {
		String sql = "SELECT lt FROM ListaTextos lt, TextoListaTexto tlt WHERE lt.id = tlt.id.listaTexto AND tlt.id.texto = ?";
		Query query = retrieveSession().createQuery(sql);
		query.setParameter(0, texto.getId());
		return query.list();
	}

}
