package br.gov.stf.estf.documento.model.util;

import java.io.Serializable;

import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.Texto;

public class TextoDocumentoResult
    implements Serializable
{

    private static final long serialVersionUID = 0x3424cc290d03691aL;
    private Texto texto;
    private DocumentoTexto documentoTexto;
    private String siglaClasseNumeroProcesso;

    public TextoDocumentoResult(Texto texto, DocumentoTexto documentoTexto)
    {
        this.texto = texto;
        this.documentoTexto = documentoTexto;
    }

    public Texto getTexto()
    {
        return texto;
    }

    public void setTexto(Texto texto)
    {
        this.texto = texto;
    }

    public DocumentoTexto getDocumentoTexto()
    {
        return documentoTexto;
    }

    public void setDocumentoTexto(DocumentoTexto documentoTexto)
    {
        this.documentoTexto = documentoTexto;
    }

	public String getSiglaClasseNumeroProcesso() {
		return siglaClasseNumeroProcesso;
	}

	public void setSiglaClasseNumeroProcesso(String siglaClasseNumeroProcesso) {
		this.siglaClasseNumeroProcesso = siglaClasseNumeroProcesso;
	}
    

    
    
}
