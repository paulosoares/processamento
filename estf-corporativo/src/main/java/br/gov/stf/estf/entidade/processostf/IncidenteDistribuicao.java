package br.gov.stf.estf.entidade.processostf;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.publicacao.AtaDistribuicao;

@Entity
@Table(name="INCIDENTE_DISTRIBUICAO", schema="JUDICIARIO")
public class IncidenteDistribuicao extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 8723474711952107822L;
	
	private ObjetoIncidente<Processo> objetoIncidente;
	private TipoDistribuicao tipoDistribuicao;
	private CaracteristicaDistribuicao caracteristicaDistribuicao;
	private List<AtaDistribuicao> atasDistribuicao;

	@Id
	@Column(name="SEQ_INCIDENTE_DISTRIBUICAO")
	public Long getId() {
		return this.id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_OBJETO_INCIDENTE", insertable=false, updatable=false)
	public ObjetoIncidente<Processo> getObjetoIncidente() {
		return objetoIncidente;
	}


	public void setObjetoIncidente(ObjetoIncidente<Processo> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@Column(name = "SEQ_TIPO_DISTRIBUICAO", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoDistribuicao")})
	public TipoDistribuicao getTipoDistribuicao() {
		return tipoDistribuicao;
	}


	public void setTipoDistribuicao(TipoDistribuicao tipoDistribuicao) {
		this.tipoDistribuicao = tipoDistribuicao;
	}
	
	@Column(name = "SEQ_CARACT_DISTRIBUICAO", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.CaracteristicaDistribuicao")})
	public CaracteristicaDistribuicao getCaracteristicaDistribuicao() {
		return caracteristicaDistribuicao;
	}

	public void setCaracteristicaDistribuicao(
			CaracteristicaDistribuicao caracteristicaDistribuicao) {
		this.caracteristicaDistribuicao = caracteristicaDistribuicao;
	}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "JUDICIARIO.DISTRIBUICAO", joinColumns = @JoinColumn(name = "SEQ_INCIDENTE_DISTRIBUICAO"), inverseJoinColumns = @JoinColumn(name = "SEQ_ATA_DISTRIBUICAO"))
	public List<AtaDistribuicao> getAtasDistribuicao() {
		return atasDistribuicao;
	}
	
	public void setAtasDistribuicao(List<AtaDistribuicao> atasDistribuicao) {
		this.atasDistribuicao = atasDistribuicao;
	}

	@Transient
	public String getTipoDistribuicaoDJ() {
		
		if ( TipoDistribuicao.COMUM==tipoDistribuicao ) {
			if ( CaracteristicaDistribuicao.PREVENCAO_RELATOR_SUCESSOR==caracteristicaDistribuicao ) {
				return "DISTRIBUÍDO POR PREVENÇÃO";
			} else if ( CaracteristicaDistribuicao.PREVENCAO_TURMA==caracteristicaDistribuicao ) {
				return "DISTRIBUÍDO POR EXCLUSÃO DE MINISTRO";
			}
		} else if ( TipoDistribuicao.REDISTRIBUICAO==tipoDistribuicao ) {
			if ( CaracteristicaDistribuicao.COMUM==caracteristicaDistribuicao ) {
				return "REDISTRIBUÍDO";
			} else if ( CaracteristicaDistribuicao.PREVENCAO_RELATOR_SUCESSOR==caracteristicaDistribuicao ) {
				return "REDISTRIBUÍDO POR PREVENÇÃO";
			}
		}
		return null;
	}

	

}
