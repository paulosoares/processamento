package br.gov.stf.estf.entidade.julgamento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.julgamento.enuns.SituacaoListaJulgamento;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.publicacao.TipoSessao;

/**
 * @author Paulo.Estevao
 * @since 06.06.2011
 */
@Entity
@Table(name = "LISTA_JULGAMENTO", schema = "JULGAMENTO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ListaJulgamento extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = 4525495613111677876L;

	private Long id;
	private Ministro ministro;
	private Sessao sessao;
	private String nome;
	private Set<ObjetoIncidente<?>> elementos;
	private Set<ProcessoListaJulgamento> listaProcessoListaJulgamento;
	private Integer ordemSessao;
	private Integer ordemSessaoMinistro;
	private Boolean julgado;
	private String nomePreLista;
	private String cabecalho;
	private SituacaoListaJulgamento situacaoListaJulgamento;
	private Date dataDisponibilizacao;
	private Andamento andamentoLiberacao;
	private Long dispositivoId;
	private String textoDecisao;
	private String andamentoPublicacao;
	private String descricaoAndamentoPublicacao;
	private Boolean publicacaoAutomatica;
	private Boolean publicado;
	private Boolean decisaoUniforme;
	private TipoResultadoJulgamento tipoResultadoJulgamento;
	private TipoListaJulgamento tipoListaJulgamento;
	private Ministro ministroVistor;
	private @Transient Long idMinistroPedidoVista = -1L;
	private String cabecalhoVistor;
	private Boolean admiteSustentacaoOral;
	private Boolean julgamentoTese;
	private Boolean julgamentoModulacao;
	private Boolean possuiSustentacaoOral;
	private Boolean possuiQuestaoFato;
	private Long quantidadeDivergencias;
	private Long quantidadeVotosAlterados;
	private Long quantidadeRascunhos;
	private Long quantidadeProcessos;
	private Boolean possuiVista;
	private Boolean possuiDestaque;
	private Ministro ministroPresidente;

	@Override
	@Id
	@Column(name = "SEQ_LISTA_JULGAMENTO")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "JULGAMENTO.SEQ_LISTA_JULGAMENTO", allocationSize = 1)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_MINISTRO")
	public Ministro getMinistro() {
		return ministro;
	}

	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_SESSAO")
	public Sessao getSessao() {
		return sessao;
	}

	public void setSessao(Sessao sessao) {
		this.sessao = sessao;
	}

	@Column(name = "DSC_LISTA_PROCESSO")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@ManyToMany(targetEntity = ObjetoIncidente.class, fetch = FetchType.LAZY)
	@JoinTable(schema = "JULGAMENTO", name = "PROCESSO_LISTA_JULG", joinColumns = @JoinColumn(name = "SEQ_LISTA_JULGAMENTO"), inverseJoinColumns = @JoinColumn(name = "SEQ_OBJETO_INCIDENTE_CANDIDATO"))	
	@Where( clause="SEQ_OBJETO_INCIDENTE IS NOT NULL")
	public Set<ObjetoIncidente<?>> getElementos() {
		return elementos;
	}

	public void setElementos(Set<ObjetoIncidente<?>> elementos) {
		this.elementos = elementos;
	}
	
	@Formula("(SELECT count(*) FROM JULGAMENTO.PROCESSO_LISTA_JULG plj WHERE plj.SEQ_LISTA_JULGAMENTO = SEQ_LISTA_JULGAMENTO)")
	public Long getQuantidadeProcessos() {
		return quantidadeProcessos;
	}
	
	public void setQuantidadeProcessos(Long quantidadeProcessos) {
		this.quantidadeProcessos = quantidadeProcessos;
	}

	@Column(name = "NUM_ORDEM_SESSAO")
	public Integer getOrdemSessao() {
		return ordemSessao;
	}

	public void setOrdemSessao(Integer ordemSessao) {
		this.ordemSessao = ordemSessao;
	}

	@Column(name = "NUM_ORDEM_SESSAO_MINISTRO")
	public Integer getOrdemSessaoMinistro() {
		return ordemSessaoMinistro;
	}

	public void setOrdemSessaoMinistro(Integer ordemSessaoMinistro) {
		this.ordemSessaoMinistro = ordemSessaoMinistro;
	}

	@Column(name = "FLG_JULGADO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getJulgado() {
		return julgado;
	}

	public void setJulgado(Boolean julgado) {
		this.julgado = julgado;
	}
	
	@Column(name = "NOM_LISTA")
	public String getNomePreLista() {
		return nomePreLista;
	}

	public void setNomePreLista(String nomePreLista) {
		this.nomePreLista = nomePreLista;
	}
	
	@Column(name = "DOC_CABECALHO")
	public String getCabecalho() {
		return cabecalho;
	}

	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}
	
	@Column(name = "TIP_SITUACAO_LISTA")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.julgamento.enuns.SituacaoListaJulgamento"),
			@Parameter(name = "idClass", value = "java.lang.String") })
	public SituacaoListaJulgamento getSituacaoListaJulgamento() {
		return situacaoListaJulgamento;
	}
	
	public void setSituacaoListaJulgamento(SituacaoListaJulgamento situacaoListaJulgamento) {
		this.situacaoListaJulgamento = situacaoListaJulgamento;
	}
	
	@Transient
	public Boolean getHaRascunhosRegistradosPorMinistro(Ministro ministroAutenticado){		
		for (ProcessoListaJulgamento julgamento : getListaProcessoListaJulgamento()) {
			if (null !=julgamento.getJulgamentoProcesso() && julgamento.getJulgamentoProcesso().getHaRascunhosRegistradosPorMinistro(ministroAutenticado))  
				return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	@Transient
	public Set<VotoJulgamentoProcesso> getRascunhosDoMinistro(Ministro ministroAutenticado){
		Set<VotoJulgamentoProcesso> lista = new TreeSet<VotoJulgamentoProcesso>();
		
		if (ministroAutenticado != null) {
			for (ProcessoListaJulgamento plj : getListaProcessoListaJulgamento()) {
				JulgamentoProcesso jp = plj.getJulgamentoProcesso();
				if (jp != null) {
					lista.addAll(jp.getRascunhosDoMinistro(ministroAutenticado));
				}
			}
		}
		
		return lista;
	}

	@Transient
    public Boolean getUnanime() {
		if (getQuantidadeDivergencias() == 0)
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
    }
	
	@Transient
    public long getQuantidadeProcessosUnanimes() {
		long processos = 0;
		for (ProcessoListaJulgamento julgamento : getListaProcessoListaJulgamento()) {
			if (null !=julgamento.getJulgamentoProcesso() && julgamento.getJulgamentoProcesso().getUnanime())  processos++;
		}
    	return processos;
    }
	
	@Transient
	public double getProporcaoProcessosUnanimes(){
		if (getQuantidadeProcessos() == 0)
			return 0;
		return (double)getQuantidadeProcessosUnanimes() / (double)getQuantidadeProcessos();
	}
	
	@Formula("(SELECT count(*) FROM JULGAMENTO.VOTO_JULGAMENTO_PROCESSO vjp "
			+ "INNER JOIN JULGAMENTO.PROCESSO_LISTA_JULG plj "
			+ "ON plj.SEQ_JULGAMENTO_PROCESSO = vjp.SEQ_JULGAMENTO_PROCESSO "
			+ "WHERE vjp.TIP_SITUACAO_VOTO = 'V' "
			+ "AND plj.SEQ_LISTA_JULGAMENTO = SEQ_LISTA_JULGAMENTO "
			+ "AND vjp.COD_TIPO_VOTO = 7)")
    public Long getQuantidadeDivergencias() {
    	return quantidadeDivergencias;
    }
	
	public void setQuantidadeDivergencias(Long quantidadeDivergencias) {
		this.quantidadeDivergencias = quantidadeDivergencias;
	}
	
	@Transient
	public long getQuantidadeAcompanhamentosComRessalva(){
		long quantidade = 0;
		for (ProcessoListaJulgamento julgamento : getListaProcessoListaJulgamento()) {
			if (null !=julgamento.getJulgamentoProcesso() && julgamento.getJulgamentoProcesso().isAcompanhamentosComRessalva()) 
				quantidade++;
		}
		return quantidade;
		
	}
	
	@Transient
	public long getQuantidadeAcompanhamentosDivergencia(){
		long quantidade = 0;
		for (ProcessoListaJulgamento julgamento : getListaProcessoListaJulgamento()) {
			if (null !=julgamento.getJulgamentoProcesso() && julgamento.getJulgamentoProcesso().isAcompanhamentoDivergencia()) 
				quantidade++;
		}
		return quantidade;		
	}
	
	@Transient
	public long getQuantidadeSuspeicao(){
		long quantidade = 0;
		for (ProcessoListaJulgamento julgamento : getListaProcessoListaJulgamento()) {
			if (null !=julgamento.getJulgamentoProcesso() && julgamento.getJulgamentoProcesso().isSuspeicao()) 
				quantidade++;
		}
		return quantidade;		
	}
	
	@Transient
	public long getQuantidadeImpedimento(){
		long quantidade = 0;
		for (ProcessoListaJulgamento julgamento : getListaProcessoListaJulgamento()) {
			if (null !=julgamento.getJulgamentoProcesso() && julgamento.getJulgamentoProcesso().isImpedimento()) 
				quantidade++;
		}
		return quantidade;		
	}
	
	@Transient
	public long getQuantidadeVista() {
		long quantidade = 0;
		
		for (ProcessoListaJulgamento julgamento : getListaProcessoListaJulgamento())
			if (julgamento.getJulgamentoProcesso() != null && julgamento.getJulgamentoProcesso().isVista()) 
				quantidade++;
		
		return quantidade;
	}
	
	@Transient
	public long getQuantidadeDestaque() {
		long quantidade = 0;
		
		for (ProcessoListaJulgamento julgamento : getListaProcessoListaJulgamento())
			if (julgamento.getJulgamentoProcesso() != null && julgamento.getJulgamentoProcesso().isDestaque()) 
				quantidade++;
		
		return quantidade;
	}
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="listaJulgamento")
	@OrderBy("ordemNaLista ASC")
	@Where(clause = "SEQ_OBJETO_INCIDENTE_CANDIDATO IN (SELECT OI.SEQ_OBJETO_INCIDENTE FROM JUDICIARIO.OBJETO_INCIDENTE OI WHERE OI.SEQ_OBJETO_INCIDENTE = SEQ_OBJETO_INCIDENTE_CANDIDATO)")
	public Set<ProcessoListaJulgamento> getListaProcessoListaJulgamento() {
		return listaProcessoListaJulgamento;
	}

	public void setListaProcessoListaJulgamento(Set<ProcessoListaJulgamento> listaProcessoListaJulgamento) {
		this.listaProcessoListaJulgamento = listaProcessoListaJulgamento;
	}
	
	@Column(name = "DAT_DISPONIBILIZACAO")
	public Date getDataDisponibilizacao() {
		return dataDisponibilizacao;
	}

	public void setDataDisponibilizacao(Date dataDisponibilizacao) {
		this.dataDisponibilizacao = dataDisponibilizacao;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_ANDAMENTO_LIBERACAO")
	public Andamento getAndamentoLiberacao() {
		return andamentoLiberacao;
	}

	public void setAndamentoLiberacao(Andamento andamento) {
		this.andamentoLiberacao = andamento;
	}
	
	@Column(name = "TXT_DECISAO")
	public String getTextoDecisao() {
		return textoDecisao;
	}
	
	
	public void setTextoDecisao(String decisaoPadrao) {
		this.textoDecisao = decisaoPadrao;
	}
	
	@Transient
	public Long getDispositivoId() {
		return dispositivoId;
	}
	
	public void setDispositivoId(Long dispositivoId) {
		this.dispositivoId = dispositivoId;
	}
	
	@Transient
	public String getAndamentoPublicacao() {
		return andamentoPublicacao;
	}
	
	@Transient
	public void setAndamentoPublicacao(String andamentoPublicacao) {
		this.andamentoPublicacao = andamentoPublicacao;
	}
	
	@Transient
	public String getDescricaoAndamentoPublicacao() {
		return descricaoAndamentoPublicacao;
	}
	
	@Transient
	public void setDescricaoAndamentoPublicacao(String descricaoAndamentoPublicacao) {
		this.descricaoAndamentoPublicacao = descricaoAndamentoPublicacao;
	}
	
	@Column(name = "FLG_PUBLICACAO_AUTOMATICA")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPublicacaoAutomatica() {
		return publicacaoAutomatica;
	}

	public void setPublicacaoAutomatica(Boolean ativo) {
		this.publicacaoAutomatica = ativo;
	}

	@Column(name = "FLG_PUBLICADO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getPublicado() {
		return publicado;
	}

	public void setPublicado(Boolean publicado) {
		this.publicado = publicado;
	}
	
	@Column(name = "FLG_DECISAO_UNIFORME")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getDecisaoUniforme() {
		return decisaoUniforme;
	}

	public void setDecisaoUniforme(Boolean decisaoUniforme) {
		this.decisaoUniforme = decisaoUniforme;
	}
	
	@Column(name = "TIP_RESULTADO_UNIFORME")
	@Type(type = "br.gov.stf.framework.util.GenericEnumUserType", parameters = {
			@Parameter(name = "enumClass", value = "br.gov.stf.estf.entidade.julgamento.TipoResultadoJulgamento"),
			@Parameter(name = "idClass", value = "java.lang.Integer"),
			@Parameter(name = "valueOfMethod", value = "valueOf") })
	public TipoResultadoJulgamento getTipoResultadoJulgamento() {
		return tipoResultadoJulgamento;
	}

	public void setTipoResultadoJulgamento(TipoResultadoJulgamento tipoResultadoJulgamento) {
		this.tipoResultadoJulgamento = tipoResultadoJulgamento;
	}
	
	@Transient
	public String getDescricaoSessaoColegiadoPeriodo(){
		String colegiado = Colegiado.TipoColegiadoConstante.valueOfSigla(sessao.getColegiado().getId()).getDescricao();
		String dataInicio = new SimpleDateFormat("d.M.yyyy").format(sessao.getDataInicio());				
		String dataFim = new SimpleDateFormat("d.M.yyyy").format(sessao.getDataFim());				
		String extraOrdinaria ="";
		if(sessao.getTipoSessao().equals(TipoSessao.EXTRAORDINARIA.getSigla())) {
			extraOrdinaria = " Extraordinária";
		}
		String descricao = " " + colegiado + ", Sessão Virtual"+ extraOrdinaria +" de " + dataInicio + " a " + dataFim + ".";
		return descricao;
	}
	
	@Transient
	public String getCabecalhoCaixaBaixa(){
		return toCaixaBaixa(cabecalho);
	}
	
	@Transient
	public String getCabecalhoVistorCaixaBaixa(){
		return toCaixaBaixa(cabecalhoVistor);
	}
	private String toCaixaBaixa(String cabecalho) {
		StringBuffer cabecalhoFormatado = new StringBuffer();
		if (cabecalho == null)
			return "";
		char ultimoCaractere = cabecalho.charAt(0); 
		cabecalhoFormatado.append(Character.toUpperCase(ultimoCaractere));
		for (int i = 1; i < cabecalho.length(); i++){
			if (ultimoCaractere == '.' || ultimoCaractere == '>' && cabecalho.charAt(i) != ' '){
				cabecalhoFormatado.append(Character.toUpperCase(cabecalho.charAt(i)));								
			}
			else
				cabecalhoFormatado.append(Character.toLowerCase(cabecalho.charAt(i)));			
			if (cabecalho.charAt(i) != ' ')
				ultimoCaractere = cabecalho.charAt(i);
		}
		return cabecalhoFormatado.toString();
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SEQ_TIPO_LISTA_JULGAMENTO")
	public TipoListaJulgamento getTipoListaJulgamento() {
		return tipoListaJulgamento;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COD_MINISTRO_VISTOR")
	public Ministro getMinistroVistor() {
		return ministroVistor;
	}
	
	public void setMinistroVistor(Ministro ministroVistor) {
		this.ministroVistor = ministroVistor;
	}

	public void setTipoListaJulgamento(TipoListaJulgamento tipoListaJulgamento) {
		this.tipoListaJulgamento = tipoListaJulgamento;
	}

	@Transient
	public Long getIdMinistroPedidoVista() {
		return idMinistroPedidoVista;
	}

	@Transient
	public void setIdMinistroPedidoVista(Long idMinistroPedidoVista) {
		this.idMinistroPedidoVista = idMinistroPedidoVista;
	}

	@Column(name = "DOC_CABECALHO_VISTOR")
	public String getCabecalhoVistor() {
		return cabecalhoVistor;
	}

	public void setCabecalhoVistor(String cabecalhoVistor) {
		this.cabecalhoVistor = cabecalhoVistor;
	}
	
	@Column(name = "FLG_ADMITE_SUSTENTACAO_ORAL")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getAdmiteSustentacaoOral() {
		return admiteSustentacaoOral;
	}
	
	public void setAdmiteSustentacaoOral(Boolean admiteSustentacaoOral) {
		this.admiteSustentacaoOral = admiteSustentacaoOral;
	}
	
	@Column(name = "FLG_JULGAMENTO_TESE")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getJulgamentoTese() {
		return julgamentoTese;
	}

	public void setJulgamentoTese(Boolean julgamentoTese) {
		this.julgamentoTese = julgamentoTese;
	}

	@Column(name = "FLG_JULGAMENTO_MODULACAO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getJulgamentoModulacao() {
		return julgamentoModulacao;
	}

	public void setJulgamentoModulacao(Boolean julgmentoModulacao) {
		this.julgamentoModulacao = julgmentoModulacao;
	}
	
	@Transient
	public String getMotivoDestaque() {
		List<String> motivos = new ArrayList<String>();
		
		if (getPossuiSustentacaoOral())
			motivos.add("Sustentação Oral");
		
		if (getPossuiQuestaoFato())
			motivos.add("Questão de Fato");
			
		if (getJulgamentoTese())
			motivos.add("Julgamento de Tese");
		
		if (getJulgamentoModulacao())
			motivos.add("Julgamento de Modulação");
		
		if (getTemDestaque())
			motivos.add("Destaque");
		
		if (getTemVista())
			motivos.add("Vista");
		
		if (!motivos.isEmpty())
			return "Possui: " + StringUtils.join(motivos, " / ");

		return null;
	}
	
	@Transient
	public String getCorFundoLista() {
		if (getTemDestaque() || getTemVista())
			return "yellow";
		
		if (getPossuiSustentacaoOral() || getPossuiQuestaoFato() || getJulgamentoTese() || getJulgamentoModulacao())
			return "aquamarine";
		
		return "";
	}
	
	@Transient
	public Boolean getTemDestaque(){		
		for (ProcessoListaJulgamento julgamento : getListaProcessoListaJulgamento())
			if (julgamento.getJulgamentoProcesso() != null && julgamento.getJulgamentoProcesso().isDestaque())  
				return Boolean.TRUE;

		return Boolean.FALSE;
	}
	
	@Transient
	public Boolean getTemVista(){		
		for (ProcessoListaJulgamento julgamento : getListaProcessoListaJulgamento())
			if (julgamento.getJulgamentoProcesso() != null && julgamento.getJulgamentoProcesso().isVista())  
				return Boolean.TRUE;

		return Boolean.FALSE;
	}

	@Formula("(SELECT count(*) FROM JULGAMENTO.MANIFESTACAO_REPRESENTANTE mr WHERE mr.TIP_MANIFESTACAO = 'SO' AND mr.SEQ_LISTA_JULGAMENTO = SEQ_LISTA_JULGAMENTO)")
	public Boolean getPossuiSustentacaoOral() {
		return possuiSustentacaoOral;
	}
	
	public void setPossuiSustentacaoOral(Boolean possuiSustentacaoOral) {
		this.possuiSustentacaoOral = possuiSustentacaoOral;
	}
	
	@Formula("(SELECT count(*) FROM JULGAMENTO.JULGAMENTO_PROCESSO jp INNER JOIN JULGAMENTO.PROCESSO_LISTA_JULG plj ON plj.SEQ_JULGAMENTO_PROCESSO = JP.SEQ_JULGAMENTO_PROCESSO WHERE plj.SEQ_LISTA_JULGAMENTO = SEQ_LISTA_JULGAMENTO AND jp.TIP_SITUACAO_PROC_SESSAO = 'D')")
	public Boolean getPossuiDestaque() {
		return possuiDestaque;
	}
	
	public void setPossuiDestaque(Boolean possuiDestaque) {
		this.possuiDestaque = possuiDestaque;
	}
	
	@Formula("(SELECT count(*) FROM JULGAMENTO.JULGAMENTO_PROCESSO jp INNER JOIN JULGAMENTO.PROCESSO_LISTA_JULG plj ON plj.SEQ_JULGAMENTO_PROCESSO = JP.SEQ_JULGAMENTO_PROCESSO WHERE plj.SEQ_LISTA_JULGAMENTO = SEQ_LISTA_JULGAMENTO AND jp.TIP_SITUACAO_PROC_SESSAO = 'S')")
	public Boolean getPossuiVista() {
		return possuiVista;
	}
	
	public void setPossuiVista(Boolean possuiVista) {
		this.possuiVista = possuiVista;
	}

	@Formula("(SELECT count(*) FROM JULGAMENTO.MANIFESTACAO_REPRESENTANTE mr WHERE mr.TIP_MANIFESTACAO = 'QF' AND mr.SEQ_LISTA_JULGAMENTO = SEQ_LISTA_JULGAMENTO)")
	public Boolean getPossuiQuestaoFato() {
		return possuiQuestaoFato;
	}
	
	public void setPossuiQuestaoFato(Boolean possuiQuestaoFato) {
		this.possuiQuestaoFato = possuiQuestaoFato;
	}
	
	@Transient
	public Boolean getPossuiSustentacaoOralOuQuestaoFato() {
		return getPossuiSustentacaoOral() || getPossuiQuestaoFato();
	}

	@Formula("(SELECT count(*) FROM JULGAMENTO.VOTO_JULGAMENTO_PROCESSO vjp "
			+ "INNER JOIN JULGAMENTO.PROCESSO_LISTA_JULG plj "
			+ "ON plj.SEQ_JULGAMENTO_PROCESSO = vjp.SEQ_JULGAMENTO_PROCESSO "
			+ "WHERE vjp.TIP_SITUACAO_VOTO = 'C' "
			+ "AND plj.SEQ_LISTA_JULGAMENTO = SEQ_LISTA_JULGAMENTO)")
	public Long getQuantidadeVotosAlterados(){
		return quantidadeVotosAlterados;
	}
	
	public void setQuantidadeVotosAlterados(Long quantidadeVotosAlterados) {
		this.quantidadeVotosAlterados = quantidadeVotosAlterados;
	}
	
	@Transient
	public Boolean getHaVotosAlterados() {
		return getQuantidadeVotosAlterados() != 0;
	}
	
	@Formula("(SELECT count(*) FROM JULGAMENTO.VOTO_JULGAMENTO_PROCESSO vjp "
			+ "INNER JOIN JULGAMENTO.PROCESSO_LISTA_JULG plj "
			+ "ON plj.SEQ_JULGAMENTO_PROCESSO = vjp.SEQ_JULGAMENTO_PROCESSO "
			+ "WHERE vjp.TIP_SITUACAO_VOTO = 'R' "
			+ "AND plj.SEQ_LISTA_JULGAMENTO = SEQ_LISTA_JULGAMENTO)")
	public Long getQuantidadeRascunhos() {
		return quantidadeRascunhos;
	}

	public void setQuantidadeRascunhos(Long quantidadeRascunhos) {
		this.quantidadeRascunhos = quantidadeRascunhos;
	}

	@Transient
	public List<ProcessoListaJulgamento> getProcessosListaJulgamento() {
		
		List<ProcessoListaJulgamento> processosDaLista = new ArrayList<ProcessoListaJulgamento>(getListaProcessoListaJulgamento());
		
		return processosDaLista;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COD_MINISTRO_PRESIDENTE")
	public Ministro getMinistroPresidente() {
		return ministroPresidente;
	}

	public void setMinistroPresidente(Ministro ministroPresidente) {
		this.ministroPresidente = ministroPresidente;
	}
	
}
