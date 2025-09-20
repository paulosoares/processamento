package br.gov.stf.estf.localizacao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.ParametroSecao;
import br.gov.stf.estf.entidade.localizacao.Secao;
import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.localizacao.model.dataaccess.SecaoDao;
import br.gov.stf.estf.localizacao.model.dataaccess.SecaoSetorDao;
import br.gov.stf.estf.localizacao.model.service.SecaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("secaoService")
public class SecaoServiceImpl 
extends GenericServiceImpl<Secao, Long, SecaoDao>  
implements SecaoService {
    
    private final SecaoSetorDao secaoSetorDao;
    
    public SecaoServiceImpl(SecaoDao daoSecao, SecaoSetorDao secaoSetorDao) {
        super(daoSecao);
        this.secaoSetorDao = secaoSetorDao;
    }
    
    public Secao recuperarSecao(Long id) throws ServiceException {
    	
    	Secao secao = null;
    	
        try {
        	
        	secao = dao.recuperarSecao(id);

        }
        catch( DaoException e ) {
            throw new ServiceException(e);
        }
        
        return secao;
    }
    
    public List<Secao> pesquisarSecao(Long codigo, String descricao, String sigla, 
            Setor localizacao, Boolean ativo)
        throws ServiceException
        {
        try {
            return dao.pesquisarSecao(codigo, descricao, sigla, localizacao, ativo);
        }
        catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
    
    public void incluirSecao(Secao secao, Setor localizacao)
    throws ServiceException {
    	try {

    		List<Secao> listaSecoes = pesquisarSecao(null, null, null, null, null);
    		
    		for( Secao secaoLista: listaSecoes ) {
    			if(secaoLista.getDescricao().equals(secao.getDescricao())){
    				throw new ServiceException("Já existe uma seção com a descrição '" +secao.getDescricao() +"'.");
    			}
    			if(secaoLista.getSigla().equals(secao.getSigla())){
    				throw new ServiceException("Já existe uma seção com a sigla '" +secao.getSigla() +"'.");
    			}    			
    		}
    		dao.incluirSecao(secao);
        }
        catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Boolean alterarSecao(Secao secao)
    throws ServiceException{
    	
    	try {
    		return dao.alterarSecao(secao);
        }
        catch (DaoException e) {
            throw new ServiceException(e);
        }
        
    }
    
    public Secao recuperarSecao(Long id, String descricao, String sigla)
    throws ServiceException{
    	try {
    		return dao.recuperarSecao(id, descricao, sigla);
        }
        catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
    
    public Boolean excluirSecao(Secao secao)
    throws ServiceException{
    	Boolean excluir = null;
    	try {
    		if(secao==null||secao.getId()==null){
    			throw new ServiceException("Objeto nulo secao");
    		}
    		/*
    		String dependencia = dao.verificarDependencia(secao.getId());
    		if(dependencia!=null&&dependencia.trim().length()>0){
    			throw new ServiceException("A seção "+secao.getDescricao()+
    									   " possui dependência "+dependencia+" e não poderá ser excluída.");
    		}
    		*/
    		excluir = dao.excluirSecao(secao);
    		
        }
        catch (DaoException e) {
            throw new ServiceException(e);
        }catch(ServiceException e){
        	throw e;
        }
        return excluir;
    }
    
    public String verificarDependencia(Long idSecao, Long idSetor) 
    throws ServiceException{

    	String dependencias;
		
		try{
			
			dependencias = dao.verificarDependencia(idSecao, idSetor);
			
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		
		return dependencias;
	}
  
    
    public Boolean persistirParametroSecao(ParametroSecao parametro, SecaoSetor secaoSetor) 
    throws ServiceException{
    	
    	Boolean alterado = Boolean.FALSE;
    	try {
    		dao.persistirParametroSecao(parametro);
    		secaoSetorDao.alterarSecaoSetor(secaoSetor);
    		alterado = Boolean.TRUE;
    		return alterado;
        }
        catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
    
    public Boolean persistirParametroSecao(ParametroSecao parametro) 
    throws ServiceException{
    	Boolean alterado = Boolean.FALSE;
    	try {
    		dao.persistirParametroSecao(parametro);
    		alterado = Boolean.TRUE;
    		return alterado;
        }
        catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
    

}