package br.gov.stf.estf.entidade.documento;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.usuario.Usuario;

@Entity
@Table(name="VW_ASSINATURA_DIGITAL" ,schema="DOC")
public class AssinaturaDigitalView extends ESTFBaseEntity<Long> {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 8638308342691176084L;
//	private DocumentoEletronicoView documentoEletronico;
	private Date dataCarimboTempo;
    private Usuario usuarioInclusao;
    private Date dataInclusao;
    private Long documentoEletronico;
//    private AssinaturaDigital assinaturaDigital;
    
    @Id
	@Column( name="SEQ_ASSINATURA_DIGITAL", insertable = false, updatable = false, unique = true )
//    @Column( name="SEQ_DOCUMENTO", insertable = false, updatable = false, unique = true )
    public Long getId() {
        return this.id;
    }
    
/*	@Cascade(value={})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_DOCUMENTO", unique=false, nullable=false, insertable=false, updatable=false)
    public DocumentoEletronico getDocumentoEletronico() {
        return this.documentoEletronico;
    }

    public void setDocumentoEletronico(DocumentoEletronico documento) {
        this.documentoEletronico = documento;
    }*/
    
    
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DAT_TIMESTAMP", unique=false, nullable=false, insertable=false, updatable=false, length=7)
    public Date getDataCarimboTempo() {
        return this.dataCarimboTempo;
    }
    
    @Column(name="SEQ_DOCUMENTO", insertable=false, updatable=false)
    public Long getDocumentoEletronico() {
		return documentoEletronico;
	}

	public void setDocumentoEletronico(Long documentoEletronico) {
		this.documentoEletronico = documentoEletronico;
	}

	public void setDataCarimboTempo(Date dataTimestamp) {
        this.dataCarimboTempo = dataTimestamp;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_INCLUSAO", unique=false, nullable=false, insertable=false, updatable=false)
    public Usuario getUsuarioInclusao() {
        return this.usuarioInclusao;
    }
    
    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DAT_INCLUSAO", unique=false, nullable=false, insertable=false, updatable=false, length=7)
    public Date getDataInclusao() {
        return this.dataInclusao;
    }
    
    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

//    @OneToOne(cascade={}, fetch=FetchType.LAZY )
//	@JoinColumn(name = "SEQ_ASSINATURA_DIGITAL", unique=true, nullable=false, insertable=false, updatable=false)
//    public AssinaturaDigital getAssinaturaDigital() {
//		return assinaturaDigital;
//	}
//
//	public void setAssinaturaDigital(AssinaturaDigital assinaturaDigital) {
//		this.assinaturaDigital = assinaturaDigital;
//	}
    
//    @OneToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name = "SEQ_DOCUMENTO", unique=false, nullable=false, insertable=false, updatable=false)
//    public DocumentoEletronicoView getDocumentoEletronico() {
//		return documentoEletronico;
//	}
//
//	public void setDocumentoEletronico(DocumentoEletronicoView documentoEletronico) {
//		this.documentoEletronico = documentoEletronico;
//	}
        
}


