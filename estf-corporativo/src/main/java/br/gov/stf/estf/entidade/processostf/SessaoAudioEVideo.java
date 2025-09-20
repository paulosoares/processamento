package br.gov.stf.estf.entidade.processostf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.julgamento.Sessao;

/**
 * Representa a entidade Indice_AV.
 *
 * @author Almir Leite de Oliveira
 * @since 31.01.2012 
 * 
 * TODO MAPEAR OS DEMAIS ATRIBUTOS DA ENTIDADE */
@Entity
@Table(name = "SESSAO_AV", schema = "AUDIO_VIDEO")
public class SessaoAudioEVideo extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 8119715537305907964L;
	
	/* Essa numeração é relacionada a Sala na tabela AUDIO_VIDEO.SALA. */
	public static final Long SALA_PLENARIO                      = 1L;
	public static final Long SALA_PRIMEIRA_TURMA                = 2L;
	public static final Long SALA_SEGUNDA_TURMA                 = 3L;
	/* Essa numeração é relacionada ao Tipo de Sessão na tabela AUDIO_VIDEO.TIPO_SESSAO_AV. */
	public static final Long TIPO_SESSAO_PLENARIA               = 1L;
	public static final Long TIPO_SESSAO_PRIMEIRA_TURMA         = 21L;
	public static final Long TIPO_SESSAO_SEGUNDA_TURMA          = 22L;
	/* Essa numeração é relacionada a configuração na tabela AUDIO_VIDEO.GRUPO_CONFIGURACAO. */
	public static final Long GRUPO_CONFIGURACAO_PLENARIO        = 2L;
	public static final Long GRUPO_CONFIGURACAO_PRIMEIRA_TURMA  = 3L;
	public static final Long GRUPO_CONFIGURACAO_SEGUNDA_TURMA   = 4L;


	private Long id;
	private Long seqSala;
	private Long seqTipoSessao;
	private Long seqGrupoConfiguracao;
	private Sessao sessao;
	private Date dataInicio;
	private Boolean restrita;
	
	@Id
	@Column( name="SEQ_SESSAO_AV" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="AUDIO_VIDEO.SEQ_SESSAO_AV", allocationSize=1 )	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column( name="SEQ_SALA" )
	public Long getSeqSala() {
		return seqSala;
	}
	public void setSeqSala(Long seqSala) {
		this.seqSala = seqSala;
	}
	@Column( name="SEQ_TIPO_SESSAO_AV" )
	public Long getSeqTipoSessao() {
		return seqTipoSessao;
	}
	public void setSeqTipoSessao(Long seqTipoSessao) {
		this.seqTipoSessao = seqTipoSessao;
	}
	@Column( name="SEQ_GRUPO_CONFIGURACAO" )
	public Long getSeqGrupoConfiguracao() {
		return seqGrupoConfiguracao;
	}
	public void setSeqGrupoConfiguracao(Long seqGrupoConfiguracao) {
		this.seqGrupoConfiguracao = seqGrupoConfiguracao;
	}
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_SESSAO")
	public Sessao getSessao() {
		return sessao;
	}
	public void setSessao(Sessao sessao) {
		this.sessao = sessao;
	}
	@Temporal( TemporalType.TIMESTAMP )
	@Column( name="DAT_INICIO_SESSAO_AV", insertable=true, updatable=true, unique=false, nullable=true, length=7 )
	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	@Column(name = "FLG_RESTRITA")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getRestrita() {
		return restrita;
	}
	public void setRestrita(Boolean restrita) {
		this.restrita = restrita;
	}


}
