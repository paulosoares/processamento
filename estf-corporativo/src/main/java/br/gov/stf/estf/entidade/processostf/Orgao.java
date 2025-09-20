package br.gov.stf.estf.entidade.processostf;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "JUDICIARIO", name = "ORGAO")
public class Orgao extends ESTFBaseEntity<Long> {
	
	private Long id;
	
	private String descricao;
	
	private String sigla;
	
	private List<RegiaoOrgao> regioesOrgaos;
	
	@Id
	@Column(name = "SEQ_ORGAO")
	public Long getId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see br.gov.stf.framework.entity.Entity#setId(java.io.Serializable)
	 */
	public void setId(Long identifier) {
		this.id = identifier;
	}

	/**
	 * @return the descricao
	 */
	@Column(name = "DSC_ORGAO")
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the sigla
	 */
	@Column(name = "SIG_ORGAO")
	public String getSigla() {
		return sigla;
	}

	/**
	 * @param sigla the sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@OneToMany(mappedBy="orgao", fetch=FetchType.LAZY)
	public List<RegiaoOrgao> getRegioesOrgaos() {
		return regioesOrgaos;
	}

	public void setRegioesOrgaos(List<RegiaoOrgao> regioesOrgaos) {
		this.regioesOrgaos = regioesOrgaos;
	}

}
