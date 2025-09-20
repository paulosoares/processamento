package br.gov.stf.estf.processostf.model.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import br.gov.stf.estf.entidade.processostf.AndamentoPeticao;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoDependenciaDao;
import br.gov.stf.estf.processostf.model.service.ProcessoDependenciaService;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.model.service.impl.GenericServiceImpl;

@Service("processoDependenciaService")
public class ProcessoDependenciaServiceImpl extends GenericServiceImpl<ProcessoDependencia, Long, ProcessoDependenciaDao> implements ProcessoDependenciaService {

	public ProcessoDependenciaServiceImpl(ProcessoDependenciaDao dao) {
		super(dao);
	}

	public List<ProcessoDependencia> recuperarApensos(Processo processo) throws ServiceException {

		List<ProcessoDependencia> listaDependencias = null;
		try {
			listaDependencias = dao.recuperarApensos(processo);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao pesquisar apensos");
		}
		return listaDependencias;
	}
	
	@Override
	public boolean isApenso(Processo processo) throws ServiceException {
		try {
			return dao.isApenso(processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<ProcessoDependencia> recuperarTodosApensos(Processo processo) throws ServiceException {
		try {
			return dao.recuperarTodosApensos(processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<ProcessoDependencia> recuperarTodosApensadosAo(Processo processo) throws ServiceException {
		try {
			return dao.recuperarTodosApensadosAo(processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public List<ProcessoDependencia> pesquisarDependencias(AndamentoProcesso andamentoProcesso) throws ServiceException {
		try {
			return dao.pesquisarDependencias(andamentoProcesso);

		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	public ProcessoDependencia criarDependencia(AndamentoProcesso andamentoProcesso, Processo processoPrincipal, Long tipoDependenciaProcesso)
			throws ServiceException {

		ProcessoDependencia dependencia = criarDependenciaProcesso(andamentoProcesso, tipoDependenciaProcesso);

		dependencia.setClasseProcessoVinculador(processoPrincipal.getSiglaClasseProcessual());
		dependencia.setNumeroProcessoVinculador(processoPrincipal.getNumeroProcessual());
		dependencia.setIdObjetoIncidenteVinculado(processoPrincipal.getId());

		salvar(dependencia);

		return dependencia;
	}

	public ProcessoDependencia criarDependencia(AndamentoProcesso andamentoProcesso, Peticao peticao, Long tipoDependenciaProcesso) throws ServiceException {

		ProcessoDependencia dependencia = criarDependenciaProcesso(andamentoProcesso, tipoDependenciaProcesso);

		dependencia.setIdObjetoIncidenteVinculado(peticao.getId());

		salvar(dependencia);

		return dependencia;
	}

	private ProcessoDependencia criarDependenciaProcesso(AndamentoProcesso andamentoProcesso, Long tipoDependenciaProcesso) throws ServiceException {

		ProcessoDependencia dependencia = new ProcessoDependencia();

		dependencia.setClasseProcesso(andamentoProcesso.getSigClasseProces());
		dependencia.setNumeroProcesso(andamentoProcesso.getNumProcesso());
		dependencia.setTipoDependenciaProcesso(tipoDependenciaProcesso);
		dependencia.setDataInicioDependencia(new Date());
		dependencia.setIdObjetoIncidente(andamentoProcesso.getObjetoIncidente().getId());
		dependencia.setAndamentoProcesso(andamentoProcesso.getId());

		return dependencia;
	}

	public ProcessoDependencia criarDependencia(AndamentoPeticao andamentoPeticao, Peticao peticao, Processo processo, Long tipoDependenciaProcesso)
			throws ServiceException {

		ProcessoDependencia dependencia = new ProcessoDependencia();

		dependencia.setAndamentoPeticao(andamentoPeticao.getId());
		dependencia.setTipoDependenciaProcesso(tipoDependenciaProcesso);
		dependencia.setDataInicioDependencia(new Date());
		dependencia.setIdObjetoIncidente(peticao.getId());
		dependencia.setIdObjetoIncidenteVinculado(processo.getId());

		salvar(dependencia);

		return dependencia;
	}

	@Override
	public void finalizarDependenciasProcesso(AndamentoProcesso andamentoProcesso, AndamentoProcesso andamentoProcessoDesvinculador) throws ServiceException {

		List<ProcessoDependencia> dependencias = pesquisarDependencias(andamentoProcesso);

		for (Iterator iterator = dependencias.iterator(); iterator.hasNext();) {

			ProcessoDependencia processoDependencia = (ProcessoDependencia) iterator.next();

			processoDependencia.setDataFimDependencia(new Date());
			processoDependencia.setAndamentoProcessoDesvinculador(andamentoProcessoDesvinculador.getId());
			salvar(processoDependencia);
		}
	}

	@Override
	public void reabrirDependenciasProcesso(AndamentoProcesso andamentoProcesso) throws ServiceException {

		List<ProcessoDependencia> dependencias = pesquisarDependencias(andamentoProcesso);

		for (Iterator iterator = dependencias.iterator(); iterator.hasNext();) {

			ProcessoDependencia processoDependencia = (ProcessoDependencia) iterator.next();
			processoDependencia.setDataFimDependencia(null);
			processoDependencia.setAndamentoProcessoDesvinculador(null);

			salvar(processoDependencia);

		}

	}

	@Override
	public boolean isPeticaoJuntada(Processo processo, Peticao peticao) throws ServiceException {

		try {
			return dao.isPeticaoJuntada(processo, peticao);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean isProcessoApensado(Processo processo, Processo processoPrincipal) throws ServiceException {

		try {
			return dao.isProcessoApensado(processo, processoPrincipal);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void finalizarApenso(Processo processo, Processo processoPrincipal) throws ServiceException {

		try {
			dao.finalizarApenso(processo, processoPrincipal);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void finalizarSobrestamentos(AndamentoProcesso andamentoProcessoVinculador) throws ServiceException {
		try {
			dao.finalizarSobrestamentos(andamentoProcessoVinculador);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Integer getQuantidadeVinculados(Processo processo) throws ServiceException {
		try {
			return dao.getQuantidadeVinculados(processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public ProcessoDependencia getProcessoVinculador(Processo processo) throws ServiceException {
		try {
			return dao.getProcessoVinculador(processo);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
