package br.gov.stf.estf.entidade.processosetor;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OrderBy;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Peticao;

@Entity
@Table(schema = "EGAB", name = "PETICAO_SETOR")
public class PeticaoSetor extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 7411578611502189911L;
	private Long id;
	private String observacao;
	private Date dataEntradaSetor;
	private Date dataFimTramite;
	private Date dataSaidaSetor;
	private Setor setor;
	private HistoricoDeslocamentoPeticao deslocamentoAtual;
	private List<HistoricoDeslocamentoPeticao> historicoDeslocamentos;
	// private PeticaoProcesso peticaoProcesso;
	private Peticao peticao;

	// private ObjetoIncidente<?> objetoIncidente;

	@Id
	@Column(name = "SEQ_PETICAO_SETOR")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "EGAB.SEQ_PETICAO_SETOR", allocationSize = 1)
	public Long getId() {
		return id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_ENTRADA_SETOR", unique = false, insertable = false, updatable = false)
	public Date getDataEntradaSetor() {
		return dataEntradaSetor;
	}

	public void setDataEntradaSetor(Date dataEntradaSetor) {
		this.dataEntradaSetor = dataEntradaSetor;
	}

	/**
	 * Obtém a data em que a petição foi tratada.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_FIM_TRAMITE", unique = false, insertable = true, updatable = true)
	public Date getDataFimTramite() {
		return dataFimTramite;
	}

	/**
	 * Define a data em que a petição foi tratada.
	 */
	public void setDataFimTramite(Date dataFimTramite) {
		this.dataFimTramite = dataFimTramite;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_SAIDA_SETOR", unique = false, insertable = false, updatable = false)
	public Date getDataSaidaSetor() {
		return dataSaidaSetor;
	}

	public void setDataSaidaSetor(Date dataSaidaSetor) {
		this.dataSaidaSetor = dataSaidaSetor;
	}

	@Column(name = "DSC_OBSERVACAO_PETICAO", unique = false, nullable = true, insertable = false, updatable = false, length = 4000)
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_HIST_DESLOCAMENTO_PETICAO", nullable = true, unique = true)
	public HistoricoDeslocamentoPeticao getDeslocamentoAtual() {
		return deslocamentoAtual;
	}

	public void setDeslocamentoAtual(
			HistoricoDeslocamentoPeticao deslocamentoAtual) {
		this.deslocamentoAtual = deslocamentoAtual;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "peticaoSetor")
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy(clause = "DAT_REMESSA DESC, SEQ_HIST_DESLOCAMENTO_PETICAO DESC")
	public List<HistoricoDeslocamentoPeticao> getHistoricoDeslocamentos() {
		return historicoDeslocamentos;
	}

	public void setHistoricoDeslocamentos(
			List<HistoricoDeslocamentoPeticao> historicoDeslocamentos) {
		this.historicoDeslocamentos = historicoDeslocamentos;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR", unique = false, nullable = true, insertable = true, updatable = true)
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	// public ObjetoIncidente<?> getObjetoIncidente() {
	// return objetoIncidente;
	// }
	//
	// public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
	// this.objetoIncidente = objetoIncidente;
	// }

	@Transient
	public String getIdentificacaoPeticao() {
		// return peticao.getIdentificacao();
		return getPeticao().getIdentificacao();
	}

	// @ManyToOne(fetch=FetchType.LAZY,optional=true)
	// @JoinColumns( {
	// @JoinColumn(name="NUM_PETICAO", referencedColumnName="NUM_PETICAO",
	// insertable=false, updatable=false),
	// @JoinColumn(name="ANO_PETICAO", referencedColumnName="ANO_PETICAO",
	// insertable=false, updatable=false)
	// } )
	// @NotFound(action=NotFoundAction.IGNORE)
	// public PeticaoProcesso getPeticaoProcesso() {
	// return peticaoProcesso;
	// }
	//
	// public void setPeticaoProcesso(PeticaoProcesso peticaoProcesso) {
	// this.peticaoProcesso = peticaoProcesso;
	// }

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public Peticao getPeticao() {
		return peticao;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPeticao(Peticao peticao) {
		this.peticao = peticao;
	}

	public Boolean adicionarHistoricoDeslocamento(
			HistoricoDeslocamentoPeticao deslocamento) {
		Boolean result = Boolean.FALSE;

		if (deslocamento == null)
			throw new NullPointerException("Objeto de deslocamento nulo.");

		setDeslocamentoAtual(deslocamento);

		List<HistoricoDeslocamentoPeticao> historico = getHistoricoDeslocamentos();
		assert (historico != null);
		historico.add(0, deslocamento);

		result = Boolean.TRUE;

		return result;
	}

	// @Transient
	// public Peticao getPeticao() {
	// return ObjetoIncidenteUtil.getPeticao(getObjetoIncidente());
	// }

	@Transient
	public boolean isTratada() {
		return getDataFimTramite() != null;
	}

	@Transient
	public void setTratada(boolean tratada) {
		Date data = null;
		
		if (tratada) {
			data = new Date();
		}
		
		setDataFimTramite(data);
	}
}
