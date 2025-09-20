package br.gov.stf.estf.processostf.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.ProcessoIntegracao;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;

public interface ProcessoIntegracaoDao extends GenericDao<ProcessoIntegracao, Long> {

	public ProcessoIntegracao pesquisar(Long idAndamentoProcesso, String classeProcesso, Long numeroProcesso) throws DaoException;
	
	public List<ProcessoIntegracao> pesquisar(Integer codOrgao, String tipoSituacao, Date dataInicial, Date dataFinal, Integer numProcesso, String siglaProcesso, Integer... tipoComunicacao) throws DaoException;

	public void excluir(ProcessoIntegracao processoIntegracao)	throws DaoException;
	
	public List<ProcessoIntegracao> pesquisar(AndamentoProcesso andamentoProcesso) throws DaoException;

	public boolean isAvisoLido(AndamentoProcesso andamentoProcesso) throws DaoException;
	
	public void inserirProcessoIntegracaoUsuario(ProcessoIntegracao processoIntegracao, Long usuarioExternoESTF) throws DaoException;
	
	public void inserirProcessoIntegracaoLog(ProcessoIntegracao processoIntegracao, String descricao) throws DaoException;
	
	public void chamaPrcPreparaInterop(ObjetoIncidente<?> objetoIncidente) throws DaoException;

	public void excluirAvisosAndamentoDeBaixa(Long IdAndamentoProcesso) throws DaoException;
    
    public void excluirProcessoIntegracaoUsuarios(ProcessoIntegracao pi) throws DaoException;
    
    public void excluirParametroIntegracao(ProcessoIntegracao pi) throws DaoException;
    
    public Long pesquisarQtdAvisosLidos(Integer seqUsuarioExterno) throws DaoException;
}
