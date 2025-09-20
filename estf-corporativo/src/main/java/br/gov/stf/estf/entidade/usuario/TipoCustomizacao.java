/**
 * 
 */
package br.gov.stf.estf.entidade.usuario;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 * @author Paulo.Estevao
 * @since 04.09.2013
 */
@Entity
@Table(name = "TIPO_CUSTOMIZACAO", schema = "GLOBAL")
@SequenceGenerator(name = "SEQUENCE", sequenceName = "GLOBAL.SEQ_TIPO_CUSTOMIZACAO", allocationSize=1)
public class TipoCustomizacao extends ESTFBaseEntity<Long> {
	
	private static final long serialVersionUID = -3572355601906985328L;

	public static final String PREFERENCIAS_USUARIO = "PREFERENCIAS_USUARIO";
	public static final String ORDENACAO_NUMERICA = "ORDENACAO_NUMERICA";
	public static final String PROIBIR_AGENDAMENTO_VIRTUAL = "PROIBIR_AGENDAMENTO_VIRTUAL";
	public static final String PROIBIR_NOVO_TEXTO = "PROIBIR_NOVO_TEXTO";
	
	private Long id;
	private String descricao;
	private TipoDefinicao definicao;
	private String parametro;
	private String siglaSistema;
	private Boolean ativo;

	@Override
	@Id
	@Column(name = "SEQ_TIPO_CUSTOMIZACAO")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE")
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "DSC_TIPO_CUSTOMIZACAO")
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column(name = "TIP_DEFINICAO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.usuario.TipoDefinicao"),
			@Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "valueOfMethod", value = "valueOfCodigo"),
			@Parameter(name = "identifierMethod", value = "getCodigo")})
	public TipoDefinicao getDefinicao() {
		return definicao;
	}
	
	public void setDefinicao(TipoDefinicao definicao) {
		this.definicao = definicao;
	}
	
	@Column(name = "DSC_PARAMETRO")
	public String getParametro() {
		return parametro;
	}
	
	public void setParametro(String parametro) {
		this.parametro = parametro;
	}
	
	@Column(name = "SIG_SISTEMA")
	public String getSiglaSistema() {
		return siglaSistema;
	}
	
	public void setSiglaSistema(String siglaSistema) {
		this.siglaSistema = siglaSistema;
	}
	
	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}
	
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}
