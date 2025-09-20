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
	 * Faz a juntada do texto selecionado ao processo. Caso já exista uma peça
	 * eletrônica, mas esteja pendente, apenas altera o status de PENDENTE DE
	 * JUNTADA para JUNTADA. Caso não se enquadre em nenhuma das situações, não
	 * faz nenhuma operação.
	 * 
	 * @param texto
	 *            Texto que deve ser juntado
	 * @return Indicação (true ou false) se foi feita alguma operação
	 * @throws ServiceException
	 * @throws NaoExisteDocumentoAssinadoException
	 *             Quando não há documento assinado para a juntada
	 * @throws TextoInvalidoParaPecaException
	 *             Quando o texto não se enquadra nos tipos que podem ser
	 *             juntados
	 */
	boolean gravarJuntadaDePecas(Texto texto) throws ServiceException, NaoExisteDocumentoAssinadoException,
			TextoInvalidoParaPecaException;

	boolean excluirJuntadaDePecas(Texto texto) throws ServiceException, TextoInvalidoParaPecaException;

	boolean excluirJuntadaDePecas(Texto texto, boolean cancelarPDF) throws ServiceException, TextoInvalidoParaPecaException;

}