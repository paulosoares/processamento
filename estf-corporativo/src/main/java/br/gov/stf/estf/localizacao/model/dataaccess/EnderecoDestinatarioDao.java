package br.gov.stf.estf.localizacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.EnderecoDestinatario;
import br.gov.stf.estf.entidade.util.EnderecoView;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface EnderecoDestinatarioDao extends GenericDao<EnderecoDestinatario, Long> {
	
	List<EnderecoDestinatario> recuperarEnderecoDoDestinatario(Long codDestinatario) throws DaoException;
	
	public EnderecoView recuperarEnderecoView(Integer cep)throws DaoException;
	
	public List<String> pesquisarUF()throws DaoException;
}
