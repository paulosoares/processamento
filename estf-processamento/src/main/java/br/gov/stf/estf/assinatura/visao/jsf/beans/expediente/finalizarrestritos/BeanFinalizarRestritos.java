package br.gov.stf.estf.assinatura.visao.jsf.beans.expediente.finalizarrestritos;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.component.html.HtmlDataTable;
import org.springframework.beans.factory.annotation.Autowired;

import br.gov.stf.estf.assinatura.service.impl.CollectionUtils;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.jsf.beans.ComunicacaoDocumento;
import br.gov.stf.estf.configuracao.model.service.ConfiguracaoSistemaService;
import br.gov.stf.estf.documento.model.util.ComunicacaoDocumentoResult;
import br.gov.stf.estf.documento.model.util.FiltroPesquisarDocumentosAssinatura;
import br.gov.stf.estf.entidade.configuracao.ConfiguracaoSistema;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.documento.TipoFaseComunicacao;
import br.gov.stf.estf.entidade.documento.TipoSituacaoPeca;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.core.KeepStateInHttpSession;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;


public class BeanFinalizarRestritos extends AssinadorBaseBean {

	private static final long serialVersionUID = -4971716149801262315L;
	
	@KeepStateInHttpSession
	private List<CheckableDataTableRowWrapper> documentosAguadandoRevisao;
	
	@SuppressWarnings("unused")
	private CheckableDataTableRowWrapper documentoSelecionado;
	
	@KeepStateInHttpSession
	private HtmlDataTable tabelaAguardandoRevisao;
	
	@SuppressWarnings("unused")
	private static final Log LOG = LogFactory.getLog(BeanFinalizarRestritos.class);
	private String anotacaoCancelamento;
	private ComunicacaoDocumento comDocumentoTemporariaRowData;
	private final String SISTEMA_PROCESSAMENTO = "PROCESSAMENTO";
	private final String SETOR_DOC_RESTRITOS_MENU = "codigo.setor.doc.restritos.menu"; 
	private Long total = 0L;
	
	@KeepStateInHttpSession
	private Long regPagina;
	
	public void setTotal(Long total) {
		this.total = total;
	}

	private String textoComunicacao;

	@SuppressWarnings("unused")
	@Autowired
	private ConfiguracaoSistemaService configuracaoSistemaService;

	public BeanFinalizarRestritos() {
		restoreStateOfHttpSession();
	}
	
	public void inicializar() {
		try {
			if(!getSetorRestritos().contains(getSetorUsuarioAutenticado().getId().toString())) {
				reportarAviso("Setor não autorizado.");
				return;
			}
			
			setDocumentosAguadandoRevisao(getCheckableDocumentoList(getComunicacaoServiceLocal().pesquisarDocumentos(TipoFaseComunicacao.RESTRITOS.getCodigoFase(), null)));
			applyStateInHttpSession();
		} catch (Exception e) {

			reportarErro("Erro ao recuperar os documentos. " + e.getMessage());
			setDocumentosAguadandoRevisao(null);
			e.printStackTrace();
			return;
		}
	}

	private String getSetorRestritos() throws ServiceException {
		try {
			ConfiguracaoSistema configuracaoSistema = getConfiguracaoSistemaService()
					.recuperarValor(SISTEMA_PROCESSAMENTO, SETOR_DOC_RESTRITOS_MENU);
			if (configuracaoSistema == null) {
				return null;
			} else {
				return configuracaoSistema.getValor();
			}
		} catch (ServiceException e) {
			return null;
		}
	}

	public void marcarTodosTextos() {
		marcarOuDesmarcarTodas(documentosAguadandoRevisao);
	}

	public void atualizarDocumentoSelecionado() throws ServiceException {
		documentoSelecionado = (CheckableDataTableRowWrapper) tabelaAguardandoRevisao.getRowData();
	}

	@SuppressWarnings("deprecation")
	public void finalizarSemJuntar() throws NumberFormatException, ServiceException {
		List<ComunicacaoDocumento> documentosAFinalizar = retornarItensSelecionados(documentosAguadandoRevisao);
		if (documentosAFinalizar.isEmpty()) {
			reportarAviso("Nenhum documento foi selecionado.");
			return;
		}
		try {
			for (ComunicacaoDocumento docs : documentosAFinalizar) {
				getFaseComunicacaoService().incluirFase(TipoFaseComunicacao.FINALIZADO, docs.getComunicacao(), null,
						null);
			}
			reportarInformacao("Documentos finalizados com sucesso!");
			inicializar();

		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void finalizarJuntar() {
		List<ComunicacaoDocumento> documentosAFinalizar = retornarItensSelecionados(documentosAguadandoRevisao);
		if (documentosAFinalizar.isEmpty()) {
			reportarAviso("Nenhum documento foi selecionado.");
			return;
		}
		try {
			String msgSucesso ="";
			String msgErro ="";
			for (ComunicacaoDocumento docs : documentosAFinalizar) {
				List<PecaProcessoEletronico> pecas = getPecaProcessoEletronicoService()
						.recuperarListaPecasComunicacao(docs.getDocumentoComunicacao());
				for (PecaProcessoEletronico pec : pecas) {
					if(pec.getTipoSituacaoPeca()!= null && pec.getTipoSituacaoPeca().getCodigo().equals(TipoSituacaoPeca.PENDENTE.getCodigo())) {
					pec.setNumeroOrdemPeca(getPecaProcessoEletronicoService().recuperarProximoNumeroDeOrdem(pec.getObjetoIncidente().getPrincipal()));
					pec.setTipoSituacaoPeca(TipoSituacaoPeca.JUNTADA);
					getPecaProcessoEletronicoService().salvar(pec);
					if (msgSucesso == "") {
						msgSucesso = "Documento(s) finalizado(s): "+ docs.getComunicacao().getIdentificacaoProcessual();
					}else {
						msgSucesso = msgSucesso +  ", " + docs.getComunicacao().getIdentificacaoProcessual();
					}
					}else {
						if (msgErro == "") {
							msgErro = "Já estava(m) juntado(s) e foi(ram) finalizado(s): "+ docs.getComunicacao().getIdentificacaoProcessual();
						}else {
							msgErro = msgErro +  ", " +  docs.getComunicacao().getIdentificacaoProcessual();
						}			
					}

				}

				getFaseComunicacaoService().incluirFase(TipoFaseComunicacao.FINALIZADO, docs.getComunicacao(), null,
						null);
			}

			// getComunicacaoServiceLocal().finalizarRestritosJuntar(documentosAFinalizar,
			// (UsuarioAssinatura) getUser());

			if(msgSucesso != "") 
				reportarInformacao(msgSucesso);
			if(msgErro != "") 
				reportarInformacao(msgErro);
			inicializar();

		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public void finalizarApenasJuntar() throws ServiceLocalException {
		List<ComunicacaoDocumento> documentosAFinalizar = retornarItensSelecionados(documentosAguadandoRevisao);
		if (documentosAFinalizar.isEmpty()) {
			reportarAviso("Nenhum documento foi selecionado.");
			return;
		}
		try {
			String msgSucesso ="";
			String msgErro ="";
			for (ComunicacaoDocumento docs : documentosAFinalizar) {
				List<PecaProcessoEletronico> pecas = getPecaProcessoEletronicoService()
						.recuperarListaPecasComunicacao(docs.getDocumentoComunicacao());
				if (pecas == null || pecas.isEmpty()) {
					if (msgSucesso == "") {
						msgSucesso = "Nenhum documento(s) a juntar: "+ docs.getComunicacao().getIdentificacaoProcessual();
					}else {
						msgSucesso = msgSucesso +  ", " + msgSucesso;
					}
				} else {
				
					for (PecaProcessoEletronico pec : pecas) {
						if(pec.getTipoSituacaoPeca()!= null && pec.getTipoSituacaoPeca().getCodigo().equals(TipoSituacaoPeca.PENDENTE.getCodigo())) {
						pec.setNumeroOrdemPeca(getPecaProcessoEletronicoService().recuperarProximoNumeroDeOrdem(pec.getObjetoIncidente().getPrincipal()));
						pec.setTipoSituacaoPeca(TipoSituacaoPeca.JUNTADA);
						getPecaProcessoEletronicoService().salvar(pec);
						if (msgSucesso == "") {
							msgSucesso = "Documento(s) Juntado(s) com sucesso: "+ docs.getComunicacao().getIdentificacaoProcessual();
						}else {
							msgSucesso = msgSucesso +  ", " +  docs.getComunicacao().getIdentificacaoProcessual();
						}
						}else {
							if (msgErro == "") {
								msgErro = "Documento(s) já juntado(s): "+ docs.getComunicacao().getIdentificacaoProcessual();
							}else {
								msgErro = msgErro +  ", " +  docs.getComunicacao().getIdentificacaoProcessual();
							}			
						}
					}
				}
			}
			if(msgSucesso != "") 
				reportarInformacao(msgSucesso);
			if(msgErro != "") 
				reportarAviso(msgErro);
			inicializar();

		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public void finalizarApenasDesJuntar() throws ServiceLocalException {
		List<ComunicacaoDocumento> documentosAFinalizar = retornarItensSelecionados(documentosAguadandoRevisao);
		String msgSucesso ="";
		String msgErro ="";
		if (documentosAFinalizar.isEmpty()) {
			reportarAviso("Nenhum documento foi selecionado.");
			return;
		}
		try {
			for (ComunicacaoDocumento docs : documentosAFinalizar) {
				List<PecaProcessoEletronico> pecas = getPecaProcessoEletronicoService()
						.recuperarListaPecasComunicacao(docs.getDocumentoComunicacao());
				if (pecas == null || pecas.isEmpty()) {
					if (msgSucesso == "") {
						msgSucesso = "Documento(s) já juntado(s): "+ docs.getComunicacao().getIdentificacaoProcessual();
					}else {
						msgSucesso = msgSucesso +  ", " +  docs.getComunicacao().getIdentificacaoProcessual();
					}
				} else {
					for (PecaProcessoEletronico pec : pecas) {
						if(pec.getTipoSituacaoPeca()!= null && pec.getTipoSituacaoPeca().getCodigo().equals(TipoSituacaoPeca.JUNTADA.getCodigo())) {
						pec.setNumeroOrdemPeca(null);
						pec.setTipoSituacaoPeca(TipoSituacaoPeca.PENDENTE);
						getPecaProcessoEletronicoService().salvar(pec);
						if (msgSucesso == "") {
							msgSucesso = "Documento(s) marcado(s) como Pendente de Juntada: "+ docs.getComunicacao().getIdentificacaoProcessual();
						}else {
							msgSucesso = msgSucesso +  ", " +  docs.getComunicacao().getIdentificacaoProcessual();
						}
						}else {
							if (msgErro == "") {
								msgErro = "Documento(s) já juntado(s): "+ docs.getComunicacao().getIdentificacaoProcessual();
							}else {
								msgErro = msgErro +  ", " +  docs.getComunicacao().getIdentificacaoProcessual();
							}
						}

					}
				}
			}
			if(msgSucesso != "") 
				reportarInformacao(msgSucesso);
			if(msgErro != "") 
				reportarAviso(msgErro);
			inicializar();

		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	

	public void recuperaLinhaParaModalPanel(ActionEvent evt) {
		comDocumentoTemporariaRowData = (ComunicacaoDocumento) ((CheckableDataTableRowWrapper) tabelaAguardandoRevisao
				.getRowData()).getWrappedObject();
	}



	public void abrirPdf(ActionEvent e) {
		recuperaLinhaParaModalPanel(null);
		FiltroPesquisarDocumentosAssinatura filtro = new FiltroPesquisarDocumentosAssinatura();
		List<Long> id = new ArrayList<Long>();
		id.add(comDocumentoTemporariaRowData.getComunicacao().getId());
		filtro.setIds(id);
		filtro.setSetor(getSetorUsuarioAutenticado());
		filtro.setFaseDocumento(TipoFaseComunicacao.RESTRITOS.getCodigoFase());
		filtro.setCarregarFilhos(false);
		try {
			ComunicacaoDocumentoResult result = getComunicacaoServiceLocal().pesquisarDocumentos(filtro).getLista()
					.get(0);
			comDocumentoTemporariaRowData = new ComunicacaoDocumento(result);
			String link = comDocumentoTemporariaRowData.getLinkPDF();
			link = link.replaceAll("../../", "/");
			HttpServletRequest httpRequest = (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
			StringBuffer requestURLStringBuffer = httpRequest.getRequestURL();
			int index = requestURLStringBuffer.indexOf("/", 8);
			String serverURL = requestURLStringBuffer.substring(0, index);
			String contextURL = serverURL + httpRequest.getContextPath();

			getFacesContext().getExternalContext().redirect(contextURL + link);
		} catch (ServiceException ee) {
			reportarErro("Erro ao recuperar caminho do arquivo.");
		} catch (IOException ee) {
			reportarErro("Erro ao recuperar caminho do arquivo.");
		} catch (RegraDeNegocioException ee) {
			reportarErro("Erro ao recuperar caminho do arquivo.");
		} catch (ServiceLocalException e1) {
			reportarErro("Erro ao recuperar caminho do arquivo.");
		}

	}


	public void imprimirDocumentosAction(ActionEvent e) {
		imprimirDocumentos();

	}

	private void imprimirDocumentos() {
		List<ComunicacaoDocumento> selecionados = retornarItensSelecionados(documentosAguadandoRevisao);

		if (CollectionUtils.isVazia(selecionados)) {
			reportarAviso("É necessário selecionar pelo menos um documento.");
		} else {
			List<CheckableDataTableRowWrapper> selecionadosCheckable = retornarItensCheckableSelecionados(
					documentosAguadandoRevisao);
			List<Long> idsDocumentos = new ArrayList<Long>();

			try {
				for (CheckableDataTableRowWrapper wrapper : selecionadosCheckable) {
					URI uri = new URI(((ComunicacaoDocumento) wrapper.getWrappedObject()).getLinkPDF());
					String query = uri.getQuery();
					String idDoc = (query.substring(0, query.indexOf('&')).split("="))[1];
					idsDocumentos.add(Long.parseLong(idDoc));
				}
				byte[] conteudopdfs = getDocumentoEletronicoService().recuperarDocumentosPDF(idsDocumentos);
				setConteudoDocumentoDownload(conteudopdfs);
				downloadArquivoPdf();

			} catch (ServiceException e) {
				reportarErro("Erro ao imprimir documentos");
			} catch (URISyntaxException e) { //
				reportarErro("Erro ao imprimir documentos");
			}
		}
	}

	public List<CheckableDataTableRowWrapper> getDocumentosAguadandoRevisao() {
		return this.documentosAguadandoRevisao;
	}

	public void setDocumentosAguadandoRevisao(List<CheckableDataTableRowWrapper> documentosAguadandoRevisao) {
		this.documentosAguadandoRevisao = documentosAguadandoRevisao;
	}

	public String getAnotacaoCancelamento() {
		return anotacaoCancelamento;
	}

	public void setAnotacaoCancelamento(String anotacaoCancelamento) {
		this.anotacaoCancelamento = anotacaoCancelamento;
	}

	public HtmlDataTable getTabelaAguardandoRevisao() {
		return tabelaAguardandoRevisao;
	}

	public void setTabelaAguardandoRevisao(HtmlDataTable tabelaAguardandoRevisao) {
		this.tabelaAguardandoRevisao = tabelaAguardandoRevisao;
	}

	public String getRecuperarTextoComunicacaoAction() {

		//return getRecuperarTextoDocumento(tabelaAguardandoRevisao);
return null;
	}

	public String getTextoComunicacao() {
		return this.textoComunicacao;
	}
	
	public long getTotal() {
		if(documentosAguadandoRevisao == null) {
			total = 0L;
		}else {
			total =  (long) documentosAguadandoRevisao.size();
		}
		return total;
	}
	
	public PecaProcessoEletronico retornaPeca( Long idDocumento, Long idComunicacao) throws ServiceException {
		PecaProcessoEletronico pecas = getPecaProcessoEletronicoService().recuperaPecaComunicacao(idDocumento, idComunicacao);
		return pecas;
	}

	public Long getRegPagina() {
		if (regPagina == null) {
			this.regPagina = 30L;
		}
		return regPagina;
	}

	public void setRegPagina(Long regPagina ) {
		if(regPagina == 30L) {
		regPagina = 9999L;}else {
			regPagina = 30L;
		}
	}
	public void setRegPagina( ) {
		if(regPagina == 30L) {
		regPagina = 9999L;}else {
			regPagina = 30L;
		}
	}
}
