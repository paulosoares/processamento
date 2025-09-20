package br.gov.stf.estf.entidade.usuario;

// Generated 01/04/2014 14:47:24 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.julgamento.model.util.TipoPessoaType;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "PESSOA", schema = "CORPORATIVO")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Pessoa extends ESTFBaseEntity<Long> {
	
	public static final long PESSOA_SEM_IDENTIFICACAO = 0L;
	public static final long PROCURADOR_GERAL_DA_REPUBLICA = 238916L;
	public static final long MINISTERIO_PUBLICO_DA_UNIAO = 13565629L;

	private static final long serialVersionUID = -3373380666642686092L;

	@Id
	@GeneratedValue(generator = "PESSOA_SEQUENCE", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "PESSOA_SEQUENCE", sequenceName = "CORPORATIVO.SEQ_PESSOA", allocationSize = 1)	
	@Column(name = "SEQ_PESSOA", unique = true, nullable = false, precision = 10, scale = 0)
	private Long id;

	/** O idOrigemDado é sem 1L e é para manter compatibilidade com os sistemas legados.**/
	@Column(name = "SEQ_ORIGEM_DADO", precision = 10, scale = 0)
	private Long idOrigemDado = 1L;

	@Column(name = "NOM_PESSOA", length = 4000)
	private String nome;

	@Column(name = "TIP_PESSOA", length = 2)
	@Type(type = TipoPessoaType.CLASS_NAME)
	private TipoPessoa tipo;

	@Column(name = "FLG_ATIVO", length = 1)
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	private Boolean ativo;

	@Column(name = "SEQ_NATUREZA_JURIDICA", precision = 6, scale = 0)
	private Integer naturezaJuridica;

/*	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_ATIVIDADE_ECONOMICA", referencedColumnName="SEQ_ATIVIDADE_ECONOMICA")
	private AtividadeEconomica atividadeEconomica;*/

/*	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_PAIS_ORIGEM", referencedColumnName="SEQ_PAIS")
	private Pais paisOrigem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_UNIDADE_FEDERACAO_ORIGEM", referencedColumnName="SEQ_UNIDADE_FEDERACAO")
	private UnidadeFederacao unidadeFederacaoOrigem;*/

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pessoa")
	private Set<PessoaEndereco> pessoaEnderecos = new HashSet<PessoaEndereco>(0);

/*	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pessoa")
	private Set<PessoaTelefone> pessoaTelefones = new HashSet<PessoaTelefone>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pessoa")
	private Set<PessoaEmail> pessoaEmails = new HashSet<PessoaEmail>(0);

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "pessoa", cascade=CascadeType.PERSIST)
	private Set<PessoaIdentificacao> identificacoes = new HashSet<PessoaIdentificacao>();*/

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "pessoa")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<UsuarioCorporativo> usuarios = new HashSet<UsuarioCorporativo>(0);

/*	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pessoa")
	private Set<Grupo> grupo = new HashSet<Grupo>(0);*/
	
	@Column(name = "TIP_MEIO_INTIMACAO", length = 1)
	private String meioIntimacao;

	@Column(name = "TXT_OBSERVACAO", length = 1000)
	private String observacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_PESSOA_PAI", referencedColumnName="SEQ_PESSOA")
	private Pessoa pessoaPai;
	
	@Column(name = "FLG_CADASTRO_RATIFICADO", nullable = false)
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	private Boolean cadastroRatificado = Boolean.valueOf(false);

	public Pessoa() {
		
	}
	
	public Pessoa(Long id) {
		this.id = id;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdOrigemDado() {
		return idOrigemDado;
	}

	public void setIdOrigemDado(Long idOrigemDado) {
		this.idOrigemDado = idOrigemDado;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoPessoa getTipo() {
		return tipo;
	}

	public void setTipo(TipoPessoa tipo) {
		this.tipo = tipo;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Integer getNaturezaJuridica() {
		return naturezaJuridica;
	}

	public void setNaturezaJuridica(Integer naturezaJuridica) {
		this.naturezaJuridica = naturezaJuridica;
	}
/*
	public AtividadeEconomica getAtividadeEconomica() {
		return atividadeEconomica;
	}

	public void setAtividadeEconomica(AtividadeEconomica atividadeEconomica) {
		this.atividadeEconomica = atividadeEconomica;
	}

	public Pais getPaisOrigem() {
		return paisOrigem;
	}

	public void setPaisOrigem(Pais paisOrigem) {
		this.paisOrigem = paisOrigem;
	}

	public UnidadeFederacao getUnidadeFederacaoOrigem() {
		return unidadeFederacaoOrigem;
	}

	public void setUnidadeFederacaoOrigem(UnidadeFederacao unidadeFederacaoOrigem) {
		this.unidadeFederacaoOrigem = unidadeFederacaoOrigem;
	}
*/
	public Set<PessoaEndereco> getPessoaEnderecos() {
		return pessoaEnderecos;
	}

	public void setPessoaEnderecos(Set<PessoaEndereco> pessoaEnderecos) {
		this.pessoaEnderecos = pessoaEnderecos;
	}

/*
	public Set<PessoaTelefone> getPessoaTelefones() {
		return pessoaTelefones;
	}

	public void setPessoaTelefones(Set<PessoaTelefone> pessoaTelefones) {
		this.pessoaTelefones = pessoaTelefones;
	}

	public Set<PessoaEmail> getPessoaEmails() {
		return pessoaEmails;
	}

	public void setPessoaEmails(Set<PessoaEmail> pessoaEmails) {
		this.pessoaEmails = pessoaEmails;
	}

	public Set<PessoaIdentificacao> getIdentificacoes() {
		return identificacoes;
	}

	public void setIdentificacoes(Set<PessoaIdentificacao> pessoasIdentificacao) {
		this.identificacoes = pessoasIdentificacao;
	}
*/
	public Set<UsuarioCorporativo> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Set<UsuarioCorporativo> usuarios) {
		this.usuarios = usuarios;
	}
/*
	public Set<Grupo> getGrupo() {
		return grupo;
	}

	public void setGrupo(Set<Grupo> grupo) {
		this.grupo = grupo;
	}
*/
	public String getMeioIntimacao() {
		return meioIntimacao;
	}

	public void setMeioIntimacao(String meioIntimacao) {
		this.meioIntimacao = meioIntimacao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Pessoa getPessoaPai() {
		return pessoaPai;
	}

	public void setPessoaPai(Pessoa pessoaPai) {
		this.pessoaPai = pessoaPai;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if (this.getId() != null) {
			sb.append(String.format("ID = %d", this.getId()));
		}
		if (this.getNome() != null && !this.getNome().trim().isEmpty()) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(String.format("nome = '%s'", this.getNome()));
		}
		if (this.getTipo() != null) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(String.format("tipo = '%s'", this.getTipo()));
		}
/*		PessoaIdentificacao cpfCnpj = PessoaUtils.extrairCpfCnpj(this);
		if (cpfCnpj != null) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(String.format("CPF/CNPJ = %s", cpfCnpj.getIdentificacao()));
		}*/
		return sb.toString();
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
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof Pessoa)) return false;
		
		Pessoa other = (Pessoa) obj;
		return new EqualsBuilder()
				.append(id, other.getId())
				.isEquals();
		
	}

	public int compareTo(Pessoa pessoa) {
		if (this.equals(pessoa))
			return 0;
		
		return this.getNome().compareTo(pessoa.getNome());
	}

	public boolean isJuridica() {
		return tipo != null && tipo.isJuridica();
	}

	public boolean isFisica() {
		return tipo != null && tipo.isFisica();
	}

	public boolean isSemIdentificacao() {
		return id != null && id.longValue() == PESSOA_SEM_IDENTIFICACAO;
	}

	public void nomeMaiusculo() {
		if (nome != null) {
			nome = nome.toUpperCase();
		}
	}

	public boolean isPublica() {
		return tipo != null && tipo.isPublica();
	}

	public void setCadastroRatificado(Boolean cadastroRatificado) {
		this.cadastroRatificado = cadastroRatificado;
	}
	
	public Boolean getCadastroRatificado() {
		return cadastroRatificado;
	}
}
