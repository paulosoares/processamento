package br.gov.stf.estf.assinatura.security;

import java.io.Serializable;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.framework.model.service.ServiceException;

public class ExpedienteAssinadoResult implements Serializable {

	private static final long serialVersionUID = 997200552739952386L;
	
	private Long seqComunicacao;
	private Long objetoIncidente;
	private String nomeProcesso;
	private String descricaoDocumento;
	private String dataAssinatura;
	private String assinador;
	private String faseAtual;
	private String dataFase;
	private String ultimo;
	private DocumentoComunicacao documentoComunicacao;
	
	public ExpedienteAssinadoResult(Long seqComunicacao, Long objetoIncidente, String nomeProcesso, 
									String descricaoDocumento, String dataAssinatura, String assinador,
									String faseAtual, String dataFase, String ultimo, DocumentoComunicacao documentoComunicacao) {
		
		this.seqComunicacao = seqComunicacao;
		this.objetoIncidente = objetoIncidente;
		this.nomeProcesso = nomeProcesso;
		this.descricaoDocumento = descricaoDocumento;
		this.dataAssinatura = dataAssinatura;
		this.assinador = assinador;
		this.faseAtual = faseAtual;
		this.dataFase = dataFase;
		this.ultimo = ultimo;
		this.documentoComunicacao = documentoComunicacao;
		
	}
	
	public ExpedienteAssinadoResult(){
	}

	public Long getObjetoIncidente() {
		return objetoIncidente;
	}

	public void setObjetoIncidente(Long objetoIncidente) {
		this.objetoIncidente = objetoIncidente;
	}

	public String getNomeProcesso() {
		return nomeProcesso;
	}

	public void setNomeProcesso(String nomeProcesso) {
		this.nomeProcesso = nomeProcesso;
	}

	public String getDescricaoDocumento() {
		return descricaoDocumento;
	}

	public void setDescricaoDocumento(String descricaoDocumento) {
		this.descricaoDocumento = descricaoDocumento;
	}

	public String getDataAssinatura() {
		return dataAssinatura;
	}

	public void setDataAssinatura(String dataAssinatura) {
		this.dataAssinatura = dataAssinatura;
	}

	public String getAssinador() {
		return assinador;
	}

	public void setAssinador(String assinador) {
		this.assinador = assinador;
	}

	public String getFaseAtual() {
		return faseAtual;
	}

	public void setFaseAtual(String faseAtual) {
		this.faseAtual = faseAtual;
	}

	public String getDataFase() {
		return dataFase;
	}

	public void setDataFase(String dataFase) {
		this.dataFase = dataFase;
	}

	public String getUltimo() {
		return ultimo;
	}

	public void setUltimo(String ultimo) {
		this.ultimo = ultimo;
	}

	public Long getSeqComunicacao() {
		return seqComunicacao;
	}

	public void setSeqComunicacao(Long seqComunicacao) {
		this.seqComunicacao = seqComunicacao;
	}
	
	public String getIdentificacaoComunicacao() {
		Comunicacao comunicacao = documentoComunicacao.getComunicacao();

		if (comunicacao != null) {
			return comunicacao.getDscNomeDocumento().replaceAll("[„‚‡·‰]", "a").replaceAll("[ÍËÈÎ]", "e").replaceAll("[ÓÏÌÔ]", "i").replaceAll("[ıÙÚÛˆ]", "o")
					.replaceAll("[˚˙˘¸]", "u").replaceAll("[√¬¿¡ƒ]", "A").replaceAll("[ »…À]", "E").replaceAll("[ŒÃÕœ]", "I").replaceAll("[’‘“”÷]", "O")
					.replaceAll("[€Ÿ⁄‹]", "U").replace('Á', 'c').replace('«', 'C').replace('Ò', 'n').replace('—', 'N').replaceAll("!", "")
					.replaceAll("\\[\\¥\\`\\?!\\@\\#\\$\\%\\®\\*", " ").replaceAll("\\(\\)\\=\\{\\}\\[\\]\\~\\^\\]", " ")
					.replaceAll("[\\.\\;\\-\\_\\+\\'\\™\\∫\\:\\;\\/]", " ");
		} else {
			return documentoComunicacao.getDscObservacao();
		}
	}

	public Boolean getPossuiPDF() throws ServiceException {
		boolean possuiPDF = false;
		if (getDocumentoComunicacao() != null) {
			possuiPDF = true;
		} 
		return possuiPDF;
	}

	public DocumentoComunicacao getDocumentoComunicacao() {
		return documentoComunicacao;
	}

	public void setDocumentoComunicacao(DocumentoComunicacao documentoComunicacao) {
		this.documentoComunicacao = documentoComunicacao;
	}

}
