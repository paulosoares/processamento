/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

import java.util.ArrayList;
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
import javax.persistence.Transient;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

/**
 * @author Paulo.Estevao
 * @since 03.08.2012
 */
@Entity
@Table(schema = "JURISPRUDENCIA", name = "LEGISLACAO_INCIDENTE_ANALISE")
public class LegislacaoIncidenteAnalise extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3216624842680530878L;
	
	private Long id;
	private IncidenteAnalise incidenteAnalise;
	private String numero;
	private Long ano;
	private String descricao;
	private String observacao;
	private Long ordemEfetivo;
	private TipoLegislacao tipoLegislacao;
	private TipoEscopoLegislacao tipoEscopoLegislacao;
	private List<ItemLegislacao> itensLegislacao;

	/* (non-Javadoc)
	 * @see br.gov.stf.framework.model.entity.BaseEntity#getId()
	 */
	@Override
	@Id
	@Column(name = "SEQ_LEGI_INCIDENTE_ANALISE")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JURISPRUDENCIA.SEQ_LEGI_INCIDENTE_ANALISE", allocationSize = 1)
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
	
	@Column(name = "NUM_LEGISLACAO", length = 6)
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	@Column(name = "NUM_ANO_LEGISLACAO")
	public Long getAno() {
		return ano;
	}
	
	public void setAno(Long ano) {
		this.ano = ano;
	}
	
	@Column(name = "NUM_ORDEM_EFETIVO")
	public Long getOrdemEfetivo() {
		return ordemEfetivo;
	}
	
	public void setOrdemEfetivo(Long ordemEfetivo) {
		this.ordemEfetivo = ordemEfetivo;
	}
	
	@Column(name = "DSC_LEGISLACAO")
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column(name = "DSC_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_LEGISLACAO")
	public TipoLegislacao getTipoLegislacao() {
		return tipoLegislacao;
	}
	
	public void setTipoLegislacao(TipoLegislacao tipoLegislacao) {
		this.tipoLegislacao = tipoLegislacao;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_ESCOPO_LEGISLACAO")
	public TipoEscopoLegislacao getTipoEscopoLegislacao() {
		return tipoEscopoLegislacao;
	}
	
	public void setTipoEscopoLegislacao(TipoEscopoLegislacao tipoEscopoLegislacao) {
		this.tipoEscopoLegislacao = tipoEscopoLegislacao;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "legislacaoIncidenteAnalise", cascade = {CascadeType.REMOVE})
	public List<ItemLegislacao> getItensLegislacao() {
		return itensLegislacao;
	}
	
	public void setItensLegislacao(List<ItemLegislacao> itensLegislacao) {
		this.itensLegislacao = itensLegislacao;
	}
	
	@Transient
	public String getIdentificacao() {
		StringBuffer sb = new StringBuffer();
		if (getTipoLegislacao() != null && getTipoLegislacao().getSigla() != null) {
			sb.append(getTipoLegislacao().getSigla());
		}
		if (getTipoLegislacao() != null && getTipoLegislacao().getSigla() != null && getNumero() != null) {
			sb.append("-");
		}
		if (getNumero() != null) {
			sb.append(getNumero());
		}
		
		return sb.toString();
			
	}
	
	@Transient
	public List<ItemLegislacao> getItensLegislacaoRaiz() {
		List<ItemLegislacao> listaLegislacaoRaiz = new ArrayList();
		for (ItemLegislacao itemLegislacao : getItensLegislacao()) {
			if (itemLegislacao.getItemLegislacaoPai() == null) listaLegislacaoRaiz.add(itemLegislacao);
		}
		return listaLegislacaoRaiz;
	}
}
