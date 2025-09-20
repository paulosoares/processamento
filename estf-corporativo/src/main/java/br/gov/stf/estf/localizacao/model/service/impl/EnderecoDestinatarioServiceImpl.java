package br.gov.stf.estf.localizacao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.EnderecoDestinatario;
import br.gov.stf.estf.entidade.util.EnderecoView;
import br.gov.stf.estf.localizacao.model.dataaccess.EnderecoDestinatarioDao;
import br.gov.stf.estf.localizacao.model.service.EnderecoDestinatarioService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("enderecoDestinatarioService")
public class EnderecoDestinatarioServiceImpl extends 
       GenericServiceImpl<EnderecoDestinatario, Long, EnderecoDestinatarioDao> implements EnderecoDestinatarioService {

	public EnderecoDestinatarioServiceImpl(EnderecoDestinatarioDao dao) {
		super(dao);
	}

	@Override
	//TODO: implementar o método que tem a finalidade de recuperar os endereços a partir de um destinatário
	public List<EnderecoDestinatario> recuperarEnderecoDoDestinatario(Long codDestinatario) throws ServiceException {
		try{
			return dao.recuperarEnderecoDoDestinatario(codDestinatario);
		}catch (DaoException e) {
			throw new ServiceException(e);
		}
		
	}

	@Override
	public EnderecoView recuperarEnderecoView(Integer cep)
			throws ServiceException {
		try{
			return dao.recuperarEnderecoView(cep);
		}catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	public List<String> pesquisarUF() throws ServiceException {
		try{
			return dao.pesquisarUF();
		}catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
