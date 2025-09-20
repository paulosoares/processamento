package br.gov.stf.estf.entidade.documento;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFBaseEntity;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Protocolo;

@Entity
@Table(name = "TEXTO_PETICAOS", schema = "STF")
public class TextoPeticao extends ESTFBaseEntity<Long> {
	
	private static final long serialVersionUID = 3827319653328423196L;
	
	public static final String TIPO_TEXTO_DECISAO = "DC";
	public static final String TIPO_TEXTO_DESPACHO = "DP";
	public static final String TIPO_TEXTO_DEVOLUCAO = "DE";
	
	private String descricaoTexto;
	private Date dataComposicaoParcial;
	private Date dataComposicaoDj;
	private Date dataLiberacaoGab;
	private String tipoTexto;
	private Integer numeroMateria;
	private Short anoMateria;
	private Boolean textosIguais;
	private ArquivoEletronico arquivoEletronico;
	private ObjetoIncidente<?> objetoIncidente;
	
	@Transient
	private <T> T getObjetoIncidente(ObjetoIncidente<?> objetoIncidente, Class<T> objetoIncidenteClass) {
		if (objetoIncidenteClass.isInstance(objetoIncidente)) {
			return objetoIncidenteClass.cast(objetoIncidente);
		}
		return null;
	}

	@Transient
	public Long getNumero () {
		Protocolo protocolo = getObjetoIncidente(getObjetoIncidente(), Protocolo.class);
		Peticao peticao = getObjetoIncidente(getObjetoIncidente(), Peticao.class);
		// PETICAO
		if ( peticao!=null && protocolo ==null ) {
			return peticao.getNumeroPeticao();
		} 
		// PROTCOLO
		else if ( protocolo!=null && peticao==null ) {
			return protocolo.getNumeroProtocolo();
		} else {
			return null;
		}
	}
	
	@Transient
	public Short getAno () {
		Protocolo protocolo = getObjetoIncidente(getObjetoIncidente(), Protocolo.class);
		Peticao peticao = getObjetoIncidente(getObjetoIncidente(), Peticao.class);
		// PETICAO
		if ( peticao!=null && protocolo ==null ) {
			return peticao.getAnoPeticao();
		} 
		// PROTCOLO
		else if ( protocolo!=null && peticao==null ) {
			return protocolo.getAnoProtocolo();
		} else {
			return null;
		}
	}
	
	@Transient
	public Protocolo getProtocolo() {
		return getObjetoIncidente(getObjetoIncidente(), Protocolo.class);
	}
	
	@Transient
	public Peticao getPeticao() {
		return getObjetoIncidente(getObjetoIncidente(), Peticao.class);
	}

	@Id
	@Column(name = "SEQ_TEXTO_PETICAOS", nullable = false, precision = 8, scale = 0)
	public Long getId() {
		return this.id;
	}
	
	@Column(name = "DSC_TEXTO", nullable = false, length = 100)
	public String getDescricaoTexto() {
		return this.descricaoTexto;
	}

	public void setDescricaoTexto(String dscTexto) {
		this.descricaoTexto = dscTexto;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DAT_COMPOSICAO_PARCIAL", length = 7)
	public Date getDataComposicaoParcial() {
		return this.dataComposicaoParcial;
	}

	public void setDataComposicaoParcial(Date datComposicaoParcial) {
		this.dataComposicaoParcial = datComposicaoParcial;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DAT_COMPOSICAO_DJ", length = 7)
	public Date getDataComposicaoDj() {
		return this.dataComposicaoDj;
	}

	public void setDataComposicaoDj(Date datComposicaoDj) {
		this.dataComposicaoDj = datComposicaoDj;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DAT_LIBERACAO_GAB", length = 7)
	public Date getDataLiberacaoGabinete() {
		return this.dataLiberacaoGab;
	}

	public void setDataLiberacaoGabinete(Date datLiberacaoGab) {
		this.dataLiberacaoGab = datLiberacaoGab;
	}

	@Column(name = "TIP_TEXTO", length = 2)
	public String getTipoTexto() {
		return this.tipoTexto;
	}

	public void setTipoTexto(String tipTexto) {
		this.tipoTexto = tipTexto;
	}

	@Column(name = "NUM_MATERIA", precision = 5, scale = 0)
	public Integer getNumeroMateria() {
		return this.numeroMateria;
	}

	public void setNumeroMateria(Integer numMateria) {
		this.numeroMateria = numMateria;
	}

	@Column(name = "ANO_MATERIA", precision = 4, scale = 0)
	public Short getAnoMateria() {
		return this.anoMateria;
	}

	public void setAnoMateria(Short anoMateria) {
		this.anoMateria = anoMateria;
	}

	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	@Column(name = "FLG_TEXTOS_IGUAIS", length = 1)
	public Boolean getTextosIguais() {
		return this.textosIguais;
	}

	public void setTextosIguais(Boolean flgTextosIguais) {
		this.textosIguais = flgTextosIguais;
	}
	
	@ManyToOne(fetch = FetchType.LAZY )
	@JoinColumn(name = "SEQ_ARQUIVO_ELETRONICO",  insertable = false, updatable = false)
	public ArquivoEletronico getArquivoEletronico() {
		return arquivoEletronico;
	}
	
	public void setArquivoEletronico(ArquivoEletronico arquivoEletronico) {
		this.arquivoEletronico = arquivoEletronico;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente<?> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(ObjetoIncidente<?> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	
}