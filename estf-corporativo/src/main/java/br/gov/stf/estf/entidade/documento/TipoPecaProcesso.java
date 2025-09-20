package br.gov.stf.estf.entidade.documento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;

@Entity
@Table(name = "TIPO_PECA_PROCESSO", schema = "STF")
public class TipoPecaProcesso extends ESTFAuditavelBaseEntity<Long> {

	private static final long serialVersionUID = 676715333977123665L;

	public static final String SIGLA_TIPO_PECA_EXTRATO_ATA = "EXTATA";
	public static final String SIGLA_TIPO_PECA_CERTIDAO_JULGAMENTO = "CERTJULG";
	public static final String SIGLA_TIPO_PECA_TERMO_JUNTADA_CERTIDAO = "TERMJUNT";
	public static final String SIGLA_TIPO_PECA_TERMO_JUNTADA_ACORDAO = "TERMJUNTACORDAO";
	public static final String SIGLA_TIPO_PECA_CERTIDAO_DATA = "CERTDATA";
	public static final String SIGLA_TIPO_PECA_CERTIDAO_PUBLICACAO = "CERTPUB";
	public static final String SIGLA_TIPO_PECA_DESPACHO = "DESPACHO";
	public static final String SIGLA_TIPO_PECA_TERMO_BAIXA = "TERMOBAIXA";
	public static final String SIGLA_TIPO_PECA_TERMO_REMESSA = "TERMOREMESSA";
	public static final String SIGLA_TIPO_PECA_VISTA_PGR = "VISTAPGR";
	public static final String SIGLA_TIPO_PECA_VISTA_AGU = "VISTAAGU";
	public static final String SIGLA_TIPO_PECA_TERMO_AUTOS_DISP = "TERMODISPAUT";	
	public static final Long CODIGO_INTEIRO_TEOR = 1221L;
	public static final Long CODIGO_INTEIRO_TEOR_COMPLETO = 1223L;
	public static final Long CODIGO_DESPACHO = 1060L;
	public static final Long CODIGO_DECISAO_MONOCRATICA = 1065L;
	
	public static final TipoPecaProcesso TERMO_REJEICAO_SUSTENTACAO_ORAL = new TipoPecaProcesso(1533L, "Termo de rejeição de sustentação oral", "TERREJSO");

	private TipoPecaProcesso tipoPecaProcessoPai;
	private String descricao;
	private Boolean ativo;
	private String sigla;

	public TipoPecaProcesso() {
	}
	
	public TipoPecaProcesso(Long id, String descricao, String sigla) {
		this.id = id;
		this.descricao = descricao;
		this.sigla = sigla;
	}

	@Id
	@Column(name = "SEQ_TIPO_PECA", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_PECA_PAI", unique = false, nullable = true, insertable = true, updatable = true)
	public TipoPecaProcesso getTipoPecaProcessoPai() {
		return this.tipoPecaProcessoPai;
	}

	public void setTipoPecaProcessoPai(TipoPecaProcesso tipoPecaProcesso) {
		this.tipoPecaProcessoPai = tipoPecaProcesso;
	}

	@Column(name = "DSC_TIPO_PECA", unique = false, nullable = false, insertable = true, updatable = true, length = 100)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricaoTipoPeca) {
		this.descricao = descricaoTipoPeca;
	}

	@Column(name = "FLG_ATIVO", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return this.ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Column(name = "SIG_TIPO_PECA", unique = false, nullable = true, insertable = true, updatable = true, length = 30)
	public String getSigla() {
		return this.sigla;
	}

	public void setSigla(String siglaTipoPeca) {
		this.sigla = siglaTipoPeca;
	}

}
