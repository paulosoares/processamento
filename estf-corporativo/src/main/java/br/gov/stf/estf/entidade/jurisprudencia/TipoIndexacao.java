/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

/**
 * @author Paulo.Estevao
 * @since 10.07.2012
 */
@Entity
@Table(schema = "JURISPRUDENCIA", name = "TIPO_INDEXACAO")
public class TipoIndexacao extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5589397725072466653L;
	
	public static final String SIGLA_TERMOS_RESGATE = "R";

	public static final String SIGLA_TERMOS_TESAUROS = "T";

	private Long id;
	private String descricao;
	private String sigla;
	private Boolean exigeMinistro;
	private Boolean exigeTermo;
	private Boolean validaTermo;
	private Boolean multiParagrafo;
	
	@Override
	@Id
	@Column(name = "SEQ_TIPO_INDEXACAO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JURISPRUDENCIA.SEQ_TIPO_INDEXACAO", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "DSC_TIPO_INDEXACAO")
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column(name = "SIG_TIPO_INDEXACAO")
	public String getSigla() {
		return sigla;
	}
	
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	@Column(name = "FLG_EXIGE_MINISTRO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getExigeMinistro() {
		return exigeMinistro;
	}
	
	public void setExigeMinistro(Boolean exigeMinistro) {
		this.exigeMinistro = exigeMinistro;
	}
	
	@Column(name = "FLG_EXIGE_TERMO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getExigeTermo() {
		return exigeTermo;
	}
	
	public void setExigeTermo(Boolean exigeTermo) {
		this.exigeTermo = exigeTermo;
	}
	
	@Column(name = "FLG_VALIDA_TERMO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getValidaTermo() {
		return validaTermo;
	}
	
	public void setValidaTermo(Boolean validaTermo) {
		this.validaTermo = validaTermo;
	}
	
	@Column(name = "FLG_MULTI_PARAGRAFO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getMultiParagrafo() {
		return multiParagrafo;
	}
	
	public void setMultiParagrafo(Boolean multiParagrafo) {
		this.multiParagrafo = multiParagrafo;
	}

}
