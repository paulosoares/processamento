package br.gov.stf.estf.alerta.model.service;

import java.util.List;

import br.gov.stf.estf.alerta.model.dataaccess.AlertaUsuarioDao;
import br.gov.stf.estf.entidade.alerta.AlertaUsuario;
import br.gov.stf.estf.entidade.alerta.TipoFiltroAlerta;
import br.gov.stf.estf.entidade.alerta.ValorFiltroAlerta;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;



public interface AlertaUsuarioService extends GenericService<AlertaUsuario, Long, AlertaUsuarioDao> {
	
	public Boolean persistirAlertaUsuario(List<AlertaUsuario> listaAlertaUsuario) throws ServiceException;
	
	public Boolean persistirAlertaUsuario(AlertaUsuario alertaUsuario) throws ServiceException;
	
	public Boolean excluirAlertaUsuario(List<AlertaUsuario> listaAlertaUsuario) throws ServiceException;
	
	public List<AlertaUsuario> pesquisarAlertaUsuario(Andamento andamento, Usuario usuario, 
			Boolean notificarPorEmail, Boolean semValorFiltroAlerta, Long idSetor) 
	throws ServiceException;
	
	public List<ValorFiltroAlerta> pesquisarValorFiltroAlerta(Andamento andamento, Usuario usuario, 
			Boolean notificarPorEmail, Long idTipoFiltroAlerta, Long idSetor)
	throws ServiceException;
	
	public Boolean excluirVolorFiltroAlerta(ValorFiltroAlerta valorFiltroAlerta) throws ServiceException;
	
	public Boolean persistirVolorFiltroAlerta(ValorFiltroAlerta valorFiltroAlerta) throws ServiceException;
	
	public List<TipoFiltroAlerta> pesquisarTipoFiltroAlerta(Long id,String descricao) throws ServiceException;
	
	public Boolean incluirValorFiltroAlerta(List<Andamento> listaAndamento, List<ValorFiltroAlerta> listaValorFiltroAlerta , 
			AlertaUsuario alertaUsuario)  throws ServiceException;
	
}
