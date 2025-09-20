/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.ministro.Ministro;

/**
 * @author Paulo.Estevao
 * @since 10.07.2012
 */
@Entity
@Table(schema = "JURISPRUDENCIA", name = "INDEXACAO_INCIDENTE_ANALISE")
public class IndexacaoIncidenteAnalise extends ESTFAuditavelBaseEntity<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8844339794311265196L;
	
	private Long id;
	private IncidenteAnalise incidenteAnalise;
	private TipoIndexacao tipoIndexacao;
	private Long ordemParagrafo;
	private String conteudo;
	private List<Ministro> ministros;
	
	@Id
	@Column(name = "SEQ_INDEXACAO_INCI_ANALISE")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JURISPRUDENCIA.SEQ_INDEXACAO_INCI_ANALISE", allocationSize = 1)
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_INDEXACAO")
	public TipoIndexacao getTipoIndexacao() {
		return tipoIndexacao;
	}
	
	public void setTipoIndexacao(TipoIndexacao tipoIndexacao) {
		this.tipoIndexacao = tipoIndexacao;
	}
	
	@Column(name = "NUM_ORDEM_PARAGRAFO")
	public Long getOrdemParagrafo() {
		return ordemParagrafo;
	}
	
	public void setOrdemParagrafo(Long ordemParagrafo) {
		this.ordemParagrafo = ordemParagrafo;
	}
	
	@Lob
	@Column(name = "DSC_CONTEUDO_INDEXADO")
	public String getConteudo() {
		return conteudo;
	}
	
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		schema="JURISPRUDENCIA",	
		name="MINISTRO_INDEXACAO",
		joinColumns=@JoinColumn(name="SEQ_INDEXACAO_INCI_ANALISE"),
		inverseJoinColumns=@JoinColumn(name="COD_MINISTRO")
	)
	public List<Ministro> getMinistros() {
		return ministros;
	}
	
	public void setMinistros(List<Ministro> ministros) {
		this.ministros = ministros;
	}

}
