package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="REGIAO_PROCEDENCIA", schema="JUDICIARIO")
public class RegiaoProcedencia extends ESTFBaseEntity<Long> {
	
	private Long id;
	
	private Procedencia procedencia;
	
	private RegiaoOrgao regiaoOrgao;
	
	@Id
	@SequenceGenerator(name="SEQ_REGIAO_PROCEDENCIA", sequenceName="JUDICIARIO.SEQ_REGIAO_PROCEDENCIA", allocationSize=1, initialValue=1)
	@GeneratedValue(generator="SEQ_REGIAO_PROCEDENCIA", strategy=GenerationType.SEQUENCE)
	@Column(name="SEQ_REGIAO_PROCEDENCIA")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_PROCEDENCIA", referencedColumnName="COD_PROCEDENCIA")
	public Procedencia getProcedencia() {
		return procedencia;
	}

	public void setProcedencia(Procedencia procedencia) {
		this.procedencia = procedencia;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_REGIAO_ORGAO", referencedColumnName="SEQ_REGIAO_ORGAO")
	public RegiaoOrgao getRegiaoOrgao() {
		return regiaoOrgao;
	}

	public void setRegiaoOrgao(RegiaoOrgao regiaoOrgao) {
		this.regiaoOrgao = regiaoOrgao;
	}
}
