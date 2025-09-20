package br.gov.stf.estf.assinatura.relatorio;

public class RelatorioAutosEmprestadosOrgaosExternos extends RelatorioAutosEmprestados {

	private String descricaoOrgaoExterno;
	private Long numeroOrgaoDestino;

	public String getDescricaoOrgaoExterno() {
		return descricaoOrgaoExterno;
	}

	public void setDescricaoOrgaoExterno(String descricaoOrgaoExterno) {
		this.descricaoOrgaoExterno = descricaoOrgaoExterno;
	}

	public Long getNumeroOrgaoDestino() {
		return numeroOrgaoDestino;
	}

	public void setNumeroOrgaoDestino(Long numeroOrgaoDestino) {
		this.numeroOrgaoDestino = numeroOrgaoDestino;
	}
	
	
	
	
}
