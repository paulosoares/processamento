package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.Auditoria;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.AuditoriaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Edvaldoo
 * @since 07.12.2020
 */
@Repository
public class AuditoriaDaoHibernate extends GenericHibernateDao<Auditoria, Long> implements AuditoriaDao {

	private static final long serialVersionUID = 6814493405850566110L;

	public AuditoriaDaoHibernate() {
		super(Auditoria.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Auditoria> pesquisarPelaReferenciaDoProcesso(Auditoria auditoria) throws DaoException {
		try {
			Session session = retrieveSession();

			StringBuilder hql = new StringBuilder("SELECT a FROM Auditoria a ");

			if (auditoria != null)
				hql.append(" WHERE REPLACE(REPLACE(LOWER(a.referenciaProcesso), ' ', ''), '-', '') "
						+ "like  :referenciaProcesso");
			
			if (auditoria.getTipoDocumento() != null) {
				hql.append(" and a.tipoDocumento= :tipoDocumento"); 
			}
			hql.append(" order by a.dataOperacao desc");

			Query q = session.createQuery(hql.toString());

			q.setString("referenciaProcesso", auditoria.getReferenciaProcesso() + "%");
			
			if (auditoria.getTipoDocumento() != null) {
				q.setString("tipoDocumento", auditoria.getTipoDocumento());
			}
			
			q.setMaxResults(500);

			return q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}

	}

}
