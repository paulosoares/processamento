package br.gov.stf.estf.entidade.processostf;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name = "CLASSE", schema = "JUDICIARIO")
public class Classe extends ESTFBaseEntity<String> {

	private static final long serialVersionUID = 1000086822691056950L;
	
	public static final String SIGLAS_CLASSES_PROCESSUAIS_ORIGINARIOS = "originarios";
	public static final String SIGLAS_CLASSES_PROCESSUAIS_AI_RE = "AIeRE";
	public static final String SIGLAS_CLASSES_PROCESSUAIS_AI_RE_ARE = "AIeREeARE";
	public static final String SIGLAS_CLASSES_PROCESSUAIS_ADI_ADC_ADO_ADPF = "ADIeADCeADOeADPF ";
	public static final String SIGLAS_CLASSES_PROCESSUAIS_RCL_MI_RMI = "RCLeMIeRMI";
	public static final String SIGLA_ACAO_DIRETA_DE_INCONSTITUCIONALIDADE = "ADI";
	public static final String SIGLA_ACAO_DECLARATORIA_DE_CONSTITUCIONALIDADE = "ADC";
	public static final String SIGLA_ACAO_DIRETA_DE_INCONSTITUCIONALIDADE_POR_EMISSAO = "ADO";
	public static final String SIGLA_ARGUICAO_DE_DESCUMPRIMENTO_DE_PRECEITO_FUNDAMENTAL = "ADPF";
	public static final String SIGLA_RECURSO_EXTRAORDINARIO = "RE";
	public static final String SIGLA_AGRAVO_DE_INSTRUMENTO = "AI";
	public static final String SIGLA_ACAO_RESCISORIA = "AR";
	public static final String SIGLA_ACAO_PENAL = "AP";
	public static final String SIGLA_ACAO_ORIGINARIA = "AO";
	public static final String SIGLA_RECURSO_EXTRAORDINARIO_COM_AGRAVO = "ARE";
	public static final String SIGLA_REVISAO_CRIMINAL = "RvC";
	public static final String SIGLA_RECURSO_ORDINARIO_CRIMINAL = "RCR";
	public static final String SIGLA_DECLARACAO_DE_SUSPENSAO_DE_DIREITOS = "SD";
	public static final String SIGLA_HABEAS_CORPUS = "HC";
	public static final String SIGLA_INQUERITO = "Inq";
	public static final String SIGLA_MANDADO_DE_SEGURANÇA = "MS";
	public static final String SIGLA_RECLAMACAO = "Rcl";
	public static final String SIGLA_RECURSO_CRIME = "RC";
	public static final String SIGLA_RECURSO_HABEAS_CORPUS = "RHC";
	
	private Short codigo;
	private String descricao;
	private Integer proximoProcesso;
	private Boolean originario;
	private Boolean admitePautaJulgamento;
	private Boolean admiteRevisor;
	private Boolean ativo;
	
	@Id
	@Column(name = "SIG_CLASSE", nullable = false, length = 6)
	public String getId() {
		return id;
	}

	@Column(name = "COD_CLASSE", precision = 3, scale = 0)
	public Short getCodigo() {
		return this.codigo;
	}

	public void setCodigo(Short codClasse) {
		this.codigo = codClasse;
	}

	@Column(name = "DSC_CLASSE", length = 80)
	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String dscClasse) {
		this.descricao = dscClasse;
	}

	@Column(name = "NUM_PROX_PROCESSO", precision = 7, scale = 0)
	public Integer getProximoProcesso() {
		return this.proximoProcesso;
	}

	public void setProximoProcesso(Integer numProxProcesso) {
		this.proximoProcesso = numProxProcesso;
	}

	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	@Column(name = "FLG_ORIGINARIO", length = 1)
	public Boolean getOriginario() {
		return this.originario;
	}

	public void setOriginario(Boolean flgOriginario) {
		this.originario = flgOriginario;
	}

	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	@Column(name = "FLG_PAUTA_JULG", length = 1)
	public Boolean getAdmitePautaJulgamento() {
		return this.admitePautaJulgamento;
	}

	public void setAdmitePautaJulgamento(Boolean flgPautaJulg) {
		this.admitePautaJulgamento = flgPautaJulg;
	}
	
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	@Column(name = "FLG_ADMITE_REVISOR", length = 1)
	public Boolean getAdmiteRevisor() {
		return this.admiteRevisor;
	}

	public void setAdmiteRevisor(Boolean admiteRevisor) {
		this.admiteRevisor = admiteRevisor;
	}

	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	@Column(name = "FLG_ATIVO", length = 1, updatable = false, insertable = false)
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

}
