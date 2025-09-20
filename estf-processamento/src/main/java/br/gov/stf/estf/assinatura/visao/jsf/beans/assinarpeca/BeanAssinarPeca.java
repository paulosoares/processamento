package br.gov.stf.estf.assinatura.visao.jsf.beans.assinarpeca;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.stficp.RequisicaoAssinaturaArquivoProcessoEletronico;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.servlet.DocumentoDownloadServlet;
import br.gov.stf.estf.assinatura.visao.util.OrdenacaoUtils;
import br.gov.stf.estf.assinatura.visao.util.ProcessoParser;
import br.gov.stf.estf.assinatura.visao.util.TipoOrdenacao;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.documento.model.service.ArquivoProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.impl.AssinaturaDigitalServiceImpl;
import br.gov.stf.estf.documento.model.util.ArquivoProcessoEletronicoSearchData;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.service.ProcessoException;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.core.KeepStateInHttpSession;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;
import br.jus.stf.assinadorweb.api.requisicao.DocumentoPDF;

public class BeanAssinarPeca extends AssinadorBaseBean {

	public static final Object ARQUIVO_PROCESSO_ELETRONICO_LIST = BeanAssinarPeca.class.getCanonicalName() + ".arquivoProcessoEletronicoList";

	private static final long serialVersionUID = 920409279734901951L;
	private static final Log LOG = LogFactory.getLog(BeanAssinarPeca.class);
	private static final Object ITENS_APENAS_DOCUMENTOS_ASSINADOS = new Object();

	private Long identificadorPeca;

	private String siglaClasseProcesso;
	private Long numeroProcesso;
	private Long numeroProtocolo;
	private Short anoProtocolo;
	private Boolean apenasAssinados;

	private List<SelectItem> itensApenasDocumentosAssinados;
	private HtmlDataTable tabelaArquivoProcessoEletronicoList;
	
	private String identificacaoProcessos;
	

	@KeepStateInHttpSession
	private List<CheckableDataTableRowWrapper> arquivoProcessoEletronicoList;

	public BeanAssinarPeca() {
		recuperarSessao();
	}

	@SuppressWarnings("unchecked")
	private void recuperarSessao() {
		restoreStateOfHttpSession();

		if (getAtributo(ITENS_APENAS_DOCUMENTOS_ASSINADOS) == null) {
			setAtributo(ITENS_APENAS_DOCUMENTOS_ASSINADOS, montarTiposSituacaoDocumento());
		}

		setItensApenasDocumentosAssinados((List<SelectItem>) getAtributo(ITENS_APENAS_DOCUMENTOS_ASSINADOS));

		atualizarSessao();
	}

	private void atualizarSessao() {
		applyStateInHttpSession();
	}

	private List<SelectItem> montarTiposSituacaoDocumento() {
		List<SelectItem> itens = new ArrayList<SelectItem>();

		itens.add(new SelectItem(Boolean.FALSE, "Pendente"));
		itens.add(new SelectItem(Boolean.TRUE, "Assinado"));

		return itens;
	}

	// --------------------------- EVENTOS DA TELA ---------------------------

	public void visualizarPDFPecaProcessual(ActionEvent e) {
		CheckableDataTableRowWrapper checkableDataTableRowWrapper = (CheckableDataTableRowWrapper) tabelaArquivoProcessoEletronicoList.getRowData();
		ArquivoProcessoEletronico arquivoProcessoEletronico = (ArquivoProcessoEletronico) checkableDataTableRowWrapper.getWrappedObject();
		DocumentoEletronico documentoEletronico = arquivoProcessoEletronico.getDocumentoEletronico();
		setPDFResponse(documentoEletronico.getArquivo(), documentoEletronico.getId() + "");
	}

	public void pesquisarPecasEletronicas(ActionEvent e) {
		pesquisarPecasEletronicas();
		atualizarSessao();
	}

	public String assinarListaArquivoProcessoEletronico() {
		RequisicaoAssinaturaArquivoProcessoEletronico requisicao = new RequisicaoAssinaturaArquivoProcessoEletronico();
		List<DocumentoPDF<ArquivoProcessoEletronico>> arquivos = new ArrayList<DocumentoPDF<ArquivoProcessoEletronico>>();

		List<ArquivoProcessoEletronico> listaArquivoProcessoEletronicoOriginal = retornarItensSelecionados(arquivoProcessoEletronicoList);
		if (CollectionUtils.isVazia(listaArquivoProcessoEletronicoOriginal)) {
			reportarInformacao("Selecione pelo menos 1 peça processual para assinar");
			return null;
		}

		for (ArquivoProcessoEletronico ape : listaArquivoProcessoEletronicoOriginal) {
			ObjetoIncidente oi = ape.getPecaProcessoEletronico().getObjetoIncidente();
			Long idDocumentoEletronico = ape.getDocumentoEletronicoView().getId();
			
			try {
        		DocumentoEletronico documentoEletronico = ape.getDocumentoEletronico();
        		
        		if (documentoEletronico.getHashValidacao() == null || documentoEletronico.getHashValidacao().isEmpty())
        			documentoEletronico.setHashValidacao(AssinaturaDigitalServiceImpl.gerarHashValidacao());
        		
				getDocumentoEletronicoService().salvar(documentoEletronico);
				arquivos.add(new DocumentoPDF<ArquivoProcessoEletronico>(AssinaturaDigitalServiceImpl.getRodapeAssinaturaDigital(documentoEletronico.getHashValidacao()), montaNomeArquivoParaAssinar(oi, idDocumentoEletronico), ape));
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
		}

		requisicao.setDocumentos(arquivos);
		requisicao.setPageRefresher(getRefreshController());
		setRequestValue(RequisicaoAssinaturaArquivoProcessoEletronico.REQUISICAO_ASSINADOR, requisicao);

		return "assinarServlet";
	}

	private String montaNomeArquivoParaAssinar(ObjetoIncidente oi, Long idDocumentoEletronico) {
		return MessageFormat.format("{0} ({1})", oi.getIdentificacao(), idDocumentoEletronico);
	}

	public void marcarTodos(ActionEvent e) {
		marcarOuDesmarcarTodas(arquivoProcessoEletronicoList);
		setArquivoProcessoEletronicoList(arquivoProcessoEletronicoList);
	}
	
	public String getIdentificacaoProcessos() {
		return identificacaoProcessos;
	}	
	
	public void setIdentificacaoProcessos(String identificacaoProcessos) {
		this.identificacaoProcessos = identificacaoProcessos;
		/*if(StringUtils.isVazia(identificacaoProcessos)){
			return;
		}

		String[] idtProcessos = identificacaoProcessos.split(";");
		ArquivoProcessoEletronicoSearchData searchData = construirSearchData();
		searchData.listaIdObjetoIncidente = new ArrayList<Long>();

		for(int i = 0; i < idtProcessos.length; i++){
			String identificacaoProcesso = idtProcessos[i].trim();

			if (StringUtils.isVazia(identificacaoProcesso)) {
				return;
			}

			String codigo = identificacaoProcesso;
			if (identificacaoProcesso.length() == 14){
				String siglaNumeroTrezeCarac = codigo.substring(0, codigo.length()-1); 
				codigo = siglaNumeroTrezeCarac;
			}else {
				codigo = identificacaoProcesso;
			}

			String classProc = ProcessoParser.getSigla(codigo);
			Long numProc = ProcessoParser.getNumero(codigo);

			if (StringUtils.isVazia(classProc) || numProc == null) {
				reportarAviso("O Processo " + identificacaoProcesso + " não é um processo válido.");
				return;
			}
			
			try {
				Processo processo = getProcessoService().recuperarProcesso(classProc, numProc);
				searchData.listaIdObjetoIncidente.add(processo.getId());
			} catch (ProcessoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			
		}
		
		if(!searchData.listaIdObjetoIncidente.isEmpty()){
			try {
				executarPesquisaBanco(searchData);
			} catch (ServiceException e) {
				reportarErro("Erro ao pesquisar peças.", e, LOG);
			}
		}*/

	}

	// -------------------------------- MÉTODOS --------------------------------

	/**
	 * Faz um switch no status de marcação de todos os elementos de uma lista de CheckableDataTableRowWrapper.
	 */
	@Override
	public void marcarOuDesmarcarTodas(List lista) {
		if (lista != null) {
			for (Object obj : lista) {
				CheckableDataTableRowWrapper check = (CheckableDataTableRowWrapper) obj;
				if (!((ArquivoProcessoEletronico) check.getWrappedObject()).getDocumentoEletronicoView().getDescricaoStatusDocumento()
						.equals(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO)) {
					check.setChecked(!check.getChecked());
				}
			}
		}
	}

	public void pesquisarPecasEletronicas() {
		try {	
			if (((siglaClasseProcesso.equals(null) || siglaClasseProcesso.trim().isEmpty())  || (numeroProcesso== null || numeroProcesso.toString().trim().isEmpty()))
					&& ((identificacaoProcessos == null || identificacaoProcessos.toString().trim().isEmpty()))
					&& ((numeroProtocolo == null || numeroProtocolo.toString().trim().isEmpty()) || (anoProtocolo== null || anoProtocolo.toString().trim().isEmpty())) ){
					reportarAviso("? obrigat?rio informar pelo menos uma das seguintes informa??es: \"N?mero do processo\", \"N?mero do protocolo\" ou \"Lista de processos\".");
					return;
			}
			
			LOG.debug("Construindo SearchData.");
			ArquivoProcessoEletronicoSearchData searchData = construirSearchData();

			executarPesquisaBanco(searchData);
		} catch (ServiceException e1) {
			reportarErro("Erro ao pesquisar peças.", e1, LOG);
		}
	}

	private void executarPesquisaBanco(ArquivoProcessoEletronicoSearchData searchData) throws ServiceException {
		ArquivoProcessoEletronicoService service = getArquivoProcessoEletronicoService();
		service.limparSessao();
		
		LOG.debug("Pesquisando peças no banco.");
		List<ArquivoProcessoEletronico> pecas = service.pesquisarArquivoProcessoEletronico(searchData);
		LOG.info(MessageFormat.format("Quantidade de peças encontradas: {0}", pecas.size()));

		LOG.debug("Ordenando peças pela data de inclusão.");
		OrdenacaoUtils.ordenarListaArquivoProcessoEletronicoDataInclusao(pecas, TipoOrdenacao.DESCENDENTE);

		LOG.debug("Convertendo lista de peças em lista de CheckableDataTableRowWrapper.");
		setArquivoProcessoEletronicoList(getCheckableDataTableRowWrapperList(pecas));

		if (CollectionUtils.isVazia(pecas)) {
			reportarAviso("Nenhuma peça encontrada");
		}else if(pecas.size() == 300){
			reportarAviso("A pesquisa retornou os primeiros 300 registros. Refine sua busca.");
		}
		
	}

	private ArquivoProcessoEletronicoSearchData construirSearchData() {
		String siglaClasseProcesso = null;
		if (StringUtils.isNotVazia(getSiglaClasseProcesso())) {
			siglaClasseProcesso = getSiglaClasseProcesso();
		}

		ArquivoProcessoEletronicoSearchData searchData = new ArquivoProcessoEletronicoSearchData();
		searchData.siglaClasseProcessual = siglaClasseProcesso;
		searchData.numeroProcessual = getNumeroProcesso();
		searchData.numeroProtocolo = getNumeroProtocolo();
		searchData.anoProtocolo = getAnoProtocolo();

		if (BooleanUtils.isTrue(apenasAssinados)) {
			searchData.situacoesDocumentoEletronicoIncluidas.add(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO);
		} else {
			searchData.situacoesDocumentoEletronicoExcluidas.add(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_ASSINADO);
			searchData.situacoesDocumentoEletronicoExcluidas.add(DocumentoEletronico.SIGLA_DESCRICAO_STATUS_AGUARDANDO);
		}
		
		if(identificacaoProcessos != null && !identificacaoProcessos.trim().isEmpty()){
			String[] idtProcessos = identificacaoProcessos.split(";");
			searchData.listaIdObjetoIncidente = new ArrayList<Long>();
	
			for(int i = 0; i < idtProcessos.length; i++){
				String identificacaoProcesso = idtProcessos[i].trim();
	
				if (StringUtils.isVazia(identificacaoProcesso)) {
					continue;
				}
	
				String codigo = identificacaoProcesso;
				if (identificacaoProcesso.length() == 14){
					String siglaNumeroTrezeCarac = codigo.substring(0, codigo.length()-1); 
					codigo = siglaNumeroTrezeCarac;
				}else {
					codigo = identificacaoProcesso;
				}
	
				String classProc = ProcessoParser.getSigla(codigo);
				Long numProc = ProcessoParser.getNumero(codigo);
	
				if (StringUtils.isVazia(classProc) || numProc == null) {
					reportarAviso("O Processo " + identificacaoProcesso + " não é um processo válido.");
					continue;
				}
				
				try {
					Processo processo = getProcessoService().recuperarProcesso(classProc, numProc);
					if (processo != null){
						searchData.listaIdObjetoIncidente.add(processo.getId());
					}else{
						reportarAviso("O Processo " + identificacaoProcesso + " n?o encontrado.");
						continue;
					}
				} catch (ProcessoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
				
			}		
		}
		
		return searchData;
	}

	public String getDocumentoDownloadURL() {

		CheckableDataTableRowWrapper wrapper = (CheckableDataTableRowWrapper) tabelaArquivoProcessoEletronicoList.getRowData();

		ArquivoProcessoEletronico arquivo = (ArquivoProcessoEletronico) wrapper.getWrappedObject();

		HttpServletRequest request = (HttpServletRequest) getRequest();

		String url = request.getContextPath() + "/documento?" + DocumentoDownloadServlet.ARQUIVO_DOCUMENTO_PARAM + "="
				+ arquivo.getDocumentoEletronico().getId();

		return url;
	}

	// --------------------------- GETTERS e SETTERS ---------------------------

	public Long getIdentificadorPeca() {
		return identificadorPeca;
	}

	public void setIdentificadorPeca(Long identificadorPeca) {
		this.identificadorPeca = identificadorPeca;
	}

	public String getSiglaClasseProcesso() {
		return siglaClasseProcesso;
	}

	public void setSiglaClasseProcesso(String siglaClasseProcesso) {
		this.siglaClasseProcesso = siglaClasseProcesso;
	}

	public Long getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(Long numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
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

	public Boolean getApenasAssinados() {
		return apenasAssinados;
	}

	public void setApenasAssinados(Boolean apenasAssinados) {
		this.apenasAssinados = apenasAssinados;
	}

	public List<SelectItem> getItensApenasDocumentosAssinados() {
		return itensApenasDocumentosAssinados;
	}

	public void setItensApenasDocumentosAssinados(List<SelectItem> itensApenasDocumentosAssinados) {
		this.itensApenasDocumentosAssinados = itensApenasDocumentosAssinados;
	}

	public HtmlDataTable getTabelaArquivoProcessoEletronicoList() {
		return tabelaArquivoProcessoEletronicoList;
	}

	public void setTabelaArquivoProcessoEletronicoList(HtmlDataTable tabelaArquivoProcessoEletronicoList) {
		this.tabelaArquivoProcessoEletronicoList = tabelaArquivoProcessoEletronicoList;
	}

	public List<CheckableDataTableRowWrapper> getArquivoProcessoEletronicoList() {
		return arquivoProcessoEletronicoList;
	}

	public void setArquivoProcessoEletronicoList(List<CheckableDataTableRowWrapper> arquivoProcessoEletronicoList) {
		this.arquivoProcessoEletronicoList = arquivoProcessoEletronicoList;
	}
}
