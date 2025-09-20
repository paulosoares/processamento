package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.TextoModelo;
import br.gov.stf.estf.julgamento.model.dataaccess.PadroesDecisoesDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class PadroesDecisoesDaoHibernate extends GenericHibernateDao<TextoModelo, Long>
		implements PadroesDecisoesDao {

	private static final long serialVersionUID = -6358173064193800675L;

	public PadroesDecisoesDaoHibernate() {
		super(TextoModelo.class);
	}
}
