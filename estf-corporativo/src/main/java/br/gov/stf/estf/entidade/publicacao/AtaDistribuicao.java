package br.gov.stf.estf.entidade.publicacao;

import java.sql.Clob;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.IncidenteDistribuicao;


@Entity
@Table(name = "ATA_DISTRIBUICAO", schema = "JUDICIARIO")
public class AtaDistribuicao extends ESTFBaseEntity<Long>{

	private static final long serialVersionUID = 3421236633944607329L;
	
	private Integer numeroAta;
	private TipoSessao tipoSessao;
	private Date dataComposicaoParcial;
	private Date dataAta;
	private TipoModoDistribuicao tipoModoDistribuicao;
	private Ministro ministroPresidente;
	private Clob documento;
	private List<IncidenteDistribuicao> distribuicoes;

	@Id
	@Column(name="SEQ_ATA_DISTRIBUICAO")
	public Long getId() {
		return this.id;
	}
	
	@Column(name="NUM_ATA")
	public Integer getNumeroAta() {
		return numeroAta;
	}

	public void setNumeroAta(Integer numeroAta) {
		this.numeroAta = numeroAta;
	}
	
	@Column(name = "TIP_SESSAO", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.publicacao.TipoSessao"),
			@Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "identifierMethod", value = "getSigla"),
			@Parameter(name = "valueOfMethod", value = "valueOfSigla"),
			@Parameter(name = "nullValue", value = "VAZIO")})
	public TipoSessao getTipoSessao() {
		return tipoSessao;
	}

	public void setTipoSessao(TipoSessao tipoSessao) {
		this.tipoSessao = tipoSessao;
	}

	@Column(name="DAT_COMPOSICAO_PARCIAL")
	public Date getDataComposicaoParcial() {
		return dataComposicaoParcial;
	}

	public void setDataComposicaoParcial(Date dataComposicaoParcial) {
		this.dataComposicaoParcial = dataComposicaoParcial;
	}
	
	@Column(name="DAT_ATA")
	public Date getDataAta() {
		return dataAta;
	}

	public void setDataAta(Date dataAta) {
		this.dataAta = dataAta;
	}
	
	@Column(name = "TIP_MODO_DISTRIBUICAO", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.publicacao.TipoModoDistribuicao"),
			@Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "identifierMethod", value = "getSigla"),
			@Parameter(name = "valueOfMethod", value = "valueOfSigla")})
	public TipoModoDistribuicao getTipoModoDistribuicao() {
		return tipoModoDistribuicao;
	}

	public void setTipoModoDistribuicao(TipoModoDistribuicao tipoModoDistribuicao) {
		this.tipoModoDistribuicao = tipoModoDistribuicao;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="COD_MIN_PRESIDENTE", insertable=false, updatable=false)
	public Ministro getMinistroPresidente() {
		return ministroPresidente;
	}

	public void setMinistroPresidente(Ministro ministroPresidente) {
		this.ministroPresidente = ministroPresidente;
	}
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "JUDICIARIO.DISTRIBUICAO", joinColumns = @JoinColumn(name = "SEQ_ATA_DISTRIBUICAO"), inverseJoinColumns = @JoinColumn(name = "SEQ_INCIDENTE_DISTRIBUICAO"))
	public List<IncidenteDistribuicao> getDistribuicoes() {
		return distribuicoes;
	}

	public void setDistribuicoes(List<IncidenteDistribuicao> distribuicoes) {
		this.distribuicoes = distribuicoes;
	}

	
}
