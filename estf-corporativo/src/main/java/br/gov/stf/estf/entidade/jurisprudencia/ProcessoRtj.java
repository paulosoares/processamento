package br.gov.stf.estf.entidade.jurisprudencia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Type;

import br.gov.stf.estf.entidade.ESTFAuditavelBaseEntity;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;

@Entity
@Table(schema = "STF", name = "PROCESSO_RTJ_UNIF")
public class ProcessoRtj extends ESTFAuditavelBaseEntity<Long> {

	private static final long serialVersionUID = -4315212380276463693L;
	
	private Long id;
	private String volume;
	private String datMesAno;
	private Long pagina;
	private Long mes;
	private Long ano;
	private String descricaoRtj;
	private ObjetoIncidente<ObjetoIncidente<?>> objetoIncidente;
	private Boolean fechamento;
	
	@Override
	@Id
	@Column(name = "SEQ_PROCESSO_RTJ_UNIF")
	@GeneratedValue(generator = "sequence", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "sequence", sequenceName = "STF.SEQ_PROCESSO_RTJ_UNIF", allocationSize = 1)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "NUM_VOLUME")
	public String getVolume() {
		return volume;
	}
	
	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	@Column(name = "DAT_MESANO")
	public String getDatMesAno() {
		return datMesAno;
	}
	
	public void setDatMesAno(String datMesAno) {
		this.datMesAno = datMesAno;
	}
	
	@Column(name = "NUM_PAGINA")
	public Long getPagina() {
		return pagina;
	}
	
	public void setPagina(Long pagina) {
		this.pagina = pagina;
	}
	
	@Column(name = "FLG_FECHAMENTO")
	@Type(type = "br.gov.stf.framework.model.dataaccess.hibernate.type.FlagUserType")
	public Boolean getFechamento() {
		return fechamento;
	}

	public void setFechamento(Boolean fechamento) {
		this.fechamento = fechamento;
	}
	
	@Transient
	private void separarMesAno(){
		String[] mesAnoSplitted = StringUtils.split(datMesAno, '/');
		
		if(mesAnoSplitted != null){
			mes = Long.valueOf(mesAnoSplitted[0]);
			ano = Long.valueOf(mesAnoSplitted[1]);
		}
	}

	@Transient
	public Long getMes() {
		if(mes == null) separarMesAno();
		return mes;
	}

	public void setMes(Long mes) {
		this.mes = mes;
	}

	@Transient
	public Long getAno() {
		if(mes == null) separarMesAno();
		return ano;
	}

	public void setAno(Long ano) {
		this.ano = ano;
	}
	
	@Transient
	private void montarDescricaoRtj(){
		if(volume != null && pagina != null)
			descricaoRtj = "RTJ "+volume+"/"+pagina;
	}

	@Transient
	public String getDescricaoRtj() {
		if(descricaoRtj == null) montarDescricaoRtj();
		return descricaoRtj;
	}

	public void setDescricaoRtj(String descricaoRtj) {
		this.descricaoRtj = descricaoRtj;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQ_OBJETO_INCIDENTE")
	public ObjetoIncidente<ObjetoIncidente<?>> getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(
			ObjetoIncidente<ObjetoIncidente<?>> objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

}
