package br.gov.stf.estf.assinatura.visao.jsf.beans.assinatura;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang.BooleanUtils;

import br.gov.stf.estf.assinatura.security.UsuarioAssinatura;
import br.gov.stf.estf.assinatura.stficp.RequisicaoAssinaturaDocumentoTexto;
import br.gov.stf.estf.assinatura.stficp.RequisicaoAssinaturaDocumentoTextoPeticao;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.OrdenacaoUtils;
import br.gov.stf.estf.assinatura.visao.util.TipoOrdenacao;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.documento.model.service.DocumentoTextoPeticaoService;
import br.gov.stf.estf.documento.model.service.DocumentoTextoService;
import br.gov.stf.estf.documento.model.service.impl.AssinaturaDigitalServiceImpl;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoTexto;
import br.gov.stf.estf.entidade.documento.DocumentoTextoPeticao;
import br.gov.stf.estf.entidade.documento.TextoPeticao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoDocumento;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.core.KeepStateInHttpSession;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;
import br.jus.stf.assinadorweb.api.requisicao.DocumentoPDF;
import br.jus.stf.assinadorweb.api.util.PageRefresher;

public class BeanAssinatura extends AssinadorBaseBean {

	private static final long serialVersionUID = -7228734104478758301L;

	public static final Object PROCESSO_OU_PROTOCOLO = BeanAssinatura.class.getCanonicalName() + ".processoOuProtocolo";

	@KeepStateInHttpSession
	private List<DocumentoTextoWrapper> listaDocumentoTexto;
	@KeepStateInHttpSession
	private List<DocumentoTextoPeticaoWrapper> listaDocumentoTextoProtocolo;
	private Boolean processoOuProtocolo;

	private String textoTipoAssinatura;

	// Parametros pesquisa
	private String filtroVisualizar = "RV";
	private Date dataInicio;
	private Date dataFim;
	private String siglaClasse;
	private Long numeroProcesso;
	private Long numeroProtocolo;
	private Short anoProtocolo;

	private HtmlDataTable tabelaTextoProcessos;
	private HtmlDataTable tabelaTextoProtocolos;

	public BeanAssinatura() {
		recuperarSessao();
	}

	private void recuperarSessao() {
		restoreStateOfHttpSession();

		if (getAtributo(PROCESSO_OU_PROTOCOLO) == null) {
			setAtributo(PROCESSO_OU_PROTOCOLO, Boolean.TRUE);
		}
		setProcessoOuProtocolo((Boolean) getAtributo(PROCESSO_OU_PROTOCOLO));

		if (BooleanUtils.isTrue(processoOuProtocolo)) {
			if (CollectionUtils.isVazia(listaDocumentoTexto)) {
				pesquisarDocumentosProcessosSetor();
			}
		} else if (BooleanUtils.isFalse(processoOuProtocolo)) {
			if (CollectionUtils.isVazia(listaDocumentoTextoProtocolo)) {
				pesquisarDocumentosProtocolosSetor();
			}
		}

		atualizarSessao();
	}

	private void atualizarSessao() {
		applyStateInHttpSession();
		setAtributo(PROCESSO_OU_PROTOCOLO, processoOuProtocolo);
	}

	// --------------------------- EVENTOS DA TELA ---------------------------

	public void visualizarPDFProcesso(ActionEvent e) {
		CheckableDataTableRowWrapper checkableDataTableRowWrapper = (CheckableDataTableRowWrapper) tabelaTextoProcessos.getRowData();

		DocumentoTexto documentoTexto = (DocumentoTexto) checkableDataTableRowWrapper.getWrappedObject();
		setPDFResponse(documentoTexto.getDocumentoEletronico().getArquivo(), documentoTexto.getDocumentoEletronico().getId() + "");
	}

	public void visualizarPDFProtocolo(ActionEvent e) {
		CheckableDataTableRowWrapper checkableDataTableRowWrapper = (CheckableDataTableRowWrapper) tabelaTextoProtocolos.getRowData();

		DocumentoTextoPeticao documentoTextoPeticao = (DocumentoTextoPeticao) checkableDataTableRowWrapper.getWrappedObject();
		setPDFResponse(documentoTextoPeticao.getDocumentoEletronico().getArquivo(), documentoTextoPeticao.getDocumentoEletronico().getId() + "");
	}

	public void cancelarDocumentosProcessosSelecionados(ActionEvent e) {
		List<DocumentoTexto> listaDocumentoTextoOriginal = retornarItensSelecionados(listaDocumentoTexto);

		try {
			if (CollectionUtils.isVazia(listaDocumentoTextoOriginal)) {
				reportarInformacao("Selecione pelo menos 1 documento para CANCELAR");
				return;
			}

			if (getDocumentoTextoService().cancelarDocumentos(listaDocumentoTextoOriginal, "Cancelado pelo ministro")) {
				reportarInformacao("Os documentos selecionados foram cancelados");
			}
			pesquisarDocumentosProcessosSetor();
		} catch (ServiceException e1) {
			e1.printStackTrace();
			reportarErro(e1.getMessage());
		}
	}

	public void cancelarDocumentosProtocolosSelecionados(ActionEvent e) {
		List<DocumentoTextoPeticao> listaDocumentoTextoProtocoloOriginal = retornarItensSelecionados(listaDocumentoTextoProtocolo);
		try {
			if (CollectionUtils.isVazia(listaDocumentoTextoProtocoloOriginal)) {
				reportarInformacao("Selecione pelo menos 1 documento para CANCELAR");
				return;
			}

			if (getDocumentoTextoPeticaoService().cancelarDocumentos(listaDocumentoTextoProtocoloOriginal, "Cancelado pelo ministro")) {
				reportarInformacao("Os documentos selecionados foram cancelados");
			}
			pesquisarDocumentosProtocolosSetor();
		} catch (ServiceException e1) {
			e1.printStackTrace();
			reportarErro(e1.getMessage());
		}
	}

	public void pesquisarDocumentosProcessosSetor(ActionEvent e) {
		pesquisarDocumentosProcessosSetor();
	}

	public void pesquisarDocumentosProtocolosSetor(ActionEvent e) {
		pesquisarDocumentosProtocolosSetor();
	}

	public void pesquisarPorDocumentosProcessos(ActionEvent e) {
		setProcessoOuProtocolo(Boolean.TRUE);
		limparCampos();
		pesquisarDocumentosProcessosSetor();
	}

	public void pesquisarPorDocumentosProtocolos(ActionEvent e) {
		setProcessoOuProtocolo(Boolean.FALSE);
		limparCampos();
		pesquisarDocumentosProtocolosSetor();
	}

	/**
	 * Assina os documentoTexto selecionados na tabela
	 * 
	 * @return
	 */
	public String assinarListaDocumentoTexto() {

		RequisicaoAssinaturaDocumentoTexto requisicao = new RequisicaoAssinaturaDocumentoTexto();
		List<DocumentoPDF<DocumentoTexto>> arquivos = new ArrayList<DocumentoPDF<DocumentoTexto>>();

		List<DocumentoTexto> listaDocumentoTextoOriginal = retornarItensSelecionados(listaDocumentoTexto);
		if (CollectionUtils.isVazia(listaDocumentoTextoOriginal)) {
			reportarInformacao("Selecione pelo menos 1 documento para assinar");
			return null;
		}

		for (DocumentoTexto dt : listaDocumentoTextoOriginal) {
			ObjetoIncidente oi = dt.getTexto().getObjetoIncidente();
			
			try {
				DocumentoEletronico documentoEletronico = dt.getDocumentoEletronico();
				
        		if (documentoEletronico.getHashValidacao() == null || documentoEletronico.getHashValidacao().isEmpty())
        			documentoEletronico.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());
        		
				getDocumentoEletronicoService().salvar(documentoEletronico);
				arquivos.add(new DocumentoPDF<DocumentoTexto>(AssinaturaDigitalServiceImpl.getRodapeAssinaturaDigital(documentoEletronico.getHashValidacao()), oi.getIdentificacao(), dt));
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
		}

		requisicao.setDocumentos(arquivos);
		requisicao.setPageRefresher((PageRefresher) getRefreshController());
		setRequestValue(RequisicaoAssinaturaDocumentoTexto.REQUISICAO_ASSINADOR, requisicao);

		return "assinarServlet";
	}

	/**
	 * Assina os documentoTextoPeticao selecionados na tabela
	 * 
	 * @return
	 */
	public String assinarListaDocumentoTextoPeticao() {

		RequisicaoAssinaturaDocumentoTextoPeticao requisicao = new RequisicaoAssinaturaDocumentoTextoPeticao();
		List<DocumentoPDF<DocumentoTextoPeticao>> arquivos = new ArrayList<DocumentoPDF<DocumentoTextoPeticao>>();

		List<DocumentoTextoPeticao> listaDocumentoTextoPeticaoOriginal = retornarItensSelecionados(listaDocumentoTextoProtocolo);
		if (CollectionUtils.isVazia(listaDocumentoTextoPeticaoOriginal)) {
			reportarInformacao("Selecione pelo menos 1 documento para assinar");
			return null;
		}

		for (DocumentoTextoPeticao documentoTextoPeticao : listaDocumentoTextoPeticaoOriginal) {
			if (documentoTextoPeticao.getTipoSituacaoDocumento() == TipoSituacaoDocumento.ASSINADO_DIGITALMENTE) {
				reportarInformacao("Selecione apenas protocolos não assinados");
				return null;
			}
		}

		for (DocumentoTextoPeticao dtp : listaDocumentoTextoPeticaoOriginal) {
			TextoPeticao tp = dtp.getTextoPeticao();
			
			try {
				DocumentoEletronico documentoEletronico = dtp.getDocumentoEletronico();
				
        		if (documentoEletronico.getHashValidacao() == null || documentoEletronico.getHashValidacao().isEmpty())
        			documentoEletronico.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());
        		
				getDocumentoEletronicoService().salvar(documentoEletronico);
				arquivos.add(new DocumentoPDF<DocumentoTextoPeticao>(AssinaturaDigitalServiceImpl.getRodapeAssinaturaDigital(documentoEletronico.getHashValidacao()), tp.getNumero() + "/" + tp.getAno(), dtp));
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
		}

		requisicao.setDocumentos(arquivos);
		requisicao.setPageRefresher((PageRefresher) getRefreshController());
		setRequestValue(RequisicaoAssinaturaDocumentoTextoPeticao.REQUISICAO_ASSINADOR, requisicao);

		return "assinarServlet";
	}

	// -------------------------------- MÉTODOS --------------------------------

	public void limparCampos() {
		setSiglaClasse(null);
		setNumeroProcesso(null);
		setListaDocumentoTexto(null);
		setAnoProtocolo(null);
		setAnoProtocolo(null);
		setListaDocumentoTextoProtocolo(null);
		setDataFim(null);
		setDataInicio(null);
		setFiltroVisualizar("RV");
		atualizarSessao();
	}

	/**
	 * Método responsável pela busca de processos que estão revisados e não
	 * assinados.
	 */
	public void pesquisarDocumentosProcessosSetor() {
		UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();
		setTextoTipoAssinatura("Processo");

		try {

			if (dataInicio == null) {
				dataInicio = dataFim;
			} else if (dataFim == null) {
				dataFim = dataInicio;
			} else {
			//	if (filtroVisualizar.equals(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE.getSigla())) {
			//		reportarAviso("A pesquisa retornou os primeiros 300 registros. Refine sua busca");
			//	}
			}

			if (processoOuProtocolo != null && processoOuProtocolo.booleanValue()) {
				DocumentoTextoService documentoTextoService = getDocumentoTextoService();
				documentoTextoService.limparSessao();

				List<DocumentoTexto> documentos = documentoTextoService.pesquisarDocumentosSetor(usuarioAssinatura.getSetor(),
						TipoSituacaoDocumento.getBySigla(filtroVisualizar), dataInicio, dataFim, siglaClasse, numeroProcesso);
				OrdenacaoUtils.ordenarListaDocumentoTextoDataInclusao(documentos, TipoOrdenacao.DESCENDENTE);
				setListaDocumentoTexto(DocumentoTextoWrapper.getDocumentoTextoWrapperList(documentos));
			}

			atualizarSessao();

		} catch (ServiceException e1) {
			reportarErro(e1.getMessage());
		}
	}

	public void pesquisarDocumentosProtocolosSetor() {
		UsuarioAssinatura usuarioAssinatura = (UsuarioAssinatura) getUser();

		setTextoTipoAssinatura("Protocolo");

		try {

			if (dataInicio == null) {
				dataInicio = dataFim;
			} else if (dataFim == null) {
				dataFim = dataInicio;
			} else {
				if (filtroVisualizar.equals(TipoSituacaoDocumento.ASSINADO_DIGITALMENTE.getSigla())) {
					reportarAviso("A pesquisa retornou os primeiros 300 registros. Refine sua busca");
				}
			}

			if (BooleanUtils.isFalse(processoOuProtocolo)) {
				DocumentoTextoPeticaoService documentoTextoPeticaoService = getDocumentoTextoPeticaoService();
				documentoTextoPeticaoService.limparSessao();

				List<DocumentoTextoPeticao> documentos = documentoTextoPeticaoService.pesquisarDocumentosSetor(usuarioAssinatura.getSetor(),
						TipoSituacaoDocumento.getBySigla(filtroVisualizar), dataInicio, dataFim, anoProtocolo, numeroProtocolo);

				OrdenacaoUtils.ordenarListaDocumentoTextoPeticaoDataRevisao(documentos, TipoOrdenacao.DESCENDENTE);

				setListaDocumentoTextoProtocolo(DocumentoTextoPeticaoWrapper.getDocumentoTextoPeticaosProtocoloWrapperList(documentos));
			}

			atualizarSessao();

		} catch (ServiceException e1) {
			reportarErro(e1.getMessage());
		}
	}

	// ------------------------------ GET e SET ------------------------------

	public List<DocumentoTextoWrapper> getListaDocumentoTexto() {
		return listaDocumentoTexto;
	}

	public void setListaDocumentoTexto(List<DocumentoTextoWrapper> listaDocumentoTexto) {
		this.listaDocumentoTexto = listaDocumentoTexto;
	}

	public HtmlDataTable getTabelaTextoProcessos() {
		return tabelaTextoProcessos;
	}

	public void setTabelaTextoProcessos(HtmlDataTable tabelaTextoProcessos) {
		this.tabelaTextoProcessos = tabelaTextoProcessos;
	}

	public String getListaDocumentoTextoSize() {
		return listaDocumentoTexto.size() + "";
	}

	public String getListaDocumentoTextoProtocoloSize() {
		return listaDocumentoTextoProtocolo.size() + "";
	}

	public String getFiltroVisualizar() {
		return filtroVisualizar;
	}

	public void setFiltroVisualizar(String filtroVisualizar) {
		this.filtroVisualizar = filtroVisualizar;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getSiglaClasse() {
		return siglaClasse;
	}

	public void setSiglaClasse(String siglaClasse) {
		this.siglaClasse = siglaClasse;
	}

	public Long getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public Boolean getProcessoOuProtocolo() {
		return processoOuProtocolo;
	}

	public void setProcessoOuProtocolo(Boolean processoOuProtocolo) {
		this.processoOuProtocolo = processoOuProtocolo;
	}

	public Long getNumeroProtocolo() {
		return numeroProtocolo;
	}

	public void setNumeroProtocolo(Long numeroProtocolo) {
		this.numeroProtocolo = numeroProtocolo;
	}

	public Short getAnoProtocolo() {
		return anoProtocolo;
	}

	public void setAnoProtocolo(Short anoProtocolo) {
		this.anoProtocolo = anoProtocolo;
	}

	public List<DocumentoTextoPeticaoWrapper> getListaDocumentoTextoProtocolo() {
		return listaDocumentoTextoProtocolo;
	}

	public void setListaDocumentoTextoProtocolo(List<DocumentoTextoPeticaoWrapper> listaDocumentoTextoProtocolo) {
		this.listaDocumentoTextoProtocolo = listaDocumentoTextoProtocolo;
	}

	public HtmlDataTable getTabelaTextoProtocolos() {
		return tabelaTextoProtocolos;
	}

	public void setTabelaTextoProtocolos(HtmlDataTable tabelaTextoProtocolos) {
		this.tabelaTextoProtocolos = tabelaTextoProtocolos;
	}

	public String getTextoTipoAssinatura() {
		return textoTipoAssinatura;
	}

	public void setTextoTipoAssinatura(String textoTipoAssinatura) {
		this.textoTipoAssinatura = textoTipoAssinatura;
	}

}
