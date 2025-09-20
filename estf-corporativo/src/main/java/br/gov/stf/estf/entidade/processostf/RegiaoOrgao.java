package br.gov.stf.estf.entidade.processostf;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Origem;

@Entity
@Table(name="REGIAO_ORGAO", schema="JUDICIARIO")
public class RegiaoOrgao extends ESTFBaseEntity<Long>{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Regiao regiao;
	
	private Orgao orgao;
	
	private List<RegiaoProcedencia> regioesProcedencia;
	
	private List<Origem> origens;

	@Id
	@SequenceGenerator(name="SEQ_REGIAO_ORGAO", sequenceName="JUDUCUARIO.SEQ_REGIAO_ORGAO", allocationSize = 1)
	@GeneratedValue(generator="SEQ_REGIAO_ORGAO", strategy=GenerationType.SEQUENCE)
	@Column(name="SEQ_REGIAO_ORGAO")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_REGIAO", referencedColumnName="SEQ_REGIAO")
	public Regiao getRegiao() {
		return regiao;
	}

	public void setRegiao(Regiao regiao) {
		this.regiao = regiao;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_ORGAO", referencedColumnName="SEQ_ORGAO")
	public Orgao getOrgao() {
		return orgao;
	}

	public void setOrgao(Orgao orgao) {
		this.orgao = orgao;
	}

	@OneToMany(mappedBy="regiaoOrgao")
	public List<RegiaoProcedencia> getRegioesProcedencia() {
		return regioesProcedencia;
	}

	public void setRegioesProcedencia(List<RegiaoProcedencia> regioesProcedencia) {
		this.regioesProcedencia = regioesProcedencia;
	}

	public void setOrigens(List<Origem> origens) {
		this.origens = origens;
	}

	@OneToMany(mappedBy="regiaoOrgao")
	public List<Origem> getOrigens() {
		return origens;
	}
}
