package br.jus.stf.estf.decisao.api;

public class ReferendarDecisaoResultadoDto {
	private Long objetoIncidente;
	private Long referendo;
	private TipoMensagem tipoMensagem;
	private String mensagem;
	private Long sessao;

	public enum TipoMensagem {
		SUCESSO, FALHA
	}

	public ReferendarDecisaoResultadoDto(Long objetoIncidente, TipoMensagem tipoMensagem, String mensagem, Long sessao, Long referendo) {
		super();
		this.objetoIncidente = objetoIncidente;
		this.tipoMensagem = tipoMensagem;
		this.mensagem = mensagem;
		this.sessao = sessao;
		this.referendo = referendo;
	}

	public Long getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(Long objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	public TipoMensagem getTipoMensagem() {
		return tipoMensagem;
	}

	public void setTipoMensagem(TipoMensagem tipoMensagem) {
		this.tipoMensagem = tipoMensagem;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Long getSessao() {
		return sessao;
	}

	public void setSessao(Long sessao) {
		this.sessao = sessao;
	}

	public Long getReferendo() {
		return referendo;
	}

	public void setReferendo(Long referendo) {
		this.referendo = referendo;
	}
}
