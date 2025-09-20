package br.gov.stf.estf.usuario.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.usuario.PerfilUsuarioSetor;
import br.gov.stf.estf.entidade.usuario.PerfilUsuarioSetor.PerfilUsuarioSetorId;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.usuario.model.dataaccess.PerfilUsuarioSetorDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface PerfilUsuarioSetorService extends GenericService<PerfilUsuarioSetor, PerfilUsuarioSetorId, PerfilUsuarioSetorDao> {

	public List<PerfilUsuarioSetor> pesquisarSetoresUsuario(Usuario usuario) throws ServiceException;

	/**
	 * Pesquisa quais usuários estão em um determinado setor, levando em consideração tanto o setor da tabela
	 * STF.USUARIOS  e GLOBAL.PERFIL_USUARIO_SETOR
	 * @param nome
	 * @param idSetor
	 * @return
	 * @throws ServiceException
	 */
	List<PerfilUsuarioSetor> pesquisarUsuarioDoSetor(String nome, Long idSetor) throws ServiceException;

	public List<PerfilUsuarioSetor> pesquisarSetoresUsuario(Usuario usuario, String siglaSistema) throws ServiceException;
	
	public List<PerfilUsuarioSetor> pesquisarUsuarioDoSetorSistema(String nome, Long idSetor, String sistema) throws ServiceException;
	
}
