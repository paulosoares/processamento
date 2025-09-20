package br.gov.stf.estf.usuario.model.dataaccess.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.usuario.TipoGrupoControle;
import br.gov.stf.estf.entidade.usuario.TipoGrupoUsuarioControle;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.usuario.model.dataaccess.TipoGrupoUsuarioControleDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoGrupoUsuarioControleDaoHibernate extends GenericHibernateDao<TipoGrupoUsuarioControle, Long> implements
		TipoGrupoUsuarioControleDao {

	private static final long serialVersionUID = 6806459435404902077L;

	public TipoGrupoUsuarioControleDaoHibernate() {
		super(TipoGrupoUsuarioControle.class);
	}

	@SuppressWarnings({"unchecked" })
	public List<TipoGrupoControle> buscaGruposDoUsuario(String sigUsuario)
			throws DaoException {
		
		List<TipoGrupoControle> listaGruposControles = null;
	    
	    try {
	    	Session session = retrieveSession();
	    	StringBuilder hql = new StringBuilder(" SELECT tgu.tipoGrupoControle FROM TipoGrupoUsuarioControle tgu ");
	    	hql.append(" WHERE (1=1) ");
	    	
	    	if (sigUsuario != null && sigUsuario.length() > 0){
	    		hql.append(" AND (upper(tgu.usuario.id) = :sigUsuario) ");
	    	}
	    	hql.append(" AND (tgu.tipoGrupoControle.flagTipoGrupoControleAtivo = 'S' ) ");
	    	hql.append(" AND tgu.flagAtivo = 'S' ");
	    	
	    	Query q = session.createQuery(hql.toString());
	    	
	    	q.setString("sigUsuario", sigUsuario.toUpperCase());
	    	
	    	listaGruposControles = (List<TipoGrupoControle>)q.list();
	    	
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaGruposControles;
	}

	@SuppressWarnings("unchecked")
	public List<TipoGrupoUsuarioControle> pesquisarGrupoUsuarioControle(
			Usuario usuario, Long idTipoGrupoControle) throws DaoException {
		
		List<TipoGrupoUsuarioControle> listaTipoGrupoUsuario = null;
		
	    try {
	    	Session session = retrieveSession();
	    	StringBuilder hql = new StringBuilder(" SELECT tgu FROM TipoGrupoUsuarioControle tgu ");
	    	hql.append(" WHERE (1=1) ");
	    	if (usuario != null && usuario.getNome() != null && usuario.getNome().length() > 0){
	    		hql.append(" AND tgu.usuario LIKE ('%" + usuario.getNome() + "%')");
	    	}
	    	if (idTipoGrupoControle != null){
	    		hql.append(" AND tgu.tipoGrupoControle.id = " + idTipoGrupoControle);
	    	}
	    	
	    	hql.append(" AND tgu.flagAtivo = 'S' ");
			Query q = session.createQuery(hql.toString());
			listaTipoGrupoUsuario = (List<TipoGrupoUsuarioControle>)q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaTipoGrupoUsuario;
	}
	
	public List<String> pesquisarUsuarioPeloGrupoControle(Long idTipoGrupoControle) throws DaoException {
		
		List<String> listaUsuario = null;
	    StringBuffer sql = new StringBuffer();
		
	    try {
	    	sql.append(" SELECT DISTINCT SIG_USUARIO ");  
	    	sql.append(" FROM JUDICIARIO.TIPO_GRUPO_CONTROLE_USUARIO ");
	    	if (idTipoGrupoControle != null){
	    		sql.append("  WHERE  SEQ_TIPO_GRUPO_CONTROLE = :idTipoGrupoControle ");
	    		sql.append(" AND FLG_ATIVO = 'S' ");
	    	}else{
	    		sql.append(" WHERE FLG_ATIVO = 'S' ");
	    	}
	    	
	    	
	    	SQLQuery sqlQuery = retrieveSession().createSQLQuery( sql.toString() );
	    	
	    	if (idTipoGrupoControle != null){
	    		sqlQuery.setLong("idTipoGrupoControle", idTipoGrupoControle);
	    	}
	    	
			listaUsuario = sqlQuery.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaUsuario;
	}	
	
	
	public void apagarGrupoUsuarioControle(TipoGrupoUsuarioControle tguc) throws DaoException{
		
		try {
			Connection con = retrieveSession().connection();
			
			StringBuffer sql = new StringBuffer();
			sql.append(" DELETE FROM JUDICIARIO.TIPO_GRUPO_USUARIO ");
			sql.append(" WHERE SEQ_TIPO_GRUPO_USUARIO = ?");
	
			PreparedStatement ps = retrieveSession().connection().prepareStatement(sql.toString());
		
			ps.setLong(1, tguc.getId());
		
			ps.executeUpdate();
			ps.close();

		}catch(SQLException e){
			throw new DaoException("SQLException",e);
		}
		catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		}
		catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
		  
	}

}
