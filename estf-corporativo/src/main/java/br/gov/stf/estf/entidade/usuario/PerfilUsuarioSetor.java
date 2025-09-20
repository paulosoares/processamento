package br.gov.stf.estf.entidade.usuario;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.HashCodeBuilder;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;

@Entity
@Table(name = "PERFIL_USUARIO_SETOR", schema = "GLOBAL")
public class PerfilUsuarioSetor extends
		ESTFBaseEntity<PerfilUsuarioSetor.PerfilUsuarioSetorId> {

	private static final long serialVersionUID = 1L;
	private PerfilUsuarioSetorId id;
	private Setor setor;
	private Usuario usuario;
	private Perfil perfil;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns( { @JoinColumn(name = "COD_SETOR", referencedColumnName = "COD_SETOR", insertable = false, updatable = false) })
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns( { @JoinColumn(name = "SIG_USUARIO", referencedColumnName = "SIG_USUARIO", insertable = false, updatable = false) })
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns( { @JoinColumn(name = "SEQ_PERFIL", referencedColumnName = "SEQ_PERFIL", insertable = false, updatable = false) })
	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	@Id
	public PerfilUsuarioSetorId getId() {
		return id;
	}
	
	public void setId(PerfilUsuarioSetorId id){
		this.id = id;
	}

	@Embeddable
	public static class PerfilUsuarioSetorId implements Serializable {

		private static final long serialVersionUID = -8974869155097260177L;
		private Long sequencePerfil;
		private String siglaUsuario;
		private Long codigoSetor;

		public PerfilUsuarioSetorId() {
			super();
		}

		public PerfilUsuarioSetorId(Long sequencePerfil, String siglaUsuario,
				Long codigoSetor) {
			super();
			this.sequencePerfil = sequencePerfil;
			this.siglaUsuario = siglaUsuario;
			this.codigoSetor = codigoSetor;
		}

		@Column(name = "SEQ_PERFIL", unique = false, nullable = false, insertable = false, updatable = false)
		public Long getSequencePerfil() {
			return sequencePerfil;
		}

		public void setSequencePerfil(Long sequencePerfil) {
			this.sequencePerfil = sequencePerfil;
		}

		@Column(name = "SIG_USUARIO", unique = false, nullable = false, insertable = false, updatable = false)
		public String getSiglaUsuario() {
			return siglaUsuario;
		}

		public void setSiglaUsuario(String siglaUsuario) {
			this.siglaUsuario = siglaUsuario;
		}

		@Column(name = "COD_SETOR", unique = false, nullable = false, insertable = false, updatable = false)
		public Long getCodigoSetor() {
			return codigoSetor;
		}

		public void setCodigoSetor(Long codigoSetor) {
			this.codigoSetor = codigoSetor;
		}

		public boolean equals(Object obj) {
			if (obj instanceof PerfilUsuarioSetorId == false) {
				return false;
			}

			if (this == obj) {
				return true;
			}

			PerfilUsuarioSetorId id = (PerfilUsuarioSetorId) obj;
			boolean isEqual = false;

			if (siglaUsuario.equals(id.getSiglaUsuario())
					&& codigoSetor == id.getCodigoSetor()
					&& sequencePerfil == id.getSequencePerfil()) {

				return true;

			}

			return isEqual;

		}

		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(getSiglaUsuario())
					.append(getCodigoSetor()).append(getSequencePerfil())
					.toHashCode();
		}

	}
}
