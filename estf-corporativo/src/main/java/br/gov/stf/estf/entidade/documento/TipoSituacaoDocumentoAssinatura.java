package br.gov.stf.estf.entidade.documento;

public enum TipoSituacaoDocumentoAssinatura {

	PENDENTE(0, "Pendente"), ASSINADO(1, "Assinado");

	private final long codigoAssinatura;
	private final String descricao;

	private TipoSituacaoDocumentoAssinatura(long codigoAssinatura, String descricao) {
		this.codigoAssinatura = codigoAssinatura;
		this.descricao = descricao;
	}
	
	public static TipoSituacaoDocumentoAssinatura valueOf(Long codigoAssinatura) {
		if (codigoAssinatura != null) {
			for (TipoSituacaoDocumentoAssinatura codigo : values()) {
				if (codigoAssinatura.equals(codigo.getCodigoAssinatura())) {
					return codigo;
				}
			}
		} else {
			return null;
		}
		throw new RuntimeException("Nao existe TipoSituacaoDocumentoAssinatura com codigo: " + codigoAssinatura);
	}

	public long getCodigoAssinatura() {
		return codigoAssinatura;
	}

	public String getDescricao() {
		return descricao;
	}

}
