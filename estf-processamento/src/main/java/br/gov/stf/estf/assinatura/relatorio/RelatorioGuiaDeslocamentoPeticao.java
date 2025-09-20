package br.gov.stf.estf.assinatura.relatorio;

/**
 * Classe que contém os dados do Relatorio de Guia de Deslocamento de Peticao
 * @author Demetrius.Jube
 *
 */
public class RelatorioGuiaDeslocamentoPeticao extends RelatorioGuiaDeslocamento {
	private String siglaClasseProcesso;
	private Long numeroProcesso;
	private Long numeroPeticao;
	private Short anoPeticao;
	private String tipoMeio;

	public String getSiglaClasseProcesso() {
		return siglaClasseProcesso;
	}
	public void setSiglaClasseProcesso(String siglaClasseProcesso) {
		this.siglaClasseProcesso = siglaClasseProcesso;
	}
	public Long getNumeroPeticao() {
		return numeroPeticao;
	}
	public void setNumeroPeticao(Long numeroPeticao) {
		this.numeroPeticao = numeroPeticao;
	}
	public Short getAnoPeticao() {
		return anoPeticao;
	}
	public void setAnoPeticao(Short anoPeticao) {
		this.anoPeticao = anoPeticao;
	}
	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}
	public Long getNumeroProcesso() {
		return numeroProcesso;
	}
	public String getTipoMeio() {
		return tipoMeio;
	}
	public void setTipoMeio(String tipoMeio) {
		this.tipoMeio = tipoMeio;
	}
}
