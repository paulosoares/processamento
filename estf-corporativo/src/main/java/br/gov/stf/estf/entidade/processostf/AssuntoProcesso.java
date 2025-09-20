package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

@MappedSuperclass
@Entity
@Table(name = "ASSUNTO_PROCESSO", schema = "STF")
public class AssuntoProcesso extends ESTFAuditavelBaseEntity<Long> {
	
	private static final long serialVersionUID = 8759035902318087017L;
	
	private Assunto assunto;
	private ObjetoIncidente objetoIncidente;
	private Integer ordem;
	
	@Id
	@Column(name="SEQ_ASSUNTO_PROCESSO")
	public Long getId() {
		return id;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "COD_ASSUNTO", nullable = false)
	public Assunto getAssunto() {
		return assunto;
	}

	public void setAssunto(Assunto assunto) {
		this.assunto = assunto;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@Column(name = "NUM_ORDEM", nullable = false)
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

}
