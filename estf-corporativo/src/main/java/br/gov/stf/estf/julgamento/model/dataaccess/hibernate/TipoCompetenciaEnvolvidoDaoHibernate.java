package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.TipoCompetenciaEnvolvido;
import br.gov.stf.estf.julgamento.model.dataaccess.TipoCompetenciaEnvolvidoDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoCompetenciaEnvolvidoDaoHibernate extends GenericHibernateDao<TipoCompetenciaEnvolvido, Long> implements TipoCompetenciaEnvolvidoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6942185980491391132L;
	
	public TipoCompetenciaEnvolvidoDaoHibernate() {
		super(TipoCompetenciaEnvolvido.class);
	}

}
