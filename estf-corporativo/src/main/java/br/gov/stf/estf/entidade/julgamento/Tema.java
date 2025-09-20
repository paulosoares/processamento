package br.gov.stf.estf.entidade.julgamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.OrderBy;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.julgamento.TipoOcorrencia.TipoOcorrenciaConstante;
import br.gov.stf.estf.entidade.processostf.Assunto;
import br.gov.stf.framework.model.entity.BaseEntity;

@Entity
@Table(name = "TEMA", schema = "JUDICIARIO")
public class Tema extends ESTFBaseEntity<Long>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4635671530826327574L;
	private String descricao;
	private TipoTema tipoTema;
	private List<Assunto> assuntos;
	private List<ProcessoTema> processosTema;
	private String tituloTema;
	private Long numeroSequenciaTema;
	private Tema temaPrecedente;
	private String observacao;
	private String situacaoTema;
	private Date dataSituacaoTema;
	private String tese;	
	private List<SuspensaoNacionalTema> historicoSuspensaoNacional;
	
	
	@Id
	@Column( name="SEQ_TEMA" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JUDICIARIO.SEQ_TEMA", allocationSize=1 )	
	public Long getId() {
		return id;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_TEMA_PRECEDENTE", unique = false, nullable = true, insertable = true, updatable = true)
	public Tema getTemaPrecedente() {
		return temaPrecedente;
	}

	public void setTemaPrecedente(Tema temaPrecedente) {
		this.temaPrecedente = temaPrecedente;
	}	
	
	@Column( name="DSC_TEMA" ,unique=false, nullable=true, insertable=true, updatable=true)
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Column( name="TXT_OBSERVACAO" ,unique=false, nullable=true, insertable=true, updatable=true)
	public String getObservacao() {
		return observacao;
	}
	
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	@Column( name="NOM_TEMA" ,unique=false, nullable=true, insertable=true, updatable=true)
	public String getTituloTema() {
		return tituloTema;
	}
	
	public void setTituloTema(String tituloTema) {
		this.tituloTema = tituloTema;
	}
	
	
	@Column( name="TIP_SITUACAO" ,unique=false, nullable=true, insertable=true, updatable=true)
	public String getSituacaoTema() {
		return situacaoTema;
	}

	public void setSituacaoTema(String situacaoTema) {
		this.situacaoTema = situacaoTema;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DAT_ATUALIZACAO_SITUACAO")
	public Date getDataSituacaoTema() {
		return dataSituacaoTema;
	}

	public void setDataSituacaoTema(Date dataSituacaoTema) {
		this.dataSituacaoTema = dataSituacaoTema;
	}

	@Column( name="NUM_TEMA" ,unique=false, nullable=true, insertable=true, updatable=true)	
	public Long getNumeroSequenciaTema() {
		return numeroSequenciaTema;
	}

	public void setNumeroSequenciaTema(Long numeroSequenciaTema) {
		this.numeroSequenciaTema = numeroSequenciaTema;
	}
	
	@Column( name="DSC_TESE" ,unique=false, nullable=true, insertable=true, updatable=true)	
	public String getTese() {
		return tese;
	}

	public void setTese(String tese) {
		this.tese = tese;
	}			

	
	@ManyToOne(cascade={CascadeType.PERSIST}, fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TIPO_TEMA", unique=false, nullable=false, insertable=true, updatable=true)
	public TipoTema getTipoTema() {
		return tipoTema;
	}
	
	public void setTipoTema(TipoTema tipoTema) {
		this.tipoTema = tipoTema;
	}

	
	@ManyToMany(cascade = {}, fetch = FetchType.LAZY)
	@JoinTable(
			schema = "JUDICIARIO",
			name = "ASSUNTO_TEMA",
			joinColumns= {@JoinColumn(name = "SEQ_TEMA",referencedColumnName = "SEQ_TEMA")},
			inverseJoinColumns = {@JoinColumn(name = "COD_ASSUNTO")}
	)
	public List<Assunto> getAssuntos() {
		return assuntos;
	}

	public void setAssuntos(List<Assunto> assuntos) {
		this.assuntos = assuntos;
	}
	 

	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="tema")
	@org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.ALL )
    public List<ProcessoTema> getProcessosTema() {
		return processosTema;
	}
	public void setProcessosTema(List<ProcessoTema> processosTema) {
		this.processosTema = processosTema;
	}
	
	
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="tema")
	@org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN )
	@OrderBy(clause = "DAT_INICIAL_SUSPENSAO DESC")    
	public List<SuspensaoNacionalTema> getHistoricoSuspensaoNacional() {
		if (historicoSuspensaoNacional == null)
			historicoSuspensaoNacional = new ArrayList<SuspensaoNacionalTema>();
		return historicoSuspensaoNacional;
	}

	public void setHistoricoSuspensaoNacional(
			List<SuspensaoNacionalTema> historicoSuspensaoNacional) {
		this.historicoSuspensaoNacional = historicoSuspensaoNacional;
	}
	
	@Transient
	public SuspensaoNacionalTema getSuspensaoNacionalAtual(){
		if (historicoSuspensaoNacional != null){			
			for (SuspensaoNacionalTema suspensao : historicoSuspensaoNacional){
				if (suspensao.getDataInicial() != null && suspensao.getDataFinal() == null && 
						(suspensao.getAndamentoProcesso() == null ||
						(suspensao.getAndamentoProcesso() != null && !suspensao.getAndamentoProcesso().getLancamentoIndevido())))
					return suspensao;
			}
		}
		return null;
	}
	
	@Transient
	public SuspensaoNacionalTema getSuspensaoNacionalMaisRecente(){
		SuspensaoNacionalTema retorno = null;
		if (historicoSuspensaoNacional != null)
			for (SuspensaoNacionalTema suspensao : historicoSuspensaoNacional){
				if (suspensao.getAndamentoProcesso() != null && suspensao.getAndamentoProcesso().getLancamentoIndevido())
					continue;
				if (retorno == null){
					retorno = suspensao;
					continue;
				}
				if (retorno.getDataInicial().after(suspensao.getDataInicial()))
					continue;
				retorno = suspensao;
			}
		return retorno;
	
	}

	@Transient
	public ProcessoTema getProcessoTemaLeadingCase(){
		if( getProcessosTema() != null && getProcessosTema().size() > 0 ){
			for( ProcessoTema proc : getProcessosTema() ){
				if( proc.getTipoOcorrencia() != null && 
						proc.getTipoOcorrencia().getId().equals(TipoOcorrenciaConstante.JULGAMENTO_LEADING_CASE.getCodigo())){
					return proc;
				}
			}
		}
		return null;
	}
	
	@Transient
	public List<ProcessoTema> getProcessoRelacionados(){
		List<ProcessoTema> lista = new LinkedList<ProcessoTema>();
		if( getProcessosTema() != null && getProcessosTema().size() > 0 ){
			for( ProcessoTema proc : getProcessosTema() ){
				if( proc.getTipoOcorrencia() != null && 
						(proc.getTipoOcorrencia().getId().equals(TipoOcorrenciaConstante.PROCESSO_RELACIONADO.getCodigo())
								|| proc.getTipoOcorrencia().getId().equals(TipoOcorrenciaConstante.PROCESSO_RELACIONADO_A_TEMA_PARA_DEVOLUCAO.getCodigo())
								|| proc.getTipoOcorrencia().getId().equals(TipoOcorrenciaConstante.PROCESSO_RELACIONADO_POR_CONTROVERSIA.getCodigo())
								|| proc.getTipoOcorrencia().getId().equals(TipoOcorrenciaConstante.LEADING_CASE_ASSOCIADO.getCodigo())
							)) {
					lista.add(proc);
				}
			}
		}
		return lista;
	}
	
	@Transient
	public Boolean getReafirmadaJurisprudencia(){
		if( getProcessosTema() != null && getProcessosTema().size() > 0 ){
			for( ProcessoTema proc : getProcessosTema() ){
				if( proc.getTipoOcorrencia() != null && 
						proc.getTipoOcorrencia().getId().equals(TipoOcorrenciaConstante.REAFIRMADA_JURISPRUDENCIA.getCodigo())){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int compareTo(BaseEntity object) {
		if (object instanceof Tema && this.getNumeroSequenciaTema() != null && ((Tema) object).getNumeroSequenciaTema() != null)
			return this.getNumeroSequenciaTema().compareTo(((Tema) object).getNumeroSequenciaTema());
		
		return super.compareTo(object);
	}
	
}
