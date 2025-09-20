package br.gov.stf.estf.entidade.julgamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="TIPO_OCORRENCIA",schema="JULGAMENTO")
public class TipoOcorrencia extends ESTFBaseEntity<Long> {
	
	/**
	 * 
	 */
	private String descricao;
	
	public enum TipoOcorrenciaConstante{
		JULGAMENTO_LEADING_CASE((long)1),
		PROCESSO_RELACIONADO((long)2),
		REAFIRMADA_JURISPRUDENCIA((long)3),
		SUBSTITUIDO_LEADING_CASE((long)4),
		MERITO_JULGADO((long)5),
		PROCESSO_RELACIONADO_A_TEMA_PARA_DEVOLUCAO((long)6),
		PROCESSO_RELACIONADO_POR_CONTROVERSIA((long)7),
		LEADING_CASE_ASSOCIADO((long)8);

		private Long codigo;

		private TipoOcorrenciaConstante(Long codigo){
			this.codigo = codigo;
		}

		public Long getCodigo(){
			return this.codigo;
		}
	}
	
	@Id
	@Column( name="COD_TIPO_OCORRENCIA" , unique=true, nullable=false, insertable=true, updatable=true )
	public Long getId() {
		return id;
	}
	
	@Column( name="DSC_TIPO_OCORRENCIA" , unique=true, nullable=false, insertable=true, updatable=true )
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}
