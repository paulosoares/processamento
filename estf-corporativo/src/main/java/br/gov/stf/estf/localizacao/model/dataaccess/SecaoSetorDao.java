package br.gov.stf.estf.localizacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Secao;
import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface SecaoSetorDao extends GenericDao<SecaoSetor, Long> {

	public SecaoSetor recuperarSecaoSetor(Long id,Long idSecao,Long idSetor, Boolean ativo) throws DaoException;
        
    public SecaoSetor recuperarSecaoSetor(Long id,Usuario usuario,Secao secao, Long idSetor, Boolean ativo)throws DaoException;
	
	public Long incluirSecaoSetor(SecaoSetor secaoSetor) throws DaoException;
	
	public Boolean alterarSecaoSetor(SecaoSetor secaoSetor) throws DaoException;	
	
	public List<SecaoSetor> pesquisarSecaoSetor(Long id,Secao secao, Boolean ativo) throws DaoException;
	
	public List<SecaoSetor> pesquisarSecaoSetor(Long id, String sigUsuario, String descricaoSecao, Long idSecao, Long idSetor, Boolean ativo) throws DaoException;
	
	public List<Secao> pesquisarSecao(Long id, String sigUsuario, String descricaoSecao, Long idSetor, Boolean ativo) throws DaoException;
	
	public List<SecaoSetor> pesquisarTarefaSecao(Long id, Long idTarefa, String descricaoTarefa, String descricaoSecao, Long idSecao, Long idSetor) throws DaoException;
	
	public Boolean persistirSecaoSetor(SecaoSetor secaoSetor) throws DaoException;

	public Boolean excluirSecaoSetor(SecaoSetor secaoSetor) throws DaoException;
	
	
}
