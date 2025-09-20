package br.gov.stf.estf.localizacao.model.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.localizacao.Origem;
import br.gov.stf.estf.entidade.processostf.Orgao;
import br.gov.stf.estf.entidade.processostf.Procedencia;
import br.gov.stf.estf.localizacao.model.dataaccess.OrigemDao;
import br.gov.stf.estf.localizacao.model.service.OrigemService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

/**
 * Service implementation for inteface OrigemService.
 * 
 * @see .OrigemService
 * @author Hibernate Tools
 */

@Service("origemService")
public class OrigemServiceImpl extends GenericServiceImpl<Origem, Long, OrigemDao> implements OrigemService {
	public OrigemServiceImpl(OrigemDao dao) {
		super(dao);
	}

	public List<Origem> recuperarOrigemPorIdOuDescricao(String id) throws ServiceException {

		List<Origem> listaOrigens = null;
		try {
			listaOrigens = dao.recuperarOrigemPorIdOuDescricao(id);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar origens.");
		}
		return listaOrigens;
	}

	@Override
	public List<Origem> recuperarApenasPgr() throws ServiceException {
		List<Origem> listaOrigens = Collections.emptyList();
		try {
			listaOrigens = dao.recuperarApenasPgr();
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar origens");
		}
		return listaOrigens;
	}
	
	@Override
	public List<Origem> recuperarApenasAgu() throws ServiceException {
		List<Origem> listaOrigens = Collections.emptyList();
		try {
			listaOrigens = dao.recuperarApenasAgu();
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar origens");
		}
		return listaOrigens;
	}

	@Override
	public List<Origem> recuperarApenasDpf() throws ServiceException {
		List<Origem> listaOrigens = Collections.emptyList();
		try {
			listaOrigens = dao.recuperarApenasDpf();
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar origens");
		}
		return listaOrigens;
	}
	
	@Override
	public List<Origem> pesquisarOrigensAtivas(Orgao orgao, Procedencia procedencia) throws ServiceException {

		try {
			return dao.pesquisarOrigensAtivas(orgao, procedencia);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Procedencia pesquisarProcedenciaPadrao(Origem origem) throws ServiceException {

		try {
			return dao.pesquisarProcedenciaPadrao(origem);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	@Override
	public Boolean isOrigemIntegrada(Origem origem) throws ServiceException {

		try {
			return dao.isOrigemIntegrada(origem);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Long> recuperaUsuarioExternoESTF(Long origem)
			throws ServiceException {
		try {
			List<Long> lista = new ArrayList<Long>();
			for(Object numero : dao.recuperaUsuarioExternoESTF(origem)) {
				lista.add(Long.parseLong(numero.toString()));
			}
			return lista;
		} catch (DaoException e) {
			throw new ServiceException("Erro ao recuperar a origem.");
		}
		
	}
	
	@Override
	public Long recuperaUsuarioExternoOrigemESTF(Long seqUsuarioExterno) throws ServiceException {
		try {
				return dao.recuperaUsuarioExternoOrigemESTF(seqUsuarioExterno);
		} catch (DaoException e) {
			throw new ServiceException("Erro ao recuperar a origem.");
		}
	}
	

	@Override
	public List<Origem> recuperarOrigemPorId(Long id,Boolean ativo) throws ServiceException {
		List<Origem> listaOrigens = null;
		try {
			listaOrigens = dao.recuperarOrigemPorId(id,ativo);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar origens.");
		}
		return listaOrigens;
	}

	@Override
	public List<Origem> recuperarOrigemPorDescricao(String id, Boolean ativo)
			throws ServiceException {
		List<Origem> listaOrigens = null;
		try {
			listaOrigens = dao.recuperarOrigemPorDescricao(id,ativo);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar origens.");
		}
		return listaOrigens;
	}
	
	@Override
	public List<Origem> recuperarTodasOrigens(Boolean ativo) throws ServiceException {
		List<Origem> listaOrigens = null;
		try {
			listaOrigens = dao.recuperarTodasOrigens(ativo);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar origens.");
		}
		return listaOrigens;
	}
	
	@Override
	public Boolean isOrigemAptaParaParaNotificacao(Long id) throws ServiceException{
		try {
			return dao.isOrigemAptaParaParaNotificacao(id);
		} catch (DaoException e) {
			throw new ServiceException("Erro ao verificar se origem está apta para notificações.");
		}
	}
}
