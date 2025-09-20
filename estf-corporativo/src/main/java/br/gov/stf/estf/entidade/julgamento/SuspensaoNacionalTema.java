package br.gov.stf.estf.entidade.julgamento;

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

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

@Entity
@Table(name = "SUSPENSAO_NACIONAL_TEMA", schema = "JUDICIARIO")
public class SuspensaoNacionalTema extends ESTFBaseEntity<Long> {
	
	private static final long serialVersionUID = 1L;
	
	private Date dataInicial;
	private Date dataFinal;
	private ObjetoIncidente<?> objetoIncidente;
	private Tema tema;
	private AndamentoProcesso andamentoProcesso;

	@Id
	@Column( name="SEQ_SUSPENSAO_NACIONAL_TEMA" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JUDICIARIO.SEQ_SUSPENSAO_NACIONAL_TEMA", allocationSize=1 )	
	public Long getId() {
		return id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_INICIAL_SUSPENSAO")
	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_FINAL_SUSPENSAO")
	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE_ORIG_SUSP")	
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(
			ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TEMA", unique = false, nullable = false, insertable = true, updatable = false)
	public Tema getTema() {
		return tema;
	}
	
	public void setTema(Tema tema){
		this.tema = tema;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ANDAMENTO_PROCESSO")	
	public AndamentoProcesso getAndamentoProcesso() {
		return andamentoProcesso;
	}
	
	public void setAndamentoProcesso(AndamentoProcesso andamentoProcesso) {
		this.andamentoProcesso = andamentoProcesso;
	}
	
	
}
