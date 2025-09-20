package br.gov.stf.estf.processostf.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.ListaProcessos;
import br.gov.stf.estf.processostf.model.dataaccess.ListaProcessosDao;
import br.gov.stf.estf.processostf.model.service.ListaProcessosService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("listaProcessosService")
public class ListaProcessosServiceImpl extends GenericServiceImpl<ListaProcessos, Long, ListaProcessosDao> implements ListaProcessosService {
    public ListaProcessosServiceImpl(ListaProcessosDao dao) { super(dao); }

	public ListaProcessos recuperarPorNome(String nome) throws ServiceException {
		try {
			return dao.recuperarPorNome(nome);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<ListaProcessos> pesquisarListaProcessos(String nome,
			Boolean ativo,Long idSetor) throws ServiceException {
		try {
			return dao.pesquisarListaProcessos(nome, ativo,idSetor);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}


}
