package br.gov.stf.estf.entidade.usuario;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;

@Entity
@Table(schema="EGAB",name="GRUPO_USUARIO")
public class GrupoUsuario extends ESTFBaseEntity<Long> implements Responsavel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3249699550702799618L;
	private String descricao;
    private Boolean ativo = Boolean.TRUE;
    private String observacao;
    private Setor setor;
    private Set<UsuarioEGab> usuarios = new HashSet<UsuarioEGab>();
    
	@Id
	@Column( name="SEQ_GRUPO_USUARIO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_GRUPO_USUARIO", allocationSize = 1 )    
    public Long getId() {
        return id;
    }
    
    @Column(name="DSC_GRUPO", unique=false, nullable=false, insertable=true, updatable=true, length=100)
    public String getDescricao() {
        return this.descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
	@Column( name="FLG_ATIVO" )
    @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" )	
    public Boolean getAtivo() {
        return this.ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    @Column(name="DSC_OBSERVACAO", unique=false, nullable=true, insertable=true, updatable=true, length=500)
    public String getObservacao() {
        return this.observacao;
    }
    
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

	@ManyToOne( fetch=FetchType.LAZY )
	@JoinColumn( name="COD_SETOR" )
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@ManyToMany( cascade={}, fetch=FetchType.LAZY  )
	@JoinTable( schema="EGAB", name="USUARIO_GRUPO",
		joinColumns={ @JoinColumn(name="SEQ_GRUPO_USUARIO") },
		inverseJoinColumns={ @JoinColumn(name="SIG_USUARIO", referencedColumnName="SIG_USUARIO"),
			@JoinColumn(name="COD_SETOR", referencedColumnName="COD_SETOR") }
	)	
	public Set<UsuarioEGab> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Set<UsuarioEGab> usuarios) {
		this.usuarios = usuarios;
	}

	@Transient
	@Override
	public String getNome() {
		return getDescricao();
	}
	
	@Override
	public String toString() {
		return getNome();
	}
}


