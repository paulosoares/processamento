package br.gov.stf.estf.entidade.tarefa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
@Entity
@Table(schema="EGAB", name="TIPO_ATRIBUICAO")
public class TipoAtribuicaoTarefa extends ESTFBaseEntity<Long>{

	private String sigla;
	private String descricao;

	public enum TipoAtribuicao{
		GESTOR("GE","Gestor"),
		EXECUTOR("EX","Executor"),
		INTERESSADO("IN","Interessado");

		private String sigla;
		private String descricao;

		private TipoAtribuicao(String sigla,String descricao){
			this.sigla = sigla;
			this.descricao = descricao;
		}

		public String getDescricao(){
			return descricao;
		}

		public String getSigla(){
			return sigla;
		}
	}

	@Id
	@Column( name="SEQ_TIPO_ATRIBUICAO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_TIPO_ATRIBUICAO", allocationSize = 1 ) 	 
	public Long getId() {
		return id;
	}
	@Column(name="SIG_ATRIBUICAO", unique=true, nullable=false, insertable=true, updatable=true, length=2)
	public String getSigla() {
		return sigla;
	}
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	@Column(name="DSC_ATRIBUICAO", unique=true, nullable=false, insertable=true, updatable=true, length=20)
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
