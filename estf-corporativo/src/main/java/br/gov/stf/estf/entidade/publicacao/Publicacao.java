package br.gov.stf.estf.entidade.publicacao;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoEletronicoView;

@Entity
@Table(name = "DATA_PUBLICACOES", schema = "STF")
public class Publicacao extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 1182413141422114332L;
	private Date dataComposicaoDj;
	private Date dataPrevistaDj;
	private Date dataPublicacaoDj;
	private Short numeroDj;
	private Date dataDivulgacaoDje;
	private Short anoEdicaoDje;
	private Short numeroEdicaoDje;
	private DocumentoEletronicoView documentoEletronicoView;
	private DocumentoEletronico documentoEletronico;
	private Boolean formatoOnline;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_DOCUMENTO", insertable=true, updatable=true)	
	public DocumentoEletronico getDocumentoEletronico() {
		return documentoEletronico;
	}
	
	public void setDocumentoEletronico(DocumentoEletronico documentoEletronico) {
		this.documentoEletronico = documentoEletronico;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_DOCUMENTO", insertable=false, updatable=false)	
	public DocumentoEletronicoView getDocumentoEletronicoView() {
		return documentoEletronicoView;
	}

	public void setDocumentoEletronicoView(
			DocumentoEletronicoView documentoEletronicoView) {
		this.documentoEletronicoView = documentoEletronicoView;
	}

	

	@Id
	@Column(name = "SEQ_DATA_PUBLICACOES", nullable = false, precision = 10, scale = 0)
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="STF.SEQ_DATA_PUBLICACOES", allocationSize=1)
	public Long getId() {
		return id;
	}
	
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DAT_COMPOSICAO_DJ", nullable = false, length = 7)
	public Date getDataComposicaoDj() {
		return this.dataComposicaoDj;
	}

	public void setDataComposicaoDj(Date datComposicaoDj) {
		this.dataComposicaoDj = datComposicaoDj;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DAT_PREVISTA_DJ", length = 7)
	public Date getDataPrevistaDj() {
		return this.dataPrevistaDj;
	}

	public void setDataPrevistaDj(Date datPrevistaDj) {
		this.dataPrevistaDj = datPrevistaDj;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DAT_PUBLICACAO_DJ", length = 7)
	public Date getDataPublicacaoDj() {
		return this.dataPublicacaoDj;
	}

	public void setDataPublicacaoDj(Date datPublicacaoDj) {
		this.dataPublicacaoDj = datPublicacaoDj;
	}

	@Column(name = "NUM_DJ", precision = 3, scale = 0)
	public Short getNumeroDj() {
		return this.numeroDj;
	}

	public void setNumeroDj(Short numDj) {
		this.numeroDj = numDj;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_DIVULGACAO_DJE", length = 7)
	public Date getDataDivulgacaoDje() {
		return this.dataDivulgacaoDje;
	}

	public void setDataDivulgacaoDje(Date datDivulgacaoDje) {
		this.dataDivulgacaoDje = datDivulgacaoDje;
	}

	@Column(name = "ANO_EDICAO_DJE", precision = 4, scale = 0)
	public Short getAnoEdicaoDje() {
		return this.anoEdicaoDje;
	}

	public void setAnoEdicaoDje(Short anoEdicaoDje) {
		this.anoEdicaoDje = anoEdicaoDje;
	}

	@Column(name = "NUM_EDICAO_DJE", precision = 3, scale = 0)
	public Short getNumeroEdicaoDje() {
		return this.numeroEdicaoDje;
	}

	public void setNumeroEdicaoDje(Short numEdicaoDje) {
		this.numeroEdicaoDje = numEdicaoDje;
	}
	
	@Column(name = "FLG_FORMATO_ONLINE")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getFormatoOnline() {
		return formatoOnline;
	}
	
	public void setFormatoOnline(Boolean formatoOnline) {
		this.formatoOnline = formatoOnline;
	}
}
