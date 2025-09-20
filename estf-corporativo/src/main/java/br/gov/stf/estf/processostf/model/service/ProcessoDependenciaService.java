package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.AndamentoPeticao;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoDependenciaDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ProcessoDependenciaService extends GenericService<ProcessoDependencia, Long, ProcessoDependenciaDao> {
	public List<ProcessoDependencia> recuperarApensos(Processo processo) throws ServiceException;

	public List<ProcessoDependencia> recuperarTodosApensos(Processo processo) throws ServiceException;

	public List<ProcessoDependencia> recuperarTodosApensadosAo(Processo processo) throws ServiceException;

	public Integer getQuantidadeVinculados(Processo processo) throws ServiceException;

	/**
	 * Cria uma dependência PROCESSO -> PROCESSO.
	 */
	public ProcessoDependencia criarDependencia(AndamentoProcesso andamentoProcesso, Processo processoPrincipal, Long tipoDependenciaProcesso)
			throws ServiceException;

	/**
	 * Cria uma dependência PROCESSO -> PETIÇÃO.
	 */
	public ProcessoDependencia criarDependencia(AndamentoProcesso andamentoProcesso, Peticao peticao, Long tipoDependenciaProcesso) throws ServiceException;

	/**
	 * Cria uma dependência PETIÇÃO -> PROCESSO.
	 */
	public ProcessoDependencia criarDependencia(AndamentoPeticao andamentoPeticao, Peticao peticao, Processo processo, Long tipoDependenciaProcesso)
			throws ServiceException;

	public void finalizarDependenciasProcesso(AndamentoProcesso andamentoProcesso, AndamentoProcesso andamentoProcessoDesvinculador) throws ServiceException;

	public void reabrirDependenciasProcesso(AndamentoProcesso andamentoProcesso) throws ServiceException;

	public boolean isPeticaoJuntada(Processo processo, Peticao peticao) throws ServiceException;

	public boolean isProcessoApensado(Processo processo, Processo processoPrincipal) throws ServiceException;

	public void finalizarApenso(Processo processo, Processo processoPrincipal) throws ServiceException;

	public void finalizarSobrestamentos(AndamentoProcesso andamentoProcessoVinculador) throws ServiceException;

	public ProcessoDependencia getProcessoVinculador(Processo processo) throws ServiceException;
	
	public boolean isApenso(Processo processo) throws ServiceException;

}
