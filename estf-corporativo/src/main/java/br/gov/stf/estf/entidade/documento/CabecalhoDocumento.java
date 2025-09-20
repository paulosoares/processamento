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
@Table( name="CABECALHO_DOCUMENTO", schema="DOC" )
public class CabecalhoDocumento extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7520055882516757751L;
	private byte[] cabecalhoProcesso;
	
	@Id
	@Column( name="SEQ_CABECALHO_DOCUMENTO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="DOC.SEQ_CABECALHO_DOCUMENTO", allocationSize=1)
	public Long getId() {
		return id;
	}


	@Lob
	@Basic( fetch=FetchType.LAZY )
	@Column( name = "BIN_CABECALHO_PROCESSO", updatable=true, insertable=true, unique=false, nullable=true )
	public byte[] getCabecalhoProcesso() {
		return cabecalhoProcesso;
	}
	public void setCabecalhoProcesso(byte[] cabecalhoProcesso) {
		this.cabecalhoProcesso = cabecalhoProcesso;
	}


}
