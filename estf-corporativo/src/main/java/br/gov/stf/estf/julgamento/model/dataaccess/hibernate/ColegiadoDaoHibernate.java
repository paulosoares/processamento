package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.Colegiado;
import br.gov.stf.estf.julgamento.model.dataaccess.ColegiadoDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ColegiadoDaoHibernate extends GenericHibernateDao<Colegiado, String> implements ColegiadoDao{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8346493655099690360L;

	public ColegiadoDaoHibernate() {
		super(Colegiado.class);
	}

}
