package br.gov.stf.estf.entidade.julgamento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoTexto;
import br.gov.stf.estf.entidade.ministro.Ministro;

@Entity
@Table( name="VOTO_JULGAMENTO_PROCESSO", schema="JULGAMENTO" )
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VotoJulgamentoProcesso extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = -2182341081345674889L;
	
	private JulgamentoProcesso julgamentoProcesso;
	private Ministro ministro;
	private Long numeroOrdemVotoSessao;
	private String tipoSituacaoVoto;
	private Date dataVoto;
	private String observacao;
	private VotoJulgamentoProcesso votoAcompanhado;
	private List<VotoJulgamentoProcesso> votosAcompanhando;
	private TipoVoto tipoVoto;
	private TipoVoto tipoVotoMerito;
	private TipoVoto tipoVotoTese;
	private TipoVoto tipoVotoModulacao;
	private VotoJulgamentoProcesso votoAnterior;
	private Boolean votoAntecipado;
	
	
	public enum TipoSituacaoVoto {
		VALIDO("Válido", "V"),
		CANCELADO("Cancelado", "C"),
		RASCUNHO("Rascunho", "R");
		
		private String descricao;
		private String sigla;
		
		private TipoSituacaoVoto(String descricao, String sigla) {
			this.descricao = descricao;
			this.sigla = sigla;
		}
		
		public String getDescricao() {
			return descricao;
		}
		public String getSigla() {
			return sigla;
		}
		
	}
	
	
	@Id
	@Column( name="SEQ_VOTO_JULGAMENTO_PROCESSO" )
	@GeneratedValue( generator="sequence", strategy=GenerationType.SEQUENCE )
	@SequenceGenerator( name="sequence", sequenceName="JULGAMENTO.SEQ_VOTO_JULGAMENTO_PROCESSO", allocationSize=1)	
	public Long getId() {
		return id;
	}		
	
	
	
	@ManyToOne( cascade={}, fetch=FetchType.LAZY )
	@JoinColumn(name="SEQ_JULGAMENTO_PROCESSO", unique=false, nullable=false)
	public JulgamentoProcesso getJulgamentoProcesso() {
		return julgamentoProcesso;
	}
	public void setJulgamentoProcesso(JulgamentoProcesso julgamentoProcesso) {
		this.julgamentoProcesso = julgamentoProcesso;
	}
	
	@ManyToOne( cascade={}, fetch=FetchType.LAZY )
	@JoinColumn(name="COD_MINISTRO", unique=false, nullable=false)
	public Ministro getMinistro() {
		return ministro;
	}
	public void setMinistro(Ministro ministro) {
		this.ministro = ministro;
	}
	
	@Column( name="NUM_ORDEM_VOTO_SESSAO", unique=false, nullable=false, insertable=true, updatable=true )
	public Long getNumeroOrdemVotoSessao() {
		return numeroOrdemVotoSessao;
	}
	public void setNumeroOrdemVotoSessao(Long numeroOrdemVotoSessao) {
		this.numeroOrdemVotoSessao = numeroOrdemVotoSessao;
	}
	
	@Column( name="TIP_SITUACAO_VOTO", unique=false, length=1, nullable=true, insertable=true, updatable=true )
	public String getTipoSituacaoVoto() {
		return tipoSituacaoVoto;
	}
	public void setTipoSituacaoVoto(String tipoSituacaoVoto) {
		this.tipoSituacaoVoto = tipoSituacaoVoto;
	}
	
	@Column( name="DAT_VOTO", unique=false, length=7, nullable=true, insertable=true, updatable=true )
	@Temporal( TemporalType.TIMESTAMP )
	public Date getDataVoto() {
		return dataVoto;
	}
	public void setDataVoto(Date dataVoto) {
		this.dataVoto = dataVoto;
	}

	@Column(name = "TXT_OBSERVACAO")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	@ManyToOne( cascade={}, fetch=FetchType.LAZY )
	@JoinColumn(name="SEQ_VOTO_JULGAMENTO_PROC_VINC", unique=false, nullable=true, insertable=true, updatable=true )
	public VotoJulgamentoProcesso getVotoAcompanhado() {
		return votoAcompanhado;
	}

	public void setVotoAcompanhado(VotoJulgamentoProcesso votoAcompanhado) {
		this.votoAcompanhado = votoAcompanhado;
	}
	
	@OneToMany( cascade={}, fetch=FetchType.LAZY )
	@JoinColumn(name="SEQ_VOTO_JULGAMENTO_PROC_VINC",unique=false, nullable=true, insertable=false, updatable=false)
	public List<VotoJulgamentoProcesso> getVotosAcompanhando(){
		return votosAcompanhando;
	}
	
	public void setVotosAcompanhando(List<VotoJulgamentoProcesso> votosAcompanhando){
		this.votosAcompanhando = votosAcompanhando;
	}
	
	@Transient
	public List<VotoJulgamentoProcesso> getVotosValidosAcompanhando(){
		List<VotoJulgamentoProcesso> votosValidos = new ArrayList<VotoJulgamentoProcesso>();
		for (VotoJulgamentoProcesso voto : getVotosAcompanhando()){
			if (voto.getTipoSituacaoVoto().equals(TipoSituacaoVoto.VALIDO.getSigla()))
				votosValidos.add(voto);
		}
		return votosValidos;
	}
	
    @Transient
    public boolean isAcompanhoRelator(){
    	return TipoVoto.TipoVotoConstante.ACOMPANHO_RELATOR.getCodigo().equals(getTipoVoto().getId());
    }
    
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TIPO_VOTO", unique=false, nullable=false, insertable=true, updatable=true)
	public TipoVoto getTipoVoto() {
		return tipoVoto;
	}

	public void setTipoVoto(TipoVoto tipoVoto) {
		this.tipoVoto = tipoVoto;
	}
	
	@Transient
	public String getTipoIncidenteJulgamento() {
		if (julgamentoProcesso != null)
			return julgamentoProcesso.getTipoJulgamento();
		
		return null;
	}

	@Transient
	public List<Texto> getVotos(Ministro ministroPresidenteAtual) {
		List<Texto> votos = new ArrayList<Texto>();
		List<TipoTexto> tipoTextoVotos = Arrays.asList(TipoTexto.VOTO, TipoTexto.VOTO_VOGAL, TipoTexto.VOTO_VISTA);
		Hibernate.initialize(julgamentoProcesso);
		if (julgamentoProcesso != null && julgamentoProcesso.getTextos() != null && !julgamentoProcesso.getTextos().isEmpty())
			for(Texto voto : julgamentoProcesso.getTextos()) {
				
				if(Ministro.COD_MINISTRO_PRESIDENTE.equals(voto.getMinistro().getId())){
					if(ministro.getId().equals(ministroPresidenteAtual.getId()) && tipoTextoVotos.contains(voto.getTipoTexto())) {
						votos.add(voto);
					}
				}
				
				if (ministro.equals(voto.getMinistro()) && tipoTextoVotos.contains(voto.getTipoTexto()))
					votos.add(voto);
			}

			return votos;
	}
	
	@Transient
	public boolean isCancelado() {
		return TipoSituacaoVoto.CANCELADO.getSigla().equals(tipoSituacaoVoto);
	}
	
	@Transient
	public boolean isValido() {
		return TipoSituacaoVoto.VALIDO.getSigla().equals(tipoSituacaoVoto);
	}
	
	@Transient
	public boolean isRascunho() {
		return TipoSituacaoVoto.RASCUNHO.getSigla().equals(tipoSituacaoVoto);
	}

	@Transient
	public VotoJulgamentoProcesso getVotoAnterior() {
		return votoAnterior;
	}

	public void setVotoAnterior(VotoJulgamentoProcesso votoAnterior) {
		this.votoAnterior = votoAnterior;
	}

	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TIPO_VOTO_MERITO", unique=false, nullable=true, insertable=true, updatable=true)
	public TipoVoto getTipoVotoMerito() {
		return tipoVotoMerito;
	}

	public void setTipoVotoMerito(TipoVoto tipoVotoMerito) {
		this.tipoVotoMerito = tipoVotoMerito;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TIPO_VOTO_TESE", unique=false, nullable=true, insertable=true, updatable=true)
	public TipoVoto getTipoVotoTese() {
		return tipoVotoTese;
	}

	public void setTipoVotoTese(TipoVoto tipoVotoTese) {
		this.tipoVotoTese = tipoVotoTese;
	}
	
	@ManyToOne(cascade={}, fetch=FetchType.LAZY)
	@JoinColumn(name="COD_TIPO_VOTO_MODULACAO", unique=false, nullable=true, insertable=true, updatable=true)
	public TipoVoto getTipoVotoModulacao() {
		return tipoVotoModulacao;
	}

	public void setTipoVotoModulacao(TipoVoto tipoVotoModulacao) {
		this.tipoVotoModulacao = tipoVotoModulacao;
	}
	
	@Column(name = "FLG_VOTO_ANTECIPADO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getVotoAntecipado() {
		return votoAntecipado;
	}
	
	public void setVotoAntecipado(Boolean votoAntecipado) {
		this.votoAntecipado = votoAntecipado;
	}
}
