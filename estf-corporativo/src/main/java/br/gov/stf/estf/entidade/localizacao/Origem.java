package br.gov.stf.estf.entidade.localizacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.processostf.RegiaoOrgao;
import br.gov.stf.estf.entidade.usuario.Pessoa;

/**
 * Mantém informações sobre os órgãos externos de origem de processos ou recursos.
 * 
 * @author Rodrigo Barreiros
 * @author Demétrius Jubé
 * 
 * @since 15.07.2009
 */
@Entity
@Table(name = "ORIGEM", schema = "JUDICIARIO")
public class Origem extends ESTFBaseEntity<Long> {
	
	public static Long TRIBUNAL_JUSTICA_ESTADUAL = 65L;
	public static Long TRIBUNAL_REGIONAL_FEDERAL = 87L;
	public static Long TRIBUNAL_DE_JUSTICA_MILITAR_ESTADUAL = 5161L;
	public static Long TRIBUNAL_DE_JUSTICA_ESTADUAL = 5187L;
	public static Long TURMA_RECURSAL_DE_JUIZADOS_ESPECIAIS_ESTADUAIS = 5192L;
	public static Long TURMA_RECURSAL_DOS_JUIZADOS_ESPECIAIS_FEDERAIS = 8016L;

	private static final long serialVersionUID = 6204713472239241232L;

	private String descricao;

	private Boolean ativo;

	private RegiaoOrgao regiaoOrgao;

	private Pessoa pessoa;
	
	private String descricaoUrlIntegracao;
	
	private String senha;
	
	private String usuario;
	
	private Boolean baixaMni;
	
	
	
	/**
	 * Identifica o órgão de origem do processo ou recurso.
	 */
	@Id
	@Column(name = "COD_ORIGEM")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Descrição do órgão de origem do processo ou recurso.
	 */
	@Column(name = "DSC_ORIGEM")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Indica se o registro está ativo ou não.
	 */
	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_REGIAO_ORGAO", referencedColumnName = "SEQ_REGIAO_ORGAO")
	public RegiaoOrgao getRegiaoOrgao() {
		return regiaoOrgao;
	}

	public void setRegiaoOrgao(RegiaoOrgao regiaoOrgao) {
		this.regiaoOrgao = regiaoOrgao;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_PESSOA", referencedColumnName = "SEQ_PESSOA")
	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@Column(name = "DSC_URL_INTEGRACAO")
	public String getDescricaoUrlIntegracao() {
		return descricaoUrlIntegracao;
	}

	public void setDescricaoUrlIntegracao(String descricaoUrlIntegracao) {
		this.descricaoUrlIntegracao = descricaoUrlIntegracao;
	}

	@Column(name = "TXT_SENHA")
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Column(name = "DSC_USUARIO")
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Column(name = "FLG_BAIXA_MNI")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getBaixaMni() {
		return baixaMni;
	}

	public void setBaixaMni(Boolean baixaMni) {
		this.baixaMni = baixaMni;
	}

	
	
}
