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

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;


@Entity
@Table(name = "TEXTO_ASSOCIADO_PROTOCOLO", schema = "STF")
public class TextoAssociadoProtocolo extends ESTFBaseEntity<TextoAssociadoProtocolo.TextoAssociadoProtocoloId> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 721626654599389910L;
	private ProtocoloPublicado protocoloPublicado;
	private ArquivoEletronico arquivoEletronico;

	@Id
	public TextoAssociadoProtocoloId getId() {
		return this.id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_PROTOCOLO_PUBLICADO", unique = false, nullable = false, insertable = false, updatable = false)
	public ProtocoloPublicado getProtocoloPublicado() {
		return this.protocoloPublicado;
	}

	public void setProtocoloPublicado(ProtocoloPublicado protocoloPublicado) {
		this.protocoloPublicado = protocoloPublicado;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ARQUIVO_ELETRONICO", unique = false, nullable = false, insertable = false, updatable = false)
	public ArquivoEletronico getArquivoEletronico() {
		return this.arquivoEletronico;
	}

	public void setArquivoEletronico(ArquivoEletronico arquivoTexto) {
		this.arquivoEletronico = arquivoTexto;
	}	

	@Embeddable
	public static class TextoAssociadoProtocoloId implements Serializable {

		private static final long serialVersionUID = 849794220710356704L;
		private Long protocoloPublicado;
		private Long arquivoEletronico;
		private String tipoAssociacao;

		@Column(name = "SEQ_PROTOCOLO_PUBLICADO", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
		public Long getProtocoloPublicado() {
			return this.protocoloPublicado;
		}

		public void setProtocoloPublicado(Long protocoloPublicado) {
			this.protocoloPublicado = protocoloPublicado;
		}

		@Column(name = "SEQ_ARQUIVO_ELETRONICO", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
		public Long getArquivoEletronico() {
			return this.arquivoEletronico;
		}

		public void setArquivoEletronico(Long arquivoEletronico) {
			this.arquivoEletronico = arquivoEletronico;
		}
		
		@Column(name = "TIP_ASSOCIACAO", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
		public String getTipoAssociacao() {
			return this.tipoAssociacao;
		}

		public void setTipoAssociacao(String tipoAssociacao) {
			this.tipoAssociacao = tipoAssociacao;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((arquivoEletronico == null) ? 0 : arquivoEletronico
							.hashCode());
			result = prime
					* result
					+ ((protocoloPublicado == null) ? 0 : protocoloPublicado
							.hashCode());
			result = prime
					* result
					+ ((tipoAssociacao == null) ? 0 : tipoAssociacao.hashCode());
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
			final TextoAssociadoProtocoloId other = (TextoAssociadoProtocoloId) obj;
			if (arquivoEletronico == null) {
				if (other.arquivoEletronico != null)
					return false;
			} else if (!arquivoEletronico.equals(other.arquivoEletronico))
				return false;
			if (protocoloPublicado == null) {
				if (other.protocoloPublicado != null)
					return false;
			} else if (!protocoloPublicado.equals(other.protocoloPublicado))
				return false;
			if (tipoAssociacao == null) {
				if (other.tipoAssociacao != null)
					return false;
			} else if (!tipoAssociacao.equals(other.tipoAssociacao))
				return false;
			return true;
		}

		

		

	}
	
	public enum TipoAssociacao {
		R("R","Republicação"),
		O("O","Observação"),
		P("P","Publicação");
		
		private String sigla;
		private String descricao;
		
		private TipoAssociacao(String sigla, String descricao) {
			this.sigla = sigla;
			this.descricao = descricao;
		}
		public String getSigla() {
			return sigla;
		}
		public void setSigla(String sigla) {
			this.sigla = sigla;
		}
		public String getDescricao() {
			return descricao;
		}
		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
	}

}
