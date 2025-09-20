package br.gov.stf.estf.intimacao.visao.dto;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.TipoIncidentePreferencia;

public class ComunicacaoExternaDTO {

	private long idComunicacao;

	private long idProcessoIncidente;

	private String modelo;

	private String processo;

	private Date dataComunicacao;

	private Date dataEnvio;

	private Date dataRecebimento;

	private String formaRecebimento;

	private String usuarioRecebeuComunicacao;

	private String nomePessoaRecebeuComunicacao;

	private String situacao;

	private String orgaoIntimado;

	private String descricaoTipoComunicacao;

	private List<TipoIncidentePreferencia> tiposPreferencias;

	public long getIdComunicacao() {
		return idComunicacao;
	}

	public void setIdComunicacao(long idComunicacao) {
		this.idComunicacao = idComunicacao;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getProcesso() {
		return processo;
	}

	public void setProcesso(String processo) {
		this.processo = processo;
	}

	public Date getDataComunicacao() {
		return dataComunicacao;
	}

	public void setDataComunicacao(Date dataComunicacao) {
		this.dataComunicacao = dataComunicacao;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public String getFormaRecebimento() {
		return formaRecebimento;
	}

	public void setFormaRecebimento(String formaRecebimento) {
		this.formaRecebimento = formaRecebimento;
	}

	public String getUsuarioRecebeuComunicacao() {
		return usuarioRecebeuComunicacao;
	}

	public void setUsuarioRecebeuComunicacao(String usuarioRecebeuComunicacao) {
		this.usuarioRecebeuComunicacao = usuarioRecebeuComunicacao;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getOrgaoIntimado() {
		return orgaoIntimado;
	}

	public void setOrgaoIntimado(String orgaoIntimado) {
		this.orgaoIntimado = orgaoIntimado;
	}

	public String getDescricaoTipoComunicacao() {
		return descricaoTipoComunicacao;
	}

	public void setDescricaoTipoComunicacao(String descricaoTipoComunicacao) {
		this.descricaoTipoComunicacao = descricaoTipoComunicacao;
	}

	public List<TipoIncidentePreferencia> getTiposPreferencias() {
		return tiposPreferencias;
	}

	public void setTiposPreferencias(
			List<TipoIncidentePreferencia> tiposPreferencias) {
		this.tiposPreferencias = tiposPreferencias;
	}

	public long getIdProcessoIncidente() {
		return idProcessoIncidente;
	}

	public void setIdProcessoIncidente(long idProcessoIncidente) {
		this.idProcessoIncidente = idProcessoIncidente;
	}

	public String getNomePessoaRecebeuComunicacao() {
		return nomePessoaRecebeuComunicacao;
	}

	public void setNomePessoaRecebeuComunicacao(
			String nomePessoaRecebeuComunicacao) {
		this.nomePessoaRecebeuComunicacao = nomePessoaRecebeuComunicacao;
	}

}
