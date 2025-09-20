package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.TipoImpressao;
import br.gov.stf.estf.processostf.model.dataaccess.TipoImpressaoDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoImpressaoDaoHibernate extends GenericHibernateDao<TipoImpressao, Short> implements TipoImpressaoDao { 
    public TipoImpressaoDaoHibernate() {
		super(TipoImpressao.class);
	}

	private static final long serialVersionUID = 1L;
}
