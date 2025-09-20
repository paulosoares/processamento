package br.gov.stf.estf.entidade.julgamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

@Entity
@Table(name = "VINCULO_PROCESSO_TEMA", schema = "JUDICIARIO")
public class VinculoProcessoTema extends ESTFBaseEntity<Long> {

	private static final long	serialVersionUID	= 8133562370994070517L;

	private Tema				tema;
	private ObjetoIncidente<?> objetoIncidente;
	private Long 				id;

	@Id
	@Column(name = "SEQ_VINCULO_PROCESSO_TEMA", unique = true, nullable = false, insertable = false, updatable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id){
		this.id = id;
	}
		
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TEMA", unique = false, nullable = false, insertable = false, updatable = false)
	public Tema getTema() {
		return tema;
	}
	public void setTema(Tema tema){
		this.tema = tema;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", updatable = false, insertable = false)
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return (ObjetoIncidente<?>) objetoIncidente;
	}
	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}	

}
