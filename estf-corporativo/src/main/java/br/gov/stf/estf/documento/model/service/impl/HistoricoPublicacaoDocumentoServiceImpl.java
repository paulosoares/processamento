package br.gov.stf.estf.documento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.HistoricoPublicacaoDocumentoDao;
import br.gov.stf.estf.documento.model.service.HistoricoPublicacaoDocumentoService;
import br.gov.stf.estf.entidade.documento.HistoricoPublicacaoDocumento;
import br.gov.stf.estf.entidade.publicacao.Publicacao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("historicoPublicacaoDocumentoService")
public class HistoricoPublicacaoDocumentoServiceImpl extends GenericServiceImpl<HistoricoPublicacaoDocumento, Long, HistoricoPublicacaoDocumentoDao> 
	implements HistoricoPublicacaoDocumentoService {
    public HistoricoPublicacaoDocumentoServiceImpl(HistoricoPublicacaoDocumentoDao dao) { super(dao); }

	public List<HistoricoPublicacaoDocumento> pesquisar(Publicacao publicacao)
			throws ServiceException {
		List<HistoricoPublicacaoDocumento> historicos = null;
		try {
			historicos = dao.pesquisar(publicacao);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
		return historicos;
	}

}
