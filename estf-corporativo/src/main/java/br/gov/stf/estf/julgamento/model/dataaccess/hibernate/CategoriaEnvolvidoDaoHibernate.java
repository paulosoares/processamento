package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.CategoriaEnvolvido;
import br.gov.stf.estf.julgamento.model.dataaccess.CategoriaEnvolvidoDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class CategoriaEnvolvidoDaoHibernate extends GenericHibernateDao<CategoriaEnvolvido, String> implements CategoriaEnvolvidoDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7313954405583385340L;
	
	
	public CategoriaEnvolvidoDaoHibernate() {
		super(CategoriaEnvolvido.class);
	}

}
