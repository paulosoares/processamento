package br.gov.stf.estf.entidade.processostf;

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

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Origem;

/**
 * Mantém o histórico de Origens e Processos relacionados.
 * 
 * @author Rodrigo Barreiros
 * @author Demétrius Jubé
 * 
 * @since 15.07.2009
 */
@Entity
@Table(name = "HISTORICO_PROCESSO_ORIGEM", schema = "JUDICIARIO")
public class HistoricoProcessoOrigem extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = -5833478215163101155L;
	
	private Long id;

	private String codigoNumeroRegistro;

	private Boolean processoInicial;

	private Boolean origemEletronica;

	private String siglaClasseOrigem;
	
	private String numeroProcessoOrigem;

	private TipoHistorico tipoHistorico;

	private Origem origem;

	private ClassificacaoJustica classificacaoJustica;

	private Procedencia procedencia;
	
	private Long objetoIncidente;
	
	private Boolean principal;
	
	private String numeroUnicoProcesso;
	
	private Boolean remetente;
	
	private Boolean destinatario;

	/**
	 * Identifica um registro de histórico.
	 */
	@Id
    @Column( name="SEQ_HISTORICO_PROCESSO_ORIGEM" )
    @GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
    @SequenceGenerator( name="sequence", sequenceName="JUDICIARIO.SEQ_HISTORICO_PROCESSO_ORIGEM", allocationSize=1 )   
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Número do registro do processo proveniente da origem.
	 */
	@Column(name = "COD_NUMERO_REGISTRO")
	public String getCodigoNumeroRegistro() {
		return codigoNumeroRegistro;
	}

	public void setCodigoNumeroRegistro(String codigoNumeroRegistro) {
		this.codigoNumeroRegistro = codigoNumeroRegistro;
	}

	/**
	 * Classifica um processo relacionado como a origem que deu início ao processo. 
	 * Utilizado para processos relacionados.
	 */
	@Column(name = "FLG_PROCESSO_INICIAL")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getProcessoInicial() {
		return processoInicial;
	}

	public void setProcessoInicial(Boolean processoInicial) {
		this.processoInicial = processoInicial;
	}

	/**
	 * Identifica se a origem foi cadastrada por outro tribunal. 
	 * Só ocorre para processo eletrônico.
	 */
	@Column(name = "FLG_ORIGEM_ELETRONICA")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getOrigemEletronica() {
		return origemEletronica;
	}

	public void setOrigemEletronica(Boolean origemEletronica) {
		this.origemEletronica = origemEletronica;
	}

	@Column(name = "SIG_CLASSE_ORIGEM")
	public String getSiglaClasseOrigem() {
		return siglaClasseOrigem;
	}

	public void setSiglaClasseOrigem(String siglaClasseOrigem) {
		this.siglaClasseOrigem = siglaClasseOrigem;
	}

	@Column(name = "NUM_PROCESSO_ORIGEM")
	public String getNumeroProcessoOrigem() {
		return numeroProcessoOrigem;
	}

	public void setNumeroProcessoOrigem(String numeroProcessoOrigem) {
		this.numeroProcessoOrigem = numeroProcessoOrigem;
	}

	public void setNumeroUnicoProcesso(String numeroUnicoProcesso) {
		this.numeroUnicoProcesso = numeroUnicoProcesso;
	}

	@Column(name = "NUM_UNICO_PROCESSO")
	public String getNumeroUnicoProcesso() {
		return numeroUnicoProcesso;
	}

	/**
	 * Tipo de classificação para registro do processo precedente ao processo registrado.
	 */
	@Column(name = "TIP_HISTORICO", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoHistorico"),
			@Parameter(name = "idClass", value = "java.lang.String") ,
			@Parameter(name = "valueOfMethod", value = "valueOfCodigo")})
	public TipoHistorico getTipoHistorico() {
		return tipoHistorico;
	}

	public void setTipoHistorico(TipoHistorico tipoHistorico) {
		this.tipoHistorico = tipoHistorico;
	}

	/**
	 * identificar o órgão de origem do processo ou recurso.
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "COD_ORIGEM")
	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}
	
	/**
	 * Identifica uma classificação de justiça.
	 */
	@Column(name = "SEQ_CLASSIFICACAO_JUSTICA", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.ClassificacaoJustica"),
			@Parameter(name = "identifierMethod", value = "getCodigo") })
	public ClassificacaoJustica getClassificacaoJustica() {
		return classificacaoJustica;
	}

	public void setClassificacaoJustica(ClassificacaoJustica classificacaoJustica) {
		this.classificacaoJustica = classificacaoJustica;
	}

	/**
	 * Identifica a procedência.
	 */
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "COD_PROCEDENCIA")
	public Procedencia getProcedencia() {
		return procedencia;
	}

	public void setProcedencia(Procedencia procedencia) {
		this.procedencia = procedencia;
	}
	
	@Column(name = "SEQ_OBJETO_INCIDENTE")
	public Long getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(Long objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	
	@Column(name = "FLG_PRINCIPAL")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}
	
	@Column(name = "FLG_REMETENTE")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getRemetente() {
		return remetente;
	}

	public void setRemetente(Boolean remetente) {
		this.remetente = remetente;
	}
	
	@Column(name = "FLG_DESTINATARIO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Boolean destinatario) {
		this.destinatario = destinatario;
	}

}
