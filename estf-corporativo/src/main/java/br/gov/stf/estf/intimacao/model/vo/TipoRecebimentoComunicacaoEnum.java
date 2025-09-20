package br.gov.stf.estf.intimacao.model.vo;

import br.gov.stf.estf.entidade.documento.TipoRecebimentoComunicacao;

public enum TipoRecebimentoComunicacaoEnum {

	NAO_LIDA(null, "Não Lida"),
	LEITURA_AVISO(TipoRecebimentoComunicacao.LEITURA_AVISO, "Leitura de Aviso"), 
	LEITURA_PECA_PROCESSUAL(TipoRecebimentoComunicacao.LEITURA_PECA_PROCESSUAL, "Leitura de Peça Processual"), 
	DECURSO_PRAZO(TipoRecebimentoComunicacao.DECURSO_PRAZO, "Decurso de Prazo"),
	CANCELADA(null, "Cancelada");

	private final TipoRecebimentoComunicacao tipoRecebimentoComunicacao;
	private final String descricao;

	private TipoRecebimentoComunicacaoEnum(TipoRecebimentoComunicacao tipoRecebimentoComunicacao, String descricao) {
		this.tipoRecebimentoComunicacao = tipoRecebimentoComunicacao;
		this.descricao = descricao;
	}

	public TipoRecebimentoComunicacao getTipoRecebimentoComunicacao() {
		return tipoRecebimentoComunicacao;
	}

	public String getDescricao() {
		return descricao;
	}
}