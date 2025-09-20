package br.gov.stf.estf.entidade.processostf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TipoComunicacaoESTF;
import br.gov.stf.estf.entidade.localizacao.Origem;

@Entity
@Table(name = "PROCESSO_INTEGRACAO", schema = "ESTF")
public class ProcessoIntegracao extends ESTFBaseEntity<Long> {

	public enum TipoSituacaoProcessoIntegracao {

		NAO_ENVIADO("Não Lido", "N"), ENVIADO("Lido", "E"), RECEBIDO(
				"Recebido", "R"), COM_PROBLEMA("Com problema", "P"), CONFIRMA_INTIMACAO(
				"Confirma intimação", "I"), EM_TRANSMISSAO("Em transmissão",
				"T");

		private String descricao;
		private String codigo;

		private TipoSituacaoProcessoIntegracao(String descricao, String codigo) {
			this.descricao = descricao;
			this.codigo = codigo;
		}

		public String getDescricao() {
			return descricao;
		}

		public String getCodigo() {
			return codigo;
		}
	}

	private static final long serialVersionUID = 2792251004210547496L;

	public static final String TIPO_SITUACAO_NAO_ENVIADO = "N";

	private Long id;
	
	private ObjetoIncidente<?> processo;
	
	private AndamentoProcesso andamentoProcesso;

	private Long idAndamentoProcesso;

	private Long codigoOrgao;

	private String tipoSituacao;

	private PecaProcessoEletronico pecaProcessoEletronico;

	private String siglaClasseProcesso;

	private Long numeroProcesso;

	private Origem origem;

	private TipoComunicacaoESTF tipoComunicacao;

	private Date dataCriacao;

	private boolean enviado;

	@Id
	@Column(name = "SEQ_PROCESSO_INTEGRACAO")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}

	@ManyToOne(cascade = {})
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", unique = false, nullable = false, insertable = true, updatable = true)
	public ObjetoIncidente<?> getProcesso() {
		return processo;
	}

	public void setProcesso(ObjetoIncidente<?> processo) {
		this.processo = processo;
	}

	@ManyToOne(cascade = {})
	@JoinColumn(name = "SEQ_ANDAMENTO_PROCESSO", unique = true)
	public AndamentoProcesso getAndamentoProcesso() {
		return andamentoProcesso;
	}

	public void setAndamentoProcesso(AndamentoProcesso andamentoProcesso) {
		this.andamentoProcesso = andamentoProcesso;
	}

	@Column(name = "COD_ORGAO")
	public Long getCodigoOrgao() {
		return codigoOrgao;
	}

	public void setCodigoOrgao(Long codigoOrgao) {
		this.codigoOrgao = codigoOrgao;
	}

	@Column(name = "TIP_SITUACAO")
	public String getTipoSituacao() {
		return tipoSituacao;
	}

	public void setTipoSituacao(String tipoSituacao) {
		this.tipoSituacao = tipoSituacao;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_PECA_PROC_ELETRONICO", unique = false, nullable = true, insertable = true, updatable = true)
	public PecaProcessoEletronico getPecaProcessoEletronico() {
		return pecaProcessoEletronico;
	}

	public void setPecaProcessoEletronico(
			PecaProcessoEletronico pecaProcessoEletronico) {
		this.pecaProcessoEletronico = pecaProcessoEletronico;
	}

	@Column(name = "SIG_CLASSE_PROCES")
	public String getSiglaClasseProcesso() {
		return siglaClasseProcesso;
	}

	public void setSiglaClasseProcesso(String siglaClasseProcesso) {
		this.siglaClasseProcesso = siglaClasseProcesso;
	}

	@Column(name = "NUM_PROCESSO")
	public Long getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	@Column(name = "SEQ_ANDAMENTO_PROCESSO", insertable = false, updatable = false)
	public Long getIdAndamentoProcesso() {
		return idAndamentoProcesso;
	}

	public void setIdAndamentoProcesso(Long idAndamentoProcesso) {
		this.idAndamentoProcesso = idAndamentoProcesso;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_ORGAO", referencedColumnName = "COD_ORIGEM", insertable = false, updatable = false)
	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	@Column(name = "DAT_INCLUSAO", insertable = false, updatable = false)
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_TIPO_COMUNICACAO")
	public TipoComunicacaoESTF getTipoComunicacao() {
		return tipoComunicacao;
	}

	public void setTipoComunicacao(TipoComunicacaoESTF tipoComunicacao) {
		this.tipoComunicacao = tipoComunicacao;
	}

	@Formula("( SELECT COUNT(*) FROM ESTF.processo_integracao_usuario piu WHERE piu.SEQ_PROCESSO_INTEGRACAO = SEQ_PROCESSO_INTEGRACAO) ")
	public boolean isEnviado() {
		return enviado;
	}

	public void setEnviado(boolean enviado) {
		this.enviado = enviado;
	}
	
	@Transient
	public String getIdentificacao(){
		return siglaClasseProcesso + " " + numeroProcesso;
	}

}
