package br.gov.stf.estf.entidade.configuracao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.stf.estf.entidade.ESTFBaseEntity;

@Entity
@Table(name="CONFIGURACAO_SISTEMA", schema="GLOBAL")
public class ConfiguracaoSistema extends ESTFBaseEntity<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String chave;
	private String valor;
	private String siglaSistema;
	
	@Id
	@Column(name = "SEQ_CONFIGURACAO_SISTEMA")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "DSC_CHAVE_CONFIGURACAO")
	public String getChave() {
		return chave;
	}
	public void setChave(String chave) {
		this.chave = chave;
	}
	
	@Column(name = "TXT_VALOR_CONFIGURACAO")
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
}
