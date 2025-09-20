package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.processostf.enuns.TipoPolo;

@Entity
@Table(schema = "JUDICIARIO", name = "CATEGORIA")
public class Categoria extends ESTFBaseEntity<Long> {
	
	public static final Long COD_CATEGORIA_ADVOGADO = 202L;
	public static final String TIPO_CATEGORIA_ADVOGADO = "A";

	private static final long serialVersionUID = 58211675904334421L;
	private String sigla;
	private String descricao;
	private Boolean ativo;
	private TipoPolo tipo;
	
	@Id
	@Column(name = "COD_CATEGORIA")
	public Long getId() {
		return id;
	}
	
	@Column(name = "FLG_ATIVO", unique = false, nullable = false, insertable = false, updatable = false)
    @Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")	
	public Boolean getAtivo() {
		return ativo;
	}
	
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	@Column(name = "DSC_CATEGORIA", unique = false, nullable = false, insertable = false, updatable = false, length = 50)
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column(name = "TIP_POLO", unique = false, nullable = false, insertable = false, updatable = false, length = 1)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
		@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.enuns.TipoPolo"),
		@Parameter(name = "idClass", value = "java.lang.String")
	})
	public TipoPolo getTipo() {
		return tipo;
	}
	
	public void setTipo(TipoPolo tipo) {
		this.tipo = tipo;
	}
	
	@Column(name = "SIG_CATEGORIA", unique = false, nullable = false, insertable = false, updatable = false, length = 20)
	public String getSigla() {
		return sigla;
	}
	
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
}

