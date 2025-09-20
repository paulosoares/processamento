package br.gov.stf.estf.entidade.julgamento;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.processostf.Assunto;

@Entity
@Table( name="ASSUNTO_TEMA", schema="JUDICIARIO" )
public class AssuntoTema extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7796264711976347648L;
	private List<Assunto> assuntos;
	private Tema tema;
	
	
	@Id
	@Column( name="SEQ_ASSUNTO_TEMA" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_ASSUNTO_TEMA", allocationSize=1 )	
	public Long getId() {
		return id;
	}
	
	

	@ManyToMany(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name="COD_ASSUNTO", unique=false, nullable=true, insertable=false, updatable=false )
	public List<Assunto> getAssuntos() {
		return assuntos;
	}
	public void setAssuntos(List<Assunto> assuntos) {
		this.assuntos = assuntos;
	}

	@ManyToOne( cascade={}, fetch=FetchType.LAZY )
	@JoinColumn(name="SEQ_TEMA", unique=false, nullable=true, insertable=true, updatable=true )
	public Tema getTema() {
		return tema;
	}
	public void setTema(Tema tema) {
		this.tema = tema;
	}

}
