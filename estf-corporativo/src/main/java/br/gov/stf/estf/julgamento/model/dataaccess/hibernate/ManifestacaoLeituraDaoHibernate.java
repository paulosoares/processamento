package br.gov.stf.estf.julgamento.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.julgamento.ManifestacaoLeitura;
import br.gov.stf.estf.julgamento.model.dataaccess.ManifestacaoLeituraDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class ManifestacaoLeituraDaoHibernate extends GenericHibernateDao<ManifestacaoLeitura, Long> implements ManifestacaoLeituraDao {

	private static final long serialVersionUID = -5862308673874736302L;

	public ManifestacaoLeituraDaoHibernate() {
		super(ManifestacaoLeitura.class);
	}
}