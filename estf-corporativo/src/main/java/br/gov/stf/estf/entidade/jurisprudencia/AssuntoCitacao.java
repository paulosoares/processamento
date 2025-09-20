/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

/**
 * @author Paulo.Estevao
 * @since 17.10.2012
 */
@Entity
@Table(schema = "JURISPRUDENCIA", name = "ASSUNTO_CITACAO")
public class AssuntoCitacao extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2959504574926446825L;
	
	private Long id;
	private TipoCitacao tipoCitacao;
	private String descricao;
	private IncidenteAnalise incidenteAnalise;
	private Long ordem;
	private List<CitacaoAcordao> acordaosCitados;
	private List<DecisaoCitada> decisoesCitadas;
	
	@Override
	@Id
	@Column(name = "SEQ_ASSUNTO_CITACAO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JURISPRUDENCIA.SEQ_ASSUNTO_CITACAO", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "TIP_CITACAO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.jurisprudencia.TipoCitacao"),
			@Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "identifierMethod", value = "getCodigo"),
			@Parameter(name = "valueOfMethod", value = "valueOfCodigo") })
	public TipoCitacao getTipoCitacao() {
		return tipoCitacao;
	}
	
	public void setTipoCitacao(TipoCitacao tipoCitacao) {
		this.tipoCitacao = tipoCitacao;
	}
	
	@Column(name = "DSC_ASSUNTO_CITACAO")
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_INCIDENTE_ANALISE")
	public IncidenteAnalise getIncidenteAnalise() {
		return incidenteAnalise;
	}
	
	public void setIncidenteAnalise(IncidenteAnalise incidenteAnalise) {
		this.incidenteAnalise = incidenteAnalise;
	}
	
	@Column(name = "NUM_ORDEM")
	public Long getOrdem() {
		return ordem;
	}
	
	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="assuntoCitacao", cascade=CascadeType.REMOVE)
	public List<CitacaoAcordao> getAcordaosCitados() {
		return acordaosCitados;
	}
	
	public void setAcordaosCitados(List<CitacaoAcordao> acordaosCitados) {
		this.acordaosCitados = acordaosCitados;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="assuntoCitacao", cascade=CascadeType.REMOVE)
	public List<DecisaoCitada> getDecisoesCitadas() {
		return decisoesCitadas;
	}
	
	public void setDecisoesCitadas(List<DecisaoCitada> decisoesCitadas) {
		this.decisoesCitadas = decisoesCitadas;
	}
	
}
