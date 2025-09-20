package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.SituacaoJulgamento;
import br.gov.stf.estf.julgamento.model.dataaccess.SituacaoJulgamentoDao;
import br.gov.stf.estf.julgamento.model.service.SituacaoJulgamentoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("situacaoJulgamentoService")
public class SituacaoJulgamentoServiceImpl extends GenericServiceImpl<SituacaoJulgamento, Long, SituacaoJulgamentoDao> 
implements SituacaoJulgamentoService {

    public SituacaoJulgamentoServiceImpl(SituacaoJulgamentoDao dao) { super(dao); }

	public List<SituacaoJulgamento> pesquisar(Long idJulgamentoProcesso)
			throws ServiceException {
		try {
			return dao.pesquisar(idJulgamentoProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
