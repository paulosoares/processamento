/**
 * 
 */
package br.jus.stf.estf.decisao.comunicacao.service;

import java.util.Date;

import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.framework.model.service.ServiceException;

/**
 * @author Paulo.Estevao
 * @since 06.09.2011
 */
public interface ComunicacaoService {

	void testarAssinaturaComunicacao(DocumentoComunicacao documentoComunicacao,
			byte[] pdfAssinado, byte[] assinatura, byte[] carimboTempo,
			Date dataCarimboTempo, String usuario, String siglaSistema)
			throws ServiceException;

}
