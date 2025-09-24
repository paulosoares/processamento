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
 * Serviço que provê informações relacionadas ao usuário logado no sistema.
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
	 * Recupera o usuário autenticado. Esse usuário é encapsulado em um objeto Principal que contém
	 * as credenciais do usuário.
	 * 
	 * @return o principal
	 */
	public Principal getPrincipal() {
		return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * Recupera o usuário autenticado.
	 * 
	 * @return o usuário logado
	 */
	public Usuario getUsuario() {
		return getPrincipal().getUsuario();
	}

	/**
	 * Recupera o ministro cujo o gabinete o usuário está lotado.
	 * 
	 * @return o ministro do usuário
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
				return; // É permitido mudar para setores listados.
			}
		}
		if (getPrincipal().getUsuario().getSetor().getId().equals(idNovoSetor)) { // É permitido mudar para o setor do usuário.
			return;
		}
		throw new ServiceException("Setor não permitido para o usuário.");
	}

	protected void checarPermissaoChangeSetor() throws ServiceException {
		if (!permissionChecker.hasPermission(getPrincipal(), ActionIdentification.ASSINAR_DIGITALMENTE)
				&& !permissionChecker.hasPermission(getPrincipal(), ActionIdentification.ASSINAR_DIGITALMENTE_COMUNICACOES)) {
			throw new ServiceException("Usuário não tem permissão para executar a ação.");
		}
	}
	
}
