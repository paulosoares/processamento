package br.gov.stf.estf.entidade.jurisdicionado;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="IDENTIFICACAO_PESSOA", schema="JUDICIARIO" )
public class IdentificacaoPessoa extends ESTFBaseEntity<Long> {


	private static final long serialVersionUID = 7138030410666148663L;
	
	private Long id;
	private TipoIdentificacao tipoIdentificacao;
	private String descricaoIdentificacao;
	private Jurisdicionado jurisdicionado;
	private Date dataExpedicao;
	private String siglaUfOrgaoExpedidor;
	
	@Id
	@Column(name = "SEQ_IDENTIFICACAO_PESSOA")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JUDICIARIO.SEQ_IDENTIFICACAO_PESSOA", allocationSize = 1)
	public Long getId() {
		return this.id;
	}

	public void setId(Long identifier) {
		this.id = identifier;
	}

	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_IDENTIFICACAO", nullable = false)
	public TipoIdentificacao getTipoIdentificacao() {
		return tipoIdentificacao;
	}

	public void setTipoIdentificacao(TipoIdentificacao tipoIdentificacao) {
		this.tipoIdentificacao = tipoIdentificacao;
	}

	
	@Column(name = "DSC_IDENTIFICACAO")
	public String getDescricaoIdentificacao() {
		return descricaoIdentificacao;
	}

	public void setDescricaoIdentificacao(String descricaoIdentificacao) {
		this.descricaoIdentificacao = descricaoIdentificacao;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_JURISDICIONADO", nullable = false)
	public Jurisdicionado getJurisdicionado() {
		return jurisdicionado;
	}

	public void setJurisdicionado(Jurisdicionado jurisdicionado) {
		this.jurisdicionado = jurisdicionado;
	}

	
	@Column(name = "DAT_EXPEDICAO")
	public Date getDataExpedicao() {
		return dataExpedicao;
	}

	public void setDataExpedicao(Date dataExpedicao) {
		this.dataExpedicao = dataExpedicao;
	}

	
	@Column(name = "SIG_UF_ORGAO_EXPEDIDOR")
	public String getSiglaUfOrgaoExpedidor() {
		return siglaUfOrgaoExpedidor;
	}

	public void setSiglaUfOrgaoExpedidor(String siglaUfOrgaoExpedidor) {
		this.siglaUfOrgaoExpedidor = siglaUfOrgaoExpedidor;
	}
	
	
	
	
}
