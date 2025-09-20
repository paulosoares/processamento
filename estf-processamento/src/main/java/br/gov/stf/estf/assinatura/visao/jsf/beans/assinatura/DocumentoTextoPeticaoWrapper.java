package br.gov.stf.estf.assinatura.visao.jsf.beans.assinatura;

import java.util.LinkedList;
import java.util.List;

import br.gov.stf.estf.entidade.documento.DocumentoTextoPeticao;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class DocumentoTextoPeticaoWrapper extends CheckableDataTableRowWrapper {

	private static final long serialVersionUID = -7681713778695157014L;
	private boolean indentar;
	
	public DocumentoTextoPeticaoWrapper(Object wrappedObject) {
		super(wrappedObject);
	}
	
	public static List<DocumentoTextoPeticaoWrapper> getDocumentoTextoPeticaosProtocoloWrapperList(List<DocumentoTextoPeticao> listaDocumentoTextoPeticao) {
		List<DocumentoTextoPeticaoWrapper> listaDocumentoTextoPeticaoWrapper = new LinkedList<DocumentoTextoPeticaoWrapper>();
		
		DocumentoTextoPeticao docTextoPeticaoAnt = null;
		
		for( DocumentoTextoPeticao docTextoPeticao : listaDocumentoTextoPeticao ) {
			DocumentoTextoPeticaoWrapper wrapper = new DocumentoTextoPeticaoWrapper( docTextoPeticao );
			if( docTextoPeticaoAnt != null ) {
				if( docTextoPeticao.getTextoPeticao().getArquivoEletronico().getId().equals(docTextoPeticaoAnt.getTextoPeticao().getArquivoEletronico().getId()) ) {
					wrapper.indentar = true;
				}else{
					wrapper.indentar = false;
				}
			}else{
				wrapper.indentar = false;
			}
			docTextoPeticaoAnt = docTextoPeticao;
			listaDocumentoTextoPeticaoWrapper.add(wrapper);
		}
		
		return listaDocumentoTextoPeticaoWrapper;
		
	}

	public boolean isIndentar() {
		return indentar;
	}

}
