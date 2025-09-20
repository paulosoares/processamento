package  br.gov.stf.estf.assinatura.visao.jsf.beans.visualizarpdf;

import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.ApplicationFactory;

public class BeanVisualizarPDF{
	
	private DocumentoEletronicoService documentoEletronicoService;
	
	public BeanVisualizarPDF() {
		documentoEletronicoService = (DocumentoEletronicoService)ApplicationFactory
			.getInstance().getServiceLocator().getService("documentoEletronicoService");
	}
	
	public byte[] recuperarDocumentoPorId(Long id) throws ServiceException{
		return documentoEletronicoService.recuperarPorId(id).getArquivo();
	}
	

}
