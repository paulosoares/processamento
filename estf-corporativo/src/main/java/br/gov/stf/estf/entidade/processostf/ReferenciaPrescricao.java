/**
 * 
 */
package br.gov.stf.estf.entidade.processostf;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 * @author Paulo.Estevao
 * @since 07.02.2011
 */
@Entity
@Table(name="REFERENCIA_PRESCRICAO", schema="JUDICIARIO" )
public class ReferenciaPrescricao extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6966251898423991481L;
	private Long id;
	private ObjetoIncidente<?> objetoIncidente;
	private Date dataFato;
	private Date dataTermoInicial;
	private Long tipoTermoInicial;
	private List<PrescricaoReu> prescricaoReus;
	
	@Override
	@Id
	@Column(name="SEQ_REFERENCIA_PRESCRICAO")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@Column(name="DAT_FATO")
	public Date getDataFato() {
		return dataFato;
	}

	public void setDataFato(Date dataFato) {
		this.dataFato = dataFato;
	}

	@Column(name="DAT_TERMO_INICIAL")
	public Date getDataTermoInicial() {
		return dataTermoInicial;
	}

	public void setDataTermoInicial(Date dataTermoInicial) {
		this.dataTermoInicial = dataTermoInicial;
	}

	@Column(name="SEQ_TIPO_TERMO_INICIAL")
	public Long getTipoTermoInicial() {
		return tipoTermoInicial;
	}

	public void setTipoTermoInicial(Long tipoTermoInicial) {
		this.tipoTermoInicial = tipoTermoInicial;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_REFERENCIA_PRESCRICAO", updatable = false, insertable = false)
	public List<PrescricaoReu> getPrescricaoReus() {
		return prescricaoReus;
	}
	
	public void setPrescricaoReus(List<PrescricaoReu> prescricaoReus) {
		this.prescricaoReus = prescricaoReus;
	}
}
