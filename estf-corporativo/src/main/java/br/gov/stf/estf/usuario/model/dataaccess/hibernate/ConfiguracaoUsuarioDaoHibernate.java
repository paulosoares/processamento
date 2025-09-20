package br.gov.stf.estf.usuario.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.usuario.ConfiguracaoUsuario;
import br.gov.stf.estf.usuario.model.dataaccess.ConfiguracaoUsuarioDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.SearchData;

@Repository
public class ConfiguracaoUsuarioDaoHibernate extends GenericHibernateDao<ConfiguracaoUsuario, Long> implements ConfiguracaoUsuarioDao { 
   
    /**
	 * 
	 */
	private static final long serialVersionUID = -666837212345724357L;

	public ConfiguracaoUsuarioDaoHibernate() {
    	super(ConfiguracaoUsuario.class);
    }
	
	 public List<ConfiguracaoUsuario> pesquisar(String siglaUsuario, Long idSetor, Long idTipoConfUsuario, String codChave) throws DaoException {
		 List<ConfiguracaoUsuario> lista = null;
		 StringBuffer hql = new StringBuffer();
		 Session session = retrieveSession();
		 
		 try {
			 hql.append(" FROM ConfiguracaoUsuario cu WHERE 1 = 1 ");
			 
			 if( idTipoConfUsuario != null && idTipoConfUsuario > 0 )
				 hql.append(" AND cu.tipoConfiguracaoUsuario.id = :idTipoConfUsuario ");
			 
			 if( SearchData.stringNotEmpty(codChave) )
				 hql.append(" AND cu.codigoChave = :codChave ");
			 
			 if( SearchData.stringNotEmpty(siglaUsuario) && (idSetor != null && idSetor > 0) ) {
				 hql.append(" AND ( cu.usuario.id = :siglaUsuario OR cu.setor.id = :idSetor ) ");
			 } else {
				 if( SearchData.stringNotEmpty(siglaUsuario) ) 
					 hql.append(" AND cu.usuario.id = :siglaUsuario ");
					 
				 if( idSetor != null && idSetor > 0 ) 
					 hql.append(" AND cu.setor.id = :idSetor ) ");					 
			 }
			 
			 hql.append(" ORDER BY cu.descricao ");
			 
			 Query q = session.createQuery(hql.toString());
			 
			 if( idTipoConfUsuario != null && idTipoConfUsuario > 0 )
				 q.setLong("idTipoConfUsuario", idTipoConfUsuario);
			 
			 if( SearchData.stringNotEmpty(codChave) )
				 q.setString("codChave", codChave);

			 if( SearchData.stringNotEmpty(siglaUsuario) )
				 q.setString("siglaUsuario", siglaUsuario);
			 
			 if( idSetor != null && idSetor > 0 )
				 q.setLong("idSetor", idSetor);
			 
			 lista = q.list();
			 
		 } catch ( Exception e ) {
			throw new DaoException(e);
		 }
		 
		 return lista;
	 }

}
