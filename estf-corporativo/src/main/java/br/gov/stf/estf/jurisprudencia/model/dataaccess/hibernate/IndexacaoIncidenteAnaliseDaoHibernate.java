/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisprudencia.IndexacaoIncidenteAnalise;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.IndexacaoIncidenteAnaliseDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author Paulo.Estevao
 * @since 10.07.2012
 */
@Repository
public class IndexacaoIncidenteAnaliseDaoHibernate extends GenericHibernateDao<IndexacaoIncidenteAnalise, Long> implements
		IndexacaoIncidenteAnaliseDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4198879643241590031L;

	public IndexacaoIncidenteAnaliseDaoHibernate() {
		super(IndexacaoIncidenteAnalise.class);
	}
}
