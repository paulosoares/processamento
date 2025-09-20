package br.gov.stf.estf.assinatura.visao.jsf.beans.expediente.assinardocumentos;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.component.html.HtmlDatascroller;
import org.richfaces.event.DataScrollerEvent;

import br.gov.stf.estf.assinatura.security.UsuarioAssinatura;
import br.gov.stf.estf.assinatura.service.ComunicacaoServiceLocal;
import br.gov.stf.estf.assinatura.stficp.RequisicaoAssinaturaDocumentoComunicacao;
import br.gov.stf.estf.assinatura.stfoffice.editor.RequisicaoAbrirDocumento;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.jsf.beans.ComunicacaoDocumento;
import br.gov.stf.estf.assinatura.visao.jsf.beans.assinatura.BeanAssinatura;
import br.gov.stf.estf.assinatura.visao.util.OrdenacaoUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoPaginatorResult;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.documento.model.util.FiltroPesquisarDocumentosAssinatura;
import br.gov.stf.estf.entidade.documento.Comunicacao;
import br.gov.stf.estf.entidade.documento.DocumentoComunicacao;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.core.KeepStateInHttpSession;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;
import br.gov.stf.stfoffice.editor.jnlp.AbrirDocumento.ArgumentosAbrirDocumento;
import br.gov.stf.stfoffice.web.requisicao.jnlp.RequisicaoJnlp;
import br.jus.stf.assinadorweb.api.requisicao.DocumentoPDF;
import br.jus.stf.assinadorweb.api.util.PageRefresher;

public class BeanAssinarDocumentos extends AssinadorBaseBean {

	private static final long serialVersionUID = -8059606167034592897L;
	private static final Log LOG = LogFactory.getLog(BeanAssinarDocumentos.class);

	// ######################### VARIAVEIS DE SESSAO #########################

	private static final String KEY_LISTA_DOCUMENTOS = BeanAssinarDocumentos.class.getCanonicalName() + ".listaDocumentos";
	private static final String KEY_LISTA_COM_DOC_RESULT = BeanAssinarDocumentos.class.getCanonicalName() + ".listaComDocRes";
	public static final Object PROCURA_PROCESSO_ASSINAR = BeanAssinatura.class.getCanonicalName() + ".procuraProcessoParaAssinar";
	public static final Object ITENS_FASE_DOCUMENTO = BeanAssinatura.class.getCanonicalName() + ".itensFaseDocumento";
	private static final Object ITENSSETORESDESTINO = new Object();
	private static final Object ITENSUSUARIOSSETOR = new Object();
	private static final Object COMDOC_TEMP_ROWDATA = new Object();
	
	public static final Object CODIGO_FASE_DOCUMENTO_SELECIONADO = new Object();
	public static final Object DATA_CRIACAO_DOCUMENTO_SELECIONADO = new Object();
	public static final Object ROWS = new Object();
	public static final Object TOTAL_REGISTROS = new Object();

	private Long idSetorDestino;
	private List<SelectItem> itensSetoresDestino;
	private List<SelectItem> itensUsuariosSetor;
	private Boolean procuraProcessoParaAssinar;
	private List<SelectItem> itensFaseDocumento;
	private Long codigoFaseDocumento;
	private Date dataCriacaoDocumento;
	private String anotacaoCancelamento;
	private ComunicacaoDocumento comDocumentoTemporariaRowData;
	private List<ComunicacaoDocumentoResult> listaComDocRes;

	@KeepStateInHttpSession
	private List<CheckableDataTableRowWrapper> listaDocumentos;

	private org.richfaces.component.html.HtmlDataTable tabelaDocumentos;
	
	private Integer rows = 15;
	@SuppressWarnings("unused")
	private int pagAtual = 0;
	private Integer totalRegistros;
	private Boolean buscarApenasSigilosos;
		
	
	@SuppressWarnings("unchecked")
	private void restaurarSessao() {
		restoreStateOfHttpSession();

		if (getAtributo(PROCURA_PROCESSO_ASSINAR) == null) {
			setAtributo(PROCURA_PROCESSO_ASSINAR, Boolean.TRUE);
		}
		setProcuraProcessoParaAssinar((Boolean) getAtributo(PROCURA_PROCESSO_ASSINAR));

		setListaDocumentos((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_DOCUMENTOS));

		if (getAtributo(KEY_LISTA_COM_DOC_RESULT) == null) {
			setAtributo(KEY_LISTA_COM_DOC_RESULT, new ArrayList<ComunicacaoDocumentoResult>());
		}
		setListaComDocRes((List<ComunicacaoDocumentoResult>) getAtributo(KEY_LISTA_COM_DOC_RESULT));

		if( getAtributo(CODIGO_FASE_DOCUMENTO_SELECIONADO) != null ){
			codigoFaseDocumento = (Long) getAtributo(CODIGO_FASE_DOCUMENTO_SELECIONADO);
			setAtributo(CODIGO_FASE_DOCUMENTO_SELECIONADO, codigoFaseDocumento);
		}

		
		if (getAtributo(ITENSSETORESDESTINO) == null || (codigoFaseDocumento !=null) ) {
			setAtributo(ITENSSETORESDESTINO, null);
			if( codigoFaseDocumento !=null && codigoFaseDocumento == 5L){
				setAtributo(ITENSSETORESDESTINO, carregarComboSetoresDestino(true,true));
			}else {
				setAtributo(ITENSSETORESDESTINO, carregarComboSetoresDestino(true,false));
			}
		}
		setItensSetoresDestino((List<SelectItem>) getAtributo(ITENSSETORESDESTINO));

		if (getAtributo(ITENSUSUARIOSSETOR) == null || (codigoFaseDocumento !=null) ) {
			setAtributo(ITENSUSUARIOSSETOR, null);
			if( codigoFaseDocumento !=null && codigoFaseDocumento == 5L){
				setAtributo(ITENSUSUARIOSSETOR, carregarComboUsuariosDoSetorEgab(idSetorSalaOficiais));
			}else {
				setAtributo(ITENSUSUARIOSSETOR, carregarComboUsuariosDoSetorEgab(idSetorSalaOficiais));
			}
		}
		setItensUsuariosSetor((List<SelectItem>) getAtributo(ITENSUSUARIOSSETOR));
		
		if (getAtributo(ITENS_FASE_DOCUMENTO) == null) {
			setAtributo(ITENS_FASE_DOCUMENTO, carregarComboFaseSituacaoDocumentoAssinatura());
		}
		setItensFaseDocumento((List<SelectItem>) getAtributo(ITENS_FASE_DOCUMENTO));

		if (getAtributo(COMDOC_TEMP_ROWDATA) == null) {
			setAtributo(COMDOC_TEMP_ROWDATA, new ComunicacaoDocumento());
		}
		setComDocumentoTemporariaRowData((ComunicacaoDocumento) getAtributo(COMDOC_TEMP_ROWDATA));
		
		if( getAtributo(DATA_CRIACAO_DOCUMENTO_SELECIONADO) != null ){
			dataCriacaoDocumento = (Date) getAtributo(DATA_CRIACAO_DOCUMENTO_SELECIONADO);
			setAtributo(DATA_CRIACAO_DOCUMENTO_SELECIONADO, dataCriacaoDocumento);
		}
				
		if(getAtributo(ROWS) != null){
			rows = (Integer) getAtributo(ROWS);
		}
		
		if(getAtributo(TOTAL_REGISTROS) != null){
			setTotalRegistros((Integer) getAtributo(TOTAL_REGISTROS));
		}
		atualizaSessao();

	}

	public void atualizaSessao() {
		applyStateInHttpSession();
		setAtributo(KEY_LISTA_DOCUMENTOS, listaDocumentos);
		setAtributo(ITENSSETORESDESTINO, itensSetoresDestino);
		setAtributo(ITENSUSUARIOSSETOR, itensUsuariosSetor);
		setAtributo(PROCURA_PROCESSO_ASSINAR, procuraProcessoParaAssinar);
		setAtributo(ITENS_FASE_DOCUMENTO, itensFaseDocumento);
		setAtributo(COMDOC_TEMP_ROWDATA, comDocumentoTemporariaRowData);
		setAtributo(KEY_LISTA_COM_DOC_RESULT, listaComDocRes);
		setAtributo(DATA_CRIACAO_DOCUMENTO_SELECIONADO, dataCriacaoDocumento);
		setAtributo(CODIGO_FASE_DOCUMENTO_SELECIONADO, codigoFaseDocumento);
	}

	// ######################### ACTION #########################

	public void pesquisarDocumentosAction(ActionEvent evt) {
		pesquisarDocumentos();
		atualizaSessao();
	}

	public void devolverDocumentosAction(ActionEvent evt) {
		devolverDocumentos();
		atualizaSessao();
	}

	public void cancelarAssinaturaDocumentoAction(ActionEvent evt) {
		cancelarAssinaturaDocumento();
		atualizaSessao();
	}

	public void encaminharDocumentosAction(ActionEvent evt) {
		encaminharDocumentos();
		atualizaSessao();
	}

	public void atribuirDocumentosAction(ActionEvent evt) {
		atribuirDocumentos();
		atualizaSessao();
	}
	
	public void encaminharParaDJeAction(ActionEvent event) {
		encaminharParaDJe();
		atualizaSessao();
	}
	
	public void imprimirDocumentosAction(ActionEvent event){
		imprimirDocumentos();
		atualizaSessao();
	}

	public void finalizarDocumentosAction(ActionEvent event) {
		finalizarDocumentos();
		atualizaSessao();
	}

	public void procurarSetorRelatorAction(ActionEvent evt) {
		procurarSetorRelator();
	}

	public void pesquisarAguardandoAssinaturaAction(ActionEvent evt) {
		pesquisarDocumentosAguardandoAssinatura();
		atualizaSessao();
	}

	public void atualizaSessaoAction(ActionEvent evt) {
		atualizaSessao();
	}

	/**
	 * Método responsável em armazenar a variavel modeloComunicacao para passar ao ModalPanel Esté método está sendo criado pois o getRowData não está passando
	 * o valor correto para o ModalPanel. Está parecendo algum bug na requisição Ajax.
	 */
	public void recuperaLinhaParaModalPanel(ActionEvent evt) {

		comDocumentoTemporariaRowData = (ComunicacaoDocumento) ((CheckableDataTableRowWrapper) tabelaDocumentos.getRowData()).getWrappedObject();
		atualizaSessao();
	}

	// ######################### METHODS #########################

	public void abrirPdf(ActionEvent e){
		//comDocumentoTemporariaRowData = (ComunicacaoDocumento) ((CheckableDataTableRowWrapper) tabelaDocumentos.getRowData()).getWrappedObject();
		recuperaLinhaParaModalPanel(null);
		FiltroPesquisarDocumentosAssinatura filtro = new FiltroPesquisarDocumentosAssinatura();
		List<Long> id = new ArrayList<Long>();
		id.add(comDocumentoTemporariaRowData.getComunicacao().getId());
		filtro.setIds(id);				
		filtro.setSetor(getSetorUsuarioAutenticado());
		filtro.setDataDocumento(dataCriacaoDocumento);
		filtro.setFaseDocumento(codigoFaseDocumento);
		filtro.setTela("assinarDocumentos");
		filtro.setApenasSigilosos( buscarApenasSigilosos );
		filtro.setCarregarFilhos(false);
		try{
			ComunicacaoDocumentoResult result = getComunicacaoServiceLocal().pesquisarDocumentos(filtro).getLista().get(0);
			comDocumentoTemporariaRowData = new ComunicacaoDocumento(result);
			String link = comDocumentoTemporariaRowData.getLinkPDF();
			link = link.replaceAll("../../", "/");
			HttpServletRequest httpRequest = (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
			StringBuffer requestURLStringBuffer = httpRequest.getRequestURL();
			int index = requestURLStringBuffer.indexOf("/", 8);
			String serverURL = requestURLStringBuffer.substring(0, index);
			String contextURL = serverURL + httpRequest.getContextPath();
	
			getFacesContext().getExternalContext().redirect(contextURL + link);
		}catch(ServiceException ee){
			reportarErro("Erro ao recuperar caminho do arquivo.");
		}catch(IOException ee){
			reportarErro("Erro ao recuperar caminho do arquivo.");
		}catch(RegraDeNegocioException ee){
			reportarErro("Erro ao recuperar caminho do arquivo.");
		} catch (ServiceLocalException e1) {
			reportarErro("Erro ao recuperar caminho do arquivo.");
		}
	}
	
	@SuppressWarnings("deprecation")
	public String editarDocumentos() {
		Comunicacao documento = comDocumentoTemporariaRowData.getComunicacao();
		try{
			documento = getComunicacaoService().recuperarPorId(documento.getId());
		}catch(ServiceException e){
			documento = comDocumentoTemporariaRowData.getComunicacao();
		}
		if (isFaseAssinado(documento)) {
			reportarAviso("Documentos assinados não podem ser editados!");
			return null;
		}

		UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();
		RequisicaoAbrirDocumento req = new RequisicaoAbrirDocumento();

		req.setArquivoEletronico(documento.getArquivoEletronico());
		req.setNomeDocumento(documento.getDscNomeDocumento());
		req.setComunicacao(documento);
		req.setUser(usuarioAssinatura.getUsername());
		req.setGerarPDF(true);

		// Verifica se o usuário tem perfil para alterar o texto
		if ((!isUsuarioEditorTextos()) && (!isUsuarioMaster())) {
			req.setReadOnly(true);
		} else {
			req.setReadOnly(false);
		}

		req.setTipoSalvar(ArgumentosAbrirDocumento.TIPO_ACAO_SALVAR_SERVIDOR);
		req.setDocumento(documento.getArquivoEletronico().getConteudo());

		setRequestValue(RequisicaoJnlp.REQUISICAO_JNLP, req);
	
		return "stfOfficeServlet";
	}

	private boolean isFaseAssinado(Comunicacao documento) {
		return documento.getFaseAtual().equalsIgnoreCase(TipoFaseComunicacao.ASSINADO.getDescricao());
	}

	public void cancelarAssinaturaDocumento() {
		UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();
		ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();

		try {
			comunicacaoServiceLocal.cancelarAssinatura(comDocumentoTemporariaRowData, anotacaoCancelamento, usuarioAssinatura);
			reportarInformacao("Assinatura cancelada com sucesso!");
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao cancelar a assinatura do documento.", exception, LOG);
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		}
	}

	// O usuário responsável pela assinatura devolve o documento para a unidade
	// que gerou.
	public void devolverDocumentos() {
		ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();
		UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();

		try {
			comunicacaoServiceLocal.devolverParaSetorOrigem(comDocumentoTemporariaRowData, anotacaoCancelamento, usuarioAssinatura);
			reportarInformacao("Documento devolvido para correção!");
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao devolver o documento para correção.", exception, LOG);
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		}
	}

	public void procurarSetorRelator() {
		List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(listaDocumentos);

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

	public void limparCampos() {
		setListaDocumentos(null);
		atualizaSessao();
	}

	private void encaminharDocumentos() {
		List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(listaDocumentos);

		if (CollectionUtils.isVazia(selecionados)) {
			reportarAviso("É necessário selecionar pelo menos um documento.");
		} else if (idSetorDestino == null) {
			reportarAviso("É necessário selecionar um setor de destino");
		} else {
			List<CheckableDataTableRowWrapper> selecionadosCheckable = retornarItensCheckableSelecionados(listaDocumentos);
			ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();

			// Somente documentos Assinados ou Aguardando Encaminhamento para o eSTF-Decisão podem ser encaminhados
			TipoFaseComunicacao[] tiposFasesPermitidos = { TipoFaseComunicacao.ASSINADO, TipoFaseComunicacao.AGUARDANDO_ENCAMINHAMENTO_ESTFDECISAO };

			try {
				final boolean permitirGabinete = true;
				final boolean incluirFaseNoDeslocamento = true;
				final boolean naoIncluirFaseDeslocamentoSeNaoForGabinete = true;

				comunicacaoServiceLocal.encaminharParaAssinaturaSetor(selecionados, idSetorDestino, permitirGabinete, incluirFaseNoDeslocamento,
						naoIncluirFaseDeslocamentoSeNaoForGabinete, tiposFasesPermitidos);

				listaDocumentos.removeAll(selecionadosCheckable);
				reportarInformacao("Documento(s) encaminhados(s) com sucesso!");
			} catch (ServiceLocalException exception) {
				reportarErro("Erro ao encaminhar o(s) documentos(s).", exception, LOG);
			} catch (RegraDeNegocioException exception) {
				reportarAviso(exception);
			}
		}
	}
	
	private void atribuirDocumentos() {
		List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(listaDocumentos);

		if (CollectionUtils.isVazia(selecionados)) {
			reportarAviso("É necessário selecionar pelo menos um documento.");
		//} else if (idUsuarioAtribuicao == null) {
			//reportarAviso("É necessário selecionar um responsável");
			return ;
		} else {
			ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();
			try {
				comunicacaoServiceLocal.atribuirResponsavel(selecionados, idUsuarioAtribuicao);
				reportarInformacao("Documento(s) atribuído(s) com sucesso!");
			} catch (ServiceLocalException exception) {
				reportarErro("Erro ao atribuir o(s) documentos(s).", exception, LOG);
			} catch (RegraDeNegocioException exception) {
				reportarAviso(exception);
			}
		}
	}
	
	private void imprimirDocumentos(){
		List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(listaDocumentos);		
		if (CollectionUtils.isVazia(selecionados)) {
			reportarAviso("É necessário selecionar pelo menos um documento.");
		} else {
			List<CheckableDataTableRowWrapper> selecionadosCheckable = retornarItensCheckableSelecionados(listaDocumentos);
			List<Long> idsDocumentos = new ArrayList<Long>();
			
			try{
				for(CheckableDataTableRowWrapper wrapper : selecionadosCheckable){
					URI uri = new URI(((ComunicacaoDocumento)wrapper.getWrappedObject()).getLinkPDF());
					String query = uri.getQuery();
					String idDoc  = (query.substring(0, query.indexOf('&')).split("="))[1];
					idsDocumentos.add(Long.parseLong(idDoc));					
				}
				byte[] conteudopdfs = getDocumentoEletronicoService().recuperarDocumentosPDF(idsDocumentos);
				setConteudoDocumentoDownload(conteudopdfs);
				downloadArquivoPdf();
			}catch(ServiceException e){
				reportarErro("Erro ao imprimir documentos");
			} catch (URISyntaxException e) {				// 
				reportarErro("Erro ao imprimir documentos");
			}
		}
	}

	private void encaminharParaDJe() {
		List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(listaDocumentos);

		if (CollectionUtils.isVazia(selecionados)) {
			reportarAviso("É necessário selecionar pelo menos um documento.");
		} else {
			ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();
			UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();
			List<CheckableDataTableRowWrapper> selecionadosCheckable = retornarItensCheckableSelecionados(listaDocumentos);

			try {
				comunicacaoServiceLocal.encaminharParaDJe(selecionados, usuarioAssinatura);
				listaDocumentos.removeAll(selecionadosCheckable);
				reportarAviso("Documento(s) encaminhado(s) para o DJe com sucesso!");
			} catch (ServiceLocalException exception) {
				reportarErro("Erro ao encaminhar o(s) documento(s) para o DJe.", exception, LOG);
			} catch (RegraDeNegocioException exception) {
				reportarAviso(exception);
			}
		}
	}

	private void finalizarDocumentos() {
		List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(listaDocumentos);

		if (CollectionUtils.isVazia(selecionados)) {
			reportarAviso("É necessário selecionar pelo menos um documento.");
		} else {
			ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();
			UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();
			List<CheckableDataTableRowWrapper> selecionadosCheckable = retornarItensCheckableSelecionados(listaDocumentos);

			try {
				comunicacaoServiceLocal.finalizar(selecionados, usuarioAssinatura);
				listaDocumentos.removeAll(selecionadosCheckable);
				reportarAviso("Documento(s) finalizado(s) com sucesso!");
			} catch (ServiceLocalException exception) {
				reportarErro("Erro ao finalizar o(s) documentos(s).", exception, LOG);
			} catch (RegraDeNegocioException exception) {
				reportarAviso(exception);
			}
		}
	}

	public void pesquisarDocumentos() {
		ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();

		List<ComunicacaoDocumentoResult> documentos = null;

		try {
			FiltroPesquisarDocumentosAssinatura filtro = new FiltroPesquisarDocumentosAssinatura();
			filtro.setSetor(getSetorUsuarioAutenticado());
			filtro.setDataDocumento(dataCriacaoDocumento);
			filtro.setFaseDocumento(codigoFaseDocumento);
			filtro.setTela("assinarDocumentos");
			filtro.setApenasSigilosos( buscarApenasSigilosos );
			filtro.setCarregarFilhos(false);
			
			ComunicacaoDocumentoPaginatorResult resultado = comunicacaoServiceLocal.pesquisarDocumentos(filtro);
			documentos = resultado.getLista();
			setTotalRegistros(resultado.getTotalResultSet());
			
			if (CollectionUtils.isVazia(documentos)) {
				reportarAviso("Nenhum documento encontrado.");
				setListaDocumentos(null);
			} else {
				setListaDocumentos(getCheckableDocumentoList(documentos));
			}
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao recuperar documentos.", exception, LOG);
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		}

		setListaComDocRes(documentos);
	}

	public void pesquisarDocumentosAguardandoAssinatura() {
		ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();

		List<ComunicacaoDocumentoResult> documentos = null;

		try {
			documentos = comunicacaoServiceLocal.pesquisarDocumentos(TipoFaseComunicacao.AGUARDANDO_ASSINATURA.getCodigoFase(), getSetorUsuarioAutenticado());

			if (CollectionUtils.isVazia(documentos)) {
				reportarAviso("Nenhum documento encontrado.");
				setListaDocumentos(null);
			} else {
				reportarInformacao(MessageFormat.format("Existe(m) {0} documento(s) com a situação selecionada.", documentos.size()));
				setListaDocumentos(getCheckableDocumentoList(documentos));
			}
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao recuperar documentos.", exception, LOG);
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		}

		setListaComDocRes(documentos);
	}

	public void marcarTodosTextos(ActionEvent evt) {
		marcarOuDesmarcarTodas(listaDocumentos);
		setListaDocumentos(listaDocumentos);
	}

	public void atualizarMarcacao(ActionEvent evt) {
		setListaDocumentos(listaDocumentos);
	}

	@SuppressWarnings("unchecked")
	public void ordenarListaPeloTipoModelo() {
		if (CollectionUtils.isVazia(listaComDocRes)) {
			return;
		}

		Collections.sort(listaComDocRes, new OrdenacaoUtils.ComparaStringsGenerica());
		setListaDocumentos(getCheckableDocumentoList(listaComDocRes));
		atualizaSessao();
	}

	/**
	 * Método responsável em assinar os documentos liberados para assinatura.
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public String assinarDocumentosSelecionados() throws ServiceException {
		List<ComunicacaoDocumento> listaDocumentoOriginal = retornarItensSelecionados(listaDocumentos);

		if (CollectionUtils.isVazia(listaDocumentoOriginal)) {
			reportarAviso("Selecione ao menos um documento para assinar.");
		} else {
			ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();
			List<DocumentoPDF<DocumentoComunicacao>> arquivos = null;

			try {
				arquivos = comunicacaoServiceLocal.assinar(listaDocumentoOriginal);
			} catch (ServiceLocalException exception) {
				reportarErro("Erro ao assinar o(s) documento(s) selecionado(s).", exception, LOG);
			} catch (RegraDeNegocioException exception) {
				reportarAviso(exception);
			}

			if (CollectionUtils.isNotVazia(arquivos)) {
				RequisicaoAssinaturaDocumentoComunicacao requisicao = new RequisicaoAssinaturaDocumentoComunicacao();
				requisicao.setDocumentos(arquivos);
				requisicao.setPageRefresher((PageRefresher) getRefreshController());

				setRequestValue(RequisicaoAssinaturaDocumentoComunicacao.REQUISICAO_ASSINADOR, requisicao);

				return "assinarServlet";
			}
		}

		atualizaSessao();
		return null;
	}
	
	public int getMaxPages(){
		int max = 0;
		
		if(listaDocumentos != null && !listaDocumentos.isEmpty()){
			max = (int) Math.ceil((double)listaDocumentos.size() / (double)getRows());
			max = max > 10 ? 10 : max;
		}
		
		return max;
	}
	
	public void onScrollerAction(ActionEvent actionEvent) {
		if(actionEvent.getComponent() instanceof HtmlDatascroller ){
			DataScrollerEvent eve=(DataScrollerEvent) actionEvent;
			if(eve.getNewScrolVal().equals("first")){
				pagAtual=0;
			}else if(eve.getNewScrolVal().equals("last")){
				pagAtual=(eve.getPage()-1)*getRows();
			}else if(eve.getNewScrolVal().equals("fastforward")){
				System.out.println(eve.getPage());
				pagAtual=(eve.getPage()-1)*getRows();
			}else if(eve.getNewScrolVal().equals("next")){
				pagAtual=(eve.getPage()-1)*getRows();
			}else if(eve.getNewScrolVal().equals("fastrewind")){
				pagAtual=(eve.getPage()*getRows())-getRows();
			}else if(eve.getNewScrolVal().equals("previous")){
				pagAtual=(eve.getPage()*getRows())-getRows();
			}else{
				pagAtual=(Integer.parseInt(eve.getNewScrolVal())*getRows())-getRows();
			}
		}
	}	
	
	public void pesquisarComPaginacao(DataScrollerEvent event){
		//pesquisarDocumentos();
	}

	// ######################### GETS AND SETs #########################

	public List<CheckableDataTableRowWrapper> getListaDocumentos() {
		return listaDocumentos;
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaDocumentos() {
		return tabelaDocumentos;
	}

	public void setTabelaDocumentos(org.richfaces.component.html.HtmlDataTable tabelaDocumentos) {
		this.tabelaDocumentos = tabelaDocumentos;
	}

	public void setListaDocumentos(List<CheckableDataTableRowWrapper> listaDocumentos) {
		setAtributo(KEY_LISTA_DOCUMENTOS, listaDocumentos);
		this.listaDocumentos = listaDocumentos;
	}

	public Long getIdSetorDestino() {
		return idSetorDestino;
	}

	public void setIdSetorDestino(Long idSetorDestino) {
		this.idSetorDestino = idSetorDestino;
	}
	
	public String getIdUsuarioAtribuicao() {
		return idUsuarioAtribuicao;
	}

	public void setIdUsuarioAtribuicao(String idUsuarioAtribuicao) {
		this.idUsuarioAtribuicao = idUsuarioAtribuicao;
	}
	public List<SelectItem> getItensSetoresDestino() {
		return itensSetoresDestino;
	}

	public void setItensSetoresDestino(List<SelectItem> itensSetoresDestino) {
		this.itensSetoresDestino = itensSetoresDestino;
	}

	public List<SelectItem> getItensUsuariosSetor() {
		return itensUsuariosSetor;
	}

	public void setItensUsuariosSetor(List<SelectItem> itensUsuariosSetor) {
		this.itensUsuariosSetor = itensUsuariosSetor;
	}
	
	public Boolean getProcuraProcessoParaAssinar() {
		return procuraProcessoParaAssinar;
	}

	public void setProcuraProcessoParaAssinar(Boolean procuraProcessoParaAssinar) {
		this.procuraProcessoParaAssinar = procuraProcessoParaAssinar;
	}

	public String getListaDocumentoSize() {
		return listaDocumentos.size() + "";
	}

	public List<SelectItem> getItensFaseDocumento() {
		return itensFaseDocumento;
	}

	public void setItensFaseDocumento(List<SelectItem> itensFaseDocumento) {
		this.itensFaseDocumento = itensFaseDocumento;
	}

	public Long getCodigoFaseDocumento() {
		return codigoFaseDocumento;
	}

	public void setCodigoFaseDocumento(Long codigoFaseDocumento) {
		this.codigoFaseDocumento = codigoFaseDocumento;
	}
	
	public Date getDataCriacaoDocumento() {
		return dataCriacaoDocumento;
	}
	
	public void setDataCriacaoDocumento(Date dataCriacaoDocumento) {
		this.dataCriacaoDocumento = dataCriacaoDocumento;
	}

	public String getAnotacaoCancelamento() {
		return anotacaoCancelamento;
	}

	public void setAnotacaoCancelamento(String anotacaoCancelamento) {
		this.anotacaoCancelamento = anotacaoCancelamento;
	}

	public ComunicacaoDocumento getComDocumentoTemporariaRowData() {
		return comDocumentoTemporariaRowData;
	}

	public void setComDocumentoTemporariaRowData(ComunicacaoDocumento comDocumentoTemporariaRowData) {
		this.comDocumentoTemporariaRowData = comDocumentoTemporariaRowData;
	}

	public List<ComunicacaoDocumentoResult> getListaComDocRes() {
		return listaComDocRes;
	}

	public void setListaComDocRes(List<ComunicacaoDocumentoResult> listaComDocRes) {
		this.listaComDocRes = listaComDocRes;
	}
	
	public Integer getRows() {
		if(rows == null){
			setRows(15);
		}
		return rows;
	}
	
	public void setRows(Integer rows) {
		setAtributo(ROWS, rows);
		this.rows = rows;
	}
	
	public Integer getTotalRegistros() {
		return totalRegistros;
	}
	
	public void setTotalRegistros(Integer totalRegistros) {
		setAtributo(TOTAL_REGISTROS,totalRegistros);
		this.totalRegistros = totalRegistros;
	}

	public Boolean getBuscarApenasSigilosos() {
		return buscarApenasSigilosos;
	}

	public void setBuscarApenasSigilosos(Boolean buscarApenasSigilosos) {
		this.buscarApenasSigilosos = buscarApenasSigilosos;
	}

	public String getRecuperarTextoDocumento(){		
		return getRecuperarTextoDocumento(tabelaDocumentos);
		
	}
	
	public Boolean getSelecionadosTela() {
		return retornarItensSelecionados(listaDocumentos).size() > 0;
	}

	public BeanAssinarDocumentos() {
		restaurarSessao();
	}	
	
}
