package br.gov.stf.estf.usuario.model.service.impl;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.usuario.PessoaEndereco;
import br.gov.stf.estf.usuario.model.dataaccess.PessoaEnderecoDao;
import br.gov.stf.estf.usuario.model.service.PessoaEnderecoService;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("PessoaEndereco")
public class PessoaEnderecoServiceImpl extends GenericServiceImpl<PessoaEndereco, Long, PessoaEnderecoDao> implements PessoaEnderecoService {


	public PessoaEnderecoServiceImpl(PessoaEnderecoDao dao) {
		super(dao);
	}

}
