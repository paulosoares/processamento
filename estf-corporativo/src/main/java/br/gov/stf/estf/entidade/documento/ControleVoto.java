package br.gov.stf.estf.entidade.documento;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.documento.model.util.TipoSessaoControleVoto;
import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.util.DateTimeHelper;

@Entity
@Table(schema = "STF", name = "CONTROLE_VOTOS")
public class ControleVoto extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 6931046525335207038L;

	private Long id;
	private Ministro ministro;
	private TipoTexto tipoTexto;
	private String anotacoes;
	private String anotacoesTaquigrafia;
	private TipoSessaoControleVoto sessao;
	private Date dataRecebimentoGab;
	private Date dataPublico;
	private Date dataProntoAcordao;
	private Date dataSessao;
	private Texto texto;
	private List<Texto> textoControleVoto;
	private TipoSituacaoTexto tipoSituacaoTexto;
	private ObjetoIncidente<?> objetoIncidente;
	private Long seqObjetoIncidente;
	private String tipoJulgamento;

	private Long sequenciaVoto;
	private String oralEscrito;
	private Long codigoMinistroRelator;
	private Long codigoRecurso;
	private Sessao sessaoJulgamento;
	private String nota_cv;
	
	@Column(name = "DAT_PRONTO_ACORDAO", nullable = true, length = 7)
	public Date getDataProntoAcordao() {
		return dataProntoAcordao;
	}

	public void setDataProntoAcordao(Date dataProntoAcordao) {
		this.dataProntoAcordao = dataProntoAcordao;
	}
	
	@Transient
	public String getDescricaoDataSessao () {
		String data = null;
		if ( getDataSessao()!=null ) {
			data = DateTimeHelper.getDataString( getDataSessao() );
		}
		return data;
	}

	@Column(name = "DAT_SESSAO", nullable = false, length = 7)
	@Temporal(TemporalType.DATE)
	public Date getDataSessao() {
		return dataSessao;
	}

	public void setDataSessao(Date dataSessao) {
		this.dataSessao = dataSessao;
	}

	@Column(name = "SEQ_VOTO")
	public Long getSequenciaVoto() {
		return sequenciaVoto;
	}

	public void setSequenciaVoto(Long sequenciaVoto) {
		this.sequenciaVoto = sequenciaVoto;
	}

	@Id
	@Column(name = "SEQ_CONTROLE_VOTOS", insertable = false, updatable = false)
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_CONTROLE_VOTOS", allocationSize = 1)
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_MINISTRO", insertable = true, updatable = false)
	@LazyToOne(value = LazyToOneOption.NO_PROXY)
	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_RECEB_GABINETE", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataRecebimentoGab() {
		return dataRecebimentoGab;
	}

	public void setDataRecebimentoGab(Date dataRecebimentoGab) {
		this.dataRecebimentoGab = dataRecebimentoGab;
	}

	@Column(name = "COD_TIPO_TEXTO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.documento.TipoTexto"),
			@Parameter(name = "identifierMethod", value = "getCodigo") })
	public TipoTexto getTipoTexto() {
		return tipoTexto;
	}

	public void setTipoTexto(TipoTexto tipoTexto) {
		this.tipoTexto = tipoTexto;
	}

	@Column(name = "ANOTACOES", unique = false, nullable = true, insertable = true, updatable = true)
	public String getAnotacoes() {
		return anotacoes;
	}

	public void setAnotacoes(String anotacoes) {
		this.anotacoes = anotacoes;
	}
	
	@Column(name = "DSC_ANOTACOES_TAQ", unique = false, nullable = true, insertable = true, updatable = true)
	public String getAnotacoesTaquigrafia() {
		return anotacoesTaquigrafia;
	}

	public void setAnotacoesTaquigrafia(String anotacoesTaquigrafia) {
		this.anotacoesTaquigrafia = anotacoesTaquigrafia;
	}

	
	@Column(name = "SESSAO", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.documento.model.util.TipoSessaoControleVoto"),
			@Parameter(name = "idClass", value = "java.lang.String") ,
			@Parameter(name = "valueOfMethod", value = "valueOfCodigo")})
	public TipoSessaoControleVoto getSessao() {
		return sessao;
	}

	public void setSessao(TipoSessaoControleVoto sessao) {
		this.sessao = sessao;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DAT_PUBLICO", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getDataPublico() {
		return dataPublico;
	}

	public void setDataPublico(Date dataPublico) {
		this.dataPublico = dataPublico;
	}

	@Column(name = "SEQ_TIPO_SITUACAO_TEXTO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.documento.TipoSituacaoTexto"),
			@Parameter(name = "identifierMethod", value = "getCodigo") })
	public TipoSituacaoTexto getTipoSituacaoTexto() {
		return tipoSituacaoTexto;
	}

	public void setTipoSituacaoTexto(TipoSituacaoTexto tipoSituacaoTexto) {
		this.tipoSituacaoTexto = tipoSituacaoTexto;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TEXTOS", nullable = true)
	public Texto getTexto() {
		return texto;
	}

	public void setTexto(Texto texto) {
		this.texto = texto;
	}
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumns(value={@JoinColumn(name="SEQ_OBJETO_INCIDENTE", referencedColumnName="SEQ_OBJETO_INCIDENTE", insertable=false, updatable=false),
			@JoinColumn(name="DAT_SESSAO", referencedColumnName="DAT_SESSAO", insertable=false, updatable=false),
			@JoinColumn(name="SEQ_VOTOS", referencedColumnName="SEQ_VOTO", insertable=false, updatable=false),
			@JoinColumn(name="COD_MINISTRO", referencedColumnName="COD_MINISTRO", insertable=false, updatable=false),
			@JoinColumn(name="COD_TIPO_TEXTO", referencedColumnName="COD_TIPO_TEXTO", insertable=false, updatable=false)})
	@OrderBy("id DESC")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public List<Texto> getTextoControleVoto() {
		return textoControleVoto;
	}

	public void setTextoControleVoto(List<Texto> textoControleVoto) {
		this.textoControleVoto = textoControleVoto;
	}

	@org.hibernate.annotations.Formula(" ( select s.cod_ministro from stf.sit_min_processos s "
			+ " where s.seq_objeto_incidente = seq_objeto_incidente and " + " s.cod_ocorrencia in ('RE','SU','RG') and "
			+ " s.FLG_RELATOR_ATUAL = 'S' ) ")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public Long getCodigoMinistroRelator() {
		return codigoMinistroRelator;
	}

	public void setCodigoMinistroRelator(Long codigoMinistroRelator) {
		this.codigoMinistroRelator = codigoMinistroRelator;
	}

	/**
	 * Classe alterada para receber o ObjetoIncidente a que se refere o controle
	 * Voto.
	 * 
	 * @return
	 */
	@Transient
	public Texto getInstanciaTexto() {
		Texto t = new Texto();
		t.setObjetoIncidente(this.getObjetoIncidente());
		t.setDataSessao(this.getDataSessao());
		t.setMinistro(this.getMinistro());
		t.setTipoTexto(this.getTipoTexto());
		return t;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@Column(name="SEQ_OBJETO_INCIDENTE", insertable=false, updatable=false)
	public Long getSeqObjetoIncidente() {
		return seqObjetoIncidente;
	}

	public void setSeqObjetoIncidente(Long seqObjetoIncidente) {
		this.seqObjetoIncidente = seqObjetoIncidente;
	}

	@Column(name = "ORAL_ESCRITO")
	public String getOralEscrito() {
		return oralEscrito;
	}

	public void setOralEscrito(String oralEscrito) {
		this.oralEscrito = oralEscrito;
	}

	@Column(name = "TIP_JULGAMENTO")
	public String getTipoJulgamento() {
		return tipoJulgamento;
	}

	public void setTipoJulgamento(String tipoJulgamento) {
		this.tipoJulgamento = tipoJulgamento;
	}
	
	@Column(name = "COD_RECURSO")
	public Long getCodigoRecurso() {
		return codigoRecurso;
	}

	public void setCodigoRecurso(Long codigoRecurso) {
		this.codigoRecurso = codigoRecurso;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_SESSAO")
	public Sessao getSessaoJulgamento() {
		return sessaoJulgamento;
	}

	public void setSessaoJulgamento(Sessao sessaoJuglamento) {
		this.sessaoJulgamento = sessaoJuglamento;
	}
	
	@Formula("(SELECT STF.FNC_NOTA_CV(SEQ_OBJETO_INCIDENTE) FROM DUAL)")
	public String getNota_cv() {
		return nota_cv;
	}

	public void setNota_cv(String nota_cv) {
		this.nota_cv = nota_cv;
	}

}