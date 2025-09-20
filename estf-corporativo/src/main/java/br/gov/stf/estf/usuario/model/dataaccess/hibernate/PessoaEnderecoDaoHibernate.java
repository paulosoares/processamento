package br.gov.stf.estf.usuario.model.dataaccess.hibernate;

import org.springframework.stereotype.Repository;

import br.gov.stf.estf.entidade.usuario.PessoaEndereco;
import br.gov.stf.estf.usuario.model.dataaccess.PessoaEnderecoDao;
import br.gov.stf.framework.model.dataaccess.hibernate.GenericHibernateDao;

@Repository
public class PessoaEnderecoDaoHibernate  extends GenericHibernateDao<PessoaEndereco, Long> implements PessoaEnderecoDao{
	

	private static final long serialVersionUID = -576864687364257672L;

	public PessoaEnderecoDaoHibernate() {
		super(PessoaEndereco.class);
	}

	
}
