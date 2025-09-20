package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.IndexacaoIncidenteAnalise;
import br.gov.stf.estf.entidade.jurisprudencia.MinistroIndexacao;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.MinistroIndexacaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Rafael.Dias
 * @since 24.10.2012
 */
@Repository
public class MinistroIndexacaoDaoHibernate extends GenericHibernateDao<MinistroIndexacao, MinistroIndexacao.MinistroIndexacaoId> implements
			MinistroIndexacaoDao {
	
	private static final long serialVersionUID = -6966897169101407637L;

	public MinistroIndexacaoDaoHibernate() {
		super(MinistroIndexacao.class);
	}
	
	public List<MinistroIndexacao> pesquisarMinistroIndexacao(IndexacaoIncidenteAnalise indexacaoIncidenteAnalise) throws DaoException{
		Session session = retrieveSession();

		List<MinistroIndexacao> ministrosIndexacao = null;

		try {
			StringBuffer hql = new StringBuffer("SELECT mi FROM MinistroIndexacao mi WHERE 1 = 1 ");

			if( indexacaoIncidenteAnalise != null && indexacaoIncidenteAnalise.getId() != null)
				hql.append("AND mi.id.indexacaoIncidenteAnalise.id = "+ indexacaoIncidenteAnalise.getId());
				
		    hql.append(" ORDER BY mi.numOrdemExibicao"); 

			Query query = session.createQuery(hql.toString());

			ministrosIndexacao = query.list();                

		} catch(HibernateException e) {
			throw new DaoException("HibernateException",
					SessionFactoryUtils.convertHibernateAccessException(e));
		} catch( RuntimeException e ) {
			throw new DaoException("RuntimeException", e);
		}

		return ministrosIndexacao;	
	}
	
}
