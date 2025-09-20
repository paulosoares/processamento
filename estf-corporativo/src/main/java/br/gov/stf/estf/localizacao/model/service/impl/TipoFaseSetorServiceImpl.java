package br.gov.stf.estf.localizacao.model.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.FluxoTipoFaseSetor;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.localizacao.TipoFaseSetor;
import br.gov.stf.estf.localizacao.model.dataaccess.TipoFaseSetorDao;
import br.gov.stf.estf.localizacao.model.service.TipoFaseSetorService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("tipoFaseSetorService")
public class TipoFaseSetorServiceImpl  
	extends GenericServiceImpl<TipoFaseSetor, Long, TipoFaseSetorDao> 
	implements TipoFaseSetorService {
	
	public TipoFaseSetorServiceImpl(TipoFaseSetorDao dao){
		super(dao);
	}

	public TipoFaseSetor recuperarTipoFaseSetor(String descricao)
			throws ServiceException {

		TipoFaseSetor faseSetor = null;

		try {

			faseSetor = dao.recuperarTipoFaseSetor(descricao);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return faseSetor;

	}

	public TipoFaseSetor recuperarTipoFaseSetor(Long idFaseSetor)
			throws ServiceException {

		TipoFaseSetor faseSetor = null;

		try {

			faseSetor = dao.recuperarTipoFaseSetor(idFaseSetor);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return faseSetor;
	}

	public Boolean persistirTipoFaseSetor(TipoFaseSetor faseSetor) 
	throws ServiceException {

		Boolean alterado = Boolean.FALSE;

		try {
			validarTipoFaseSetor(faseSetor);	
			alterado = dao.persistirTipoFaseSetor(faseSetor);
		} 
		catch (DaoException e) {
			throw new ServiceException(e);
		}

		return alterado;
	}
	
	public void validarTipoFaseSetor(TipoFaseSetor faseSetor) throws ServiceException{
		try{
			if(faseSetor==null){
				throw new ServiceException("Objeto nulo TipoFaseSetor.");
			}else if(faseSetor.getDescricao()==null||faseSetor.getDescricao().equals("")){
				throw new ServiceException("A descrição deve ser informada.");
			}else if(faseSetor.getAtivo()==null){
				faseSetor.setAtivo(Boolean.TRUE);
			}
			faseSetor.setDescricao(faseSetor.getDescricao().toUpperCase());
			if(faseSetor.getId() == null){
				if(dao.verificarUnicidade(faseSetor.getDescricao(),faseSetor.getSetor().getId()).booleanValue()){
					throw new ServiceException("Já existe uma fase cadastrada com a descrição informada.");
				}
			}
			
		}catch(ServiceException e){
			throw e;
		}catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public Boolean excluirTipoFaseSetor(TipoFaseSetor faseSetor)
			throws ServiceException{

		try {
			
			if(faseSetor==null||faseSetor.getId()==null){
				throw new ServiceException("Objeto nulo ao excluir: TipoFaseSetor.");
			}
			/*
			String possuiDependencia = dao.verificarDependencia(faseSetor.getId());
			if(possuiDependencia!=null&&possuiDependencia.trim().length()>0){
				throw new ServiceException("A fase "+faseSetor.getDescricao()+" não pode ser excluída pois "+possuiDependencia);
			}
			*/
			return dao.excluirTipoFaseSetor(faseSetor);
		}catch (DaoException e){
			throw new ServiceException(e);
		}catch (ServiceException e){
			throw e;
		}
	}

	public String verificarDependencia(Long idFase) throws ServiceException {
		String dependencias;
		
		if(idFase == null ){
			throw new ServiceException("Objeto nulo ao excluir: TipoFaseSetor.");
		}
		
		try{
			
			dependencias = dao.verificarDependencia(idFase);
			
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		
		return dependencias;
	}

	public List<TipoFaseSetor> pesquisarTipoFaseSetor(String descricao, Long idSetor,  
			Boolean comumEntreSetores, Boolean soAtivo)
			throws ServiceException {
		List<TipoFaseSetor> listaTipoFaseSetor = null;

		try {

			listaTipoFaseSetor = dao.pesquisarTipoFaseSetor(descricao, idSetor,  
					comumEntreSetores, soAtivo);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return listaTipoFaseSetor;
	}
	
    public List<FluxoTipoFaseSetor> pesquisarFluxoTipoFaseSetor(
    		TipoFaseSetor tipoFaseSetorAntecessor,
    		TipoFaseSetor tipoFaseSetorSucessor,
    		Setor localizacao) 
    throws ServiceException {
		List<FluxoTipoFaseSetor> lista = new LinkedList<FluxoTipoFaseSetor>();

		try {

			if( localizacao == null || localizacao.getId() == null )
				throw new ServiceException("É obrigatório informar o localizacao/gabinete para pesquisa do fluxo de fases.");
			
			lista = dao.pesquisarFluxoTipoFaseSetor(tipoFaseSetorAntecessor, tipoFaseSetorSucessor, localizacao);
			
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

		return lista;    	
    }
    
	public void validarFluxoTipoFaseSetor(FluxoTipoFaseSetor fluxoTipoFaseSetor) 
	throws ServiceException {
		try {
			if( fluxoTipoFaseSetor==null ) {
				throw new ServiceException("Objeto nulo ao persistir: FluxoTipoFaseSetor.");
			}
			else if(fluxoTipoFaseSetor.getTipoFaseSucessor() == null ){
				throw new ServiceException("A fase sucessora deve ser informada.");
			}
			else if(fluxoTipoFaseSetor.getSetor()==null){
				throw new ServiceException("O localizacao deve ser informado.");
			}
			
		}catch(ServiceException e){
			throw e;
		}
	}    
    
    public Boolean persistirFluxoTipoFaseSetor(FluxoTipoFaseSetor fluxoTipoFaseSetor) 
    throws ServiceException {
		Boolean persistido = Boolean.FALSE;

		try {
			validarFluxoTipoFaseSetor(fluxoTipoFaseSetor);	
			
			persistido = dao.persistirFluxoTipoFaseSetor(fluxoTipoFaseSetor);

		} 
		catch (DaoException e) {
			throw new ServiceException(e);
		}

		return persistido;    	
    }
    
    public Boolean excluirFluxoTipoFaseSetor(FluxoTipoFaseSetor fluxoTipoFaseSetor)
    throws ServiceException {
		try {
			if(fluxoTipoFaseSetor == null){
				throw new ServiceException("Objeto nulo ao excluir: FluxoTipoFaseSetor.");
			}
			
			return dao.excluirFluxoTipoFaseSetor(fluxoTipoFaseSetor);
		}
		catch (DaoException e){
			throw new ServiceException(e);
		}    	
    } 
    
}