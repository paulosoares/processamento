package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.AcordaoAgendado;
import br.gov.stf.estf.entidade.processostf.AcordaoAgendado.AcordaoAgendadoId;
import br.gov.stf.estf.processostf.model.dataaccess.AcordaoAgendadoDao;
import br.gov.stf.estf.processostf.model.util.ObjetoIncidenteComConfidencialidadeQuery;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class AcordaoAgendadoDaoHibernate extends GenericHibernateDao<AcordaoAgendado, AcordaoAgendadoId> 
	implements AcordaoAgendadoDao {

	public AcordaoAgendadoDaoHibernate() {
		super(AcordaoAgendado.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8837988158613160172L;

	
	
	@SuppressWarnings("unchecked")
	public List<AcordaoAgendado> pesquisarSessaoEspecial(
			Boolean recuperarOcultos, String... siglaClasseProcessual) throws DaoException {
		List<AcordaoAgendado> acordaos = null;
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			hql.append(" SELECT aa FROM AcordaoAgendado aa, Processo p ");
			hql.append(" JOIN FETCH aa.objetoIncidente oi ");
			hql.append(" JOIN FETCH aa.objetoIncidente.principal  ");
			hql.append(" WHERE aa.objetoIncidente.principal = p ");
			hql.append(" AND p.siglaClasseProcessual IN ( :classes ) ");
			hql.append(" AND aa.publico = :publico ");
			
			// Restrição para não recuperar processos ocultos
			hql.append(" AND " + ObjetoIncidenteComConfidencialidadeQuery.restringirProcessoOculto_HQL_JPQL("oi", recuperarOcultos));			
			
			Query q = session.createQuery( hql.toString() );
			q.setParameterList("classes", siglaClasseProcessual);
			q.setString("publico", "N");
			
			acordaos = q.list();
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return acordaos;
	}

	@SuppressWarnings("unchecked")
	public List<AcordaoAgendado> pesquisarComposto(Date dataComposicaoDj)
			throws DaoException {
		List<AcordaoAgendado> acordaos = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(AcordaoAgendado.class);
			c.createAlias("objetoIncidente", "oi");
			
			c.add( Restrictions.eq("publico", Boolean.TRUE) );
			c.add( Restrictions.eq("composicaoDj", dataComposicaoDj) );

			// Restrição para não recuperar processos ocultos
			//ObjetoIncidenteComConfidencialidadeQuery.restringirProcessoOculto_Criteria(c, "oi", recuperarOcultos);			
			
			
			acordaos = c.list();
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		
		return acordaos;
	}

}
