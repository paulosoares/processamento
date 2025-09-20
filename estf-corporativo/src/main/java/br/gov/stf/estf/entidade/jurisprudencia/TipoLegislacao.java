/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

/**
 * @author Paulo.Estevao
 * @since 03.08.2012
 */
@Entity
@Table(schema = "SJUR", name = "SIG_LEGISLACOES")
public class TipoLegislacao extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8347471397399392642L;
	
	private Long id;
	private String tipoLegislacao;
	private String sigla;
	private String descricao;
	private Boolean apresentacao;
	private Long ordem;

	/* (non-Javadoc)
	 * @see br.gov.stf.framework.model.entity.BaseEntity#getId()
	 */
	@Override
	@Id
	@Column(name = "SEQ_TIPO_LEGISLACAO")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "TIPO_LEGIS")
	public String getTipoLegislacao() {
		return tipoLegislacao;
	}
	
	public void setTipoLegislacao(String tipoLegislacao) {
		this.tipoLegislacao = tipoLegislacao;
	}
	
	@Column(name = "SIG_LEGIS")
	public String getSigla() {
		return sigla;
	}
	
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	@Column(name = "DSC_LEGIS")
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column(name = "FLG_APRESENTACAO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getApresentacao() {
		return apresentacao;
	}
	
	public void setApresentacao(Boolean apresentacao) {
		this.apresentacao = apresentacao;
	}
	
	@Column(name = "NUM_ORDEM")
	public Long getOrdem() {
		return ordem;
	}
	
	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}

}
