/**
 * 
 */
package br.gov.stf.estf.entidade.julgamento;


/**
 * @author Paulo.Estevao
 * @since 14.09.2011
 */
public enum TipoSituacaoProcessoSessao {
	JULGADO("J", "Julgado"), NAO_JULGADO("N", "Não Julgado"), SUSPENSO("S", "Suspenso"), DESTAQUE("D","Destaque");
	
	private String sigla;
	
	private String descricao;
	
	private TipoSituacaoProcessoSessao(String sigla, String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}
	
	public String getSigla() {
		return sigla;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public static TipoSituacaoProcessoSessao valueOfSigla(String sigla) {
		for (TipoSituacaoProcessoSessao situacao : TipoSituacaoProcessoSessao.values()) {
			if (situacao.getSigla().equals(sigla)) {
				return situacao;
			}
		}
		
		return null;
	}
}