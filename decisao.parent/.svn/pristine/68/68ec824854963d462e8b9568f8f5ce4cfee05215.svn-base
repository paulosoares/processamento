/**
 * 
 */
package br.jus.stf.estf.decisao.comunicacao.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.estf.decisao.comunicacao.service.ComunicacaoService;
import br.jus.stf.estf.decisao.texto.support.ErroTesteAssinaturaException;

/**
 * @author Paulo.Estevao
 * @since 06.09.2011
 */
@Service("comunicacaoServiceLocal")
public class ComunicacaoServiceImpl implements ComunicacaoService {

	@Resource(name="transactionManager")
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private DocumentoComunicacaoService documentoComunicacaoService;
	
	@Override
	public void testarAssinaturaComunicacao(
			final DocumentoComunicacao documentoComunicacao,
			final byte[] pdfAssinado, final byte[] assinatura,
			final byte[] carimboTempo, final Date dataCarimboTempo,
			final String usuario, final String siglaSistema)
			throws ServiceException {
		TransactionTemplate template = new TransactionTemplate(transactionManager);
		template.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					documentoComunicacaoService.salvarDocumentoComunicacaoAssinadoeSTFDecisao(documentoComunicacao, pdfAssinado, assinatura, carimboTempo, dataCarimboTempo, usuario, siglaSistema);
				} catch (ServiceException e) {
					throw new ErroTesteAssinaturaException(e);
				} finally {
					status.setRollbackOnly();
				}
			}
		});
	}
}
