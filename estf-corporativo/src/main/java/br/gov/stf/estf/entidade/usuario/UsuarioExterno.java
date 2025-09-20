package br.gov.stf.estf.entidade.usuario;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name = "USUARIO_EXTERNO", schema = "CORP")
public class UsuarioExterno extends ESTFBaseEntity<Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String cpfCnpj;

	private String ufOAB;

	private String numeroOAB;

	private String nome;

	private String tipo;

	private String sexo;

	private String email;

	private String emailAlternativo;

	private Integer situacaoUsuario;

	private boolean indicadorCadastro;

	private Date cadastro;

	private UsuarioExterno pai;

	private String senha;

	private String senhaSemUso;

	private String usrSistemas;

	private String flgAutenticacaoMutua;

	private boolean senhaVazia;

	private boolean gestorTICadastrado;

	private boolean eTribunaleOrgao;

	private String tecnologia;

	@Override
	@Id
	@SequenceGenerator(allocationSize = 1, name = "SEQ_USUARIO_EXTERNO", sequenceName = "CORP.SEQ_USUARIO_EXTERNO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO_EXTERNO")
	@Column(name = "SEQ_USUARIO_EXTERNO")
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "COD_CPF_CNPJ")
	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	@Column(name = "SIG_UF_OAB")
	public String getUfOAB() {
		return ufOAB;
	}

	public void setUfOAB(String ufOAB) {
		this.ufOAB = ufOAB;
	}

	@Column(name = "COD_OAB")
	public String getNumeroOAB() {
		return numeroOAB;
	}

	public void setNumeroOAB(String numeroOAB) {
		this.numeroOAB = numeroOAB;
	}

	@Column(name = "NOM_USUARIO_EXTERNO")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "TIP_USUARIO_EXTERNO")
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Column(name = "TIP_SEXO")
	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	@Column(name = "DSC_EMAIL_USUARIO_EXTERNO", unique = true, nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "DSC_EMAIL_ALTERNATIVO")
	public String getEmailAlternativo() {
		return emailAlternativo;
	}

	public void setEmailAlternativo(String emailAlternativo) {
		this.emailAlternativo = emailAlternativo;
	}

	@Column(name = "SEQ_TIPO_SITUACAO_USUARIO")
	public Integer getSituacaoUsuario() {
		return situacaoUsuario;
	}

	public void setSituacaoUsuario(Integer situacaoUsuario) {
		this.situacaoUsuario = situacaoUsuario;
	}

	@Column(name = "FLG_CADASTRO_CONFIRMADO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public boolean getIndicadorCadastro() {
		return indicadorCadastro;
	}

	public void setIndicadorCadastro(boolean indicadorCadastro) {
		this.indicadorCadastro = indicadorCadastro;
	}

	@Column(name = "DAT_CADASTRO")
	@Temporal(TemporalType.DATE)
	public Date getCadastro() {
		return cadastro;
	}

	public void setCadastro(Date cadastro) {
		this.cadastro = cadastro;
	}

	@ManyToOne
	@JoinColumn(name = "SEQ_USUARIO_EXT_PAI")
	public UsuarioExterno getPai() {
		return pai;
	}

	public void setPai(UsuarioExterno pai) {
		this.pai = pai;
	}

	@Column(name = "DSC_HASH_SENHA")
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Column(name = "DSC_SENHA")
	public String getSenhaSemUso() {
		return senhaSemUso;
	}

	public void setSenhaSemUso(String senhaSemUso) {
		this.senhaSemUso = senhaSemUso;
	}

	@Column(name = "SIG_USUARIO_EXTERNO")
	public String getUsrSistemas() {
		return usrSistemas;
	}

	public void setUsrSistemas(String usrSistemas) {
		this.usrSistemas = usrSistemas;
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
		UsuarioExterno other = (UsuarioExterno) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public int compareTo(UsuarioExterno o) {
		return this.nome.toLowerCase().compareTo(o.nome.toLowerCase());
	}

	@Transient
	public String getFlgAutenticacaoMutua() {
		return flgAutenticacaoMutua;
	}

	public void setFlgAutenticacaoMutua(String flgAutenticacaoMutua) {
		this.flgAutenticacaoMutua = flgAutenticacaoMutua;
	}

	@Transient
	public boolean getSenhaVazia() {
		return senhaVazia;
	}

	public void setSenhaVazia(boolean senhaVazia) {
		this.senhaVazia = senhaVazia;
	}

	@Transient
	public boolean getGestorTICadastrado() {
		return this.gestorTICadastrado;
	}

	public void setGestorTICadastrado(boolean gestorTICadastrado) {
		this.gestorTICadastrado = gestorTICadastrado;
	}

	@Transient
	public boolean getETribunaleOrgao() {
		return this.eTribunaleOrgao;
	}

	public void setETribunaleOrgao(boolean eTribunaleOrgao) {
		this.eTribunaleOrgao = eTribunaleOrgao;
	}

	@Transient
	public String getTecnologia() {
		return tecnologia;
	}

	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}
}
