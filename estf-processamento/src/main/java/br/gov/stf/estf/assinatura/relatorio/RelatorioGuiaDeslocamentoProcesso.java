package br.gov.stf.estf.assinatura.relatorio;

/**
 * Classe que contém os dados do Relatório de Guia de Deslocamento de Processo
 * @author Demetrius.Jube
 *
 */
public class RelatorioGuiaDeslocamentoProcesso extends RelatorioGuiaDeslocamento {
	private String siglaClasseProcesso;
	private Long numeroProcesso;
	private Integer quantidadeVolumes;
	private Integer quantidadeApensos;
	private Integer quantidadeJuntadaLinha;
	private Integer quantidadeVinculo;
	private Integer vinculado;
	private String tipoMeio;

	public String getSiglaClasseProcesso() {
		return siglaClasseProcesso;
	}

	public void setSiglaClasseProcesso(String siglaClasseProcesso) {
		this.siglaClasseProcesso = siglaClasseProcesso;
	}

	public Long getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public Integer getQuantidadeVolumes() {
		return quantidadeVolumes;
	}

	public void setQuantidadeVolumes(Integer quantidadeVolumes) {
		this.quantidadeVolumes = quantidadeVolumes;
	}

	public Integer getQuantidadeApensos() {
		return quantidadeApensos;
	}

	public void setQuantidadeApensos(Integer quantidadeApensos) {
		this.quantidadeApensos = quantidadeApensos;
	}

	public Integer getQuantidadeJuntadaLinha() {
		return quantidadeJuntadaLinha;
	}

	public void setQuantidadeJuntadaLinha(Integer quantidadeJuntadaLinha) {
		this.quantidadeJuntadaLinha = quantidadeJuntadaLinha;
	}
	public Integer getQuantidadeVinculo() {
		return quantidadeVinculo;
	}

	public void setQuantidadeVinculo(Integer quantidadeVinculo) {
		this.quantidadeVinculo = quantidadeVinculo;
	}

	public void setVinculado(Integer vinculado) {
		this.vinculado = vinculado;
	}

	public Integer getVinculado() {
		return vinculado;
	}

	public void setTipoMeio(String tipoMeio) {
		this.tipoMeio = tipoMeio;
	}

	public String getTipoMeio() {
		return tipoMeio;
	}

}
