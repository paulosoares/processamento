package br.gov.stf.estf.entidade.jurisprudencia;

import javax.persistence.Basic;
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

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.ministro.Ministro;

@Entity
@Table(schema = "JURISPRUDENCIA", name = "JURISPRUDENCIA_RELEVANTE")
public class JurisprudenciaRelevante extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3364646474697997326L;
	/**
	 * 
	 */
	
	
	private Long id;
	private AcordaoJurisprudencia acordaoJurisprudencia;
	private Despacho despacho;
	private BaseJurisprudenciaRepercussaoGeral repercussaoGeral;
	private String tipoJurisprudencia;
	private Ministro ministro;
	private Boolean publicado;
	private String numeroOrdem;
	private String indexacao;
	
	@Override
	@Id
	@Column(name = "SEQ_JURISPRUDENCIA_RELEVANTE")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JURISPRUDENCIA.SEQ_JURISPRUDENCIA_RELEVANTE", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_SJUR")
	public AcordaoJurisprudencia getAcordaoJurisprudencia() {
		return acordaoJurisprudencia;
	}

	public void setAcordaoJurisprudencia(AcordaoJurisprudencia acordaoJurisprudencia) {
		this.acordaoJurisprudencia = acordaoJurisprudencia;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_DESPACHO")
	public Despacho getDespacho() {
		return despacho;
	}

	public void setDespacho(Despacho despacho) {
		this.despacho = despacho;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_REPERCUSSAO_GERAL")
	public BaseJurisprudenciaRepercussaoGeral getRepercussaoGeral() {
		return repercussaoGeral;
	}

	public void setRepercussaoGeral(
			BaseJurisprudenciaRepercussaoGeral repercussaoGeral) {
		this.repercussaoGeral = repercussaoGeral;
	}

	@Column(name = "TIP_JURISPRUDENCIA")
	public String getTipoJurisprudencia() {
		return tipoJurisprudencia;
	}

	public void setTipoJurisprudencia(String tipoJurisprudencia) {
		this.tipoJurisprudencia = tipoJurisprudencia;
	}

	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPublicado() {
		return publicado;
	}

	public void setPublicado(Boolean publicado) {
		this.publicado = publicado;
	}

	@Column(name = "NUM_ORDEM")
	public String getNumeroOrdem() {
		return numeroOrdem;
	}

	public void setNumeroOrdem(String numeroOrdem) {
		this.numeroOrdem = numeroOrdem;
	}

	@Basic(fetch = FetchType.LAZY)
	@Column(name = "DSC_INDEXACAO")
	public String getIndexacao() {
		return indexacao;
	}

	public void setIndexacao(String indexacao) {
		this.indexacao = indexacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_mINISTRO")	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}
	
	
	
}
