package br.gov.stf.estf.processostf.model.util;


public enum TipoControleEnum {
	
	CUMPRIMENTO_DESP_DESC(1),
	EXPEDIENTE_EXPEDIDOS(2);
	
	private int codigo;
	
	private TipoControleEnum(int codigo) {
		this.codigo = codigo;
	}
	
	public int getCodigo() {
		return codigo;
	}
	

}
