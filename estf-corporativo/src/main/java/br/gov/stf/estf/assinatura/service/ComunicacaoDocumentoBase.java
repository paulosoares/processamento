package br.gov.stf.estf.assinatura.service;

import java.io.Serializable;

import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao.FlagGenericaModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.montadortexto.MontadorTextoServiceException;

/**
 * Classe utilit·ria que empacota um objeto ComunicacaoDocumentoResult.
 */
public abstract class ComunicacaoDocumentoBase implements Serializable {

	private static final long serialVersionUID = 1L;

	private ComunicacaoDocumentoResult documentoComunicacao;
	private Boolean possuiPDF;
	private Boolean checkInativo;
	private byte[] conteudoPDFUnico;

	public ComunicacaoDocumentoBase(ComunicacaoDocumentoResult comunicacaoDocumento) {
		this.documentoComunicacao = comunicacaoDocumento;
	}

	public ComunicacaoDocumentoBase() {

	}

	public Boolean getCheckInativo() {
		if (checkInativo == null) {
			checkInativo = false;
			if (documentoComunicacao.getDocumentoComunicacao() == null) {
				checkInativo = true;
			}
		}
		return checkInativo;
	}

	public Boolean getPossuiPDF() throws ServiceException {
		if (possuiPDF == null) {
			if (getDocumentoComunicacao() == null) {
				possuiPDF = false;
			} else {
				possuiPDF = true;
			}
		}
		return possuiPDF;
	}

	public ComunicacaoDocumentoResult getDocumentoComunicacaoResult() {
		return this.documentoComunicacao;
	}

	public Comunicacao getComunicacao() {
		return documentoComunicacao.getComunicacao();
	}

	public br.gov.stf.estf.entidade.documento.DocumentoComunicacao getDocumentoComunicacao() {
		return documentoComunicacao.getDocumentoComunicacao();
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
			return documentoComunicacao.getNomeDocumento();
		}
	}

	public abstract String getLinkPDF() throws ServiceException;
	
	public abstract String getLinkPDFUnico() throws MontadorTextoServiceException;

	public boolean podeSerEncaminhadoParaDJe() {
		return getComunicacao().getModeloComunicacao().getFlagEncaminharParaDJe() == FlagGenericaModeloComunicacao.S;
	}

	public boolean isFaseAtualComunicacao(TipoFaseComunicacao... fases) {
		String faseAtual = getComunicacao().getFaseAtual();

		for (TipoFaseComunicacao tipoFaseComunicacao : fases) {
			if (faseAtual.contains(tipoFaseComunicacao.getDescricao())) {
				return true;
			}
		}

		return false;
	}

	public boolean isNecessariaAssinaturaMinistroComunicacao() {
		return getComunicacao().getModeloComunicacao().getFlagAssinaturaMinistro().equals(FlagGenericaModeloComunicacao.S);
	}

	public boolean isSituacaoDocumentoComunicacao(TipoSituacaoDocumento situacao) {
		return getDocumentoComunicacao().getTipoSituacaoDocumento().equals(situacao);
	}

	public byte[] getConteudoPDFUnico() {
		return conteudoPDFUnico;
	}

	public void setConteudoPDFUnico(byte[] conteudoPDFUnico) {
		this.conteudoPDFUnico = conteudoPDFUnico;
	}
}