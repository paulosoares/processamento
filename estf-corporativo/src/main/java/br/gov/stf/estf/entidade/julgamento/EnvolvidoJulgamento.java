package br.gov.stf.estf.entidade.julgamento;

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

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;

@Entity
@Table( name="ENVOLVIDO_JULGAMENTO", schema="JULGAMENTO" )
public class EnvolvidoJulgamento extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2302679824532714684L;
	private JulgamentoProcesso julgamentoProcesso;
	private Envolvido envolvido;
	private TipoCompetenciaEnvolvido tipoCompetenciaEnvovido;
	private Jurisdicionado advogado;
	private Jurisdicionado parte;
	private String observacao;
	
	@Id
	@Column( name="SEQ_ENVOLVIDO_JULGAMENTO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_ENVOLVIDO_JULGAMENTO", allocationSize=1 )	
	public Long getId() {
		return id;
	}	

	@ManyToOne( cascade={}, fetch=FetchType.LAZY )
	@JoinColumn(name="SEQ_JULGAMENTO_PROCESSO", unique=false, nullable=false, insertable=true, updatable=true )
	public JulgamentoProcesso getJulgamentoProcesso() {
		return julgamentoProcesso;
	}
	public void setJulgamentoProcesso(JulgamentoProcesso julgamentoProcesso) {
		this.julgamentoProcesso = julgamentoProcesso;
	}
	
	@ManyToOne( cascade={}, fetch=FetchType.LAZY )
	@JoinColumn(name="SEQ_ENVOLVIDO", unique=false, nullable=false, insertable=true, updatable=true )
	public Envolvido getEnvolvido() {
		return envolvido;
	}
	
	public void setEnvolvido(Envolvido envolvido) {
		this.envolvido = envolvido;
	}
	
	@ManyToOne( cascade={}, fetch=FetchType.LAZY )
	@JoinColumn(name="COD_TIPO_COMPETENCIA", unique=false, nullable=true, insertable=true, updatable=true )
	public TipoCompetenciaEnvolvido getTipoCompetenciaEnvovido() {
		return tipoCompetenciaEnvovido;
	}
	
	public void setTipoCompetenciaEnvovido(
			TipoCompetenciaEnvolvido tipoCompetenciaEnvovido) {
		this.tipoCompetenciaEnvovido = tipoCompetenciaEnvovido;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_JURISDICIONADO")
	public Jurisdicionado getAdvogado() {
		return advogado;
	}

	public void setAdvogado(Jurisdicionado advogado) {
		this.advogado = advogado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_JURISDICIONADO_REPR")
	public Jurisdicionado getParte() {
		return parte;
	}

	public void setParte(Jurisdicionado parte) {
		this.parte = parte;
	}

	@Column(name = "TXT_OBSERVACAO_ENVOLVIDO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}
