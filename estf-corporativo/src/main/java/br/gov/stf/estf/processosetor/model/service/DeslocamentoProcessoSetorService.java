package br.gov.stf.estf.processosetor.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.Secao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processosetor.HistoricoDeslocamento;
import br.gov.stf.estf.entidade.processosetor.HistoricoDeslocamentoPeticao;
import br.gov.stf.estf.entidade.processosetor.PeticaoSetor;
import br.gov.stf.estf.entidade.processosetor.ProcessoSetor;
import br.gov.stf.estf.processosetor.model.dataaccess.DeslocamentoProcessoSetorDao;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;

public interface DeslocamentoProcessoSetorService extends GenericService<HistoricoDeslocamento, Long, DeslocamentoProcessoSetorDao> {

	/**
	 * metodo responsavel por recuperar um deslocamento 
	 * @param id codigo do deslocamento
	 * @return retorna um deslocamentoSetor
	 * @throws ServiceException
	 * @since 1.0
	 * @athor Tiagocp
	 */
	public HistoricoDeslocamento recuperarDeslocamento(Long id)
	throws ServiceException;

	/**
	 * método resposanvel por pesquisar os deslocamentos
	 * @param id codigo do deslocamento
	 * @param setor id do setor de lotação do usuario
	 * @param processoSetor id do processo ou protocolo em que o processo esta 
	 * @param dataRemessa data em que foi relazada a remessa do processo dentro do setor
	 * @param numeroSala  numero da sala onde o processo se encontra
	 * @param numeroEstante numero da estante onde o processo se encontra
	 * @param numeroPrateleira numero da prateleira onde o processo se encontra
	 * @param numeroArmario numero do armario onde o processo se encontra
	 * @return retorna uma lista de deslocametos 
	 * @throws ServiceException
	 * @since 1.0
	 * @athor guilhermea
	 */
	public List<HistoricoDeslocamento> pesquisarDeslocamentos(Long id, Setor setor, ProcessoSetor processoSetor,
			PeticaoSetor peticaoSetor, Date dataRemessa, String numeroSala, String numeroEstante, 
			String numeroPrateleira, String numeroArmario, String numeroColuna) throws ServiceException;

	/**
	 * metodo resposanvel por pesquisar os deslocamentomentos
	 * @param id codigo do deslocamento
	 * @param setor setor de lotação do usuario
	 * @param processoSetor processoSetor
	 * @param dateRecebimento data de recebimeto do processo setor
	 * @param secaoOrigem secão a onde o processoSetor se originou
	 * @param dataRemessa data em que o processoSetor foi remetido
	 * @param secaoDestino seção para qual o processo foi remetido
	 * @param numeroSala localização
	 * @param numeroEstante localização
	 * @param numeroPrateleira localização
	 * @param numeroArmario localização
	 * @param numeroColuna localização
	 * @param dataRecebimentoNula true = DataRecebimento is null, false dataRecebimento is not null
	 * @param dataRemessaNula true = dataRemessa is null, false dataRemessa is not null
	 * @return
	 * @throws ServiceException
	 */
	public List<HistoricoDeslocamento> pesquisarDeslocamentos(Long id, Setor setor, ProcessoSetor processoSetor,
			Date dataRecebimento,Secao secaoOrigem,Date dataRemessa,Secao secaoDestino, 
			String numeroSala, String numeroEstante, String numeroPrateleira,
			String numeroArmario,String numeroColuna,Boolean dataRecebimentoNula, Boolean localizadoSetor) throws ServiceException;

	/**
	 * metodo resposanvel por pesquisar os deslocamentomentos
	 * @param id codigo do deslocamento
	 * @param setor setor de lotação do usuario
	 * @param siglaClasseProcessual 
	 * @param dateRecebimento data de recebimeto do processo setor
	 * @param secaoOrigem secão a onde o processoSetor se originou
	 * @param dataRemessa data em que o processoSetor foi remetido
	 * @param secaoDestino seção para qual o processo foi remetido
	 * @param numeroSala localização
	 * @param numeroEstante localização
	 * @param numeroPrateleira localização
	 * @param numeroArmario localização
	 * @param numeroColuna localização
	 * @param semLocalizacao processo setor que ainda não tem localização
	 * @param dataInicio , dataFim perido para a data de entrada no setor
	 * @return 
	 * @throws ServiceException
	 */
	public List<HistoricoDeslocamento> pesquisarDeslocamentos(Long id, Setor setor, String siglaClasseProcessual,
			Date dataRecebimento,Secao secaoOrigem,Date dataRemessa,Secao secaoDestino, 
			String numeroSala, String numeroEstante, String numeroPrateleira,
			String numeroArmario,String numeroColuna,Boolean semLocalizacao,
			Date dataInicio,Date dataFim,Boolean emTramite) throws ServiceException;


	public Boolean registrarRemessa(List<ProcessoSetor> listaProcessoSetor, 
			HistoricoDeslocamento historicoDeslocamento) 
	throws ServiceException;

	public Boolean registrarRemessa(HistoricoDeslocamento historicoDeslocamento) 
	throws ServiceException;
	
	public Boolean registrarRemessaPeticao(HistoricoDeslocamentoPeticao historicoDeslocamento) 
	throws ServiceException;	

	public Boolean registrarRemessaPeticao(List<PeticaoSetor> listaPeticaoSetor, 
			HistoricoDeslocamentoPeticao historicoDeslocamento) 
	throws ServiceException;

	public Boolean registrarRecebimento(List<ProcessoSetor> listaProcessoSetor, 
			HistoricoDeslocamento historicoDeslocamento) 
	throws ServiceException;
	
	public Boolean registrarRecebimentoPeticao(List<PeticaoSetor> listaPeticaoSetor, 
			HistoricoDeslocamentoPeticao historicoDeslocamento) 
	throws ServiceException;

	public Boolean registrarRecebimento(HistoricoDeslocamento historicoDeslocamento) 
	throws ServiceException;
	
	public Boolean registrarRecebimentoPeticao(HistoricoDeslocamentoPeticao historicoDeslocamento) 
	throws ServiceException;	
	
	public Boolean persistirHistoricoDeslocamento(HistoricoDeslocamento historicoDeslocamento) 
    throws ServiceException;
	
	public Boolean persistirHistoricoDeslocamentoPeticao(HistoricoDeslocamentoPeticao historicoDeslocamento) 
    throws ServiceException;
	
	public Boolean excluirHistoricoDeslocamento(HistoricoDeslocamento deslocamento) 
	throws ServiceException;
	
	public List<HistoricoDeslocamento> pesquisarDeslocamentos(Long idSetor, 
            Secao secaoOrigem, Secao secaoDestino)
    throws ServiceException;
}
