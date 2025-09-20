package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.Categoria;
import br.gov.stf.estf.processostf.model.dataaccess.CategoriaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.SearchData;

@Repository
public class CategoriaDaoHibernate extends GenericHibernateDao<Categoria, Long>
		implements CategoriaDao {
	
	public CategoriaDaoHibernate() {
		super(Categoria.class);
	}

	private static final long serialVersionUID = -6996835210272663629L;

	public List<Categoria> pesquisar(String sigla, String descricao,
			Boolean ativo) throws DaoException {
		List<Categoria> lista;
		try {
			Session session = retrieveSession();
			
			Criteria c = session.createCriteria(Categoria.class);
			
			if( SearchData.stringNotEmpty(sigla) ){
				c.add(Restrictions.eq("sigla", sigla.trim()));
			}
			
			if( SearchData.stringNotEmpty(descricao) ){
				descricao = descricao.replace("|", "\\|");
				descricao = descricao.replace('%', ' ');
				c.add(Restrictions.ilike("descricao", descricao.trim(), MatchMode.ANYWHERE));
			}
			
			if( ativo != null ){
				c.add(Restrictions.eq("ativo", ativo));
			}
			
			c.addOrder(Order.asc("sigla"));
			
			lista = c.list();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return lista;
	}



	

}
