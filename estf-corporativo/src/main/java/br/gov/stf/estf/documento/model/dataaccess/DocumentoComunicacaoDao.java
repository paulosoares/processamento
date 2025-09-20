package br.gov.stf.estf.documento.model.dataaccess;


import java.util.List;

import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface DocumentoComunicacaoDao extends GenericDao<DocumentoComunicacao, Long> {
	
	public DocumentoComunicacao recuperarNaoCancelado(Comunicacao comunicacao) throws DaoException;

	List<DocumentoComunicacao> pesquisarDocumentosPelaComunicacao(Comunicacao comunicacao) throws DaoException;

}
