package br.gov.stf.estf.assinatura.visao.jsf.beans.expediente.assinardocumentos;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;

import br.gov.stf.estf.assinatura.security.UsuarioAssinatura;
import br.gov.stf.estf.assinatura.service.ComunicacaoServiceLocal;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.jsf.beans.ComunicacaoDocumento;
import br.gov.stf.estf.assinatura.visao.jsf.beans.expediente.manterdocumentos.BeanManterDocumentos;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.documento.model.service.ComunicacaoService;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.entidade.documento.ComunicacaoIncidente;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronicoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.estf.ministro.model.service.MinistroService;
import br.gov.stf.estf.processostf.model.service.ProcessoService;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanAguardandoAssinatura extends AssinadorBaseBean {

    private static final long serialVersionUID = 1L;
    private static final Log LOG = LogFactory.getLog(BeanAguardandoAssinatura.class);

    // --------------------- VARIAVES DE SESSAO ------------------------
    private static final String KEY_LISTA_DOCUMENTOS = BeanManterDocumentos.class.getCanonicalName() + ".listaAguadAssinatura";
    private static final Object ITENSSETORESDESTINO = new Object();
    private static final Object COMDOC_TEMP_ROWDATA = new Object();
    private static final Object ENCAMINHANDO_RELATOR_DIVERSO_PROCESSO = new Object();

    // --------------------- VARIÁVEIS ---------------------------------
    private String anotacaoCancelamento;
    private Long idSetorDestino;
    private ComunicacaoDocumento comDocumentoTemporariaRowData;
    private List<SelectItem> itensSetoresDestino;
    private Boolean isEncaminhandoRelatorDiversoProcesso;
    private PecaProcessoEletronicoComunicacao valor;

    // --------------------- VARIAVEIS DA TABELA -----------------------
    private List<CheckableDataTableRowWrapper> listaAguadAssinatura;
    private org.richfaces.component.html.HtmlDataTable tabelaAguardAssinatura;

    // --------------------- SESSAO ------------------------------------
    public BeanAguardandoAssinatura() {
        restaurarSessao();
    }

    @SuppressWarnings("unchecked")
    private void restaurarSessao() {
        if (getAtributo(ITENSSETORESDESTINO) == null) {
            setAtributo(ITENSSETORESDESTINO, carregarComboSetoresDestino(true,false));
        }
        setItensSetoresDestino((List<SelectItem>) getAtributo(ITENSSETORESDESTINO));

        setListaAguadAssinatura((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_DOCUMENTOS));

        if (getAtributo(COMDOC_TEMP_ROWDATA) == null) {
            setAtributo(COMDOC_TEMP_ROWDATA, new ComunicacaoDocumento());
        }
        setComDocumentoTemporariaRowData((ComunicacaoDocumento) getAtributo(COMDOC_TEMP_ROWDATA));

        if (getAtributo(ENCAMINHANDO_RELATOR_DIVERSO_PROCESSO) == null) {
            setAtributo(ENCAMINHANDO_RELATOR_DIVERSO_PROCESSO, Boolean.FALSE);
        }

        setIsEncaminhandoRelatorDiversoProcesso((Boolean) getAtributo(ENCAMINHANDO_RELATOR_DIVERSO_PROCESSO));
    }

    public void atualizarSessao() {
        setAtributo(KEY_LISTA_DOCUMENTOS, listaAguadAssinatura);
        setAtributo(COMDOC_TEMP_ROWDATA, comDocumentoTemporariaRowData);
        setAtributo(ENCAMINHANDO_RELATOR_DIVERSO_PROCESSO, isEncaminhandoRelatorDiversoProcesso);
    }

    // ---------------------- ACTIONS ----------------------------------
    public void atualizaSessaoAction(ActionEvent evt) {
        atualizarSessao();
    }

    public void pesquisaDocumentosGeradosAction(ActionEvent evt) {
        pesquisarDocumentosAssinados();
        atualizarSessao();
    }

    public void cancelaPDFDocumentoAction(ActionEvent evt) {
        cancelarPDFDocumento();
        atualizarSessao();
    }

    public void encaminharDocumentosAction(ActionEvent evt) {
        encaminharDocumentos();
        atualizarSessao();
    }

    public void encaminharDocumentosParaRevisaoAction(ActionEvent evt) {
        encaminharDocumentosParaRevisao();
        atualizarSessao();
    }

    public void procurarSetorRelatorAction(ActionEvent evt) {
        procurarSetorRelator();
    }

    public void alterarSetorDestino(ActionEvent event) {
        alterarSetorDestino();
        atualizarSessao();
    }

    /**
     * Método responsável em armazenar a variavel modeloComunicacao para passar
     * ao ModalPanel Esté método está sendo criado pois o getRowData não está
     * passando o valor correto para o ModalPanel. Está parecendo algum bug na
     * requisição ajax.
     *
     * @param evt
     */
    public void recuperaLinhaParaModalPanel(ActionEvent evt) {
        comDocumentoTemporariaRowData = (ComunicacaoDocumento) ((CheckableDataTableRowWrapper) tabelaAguardAssinatura.getRowData()).getWrappedObject();
        atualizarSessao();
    }

    // ---------------------- METHODS ----------------------------------
    public void alterarSetorDestino() {
        SetorService setorService = getSetorService();

        try {
            if (idSetorDestino == null) {
                return;
            }

            boolean isGabinete = setorService.isSetorGabinete(setorService.recuperarPorId(idSetorDestino));

            if (isGabinete) {
                List<CheckableDataTableRowWrapper> selecionados = retornarItensCheckableSelecionados(listaAguadAssinatura);
                ComunicacaoDocumento doc = null;

                for (CheckableDataTableRowWrapper linha : selecionados) {
                    doc = (ComunicacaoDocumento) linha.getWrappedObject();

                    if (isGabineteDiferenteRelatorProcesso(doc, idSetorDestino)) {
                        setIsEncaminhandoRelatorDiversoProcesso(true);
                        return;
                    }
                }
            }
        } catch (ServiceException se) {
            se.printStackTrace();
            reportarErro("Erro ao verificar setor de destino.");
        }

        setIsEncaminhandoRelatorDiversoProcesso(false);
    }

    private boolean isGabineteDiferenteRelatorProcesso(ComunicacaoDocumento doc, Long codSetor) throws ServiceException {
    	MinistroService ministroService = getMinistroService();
    	Ministro ministro = ministroService.recuperarPorId(doc.getComunicacao().getObjetoIncidenteUnico().getRelatorIncidenteId());

    	return doc.isNecessariaAssinaturaMinistroComunicacao() && !codSetor.equals(ministro.getSetor().getId());
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public void pesquisarDocumentosAssinados() {

        List<ComunicacaoDocumentoResult> documentos = new LinkedList<ComunicacaoDocumentoResult>();
        setListaAguadAssinatura(null);

        try {
            ComunicacaoService comunicacaoService = getComunicacaoService();
            comunicacaoService.limparSessao();
            List<ComunicacaoDocumentoResult> documentosPDFGerados = getComunicacaoServiceLocal().pesquisarDocumentosPorComunicacao(getSetorUsuarioAutenticado(), true);
            inicializarDocumentos(documentosPDFGerados);
            documentos.addAll(documentosPDFGerados);

            List<ComunicacaoDocumentoResult> documentosRevisados = getComunicacaoServiceLocal().pesquisarDocumentos(TipoFaseComunicacao.REVISADO.getCodigoFase(), getSetorUsuarioAutenticado());
            inicializarDocumentos(documentosRevisados);
            documentos.addAll(documentosRevisados);

            setListaAguadAssinatura(getCheckableDocumentoList(documentos));

            if (CollectionUtils.isVazia(documentos)) {
                reportarAviso("Nenhum documento a encaminhar.");
                setListaAguadAssinatura(null);
            }
        } catch (Exception e) {
            reportarErro("Erro ao recuperar os documentos.");
            setListaAguadAssinatura(null);
            e.printStackTrace();
        }
    }

    private void inicializarDocumentos(List<ComunicacaoDocumentoResult> documentos) {

        for (ComunicacaoDocumentoResult comunicacaoDocumentoResult : documentos) {
            Hibernate.initialize(comunicacaoDocumentoResult.getComunicacao().getComunicacaoIncidente());
//            Hibernate.initialize(comunicacaoDocumentoResult.getComunicacao().getNomeMinistroRelator());
            Hibernate.initialize(comunicacaoDocumentoResult.getComunicacao().getModeloComunicacao().getTipoComunicacao());
            Hibernate.initialize(comunicacaoDocumentoResult.getComunicacao().getFaseAtual());
            Hibernate.initialize(comunicacaoDocumentoResult.getComunicacao().getObjetoIncidenteUnico());

            Hibernate.initialize(comunicacaoDocumentoResult.getComunicacao().getObjetoIncidenteUnico());
            for (ComunicacaoIncidente ci : comunicacaoDocumentoResult.getComunicacao().getComunicacaoIncidente()) {
                Hibernate.initialize(ci.getObjetoIncidente().getPrincipal());
            }

            ProcessoService processoService = getProcessoService();
            try {
				Ministro ministroRelator = processoService.pesquisarRelatorAtual(comunicacaoDocumentoResult.getComunicacao().getObjetoIncidenteUnico());
				if (ministroRelator != null)
					comunicacaoDocumentoResult.getComunicacao().setNomeMinistroRelatorAtual(ministroRelator.getNome());
			} catch (ServiceException e) {
				e.printStackTrace();
			}
            
            List<PecaProcessoEletronicoComunicacao> pecas = comunicacaoDocumentoResult.getListaPecasProcessoEletronicoComunicacao();
            if (pecas != null) {
                for (PecaProcessoEletronicoComunicacao peca : pecas) {
                    Hibernate.initialize(peca.getPecaProcessoEletronico().getTipoPecaProcesso());
                    Hibernate.initialize(peca.getPecaProcessoEletronico().getObjetoIncidente());
                    Hibernate.initialize(peca.getPecaProcessoEletronico().getDocumentos().get(0).getDocumentoEletronico().getId());
                }
            }
        }
    }

    public void cancelarPDFDocumento() {
        UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();
        ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();

        // Cancela o PDF e gera fase de correção
        try {

            DocumentoEletronico de = getDocumentoEletronicoService().recuperarPorId(comDocumentoTemporariaRowData.getDocumentoComunicacao().getDocumentoEletronico().getId());

            comunicacaoServiceLocal.cancelarPDF(comDocumentoTemporariaRowData.getDocumentoComunicacao(), de.getDescricaoStatusDocumento(), anotacaoCancelamento, usuarioAssinatura, true);
            reportarInformacao("PDF(s) cancelado(s) com sucesso!");
            anotacaoCancelamento = "";
            pesquisarDocumentosAssinados();
            getRefreshController().executarRefreshPagina();
        } catch (RegraDeNegocioException exception) {
            reportarAviso(exception);
        } catch (ServiceLocalException exception) {
            reportarErro("Erro ao cancelar o PDF do documento.", exception, LOG);
        } catch (ServiceException exception) {
            reportarErro("Erro ao cancelar o PDF do documento.", exception, LOG);
        }
    }

    public void procurarSetorRelator() {
        List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(listaAguadAssinatura);

        if (CollectionUtils.isVazia(selecionados)) {
            reportarAviso("É necessário selecionar pelo menos um documento.");
            return;
        }

        ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();
        Long idSetorDestino = null;

        try {
            idSetorDestino = comunicacaoServiceLocal.pesquisarSetorDestinoPadrao(selecionados);
        } catch (ServiceLocalException exception) {
            reportarErro("Erro ao selecionar setor de destino padrão.", exception, LOG);
        } catch (RegraDeNegocioException exception) {
            reportarAviso(exception);
        }

        setIdSetorDestino(idSetorDestino);
    }

    private void encaminharDocumentos() {
        List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(listaAguadAssinatura);

        if (validarEncaminhamento(selecionados)) {

            List<CheckableDataTableRowWrapper> selecionadosCheckable = retornarItensCheckableSelecionados(listaAguadAssinatura);
            ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();

            // Somente documentos com PDF Gerado podem ser encaminhados
            TipoFaseComunicacao[] tiposFasesPermitidos = {TipoFaseComunicacao.PDF_GERADO, TipoFaseComunicacao.REVISADO};

            try {
                final boolean permitirGabinete = true;
                final boolean incluirFaseNoDeslocamento = true;
                final boolean naoIncluirFaseDeslocamentoSeNaoForGabinete = false;

                comunicacaoServiceLocal.encaminharParaAssinaturaSetor(selecionados, idSetorDestino, permitirGabinete, incluirFaseNoDeslocamento, naoIncluirFaseDeslocamentoSeNaoForGabinete,
                        tiposFasesPermitidos);

                listaAguadAssinatura.removeAll(selecionadosCheckable);
                reportarInformacao("Documento(s) encaminhados(s) com sucesso!");
            } catch (ServiceLocalException exception) {
                reportarErro("Erro ao encaminhar o(s) documentos(s).", exception, LOG);
            } catch (RegraDeNegocioException exception) {
                reportarAviso(exception);
            }
        }
    }

    private void encaminharDocumentosParaRevisao() {

        List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(listaAguadAssinatura);

        if (validarEncaminhamento(selecionados)) {

            List<CheckableDataTableRowWrapper> selecionadosCheckable = retornarItensCheckableSelecionados(listaAguadAssinatura);
            ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();

            // Somente documentos com PDF Gerado podem ser encaminhados
            TipoFaseComunicacao[] tiposFasesPermitidos = {TipoFaseComunicacao.PDF_GERADO};

            try {
                final boolean permitirGabinete = true;
                final boolean incluirFaseNoDeslocamento = true;
                final boolean naoIncluirFaseDeslocamentoSeNaoForGabinete = false;

                comunicacaoServiceLocal.encaminharParaRevisaoSetor(selecionados, idSetorDestino, permitirGabinete, incluirFaseNoDeslocamento, naoIncluirFaseDeslocamentoSeNaoForGabinete,
                        tiposFasesPermitidos);

                listaAguadAssinatura.removeAll(selecionadosCheckable);
                reportarInformacao("Documento(s) encaminhados(s) com sucesso!");
            } catch (ServiceLocalException exception) {
                reportarErro("Erro ao encaminhar o(s) documentos(s).", exception, LOG);
            } catch (RegraDeNegocioException exception) {
                reportarAviso(exception);
            }
        }
    }

    private boolean validarEncaminhamento(List<ComunicacaoDocumento> selecionados) {

        boolean validado = true;

        if (CollectionUtils.isVazia(selecionados)) {

            reportarAviso("É necessário selecionar pelo menos um documento.");
            validado = false;

        } else if (idSetorDestino == null) {

            reportarAviso("É necessário selecionar um setor de destino");
            validado = false;
        }

        return validado;
    }

    public void marcarTodosTextos(ActionEvent evt) {
        marcarOuDesmarcarTodas(listaAguadAssinatura);
        setListaAguadAssinatura(listaAguadAssinatura);
    }

    public void atualizarMarcacao(ActionEvent evt) {
        setListaAguadAssinatura(listaAguadAssinatura);
    }

    public boolean isCheckBoxSelecionado() {
        List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(listaAguadAssinatura);
        if (CollectionUtils.isVazia(selecionados)) {
            return false;
        }
        return true;
    }

    public boolean getVerificarPerfilEncaminharRevisao() {
        return !isUsuarioRevisorTextos() && !isUsuarioGestorTextos();
    }

    public boolean getVerificarPerfilEncaminharAssinatura() {
        return !isUsuarioAssinaturaTextos() && !isUsuarioGestorTextos();
    }

    public String getHintBotaoEncaminharRevisao() {
        if (isUsuarioRevisorTextos()) {
            return "";
        } else {
            return "Usuário sem perfil para executar esta ação.";
        }
    }

    public String getHintBotaoEncaminharAssinatura() {
        if (isUsuarioAssinaturaTextos()) {
            return "";
        } else {
            return "Usuário sem perfil para executar esta ação.";
        }
    }

    /**
     * Método responsável em abrir o PDF das peças eletrônicas vinculadas ao
     * documento.
     */
    @Override
    public void report() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setHeader("Content-disposition", String.format("attachment; filename=\"%s.pdf\"", valor.getPecaProcessoEletronico().getTipoPecaProcesso().getDescricao()));
        response.setContentType("application/x-pdf");

        ByteArrayInputStream input = null;
        try {
            input = new ByteArrayInputStream(
                    getDocumentoEletronicoService().recuperarPorId(valor.getPecaProcessoEletronico().getDocumentos().get(0).getDocumentoEletronico().getId()).getArquivo());

            IOUtils.copy(input, response.getOutputStream());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(input);
        }

        facesContext.responseComplete();
    }

    // ---------------------- GET AND SETS -----------------------------
    public List<CheckableDataTableRowWrapper> getListaAguadAssinatura() {
        return listaAguadAssinatura;
    }

    public void setListaAguadAssinatura(List<CheckableDataTableRowWrapper> listaAguadAssinatura) {
        this.listaAguadAssinatura = listaAguadAssinatura;
    }

    public String getAnotacaoCancelamento() {
        return anotacaoCancelamento;
    }

    public void setAnotacaoCancelamento(String anotacaoCancelamento) {
        this.anotacaoCancelamento = anotacaoCancelamento;
    }

    public Long getIdSetorDestino() {
        return idSetorDestino;
    }

    public void setIdSetorDestino(Long idSetorDestino) {
        this.idSetorDestino = idSetorDestino;
    }

    public List<SelectItem> getItensSetoresDestino() {
        return itensSetoresDestino;
    }

    public void setItensSetoresDestino(List<SelectItem> itensSetoresDestino) {
        this.itensSetoresDestino = itensSetoresDestino;
    }

    public org.richfaces.component.html.HtmlDataTable getTabelaAguardAssinatura() {
        return tabelaAguardAssinatura;
    }

    public void setTabelaAguardAssinatura(org.richfaces.component.html.HtmlDataTable tabelaAguardAssinatura) {
        this.tabelaAguardAssinatura = tabelaAguardAssinatura;
    }

    public ComunicacaoDocumento getComDocumentoTemporariaRowData() {
        return comDocumentoTemporariaRowData;
    }

    public void setComDocumentoTemporariaRowData(ComunicacaoDocumento comDocumentoTemporariaRowData) {
        this.comDocumentoTemporariaRowData = comDocumentoTemporariaRowData;
    }

    public Boolean getIsEncaminhandoRelatorDiversoProcesso() {
        return isEncaminhandoRelatorDiversoProcesso;
    }

    public void setIsEncaminhandoRelatorDiversoProcesso(Boolean isEncaminhandoRelatorDiversoProcesso) {
        this.isEncaminhandoRelatorDiversoProcesso = isEncaminhandoRelatorDiversoProcesso;
    }

    @Override
    public PecaProcessoEletronicoComunicacao getValor() {
        return valor;
    }

    @Override
    public void setValor(PecaProcessoEletronicoComunicacao valor) {
        this.valor = valor;
    }
    
    public String getRecuperarTextoDocumento(){		
		return getRecuperarTextoDocumento(tabelaAguardAssinatura);
		
	}
}
