package br.jus.stf.estf.decisao.api;

public class ReferendarDecisaoDto {
	private Long objetoIncidente;
	private Long ministro;
	private String colegiado;
	private String tipoLiberacao;
	private Boolean ignorarCpc;
	
	public ReferendarDecisaoDto() {
	}
	
	public ReferendarDecisaoDto(Long objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	public ReferendarDecisaoDto(Long objetoIncidente, Boolean ignorarCpc) {
		this.objetoIncidente = objetoIncidente;
		this.ignorarCpc = ignorarCpc;
	}
	public Long getObjetoIncidente() {
		return objetoIncidente;
	}
	public void setObjetoIncidente(Long objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}
	public Long getMinistro() {
		return ministro;
	}
	public void setMinistro(Long ministro) {
		this.ministro = ministro;
	}
	public String getColegiado() {
		return colegiado;
	}
	public void setColegiado(String colegiado) {
		this.colegiado = colegiado;
	}
	public String getTipoLiberacao() {
		return tipoLiberacao;
	}
	public void setTipoLiberacao(String tipoLiberacao) {
		this.tipoLiberacao = tipoLiberacao;
	}
	public Boolean getIgnorarCpc() {
		return ignorarCpc;
	}
	public void setIgnorarCpc(Boolean ignorarCpc) {
		this.ignorarCpc = ignorarCpc;
	}
}