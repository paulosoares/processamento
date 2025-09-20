package br.gov.stf.estf.entidade.localizacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema = "EGAB", name = "TIPO_CONFIGURACAO_SETOR")
public class TipoConfiguracaoSetor extends ESTFBaseEntity<Long> {

	public static final String SIGLA_TIPO_CONFIGURACAO_EGAB = "EGAB";
	public static final String SIGLA_TIPO_CONFIGURACAO_FLUXO_FASE = "FLOW";
	public static final String SIGLA_TIPO_CONFIGURACAO_DECREMENTAR_CONTAGEM_USUARIO_DISTRIBUIÇÃO = "DCDU";
	public static final String SIGLA_TIPO_CONFIGURACAO_EGABE = "EGAB-E";
	public static final String SIGLA_TIPO_CONFIGURACAO_ANDAMENTO_SETOR = "ANDAMENTO";
	public static final String EXIBIR_ESTATÍSTICAS = "EST";
	public static final String DISTRIBUIR_PROCESSOS_ELETRONICOS_COM_COMPENSACAO = "DPECC";
	public static final String DISTRIBUIR_PROCESSOS_ELETRONICOS_SEM_COMPENSACAO = "DPESC";
	public static final String EXIBIR_DESLOCAMENTOS_DO_MAG = "D-MAG";
	public static final String SIGLA_TIPO_CONFIGURACAO_CARGA_DISTRIBUICAO_FASE_ATUAL = "CDFA";
	public static final String EXIBIR_PROVIDENCIA_CONTATO = "EPC";
	public static final String REGISTRO_DE_FASE_NAO_LOCALIZADO_NO_SETOR = "RFNLS";
	public static final String REGISTRAR_DISTRIBUICAO_PARA_NAO_LOCALIZADO_SETOR = "RDNLS";
	/*Usado na distribuição por classe e tipo de julgamento*/
	public static final String NAO_DECREMENTAR_MERITO_QUANDO_HOUVER_MEDIDA_CAUTELA = "DCTJ"; 
	public static final String NAO_EXIBIR_SECAO_DESABILITADA = "NESD";
	public static final String UTILIZA_CARGA_PROGRAMADA = "CARGA-PROG";
	public static final String ZERA_CONTAGEM = "ZERA-CONT";
	
	
	private String descricao;
	private String sigla;
	private Boolean ativo;

	@Id
	@Column( name="SEQ_TIPO_CONFIGURACAO_SETOR" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_TIPO_CONFIGURACAO_SETOR", allocationSize = 1 )	
	public Long getId() {
		return id;
	}
	
	@Column(name = "FLG_ATIVO", unique = false, nullable = false, insertable = false, updatable = false)
    @Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}
	
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	@Column(name="DSC_TIPO_CONFIGURACAO_SETOR", unique = true, nullable = false, insertable = false, 
			updatable = false, length = 50)
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column(name="SIG_TIPO_CONFIGURACAO_SETOR", unique = true, nullable = false, insertable = false, 
			updatable = false, length = 10)
	public String getSigla() {
		return sigla;
	}
	
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Transient
	public boolean getIsTipoEGAB() {
		return getIsTipo(SIGLA_TIPO_CONFIGURACAO_EGAB);
	}
	
	@Transient
	public boolean getIsTipoFluxoFase() {
		return getIsTipo(SIGLA_TIPO_CONFIGURACAO_FLUXO_FASE);
	}
	
	@Transient
	public boolean getIsTipoEGABe() {
		return getIsTipo(SIGLA_TIPO_CONFIGURACAO_EGABE);
	}
	
	@Transient
	public boolean getIsTipoDecrementarContagemUsuarioDistribuicao() {
		return getIsTipo(SIGLA_TIPO_CONFIGURACAO_DECREMENTAR_CONTAGEM_USUARIO_DISTRIBUIÇÃO);
	}	
	
	@Transient
	public boolean getIsTipoAndamentoSetor() {
		return getIsTipo(SIGLA_TIPO_CONFIGURACAO_ANDAMENTO_SETOR);
	}	
	
	@Transient
	public boolean getIsRegistrarFaseNaoLocalizadoNoSetor() {
		return getIsTipo(REGISTRO_DE_FASE_NAO_LOCALIZADO_NO_SETOR);
	}
	
	@Transient
	public boolean getIsTipo(String siglaTipoConfiguracao) {
		if(sigla == null)
			return false;
		
		return sigla.equals(siglaTipoConfiguracao);
	}
	
	@Transient
	public boolean getIsZeraContagem() {
		return getIsTipo(ZERA_CONTAGEM);
	}	

}
