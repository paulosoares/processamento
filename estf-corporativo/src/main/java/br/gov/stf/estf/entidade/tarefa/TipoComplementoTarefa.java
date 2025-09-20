package br.gov.stf.estf.entidade.tarefa;

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
import br.gov.stf.estf.entidade.localizacao.Setor;

@Entity
@Table(schema="EGAB",name="TIPO_COMPLEMENTO_TAREFA")
public class TipoComplementoTarefa extends ESTFBaseEntity<Long> {
	
	private Setor setor;
    private String descricao;
    private Boolean ativo = false;

	@Id
	@Column( name="SEQ_TIPO_COMPLEMENTO_TAREFA" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_TIPO_COMPLEMENTO_TAREFA", allocationSize = 1 )    
    public Long getId() {
        return id;
    }
    
    
    @Column(name="DSC_TIPO_COMPLEMENTO_TAREFA", unique=false, nullable=false, insertable=true, updatable=true, length=1)
    public String getDescricao() {
		return descricao;
	}
	
    public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
    
    @Column(name="FLG_ATIVO", unique=false, nullable=true, insertable=true, updatable=true, length=1)
    @Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
    public Boolean getAtivo() {
		return ativo;
	}

    public void setAtivo(Boolean flgAtivo) {
		this.ativo = flgAtivo;
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
