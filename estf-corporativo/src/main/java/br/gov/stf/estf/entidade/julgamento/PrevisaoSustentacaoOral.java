/**
 * 
 */
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

/**
 * @author Paulo.Estevao
 * @since 30.06.2011
 */
@Entity
@Table(name="PREVISAO_SUSTENTACAO_ORAL", schema="JULGAMENTO")
public class PrevisaoSustentacaoOral extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -943623827135127492L;
	
	private Long id;
	private InformacaoPautaProcesso informacaoPautaProcesso;
	private Jurisdicionado representado;
	private Jurisdicionado jurisdicionado;
	private Envolvido envolvido;
	private String observacao;
	
	@Override
	@Id
	@Column( name="SEQ_PREVISAO_SUSTENTACAO_ORAL" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_PREVISAO_SUSTENTACAO_ORAL", allocationSize=1 )	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_INFORMACAO_PAUTA_PROCESSO")
	public InformacaoPautaProcesso getInformacaoPautaProcesso() {
		return informacaoPautaProcesso;
	}

	public void setInformacaoPautaProcesso(
			InformacaoPautaProcesso informacaoPautaProcesso) {
		this.informacaoPautaProcesso = informacaoPautaProcesso;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_JURISDICIONADO_REPR")
	public Jurisdicionado getRepresentado() {
		return representado;
	}

	public void setRepresentado(Jurisdicionado representado) {
		this.representado = representado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_JURISDICIONADO")
	public Jurisdicionado getJurisdicionado() {
		return jurisdicionado;
	}

	public void setJurisdicionado(Jurisdicionado jurisdicionado) {
		this.jurisdicionado = jurisdicionado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ENVOLVIDO")
	public Envolvido getEnvolvido() {
		return envolvido;
	}

	public void setEnvolvido(Envolvido envolvido) {
		this.envolvido = envolvido;
	}

	@Column(name = "TXT_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
}
