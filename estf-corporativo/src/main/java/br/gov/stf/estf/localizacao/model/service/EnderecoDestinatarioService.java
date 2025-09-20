package br.gov.stf.estf.localizacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.EnderecoDestinatario;
import br.gov.stf.estf.entidade.util.EnderecoView;
import br.gov.stf.estf.localizacao.model.dataaccess.EnderecoDestinatarioDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface EnderecoDestinatarioService extends GenericService<EnderecoDestinatario, Long, EnderecoDestinatarioDao> {

	List<EnderecoDestinatario> recuperarEnderecoDoDestinatario(Long codDestinatario) throws ServiceException;
	
	public EnderecoView recuperarEnderecoView(Integer cep)throws ServiceException;
	
	public List<String> pesquisarUF()throws ServiceException;

}
