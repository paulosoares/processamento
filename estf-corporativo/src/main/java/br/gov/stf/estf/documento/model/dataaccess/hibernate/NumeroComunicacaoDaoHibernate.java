package br.gov.stf.estf.documento.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.documento.model.dataaccess.NumeroComunicacaoDao;
import br.gov.stf.estf.entidade.documento.NumeroComunicacao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class NumeroComunicacaoDaoHibernate extends
		GenericHibernateDao<NumeroComunicacao, NumeroComunicacao.NumeroComunicacaoId> implements
		NumeroComunicacaoDao {

	private static final long serialVersionUID = 1932089241789395850L;

	public NumeroComunicacaoDaoHibernate() {
		super(NumeroComunicacao.class);
	}





}
