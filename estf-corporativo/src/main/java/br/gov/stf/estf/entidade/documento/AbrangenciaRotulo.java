/**
 * 
 */
package br.gov.stf.estf.entidade.documento;

/**
 * @author Paulo.Estevao
 * @since 28.11.2013
 */
public enum AbrangenciaRotulo {

	SETORIAL("S"), INSTITUCIONAL("I");
	
	private String codigo;
	
	private AbrangenciaRotulo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public static AbrangenciaRotulo valueOfCodigo(String codigo) {
		if (codigo == null) {
			return null;
		}
		
		for (AbrangenciaRotulo abrangencia : values()) {
			if (abrangencia.getCodigo().equals(codigo)) {
				return abrangencia;
			}
		}
		
		return null;
	}
}
