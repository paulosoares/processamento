package br.gov.stf.estf.entidade.usuario;

public class CargaClasseProcessualTipoJulgamento implements java.io.Serializable {
	/*private ClasseProcessual classeProcessual;
	private TipoJulgamento tipoJulgamento;
	private Long peso;*/
	private String classeProcessual;
	private String tipoJulgamento;
	private Long carga;
	
	
	public String getClasseProcessual() {
		return classeProcessual;
	}
	public void setClasseProcessual(String classeProcessual) {
		this.classeProcessual = classeProcessual;
	}
	public String getTipoJulgamento() {
		return tipoJulgamento;
	}
	public void setTipoJulgamento(String tipoJulgamento) {
		this.tipoJulgamento = tipoJulgamento;
	}
	public Long getCarga() {
		return carga;
	}
	public void setCarga(Long carga) {
		this.carga = carga;
	}
	/*
	public Long getPeso() {
		return peso;
	}
	public void setPeso(Long peso) {
		this.peso = peso;
	}    	
	*/
}
