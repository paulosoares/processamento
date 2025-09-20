package br.gov.stf.estf.entidade.processostf;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("RC")
public class TipoRecursoProcesso extends TipoRecurso {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3517824244630465347L;


}
