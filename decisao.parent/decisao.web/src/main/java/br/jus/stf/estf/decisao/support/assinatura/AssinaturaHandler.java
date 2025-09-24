package br.jus.stf.estf.decisao.support.assinatura;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.jus.stf.assinadorweb.handler.IDocumentoPdfHandler;

public abstract class AssinaturaHandler<ENTIDADE> implements IDocumentoPdfHandler<ENTIDADE> {

	protected static final String SIGLA_SISTEMA = "ESTFDECISAO";

	protected final Log logger = LogFactory.getLog(getClass());

}
