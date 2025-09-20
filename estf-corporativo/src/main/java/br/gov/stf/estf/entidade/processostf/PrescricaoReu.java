/**
 * 
 */
package br.gov.stf.estf.entidade.processostf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.jurisdicionado.Jurisdicionado;

/**
 * @author Paulo.Estevao
 * @since 07.02.2011
 */
@Entity
@Table(name="PRESCRICAO_REU", schema="JUDICIARIO" )
public class PrescricaoReu extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6164084096295079722L;

	private Long id;
	private String tipoReuIncidente;
	private Date dataPrescricaoPenaMinima;
	private Date dataPrescricaoPenaMaxima;
	private Date dataPrescricao;
	private ReferenciaPrescricao referenciaPrescricao;
	private Jurisdicionado jurisdicionado;
	
	@Override
	@Id
	@Column(name="SEQ_PRESCRICAO_REU")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="TIP_REU_REINCIDENTE")
	public String getTipoReuIncidente() {
		return tipoReuIncidente;
	}

	public void setTipoReuIncidente(String tipoReuIncidente) {
		this.tipoReuIncidente = tipoReuIncidente;
	}

	@Column(name="DAT_PRESCRICAO_PENA_MINIMA")
	public Date getDataPrescricaoPenaMinima() {
		return dataPrescricaoPenaMinima;
	}

	public void setDataPrescricaoPenaMinima(Date dataPrescricaoPenaMinima) {
		this.dataPrescricaoPenaMinima = dataPrescricaoPenaMinima;
	}

	@Column(name="DAT_PRESCRICAO_PENA_MAXIMA")
	public Date getDataPrescricaoPenaMaxima() {
		return dataPrescricaoPenaMaxima;
	}

	public void setDataPrescricaoPenaMaxima(Date dataPrescricaoPenaMaxima) {
		this.dataPrescricaoPenaMaxima = dataPrescricaoPenaMaxima;
	}

	@Column(name="DAT_PRESCRICAO")
	public Date getDataPrescricao() {
		return dataPrescricao;
	}

	public void setDataPrescricao(Date dataPrescricao) {
		this.dataPrescricao = dataPrescricao;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_REFERENCIA_PRESCRICAO")
	public ReferenciaPrescricao getReferenciaPrescricao() {
		return referenciaPrescricao;
	}

	public void setReferenciaPrescricao(ReferenciaPrescricao referenciaPrescricao) {
		this.referenciaPrescricao = referenciaPrescricao;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_JURISDICIONADO")
	public Jurisdicionado getJurisdicionado() {
		return jurisdicionado;
	}

	public void setJurisdicionado(Jurisdicionado jurisdicionado) {
		this.jurisdicionado = jurisdicionado;
	}

}
