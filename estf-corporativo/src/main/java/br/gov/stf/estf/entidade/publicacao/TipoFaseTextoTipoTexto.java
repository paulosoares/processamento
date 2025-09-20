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
import br.gov.stf.estf.entidade.documento.TipoTexto;


@Entity
@Table( name="TIPO_FASE_TEXTO_TIPO_TEXTO", schema="PUBLICACAO" )
public class TipoFaseTextoTipoTexto extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8279126270221167714L;
	private TipoTexto tipoTexto;
	private TipoFaseTexto tipoFaseTexto;
	
	
	@Id
	@Column( name="SEQ_TIPO_FASE_TEXTO_TIPO_TEXTO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_TIPO_FASE_TEXTO_TIPO_TEXTO", allocationSize=1 )
	public Long getId() {
		return id;
	}	
	
	
	
	@Column(name="COD_TIPO_TEXTO")
	public TipoTexto getTipoTexto() {
		return tipoTexto;
	}
	public void setTipoTexto(TipoTexto tipoTexto) {
		this.tipoTexto = tipoTexto;
	}

	@ManyToOne( cascade={}, fetch=FetchType.LAZY )
	@JoinColumn(name="SEQ_TIPO_FASE_TEXTO", unique=false, nullable=true, insertable=true, updatable=true )
	public TipoFaseTexto getTipoFaseTexto() {
		return tipoFaseTexto;
	}
	public void setTipoFaseTexto(TipoFaseTexto tipoFaseTexto) {
		this.tipoFaseTexto = tipoFaseTexto;
	}

}
