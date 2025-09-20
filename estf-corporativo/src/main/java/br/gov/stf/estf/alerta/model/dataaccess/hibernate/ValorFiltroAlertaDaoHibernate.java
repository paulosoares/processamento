package br.gov.stf.estf.alerta.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.alerta.model.dataaccess.ValorFiltroAlertaDao;
import br.gov.stf.estf.entidade.alerta.ValorFiltroAlerta;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ValorFiltroAlertaDaoHibernate extends GenericHibernateDao<ValorFiltroAlerta, Long>
		implements ValorFiltroAlertaDao {

	private static final long serialVersionUID = -8269367714203832941L;

	public ValorFiltroAlertaDaoHibernate() {
		super(ValorFiltroAlerta.class);
	}

}