package br.gov.stf.estf.entidade.documento;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;

@Entity
@Table(name = "DESLOCAMENTO_COMUNICACAO", schema = "JUDICIARIO")
public class DeslocamentoComunicacao extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 3117802185249858172L;
	private Long id;
	private Comunicacao comunicacao;
	private Date dataEntrada;
	private Date dataSaida;
	private Setor setor;

	@Id
	@Column(name = "SEQ_DESLOCAMENTO_COMUNICACAO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_DESLOCAMENTO_COMUNICACAO", allocationSize = 1)
	public Long getId() {

		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_COMUNICACAO")
	public Comunicacao getComunicacao() {
		return comunicacao;
	}

	public void setComunicacao(Comunicacao comunicacao) {
		this.comunicacao = comunicacao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_ENTRADA", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_SAIDA", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR", unique = false, nullable = true, insertable = true, updatable = true)
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	/**
	 * Verifica se o deslocamento é atual - ou seja, se ainda não possui data de saída.
	 * 
	 * @return
	 */
	@Transient
	public boolean isDeslocamentoAtual() {
		return getDataSaida() == null;
	}
}
