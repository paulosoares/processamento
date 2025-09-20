package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "PROCESSO_INTERESSE", schema = "JUDICIARIO")
public class ProcessoInteresse extends ESTFBaseEntity<Long> {

	/**
	 * Processos de interesse de advogados
	 */
	private static final long serialVersionUID = -5475691318727365215L;
	private Long id;
	private Processo processo;
	private Jurisdicionado advogado;
	
	@Id
	@Column(name = "SEQ_PROCESSO_INTERESSE")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_PROCESSO_INTERESSE", allocationSize=1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	@ManyToOne(cascade = {})
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", unique = false, nullable = false, insertable = true, updatable = true)
	public Processo getProcesso() {
		return processo;
	}
	public void setProcesso(Processo processo) {
		this.processo = processo;
	}
	
	@ManyToOne(cascade = {})
	@JoinColumn(name = "SEQ_JURISDICIONADO", unique = false, nullable = false, insertable = true, updatable = true)
	public Jurisdicionado getAdvogado() {
		return advogado;
	}
	public void setAdvogado(Jurisdicionado advogado) {
		this.advogado = advogado;
	}
	
}
