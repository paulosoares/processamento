package br.gov.stf.estf.documento.model.util;

import java.util.Date;

import br.gov.stf.estf.entidade.documento.FlagTipoAssinatura;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoDocumentoTexto;

public class AssinaturaDto {

	private Texto texto;
	private TipoDocumentoTexto tipo;
	private byte[] conteudoAssinado;
	private String siglaSistema;
	private Long sequencialDocumentoEletronico;
	private String hashValidacao;
	private String usuarioLogado;
	private byte[] carimbo;
	private Date dataCarimboTempo;
	private String observacao;
	private FlagTipoAssinatura tipoAssinatura;
	private byte[] assinatura;
	private String subjectDN;

	public Texto getTexto() {
		return texto;
	}

	public void setTexto(Texto texto) {
		this.texto = texto;
	}

	public TipoDocumentoTexto getTipo() {
		return tipo;
	}

	public void setTipo(TipoDocumentoTexto tipo) {
		this.tipo = tipo;
	}

	public byte[] getConteudoAssinado() {
		return conteudoAssinado;
	}

	public void setConteudoAssinado(byte[] conteudoAssinado) {
		this.conteudoAssinado = conteudoAssinado;
	}

	public String getSiglaSistema() {
		return siglaSistema;
	}

	public void setSiglaSistema(String siglaSistema) {
		this.siglaSistema = siglaSistema;
	}

	public Long getSequencialDocumentoEletronico() {
		return sequencialDocumentoEletronico;
	}

	public void setSequencialDocumentoEletronico(
			Long sequencialDocumentoEletronico) {
		this.sequencialDocumentoEletronico = sequencialDocumentoEletronico;
	}

	public String getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(String usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public byte[] getCarimbo() {
		return carimbo;
	}

	public void setCarimbo(byte[] carimbo) {
		this.carimbo = carimbo;
	}

	public Date getDataCarimboTempo() {
		return dataCarimboTempo;
	}

	public void setDataCarimboTempo(Date dataCarimboTempo) {
		this.dataCarimboTempo = dataCarimboTempo;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public FlagTipoAssinatura getTipoAssinatura() {
		return tipoAssinatura;
	}

	public void setTipoAssinatura(FlagTipoAssinatura tipoAssinatura) {
		this.tipoAssinatura = tipoAssinatura;
	}

	public byte[] getAssinatura() {
		return assinatura;
	}

	public void setAssinatura(byte[] assinatura) {
		this.assinatura = assinatura;
	}

	public String getSubjectDN() {
		return subjectDN;
	}

	public void setSubjectDN(String subjectDN) {
		this.subjectDN = subjectDN;
	}

	public String getHashValidacao() {
		return hashValidacao;
	}

	public void setHashValidacao(String hashValidacao) {
		this.hashValidacao = hashValidacao;
	}	
}