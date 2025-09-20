/**
 * 
 */
package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.InformacaoPautaProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.SubtemaPauta;
import br.gov.stf.estf.julgamento.model.dataaccess.InformacaoPautaProcessoDao;
import br.gov.stf.estf.julgamento.model.service.InformacaoPautaProcessoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * @author Almir.Oliveira
 * @since 29.06.2011 */
@Service("informacaoPautaProcessoService")
public class InformacaoPautaProcessoServiceImpl extends
		GenericServiceImpl<InformacaoPautaProcesso, Long, InformacaoPautaProcessoDao> implements
		InformacaoPautaProcessoService {

	protected InformacaoPautaProcessoServiceImpl(InformacaoPautaProcessoDao dao) {
		super(dao);
	}

	@Override
	public InformacaoPautaProcesso recuperar(ObjetoIncidente<?> objetoIncidente)
			throws ServiceException {
		try {
			return dao.recuperar(objetoIncidente);
		} catch(DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<ObjetoIncidente<?>> recuperarProcessosJulgamentoConjunto(
		ObjetoIncidente<?> objetoIncidente) throws ServiceException {
		return recuperarProcessosJulgamentoConjunto(objetoIncidente, true);
	}

	@Override
	public List<ObjetoIncidente<?>> recuperarProcessosJulgamentoConjunto(
			Long seqListaJulgamentoConjunto) throws ServiceException {
		try {
			return dao.recuperarProcessosJulgamentoConjunto(seqListaJulgamentoConjunto);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<ObjetoIncidente<?>> recuperarProcessosJulgamentoConjunto(
			ObjetoIncidente<?> objetoIncidente, boolean incluirProcessoAtual)
			throws ServiceException {
		InformacaoPautaProcesso informacaoPautaProcesso = recuperar(objetoIncidente);
		List<ObjetoIncidente<?>> lista = new ArrayList<ObjetoIncidente<?>>();
		if (informacaoPautaProcesso != null && informacaoPautaProcesso.getSeqListaJulgamentoConjunto() != null) {
			lista = recuperarProcessosJulgamentoConjunto(informacaoPautaProcesso.getSeqListaJulgamentoConjunto());
			if (!incluirProcessoAtual) {
				lista.remove(objetoIncidente);
			}
		}
		return lista;
	}

	@Override
	public Long recuperarProximaSequenciaListaJulgamentoConjunto()
			throws ServiceException {
		try {
			return dao.recuperarProximaSequenceListaJulgamentoConjunto();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Long recuperarQtdProcessosSubTema(SubtemaPauta subTemaPauta)
			throws ServiceException {
		try {
			return dao.recuperarQtdProcessosSubTema(subTemaPauta);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void refresh(InformacaoPautaProcesso informacaoPautaProcesso) throws ServiceException {
		try {
			dao.refresh( informacaoPautaProcesso );
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	
}
