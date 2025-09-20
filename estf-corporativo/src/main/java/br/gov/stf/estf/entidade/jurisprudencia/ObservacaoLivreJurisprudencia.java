/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(schema = "JURISPRUDENCIA", name = "OBSERVACAO_LIVRE")
public class ObservacaoLivreJurisprudencia extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1984479764747326493L;
	
	private Long id;
	private IncidenteAnalise incidenteAnalise;
	private String observacao;
	private TipoOrdenacaoObservacaoJurisprudencia tipoOrdenacao;
	
	/* (non-Javadoc)
	 * @see br.gov.stf.framework.model.entity.BaseEntity#getId()
	 */
	@Override
	@Id
	@Column(name = "SEQ_OBSERVACAO_LIVRE")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JURISPRUDENCIA.SEQ_OBSERVACAO_LIVRE", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_INCIDENTE_ANALISE")
	public IncidenteAnalise getIncidenteAnalise() {
		return incidenteAnalise;
	}
	
	public void setIncidenteAnalise(IncidenteAnalise incidenteAnalise) {
		this.incidenteAnalise = incidenteAnalise;
	}
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "TXT_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Column(name = "TIP_ORDENACAO")
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
