package br.gov.stf.estf.entidade.documento;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="INTEIRO_TEOR_ACORDAO", schema="DOC")
public class InteiroTeorAcordao extends ESTFBaseEntity<Long>{

	private static final long serialVersionUID = 6565519512877599278L;
	private Long id;
	private byte[] binArquivo;
	private String formato;

	public InteiroTeorAcordao() {
		
	}
	

	@Id          
    @Column(name="SEQ_INTEIRO_TEOR_ACORDAO") 
    @GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "DOC.SEQ_INTEIRO_TEOR_ACORDAO", allocationSize=1)
	public Long getId() {
		return id;
	}
	
	
	public void setId(Long id) {
		this.id = id;
	}


	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name = "BIN_ARQUIVO")
	public byte[] getBinArquivo() {
		return this.binArquivo;
	}

	public void setBinArquivo(byte[] binArquivo) {
		this.binArquivo = binArquivo;
	}
	

	@Column(name = "DSC_FORMATO", length = 20)
	public String getFormato() {
		return this.formato;
	}

	public void setFormato(String dscFormato) {
		this.formato = dscFormato;
	}

}
