package br.gov.stf.estf.assinatura.relatorio;

public class RelatorioAutosEmprestadosAdvogados extends RelatorioAutosEmprestados {

	private String nomeAdvogado;
	private Long codigoAdvogado;
	
	public String getNomeAdvogado() {
		return nomeAdvogado;
	}
	public void setNomeAdvogado(String nomeAdvogado) {
		this.nomeAdvogado = nomeAdvogado;
	}
	public Long getCodigoAdvogado() {
		return codigoAdvogado;
	}
	public void setCodigoAdvogado(Long codigoAdvogado) {
		this.codigoAdvogado = codigoAdvogado;
	}
}
