/**
 * 
 */
package br.gov.stf.estf.documento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.documento.model.dataaccess.RotuloDao;
import br.gov.stf.estf.documento.model.service.RotuloService;
import br.gov.stf.estf.entidade.documento.Rotulo;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Paulo.Estevao
 * @since 28.11.2013
 */
@Service("rotuloService")
public class RotuloServiceImpl extends GenericServiceImpl<Rotulo, Long, RotuloDao> implements RotuloService {

	protected RotuloServiceImpl(RotuloDao dao) {
		super(dao);
	}

	@Override
	public List<Rotulo> pesquisarRotulos(ObjetoIncidente<?> objetoIncidente, Setor setor) throws ServiceException {
		try {
			return dao.pesquisarRotulos(objetoIncidente, setor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
