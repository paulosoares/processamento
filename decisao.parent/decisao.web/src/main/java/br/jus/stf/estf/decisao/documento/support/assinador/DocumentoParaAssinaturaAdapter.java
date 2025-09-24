package br.jus.stf.estf.decisao.documento.support.assinador;

import br.jus.stf.estf.decisao.documento.support.Documento;
import br.jus.stf.estf.decisao.pesquisa.domain.ComunicacaoDto;
import br.jus.stf.estf.decisao.pesquisa.domain.TextoDto;

public class DocumentoParaAssinaturaAdapter {

	private Documento documento;
	
	private DocumentoParaAssinaturaAdapter(TextoDto textoDto) {
		this.documento = textoDto;
	}
	
	private DocumentoParaAssinaturaAdapter(ComunicacaoDto comunicacaoDto) {
		this.documento = comunicacaoDto;
	}
	
	public String getAdaptedId() {
		if (documento instanceof TextoDto) {
			return "t-" + ((TextoDto)documento).getId().toString();
		} else if (documento instanceof ComunicacaoDto) {
			return "c-" + ((ComunicacaoDto)documento).getId().toString();
		} else {
			throw new IllegalStateException("DocumentoParaAssinaturaAdapter está adaptando um tipo inválido de Documento.");
		}
	}
	
	public static DocumentoParaAssinaturaAdapter adapt(TextoDto textoDto) {
		DocumentoParaAssinaturaAdapter adapter = new DocumentoParaAssinaturaAdapter(textoDto);
		return adapter;
	}
	
	public static DocumentoParaAssinaturaAdapter adapt(ComunicacaoDto comunicacaoDto) {
		DocumentoParaAssinaturaAdapter adapter = new DocumentoParaAssinaturaAdapter(comunicacaoDto);
		return adapter;
	}
	
}
