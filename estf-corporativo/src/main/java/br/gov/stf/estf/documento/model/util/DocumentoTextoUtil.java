package br.gov.stf.estf.documento.model.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.gov.stf.estf.documento.model.service.exception.NaoExisteDocumentoAssinadoException;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;

public class DocumentoTextoUtil {

	/**
	 * Recupera o documento do texto que está assinado.
	 * 
	 * @param texto
	 *            Texto que contém os vários documentos texto
	 * @return O DocumentoTexto com status assinado
	 * @throws NaoExisteDocumentoAssinadoException
	 */
	public static DocumentoTexto recuperarDocumentoAssinadoDoTexto(Texto texto) throws NaoExisteDocumentoAssinadoException {
		List<DocumentoTexto> documentosDoTexto = texto.getDocumentosTexto();
		for (DocumentoTexto documentoTexto : documentosDoTexto) {
			if (isDocumentoTextoAssinado(documentoTexto) && isDocumentoEletronicoAssinado(documentoTexto)) {
				return documentoTexto;
			}
		}
		throw new NaoExisteDocumentoAssinadoException();
	}

	private static boolean isDocumentoEletronicoAssinado(DocumentoTexto documentoTexto) {
		return (documentoTexto.getDocumentoEletronicoView() != null && documentoTexto.getDocumentoEletronicoView()
				.getDescricaoStatusDocumento().equals(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO));
	}

	private static boolean isDocumentoTextoAssinado(DocumentoTexto documentoTexto) {
		return (documentoTexto.getTipoSituacaoDocumento().equals(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE))
				|| (documentoTexto.getTipoSituacaoDocumento().equals(TipoSituacaoDocumento.ASSINADO_MANUALMENTE));
	}

	/**
	 * Recupera o documento do texto mais recente (maior id)
	 * 
	 * @param texto
	 *            Texto que contém os documentos
	 * @return O DocumentoTexto com maior id
	 */
	public static DocumentoTexto recuperaDocumentoTextoMaisRecente(Texto texto) {
		List<DocumentoTexto> documentosTexto = texto.getDocumentosTexto();
		if (documentosTexto.isEmpty()) {
			return null;
		}
		// Ordena a coleção de forma ascendente pelo Id
		Collections.sort(documentosTexto, new ComparadorDeDocumentoTexto());
		// Recupera o maior id
		DocumentoTexto documentoTexto = documentosTexto.get(documentosTexto.size() - 1);
		// TODO Jubé - Verificar se a restrição de não trazer documentos
		// cancelados vai dar impacto
		return documentoTexto;
	}

	static class ComparadorDeDocumentoTexto implements Comparator<DocumentoTexto> {

		public int compare(DocumentoTexto documentoTexto, DocumentoTexto outroDocumentoTexto) {
			return documentoTexto.getId().compareTo(outroDocumentoTexto.getId());
		}

	}

}
