/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

/**
 * 
 */
@Entity
@Table(schema = "BRS", name = "PUBLICACAO_ACORDAO")
public class PublicacaoAcordao extends ESTFAuditavelBaseEntity<Long> {

	private static final long serialVersionUID = -879596355056533834L;

	
	private Long id;
	private AcordaoJurisprudencia acordaoJurisprudencia;
	
	@Override
	@Id
	@Column(name = "SEQ_PUBLICACAO_ACORDAO")
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_SJUR")
	public AcordaoJurisprudencia getAcordaoJurisprudencia() {
		return acordaoJurisprudencia;
	}
	
	public void setAcordaoJurisprudencia(AcordaoJurisprudencia acordaoJurisprudencia) {
		this.acordaoJurisprudencia = acordaoJurisprudencia;
	}
	
}
