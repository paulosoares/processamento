package br.gov.stf.estf.entidade.jurisdicionado;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="INFORMACAO_JURISDICIONADO", schema="JUDICIARIO" )
public class InformacaoJurisdicionado {
	
	private Long id;
	private String email;
	
	@Id
	@Column(name = "SEQ_INFORMACAO_JURISDICIONADO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_INFORMACAO_JURISDICIONADO", allocationSize = 1)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "DSC_CORREIO_ELETRONICO", nullable = false)
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
}
