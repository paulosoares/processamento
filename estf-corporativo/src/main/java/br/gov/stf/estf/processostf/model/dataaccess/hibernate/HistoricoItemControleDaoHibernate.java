/**
 * 
 */
package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.HistoricoItemControle;
import br.gov.stf.estf.processostf.model.dataaccess.HistoricoItemControleDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

/**
 * @author paulo.estevao
 * @since 29.06.2012
 */
@Repository
public class HistoricoItemControleDaoHibernate extends GenericHibernateDao<HistoricoItemControle, Long> implements HistoricoItemControleDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4376708727519166241L;

	public HistoricoItemControleDaoHibernate() {
		super(HistoricoItemControle.class);
	}

}
