package br.gov.stf.estf.localizacao.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.OrigemDestino;
import br.gov.stf.estf.localizacao.model.dataaccess.OrigemDestinoDao;
import br.gov.stf.estf.localizacao.model.service.OrigemDestinoService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("origemDestinoService")
public class OrigemDestinoServiceImpl extends GenericServiceImpl<OrigemDestino, Long, OrigemDestinoDao> implements OrigemDestinoService {

	protected OrigemDestinoServiceImpl(OrigemDestinoDao dao) {
		super(dao);
	}
	
	@Override
	public List<OrigemDestino> recuperarPorIdOuDescricao(String value, Integer tipoOrigemDestino) throws ServiceException {
		List<OrigemDestino> listaOrigensDestinos = null;
		try {
			listaOrigensDestinos = dao.recuperarPorIdOuDescricao(value, tipoOrigemDestino);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar origem/destino");
		}
		return listaOrigensDestinos;
	}

	@Override
	public List<OrigemDestino> recuperarPorIdOuDescricaoSetoresEOrgaosExternos(String value) throws ServiceException {

		List<OrigemDestino> listaOrigensDestinos = null;
		try {
			listaOrigensDestinos = dao.recuperarPorIdOuDescricaoSetoresEOrgaosExternos(value);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar origem/destino");
		}
		return listaOrigensDestinos;
	}

	@Override
	public List<OrigemDestino> recuperarPorIdOuDescricao(String value, Boolean ativo, Boolean deslocaProcesso) throws ServiceException {

		List<OrigemDestino> listaOrigensDestinos = null;
		try {
			listaOrigensDestinos = dao.recuperarPorIdOuDescricao(value, ativo, deslocaProcesso);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar origem/destino");
		}
		return listaOrigensDestinos;
	}

	@Override
	public List<OrigemDestino> recuperarPorId(String id, Boolean ativo,
			Boolean deslocaProcesso) throws ServiceException {
		List<OrigemDestino> listaOrigensDestinos = null;
		try {
			listaOrigensDestinos = dao.recuperarPorId(id, ativo, deslocaProcesso);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar origem/destino");
		}
		return listaOrigensDestinos;
	}

	@Override
	public List<OrigemDestino> recuperarPorDescricao(String descricao,
			Boolean ativo, Boolean deslocaProcesso) throws ServiceException {
		List<OrigemDestino> listaOrigensDestinos = null;
		try {
			listaOrigensDestinos = dao.recuperarPorDescricao(descricao, ativo, deslocaProcesso);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar origem/destino");
		}
		return listaOrigensDestinos;
	}
	
	@Override
	public OrigemDestino recuperarPorId(Long id, int tipoOrigemDestino) throws ServiceException {
		OrigemDestino origemDestino = null;
		try {
			origemDestino = dao.recuperarPorId(id, tipoOrigemDestino);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar origem/destino");
		}
		return origemDestino;
		
	}

	@Override
	public List<OrigemDestino> recuperarPorIdOuDescricao(String value) throws ServiceException {
		List<OrigemDestino> listaOrigensDestinos = null;
		try {
			listaOrigensDestinos = dao.recuperarPorIdOuDescricao(value);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar origem/destino");
		}
		return listaOrigensDestinos;
	}

}
