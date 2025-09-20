package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.List;

import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.Peticao;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoDependencia;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ProcessoDependenciaDao extends GenericDao<ProcessoDependencia, Long> {
	public List<ProcessoDependencia> recuperarApensos(Processo processo) throws DaoException;

	public List<ProcessoDependencia> recuperarTodosApensos(Processo processo) throws DaoException;

	public List<ProcessoDependencia> recuperarTodosApensadosAo(Processo processo) throws DaoException;

	public Integer getQuantidadeVinculados(Processo processo) throws DaoException;

	public List<ProcessoDependencia> pesquisarDependencias(AndamentoProcesso andamentoProcesso) throws DaoException;

	public boolean isPeticaoJuntada(Processo processo, Peticao peticao) throws DaoException;

	public boolean isProcessoApensado(Processo processo, Processo processoPrincipal) throws DaoException;

	public void finalizarApenso(Processo processo, Processo processoPrincipal) throws DaoException;

	public void finalizarSobrestamentos(AndamentoProcesso andamentoProcessoVinculador) throws DaoException;

	public ProcessoDependencia getProcessoVinculador(Processo processo) throws DaoException;
	
	public boolean isApenso(Processo processo) throws DaoException;

}
