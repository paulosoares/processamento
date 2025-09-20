/**
 * 
 */
package br.gov.stf.estf.entidade.usuario;

/**
 * @author Paulo.Estevao
 * @since 04.09.2013
 */
public enum TipoDefinicao {

	PARAMETRO_VALOR("PAR"), XML("XML"), JSON("JSN");
	
	private String codigo;
	
	private TipoDefinicao(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public static TipoDefinicao valueOfCodigo(String codigo) {
		for (TipoDefinicao definicao : values()) {
			if (definicao.getCodigo().equals(codigo)) {
				return definicao;
			}
		}
		return null;
	}
}
