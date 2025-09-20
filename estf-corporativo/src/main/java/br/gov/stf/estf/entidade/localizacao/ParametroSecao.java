package br.gov.stf.estf.entidade.localizacao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table( schema="EGAB", name="PARAMETRO" )
public class ParametroSecao extends ESTFBaseEntity<Long> {
    private Boolean tarefa;
          private Boolean recebimentoAutomatico;
          private Boolean remessaAutomatica;
          private Boolean tarefaProcesso;
          private Boolean sala;
          private Boolean armario;
          private Boolean estante;
          private Boolean prateleira;
          private Boolean coluna;
          
          @Id
          @Column( name="SEQ_PARAMETRO" )
          @GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
          @SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_PARAMETRO", allocationSize = 1 ) 
          public Long getId() {
                  return id;
          }               
          
          @Column( name="FLG_RECEBE_AUTO" )
          @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" ) 
          public Boolean getRecebimentoAutomatico() {
                  return recebimentoAutomatico;
          }
          
          public void setRecebimentoAutomatico(Boolean recebimentoAutomatico) {
                  this.recebimentoAutomatico = recebimentoAutomatico;
          }
          
          @Column( name="FLG_REMESSA_AUTO" )
          @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" ) 
          public Boolean getRemessaAutomatica() {
                  return remessaAutomatica;
          }
          
          public void setRemessaAutomatica(Boolean remessaAutomatica) {
                  this.remessaAutomatica = remessaAutomatica;
          }
          
          @Column( name="FLG_TAREFA" )
          @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" ) 
          public Boolean getTarefa() {
              return tarefa;
          }
          
          public void setTarefa(Boolean tarefa) {
              this.tarefa = tarefa;
          }

          @Column( name="FLG_TAREFA_PROCESSO" )
          @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" ) 
          public Boolean getTarefaProcesso() {
              return tarefaProcesso;
          }
          
          public void setTarefaProcesso(Boolean tarefaProcesso) {
              this.tarefaProcesso = tarefaProcesso;
          }

          @Column( name="FLG_SALA" )
          @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" ) 
          public Boolean getSala() {
              return sala;
          }
          
          public void setSala(Boolean sala) {
              this.sala = sala;
          }

          @Column( name="FLG_ARMARIO" )
          @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" ) 
          public Boolean getArmario() {
              return armario;
          }
      
          public void setArmario(Boolean armario) {
              this.armario = armario;
          }

          @Column( name="FLG_ESTANTE" )
          @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" ) 
          public Boolean getEstante() {
              return estante;
          }
          
          public void setEstante(Boolean estante) {
              this.estante = estante;
          }

          @Column( name="FLG_PRATELEIRA" )
          @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" ) 
          public Boolean getPrateleira() {
              return prateleira;
          }
      
          public void setPrateleira(Boolean prateleira) {
              this.prateleira = prateleira;
          }

          @Column( name="FLG_COLUNA" )
          @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" ) 
          public Boolean getColuna() {
              return coluna;
          }
          public void setColuna(Boolean coluna) {
              this.coluna = coluna;
          }
}
