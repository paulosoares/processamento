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

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="INCIDENTE_PREFERENCIA", schema="JUDICIARIO" )
public class IncidentePreferencia extends ESTFBaseEntity<Long> {
	
	private static final long serialVersionUID = 769969877470111753L;
	
	private TipoIncidentePreferencia tipoPreferencia;
	private ObjetoIncidente<?> objetoIncidente;
	
	@Id
	@Column(name="SEQ_INCIDENTE_PREFERENCIA")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_INCIDENTE_PREFERENCIA", allocationSize=1)
	public Long getId() {
		return id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_TIPO_PREFERENCIA")
	public TipoIncidentePreferencia getTipoPreferencia() {
		return tipoPreferencia;
	}

	public void setTipoPreferencia(TipoIncidentePreferencia tipoPreferencia) {
		this.tipoPreferencia = tipoPreferencia;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
}
