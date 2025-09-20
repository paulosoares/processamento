package br.gov.stf.estf.localizacao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.ParametroSecao;
import br.gov.stf.estf.entidade.localizacao.Secao;
import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.usuario.UsuarioEGab;
import br.gov.stf.estf.localizacao.model.dataaccess.SecaoSetorDao;
import br.gov.stf.estf.localizacao.model.service.SecaoService;
import br.gov.stf.estf.localizacao.model.service.SecaoSetorService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("secaoSetorService")
public class SecaoSetorServiceImpl 
extends GenericServiceImpl<SecaoSetor, Long, SecaoSetorDao>  
implements SecaoSetorService {
    
    private SecaoService secaoService;
    public SecaoSetorServiceImpl(SecaoSetorDao daoSecaoSetor, SecaoService secaoService) {
        super(daoSecaoSetor);
        this.secaoService = secaoService;
    }
    
    public SecaoSetor recuperarSecaoSetor(Long id,Long idSecao,Long idSetor, Boolean ativo) throws ServiceException {
    	
    	SecaoSetor secaoSetor = null;
    	
        try {
        	secaoSetor = dao.recuperarSecaoSetor(id, idSecao, idSetor, ativo);
        }catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return secaoSetor;
    }


    public SecaoSetor recuperarSecaoSetor(Long id, UsuarioEGab usuario, Secao secao, Long idSetor, boolean validarExistenciaSecaoUsuario, Boolean ativo) throws ServiceException {
        
        SecaoSetor secaoSetor = null;
        boolean semParametro = false;
        try {
                secaoSetor = dao.recuperarSecaoSetor(id,usuario.getUsuario(),secao, idSetor, ativo);
                
                if(validarExistenciaSecaoUsuario){
                      if(usuario!=null){
                          if(secaoSetor==null||secaoSetor.getId()==null)
                              semParametro = true;
                          else if(usuario==null||usuario.getSetor()==null)
                              semParametro = true;
                          else if(usuario.getSetor()!=null&&!usuario.getSetor().getId().equals(secaoSetor.getSetor().getId()))
                              semParametro = true;    
                          if(semParametro)
                              throw new ServiceException("Usuário não está associado a alguma seção.");
                      }
                }
            
                
        }catch( DaoException e ) {
            throw new ServiceException(e);
        }
        return secaoSetor;
    }
    
    public List<SecaoSetor> pesquisarSecaoSetor(Long id, String sigUsuario,String descricaoSecao, Long idSecao, Long idSetor, Boolean ativo) throws ServiceException {
    	try{
    		List<SecaoSetor> result = dao.pesquisarSecaoSetor(id, sigUsuario,descricaoSecao,idSecao,idSetor, ativo);
    		
    		return result;
    	}
    	catch(DaoException e ) {
            throw new ServiceException(e);
        }
	}
    
    public List<Secao> pesquisarSecao(Long id, String sigUsuario, String descricaoSecao, Long idSetor, Boolean ativo) throws ServiceException {
        try{
            List<Secao> result = dao.pesquisarSecao(id, sigUsuario, descricaoSecao, idSetor, ativo);
            
            return result;
        }
        catch(DaoException e ) {
            throw new ServiceException(e);
        }
    }    
    
    public List<SecaoSetor> pesquisarTarefaSecao(Long id, Long idTarefa, String descricaoTarefa, String descricaoSecao, Long idSecao, Long idSetor) throws ServiceException {
    	try{
    		List<SecaoSetor> result = dao.pesquisarTarefaSecao(id, idTarefa, descricaoTarefa, descricaoSecao, idSecao, idSetor);
    		
    		return result;
    	}
    	catch(DaoException e ) {
            throw new ServiceException(e);
        }
	}
    
    public List pesquisarSecaoSetor(Long id,Secao secao, Boolean ativo) throws ServiceException{
    	try{
    		List result = dao.pesquisarSecaoSetor(id, secao, ativo);
    		return result;
    	}
    	catch(DaoException e ) {
            throw new ServiceException(e);
        }
    }

    public Boolean persistirSecaoSetor(SecaoSetor secaoSetor) throws ServiceException{
		
		Boolean alterado = Boolean.FALSE;
		
		try {
			validarSecaoSetor(secaoSetor);
			alterado = dao.persistirSecaoSetor(secaoSetor);
			
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
		
		return alterado;                
		
	}
    
    public void validarSecaoSetor(SecaoSetor secaoSetor)throws ServiceException{
    	try{
    		if(secaoSetor==null){
    			throw new ServiceException("Objeto nulo SecaoSetor.");	
    		}
    		if(secaoSetor.getSecao()==null){
    			throw new ServiceException("Objeto nulo Secao.");
    		}
    		if(secaoSetor.getSetor()==null){
    			throw new ServiceException("Objeto nulo Setor");
    		}
    		if(secaoSetor.getSecao().getId()==null){
    			if(secaoSetor.getSecao().getSigla()!=null&&!secaoSetor.getSecao().getSigla().equals("")){
	    			secaoSetor.getSecao().setSigla(secaoSetor.getSecao().getSigla().toUpperCase());
	    			Secao secao= secaoService.recuperarSecao(null, null, secaoSetor.getSecao().getSigla());
	    			if(secao!=null){
	    				throw new ServiceException("Já existe uma seção cadastrada com a sigla informada.");
	    			}
    			}else{
    				throw new ServiceException("A sigla da seção deve ser informada.");
    			}
    			if(secaoSetor.getSecao().getDescricao()!=null&&!secaoSetor.getSecao().getDescricao().equals("")){
	    			secaoSetor.getSecao().setDescricao(secaoSetor.getSecao().getDescricao().toUpperCase());
	    			Secao secao= secaoService.recuperarSecao(null, secaoSetor.getSecao().getDescricao(), null);
	    			if(secao!=null){
	    				throw new ServiceException("Já existe um seção cadastrada com a descrição informada.");
	    			}
    			}else{
    				throw new ServiceException("A descricão da seção deve ser informada.");
    			}
    		}
    		
    		if(secaoSetor.getParametro()==null){
	    		secaoSetor.setParametro(new ParametroSecao());
	    		secaoSetor.getParametro().setSala(Boolean.TRUE);
	    		secaoSetor.getParametro().setArmario(Boolean.TRUE);
	    		secaoSetor.getParametro().setColuna(Boolean.TRUE);
	    		secaoSetor.getParametro().setEstante(Boolean.TRUE);
	    		secaoSetor.getParametro().setPrateleira(Boolean.TRUE);
	    		secaoSetor.getParametro().setRecebimentoAutomatico(Boolean.FALSE);
	    		secaoSetor.getParametro().setRemessaAutomatica(Boolean.FALSE);
	    		secaoSetor.getParametro().setTarefa(Boolean.FALSE);
	    		secaoSetor.getParametro().setTarefaProcesso(Boolean.FALSE);
	    	 }
    		
    	}catch (ServiceException e) {
			throw e;
		}
    }
    
    
    public Boolean excluirSecaoSetor(SecaoSetor secaoSetor) throws ServiceException {
    	Boolean excluido = Boolean.FALSE;
		
		try {
			boolean excluirSecao = false;
			Secao secao = null;
			if(secaoSetor==null||secaoSetor.getId()==null){
				throw new ServiceException("Objeto nulo SecaoSetor.");	
			}
			List lista = pesquisarSecaoSetor(null,null, null, secaoSetor.getSecao().getId(),null, null);
			if(lista==null || lista.size()==1){
				excluirSecao = true;
				secao = secaoSetor.getSecao();
			}
			excluido = dao.excluirSecaoSetor(secaoSetor);
			
		   	if(excluirSecao){
	       		 excluido =  secaoService.excluirSecao(secao);
	       	}
			
		}catch( DaoException e ) {
			throw new ServiceException(e);
		}
		
		return excluido;    
	}
    
	
}