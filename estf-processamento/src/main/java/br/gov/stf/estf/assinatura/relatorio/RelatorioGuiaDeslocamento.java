package br.gov.stf.estf.assinatura.relatorio;

import java.util.Date;

/**
 * Bean utilizado para imprimir o Relatório de Guia de Deslocamento. Possui os dados comuns
 * para o deslocamento de Processos e de Petições. 
 * @author Demetrius.Jube
 *
 */
public class RelatorioGuiaDeslocamento {

	private Long numeroDaGuia;
	private Short anoDaGuia;
	private Long codigoOrgaoOrigem;
	private String descricaoOrgaoOrigem;
	private Long codigoOrgaoDestino;
	private String descricaoOrgaoDestino;
	private String observacao;
	private Date dataRemessa;
	private Date dataRecebimento;
	// dados da folha postal
	private String enderecoOrgaoDestino;
	private String municipioOrgaoDestino;
	private String ufOrgaoDestino;
	private String cepOrgaoDestino;
	private String numLocalizacao;
	private String bairro;
	private String complemento;
	private String destinatario;
	private Long codigoDestinatario;


	public Long getNumeroDaGuia() {
		return numeroDaGuia;
	}

	public void setNumeroDaGuia(Long numeroDaGuia) {
		this.numeroDaGuia = numeroDaGuia;
	}

	public Short getAnoDaGuia() {
		return anoDaGuia;
	}

	public void setAnoDaGuia(Short anoDaGuia) {
		this.anoDaGuia = anoDaGuia;
	}

	public Long getCodigoOrgaoOrigem() {
		return codigoOrgaoOrigem;
	}

	public void setCodigoOrgaoOrigem(Long codigoOrgaoOrigem) {
		this.codigoOrgaoOrigem = codigoOrgaoOrigem;
	}

	public String getDescricaoOrgaoOrigem() {
		return descricaoOrgaoOrigem;
	}

	public void setDescricaoOrgaoOrigem(String descricaoOrgaoOrigem) {
		this.descricaoOrgaoOrigem = descricaoOrgaoOrigem;
	}

	public Long getCodigoOrgaoDestino() {
		return codigoOrgaoDestino;
	}

	public void setCodigoOrgaoDestino(Long codigoOrgaoDestino) {
		this.codigoOrgaoDestino = codigoOrgaoDestino;
	}

	public String getDescricaoOrgaoDestino() {
		return descricaoOrgaoDestino;
	}

	public void setDescricaoOrgaoDestino(String descricaoOrgaoDestino) {
		this.descricaoOrgaoDestino = descricaoOrgaoDestino;
	}
	
	public String getEnderecoOrgaoDestino() {
		return enderecoOrgaoDestino;
	}

	public void setEnderecoOrgaoDestino(String enderecoOrgaoDestino) {
		this.enderecoOrgaoDestino = enderecoOrgaoDestino;
	}

	public String getMunicipioOrgaoDestino() {
		return municipioOrgaoDestino;
	}

	public void setMunicipioOrgaoDestino(String municipioOrgaoDestino) {
		this.municipioOrgaoDestino = municipioOrgaoDestino;
	}

	public String getUfOrgaoDestino() {
		return ufOrgaoDestino;
	}

	public void setUfOrgaoDestino(String ufOrgaoDestino) {
		this.ufOrgaoDestino = ufOrgaoDestino;
	}

	public String getCepOrgaoDestino() {
		return cepOrgaoDestino;
	}

	public void setCepOrgaoDestino(String cepOrgaoDestino) {
		this.cepOrgaoDestino = cepOrgaoDestino;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setDataRemessa(Date dataRemessa) {
		this.dataRemessa = dataRemessa;
	}

	public Date getDataRemessa() {
		return dataRemessa;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setNumLocalizacao(String numLocalizacao) {
		this.numLocalizacao = numLocalizacao;
	}

	public String getNumLocalizacao() {
		return numLocalizacao;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public Long getCodigoDestinatario() {
		return codigoDestinatario;
	}

	public void setCodigoDestinatario(Long codigoDestinatario) {
		this.codigoDestinatario = codigoDestinatario;
	}
}
