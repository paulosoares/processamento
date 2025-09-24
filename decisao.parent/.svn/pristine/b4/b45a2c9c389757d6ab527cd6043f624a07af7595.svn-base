package br.jus.stf.estf.decisao.texto.support;

public enum SituacaoDoTextoParaPublicacao {

	NAO_PUBLICADO("Texto não publicado", true, true), PUBLICADO_NO_DJ("Texto publicado", true, false), ATA_DE_PUBLICACAO(
			"Texto consta em Ata de Publicação", true, false), PREPARADO_PARA_PUBLICACAO(
			"Texto se encontra preparado para Publicação", true, false), PUBLICADO_DJ_RTJ_SALA_SESSOES(
			"Texto já liberado para DJ, RTJ e Sala de Sessões", false, true);
	private String descricao;
	private boolean permiteLiberacaoParaPublicacao;
	private boolean permiteSuspensaoPublicacao;

	SituacaoDoTextoParaPublicacao(String descricao, boolean permiteLiberacaoParaPublicacao, boolean permiteSuspensaoPublicacao) {
		this.descricao = descricao;
		this.permiteLiberacaoParaPublicacao = permiteLiberacaoParaPublicacao;
		this.permiteSuspensaoPublicacao = permiteSuspensaoPublicacao;
	}

	public boolean isPermiteSuspensaoPublicacao() {
		return permiteSuspensaoPublicacao;
	}

	public boolean isPermiteLiberacaoParaPublicacao() {
		return permiteLiberacaoParaPublicacao;
	}

	public String getDescricao() {
		return descricao;
	}

}