package br.gov.stf.estf.entidade.julgamento;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.Pessoa;
import br.gov.stf.estf.entidade.usuario.UsuarioCorporativo;

@Entity
@Table(name = "MANIFESTACAO_REPRESENTANTE", schema = "JULGAMENTO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ManifestacaoRepresentante extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = -7889046906738646086L;
	
	private Long id;
	private ObjetoIncidente<?> objetoIncidente;
	private AndamentoProcesso andamentoProcesso;
	private Date dataEnvio;
	private ListaJulgamento listaJulgamento;
	private PecaProcessoEletronico pecaProcessual;
	private Pessoa representado;
	private Pessoa representante;
	private String nomeRepresentadoSegredoJustica;
	private String nomeRepresentanteSegredoJustica;
	private TipoManifestacao tipoManifestacao;
	private String hashArquivo;
	private Set<ManifestacaoLeitura> manifestacaoLeitura;
	private SituacaoManifestacaoRepresentante situacao;
	private UsuarioCorporativo enviadoPor;
	private boolean sustentacaoOralOuQuestaoFato;

	@Id
	@Column(name = "SEQ_MANIFESTACAO_REPRESENTANTE")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JULGAMENTO.SEQ_MANIFESTACAO_REPRESENTANTE", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ANDAMENTO_PROCESSO")
	public AndamentoProcesso getAndamentoProcesso() {
		return andamentoProcesso;
	}

	public void setAndamentoProcesso(AndamentoProcesso andamentoProcesso) {
		this.andamentoProcesso = andamentoProcesso;
	}

	@Column(name = "DAT_ENVIO")
	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_LISTA_JULGAMENTO")
	public ListaJulgamento getListaJulgamento() {
		return listaJulgamento;
	}

	public void setListaJulgamento(ListaJulgamento listaJulgamento) {
		this.listaJulgamento = listaJulgamento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_PECA_PROCESSUAL")
	public PecaProcessoEletronico getPecaProcessual() {
		return pecaProcessual;
	}

	public void setPecaProcessual(PecaProcessoEletronico pecaProcessual) {
		this.pecaProcessual = pecaProcessual;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SEQ_PESSOA_REPRESENTADA", referencedColumnName = "SEQ_PESSOA")
	public Pessoa getRepresentado() {
		return representado;
	}

	public void setRepresentado(Pessoa representado) {
		this.representado = representado;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SEQ_PESSOA_REPRESENTANTE", referencedColumnName = "SEQ_PESSOA")
	public Pessoa getRepresentante() {
		return representante;
	}

	public void setRepresentante(Pessoa representado) {
		this.representante = representado;
	}

	@Column(name = "TIP_MANIFESTACAO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = { @Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.julgamento.TipoManifestacao"), @Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "valueOfMethod", value = "valueOfSigla") })
	public TipoManifestacao getTipoManifestacao() {
		return tipoManifestacao;
	}

	public void setTipoManifestacao(TipoManifestacao tipoManifestacao) {
		this.tipoManifestacao = tipoManifestacao;
	}

	@Column(name = "TXT_HASH_ARQUIVO")
	public String getHashArquivo() {
		return hashArquivo;
	}

	public void setHashArquivo(String hashArquivo) {
		this.hashArquivo = hashArquivo;
	}
	

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name="SEQ_MANIFESTACAO_REPRESENTANTE")
	public Set<ManifestacaoLeitura> getManifestacaoLeitura() {
		return manifestacaoLeitura;
	}

	public void setManifestacaoLeitura(Set<ManifestacaoLeitura> manifestacaoLeitura) {
		this.manifestacaoLeitura = manifestacaoLeitura;
	}
	
	@Transient
	public Set<Ministro> getMinistrosLeitores() {
		Set<Ministro> lista = new HashSet<Ministro>();
		
		for(ManifestacaoLeitura ml : getManifestacaoLeitura())
			lista.add(ml.getMinistro());
		
		return lista;
	}

	@Transient
	public boolean isSustentacaoOralLidaPeloMinistro(Ministro ministro) { // método necessário para o jsf do repgeral
		if (!getMinistrosLeitores().contains(ministro))
			return  false;

		return true;
	}
	
	@Transient
	public boolean getSustentacaoOralLidaPeloMinistro(Ministro ministro) { // necessário para o jsf do decisão
		return isSustentacaoOralLidaPeloMinistro(ministro);
	}

	@Column(name = "TIP_SITUACAO_ARQUIVO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = { @Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.julgamento.SituacaoManifestacaoRepresentante"), @Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "valueOfMethod", value = "valueOfSigla") })
	public SituacaoManifestacaoRepresentante getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoManifestacaoRepresentante situacao) {
		this.situacao = situacao;
	}
	
	@Transient
	public boolean isSustentacaoOralOuQuestaoFato() {
		sustentacaoOralOuQuestaoFato = Arrays.asList(TipoManifestacao.QUESTAO_FATO, TipoManifestacao.SUSTENTACAO_ORAL).contains(tipoManifestacao);
		return sustentacaoOralOuQuestaoFato;
	}
	
	@Transient
	public boolean isSustentacaoOral() {
		return TipoManifestacao.SUSTENTACAO_ORAL.equals(tipoManifestacao);
	}
	
	@Transient
	public boolean getSustentacaoOralOuQuestaoFato() {
		return isSustentacaoOralOuQuestaoFato();
	}

	public void setSustentacaoOralOuQuestaoFato(boolean sustentacaoOralOuQuestaoFato) {
		this.sustentacaoOralOuQuestaoFato = sustentacaoOralOuQuestaoFato;
	}
	
	@Formula("(SELECT pp.DSC_APRESENTACAO_PARTE FROM JUDICIARIO.PARTE_PROCESSUAL pp WHERE pp.SEQ_PESSOA = SEQ_PESSOA_REPRESENTADA AND pp.SEQ_OBJETO_INCIDENTE = SEQ_OBJETO_INCIDENTE AND ROWNUM = 1)")
	public String getNomeRepresentadoSegredoJustica() {
		return nomeRepresentadoSegredoJustica;
	}

	public void setNomeRepresentadoSegredoJustica(String nomeRepresentadoSegredoJustica) {
		this.nomeRepresentadoSegredoJustica = nomeRepresentadoSegredoJustica;
	}

	@Formula("(SELECT pp.DSC_APRESENTACAO_PARTE FROM JUDICIARIO.PARTE_PROCESSUAL pp WHERE pp.SEQ_PESSOA = SEQ_PESSOA_REPRESENTANTE AND pp.SEQ_OBJETO_INCIDENTE = SEQ_OBJETO_INCIDENTE AND ROWNUM = 1)")
	public String getNomeRepresentanteSegredoJustica() {
		return nomeRepresentanteSegredoJustica;
	}

	public void setNomeRepresentanteSegredoJustica(String nomeRepresentanteSegredoJustica) {
		this.nomeRepresentanteSegredoJustica = nomeRepresentanteSegredoJustica;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SEQ_USUARIO_PETICIONADOR", referencedColumnName = "SEQ_USUARIO")
	@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
	public UsuarioCorporativo getEnviadoPor() {
		return enviadoPor;
	}

	public void setEnviadoPor(UsuarioCorporativo enviadoPor) {
		this.enviadoPor = enviadoPor;
	}
	
}