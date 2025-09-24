package br.jus.stf.estf.decisao.support.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.usuario.model.service.UsuarioService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.support.action.support.ActionIdentification;
import br.jus.stf.estf.decisao.support.security.PermissionChecker;
import br.jus.stf.estf.decisao.support.security.Principal;

/**
 * Servi�o que prov� informa��es relacionadas ao usu�rio logado no sistema.
 * 
 * @author Tomas.Godoi
 * 
 */
@Service
public class UsuarioLogadoService {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private MinistroService ministroService;
	
	@Autowired
	private SetorService setorService;
	
	@Autowired
	private ConfiguracaoSistemaService configuracaoSistemaService;
	
	@Autowired
	private PermissionChecker permissionChecker;
	
	/**
	 * Recupera o usu�rio autenticado. Esse usu�rio � encapsulado em um objeto Principal que cont�m
	 * as credenciais do usu�rio.
	 * 
	 * @return o principal
	 */
	public Principal getPrincipal() {
		return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * Recupera o usu�rio autenticado.
	 * 
	 * @return o usu�rio logado
	 */
	public Usuario getUsuario() {
		return getPrincipal().getUsuario();
	}

	/**
	 * Recupera o ministro cujo o gabinete o usu�rio est� lotado.
	 * 
	 * @return o ministro do usu�rio
	 */
	public Ministro getMinistro() {
		return getPrincipal().getMinistro();
	}

	public void changeSetor(Long idNovoSetor) throws ServiceException {
		checarPermissaoChangeSetor();
		checarSetorPermitido(idNovoSetor);
		getPrincipal().setIdSetor(idNovoSetor);
		if (getPrincipal().getIdSetor() != null) {
			getPrincipal().setGruposEgabDoUsuario(usuarioService.recuperarGrupoUsuario(getPrincipal().getUsuario()));
			getPrincipal().setMinistro(ministroService.recuperarMinistro(setorService.recuperarPorId(getPrincipal().getIdSetor())));
			getPrincipal().setSetorRestringeTextoAoResponsavel(configuracaoSistemaService.isTextoRestritoResponsavel());
		} else {
			getPrincipal().setGruposEgabDoUsuario(null);
			getPrincipal().setMinistro(null);
			getPrincipal().setSetorRestringeTextoAoResponsavel(false);
		}
	}
	
	private void checarSetorPermitido(Long idNovoSetor) throws ServiceException {
		for (Setor setor : getPrincipal().getSetores()) {
			if (setor.getId().equals(idNovoSetor)) {
				return; // � permitido mudar para setores listados.
			}
		}
		if (getPrincipal().getUsuario().getSetor().getId().equals(idNovoSetor)) { // � permitido mudar para o setor do usu�rio.
			return;
		}
		throw new ServiceException("Setor n�o permitido para o usu�rio.");
	}

	protected void checarPermissaoChangeSetor() throws ServiceException {
		if (!permissionChecker.hasPermission(getPrincipal(), ActionIdentification.ASSINAR_DIGITALMENTE)
				&& !permissionChecker.hasPermission(getPrincipal(), ActionIdentification.ASSINAR_DIGITALMENTE_COMUNICACOES)) {
			throw new ServiceException("Usu�rio n�o tem permiss�o para executar a a��o.");
		}
	}
	
}
