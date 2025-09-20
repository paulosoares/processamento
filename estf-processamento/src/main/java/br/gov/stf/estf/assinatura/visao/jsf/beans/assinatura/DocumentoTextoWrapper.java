package br.gov.stf.estf.assinatura.visao.jsf.beans.assinatura;

import java.util.LinkedList;
import java.util.List;

import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class DocumentoTextoWrapper extends CheckableDataTableRowWrapper{

	private static final long serialVersionUID = 3145939155267567846L;
	
	private boolean indentar;

	public DocumentoTextoWrapper(Object wrappedObject) {
		super(wrappedObject);
	}
	
	public static List<DocumentoTextoWrapper> getDocumentoTextoWrapperList(List<DocumentoTexto> listaDocumentoTexto){
		List<DocumentoTextoWrapper> listaDocumentoTextoWrapper = new LinkedList<DocumentoTextoWrapper>();
		
		DocumentoTexto docTextoAnt = null;
		
		for(DocumentoTexto docTexto : listaDocumentoTexto){
			DocumentoTextoWrapper wrapper = new DocumentoTextoWrapper(docTexto);
			if(docTextoAnt != null){
				if(docTexto.getTexto().getArquivoEletronico().getId().equals(docTextoAnt.getTexto().getArquivoEletronico().getId())){
					wrapper.indentar = true;
				}else{
					wrapper.indentar = false;
				}
			}else{
				wrapper.indentar = false;
			}
			docTextoAnt = docTexto;
			listaDocumentoTextoWrapper.add(wrapper);
		}
		
		return listaDocumentoTextoWrapper;
		
	}

	public boolean isIndentar() {
		return indentar;
	}

}
