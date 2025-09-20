package br.gov.stf.estf.entidade.documento;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="PECA_PROC_ELET_COMUNICACAO", schema="JUDICIARIO")
public class PecaProcessoEletronicoComunicacao extends ESTFBaseEntity<Long>{
	
	private static final long serialVersionUID = 7471304944816868811L;
	
	private Long id;
	private Comunicacao comunicacao;
	private PecaProcessoEletronico pecaProcessoEletronico;
	private Date dataVinculacao;
	private String situacaoPeca;
	private Long pecaDividida;
	private Boolean excluida = false;

	private DocumentoEletronico documentoAcordao;
	
	
	@Id
	@Column( name="SEQ_PECA_PROC_ELET_COMUNICACAO", nullable = false)	
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JUDICIARIO.SEQ_PECA_PROC_ELET_COMUNICACAO", allocationSize=1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(cascade={},  fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_COMUNICACAO", unique = true)		
	public Comunicacao getComunicacao() {
		return comunicacao;
	}

	public void setComunicacao(Comunicacao comunicacao) {
		this.comunicacao = comunicacao;
	}
	
	@ManyToOne(cascade={},  fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_PECA_PROC_ELETRONICO", unique = true)	
	public PecaProcessoEletronico getPecaProcessoEletronico() {
		return pecaProcessoEletronico;
	}

	public void setPecaProcessoEletronico(
			PecaProcessoEletronico pecaProcessoEletronico) {
		this.pecaProcessoEletronico = pecaProcessoEletronico;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_VINCULACAO", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataVinculacao() {
		return dataVinculacao;
	}

	public void setDataVinculacao(Date dataVinculacao) {
		this.dataVinculacao = dataVinculacao;
	}
	
	@ManyToOne(cascade={}, fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_DOCUMENTO_ACORDAO", referencedColumnName="SEQ_DOCUMENTO")	
	public DocumentoEletronico getDocumentoAcordao() {
		return documentoAcordao;
	}

	public void setDocumentoAcordao(DocumentoEletronico documentoAcordao) {
		this.documentoAcordao = documentoAcordao;
	}
	
	
	@Formula("(SELECT NVL(PP.SEQ_TIPO_SITUACAO_PECA,'1') from JUDICIARIO.PECA_PROCESSUAL PP where PP.SEQ_TIPO_SITUACAO_PECA in (3) AND PP.SEQ_PECA_PROCESSUAL = SEQ_PECA_PROC_ELETRONICO )")
	public String getSituacaoPeca() {
		return situacaoPeca;
	}
	
	public void setSituacaoPeca(String situacaoPeca) {
		this.situacaoPeca = situacaoPeca;
	}
	
	@Formula("(SELECT PP.SEQ_PECA_PROC_ELETRONICO from STF.PECA_PROCESSO_ELETRONICO PP  where PP.SEQ_PECA_PROC_ELETRONICO = SEQ_PECA_PROC_ELETRONICO )")
	public Long getPecaDividida() {
		return pecaDividida;
		
	}

	public void setPecaDividida(Long pecaDividida) {
		this.pecaDividida = pecaDividida;
	}

	@Transient
	public Boolean getExcluida() {
		if(pecaDividida != null) {
			return false;
		}else {
			return true;
		}
	}
		public void setExcluida(Boolean excluida) {
		this.excluida = excluida;
	}
}
