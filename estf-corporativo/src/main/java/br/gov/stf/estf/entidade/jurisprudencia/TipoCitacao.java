/**
 * 
 */
package br.gov.stf.estf.entidade.jurisprudencia;

/**
 * @author Paulo.Estevao
 * @since 17.10.2012
 */
public enum TipoCitacao {

	ACORDAO("A", "Acórdão"),
	DECISAO_MONOCRATICA("D", "Decisão Monocrática");
	
	private String codigo;
	private String descricao;
	
	private TipoCitacao(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public static TipoCitacao valueOfCodigo(String codigo) {
		for (TipoCitacao tipoCitacao : TipoCitacao.values()) {
			if (tipoCitacao.getCodigo().equals(codigo)) {
				return tipoCitacao;
			}
		}
		
		return null;
	}
}
