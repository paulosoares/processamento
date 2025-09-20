package br.gov.stf.estf.jurisdicionado.model.service;

import br.gov.stf.estf.entidade.jurisdicionado.IdentificacaoPessoa;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.IdentificacaoPessoaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface IdentificacaoPessoaService extends GenericService<IdentificacaoPessoa, Long, IdentificacaoPessoaDao> {
	
	boolean verificaExistenciaCadastro(String identificacao, String tipo) throws ServiceException;
	
	IdentificacaoPessoa recuperarPrimeiroCadastro (String identificacao, String tipo) throws ServiceException;

}
