package br.gov.stf.estf.processosetor.model.dataaccess;

import java.util.Date;
import java.util.List;

import br.gov.stf.estf.entidade.localizacao.SecaoSetor;
import br.gov.stf.estf.entidade.localizacao.TipoFaseSetor;
import br.gov.stf.estf.entidade.processosetor.EstatisticaProcessoSetor;
import br.gov.stf.estf.entidade.processosetor.ProcessoSetor;
import br.gov.stf.estf.entidade.processosetor.RelatorioAnaliticoProcessoSetor;
import br.gov.stf.estf.entidade.processostf.Andamento;
import br.gov.stf.estf.processosetor.model.service.impl.ProcessoSetorEletronicoSearchData;
import br.gov.stf.estf.processosetor.model.util.ProcessoSetorSearchData;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.dataaccess.GenericDao;
import br.gov.stf.framework.util.SearchResult;

public interface ProcessoSetorDao extends GenericDao<ProcessoSetor, Long> {
	
	public ProcessoSetor recuperarProcessoSetor(String sigla, Long numero, Short recurso, Long idSetor) 
	throws DaoException;
	 
	public ProcessoSetor recuperarProcessoSetor(Long numeroProtocolo, Short anoProtocolo, Long idSetor ) 
	throws DaoException;
	
	/*public Long pesquisarQuantidadeProcessoSetor(
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
			Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox,
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
			List<Andamento> listaIncluirAndamentos, 
			Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos,
			List<Andamento> listaNaoIncluirAndamentos,
			Date dataInicialNaoIncluirAndamentos, Date dataFinalNaoIncluirAndamentos)
	throws DaoException;		*/
	
	public List<ProcessoSetor> pesquisarProcessoSetor(
            Short anoProtocolo, Long numeroProtocolo, 
            String siglasClassesProcessuaisAgrupadas, String sigla, Long numeroProcesso, Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada, 
			String siglaTipoJulgamento, 
			String codigoTipoMeioProcesso, 
			Long numeroPeticao, 
			String codigoAssunto, String descricaoAssunto, String complementoAssunto, 
			Long codigoMinistroRelator, 
			String numeroSala, String numeroArmario, String numeroEstante, String numeroPrateleira, 
			String numeroColuna,
			String obsDeslocamento,
			Boolean pesquisarAssuntoEmTodosNiveis,
			Boolean pesquisarInicio,
			Long idSecaoUltimoDeslocamento, 
			String siglaUsuarioDistribuicao,Long idGrupoProcessoSetor, 
			Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal,
			Date dataDistribuicaoInicial,
		    Date dataDistribuicaoFinal, Date dataFaseInicial, Date dataFaseFinal, Date dataRemessaInicial,
            Date dataRemessaFinal, Date dataRecebimentoInicial, Date dataRecebimentoFinal, 
            Date dataEntradaInicial, Date dataEntradaFinal, Date dataSaidaInicial, Date dataSaidaFinal, Long idSetor, 
		    Long idTipoUltimaFaseSetor, Long idTipoUltimoStatusSetor,
		    Boolean faseAtualProcesso,
		    Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox,
		    Boolean semLocalizacao,Boolean semFase,Boolean semDistribuicao, Boolean semVista,
            Long idCategoriaPartePesquisa,
            String nomeParte,
		    Long idTipoTarefa,
            Boolean localizadosNoSetor, 
            Boolean emTramiteNoSetor,
            Boolean possuiLiminar, 
            Boolean possuiPreferencia, 
            Boolean sobrestado,
            Boolean julgado,
            Boolean preFetchAssunto, Boolean readOnlyQuery,Boolean limitarPequisa,		
			Boolean orderByProcesso,
            Boolean orderByProtocolo,
            Boolean orderByValorGut,
            Boolean orderByDataEntrada,
            Boolean orderByAssunto,
            Boolean orderByCrescente,
			List<Andamento> listaIncluirAndamentos, 
			Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos,
			List<Andamento> listaNaoIncluirAndamentos,
			Date dataInicialNaoIncluirAndamentos, Date dataFinalNaoIncluirAndamentos) 
	throws DaoException;

    public List<EstatisticaProcessoSetor> pesquisarProcessoSetor(
    		Short anoProtocolo, Long numeroProtocolo, 
    		String siglasClassesProcessuaisAgrupadas, String sigla, Long numeroProcesso, Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada, 
            String siglaTipoJulgamento,
            String codigoTipoMeioProcesso,
            Long numeroPeticao, 
            String codigoAssunto, String descricaoAssunto, String complementoAssunto, 
            Long codigoMinistroRelator, 
            String numeroSala,String numeroArmario,String numeroEstante,String numeroPrateleira,String numeroColuna,
            String obsDeslocamento,
            Boolean pesquisarAssuntoEmTodosNiveis,
            Boolean pesquisarInicio,
            Long idSecaoUltimoDeslocamento,
            String siglaUsuarioDistribuicao,
            Long idGrupoProcessoSetor,
            Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal,
            Date dataDistribuicaoInicial,Date dataDistribuicaoFinal,
            Date dataFaseInicial,Date dataFaseFinal,
            Date dataRemessaInicial,Date dataRemessaFinal,
            Date dataRecebimentoInicial,Date dataRecebimentoFinal,
            Date dataEntradaInicial,Date dataEntradaFinal,
            Date dataSaidaInicial, Date dataSaidaFinal,
            Long idSetor,
            Long idTipoUltimaFaseSetor,
            Long idTipoUltimoStatusSetor,
            Boolean faseAtualProcesso,
            Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox,
            Boolean semLocalizacao,Boolean semFase,Boolean semDistribuicao, Boolean semVista, 
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
            Boolean groupByFase,Boolean groupByFaseStatus,Boolean groupByDistribuicao,Boolean groupByDeslocamento, Boolean groupByAssunto) 
    throws DaoException;  
    
	public List<RelatorioAnaliticoProcessoSetor> pesquisarRelatorioAnaliticoProcessoSetor(
            Short anoProtocolo, Long numeroProtocolo, 
            String siglasClassesProcessuaisAgrupadas, String sigla, Long numeroProcesso, Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada, 
			String siglaTipoJulgamento, 
			String codigoTipoMeioProcesso, 
			Long numeroPeticao, 
			String codigoAssunto, String descricaoAssunto, String complementoAssunto, 
			Long codigoMinistroRelator, 
			String numeroSala, String numeroArmario, String numeroEstante, String numeroPrateleira, 
			String numeroColuna,
			String obsDeslocamento,
			Boolean pesquisarAssuntoEmTodosNiveis,
			Boolean pesquisarInicio,
			Long idSecaoUltimoDeslocamento, 
			String siglaUsuarioDistribuicao,Long idGrupoProcessoSetor, 
			Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal,
			Date dataDistribuicaoInicial,
		    Date dataDistribuicaoFinal, Date dataFaseInicial, Date dataFaseFinal, Date dataRemessaInicial,
            Date dataRemessaFinal, Date dataRecebimentoInicial, Date dataRecebimentoFinal, 
            Date dataEntradaInicial, Date dataEntradaFinal, Date dataSaidaInicial, Date dataSaidaFinal, Long idSetor, 
		    Long idTipoUltimaFaseSetor, Long idTipoUltimoStatusSetor, 
		    Boolean faseProcessualAtual, Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox,
		    Boolean semLocalizacao,Boolean semFase,Boolean semDistribuicao, Boolean semVista,
            Long idCategoriaPartePesquisa,
            String nomeParte,
		    Long idTipoTarefa,
            Boolean localizadosNoSetor, 
            Boolean emTramiteNoSetor,
            Boolean possuiLiminar, 
            Boolean possuiPreferencia, 
            Boolean sobrestado,
            Boolean julgado,
            Boolean preFetchAssunto, Boolean readOnlyQuery,Boolean limitarPequisa,
            String tipoRelatorio,
            Boolean groupByFase, 
            Boolean groupByFaseStatus,	
            Boolean groupByDistribuicao, 
            Boolean groupByDeslocamento, 
            Boolean groupByAssunto,
			Boolean orderByProcesso,
            Boolean orderByProtocolo,
            Boolean orderByValorGut,
            Boolean orderByDataEntrada,
            Boolean orderByAssunto,
            Boolean orderByCrescente,
			List<Andamento> listaIncluirAndamentos, 
			Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos,
			List<Andamento> listaNaoIncluirAndamentos,
			Date dataInicialNaoIncluirAndamentos, Date dataFinalNaoIncluirAndamentos) 
    throws DaoException;
    
    public List<EstatisticaProcessoSetor> pesquisarRelatorioSinteticoProcessoSetor(
            Short anoProtocolo, Long numeroProtocolo, 
            String siglasClassesProcessuaisAgrupadas, String sigla, Long numeroProcesso, Short recurso, Boolean possuiRecurso, String siglaRecursoUnificada, 
			String siglaTipoJulgamento, 
			String codigoTipoMeioProcesso, 
			Long numeroPeticao, 
			String codigoAssunto, String descricaoAssunto, String complementoAssunto, 
			Long codigoMinistroRelator, 
			String numeroSala, String numeroArmario, String numeroEstante, String numeroPrateleira, 
			String numeroColuna,
			String obsDeslocamento,
			Boolean pesquisarAssuntoEmTodosNiveis,
			Boolean pesquisarInicio,
			Long idSecaoUltimoDeslocamento, 
			String siglaUsuarioDistribuicao,Long idGrupoProcessoSetor, 
			Date dataDistribuicaoMinistroInicial, Date dataDistribuicaoMinistroFinal,
			Date dataDistribuicaoInicial,
		    Date dataDistribuicaoFinal, Date dataFaseInicial, Date dataFaseFinal, Date dataRemessaInicial,
            Date dataRemessaFinal, Date dataRecebimentoInicial, Date dataRecebimentoFinal, 
            Date dataEntradaInicial, Date dataEntradaFinal, Date dataSaidaInicial, Date dataSaidaFinal, Long idSetor, 
		    Long idTipoUltimaFaseSetor, Long idTipoUltimoStatusSetor, 
		    Boolean faseProcessualAtual, Boolean repercussaoGeralCheckbox, Boolean protocoloNaoAutuadoCheckbox,
		    Boolean semLocalizacao,Boolean semFase,Boolean semDistribuicao, Boolean semVista,
            Long idCategoriaPartePesquisa,
            String nomeParte,
		    Long idTipoTarefa,
            Boolean localizadosNoSetor, 
            Boolean emTramiteNoSetor,
            Boolean possuiLiminar, 
            Boolean possuiPreferencia, 
            Boolean sobrestado,
            Boolean julgado,
            Boolean preFetchAssunto, Boolean readOnlyQuery,Boolean limitarPequisa,
            String tipoRelatorio,
            Boolean groupByFase, 
            Boolean groupByFaseStatus,	
            Boolean groupByDistribuicao, 
            Boolean groupByDeslocamento, 
            Boolean groupByAssunto,
			Boolean orderByProcesso,
            Boolean orderByProtocolo,
            Boolean orderByValorGut,
            Boolean orderByDataEntrada,
            Boolean orderByAssunto,
            Boolean orderByCrescente,
			List<Andamento> listaIncluirAndamentos, 
			Date dataInicialIncluirAndamentos, Date dataFinalIncluirAndamentos,
			List<Andamento> listaNaoIncluirAndamentos,
			Date dataInicialNaoIncluirAndamentos, Date dataFinalNaoIncluirAndamentos) 
    throws DaoException;
	
    public List<ProcessoSetor> pesquisarProcessoSetor(String sigla, Long idSetor ) throws DaoException;
	
    public List<EstatisticaProcessoSetor> pesquisarProcessoSetorEstatistica(String sigla , Long idSetor)
    throws DaoException;
    
	public Boolean persistirProcessoSetor(ProcessoSetor processoSetor) throws DaoException;
	
	public Boolean alterarProcessoSetor(ProcessoSetor processoSetor) throws DaoException;
    
    public List<ProcessoSetor> pesquisarProcessoSetor(String siglaUsuario ) 
    throws DaoException;
    
    public Boolean isProcessoProtocoloPosseUsuario(Long idProcessoSetor, 
    		String siglaUsuario, Long idSetorUsuario) 
    throws DaoException;
    
    public List<RelatorioAnaliticoProcessoSetor> pesquisarRelatorioAnaliticoProcessoSetor( ProcessoSetorSearchData pssd )
    throws DaoException;
    
    public List<EstatisticaProcessoSetor> pesquisarRelatorioSinteticoProcessoSetor( ProcessoSetorSearchData pssd ) 
    throws DaoException;
    
	public List<ProcessoSetor> pesquisarProcessoSetor(TipoFaseSetor faseSetor)
	throws DaoException;    
	
	public List<ProcessoSetor> pesquisarProcessoSetor(SecaoSetor secaoSetor, Long idSetor )
	throws DaoException; 
	
	public void evictObjectSession(Object obj) throws DaoException;
	
	public void refreshObjectSession(Object obj) throws DaoException;
	
	public SearchResult pesquisarProcessoSetor( ProcessoSetorSearchData sdProcessoSetor )throws DaoException;
	
	public ProcessoSetor recuperarProcessoSetor(Long seqObjetoIncidente, Long idSetor) throws DaoException;
	public List<ProcessoSetor> recuperarListaProcessoSetor(Long seqObjetoIncidente, Long idSetor) throws DaoException;
	
	public Integer pesquisarQuantidadeComplementoAssunto( String complementoAssunto, Long idSetor) throws DaoException;
	
	public List<String> pesquisarComplementoAssunto( String complementoAssunto, Long idSetor, Short numeroMaximoDeResultados ) throws DaoException;
	
	public Integer pesquisarQuantidadeProcessoSetorEletronico( ProcessoSetorEletronicoSearchData sd ) throws DaoException;
	public SearchResult<ProcessoSetor> pesquisarProcessoSetorEletronico( ProcessoSetorEletronicoSearchData sd ) throws DaoException;
}
