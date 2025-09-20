package br.gov.stf.estf.assinatura.visao.jsf.beans.expediente.expedirdocumentos;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.security.UsuarioAssinatura;
import br.gov.stf.estf.assinatura.service.ComunicacaoServiceLocal;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.jsf.beans.ComunicacaoDocumento;
import br.gov.stf.estf.assinatura.visao.jsf.beans.assinatura.BeanAssinatura;
import br.gov.stf.estf.assinatura.visao.util.OrdenacaoUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.documento.model.service.ComunicacaoService;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoPaginatorResult;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.documento.model.util.FiltroPesquisarDocumentosAssinatura;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.entidade.processostf.TipoConfidencialidade;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.core.KeepStateInHttpSession;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

import com.itextpdf.text.pdf.PdfReader;

public class BeanExpedirDocumentos extends AssinadorBaseBean {

	private static final long serialVersionUID = -8059606167034592897L;
	private static final Log LOG = LogFactory.getLog(BeanExpedirDocumentos.class);

	// ###################### VARIAVEIS DE SESSAO ##############################
	private static final String KEY_LISTA_DOCUMENTOS = BeanExpedirDocumentos.class.getCanonicalName() + ".listaDocumentos";
	public static final Object ITENS_FASE_DOCUMENTO = BeanAssinatura.class.getCanonicalName() + ".itensFaseDocumento";
	private static final Object COMDOC_TEMP_ROWDATA = new Object();
	private static final Object ITENSSETORESDESTINO = new Object();
	private static final Object ITENSUSUARIOSSETOR = new Object();
	
	public static final Object DATA_ASSINATURA_DOCUMENTO_SELECIONADO = new Object();
	public static final Object ROWS = new Object();
	public static final Object TOTAL_REGISTROS = new Object();

	private Long idSetorDestino;
	private List<SelectItem> itensSetoresDestino;
	private List<SelectItem> itensFaseDocumento;
	private Long codigoFaseDocumento;
	private String anotacaoCancelamento;
	private ComunicacaoDocumento comDocumentoTemporariaRowData;
	private List<ComunicacaoDocumento> listaSelecionados ;

	@KeepStateInHttpSession
	private List<CheckableDataTableRowWrapper> listaDocumentos;

	private HtmlDataTable tabelaDocumentos;
	private Date dataAssinaturaDocumento;
	private Integer rows;
	private int pagAtual = 0;
	private Integer totalRegistros;
	private Boolean buscarApenasSigilosos;
	private List<SelectItem> itensUsuariosSetor;
	private String idUsuarioAtribuicao;
	
	public BeanExpedirDocumentos() {
		restaurarSessao();
	}

	@SuppressWarnings("unchecked")
	private void restaurarSessao() {
		restoreStateOfHttpSession();

		setListaDocumentos((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_DOCUMENTOS));

		if (getAtributo(ITENS_FASE_DOCUMENTO) == null) {
			setAtributo(ITENS_FASE_DOCUMENTO, carregarComboFaseSituacaoDocumentoExpedicao());
		}
		setItensFaseDocumento((List<SelectItem>) getAtributo(ITENS_FASE_DOCUMENTO));

		if (getAtributo(ITENSSETORESDESTINO) == null) {
			setAtributo(ITENSSETORESDESTINO, carregarComboSetoresDestino(true,false));
		}
		setItensSetoresDestino((List<SelectItem>) getAtributo(ITENSSETORESDESTINO));

		if (getAtributo(COMDOC_TEMP_ROWDATA) == null) {
			setAtributo(COMDOC_TEMP_ROWDATA, new ComunicacaoDocumento());
		}
		setComDocumentoTemporariaRowData((ComunicacaoDocumento) getAtributo(COMDOC_TEMP_ROWDATA));
		
		if( getAtributo(DATA_ASSINATURA_DOCUMENTO_SELECIONADO) != null ){
			dataAssinaturaDocumento = (Date) getAtributo(DATA_ASSINATURA_DOCUMENTO_SELECIONADO);
			setAtributo(DATA_ASSINATURA_DOCUMENTO_SELECIONADO, dataAssinaturaDocumento);
		}
			
		if (getAtributo(ITENSUSUARIOSSETOR) == null || (codigoFaseDocumento !=null) ) {
			setAtributo(ITENSUSUARIOSSETOR, null);
			if( codigoFaseDocumento !=null && codigoFaseDocumento == 5L){
				setAtributo(ITENSUSUARIOSSETOR, carregarComboUsuariosDoSetorEgab(idSetorSalaOficiais));
			}else {
				setAtributo(ITENSUSUARIOSSETOR, carregarComboUsuariosDoSetorEgab(idSetorSalaOficiais));
			}
		}
		setItensUsuariosSetor((List<SelectItem>) getAtributo(ITENSUSUARIOSSETOR));
		
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
		setAtributo(ITENS_FASE_DOCUMENTO, itensFaseDocumento);
		setAtributo(COMDOC_TEMP_ROWDATA, comDocumentoTemporariaRowData);
		setAtributo(DATA_ASSINATURA_DOCUMENTO_SELECIONADO, dataAssinaturaDocumento);
	}

	// ###################### ACTION ######################

	public void encaminharDocumentosAction(ActionEvent evt) {
		encaminharDocumentos();
		atualizaSessao();
	}

	public void procurarSetorRelatorAction(ActionEvent evt) {
		procurarSetorRelator();
	}

	public void expedirDocumentosAction(ActionEvent evt) {
		expedirDocumentos();
		atualizaSessao();
	}
	
	public void imprimirDocumentosAction(ActionEvent evt){
		imprimirDocumentos();
		atualizaSessao();
	}

	public void finalizarDocumentosAction(ActionEvent event) {
		finalizarDocumentos();
		atualizaSessao();
	}

	public void pesquisarAssinadosAction(ActionEvent evt) {
		pesquisarDocumentosAssinados();
		atualizaSessao();
	}

	public void atualizaSessaoAction(ActionEvent evt) {
		atualizaSessao();
	}
	
	public void atribuirDocumentosAction(ActionEvent evt) {
		atribuirDocumentos();
		atualizaSessao();
	}
	// ###################### METHODS ######################

	
	private void atribuirDocumentos() {
		List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(listaDocumentos);

		if (CollectionUtils.isVazia(selecionados)) {
			reportarAviso("É necessário selecionar pelo menos um documento.");
		//} else if (idUsuarioAtribuicao == null) {
			//reportarAviso("É necessário selecionar um responsável");
			return;
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
	
	public void expedirDocumentos() {
		ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();
		UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();

		List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(listaDocumentos);

		if (CollectionUtils.isVazia(selecionados)) {
			reportarAviso("É necessário selecionar ao menos um documento.");
		} else {
			List<CheckableDataTableRowWrapper> selecionadosCheckable = retornarItensCheckableSelecionados(listaDocumentos);

			try {
				comunicacaoServiceLocal.expedir(selecionados, usuarioAssinatura);

				listaDocumentos.removeAll(selecionadosCheckable);
				reportarAviso("Documento(s) expedido(s) com sucesso!");
			} catch (RegraDeNegocioException exception) {
				reportarAviso(exception);
			} catch (ServiceLocalException exception) {
				reportarErro("Erro ao expedir o(s) documentos(s).", exception, LOG);
			}
		}
	}

	public void encaminharDocumentos() {
		ComunicacaoServiceLocal comunicacaoServiceLocal = getComunicacaoServiceLocal();
		List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(listaDocumentos);
		if(getSetorUsuarioAutenticado().getId().equals(idSetorDestino)) {
			reportarAviso("Não é possível encaminhar para o mesmo setor ("+getSetorUsuarioAutenticado().getId()+ " -> "+idSetorDestino+")");
			return;
		}
		if (CollectionUtils.isVazia(selecionados)) {
			reportarAviso("É necessário selecionar pelo menos um documento.");
			return;
		}

		final boolean permitirGabinete = false;
		final boolean incluirFaseNoDeslocamento = false;
		TipoFaseComunicacao[] tiposFasesPermitidos = { TipoFaseComunicacao.ASSINADO };

		try {
			List<CheckableDataTableRowWrapper> selecionadosCheckable = retornarItensCheckableSelecionados(listaDocumentos);

			if (LOG.isInfoEnabled()) {
				LOG.info(MessageFormat.format("Encaminhando {0} documentos para o setor de ID = {1}.", selecionados.size(), idSetorDestino));
			}

			comunicacaoServiceLocal.encaminharParaAssinaturaSetor(selecionados, idSetorDestino, permitirGabinete, incluirFaseNoDeslocamento, false, tiposFasesPermitidos);

			listaDocumentos.removeAll(selecionadosCheckable);
			reportarAviso("Documento(s) encaminhados(s) com sucesso!");
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao encaminhar o(s) documentos(s).", exception, LOG);
		}
	}
	
	private void imprimirDocumentos(){
		List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(listaDocumentos);
		
		if (CollectionUtils.isVazia(selecionados)) {
			reportarAviso("É necessário selecionar pelo menos um documento.");
		} else {
			List<CheckableDataTableRowWrapper> selecionadosCheckable = retornarItensCheckableSelecionados(listaDocumentos);
			List<Long> idsDocumentos = new ArrayList<Long>();
			List<Long> idsSegredoJustica = new ArrayList<Long>();
			
			try{
				for(CheckableDataTableRowWrapper wrapper : selecionadosCheckable){
					URI uri = new URI(((ComunicacaoDocumento)wrapper.getWrappedObject()).getLinkPDF());
					String query = uri.getQuery();
					String idDoc  = (query.substring(0, query.indexOf('&')).split("="))[1];
					idsDocumentos.add(Long.parseLong(idDoc));			
					if (((ComunicacaoDocumento)wrapper.getWrappedObject()).getComunicacao().getConfidencialidade()
						.equals(TipoConfidencialidade.SEGREDO_JUSTICA.getDescricao()))
						idsSegredoJustica.add(Long.parseLong(idDoc));	
							
				}				
				byte[] conteudopdfs = getDocumentoEletronicoService().recuperarDocumentosPDF(idsDocumentos, idsSegredoJustica);
				setConteudoDocumentoDownload(conteudopdfs);
				downloadArquivoPdf();
								
			}catch(ServiceException e){
				reportarErro("Erro ao imprimir documentos");
			} catch (URISyntaxException e) {				// 
				reportarErro("Erro ao imprimir documentos");			
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

	public void limparCampos() {
		setListaDocumentos(null);
		atualizaSessao();
	}

	private void procurarSetorRelator() {
		setIdSetorDestino(Setor.CODIGO_GABINETE_SECRETARIA_JUDICIARIA);
	}

	public void pesquisarDocumentosAssinados() {

		List<ComunicacaoDocumentoResult> documentos = null;
		List<Long> situacaoDoPdf = new LinkedList<Long>();

		// busca o usuário logado
		UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();

		// buscar os documentos com PDF assinado e fase ASSINADO
		situacaoDoPdf.add(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE.getCodigo());
		situacaoDoPdf.add(TipoSituacaoDocumento.JUNTADO.getCodigo());

		ComunicacaoDocumentoPaginatorResult resultado = procuraFasePdf(usuarioAssinatura.getSetor(), situacaoDoPdf, 5L);
		if(resultado != null){
			documentos = resultado.getLista();
			setTotalRegistros(resultado.getTotalResultSet());		
		}
		
		if (CollectionUtils.isVazia(documentos)) {
			reportarAviso("Nenhum documento a expedir.");
			setListaDocumentos(null);
			return;
		}

		OrdenacaoUtils.ordenarListaComunicacaoDocumentoResultDataAssinatura(documentos);
		setListaDocumentos(getCheckableDocumentoList(documentos));

		reportarInformacao(MessageFormat.format("Existe(m) {0} documento(s) a expedir.", documentos.size()));
	}

	/**
	 * Busca a lista de documentos de acordo com a fase do documento e a fase do PDF
	 * 
	 * @param setor
	 * @param tipoSituacaoDocumento
	 * @param filtro
	 * @return
	 */
	public ComunicacaoDocumentoPaginatorResult procuraFasePdf(Setor setor, List<Long> tipoSituacaoDocumento, Long faseDocumento) {
		@SuppressWarnings("deprecation")
		ComunicacaoService comunicacaoService = getComunicacaoService();

		// limpa o cache do hibernate
		try {
			comunicacaoService.limparSessao();
		} catch (ServiceException e1) {
			e1.printStackTrace();
			reportarErro("Erro ao limpar o cache.");
		}

		try {
			FiltroPesquisarDocumentosAssinatura filtro = new FiltroPesquisarDocumentosAssinatura();
			filtro.setSetor(setor);
			filtro.setListaTipoSituacaoDocumento(tipoSituacaoDocumento);
			filtro.setFaseDocumento(faseDocumento);			
			filtro.setDataDocumento(dataAssinaturaDocumento);
			filtro.setTela("expedirDocumentos");
			filtro.setApenasSigilosos(buscarApenasSigilosos);
			filtro.setCarregarFilhos(false);			
			
			return comunicacaoService.pesquisarDocumentosAssinatura(filtro);
		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar os PDFs.");
			setListaDocumentos(null);
			e.printStackTrace();
		}

		return null;
	}

	public void marcarTodosTextos(ActionEvent evt) {
		marcarOuDesmarcarTodas(listaDocumentos);
		setListaDocumentos(listaDocumentos);
	}

	public void atualizarMarcacao(ActionEvent evt) {
		setListaDocumentos(listaDocumentos);
	}
	
	public int getMaxPages(){
		int max = 0;
		
		if(listaDocumentos != null && !listaDocumentos.isEmpty()){
			max = (int) Math.ceil((double)listaDocumentos.size() / (double)getRows());
			max = max > 10 ? 10 : max;
		}
		
		return max;
	}
	
	
	// ###################### GETS AND SETs ######################

	public List<CheckableDataTableRowWrapper> getListaDocumentos() {
		return listaDocumentos;
	}
	
	public void setListaDocumentos(List<CheckableDataTableRowWrapper> listaDocumentos) {
		setAtributo(KEY_LISTA_DOCUMENTOS, listaDocumentos);
		this.listaDocumentos = listaDocumentos;
	}

	public HtmlDataTable getTabelaDocumentos() {
		return tabelaDocumentos;
	}

	public void setTabelaDocumentos(HtmlDataTable tabelaDocumentos) {
		this.tabelaDocumentos = tabelaDocumentos;
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
	
	public Date getDataAssinaturaDocumento() {
		return dataAssinaturaDocumento;
	}
	
	public void setDataAssinaturaDocumento(Date dataAssinaturaDocumento) {
		this.dataAssinaturaDocumento = dataAssinaturaDocumento;
	}
	
	public Integer getRows() {
		if(rows == null){
			if(getSetorUsuarioAutenticado().getId().equals(Setor.CODIGO_SETOR_BAIXA_EXPEDICAO)) {
				setRows(75);
			}else {
				setRows(15);
			}
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
	
	public List<SelectItem> getItensUsuariosSetor() {
		return itensUsuariosSetor;
	}

	public void setItensUsuariosSetor(List<SelectItem> itensUsuariosSetor) {
		this.itensUsuariosSetor = itensUsuariosSetor;
	}
	
	
	public Boolean getSelecionadosTela() {
		return retornarItensSelecionados(listaDocumentos).size() > 0;
	}
	
	public String getIdUsuarioAtribuicao() {
		return idUsuarioAtribuicao;
	}

	public void setIdUsuarioAtribuicao(String idUsuarioAtribuicao) {
		this.idUsuarioAtribuicao = idUsuarioAtribuicao;
	}

	public List<ComunicacaoDocumento> getListaSelecionados() {
		return retornarItensSelecionados(listaDocumentos);
	}

	public void setListaSelecionados(List<ComunicacaoDocumento> listaSelecionados) {
		this.listaSelecionados = listaSelecionados;
	}
}
