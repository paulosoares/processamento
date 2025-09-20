package br.gov.stf.estf.entidade.publicacao;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.documento.ArquivoEletronico;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.TipoMeioProcesso;
import br.gov.stf.estf.entidade.util.ObjetoIncidenteUtil;

@Entity
@Table(name = "PROCESSO_PUBLICADOS", schema = "STF")
public class ProcessoPublicado extends ESTFBaseEntity<Long> {

	private static final long serialVersionUID = -483788114343592777L;
	private ArquivoEletronico arquivoEletronicoTexto;
	private ArquivoEletronico arquivoEletronicoObs;

	private ObjetoIncidente<?> objetoIncidente;
	// private Ministro ministroRelator;
	private Texto texto;
	
	private List<ConteudoPublicacao> conteudosPublicacao;

	private Integer codigoCapitulo;
	private Integer codigoMateria;
	private Short anoMateria;
	private Integer numeroMateria;
	private Long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE", nullable = false)
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	@Id
	@Column(name = "SEQ_PROCESSO_PUBLICADOS", insertable = false, updatable = false)
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_PROCESSO_PUBLICADOS", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ARQUIVO_ELETRONICO_TEXTO", nullable = true, updatable = true)
	public ArquivoEletronico getArquivoEletronicoTexto() {
		return this.arquivoEletronicoTexto;
	}

	public void setArquivoEletronicoTexto(ArquivoEletronico arquivoEletronicoTexto) {
		this.arquivoEletronicoTexto = arquivoEletronicoTexto;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_ARQUIVO_ELETRONICO_OBS", nullable = true, updatable = false)
	public ArquivoEletronico getArquivoEletronicoObs() {
		return this.arquivoEletronicoObs;
	}

	public void setArquivoEletronicoObs(ArquivoEletronico arquivoEletronicoObs) {
		this.arquivoEletronicoObs = arquivoEletronicoObs;
	}

	@Column(name = "COD_CAPITULO", nullable = false, precision = 2, scale = 0)
	public Integer getCodigoCapitulo() {
		return this.codigoCapitulo;
	}

	public void setCodigoCapitulo(Integer codCapitulo) {
		this.codigoCapitulo = codCapitulo;
	}

	@Column(name = "COD_MATERIA", nullable = false, precision = 2, scale = 0)
	public Integer getCodigoMateria() {
		return this.codigoMateria;
	}

	public void setCodigoMateria(Integer codMateria) {
		this.codigoMateria = codMateria;
	}

	@Column(name = "ANO_MATERIA", nullable = false, precision = 4, scale = 0)
	public Short getAnoMateria() {
		return this.anoMateria;
	}

	public void setAnoMateria(Short anoMateria) {
		this.anoMateria = anoMateria;
	}

	@Column(name = "NUM_MATERIA", nullable = false, precision = 5, scale = 0)
	public Integer getNumeroMateria() {
		return this.numeroMateria;
	}

	public void setNumeroMateria(Integer numMateria) {
		this.numeroMateria = numMateria;
	}
	
	@OneToOne( fetch=FetchType.LAZY )
	@JoinColumns( { 
			@JoinColumn(name="SEQ_ARQUIVO_ELETRONICO_TEXTO", referencedColumnName = "SEQ_ARQUIVO_ELETRONICO", insertable=false, updatable=false),
			@JoinColumn(name="SEQ_OBJETO_INCIDENTE", referencedColumnName = "SEQ_OBJETO_INCIDENTE", insertable=false, updatable=false)	
	} )
	@NotFound(action=NotFoundAction.IGNORE)
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public Texto getTexto() {
		return texto;
	}

	public void setTexto(Texto texto) {
		this.texto = texto;
	}
	
	@OneToMany( fetch=FetchType.LAZY )
	@JoinColumns( { 
			@JoinColumn(name="COD_CAPITULO", referencedColumnName = "COD_CAPITULO", insertable=false, updatable=false), 
			@JoinColumn(name="COD_MATERIA", referencedColumnName = "COD_MATERIA", insertable=false, updatable=false),
			@JoinColumn(name="NUM_MATERIA", referencedColumnName = "NUM_MATERIA", insertable=false, updatable=false),
			@JoinColumn(name="ANO_MATERIA", referencedColumnName = "ANO_MATERIA", insertable=false, updatable=false)
	} )
	@LazyToOne(LazyToOneOption.NO_PROXY)
	public List<ConteudoPublicacao> getConteudosPublicacao() {
		return conteudosPublicacao;
	}
	public void setConteudosPublicacao(List<ConteudoPublicacao> conteudosPublicacao) {
		this.conteudosPublicacao = conteudosPublicacao;
	}
	
	@Transient
	public ConteudoPublicacao getConteudoPublicacao(){
		if( conteudosPublicacao != null && conteudosPublicacao.size() > 0 ){
			return conteudosPublicacao.get(0);
		}
		return null;
	}
	
	@Transient
	public String getDescricaoProcesso() {

		return getObjetoIncidente().getIdentificacaoCompleta();
	}
	
	@Transient
	public Processo getProcesso() {
		return ObjetoIncidenteUtil.getProcesso(getObjetoIncidente());
	}
	
	@Transient
	public Boolean getIsEletronico() {
		if ((getProcesso() != null && getProcesso().getTipoMeioProcesso() != null && TipoMeioProcesso.ELETRONICO.getCodigo().
				equals(getProcesso().getTipoMeioProcesso().getCodigo())))
			return true;
		else
			return false;
	}

}
