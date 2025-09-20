package br.gov.stf.estf.entidade.documento;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name = "NUMERO_COMUNICACAO", schema = "JUDICIARIO")
public class NumeroComunicacao extends ESTFBaseEntity<NumeroComunicacao.NumeroComunicacaoId> {

	private static final long serialVersionUID = -5871645711681029675L;

	private NumeroComunicacao.NumeroComunicacaoId id;

	@Override
	@Id
	public NumeroComunicacaoId getId() {
		return id;
	}
	
	public void setId(NumeroComunicacaoId id) {
		this.id = id;
	}

	@Embeddable
	public static class NumeroComunicacaoId implements Serializable {

		private static final long serialVersionUID = -5498841104852895982L;
		
		private TipoComunicacao tipoComunicacao;
		private Long numeroComunicacao;
		private Integer anoComunicacao;
		
		@Column(name = "SEQ_TIPO_COMUNICACAO")
		public TipoComunicacao getTipoComunicacao() {
			return tipoComunicacao;
		}
		
		public void setTipoComunicacao(TipoComunicacao tipoComunicacao) {
			this.tipoComunicacao = tipoComunicacao;
		}
		
		@Column(name = "NUM_COMUNICACAO")
		public Long getNumeroComunicacao() {
			return numeroComunicacao;
		}
		public void setNumeroComunicacao(Long numeroComunicacao) {
			this.numeroComunicacao = numeroComunicacao;
		}
		
		@Column(name = "NUM_ANO_COMUNICACAO")
		public Integer getAnoComunicacao() {
			return anoComunicacao;
		}
		public void setAnoComunicacao(Integer anoComunicacao) {
			this.anoComunicacao = anoComunicacao;
		}
		
	}
}
