package br.gov.stf.estf.entidade.publicacao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.julgamento.Sessao;
import br.gov.stf.estf.entidade.util.TipoSituacaoAta;

@Entity
@Table(name = "MATERIAS", schema = "STF")
public class ConteudoPublicacao extends ESTFBaseEntity<Long> {

	public static final String SITUACAO_EM_ELABORACAO = "Em elaboração";
	public static final String SITUACAO_ATA_COMPOSTA = "Ata composta em: ";
	public static final String SITUACAO_DJ_COMPOSTO = "DJ composto em: ";
	public static final String SITUACAO_DJ_PUBLICADO = "DJ publicado em: ";

	private static final long serialVersionUID = 3817922345430619209L;

	private EstruturaPublicacao estruturaPublicacao;
	private Publicacao publicacao;
	private Date dataComposicaoParcial;
	private Date dataComposicaoDj;
	private Date dataPrevistaPublicacaoDJ;
	private ArquivoEletronico arquivoEletronico;
	private TipoSessao tipoSessao;
	private Short ano;
	private Integer numero;
	private Date dataCriacao;
	private Integer codigoCapitulo;
	private Integer codigoMateria;
	private Integer codigoConteudo;
	private Sessao sessao;

	@Id
	@Column(name = "SEQ_MATERIAS", nullable = false, precision = 10, scale = 0)
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_MATERIAS", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	@Column(name = "DAT_PREVISTA_PUBLICACAO_DJ")
	public Date getDataPrevistaPublicacaoDJ() {
		return dataPrevistaPublicacaoDJ;
	}

	public void setDataPrevistaPublicacaoDJ(Date dataPrevistaPublicacaoDJ) {
		this.dataPrevistaPublicacaoDJ = dataPrevistaPublicacaoDJ;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns( {
			@JoinColumn(name = "COD_CAPITULO", referencedColumnName = "COD_CAPITULO", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_MATERIA", referencedColumnName = "COD_MATERIA", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "COD_CONTEUDO", referencedColumnName = "COD_CONTEUDO", nullable = false, insertable = false, updatable = false) })
	public EstruturaPublicacao getEstruturaPublicacao() {
		return this.estruturaPublicacao;
	}

	public void setEstruturaPublicacao(EstruturaPublicacao estruturaPublicacao) {
		this.estruturaPublicacao = estruturaPublicacao;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_DATA_PUBLICACOES")
	public Publicacao getPublicacao() {
		return this.publicacao;
	}

	public void setPublicacao(Publicacao publicacao) {
		this.publicacao = publicacao;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DAT_COMPOSICAO_PARCIAL", length = 7)
	public Date getDataComposicaoParcial() {
		return this.dataComposicaoParcial;
	}

	public void setDataComposicaoParcial(Date datComposicaoParcial) {
		this.dataComposicaoParcial = datComposicaoParcial;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DAT_COMPOSICAO_DJ", length = 7)
	public Date getDataComposicaoDj() {
		return this.dataComposicaoDj;
	}

	public void setDataComposicaoDj(Date dataComposicaoDj) {
		this.dataComposicaoDj = dataComposicaoDj;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ARQUIVO_ELETRONICO")
	public ArquivoEletronico getArquivoEletronico() {
		return this.arquivoEletronico;
	}

	public void setArquivoEletronico(ArquivoEletronico arquivoTexto) {
		this.arquivoEletronico = arquivoTexto;
	}
	
	@Column(name = "TIP_SESSAO", insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.publicacao.TipoSessao"),
			@Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "identifierMethod", value = "getSigla"),
			@Parameter(name = "valueOfMethod", value = "valueOfSigla"),
			@Parameter(name = "nullValue", value = "VAZIO")})
	public TipoSessao getTipoSessao() {
		return this.tipoSessao;
	}

	public void setTipoSessao(TipoSessao tipSessao) {
		this.tipoSessao = tipSessao;
	}

	@Column(name = "COD_CAPITULO", nullable = false, precision = 2, scale = 0)
	public Integer getCodigoCapitulo() {
		return this.codigoCapitulo;
	}

	public void setCodigoCapitulo(Integer codCapitulo) {
		this.codigoCapitulo = codCapitulo;
	}

	@Column(name = "COD_MATERIA", nullable = false, precision = 2, scale = 0)
	public Integer getCodigoMateria() {
		return this.codigoMateria;
	}

	public void setCodigoMateria(Integer codMateria) {
		this.codigoMateria = codMateria;
	}

	@Column(name = "COD_CONTEUDO", nullable = false, precision = 2, scale = 0)
	public Integer getCodigoConteudo() {
		return this.codigoConteudo;
	}

	public void setCodigoConteudo(Integer codConteudo) {
		this.codigoConteudo = codConteudo;
	}

	@Column(name = "ANO_MATERIA", nullable = false, precision = 4, scale = 0)
	public Short getAno() {
		return this.ano;
	}

	public void setAno(Short anoMateria) {
		this.ano = anoMateria;
	}

	@Column(name = "NUM_MATERIA", nullable = false, precision = 5, scale = 0)
	public Integer getNumero() {
		return this.numero;
	}

	public void setNumero(Integer numMateria) {
		this.numero = numMateria;
	}

	@Column(name = "DAT_CRIACAO", nullable = false, length = 7)
	@Temporal(TemporalType.DATE)
	public Date getDataCriacao() {
		return this.dataCriacao;
	}

	public void setDataCriacao(Date datCriacao) {
		this.dataCriacao = datCriacao;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_SESSAO")
	public Sessao getSessao() {
		return sessao;
	}

	public void setSessao(Sessao sessao) {
		this.sessao = sessao;
	}
	
	@Transient
	public TipoSituacaoAta getTipoSituacaoAta () {
		TipoSituacaoAta situacao = null;
		if ( dataComposicaoParcial==null ) {
			situacao = TipoSituacaoAta.EM_ELABORACAO;
		} else if ( dataComposicaoDj==null ) {
			situacao = TipoSituacaoAta.LIBERADA;
		} else {
			situacao = TipoSituacaoAta.COMPOSTA;
		}
		return situacao;
	}

	// RECUPERA A SITUACAO DA MATERIA
	@Transient
	public String getSituacao() {
		String situacao = null;
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if (dataComposicaoParcial == null) {
			situacao = SITUACAO_EM_ELABORACAO;
		} else if (dataComposicaoDj == null) {
			situacao = SITUACAO_ATA_COMPOSTA
					+ dateFormat.format(dataComposicaoParcial);
		} else if (publicacao != null
				&& publicacao.getDataPublicacaoDj() != null) {
			situacao = SITUACAO_DJ_PUBLICADO
					+ dateFormat.format(publicacao.getDataPublicacaoDj());
		} else {
			situacao = SITUACAO_DJ_COMPOSTO
					+ dateFormat.format(dataComposicaoDj);
		}

		return situacao;
	}
	
	@Transient
	public String getIdentificacao(){
		return numero+"/"+ano;
	}

}
