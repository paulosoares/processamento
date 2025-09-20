package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.AssinaturaDigitalDao;
import br.gov.stf.estf.entidade.documento.AssinaturaDigital;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class AssinaturaDigitalDaoHibernate extends GenericHibernateDao<AssinaturaDigital, Long> implements AssinaturaDigitalDao { 
    public AssinaturaDigitalDaoHibernate() {
		super(AssinaturaDigital.class);
	}

	private static final long serialVersionUID = 1L;
}
