package br.gov.stf.estf.entidade.documento;


public enum TipoRecebimentoComunicacao {

	LEITURA_AVISO(1L, "Leitura de Aviso"), 
	LEITURA_PECA_PROCESSUAL(2L, "Leitura de Peça Processual"), 
	DECURSO_PRAZO(3L, "Decurso de Prazo");

	private final Long codigo;
	private final String descricao;

	private TipoRecebimentoComunicacao(Long codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public static TipoRecebimentoComunicacao valueOf(Long codigo) {
		if (codigo != null) {
			for (TipoRecebimentoComunicacao tipoRecebimento : values()) {
				if (codigo.equals(tipoRecebimento.getCodigo())) {
					return tipoRecebimento;
				}
			}
		} else {
			return null;
		}
		throw new RuntimeException("Não existe TipoRecebimentoComunicacao com codigo: " + codigo);
	}

	public Long getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}


}