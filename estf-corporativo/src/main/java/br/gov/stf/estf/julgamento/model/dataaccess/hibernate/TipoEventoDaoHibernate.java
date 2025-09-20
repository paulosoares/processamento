package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.TipoEvento;
import br.gov.stf.estf.julgamento.model.dataaccess.TipoEventoDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoEventoDaoHibernate extends GenericHibernateDao<TipoEvento, String> implements TipoEventoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7686498983129755633L;
	
	public TipoEventoDaoHibernate() {
		super(TipoEvento.class);
	}

}
