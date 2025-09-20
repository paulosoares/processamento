/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

/**
 * @author Paulo.Estevao
 * @since 10.07.2012
 */
@Entity
@Table(schema = "JURISPRUDENCIA", name = "INCIDENTE_ANALISE")
public class IncidenteAnalise extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8054522581665103024L;
	
	private Long id;
	private ObjetoIncidente<?> objetoIncidente;
	private IncidenteAnalise incidenteAnalisePai;
	private TipoClassificacao tipoClassificacao;
	private AcordaoJurisprudencia acordaoJurisprudencia;
	private List<IndexacaoIncidenteAnalise> paragrafos;
	private List<LegislacaoIncidenteAnalise> legislacoes;
	private String doutrina;
	private String tipoMateria;
	private String descricaoTipoMateria;
	private String publicacao;
	private String partes;
	
	private Long idItemPublicacao;
	
	private Boolean bloqueadoSucessivo;
	private String motivoBloqueioSucessivo;
	
	@Override
	@Id
	@Column(name = "SEQ_INCIDENTE_ANALISE")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JURISPRUDENCIA.SEQ_INCIDENTE_ANALISE", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}
	
	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_INCIDENTE_ANALISE_PAI")
	public IncidenteAnalise getIncidenteAnalisePai() {
		return incidenteAnalisePai;
	}
	
	public void setIncidenteAnalisePai(IncidenteAnalise incidenteAnalisePai) {
		this.incidenteAnalisePai = incidenteAnalisePai;
	}
	
	@Column(name = "TIP_CLASSIFICACAO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.jurisprudencia.TipoClassificacao"),
			@Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "identifierMethod", value = "getSigla"),
			@Parameter(name = "valueOfMethod", value = "valueOfSigla") })
	public TipoClassificacao getTipoClassificacao() {
		return tipoClassificacao;
	}
	
	public void setTipoClassificacao(TipoClassificacao tipoClassificacao) {
		this.tipoClassificacao = tipoClassificacao;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_SJUR")
	public AcordaoJurisprudencia getAcordaoJurisprudencia() {
		return acordaoJurisprudencia;
	}
	
	public void setAcordaoJurisprudencia(AcordaoJurisprudencia acordaoJurisprudencia) {
		this.acordaoJurisprudencia = acordaoJurisprudencia;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "incidenteAnalise")
	@OrderBy(value = "ordemParagrafo")
	public List<IndexacaoIncidenteAnalise> getParagrafos() {
		return paragrafos;
	}
	
	public void setParagrafos(List<IndexacaoIncidenteAnalise> paragrafos) {
		this.paragrafos = paragrafos;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "incidenteAnalise")
	@OrderBy(value = "ordemEfetivo")
	public List<LegislacaoIncidenteAnalise> getLegislacoes() {
		return legislacoes;
	}
	
	public void setLegislacoes(List<LegislacaoIncidenteAnalise> legislacoes) {
		this.legislacoes = legislacoes;
	}
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "TXT_REFERENCIA_DOUTRINA")
	public String getDoutrina() {
		return doutrina;
	}
	
	public void setDoutrina(String doutrina) {
		this.doutrina = doutrina;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "TIP_MATERIA")
	public String getTipoMateria() {
		return tipoMateria;
	}
	
	public void setTipoMateria(String tipoMateria) {
		this.tipoMateria = tipoMateria;
	}
	
	@Transient
	public String getDescricaoTipoMateria() {
		if(tipoMateria != null){
			if (tipoMateria.equalsIgnoreCase("TR")) {
				descricaoTipoMateria = "Trabalhista";
			} else if (tipoMateria.equalsIgnoreCase("CR")) {
				descricaoTipoMateria = "Criminal";
			} else if (tipoMateria.equalsIgnoreCase("CI")) {
				descricaoTipoMateria = "Cível";
			} else {
				descricaoTipoMateria = "Nenhuma";
			}
		} else {
			descricaoTipoMateria = "Nenhuma";
		}
		
		return descricaoTipoMateria;
	}

	@Column(name = "TXT_INFORMACAO_PUBLICACAO", length = 300)
	public String getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(String publicacao) {
		this.publicacao = publicacao;
	}

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "TXT_LISTA_PARTE")
	public String getPartes() {
		return partes;
	}
	
	public void setPartes(String partes) {
		this.partes = partes;
	}

	/**
	 * @return the idBancoTese
	 */
	//@Transient
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "SEQ_ITEM_PUBLICACAO")
	public Long getIdItemPublicacao() {
		return idItemPublicacao;
	}

	/**
	 * @param idBancoTese the idBancoTese to set
	 */
	public void setIdItemPublicacao(Long idBancoTese) {
		this.idItemPublicacao = idBancoTese;
	}

	@Column(name = "FLG_BLOQUEIO_SUCESSIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getBloqueadoSucessivo() {
		return bloqueadoSucessivo;
	}

	public void setBloqueadoSucessivo(Boolean bloqueadoSucessivo) {
		this.bloqueadoSucessivo = bloqueadoSucessivo;
	}

	@Column(name = "DSC_MOTIVO_BLOQUEIO")	
	public String getMotivoBloqueioSucessivo() {
		return motivoBloqueioSucessivo;
	}

	public void setMotivoBloqueioSucessivo(String motivoBloqueioSucessivo) {
		this.motivoBloqueioSucessivo = motivoBloqueioSucessivo;
	}
}
