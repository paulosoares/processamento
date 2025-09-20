package br.gov.stf.estf.processostf.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.processostf.TipoDevolucao;
import br.gov.stf.estf.processostf.model.dataaccess.TipoDevolucaoDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoDevolucaoDaoHibernate extends GenericHibernateDao<TipoDevolucao, Long> implements TipoDevolucaoDao {
	
	private static final long serialVersionUID = 2834446254979748037L;

	public TipoDevolucaoDaoHibernate() {
		super(TipoDevolucao.class);
	}
}
