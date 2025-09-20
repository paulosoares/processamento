package br.gov.stf.estf.entidade.jurisdicionado;

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

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;


@Entity
@Table(schema = "JUDICIARIO", name = "PAPEL_JURISDICIONADO")
public class PapelJurisdicionado extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private TipoJurisdicionado tipoJurisdicionado;
	private Jurisdicionado jurisdicionado;
	private String tipoPrazo;
	private Boolean padrao;
	private String tipoIntimacao;
	
	
	
	@Id
	@Column(name="SEQ_PAPEL_JURISDICIONADO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_PAPEL_JURISDICIONADO", allocationSize=1)
	public Long getId() {
		return this.id;
	}

	public void setId(Long identifier) {
		this.id = identifier;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_JURISDICIONADO", unique = false, nullable = false, insertable = true, updatable = true)
	public TipoJurisdicionado getTipoJurisdicionado() {
		return tipoJurisdicionado;
	}

	public void setTipoJurisdicionado(TipoJurisdicionado tipoJurisdicionado) {
		this.tipoJurisdicionado = tipoJurisdicionado;
	}



	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_JURISDICIONADO")
	public Jurisdicionado getJurisdicionado() {
		return jurisdicionado;
	}
	public void setJurisdicionado(Jurisdicionado jurisdicionado) {
		this.jurisdicionado = jurisdicionado;
	}

	

	@Column(name="TIP_PRAZO")
	public String getTipoPrazo() {
		return tipoPrazo;
	}

	public void setTipoPrazo(String tipoPrazo) {
		this.tipoPrazo = tipoPrazo;
	}

	
	
	@Column(name = "FLG_PADRAO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPadrao() {
		return padrao;
	}

	public void setPadrao(Boolean padrao) {
		this.padrao = padrao;
	}

	
	@Column(name = "TIP_INTIMACAO")
	public String getTipoIntimacao() {
		return tipoIntimacao;
	}

	public void setTipoIntimacao(String tipoIntimacao) {
		this.tipoIntimacao = tipoIntimacao;
	}
}
