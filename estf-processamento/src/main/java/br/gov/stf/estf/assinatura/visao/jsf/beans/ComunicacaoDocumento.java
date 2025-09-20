package br.gov.stf.estf.assinatura.visao.jsf.beans;

import br.gov.stf.estf.assinatura.service.ComunicacaoDocumentoBase;
import br.gov.stf.estf.assinatura.visao.servlet.VerPDFServlet;
import br.gov.stf.estf.assinatura.visao.servlet.VerPDFUnificadoServlet;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronicoComunicacao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao.FlagGenericaModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.montadortexto.MontadorTextoServiceException;

/**
 * Classe utilitária que empacota um objeto ComunicacaoDocumentoResult.
 */
public class ComunicacaoDocumento extends ComunicacaoDocumentoBase {

	private static final long serialVersionUID = -9060704139524868503L;
	private static final long maximaTamanho = 2147483648L;
	
	private ComunicacaoDocumentoResult documentoComunicacao;
	private Boolean possuiPDF;
	private String linkPDF;
	private Boolean checkInativo;
	private byte[] conteudoPDFUnico;
	private String linkPDFUnico;
	private String linkPecaUnico;
	private String linkPecaTodas;
	private Boolean restrito;

	public ComunicacaoDocumento(ComunicacaoDocumentoResult comunicacaoDocumento) {
		this.documentoComunicacao = comunicacaoDocumento;
	}

	public ComunicacaoDocumento() {

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


	
	@SuppressWarnings("unlikely-arg-type")
	public Boolean getRestrito() throws ServiceException {
		
			if (getDocumentoComunicacao() != null && getDocumentoComunicacao().getComunicacao().getFaseComunicacaoAtual() !=null && getDocumentoComunicacao().getComunicacao().getFaseComunicacaoAtual().getTipoFase().getCodigoFase().equals(TipoFaseComunicacao.RESTRITOS.getCodigoFase())) {
				restrito = true;
			} else {
				restrito = false;
			}
		return restrito;
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
			return comunicacao.getDscNomeDocumento().replaceAll("[ãâàáä]", "a").replaceAll("[êèéë]", "e").replaceAll("[îìíï]", "i").replaceAll("[õôòóö]", "o")
					.replaceAll("[ûúùü]", "u").replaceAll("[ÃÂÀÁÄ]", "A").replaceAll("[ÊÈÉË]", "E").replaceAll("[ÎÌÍÏ]", "I").replaceAll("[ÕÔÒÓÖ]", "O")
					.replaceAll("[ÛÙÚÜ]", "U").replace('ç', 'c').replace('Ç', 'C').replace('ñ', 'n').replace('Ñ', 'N').replaceAll("!", "")
					.replaceAll("\\[\\´\\`\\?!\\@\\#\\$\\%\\¨\\*", " ").replaceAll("\\(\\)\\=\\{\\}\\[\\]\\~\\^\\]", " ")
					.replaceAll("[\\.\\;\\-\\_\\+\\'\\ª\\º\\:\\;\\/]", " ");
		} else {
			return documentoComunicacao.getNomeDocumento();
		}
	}

	public String getLinkPDF() throws ServiceException {
		if (linkPDF == null) {
			if(getDocumentoComunicacao().getDocumentoEletronicoView() != null){
			this.linkPDF = "../../verPDFServlet?" + VerPDFServlet.PARAM_SEQ_DOCUMENTO_ELETRONICO + "="
					+ getDocumentoComunicacao().getDocumentoEletronicoView().getId() + "&" + VerPDFServlet.PARAM_NOME_DOCUMENTO + "="
					+ getIdentificacaoComunicacao().replaceAll(" ", "_");
			}
			if (getComunicacao().getConfidencialidade() != null && !getComunicacao().getConfidencialidade().isEmpty())
				this.linkPDF += "&" + VerPDFServlet.PARAM_CONFIDENCIALIDADE + "=" + 
					  getComunicacao().getConfidencialidade().replaceAll(" ","_").replaceAll("ç","c");
		}

		return linkPDF;
	}
	
	public String getLinkPDFUnico() throws MontadorTextoServiceException{
		this.linkPDFUnico = "../../verPDFUnificadoServlet?" + VerPDFUnificadoServlet.PARAM_SEQ_COMUNICACAO + "="
			+ getComunicacao().getId() + "&" + VerPDFUnificadoServlet.PARAM_SEQ_DOCUMENTO_COMUNICACAO+ "="
			+ getDocumentoComunicacao().getDocumentoEletronicoView().getId() + "&" + VerPDFUnificadoServlet.PARAM_TIPO_ARQUIVO+"=PDF";
		
		return linkPDFUnico;
	}
	
	public String getLinkPecaUnico() throws MontadorTextoServiceException{
		Long tamPecas = 0L;
		Boolean limite = true; 
		for(PecaProcessoEletronicoComunicacao docAtual : getComunicacao().getPecasProcessoEletronico()) {
			tamPecas  = tamPecas + docAtual.getPecaProcessoEletronico().getDocumentos().get(0).getDocumentoEletronicoView().getNumTamanhoDocumento();
			if(tamPecas > maximaTamanho ) {
				limite = false;
				break;
			}
		}
		this.linkPecaUnico = "../../verPDFUnificadoServlet?" + VerPDFUnificadoServlet.PARAM_SEQ_COMUNICACAO + "="
			+ getComunicacao().getId() + "&" + VerPDFUnificadoServlet.PARAM_SEQ_DOCUMENTO_COMUNICACAO+ "="
			+ getDocumentoComunicacao().getDocumentoEletronicoView().getId() + "&" + VerPDFUnificadoServlet.PARAM_TIPO_ARQUIVO+"=MID";
		if(!limite) {
			this.linkPecaUnico ="#";
		}
		return linkPecaUnico;
	}
	
	public String getLinkPecaTodas() throws MontadorTextoServiceException{
		Long tamPecas = 0L;
		Boolean limite = true; 
		for(PecaProcessoEletronicoComunicacao docAtual : getComunicacao().getPecasProcessoEletronico()) {
			tamPecas  = tamPecas + docAtual.getPecaProcessoEletronico().getDocumentos().get(0).getDocumentoEletronicoView().getNumTamanhoDocumento();
			if(tamPecas > maximaTamanho ) {
				limite = false;
				break;
			}
		}

			this.linkPecaTodas = "../../verPDFUnificadoServlet?" + VerPDFUnificadoServlet.PARAM_SEQ_COMUNICACAO + "="
				+ getComunicacao().getId() + "&" + VerPDFUnificadoServlet.PARAM_SEQ_DOCUMENTO_COMUNICACAO+ "="
				+ getDocumentoComunicacao().getDocumentoEletronicoView().getId() + "&" + VerPDFUnificadoServlet.PARAM_TIPO_ARQUIVO+"=TODAS";
			if(!limite) {
				this.linkPecaTodas ="#";
			}
		return linkPecaTodas;
	}
	
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