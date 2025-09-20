package br.gov.stf.estf.entidade.localizacao;

import java.util.Set;

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

import br.gov.stf.estf.entidade.ESTFBaseEntity;


@Entity
@Table( schema="EGAB", name="CONFIGURACAO_SETOR" )
public class ConfiguracaoSetor extends ESTFBaseEntity<Long> {
	
	public static final String CONG_EGAB = "EGAB";
	public static final String CONG_EGAB_E = "EGAB-E";
	public static final String CONF_FLUXO_FASES = "FLOW";
	public static final String CONF_EXIBIR_DESLOCAMENTOS_MAG = "D-MAG";
	public static final String CONF_EXIBIR_ESTATISTICA = "EST";

	//private Long seqTipoConfiguracaoSetor;
	private Setor setor;
	private TipoConfiguracaoSetor tipoConfiguracaoSetor;
	private Set<ItemConfiguracaoSetor> listaItensConfiguracaoSetor;
	
	
	@Id
	@Column( name="SEQ_CONFIGURACAO_SETOR" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_CONFIGURACAO_SETOR", allocationSize = 1 )	
	public Long getId() {
		return id;
	}
	/*
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_CONFIGURACAO_SETOR", unique=false, nullable=false, insertable=true, updatable=true)
	public Long getSeqTipoConfiguracaoSetor() {
		return seqTipoConfiguracaoSetor;
	}

	public void setSeqTipoConfiguracaoSetor(Long seqTipoConfiguracaoSetor) {
		this.seqTipoConfiguracaoSetor = seqTipoConfiguracaoSetor;
	}
	*/
	@ManyToOne (cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR", unique = false, nullable = true, insertable = true, updatable = true)
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}
	
	@ManyToOne (cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_CONFIGURACAO_SETOR", unique = false, nullable = true, insertable = true, updatable = true)	
	public TipoConfiguracaoSetor getTipoConfiguracaoSetor() {
		return tipoConfiguracaoSetor;
	}
	public void setTipoConfiguracaoSetor(TipoConfiguracaoSetor tipoConfiguracaoSetor) {
		this.tipoConfiguracaoSetor = tipoConfiguracaoSetor;
	}
	
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="configuracaoSetor")
    @org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
	public Set<ItemConfiguracaoSetor> getListaItensConfiguracaoSetor() {
		return listaItensConfiguracaoSetor;
	}
	public void setListaItensConfiguracaoSetor(
			Set<ItemConfiguracaoSetor> listaItensConfiguracaoSetor) {
		this.listaItensConfiguracaoSetor = listaItensConfiguracaoSetor;
	}
	
	/*
	@OneToMany(fetch=FetchType.LAZY)
	public List<TipoConfiguracaoSetor> getTipoConfiguracoes() {
		return tipoConfiguracoes;
	}

	public void setTipoConfiguracoes(List<TipoConfiguracaoSetor> tipoConfiguracoes) {
		this.tipoConfiguracoes = tipoConfiguracoes;
	}
	*/	
	
}
