package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.PeticaoEletronica;
import br.gov.stf.estf.processostf.model.dataaccess.PeticaoEletronicaDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class PeticaoEletronicaDaoHibernate extends GenericHibernateDao<PeticaoEletronica, Long> implements PeticaoEletronicaDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4017676997804821982L;

	public PeticaoEletronicaDaoHibernate() {
		super(PeticaoEletronica.class);
	}


}