package br.gov.stf.estf.entidade.ministro;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.processostf.TipoExclusaoDistribuicao;

@Entity
@Table(schema = "JUDICIARIO", name = "PERIODO_EXCLUSAO_DISTRIBUICAO")
public class ExclusaoDistribuicao extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date dataInicioPeriodo;
	private Date dataFimPeriodo;
	private Ministro ministro;
	private Boolean compensado;
	private TipoExclusaoDistribuicao tipoExclusaoDistribuicao;
	private String justificativa;
	private String observacao;
	private Long quantidadeProcessoPrevencao;
	private Boolean distribuicaoPrevencao;
	private Boolean ajusteDistribuicao;
	
	@Id
	@Column( name="SEQ_PERIODO_EXCL_DISTRIBUICAO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JUDICIARIO.SEQ_PERIODO_EXCL_DISTRIBUICAO", allocationSize = 1 ) 	 
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_INICIO_PERIODO")
	public Date getDataInicioPeriodo() {
		return dataInicioPeriodo;
	}

	public void setDataInicioPeriodo(Date dataInicioPeriodo) {
		this.dataInicioPeriodo = dataInicioPeriodo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_FIM_PERIODO")
	public Date getDataFimPeriodo() {
		return dataFimPeriodo;
	}

	public void setDataFimPeriodo(Date dataFimPeriodo) {
		this.dataFimPeriodo = dataFimPeriodo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_MINISTRO", nullable = false, insertable = true, updatable = false)
	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}
	
	@Column(name = "FLG_COMPENSADO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getCompensado() {
		return compensado;
	}

	public void setCompensado(Boolean compensado) {
		this.compensado = compensado;
	}

	@Column(name = "SEQ_TIPO_EXCL_DISTRIBUICAO", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
		@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoExclusaoDistribuicao")})
	public TipoExclusaoDistribuicao getTipoExclusaoDistribuicao() {
		return tipoExclusaoDistribuicao;
	}

	public void setTipoExclusaoDistribuicao(TipoExclusaoDistribuicao tipoExclusaoDistribuicao) {
		this.tipoExclusaoDistribuicao = tipoExclusaoDistribuicao;
	}

	@Column(name = "DSC_JUSTIFICATIVA")
	public String getJustificativa() {
		return justificativa;
	}
	
	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	@Column(name = "DSC_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Column(name = "QTD_PROCESSO_PREVENCAO")
	public Long getQuantidadeProcessoPrevencao() {
		return quantidadeProcessoPrevencao;
	}
	
	public void setQuantidadeProcessoPrevencao(Long quantidadeProcessoPrevencao) {
		this.quantidadeProcessoPrevencao = quantidadeProcessoPrevencao;
	}

	@Column(name = "FLG_DISTRIBUICAO_PREVENCAO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getDistribuicaoPrevencao() {
		return distribuicaoPrevencao;
	}
	
	public void setDistribuicaoPrevencao(Boolean distribuicaoPrevencao) {
		this.distribuicaoPrevencao = distribuicaoPrevencao;
	}

	@Column(name = "FLG_AJUSTE_DISTRIBUICAO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAjusteDistribuicao() {
		return ajusteDistribuicao;
	}
	
	public void setAjusteDistribuicao(Boolean ajusteDistribuicao) {
		this.ajusteDistribuicao = ajusteDistribuicao;
	}
	
	
	/**
	 * Retorna 'Sim' ou 'Não' do campo 'compensado' que pode possuir os valores 'true' ou 'false'
	 * @return
	 */
	@Transient
	public String getAjusteDistribuicaoFormatado(){
		if (ajusteDistribuicao == null) {
			return "Não";
		} else {
			return (ajusteDistribuicao?"Sim":"Não");
		}
	}
	
	/**
	 * Retorna 'Sim' ou 'Não' do campo 'compensado' que pode possuir os valores 'true' ou 'false'
	 * @return
	 */
	@Transient
	public String getCompensadoFormatado(){
		if (compensado == null) {
			return "Não";
		} else {
			return (compensado?"Sim":"Não");
		}
	}
	
	/**
	 * Retorna 'Sim' ou 'Não' do campo 'distribuicaoPrevencao' que pode possuir os valores 'true' ou 'false'
	 * @return
	 */
	@Transient
	public String getDistribuicaoPrevencaoFormatado(){
		if (distribuicaoPrevencao == null) {
			return "Não";
		} else {
			return (distribuicaoPrevencao?"Sim":"Não");
		}
	}
	
	/**
	 * 
	 * @return String de datas formatadas
	 */
	@Transient
	public String getDataInicioPeriodoFormatado(){
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return sd.format(getDataInicioPeriodo());
	}
	
	@Transient
	public String getDataFimPeriodoFormatado(){
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return sd.format(getDataFimPeriodo());
	}
	
}
