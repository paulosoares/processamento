package br.gov.stf.estf.assinatura.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import br.gov.stf.estf.assinatura.security.ExpedienteAssinadoResult;
import br.gov.stf.estf.assinatura.security.UsuarioAssinatura;
import br.gov.stf.estf.assinatura.visao.jsf.beans.ComunicacaoDocumento;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoPaginatorResult;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.documento.model.util.FiltroPesquisarDocumentosAssinatura;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente.FlagProcessoLote;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.estf.intimacao.model.vo.TipoRecebimentoComunicacaoEnum;
import br.gov.stf.estf.intimacao.visao.dto.ComunicacaoExternaDTO;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.assinadorweb.api.requisicao.DocumentoPDF;

/**
 * Serviço local utilizado para realizar diversas operações sobre comunicações e documentos.
 * 
 * @author thiago.miranda
 * @since 3.16.1
 */
public interface ComunicacaoServiceLocal {
	
	List<DocumentoPDF<DocumentoComunicacao>> assinar(List<ComunicacaoDocumento> documentos) throws ServiceLocalException, RegraDeNegocioException;

	List<DocumentoPDF<DocumentoComunicacao>> assinarDB(List<? extends ComunicacaoDocumentoBase> documentos) throws ServiceLocalException, RegraDeNegocioException;
	
	void cancelarAssinatura(ComunicacaoDocumento comunicacaoDocumento, String anotacaoCancelamento, UsuarioAssinatura usuario) throws ServiceLocalException,
			RegraDeNegocioException;

	void cancelarPDF(DocumentoComunicacao docComunicacao, String observacao, UsuarioAssinatura usuario, boolean gerarFaseCorrecao)
			throws ServiceLocalException, RegraDeNegocioException;

	void cancelarPDF(DocumentoComunicacao docComunicacao,String descricaoStatusDocumento, String observacao, UsuarioAssinatura usuario, boolean gerarFaseCorrecao)
			throws ServiceLocalException, RegraDeNegocioException;

	void devolverParaSetorOrigem(ComunicacaoDocumento comunicacaoDocumento, String anotacaoCancelamento, UsuarioAssinatura usuario)
			throws ServiceLocalException, RegraDeNegocioException;

	void encaminharParaAssinaturaSetor(List<ComunicacaoDocumento> comunicacaoDocumentos, Long idSetorDestino, boolean permitirGabinete,
			boolean incluirFaseNoDeslocamento, boolean naoIncluirFaseDeslocamentoSeNaoForGabinete, TipoFaseComunicacao... tiposFasesPermitidos)
			throws ServiceLocalException, RegraDeNegocioException;
	
	void atribuirResponsavel(List<ComunicacaoDocumento> comunicacaoDocumentos, String idUsuarioResponsavel)
			throws ServiceLocalException, RegraDeNegocioException;
	
	void encaminharParaRevisaoSetor(List<ComunicacaoDocumento> comunicacaoDocumentos, Long idSetorDestino, boolean permitirGabinete,
			boolean incluirFaseNoDeslocamento, boolean naoIncluirFaseDeslocamentoSeNaoForGabinete, TipoFaseComunicacao... tiposFasesPermitidos)
			throws ServiceLocalException, RegraDeNegocioException;

	Long pesquisarSetorDestinoPadrao(List<ComunicacaoDocumento> comunicacaoDocumentos) throws ServiceLocalException, RegraDeNegocioException;

	void excluir(List<ComunicacaoDocumento> comunicacaoDocumentos) throws ServiceLocalException, RegraDeNegocioException;

	void expedir(List<ComunicacaoDocumento> comunicacaoDocumentos, UsuarioAssinatura usuario) throws ServiceLocalException, RegraDeNegocioException;

	void encaminharParaDJe(List<ComunicacaoDocumento> comunicacaoDocumentos, UsuarioAssinatura usuario) throws ServiceLocalException, RegraDeNegocioException;

	void finalizar(List<ComunicacaoDocumento> comunicacaoDocumentos, UsuarioAssinatura usuario) throws ServiceLocalException, RegraDeNegocioException;

	void finalizarRevisao(List<ComunicacaoDocumento> comunicacaoDocumentos, UsuarioAssinatura usuario) throws ServiceLocalException;
	
	void finalizarRestritosNaoJuntar(List<ComunicacaoDocumento> comunicacaoDocumentos, UsuarioAssinatura usuario) throws ServiceLocalException;
	
	void finalizarRestritosJuntar(List<ComunicacaoDocumento> comunicacaoDocumentos, UsuarioAssinatura usuario) throws ServiceLocalException, ServiceException;
	
	void finalizarRestritosApenasJuntar(List<ComunicacaoDocumento> comunicacaoDocumentos, UsuarioAssinatura usuario) throws ServiceLocalException;

	List<ComunicacaoDocumentoResult> pesquisarComunicacoes(
			String siglaProcesso, Long numeroProcesso,
			Long codigoFaseDocumento, Long codigoSetorAtual,
			Long codigoSetorFase, Long numeracaoUnica, Long anoNumeracaoUnica,
			String dataInicial, String dataFinal) throws ServiceLocalException, RegraDeNegocioException;

	ComunicacaoDocumentoPaginatorResult pesquisarDocumentos(FiltroPesquisarDocumentosAssinatura filtro) throws ServiceLocalException, RegraDeNegocioException;

	List<ComunicacaoDocumentoResult> pesquisarDocumentos(Date data, Setor setor) throws ServiceLocalException, RegraDeNegocioException;

	List<ComunicacaoDocumentoResult> pesquisarDocumentos(Long seqObjetoIncidente, Long codigoModelo, Setor setor, UsuarioAssinatura usuario)
			throws ServiceLocalException, RegraDeNegocioException;
	
	void atualizarSessao();

	List<ExpedienteAssinadoResult> pesquisarComunicacoesAssinadasPorPeriodo(Long codigoSetor, String usuario, String dataInicial, String dataFinal) throws ServiceLocalException,RegraDeNegocioException;

	List<ComunicacaoDocumentoResult> pesquisarDocumentosCorrecao(Setor setorUsuarioAutenticado, UsuarioAssinatura usuario) throws ServiceLocalException, RegraDeNegocioException;

	  /**
     * Cria uma comunicação de intimacao com os dados informados, e salva no
     * banco de dados.
     *
     * @param dataEnvioComunicacao
     * @param usuarioCriador
     * @param usuarioDestinatario
     * @param setor
     * @param modeloComunicacaoEnum
     * @param idsObjetoIncidente
     * @param tipoFaseComunicacao
     * @param pecas
     * @param andamentos
     * @param numeroDocumento
     * @param descricaoComunicacao,
     * @param arquivoEletronico
     * @return
     * @throws ServiceException
     */
    Comunicacao criarComunicacaoIntimacao(Date dataEnvioComunicacao,
            String usuarioCriador,
            Setor setor,
            String usuarioDestinatario,
            ModeloComunicacaoEnum modeloComunicacaoEnum,
            Collection<Long> idsObjetoIncidente,
            TipoFaseComunicacao tipoFaseComunicacao,
            List<?> pecas,
            List<AndamentoProcesso> andamentos,
            Long numeroDocumento,
            String descricaoComunicacao,
            byte[] arquivoEletronico) throws ServiceException;

    List pesquisarDocumentosPorComunicacao(Setor usuario, Boolean buscaSomenteDocumentoFaseGerados) throws ServiceException;

    List pesquisarDocumentosAssinatura(Setor setor, List<Long> list, Long l) throws ServiceException;

	List<ComunicacaoDocumentoResult> pesquisarDocumentos(Long codigoFaseDocumento, Setor setor)	throws ServiceLocalException, RegraDeNegocioException;
	
    /**
     * Insere na tabela JUDICIARIO.FASE_COMUNICACAO.
     *
     * @param comunicacao
     * @throws ServiceException, RegraDeNegocioException
     * @throws br.gov.stf.framework.exception.RegraDeNegocioException
     */
    void inserirFaseComunicacao(Comunicacao comunicacao) throws ServiceException, RegraDeNegocioException;
	
    Long recuperarCodigoOrigemDestinatario(Long idComunicacao) throws ServiceException;
    
    /**
     * Cria uma comunicação de intimacao com os dados informados, e salva no
     * banco de dados.
     *
     * @param dataEnvioComunicacao
     * @param setor
     * @param usuarioCriador
     * @param codigoPessoaDestinatario
     * @param modeloComunicacaoEnum
     * @param idsObjetoIncidente
     * @param tipoFaseComunicacao
     * @param pecas
     * @param andamentos
     * @param numeroDocumento
     * @param descricaoComunicacao
     * @param arquivoEletronico
     * @param documentoAcordao
     * @return
     * @throws ServiceException
     */
    Comunicacao criarComunicacaoIntimacao(Date dataEnvioComunicacao,
            Setor setor,
            String usuarioCriador,
            Long codigoPessoaDestinatario,
            ModeloComunicacaoEnum modeloComunicacaoEnum,
            Collection<Long> idsObjetoIncidente,
            TipoFaseComunicacao tipoFaseComunicacao,
            List<?> pecas,
            List<AndamentoProcesso> andamentos,
            Long numeroDocumento,
            String descricaoComunicacao,
            byte[] arquivoEletronico,
            DocumentoEletronico documentoAcordao) throws ServiceException;

    Comunicacao criarComunicacaoIndevidoAutosDisp(Date dataEnvioComunicacao,
            Setor setor,
            String usuarioCriador,
            Long codigoPessoaDestinatario,
            ModeloComunicacaoEnum modeloComunicacaoEnum,
            Collection<Long> idsObjetoIncidente,
            TipoFaseComunicacao tipoFaseComunicacao,
            List<?> pecas,
            List<AndamentoProcesso> andamentos,
            Long numeroDocumento,
            String descricaoComunicacao,
            byte[] arquivoEletronico,
            DocumentoEletronico documentoAcordao) throws ServiceException;
    
    Comunicacao criarComunicacaoAutosDisponibilizados(Date dataEnvioComunicacao,
            Setor setor,
            String usuarioCriador,
            Long codigoPessoaDestinatario,
            ModeloComunicacaoEnum modeloComunicacaoEnum,
            Collection<Long> idsObjetoIncidente,
            TipoFaseComunicacao tipoFaseComunicacao,
            List<?> pecas,
            List<AndamentoProcesso> andamentos,
            Long numeroDocumento,
            String descricaoComunicacao,
            byte[] arquivoEletronico,
            DocumentoEletronico documentoAcordao) throws ServiceException;
    
    void associarAndamentoProcessoComunicacao(List<AndamentoProcesso> andamentos, Comunicacao comunicacao) throws ServiceException;
    
    ComunicacaoIncidente criarComunicacaoObjetoIncidente(Long idObjetoIncidente, Comunicacao comunicacao, FlagProcessoLote tipoVinculo) throws ServiceException;    

    /**
     * Lista as comunicações eletrônicas.
     *
     * @param idParte
     * @param tipoRecebimentoComunicacaoEnum
     * @param descricaoTipoComunicacao
     * @param descricaoModelo
     * @param periodoEnvioInicio
     * @param periodoEnvioFim
     * @param idProcesso
     * @return
     * @throws ServiceException
     */
    List<ComunicacaoExternaDTO> pesquisarComunicacaoExterna(String idParte,
            TipoRecebimentoComunicacaoEnum tipoRecebimentoComunicacaoEnum,
            String descricaoTipoComunicacao,
            String descricaoModelo,
            Date periodoEnvioInicio,
            Date periodoEnvioFim,
            Long idProcesso, Long idPreferemcia) throws ServiceException;

	List<ComunicacaoDocumentoResult> pesquisarComunicacoesSigilosos(String siglaProcesso, Long numeroProcesso,
			Long codigoFaseDocumento, Long codigoSetorAtual, Long codigoSetorFase, Long numeracaoUnica,
			Long anoNumeracaoUnica, String dataInicial, String dataFinal)
			throws ServiceLocalException, RegraDeNegocioException;


}
