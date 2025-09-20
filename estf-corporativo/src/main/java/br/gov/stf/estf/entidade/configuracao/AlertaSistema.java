package br.gov.stf.estf.entidade.configuracao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="ALERTA_SISTEMA", schema="GLOBAL")
public class AlertaSistema extends ESTFBaseEntity<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String chave;
	private String valor;
	private String siglaSistema;
	private Date dataInicial;
	private Date dataFinal;
	
	@Id
	@Column(name = "SEQ_ALERTA_SISTEMA")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "DSC_CHAVE_ALERTA")
	public String getChave() {
		return chave;
	}
	public void setChave(String chave) {
		this.chave = chave;
	}
	
	@Column(name = "TXT_VALOR_ALERTA")
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	@Column(name = "SIG_SISTEMA")
	public String getSiglaSistema() {
		return siglaSistema;
	}
	public void setSiglaSistema(String siglaSistema) {
		this.siglaSistema = siglaSistema;
	}
	
	@Column(name = "DAT_INICIAL")
	public Date getDataInicial() {
		return dataInicial;
	}
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}
	
	@Column(name = "DAT_FINAL")
	public Date getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}
}
