/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

/**
 * @author Paulo.Estevao
 * @since 03.08.2012
 */
@Entity
@Table(schema = "JURISPRUDENCIA", name = "TIPO_ESCOPO_LEGISLACAO")
public class TipoEscopoLegislacao extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6719946793964599991L;
	
	private Long id;
	private String sigla;
	private String descricao;
	private Long ordem;

	/* (non-Javadoc)
	 * @see br.gov.stf.framework.model.entity.BaseEntity#getId()
	 */
	@Override
	@Id
	@Column(name = "SEQ_TIPO_ESCOPO_LEGISLACAO")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "SIG_TIPO_ESCOPO_LEGISLACAO")
	public String getSigla() {
		return sigla;
	}
	
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	@Column(name = "DSC_TIPO_ESCOPO_LEGISLACAO")
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column(name = "NUM_ORDEM")
	public Long getOrdem() {
		return ordem;
	}
	
	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}

}
