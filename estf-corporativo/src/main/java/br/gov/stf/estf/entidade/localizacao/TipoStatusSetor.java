package br.gov.stf.estf.entidade.localizacao;

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

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="TIPO_STATUS_SETOR" ,schema="EGAB")
public class TipoStatusSetor extends ESTFBaseEntity<Long> {
    
	private Setor setor;
    private String descricao;
    private Boolean ativo;
    
    public TipoStatusSetor() {
    }
    
    @Id
    @Column( name="SEQ_TIPO_STATUS_SETOR" )
    @GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
    @SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_TIPO_STATUS_SETOR", allocationSize = 1 )   
    public Long getId() {
     return id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Column(name="DSC_TIPO_STATUS_SETOR", unique=false, nullable=true, insertable=true, updatable=true, length=1000)
    public String getDescricao() {
        return descricao;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    @Column(name="FLG_ATIVO", unique=false, nullable=true, insertable=true, updatable=true, length=1000)
    @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" )
    public Boolean getAtivo() {
        return ativo;
    }
    
    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="COD_SETOR", unique=false, nullable=true, insertable=true, updatable=true)
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}    
}
