package br.gov.stf.estf.usuario.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.usuario.PerfilUsuarioSetor;
import br.gov.stf.estf.entidade.usuario.PerfilUsuarioSetor.PerfilUsuarioSetorId;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.usuario.model.dataaccess.PerfilUsuarioSetorDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.SearchData;

@Repository
public class PerfilUsuarioSetorDaoHibernate extends GenericHibernateDao<PerfilUsuarioSetor, PerfilUsuarioSetorId> 
	implements PerfilUsuarioSetorDao {

	public PerfilUsuarioSetorDaoHibernate() {
		super(PerfilUsuarioSetor.class);
	}

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public List<PerfilUsuarioSetor> pesquisarSetoresUsuario(Usuario usuario) throws DaoException {
		try {
			
			Session session = retrieveSession();
			StringBuilder strBuilder = new StringBuilder(); 
			strBuilder.append(" SELECT pus FROM PerfilUsuarioSetor pus");
			strBuilder.append(" WHERE pus.usuario.id = ? ");
			
			Query hql =  session.createQuery(strBuilder.toString());
			hql.setString(0, usuario.getId());
			return hql.list();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<PerfilUsuarioSetor> pesquisarUsuarioDoSetor(String nome, Long idSetor) throws DaoException {
		List<PerfilUsuarioSetor> lista;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(PerfilUsuarioSetor.class, "p");
			c.createAlias("p.usuario", "u");
			
			
			if( SearchData.stringNotEmpty(nome) ){
				nome = nome.replace("|", "\\|");
				c.add(Restrictions.ilike("u.nome", nome.trim(), MatchMode.ANYWHERE));
			}
			
			c.add( Restrictions.eq("u.ativo", true) );
			
			if( idSetor != null ){
				c.add(Restrictions.or(Restrictions.eq("p.setor.id", idSetor), Restrictions.eq("u.setor.id", idSetor) ));
			}
			c.addOrder(Order.asc("u.nome"));
			
			lista = c.list();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return lista;
	}

	@Override
	public List<PerfilUsuarioSetor> pesquisarUsuarioDoSetorSistema(String nome, Long idSetor, String sistema) throws DaoException {
		List<PerfilUsuarioSetor> lista;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(PerfilUsuarioSetor.class, "p");
			c.createAlias("p.usuario", "u");
			c.createAlias("p.perfil", "e");
			
			
			if( SearchData.stringNotEmpty(nome) ){
				nome = nome.replace("|", "\\|");
				c.add(Restrictions.ilike("u.nome", nome.trim(), MatchMode.ANYWHERE));
			}
			
			c.add( Restrictions.eq("u.ativo", true) );
			
			if( idSetor != null ){
				c.add(Restrictions.or(Restrictions.eq("p.setor.id", idSetor), Restrictions.eq("u.setor.id", idSetor) ));
			}
			c.add(Restrictions.eq("e.sistema", sistema.trim()));
			c.addOrder(Order.asc("u.nome"));
			
			lista = c.list();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return lista;
	}
	
	
	@Override
	public List<PerfilUsuarioSetor> pesquisarSetoresUsuario(Usuario usuario,
			String siglaSistema) throws DaoException {
		try {
			
			Session session = retrieveSession();
			StringBuilder strBuilder = new StringBuilder(); 
			strBuilder.append(" SELECT pus FROM PerfilUsuarioSetor pus");
			strBuilder.append(" JOIN FETCH pus.perfil ");
			strBuilder.append(" WHERE pus.usuario.id = ? ");
			strBuilder.append(" AND pus.perfil.sistema = ? ");
			
			Query hql =  session.createQuery(strBuilder.toString());
			hql.setString(0, usuario.getId());
			hql.setString(1, siglaSistema);
			return hql.list();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
	}

}
