package br.gov.stf.estf.entidade.jurisdicionado;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 * @author ViniciusK
 * @since 27.05.2013
 */


@Entity
@Table(schema = "JUDICIARIO", name = "TIPO_IDENTIFICACAO")
public class TipoIdentificacao extends ESTFBaseEntity<Long> {
	
	private static final long serialVersionUID = -8987971781059642809L;
	
	private Long id;
	private String descricaoTipoIdentificacao;
	private String siglaTipoIdentificacao;
	
	
	@Id
	@Column(name="SEQ_TIPO_IDENTIFICACAO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_TIPO_IDENTIFICACAO", allocationSize=1)
	public Long getId() {
		return this.id;
	}

	public void setId(Long identifier) {
		this.id = identifier;
	}
	
	
	@Column(name = "DSC_TIPO_IDENTIFICACAO")
	public String getDescricaoTipoIdentificacao() {
		return descricaoTipoIdentificacao;
	}

	public void setDescricaoTipoIdentificacao(String descricaoTipoIdentificacao) {
		this.descricaoTipoIdentificacao = descricaoTipoIdentificacao;
	}

	
	@Column(name = "SIG_TIPO_IDENTIFICACAO")
	public String getSiglaTipoIdentificacao() {
		return siglaTipoIdentificacao;
	}

	public void setSiglaTipoIdentificacao(String siglaTipoIdentificacao) {
		this.siglaTipoIdentificacao = siglaTipoIdentificacao;
	}

}
