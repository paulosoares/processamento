package br.gov.stf.estf.entidade.documento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.localizacao.Setor;

@Entity
@Table(name = "ROTULO", schema = "JUDICIARIO")
@PrimaryKeyJoinColumn(name = "SEQ_ADENDO_TEXTUAL")
public class Rotulo extends Adendo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6707212428666975328L;
	
	private String nome;
	private String descricao;
	private String cor;
	private Setor setor;
	private Boolean ativo;
	private AbrangenciaRotulo abrangencia;

	@Column(name = "NOM_ROTULO")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "DSC_ROTULO")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "DSC_COR_ROTULO")
	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR")
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	@Column(name = "TIP_ABRANGENCIA")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.documento.AbrangenciaRotulo"),
			@Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "valueOfMethod", value = "valueOfCodigo") })
	public AbrangenciaRotulo getAbrangencia() {
		return abrangencia;
	}
	
	public void setAbrangencia(AbrangenciaRotulo abrangencia) {
		this.abrangencia = abrangencia;
	}
}