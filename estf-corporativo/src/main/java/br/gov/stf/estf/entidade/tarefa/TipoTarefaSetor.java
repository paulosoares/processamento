package br.gov.stf.estf.entidade.tarefa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OrderBy;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.tarefa.ConfiguracaoTipoTarefaSetor.ConstanteConfiguracaoTipoTarefa;


@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
@Table( schema="EGAB", name="TIPO_TAREFA")
public class TipoTarefaSetor extends ESTFBaseEntity<Long> {

	private String descricao;
	private Boolean ativo;
	private Setor setor;

	private Set<TipoCampoTarefa> tiposCampoTarefa = new HashSet<TipoCampoTarefa>();
	private Set<ConfiguracaoTipoTarefaSetor> configuracoesTipoTarefaSetor = new HashSet<ConfiguracaoTipoTarefaSetor>();

	public enum TipoTarefa{
		REPERCUSSAO_GERAL((long)1,"Repercussão Geral"),
		AGENDA((long)2,"Agenda"),
		CONTROLAR_CONTATO((long)3,"Controle de contato");

		private Long codigo;
		private String descricao;

		private TipoTarefa(Long codigo,String descricao){
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
	@Column( name="SEQ_TIPO_TAREFA" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="EGAB.SEQ_TIPO_TAREFA", allocationSize = 1 )   
	public Long getId() {
		return id;
	}

	@Column(name="DSC_TIPO_TAREFA", unique=false, nullable=false, insertable=true, updatable=true, length=50)
	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Column( name="FLG_ATIVO" )
	@org.hibernate.annotations.Type( type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType" )
	public Boolean getAtivo() {
		return ativo;
	}


	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="tipoTarefaSetor")
	@org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
	@OrderBy(clause = "NUM_ORDEM_CAMPO")
	public Set<TipoCampoTarefa> getTiposCampoTarefa() {
		return tiposCampoTarefa;
	}

	public void setTiposCampoTarefa(Set<TipoCampoTarefa> tiposCampoTarefa) {
		this.tiposCampoTarefa = tiposCampoTarefa;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="COD_SETOR", unique=false, nullable=true, insertable=true, updatable=true)
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	@ManyToMany(cascade = {}, fetch = FetchType.LAZY)
	@JoinTable(
			schema = "EGAB",
			name = "CONFIGURACAO_TIPO_TAREFA",
			joinColumns = {@JoinColumn(name = "SEQ_TIPO_TAREFA")},
			inverseJoinColumns = {@JoinColumn(name = "SEQ_CONFIGURACAO_TAREFA")}
	)
	public Set<ConfiguracaoTipoTarefaSetor> getConfiguracoesTipoTarefaSetor() {
		return configuracoesTipoTarefaSetor;
	}

	public void setConfiguracoesTipoTarefaSetor(
			Set<ConfiguracaoTipoTarefaSetor> configuracoesTipoTarefaSetor) {
		this.configuracoesTipoTarefaSetor = configuracoesTipoTarefaSetor;
	}
	@Transient
	public boolean getPossuiConfiguracaoTipoTarefa(ConstanteConfiguracaoTipoTarefa conf){
		if( configuracoesTipoTarefaSetor != null && configuracoesTipoTarefaSetor.size() > 0 ){
			if( conf != null ){
				for(ConfiguracaoTipoTarefaSetor conf2:configuracoesTipoTarefaSetor){
					if(conf2.getSigla().equals(conf.getSigla())){
						return true;
					}
				}
			}
		}
		return false;
	}










}
