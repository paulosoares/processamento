package br.gov.stf.estf.entidade.usuario;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.usuario.TipoGrupoUsuarioControle.FlagGenericaGrupoUsuario;

@Entity
@Table( schema="STF", name="USUARIOS" )
public class Usuario extends ESTFBaseEntity<String> implements Responsavel {	
	/**
	 * 
	 */
	private static final long serialVersionUID = -286340047248411701L;
	private Setor setor;
	private String nome;	
	private String matricula;
	private Boolean ativo;
	private Set<Perfil> perfis;
	private List<TipoGrupoUsuarioControle> listaGrupoUsuarioControle;

	@Column( name="FLG_ATIVO" )
    @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" )	
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Id
	@Column( name="SIG_USUARIO" )
	public String getId() { 
		return id;
	}	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn( name="COD_SETOR")
	public Setor getSetor() {
		return setor;
	}	

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@Column( name="NOM_USUARIO" )
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column( name="NUM_MATRICULA" )
	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	@ManyToMany(targetEntity=Perfil.class, fetch = FetchType.LAZY)
	@JoinTable(
		schema="GLOBAL",	
		name="PERFIL_USUARIO",
		joinColumns=@JoinColumn(name="SIG_USUARIO"),
		inverseJoinColumns=@JoinColumn(name="SEQ_PERFIL")
	)
	public Set<Perfil> getPerfis() {
		return perfis;
	}
	public void setPerfis(Set<Perfil> perfis) {
		this.perfis = perfis;
	}
	
	
	@OneToMany(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SIG_USUARIO", referencedColumnName = "SIG_USUARIO", insertable = false, updatable = false)
	@Where(clause="FLG_ATIVO = 'S'")
	public List<TipoGrupoUsuarioControle> getListaGrupoUsuarioControle() {
		return listaGrupoUsuarioControle;
	}

	public void setListaGrupoUsuarioControle(
			List<TipoGrupoUsuarioControle> listaGrupoUsuarioControle) {
		this.listaGrupoUsuarioControle = listaGrupoUsuarioControle;
	}

	public void copiarDados(Usuario usuario)
	{
		if (usuario == null)
			throw new NullPointerException("Usuário para cópia é nulo");
		
		setId(usuario.getId());
		setNome(usuario.getNome());
		setSetor(usuario.getSetor());
	}
	
	@Transient
	public String getListaDeGruposConcatenados(){
		StringBuffer listaconcatenada = new StringBuffer();
		Boolean existeGrupoUsuarioAtivo = false;
		if (getListaGrupoUsuarioControle() != null && getListaGrupoUsuarioControle().size() > 0){
			for (TipoGrupoUsuarioControle tipoGUC : getListaGrupoUsuarioControle()){
				if (tipoGUC.getFlagAtivo().equals(FlagGenericaGrupoUsuario.S)){
					listaconcatenada.append(tipoGUC.getTipoGrupoControle().getDscTipoGrupoControle());
					listaconcatenada.append(" / ");
					existeGrupoUsuarioAtivo = true;
				}
			}
			if (existeGrupoUsuarioAtivo){
				listaconcatenada.delete(listaconcatenada.length()-2, listaconcatenada.length());
			}
		}
		return listaconcatenada.toString();
	}
	
	@Override
	public String toString() {
		return getNome();
	}
}