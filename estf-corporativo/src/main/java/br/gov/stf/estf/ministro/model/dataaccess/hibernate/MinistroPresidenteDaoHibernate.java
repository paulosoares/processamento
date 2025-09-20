package br.gov.stf.estf.ministro.model.dataaccess.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.ministro.MinistroPresidente;
import br.gov.stf.estf.entidade.ministro.MinistroPresidente.MinistroPresidenteId;
import br.gov.stf.estf.entidade.ministro.TipoOcorrenciaMinistro;
import br.gov.stf.estf.ministro.model.dataaccess.MinistroPresidenteDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;
import br.gov.stf.framework.util.DateTimeHelper;

@Repository
public class MinistroPresidenteDaoHibernate extends
		GenericHibernateDao<MinistroPresidente, MinistroPresidenteId> implements
		MinistroPresidenteDao {

	public MinistroPresidenteDaoHibernate() {
		super(MinistroPresidente.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7101193937767563818L;

	public MinistroPresidente recuperarMinistroPresidenteAtual()
			throws DaoException {
		TipoOcorrenciaMinistro tipoOcorrenciaMinistro = TipoOcorrenciaMinistro.MP;
		return recuperarMinistro(tipoOcorrenciaMinistro);
	}

	public MinistroPresidente recuperarMinistro(
			TipoOcorrenciaMinistro tipoOcorrenciaMinistro) throws DaoException {
		MinistroPresidente ministro = null;
		Session session = retrieveSession();

		try {
			Criteria criteria = session
					.createCriteria(MinistroPresidente.class);
			criteria.add(Restrictions.isNull("dataAfastamento"));
			criteria.add(Restrictions.eq("tipoOcorrencia",
					tipoOcorrenciaMinistro));
			ministro = (MinistroPresidente) criteria.uniqueResult();
		} catch (HibernateException e) {
			throw new DaoException(e);
		}

		return ministro;
	}

	public List<MinistroPresidente> recuperarMinistrosPresidentesAtuais()
			throws DaoException {
		Session session = retrieveSession();
		Criteria criteria = session.createCriteria(MinistroPresidente.class);
		criteria.add(Restrictions.isNull("dataAfastamento"));
		return criteria.list();
	}

	@Override
	public MinistroPresidente recuperarMinistro(TipoOcorrenciaMinistro tipoOcorrenciaMinistro, Date dataPosse) throws DaoException {
		Session session = retrieveSession();
		StringBuffer hql = new StringBuffer();
		
		try {
		
			hql.append("SELECT m FROM MinistroPresidente m WHERE 1=1 ");
			
			if(tipoOcorrenciaMinistro != null)
				hql.append(" AND m.tipoOcorrencia = :tipoOcorrencia ");
			
			if(dataPosse != null)
				hql.append(" AND m.id.dataPosse BETWEEN to_date(:dataPosseInicial, 'DD/MM/YYYY HH24:MI:SS') AND to_date(:dataPosseFinal, 'DD/MM/YYYY HH24:MI:SS') ");
			
			Query q = session.createQuery(hql.toString());
			
			if(tipoOcorrenciaMinistro != null)
				q.setString("tipoOcorrencia", tipoOcorrenciaMinistro.getSigla());
				
			if(dataPosse != null)
				q.setString("dataPosseInicial", DateTimeHelper.getDataString(dataPosse)	+ "00:00:00");
			
			if(dataPosse != null)
				q.setString("dataPosseFinal", DateTimeHelper.getDataString(dataPosse)	+ "23:59:59");
			
			return (MinistroPresidente) q.uniqueResult();
			
		} catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}
	}
	
	
	@Override
	public MinistroPresidente recuperarMinistroPresidenteNoPeriodo(TipoOcorrenciaMinistro tipoOcorrenciaMinistro, Date dataInicio, Date dataFim) {
		try {
			Session session = retrieveSession();
			
			StringBuffer hql = new StringBuffer();
			
			
			hql.append("SELECT m FROM MinistroPresidente m WHERE 1=1 ");
			
			if(tipoOcorrenciaMinistro != null)
				hql.append(" AND m.tipoOcorrencia = :tipoOcorrencia ");
			
			if(dataInicio != null && dataFim != null)
				hql.append(" AND m.id.dataPosse <= to_date(:dataInicial, 'DD/MM/YYYY HH24:MI:SS') AND (m.dataAfastamento >= to_date(:dataFim, 'DD/MM/YYYY HH24:MI:SS') OR m.dataAfastamento IS NULL) ");
			
			Query q = session.createQuery(hql.toString());
			
			if(tipoOcorrenciaMinistro != null)
				q.setString("tipoOcorrencia", tipoOcorrenciaMinistro.getSigla());
				
			if(dataInicio != null && dataFim != null) {
				q.setString("dataInicial", DateTimeHelper.getDataString(dataInicio)	+ "00:00:00");
				q.setString("dataFim", DateTimeHelper.getDataString(dataInicio)	+ "23:59:59");
			}
			
			return (MinistroPresidente) q.uniqueResult();
		} catch (DaoException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
