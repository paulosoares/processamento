package br.gov.stf.estf.entidade.documento;
// Generated 11/06/2008 15:15:52 by Hibernate Tools 3.1.0.beta5


import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Entity;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.usuario.Usuario;

@javax.persistence.Entity
@Entity(persister="br.gov.stf.estf.entidade.documento.AssinaturaDigitalPersister")
@Table(name="ASSINATURA_DIGITAL_TEMP" ,schema="DOC")
public class AssinaturaDigital extends ESTFBaseEntity<Long> {


    /**
	 * 
	 */
	private static final long serialVersionUID = 2138965872237956201L;
	
    private DocumentoEletronico documentoEletronico;
    private byte[] assinatura;
    private byte[] carimboTempo;
    private Date dataCarimboTempo;
    private Usuario usuarioInclusao;
    private Date dataInclusao;
    private FlagTipoAssinatura tipoAssinatura;
    
    @Id
	@Column( name="SEQ_ASSINATURA_DIGITAL" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="DOC.SEQ_ASSINATURA_DIGITAL", allocationSize=1)	
    public Long getId() {
        return this.id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_DOCUMENTO")
    public DocumentoEletronico getDocumentoEletronico() {
        return this.documentoEletronico;
    }
    
    public void setDocumentoEletronico(DocumentoEletronico documento) {
        this.documentoEletronico = documento;
    }
    
    @Lob
	@Basic(fetch=FetchType.LAZY)
    @Column(name="BIN_ASSINATURA", unique=false, nullable=true, insertable=true, updatable=true)
    public byte[] getAssinatura() {
        return this.assinatura;
    }
    
    public void setAssinatura(byte[] binAssinatura) {
        this.assinatura = binAssinatura;
    }
    
    @Lob
	@Basic(fetch=FetchType.LAZY)
    @Column(name="BIN_TIMESTAMP", unique=false, nullable=true, insertable=true, updatable=true)
    public byte[] getCarimboTempo() {
        return this.carimboTempo;
    }
    
    public void setCarimboTempo(byte[] binTimestamp) {
        this.carimboTempo = binTimestamp;
    }
    
    @Temporal(TemporalType.DATE)
    @Column(name="DAT_TIMESTAMP", unique=false, nullable=true, insertable=true, updatable=true, length=7)
    public Date getDataCarimboTempo() {
        return this.dataCarimboTempo;
    }
    
    public void setDataCarimboTempo(Date dataTimestamp) {
        this.dataCarimboTempo = dataTimestamp;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_INCLUSAO", updatable = false)
    public Usuario getUsuarioInclusao() {
        return this.usuarioInclusao;
    }
    
    public void setUsuarioInclusao(Usuario usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    @Column(name="DAT_INCLUSAO", unique=false, nullable=true, insertable=true, updatable=false, length=7)
    public Date getDataInclusao() {
        return this.dataInclusao;
    }
    
    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    @Column(name = "TIP_ASSINATURA")
    @Type(type="br.gov.stf.framework.util.GenericEnumUserType", parameters={
			@Parameter( name = "enumClass", 
					    value = "br.gov.stf.estf.entidade.documento.FlagTipoAssinatura"),
			@Parameter( name = "identifierMethod",
						value = "getCodigo"),
			@Parameter( name = "idClass", 
						value = "java.lang.String"),
			@Parameter( name = "valueOfMethod",
					    value = "valueOfCodigo")})
	public FlagTipoAssinatura getTipoAssinatura() {
		return tipoAssinatura;
	}

	public void setTipoAssinatura(FlagTipoAssinatura tipoAssinatura) {
		this.tipoAssinatura = tipoAssinatura;
	}
    
}


