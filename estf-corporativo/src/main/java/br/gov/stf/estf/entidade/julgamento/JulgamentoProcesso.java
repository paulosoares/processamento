package br.gov.stf.estf.entidade.julgamento;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.julgamento.TipoVoto.TipoVotoConstante;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso.TipoSituacaoVoto;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.TipoObjetoIncidente;
import br.gov.stf.framework.util.DateTimeHelper;

@Entity
@Table( name="JULGAMENTO_PROCESSO", schema="JULGAMENTO" )
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class JulgamentoProcesso extends ESTFBaseEntity<Long>  {

	private static final long serialVersionUID = 1955699285785056833L;
    private Long id;
    private Sessao sessao;
    private ObjetoIncidente<?> objetoIncidente;
    private AndamentoProcesso andamentoProcesso;
    private Set<VotoJulgamentoProcesso> listaVotoJulgamentoProcesso;
    private List<SituacaoJulgamento> listaSituacaoJulgamento;
    private String tipoJulgamento;
    private Integer ordemSessao;
    private TipoSituacaoProcessoSessao situacaoProcessoSessao;
    private String observacao;
    private Ministro ministroDestaque;
    private Ministro ministroVista;
    private List<Texto> textos;
	private Long quantidadeDivergencias;
	private Long quantidadeVotosAlterados;
	private ProcessoListaJulgamento processoListaJulgamento;
	private Long quantidadeRascunhos;
	private Boolean destaqueCancelado;
	private Boolean exclusivoDigital;
	
    @Id
    @Column( name="SEQ_JULGAMENTO_PROCESSO" )
    @GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
    @SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_JULGAMENTO_PROCESSO", allocationSize=1 )         
	public Long getId() {
		return id;
	}
    
	public void setId(Long id){
		this.id = id;
	}


	@ManyToOne( cascade={}, fetch=FetchType.LAZY )
    @JoinColumn(name="SEQ_SESSAO", unique=false, nullable=false, insertable=true, updatable=true )
    public Sessao getSessao() {
    	return sessao;
    }
    public void setSessao(Sessao sessao) {
    	this.sessao = sessao;
    }

    @ManyToOne( cascade={}, fetch=FetchType.LAZY )
    @JoinColumn(name="SEQ_ANDAMENTO_PROCESSO", unique=false, nullable=true, insertable=true, updatable=true )
    public AndamentoProcesso getAndamentoProcesso() {
    	return andamentoProcesso;
    }
    public void setAndamentoProcesso(AndamentoProcesso andamentoProcesso) {
    	this.andamentoProcesso = andamentoProcesso;
    }
    
    @OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name = "SEQ_JULGAMENTO_PROCESSO", referencedColumnName = "SEQ_JULGAMENTO_PROCESSO")
    @OrderBy("dataVoto")
    public Set<VotoJulgamentoProcesso> getListaVotoJulgamentoProcesso() {
    	return listaVotoJulgamentoProcesso;
    }
    
    public void setListaVotoJulgamentoProcesso(Set<VotoJulgamentoProcesso> listaVotoJulgamentoProcesso ) {
    	this.listaVotoJulgamentoProcesso = listaVotoJulgamentoProcesso;
    }

    @OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name = "SEQ_JULGAMENTO_PROCESSO", referencedColumnName = "SEQ_JULGAMENTO_PROCESSO")
    @org.hibernate.annotations.Cascade( value=org.hibernate.annotations.CascadeType.ALL )
    public List<SituacaoJulgamento> getListaSituacaoJulgamento() {
    	return listaSituacaoJulgamento;
    }
    public void setListaSituacaoJulgamento(List<SituacaoJulgamento> listaSituacaoJulgamento) {
    	this.listaSituacaoJulgamento = listaSituacaoJulgamento;
    }
    
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", insertable=true, updatable=false)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
    
	@Column(name = "TIPO_JULGAMENTO", unique = false, nullable = true, insertable = true, updatable = true, length = 15)
	public String getTipoJulgamento() {
		return tipoJulgamento;
	}

	public void setTipoJulgamento(String tipoJulgamento) {
		this.tipoJulgamento = tipoJulgamento;
	}

	@Column(name = "NUM_ORDEM_SESSAO")
	public Integer getOrdemSessao() {
		return ordemSessao;
	}
	
	public void setOrdemSessao(Integer ordemSessao) {
		this.ordemSessao = ordemSessao;
	}

	@Column(name = "TIP_SITUACAO_PROC_SESSAO")
	@Type(type="br.gov.stf.framework.util.GenericEnumUserType", parameters={
				@Parameter( name = "enumClass", 
						    value = "br.gov.stf.estf.entidade.julgamento.TipoSituacaoProcessoSessao"),
				@Parameter( name = "identifierMethod",
							value = "getSigla"),
				@Parameter( name = "idClass", 
							value = "java.lang.String"),
				@Parameter( name = "valueOfMethod",
						    value = "valueOfSigla")})
	public TipoSituacaoProcessoSessao getSituacaoProcessoSessao() {
		return situacaoProcessoSessao;
	}
	
	public void setSituacaoProcessoSessao(
			TipoSituacaoProcessoSessao situacaoProcessoSessao) {
		this.situacaoProcessoSessao = situacaoProcessoSessao;
	}

//============================================ MÉTODOS TRANSIENT ============================================

	@Transient
    public List<VotoJulgamentoProcesso> getListaVotoJulgamentoRascunho(){
    	List<VotoJulgamentoProcesso> listaVotos = new ArrayList<VotoJulgamentoProcesso>();
		if (getListaVotoJulgamentoProcesso() != null)
	    	for (VotoJulgamentoProcesso voto : getListaVotoJulgamentoProcesso()){
	    		if (TipoSituacaoVoto.RASCUNHO.getSigla().equals(voto.getTipoSituacaoVoto()))
	    			listaVotos.add(voto);
    	}
		return listaVotos;
    }
    
    @Transient
    public List<VotoJulgamentoProcesso> getListaVotoJulgamentoDefinitivo(){
    	List<VotoJulgamentoProcesso> listaVotos = new ArrayList<VotoJulgamentoProcesso>();
		if (getListaVotoJulgamentoProcesso() != null)
	    	for (VotoJulgamentoProcesso voto : getListaVotoJulgamentoProcesso()){
	    		if (TipoSituacaoVoto.VALIDO.getSigla().equals(voto.getTipoSituacaoVoto()))
	    			listaVotos.add(voto);
	    	}
		return listaVotos;
    }
        
	@Transient
	public IncidenteJulgamento getIncidenteJulgamento(){
		if( objetoIncidente != null && objetoIncidente.getTipoObjetoIncidente() != null && 
				objetoIncidente.getTipoObjetoIncidente().equals(TipoObjetoIncidente.INCIDENTE_JULGAMENTO) ){
			return (IncidenteJulgamento) objetoIncidente;
		}
		return null;
	}
	
	@Transient
	public String getIdentificacao(){
		if( objetoIncidente != null  ){
			if( objetoIncidente.getPai().getTipoObjetoIncidente().equals(TipoObjetoIncidente.RECURSO) ){
				return objetoIncidente.getPai().getIdentificacao();
			}else{ 
				return objetoIncidente.getPrincipal().getIdentificacao();
			}
		}
		return null;
	}
    
	@Transient
    public List<VotoJulgamentoProcesso> getListVotoJulgamentoProcessoValido() {
        return getListaVotoJulgamentoDefinitivo();
    }
    
    @Transient
	public SituacaoJulgamento getSituacaoAtual() {
		if (listaSituacaoJulgamento != null && listaSituacaoJulgamento.size() > 0) {
			for (SituacaoJulgamento situacao : listaSituacaoJulgamento) {
				if (situacao.getAtual() != null && situacao.getAtual()) {
					return situacao;
				}
			}
		}

		return null;
	}
    @Transient
    public String getDescricaoSituacaoAtual(){
    	SituacaoJulgamento situacao = getSituacaoAtual();
    	if( situacao != null ){
    		if( situacao.getTipoSituacaoJulgamento().getId().equals(TipoSituacaoJulgamento.TipoSitucacaoJulgamentoConstant.AGENDADO.getCodigo()) ){
    			return situacao.getTipoSituacaoJulgamento().getDescricao()+" para "+DateTimeHelper.getDataString(getSessao().getDataPrevistaInicio());
    		}else if( situacao.getTipoSituacaoJulgamento().getId().equals(TipoSituacaoJulgamento.TipoSitucacaoJulgamentoConstant.FINALIZADO.getCodigo()) ){
    			return situacao.getTipoSituacaoJulgamento().getDescricao()+" em "+DateTimeHelper.getDataString(getSessao().getDataFim());
    		}else{
    			return situacao.getTipoSituacaoJulgamento().getDescricao();
    		}
    	}
    	return "";
    }
    
    @Transient
    public void adicionarSituacaoJulgamento(TipoSituacaoJulgamento tipoSituacao) {
		if (listaSituacaoJulgamento == null)
			listaSituacaoJulgamento = new ArrayList<SituacaoJulgamento>();
        
		for (SituacaoJulgamento situacao : listaSituacaoJulgamento)
			situacao.setAtual(false);
		
		SituacaoJulgamento novaSituacao =  new SituacaoJulgamento();
        novaSituacao.setJulgamentoProcesso(this);
        novaSituacao.setDataSituacaoJulgamento(Calendar.getInstance().getTime());
        novaSituacao.setTipoSituacaoJulgamento(tipoSituacao);
        novaSituacao.setAtual(true);
        
		listaSituacaoJulgamento.add(novaSituacao);
    }
    
    @Transient
    public Boolean getUnanime() {
    	return getQuantidadeDivergencias() == 0;
    }
    
	@Formula("(SELECT count(*) FROM JULGAMENTO.VOTO_JULGAMENTO_PROCESSO vjp WHERE vjp.TIP_SITUACAO_VOTO = 'V' AND vjp.COD_TIPO_VOTO IN (7,8) AND vjp.SEQ_JULGAMENTO_PROCESSO = SEQ_JULGAMENTO_PROCESSO)")
    public Long getQuantidadeDivergencias() {
    	return quantidadeDivergencias;
    }
	
	public void setQuantidadeDivergencias(Long quantidadeDivergencias) {
		this.quantidadeDivergencias = quantidadeDivergencias;
	}
    
    //verifica se a lista possui algum voto de acompanhamento com ressalva
    @Transient
	public boolean isAcompanhamentosComRessalva(){		
		for (VotoJulgamentoProcesso voto : getListVotoJulgamentoProcessoValido()) {
			if (voto != null && voto.getTipoVoto() != null && (voto.getTipoVoto().getId().equals(TipoVoto.TipoVotoConstante.ACOMPANHO_RELATOR_RESSALVA.getCodigo())))
				return true;
		}
		return false;
		
	}
    
  //verifica se a lista possui algum voto de acompanhamento de divergência
    @Transient
	public boolean isAcompanhamentoDivergencia(){		
		for (VotoJulgamentoProcesso voto : getListVotoJulgamentoProcessoValido()) {
			if (voto != null && voto.getTipoVoto() != null && voto.getTipoVoto().getId().equals(TipoVoto.TipoVotoConstante.ACOMPANHO_DIVERGENCIA.getCodigo()))
				return true;
		}
		return false;
	}
    
    //verifica se a lista possui algum ministro suspeito
    @Transient
	public boolean isSuspeicao(){		
		for (VotoJulgamentoProcesso voto : getListVotoJulgamentoProcessoValido()) {
			if (voto != null && voto.getTipoVoto() != null && voto.getTipoVoto().getId().equals(TipoVoto.TipoVotoConstante.SUSPEITO.getCodigo()))
				return true;
		}
		return false;		
	}
    
    //verifica se a lista possui algum ministro impedido
    @Transient
	public boolean isImpedimento(){		
		for (VotoJulgamentoProcesso voto : getListVotoJulgamentoProcessoValido()) {
			if (voto != null && voto.getTipoVoto() != null && voto.getTipoVoto().getId().equals(TipoVoto.TipoVotoConstante.IMPEDIDO.getCodigo()))
				return true;
		}
		return false;		
	}
    
	@Formula("(SELECT count(*) FROM JULGAMENTO.VOTO_JULGAMENTO_PROCESSO vjp WHERE vjp.TIP_SITUACAO_VOTO = 'C' AND vjp.SEQ_JULGAMENTO_PROCESSO = SEQ_JULGAMENTO_PROCESSO)")
    public Long getQuantidadeVotosAlterados() {
		return quantidadeVotosAlterados;
	}
	
	public void setQuantidadeVotosAlterados(Long quantidadeVotosAlterados) {
		this.quantidadeVotosAlterados = quantidadeVotosAlterados;
	}
	
	@Transient
	public Boolean getHaVotosAlterados(){
    	return getQuantidadeVotosAlterados() != 0;
    }
    
    @Transient
    public boolean isDestaque(){
    	return TipoSituacaoProcessoSessao.DESTAQUE.equals(situacaoProcessoSessao);
    }
    
    @Transient
    public boolean isVista(){
    	return TipoSituacaoProcessoSessao.SUSPENSO.equals(situacaoProcessoSessao);
    }
    
    @Transient
    public boolean isProcVista(){
    	return TipoSituacaoProcessoSessao.SUSPENSO.equals(situacaoProcessoSessao) && (ministroVista != null);
    }
    
    @Transient
    public Boolean getHaRascunhosRegistradosPorMinistro(Ministro ministroAutenticado){
    	for (VotoJulgamentoProcesso rascunho : getListaVotoJulgamentoRascunho()){
    		if (rascunho.getMinistro().getId().equals(ministroAutenticado.getId()))
    			return Boolean.TRUE;    			
    	}
    	return Boolean.FALSE;
    }
    
    public Set<VotoJulgamentoProcesso> getRascunhosDoMinistro(Ministro ministroAutenticado) {
    	Set<VotoJulgamentoProcesso> rascunhos = new TreeSet<VotoJulgamentoProcesso>();
    	
    	if (getListaVotoJulgamentoRascunho() != null)
	    	for (VotoJulgamentoProcesso rascunho : getListaVotoJulgamentoRascunho())
	    		if (rascunho.getMinistro().getId().equals(ministroAutenticado.getId()))
	    			rascunhos.add(rascunho);    			

    	return rascunhos;
    }
    
    public VotoJulgamentoProcesso getVotoDoMinistro(Ministro ministroAutenticado) {
    	if (getListaVotoJulgamentoDefinitivo() != null)
	    	for (VotoJulgamentoProcesso voto : getListaVotoJulgamentoDefinitivo())
	    		if (voto.getMinistro().getId().equals(ministroAutenticado.getId()))
	    			return voto;  			

    	return null;
    }
    
    @Column(name = "TXT_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	@Transient
	public Long getTotalImpedido() {
		Long total = 0L;
		 for (VotoJulgamentoProcesso voto : getListaVotoJulgamentoDefinitivo()) {
			 if (voto != null && voto.getTipoVoto() != null && voto.getTipoVoto().getId().equals(TipoVotoConstante.IMPEDIDO.getCodigo())) total++;			
		}
		return total;
	}
	
	@Transient
	public Long getTotalSuspeito() {
		Long total = 0L;
		 for (VotoJulgamentoProcesso voto : getListaVotoJulgamentoDefinitivo()) {
			 if (voto != null && voto.getTipoVoto() != null && voto.getTipoVoto().getId().equals(TipoVotoConstante.SUSPEITO.getCodigo())) total++;			
		}
		return total;
	}
	
	@Transient
	public Long getTotalAcompanhoRelator() {
		Long total = 0L;
		 for (VotoJulgamentoProcesso voto : getListaVotoJulgamentoDefinitivo()) {
			if (voto != null && voto.getTipoVoto() != null && voto.getTipoVoto().getId().equals(TipoVotoConstante.ACOMPANHO_RELATOR.getCodigo())) total++;
		}
		return total;
	}
	
	@Transient
	public Long getTotalDivergencia() {
		Long total = 0L;
		 for (VotoJulgamentoProcesso voto : getListaVotoJulgamentoDefinitivo()) {
			 if (voto != null && voto.getTipoVoto() != null && voto.getTipoVoto().getId().equals(TipoVotoConstante.DIVERGENTE.getCodigo())) total++;			
		}
		return total;
	}
	
	@Transient
	public Long getTotalAcompanhoDivergencia() {
		Long total = 0L;
		 for (VotoJulgamentoProcesso voto : getListaVotoJulgamentoDefinitivo()) {
			 if (voto != null && voto.getTipoVoto() != null && voto.getTipoVoto().getId().equals(TipoVotoConstante.ACOMPANHO_DIVERGENCIA.getCodigo())) total++;			
		}
		return total;
	}
	
	@Transient
	public Long getTotalAcompanhoRessalva() {
		Long total = 0L;
		 for (VotoJulgamentoProcesso voto : getListaVotoJulgamentoDefinitivo()) {
			 if (voto != null && voto.getTipoVoto() != null && voto.getTipoVoto().getId().equals(TipoVotoConstante.ACOMPANHO_RELATOR_RESSALVA.getCodigo())) total++;			
		}
		return total;
	}
	
	@Transient
	public int getQtdObservacoes(){
		int qtdObservacoes = 0;
		for (VotoJulgamentoProcesso voto : this.getListaVotoJulgamentoDefinitivo()) {
			if (voto != null && voto.getObservacao() != null) qtdObservacoes++; 
		}		
		return qtdObservacoes;
	}
	
	@Transient
	public List<VotoJulgamentoProcesso> getVotosDivergencia(){
		List<VotoJulgamentoProcesso> listaVotos = new ArrayList<VotoJulgamentoProcesso>();
		for (VotoJulgamentoProcesso voto : getListaVotoJulgamentoDefinitivo()) {
			 if (voto != null && voto.getTipoVoto() != null && voto.getTipoVoto().getId().equals(TipoVotoConstante.DIVERGENTE.getCodigo())) 
				 listaVotos.add(voto);			
		}
		return listaVotos;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "COD_MINISTRO_DESTAQUE")
	public Ministro getMinistroDestaque() {
		return ministroDestaque;
	}

	public void setMinistroDestaque(Ministro ministroDestaque) {
		this.ministroDestaque = ministroDestaque;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "COD_MINISTRO_VISTA")
	public Ministro getMinistroVista() {
		return ministroVista;
	}

	public void setMinistroVista(Ministro ministroVista) {
		this.ministroVista = ministroVista;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", referencedColumnName = "SEQ_OBJETO_INCIDENTE")
	public List<Texto> getTextos() {
		return textos;
	}

	public void setTextos(List<Texto> textos) {
		this.textos = textos;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "julgamentoProcesso")
	public ProcessoListaJulgamento getProcessoListaJulgamento() {
		return processoListaJulgamento;
	}

	public void setProcessoListaJulgamento(ProcessoListaJulgamento processoListaJulgamento) {
		this.processoListaJulgamento = processoListaJulgamento;
	}
	
	@Formula("(SELECT count(*) FROM JULGAMENTO.VOTO_JULGAMENTO_PROCESSO vjp WHERE vjp.TIP_SITUACAO_VOTO = 'R' AND vjp.SEQ_JULGAMENTO_PROCESSO = SEQ_JULGAMENTO_PROCESSO)")
    public Long getQuantidadeRascunhos() {
    	return quantidadeRascunhos;
    }
	
	public void setQuantidadeRascunhos(Long quantidadeRascunhos) {
		this.quantidadeRascunhos = quantidadeRascunhos;
	}

	@Column(name = "FLG_DESTAQUE_CANCELADO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getDestaqueCancelado() {
		return destaqueCancelado;
	}

	public void setDestaqueCancelado(Boolean destaqueCancelado) {
		this.destaqueCancelado = destaqueCancelado;
	}

	@Column(name = "FLG_EXCLUSIVO_DIGITAL")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getExclusivoDigital() {
		return exclusivoDigital;
	}

	public void setExclusivoDigital(Boolean exclusivoDigital) {
		this.exclusivoDigital = exclusivoDigital;
	}
}
