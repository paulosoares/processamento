package br.gov.stf.estf.entidade.usuario;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;

/**
 * @author Paulo.Estevao
 * @since 04.09.2013
 */
@Entity
@Table(name = "CUSTOMIZACAO_USUARIO", schema = "GLOBAL")
@SequenceGenerator(name = "SEQUENCE", sequenceName = "GLOBAL.SEQ_CUSTOMIZACAO_USUARIO", allocationSize=1)
public class CustomizacaoUsuario extends ESTFBaseEntity<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8871496287167906426L;
	
	private Long id;
	private TipoCustomizacao tipo;
	private String descricao;
	private String nome;
	private String documento;
	private Usuario usuario;
	private Setor setor;
	
	@Override
	@Id
	@Column(name = "SEQ_CUSTOMIZACAO_USUARIO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_CUSTOMIZACAO")
	public TipoCustomizacao getTipo() {
		return tipo;
	}
	
	public void setTipo(TipoCustomizacao tipo) {
		this.tipo = tipo;
	}
	
	@Column(name = "TXT_CUSTOMIZACAO")
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column(name = "DSC_CUSTOMIZACAO")
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Lob
	@Column(name = "DOC_CUSTOMIZACAO")
	public String getDocumento() {
		return documento;
	}
	
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SIG_USUARIO")
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR")
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}
}	