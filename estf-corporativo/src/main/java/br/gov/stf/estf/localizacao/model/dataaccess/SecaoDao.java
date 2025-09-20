package br.gov.stf.estf.localizacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.ParametroSecao;
import br.gov.stf.estf.entidade.localizacao.Secao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface SecaoDao extends GenericDao<Secao, Long> {

	public Secao recuperarSecao(Long id) throws DaoException;
	
	public String verificarDependencia(Long idSecao, Long idSetor) throws DaoException;
        
    public List<Secao> pesquisarSecao(Long id, String descricao, String sigla, Setor localizacao, Boolean ativo) throws DaoException;
	
	public Long incluirSecao(Secao secao) throws DaoException;
	
	public Boolean alterarSecao(Secao secao) throws DaoException;
	
	public Secao recuperarSecao(Long id, String secao, String sigla) throws DaoException;
	
	public Boolean excluirSecao(Secao secao) throws DaoException;
	
	public Boolean persistirParametroSecao(ParametroSecao parametro) throws DaoException;
	
}
