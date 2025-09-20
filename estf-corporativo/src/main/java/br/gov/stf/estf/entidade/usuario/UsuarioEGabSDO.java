package br.gov.stf.estf.entidade.usuario;

import javax.persistence.Transient;

import br.gov.stf.framework.security.AcegiSecurityUtils;

public class UsuarioEGabSDO extends UsuarioEGab {

	private static final long serialVersionUID = -4788892561520411276L;
	
	public static final String PERFIL_BASICO = "GS_EGAB_BASICO";
	public static final String PERFIL_DISTRIBUICAO = "GS_EGAB_DISTRIBUICAO";
	public static final String PERFIL_GESTAO = "GS_EGAB_GESTAO";
	public static final String PERFIL_ADMINISTRACAO = "GS_EGAB_ADMINISTRACAO";
	public static final String PERFIL_MINISTRO = "GS_EGAB_MINISTRO";
	public static final String PERFIL_MASTER = "GS_EGAB_MASTER";
	public static final String PERFIL_CONSULTA = "GS_EGAB_CONSUTLA";
	public static final String PERFIL_TEXTO = "GS_EGAB_TEXTO";

	public static final String PERFIL_REPGERAL_CONSULTA = "GS_REPGERAL_CONSULTA";
	public static final String PERFIL_REPGERAL_ADMINISTRADOR = "GS_REPGERAL_ADMINISTRADOR";
	public static final String PERFIL_REPGERAL_MASTER = "GS_REPGERAL_MASTER";
	public static final String PERFIL_REPGERAL_PLENARIO_VIRTUAL = "GS_REPGERAL_PLENARIO_VIRTUAL";
	public static final String PERFIL_REPGERAL_TEMAS = "GS_REPGERAL_TEMAS";
	public static final String PERFIL_REPGERAL_TESES = "GS_REPGERAL_TESES";

	public static final String PERFIL_COAJ = "GS_EGAB_COAJ";

	@Transient
	public Boolean getPossuiPerfilMaster() {
		return AcegiSecurityUtils.isUserInRole(PERFIL_MASTER);
	}

	@Transient
	public Boolean getPossuiPerfilMinistro() {

		if (AcegiSecurityUtils.isUserInRole(PERFIL_MINISTRO))
			return Boolean.TRUE;
		else
			return getPossuiPerfilMaster();
	}

	@Transient
	public Boolean getPossuiPerfilAdministracao() {

		if (AcegiSecurityUtils.isUserInRole(PERFIL_ADMINISTRACAO))
			return Boolean.TRUE;
		else
			return getPossuiPerfilMinistro();
	}

	@Transient
	public Boolean getPossuiPerfilGestao() {

		if (AcegiSecurityUtils.isUserInRole(PERFIL_GESTAO))
			return Boolean.TRUE;
		else
			return getPossuiPerfilAdministracao();
	}

	@Transient
	public Boolean getPossuiPerfilDistribuicao() {

		if (AcegiSecurityUtils.isUserInRole(PERFIL_DISTRIBUICAO))
			return Boolean.TRUE;
		else
			return getPossuiPerfilGestao();
	}

	@Transient
	public Boolean getPossuiPerfilBasico() {

		if (AcegiSecurityUtils.isUserInRole(PERFIL_BASICO))
			return Boolean.TRUE;
		else
			return getPossuiPerfilDistribuicao();
	}

	@Transient
	public Boolean getPossuiPerfilConsulta() {
		if (AcegiSecurityUtils.isUserInRole(PERFIL_CONSULTA))
			return Boolean.TRUE;
		else
			return getPossuiPerfilMaster();

	}

	@Transient
	public Boolean getPossuiPerfilTexto() {
		if (AcegiSecurityUtils.isUserInRole(PERFIL_TEXTO))
			return Boolean.TRUE;
		else
			return getPossuiPerfilMaster();

	}

	@Transient
	public Boolean getPossuiPerfilCoaj() {
		return AcegiSecurityUtils.isUserInRole(PERFIL_COAJ);
	}

	// controle de perfis para a repercussao geral

	@Transient
	public Boolean getPossuiPerfilRepGeralMaster() {
		return AcegiSecurityUtils.isUserInRole(PERFIL_REPGERAL_MASTER);
	}

	@Transient
	public Boolean getPossuiPerfilRepGeralPlenarioVirtual() {
		if (AcegiSecurityUtils.isUserInRole(PERFIL_REPGERAL_PLENARIO_VIRTUAL))
			return Boolean.TRUE;
		else
			return getPossuiPerfilRepGeralMaster();

	}

	@Transient
	public Boolean getPossuiPerfilRepGeralAdministrador() {
		if (AcegiSecurityUtils.isUserInRole(PERFIL_REPGERAL_ADMINISTRADOR))
			return Boolean.TRUE;
		else
			return getPossuiPerfilRepGeralPlenarioVirtual();

	}

	@Transient
	public Boolean getPossuiPerfilRepGeralConsulta() {
		if (AcegiSecurityUtils.isUserInRole(PERFIL_REPGERAL_CONSULTA))
			return Boolean.TRUE;
		else
			return getPossuiPerfilRepGeralAdministrador();

	}

	@Transient
	public Boolean getPossuiPerfilRepGeralTemas() {
		if (AcegiSecurityUtils.isUserInRole(PERFIL_REPGERAL_TEMAS))
			return Boolean.TRUE;
		else
			return getPossuiPerfilRepGeralMaster();

	}
	
	@Transient
	public Boolean getPossuiPerfilRepGeralTeses() {
		if (AcegiSecurityUtils.isUserInRole(PERFIL_REPGERAL_TESES))
			return Boolean.TRUE;
		else
			return getPossuiPerfilRepGeralMaster();

	}
}
