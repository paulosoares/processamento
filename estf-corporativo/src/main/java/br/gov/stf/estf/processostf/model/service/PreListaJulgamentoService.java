package br.gov.stf.estf.processostf.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.julgamento.ListaJulgamento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.Agrupador;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamento;
import br.gov.stf.estf.entidade.processostf.PreListaJulgamentoObjetoIncidente;
import br.gov.stf.estf.entidade.usuario.Usuario;
import br.gov.stf.estf.processostf.model.dataaccess.PreListaJulgamentoDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface PreListaJulgamentoService extends GenericService<PreListaJulgamento, Long, PreListaJulgamentoDao> {
	
	public PreListaJulgamentoObjetoIncidente alterarProcessoParaRevisado(ObjetoIncidente<?> objetoIncidente, PreListaJulgamento preListaJulgamento, Boolean revisado, Usuario revisor) throws ServiceException;
	
	public List<PreListaJulgamento> listarPreListasJulgamentoDoSetor (Setor gabinete, Boolean ordenarListasPorData) throws ServiceException;
	
	public PreListaJulgamento recuperarPreListaJulgamentoAtiva(ObjetoIncidente<?> objetoIncidente) throws ServiceException;
	
	public PreListaJulgamentoObjetoIncidente recuperarPreListaJulgamentoObjetoIncidente (ObjetoIncidente<?> objetoIncidente) throws ServiceException;
	
	public void removerProcessosRevisados(PreListaJulgamento preLista) throws ServiceException;
	
	public PreListaJulgamento recuperarPorListaJulgamento (ListaJulgamento listaJulgamento) throws ServiceException;
	
	public PreListaJulgamento recuperarPreListaPorCategoria(Agrupador agrupador) throws ServiceException;

	public List<PreListaJulgamento> listarPreListasJulgamentoDoSetor(Setor setorCarregado) throws ServiceException;
	
	public Long recuperarProximoSequencialParaNomeLista(Integer ano, Long codMinistro, Boolean avulso) throws ServiceException;

	public PreListaJulgamento recuperarPreListaDeCancelamentoDePedidoDeDestaque(Setor setorDoUsuario) throws ServiceException;

	public PreListaJulgamentoObjetoIncidente incluirObjetoIncidenteNaPreLista(PreListaJulgamento entidade, ObjetoIncidente<?> oi) throws ServiceException;
	
	public PreListaJulgamentoObjetoIncidente incluirObjetoIncidenteNaPreLista(PreListaJulgamento preLista, ObjetoIncidente<?> oi, ListaJulgamento listaJulgamento) throws ServiceException;

	PreListaJulgamento recuperarPorIdComObjetoIncidente(Long id) throws ServiceException;
}