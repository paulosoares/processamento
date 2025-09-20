package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.LegislacaoProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.LegislacaoProcessoDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class LegislacaoProcessoDaoHibernate 
	extends GenericHibernateDao<LegislacaoProcesso, Long> 
	implements LegislacaoProcessoDao {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8235009474333063854L;

	public LegislacaoProcessoDaoHibernate() {
		super(LegislacaoProcesso.class);
	}

}
