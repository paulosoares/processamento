package br.gov.stf.estf.localizacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.ParametroSecao;
import br.gov.stf.estf.entidade.localizacao.Secao;
import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.localizacao.model.dataaccess.SecaoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface SecaoService extends GenericService<Secao, Long, SecaoDao> {

	public Secao recuperarSecao(Long id) throws ServiceException;
	
    /**
     * metodo responsavel por pesquisar a Seções
     * @param id codigo da seção
     * @param descricao descricao da seção
     * @param sigla sigla da Seção
     * @param localizacao localizacao da Seção
     * @param ativo 
     * @return lista de Seções
     * @throws ServiceException
     * @since 1.0
     * @athor guilhermea
     */
    public List pesquisarSecao(Long id, String descricao, String sigla, Setor localizacao, Boolean ativo)
    throws ServiceException;
    
    public void incluirSecao(Secao secao, Setor localizacao)
    throws ServiceException;
    
    public Boolean alterarSecao(Secao secao)
    throws ServiceException;
    
    public Secao recuperarSecao(Long id, String secao, String sigla)
    throws ServiceException;
    
    public Boolean excluirSecao(Secao secao)
    throws ServiceException;
    
    public Boolean persistirParametroSecao(ParametroSecao parametro, SecaoSetor secaoSetor) 
    throws ServiceException;
    
    public Boolean persistirParametroSecao(ParametroSecao parametro) 
    throws ServiceException;
    
    public String verificarDependencia(Long idSecao, Long idSetor) 
    throws ServiceException;
}
