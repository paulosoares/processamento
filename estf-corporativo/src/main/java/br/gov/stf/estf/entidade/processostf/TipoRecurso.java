package br.gov.stf.estf.entidade.processostf;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

/**
 * Representa os tipos de recursos. Ao contrário da antiga tabela
 * STF.TIPO_RECURSOS, não irá guardar a cadeia de dependência (hierarquia), mas
 * somente o tipo do recurso.
 * 
 * <p>
 * Ex.:</br> Na antiga tabela :
 * "AG.REG.NA MEDIDA CAUTELAR NO HABEAS CORPUS".</br> Nesta tabela:
 * "Agravo Regimental".</br>
 * 
 * <p>
 * A informação do exemplo de Medida Cautelar e Habeas Corpus estará na relação
 * hierárquica da tabela JUDICIARIO.OBJETO_INCIDENTE.
 * 
 * @author Rodrigo Barreiros
 * @author Demétrius Jubé
 * 
 * @since 15.07.2009
 */
@Entity
@Table(schema = "JUDICIARIO", name = "TIPO_RECURSO")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIP_OBJETO_INCIDENTE", discriminatorType = DiscriminatorType.STRING)
public abstract class TipoRecurso extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 8456474891971663758L;

	private String sigla;

	private String descricao;

	private String descricaoTipoRecursoCadeia;

	private String descricaoConectorCadeiaIncidente;

	private Boolean distribuicao;

	private Boolean ativo;

	private Andamento tipoAndamento;

	private TipoGeneroNumero tipoGeneroNumero;

	private Boolean admitePautaJulgamento;
	
	private Boolean permiteCriacaoIncidenteGabinete;
	
	private String TipoObjetoIncidente;
	
	private Set<Classe> classes;
	
	public static final String SIGLA_TIPO_REAFIRMACAO_JURISPRUDENCIA = "RJ";
	
	public static final String SIGLA_AGRAVO = "AgR";
	
	public static final String SIGLA_EMBARGOS_DE_DIVERGENCIA = "EDv";


	/**
	 * Identifica do Tipo de Recurso.
	 */
	@Id
	@Column(name = "SEQ_TIPO_RECURSO")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Sigla do Tipo do Recurso para a formação da cadeia do Recurso ou do
	 * Incidente Julgamento.
	 */
	@Column(name = "SIG_TIPO_RECURSO")
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String siglaTipoRecurso) {
		this.sigla = siglaTipoRecurso;
	}

	/**
	 * Descrição do Tipo de Recurso.
	 */
	@Column(name = "DSC_TIPO_RECURSO")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricaoTipoRecurso) {
		this.descricao = descricaoTipoRecurso;
	}

	/**
	 * Cadeia do Tipo do Recurso.
	 */
	@Column(name = "DSC_TIPO_RECURSO_CADEIA")
	public String getDescricaoTipoRecursoCadeia() {
		return descricaoTipoRecursoCadeia;
	}

	public void setDescricaoTipoRecursoCadeia(String descricaoTipoRecursoCadeia) {
		this.descricaoTipoRecursoCadeia = descricaoTipoRecursoCadeia;
	}

	/**
	 * Concetor do tipo do recurso para a formação da cadeia incidente.
	 * 
	 * <p>
	 * Ex.: Para a formação da cadeia "EMB.DECL.NA RECLAMAÇÃO", para o tipo de
	 * recurso "RECLAMAÇÃO", o conector será "NA".
	 */
	@Column(name = "DSC_CONECTOR_CADEIA_INCIDENTE")
	public String getDescricaoConectorCadeiaIncidente() {
		return descricaoConectorCadeiaIncidente;
	}

	public void setDescricaoConectorCadeiaIncidente(String descricaoConectorCadeiaIncidente) {
		this.descricaoConectorCadeiaIncidente = descricaoConectorCadeiaIncidente;
	}

	/**
	 * Flag que indica se o recurso deve ser distribuído ou não.
	 */
	@Column(name = "FLG_DISTRIBUICAO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getDistribuicao() {
		return distribuicao;
	}

	public void setDistribuicao(Boolean distribuicao) {
		this.distribuicao = distribuicao;
	}

	/**
	 * Indica se o registro está ativo ou não.
	 */
	@Column(name = "FLG_ATIVO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * Identifica o andamento.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_ANDAMENTO")
	public Andamento getTipoAndamento() {
		return tipoAndamento;
	}

	public void setTipoAndamento(Andamento tipoAndamento) {
		this.tipoAndamento = tipoAndamento;
	}

	/**
	 * Tipo de gênero e número do tipo de recurso para a formação da cadeia em
	 * Recurso e Incidente Julgamento.
	 */
	@Column(name = "TIP_GENERO_NUMERO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoGeneroNumero"),
			@Parameter(name = "idClass", value = "java.lang.String") })
	public TipoGeneroNumero getTipoGeneroNumero() {
		return tipoGeneroNumero;
	}

	public void setTipoGeneroNumero(TipoGeneroNumero tipoGeneroNumero) {
		this.tipoGeneroNumero = tipoGeneroNumero;
	}

	@Column(name = "FLG_PAUTA_JULG")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAdmitePautaJulgamento() {
		return admitePautaJulgamento;
	}

	public void setAdmitePautaJulgamento(Boolean pautaJulgamento) {
		this.admitePautaJulgamento = pautaJulgamento;
	}
	
	/**
	 * Flag que indica se o recurso deve ser distribuído ou não. */
	@Column(name = "FLG_CRIACAO_GABINETE")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPermiteCriacaoIncidenteGabinete() {
		return permiteCriacaoIncidenteGabinete;
	}

	public void setPermiteCriacaoIncidenteGabinete(Boolean permiteCriacaoIncidenteGabinete) {
		this.permiteCriacaoIncidenteGabinete = permiteCriacaoIncidenteGabinete;
	}
	
	@ManyToMany(targetEntity=Classe.class, fetch = FetchType.LAZY)
	@JoinTable(
		schema="JUDICIARIO",	
		name="RESTRICAO_CLASSE_RECURSO",
		joinColumns=@JoinColumn(name="SEQ_TIPO_RECURSO"),
		inverseJoinColumns=@JoinColumn(name="SIG_CLASSE")
	)
	public Set<Classe> getClasses() {
		return classes;
	}
	
	public void setClasses(Set<Classe> classes) {
		this.classes = classes;
	}

	@Column(name = "TIP_OBJETO_INCIDENTE", insertable = false, updatable = false)
	public String getTipoObjetoIncidente() {
		return TipoObjetoIncidente;
	}

	public void setTipoObjetoIncidente(String tipoObjetoIncidente) {
		TipoObjetoIncidente = tipoObjetoIncidente;
	}

}
