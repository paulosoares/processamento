package br.gov.stf.estf.entidade.julgamento;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="SESSAO", schema="JULGAMENTO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sessao extends ESTFBaseEntity<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3752399064597087432L;
	private Long numero;
	private Short ano;
	private Date dataInicio;
	private Date dataFim;
	private Date dataPrevistaInicio;
	private Date dataPrevistaFim;
	private String tipoSessao;
	private Colegiado colegiado;
	private String tipoAmbiente;
	private Boolean disponibilizadoInternet;
	private String observacao;
	private Integer tipoJulgamentoVirtual;
	private Boolean exclusivoDigital;
	
	private String memoriaCalculo;
	
	public  static final String VIDEO_CONFERENCIA = "videoconferência";
	public  static final String PRESENCIAL = "presencial";
	 
	
	private List<EventoSessao> listaEventoSessao = new LinkedList<EventoSessao>();
	private List<EnvolvidoSessao> listaEnvolvidoSessao = new LinkedList<EnvolvidoSessao>();
	private List<JulgamentoProcesso> listaJulgamentoProcesso = new LinkedList<JulgamentoProcesso>();
	private List<ListaJulgamento> listasJulgamento = new LinkedList<ListaJulgamento>();

	public final static Long VALOR_MAXIMO_PARA_NUMERO_SESSAO = 9999999999L;
	
	public enum TipoAmbienteConstante {
		PRESENCIAL("Sessão Presencial", "F"),
		VIRTUAL("Sessão Virtual", "V");
				
		private String descricao;
		private String sigla;
		
		private TipoAmbienteConstante( String descricao, String sigla ) {
			this.descricao = descricao;
			this.sigla = sigla;
		}

		public String getDescricao() {
			return descricao;
		}
		public String getSigla() {
			return sigla;
		}
		
		public static TipoAmbienteConstante valueOfSigla(String sigla) {
			for (TipoAmbienteConstante tipoAmbienteConstante : TipoAmbienteConstante.values()) {
				if (tipoAmbienteConstante.getSigla().equalsIgnoreCase(sigla)) {
					return tipoAmbienteConstante;
				}
			}
			return null;
		}
	}
	
	public enum TipoSessaoConstante {
		ORDINARIA("Sessão Ordinária", "O"),
		EXTRAORDINARIA("Sessão Extraordinária", "E"),
		SOLENE("Sessão Solene", "S");
				
		private String descricao;
		private String sigla;
		
		private TipoSessaoConstante( String descricao, String sigla ) {
			this.descricao = descricao;
			this.sigla = sigla;
		}

		public String getDescricao() {
			return descricao;
		}
		public String getSigla() {
			return sigla;
		}
		
		public static TipoSessaoConstante valueOfSigla(String sigla) {
			for (TipoSessaoConstante tipoSessao : TipoSessaoConstante.values()) {
				if (tipoSessao.getSigla().equalsIgnoreCase(sigla)) {
					return tipoSessao;
				}
			}
			return null;
		}
	}		
	
	public enum TipoJulgamentoVirtual{
		REPERCUSSAO_GERAL(1, "Repercussão Geral"),
		LISTAS_DE_JULGAMENTO(2, "Listas de Julgamento");
		
		private Integer id;
		private String descricao;
		
		private TipoJulgamentoVirtual(Integer id, String descricao){
			this.id = id;
			this.descricao = descricao;
		}
		
		public Integer getId(){
			return this.id;
		}
		
		public Long getCodigo(){
			return Long.parseLong(String.valueOf(this.id));
		}
		
		public String getDescricao(){
			return this.descricao;
		}
		
		public static TipoJulgamentoVirtual valueOfId(Integer id) {
			for (TipoJulgamentoVirtual tipoJulgamento : TipoJulgamentoVirtual.values()) {
				if (tipoJulgamento.getId() == id) {
					return tipoJulgamento;
				}
			}
			return null;
		}
	}
	
	@Id
	@Column( name="SEQ_SESSAO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_SESSAO", allocationSize=1 )	
	public Long getId() {
		return id;
	}	
	
	@Column( name="NUM_SESSAO", insertable=true, updatable=true, unique=false, nullable=true )
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	
	@Column( name="ANO_SESSAO", insertable=true, updatable=true, unique=false, nullable=true )
	public Short getAno() {
		return ano;
	}
	public void setAno(Short ano) {
		this.ano = ano;
	}
	
	@Temporal( TemporalType.TIMESTAMP )
	@Column( name="DAT_INICIO", insertable=true, updatable=true, unique=false, nullable=true, length=7 )
	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	@Temporal( TemporalType.TIMESTAMP )
	@Column( name="DAT_FIM", insertable=true, updatable=true, unique=false, nullable=true, length=7 )	
	public Date getDataFim() {
		return dataFim;
	}
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	@Temporal( TemporalType.TIMESTAMP )
	@Column( name="DAT_PREVISTA_INICIO", insertable=true, updatable=true, unique=false, nullable=true, length=7 )		
	public Date getDataPrevistaInicio() {
		return dataPrevistaInicio;
	}
	public void setDataPrevistaInicio(Date dataPrevistaInicio) {
		this.dataPrevistaInicio = dataPrevistaInicio;
	}
	
	@Temporal( TemporalType.TIMESTAMP )
	@Column( name="DAT_PREVISTA_FIM", insertable=true, updatable=true, unique=false, nullable=false, length=7 )	
	public Date getDataPrevistaFim() {
		return dataPrevistaFim;
	}
	public void setDataPrevistaFim(Date dataPrevistaFim) {
		this.dataPrevistaFim = dataPrevistaFim;
	}
		
	@Column(name = "TIP_SESSAO", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	public String getTipoSessao() {
		return tipoSessao;
	}
	public void setTipoSessao(String tipoSessao) {
		this.tipoSessao = tipoSessao;
	}
	
	@ManyToOne( cascade={}, fetch=FetchType.LAZY )
	@JoinColumn(name="COD_COLEGIADO",insertable=true, updatable=false )
	public Colegiado getColegiado() {
		return colegiado;
	}
	public void setColegiado(Colegiado colegiado) {
		this.colegiado = colegiado;
	}
		
	@Column(name = "TIP_AMBIENTE_SESSAO", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	public String getTipoAmbiente() {
		return tipoAmbiente;
	}
	public void setTipoAmbiente(String tipoAmbiente) {
		this.tipoAmbiente = tipoAmbiente;
	}

    @OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="sessao")
    @org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
	@OrderBy(clause = "DSC_EVENTO_SESSAO")  
	public List<EventoSessao> getListaEventoSessao() {
		return listaEventoSessao;
	}

	public void setListaEventoSessao(List<EventoSessao> listaEventoSessao) {
		this.listaEventoSessao = listaEventoSessao;
	}

    @OneToMany( cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="sessao" )
	@OrderBy(clause = "SEQ_SESSAO, SEQ_ENVOLVIDO, COD_MINISTRO, DSC_JUSTIFICATIVA") 
	public List<EnvolvidoSessao> getListaEnvolvidoSessao() {
		return listaEnvolvidoSessao;
	}

	public void setListaEnvolvidoSessao(List<EnvolvidoSessao> listaEnvolvidoSessao) {
		this.listaEnvolvidoSessao = listaEnvolvidoSessao;
	}

    @OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="sessao")
	@OrderBy(clause = "SEQ_SESSAO, SEQ_JULGAMENTO_PROCESSO, TIPO_JULGAMENTO, COD_RECURSO") 
	public List<JulgamentoProcesso> getListaJulgamentoProcesso() {
		return listaJulgamentoProcesso;
	}

	public void setListaJulgamentoProcesso(
			List<JulgamentoProcesso> listaJulgamentoProcesso) {
		this.listaJulgamentoProcesso = listaJulgamentoProcesso;
	}
	
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="sessao")
    @org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
	@OrderBy(clause = "SEQ_SESSAO, NUM_ORDEM_SESSAO") 
	public List<ListaJulgamento> getListasJulgamento() {
		return listasJulgamento;
	}

	public void setListasJulgamento(
			List<ListaJulgamento> listasJulgamento) {
		this.listasJulgamento = listasJulgamento;
	}
	
	@Column(name = "FLG_DISPONIVEL_INTERNET")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getDisponibilizadoInternet() {
		return disponibilizadoInternet;
	}
	
	public void setDisponibilizadoInternet(Boolean disponibilizadoInternet) {
		this.disponibilizadoInternet = disponibilizadoInternet;
	}
	
	@Column(name = "FLG_EXCLUSIVO_DIGITAL")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getExclusivoDigital() {
		return exclusivoDigital;
	}
	
	public void setExclusivoDigital(Boolean exclusivoDigital) {
		this.exclusivoDigital = exclusivoDigital;
	}
	
	@Column(name = "TXT_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Column(name = "TIP_JULGAMENTO_VIRTUAL")
	public Integer getTipoJulgamentoVirtual() {
		return tipoJulgamentoVirtual;
	}

	public void setTipoJulgamentoVirtual(Integer tipoJulgamentoVirtual) {
		this.tipoJulgamentoVirtual = tipoJulgamentoVirtual;
	}
	
	@Transient
	public boolean isAtiva() {
		return new Date().before(dataPrevistaFim);
	}

	@Transient
	public String getMemoriaCalculo() {
		return memoriaCalculo;
	}

	public void setMemoriaCalculo(String memoriaCalculo) {
		this.memoriaCalculo = memoriaCalculo;
	}
	
	
}
