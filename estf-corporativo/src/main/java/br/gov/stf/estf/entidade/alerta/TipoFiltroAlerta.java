package br.gov.stf.estf.entidade.alerta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table( schema="EGAB", name="TIPO_FILTRO_ALERTA")
public class TipoFiltroAlerta extends ESTFBaseEntity<Long> {
      	
		private String sigla;
		private String descricao;
		
		public enum TipoFiltro {
			
			CLASSE_PROCESSUAL("SIGLA"),
			PROCESSO("PROC"),
			PROTOCOLO("PROT"),
			PROCESSOS_PROTOCOLOS_DISTRIBUIDOS_PARA_MIM("DIST"),
			PROCESSOS_PROTOCOLOS_DISTRIBUIDOS_PARA_MEUS_GRUPOS("DIST-GRUPO"),
			PROCESSOS_PROTOCOLOS_DA_MINHA_SECAO("SECAO"),
			PROCESSOS_PROTOCOLOS_DO_MEU_SETOR("SETOR"), 
			PROCESSOS_PROTOCOLOS_ELETRONICOS_DO_SETOR("SETOR-E"),
			PROCESSOS_PROTOCOLOS_COM_TAREFA("TAREFA"),
			PROCESSOS_PROTOCOLOS_COM_TAREFA_PARA_MIM("TARE-U"),
			PROCESSOS_PROTOCOLOS_DO_RELATOR("RELATOR"),
			PROCESSOS_PROTOCOLOS_ELETRONICOS_DO_RELATOR("RELATOR-E"),
			PROCESSOS_PROTOCOLOS_DA_TURMA("TURMA"),
			PROCESSOS_PROTOCOLOS_ELETRONICOS_DA_TURMA("TURMA-E"),
			PROCESSOS_ELETRONICOS("EPROC"),
			PROCESSOS_COM_LIMINAR("PROC-L"),
			PROCESSOS_DESLOCADOS_PGR("PROC-PGR"),
			PROCESSOS_EM_JULGAMENTO_VIRTUAL("JULG-V");
			
			private String sigla;

			private TipoFiltro(String sigla) {
				this.sigla = sigla;
			}

			public String getSigla() {
				return this.sigla;
			}
		}
		
		@Id
		@Column( name="SEQ_TIPO_FILTRO_ALERTA" )
		public Long getId() {
			return id;
		}
		
		@Column( name="SIG_TIPO_FILTRO_ALERTA" )
		public String getSigla() {
			return sigla;
		}
		
		public void setSigla(String sigla){
			this.sigla = sigla;
		}
		
		@Column(name="DSC_TIPO_FILTRO_ALERTA", unique=false, nullable=false, insertable=true, updatable=true, length=50)
		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
}
