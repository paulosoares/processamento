package br.gov.stf.estf.entidade.publicacao;

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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;

/**
 * Representa a Ata, Índice ou Pauta de julgamento. 
 * 
 * @author Rodrigo Barreiros
 * @since 08.06.2009
 */
@Entity
@Table(name = "ATA_JULGAMENTO", schema = "STF")
public class AtaJulgamento extends ESTFBaseEntity<AtaJulgamento.AtaJulgamentoId> {

	private static final long serialVersionUID = -2143237625483552831L;
	
	public static final int PLENO = 2;
	public static final int PRIMEIRA_TURMA = 3;
	public static final int SEGUNDA_TURMA = 4;
	
	public enum CategoriaAta {ATA, INDICE, PAUTA, ATAORDINARIA, ATAEXTRAORDINARIA, SESSAOVIRTUAL}
	
	public enum TipoAta {
		A("Ata de Julgamento"), AE("Ata Extraordinária"), AO("Ata Ordinária"), P("Pauta de Julgamento"), I("Índice"), AV("Sessão Virtual");

		private String descricao;

		TipoAta(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	};

	private ArquivoEletronico arquivoEletronico;

	@Id
	public AtaJulgamentoId getId() {
		return id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ARQUIVO_ELETRONICO")
	public ArquivoEletronico getArquivoEletronico() {
		return arquivoEletronico;
	}

	public void setArquivoEletronico(ArquivoEletronico arquivoEletronico) {
		this.arquivoEletronico = arquivoEletronico;
	}

	@Embeddable
	public static class AtaJulgamentoId implements Serializable {

		private static final long serialVersionUID = 7448791016916134787L;
		
		private Date dataSessao;
		private Integer codigoSessao;
		private TipoAta tipoAta;
		
		@Temporal(TemporalType.DATE)
		@Column(name="DAT_SESSAO")
		public Date getDataSessao() {
			return dataSessao;
		}

		public void setDataSessao(Date dataSessao) {
			this.dataSessao = dataSessao;
		}

		@Column(name="COD_CAPITULO")
		public Integer getCodigoSessao() {
			return codigoSessao;
		}

		public void setCodigoSessao(Integer codigoSessao) {
			this.codigoSessao = codigoSessao;
		}

		@Enumerated(EnumType.STRING)
		@Column(name="TIPO_ATA")
		public TipoAta getTipoAta() {
			return tipoAta;
		}

		public void setTipoAta(TipoAta tipoAta) {
			this.tipoAta = tipoAta;
		}
		
		public boolean equals(Object obj) {
			if (obj instanceof AtaJulgamentoId == false) {
				return false;
			}
			if (this == obj) {
				return true;
			}
			AtaJulgamentoId id = (AtaJulgamentoId) obj;
			return new EqualsBuilder().appendSuper(super.equals(obj))
				.append(getDataSessao(), id.getDataSessao())
				.append(getCodigoSessao(), id.getCodigoSessao())
				.append(getTipoAta(), id.getTipoAta()).isEquals();
		}
		
		public int hashCode() {
			return new HashCodeBuilder(17, 37)
				.append(getDataSessao())
				.append(getCodigoSessao())
				.append(getTipoAta()).toHashCode();
		}
	}

}
