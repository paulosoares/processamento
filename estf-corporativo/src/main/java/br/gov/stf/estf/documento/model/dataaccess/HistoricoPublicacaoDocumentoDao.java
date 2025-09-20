package br.gov.stf.estf.documento.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.documento.HistoricoPublicacaoDocumento;
import br.gov.stf.estf.entidade.publicacao.Publicacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface HistoricoPublicacaoDocumentoDao extends GenericDao<HistoricoPublicacaoDocumento, Long> {
	public List<HistoricoPublicacaoDocumento> pesquisar (Publicacao publicacao) throws DaoException;
}
