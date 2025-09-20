package br.gov.stf.estf.assinatura.relatorio;

import java.util.Date;

/**
 * Bean utilizado para imprimir o Relatório de Autos Emprestados. Possui os dados comuns
 * para os relatórios de autos emprestados a advogados e órgão externo. 
 * @author RicardoLe
 *
 */

public class RelatorioAutosEmprestados {

	private String siglaClasseProcesso;
	private Long numeroProcesso;
	private Date dataRemessa;
	private String relator;
	private Integer tipoOrgaoDestino;
	private String descricaoDestino;

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
	public Date getDataRemessa() {
		return dataRemessa;
	}
	public void setDataRemessa(Date dataRemessa) {
		this.dataRemessa = dataRemessa;
	}
	public String getRelator() {
		return relator;
	}
	public void setRelator(String relator) {
		this.relator = relator;
	}
	public void setTipoOrgaoDestino(Integer tipoOrgaoDestino) {
		this.tipoOrgaoDestino = tipoOrgaoDestino;
	}
	public Integer getTipoOrgaoDestino() {
		return tipoOrgaoDestino;
	}
	public void setDescricaoDestino(String descricaoDestino) {
		this.descricaoDestino = descricaoDestino;
	}
	public String getDescricaoDestino() {
		return descricaoDestino;
	}
	
}
