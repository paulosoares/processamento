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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import br.gov.stf.estf.entidade.ESTFBaseEntity;


@Entity
@Table( schema="STF", name="LEMBRETE" )
public class Lembrete extends ESTFBaseEntity<Long>{

    private String descricao;
    private Integer paginaInicio;
    private Integer paginaFim;
    private Integer ordem;
    private Boolean ativo;    
    private String usuario;
    //private PecaProcessoEletronico pecaProcessual;
    private PecaProcessoEletronico pecaProcessoEletronico;
    

    @Id      
    @Column( name="SEQ_LEMBRETE")     
    @GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
    @SequenceGenerator( name="sequence", sequenceName="STF.SEQ_LEMBRETE", allocationSize=1 )    
    public Long getId() {
            return id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    @Column( name="DSC_LEMBRETE")   
    public String getDescricao() {
        return descricao;
    }

    public void setPaginaInicio(Integer paginaInicio) {
        this.paginaInicio = paginaInicio;
    }
    
    @Column( name="NUM_PAG_INICIO")   
    public Integer getPaginaInicio() {
        return paginaInicio;
    }

    public void setPaginaFim(Integer paginaFim) {
        this.paginaFim = paginaFim;
    }
    
    @Column( name="NUM_PAG_FIM")   
    public Integer getPaginaFim() {
        return paginaFim;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
    
    @Column( name="NUM_ORDEM")   
    public Integer getOrdem() {
        return ordem;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    @Column( name="FLG_ATIVO")   
    @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType") 
    public Boolean getAtivo() {
        return ativo;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    @Column ( name="SIG_USUARIO" )
    public String getUsuario() {
        return usuario;
    }

    public boolean equals(Object obj) {
            if (obj instanceof Lembrete == false) {
                    return false;
            }            
            if (this == obj) {
                    return true;
            }            
            Lembrete lembrete = (Lembrete) obj;
            
            return new EqualsBuilder()                    
                    .append(getId() , lembrete.getId() )
                    .isEquals();
    }    
    public int hashCode() {
         return new HashCodeBuilder(17, 37).
           append(getId()).
           toHashCode();
    }
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn( name="SEQ_PECA_PROC_ELETRONICO" )
	public PecaProcessoEletronico getPecaProcessoEletronico() {
		return pecaProcessoEletronico;
	}

	public void setPecaProcessoEletronico(
			PecaProcessoEletronico pecaProcessoEletronico) {
		this.pecaProcessoEletronico = pecaProcessoEletronico;
	}



}
