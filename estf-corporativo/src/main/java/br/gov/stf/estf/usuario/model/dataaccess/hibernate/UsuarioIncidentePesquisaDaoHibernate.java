package br.gov.stf.estf.usuario.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.usuario.UsuarioIncidentePesquisa;
import br.gov.stf.estf.usuario.model.dataaccess.UsuarioIncidentePesquisaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class UsuarioIncidentePesquisaDaoHibernate extends GenericHibernateDao<UsuarioIncidentePesquisa, UsuarioIncidentePesquisa.UsuarioIncidentePesquisaId>
	implements UsuarioIncidentePesquisaDao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsuarioIncidentePesquisaDaoHibernate() {
		super(UsuarioIncidentePesquisa.class);
	}
	
	@Override
	public List<UsuarioIncidentePesquisa> pesquisarPorUsuario(String usuario) throws DaoException {
		try {
			Session session = retrieveSession();
			
			Criteria c = session.createCriteria(UsuarioIncidentePesquisa.class);
			c.add(Restrictions.eq("id.sigUsuario", usuario));

			return c.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	@Override
	public void apagarListaExportacao(String usuario){		
		try {
			Session session = retrieveSession();
			
			StringBuffer queryString = new StringBuffer();
			
			queryString.append(" DELETE FROM EGAB.USUARIO_INCIDENTE_PESQUISA WHERE SIG_USUARIO_PESQUISA = :usuario ");
			
			SQLQuery query = session.createSQLQuery(queryString.toString());
			query.setParameter("usuario", usuario);
			query.executeUpdate();
			
		} catch (Exception e) {

		}		
	}
	

}
