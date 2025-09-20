package br.gov.stf.estf.entidade.ministro;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.usuario.model.util.TipoTurma;
import br.gov.stf.framework.model.entity.TipoSexo;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Table(schema = "STF", name = "MINISTROS")
public class Ministro extends ESTFBaseEntity<Long> {

	public static final Short CODIGO_PRIMEIRA_TURMA = 1;
	public static final Short CODIGO_SEGUNDA_TURMA = 2;
	// Código do registro que representa o dado MINISTRO PRESIDENTE no banco
	// de dados
	public static final Long COD_MINISTRO_PRESIDENTE = 1L;
	public static final Long COD_MINISTRO_VICE_PRESIDENTE = 400L;
	
	// codigo do ministro que exerce a presidência no biênio
	public static final Long COD_MINISTRO_PRESIDENTE_BIENIO = 44L;
	
	// Usado no repercussão geral para definir a unanimidade em listas de julgamento. Quando só o Ministro Marco Aurélio diverge, a ListaJulgamento tem tratamento diferente.
	public static final Long COD_MINISTRO_MARCO_AURELIO = 30L;
	
	private static final long serialVersionUID = 1L;
	
	private String sigla;
	private String nome;
	private Ministro ministroRevisor;
	private Date dataPosse;
	private Ministro ministroAntecessor;
	private Ministro ministroSussessor;
	private Boolean eleitorial;
	private Usuario usuario;
	private Setor setor;
	private TipoSexo tipoSexo;
	private Date dataAfastamento;
	private Short codigoTurma;
	private TipoTurma tipoTurma;
	private MinistroPresidente ministroPresidente;

	@Id
	@Column(name = "COD_MINISTRO", unique = true, nullable = false, insertable = false, updatable = false)
	public Long getId() {
		return id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_AFAST_MINISTRO", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataAfastamento() {
		return dataAfastamento;
	}

	public void setDataAfastamento(Date dataAfastamento) {
		this.dataAfastamento = dataAfastamento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_POSSE_MIN", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataPosse() {
		return dataPosse;
	}

	public void setDataPosse(Date dataPosse) {
		this.dataPosse = dataPosse;
	}

	@Column(name = "FLG_ELEITORAL")
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getEleitorial() {
		return eleitorial;
	}

	public void setEleitorial(Boolean eleitorial) {
		this.eleitorial = eleitorial;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_MINISTRO_ANTER", unique = false, nullable = true, insertable = true, updatable = true)
	public Ministro getMinistroAntecessor() {
		return ministroAntecessor;
	}

	public void setMinistroAntecessor(Ministro ministroAntecessor) {
		this.ministroAntecessor = ministroAntecessor;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_MINISTRO_REVIS", unique = false, nullable = true, insertable = true, updatable = true)
	public Ministro getMinistroRevisor() {
		return ministroRevisor;
	}

	public void setMinistroRevisor(Ministro ministroRevisor) {
		this.ministroRevisor = ministroRevisor;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_MINISTRO_SUCES", unique = false, nullable = true, insertable = true, updatable = true)
	public Ministro getMinistroSussessor() {
		return ministroSussessor;
	}

	public void setMinistroSussessor(Ministro ministroSussessor) {
		this.ministroSussessor = ministroSussessor;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumns( {
			@JoinColumn(name = "DAT_POSSE_MIN", referencedColumnName = "DAT_POSSE", nullable = true, insertable = false, updatable = false),
			@JoinColumn(name = "COD_MINISTRO", referencedColumnName = "COD_MINISTRO", nullable = true, insertable = false, updatable = false) })
	public MinistroPresidente getMinistroPresidente() {
		return ministroPresidente;
	}

	public void setMinistroPresidente(MinistroPresidente ministroPresidente) {
		this.ministroPresidente = ministroPresidente;
	}

	@Column(name = "NOM_MINISTRO", unique = false, nullable = true, insertable = true, updatable = true)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR", unique = false, nullable = true, insertable = true, updatable = true)
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@Column(name = "SIG_MINISTRO", unique = false, nullable = true, insertable = true, updatable = true)
	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Column(name = "SEXO")
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.TipoSexoUserType")
	public TipoSexo getTipoSexo() {
		return tipoSexo;
	}

	public void setTipoSexo(TipoSexo tipoSexo) {
		this.tipoSexo = tipoSexo;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SIG_USUARIO", unique = false, nullable = true, insertable = true, updatable = true)
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Column(name = "COD_TURMA_MINISTRO")
	public Short getCodigoTurma() {
		return codigoTurma;
	}

	public void setCodigoTurma(Short codigoTurma) {
		this.codigoTurma = codigoTurma;
	}
	
	@Column( name="COD_TURMA_MINISTRO", insertable=false, updatable=false )
    @org.hibernate.annotations.Type( type = "br.gov.stf.estf.usuario.model.util.TipoTurmaEnumUserType" )
	public TipoTurma getTipoTurma() {
		return tipoTurma;
	}
	public void setTipoTurma(TipoTurma tipoTurma) {
		this.tipoTurma = tipoTurma;
	}	

	
	/**
	 * monta o nome do ministro com letras maiúsculas e minúsculas. Ex.: 'Gilmar Mendes'
	 * @param mostraMinistroInicio mostra no início do nome, 'Ministro' ou 'Ministra' de acordo com o tipoSexto
	 * @return nome
	 */
	@Transient
	public String getNomeMinistroCapsulado(boolean mostraMinistroInicio){
		return formatarNomeMinistroCapsulado(mostraMinistroInicio, false);
	}	
	
	/**
	 * Monta o nome do ministro com letras maiúsculas e/ou minúsculas. 
	 * @param mostraMinistroInicio mostra no início do nome, 'Ministro' ou 'Ministra' de acordo com o tipoSexto
	 * @param nomeMaiusculo caso seja false, fica na forma 'Gilmar Mendes', agora se for true 'GILMAR MENDES'
	 * @return nome
	 */
	@Transient
	public String getNomeMinistroCapsulado(boolean mostraMinistroInicio, boolean nomeMaiusculo){
		return formatarNomeMinistroCapsulado(mostraMinistroInicio, nomeMaiusculo);
	}
	
	/**
	 * Monta o nome do ministro com letras maiúsculas e/ou minúsculas. 
	 * @param mostraMinistroInicio mostra no início do nome, 'Ministro' ou 'Ministra' de acordo com o tipoTexto
	 * @param nomeMaiusculo caso seja false, fica na forma 'Gilmar Mendes', agora se for true 'GILMAR MENDES'
	 * @param ministroInicioAbreviado mostra no início do nome 'Min.' se mostraMinistroInicio for true
	 * @return nome
	 */
	@Transient
	public String getNomeMinistroCapsulado(boolean mostraMinistroInicio, boolean nomeMaiusculo, boolean ministroInicioAbreviado){
		return formatarNomeMinistroCapsulado(mostraMinistroInicio, nomeMaiusculo, ministroInicioAbreviado);
	}
	
	@Transient
	public String formatarNomeMinistroCapsulado(boolean mostraMinistroInicio, boolean nomeMaiusculo){
		return formatarNomeMinistroCapsulado(mostraMinistroInicio, nomeMaiusculo, false);
	}
	
	@Transient
	public String formatarNomeMinistroCapsulado(boolean mostraMinistroInicio, boolean nomeMaiusculo, boolean ministroInicioAbreviado){
		String nomeCapsulado = "";
		if(getNome()!=null&&getNome().trim().length()>0){
			String nome = getNome().toLowerCase();
			if(getId()!=1 && ((mostraMinistroInicio && !ministroInicioAbreviado) || !mostraMinistroInicio)) {
				nome = nome.replace("min. ","");
			}
			for(int i = 0; i<nome.length();i++){
				Character letra = nome.charAt(i);
				if(i==0){
					nomeCapsulado = nomeCapsulado + letra.toString().toUpperCase();
				}else if(nome.codePointAt(i)==32){
					int proximo = i+1;
					if(proximo < nome.length()){
						Character proximaLetra = nome.charAt(i+1);
						nomeCapsulado = nomeCapsulado + " "+ proximaLetra.toString().toUpperCase();
					}
				}else if(nome.codePointAt(i-1)!=32){
					nomeCapsulado = nomeCapsulado + letra;
				}
			}
			nomeCapsulado = nomeCapsulado.replace(" De "," de ");
			nomeCapsulado = nomeCapsulado.replace(" E "," e ");
			nomeCapsulado = nomeCapsulado.replace(" Dos "," dos ");
			if(nomeMaiusculo)
				nomeCapsulado = nomeCapsulado.toUpperCase();
			if(getId()!=1 && mostraMinistroInicio && !ministroInicioAbreviado){
				nomeCapsulado = (getTipoSexo().getValor().equals(TipoSexo.FEMININO)?"Ministra ":"Ministro ") + nomeCapsulado;
			}
		}
		return nomeCapsulado;
	}

}