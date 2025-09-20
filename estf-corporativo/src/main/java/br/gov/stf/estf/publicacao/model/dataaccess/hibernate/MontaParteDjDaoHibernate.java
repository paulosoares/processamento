package br.gov.stf.estf.publicacao.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.publicacao.model.dataaccess.MontaParteDjDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.model.entity.BaseEntity;

@SuppressWarnings("unchecked")
@Repository
public class MontaParteDjDaoHibernate extends GenericHibernateDao implements MontaParteDjDao {

	
	public MontaParteDjDaoHibernate() {
		super(BaseEntity.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7822270998023881357L;

	public Boolean recuperarRedatorAcordao(String siglaProcesso,
			Long numProcesso, Long codRecurso, String tipoJulgamento)
			throws DaoException {
		
		try {
			StringBuffer hql = new StringBuffer("SELECT m FROM Ministro m, SituacaoMinistroProcesso smp WHERE");
			hql.append(" smp.id.siglaClasseProcessual = ? AND smp.id.numeroProcessual = ? AND smp.id.codigoRecurso = ? ");
			hql.append("AND smp.tipoJulgamento = ? ");
			hql.append("AND smp.id.codigoOcorrencia IN (?,?,?) AND m.id = smp.id.codigoMinistroRelator");
			
			Session session = retrieveSession();
			Query q = session.createQuery(hql.toString());
			q.setString(0, siglaProcesso);
			q.setLong(1, numProcesso);
			q.setLong(2, codRecurso);
			q.setString(3, tipoJulgamento);
			q.setString(4, "RA");
			q.setString(5, "RR");
			q.setString(6, "WW");
			
			List resp = q.list();
			
			if ( resp!=null && resp.size()>0 ) {
				return true;
			} else {
				return false;
			}
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		
	}

}
