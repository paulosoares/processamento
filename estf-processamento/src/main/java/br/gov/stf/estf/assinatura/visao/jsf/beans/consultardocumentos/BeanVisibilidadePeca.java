package br.gov.stf.estf.assinatura.visao.jsf.beans.consultardocumentos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.jsf.beans.assinatura.DocumentoTextoWrapper;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.documento.model.service.ArquivoProcessoEletronicoService;
import br.gov.stf.estf.documento.model.service.enums.TipoDeAcessoDoDocumento;
import br.gov.stf.estf.entidade.documento.ArquivoProcessoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoEletronico;
import br.gov.stf.estf.entidade.documento.DocumentoEletronicoView;
import br.gov.stf.estf.entidade.documento.PecaProcessoEletronico;
import br.gov.stf.estf.entidade.processostf.ObjetoIncidente;
import br.gov.stf.estf.entidade.processostf.Processo;
import br.gov.stf.estf.processostf.model.service.ObjetoIncidenteService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.core.KeepStateInHttpSession;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanVisibilidadePeca extends AssinadorBaseBean {

	private static final long serialVersionUID = 7207921218659944698L;
	private static final Log LOG = LogFactory.getLog(BeanVisibilidadePeca.class);

	private static final String FILTRO_TODAS_PECAS = "TODAS";

	private static final String KEY_LISTA_PECA_PROCESSO_ELET = BeanVisibilidadePeca.class.getCanonicalName() + ".listaPecaProcessoEletronico";

	private String textoTipoAssinatura;
	@KeepStateInHttpSession
	private List<DocumentoTextoWrapper> listaDocumentoTexto;

	// Parametros pesquisa
	private String filtroVisualizar = FILTRO_TODAS_PECAS;
	private String siglaClasse;
	private String numeroProcesso;

	private HtmlDataTable tabelaPecaProcessoEletronico;
	private List<CheckableDataTableRowWrapper> listaPecaProcessoEletronico;
	private List<SelectItem> tiposSituacaoPeca = carregarComboTipoSituacaoPeca();

	@PostConstruct
	@SuppressWarnings({ "unchecked" })
	private void restaurarSessao() {
		if (getAtributo(KEY_LISTA_PECA_PROCESSO_ELET) == null) {
			setAtributo(KEY_LISTA_PECA_PROCESSO_ELET, listaPecaProcessoEletronico);
		}

		setListaPecaProcessoEletronico((List<CheckableDataTableRowWrapper>) getAtributo(KEY_LISTA_PECA_PROCESSO_ELET));
	}

	public void atualizaSessao() {
		applyStateInHttpSession();
		setAtributo(KEY_LISTA_PECA_PROCESSO_ELET, listaPecaProcessoEletronico);
	}

	public String getSetor() {
		return getSetorUsuarioAutenticado().getNome();
	}

	public void pesquisarPecasAction(ActionEvent evt) {
		pesquisarPecas();
		atualizaSessao();
	}

	public void pesquisarPecas() {

		ArquivoProcessoEletronicoService arquivoProcessoEletronicoService = getArquivoProcessoEletronicoService();

		Long codSetor = getSetorUsuarioAutenticado().getId();
		List<Long> idsArquivos = Collections.emptyList();
		String siglaClasseProcesso = "";
		Long numProcesso = null;
		if (!StringUtils.isVazia(numeroProcesso)) {
			numProcesso = Long.parseLong(numeroProcesso);
		}
		if(siglaClasse != null && siglaClasse.trim() != "" ){
			siglaClasseProcesso = siglaClasse.toUpperCase();
		}

		try {
			idsArquivos = arquivoProcessoEletronicoService.pesquisarPecasSetor(codSetor, getTiposAcesso(), isUsuarioGabineteSEJ(), siglaClasseProcesso, numProcesso);
		} catch (ServiceException exception) {
			reportarErro("Erro ao pesquisar as peças.", exception, LOG);
			return;
		}

		if (CollectionUtils.isNotVazia(idsArquivos)) {
			List<ArquivoProcessoEletronico> pecas = Collections.emptyList();

			try {
				arquivoProcessoEletronicoService.limparSessao();
				pecas = arquivoProcessoEletronicoService.pesquisar(idsArquivos);
			} catch (ServiceException exception) {
				reportarErro("Erro ao pesquisar peças.", exception, LOG);
				return;
			}

			setListaPecaProcessoEletronico(getCheckableDataTableRowWrapperList(pecas));

			if (CollectionUtils.isVazia(pecas)) {
				reportarAviso("Nenhuma peça encontrada.");
			}
		} else {
			reportarAviso("Nenhuma peça encontrada.");
		}
	}

	private List<String> getTiposAcesso() {
		List<String> tiposAcesso = new ArrayList<String>();

		if (!filtroVisualizar.equals(FILTRO_TODAS_PECAS)) {
			tiposAcesso.add(filtroVisualizar);
		} else {
			tiposAcesso.add(DocumentoEletronico.TIPO_ACESSO_INTERNO);
			tiposAcesso.add(DocumentoEletronico.TIPO_ACESSO_PUBLICO);
		}

		return tiposAcesso;
	}

	public void pendenteParaPublicoAction(ActionEvent evt) {
		DocumentoEletronico doc  = recuperarDocumentoEletronicoPelaView(getDocumentoEletronico());
		alterarTipoAcessoDocumento(doc, TipoDeAcessoDoDocumento.PUBLICO);
	}

	public void publicoParaPendenteAction(ActionEvent evt) {
		DocumentoEletronico doc  = recuperarDocumentoEletronicoPelaView(getDocumentoEletronico());
		alterarTipoAcessoDocumento(doc, TipoDeAcessoDoDocumento.INTERNO);
	}

	private void alterarTipoAcessoDocumento(DocumentoEletronico doc, TipoDeAcessoDoDocumento tipoAcesso) {
		try {
			getDocumentoEletronicoService().alterarTipoDeAcessoDoDocumento(doc, tipoAcesso);
			getDocumentoEletronicoService().limparSessao();
			pesquisarPecas();
			atualizaSessao();
			reportarInformacao("Tipo de acesso de documento alterado com sucesso!");
		} catch (ServiceException e) {
			e.printStackTrace();
			reportarErro("Erro na alteração do tipo de acesso do documento: " + doc.getId());
		}
	}
	
	public DocumentoEletronico recuperarDocumentoEletronicoPelaView(DocumentoEletronicoView view){
		try {
			return getDocumentoEletronicoService().recuperarPorId(view.getId());
		} catch (Exception e) {
			e.printStackTrace();
			reportarErro("Erro o tentar localizar o documento na base.");
		}
		return null;
	}

	public void pesquisarPecas(Long idObjIncidente) {
		try {
			ObjetoIncidenteService objetoIncidenteService = getObjetoIncidenteService();
			ObjetoIncidente<?> objIncidente = objetoIncidenteService.recuperarPorId(idObjIncidente);

			ArquivoProcessoEletronicoService service = getArquivoProcessoEletronicoService();
			List<ArquivoProcessoEletronico> pecas = service.pesquisarPecasPeloIdObjetoIncidente(objIncidente.getPrincipal().getId());
			List<ArquivoProcessoEletronico> pecasFiltradas = verificarFiltro(pecas);

			setListaPecaProcessoEletronico(getCheckableDataTableRowWrapperList(pecasFiltradas));

			if (CollectionUtils.isVazia(pecasFiltradas)) {
				reportarAviso("Nenhuma peça encontrada.");
			}

		} catch (ServiceException e) {
			reportarErro("Erro na pequisa do processo.", e, LOG);
		}
	}

	public List<ArquivoProcessoEletronico> verificarFiltro(List<ArquivoProcessoEletronico> pecas) {

		List<ArquivoProcessoEletronico> pecasFiltradas = new ArrayList<ArquivoProcessoEletronico>();
		if (!filtroVisualizar.equals(FILTRO_TODAS_PECAS)) {
			for (ArquivoProcessoEletronico arquivoProcessoEletronico : pecas) {
				if (arquivoProcessoEletronico.getDocumentoEletronico().getTipoAcesso().equals(filtroVisualizar)) {
					pecasFiltradas.add(arquivoProcessoEletronico);
				}
			}
		} else {
			pecasFiltradas.addAll(pecas);
		}

		return pecasFiltradas;
	}

	public String getEstiloBotaoPendentePublicoTabela() {

		if (isPecaPendente() && isUsuarioPecaPendentePublica()) {
			return "opacity:1; color:red";
		} else {
			return "opacity:0.4; color:red";
		}
	}

	public String getEstiloBotaoPublicoPendenteTabela() {

		if (isPecaPublica() && isUsuarioPecaPublicaPendente()) {
			return "opacity:1";
		} else {
			return "opacity:0.4";
		}
	}

	public String getTitleBotaoPublicoPendenteTabela() {
		String title = "";
		if (!isUsuarioPecaPublicaPendente()) {
			title = "Privilégio insuficiente para modificar esta peça para 'Pendente'.";
		} else if (isPecaPendente()) {
			title = "Esta peça já está como 'Pendente'.";
		}

		return title;
	}

	public String getTitleBotaoPendentePublicoTabela() {
		String title = "";
		if (!isUsuarioPecaPendentePublica()) {
			title = "Privilégio insuficiente para modificar esta peça para 'Pública'.";
		} else if (isPecaPublica()) {
			title = "Esta peça já está como 'Pública'.";
		}

		return title;
	}

	public boolean getDisabledBotaoPendentePublicoTabela() {
		return !(isUsuarioPecaPendentePublica() && isPecaPendente());
	}

	public boolean getDisabledBotaoPublicoPendenteTabela() {
		return !(isUsuarioPecaPublicaPendente() && isPecaPublica());
	}

	private DocumentoEletronicoView getDocumentoEletronico() {
		
		CheckableDataTableRowWrapper wrapper = (CheckableDataTableRowWrapper) tabelaPecaProcessoEletronico.getRowData();
		ArquivoProcessoEletronico arquivo = (ArquivoProcessoEletronico) wrapper.getWrappedObject();
		DocumentoEletronicoView documento = arquivo.getDocumentoEletronicoView();

		return documento;
	}

	private ArquivoProcessoEletronico getArquivoProcessoEletronico() {
		CheckableDataTableRowWrapper wrapper = (CheckableDataTableRowWrapper) tabelaPecaProcessoEletronico.getRowData();
		ArquivoProcessoEletronico arquivo = (ArquivoProcessoEletronico) wrapper.getWrappedObject();
		return arquivo;
	}

	public String getSituacaoPeca() {
		DocumentoEletronicoView documento = getDocumentoEletronico();
		PecaProcessoEletronico pecaProcessoEletronico = getArquivoProcessoEletronico().getPecaProcessoEletronico();
		String situacao = "";
		if (isPecaPendente(documento)) {
			situacao = "Pendente";
		} else if (isPecaPublica(documento)) {
			situacao = "Pública";
		}

		return pecaProcessoEletronico.getTipoPecaProcesso().getDescricao() + " - " + situacao;
	}

	public String getDataInclusaoPeca() {
		ArquivoProcessoEletronico arquivoProcessoEletronico = getArquivoProcessoEletronico();
		PecaProcessoEletronico pecaProcessoEletronico = arquivoProcessoEletronico.getPecaProcessoEletronico();
		Date dataInclusaoPeca = pecaProcessoEletronico.getDataInclusao();

		String dataInclusaoFormatada = "";
		if (dataInclusaoPeca != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			dataInclusaoFormatada = dateFormat.format(dataInclusaoPeca);
		}

		return dataInclusaoFormatada;
	}

	public String getRelator() {
		PecaProcessoEletronico pecaProcessoEletronico = getArquivoProcessoEletronico().getPecaProcessoEletronico();

		try {
			return ((Processo) pecaProcessoEletronico.getObjetoIncidente().getPrincipal()).getMinistroRelatorAtual().getNome();
		} catch (Exception e) {
			return "";
		}
	}

	private boolean isPecaPublica() {
		return isPecaPublica(getDocumentoEletronico());
	}

	private boolean isPecaPublica(DocumentoEletronicoView view) {
		return view.getTipoAcesso().equals(DocumentoEletronico.TIPO_ACESSO_PUBLICO);
	}

	private boolean isPecaPendente() {
		return isPecaPendente(getDocumentoEletronico());
	}

	private boolean isPecaPendente(DocumentoEletronicoView view) {
		return view.getTipoAcesso().equals(DocumentoEletronico.TIPO_ACESSO_INTERNO);
	}

	public String getDocumentoDownloadURL() {
		CheckableDataTableRowWrapper wrapper = (CheckableDataTableRowWrapper) tabelaPecaProcessoEletronico.getRowData();
		ArquivoProcessoEletronico arquivo = (ArquivoProcessoEletronico) wrapper.getWrappedObject();
		return montaUrlDownload(arquivo);
	}

	// ------------------------------ GET e SET ------------------------------

	public String getFiltroVisualizar() {
		return filtroVisualizar;
	}

	public void setFiltroVisualizar(String filtroVisualizar) {
		this.filtroVisualizar = filtroVisualizar;
	}

	public String getSiglaClasse() {
		return siglaClasse;
	}

	public void setSiglaClasse(String siglaClasse) {
		this.siglaClasse = siglaClasse;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getTextoTipoAssinatura() {
		return textoTipoAssinatura;
	}

	public void setTextoTipoAssinatura(String textoTipoAssinatura) {
		this.textoTipoAssinatura = textoTipoAssinatura;
	}

	public void setListaDocumentoTexto(List<DocumentoTextoWrapper> listaDocumentoTexto) {
		this.listaDocumentoTexto = listaDocumentoTexto;
	}

	public List<DocumentoTextoWrapper> getListaDocumentoTexto() {
		return listaDocumentoTexto;
	}

	public String getListaDocumentoTextoSize() {
		return (listaDocumentoTexto == null) ? "" : listaDocumentoTexto.size() + "";
	}

	public void setListaPecaProcessoEletronico(List<CheckableDataTableRowWrapper> listaPecaProcessoEletronico) {
		this.listaPecaProcessoEletronico = listaPecaProcessoEletronico;
	}

	public List<CheckableDataTableRowWrapper> getListaPecaProcessoEletronico() {
		setAtributo(KEY_LISTA_PECA_PROCESSO_ELET, listaPecaProcessoEletronico);
		return listaPecaProcessoEletronico;
	}

	public void setTabelaPecaProcessoEletronico(HtmlDataTable tabelaPecaProcessoEletronico) {
		this.tabelaPecaProcessoEletronico = tabelaPecaProcessoEletronico;
	}

	public HtmlDataTable getTabelaPecaProcessoEletronico() {
		return tabelaPecaProcessoEletronico;
	}

	public void setTiposSituacaoPeca(List<SelectItem> tiposSituacaoPeca) {
		this.tiposSituacaoPeca = tiposSituacaoPeca;
	}

	public List<SelectItem> getTiposSituacaoPeca() {
		return tiposSituacaoPeca;
	}

}