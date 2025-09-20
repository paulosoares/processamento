package br.gov.stf.estf.entidade.usuario;

// Generated 01/04/2014 14:47:24 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "PESSOA_TELEFONE", schema = "CORPORATIVO")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class PessoaTelefone extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 4210748548205742577L;

	@Id
	@GeneratedValue(generator = "PESSOA_TELEFONE_SEQUENCE", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "PESSOA_TELEFONE_SEQUENCE", sequenceName = "CORPORATIVO.SEQ_PESSOA_TELEFONE", allocationSize = 1)
	@Column(name = "SEQ_PESSOA_TELEFONE", unique = true, nullable = false, precision = 10, scale = 0)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_PESSOA", nullable = false)
	private Pessoa pessoa;

	@Column(name = "NUM_DDD")
	private String ddd;

	@Column(name = "NUM_TELEFONE")
	private String telefone;

	@Column(name = "FLG_PRINCIPAL", nullable = false)
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	private Boolean principal;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

}
