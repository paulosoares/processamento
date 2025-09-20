/**
 * 
 */
package br.gov.stf.estf.jurisprudencia.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.jurisprudencia.TextoPadraoObservacaoJurisprudencia;
import br.gov.stf.estf.entidade.jurisprudencia.TipoOrdenacaoObservacaoJurisprudencia;
import br.gov.stf.estf.jurisprudencia.model.dataaccess.TextoPadraoObservacaoJurisprudenciaDao;
import br.gov.stf.estf.jurisprudencia.model.service.TextoPadraoObservacaoJurisprudenciaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.AscendantOrder;
import br.gov.stf.framework.model.dataaccess.hibernate.criterion.OrderCriterion;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 08.10.2012
 */
@Service("textoPadraoObservacaoJurisprudenciaService")
public class TextoPadraoObservacaoJurisprudenciaServiceImpl extends
		GenericServiceImpl<TextoPadraoObservacaoJurisprudencia, Long, TextoPadraoObservacaoJurisprudenciaDao> implements
		TextoPadraoObservacaoJurisprudenciaService {

	protected TextoPadraoObservacaoJurisprudenciaServiceImpl(TextoPadraoObservacaoJurisprudenciaDao dao) {
		super(dao);
	}

	@Override
	public List<TextoPadraoObservacaoJurisprudencia> pesquisarTodos() throws ServiceException {
		try {
			OrderCriterion order = new AscendantOrder("texto");
			return dao.pesquisar(order);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<TextoPadraoObservacaoJurisprudencia> pesquisar(TipoOrdenacaoObservacaoJurisprudencia tipoOrdenacao)
			throws ServiceException {
		try {
			return dao.pesquisar(tipoOrdenacao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
