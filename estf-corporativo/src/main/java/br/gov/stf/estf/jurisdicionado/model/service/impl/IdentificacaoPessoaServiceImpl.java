package br.gov.stf.estf.jurisdicionado.model.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisdicionado.IdentificacaoPessoa;
import br.gov.stf.estf.entidade.jurisdicionado.TipoIdentificacao;
import br.gov.stf.estf.jurisdicionado.model.dataaccess.IdentificacaoPessoaDao;
import br.gov.stf.estf.jurisdicionado.model.service.IdentificacaoPessoaService;
import br.gov.stf.estf.jurisdicionado.model.service.TipoIdentificacaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("identificacaoPessoaService")
public class IdentificacaoPessoaServiceImpl extends GenericServiceImpl<IdentificacaoPessoa, Long, IdentificacaoPessoaDao> 
	implements IdentificacaoPessoaService {
	
	private final TipoIdentificacaoService tipoIdentificacaoService;
	
	public IdentificacaoPessoaServiceImpl(IdentificacaoPessoaDao dao, TipoIdentificacaoService tipoIdentificacaoService) {
		super(dao);
		this.tipoIdentificacaoService = tipoIdentificacaoService;
	}

	@Override
	public boolean verificaExistenciaCadastro(String identificacao,
			String tipo) throws ServiceException {
		
		IdentificacaoPessoa identiPessoa = null;
		identiPessoa = recuperarPrimeiroCadastro(identificacao, tipo);
		if (identiPessoa != null){
			return true;
		}else{
			return false;
		}
	}
	
	public IdentificacaoPessoa recuperarPrimeiroCadastro (String identificacao, String tipo) throws ServiceException{
		
		TipoIdentificacao tipoI = new TipoIdentificacao();
		IdentificacaoPessoa idPessoa = null;
		List<IdentificacaoPessoa> listaI = new ArrayList<IdentificacaoPessoa>();
		
		try {
			tipoI = tipoIdentificacaoService.pesquisaPelaSigla(tipo);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

		try {
			listaI =  dao.verificaExistenciaCadastro(identificacao, tipoI);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		
		if (listaI.size() > 0){
			idPessoa = listaI.get(0);
		}
		
		return idPessoa;
	}

}
