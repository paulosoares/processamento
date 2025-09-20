package br.gov.stf.estf.processostf.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.ProcessoIntegracao;
import br.gov.stf.estf.processostf.model.dataaccess.ProcessoIntegracaoDao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface ProcessoIntegracaoService extends GenericService<ProcessoIntegracao, Long, ProcessoIntegracaoDao> {
	
	public ProcessoIntegracao pesquisar(Long idAndamentoProcesso, String classeProcesso, Long numeroProcesso) throws ServiceException;
	
	public List<ProcessoIntegracao> pesquisar(Integer codOrgao, String tipoSituacao, Date dataInicial, Date dataFinal, Integer numProcesso, String siglaProcesso, Integer... tipoComunicacao) throws ServiceException;
	
	public List<ProcessoIntegracao> pesquisar(AndamentoProcesso andamentoProcesso) throws ServiceException;
	
	public boolean isAvisoLido(AndamentoProcesso andamentoProcesso) throws ServiceException;

	public void excluir(ProcessoIntegracao processoIntegracao) throws ServiceException;
	
	public void inserirEncaminhadoPorMidia(ProcessoIntegracao processoIntegracao, Long usuarioExternoESTF, String descricao) throws ServiceException;
	
	public void chamaPrcPreparaInterop(ObjetoIncidente<?> objetoIncidente) throws ServiceException;
	
	public void incluirEncaminhadoPorMidia(ProcessoIntegracao processoIntegracao, Processo processo, AndamentoProcesso andamentoProcesso,Long usuarioExternoESTF, String observacao) throws ServiceException;
    
	public void excluirAvisosAndamentoDeBaixa(AndamentoProcesso andamentoProcesso) throws ServiceException;
	
	public void atualizaSession() throws DaoException;
	
	public Long pesquisarQtdAvisosLidos(Integer seqUsuarioExterno) throws ServiceException;
	
	public int notificarBaixaMNI(Long objetoIncidenteId, Long seqAndamentoProcesso);
	
}
