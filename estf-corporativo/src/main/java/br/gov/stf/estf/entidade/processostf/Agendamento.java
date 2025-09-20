package br.gov.stf.estf.entidade.processostf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.julgamento.Colegiado.TipoColegiadoConstante;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.publicacao.EstruturaPublicacao;

@Entity
@Table(schema = "STF", name = "AGENDAMENTOS")
public class Agendamento extends ESTFAuditavelBaseEntity<Agendamento.AgendamentoId> {

	private static final long serialVersionUID = -5306085511149257886L;
	public static final Integer COD_MATERIA_AGENDAMENTO_PAUTA = 1;
	// Índice definitivo
	public static final Integer COD_MATERIA_AGENDAMENTO_JULGAMENTO = 2;
	// Pré-Índice
	public static final Integer COD_MATERIA_AGENDAMENTO_INDICE = 9;

	private AgendamentoId id;
	private Ministro ministro;
	private Boolean vista;
	private String observacaoProcesso;
	private Boolean julgado;
	private Ministro ministroRelator;
	private Boolean dirigida;
	private String descricaoListaAgenda;	
	private ObjetoIncidente<?> objetoIncidente;
	private Boolean destacadoPlenarioVirtual;

	@Id
	public AgendamentoId getId() {
		return id;
	}
	
	public void setId(AgendamentoId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_MINISTRO", unique = false, nullable = false)
	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}

	@Column(name = "OBS_PROCESSO", unique = false, nullable = true)
	public String getObservacaoProcesso() {
		return observacaoProcesso;
	}

	public void setObservacaoProcesso(String observacaoProcesso) {
		this.observacaoProcesso = observacaoProcesso;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_MIN_RELATOR", unique = false, nullable = true)
	public Ministro getMinistroRelator() {
		return ministroRelator;
	}

	public void setMinistroRelator(Ministro ministroRelator) {
		this.ministroRelator = ministroRelator;
	}

	@Column(name = "DSC_LISTA_AGENDA", unique = false, nullable = true)
	public String getDescricaoListaAgenda() {
		return descricaoListaAgenda;
	}

	public void setDescricaoListaAgenda(String descricaoListaAgenda) {
		this.descricaoListaAgenda = descricaoListaAgenda;
	}

	@Column(name = "FLG_VISTAS")
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getVista() {
		return vista;
	}

	public void setVista(Boolean vista) {
		this.vista = vista;
	}

	@Column(name = "FLG_JULGADO")
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getJulgado() {
		return julgado;
	}

	public void setJulgado(Boolean julgado) {
		this.julgado = julgado;
	}

	@Column(name = "FLG_DIRIGIDA")
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getDirigida() {
		return dirigida;
	}

	public void setDirigida(Boolean dirigida) {
		this.dirigida = dirigida;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", updatable = false, insertable = false)
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@Transient
	public String getTipoAgendamento() {
		if (getId().getCodigoMateria() != null) {
			if (getId().getCodigoMateria().equals(COD_MATERIA_AGENDAMENTO_PAUTA)) {
				return "Pauta";
			}
			if (getId().getCodigoMateria().equals(COD_MATERIA_AGENDAMENTO_INDICE)) {
				return "Índice";
			}
			if (getId().getCodigoMateria().equals(COD_MATERIA_AGENDAMENTO_JULGAMENTO)) {
				return "Julgamento";
			}
		}
		return "";
	}

	@Transient
	public String getColegiado() {
		if (getId().getCodigoCapitulo() != null) {
			if (EstruturaPublicacao.COD_CAPITULO_PLENARIO.equals(getId().getCodigoCapitulo())) {
				return "Plenário";
			}
			if (EstruturaPublicacao.COD_CAPITULO_PRIMEIRA_TURMA.equals(getId().getCodigoCapitulo())) {
				return "1ª Turma";
			}
			if (EstruturaPublicacao.COD_CAPITULO_SEGUNDA_TURMA.equals(getId().getCodigoCapitulo())) {
				return "2ª Turma";
			}
		}
		return "";
	}
	
	@Transient
	public TipoColegiadoConstante getTipoColegiadoConstante() {
		if (getId().getCodigoCapitulo() != null)
			return TipoColegiadoConstante.valueOfCodigoCapitulo(getId().getCodigoCapitulo());
		else
			return null;
	}
	
	@Column(name = "FLG_DESTAQUE")
	@org.hibernate.annotations.Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getDestacadoPlenarioVirtual() {
		return destacadoPlenarioVirtual;
	}

	public void setDestacadoPlenarioVirtual(Boolean destacadoPlenarioVirtual) {
		this.destacadoPlenarioVirtual = destacadoPlenarioVirtual;
	}

	@Embeddable
	public static class AgendamentoId implements Serializable {

		private static final long serialVersionUID = 8882369727547005154L;

		private Integer codigoCapitulo;
		private Integer codigoMateria;
		private Long objetoIncidente;

		@Column(name = "SEQ_OBJETO_INCIDENTE")
		public Long getObjetoIncidente() {
			return objetoIncidente;
		}

		public void setObjetoIncidente(Long objetoIncidente) {
			this.objetoIncidente = objetoIncidente;
		}

		@Column(name = "COD_CAPITULO")
		public Integer getCodigoCapitulo() {
			return codigoCapitulo;
		}

		public void setCodigoCapitulo(Integer codigoCapitulo) {
			this.codigoCapitulo = codigoCapitulo;
		}

		@Column(name = "COD_MATERIA")
		public Integer getCodigoMateria() {
			return codigoMateria;
		}

		public void setCodigoMateria(Integer codigoMateria) {
			this.codigoMateria = codigoMateria;
		}

		public boolean equals(Object obj) {
			if (obj instanceof AgendamentoId == false) {
				return false;
			}

			if (this == obj) {
				return true;
			}

			AgendamentoId id = (AgendamentoId) obj;

			return new EqualsBuilder().append(getCodigoCapitulo(), id.getCodigoCapitulo()).append(getCodigoMateria(),
					id.getCodigoMateria()).append(getObjetoIncidente(), id.getObjetoIncidente()).isEquals();
		}

		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(getCodigoCapitulo()).append(getCodigoMateria())
					.append(getObjetoIncidente()).toHashCode();
		}
	}

}
