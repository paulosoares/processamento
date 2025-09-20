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

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="CONTROLE_PRAZO_INTIMACAO", schema="STF")
// TODO não achei nenhuma tabela com o nome abaixo
//@Table(name="CONTROLE_PRAZO", schema="STF")
public class ControlePrazoIntimacao extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1455390977304567704L;
	
	private AndamentoProcesso andamentoProcesso;
	private Long parte;
	private Integer prazo;
	private Date dataIntimacao;
	private Date dataIntimado;

	@Id
	@Column(name="SEQ_CONTROLE_PRAZO_INTIMACAO", insertable = false, updatable = false) 
    @GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_CONTROLE_PRAZO_INTIMACAO", allocationSize=1)
	public Long getId() {
		return id;
	}
	
	@OneToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name = "SEQ_ANDAMENTO_PROCESSO",  insertable=true, updatable=false)
	public AndamentoProcesso getAndamentoProcesso() {
		return andamentoProcesso;
	}
	public void setAndamentoProcesso(AndamentoProcesso andamentoProcesso) {
		this.andamentoProcesso = andamentoProcesso;
	}
	
    @Column(name = "COD_PARTE")
	public Long getParte() {
		return parte;
	}

	public void setParte(Long parte) {
		this.parte = parte;
	}
	
	@Column(name="QTD_PRAZO")
	public Integer getPrazo() {
		return prazo;
	}

	public void setPrazo(Integer prazo) {
		this.prazo = prazo;
	}
	
	@Column ( name="DAT_INTIMACAO")
	public Date getDataIntimacao() {
		return dataIntimacao;
	}

	public void setDataIntimacao(Date dataIntimacao) {
		this.dataIntimacao = dataIntimacao;
	}
	
	@Column(name = "DAT_INTIMADO")
	public Date getDataIntimado() {
		return dataIntimado;
	}


	public void setDataIntimado(Date dataIntimado) {
		this.dataIntimado = dataIntimado;
	}

}
