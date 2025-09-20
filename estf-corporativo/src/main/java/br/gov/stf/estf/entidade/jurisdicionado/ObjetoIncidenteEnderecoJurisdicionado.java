package br.gov.stf.estf.entidade.jurisdicionado;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "JUDICIARIO", name = "OBJETO_INCIDENTE_ENDERECO_JURI")
public class ObjetoIncidenteEnderecoJurisdicionado extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long seqObjetoIncidente;
	private Long seqEnderecoJurisdicionado;
	
	@Id
	@Column(name = "SEQ_OBJETO_INCI_ENDERECO_JURI")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_OBJETO_INCI_ENDERECO_JURI", allocationSize = 1)
	public Long getId() {
		return this.id;
	}
	public void setId(Long id){
		this.id = id;
	}

	@Column(name = "SEQ_ENDERECO_JURISDICIONADO")
	public Long getSeqEnderecoJurisdicionado() {
		return seqEnderecoJurisdicionado;
	}
	
	public void setSeqEnderecoJurisdicionado(Long seqEnderecoJurisdicionado) {
		this.seqEnderecoJurisdicionado = seqEnderecoJurisdicionado;
	}
	
	@Column(name = "SEQ_OBJETO_INCIDENTE")
	public Long getSeqObjetoIncidente() {
		return seqObjetoIncidente;
	}
	public void setSeqObjetoIncidente(Long seqObjetoIncidente) {
		this.seqObjetoIncidente = seqObjetoIncidente;
	}
}
