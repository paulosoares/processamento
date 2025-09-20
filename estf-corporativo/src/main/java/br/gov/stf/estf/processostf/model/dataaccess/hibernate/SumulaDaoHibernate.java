package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.Sumula;
import br.gov.stf.estf.entidade.processostf.Sumula.FlagGenericaSumula;
import br.gov.stf.estf.processostf.model.dataaccess.SumulaDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.DateTimeHelper;


@Repository
public class SumulaDaoHibernate extends GenericHibernateDao<Sumula, Long>     
	implements SumulaDao {

	public SumulaDaoHibernate() {
		super(Sumula.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8811919210899058732L;

	@SuppressWarnings("unchecked")
	public Long recuperarNumeroUltimaSumula() throws DaoException {
		Long numero = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Sumula.class);
			c.addOrder( Order.desc("numeroSumula") );
			List<Sumula> sumulas = c.list();
			if ( sumulas!=null && sumulas.size()>0 ) {
				numero = sumulas.get(0).getNumeroSumula();
			} else {
				numero = 0L;
			}
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return numero;
	}

	@SuppressWarnings("unchecked")
	public Long recuperarNumeroUltimaSeqSumula() throws DaoException {
		Long numero = null;
		try {
			Session session = retrieveSession();
			Criteria c = session.createCriteria(Sumula.class);
			c.addOrder( Order.desc("seqSumula") );
			List<Sumula> sumulas = c.list();
			if ( sumulas!=null && sumulas.size()>0 ) {
				numero = sumulas.get(0).getSeqSumula();
			} else {
				numero = 0L;
			}
		} catch ( Exception e ) {
			throw new DaoException(e);
		}
		return numero;
	}
	
	public List<Sumula> pesquisarSumula(Long numeroSumula, Long processoPrecedente, 
			String descricaoVerbete, Date dataAprovacao, String tipoSumula) throws DaoException{
		
		List<Sumula> lista = null;
		
		StringBuffer hql = new StringBuffer();
		Session session = retrieveSession();
		
		try {
			hql.append(" SELECT su FROM Sumula su ");
			
			if (processoPrecedente != null){
				hql.append(" INNER JOIN su.sumulaIncidente si");
			}
			
			hql.append(" WHERE (1=1) ");		
			
			if (numeroSumula != null && numeroSumula > 0L){
				hql.append(" AND su.numeroSumula = :numeroSumula ");
			}
			
			if (processoPrecedente != null){
				hql.append(" AND si.objetoIncidente.principal = :idObjetoIncidente ");
			}
			
			if (descricaoVerbete != null && descricaoVerbete.trim().length() > 0L){
				hql.append(" AND upper(su.verbete) LIKE :descricaoVerbetePesq"); 
			}
			
			if( dataAprovacao != null ) {
				hql.append(" AND su.dataAprovacao >= TO_DATE(:dataAprovacaoPesq, 'DD/MM/YYYY')");
			}
			
			if (tipoSumula != null && tipoSumula.trim().length() > 0 &&
					tipoSumula.equals(FlagGenericaSumula.S.name())){
				hql.append(" AND su.flgVinculante = 'S' ");
			}
			
			if (tipoSumula != null && tipoSumula.trim().length() > 0 &&
					tipoSumula.equals(FlagGenericaSumula.N.name())){
				hql.append(" AND su.flgVinculante = 'N' ");
			}
			
			hql.append(" ORDER BY su.numeroSumula ");
			
			
			Query q = session.createQuery( hql.toString() );
			
			if (numeroSumula != null && numeroSumula > 0L){
				q.setLong("numeroSumula", numeroSumula);
			}
			
			if (processoPrecedente != null){
				q.setLong("idObjetoIncidente", processoPrecedente);
			}
			
			if (descricaoVerbete != null && descricaoVerbete.trim().length() > 0L){
				q.setString("descricaoVerbetePesq", "%" + descricaoVerbete.toUpperCase() + "%");
			}
			
			if( dataAprovacao != null ) {
				q.setString("dataAprovacaoPesq", DateTimeHelper.getDataString(dataAprovacao));
			}
			
			lista = q.list();
			
		} catch (HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch (RuntimeException e) {
			throw new DaoException("RuntimeException", e);
		}

		return lista;
	}

}
