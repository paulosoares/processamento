package br.gov.stf.estf.entidade.processostf;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.InteiroTeorAcordao;

@Entity
@Table(name = "PROCESSOS_IMG", schema = "STF")
public class ProcessoImagem extends ESTFAuditavelBaseEntity<ProcessoImagem.ProcessoImagemId> {

	private static final long serialVersionUID = -3803254866640483754L;

	public static final String ELETRONICO = "ELETRONICO";

	private ProcessoImagemId id;
	private ObjetoIncidente<?> objetoIncidente;
	private Boolean flgLiberado;
	private InteiroTeorAcordao seqInteiroTeor;
	// private ArquivoEletronico seqDocumento;
	private DocumentoEletronico seqDocumento;
	private Date dataPublicacao;
	private TipoColecao tipoColecao;
	private TipoStatusRevisao tipoStatusRevisao;
	private ClasseUnificada classeUnificada;
	private Long numeroPaginas;

	@Id
	public ProcessoImagemId getId() {
		return this.id;
	}

	public void setId(ProcessoImagemId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@Column(name = "FLG_LIBERADO", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getFlgLiberado() {
		return flgLiberado;
	}

	public void setFlgLiberado(Boolean flgLiberado) {
		this.flgLiberado = flgLiberado;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_INTEIRO_TEOR_ACORDAO", unique = false, nullable = true, insertable = true, updatable = true)
	public InteiroTeorAcordao getSeqInteiroTeor() {
		return seqInteiroTeor;
	}

	public void setSeqInteiroTeor(InteiroTeorAcordao seqInteiroTeor) {
		this.seqInteiroTeor = seqInteiroTeor;
	}

	/*
	 * Obtém o arquivo eletrônico RTF. Pode ser null.
	 * 
	 * @return
	 * 
	 * @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "SEQ_DOCUMENTO", unique = false, nullable = true, insertable = true, updatable = true) public ArquivoEletronico getSeqDocumento() {
	 * return seqDocumento; }
	 * 
	 * public void setSeqDocumento(ArquivoEletronico seqDocumento) { this.seqDocumento = seqDocumento; }
	 */

	/**
	 * Obtém o arquivo eletrônico PDF. Pode ser null.
	 * 
	 * @return
	 */
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_DOCUMENTO", unique = false, nullable = true, insertable = true, updatable = true)
	public DocumentoEletronico getSeqDocumento() {
		return seqDocumento;
	}

	public void setSeqDocumento(DocumentoEletronico seqDocumento) {
		this.seqDocumento = seqDocumento;
	}

	@Column(name = "DAT_PUBLICACAO", unique = false, nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	/**
	 * O tipo de coleção ao qual o processo imagem se enquadra.
	 * 
	 * @return
	 * @see TipoColecao
	 */
	@Column(name = "TIP_COLECAO", unique = false, nullable = false, insertable = false, updatable = false, length = 10)
	@Enumerated(EnumType.STRING)
	public TipoColecao getTipoColecao() {
		return tipoColecao;
	}

	public void setTipoColecao(TipoColecao tipoColecao) {
		this.tipoColecao = tipoColecao;
	}

	@Column(name = "COD_STATUS_REVISAO", unique = false, nullable = true, insertable = true, updatable = true)
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.processostf.TipoStatusRevisao"),
			@Parameter(name = "idClass", value = "java.lang.String"), @Parameter(name = "identifierMethod", value = "getCodigo") })
	public TipoStatusRevisao getTipoStatusRevisao() {
		return tipoStatusRevisao;
	}

	public void setTipoStatusRevisao(TipoStatusRevisao tipoStatusRevisao) {
		this.tipoStatusRevisao = tipoStatusRevisao;
	}

	/*@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_CLASSE", unique = false, nullable = false, insertable = false, updatable = false)*/
	@Transient
	public ClasseUnificada getClasseUnificada() {
		return classeUnificada;
	}

	public void setClasseUnificada(ClasseUnificada classeUnificada) {
		this.classeUnificada = classeUnificada;
	}
	
	@Column(name = "NUM_PAGINA")
	public Long getNumeroPaginas() {
		return numeroPaginas;
	}
	
	public void setNumeroPaginas(Long numeroPaginas) {
		this.numeroPaginas = numeroPaginas;
	}

	/**
	 * Verifica se o tipo de coleção é igual ao tipo passado por parâmetro.
	 * 
	 * @param tipo
	 * @return
	 */
	@Transient
	public boolean isTipoColecao(TipoColecao tipo) {
		return ObjectUtils.equals(getTipoColecao(), tipo);
	}

	
	/**
	 * Mapeamento da chave composta da tabela STF.PROCESSOS_IMG
	 * 
	 * @author ViniciusK
	 * 
	 */
	@Embeddable
	public static class ProcessoImagemId implements Serializable {
		private static final long serialVersionUID = 6664568674447476424L;
		private Integer codClasse;
		private Long numProcesso;
		private Long numEmenta;
		private Long numTomo;
		private String tipColecao;

		@Column(name = "COD_CLASSE", unique = false, nullable = false, insertable = true, updatable = true, precision = 4, scale = 0)
		public Integer getCodClasse() {
			return codClasse;
		}

		public void setCodClasse(Integer codClasse) {
			this.codClasse = codClasse;
		}

		@Column(name = "NUM_PROCESSO", unique = false, nullable = false, insertable = true, updatable = true, precision = 6, scale = 0)
		public Long getNumProcesso() {
			return numProcesso;
		}

		public void setNumProcesso(Long numProcesso) {
			this.numProcesso = numProcesso;
		}

		@Column(name = "NUM_EMENTA", unique = false, nullable = false, insertable = true, updatable = true, precision = 4, scale = 0)
		public Long getNumEmenta() {
			return numEmenta;
		}

		public void setNumEmenta(Long numEmenta) {
			this.numEmenta = numEmenta;
		}

		@Column(name = "NUM_TOMO", unique = false, nullable = false, insertable = true, updatable = true, precision = 4, scale = 0)
		public Long getNumTomo() {
			return numTomo;
		}

		public void setNumTomo(Long numTomo) {
			this.numTomo = numTomo;
		}

		@Column(name = "TIP_COLECAO", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
		public String getTipColecao() {
			return tipColecao;
		}

		public void setTipColecao(String tipColecao) {
			this.tipColecao = tipColecao;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((codClasse == null) ? 0 : codClasse.hashCode());
			result = prime * result + ((numEmenta == null) ? 0 : numEmenta.hashCode());
			result = prime * result + ((numProcesso == null) ? 0 : numProcesso.hashCode());
			result = prime * result + ((numTomo == null) ? 0 : numTomo.hashCode());
			result = prime * result + ((tipColecao == null) ? 0 : tipColecao.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ProcessoImagemId other = (ProcessoImagemId) obj;
			if (codClasse == null) {
				if (other.codClasse != null)
					return false;
			} else if (!codClasse.equals(other.codClasse))
				return false;
			if (numEmenta == null) {
				if (other.numEmenta != null)
					return false;
			} else if (!numEmenta.equals(other.numEmenta))
				return false;
			if (numProcesso == null) {
				if (other.numProcesso != null)
					return false;
			} else if (!numProcesso.equals(other.numProcesso))
				return false;
			if (numTomo == null) {
				if (other.numTomo != null)
					return false;
			} else if (!numTomo.equals(other.numTomo))
				return false;
			if (tipColecao == null) {
				if (other.tipColecao != null)
					return false;
			} else if (!tipColecao.equals(other.tipColecao))
				return false;
			return true;
		}
	}

	/**
	 * Enumeração dos tipos de coleção existentes.
	 * 
	 * @author thiago.miranda
	 */
	public static enum TipoColecao {

		EMENTARIO("Ementário"), REVFORENSE("RevForense"), REVDIR("RevDir"), ACORDAO("Acórdão"), COLAC("Colac"), ADJ("ADJ"), REVSTF("RevSTF"), ELETRONICO(
				"Eletrônico");

		private String descricao;

		TipoColecao(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}
}
