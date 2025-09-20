/**
 * 
 */
package br.gov.stf.estf.entidade.julgamento;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.SubtemaPauta;

/**
 * @author Paulo.Estevao
 * @since 29.06.2011
 */
@Entity
@Table(name="INFORMACAO_PAUTA_PROCESSO", schema="JULGAMENTO")
public class InformacaoPautaProcesso extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4562313039725989859L;
	
	private Long id;
	private ObjetoIncidente<?> objetoIncidente;
	private SubtemaPauta subtemaPauta;
	private Date dataLiberacaoGabinete;
	private Boolean aguardaLiberacao;
	private String textoAguardaLiberacao;
	private Date dataJulgamentoSugerida;
	private TipoSituacaoPauta situacaoPauta;
	private String observacaoDataSugerida;
	private String observacao;
	private Boolean materiaRelevante;
	private Boolean repercussaoGeral;
	private String temaEspelho;
	private String teseEspelho;
	private String parecerPGREspelho;
	private String parecerAGUEspelho;
	private String votoRelatorEspelho;
	private String votosEspelho;
	private String informacoesEspelho;
	private String anotacoesEspelho;
	private String preferenciasEspelho;
	private Long seqListaJulgamentoConjunto;
	private Boolean pautaExtra;
	
	private List<PrevisaoSustentacaoOral> sustentacoesOrais;

	private String faltaVotarEspelho;

	private String quemVotaEspelho;

	private String naoVotamEspelho;
	
	@Override
	@Id
	@Column( name="SEQ_INFORMACAO_PAUTA_PROCESSO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_INFORMACAO_PAUTA_PROCESSO", allocationSize=1 )
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", updatable = true, insertable = true)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_SUBTEMA_PAUTA")
	public SubtemaPauta getSubtemaPauta() {
		return subtemaPauta;
	}

	public void setSubtemaPauta(SubtemaPauta subtemaPauta) {
		this.subtemaPauta = subtemaPauta;
	}

	@Column(name = "DAT_LIBERACAO_GABINETE")
	public Date getDataLiberacaoGabinete() {
		return dataLiberacaoGabinete;
	}

	public void setDataLiberacaoGabinete(Date dataLiberacaoGabinete) {
		this.dataLiberacaoGabinete = dataLiberacaoGabinete;
	}

	@Column(name = "FLG_AGUARDA_LIBERACAO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAguardaLiberacao() {
		return aguardaLiberacao;
	}

	public void setAguardaLiberacao(Boolean aguardaLiberacao) {
		this.aguardaLiberacao = aguardaLiberacao;
	}

	@Column(name = "TXT_AGUARDA_LIBERACAO")
	public String getTextoAguardaLiberacao() {
		return textoAguardaLiberacao;
	}

	public void setTextoAguardaLiberacao(String textoAguardaLiberacao) {
		this.textoAguardaLiberacao = textoAguardaLiberacao;
	}

	@Column(name = "DAT_JULGAMENTO_SUGERIDA")
	public Date getDataJulgamentoSugerida() {
		return dataJulgamentoSugerida;
	}

	public void setDataJulgamentoSugerida(Date dataJulgamentoSugerida) {
		this.dataJulgamentoSugerida = dataJulgamentoSugerida;
	}

	@Column(name = "TIP_SITUACAO_PAUTA", nullable = false)
	@Enumerated(EnumType.STRING)
	public TipoSituacaoPauta getSituacaoPauta() {
		return situacaoPauta;
	}

	public void setSituacaoPauta(TipoSituacaoPauta situacaoPauta) {
		this.situacaoPauta = situacaoPauta;
	}

	@Column(name = "TXT_OBSERVACAO_DATA")
	public String getObservacaoDataSugerida() {
		return observacaoDataSugerida;
	}

	public void setObservacaoDataSugerida(String observacaoDataSugerida) {
		this.observacaoDataSugerida = observacaoDataSugerida;
	}

	@Column(name = "TXT_OBSERVACAO_GERAL")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Column(name = "FLG_MATERIA_RELEVANTE")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getMateriaRelevante() {
		return materiaRelevante;
	}

	public void setMateriaRelevante(Boolean materiaRelevante) {
		this.materiaRelevante = materiaRelevante;
	}

	@Column(name = "FLG_REPERCUSSAO_GERAL")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getRepercussaoGeral() {
		return repercussaoGeral;
	}

	public void setRepercussaoGeral(Boolean repercussaoGeral) {
		this.repercussaoGeral = repercussaoGeral;
	}

	@Lob
	@Column(name = "DSC_TEMA_ESPELHO")
	public String getTemaEspelho() {
		return temaEspelho;
	}

	public void setTemaEspelho(String temaEspelho) {
		this.temaEspelho = temaEspelho;
	}
	
	@Lob
	@Column(name = "TXT_TESE_ESPELHO")
	public String getTeseEspelho() {
		return teseEspelho;
	}

	public void setTeseEspelho(String teseEspelho) {
		this.teseEspelho = teseEspelho;
	}

	@Column(name = "TXT_PARECER_PGR_ESPELHO")
	public String getParecerPGREspelho() {
		return parecerPGREspelho;
	}

	public void setParecerPGREspelho(String parecerPGREspelho) {
		this.parecerPGREspelho = parecerPGREspelho;
	}

	@Column(name = "TXT_PARECER_AGU_ESPELHO")
	public String getParecerAGUEspelho() {
		return parecerAGUEspelho;
	}

	public void setParecerAGUEspelho(String parecerAGUEspelho) {
		this.parecerAGUEspelho = parecerAGUEspelho;
	}
	
	@Column(name = "TXT_VOTO_RELATOR_ESPELHO")
	public String getVotoRelatorEspelho() {
		return votoRelatorEspelho;
	}

	public void setVotoRelatorEspelho(String votoRelatorEspelho) {
		this.votoRelatorEspelho = votoRelatorEspelho;
	}

	@Column(name = "TXT_VOTOS_ESPELHO")
	public String getVotosEspelho() {
		return votosEspelho;
	}

	public void setVotosEspelho(String votosEspelho) {
		this.votosEspelho = votosEspelho;
	}

	@Column(name = "TXT_INFORMACAO_ESPELHO")
	public String getInformacoesEspelho() {
		return informacoesEspelho;
	}

	public void setInformacoesEspelho(String informacoesEspelho) {
		this.informacoesEspelho = informacoesEspelho;
	}
	
	@Column(name = "TXT_ANOTACAO_ESPELHO")
	public String getAnotacoesEspelho(){
		return anotacoesEspelho;
	}
	
	public void setAnotacoesEspelho(String anotacoesEspelho){
		this.anotacoesEspelho = anotacoesEspelho;
	}
	
	@Column(name = "TXT_PREFERENCIA_ESPELHO")
	public String getPreferenciasEspelho(){
		return preferenciasEspelho;
	}
	
	public void setPreferenciasEspelho(String preferenciasEspelho){
		this.preferenciasEspelho = preferenciasEspelho;
	}
	
	@Column(name = "TXT_FALTA_VOTAR_ESPELHO")
	public String getFaltaVotarEspelho() {
		return faltaVotarEspelho;
	}
	
	public void setFaltaVotarEspelho(String faltaVotarEspelho) {
		this.faltaVotarEspelho = faltaVotarEspelho;
	}

	@Column(name = "TXT_QUEM_VOTA_ESPELHO")
	public String getQuemVotaEspelho() {
		return quemVotaEspelho;
	}
	
	public void setQuemVotaEspelho(String quemVotaEspelho) {
		this.quemVotaEspelho = quemVotaEspelho;
	}

	@Column(name = "TXT_NAO_VOTAM_ESPELHO")
	public String getNaoVotamEspelho() {
		return naoVotamEspelho;
	}
	
	public void setNaoVotamEspelho(String naoVotamEspelho) {
		this.naoVotamEspelho = naoVotamEspelho;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_INFORMACAO_PAUTA_PROCESSO", insertable = false, updatable = false)
	public List<PrevisaoSustentacaoOral> getSustentacoesOrais() {
		return sustentacoesOrais;
	}

	public void setSustentacoesOrais(List<PrevisaoSustentacaoOral> sustentacoesOrais) {
		this.sustentacoesOrais = sustentacoesOrais;
	}

	@Column(name = "SEQ_LISTA_JULGAMENTO_CONJUNTO")
	public Long getSeqListaJulgamentoConjunto() {
		return seqListaJulgamentoConjunto;
	}

	public void setSeqListaJulgamentoConjunto(Long seqListaJulgamentoConjunto) {
		this.seqListaJulgamentoConjunto = seqListaJulgamentoConjunto;
	}
	
	@Column(name = "FLG_PAUTA_EXTRA")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPautaExtra() {
		return pautaExtra;
	}
	
	public void setPautaExtra(Boolean pautaExtra) {
		this.pautaExtra = pautaExtra;
	}
	
	public enum TipoSituacaoPauta {
		P("Pendente"), E("Elaborado"), C("Confirmado");
		
		private String descricao;
		
		private TipoSituacaoPauta(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
	}
}