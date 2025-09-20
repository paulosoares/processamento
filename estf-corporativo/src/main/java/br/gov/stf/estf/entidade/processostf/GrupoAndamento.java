package br.gov.stf.estf.entidade.processostf;

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

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table( schema="STF", name="GRUPO_ANDAMENTO" )
public class GrupoAndamento extends ESTFBaseEntity<Long> {
	
	private static final long serialVersionUID = 1L;
	private String descricao;
	private GrupoAndamento grupoAndamentoPai;
	
	@Id
	@Column( name="SEQ_GRUPO_ANDAMENTO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="STF.SEQ_GRUPO_ANDAMENTO", allocationSize = 1 )	
	public Long getId() {
		return id;
	}	   
	
	@Column( name="DSC_GRUPO" )
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="SEQ_PAI_GRUPO_ANDAMENTO", unique=false, nullable=true, insertable=true, updatable=true)
	public GrupoAndamento getGrupoAndamentoPai() {
		return grupoAndamentoPai;
	}

	public void setGrupoAndamentoPai(GrupoAndamento grupoAndamentoPai) {
		this.grupoAndamentoPai = grupoAndamentoPai;
	}
	public enum GruposAndamento {
		VISTA_JULGAMENTO("Vista Julgamento", 53), 
		DECISAO_FINAL("Decisão Final", 27), 
		DEVOLUCAO_DE_PROCESSO_COM_VISTA("Devolução de Processo com Vista", 9);

		private String descricao;
		private long codigo;

		GruposAndamento(String descricao, long codigo) {
			this.descricao = descricao;
			this.codigo = codigo;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}

		public long getCodigo() {
			return codigo;
		}

		public void setCodigo(long codigo) {
			this.codigo = codigo;
		}
	}
	
}


