package br.gov.stf.estf.jurisdicionado.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.jurisdicionado.EnderecoJurisdicionado;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.EnderecoJurisdicionadoDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class EnderecoJurisdicionadoDaoHibernate extends GenericHibernateDao<EnderecoJurisdicionado, Long>
implements EnderecoJurisdicionadoDao {
	
	private static final long serialVersionUID = 1L;

	public EnderecoJurisdicionadoDaoHibernate() {
		super(EnderecoJurisdicionado.class);
	}


}
