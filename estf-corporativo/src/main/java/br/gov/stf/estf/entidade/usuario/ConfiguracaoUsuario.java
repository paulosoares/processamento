package br.gov.stf.estf.entidade.usuario;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;

@SuppressWarnings("serial")
@Entity
@Table( schema="GLOBAL", name="CONFIGURACAO_USUARIO" )
public class ConfiguracaoUsuario extends ESTFBaseEntity<Long> {
	
	public static final String PESQUISA_AVANCADA_XML = "AVANÇADA_XML";
	
	private Usuario usuario;
	private TipoConfiguracaoUsuario tipoConfiguracaoUsuario;
	private String descricao;
	private Setor setor;
	private Date dataDesativacao;
	private String codigoChave;
	private String valor;
	
	
	public static enum TipoConfiguracaoPesquisaPor {
		USUARIO("US", "Usuário"),
		SETOR("ST", "Setor");
		
		private String sigla;
		private String descricao;
		
		private TipoConfiguracaoPesquisaPor( String sigla, String descricao ) {
			this.sigla = sigla;
			this.descricao = descricao;
		}

		public String getSigla() {
			return sigla;
		}

		public String getDescricao() {
			return descricao;
		}
		
		
	}
	

	@Id
	@Column( name="SEQ_CONFIGURACAO_USUARIO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="GLOBAL.SEQ_CONFIGURACAO_USUARIO" ,allocationSize = 1)	
	public Long getId() {
		return id;
	}	    
    
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn( name="SIG_USUARIO")
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn( name="SEQ_TIPO_CONFIGURACAO_USUARIO")
	public TipoConfiguracaoUsuario getTipoConfiguracaoUsuario() {
		return tipoConfiguracaoUsuario;
	}

	public void setTipoConfiguracaoUsuario(
			TipoConfiguracaoUsuario tipoConfiguracaoUsuario) {
		this.tipoConfiguracaoUsuario = tipoConfiguracaoUsuario;
	}

	@Column( name="DSC_CONFIGURACAO", nullable=false, length=80 )
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR", unique = false, nullable = true, insertable = true, updatable = true)
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_DESATIVACAO", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataDesativacao() {
		return dataDesativacao;
	}

	public void setDataDesativacao(Date dataDesativacao) {
		this.dataDesativacao = dataDesativacao;
	}

	@Column(name="DSC_SUBTIPO_CONFIGURACAO", unique = false, nullable = true, insertable = true, updatable = true)
	public String getCodigoChave() {
		return codigoChave;
	}

	public void setCodigoChave(String codigoChave) {
		this.codigoChave = codigoChave;
	}

	@Lob
	@Column(name="DSC_CODIGO_CONFIGURACAO", unique = false, nullable = true, insertable = true, updatable = true, length=4000)
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	

}	