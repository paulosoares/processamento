package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.TipoSituacaoJulgamento;
import br.gov.stf.estf.julgamento.model.dataaccess.TipoSituacaoJulgamentoDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class TipoSituacaoJulgamentoDaoHibernate extends GenericHibernateDao<TipoSituacaoJulgamento, String> implements TipoSituacaoJulgamentoDao {

	private static final long serialVersionUID = 1L;

	public TipoSituacaoJulgamentoDaoHibernate() {
		super(TipoSituacaoJulgamento.class);
	}
	
	

}
