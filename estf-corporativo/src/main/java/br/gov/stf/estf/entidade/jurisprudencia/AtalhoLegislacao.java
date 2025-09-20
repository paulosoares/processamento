/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

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

/**
 * @author Paulo.Estevao
 * @since 10.08/2012
 */
@Entity
@Table(name = "LEGISLACOES", schema = "SJUR")
public class AtalhoLegislacao extends ESTFBaseEntity<AtalhoLegislacao.AtalhoLegislacaoId> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2509089447943926913L;
	
	private AtalhoLegislacao.AtalhoLegislacaoId id;
	private TipoLegislacao tipoNorma;
	private TipoLegislacao tipoLegislacao;
	private String numero;
	private TipoEscopoLegislacao tipoEscopoLegislacao;
	
	@Override
	@Id
	public AtalhoLegislacaoId getId() {
		return id;
	}
	
	@Override
	public void setId(AtalhoLegislacaoId id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SIG_NORMA", referencedColumnName = "SIG_LEGIS")
	public TipoLegislacao getTipoNorma() {
		return tipoNorma;
	}
	
	public void setTipoNorma(TipoLegislacao tipoNorma) {
		this.tipoNorma = tipoNorma;
	}
	
	@Column(name = "COD_LEGIS")
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TIPO_ESCOPO_LEGISLACAO")
	public TipoEscopoLegislacao getTipoEscopoLegislacao() {
		return tipoEscopoLegislacao;
	}
	
	public void setTipoEscopoLegislacao(TipoEscopoLegislacao tipoEscopoLegislacao) {
		this.tipoEscopoLegislacao = tipoEscopoLegislacao;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SIG_LEGIS", referencedColumnName = "SIG_LEGIS", insertable = false, updatable = false)
	public TipoLegislacao getTipoLegislacao() {
		return tipoLegislacao;
	}
	
	public void setTipoLegislacao(TipoLegislacao tipoLegislacao) {
		this.tipoLegislacao = tipoLegislacao;
	}
	
	@Embeddable
	public static class AtalhoLegislacaoId implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1581786649718860789L;
		
		private String sigla;
		private Long ano;
		
		@Column(name = "SIG_LEGIS")
		public String getSigla() {
			return sigla;
		}
		
		public void setSigla(String sigla) {
			this.sigla = sigla;
		}
		
		@Column(name = "ANO_LEGIS")
		public Long getAno() {
			return ano;
		}
		
		public void setAno(Long ano) {
			this.ano = ano;
		}
		
	}

}
