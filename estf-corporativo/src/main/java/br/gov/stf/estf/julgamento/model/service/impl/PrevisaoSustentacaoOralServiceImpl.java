/**
 * 
 */
package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.julgamento.PrevisaoSustentacaoOral;
import br.gov.stf.estf.julgamento.model.dataaccess.PrevisaoSustentacaoOralDao;
import br.gov.stf.estf.julgamento.model.service.PrevisaoSustentacaoOralService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 *
 */
@Service("previsaoSustentacaoOralService")
public class PrevisaoSustentacaoOralServiceImpl extends
		GenericServiceImpl<PrevisaoSustentacaoOral, Long, PrevisaoSustentacaoOralDao> implements
		PrevisaoSustentacaoOralService {

	protected PrevisaoSustentacaoOralServiceImpl(PrevisaoSustentacaoOralDao dao) {
		super(dao);
	}

	@Override
	public List<PrevisaoSustentacaoOral> pesquisar(
			InformacaoPautaProcesso informacaoPautaProcesso)
			throws ServiceException {
		try {
			return dao.pesquisar(informacaoPautaProcesso);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
