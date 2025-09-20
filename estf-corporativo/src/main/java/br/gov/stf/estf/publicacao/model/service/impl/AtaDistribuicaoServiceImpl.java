package br.gov.stf.estf.publicacao.model.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.IncidenteDistribuicao;
import br.gov.stf.estf.entidade.publicacao.AtaDistribuicao;
import br.gov.stf.estf.entidade.publicacao.TipoSessao;
import br.gov.stf.estf.publicacao.model.dataaccess.AtaDistribuicaoDao;
import br.gov.stf.estf.publicacao.model.service.AtaDistribuicaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * 
 * @author alvaro.silva
 *
 */
@Service("ataDistribuicaoService")
public class AtaDistribuicaoServiceImpl extends GenericServiceImpl<AtaDistribuicao, Long, AtaDistribuicaoDao> 
implements AtaDistribuicaoService{
	public AtaDistribuicaoServiceImpl(AtaDistribuicaoDao dao) { super(dao); }

	public AtaDistribuicao recuperar(Integer numero, TipoSessao tipoSessao, Date dataComposicaoParcial) 
	throws ServiceException {
		try {

			return dao.recuperar( numero,  tipoSessao,  dataComposicaoParcial);

		} catch (DaoException e) {

			throw new ServiceException(e);

		}
	}

	public List<IncidenteDistribuicao> pesquisarIncidenteDistribuicao(AtaDistribuicao ataDistribuicao, Boolean recuperarOcultos)  
	throws ServiceException {

		try {

			return dao.pesquisarIncidenteDistribuicao(ataDistribuicao, recuperarOcultos);

		} catch (DaoException e) {

			throw new ServiceException(e);

		}		
	}    
}
