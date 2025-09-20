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

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

/**
 * @author Paulo.Estevao
 * @since 08.10.2012
 */
@Entity
@Table(schema = "JURISPRUDENCIA", name = "TEXTO_PADRAO_OBSERVACAO")
public class TextoPadraoObservacaoJurisprudencia extends ESTFAuditavelBaseEntity<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3836550744917219054L;
	
	private Long id;
	private String texto;
	private TipoOrdenacaoObservacaoJurisprudencia tipoOrdenacao;

	/* (non-Javadoc)
	 * @see br.gov.stf.framework.model.entity.BaseEntity#getId()
	 */
	@Override
	@Id
	@Column(name = "SEQ_TEXTO_PADR_OBSERVACAO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JURISPRUDENCIA.SEQ_TEXTO_PADR_OBSERVACAO", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "TXT_PADRAO_OBSERVACAO")
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}

	@Column(name = "TIP_ORDENACAO_OBSERVACAO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.jurisprudencia.TipoOrdenacaoObservacaoJurisprudencia"),
			@Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "identifierMethod", value = "getSigla"),
			@Parameter(name = "valueOfMethod", value = "valueOfSigla") })
	public TipoOrdenacaoObservacaoJurisprudencia getTipoOrdenacao() {
		return tipoOrdenacao;
	}
	
	public void setTipoOrdenacao(TipoOrdenacaoObservacaoJurisprudencia tipoOrdenacao) {
		this.tipoOrdenacao = tipoOrdenacao;
	}
}
