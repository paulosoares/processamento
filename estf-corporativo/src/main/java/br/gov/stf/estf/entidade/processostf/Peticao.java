package br.gov.stf.estf.entidade.processostf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.localizacao.Setor;

/**
 * Representa as petições avulsas registradas no protocolo do STF.
 * 
 * <p>
 * As petições avulsas não estão necessariamente vinculadas a um processo. Podem
 * se tratar de uma informação, um requerimento ou um esclarecimento. Ao darem
 * entrada no Supremo, elas pode ser identificada como avulsas e depois serem
 * convertida inicial (protocolo), se for o caso.
 * 
 * <p>
 * A petição avulsa pode ser utilizada para dar entrada em um recurso relativo a
 * um processo. Neste caso, ela será vinculada ao processo.
 * 
 * @author Rodrigo Barreiros
 * @author Demétrius Jubé
 * 
 * @since 15.07.2009
 */
@Entity
@Table(name = "PETICAO", schema = "JUDICIARIO")
public class Peticao extends ObjetoIncidente {

	private static final long serialVersionUID = -2821332526089510487L;

	private Long numeroPeticao;

	private Short anoPeticao;

	private String descricao;

	private Date data;

	private Boolean remessaIndevida;

	private String descricaoIdentificaCorrespondencia;

	private Boolean juntadaProcesso;

	private Setor setorRecebimento;

	private TipoRecebimento tipoRecebimento;

	private TipoConfidencialidade tipoConfidencialidade;

	private ObjetoIncidente<?> objetoIncidenteVinculado;

	private ObjetoIncidente<?> objetoIncidente;
	
	private Long idObjetoIncidente;

	private TipoRecurso tipoRecurso;

	private Long seqAndamentoJuntadaProcesso;
	
	private Boolean pendenteDigitalizacao;

	/**
	 * TODO: Descomentar: Ainda não está na base de Homologação. private
	 * SituacaoProcesso situacao;
	 */

	/**
	 * Número sequencial da petição fornecido pelo protocolo. Esse número é
	 * reinicializado a cada ano.
	 */
	@Column(name = "NUM_PETICAO")
	public Long getNumeroPeticao() {
		return numeroPeticao;
	}

	public void setNumeroPeticao(Long numero) {
		this.numeroPeticao = numero;
	}

	/**
	 * Ano corrente em que a petição deu entrada no STF.
	 */
	@Column(name = "ANO_PETICAO")
	public Short getAnoPeticao() {
		return anoPeticao;
	}

	public void setAnoPeticao(Short ano) {
		this.anoPeticao = ano;
	}

	/**
	 * Resumo do conteúdo da petição.
	 */
	@Column(name = "DSC_PETICAO")
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Data em que a petição foi confeccionada.
	 */
	@Column(name = "DAT_PETICAO")
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * Caracteriza uma petição como remessa indevida.
	 */
	@Column(name = "FLG_REMESSA_INDEVIDA")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getRemessaIndevida() {
		return remessaIndevida;
	}

	public void setRemessaIndevida(Boolean remessaIndevida) {
		this.remessaIndevida = remessaIndevida;
	}

	/**
	 * Caracteriza uma petição pendente de digitalização
	 */
	@Column(name = "FLG_PENDENTE_DIGITALIZACAO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPendenteDigitalizacao() {
		return pendenteDigitalizacao;
	}

	public void setPendenteDigitalizacao(Boolean pendenteDigitalizacao) {
		this.pendenteDigitalizacao = pendenteDigitalizacao;
	}
	
	/**
	 * Identificação da correspondência entregue ao STF.
	 */
	@Column(name = "DSC_IDENTIFICA_CORRESPONDENCIA")
	public String getDescricaoIdentificaCorrespondencia() {
		return descricaoIdentificaCorrespondencia;
	}

	public void setDescricaoIdentificaCorrespondencia(
			String descricaoIdentificaCorrespondencia) {
		this.descricaoIdentificaCorrespondencia = descricaoIdentificaCorrespondencia;
	}

	/**
	 * Indica se esta Petição foi juntada ou não ao processo.
	 */

	/**
	 * Com a entrada do eJUD em produção, a FLG_JUNTADA_PROCESSO deixou de ser
	 * alimentada, o sistema verifica que existe incidente vinculado se existir,
	 * foi juntada, se não existir não foi juntada.
	 * 
	 * Código anterior:
	 * 
	 * @Column(name = "FLG_JUNTADA_PROCESSO")
	 * @Type(type =
	 *            "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType"
	 *            ) public Boolean getJuntadaProcesso() { return
	 *            juntadaProcesso; } public void setJuntadaProcesso(Boolean
	 *            juntadaProcesso) { this.juntadaProcesso = juntadaProcesso; }
	 */

	/**
	 * @Transient public Boolean getJuntadaProcesso() { if
	 *            (getObjetoIncidenteVinculado() != null) return true; else
	 *            return false; }
	 */

	/*
	 * Se for processo eletrônico o sistema mostra como juntada se for físico
	 * verifica se existe o andamento
	 */
	@Transient
	public Boolean getJuntadaProcesso() {

		if (getObjetoIncidente().getPrincipal() != null) {

			Processo processo = (Processo) getObjetoIncidente().getPrincipal();
			if (processo != null && processo.getIsEletronico()) {
				return true;
			} else {
				if (getSeqAndamentoJuntadaProcesso() != null
						&& getSeqAndamentoJuntadaProcesso().longValue() != 0)
					return true;
				else
					return false;
			}
		} else {
			return false;
		}
			
	}

	public void setJuntadaProcesso(Boolean juntadaProcesso) {
		this.juntadaProcesso = juntadaProcesso;
	}

	/**
	 * Setor do STF a qual esta Petição está associada.
	 * 
	 * <p>
	 * Este campo só será preenchido se o tipo de recebimento dessa petição for
	 * "Setor do STF", informando qual o setor que iniciou essa petição.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_SETOR_RECEBIMENTO")
	public Setor getSetorRecebimento() {
		return setorRecebimento;
	}

	public void setSetorRecebimento(Setor setorRecebimento) {
		this.setorRecebimento = setorRecebimento;
	}

	/**
	 * Tipo de recebimento da Petição que será registrado no STF.
	 */
	@Column(name = "TIP_RECEBIMENTO")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoRecebimento"),
			@Parameter(name = "idClass", value = "java.lang.String") })
	public TipoRecebimento getTipoRecebimento() {
		return tipoRecebimento;
	}

	public void setTipoRecebimento(TipoRecebimento tipoRecebimento) {
		this.tipoRecebimento = tipoRecebimento;
	}

	/**
	 * Classificação da petição quanto a permissão de acesso as informações por
	 * usuários específicos.
	 * 
	 * <p>
	 * Valores: </br> OC - Oculto </br> SJ - Segredo de Justiça
	 * 
	 * <p>
	 * Uma petição sigilosa é marcada como Segredo de Justiça.
	 */
	@Column(name = "TIP_CONFIDENCIALIDADE")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoConfidencialidade"),
			@Parameter(name = "idClass", value = "java.lang.String"),
			@Parameter(name = "valueOfMethod", value = "valueOfCodigo") } )
	public TipoConfidencialidade getTipoConfidencialidade() {
		return tipoConfidencialidade;
	}

	public void setTipoConfidencialidade(
			TipoConfidencialidade tipoConfidencialidade) {
		this.tipoConfidencialidade = tipoConfidencialidade;
	}

	/**
	 * Uma petição pode ser pré-vinculada a uma outra Petição, ou a um Processo,
	 * ou a um Protocolo. Neste campo armazenamos este Objeto quando há este
	 * pré-vínculo.
	 * 
	 * <p>
	 * Esta é apenas uma pré-vinculação quando cadastramos uma petição.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE_VINC")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidenteVinculado() {
		return objetoIncidenteVinculado;
	}

	public void setObjetoIncidenteVinculado(
			ObjetoIncidente<?> objetoIncidenteVinculado) {
		this.objetoIncidenteVinculado = objetoIncidenteVinculado;
	}

	/**
	 * Uma petição pode ser pré-vinculada a uma outra Petição, ou a um Processo,
	 * ou a um Protocolo.
	 * 
	 * <p>
	 * Neste campo armazenamos a chave deste Objeto quando há este pré-vínculo.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_RECURSO")
	public TipoRecurso getTipoRecurso() {
		return tipoRecurso;
	}

	public void setTipoRecurso(TipoRecurso tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", updatable = false, insertable = false)
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	
	public void setIdObjetoIncidente(Long idObjetoIncidente) {
		this.idObjetoIncidente = idObjetoIncidente;
	}
	@Column(name="SEQ_OBJETO_INCIDENTE", updatable = false, insertable = false)
	public Long getIdObjetoIncidente() {
		return idObjetoIncidente;
	}

	@Transient
	@Override
	public String getIdentificacao() {
		return getNumeroPeticao() + "/" + getAnoPeticao();
	}

	public void setSeqAndamentoJuntadaProcesso(Long seqAndamentoJuntadaProcesso) {
		this.seqAndamentoJuntadaProcesso = seqAndamentoJuntadaProcesso;
	}

	@Formula("(SELECT MAX(AP.SEQ_ANDAMENTO_PETICAO) FROM STF.ANDAMENTO_PETICAO "
			+ "AP WHERE AP.SEQ_OBJETO_INCIDENTE = SEQ_OBJETO_INCIDENTE AND AP.COD_ANDAMENTO = 8245)")
	private Long getSeqAndamentoJuntadaProcesso() {
		return seqAndamentoJuntadaProcesso;
	}
	
	// incluido para manter compatibilidade
	@Transient
	public boolean getEletronico() {
		return false;
	}
	
}
