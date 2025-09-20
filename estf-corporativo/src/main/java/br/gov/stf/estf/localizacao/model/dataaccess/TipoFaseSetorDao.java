package br.gov.stf.estf.localizacao.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.localizacao.FluxoTipoFaseSetor;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.localizacao.TipoFaseSetor;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface TipoFaseSetorDao extends GenericDao<TipoFaseSetor, Long> {
	
	public Boolean verificarUnicidade(String descricao,Long idSetor) throws DaoException;
	
	public String verificarDependencia(Long idFase) throws DaoException;
	
	public TipoFaseSetor recuperarTipoFaseSetor(String descricao) throws DaoException;
	
    public TipoFaseSetor recuperarTipoFaseSetor(Long idFaseSetor) throws DaoException;
    
	public Boolean persistirTipoFaseSetor(TipoFaseSetor faseSetor) throws DaoException;
	
    public List <TipoFaseSetor> pesquisarTipoFaseSetor(String descricao, Long idSetor, 
    		Boolean comumEntreSetores, Boolean soAtivo) throws DaoException;
        
    public Boolean excluirTipoFaseSetor(TipoFaseSetor faseSetor)throws DaoException;
    
    public List<FluxoTipoFaseSetor> pesquisarFluxoTipoFaseSetor(
    		TipoFaseSetor tipoFaseSetorAntecessor, TipoFaseSetor tipoFaseSetorSucessor, Setor localizacao) 
    throws DaoException;
    
    public Boolean persistirFluxoTipoFaseSetor(FluxoTipoFaseSetor fluxoTipoFaseSetor) throws DaoException;
    
    public Boolean excluirFluxoTipoFaseSetor(FluxoTipoFaseSetor fluxoTipoFaseSetor)throws DaoException;
}
