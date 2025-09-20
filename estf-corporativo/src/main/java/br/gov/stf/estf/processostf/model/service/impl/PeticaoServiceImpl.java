package br.gov.stf.estf.processostf.model.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.processostf.model.dataaccess.PeticaoDao;
import br.gov.stf.estf.processostf.model.service.PeticaoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("peticaoService")
public class PeticaoServiceImpl extends GenericServiceImpl<Peticao, Long, PeticaoDao> implements PeticaoService {
	
	protected PeticaoServiceImpl(PeticaoDao dao) {
		super(dao);
	}

	public Peticao recuperarPeticaoProcesso( Long numeroPeticao, Short anoPeticao, String siglaProcessual, 
									Long numeroProcessual, Short codRecurso, Boolean flgJuntado ) 
	throws ServiceException {
		
		Peticao peticaoProcesso = null;
		
		try {
			peticaoProcesso = dao.recuperarPeticaoProcesso(numeroPeticao, anoPeticao, siglaProcessual, 
																	numeroProcessual, codRecurso, flgJuntado); 	
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
	
		return peticaoProcesso;
	}

	public Long persistirPeticao(Peticao peticao) throws ServiceException {
		try {
			return dao.persistirPeticao(peticao);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
	}

	public Peticao recuperarPeticao(Long numero, Short ano)
			throws ServiceException {
		try {
			return dao.recuperarPeticao(numero, ano);
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
	}
	
	public List<ObjetoIncidente<?>> recuperarListaObjetoPeloObjetoIncidentePrincipal(Long idObjetoIncidente) throws ServiceException{
		
		List<ObjetoIncidente<?>> listaObjetoIncidente = new ArrayList<ObjetoIncidente<?>>();
		try {
			listaObjetoIncidente = dao.recuperarListaObjetoPeloObjetoIncidentePrincipal(idObjetoIncidente);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
		return listaObjetoIncidente;
	}

	@Override
	public Peticao recuperarPeticao(Long idObjetoIndicente) throws ServiceException {

		try {
			return dao.recuperarPeticao(idObjetoIndicente); 	
		} catch ( DaoException e ) {
			throw new ServiceException(e);
		}
	}
	
	public List<Peticao> recuperarPeticoes(Long numero, Short ano) throws ServiceException{
		try {
			return dao.recuperarPeticoes(numero, ano);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	@Override
	public Boolean isPendenteDigitalizacao(Peticao peticao) throws ServiceException {
		try {
			return dao.isPendenteDigitalizacao(peticao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	@Override
	public Boolean isRemessaIndevida(Peticao peticao) throws ServiceException {
		try {
			return dao.isRemessaIndevida(peticao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
