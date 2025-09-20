package br.gov.stf.estf.entidade.jurisdicionado;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.processostf.Categoria;


@Entity
@Table(schema = "JUDICIARIO", name = "TIPO_JURISDICIONADO")
public class TipoJurisdicionado extends ESTFBaseEntity<Long> {
	
	private Long id;
	private String descricaoTipoJurisdicionado;
	//private GrupoTipoJurisdicionado grupoTipoJurisdicionado;
	private String descricaoConectorApresentacao;
	private String tipoMateria;
	private String tipoPosicao;
	private String siglaUf;
	private Boolean postulante;
	private List<Categoria> categorias;
	private String siglaTipoJurisdicionado;

	@Id
	@Column(name="SEQ_TIPO_JURISDICIONADO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_TIPO_JURISDICIONADO", allocationSize=1)
	public Long getId() {
		return this.id;
	}

	public void setId(Long identifier) {
		this.id = identifier;
	}

	
	@Column(name = "DSC_TIPO_JURISDICIONADO")
	public String getDescricaoTipoJurisdicionado() {
		return descricaoTipoJurisdicionado;
	}

	public void setDescricaoTipoJurisdicionado(String descricaoTipoJurisdicionado) {
		this.descricaoTipoJurisdicionado = descricaoTipoJurisdicionado;
	}

	
	@Column(name = "DSC_CONECTOR_APRESENTACAO")
	public String getDescricaoConectorApresentacao() {
		return descricaoConectorApresentacao;
	}

	public void setDescricaoConectorApresentacao(
			String descricaoConectorApresentacao) {
		this.descricaoConectorApresentacao = descricaoConectorApresentacao;
	}

	
	@Column(name = "TIP_MATERIA")
	public String getTipoMateria() {
		return tipoMateria;
	}

	public void setTipoMateria(String tipoMateria) {
		this.tipoMateria = tipoMateria;
	}

	
	@Column(name = "TIP_POSICAO")
	public String getTipoPosicao() {
		return tipoPosicao;
	}

	public void setTipoPosicao(String tipoPosicao) {
		this.tipoPosicao = tipoPosicao;
	}

	
	@Column(name = "SIG_UF")
	public String getSiglaUf() {
		return siglaUf;
	}

	public void setSiglaUf(String siglaUf) {
		this.siglaUf = siglaUf;
	}

	
	@Column(name = "FLG_POSTULANTE")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPostulante() {
		return postulante;
	}

	public void setPostulante(Boolean postulante) {
		this.postulante = postulante;
	}
	
	@Column(name = "SIG_TIPO_JURISDICIONADO")
	public String getSiglaTipoJurisdicionado() {
		return siglaTipoJurisdicionado;
	}

	public void setSiglaTipoJurisdicionado(String siglaTipoJurisdicionado) {
		this.siglaTipoJurisdicionado = siglaTipoJurisdicionado;
	}

	@ManyToMany   
	@JoinTable(name="JUDICIARIO.CATEGORIA_TIPO_JURISDICIONADO",   
			joinColumns={@JoinColumn(name="SEQ_TIPO_JURISDICIONADO")}, 
			inverseJoinColumns={@JoinColumn(name="COD_CATEGORIA")})
	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

}
