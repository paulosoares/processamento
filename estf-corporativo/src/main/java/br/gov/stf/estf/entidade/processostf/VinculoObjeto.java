/**
 * 
 */
package br.gov.stf.estf.entidade.processostf;

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

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 * @author Paulo.Estevao
 * @since 24.06.2011
 */
@Entity
@Table(schema="JUDICIARIO", name="VINCULO_OBJETO")
public class VinculoObjeto extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4763166076761158393L;
	
	private Long id;
	private ObjetoIncidente<?> objetoIncidenteVinculador;
	private ObjetoIncidente<?> objetoIncidente;
	private TipoVinculoObjeto tipoVinculoObjeto;
	
	@Override
	@Id
	@Column(name = "SEQ_VINCULO_OBJETO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_VINCULO_OBJETO", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE_VINC")
	public ObjetoIncidente<?> getObjetoIncidenteVinculador() {
		return objetoIncidenteVinculador;
	}

	public void setObjetoIncidenteVinculador(
			ObjetoIncidente<?> objetoIncidenteVinculador) {
		this.objetoIncidenteVinculador = objetoIncidenteVinculador;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

//	@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "SEQ_TIPO_VINCULO_OBJETO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoVinculoObjeto")})
	public TipoVinculoObjeto getTipoVinculoObjeto() {
		return tipoVinculoObjeto;
	}

	public void setTipoVinculoObjeto(TipoVinculoObjeto tipoVinculoObjeto) {
		this.tipoVinculoObjeto = tipoVinculoObjeto;
	}
}
