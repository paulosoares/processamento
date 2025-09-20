package br.gov.stf.estf.documento.model.service.impl;

import br.gov.stf.estf.documento.model.exception.TextoInvalidoParaPecaException;
import br.gov.stf.estf.documento.model.service.exception.NaoExisteDocumentoAssinadoException;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.Texto;
import br.gov.stf.estf.entidade.processostf.Protocolo;
import br.gov.stf.framework.model.service.ServiceException;

public interface ArquivoProcessoEletronicoServiceExtra {
	/**
	 * Faz a juntada de um processo
	 * 
	 * @param documentoTexto
	 * @param siglaTipoPecaProcesso
	 * @param protocolo
	 * @throws ServiceException
	 */
	void inserirArquivoProcessoEletronicoJuntado(DocumentoTexto documentoTexto, String siglaTipoPecaProcesso, Protocolo protocolo)
			throws ServiceException;

	/**
	 * Faz a juntada do texto selecionado ao processo. Caso j� exista uma pe�a
	 * eletr�nica, mas esteja pendente, apenas altera o status de PENDENTE DE
	 * JUNTADA para JUNTADA. Caso n�o se enquadre em nenhuma das situa��es, n�o
	 * faz nenhuma opera��o.
	 * 
	 * @param texto
	 *            Texto que deve ser juntado
	 * @return Indica��o (true ou false) se foi feita alguma opera��o
	 * @throws ServiceException
	 * @throws NaoExisteDocumentoAssinadoException
	 *             Quando n�o h� documento assinado para a juntada
	 * @throws TextoInvalidoParaPecaException
	 *             Quando o texto n�o se enquadra nos tipos que podem ser
	 *             juntados
	 */
	boolean gravarJuntadaDePecas(Texto texto) throws ServiceException, NaoExisteDocumentoAssinadoException,
			TextoInvalidoParaPecaException;

	boolean excluirJuntadaDePecas(Texto texto) throws ServiceException, TextoInvalidoParaPecaException;

	boolean excluirJuntadaDePecas(Texto texto, boolean cancelarPDF) throws ServiceException, TextoInvalidoParaPecaException;

}