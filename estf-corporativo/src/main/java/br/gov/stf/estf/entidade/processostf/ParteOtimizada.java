package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name = "VW_JURISDIC_INCID_OTIMIZADA", schema = "JUDICIARIO")
public class ParteOtimizada extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5568159659868796311L;

	private Long id;
	private ObjetoIncidente<?> objetoIncidente;
	private String nomeJurisdicionado;
	private Categoria categoria;

	@Id
	@Column(name = "SEQ_JURISDICIONADO")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", nullable = false, insertable = false, updatable = false)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@Column(name = "NOM_JURISDICIONADO")
	public String getNomeJurisdicionado() {
		return nomeJurisdicionado;
	}

	public void setNomeJurisdicionado(String nomeJurisdicionado) {
		this.nomeJurisdicionado = nomeJurisdicionado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_CATEGORIA", nullable = false, insertable = false, updatable = false)
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

}
