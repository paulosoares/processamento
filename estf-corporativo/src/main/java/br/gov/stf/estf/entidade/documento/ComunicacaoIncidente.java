package br.gov.stf.estf.entidade.documento;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

@Entity
@Table(name = "COMUNICACAO_OBJETO_INCIDENTE", schema = "JUDICIARIO")
public class ComunicacaoIncidente extends ESTFAuditavelBaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	private ObjetoIncidente<?> objetoIncidente;
	private Comunicacao comunicacao;
	private AndamentoProcesso andamentoProcesso;
	private FlagProcessoLote tipoVinculo;

	@Id
	@Column(name = "SEQ_COMUNICACAO_OBJ_INCIDENTE")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_COMUNICACAO_OBJ_INCIDENTE", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = {})
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", updatable = true, insertable = true)
	// @LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_COMUNICACAO", unique = false, insertable = true, updatable = true)
	public Comunicacao getComunicacao() {
		return comunicacao;
	}

	public void setComunicacao(Comunicacao comunicacao) {
		this.comunicacao = comunicacao;
	}

	/**
	 * <p>
	 * Andamento do processo ao qual a comunicação incidente está vinculada. Fica nulo em casos que não geram andamento, como na criação da comunicação.
	 * </p>
	 * 
	 * <p>
	 * Ex.: para comunicações assinadas, o andamento associado é um do tipo "Comunicação Assinada".
	 * </p>
	 * 
	 * @return o andamento do processo
	 */
	@OneToOne(fetch = FetchType.EAGER, cascade = {}, optional = true)
	@JoinColumn(name = "SEQ_ANDAMENTO_PROCESSO", unique = false, insertable = true, updatable = true)
	public AndamentoProcesso getAndamentoProcesso() {
		return andamentoProcesso;
	}

	public void setAndamentoProcesso(AndamentoProcesso andamentoProcesso) {
		this.andamentoProcesso = andamentoProcesso;
	}

	// tipo do vinculo para identificar se o registro criado é de um processo em lote ou o principal relacionado a comunicacao
	@Column(name = "TIP_VINCULO")
	@Enumerated(EnumType.STRING)
	public FlagProcessoLote getTipoVinculo() {
		return tipoVinculo;
	}

	public void setTipoVinculo(FlagProcessoLote tipoVinculo) {
		this.tipoVinculo = tipoVinculo;
	}

	@Transient
	public boolean isVinculoProcessoLotePrincipal() {
		return getTipoVinculo().equals(FlagProcessoLote.P);
	}

	public enum FlagProcessoLote {
		P("Principal"), V("Vinculado");

		private String descricao;

		FlagProcessoLote(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	};
}
