package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.judiciario.NumeroProcesso;
import br.gov.stf.estf.processostf.model.dataaccess.NumeroProcessoDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class NumeroProcessoDaoHibernate extends GenericHibernateDao<NumeroProcesso, Long> implements NumeroProcessoDao {
	
	private static final long serialVersionUID = 1L;
	
	public NumeroProcessoDaoHibernate() {
		super(NumeroProcesso.class);
	}

}
