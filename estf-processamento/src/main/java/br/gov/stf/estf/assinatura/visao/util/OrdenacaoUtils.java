package br.gov.stf.estf.assinatura.visao.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.DocumentoTextoPeticao;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.Guia;
import br.jus.stf.estf.montadortexto.ParteProcessoSecretaria;

/**
 * Classe utilitária para auxiliar na ordenação de coleções.
 * 
 * @author thiago.miranda
 */
public class OrdenacaoUtils {

	/**
	 * Ordena uma lista de ComunicacaoDocumentoResult pela data de criação da
	 * comunicação associada a cada um.
	 * 
	 * @param comunicacaoDocumentoList
	 * @param ascendente
	 *            true para ordenação ascendente, false para descendente
	 */
	public static void ordenarListaComunicacaoDocumentoResultDataCriacao(List<ComunicacaoDocumentoResult> comunicacaoDocumentoList, TipoOrdenacao tipoOrdenacao) {
		Collections.sort(comunicacaoDocumentoList, new PropertyComparator<ComunicacaoDocumentoResult>(tipoOrdenacao, "comunicacao.dataCriacao"));
	}
	
	public static void ordenarListaComunicacaoDocumentoResultAssinatura(List<ComunicacaoDocumentoResult> comunicacaoDocumentoList, TipoOrdenacao tipoOrdenacao) {
		Collections.sort(comunicacaoDocumentoList, new PropertyComparator<ComunicacaoDocumentoResult>(tipoOrdenacao, "comunicacao.dataAssinaturaExpedirDocumentos"));
	}
	
	public static void ordenarListaComunicacaoDocumentoResultProcesso(List<ComunicacaoDocumentoResult> comunicacaoDocumentoList, TipoOrdenacao tipoOrdenacao) {
		Collections.sort(comunicacaoDocumentoList, new PropertyComparator<ComunicacaoDocumentoResult>(tipoOrdenacao, "comunicacao.identificacaoObjetoIncidenteUnico"));
	}

	public static void listaOrdenaUsuarioItemControle(List<ItemControleSearchData> listaUsuarioItemControle, TipoOrdenacao tipoOrdenacao) {
		Collections.sort(listaUsuarioItemControle, new PropertyComparator<ItemControleSearchData>(tipoOrdenacao, "itemControleResult.itemControle.usuarios"));
	}
	
	public static void listaOrdenaPecaVisibilidade(List<ArquivoProcessoEletronico> listaVisibilidade, TipoOrdenacao tipoOrdenacao) {
		Collections.sort(listaVisibilidade, new PropertyComparator<ArquivoProcessoEletronico>(tipoOrdenacao, "pecaProcessoEletronico.dataInclusao"));
	}

	public static void ordenarListaComunicacaoDocumentoResultDataCriacao(List<ComunicacaoDocumentoResult> comunicacaoDocumentoList) {
		ordenarListaComunicacaoDocumentoResultDataCriacao(comunicacaoDocumentoList, TipoOrdenacao.ASCENDENTE);
	}
	
	public static void ordenarListaComunicacaoDocumentoResultDataAssinatura(List<ComunicacaoDocumentoResult> comunicacaoDocumentoList) {
		ordenarListaComunicacaoDocumentoResultAssinatura(comunicacaoDocumentoList, TipoOrdenacao.ASCENDENTE);
	}
	
	public static void ordenarListaComunicacaoDocumentoResultProcesso(List<ComunicacaoDocumentoResult> comunicacaoDocumentoList) {
		ordenarListaComunicacaoDocumentoResultProcesso(comunicacaoDocumentoList, TipoOrdenacao.ASCENDENTE);
	}

	public static void listaOrdenaUsuarioItemControle(List<ItemControleSearchData> listaUsuarioItemControle) {
		listaOrdenaUsuarioItemControle(listaUsuarioItemControle, TipoOrdenacao.DESCENDENTE);
	}
	
	public static void listaOrdenaPecaVisibilidade(List<ArquivoProcessoEletronico> listaVisibilidadePeca) {
		listaOrdenaPecaVisibilidade(listaVisibilidadePeca, TipoOrdenacao.ASCENDENTE);
	}

	public static void ordenarListaClasseProcessualId(List<Classe> listaClasses, TipoOrdenacao tipoOrdenacao) {
		Collections.sort(listaClasses, new PropertyComparator<Classe>(tipoOrdenacao, "id"));
	}
	
	public static void ordenarListaGuiasAnoNumeroOrigem(List<Guia> listaGuias, TipoOrdenacao tipoOrdenacao) {
		Collections.sort(listaGuias, new PropertyComparator<Guia>(tipoOrdenacao, "anoGuia", "numeroGuia", "descricaoOrigem"));
	}

	/**
	 * Ordena uma lista de DocumentoTexto pela data de inclusão de cada um.
	 * 
	 * @param documentos
	 * @param ascendente
	 *            true para ordenação ascendente, false para descendente
	 */
	public static void ordenarListaDocumentoTextoDataInclusao(List<DocumentoTexto> documentos, TipoOrdenacao tipoOrdenacao) {
		Collections.sort(documentos, new PropertyComparator<DocumentoTexto>(tipoOrdenacao, "dataInclusao"));
	}

	public static void ordenarListaDocumentoTextoDataInclusao(List<DocumentoTexto> documentos) {
		ordenarListaDocumentoTextoDataInclusao(documentos, TipoOrdenacao.ASCENDENTE);
	}

	/**
	 * Ordena uma lista de DocumentoTextoPeticao pela data de revisão de cada
	 * um.
	 * 
	 * @param documentos
	 * @param ascendente
	 *            true para ordenação ascendente, false para descendente
	 */
	public static void ordenarListaDocumentoTextoPeticaoDataRevisao(List<DocumentoTextoPeticao> documentos, TipoOrdenacao tipoOrdenacao) {
		Collections.sort(documentos, new PropertyComparator<DocumentoTextoPeticao>(tipoOrdenacao, "dataRevisao"));
	}

	public static void ordenarListaDocumentoTextoPeticaoDataInclusao(List<DocumentoTextoPeticao> documentos) {
		ordenarListaDocumentoTextoPeticaoDataRevisao(documentos, TipoOrdenacao.ASCENDENTE);
	}

	public static void ordenarListaArquivoProcessoEletronicoDataInclusao(List<ArquivoProcessoEletronico> arquivos, TipoOrdenacao tipoOrdenacao) {
		Collections.sort(arquivos, new PropertyComparator<ArquivoProcessoEletronico>(tipoOrdenacao, "dataInclusao"));
	}

	public static void ordenarListaArquivoProcessoEletronicoDataInclusao(List<ArquivoProcessoEletronico> arquivos) {
		ordenarListaArquivoProcessoEletronicoDataInclusao(arquivos, TipoOrdenacao.ASCENDENTE);
	}

	// -------------------- CLASSES COMPARATOR --------------------

	@SuppressWarnings({ "rawtypes", "serial" })
	public static class ComparaStringsGenerica implements Comparator, Serializable {
		public int compare(Object obj1, Object obj2) {
			if (!(obj1 instanceof ComunicacaoDocumentoResult) || !(obj2 instanceof ComunicacaoDocumentoResult)) {
				return 0;
			}

			ComunicacaoDocumentoResult com1 = (ComunicacaoDocumentoResult) obj1;
			ComunicacaoDocumentoResult com2 = (ComunicacaoDocumentoResult) obj2;

			if (com1.getComunicacao().getModeloComunicacao().getTipoComunicacao().getDescricao() == null
					|| com1.getComunicacao().getModeloComunicacao().getTipoComunicacao().getDescricao().trim().length() == 0
					|| com2.getComunicacao().getModeloComunicacao().getTipoComunicacao().getDescricao() == null
					|| com2.getComunicacao().getModeloComunicacao().getTipoComunicacao().getDescricao().trim().length() == 0) {
				return 0;
			}

			return com1.getComunicacao().getModeloComunicacao().getTipoComunicacao().getDescricao().toLowerCase()
					.compareTo(com2.getComunicacao().getModeloComunicacao().getTipoComunicacao().getDescricao().toLowerCase());
		}
	}

	@SuppressWarnings({ "serial", "rawtypes" })
	public static class ListaParteOrdenator implements Comparator, Serializable {

		public int compare(Object obj1, Object obj2) {

			if (!(obj1 instanceof ParteProcessoSecretaria) || obj2 instanceof ParteProcessoSecretaria) {
				return 0;
			}
			ParteProcessoSecretaria pps1 = (ParteProcessoSecretaria) obj1;
			ParteProcessoSecretaria pps2 = (ParteProcessoSecretaria) obj2;

			if (pps1.getNumeroOrdem() == null || pps1.getCodigoCategoria() == null || pps1.getCodigoCategoria() == 0L || pps2.getNumeroOrdem() == null
					|| pps2.getCodigoCategoria() == null || pps2.getCodigoCategoria() == 0L) {
				return 0;
			}

			return pps1.getNumeroOrdem().compareTo(pps2.getNumeroOrdem());
		}
	}
}
