package br.gov.stf.estf.jurisdicionado.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.jurisdicionado.IdentificacaoPessoa;
import br.gov.stf.estf.entidade.jurisdicionado.TipoIdentificacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface IdentificacaoPessoaDao extends GenericDao<IdentificacaoPessoa, Long> {
	
	List<IdentificacaoPessoa> verificaExistenciaCadastro(String identificacao, TipoIdentificacao tipo) throws DaoException;

}
