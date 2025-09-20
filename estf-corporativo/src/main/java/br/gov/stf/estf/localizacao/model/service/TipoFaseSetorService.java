package br.gov.stf.estf.localizacao.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.FluxoTipoFaseSetor;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.localizacao.TipoFaseSetor;
import br.gov.stf.estf.localizacao.model.dataaccess.TipoFaseSetorDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface TipoFaseSetorService extends GenericService<TipoFaseSetor, Long, TipoFaseSetorDao> {
	
	public TipoFaseSetor recuperarTipoFaseSetor(String descricao) throws ServiceException;
	
    public TipoFaseSetor recuperarTipoFaseSetor(Long idFaseSetor) throws ServiceException;
    
	public Boolean persistirTipoFaseSetor(TipoFaseSetor faseSetor) 
	throws ServiceException;
    
	public List<TipoFaseSetor> pesquisarTipoFaseSetor(String descricao, Long idSetor,  
			Boolean comumEntreSetores, Boolean soAtivo) throws ServiceException;
        
    public Boolean excluirTipoFaseSetor(TipoFaseSetor tipoFaseSetor)throws ServiceException;
    
    public List<FluxoTipoFaseSetor> pesquisarFluxoTipoFaseSetor(
    		TipoFaseSetor tipoFaseSetorAntecessor, TipoFaseSetor tipoFaseSetorSucessor, Setor localizacao) 
    throws ServiceException;
    
    public Boolean persistirFluxoTipoFaseSetor(FluxoTipoFaseSetor fluxoTipoFaseSetor) throws ServiceException;
    
    public Boolean excluirFluxoTipoFaseSetor(FluxoTipoFaseSetor fluxoTipoFaseSetor)throws ServiceException;    
    
    public String verificarDependencia(Long idFase) throws ServiceException;
}
