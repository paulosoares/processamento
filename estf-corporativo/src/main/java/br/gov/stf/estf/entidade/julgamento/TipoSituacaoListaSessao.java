package br.gov.stf.estf.entidade.julgamento;


/**
 * @author Almir.oliveira
 * @since 05.12.2011
 */
public enum TipoSituacaoListaSessao {
	JULGADA("J"), NAO_JULGADA("N");
	
	private String sigla;
	
	private TipoSituacaoListaSessao(String sigla) {
		this.sigla = sigla;
	}
	
	public String getSigla() {
		return sigla;
	}
	
	public static TipoSituacaoListaSessao valueOfSigla(String sigla) {
		for (TipoSituacaoListaSessao situacao : TipoSituacaoListaSessao.values()) {
			if (situacao.getSigla().equals(sigla)) {
				return situacao;
			}
		}
		
		return null;
	}
}