package br.gov.stf.estf.entidade.localizacao;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.processostf.Parte;
import br.gov.stf.estf.entidade.processostf.TipoIntimacaoAdvogado;

@Entity
@Table(schema = "STF", name = "ADVOGADOS")
public class Advogado extends ESTFBaseEntity<Long> {
	
	private static final long serialVersionUID = 1L;
	
	public static final String TIPO_INTIMACAO_ELETRONICA = "E";
	
	private String nome;
	private List<Parte> partes;
	// TODO criado o objeto TipoIntimacaoAdvogado
	private TipoIntimacaoAdvogado tipoIntimacaoAdvogado;
	private String tipoIntimacao;
	private String bairro;
	private String cep;
	private String cidade;
	private String cpf;
	private String email;
	private Boolean flagAdvogado;
	private Boolean flagEstagiario;
	//private Long id;
	private String logradouro;
	private String numeroOab;
	private String telefoneResidencial;	
	private String uf;
	
	@Id
	@Column(name = "COD_ADV")
	public Long getId() {
		return (Long) id;
	}
	
	//@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "COD_PARTE", referencedColumnName = "SEQ_JURISDICIONADO")
	@ManyToMany(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name="COD_PARTE", unique=false, nullable=true, insertable=false, updatable=false )
	public List<Parte> getPartes() {
		return partes;
	}

	public void setPartes(List<Parte> partes) {
		this.partes = partes;
	}
	
	@Column(length=1, name="TIP_INTIMACAO")
	public String getTipoIntimacao() {
		return tipoIntimacao;
	}

	public void setTipoIntimacao(String tipoIntimacao) {
		this.tipoIntimacao = tipoIntimacao;
	}
	 
	@Column(name = "NOM_ADV", unique = false, nullable = false, insertable = false, updatable = false, 
			length = 240)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Column(name = "END_ADV_BAIRRO", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	@Column(name = "CEP_ADV", unique = false, nullable = true, insertable = true, updatable = true, length = 8)
	public String getCep() {
		return cep;
	}

	/**
	 * CEP com apenas 8 dígitos.
	 * 
	 * @param cep
	 */
	public void setCep(String cep) {
		if (cep != null && cep.length() > 0) {
			cep = cep.replace(".", "");
			cep = cep.replace("-", "");
		}
		this.cep = cep;
	}

	@Column(name = "END_ADV_CIDADE", unique = false, nullable = true, insertable = true, updatable = true, length = 30)
	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidado) {
		this.cidade = cidado;
	}
	
	@Column(name = "NUM_CPF_ADV", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	@Column(name = "END_MAIL", unique = false, nullable = true, insertable = true, updatable = true, length = 60)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "FLG_ADV", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
    @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" ) 
	public Boolean getFlagAdvogado() {
		return flagAdvogado;
	}

	public void setFlagAdvogado(Boolean flagAdvogado) {
		this.flagAdvogado = flagAdvogado;
	}
	
	@Column(name = "FLG_ESTAGIARIO", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
    @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" ) 
	public Boolean getFlagEstagiario() {
		return flagEstagiario;
	}
	
	public void setFlagEstagiario(Boolean flagEstagiario) {
		this.flagEstagiario = flagEstagiario;
	}

	@Column(name = "END_ADV_LOGRAD", unique = false, nullable = true, insertable = true, updatable = true, length = 240)
	public String getLogradouro() {
		return logradouro;
	}
	
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	@Column(name = "NUM_OAB_ADV", unique = false, nullable = true, insertable = true, updatable = true, length = 12)
	public String getNumeroOab() {
		return numeroOab;
	}

	public void setNumeroOab(String numeroOab) {
		this.numeroOab = numeroOab;
	}

	@Column(name = "TEL_ADV_RESID", unique = false, nullable = true, insertable = true, updatable = true, length = 15)
	public String getTelefoneResidencial() {
		return telefoneResidencial;
	}
	
	public void setTelefoneResidencial(String telefoneResidencial) {
		this.telefoneResidencial = telefoneResidencial;
	}	


	@Enumerated(EnumType.STRING)
	@Column(name = "TIP_INTIMACAO", unique = false, nullable = false, insertable = false, updatable = false)
	public TipoIntimacaoAdvogado getTipoIntimacaoAdvogado() {
		return tipoIntimacaoAdvogado;
	}

	public void setTipoIntimacaoAdvogado(TipoIntimacaoAdvogado tipoIntimacaoAdvogado) {
		this.tipoIntimacaoAdvogado = tipoIntimacaoAdvogado;
	}

	@Column(name = "UF_ADV", unique = false, nullable = true, insertable = true, updatable = true, length = 2)
	public String getUf() {
		return uf;
	}
	
	public void setUf(String uf) {
		this.uf = uf;
	}		
	
}

