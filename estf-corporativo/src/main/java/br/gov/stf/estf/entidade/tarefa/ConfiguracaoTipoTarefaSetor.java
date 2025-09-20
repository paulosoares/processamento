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
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table( schema="EGAB", name="CONFIGURACAO_TAREFA")
public class ConfiguracaoTipoTarefaSetor extends ESTFBaseEntity<Long> {
      
		private String sigla;
		private String descricao;
		
		public enum ConstanteConfiguracaoTipoTarefa{
			SITUACAO("SIT","Situação"),
			DATA_PREVISTA_INICIO("DPI","Data prevista início"),
			DATA_PREVISTA_FIM("DPF","Data prevista fim"),
			PRIORIDADE("PRI","Prioriodade"),
			USUARIO_ATRIBUIDO("UAT","Usuário atribuido"),
			PROCESSO_PROTOCOLO("PCP","Processo"),
			DESCRICAO("DSC","Descrição"),
			PROCESSO_PROTOCOLO_REPETIDO("PCR","Processo repertido");
			
			
			private String sigla;
			private String descricao;
			
			private ConstanteConfiguracaoTipoTarefa(String sigla,String descricao){
				this.sigla = sigla;
				this.descricao = descricao;
			}
			
			public String getSigla(){
				return this.sigla;
			}
			
			public String getDescricao(){
				return this.descricao;
			}
			
		}
		
		@Id
		@Column( name="SEQ_CONFIGURACAO_TAREFA" )
		@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
		@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_CONFIGURACAO_TAREFA", allocationSize = 1 )   
		public Long getId() {
			return id;
		}
		
		@Column(name="DSC_CONFIGURACAO_TAREFA", unique=false, nullable=false, insertable=true, updatable=true, length=20)
		public String getDescricao() {
			return descricao;
		}


		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
		@Column(name="SIG_CONFIGURACAO_TAREFA", unique=false, nullable=false, insertable=true, updatable=true, length=3)
		public String getSigla() {
			return sigla;
		}

		public void setSigla(String sigla) {
			this.sigla = sigla;
		}
								
}
