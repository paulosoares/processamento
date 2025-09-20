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
import br.gov.stf.estf.entidade.ministro.Ministro;

/**
 * @author Rafael.Dias
 * @since 23.10.2012
 */
@Entity
@Table(schema = "JURISPRUDENCIA", name = "MINISTRO_INDEXACAO")
public class MinistroIndexacao extends ESTFBaseEntity<MinistroIndexacao.MinistroIndexacaoId> {

	private static final long serialVersionUID = -2163794172281735415L;
	
	private MinistroIndexacao.MinistroIndexacaoId id;
	private Long numOrdemExibicao;
	
	@Override
	@Id
	public MinistroIndexacaoId getId() {
		return id;
	}
	
	@Override
	public void setId(MinistroIndexacaoId id) {
		this.id = id;
	}
	
	@Column(name = "NUM_ORDEM_EXIBICAO")
	public Long getNumOrdemExibicao() {
		return numOrdemExibicao;
	}
	
	public void setNumOrdemExibicao(Long numOrdemExibicao) {
		this.numOrdemExibicao = numOrdemExibicao;
	}
	
	@Embeddable
	public static class MinistroIndexacaoId implements Serializable {

		private static final long serialVersionUID = -5498841104852895982L;
		
		private IndexacaoIncidenteAnalise indexacaoIncidenteAnalise;
		private Ministro ministro;
		
		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name="SEQ_INDEXACAO_INCI_ANALISE")
		public IndexacaoIncidenteAnalise getIndexacaoIncidenteAnalise() {
			return indexacaoIncidenteAnalise;
		}
		
		public void setIndexacaoIncidenteAnalise(IndexacaoIncidenteAnalise indexacaoIncidenteAnalise) {
			this.indexacaoIncidenteAnalise = indexacaoIncidenteAnalise;
		}
		
		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name = "COD_MINISTRO")
		public Ministro getMinistro() {
			return ministro;
		}
		
		public void setMinistro(Ministro ministro) {
			this.ministro = ministro;
		}
	}

}
