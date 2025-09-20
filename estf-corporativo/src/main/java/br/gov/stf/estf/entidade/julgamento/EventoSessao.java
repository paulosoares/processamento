package br.gov.stf.estf.entidade.julgamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table( name="EVENTO_SESSAO", schema="JULGAMENTO" )
public class EventoSessao extends ESTFBaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5422493857595692058L;
	private String descricao;
	private String tipoOcorrenciaEvento;
	private TipoEvento tipoEvento;
	private Sessao sessao;
	
	public enum TipoOcorrenciaEventoConstante {
		INICIO("Início", "I"),
		FIM("Fim", "F");
				
		private String descricao;
		private String sigla;
		
		private TipoOcorrenciaEventoConstante( String descricao, String sigla ) {
			this.descricao = descricao;
			this.sigla = sigla;
		}

		public String getDescricao() {
			return descricao;
		}
		public String getSigla() {
			return sigla;
		}
		
	}	
	
	
	
	@Id
	@Column( name="SEQ_EVENTO_SESSAO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_EVENTO_SESSAO", allocationSize=1 )	
	public Long getId() {
		return id;
	}
	
	
	@Column( name="DSC_EVENTO_SESSAO", nullable=false, updatable=true, insertable=true, unique=false )
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column( name="TIP_OCORRENCIA_EVENTO", nullable=false, updatable=true, insertable=true, unique=false, length=1 )	
	public String getTipoOcorrenciaEvento() {
		return tipoOcorrenciaEvento;
	}
	public void setTipoOcorrenciaEvento(String tipoOcorrenciaEvento) {
		this.tipoOcorrenciaEvento = tipoOcorrenciaEvento;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn( name="COD_TIPO_EVENTO", unique=false, nullable=false, insertable=true, updatable=true )
	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}
	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn( name="SEQ_SESSAO", unique=false, nullable=false, insertable=true, updatable=true )
	public Sessao getSessao() {
		return sessao;
	}
	public void setSessao(Sessao sessao) {
		this.sessao = sessao;
	}
	
	
	@Transient
	public String getTipoOcorrenciaEventoDescricao() {
		if( getTipoOcorrenciaEvento().equals(EventoSessao.TipoOcorrenciaEventoConstante.INICIO.getSigla()) )
				return EventoSessao.TipoOcorrenciaEventoConstante.INICIO.getDescricao();
		else if( getTipoOcorrenciaEvento().equals(EventoSessao.TipoOcorrenciaEventoConstante.FIM.getSigla()) )
			return EventoSessao.TipoOcorrenciaEventoConstante.FIM.getDescricao();
		else return null;
	}

}
