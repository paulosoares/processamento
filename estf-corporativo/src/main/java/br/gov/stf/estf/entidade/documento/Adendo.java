/**
 * 
 */
package br.gov.stf.estf.entidade.documento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

/**
 * @author Paulo.Estevao
 * @since 28.11.2013
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "ADENDO_TEXTUAL", schema = "JUDICIARIO")
@SequenceGenerator(name = "SEQUENCE", sequenceName = "JUDICIARIO.SEQ_ADENDO_TEXTUAL", allocationSize = 1)
public class Adendo extends ESTFAuditavelBaseEntity<Long>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1843609865154680987L;
	
	private Long id;
	private TipoAdendo tipoAdendo;
	
	@Override
	@Id
	@Column(name = "SEQ_ADENDO_TEXTUAL")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.documento.TipoAdendo"),
			@Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "valueOfMethod", value = "valueOfSigla"),
			@Parameter(name = "identifierMethod", value = "getSigla")})
	@Column(name="TIP_ADENDO_TEXTUAL")
	public TipoAdendo getTipoAdendo() {
		return tipoAdendo;
	}

	public void setTipoAdendo(TipoAdendo tipoAdendo) {
		this.tipoAdendo = tipoAdendo;
	}
}
