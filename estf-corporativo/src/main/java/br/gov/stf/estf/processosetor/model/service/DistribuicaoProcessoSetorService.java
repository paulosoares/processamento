package br.gov.stf.estf.processosetor.model.service;

import java.util.List;

import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processosetor.ControleDistribuicao;
import br.gov.stf.estf.entidade.processosetor.HistoricoDistribuicao;
import br.gov.stf.estf.entidade.processosetor.ProcessoSetor;
import br.gov.stf.estf.entidade.processosetor.UsuarioDistribuicaoClasseTipoJulgamento;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.usuario.UsuarioDistribuicao;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.service.ServiceException;

public interface DistribuicaoProcessoSetorService {
    
	/**
	 * metodo resposanvel por distribuir o processoSetor verifica se o processo ja possui um 
	 * usuario que o esta anallizando e antes de alterar inclui esse usuario no 
	 * historicoDistribuicaoSetor
	 * 
	 * @param processoSetor processoSetor a ser distribuido
	 * @param usuario usuario que ira recebero processo 
	 * @return Boolean
	 * @throws ServiceException
	 * @since 1.1
	 * @athor Tiagocp, Guilhermea
	 */
	public Boolean distribuirProcessoSetor(HistoricoDistribuicao distribuicao) 
	throws ServiceException;
	
    /**
     * Metodo responsavel por distribuir os processos entre os usuarios
     * quando possuir compensação o sistema irá distribuir o processo de acordo com a quandade 
     * de processo que usuario ja possui até que se igualem.
     * @param listaProcessoSetor lista de Processos
     * @param listaUsuários lista de usuarios
     * @param compensacao 
     * @return boolean
     * @throws ServiceException
     * @since 1.0
     * @athor guilhermea
     */
	public Boolean distribuirProcessoSetorAutomaticamente(List<ProcessoSetor> listaProcessoSetor, 
            List<UsuarioDistribuicao> listaUsuarios) 
			throws ServiceException ;	
	
    /**
	 * Método responsavel por confirmar os processos que foram distribuidos no método confirmarDistribuicaoProcessoSetorClasseTipoJulgamento.
	 * @param listaMapaDistribuicao
	 * @return
	 * @throws ServiceException
	 * @throws RegraDeNegocioException
	 * @author GuilhermeA
	 */
	public Boolean confirmarDistribuicaoProcessoSetorClasseTipoJulgamento(List<UsuarioDistribuicaoClasseTipoJulgamento> listaMapaDistribuicao)throws 
	ServiceException;
	
	
	/**
     * Método responsável por distribuir os processos entre os usuarios compensando por classe processual e tipo de julgamento
     * tentando igualar o valor devedor até que esse valor seja igual a 0, o objeto UsuarioDistribuicao tem que estar com a 
     * listaMapaDistribuicao inforamda
     * @param listaProcessoSetor lista de Processos
     * @param listaUsuarios lista de usuarios
     * @param compensacao 
     * @return boolean
     * @throws ServiceException
     * @since 1.0
     * @athor GuilhermeA
     */
	public List<UsuarioDistribuicaoClasseTipoJulgamento> previaDistribuicaoProcessoSetorTipoJulgamento(List<HistoricoDistribuicao> listaProcesso, 
			List<UsuarioDistribuicaoClasseTipoJulgamento> listaUsuarioDistribuicao)throws ServiceException;
	
	public List<UsuarioDistribuicaoClasseTipoJulgamento> calcularDebitoDistribuicaoManual(List<UsuarioDistribuicaoClasseTipoJulgamento> listaUsuarioDistribuicao) throws ServiceException;
	
	public ControleDistribuicao recuperarControleDistribuicao( Long id,Long idGrupoUsuario, String sigClasse, String tipoJuglamento,String sigUsuario ) 
	throws ServiceException;
	
	public List<ControleDistribuicao> pesquisarControleDistribuicao( Long id,Long idGrupoUsuario, String sigClasse, String tipoJuglamento,String sigUsuario ) 
	throws ServiceException;
	
	public Boolean cancelarDistribuicao(List<ProcessoSetor> processos,boolean atualizarControleDistribuicao) throws ServiceException;
	
	public Boolean excluirControleDistribuicao(ControleDistribuicao controleDistribuicao)throws ServiceException;
	
	/**
	 * Retorna o grupo de distribuição no qual o processo foi distribuído. 
	 */
	public Long pesquisarGrupoDistribuicao(Processo processo) throws ServiceException;
	
	/**
	 * Subtrai o valor do mapa de distribuição do grupo do Ministro
	 */
	public void subtrairMapaDistribuicao(Long quantidade, Long grupoDistribuicao, Ministro ministro) throws ServiceException;
}
