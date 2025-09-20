package br.gov.stf.estf.entidade.processostf;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
/*
 * Essa classe não será mais utilizada pois a tabela na qual ela faz referência será desativada,
 * e não existe nenhum sistema da SSGJ que referencie essa classe.
 */
@Entity
@Table(schema = "SINP", name = "PETICAO_INICIAL")
public class PeticaoInicial extends DocumentoProcessual<PeticaoInicial.PeticaoInicialId> {
	
	private static final long				serialVersionUID	= 1L;
	private Date							dataEntrada;
	private Boolean							procSigiloso;
	private Long							sequenceObjeto;
	private Boolean							sigiloso;
	private TipoCusta						tipoCusta;
	private TipoMeioPeticaoInicial			tipoMeioProcessoPeticaoInicial;
	private TipoRecebimentoPeticaoInicial	tipoRecebimento;
	private List<TipoPreferencia> tiposPreferencia = new LinkedList<TipoPreferencia>();


	
	@Id
	public PeticaoInicialId getId() {
		return id;
	}

	@Column(name = "DAT_ENTRADA_STF")
	public Date getDataEntrada() {
		return dataEntrada;
	}


	


	@Column(name = "FLG_PROC_SIGILOSO", unique = false, insertable = true, updatable = true, length = 1)
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getProcSigiloso() {
		return procSigiloso;
	}


	@Column(name = "SEQ_OBJETO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "SINP.SEQ_OBJETO", allocationSize = 1)
	public Long getSequenceObjeto() {
		return sequenceObjeto;
	}


	@Column(name = "FLG_SIGILOSO", unique = false, insertable = true, updatable = true, length = 1)
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getSigiloso() {
		return this.sigiloso;
	}


	@Column(name = "TIP_CUSTA", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	@org.hibernate.annotations.Type(type = "br.gov.stf.estf.processostf.model.dataaccess.TipoCustaEnumUserType")
	public TipoCusta getTipoCusta() {
		return tipoCusta;
	}


	@Column(name = "TIP_MEIO_PROCESSO", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	@org.hibernate.annotations.Type(type = "br.gov.stf.estf.processostf.model.dataaccess.TipoMeioPeticaoInicialEnumUserType")
	public TipoMeioPeticaoInicial getTipoMeioProcessoPeticaoInicial() {
		return this.tipoMeioProcessoPeticaoInicial;
	}


	@Column(name = "TIP_RECEBIMENTO", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	@org.hibernate.annotations.Type(type = "br.gov.stf.estf.processostf.model.dataaccess.TipoRecebimentoPeticaoInicialEnumUserType")
	public TipoRecebimentoPeticaoInicial getTipoRecebimento() {
		return this.tipoRecebimento;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "PETICAO_INICIAL_PREFERENCIA", schema = "SINP", 
			joinColumns = {@JoinColumn(name = "ANO_PETICAO_INICIAL", 
					referencedColumnName = "ANO_PETICAO_INICIAL"), 
					@JoinColumn(name = "NUM_PETICAO_INICIAL", 
							referencedColumnName = "NUM_PETICAO_INICIAL")},
			inverseJoinColumns = {@JoinColumn(name = "SEQ_TIPO_PREFERENCIA")})
	public List<TipoPreferencia> getTiposPreferencia() {
		return tiposPreferencia;
	}


	public void setTiposPreferencia(List<TipoPreferencia> tiposPreferencia) {
		this.tiposPreferencia = tiposPreferencia;
	}
	

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}


	public void setProcSigiloso(Boolean procSigiloso) {
		this.procSigiloso = procSigiloso;
	}


	public void setSequenceObjeto(Long sequenceObjeto) {
		this.sequenceObjeto = sequenceObjeto;
	}


	public void setSigiloso(Boolean sigiloso) {
		this.sigiloso = sigiloso;
	}


	public void setTipoCusta(TipoCusta tipoCusta) {
		this.tipoCusta = tipoCusta;
	}


	public void setTipoMeioProcessoPeticaoInicial(TipoMeioPeticaoInicial tipoMeioProcesso) {
		this.tipoMeioProcessoPeticaoInicial = tipoMeioProcesso;
	}


	public void setTipoRecebimento(TipoRecebimentoPeticaoInicial tipoRecebimento) {
		this.tipoRecebimento = tipoRecebimento;
	}

	@Embeddable
	public static class PeticaoInicialId implements java.io.Serializable {
		private static final long	serialVersionUID	= 1L;
		private Short				ano;
		private Long				numero;


		public boolean equals(Object obj) {

			if (obj instanceof PeticaoInicialId == false) {
				return false;
			}

			if (this == obj) {
				return true;
			}

			PeticaoInicialId id = (PeticaoInicialId) obj;

			return new EqualsBuilder().appendSuper(super.equals(obj)).append(getNumero(), id.getNumero()).append(
					getAno(), id.getAno()).isEquals();
		}


		@Column(name = "ANO_PETICAO_INICIAL")
		public Short getAno() {
			return ano;
		}


		@Column(name = "NUM_PETICAO_INICIAL")
		public Long getNumero() {
			return numero;
		}


		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(getNumero()).append(getAno()).toHashCode();
		}


		public void setAno(Short ano) {
			this.ano = ano;
		}


		public void setNumero(Long numero) {
			this.numero = numero;
		}
	}
}
