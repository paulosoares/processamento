package br.gov.stf.estf.alerta.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.alerta.AlertaUsuario;
import br.gov.stf.estf.entidade.alerta.TipoFiltroAlerta;
import br.gov.stf.estf.entidade.alerta.ValorFiltroAlerta;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface AlertaUsuarioDao extends GenericDao<AlertaUsuario, Long> {

	public Boolean persistirAlertaUsuario(AlertaUsuario alertaUsuario) throws DaoException;
	
	public Boolean excluirAlertaUsuario(AlertaUsuario alertaUsuario) throws DaoException;
	
	public List<AlertaUsuario> pesquisarAlertaUsuario(Andamento andamento, Usuario usuario, 
			Boolean notificarPorEmail, Boolean semValorFiltroAlerta, Long idSetor) 
	throws DaoException;
	
	public List<ValorFiltroAlerta> pesquisarValorFiltroAlerta(Andamento andamento, 
			Usuario usuario, Boolean notificarPorEmail, Long idTipoFiltroAlerta, Long idSetor) 
	throws DaoException;
	
	public Boolean excluirVolorFiltroAlerta(ValorFiltroAlerta valorFiltroAlerta) throws DaoException;
	
	public Boolean persistirVolorFiltroAlerta(ValorFiltroAlerta valorFiltroAlerta) throws DaoException;
	
	public List<TipoFiltroAlerta> pesquisarTipoFiltroAlerta(Long id,String descricao) throws DaoException;
}
