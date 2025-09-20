package br.gov.stf.estf.entidade.processostf;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 * Representa os Tipos de Andamento. Informa as fases do Processo / Peti��o / Protocolo.
 * 
 * <p>A cria��o dos andamentos s� � liberada depois de passar pela Comiss�o de Estat�stica, 
 * que disciplina a crita��o e utiliza��o dos andamentos nas fases processuais.
 */
@Entity
@Table(name = "ANDAMENTOS", schema = "STF")
public class Andamento extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = -1165823763409263431L;
	private Long id;
	private Integer codigoEstatistica;
	private Long codigoReferencia;
	private String descricao;
	private Boolean andamentoEspecial;
	private Boolean andamento;
	private Boolean bloqueioAndamento;
	private Boolean desbloqueioAndamento;
	private Boolean entradaSemDistribuicao;
	private Boolean geraProximoAndamento;
	private Boolean julgaMeritoLiminar;
	private Boolean publicacao;
	private Boolean recurso;
	private Andamento proximoAndamento;
	private Long grupoAndamento;
	
	public static Andamento INCLUIDO_NO_CALENDARIO = new Andamento(8545L, "Inclu�do no calend�rio de julgamento");
	public static Andamento INCLUIDO_NO_CALENDARIO_PELO_PRESIDENTE = new Andamento(8534L, "Inclu�do no calend�rio de julgamento pelo Presidente");
	public static final Andamento SUSPENSAO_E_REINICIO_DE_JULGAMENTO = new Andamento(8559L, "Suspens�o e rein�cio de julgamento");


	public Andamento() {
		super();
	}
	
	public Andamento(Long id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}
	
	/**
	 * C�digo identificados do Andamento.
	 */
	@Id @Column(name = "COD_ANDAMENTO")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "COD_ESTATISTICA")
	public Integer getCodigoEstatistica() {
		return codigoEstatistica;
	}

	public void setCodigoEstatistica(Integer codigoEstatistica) {
		this.codigoEstatistica = codigoEstatistica;
	}

	/**
	 * C�digo de refer�ncia ao andamento de convers�o DE-PARA andamentos.
	 */
	@Column(name = "COD_REFERENCIA")
	public Long getCodigoReferencia() {
		return codigoReferencia;
	}

	public void setCodigoReferencia(Long codigoReferencia) {
		this.codigoReferencia = codigoReferencia;
	}

	/**
	 * Descreve o andamento de um processo, peti��o, protocolo ou recurso. 
	 */
	@Column(name = "DSC_ANDAMENTO")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column(name = "FLG_AND_ESPECIAL")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAndamentoEspecial() {
		return andamentoEspecial;
	}

	public void setAndamentoEspecial(Boolean andamentoEspecial) {
		this.andamentoEspecial = andamentoEspecial;
	}

	@Column(name = "FLG_ANDAMENTO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAndamento() {
		return andamento;
	}

	public void setAndamento(Boolean andamento) {
		this.andamento = andamento;
	}

	@Column(name = "FLG_BLOQ_ANDAMENTO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getBloqueioAndamento() {
		return bloqueioAndamento;
	}

	public void setBloqueioAndamento(Boolean bloqueioAndamento) {
		this.bloqueioAndamento = bloqueioAndamento;
	}

	@Column(name = "FLG_DESBLOQ_ANDAM")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getDesbloqueioAndamento() {
		return desbloqueioAndamento;
	}

	public void setDesbloqueioAndamento(Boolean desbloqueioAndamento) {
		this.desbloqueioAndamento = desbloqueioAndamento;
	}

	@Column(name = "FLG_ENTRA_SEM_DIST")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getEntradaSemDistribuicao() {
		return entradaSemDistribuicao;
	}

	public void setEntradaSemDistribuicao(Boolean entradaSemDistribuicao) {
		this.entradaSemDistribuicao = entradaSemDistribuicao;
	}

	@Column(name = "FLG_GERA_PROX_AND")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getGeraProximoAndamento() {
		return geraProximoAndamento;
	}

	public void setGeraProximoAndamento(Boolean geraProximoAndamento) {
		this.geraProximoAndamento = geraProximoAndamento;
	}

	@Column(name = "FLG_JULG_MER_LIM")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getJulgaMeritoLiminar() {
		return julgaMeritoLiminar;
	}

	public void setJulgaMeritoLiminar(Boolean julgaMeritoLiminar) {
		this.julgaMeritoLiminar = julgaMeritoLiminar;
	}

	@Column(name = "FLG_PUBLICACAO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPublicacao() {
		return publicacao;
	}

	public void setPublicacao(Boolean publicacao) {
		this.publicacao = publicacao;
	}

	@Column(name = "FLG_RECURSO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getRecurso() {
		return recurso;
	}

	public void setRecurso(Boolean recurso) {
		this.recurso = recurso;
	}

	@ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn(name = "COD_PROX_ANDAMENTO")
	public Andamento getProximoAndamento() {
		return proximoAndamento;
	}

	public void setProximoAndamento(Andamento proximoAndamento) {
		this.proximoAndamento = proximoAndamento;
	}


	public void setGrupoAndamento(Long grupoAndamento) {
		this.grupoAndamento = grupoAndamento;
	}

	@Column(name = "SEQ_GRUPO_ANDAMENTO")
	public Long getGrupoAndamento() {
		return grupoAndamento;
	}

	@Transient
	public boolean isAndamentoAutomaticoPlenarioVirtual() {
		return id.equals(Andamentos.MERITO_REPERCUSSAO_GERAL.getId()) || 
			   id.equals(Andamentos.SUBSTITUIDO_JULGAMENTO_REPERCUSSAO_GERAL.getId());
	}
	
	@Transient
	public String getIdentificacao(){		
		return id + " - " + descricao;
	}

	/*
	 * Esta enumera��o guarda os valores relativos
	 * ao tipo de andamento do Processo.
	 * Adicionado, segundo ISSUE 941.
	 * */
	public enum Andamentos {
		VISTA_AO_MINISTRO("Vista ao Ministro", 4020L), 
		VISTA_AA_MINISTRA("Vista � Ministra", 4021L), 
		VISTA_AOS_MINISTROS("Vista aos(�) Ministros(a)", 8304L),
		VISTA_AOS_MINISTROS_DEVOLUCAO("Vista - Devolu��o dos autos para julgamento", 8305L),
		JUNTADA_AO_PROCESSO("Juntada ao processo n�", 8245L),
		JUNTADA_A_PETICAO("Juntada a peti��o n�", 8246L),
		PROCESSO_FINDO("PROCESSO FINDO", 2309L),
		PROCESSO_FINDO_2("Processo Findo", 7106L),
		CONVERTIDO_EM_DILIGENCIA("Convertido em Dilig�ncia", 6001L),
		SUBSTITUIDO_JULGAMENTO_REPERCUSSAO_GERAL("Substitu�do para julgamento de tema de repercuss�o geral pelo processo n�", 8242L),
		MERITO_REPERCUSSAO_GERAL("M�rito da repercuss�o geral julgado no processo n�", 8243L),
		LANCAMENTO_INDEVIDO("Lan�amento indevido", 7700L),
		DISTRIBUICAO_POR_EXCLUSAO_DE_MINISTRO("Distribu�do por exclus�o de Ministro", 7501L),
		DISTRIBUIDO("Distribu�do", 7502L),
		DISTRIBUIDO_POR_PREVENCAO("Distribu�do por preven��o", 7503L),
		DISTRIBUIDO_POR_PREVENCA0_DE_TURMA("Distribu�do por preven��o de Turma", 7504L),
		APRESENTACAO_EM_MESA("Apresentado em Mesa", 7600L),
		INCLUSAO_EM_PAUTA("Inclus�o na pauta", 7601L),
		BAIXA_EM_DILIGENCIA("Baixa em dilig�ncia", 7100L),
		REMESSA_EXTERNA_DOS_AUTOS("Remessa externa dos autos", 7101L),
		BAIXA_AO_ARQUIVO_DO_STF_GUIA_NO("Baixa ao Arquivo do STF - guia n�", 7103L),
		BAIXA_DEFINITIVA_DOS_AUTOS("Baixa definitiva dos autos", 7104L),
		REMESSA_AO_JUIZO_COMPETENTE("Remessa ao ju�zo competente", 7108L),
		AUTOS_DEVOLVIDOS_A_ORIGEM("Autos devolvidos � origem", 7109L),
		SUSPENSO_JULGAMENTO("Suspenso o julgamento", 7205L),
		RETIRADO_JULGAMENTO_VIRTUAL("Retirado do Julgamento Virtual", 8521L),
		ARQUIVO_DE_SUSTENTACAO_ORAL_REJEITADO("Arquivo de sustenta��o oral rejeitado", 8557l),
		INCLUIDO_NA_LISTA_DE_JULGAMENTO("Inclu�do na lista de julgamento", 8546L),
		SUBSTITUICAO_DO_RELATOR("Substitui��o do Relator, art. 38, II, do RISTF", 8509L),
		SUBSTITUICAO_DO_RELATOR_IV_B("Substitui��o do Relator, art. 38, IV, b, do RISTF", 8572L),
		JULGADO_MERITO_TEMA_COM_RG_SEM_TESE("Julgado m�rito de tema com repercuss�o geral sem fixa��o de tese", 8562L),
		DESTAQUE_DO_MINISTRO("Destaque do(a) Ministro(a)", 8564L),
		PEDIDO_DE_DESTAQUE_CANCELADO("Pedido de destaque cancelado", 8570L),
		PROCESSO_DESTACADO_NO_JULGAMENTO_VIRTUAL("Processo destacado no Julgamento Virtual", 8522L);

		private String descricao;
		private Long id;

		Andamentos(String descricao, Long id) {
			this.descricao = descricao;
			this.id = id;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
	
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
		
		public static Andamentos valueOfCodigoAndamento(Long codigoAndamento) {
			for (Andamentos andamento : Andamentos.values()) {
				if (andamento.getId().equals(codigoAndamento)) {
					return andamento;
				}
			}
			
			return null;
		}
	}

}
