package br.gov.stf.estf.entidade.usuario;
// Generated 27/06/2008 18:07:58 by Hibernate Tools 3.1.0.beta5


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="PERFIL" ,schema="GLOBAL")
public class Perfil extends ESTFBaseEntity<Integer> {

     /**
	 * 
	 */
	private static final long serialVersionUID = 8138498780396888225L;
	private String sistema;
    private String descricao;
    private Boolean ativo;
    private String descricaoRole;
    private Boolean publico;
    private List<Usuario> usuarios;

    @Id    
    @Column(name="SEQ_PERFIL", unique=false, nullable=false, insertable=true, updatable=true, precision=6, scale=0)
    public Integer getId() {
        return this.id;
    }
    
    @Column(name="SIG_SISTEMA", unique=false, nullable=false, insertable=true, updatable=true)
    public String getSistema() {
        return this.sistema;
    }
    
    public void setSistema(String sistema) {
        this.sistema = sistema;
    }
    
    @Column(name="DSC_PERFIL", unique=false, nullable=false, insertable=true, updatable=true, length=500)
    public String getDescricao() {
        return this.descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    @Column(name="FLG_ATIVO", unique=false, nullable=false, insertable=true, updatable=true, length=1)
    @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" )
    public Boolean getAtivo() {
        return this.ativo;
    }
    
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    @Column(name="DSC_ROLE", unique=false, nullable=true, insertable=true, updatable=true, length=30)
    public String getDescricaoRole() {
        return this.descricaoRole;
    }
    
    public void setDescricaoRole(String descricaoRole) {
        this.descricaoRole = descricaoRole;
    }
    
    @Column(name="FLG_PUBLICO", unique=false, nullable=false, insertable=true, updatable=true, length=1)
    @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" )
    public Boolean getPublico() {
        return this.publico;
    }
    
    public void setPublico(Boolean publico) {
        this.publico = publico;
    }

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "GLOBAL.PERFIL_USUARIO", 
			joinColumns = @JoinColumn(name = "SEQ_PERFIL"), 
			inverseJoinColumns = @JoinColumn(name = "SIG_USUARIO"))			
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
    
}


