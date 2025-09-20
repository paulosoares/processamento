package br.gov.stf.estf.entidade.processosetor;

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

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema="EGAB", name="PRIORIDADE")
public class Prioridade extends ESTFBaseEntity<Long> {

    private String tipo;
    private String nomeInteressado;
    private Date dataPedido;
    private String numeroTel1;
    private String numeroTel2;
    private String observacaoPedido;
    private ProcessoSetor processoSetor;
    
	@Id
	@Column( name="SEQ_PRIORIDADE" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_PRIORIDADE", allocationSize = 1 )	
	public Long getId() {
		return id;
	}	    
    
    @Column(name="TIP_PRIORIDADE", unique=false, nullable=false, insertable=true, updatable=true, length=2)
    public String getTipo() {
        return this.tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    @Column(name="NOM_INTERESSADO", unique=false, nullable=false, insertable=true, updatable=true, length=240)
    public String getNomeInteressado() {
        return this.nomeInteressado;
    }
    
    public void setNomeInteressado(String nomeInteressado) {
        this.nomeInteressado = nomeInteressado;
    }
    
    @Temporal(TemporalType.DATE)
    @Column(name="DAT_PEDIDO", unique=false, nullable=false, insertable=true, updatable=true, length=7)
    public Date getDataPedido() {
        return this.dataPedido;
    }
    
    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }
    
    @Column(name="NUM_TEL1", unique=false, nullable=true, insertable=true, updatable=true, length=240)
    public String getNumeroTel1() {
        return this.numeroTel1;
    }
    
    public void setNumeroTel1(String numeroTel1) {
        this.numeroTel1 = numeroTel1;
    }
    
    @Column(name="NUM_TEL2", unique=false, nullable=true, insertable=true, updatable=true, length=240)
    public String getNumeroTel2() {
        return this.numeroTel2;
    }
    
    public void setNumeroTel2(String numeroTel2) {
        this.numeroTel2 = numeroTel2;
    }
    
    @Column(name="OBS_PEDIDO", unique=false, nullable=true, insertable=true, updatable=true, length=240)
    public String getObservacaoPedido() {
        return this.observacaoPedido;
    }
    
    public void setObservacaoPedido(String observacaoPedido) {
        this.observacaoPedido = observacaoPedido;
    }
    
    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="SEQ_PROCESSO_SETOR", unique=false, nullable=true, insertable=true, updatable=true)    
	public ProcessoSetor getProcessoSetor() {
		return processoSetor;
	}

	public void setProcessoSetor(ProcessoSetor processoSetor) {
		this.processoSetor = processoSetor;
	}    
}