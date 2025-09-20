package br.gov.stf.estf.documento.model.service;

import java.util.List;

import br.gov.stf.estf.documento.model.dataaccess.HistoricoPublicacaoDocumentoDao;
import br.gov.stf.estf.entidade.documento.HistoricoPublicacaoDocumento;
import br.gov.stf.estf.entidade.publicacao.Publicacao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface HistoricoPublicacaoDocumentoService extends GenericService<HistoricoPublicacaoDocumento, Long, HistoricoPublicacaoDocumentoDao> {
	public List<HistoricoPublicacaoDocumento> pesquisar (Publicacao publicacao) throws ServiceException;
}
