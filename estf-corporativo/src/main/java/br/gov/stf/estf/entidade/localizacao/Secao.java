package br.gov.stf.estf.entidade.localizacao;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="SECAO",schema="EGAB")
public class Secao extends ESTFBaseEntity<Long> {

    private String sigla;
    private String descricao;
    private List<SecaoSetor> secoesSetor;

	@Id
	@Column( name="SEQ_SECAO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_SECAO", allocationSize = 1 )
	public Long getId() {
		return id;
	}	
    
    @Column(name="SIG_SECAO", unique=false, nullable=false, insertable=true, updatable=true, length=15)
    public String getSigla() {
        return this.sigla;
    }
    
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
    
    @Column(name="DSC_SECAO", unique=false, nullable=false, insertable=true, updatable=true, length=30)
    public String getDescricao() {
        return this.descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    
    @OneToMany( cascade = { CascadeType.REMOVE }, mappedBy="secao")         	
	@JoinColumn(name="SEQ_SECAO", referencedColumnName="SEQ_SECAO") 		   
	//@org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
	public List<SecaoSetor> getSecoesSetor() {
		return secoesSetor;
	}

	public void setSecoesSetor(List<SecaoSetor> secoesSetor) {
		this.secoesSetor = secoesSetor;
	}
}


