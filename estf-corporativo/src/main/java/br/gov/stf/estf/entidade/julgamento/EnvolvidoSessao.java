package br.gov.stf.estf.entidade.julgamento;

import javax.persistence.CascadeType;
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

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.ministro.Ministro;

@Entity
@Table( name="ENVOLVIDO_SESSAO", schema="JULGAMENTO" )
public class EnvolvidoSessao extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6392621817494747588L;
	public static final String MINISTRO_SUBSTITUTO_MENSAGEM = "MINISTRO SUBSTITUTO";
	
	private Sessao sessao;
	private Ministro ministro;
	private String descricaoJustificativa;
	private Envolvido envolvido;
	private Boolean presente;
	private TipoCompetenciaEnvolvido tipoCompetenciaEnvolvido;
	
	
	public enum TipoCargoConstante {
		COORDENADOR("Coordenador", "C"),
		SECRETARIO("Secretário", "S"),
		PROCURADOR_GERAL_DA_REPUBLICA("Procurador-Geral da República", "PGR"),
		SUB_PROCURADOR_GERAL_DA_REPUBLICA("Sub-Procurador Geral da República ", "SBPGR");
		
		private String descricao;
		private String sigla;
		
		private TipoCargoConstante(String descricao, String sigla) {
			this.descricao = descricao;
			this.sigla = sigla;
		}

		public String getDescricao() {
			return descricao;
		}

		public String getSigla() {
			return sigla;
		}
		
		
	}
	
	
	@Id
	@Column( name="SEQ_ENVOLVIDO_SESSAO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_ENVOLVIDO_SESSAO", allocationSize=1 )	
	public Long getId() {
		return id;
	}
	
	
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn( name="SEQ_SESSAO", unique=false, nullable=false, insertable=true, updatable=true )
	public Sessao getSessao() {
		return sessao;
	}
	public void setSessao(Sessao sessao) {
		this.sessao = sessao;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn( name="COD_MINISTRO", unique=false, nullable=true, insertable=true, updatable=true )	
	public Ministro getMinistro() {
		return ministro;
	}
	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}
	
	@Column( name="DSC_JUSTIFICATIVA", unique=false, nullable=true, insertable=true, updatable=true )
	public String getDescricaoJustificativa() {
		return descricaoJustificativa;
	}
	public void setDescricaoJustificativa(String descricaoJustificativa) {
		this.descricaoJustificativa = descricaoJustificativa;
	}
	
	@ManyToOne( cascade={CascadeType.PERSIST}, fetch=FetchType.LAZY )
	@org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.PERSIST )
	@JoinColumn( name="SEQ_ENVOLVIDO", unique=false, nullable=true, insertable=true, updatable=true )
	public Envolvido getEnvolvido() {
		return envolvido;
	}
	public void setEnvolvido(Envolvido envolvido) {
		this.envolvido = envolvido;
	}
	
	@Column( name="FLG_PRESENTE", unique=false, nullable=false, insertable=true, updatable=true )
	@Type( type="br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" )
	public Boolean getPresente() {
		return presente;
	}
	public void setPresente(Boolean presente) {
		this.presente = presente;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn( name="COD_TIPO_COMPETENCIA", unique=false, nullable=true, insertable=true, updatable=true )
	public TipoCompetenciaEnvolvido getTipoCompetenciaEnvolvido() {
		return tipoCompetenciaEnvolvido;
	}
	public void setTipoCompetenciaEnvolvido(
			TipoCompetenciaEnvolvido tipoCompetenciaEnvolvido) {
		this.tipoCompetenciaEnvolvido = tipoCompetenciaEnvolvido;
	}

	
}
