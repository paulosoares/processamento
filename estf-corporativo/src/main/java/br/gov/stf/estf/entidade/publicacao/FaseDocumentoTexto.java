package br.gov.stf.estf.entidade.publicacao;

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

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;

@Entity
@Table( name="FASE_DOCUMENTO_TEXTO", schema="PUBLICACAO" )
public class FaseDocumentoTexto extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6016768504495635947L;
	private FaseTextoProcesso faseTextoProcesso;
	private DocumentoTexto documentoTexto; 
	
	
	@Id
	@Column( name="SEQ_FASE_DOCUMENTO_TEXTO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_FASE_DOCUMENTO_TEXTO", allocationSize=1 )
	public Long getId() {
		return id;
	}

	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_FASE_TEXTO_PROCESSO", unique=false, nullable=true, insertable=true, updatable=true)
	public FaseTextoProcesso getFaseTextoProcesso() {
		return faseTextoProcesso;
	}
	public void setFaseTextoProcesso(FaseTextoProcesso faseTextoProcesso) {
		this.faseTextoProcesso = faseTextoProcesso;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_DOCUMENTO", unique=false, nullable=true, insertable=true, updatable=true)
	public DocumentoTexto getDocumentoTexto() {
		return documentoTexto;
	}
	public void setDocumentoTexto(DocumentoTexto documentoTexto) {
		this.documentoTexto = documentoTexto;
	}

}
