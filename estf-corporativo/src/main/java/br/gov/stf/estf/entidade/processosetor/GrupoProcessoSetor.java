package br.gov.stf.estf.entidade.processosetor;

import java.util.LinkedList;
import java.util.List;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.localizacao.Setor;

@Entity
@Table(schema = "EGAB", name = "GRUPO_PROCESSO_SETOR")
public class GrupoProcessoSetor extends ESTFBaseEntity<Long> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7555715244204947325L;
	private String nomeGrupo;
	private String observacoes;
	private Boolean ativo;
	private Setor setor;
	
	private List<ProcessoSetor> processosSetor = new LinkedList<ProcessoSetor>();
//	private List<ObjetoIncidente<?>> objetoIncidente = new LinkedList<ObjetoIncidente<?>>();
	
	@Id
	@Column(name = "SEQ_GRUPO_PROCESSO_SETOR")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "EGAB.SEQ_GRUPO_PROCESSO_SETOR", allocationSize = 1)	
	public Long getId() {
		return id;
	}
	
	@Column(name = "FLG_ATIVO", unique = false, nullable = false, insertable = true, updatable = true)
    @Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Column(name="NOM_GRUPO_PROCESSO_SETOR", unique = true, nullable = false, insertable = true, updatable = true, 
			length = 80)
	public String getNomeGrupo() {
		return nomeGrupo;
	}

	public void setNomeGrupo(String nomeGrupo) {
		this.nomeGrupo = nomeGrupo;
	}
	
	@Column(name="DSC_OBSERVACAO", unique = false, nullable = true, insertable = true, updatable = true, 
			length = 250)
	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
	
	@ManyToMany(
			targetEntity = br.gov.stf.estf.entidade.processosetor.ProcessoSetor.class,
			cascade = {},
			fetch = FetchType.LAZY
	)
	@JoinTable(
			schema = "EGAB",
			name = "PROCESSO_SETOR_GRUPO",
			joinColumns = {@JoinColumn(name = "SEQ_GRUPO_PROCESSO_SETOR")},
			inverseJoinColumns = {@JoinColumn(name = "SEQ_PROCESSO_SETOR")}
	)	
	public List<ProcessoSetor> getProcessosSetor() {
		return processosSetor;
	}

	public void setProcessosSetor(List<ProcessoSetor> processosSetor) {
		this.processosSetor = processosSetor;
	}
	
//	@ManyToMany(
//	targetEntity = br.gov.stf.estf.entidade.processosetor.ProcessoSetor.class,
//	cascade = {},
//	fetch = FetchType.LAZY
//	)
//	@JoinTable(
//		schema = "EGAB",
//		name = "PROCESSO_SETOR_GRUPO",
//		joinColumns = {@JoinColumn(name = "SEQ_GRUPO_PROCESSO_SETOR")},
//		inverseJoinColumns = {@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")}
//	)	
//	public List<ObjetoIncidente<?>> getObjetoIncidente() {
//		return objetoIncidente;
//	}
//	
//	public void setObjetoIncidente(List<ObjetoIncidente<?>> objetoIncidente) {
//		this.objetoIncidente = objetoIncidente;
//	}	
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
    @JoinColumn(name="COD_SETOR", unique=false, nullable=false, insertable=true, updatable=true)
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}	    
}
