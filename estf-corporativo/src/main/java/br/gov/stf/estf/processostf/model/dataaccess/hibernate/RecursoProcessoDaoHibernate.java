package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.RecursoProcessoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class RecursoProcessoDaoHibernate extends GenericHibernateDao<RecursoProcesso, Long> implements RecursoProcessoDao {

	public RecursoProcessoDaoHibernate() {
		super(RecursoProcesso.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6041658672426918049L;

	@SuppressWarnings("unchecked")
	public List<RecursoProcesso> pesquisar(String siglaProcessual, Long numeroProcessual) throws DaoException {
		List<RecursoProcesso> recursos = null;
		try {
			Session session = retrieveSession();
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT rp FROM RecursoProcesso rp, Processo p ");
			hql.append(" WHERE rp.principal = p ");
			hql.append(" AND p.siglaClasseProcessual = :siglaClasseProcessual ");
			hql.append(" AND p.numeroProcessual = :numeroProcessual ");

			Query q = session.createQuery( hql.toString() );
			q.setString("siglaClasseProcessual", siglaProcessual);
			q.setLong("numeroProcessual", numeroProcessual);
			
			recursos = q.list();
		} catch (Exception e) {
			throw new DaoException(e);
		}
		return recursos;
	}

	@Override
	public Long pesquisar(Processo processo, Date data) throws DaoException {
		
		Session session = retrieveSession();
		String sql = "select cod_recurso from judiciario.recurso_processo rp where sig_classe_proces = :sigla and num_processo = :numero and " +
			"dat_interposicao = (select max(rp2.dat_interposicao) from judiciario.recurso_processo rp2 where rp2.sig_classe_proces = :sigla and rp2.num_processo = :numero" +
			" AND rp2.dat_interposicao <= :data " +
			")";
		
		Query query = session.createSQLQuery(sql);
		query.setParameter("sigla", processo.getSiglaClasseProcessual());
		query.setParameter("numero", processo.getNumeroProcessual());
		query.setDate("data", data);
		
		Number codigoRecurso = (Number)query.uniqueResult();
		
		return codigoRecurso == null ? null : codigoRecurso.longValue();
	}

}
