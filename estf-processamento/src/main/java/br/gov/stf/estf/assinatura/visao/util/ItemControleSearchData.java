package br.gov.stf.estf.assinatura.visao.util;

import java.io.Serializable;

import br.gov.stf.estf.assinatura.visao.servlet.VerPDFServlet;
import br.gov.stf.estf.processostf.model.util.ItemControleResult;
import br.gov.stf.framework.model.service.ServiceException;

public class ItemControleSearchData implements Serializable{

	private static final long serialVersionUID = -6118333669254264278L;
	
	private ItemControleResult itemControleResult;
	private Boolean possuiPDF;
	private String linkPDF;
	
	public ItemControleSearchData(){
	}
	
	public ItemControleSearchData(ItemControleResult itemControleResult){
		this.itemControleResult = itemControleResult;
	}
	
	
	public ItemControleResult getItemControleResult() {
		return itemControleResult;
	}

	public void setItemControleResult(ItemControleResult itemControleResult) {
		this.itemControleResult = itemControleResult;
	}
	
	public String getLinkPDF() throws ServiceException {
		if (linkPDF == null && getPossuiPDF()) {
			this.linkPDF = "../../verPDFServlet?" + VerPDFServlet.PARAM_SEQ_DOCUMENTO_ELETRONICO + "="
					+ getItemControleResult().getArquivoProcessoEletronico().getDocumentoEletronicoView().getId() + "&" + VerPDFServlet.PARAM_NOME_DOCUMENTO + "="
					+ getItemControleResult().getArquivoProcessoEletronico().getPecaProcessoEletronico().getDescricaoPeca();
		}

		return linkPDF;
	}
	
	public Boolean getPossuiPDF() throws ServiceException {
		if (possuiPDF == null) {
			if (getItemControleResult().getArquivoProcessoEletronico() == null) {
				possuiPDF = false;
			} else {
				possuiPDF = true;
			}
		}
		return possuiPDF;
	}
	
}
