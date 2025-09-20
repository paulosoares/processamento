package br.gov.stf.estf.entidade.documento;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

@MappedSuperclass
@Entity
@Table(name = "PECA_PROCESSO_ELETRONICO", schema = "STF")
public class PecaProcessoEletronico extends ESTFAuditavelBaseEntity<Long> {

	private static final long serialVersionUID = -2294645780715242998L;

	public static final String TIPO_ORIGEM_INTERNA = "I";
	
	private Long id;
	private Long numeroPagInicio;
	private Long numeroPagFim;
	private Long numeroOrdemPeca;
	private Integer andamentoProtocolo;
	private Setor setor;
	private String tipoOrigemPeca;
	private String descricaoPeca;
	private List<ArquivoProcessoEletronico> documentos;
	private Set<Lembrete> lembretes = new HashSet<Lembrete>();
	private TipoSituacaoPeca tipoSituacaoPeca;
	private TipoPecaProcesso tipoPecaProcesso;
	private Set<ArquivoProcessoEletronico> documentosEletronicos = new HashSet<ArquivoProcessoEletronico>();
	private ObjetoIncidente<?> objetoIncidente;
	private Integer numeroNivelSigilo;

	
	

	private String tipoArquivo;
	private String descricaoTipoArquivo;

	@Formula("(SELECT DD.SEQ_TIPO_ARQUIVO FROM DOC.VW_DOCUMENTO DD, JUDICIARIO.PECA_PROCESSUAL PP WHERE DD.SEQ_DOCUMENTO = PP.SEQ_DOCUMENTO AND PP.SEQ_PECA_PROCESSUAL = SEQ_PECA_PROC_ELETRONICO )")
	public String getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(String tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}
	
	@Transient
	public String getDescricaoTipoArquivo() {
		if( tipoArquivo == "1") {
			return "DOC/HTML";
			} else if (tipoArquivo.equals("2") || tipoArquivo.equals("3")|| tipoArquivo.equals("4")|| tipoArquivo.equals("5")|| tipoArquivo.equals("6")|| tipoArquivo.equals("7")|| tipoArquivo.equals("8")|| tipoArquivo.equals("9")|| tipoArquivo.equals("10")) {
				return "DOC";
			} else if (tipoArquivo.equals("11")|| tipoArquivo.equals("12")|| tipoArquivo.equals("13")|| tipoArquivo.equals("14")) {
				return "IMG";
			} else if (tipoArquivo.equals("15")|| tipoArquivo.equals("16")|| tipoArquivo.equals("17")) {
				return "VID";
			} else if (tipoArquivo.equals("18")|| tipoArquivo.equals("19")) {
				return "AUD";
			} else {
				return "IND";
			}
		
	}

	public void setDescricaoTipoArquivo(String descriaotipoArquivo) {
		this.descricaoTipoArquivo = descricaoTipoArquivo;
	}

	@Id
	@Column(name = "SEQ_PECA_PROC_ELETRONICO", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_PECA_PROC_ELETRONICO", allocationSize = 1)
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NUM_PAG_INICIO", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getNumeroPagInicio() {
		return this.numeroPagInicio;
	}

	public void setNumeroPagInicio(Long numeroPagInicio) {
		this.numeroPagInicio = numeroPagInicio;
	}

	@Column(name = "NUM_PAG_FIM", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getNumeroPagFim() {
		return this.numeroPagFim;
	}

	public void setNumeroPagFim(Long numeroPagFim) {
		this.numeroPagFim = numeroPagFim;
	}

	@Column(name = "NUM_ORDEM_PECA", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getNumeroOrdemPeca() {
		return this.numeroOrdemPeca;
	}

	public void setNumeroOrdemPeca(Long numeroOrdemPeca) {
		this.numeroOrdemPeca = numeroOrdemPeca;
	}

	@Column(name = "SEQ_ANDAMENTO_PROTOCOLO", unique = false, nullable = true, insertable = true, updatable = true, precision = 9, scale = 0)
	public Integer getAndamentoProtocolo() {
		return this.andamentoProtocolo;
	}

	public void setAndamentoProtocolo(Integer andamentoProtocolo) {
		this.andamentoProtocolo = andamentoProtocolo;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR", unique = false, nullable = true, insertable = true, updatable = true)
	public Setor getSetor() {
		return this.setor;
	}

	public void setSetor(Setor codigoSetor) {
		this.setor = codigoSetor;
	}

	@Column(name = "DSC_PECA", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getDescricaoPeca() {
		return this.descricaoPeca;
	}

	public void setDescricaoPeca(String descricaoPeca) {
		this.descricaoPeca = descricaoPeca;
	}
	
	@Column(name = "TIP_ORIGEM_PECA", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getTipoOrigemPeca() {
		return this.tipoOrigemPeca;
	}

	public void setTipoOrigemPeca(String tipoOrigemPeca) {
		this.tipoOrigemPeca = tipoOrigemPeca;
	}

	@OneToMany(mappedBy = "pecaProcessoEletronico", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public List<ArquivoProcessoEletronico> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(List<ArquivoProcessoEletronico> documentos) {
		this.documentos = documentos;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "SEQ_PECA_PROC_ELETRONICO", referencedColumnName = "SEQ_PECA_PROC_ELETRONICO")
	@OrderBy(clause = "NUM_ORDEM")
	public Set<Lembrete> getLembretes() {
		return lembretes;
	}

	public void setLembretes(Set<Lembrete> lembretes) {
		this.lembretes = lembretes;
	}

	@Column(name = "SEQ_TIPO_SITUACAO_PECA", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.documento.TipoSituacaoPeca"),
			@Parameter(name = "identifierMethod", value = "getCodigo") })
	public TipoSituacaoPeca getTipoSituacaoPeca() {
		return tipoSituacaoPeca;
	}

	public void setTipoSituacaoPeca(TipoSituacaoPeca tipoSituacaoPeca) {
		this.tipoSituacaoPeca = tipoSituacaoPeca;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_PECA", referencedColumnName = "SEQ_TIPO_PECA")
	public TipoPecaProcesso getTipoPecaProcesso() {
		return tipoPecaProcesso;
	}

	public void setTipoPecaProcesso(TipoPecaProcesso tipoPecaProcessoEletronico) {
		this.tipoPecaProcesso = tipoPecaProcessoEletronico;
	}

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "pecaProcessoEletronico")
	@org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@org.hibernate.annotations.OrderBy(clause = "NUM_ORDEM")
	public Set<ArquivoProcessoEletronico> getDocumentosEletronicos() {
		return documentosEletronicos;
	}

	public void setDocumentosEletronicos(Set<ArquivoProcessoEletronico> documentosEletronicos) {
		this.documentosEletronicos = documentosEletronicos;
	}

	/**
	 * O Objeto Incidente a que se refere a Peca Processo Eletrônico, podendo
	 * ser um Processo, um Recurso ou um Incidente de Julgamento. Alimentada
	 * pela coluna SEQ_OBJETO_INCIDENTE_COMPLETO.
	 * 
	 * @param objetoIncidente
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE_COMPLETO", insertable = true, updatable = false)
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", insertable = true, updatable = false)
	@LazyToOne(LazyToOneOption.NO_PROXY)
	private ObjetoIncidente<?> getObjetoIncidenteProcesso() {
		if (objetoIncidente != null) {
			return objetoIncidente.getPrincipal();
		}
		return null;
	}

	private void setObjetoIncidenteProcesso(ObjetoIncidente<?> objetoIncidente) {

	}

	public void adicionarArquivoProcessoEletronico(ArquivoProcessoEletronico arquivoProcessoEletronico) {
		arquivoProcessoEletronico.setPecaProcessoEletronico(this);
		getDocumentosEletronicos().add(arquivoProcessoEletronico);
	}

	public Long recuperarSequencialNumeroOrdemDocumentoEletronico() {
		Set documentos = getDocumentosEletronicos();
		if (documentos.size() > 0) {
			ArquivoProcessoEletronico[] s = new ArquivoProcessoEletronico[documentos.size()];
			documentos.toArray(s);
			ArquivoProcessoEletronico p = s[s.length - 1];
			return p.getNumeroOrdem() + 1L;
		}
		return 1L;
	}

	public void adicionarLembrete(Lembrete lembrete) {
		lembrete.setPecaProcessoEletronico(this);
		getLembretes().add(lembrete);
	}

	public void excluirLembrete(Lembrete lembrete) {
		if (getLembretes().contains(lembrete)) {
			getLembretes().remove(lembrete);
		}
	}

	@Transient
	public boolean isTipoSituacaoPeca(TipoSituacaoPeca tipoSituacaoPeca) {
		return getTipoSituacaoPeca() != null && getTipoSituacaoPeca().equals(tipoSituacaoPeca);
	}
	
	@Column(name = "NUM_NIVEL_SIGILO", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public Integer getNumeroNivelSigilo() {
		return numeroNivelSigilo;
	}

	public void setNumeroNivelSigilo(Integer numeroNivelSigilo) {
		this.numeroNivelSigilo = numeroNivelSigilo;
	}

}
