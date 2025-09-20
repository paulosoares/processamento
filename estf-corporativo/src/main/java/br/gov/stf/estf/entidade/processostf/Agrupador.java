package br.gov.stf.estf.entidade.processostf;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;

@Entity
@Table(schema = "JUDICIARIO", name = "AGRUPADOR")
public class Agrupador extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 6292481667592602662L;

	private String descricao;
	private Setor setor;
	private List<ObjetoIncidente<?>> objetosIncidentes;
	
	@Id
	@Column(name = "SEQ_AGRUPADOR")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_AGRUPADOR", allocationSize = 1)
	@Override
	public Long getId() {
		return id;
	}

	@Column(name = "DSC_AGRUPADOR", unique = false, nullable = false, insertable = true, updatable = true, length = 200)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@ManyToOne
	@JoinColumn(name = "COD_SETOR", referencedColumnName = "COD_SETOR")
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@ManyToMany(fetch = FetchType.LAZY, targetEntity = ObjetoIncidente.class)
	@JoinTable(schema = "JUDICIARIO", name = "AGRUPADOR_OBJETO_INCIDENTE", joinColumns = @JoinColumn(name = "SEQ_AGRUPADOR"), inverseJoinColumns = @JoinColumn(name = "SEQ_OBJETO_INCIDENTE"))
	public List<ObjetoIncidente<?>> getObjetosIncidentes() {
		return this.objetosIncidentes;
	}

	public void setObjetosIncidentes(List<ObjetoIncidente<?>> objetosIncidentes) {
		this.objetosIncidentes = objetosIncidentes;
	}
}
