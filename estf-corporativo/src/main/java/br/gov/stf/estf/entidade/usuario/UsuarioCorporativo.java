package br.gov.stf.estf.entidade.usuario;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name = "USUARIO", schema = "CORPORATIVO")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class UsuarioCorporativo extends ESTFBaseEntity<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String sigla;
	private Boolean ativo;
	private Boolean usuarioExterno;
	private Boolean usuarioLdap;
	private Pessoa pessoa;
	

	@Override
	@Id
	@SequenceGenerator(allocationSize = 1, name = "SEQ_USUARIO", sequenceName = "CORP.SEQ_USUARIO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO")
	@Column(name = "SEQ_USUARIO")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "SIG_USUARIO")
	public String getSigla() {
		return sigla;
	}
	
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}


	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Column(name = "FLG_USUARIO_EXTERNO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getUsuarioExterno() {
		return usuarioExterno;
	}
	
	public void setUsuarioExterno(Boolean usuarioExterno) {
		this.usuarioExterno = usuarioExterno;
	}
	
	@Column(name = "FLG_USUARIO_LDAP")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getUsuarioLdap() {
		return usuarioLdap;
	}
	
	public void setUsuarioLdap(Boolean usuarioLdap) {
		this.usuarioLdap = usuarioLdap;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn( name="SEQ_PESSOA")
	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioCorporativo other = (UsuarioCorporativo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int compareTo(UsuarioCorporativo o) {
		return this.sigla.toLowerCase().compareTo(o.sigla.toLowerCase());
	}


}
