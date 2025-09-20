package br.gov.stf.estf.entidade.documento;


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

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.NotFoundAction;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

@Entity
@Table(name="ARQUIVO_PROCESSO_ELETRONICO" ,schema="STF")
public class ArquivoProcessoEletronico extends ESTFAuditavelBaseEntity<Long> {

	private static final long serialVersionUID = 885348627574475803L;
	
	private PecaProcessoEletronico pecaProcessoEletronico;
    private Long numeroOrdem;
    private DocumentoEletronico documentoEletronico;
    private DocumentoEletronicoView documentoEletronicoView;

	@Id
	@Column(name="SEQ_ARQ_PROCESSO_ELETRONICO", nullable=false, insertable=true, updatable=true, precision=10, scale=0)
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="STF.SEQ_ARQ_PROCESSO_ELETRONICO", allocationSize=1 )
    public Long getId() {
        return id;
    }    
   
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)    
    @JoinColumn(name="SEQ_PECA_PROC_ELETRONICO", unique=false, nullable=false, insertable=true, updatable=true)
    public PecaProcessoEletronico getPecaProcessoEletronico() {
        return this.pecaProcessoEletronico;
    }
    
    public void setPecaProcessoEletronico(PecaProcessoEletronico pecaProcessoEletronico) {
        this.pecaProcessoEletronico = pecaProcessoEletronico;
    }
    
    @Column(name="NUM_ORDEM", unique=false, nullable=true, insertable=true, updatable=true, precision=10, scale=0)
    public Long getNumeroOrdem() {
        return this.numeroOrdem;
    }
    
    public void setNumeroOrdem(Long numeroOrdem) {
        this.numeroOrdem = numeroOrdem;
    }
    
	@ManyToOne(cascade={}, fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_DOCUMENTO", unique = true)
    public DocumentoEletronico getDocumentoEletronico() {
        return this.documentoEletronico;
    }
    
    public void setDocumentoEletronico(DocumentoEletronico documentoEletronico) {
        this.documentoEletronico = documentoEletronico;
    }
    
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_DOCUMENTO", referencedColumnName="SEQ_DOCUMENTO", updatable = false, insertable = false)
	@LazyToOne(value=LazyToOneOption.NO_PROXY)
	@org.hibernate.annotations.NotFound(action=NotFoundAction.IGNORE)
	public DocumentoEletronicoView getDocumentoEletronicoView() {
		return documentoEletronicoView;
	}

	public void setDocumentoEletronicoView(
			DocumentoEletronicoView documentoEletronicoView) {
		this.documentoEletronicoView = documentoEletronicoView;
	}

}


