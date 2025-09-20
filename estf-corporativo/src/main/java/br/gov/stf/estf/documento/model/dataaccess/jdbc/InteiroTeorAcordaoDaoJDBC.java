package br.gov.stf.estf.documento.model.dataaccess.jdbc;


import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.InteiroTeorAcordaoDao;
import br.gov.stf.estf.entidade.documento.InteiroTeorAcordao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository("inteiroTeorAcordaoDao")
public class InteiroTeorAcordaoDaoJDBC extends GenericHibernateDao<InteiroTeorAcordao, Long> implements InteiroTeorAcordaoDao {

	private static final long serialVersionUID = -8850908536461553448L;
	
	public InteiroTeorAcordaoDaoJDBC() {
		super(InteiroTeorAcordao.class);
	}
	
	public InteiroTeorAcordaoDaoJDBC(Class<InteiroTeorAcordao> clazz) {
		super(clazz);
	}


}
