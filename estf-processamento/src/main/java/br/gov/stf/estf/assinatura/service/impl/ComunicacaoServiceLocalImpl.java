package br.gov.stf.estf.assinatura.service.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.stf.estf.assinatura.security.ExpedienteAssinadoResult;
import br.gov.stf.estf.assinatura.security.UsuarioAssinatura;
import br.gov.stf.estf.assinatura.service.ComunicacaoDocumentoBase;
import br.gov.stf.estf.assinatura.service.ComunicacaoServiceLocal;
import br.gov.stf.estf.assinatura.visao.jsf.beans.ComunicacaoDocumento;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.documento.model.service.ComunicacaoIncidenteService;
import br.gov.stf.estf.documento.model.service.ComunicacaoService;
import br.gov.stf.estf.documento.model.service.DeslocamentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoComunicacaoService;
import br.gov.stf.estf.documento.model.service.DocumentoEletronicoService;
import br.gov.stf.estf.documento.model.service.FaseComunicacaoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoComunicacaoService;
import br.gov.stf.estf.documento.model.service.PecaProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.impl.AssinaturaDigitalServiceImpl;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoPaginatorResult;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.documento.model.util.FiltroPesquisarDocumentosAssinatura;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente.FlagProcessoLote;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.FaseComunicacao;
import br.gov.stf.estf.entidade.documento.FaseComunicacao.FlagFaseAtual;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao.FlagGenericaModeloComunicacao;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronicoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.AndamentoProcesso;
import br.gov.stf.estf.entidade.processostf.AndamentoProcessoComunicacao;
import br.gov.stf.estf.entidade.processostf.IncidenteJulgamento;
import br.gov.stf.estf.entidade.processostf.ModeloComunicacaoEnum;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.entidade.processostf.RecursoProcesso;
import br.gov.stf.estf.entidade.processostf.TipoVinculoAndamento;
import br.gov.stf.estf.entidade.usuario.Pessoa;
import br.gov.stf.estf.intimacao.model.dataaccess.ComunicacaoLocalDao;
import br.gov.stf.estf.intimacao.model.service.AndamentoProcessoComunicacaoLocalService;
import br.gov.stf.estf.intimacao.model.service.ModeloComunicacaoLocalService;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.estf.intimacao.model.vo.TipoRecebimentoComunicacaoEnum;
import br.gov.stf.estf.intimacao.visao.dto.ComunicacaoExternaDTO;
import br.gov.stf.estf.intimacao.visao.dto.PecaDTO;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.estf.usuario.model.dataaccess.PessoaDao;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.dataaccess.DaoException;
import br.gov.stf.framework.model.service.GenericService;
import br.gov.stf.framework.model.service.ServiceException;
import br.jus.stf.assinadorweb.api.requisicao.DocumentoPDF;

@Service("comunicacaoServiceLocal")
public class ComunicacaoServiceLocalImpl implements ComunicacaoServiceLocal {

    private static final Log LOG = LogFactory.getLog(ComunicacaoServiceLocalImpl.class);

    private static final String DSC_TIPO_PECA_INTEIRO_TEOR = "Inteiro teor do acórdão";

    @Autowired
    private ComunicacaoService comunicacaoService;
    @Autowired
    private ComunicacaoIncidenteService comunicacaoIncidenteService;
    @Autowired
    private DeslocamentoComunicacaoService deslocamentoComunicacaoService;
    @Autowired
    private DocumentoComunicacaoService documentoComunicacaoService;
    @Autowired
    private FaseComunicacaoService faseComunicacaoService;
    @Autowired
    private MinistroService ministroService;
    @Autowired
    private PecaProcessoEletronicoService pecaProcessoEletronicoService;
    @Autowired
    private SetorService setorService;
    @Autowired
    private DocumentoEletronicoService documentoEletronicoService;
    @Autowired
    private ModeloComunicacaoLocalService modeloComunicacaoServiceLocal;
    @Autowired
    private ObjetoIncidenteService objetoIncidenteService;
    @Autowired
    private PecaProcessoEletronicoComunicacaoService pecaProcessoEletronicoComunicacaoService;
    @Autowired
    private AndamentoProcessoComunicacaoLocalService andamentoProcessoComunicacaoLocalService;
	@Autowired
	private PessoaDao pessoaDao;
    @Autowired
    private ComunicacaoLocalDao comunicacaoLocalDao;

    @Override
    public List<DocumentoPDF<DocumentoComunicacao>> assinar(List<ComunicacaoDocumento> documentos) throws ServiceLocalException, RegraDeNegocioException {
        validarParaAssinatura(documentos);

        List<DocumentoPDF<DocumentoComunicacao>> arquivos = new ArrayList<DocumentoPDF<DocumentoComunicacao>>();

        for (ComunicacaoDocumento cd : documentos) {
            if (cd.getDocumentoComunicacao() != null && cd.getComunicacao() != null) {
            	try {
            		DocumentoEletronico documentoEletronico = cd.getDocumentoComunicacao().getDocumentoEletronico();
            		
            		if (documentoEletronico.getHashValidacao() == null || documentoEletronico.getHashValidacao().isEmpty())
            			documentoEletronico.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());
            		
					documentoEletronicoService.salvar(documentoEletronico);
					arquivos.add(new DocumentoPDF<DocumentoComunicacao>(AssinaturaDigitalServiceImpl.getRodapeAssinaturaDigital(documentoEletronico.getHashValidacao()), cd.getIdentificacaoComunicacao(), cd.getDocumentoComunicacao()));
				} catch (ServiceException e) {
					e.printStackTrace();
				}
            	
            }
        }

        return arquivos;
    }
    
    private void validarParaAssinaturaDB(List<? extends ComunicacaoDocumentoBase> documentos) throws RegraDeNegocioException {
        for (ComunicacaoDocumentoBase doc : documentos) {
            validarSituacaoDocumentoParaAssinaturaDB(doc);
            validarFasesParaAssinaturaDB(doc);
        }
    }
    

    private void validarParaAssinatura(List<ComunicacaoDocumento> documentos) throws RegraDeNegocioException {
        for (ComunicacaoDocumento doc : documentos) {
            validarSituacaoDocumentoParaAssinatura(doc);
            validarFasesParaAssinatura(doc);
        }
    }

    private void validarSituacaoDocumentoParaAssinaturaDB(ComunicacaoDocumentoBase doc) throws RegraDeNegocioException {
        if (!doc.isSituacaoDocumentoComunicacao(TipoSituacaoDocumento.GERADO)) {
            throw new RegraDeNegocioException(MessageFormat.format("O(s) documento(s) deve(m) estar liberado(s) para assinatura. Documento não permitido: {0}.", doc.getIdentificacaoComunicacao()));
        }
    }

    private void validarSituacaoDocumentoParaAssinatura(ComunicacaoDocumento doc) throws RegraDeNegocioException {
    	if (!doc.isSituacaoDocumentoComunicacao(TipoSituacaoDocumento.GERADO)) {
            throw new RegraDeNegocioException(MessageFormat.format(
                    "O(s) documento(s) deve(m) estar liberado(s) para assinatura. Documento não permitido: {0}.", doc.getIdentificacaoComunicacao()));
        }
    }

    private void validarFasesParaAssinaturaDB(ComunicacaoDocumentoBase doc) throws RegraDeNegocioException {
        TipoFaseComunicacao[] fasesPermitidas = {TipoFaseComunicacao.AGUARDANDO_ASSINATURA};

        if (!doc.isFaseAtualComunicacao(fasesPermitidas)) {
            throw new RegraDeNegocioException(MessageFormat.format("Somente documentos nas seguintes fases podem ser assinados: {0}. Documento não permitido: {1}.", toString(fasesPermitidas), doc.getIdentificacaoComunicacao()));
        }
    }
    
    private void validarFasesParaAssinatura(ComunicacaoDocumento doc) throws RegraDeNegocioException {
        TipoFaseComunicacao[] fasesPermitidas = {TipoFaseComunicacao.AGUARDANDO_ASSINATURA};

        if (!doc.isFaseAtualComunicacao(fasesPermitidas)) {
            throw new RegraDeNegocioException(MessageFormat.format(
                    "Somente documentos nas seguintes fases podem ser assinados: {0}. Documento não permitido: {1}.", toString(fasesPermitidas),
                    doc.getIdentificacaoComunicacao()));
        }
    }

    @Override
    public void cancelarAssinatura(ComunicacaoDocumento comunicacaoDocumento, String anotacaoCancelamento, UsuarioAssinatura usuario)
            throws ServiceLocalException, RegraDeNegocioException {
        Validate.notNull(usuario, "O usuário não pode ser nulo!");
        Validate.notNull(comunicacaoDocumento, "A comunicação não pode ser nula!");

        validarDocumentosParaCancelamentoAssinatura(comunicacaoDocumento);

        cancelarPDF(comunicacaoDocumento.getDocumentoComunicacao(), anotacaoCancelamento, usuario, false);

        Comunicacao comunicacao = comunicacaoDocumento.getComunicacao();

        incluirFase(TipoFaseComunicacao.ASSINATURA_CANCELADA, comunicacao, anotacaoCancelamento, usuario.getUsername());

        excluirPecasCasoProcessoLote(comunicacaoDocumento);

        incluirFase(TipoFaseComunicacao.CORRECAO, comunicacao, anotacaoCancelamento, null);

        deslocarParaSetorOrigem(comunicacao, null);
    }

    private void validarDocumentosParaCancelamentoAssinatura(ComunicacaoDocumento comunicacaoDocumento) throws RegraDeNegocioException {
        validarFasesParaCancelamentoAssinatura(comunicacaoDocumento);

        // TODO Solução temporária enquanto houverem casos em que uma comunicação assinada não possui referência ao andamento do processo
        validarAssociacaoComunicacaoAndamentoProcesso(comunicacaoDocumento);
    }

    private void validarAssociacaoComunicacaoAndamentoProcesso(ComunicacaoDocumento comunicacaoDocumento) throws RegraDeNegocioException {
        Comunicacao comunicacao = comunicacaoDocumento.getComunicacao();
        ComunicacaoIncidente comunicacaoIncidente = comunicacao.getComunicacaoIncidentePrincipal();
        AndamentoProcesso andamentoComunicacaoAssinada = comunicacaoIncidente.getAndamentoProcesso();

        if (andamentoComunicacaoAssinada == null) {
            throw new RegraDeNegocioException(
                    MessageFormat
                    .format("A assinatura não pode ser cancelada pois não há referência ao andamento processual que a gerou. Entrar em contato com a TI para realizar a normalização do dado, informando: SEQ_COMUNICACAO = {0}.",
                            comunicacao.getId()));
        }
    }

    private void validarFasesParaCancelamentoAssinatura(ComunicacaoDocumento comunicacaoDocumento) throws RegraDeNegocioException {
        TipoFaseComunicacao[] fasesPermitidas = {TipoFaseComunicacao.ASSINADO, TipoFaseComunicacao.AGUARDANDO_ENCAMINHAMENTO_ESTFDECISAO};

        if (!comunicacaoDocumento.isFaseAtualComunicacao(fasesPermitidas)) {
            throw new RegraDeNegocioException(MessageFormat.format(
                    "Apenas documentos assinados podem ter a assinatura cancelada. Documento não permitido: {0}.",
                    comunicacaoDocumento.getIdentificacaoComunicacao()));
        }
    }

    private void incluirFase(TipoFaseComunicacao fase, Comunicacao comunicacao, String observacao, String usuario) throws ServiceLocalException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Incluindo fase " + fase + " na comunicação " + comunicacao + " com a observação " + observacao + ". Usuário: " + usuario);
        }

        try {
            faseComunicacaoService.incluirFase(fase, comunicacao, observacao, usuario);
        } catch (ServiceException exception) {
            throw new ServiceLocalException("Não foi possível gerar a fase de correção.", exception);
        }
    }

    private void excluirPecasCasoProcessoLote(ComunicacaoDocumento comunicacaoDocumento) throws ServiceLocalException {
        List<PecaProcessoEletronico> listaPecas = recuperarPecas(comunicacaoDocumento);

        if (CollectionUtils.isNotVazia(listaPecas)) {
            for (PecaProcessoEletronico peca : listaPecas) {
                try {
                    pecaProcessoEletronicoService.excluir(peca, true, false);
                } catch (ServiceException exception) {
                    throw new ServiceLocalException(MessageFormat.format("Erro ao excluir a peça: {0} (ID: {1} ).", peca.getDescricaoPeca(), peca.getId()),
                            exception);
                }
            }
        }
    }

    private List<PecaProcessoEletronico> recuperarPecas(ComunicacaoDocumento comunicacaoDocumento) throws ServiceLocalException {
        List<PecaProcessoEletronico> listaPecas;

        try {
            listaPecas = pecaProcessoEletronicoService.recuperarListaPecasComunicacao(comunicacaoDocumento.getDocumentoComunicacao());
        } catch (ServiceException exception) {
            throw new ServiceLocalException("Erro ao recuperar a lista de peças da comunicação.", exception);
        }

        return listaPecas;
    }

    private void deslocarParaSetorOrigem(Comunicacao comunicacao, TipoFaseComunicacao faseComunicacao) throws ServiceLocalException {
        try {
            deslocamentoComunicacaoService.incluirDeslocamento(comunicacao, comunicacao.getSetor().getId(), faseComunicacao);
            
        } catch (ServiceException exception) {
            throw new ServiceLocalException("Erro ao deslocar o documento para o setor de origem.", exception);
        }
    }

    @Override
    public void cancelarPDF(DocumentoComunicacao documentoComunicacao, String observacao, UsuarioAssinatura usuario, boolean gerarFaseCorrecao)
            throws ServiceLocalException, RegraDeNegocioException {
        Validate.notNull(usuario, "O usuário não pode ser nulo!");

        try {
            if (gerarFaseCorrecao) {
                incluirFase(TipoFaseComunicacao.CORRECAO, documentoComunicacao.getComunicacao(), observacao, null);
            }

            if (documentoComunicacao.getDocumentoEletronico().getDescricaoStatusDocumento().compareToIgnoreCase("CAN") != 0) {
                documentoComunicacaoService.cancelarDocumentos(documentoComunicacao, observacao, usuario.getUsername());
            }
        } catch (Exception exception) {
            throw new ServiceLocalException("Não foi possível cancelar o PDF do documento.", exception);
        }
    }

    @Override
    public void cancelarPDF(DocumentoComunicacao documentoComunicacao, String descricaoStatusDocumento, String observacao, UsuarioAssinatura usuario, boolean gerarFaseCorrecao)
            throws ServiceLocalException, RegraDeNegocioException {
        Validate.notNull(usuario, "O usuário não pode ser nulo!");

        try {
            if (gerarFaseCorrecao) {
                incluirFase(TipoFaseComunicacao.CORRECAO, documentoComunicacao.getComunicacao(), observacao, null);
            }

            if (descricaoStatusDocumento.compareToIgnoreCase("CAN") != 0) {
                documentoComunicacaoService.cancelarDocumentos(documentoComunicacao, observacao, usuario.getUsername());
            }
        } catch (Exception exception) {
            throw new ServiceLocalException("Não foi possível cancelar o PDF do documento.", exception);
        }
    }

    @Override
    public void devolverParaSetorOrigem(ComunicacaoDocumento comunicacaoDocumento, String anotacaoCancelamento, UsuarioAssinatura usuario)
            throws ServiceLocalException, RegraDeNegocioException {
        Validate.notNull(usuario, "O usuário não pode ser nulo!");
        Validate.notNull(comunicacaoDocumento, "A comunicação não pode ser nula!");

        validarDocumentoParaDevolucao(comunicacaoDocumento);

        cancelarPDF(comunicacaoDocumento.getDocumentoComunicacao(), anotacaoCancelamento, usuario, false);

        incluirFase(TipoFaseComunicacao.CORRECAO, comunicacaoDocumento.getComunicacao(), anotacaoCancelamento, null);

        deslocarParaSetorOrigem(comunicacaoDocumento.getComunicacao(), null);
    }

    private void validarDocumentoParaDevolucao(ComunicacaoDocumento comunicacaoDocumento) throws RegraDeNegocioException {
        if (comunicacaoDocumento.isFaseAtualComunicacao(TipoFaseComunicacao.ASSINADO)) {
            throw new RegraDeNegocioException("Documentos assinados não podem ser devolvidos para correção!");
        }
    }

    @Override
    public void encaminharParaAssinaturaSetor(List<ComunicacaoDocumento> comunicacaoDocumentos, Long idSetorDestino, boolean permitirGabinete,
            boolean incluirFaseNoDeslocamento, boolean naoIncluirFaseDeslocamentoSeNaoForGabinete, TipoFaseComunicacao... tiposFasesPermitidos)
            throws ServiceLocalException, RegraDeNegocioException {

        encaminharParaSetor(comunicacaoDocumentos, idSetorDestino, permitirGabinete, incluirFaseNoDeslocamento, naoIncluirFaseDeslocamentoSeNaoForGabinete,
                TipoFaseComunicacao.AGUARDANDO_ASSINATURA, tiposFasesPermitidos);

    }

    
    public void atribuirResponsavel(List<ComunicacaoDocumento> comunicacaoDocumentos, String idUsuarioResponsavel)
            throws ServiceLocalException, RegraDeNegocioException {

    	 
    	        try {
    	        	  for (ComunicacaoDocumento doc : comunicacaoDocumentos) {
    	        		  Comunicacao ndoc = comunicacaoService.recuperarPorId(doc.getComunicacao().getId());
    	        		  if(idUsuarioResponsavel == null) {
    	        			  ndoc.setDataAtribuicao(null);
    	        		  }else {
    	        			  ndoc.setDataAtribuicao(recuperarDataAtual());
    	        		  }
    	        		  ndoc.setUsuarioResponsavel(idUsuarioResponsavel);
    	        		  comunicacaoService.alterar(ndoc);
    	        	  }
    	                // deslocamentoComunicacaoService.incluirDeslocamento(doc.getComunicacao(), idSetorDestino, null);
    	             
    	            
    	        } catch (Exception e) {
    	            throw new ServiceLocalException(e);
    	        }
    	    
    }

    
    @Override
    public void encaminharParaRevisaoSetor(List<ComunicacaoDocumento> comunicacaoDocumentos, Long idSetorDestino, boolean permitirGabinete,
            boolean incluirFaseNoDeslocamento, boolean naoIncluirFaseDeslocamentoSeNaoForGabinete, TipoFaseComunicacao... tiposFasesPermitidos)
            throws ServiceLocalException, RegraDeNegocioException {

        encaminharParaSetor(comunicacaoDocumentos, idSetorDestino, permitirGabinete, incluirFaseNoDeslocamento, naoIncluirFaseDeslocamentoSeNaoForGabinete,
                TipoFaseComunicacao.EM_REVISAO, tiposFasesPermitidos);

    }

    private void encaminharParaSetor(List<ComunicacaoDocumento> comunicacaoDocumentos, Long idSetorDestino, boolean permitirGabinete,
            boolean incluirFaseNoDeslocamento, boolean naoIncluirFaseDeslocamentoSeNaoForGabinete, TipoFaseComunicacao novaFase, TipoFaseComunicacao... tiposFasesPermitidos)
            throws ServiceLocalException, RegraDeNegocioException {

        if (idSetorDestino == null) {
            throw new RegraDeNegocioException("É necessário informar o setor de destino!");
        }

        boolean isGabinete = isGabinete(idSetorDestino);
        if (LOG.isDebugEnabled()) {
            LOG.info("Encaminhando documentos. O setor de destino é um gabinete? " + isGabinete);
        }

        validarDocumentosParaEncaminhamento(comunicacaoDocumentos, permitirGabinete, isGabinete, tiposFasesPermitidos);

        if (LOG.isDebugEnabled()) {
            LOG.debug("incluirFaseNoDeslocamento = " + incluirFaseNoDeslocamento + ", naoIncluirFaseDeslocamentoSeNaoForGabinete = "
                    + naoIncluirFaseDeslocamentoSeNaoForGabinete);
        }

        for (ComunicacaoDocumento doc : comunicacaoDocumentos) {
            encaminharParaSetor(doc, idSetorDestino, isGabinete, incluirFaseNoDeslocamento, naoIncluirFaseDeslocamentoSeNaoForGabinete, novaFase);
        }
    }

    private void encaminharParaSetor(ComunicacaoDocumento doc, Long idSetorDestino, boolean isGabinete, boolean incluirFaseNoDeslocamento,
            boolean naoIncluirFaseDeslocamentoSeNaoForGabinete, TipoFaseComunicacao novaFase) throws ServiceLocalException {
        try {
            if ((incluirFaseNoDeslocamento && naoIncluirFaseDeslocamentoSeNaoForGabinete && !isGabinete) || !incluirFaseNoDeslocamento) {
                deslocamentoComunicacaoService.incluirDeslocamento(doc.getComunicacao(), idSetorDestino, null);
            } else {
                deslocamentoComunicacaoService.incluirDeslocamento(doc.getComunicacao(), idSetorDestino, novaFase);
            }
        } catch (ServiceException exception) {
            throw new ServiceLocalException(exception);
        }
    }

    
    private boolean isGabinete(Long idSetorDestino) throws ServiceLocalException {
        boolean isGabinete = false;

        try {
            isGabinete = setorService.isSetorGabinete(setorService.recuperarPorId(idSetorDestino));
        } catch (ServiceException exception) {
            throw new ServiceLocalException("Erro ao processar o setor atual.", exception);
        }

        return isGabinete;
    }

    private void validarDocumentosParaEncaminhamento(List<ComunicacaoDocumento> comunicacaoDocumentos, boolean permitirGabinete, boolean isGabinete,
            TipoFaseComunicacao... tiposFasesPermitidos) throws RegraDeNegocioException {
        for (ComunicacaoDocumento doc : comunicacaoDocumentos) {
            validarFaseComunicacaoParaEncaminhamento(doc, tiposFasesPermitidos);

            // Verifica se o documento está sendo encaminhado para um Gabinete e não necessita de assinatura do Ministro
            if (permitirGabinete && isGabinete && !doc.isNecessariaAssinaturaMinistroComunicacao()) {
                throw new RegraDeNegocioException(
                        "O(s) documento(s) selecionado(s) não necessita(m) de assinatura do Ministro, portanto não deve(m) ser encaminhado(s) para um Gabinete.");
            } else if (!permitirGabinete && isGabinete) {
                throw new RegraDeNegocioException("O(s) documento(s) selecionado(s) não deve(m) ser encaminhado(s) para Gabinetes.");
            }
        }
    }

    private void validarFaseComunicacaoParaEncaminhamento(ComunicacaoDocumento doc, TipoFaseComunicacao... tiposFasesPermitidos) throws RegraDeNegocioException {
        if (!doc.isFaseAtualComunicacao(tiposFasesPermitidos)) {
            throw new RegraDeNegocioException(MessageFormat.format(
                    "Somente documentos na(s) seguinte(s) fase(s) podem ser encaminhados: {0}. Documento não permitido: {1}.", toString(tiposFasesPermitidos),
                    doc.getIdentificacaoComunicacao()));
        }
    }

    @Override
    public Long pesquisarSetorDestinoPadrao(List<ComunicacaoDocumento> comunicacaoDocumentos) throws ServiceLocalException, RegraDeNegocioException {
        Long idSetorDestino;
        ObjetoIncidente<?> objetoIncidente = getObjetoIncidenteUnico(comunicacaoDocumentos);

        if (objetoIncidente != null && isNecessariaAssinaturaMinistro(comunicacaoDocumentos)) {
            Ministro ministro = recuperarMinistroRelator(objetoIncidente);
            Setor setor = ministro.getSetor();

            if (setor != null) {
                idSetorDestino = setor.getId();
            } else {
                idSetorDestino = Setor.CODIGO_GABINETE_SECRETARIA_JUDICIARIA;
            }
        } else {
            idSetorDestino = Setor.CODIGO_GABINETE_SECRETARIA_JUDICIARIA;
        }

        return idSetorDestino;
    }

    private ObjetoIncidente<?> getObjetoIncidenteUnico(List<ComunicacaoDocumento> selecionados) {
        return CollectionUtils.isNotVazia(selecionados) ? selecionados.get(0).getComunicacao().getObjetoIncidenteUnico() : null;
    }

    private boolean isNecessariaAssinaturaMinistro(List<ComunicacaoDocumento> selecionados) {
        boolean assinaturaNecessaria = false;

        for (ComunicacaoDocumento comunicacaoDocumento : selecionados) {
            if (comunicacaoDocumento.isNecessariaAssinaturaMinistroComunicacao()) {
                assinaturaNecessaria = true;
                break;
            }
        }

        return assinaturaNecessaria;
    }

    private Ministro recuperarMinistroRelator(ObjetoIncidente<?> objetoIncidente) throws ServiceLocalException {
        Ministro ministro = null;

        try {
            ministro = ministroService.recuperarMinistroRelator(objetoIncidente);
        } catch (ServiceException exception) {
            throw new ServiceLocalException(MessageFormat.format("Erro ao recuperar ministro relator do incidente: {0}.", objetoIncidente.getIdentificacao()),
                    exception);
        }

        return ministro;
    }

    @Override
    public void excluir(List<ComunicacaoDocumento> comunicacaoDocumentos) throws ServiceLocalException, RegraDeNegocioException {
        validarDocumentosParaExclusao(comunicacaoDocumentos);

        for (ComunicacaoDocumento documento : comunicacaoDocumentos) {
            Comunicacao comunicacao = documento.getComunicacao();

            if (LOG.isDebugEnabled()) {
                LOG.debug("Tentando excluir documento: " + comunicacao.getDscNomeDocumento() + " (ID: " + comunicacao.getId() + ", Fase: "
                        + comunicacao.getFaseAtual() + ")");
            }

            incluirFase(TipoFaseComunicacao.EXCLUIDO, comunicacao, null, null);
        }
    }

    private void validarDocumentosParaExclusao(List<ComunicacaoDocumento> comunicacaoDocumentos) throws RegraDeNegocioException {
        for (ComunicacaoDocumento comunicacaoDocumento : comunicacaoDocumentos) {
            TipoFaseComunicacao[] fasesPermitidas = {TipoFaseComunicacao.EM_ELABORACAO, TipoFaseComunicacao.CORRECAO};

            if (!comunicacaoDocumento.isFaseAtualComunicacao(fasesPermitidas)) {
                throw new RegraDeNegocioException(MessageFormat.format(
                        "Somente documentos nas seguintes fases podem ser excluídos: {0}. Documento não permitido: {1}.", toString(fasesPermitidas),
                        comunicacaoDocumento.getIdentificacaoComunicacao()));
            }
        }
    }

    private String toString(TipoFaseComunicacao[] tiposFasesPermitidos) {
        StringBuilder builder = new StringBuilder();

        for (TipoFaseComunicacao tipoFaseComunicacao : tiposFasesPermitidos) {
            builder.append("\"").append(tipoFaseComunicacao.getDescricao()).append("\"").append("; ");
        }

        // remove o último ponto-e-vírgula
        builder.replace(builder.length() - 2, builder.length(), "");

        return builder.toString();
    }

    @Override
    public void expedir(List<ComunicacaoDocumento> comunicacaoDocumentos, UsuarioAssinatura usuario) throws ServiceLocalException, RegraDeNegocioException {
        Validate.notNull(usuario, "O usuário não pode ser nulo!");

        validarDocumentosParaExpedir(comunicacaoDocumentos);

        for (ComunicacaoDocumento doc : comunicacaoDocumentos) {
            incluirFase(TipoFaseComunicacao.EXPEDIDO, doc.getComunicacao(), null, usuario.getUsername().toUpperCase());
           
            
            boolean faseRestrito = false;
            
            if(doc.getComunicacao().getModeloComunicacao().getFlagJuntadaPecaProc() !=null && doc.getComunicacao().getModeloComunicacao().getFlagJuntadaPecaProc().equals(FlagGenericaModeloComunicacao.S)) {
            	faseRestrito = true;}
            
    		if(doc.getComunicacao().getFlagJuntadaPecaProc() != null && doc.getComunicacao().getFlagJuntadaPecaProc().equals(FlagGenericaModeloComunicacao.S) ){			
    			faseRestrito = true;}
            
			if(doc.getComunicacao().getFlagJuntadaPecaProc() != null && doc.getComunicacao().getFlagJuntadaPecaProc().equals(FlagGenericaModeloComunicacao.N) ){			
				faseRestrito = false;}
    		
    		if(faseRestrito) {
    			incluirFase(TipoFaseComunicacao.RESTRITOS, doc.getComunicacao(), null, usuario.getUsername().toUpperCase());
    			}
    		
            
           /*          
            if(doc.getComunicacao().getModeloComunicacao().getFlagJuntadaPecaProc().equals(FlagGenericaModeloComunicacao.S)) {
            	incluirFase(TipoFaseComunicacao.RESTRITOS, doc.getComunicacao(), null, usuario.getUsername().toUpperCase());
            }*/
        }
    }

    private void validarDocumentosParaExpedir(List<ComunicacaoDocumento> comunicacaoDocumentos) throws RegraDeNegocioException {
        for (ComunicacaoDocumento doc : comunicacaoDocumentos) {
            if (!doc.isFaseAtualComunicacao(TipoFaseComunicacao.ASSINADO)) {
                throw new RegraDeNegocioException(MessageFormat.format("Somente documentos assinados podem ser expedidos. Documento não permitido: {0}.",
                        doc.getIdentificacaoComunicacao()));
            }
        }
    }

    @Override
    public void encaminharParaDJe(List<ComunicacaoDocumento> comunicacaoDocumentos, UsuarioAssinatura usuario) throws ServiceLocalException,
            RegraDeNegocioException {
        Validate.notNull(usuario, "O usuário não pode ser nulo!");

        validarDocumentosParaEncaminharParaDJe(comunicacaoDocumentos);

        for (ComunicacaoDocumento doc : comunicacaoDocumentos) {
            encaminharParaDJe(doc, usuario);
        }
    }

    private void validarDocumentosParaEncaminharParaDJe(List<ComunicacaoDocumento> comunicacaoDocumentos) throws RegraDeNegocioException {
        for (ComunicacaoDocumento doc : comunicacaoDocumentos) {
            if (!doc.podeSerEncaminhadoParaDJe()) {
                throw new RegraDeNegocioException(
                        "Um ou mais documentos não podem ser encaminhados pois o modelo não possui a marcação \"Encaminhar para DJe\".");
            }

            if (!doc.isFaseAtualComunicacao(TipoFaseComunicacao.ASSINADO)) {
                throw new RegraDeNegocioException(MessageFormat.format(
                        "Somente documentos assinados podem ser encaminhados ao DJe. Documento não permitido: {0}.", doc.getIdentificacaoComunicacao()));
            }
        }
    }

    private void encaminharParaDJe(ComunicacaoDocumento doc, UsuarioAssinatura usuario) throws ServiceLocalException {
        try {
            documentoComunicacaoService.encaminharParaDJe(doc.getComunicacao(), usuario.getUsername());
        } catch (ServiceException exception) {
            throw new ServiceLocalException(MessageFormat.format("Erro ao encaminhar documento para o DJe: {0}.", doc), exception);
        }
    }

    @Override
    public void finalizarRestritosJuntar (List<ComunicacaoDocumento> comunicacaoDocumentos, UsuarioAssinatura usuario) throws ServiceLocalException, ServiceException {
        Validate.notNull(usuario, "O usuário não pode ser nulo!");

        for (ComunicacaoDocumento doc : comunicacaoDocumentos) {

       //     PecaProcessoEletronico pecaComunicacao = pecaProcessoEletronicoService.recuperaPecaComunicacao(doc.getDocumentoComunicacao().getDocumentoEletronico().getId(), doc.getComunicacao().getComunicacaoIncidentePrincipal().getObjetoIncidente().getId());
            deslocarParaSetorOrigem(doc.getComunicacao(), TipoFaseComunicacao.FINALIZADO);
        }
    }

    @Override
    public void finalizarRestritosNaoJuntar(List<ComunicacaoDocumento> comunicacaoDocumentos, UsuarioAssinatura usuario) throws ServiceLocalException {
        Validate.notNull(usuario, "O usuário não pode ser nulo!");

        for (ComunicacaoDocumento doc : comunicacaoDocumentos) {
            deslocarParaSetorOrigem(doc.getComunicacao(), TipoFaseComunicacao.REVISADO);
        }
    }
    
    @Override
    public void finalizarRestritosApenasJuntar(List<ComunicacaoDocumento> comunicacaoDocumentos, UsuarioAssinatura usuario) throws ServiceLocalException {
        Validate.notNull(usuario, "O usuário não pode ser nulo!");

        for (ComunicacaoDocumento doc : comunicacaoDocumentos) {
            deslocarParaSetorOrigem(doc.getComunicacao(), TipoFaseComunicacao.REVISADO);
        }
    }
    
    @Override
    public void finalizarRevisao(List<ComunicacaoDocumento> comunicacaoDocumentos, UsuarioAssinatura usuario) throws ServiceLocalException {
        Validate.notNull(usuario, "O usuário não pode ser nulo!");

        for (ComunicacaoDocumento doc : comunicacaoDocumentos) {
            deslocarParaSetorOrigem(doc.getComunicacao(), TipoFaseComunicacao.REVISADO);
        }
    }
    
    
    @Override
    public void finalizar(List<ComunicacaoDocumento> comunicacaoDocumentos, UsuarioAssinatura usuario) throws ServiceLocalException {
        Validate.notNull(usuario, "O usuário não pode ser nulo!");

        for (ComunicacaoDocumento doc : comunicacaoDocumentos) {
            finalizar(doc, usuario);
        }
    }

    private void finalizar(ComunicacaoDocumento doc, UsuarioAssinatura usuario) throws ServiceLocalException {
        Comunicacao comunicacao = doc.getComunicacao();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Incluindo fase FINALIZADO na Comunicação: " + comunicacao);
        }

        try {
            faseComunicacaoService.incluirFase(TipoFaseComunicacao.FINALIZADO, comunicacao, null, usuario.getUsername().toUpperCase());
        } catch (ServiceException exception) {
            throw new ServiceLocalException("Erro ao finalizar documento: " + comunicacao, exception);
        }
    }

    @Override
    public List<ComunicacaoDocumentoResult> pesquisarComunicacoes(String siglaProcesso, Long numeroProcesso, Long codigoFaseDocumento, Long codigoSetorAtual, Long codigoSetorFase,
            Long numeracaoUnica, Long anoNumeracaoUnica, String dataInicial, String dataFinal) throws ServiceLocalException, RegraDeNegocioException {

        limparSessao(comunicacaoService);

        List<ComunicacaoDocumentoResult> documentos;

        try {
            documentos = comunicacaoService.pesquisarDocumentosElaborados(siglaProcesso, numeroProcesso, codigoFaseDocumento, codigoSetorAtual, codigoSetorFase, numeracaoUnica,
                    anoNumeracaoUnica, dataInicial, dataFinal);
        } catch (ServiceException exception) {
            throw new ServiceLocalException(MessageFormat.format(
                    "Erro ao pesquisar documentos elaborados para o processo {0} {1} com a fase (cód.) {2} cujo setor atual é o (cód.) {3}.", siglaProcesso,
                    numeroProcesso, codigoFaseDocumento, codigoSetorAtual), exception);
        }

        return documentos;
    }

        @Override
    public List<ComunicacaoDocumentoResult> pesquisarComunicacoesSigilosos(String siglaProcesso, Long numeroProcesso, Long codigoFaseDocumento, Long codigoSetorAtual, Long codigoSetorFase,
            Long numeracaoUnica, Long anoNumeracaoUnica, String dataInicial, String dataFinal) throws ServiceLocalException, RegraDeNegocioException {

        limparSessao(comunicacaoService);

        List<ComunicacaoDocumentoResult> documentos;

        try {
            documentos = comunicacaoService.pesquisarDocumentosElaboradosSigilosos(siglaProcesso, numeroProcesso, codigoFaseDocumento, codigoSetorAtual, codigoSetorFase, numeracaoUnica,
                    anoNumeracaoUnica, dataInicial, dataFinal);
        } catch (ServiceException exception) {
            throw new ServiceLocalException(MessageFormat.format(
                    "Erro ao pesquisar documentos elaborados para o processo {0} {1} com a fase (cód.) {2} cujo setor atual é o (cód.) {3}.", siglaProcesso,
                    numeroProcesso, codigoFaseDocumento, codigoSetorAtual), exception);
        }

        return documentos;
    }

    
    
    @Override
    public List<ExpedienteAssinadoResult> pesquisarComunicacoesAssinadasPorPeriodo(Long codigoSetor, String usuario, String dataInicial, String dataFinal) throws ServiceLocalException, RegraDeNegocioException {

        limparSessao(comunicacaoService);

        List<ExpedienteAssinadoResult> documentos = Collections.emptyList();

        try {
            List result = comunicacaoService.pesquisarDocumentosAssinadosPorPeriodo(codigoSetor, usuario, dataInicial, dataFinal);

            if (result != null && result.size() > 0) {
                documentos = new ArrayList<ExpedienteAssinadoResult>();

                for (Object object : result) {
                    Object[] registro = (Object[]) object;

                    Long idDoDocumentoComunicacao = ((BigDecimal) registro[1]).longValue();

                    documentos.add(new ExpedienteAssinadoResult(
                            ((BigDecimal) registro[0]).longValue(),
                            ((BigDecimal) registro[2]).longValue(),
                            (String) registro[3],
                            (String) registro[4],
                            new SimpleDateFormat("dd/MM/yyyy").format(registro[5]),
                            (String) registro[6],
                            (String) registro[7],
                            new SimpleDateFormat("dd/MM/yyyy").format(registro[8]),
                            (String) registro[9],
                            documentoComunicacaoService.recuperarPorId(idDoDocumentoComunicacao)));
                }
            }
        } catch (ServiceException exception) {
            throw new ServiceLocalException("Erro ao pesquisar documentos assinados.", exception);
        }

        return documentos;
    }

    @Override
    public List<ComunicacaoDocumentoResult> pesquisarDocumentos(Long codigoFaseDocumento, Setor setor) throws ServiceLocalException, RegraDeNegocioException {
    	FiltroPesquisarDocumentosAssinatura filtro = new FiltroPesquisarDocumentosAssinatura();
    	filtro.setSetor(setor);
    	filtro.setFaseDocumento(codigoFaseDocumento);
    	ComunicacaoDocumentoPaginatorResult resultado = pesquisarDocumentos(filtro);
    	return resultado.getLista();
    }
    
    @Override
    public ComunicacaoDocumentoPaginatorResult pesquisarDocumentos(FiltroPesquisarDocumentosAssinatura filtro) throws ServiceLocalException, RegraDeNegocioException {
        List<Long> situacaoDoPdf = new LinkedList<Long>();

        if (filtro.getFaseDocumento() == TipoFaseComunicacao.AGUARDANDO_ASSINATURA.getCodigoFase()
                || filtro.getFaseDocumento() == TipoFaseComunicacao.PDF_GERADO.getCodigoFase()
                || filtro.getFaseDocumento() == TipoFaseComunicacao.EM_REVISAO.getCodigoFase()
                || filtro.getFaseDocumento() == TipoFaseComunicacao.REVISADO.getCodigoFase()) {

            // procurar os documentos que está com a situação de PDF gerado e prontos para assinar
            situacaoDoPdf.add(TipoSituacaoDocumento.GERADO.getCodigo());

        } else if (filtro.getFaseDocumento() == TipoFaseComunicacao.RESTRITOS.getCodigoFase()) {

            // buscar os documentos com PDF assinado e fase ASSINADO
            situacaoDoPdf.add(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE.getCodigo());
            situacaoDoPdf.add(TipoSituacaoDocumento.JUNTADO.getCodigo());
            
        } else if (filtro.getFaseDocumento() == TipoFaseComunicacao.ASSINADO.getCodigoFase()) {

            // buscar os documentos com PDF assinado e fase ASSINADO
            situacaoDoPdf.add(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE.getCodigo());
            situacaoDoPdf.add(TipoSituacaoDocumento.JUNTADO.getCodigo());

        } else if (filtro.getFaseDocumento() == TipoFaseComunicacao.AGUARDANDO_ASSINATURA_MINISTRO.getCodigoFase()) {

            // buscar os documentos onde o PDF pode estar assinado ou não.
            situacaoDoPdf.add(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE.getCodigo());
            situacaoDoPdf.add(TipoSituacaoDocumento.GERADO.getCodigo());

        } else if (filtro.getFaseDocumento() == TipoFaseComunicacao.AGUARDANDO_ENCAMINHAMENTO_ESTFDECISAO.getCodigoFase()) {

            // buscar os documentos com PDF assinado e fase Aguardando Encaminhamento para o eSTF-Decisão
            situacaoDoPdf.add(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE.getCodigo());
        }
        
        filtro.setListaTipoSituacaoDocumento(situacaoDoPdf);

        
        ComunicacaoDocumentoPaginatorResult resultado = efetuarPesquisarDocumentos(filtro);
        
        // [PROCESSAMENTO-3882]: Resolve um LazyInitializationException
        if (resultado != null && resultado.getLista() != null && !resultado.getLista().isEmpty()) {
        	for(ComunicacaoDocumentoResult result : resultado.getLista())
        		if (result.getComunicacao() != null) {
        			Hibernate.initialize(result.getComunicacao().getComunicacaoIncidente());
        			Hibernate.initialize(result.getComunicacao().getDeslocamentos());
        			Hibernate.initialize(result.getComunicacao().getFases());
        			Hibernate.initialize(result.getListaProcessoLoteVinculados()); // [PROCESSAMENTO-3926]: Resolve um LazyInitializationException
        		}
        }
        
        return resultado;
    }

    private ComunicacaoDocumentoPaginatorResult efetuarPesquisarDocumentos(FiltroPesquisarDocumentosAssinatura filtro) throws ServiceLocalException {
//        limparSessao(comunicacaoService);

        ComunicacaoDocumentoPaginatorResult result;

        try {        	
        	result = comunicacaoService.pesquisarDocumentosAssinatura(filtro);
            if(filtro.isCarregarFilhos()){
            	result.setLista(carregarFilhos(result.getLista()));
            }
        } catch (ServiceException exception) {
            throw new ServiceLocalException(MessageFormat.format("Erro ao pesquisar documentos com a fase {0}.",
                    TipoFaseComunicacao.valueOf(filtro.getFaseDocumento()).getDescricao()), exception);
        }

        return result;
    }

    private List<ComunicacaoDocumentoResult> carregarFilhos(List<ComunicacaoDocumentoResult> documentos) {
        for (ComunicacaoDocumentoResult documento : documentos) {
            Comunicacao comunicacao = documento.getComunicacao();
            for (ComunicacaoIncidente comunicacaoIncidente : comunicacao.getComunicacaoIncidente()) {
                comunicacaoIncidente.getComunicacao().getId();
                comunicacaoIncidente.getObjetoIncidente().getId();
                comunicacaoIncidente.getObjetoIncidente().getPrincipal().getId();
            }            
            if (documento.getListaPecasProcessoEletronicoComunicacao() != null) {
                for (PecaProcessoEletronicoComunicacao pecaProcessoEletronicoComunicacao : documento.getListaPecasProcessoEletronicoComunicacao()) {
                    if (pecaProcessoEletronicoComunicacao.getPecaProcessoEletronico() != null) {
                        pecaProcessoEletronicoComunicacao.getPecaProcessoEletronico().getObjetoIncidente().getId();
                        ObjetoIncidente objetoIncidente = pecaProcessoEletronicoComunicacao.getPecaProcessoEletronico().getObjetoIncidente();
                        objetoIncidente.getIdentificacao();
                    }
                }
            }
        }
        return documentos;
    }

    @Override
    public List<ComunicacaoDocumentoResult> pesquisarDocumentos(Date data, Setor setor) throws ServiceLocalException, RegraDeNegocioException {
        validarDataParaPesquisa(data);

        limparSessao(comunicacaoService);

        List<ComunicacaoDocumentoResult> documentos = Collections.emptyList();

        try {
            documentos = comunicacaoService.pesquisarDocumentosUnidade(data, setor.getId());
        } catch (ServiceException exception) {
            throw new ServiceLocalException(exception);
        }

        return documentos;
    }

    private void validarDataParaPesquisa(Date data) throws RegraDeNegocioException {
        if (data == null) {
            throw new RegraDeNegocioException("É necessário informar uma data!");
        }

        Date dataAtual = recuperarDataAtual();
        if (data.after(dataAtual)) {
            throw new RegraDeNegocioException("Não é possível pesquisar por data futura!");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ComunicacaoDocumentoResult> pesquisarDocumentos(Long seqObjetoIncidente, Long codigoModelo, Setor setor, UsuarioAssinatura usuario)
            throws ServiceLocalException, RegraDeNegocioException {
        limparSessao(comunicacaoService);

        List<ComunicacaoDocumentoResult> documentos;

        try {
            String username = usuario != null ? usuario.getUsername().toUpperCase() : null;
            documentos = comunicacaoService.pesquisarDocumentos(seqObjetoIncidente, codigoModelo, setor, username, false);
        } catch (ServiceException exception) {
            throw new ServiceLocalException(MessageFormat.format(
                    "Erro ao pesquisar documentos do incidente (cód.) {0} com modelo (cód.) {1} localizados no setor {2} e criados pelo usuário {3}.",
                    seqObjetoIncidente, codigoModelo, setor, usuario), exception);
        }

        return documentos;
    }

    private void limparSessao(GenericService<?, ?, ?> service) throws ServiceLocalException {
        try {
            service.limparSessao();
        } catch (ServiceException exception) {
            throw new ServiceLocalException("Erro ao limpar o cache da sessão.", exception);
        }
    }

    private Date recuperarDataAtual() {
        Date data = null;

        try {
            data = comunicacaoService.recuperarDataAtual();
        } catch (ServiceException exception) {
            LOG.info("Erro ao recuperar data atual do banco de dados.", exception);
        }

        // se não conseguir recuperar a data pelo banco, recuperar a data local
        if (data == null) {
            data = new Date();
        }

        return data;
    }

    @Override
    public void atualizarSessao() {
        recuperarDataAtual();
    }

    @Override
    public List<ComunicacaoDocumentoResult> pesquisarDocumentosCorrecao(Setor setorUsuarioAutenticado, UsuarioAssinatura usuario)
            throws ServiceLocalException, RegraDeNegocioException {
        limparSessao(comunicacaoService);

        List<ComunicacaoDocumentoResult> documentos = Collections.emptyList();

        try {
            String username = usuario != null ? usuario.getUsername().toUpperCase() : null;
            documentos = comunicacaoService.pesquisarDocumentosCorrecao(setorUsuarioAutenticado, username);
        } catch (ServiceException exception) {
            throw new ServiceLocalException("Erro ao pesquisar documentos.", exception);
        }

        return documentos;
    }

    @Override
    public Comunicacao criarComunicacaoIntimacao(Date dataEnvioComunicacao,
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
            byte[] arquivoEletronico) throws ServiceException {
        Comunicacao comunicacao = new Comunicacao();

        ModeloComunicacao modeloComunicacao = modeloComunicacaoServiceLocal.buscar(modeloComunicacaoEnum);

        comunicacao.setDscNomeDocumento(descricaoComunicacao);
        comunicacao.setModeloComunicacao(modeloComunicacao);
        comunicacao.setDataEnvio(dataEnvioComunicacao);
        comunicacao.setSetor(setor);
        comunicacao.setUsuarioDestinatario(usuarioDestinatario);
        comunicacao.setUsuarioCriacao(usuarioCriador);
        comunicacao.setNumeroComunicacao(numeroDocumento);
        comunicacao = comunicacaoService.salvar(comunicacao);

        associarPecaProcessoEletronicoComunicacao(pecas, comunicacao);
        associarAndamentoProcessoComunicacao(andamentos, comunicacao);

        criarFaseComunicacao(dataEnvioComunicacao, tipoFaseComunicacao, comunicacao);

        FlagProcessoLote flagProcessoLote = FlagProcessoLote.P;
        for (Long idObjetoIncidente : idsObjetoIncidente) {
            criarComunicacaoObjetoIncidente(idObjetoIncidente, comunicacao, flagProcessoLote);
            flagProcessoLote = FlagProcessoLote.V;
        }

        if (arquivoEletronico != null) {
            criarDocumentoIntimacao(comunicacao, arquivoEletronico);
        }

        return comunicacao;
    }

    /**
     * Associa pecas processo eletrônico comunicação a comunicação
     *
     * @param pecas
     * @param comunicacao
     * @return
     * @throws ServiceException
     */
    private void associarPecaProcessoEletronicoComunicacao(List<?> pecas,
			Comunicacao comunicacao) throws ServiceException {

		List<PecaProcessoEletronico> listaPecas = null;
	
		if(!pecas.isEmpty()){
			Object peca = (Object)pecas.get(0);
			
			if (peca instanceof PecaDTO) {
				listaPecas = new ArrayList<PecaProcessoEletronico>();
				for (int i = 0; i < pecas.size(); i++) {
					
					PecaDTO pecaDto = (PecaDTO) pecas.get(i);
					PecaProcessoEletronico pecaProcessoEletronico = new PecaProcessoEletronico();
					pecaProcessoEletronico.setId(pecaDto.getId());
					pecaProcessoEletronico.setNumeroPagInicio(pecaDto.getNumeroPagInicio());
					pecaProcessoEletronico.setNumeroPagFim(pecaDto.getNumeroPagFim());
					pecaProcessoEletronico.setNumeroOrdemPeca(pecaDto.getNumeroOrdemPeca());
					pecaProcessoEletronico.setAndamentoProtocolo(pecaDto.getAndamentoProtocolo());
					pecaProcessoEletronico.setSetor(pecaDto.getSetor());
					pecaProcessoEletronico.setTipoOrigemPeca(pecaDto.getTipoOrigemPeca());
					pecaProcessoEletronico.setDescricaoPeca(pecaDto.getDescricaoPeca());
					pecaProcessoEletronico.setDocumentos(pecaDto.getDocumentos());
					pecaProcessoEletronico.setLembretes(pecaDto.getLembretes());
					pecaProcessoEletronico.setTipoSituacaoPeca(pecaDto.getTipoSituacaoPeca());
					pecaProcessoEletronico.setTipoPecaProcesso(pecaDto.getTipoPecaProcesso());
					pecaProcessoEletronico.setDocumentosEletronicos(pecaDto.getDocumentosEletronicos());
					pecaProcessoEletronico.setObjetoIncidente(pecaDto.getObjetoIncidente());
					pecaProcessoEletronico.setDataAlteracao(pecaDto.getDataAlteracao());
					pecaProcessoEletronico.setDataInclusao(pecaDto.getDataInclusao());
					
					listaPecas.add(pecaProcessoEletronico);
				}

			}else if (peca instanceof PecaProcessoEletronico){
				listaPecas = (List<PecaProcessoEletronico>) pecas;
			}
			
		}
		

		for (PecaProcessoEletronico pecaProcessoEletronico : listaPecas) {
			PecaProcessoEletronicoComunicacao pecaProcessoEletronicoComunicacao = new PecaProcessoEletronicoComunicacao();
			pecaProcessoEletronicoComunicacao
					.setPecaProcessoEletronico(pecaProcessoEletronico);
			pecaProcessoEletronicoComunicacao.setComunicacao(comunicacao);
			pecaProcessoEletronicoComunicacao.setDataVinculacao(pecaProcessoEletronico
							.getDataAlteracao()); // todo que data será
													// informada ?
			pecaProcessoEletronicoComunicacaoService
					.salvar(pecaProcessoEletronicoComunicacao);
		}
	}

    /**
     * Associa andamento processo comunicação a comunicação
     *
     * @param andamentos
     * @param comunicacao
     * @return
     * @throws ServiceException
     */
    public void associarAndamentoProcessoComunicacao(List<AndamentoProcesso> andamentos, Comunicacao comunicacao) throws ServiceException {
        for (AndamentoProcesso andamentoProcesso : andamentos) {
            AndamentoProcessoComunicacao andamentoProcessoComunicacao = new AndamentoProcessoComunicacao();
            andamentoProcessoComunicacao.setAndamentoProcesso(andamentoProcesso);
            andamentoProcessoComunicacao.setComunicacao(comunicacao);
            andamentoProcessoComunicacao.setTipoVinculoAndamento(TipoVinculoAndamento.RELACIONADO);
            andamentoProcessoComunicacaoLocalService.salvar(andamentoProcessoComunicacao);
        }
    }

    /**
     * Cria uma fase de comunicação e define como única fase d lista de fases da
     * comunicação informada.
     *
     * @param comunicacao
     * @param pdf
     * @param usuario
     * @return
     * @throws ServiceException
     */
    private void criarFaseComunicacao(Date dataEnvioComunicacao, TipoFaseComunicacao tipoFaseComunicacao, Comunicacao comunicacao) throws ServiceException {
        FaseComunicacao faseComunicacao = new FaseComunicacao();
        faseComunicacao.setComunicacao(comunicacao);
        faseComunicacao.setTipoFase(tipoFaseComunicacao);
        faseComunicacao.setDataLancamento(dataEnvioComunicacao);
        faseComunicacao.setFlagFaseAtual(FaseComunicacao.FlagFaseAtual.S);
        faseComunicacaoService.salvar(faseComunicacao);
        List<FaseComunicacao> fases = new ArrayList<FaseComunicacao>();
        fases.add(faseComunicacao);
        comunicacao.setFases(fases);
    }

    /**
     * Cria um objeto do tipo ComunicacaoIncidente para intimação e o associa à
     * comunicação informada.
     *
     * @param comunicacao
     * @param pdf
     * @param usuario
     * @return
     * @throws ServiceException
     */
    public ComunicacaoIncidente criarComunicacaoObjetoIncidente(Long idObjetoIncidente, Comunicacao comunicacao, FlagProcessoLote tipoVinculo) throws ServiceException {
		ObjetoIncidente objetoIncidente = objetoIncidenteService.recuperarPorId(idObjetoIncidente);
		List<AndamentoProcesso> andamentoProcessos;
		Processo processo;
		try{
			if(objetoIncidente instanceof IncidenteJulgamento){
				IncidenteJulgamento ij = (IncidenteJulgamento) objetoIncidente;
				processo = ij.getPrincipal();
			}else if(objetoIncidente instanceof RecursoProcesso){
				RecursoProcesso rp = (RecursoProcesso) objetoIncidente;
				processo = rp.getPrincipal();
			}else{			
				processo = (Processo) objetoIncidente;
			}
			andamentoProcessos =  andamentoProcessoComunicacaoLocalService.pesquisarAndamentoProcesso(processo.getSiglaClasseProcessual(), processo.getNumeroProcessual());
			ComunicacaoIncidente comunicacaoIncidente = new ComunicacaoIncidente();
			comunicacaoIncidente.setObjetoIncidente(objetoIncidente);
			comunicacaoIncidente.setTipoVinculo(tipoVinculo);
			comunicacaoIncidente.setComunicacao(comunicacao);
			comunicacaoIncidente.setAndamentoProcesso(andamentoProcessos.get(0));
			return comunicacaoIncidenteService.salvar(comunicacaoIncidente);
		}catch(Exception e){
			return null;
		}
    }

    /**
     * Cria um objeto do tipo DocumentoEletronico para intimação e o associa à
     * comunicação informada.
     *
     * @param comunicacao
     * @param pdf
     * @return
     * @throws ServiceException
     */
    private DocumentoComunicacao criarDocumentoIntimacao(Comunicacao comunicacao, byte[] pdf) throws ServiceException {
        DocumentoEletronico documentoEletronico = new DocumentoEletronico();
        documentoEletronico.setArquivo(pdf);
        documentoEletronico.setTipoArquivo(br.gov.stf.estf.entidade.documento.TipoArquivo.PDF);
        documentoEletronico.setTipoAcesso(DocumentoEletronico.TIPO_ACESSO_PUBLICO);
        documentoEletronico.setDescricaoStatusDocumento(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_AGUARDANDO);
        documentoEletronico.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());
        documentoEletronico.setSiglaSistema("PROCESSAMENTO");
        documentoEletronico = documentoEletronicoService.salvar(documentoEletronico);

        DocumentoComunicacao documentoComunicacao = new DocumentoComunicacao();
        documentoComunicacao.setComunicacao(comunicacao);
        documentoComunicacao.setDocumentoEletronico(documentoEletronico);
        documentoComunicacao.setTipoSituacaoDocumento(TipoSituacaoDocumento.GERADO);
        return documentoComunicacaoService.salvar(documentoComunicacao);
    }

    private DocumentoComunicacao criarDocumentoAutosDisponibilizados(Comunicacao comunicacao, byte[] pdf) throws ServiceException {
        DocumentoEletronico documentoEletronico = new DocumentoEletronico();
        documentoEletronico.setArquivo(pdf);
        documentoEletronico.setTipoArquivo(br.gov.stf.estf.entidade.documento.TipoArquivo.PDF);
        documentoEletronico.setTipoAcesso(DocumentoEletronico.TIPO_ACESSO_PUBLICO);
        documentoEletronico.setDescricaoStatusDocumento(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_AGUARDANDO);
        documentoEletronico.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());
        documentoEletronico.setSiglaSistema("PROCESSAMENTO");
        documentoEletronico = documentoEletronicoService.salvar(documentoEletronico);

        DocumentoComunicacao documentoComunicacao = new DocumentoComunicacao();
        documentoComunicacao.setComunicacao(comunicacao);
        documentoComunicacao.setDocumentoEletronico(documentoEletronico);
        documentoComunicacao.setTipoSituacaoDocumento(TipoSituacaoDocumento.GERADO);
        return documentoComunicacaoService.salvar(documentoComunicacao);
    }
    
    @Override
    public List pesquisarDocumentosPorComunicacao(Setor setor, Boolean buscaSomenteDocumentoFaseGerados) throws ServiceException {
        List lista = comunicacaoService.pesquisarDocumentos(null, null, setor, null, buscaSomenteDocumentoFaseGerados);
        carregarFilhos(lista);
        return lista;
    }

    @Override
    public List pesquisarDocumentosAssinatura(Setor setor, List<Long> tipoSituacaoDocumento, Long filtro) throws ServiceException {
        List lista = comunicacaoService.pesquisarDocumentosAssinatura(setor, tipoSituacaoDocumento, filtro);
        carregarFilhos(lista);
        return lista;
    }

	@Override
	public List<DocumentoPDF<DocumentoComunicacao>> assinarDB(
			List<? extends ComunicacaoDocumentoBase> documentos)
			throws ServiceLocalException, RegraDeNegocioException {
		validarParaAssinaturaDB(documentos);

        List<DocumentoPDF<DocumentoComunicacao>> arquivos = new ArrayList<DocumentoPDF<DocumentoComunicacao>>();

        for (ComunicacaoDocumentoBase cd : documentos) {
            if (cd.getDocumentoComunicacao() != null && cd.getComunicacao() != null) {
            	
            	try {
            		DocumentoEletronico documentoEletronico = cd.getDocumentoComunicacao().getDocumentoEletronico();
            		
            		if (documentoEletronico.getHashValidacao() == null || documentoEletronico.getHashValidacao().isEmpty())
            			documentoEletronico.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());
            		
					documentoEletronicoService.salvar(documentoEletronico);
					arquivos.add(new DocumentoPDF<DocumentoComunicacao>(AssinaturaDigitalServiceImpl.getRodapeAssinaturaDigital(documentoEletronico.getHashValidacao()), cd.getIdentificacaoComunicacao(), cd.getDocumentoComunicacao()));
				} catch (ServiceException e) {
					e.printStackTrace();
				}
            	
            }
        }

        return arquivos;
	}

	
	@Override
	public void inserirFaseComunicacao(Comunicacao comunicacao)
			throws ServiceException, RegraDeNegocioException {
        FaseComunicacao faseComunicacao = new FaseComunicacao();
        faseComunicacao.setTipoFase(TipoFaseComunicacao.EXCLUIDO);
        faseComunicacao.setFlagFaseAtual(FlagFaseAtual.S);
        faseComunicacao.setDataLancamento(new Date());
        faseComunicacao.setComunicacao(comunicacao);
        faseComunicacaoService.incluir(faseComunicacao);
	}

	@Override
	public Long recuperarCodigoOrigemDestinatario(Long idComunicacao)
			throws ServiceException {
        try {
            return comunicacaoLocalDao.recuperarCodigoOrigemDestinatario(idComunicacao);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
	}

	@Override
	public Comunicacao criarComunicacaoIntimacao(Date dataEnvioComunicacao,
			Setor setor, String usuarioCriador, Long codigoPessoaDestinatario,
			ModeloComunicacaoEnum modeloComunicacaoEnum,
			Collection<Long> idsObjetoIncidente,
			TipoFaseComunicacao tipoFaseComunicacao, List<?> pecas,
			List<AndamentoProcesso> andamentos, Long numeroDocumento,
			String descricaoComunicacao, byte[] arquivoEletronico,
			DocumentoEletronico documentoAcordao) throws ServiceException {
		Comunicacao comunicacao = new Comunicacao();

		ModeloComunicacao modeloComunicacao = modeloComunicacaoServiceLocal.buscar(modeloComunicacaoEnum);

		comunicacao.setDscNomeDocumento(descricaoComunicacao);
		comunicacao.setModeloComunicacao(modeloComunicacao);
		comunicacao.setDataEnvio(dataEnvioComunicacao);
		comunicacao.setSetor(setor);
		if (codigoPessoaDestinatario == null) {
			throw new ServiceException(
					"Um usuário ou uma pessoa deve ser informada como destinatário da comunicação.");
		}
		Pessoa pessoaDestinatario;
		try {
			pessoaDestinatario = pessoaDao
					.recuperarPorId(codigoPessoaDestinatario);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao buscar pessoa destinatário.",
					ex);
		}
		comunicacao.setPessoaDestinataria(pessoaDestinatario);
		comunicacao.setUsuarioCriacao(usuarioCriador);
		comunicacao.setNumeroComunicacao(numeroDocumento);
		comunicacao = comunicacaoService.salvar(comunicacao);

		associarPecaProcessoEletronicoComunicacao(pecas, comunicacao,
				documentoAcordao);

		criarFaseComunicacao(dataEnvioComunicacao, tipoFaseComunicacao, comunicacao);

		if (arquivoEletronico != null) {
			criarDocumentoIntimacao(comunicacao, arquivoEletronico);
		}

		return comunicacao;
	}
	
	@Override
	public Comunicacao criarComunicacaoIndevidoAutosDisp(Date dataEnvioComunicacao,
			Setor setor, String usuarioCriador, Long codigoPessoaDestinatario,
			ModeloComunicacaoEnum modeloComunicacaoEnum,
			Collection<Long> idsObjetoIncidente,
			TipoFaseComunicacao tipoFaseComunicacao, List<?> pecas,
			List<AndamentoProcesso> andamentos, Long numeroDocumento,
			String descricaoComunicacao, byte[] arquivoEletronico,
			DocumentoEletronico documentoAcordao) throws ServiceException {
		Comunicacao comunicacao = new Comunicacao();

		ModeloComunicacao modeloComunicacao = modeloComunicacaoServiceLocal.buscar(modeloComunicacaoEnum);

		comunicacao.setDscNomeDocumento(descricaoComunicacao);
		comunicacao.setModeloComunicacao(modeloComunicacao);
		comunicacao.setDataEnvio(dataEnvioComunicacao);
		comunicacao.setSetor(setor);
		if (codigoPessoaDestinatario == null) {
			throw new ServiceException(
					"Um usuário ou uma pessoa deve ser informada como destinatário da comunicação.");
		}
		Pessoa pessoaDestinatario;
		try {
			pessoaDestinatario = pessoaDao
					.recuperarPorId(codigoPessoaDestinatario);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao buscar pessoa destinatário.",
					ex);
		}
		comunicacao.setPessoaDestinataria(pessoaDestinatario);
		comunicacao.setUsuarioCriacao(usuarioCriador);
		comunicacao.setNumeroComunicacao(numeroDocumento);
		comunicacao = comunicacaoService.salvar(comunicacao);

		associarPecaProcessoEletronicoComunicacao(pecas, comunicacao,
				documentoAcordao);

		criarFaseComunicacao(dataEnvioComunicacao, tipoFaseComunicacao, comunicacao);

		if (arquivoEletronico != null) {
			criarDocumentoIntimacao(comunicacao, arquivoEletronico);
		}

		return comunicacao;
	}
	
	@Override
	public Comunicacao criarComunicacaoAutosDisponibilizados(Date dataEnvioComunicacao,
			Setor setor, String usuarioCriador, Long codigoPessoaDestinatario,
			ModeloComunicacaoEnum modeloComunicacaoEnum,
			Collection<Long> idsObjetoIncidente,
			TipoFaseComunicacao tipoFaseComunicacao, List<?> pecas,
			List<AndamentoProcesso> andamentos, Long numeroDocumento,
			String descricaoComunicacao, byte[] arquivoEletronico,
			DocumentoEletronico documentoAcordao) throws ServiceException {
		Comunicacao comunicacao = new Comunicacao();

		ModeloComunicacao modeloComunicacao = modeloComunicacaoServiceLocal.buscar(modeloComunicacaoEnum);

		comunicacao.setDscNomeDocumento(descricaoComunicacao);
		comunicacao.setModeloComunicacao(modeloComunicacao);
		comunicacao.setDataEnvio(dataEnvioComunicacao);
		comunicacao.setSetor(setor);
		if (codigoPessoaDestinatario == null) {
			throw new ServiceException(
					"Um usuário ou uma pessoa deve ser informada como destinatário da comunicação.");
		}
		Pessoa pessoaDestinatario;
		try {
			pessoaDestinatario = pessoaDao
					.recuperarPorId(codigoPessoaDestinatario);
		} catch (DaoException ex) {
			throw new ServiceException("Erro ao buscar pessoa destinatário.",
					ex);
		}
		comunicacao.setPessoaDestinataria(pessoaDestinatario);
		comunicacao.setUsuarioCriacao(usuarioCriador);
		comunicacao.setNumeroComunicacao(numeroDocumento);
		comunicacao = comunicacaoService.salvar(comunicacao);

		associarPecaProcessoEletronicoComunicacao(pecas, comunicacao,
				documentoAcordao);

		criarFaseComunicacao(dataEnvioComunicacao, tipoFaseComunicacao, comunicacao);

		if (arquivoEletronico != null) {
			criarDocumentoAutosDisponibilizados(comunicacao, arquivoEletronico);
		}

		return comunicacao;
	}
	
	
	/**
	 * Associa pecas processo eletrônico comunicação a comunicação
	 *
	 * @param pecas
	 * @param comunicacao
	 * @return
	 * @throws ServiceException
	 */
	private void associarPecaProcessoEletronicoComunicacao(List<?> pecas,
			Comunicacao comunicacao, DocumentoEletronico documentoAcordao)
			throws ServiceException {

		List<PecaProcessoEletronico> listaPecas = new ArrayList<PecaProcessoEletronico>();
		Set<Long> idsPecas = new HashSet<Long>();
		if (pecas != null && !pecas.isEmpty()) {
			Object peca = (Object) pecas.get(0);
			if (peca instanceof PecaDTO) {
				for (Object objetoPeca : pecas) {
					PecaDTO pecaDto = (PecaDTO) objetoPeca;
					if (!idsPecas.contains(pecaDto.getId())) {
						idsPecas.add(pecaDto.getId());
						PecaProcessoEletronico pecaProcessoEletronico = new PecaProcessoEletronico();
						pecaProcessoEletronico.setId(pecaDto.getId());
						pecaProcessoEletronico.setNumeroPagInicio(pecaDto
								.getNumeroPagInicio());
						pecaProcessoEletronico.setNumeroPagFim(pecaDto
								.getNumeroPagFim());
						pecaProcessoEletronico.setNumeroOrdemPeca(pecaDto
								.getNumeroOrdemPeca());
						pecaProcessoEletronico.setAndamentoProtocolo(pecaDto
								.getAndamentoProtocolo());
						pecaProcessoEletronico.setSetor(pecaDto.getSetor());
						pecaProcessoEletronico.setTipoOrigemPeca(pecaDto
								.getTipoOrigemPeca());
						pecaProcessoEletronico.setDescricaoPeca(pecaDto
								.getDescricaoPeca());
						pecaProcessoEletronico.setDocumentos(pecaDto
								.getDocumentos());
						pecaProcessoEletronico.setLembretes(pecaDto
								.getLembretes());
						pecaProcessoEletronico.setTipoSituacaoPeca(pecaDto
								.getTipoSituacaoPeca());
						pecaProcessoEletronico.setTipoPecaProcesso(pecaDto
								.getTipoPecaProcesso());
						pecaProcessoEletronico.setDocumentosEletronicos(pecaDto
								.getDocumentosEletronicos());
						pecaProcessoEletronico.setObjetoIncidente(pecaDto
								.getObjetoIncidente());
						pecaProcessoEletronico.setDataAlteracao(pecaDto
								.getDataAlteracao());
						pecaProcessoEletronico.setDataInclusao(pecaDto
								.getDataInclusao());

						listaPecas.add(pecaProcessoEletronico);
					}
				}
			} else if (peca instanceof PecaProcessoEletronico) {
				listaPecas = (List<PecaProcessoEletronico>) pecas;
			}
		}

		for (PecaProcessoEletronico pecaProcessoEletronico : listaPecas) {
			PecaProcessoEletronicoComunicacao pecaProcessoEletronicoComunicacao = new PecaProcessoEletronicoComunicacao();
			pecaProcessoEletronicoComunicacao.setPecaProcessoEletronico(pecaProcessoEletronico);

			if (pecaProcessoEletronico.getTipoPecaProcesso().getDescricao().equals(DSC_TIPO_PECA_INTEIRO_TEOR)
				&& documentoAcordao != null) {
				pecaProcessoEletronicoComunicacao.setDocumentoAcordao(documentoAcordao);
			}

			pecaProcessoEletronicoComunicacao.setComunicacao(comunicacao);
			
			if (pecaProcessoEletronico.getDataAlteracao() != null)
				pecaProcessoEletronicoComunicacao.setDataVinculacao(pecaProcessoEletronico.getDataAlteracao());
			else
				pecaProcessoEletronicoComunicacao.setDataVinculacao(new Date());


			pecaProcessoEletronicoComunicacaoService
					.salvar(pecaProcessoEletronicoComunicacao);
		}
	}

	@Override
	public List<ComunicacaoExternaDTO> pesquisarComunicacaoExterna(
			String idParte,
			TipoRecebimentoComunicacaoEnum tipoRecebimentoComunicacaoEnum,
			String descricaoTipoComunicacao, String descricaoModelo,
			Date periodoEnvioInicio, Date periodoEnvioFim, Long idProcesso,
			Long idPreferemcia) throws ServiceException {
        try {
            return comunicacaoLocalDao.buscar(idParte,
                    tipoRecebimentoComunicacaoEnum, descricaoTipoComunicacao,
                    descricaoModelo, periodoEnvioInicio, periodoEnvioFim,
                    idProcesso, idPreferemcia);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage());
        }
	}
	
}
