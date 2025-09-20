package br.gov.stf.estf.entidade.documento;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="FASE_COMUNICACAO", schema="JUDICIARIO")
public class FaseComunicacao extends ESTFBaseEntity<Long>{
	
	private static final long serialVersionUID = 3117802185249858172L;
	private Long id;
	private TipoFaseComunicacao tipoFase;
	private Comunicacao comunicacao;
	private Date dataLancamento;
	private FlagFaseAtual flagFaseAtual;
	private String observacao;
	private Date dataCriacao;
	
	@Id
	@Column(name = "SEQ_FASE_COMUNICACAO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_FASE_COMUNICACAO", allocationSize = 1)
	public Long getId() {
		
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="SEQ_TIPO_FASE_COMUNICACAO", insertable = true, updatable = true, nullable = true)
	@Type(type="br.gov.stf.framework.util.GenericEnumUserType", parameters={
				@Parameter( name = "enumClass", 
						    value = "br.gov.stf.estf.entidade.documento.TipoFaseComunicacao"),
				@Parameter( name = "identifierMethod",
							value = "getCodigoFase" )})
	public TipoFaseComunicacao getTipoFase() {
		return tipoFase;
	}

	public void setTipoFase(TipoFaseComunicacao tipoFase) {
		this.tipoFase = tipoFase;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_LANCAMENTO", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	@Column(name = "FLG_FASE_ATUAL")
	@Enumerated(EnumType.STRING)
	public FlagFaseAtual getFlagFaseAtual() {
		return flagFaseAtual;
	}

	public void setFlagFaseAtual(FlagFaseAtual flagFaseAtual) {
		this.flagFaseAtual = flagFaseAtual;
	}	
	
	public enum FlagFaseAtual {
		N("Não"), 
		S("Sim");

		private String descricao;

		FlagFaseAtual(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}
	
	@ManyToOne( fetch=FetchType.LAZY, cascade = {} )
	@JoinColumn( name="SEQ_COMUNICACAO" )
	public Comunicacao getComunicacao() {
		return comunicacao;
	}

	public void setComunicacao(Comunicacao comunicacao) {
		this.comunicacao = comunicacao;
	}

	@Column(name="DSC_OBSERVACAO", insertable = true, updatable = true, nullable=true, unique=false)
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_INCLUSAO")
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	
}
