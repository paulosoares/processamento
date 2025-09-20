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

@Entity
@Table(schema = "JUDICIARIO", name = "AGRUPADOR_OBJETO_INCIDENTE")
public class AgrupadorObjetoIncidente extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 1;

	private Agrupador categoria;
	private ObjetoIncidente<?> objetoIncidente;
	
	public AgrupadorObjetoIncidente() {
		super();
	}
	
	public AgrupadorObjetoIncidente(Agrupador categoria, ObjetoIncidente<?> objetoIncidente) {
		super();
		this.categoria = categoria;
		this.objetoIncidente = objetoIncidente;
	}

	@Id
	@Column(name = "SEQ_AGRUPADOR_OBJETO_INCIDENTE")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_AGRUPADOR_OBJETO_INCIDENTE", allocationSize = 1)
	@Override
	public Long getId() {
		return id;
	}

	@ManyToOne
	@JoinColumn(name = "SEQ_AGRUPADOR", referencedColumnName = "SEQ_AGRUPADOR")
	public Agrupador getCategoria() {
		return categoria;
	}

	public void setCategoria(Agrupador categoria) {
		this.categoria = categoria;
	}

	@ManyToOne
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", referencedColumnName = "SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	
}
