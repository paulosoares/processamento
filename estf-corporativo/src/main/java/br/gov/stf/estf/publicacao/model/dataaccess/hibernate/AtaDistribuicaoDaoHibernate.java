package br.gov.stf.estf.publicacao.model.dataaccess.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.IncidenteDistribuicao;
import br.gov.stf.estf.entidade.publicacao.AtaDistribuicao;
import br.gov.stf.estf.entidade.publicacao.TipoSessao;
import br.gov.stf.estf.processostf.model.util.ObjetoIncidenteComConfidencialidadeQuery;
import br.gov.stf.estf.publicacao.model.dataaccess.AtaDistribuicaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * 
 * @author alvaro.silva
 *
 */
@Repository
public class AtaDistribuicaoDaoHibernate extends GenericHibernateDao<AtaDistribuicao, Long>
						implements AtaDistribuicaoDao {

	private static final long serialVersionUID = 439501977197292766L;
	
	public AtaDistribuicaoDaoHibernate () {
		super(AtaDistribuicao.class);
	}

	public AtaDistribuicao recuperar(Integer numero, TipoSessao tipoSessao, Date dataComposicaoParcial)  throws DaoException {
		AtaDistribuicao resp = null;
		
		try {
			
			Session session = retrieveSession();
			Criteria c = session.createCriteria(AtaDistribuicao.class);
			
			c.add( Restrictions.eq("numeroAta", numero));
			c.add( Restrictions.eq("tipoSessao", tipoSessao));
			c.add( Restrictions.eq("dataComposicaoParcial", dataComposicaoParcial));
			
			resp = (AtaDistribuicao) c.uniqueResult();
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		
		return resp;
	}

	@SuppressWarnings("unchecked")
	public List<IncidenteDistribuicao> pesquisarIncidenteDistribuicao(AtaDistribuicao ataDistribuicao, Boolean recuperarOcultos)  
	throws DaoException {
		
		List<IncidenteDistribuicao> result = null;
		
		try {
			Session session = retrieveSession();
 			 
			StringBuffer hql = new StringBuffer();
			
			hql.append(" SELECT incd FROM IncidenteDistribuicao incd ");
			hql.append(" JOIN incd.atasDistribuicao ad ");
			hql.append(" JOIN FETCH incd.objetoIncidente oi ");
			hql.append(" JOIN FETCH oi.principal p ");
			hql.append(" WHERE ad.id = :adid");
			
			// Restrição para não recuperar processos ocultos
			hql.append(" AND " + ObjetoIncidenteComConfidencialidadeQuery.restringirProcessoOculto_HQL_JPQL("oi", recuperarOcultos));			
			
			Query query = session.createQuery(hql.toString());
			
			query.setParameter("adid", ataDistribuicao.getId());
			
			result = (List<IncidenteDistribuicao>) query.list();
			
			//JUDICIARIO.DISTRIBUICAO
			/*
			Criteria c = session.createCriteria(IncidenteDistribuicao.class, "incd");
			c.createCriteria("incd.atasDistribuicao", "ad", CriteriaSpecification.INNER_JOIN);
			c.createCriteria("incd.objetoIncidente", "oi", CriteriaSpecification.INNER_JOIN);
			c.setFetchMode("oi", FetchMode.EAGER);
			c.createCriteria("oi.principal", "principal");
			c.setFetchMode("principal", FetchMode.EAGER);
			c.add( Restrictions.eq("ad.id", ataDistribuicao.getId()));
			
			result = (List<IncidenteDistribuicao>) c.list();
			*/
			
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		
		return result;
	}
}
