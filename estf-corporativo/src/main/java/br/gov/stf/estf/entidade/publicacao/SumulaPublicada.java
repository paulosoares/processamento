package br.gov.stf.estf.entidade.publicacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.processostf.Sumula;

@Entity
@Table(name = "SUMULA_PUBLICADAS", schema = "STF")
public class SumulaPublicada extends ESTFBaseEntity<Long>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5694523531258852934L;

	@Id
	public Long getId() {
		return id;
	}
	
	private Sumula sumula;
	private Integer codigoCapitulo;
	private Integer codigoMateria;
	private Integer numero;
	private Short ano;
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumns( { 		 
		@JoinColumn(name = "SEQ_SUMULA", insertable = false, updatable = false)
	} )	
	public Sumula getSumula() {
		return sumula;
	}
	public void setSumula(Sumula sumula) {
		this.sumula = sumula;
	}
	
	@Column(name = "COD_CAPITULO", nullable = false, precision = 2, scale = 0)
	public Integer getCodigoCapitulo() {
		return codigoCapitulo;
	}
	public void setCodigoCapitulo(Integer codigoCapitulo) {
		this.codigoCapitulo = codigoCapitulo;
	}
	
	@Column(name = "COD_MATERIA", nullable = false, precision = 2, scale = 0)
	public Integer getCodigoMateria() {
		return codigoMateria;
	}
	public void setCodigoMateria(Integer codigoMateria) {
		this.codigoMateria = codigoMateria;
	}
	
	@Column(name = "NUM_MATERIA", nullable = false, precision = 5, scale = 0)
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	@Column(name = "ANO_MATERIA", nullable = false, precision = 4, scale = 0)
	public Short getAno() {
		return ano;
	}
	public void setAno(Short ano) {
		this.ano = ano;
	}

}
