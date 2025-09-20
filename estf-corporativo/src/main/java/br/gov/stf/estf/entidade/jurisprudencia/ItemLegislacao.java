/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

/**
 * @author Paulo.Estevao
 * @since 07.08.2012
 */
@Entity
@Table(schema = "JURISPRUDENCIA", name = "ITEM_LEGISLACAO")
public class ItemLegislacao extends ESTFAuditavelBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7225598684724792273L;
	
	private Long id;
	private LegislacaoIncidenteAnalise legislacaoIncidenteAnalise;
	private ItemLegislacao itemLegislacaoPai;
	private Boolean caput;
	private String valor;
	private String observacao;
	private TipoItemLegislacaoConstante tipoItemLegislacao;
	private Long ordem;
	
	
	/* (non-Javadoc)
	 * @see br.gov.stf.framework.model.entity.BaseEntity#getId()
	 */
	@Override
	@Id
	@Column(name = "SEQ_ITEM_LEGISLACAO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JURISPRUDENCIA.SEQ_ITEM_LEGISLACAO", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_LEGI_INCIDENTE_ANALISE")
	public LegislacaoIncidenteAnalise getLegislacaoIncidenteAnalise() {
		return legislacaoIncidenteAnalise;
	}
	
	public void setLegislacaoIncidenteAnalise(LegislacaoIncidenteAnalise legislacaoIncidenteAnalise) {
		this.legislacaoIncidenteAnalise = legislacaoIncidenteAnalise;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ITEM_LEGISLACAO_PAI")
	public ItemLegislacao getItemLegislacaoPai() {
		return itemLegislacaoPai;
	}
	
	public void setItemLegislacaoPai(ItemLegislacao itemLegislacaoPai) {
		this.itemLegislacaoPai = itemLegislacaoPai;
	}
	
	@Column(name = "FLG_CAPUT")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getCaput() {
		return caput;
	}
	
	public void setCaput(Boolean caput) {
		this.caput = caput;
	}
	
	@Column(name = "VLR_ITEM")
	public String getValor() {
		return valor;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	@Column(name = "DSC_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	@Column(name = "SEQ_TIPO_ITEM_LEGISLACAO")
	@Type(type="br.gov.stf.framework.util.GenericEnumUserType", parameters={
			@Parameter( name = "enumClass", 
					    value = "br.gov.stf.estf.entidade.jurisprudencia.TipoItemLegislacaoConstante"),
			@Parameter( name = "identifierMethod",
						value = "getId"),
			@Parameter( name = "idClass", 
						value = "java.lang.Long"),
			@Parameter( name = "valueOfMethod",
					    value = "valueOf")})
	public TipoItemLegislacaoConstante getTipoItemLegislacao() {
		return tipoItemLegislacao;
	}
	
	public void setTipoItemLegislacao(TipoItemLegislacaoConstante tipoItemLegislacao) {
		this.tipoItemLegislacao = tipoItemLegislacao;
	}
	
	@Column(name = "NUM_ORDEM")
	public Long getOrdem() {
		return ordem;
	}
	
	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}
	
	@Transient
	public Integer getNivel() {
		Integer nivel = 1;
		ItemLegislacao filho = this;
		ItemLegislacao pai = getItemLegislacaoPai();
		while (pai != null) {
			nivel++;
			filho = pai;
			pai = filho.getItemLegislacaoPai();
		}
		return nivel;
	}
	
	@Transient
	public String getIdentificacao() {
		String identificacao = obterDescricaoEValor(this);
		ItemLegislacao filho = this;
		ItemLegislacao pai = getItemLegislacaoPai();
		while (pai != null) {
			identificacao = obterDescricaoEValor(pai) + " " + identificacao;
			filho = pai;
			pai = filho.getItemLegislacaoPai();
		}
		return identificacao;
	}

	private String obterDescricaoEValor(ItemLegislacao itemLegislacao) {
		String identificacao = itemLegislacao.getTipoItemLegislacao().getDescricao();
		if (itemLegislacao.getValor() != null) {
			identificacao = identificacao + " " + itemLegislacao.getValor();
		}
		return identificacao;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this.getId() != null) {
			return super.equals(other);
		} else {
			if (this == other)
				return true;

			if (other == null || !(other instanceof ItemLegislacao))
				return false;

			if (Hibernate.getClass(this).equals(Hibernate.getClass(other))) {
				ItemLegislacao otherEntity = (ItemLegislacao) other;
				if (otherEntity.getId() != null) {
					return false;
				}
				
				return (this.getOrdem() != null && otherEntity.getOrdem() != null && this.getOrdem().equals(otherEntity.getOrdem()))
						&& itemLegislacaoPaiEquals(otherEntity);
			}
			return false;
		}
	}

	private boolean itemLegislacaoPaiEquals(ItemLegislacao otherEntity) {
		return ((this.getItemLegislacaoPai() == null && otherEntity.getItemLegislacaoPai() == null) 
				|| (this.getItemLegislacaoPai() != null && otherEntity.getItemLegislacaoPai() != null 
						&& this.getItemLegislacaoPai().equals(otherEntity.getItemLegislacaoPai())));
	}
	


	public static boolean equals(ItemLegislacao itemLegislacao1, ItemLegislacao itemLegislacao2) {
		if ((itemLegislacao1 == null && itemLegislacao2 == null) 
				|| (itemLegislacao1 != null && itemLegislacao2 != null && itemLegislacao1.equals(itemLegislacao2))) {
			return true;
		}
		return false;
	}
	
	@Transient
	public ItemLegislacao getAncestral(Integer nivel) {
		if (getNivel().equals(nivel)) {
			return this;
		}
		if (nivel > getNivel()) {
			return null;
		}
		
		ItemLegislacao pai = getItemLegislacaoPai();
		if (pai.getNivel() != nivel) {
			return pai.getAncestral(nivel);
		} else {
			return pai;
		}		
		
	}
	
	@Transient
	public double getOrdemCompleta() {
		if (getNivel() == 1) {
			return getOrdem().doubleValue();
		}
		
		return getItemLegislacaoPai().getOrdemCompleta() + getOrdem().doubleValue() * Math.pow(10, (getNivel() - 1) * (-1));
	}
	
	@Transient
	public List<ItemLegislacao> getLegislacaoFilha() {
		List<ItemLegislacao> listaLegislacaoFilha = new ArrayList();
		for (ItemLegislacao itemLegislacao : legislacaoIncidenteAnalise.getItensLegislacao()) {
			if (itemLegislacao.getItemLegislacaoPai() != null && itemLegislacao.getItemLegislacaoPai().getId().equals(getId())) listaLegislacaoFilha.add(itemLegislacao);
		}
		return listaLegislacaoFilha;
	}
	
	@Transient
	public String getIdentificacaoNivelAtual() {
		String identificacao = obterDescricaoEValor(this);		
		return identificacao;
	}
}
