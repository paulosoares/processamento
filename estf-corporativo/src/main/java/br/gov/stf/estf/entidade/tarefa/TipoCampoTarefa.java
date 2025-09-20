package br.gov.stf.estf.entidade.tarefa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(schema="EGAB", name="TIPO_CAMPO_TAREFA")
public class TipoCampoTarefa extends ESTFBaseEntity<Long>{

    private String descricao;
    
    private TipoTarefaSetor tipoTarefaSetor;
    private ClassificacaoTipoCampoTarefa tipo;
    private Integer ordem;
    
    private Boolean obrigatorio;
    private Boolean ativo;
    
    private Set<TipoCampoTarefaValor> camposTarefaValor = new HashSet<TipoCampoTarefaValor>();
    
    public enum TipoCampoTarefaContante{
		
    	VOTO_SOBRE_REPERCUSSAO_GERAL((long)1,"Repercussão Geral"),
		RECESSO((long)2,"Recesso"),
		DIA_INICIO_VOTACAO((long)3,"Dia inicio votação"),
    	DATA_INICIO_CONTAGEM((long)4,"Data inicio contagem");
		
		private Long codigo;
		private String descricao;
		
		private TipoCampoTarefaContante(Long codigo,String descricao){
			this.codigo = codigo;
			this.descricao = descricao;
		}
		
		public Long getCodigo(){
			return this.codigo;
		}
		
		public String getDescricao(){
			return this.descricao;
		}
		
	}
    
    
    
    @Id
	@Column( name="SEQ_TIPO_CAMPO_TAREFA" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_TIPO_CAMPO_TAREFA", allocationSize = 1 ) 	 
	public Long getId() {
		return id;
	}
    
    @Column(name="DSC_TIPO_CAMPO_TAREFA", unique=false, nullable=false, insertable=true, updatable=true, length=1000)
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="SEQ_TIPO_TAREFA", unique=false, nullable=true, insertable=true, updatable=true)
	public TipoTarefaSetor getTipoTarefaSetor() {
		return tipoTarefaSetor;
	}

	public void setTipoTarefaSetor(TipoTarefaSetor tipoTarefaSetor) {
		this.tipoTarefaSetor = tipoTarefaSetor;
	}
	
	@Enumerated(value=EnumType.STRING)
    @Column(name="TIP_CAMPO_TAREFA", nullable=false)   
	public ClassificacaoTipoCampoTarefa getTipo() {
		return tipo;
	}

	public void setTipo(ClassificacaoTipoCampoTarefa tipo) {
		this.tipo = tipo;
	}
	
	@Column( name="FLG_ATIVO" )
    @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" )
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Column( name="FLG_OBRIGATORIO" )
    @org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" )
	public Boolean getObrigatorio() {
		return obrigatorio;
	}

	public void setObrigatorio(Boolean obrigatorio){
		this.obrigatorio = obrigatorio;
	}
	
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="tipoCampoTarefa")
	@org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
	public Set<TipoCampoTarefaValor> getCamposTarefaValor() {
		return camposTarefaValor;
	}

	public void setCamposTarefaValor(Set<TipoCampoTarefaValor> camposTarefaValor) {
		this.camposTarefaValor = camposTarefaValor;
	}

	@Column(name="NUM_ORDEM_CAMPO", unique=false, nullable=true, insertable=true, updatable=true, length=10)
	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
}
