/**
 * 
 */
package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 * @author Paulo.Estevao
 *
 */
@Entity
@Table(name = "TIPO_JULGAMENTOS", schema = "STF")
public class TipoJulgamento extends ESTFBaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String descricao;
	private Boolean ativo;
	private Long sequenciaCadeia;
	private TipoRecurso tipoRecurso;
	private String siglaRecurso;
	
	@Id
	@Column(name = "TIPO_JULGAMENTO")
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "DSC_TIPO")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Column(name = "NUM_SEQUENCIA_CADEIA")
	public Long getSequenciaCadeia() {
		return sequenciaCadeia;
	}

	public void setSequenciaCadeia(Long sequenciaCadeia) {
		this.sequenciaCadeia = sequenciaCadeia;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_RECURSO")
	public TipoRecurso getTipoRecurso() {
		return tipoRecurso;
	}

	public void setTipoRecurso(TipoRecurso tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
	}

	@Column(name = "SIG_RECURSO")
	public String getSiglaRecurso() {
		return siglaRecurso;
	}

	public void setSiglaRecurso(String siglaRecurso) {
		this.siglaRecurso = siglaRecurso;
	}

}
