package br.gov.stf.estf.processosetor.model.service;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.localizacao.TipoFaseSetor;
import br.gov.stf.estf.entidade.processosetor.EstatisticaProcessoSetor;
import br.gov.stf.estf.entidade.processosetor.ProcessoSetor;
import br.gov.stf.estf.entidade.processosetor.RelatorioAnaliticoProcessoSetor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.entidade.processostf.Classe;
import br.gov.stf.estf.entidade.processostf.ClasseConversao;
import br.gov.stf.estf.processosetor.model.dataaccess.ProcessoSetorDao;
import br.gov.stf.estf.processosetor.model.service.impl.ProcessoSetorEletronicoSearchData;
import br.gov.stf.estf.processosetor.model.util.ProcessoSetorSearchData;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.SearchResult;

public interface ProcessoSetorService extends GenericService<ProcessoSetor, Long, ProcessoSetorDao> {

	/**
	 * Metodo resposanvel por pesquisar processo por setor
	 * @param sigla classe do processo
	 * @param numero numero do processo
	 * @param recurso codigo do recurso do processo
	 * @param idSetor codido do setor do usuario que ira relaizar a pesquisa
	 * @return Processo Setor
	 * @throws ServiceException
	 * @since 1.0
	 * @athor tiagocp
	 */
	public ProcessoSetor recuperarProcessoSetor(String sigla, Long numero, Short recurso, Long idSetor) 
	throws ServiceException;

	/**
	 * metodo responsavel por recuperar o protocolo do setor informado
	 * @param numeroProtocolo numero do protocolo 
	 * @param anoProtocolo ano do protocolo
	 * @param idSetor codigo do setor do usuario 
	 * @return ProcessoSetor
	 * @throws ServiceException
	 * @since 1.0
	 * @athor Tiagocp
	 */
	public ProcessoSetor recuperarProcessoSetor(Long numeroProtocolo, Short anoProtocolo, Long idSetor ) 
	throws ServiceException;

	/**
	 * Método resposável pela pesquisa de processos ou protocolos
	 * @param anoProtocolo ano do protocolo
	 * @param numeroProtocolo número do protocolo
	 * @param sigla sigla da classe do processo
	 * @param numeroProcesso numero do processo
	 * @param recurso numero do recurso
	 * @param idSetor codigo do setor do usuario 
	 * @return lista de ProcessoSetor
	 * @throws DaoException
	 * @since 1.0
	 * @athor leonardod

	public List<ProcessoSetor> pesquisarProcessoSetor(Short anoProtocolo, Long numeroProtocolo, 
			String sigla, Long numeroProcesso, Short recurso, String siglaTipoJulgamento, 
			TipoMeioProcesso tipoMeioProcesso, Long idSetor) 
	throws ServiceException;
	 */

	/**
	 * @deprecated Utilizar {@link #pesquisarQuantidadeProcessoSetor(ProcessoSetorSearchData)} ao invés deste.
	 */
	@Deprecated
	public Long pesquisarQuantidadeProcessoSetor(
			Short anoProtocolo, Long numeroProtocolo, 
			String siglasClassesProcessuaisAgrupadas, String sigla, Long numeroProcesso, Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada, 
			String siglaTipoJulgamento, String codigoTipoMeioProcesso,
			Long numeroPeticao, 
			String codigoAssunto, String descricaoAssunto, String complementoAssunto, 
			Long codigoMinistroRelator, 
			String numeroSala, String numeroArmario, String numeroEstante, String numeroPrateleira, 
			String numeroColuna,
			String obsDeslocamento,
			Boolean pesquisarAssuntoEmTodosNiveis,
			Boolean pesquisarInicio,
			Long idSecaoUltimoDeslocamento,
			String siglaUsuarioDistribuicao,
			Long idGrupoProcessoSetor,
			Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal,
			Date dataDistribuicaoInicial, Date dataDistribuicaoFinal,
			Date dataFaseInicial, Date dataFaseFinal,
			Date dataRemessaInicial, Date dataRemessaFinal,
            Date dataRecebimentoInicial, Date dataRecebimentoFinal,
            Date dataEntradaInicial, Date dataEntradaFinal,
            Date dataSaidaInicial, Date dataSaidaFinal,
			Long idSetor,
			Long idTipoUltimaFaseSetor,
			Long idTipoUltimoStatusSetor,
			Boolean faseAtualProcesso,
			Boolean repercussaoGeralCheckbox,
			Boolean protocoloNaoAutuadoCheckbox,
			Boolean semLocalizacao, 
            Boolean semFase, 
            Boolean semDistribuicao,
            Boolean semVista,
            Long idCategoriaPartePesquisa,
            String nomeParte,
            Long idTipoTarefa,
            Boolean localizadosNoSetor, 
            Boolean emTramiteNoSetor,
            Boolean possuiLiminar, 
            Boolean possuiPreferencia,
            Boolean sobrestado,
            Boolean julgado, Boolean mostraProcessoReautuadoRejeitadoCheckbox,
			List<Andamento> listaIncluirAndamentos, 
			Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos,
			List<Andamento> listaNaoIncluirAndamentos,
			Date dataInicialNaoIncluirAndamentos, Date dataFinalNaoIncluirAndamentos)            
	throws ServiceException;	
	
	public Long pesquisarQuantidadeProcessoSetor(ProcessoSetorSearchData searchData) throws ServiceException;

	/**
	 * @deprecated Utilizar {@link #pesquisarProcessoSetor(ProcessoSetorSearchData)} ao invés deste.
	 */
	@Deprecated
	public List<ProcessoSetor> pesquisarProcessoSetor(
            Short anoProtocolo, Long numeroProtocolo, 
            String siglasClassesProcessuaisAgrupadas, String sigla, Long numeroProcesso, Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada, 
			String siglaTipoJulgamento, String codigoTipoMeioProcesso, 
			Long numeroPeticao, 
			String codigoAssunto, String descricaoAssunto, String complementoAssunto, 
			Long codigoMinistroRelator, 
			String numeroSala, String numeroArmario, String numeroEstante, String numeroPrateleira, 
			String numeroColuna,
			String obsDeslocamento,
			Boolean pesquisarAssuntoEmTodosNiveis,
			Boolean pesquisarInicio,
			Long idSecaoUltimoDeslocamento, 
			String siglaUsuarioDistribuicao, 
		    Long idGrupoProcessoSetor, 
		    Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal,
            Date dataDistribuicaoInicial, Date dataDistribuicaoFinal, 
            Date dataFaseInicial, Date dataFaseFinal, 
            Date dataRemessaInicial, Date dataRemessaFinal, 
            Date dataRecebimentoInicial, Date dataRecebimentoFinal, 
            Date dataEntradaInicial, Date dataEntradaFinal, 
            Date dataSaidaInicial, Date dataSaidaFinal,
            Long idSetor, 
		    Long idTipoUltimaFaseSetor, Long idTipoUltimoStatusSetor,
		    Boolean faseAtualProcesso,
		    Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox, Boolean mostraProcessoReautuadoRejeitadoCheckbox,
		    Boolean semLocalizacao, Boolean semFase, Boolean semDistribuicao, Boolean semVista,
            Long idCategoriaPartePesquisa,
            String nomeParte,
		    Long idTipoTarefa,
		    Boolean localizadosNoSetor, 
            Boolean emTramiteNoSetor,
		    Boolean possuiLiminar, 
            Boolean possuiPreferencia,
            Boolean sobrestado,
            Boolean julgado,
		    Boolean preFetchAssunto, 
		    Boolean readOnlyQuery,
		    Boolean limitarPesquisa,
		    Boolean orderByProcesso, Boolean orderByProtocolo,Boolean orderByValorGut,Boolean orderByDataEntrada, Boolean orderByAssunto, 
		    Boolean orderByCrescente,
			List<Andamento> listaIncluirAndamentos, 
			Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos,
			List<Andamento> listaNaoIncluirAndamentos,
			Date dataInicialNaoIncluirAndamentos, Date dataFinalNaoIncluirAndamentos)
			throws ServiceException;
	
	/**
	 * @deprecated Utilizar {@link #pesquisarProcessoSetor(ProcessoSetorSearchData)} ao invés deste.
	 */
	@Deprecated
    public List<EstatisticaProcessoSetor> pesquisarProcessoSetor(Short anoProtocolo, Long numeroProtocolo, 
    		String siglasClassesProcessuaisAgrupadas, String sigla, Long numeroProcesso, Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada, 
            String siglaTipoJulgamento,
            String codigoTipoMeioProcesso,
            Long numeroPeticao, 
            String codigoAssunto,
            String descricaoAssunto,
            String complementoAssunto,            
            Long codigoMinistroRelator,
            String numeroSala,
            String numeroArmario,
            String numeroEstante,
            String numeroPrateleira,
            String numeroColuna,
            String obsDeslocamento,
            Boolean pesquisarAssuntoEmTodosNiveis,
            Boolean pesquisarInicio,
            Long idSecaoUltimoDeslocamento,
            String siglaUsuarioDistribuicao,
            Long idGrupoProcessoSetor,
            Date dataDistribuicaoMinistroInicial, 
            Date dataDistribuicaoMinistroFinal,
            Date dataDistribuicaoInicial,
            Date dataDistribuicaoFinal,
            Date dataFaseInicial,
            Date dataFaseFinal,
            Date dataRemessaInicial,
            Date dataRemessaFinal,
            Date dataRecebimentoInicial,
            Date dataRecebimentoFinal,
            Date dataEntradaInicial,
            Date dataEntradaFinal,
            Date dataSaidaInicial,
            Date dataSaidaFinal,
            Long idSetor,
            Long idTipoUltimaFaseSetor,
            Long idTipoUltimoStatusSetor,
            Boolean faseAtualProcesso,
            Boolean repercussaoGeralCheckbox,
            Boolean protocoloNaoAutuadoCheckbox,
            Boolean semLocalizacao,
            Boolean semFase,
            Boolean semDistribuicao,
            Boolean semVista,
            Long idCategoriaPartePesquisa,
            String nomeParte,
            Long idTipoTarefa,
            Boolean localizadosNoSetor, 
            Boolean emTramiteNoSetor,
            Boolean possuiLiminar, 
            Boolean possuiPreferencia,
            Boolean sobrestado,
            Boolean julgado,
			List<Andamento> listaIncluirAndamentos, 
			Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos,
			List<Andamento> listaNaoIncluirAndamentos,
			Date dataInicialNaoIncluirAndamentos, Date dataFinalNaoIncluirAndamentos,
            Boolean groupByFase,
            Boolean groupByFaseStatus,
            Boolean groupByDistribuicao,
            Boolean groupByDeslocamento,
            Boolean groupByAssunto) throws ServiceException; 

	public Boolean persistirProcessoSetor(ProcessoSetor processoSetor) throws ServiceException;

	public Boolean alterarProcessoSetor(ProcessoSetor processoSetor) throws ServiceException;

	/**
	 * Método responsável por recuperar os protocolos associados a determinado usuário. 
	 * A associação é dada pela distribuição "interna" que pode ser realizada de forma manual ou automática.
	 * @param siglaUsuario usuário 
	 * @return List<ProcessoSetor>
	 * @throws ServiceException
	 * @since 1.0
	 * @athor Thiagom
	 */        
	public List<ProcessoSetor> pesquisarProcessoSetor(String siglaUsuario ) 
	throws ServiceException;
	
	public Boolean isProcessoProtocoloPosseUsuario(Long idProcessoSetor, 
			String siglaUsuario, Long idSetorUsuario) 
	throws ServiceException;
	
	
    public List<ProcessoSetor> pesquisarProcessoSetor(String sigla, Long idSetor ) throws ServiceException;
	
    public List<EstatisticaProcessoSetor> pesquisarProcessoSetorEstatistica(String sigla , Long idSetor)
    throws ServiceException;
	
	public List<RelatorioAnaliticoProcessoSetor> pesquisarRelatorioAnaliticoProcessoSetor( ProcessoSetorSearchData pssd ) 
	throws ServiceException;
	
    public List<EstatisticaProcessoSetor> pesquisarRelatorioSinteticoProcessoSetor( ProcessoSetorSearchData pssd )  
    throws ServiceException;	
	
	public List<ProcessoSetor> pesquisarProcessoSetor(TipoFaseSetor faseSetor)
	throws ServiceException;
	
	public List<ProcessoSetor> pesquisarProcessoSetor(SecaoSetor secaoSetor, Long idSetor)
	throws ServiceException;
	
	public SearchResult pesquisarProcessoSetor( ProcessoSetorSearchData sdProcessoSetor )throws ServiceException;
	
	public ProcessoSetor recuperarProcessoSetor(Long seqObjetoIncidente, Long idSetor) throws ServiceException;
	public List<ProcessoSetor> recuperarListaProcessoSetor(Long seqObjetoIncidente, Long idSetor) throws ServiceException;
	
	public Integer pesquisarQuantidadeComplementoAssunto( String complementoAssunto, Long idSetor) throws ServiceException;
	
	public List<String> pesquisarComplementoAssunto( String complementoAssunto, Long idSetor, Short numeroMaximoDeResultados ) throws ServiceException;
	
	public SearchResult pesquisarProcessoSetorCanetaOtica( ProcessoSetorSearchData sdProcessoSetor, List<Classe> listaClasse, List<ClasseConversao> listaClasseAntiga )throws ServiceException;
	
	public Integer pesquisarQuantidadeProcessoSetorEletronico( ProcessoSetorEletronicoSearchData sd ) throws ServiceException;
	public SearchResult<ProcessoSetor> pesquisarProcessoSetorEletronico( ProcessoSetorEletronicoSearchData sd ) throws ServiceException;
	public void limparFaseAtual(List<ProcessoSetor> processosSelecionados) throws ServiceException;
		
}
