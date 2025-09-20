package br.gov.stf.estf.localizacao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.TipoStatusSetor;
import br.gov.stf.estf.localizacao.model.dataaccess.TipoStatusSetorDao;
import br.gov.stf.estf.localizacao.model.service.TipoStatusSetorService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoStatusSetorService")
public class TipoStatusSetorServiceImpl extends GenericServiceImpl<TipoStatusSetor, Long, TipoStatusSetorDao>  
implements TipoStatusSetorService {

	protected TipoStatusSetorServiceImpl(TipoStatusSetorDao dao) {
		super(dao);
	}
	
	
	
    /**
     * Recupera os possíveis Status de um ProcessoSetor. se o parâmetro "ativo"
     * for TRUE o método retorna todos os Status ativos, se FALSE os não ativos
     * e se NULL retorna todos.
     * 
     * Também é possível recuperar um status especifico usando o id
     * 
     * @param idStatus 
     * @param ativo
     * @return
     */
  
	public List <TipoStatusSetor> pesquisarTipoStatusSetor(String descricao, Long idSetor, 
			Boolean comumEntreSetores, Boolean ativo)
		throws ServiceException{
			
			List<TipoStatusSetor> listaDeStatus;
			
			try{		
				listaDeStatus = dao.pesquisarTipoStatusSetor(descricao, idSetor, 
						comumEntreSetores, ativo);
				
			}
			catch( DaoException ex ){
				
				throw new ServiceException( ex );
			}
		
		return listaDeStatus;
	}
	
	public TipoStatusSetor recuperarTipoStatusSetor(Long idStatus)
		throws ServiceException{
		
			TipoStatusSetor statusSetor = null;
			
			try{
				statusSetor = dao.recuperarTipoStatusSetor( idStatus );
			}
			catch( DaoException ex ){
				throw new ServiceException( ex );
			}
		
		return statusSetor;
		
	}
	
	public Boolean persistirTipoStatusSetor(TipoStatusSetor status)
		throws ServiceException{
		
		Boolean sucesso = Boolean.FALSE;
		
		try{
			validarCampos(status);
			sucesso = dao.persistirTipoStatusSetor( status );		
		}
		catch( DaoException ex ){
			throw new ServiceException( ex );
		}
		
		return sucesso;
		
	}
	
	public void validarCampos(TipoStatusSetor status) throws ServiceException{
		try{
			if(status == null){
				throw new ServiceException("Objeto nulo TipoStatusSetor");
			}else if(status.getDescricao()==null||status.getDescricao().equals("")){
				throw new ServiceException("A descrição deve ser informada.");
			}
			if(status.getAtivo()==null){
				status.setAtivo(Boolean.TRUE);
			}
			status.setDescricao(status.getDescricao().toUpperCase());
			if(status.getId()==null){
				if(dao.verificarUnicidade(status.getDescricao(),status.getSetor().getId())){
		    		throw new ServiceException("Já existe um status cadastrado com a descrição "+status.getDescricao()+".");
		    	}
			}
			
			
		}catch (ServiceException e) {
			throw e; 
		}catch( DaoException ex ){
			throw new ServiceException( ex );
		}
	}
	
	public Boolean excluirTipoStatusSetor(TipoStatusSetor status)
		throws ServiceException{
		
		Boolean sucesso = Boolean.FALSE;
		
		try{
			if(status == null||status.getId()==null){
				throw new ServiceException("Objeto nulo TipoStatusSetor");
			}if(dao.verificarDependecia(status.getId()).booleanValue()){
				throw new ServiceException("O status "+status.getDescricao()+" possui dependência com um ou mais processos/protocolos e não poderá ser excluído.");
			}
			sucesso = dao.excluirTipoStatusSetor( status );
		}
		catch( DaoException ex ){
			throw new ServiceException( ex );					
		}
		
		return sucesso;
		
	}

}
