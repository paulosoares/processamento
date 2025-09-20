package br.gov.stf.estf.julgamento.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.julgamento.JulgamentoProcesso;
import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.julgamento.VotoJulgamentoProcesso;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.julgamento.model.dataaccess.VotoJulgamentoProcessoDao;
import br.gov.stf.estf.julgamento.model.service.VotoJulgamentoProcessoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("votoJulgamentoProcessoService")
public class VotoJulgamentoProcessoServiceImpl
		extends
		GenericServiceImpl<VotoJulgamentoProcesso, Long, VotoJulgamentoProcessoDao>
		implements VotoJulgamentoProcessoService {

	public VotoJulgamentoProcessoServiceImpl(VotoJulgamentoProcessoDao dao) {
		super(dao);
	}

	public Long getProximaOrdemVoto(JulgamentoProcesso julgamentoProcesso)
			throws ServiceException {

		try {
			Long proximaOrdemVoto = dao.getProximaOrdemVoto(julgamentoProcesso);
			if (proximaOrdemVoto == null)
				return 1L;
			else
				return proximaOrdemVoto;

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean temVotoMinistroProcesso(ObjetoIncidente objetoIncidente, Ministro ministro) throws ServiceException {
		try {
			return dao.temVotoMinistroProcesso(objetoIncidente, ministro);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<VotoJulgamentoProcesso> listarRascunhosDoMinistroNaLista(Ministro ministro, ListaJulgamento listaJulgamento) throws ServiceException {
		try {
			return dao.listarRascunhosDoMinistroNaLista(ministro, listaJulgamento);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
