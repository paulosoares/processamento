package br.gov.stf.estf.alerta.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.alerta.model.dataaccess.AlertaUsuarioDao;
import br.gov.stf.estf.alerta.model.service.AlertaUsuarioService;
import br.gov.stf.estf.entidade.alerta.AlertaUsuario;
import br.gov.stf.estf.entidade.alerta.TipoFiltroAlerta;
import br.gov.stf.estf.entidade.alerta.ValorFiltroAlerta;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("alertaUsuarioService")
public class AlertaUsuarioServiceImpl
extends GenericServiceImpl<AlertaUsuario, Long, AlertaUsuarioDao> 
implements AlertaUsuarioService {

    protected AlertaUsuarioServiceImpl(AlertaUsuarioDao dao) {
		super(dao);
	}
    
    public Boolean persistirAlertaUsuario(List<AlertaUsuario> listaAlertaUsuario) throws ServiceException{
    	Boolean alterado = Boolean.FALSE;
		
		try {
			for(AlertaUsuario alerta: listaAlertaUsuario){
				alterado = dao.persistirAlertaUsuario(alerta);
			}
			
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
		
		return alterado;		
    }
    
    public Boolean persistirAlertaUsuario(AlertaUsuario alertaUsuario) throws ServiceException{
    	Boolean alterado = Boolean.FALSE;
		
		try {
			//validarAlertaUsuario(alertaUsuario);
			alterado = dao.persistirAlertaUsuario(alertaUsuario);
			
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
		
		return alterado;
    }
    
    public void validarAlertaUsuario(AlertaUsuario alertaUsuario) throws ServiceException{
    	try {
	    	if(alertaUsuario==null){
	    		throw new ServiceException("Objeto nulo alertaUsuario");
	    	}
	    	if(alertaUsuario.getNotificarPorEmail()==null){
	    		throw new ServiceException("Objeto nulo Notificar por Email"); 
	    	}
	    	if(alertaUsuario.getTipoAndamento()==null){
	    		throw new ServiceException("Objeto nulo Andamento"); 
	    	}
	    	if(alertaUsuario.getUsuario()==null){
	    		throw new ServiceException("Objeto nulo Usuario"); 
	    	}
    		if(alertaUsuario.getId()==null){
    			List<AlertaUsuario> 
    			lista = dao.pesquisarAlertaUsuario(alertaUsuario.getTipoAndamento(),
    					alertaUsuario.getUsuario(), null, false, alertaUsuario.getSetor().getId());
				if(lista!=null&&lista.size()>0){
					throw new ServiceException("Ja existe um alerta cadastrado para o andamento "+
							                   alertaUsuario.getTipoAndamento().getDescricao());
				}
    		}
			
		}
    	catch( DaoException e ) {
			throw new ServiceException(e);
		}
    }
    
    @Deprecated
    public Boolean excluirAlertaUsuario(List<AlertaUsuario> listaAlertaUsuario) throws ServiceException{
    	Boolean alterado = Boolean.FALSE;
		
		try {
			for(AlertaUsuario alertaUsuario: listaAlertaUsuario){
				List<ValorFiltroAlerta> listaValorFiltroAlerta = pesquisarValorFiltroAlerta(
						alertaUsuario.getTipoAndamento(), alertaUsuario.getUsuario(), null, null, 
						alertaUsuario.getSetor().getId());
				if(listaValorFiltroAlerta!=null&&listaValorFiltroAlerta.size()>0){
					for(ValorFiltroAlerta valor :listaValorFiltroAlerta){
						alterado = excluirVolorFiltroAlerta(valor);
					}
				}
				alterado = dao.excluirAlertaUsuario(alertaUsuario);
			}
			
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
		
		return alterado;
    }
    
    public List<AlertaUsuario> pesquisarAlertaUsuario(Andamento andamento, Usuario usuario, 
    		Boolean notificarPorEmail, Boolean semValorFiltroAlerta, Long idSetor) 
    throws ServiceException {
    	
    	List<AlertaUsuario> listaAlertaUsuario = null;
		
		try {
			listaAlertaUsuario = dao.pesquisarAlertaUsuario(andamento, usuario, 
					notificarPorEmail, semValorFiltroAlerta, idSetor);
			
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
		
		return listaAlertaUsuario;
    }
    
    
    // ################################# CRUD VALOR FILTRO ALERTA ################################# //
    
    public Boolean excluirVolorFiltroAlerta(ValorFiltroAlerta valorFiltroAlerta) throws ServiceException {
    	try {
			return dao.excluirVolorFiltroAlerta(valorFiltroAlerta);
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
		
	}

	public Boolean persistirVolorFiltroAlerta(ValorFiltroAlerta valorFiltroAlerta) throws ServiceException {
		try {
			return dao.persistirVolorFiltroAlerta(valorFiltroAlerta);
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
	}

	public List<TipoFiltroAlerta> pesquisarTipoFiltroAlerta(Long id, String descricao) throws ServiceException {
		try {
			return dao.pesquisarTipoFiltroAlerta(id, descricao);
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
	}

	public List<ValorFiltroAlerta> pesquisarValorFiltroAlerta(Andamento andamento, Usuario usuario, 
			Boolean notificarPorEmail, Long idTipoFiltroAlerta, Long idSetor) 
	throws ServiceException {
		try {
			return dao.pesquisarValorFiltroAlerta(andamento, usuario, notificarPorEmail, 
					idTipoFiltroAlerta, idSetor);
		}
		catch( DaoException e ) {
			throw new ServiceException(e);
		}
	}
	
	public Boolean incluirValorFiltroAlerta(List<Andamento> listaAndamento, 
			List<ValorFiltroAlerta> listaValorFiltroAlerta , 
			AlertaUsuario alertaUsuario) throws ServiceException {
		
		Boolean incluido = Boolean.FALSE;
		
		try {
			
			if( alertaUsuario == null ){
				throw new ServiceException("Objeto nulo alerta usuário");
			}
			
			if(listaAndamento != null || listaAndamento.size() > 0) {
				
				for(Andamento andamento : listaAndamento) {
					
					AlertaUsuario alerta = null;
					alerta = instanciarAlertaUsuario(alertaUsuario, andamento);
					incluido = persistirAlertaUsuario(alerta);
					
					if(incluido) {
						
						if(listaValorFiltroAlerta != null && listaValorFiltroAlerta.size() > 0) {
							
							for(ValorFiltroAlerta valorFiltroAlerta : listaValorFiltroAlerta) {
								ValorFiltroAlerta valor = new ValorFiltroAlerta();
								valor.setAlertaUsuario(alerta);
								valor.setDescricao(valorFiltroAlerta.getDescricao());
								valor.setTipoFiltroAlerta(valorFiltroAlerta.getTipoFiltroAlerta());
								incluido = persistirVolorFiltroAlerta(valor);
							}
						}
					}
				}
			}
			else {
				throw new ServiceException("Informe os andamentos");
			}
		}
		catch( ServiceException e ) {
			throw e;
		}
		return incluido;
	}
	
	private AlertaUsuario instanciarAlertaUsuario(AlertaUsuario alerta, Andamento andamento){
		AlertaUsuario a = new AlertaUsuario();
		a.setNotificarPorEmail(alerta.getNotificarPorEmail());
		a.setUsuario(alerta.getUsuario());
		a.setTipoAndamento(andamento);
		a.setSetor(alerta.getSetor());
		a.setConcatenarFiltro(alerta.getConcatenarFiltro());
		return a;
	}


}