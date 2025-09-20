package br.gov.stf.estf.expedicao.model.util;

import java.io.Serializable;
import java.util.Date;

import br.gov.stf.estf.expedicao.model.dataaccess.hibernate.Util;

/**
 *
 * @author Roberio.Fernandes
 */
public class PesquisaListaRemessaDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long numeroListaRemessa;
	private Long anoListaRemessa;
	private Date dataCriacaoInicio;
	private Date dataCriacaoFim;
	private Date dataEnvioInicio;
	private Date dataEnvioFim;
	private String destinatario;
	private String documento;
	private String vinculo;
	private String guiaDeslocamento;
	private String observacao;
	private String malote;
	private String lacre;
	private String codigoRastreio;
	private String remessasListaRemessa;

	public Long getNumeroListaRemessa() {
		return numeroListaRemessa;
	}

	public void setNumeroListaRemessa(Long numeroListaRemessa) {
		this.numeroListaRemessa = numeroListaRemessa;
	}

	public Date getDataCriacaoInicio() {
		return dataCriacaoInicio;
	}

	public void setDataCriacaoInicio(Date dataCriacaoInicio) {
		this.dataCriacaoInicio = dataCriacaoInicio;
	}

	public Date getDataCriacaoFim() {
		return dataCriacaoFim;
	}

	public void setDataCriacaoFim(Date dataCriacaoFim) {
		this.dataCriacaoFim = dataCriacaoFim;
	}

	public Date getDataEnvioInicio() {
		return dataEnvioInicio;
	}

	public void setDataEnvioInicio(Date dataEnvioInicio) {
		this.dataEnvioInicio = dataEnvioInicio;
	}

	public Date getDataEnvioFim() {
		return dataEnvioFim;
	}

	public void setDataEnvioFim(Date dataEnvioFim) {
		this.dataEnvioFim = dataEnvioFim;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getVinculo() {
		return vinculo;
	}

	public void setVinculo(String vinculo) {
		this.vinculo = vinculo;
	}

	public String getGuiaDeslocamento() {
		return guiaDeslocamento;
	}

	public void setGuiaDeslocamento(String guiaDeslocamento) {
		this.guiaDeslocamento = guiaDeslocamento;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getMalote() {
		return malote;
	}

	public void setMalote(String malote) {
		this.malote = malote;
	}

	public String getLacre() {
		return lacre;
	}

	public void setLacre(String lacre) {
		this.lacre = lacre;
	}

	public void limpar() {
		numeroListaRemessa = null;
		dataCriacaoInicio = null;
		dataCriacaoFim = null;
		dataEnvioInicio = null;
		dataEnvioFim = null;
		destinatario = null;
		documento = null;
		vinculo = null;
		guiaDeslocamento = null;
		observacao = null;
		malote = null;
		lacre = null;
		codigoRastreio=null;
		anoListaRemessa=null;
		remessasListaRemessa=null;
	}

	public boolean isVazio() {
		return (Util.verificarObjetoNulloOuTextoVazio(true,
				numeroListaRemessa,
				dataCriacaoInicio,
				dataCriacaoFim,
				dataEnvioInicio,
				dataEnvioFim,
				destinatario,
				documento,
				vinculo,
				guiaDeslocamento,
				observacao,
				malote,
				lacre,
				codigoRastreio,
				anoListaRemessa,
				remessasListaRemessa));

	}

	public String getCodigoRastreio() {
		return codigoRastreio;
	}

	public void setCodigoRastreio(String codigoRastreio) {
		this.codigoRastreio = codigoRastreio;
	}
	
	public Long getAnoListaRemessa() {
		return anoListaRemessa;
	}
	
	public void setAnoListaRemessa(Long anoListaRemessa) {
		this.anoListaRemessa = anoListaRemessa;
	}

	public String getRemessasListaRemessa() {
		return remessasListaRemessa;
	}
	
	public void setRemessasListaRemessa(String remessasListaRemessa) {
		this.remessasListaRemessa = remessasListaRemessa;
	}
}