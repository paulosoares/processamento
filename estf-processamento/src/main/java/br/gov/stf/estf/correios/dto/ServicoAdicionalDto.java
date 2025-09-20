package br.gov.stf.estf.correios.dto;

public class ServicoAdicionalDto {

    private String codigoServicoAdicional;

	public ServicoAdicionalDto(String codigoServicoAdicional) {
		super();
		this.codigoServicoAdicional = codigoServicoAdicional;
	}

	public String getCodigoServicoAdicional() {
		return codigoServicoAdicional;
	}

	public void setCodigoServicoAdicional(String codigoServicoAdicional) {
		this.codigoServicoAdicional = codigoServicoAdicional;
	}
}