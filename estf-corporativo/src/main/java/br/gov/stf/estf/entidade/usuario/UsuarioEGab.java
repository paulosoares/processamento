package br.gov.stf.estf.entidade.usuario;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processosetor.ControleDistribuicao;
import br.gov.stf.estf.usuario.model.util.TipoUsuario;
import br.gov.stf.framework.security.AcegiSecurityUtils;
import br.gov.stf.framework.security.user.User;

@Entity
@Table(schema = "EGAB", name = "USUARIO_SETOR")
@IdClass(UsuarioEGab.UsuarioSetorId.class)
public class UsuarioEGab extends User {

	public static final String PERFIL_BASICO = "RS_EGAB-BASICO";
	public static final String PERFIL_DISTRIBUICAO = "RS_EGAB-DISTRIBUICAO";
	public static final String PERFIL_GESTAO = "RS_EGAB-GESTAO";
	public static final String PERFIL_ADMINISTRACAO = "RS_EGAB-ADMINISTRACAO";
	public static final String PERFIL_MINISTRO = "RS_EGAB-MINISTRO";
	public static final String PERFIL_MASTER = "RS_EGAB-MASTER";
	public static final String PERFIL_CONSULTA = "RS_EGAB-CONSULTA";
	public static final String PERFIL_TEXTO = "RS_EGAB-TEXTO";
	public static final String PERFIL_REPGERAL_CONSULTA = "RS_ESTFREPGERAL-CONSULTA";
	public static final String PERFIL_REPGERAL_ADMINISTRADOR = "RS_ESTFREPGERAL-ADMINISTRADOR";
	public static final String PERFIL_REPGERAL_PLENARIO_VIRTUAL = "RS_ESTFREPGERAL-PLENARIO_VIRTUAL";
	public static final String PERFIL_REPGERAL_MASTER = "RS_ESTFREPGERAL-MASTER";
	public static final String PERFIL_REPGERAL_TEMAS = "RS_ESTFREPGERAL-TEMAS";
	public static final String PERFIL_REPGERAL_TESES = "RS_ESTFREPGERAL-TESES";
	public static final String PERFIL_REPGERAL_ASSESSOR_MINISTRO = "RS_ESTFREPGERAL-ASSESSOR DE MINISTRO";
	public static final String PERFIL_REPGERAL_MINISTRO = "RS_ESTFREPGERAL-MINISTRO";
	public static final String PERFIL_REPGERAL_VOTO_PARA_RASCUNHO = "RS_ESTFREPGERAL-VOTO_PARA_RASCUNHO";

	public static final String PERFIL_COAJ = "GS_EGAB_COAJ";

	private Usuario usuario = new Usuario();
	private Setor setor;
	private TipoUsuario tipoUsuario;
	private Set<GrupoUsuario> grupos;
	private List<ControleDistribuicao> mapasUsuario;
	
	private Boolean setorAtual;

	@Id
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {

		if (usuario == null)
			throw new NullPointerException("Usuário não pode ser nulo.");

		this.usuario = usuario;
	}

	@Id
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TIP_USUARIO_SETOR", nullable = false)
	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	@ManyToMany(mappedBy = "usuarios", cascade = {}, fetch = FetchType.LAZY)
	public Set<GrupoUsuario> getGrupos() {
		return grupos;
	}

	public void setGrupos(Set<GrupoUsuario> grupos) {
		this.grupos = grupos;
	}

	@Column(name = "FLG_SETOR_ATUAL", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getSetorAtual() {
		return setorAtual;
	}

	public void setSetorAtual(Boolean setorAtual) {
		this.setorAtual = setorAtual;
	}

	// @ManyToMany(mappedBy="usuarios", cascade={}, fetch=FetchType.LAZY)
	@Transient
	public List<ControleDistribuicao> getMapasUsuario() {
		return mapasUsuario;
	}

	/*
	 * public void setMapasUsuario(List<MapaDistribuicao> mapasUsuario) { this.mapasUsuario = mapasUsuario; }
	 */

	public void copiarDados(UsuarioEGab usuario) {
		if (usuario == null)
			throw new NullPointerException("Usuário para cópia é nulo");

		setUsuario(usuario.getUsuario());
		setSetor(usuario.getSetor());
		setTipoUsuario(usuario.getTipoUsuario());
	}

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
	
	@Transient
	public Boolean getPossuiPerfilRepGeralTesesSemMaster() {
		return AcegiSecurityUtils.isUserInRole(PERFIL_REPGERAL_TESES) && !getPossuiPerfilRepGeralMaster();			
	}
	
	@Transient
	public Boolean getPossuiPerfilRepGeralAssessorMinistro(){
		return AcegiSecurityUtils.isUserInRole(PERFIL_REPGERAL_ASSESSOR_MINISTRO);
	}
	
	@Transient
	public Boolean getPossuiPerfilRepGeralVotoParaRascunho(){
		return AcegiSecurityUtils.isUserInRole(PERFIL_REPGERAL_VOTO_PARA_RASCUNHO);
	}
	
	@Transient
	public Boolean getPossuiPerfilRepGeralMinistro(){
		return AcegiSecurityUtils.isUserInRole(PERFIL_REPGERAL_MINISTRO);		
	}

	// fim controle de perfis para a repercussao geral
	@Embeddable
	public static class UsuarioSetorId implements Serializable {

		private Usuario usuario;
		private Setor setor;

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "SIG_USUARIO", nullable = false, insertable = false, updatable = false)
		public Usuario getUsuario() {
			return usuario;
		}

		public void setUsuario(Usuario usuario) {
			this.usuario = usuario;
		}

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "COD_SETOR", nullable = false, insertable = false, updatable = false)
		public Setor getSetor() {
			return setor;
		}

		public void setSetor(Setor setor) {
			this.setor = setor;
		}

		public boolean equals(Object obj) {
			if (obj instanceof UsuarioSetorId == false) {
				return false;
			}

			if (this == obj) {
				return true;
			}

			UsuarioSetorId id = (UsuarioSetorId) obj;

			return new EqualsBuilder().appendSuper(super.equals(obj)).append(getUsuario(), id.getUsuario()).append(getSetor(), id.getSetor()).isEquals();
		}

		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(getUsuario()).append(getSetor()).toHashCode();
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("usuario", getUsuario()).append("setor", getSetor()).toString();
		}
	}
}
