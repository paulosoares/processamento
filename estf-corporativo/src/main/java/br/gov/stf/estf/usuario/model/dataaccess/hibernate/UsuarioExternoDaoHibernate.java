package br.gov.stf.estf.usuario.model.dataaccess.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.usuario.UsuarioExterno;
import br.gov.stf.estf.usuario.model.dataaccess.UsuarioExternoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class UsuarioExternoDaoHibernate extends
		GenericHibernateDao<UsuarioExterno, Integer> implements
		UsuarioExternoDao {

	public UsuarioExternoDaoHibernate() {
		super(UsuarioExterno.class);
	}

	private static final long serialVersionUID = 1L;

	public List<UsuarioExterno> usuariosIntegracaoESTF() throws DaoException {
		List<UsuarioExterno> listaUsuarioExternos = new ArrayList<UsuarioExterno>();

		Session session = retrieveSession();
		try {
			StringBuffer sql = new StringBuffer();

			sql.append("SELECT ue.SEQ_USUARIO_EXTERNO FROM CORP.USUARIO_EXTERNO ue , ESTF.USUARIO_EXTERNO_INTEGRACAO uei ");
			sql.append(" WHERE ue.SEQ_USUARIO_EXTERNO = uei.SEQ_USUARIO_EXTERNO AND UE.FLG_CADASTRO_CONFIRMADO = 'S' ");
			sql.append(" ORDER BY ue.NOM_USUARIO_EXTERNO ");

			Query q = session.createSQLQuery(sql.toString());
			List<Integer> ids = q.list();

			StringBuffer hql = new StringBuffer();
			
			hql.append("SELECT ue FROM UsuarioExterno ue ");
			hql.append("WHERE ue.id IN ( :ids ) ORDER BY ue.nome ASC");
			
			Query query = session.createQuery(hql.toString());	
			
			query.setParameterList("ids", ids);
		
			listaUsuarioExternos = query.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return listaUsuarioExternos;
	}
}
