package br.gov.stf.estf.assinatura.visao.jsf.beans.expediente.modelos;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.stf.estf.assinatura.service.TagsLivresServiceLocal;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.entidade.documento.TipoTagsLivresUsuario;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanAdministrarTipoTagsLivres extends AssinadorBaseBean {

	private static final long serialVersionUID = -6754044005126595804L;
	private static final Log LOG = LogFactory.getLog(BeanAdministrarTipoTagsLivres.class);

	// ************************ VARIAVEIS DE SESSAO ************************* //

	private static final Object LISTA_TIPO_TAGS_LIVRES = new Object();
	private static final Object TIPO_TAGS_LIVRES_USUARIO = new Object();

	// ---------------------- VARIÁVEIS ------------------------//

	private TipoTagsLivresUsuario tipoTagsLivresUsuario;

	private List<SelectItem> itensTipoTags;

	private List<CheckableDataTableRowWrapper> listaTipoTags;
	private HtmlDataTable tabelaTipoTags;

	// ########################### SESSAO ################### //

	public BeanAdministrarTipoTagsLivres() {
		restaurarSessao();
	}

	@SuppressWarnings("unchecked")
	public void restaurarSessao() {

		if (getAtributo(TIPO_TAGS_LIVRES_USUARIO) == null) {
			setAtributo(TIPO_TAGS_LIVRES_USUARIO, new TipoTagsLivresUsuario());
		}
		setTipoTagsLivresUsuario((TipoTagsLivresUsuario) getAtributo(TIPO_TAGS_LIVRES_USUARIO));

		setListaTipoTags((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_TIPO_TAGS_LIVRES));
	}

	public void atualizarSessao() {
		setAtributo(TIPO_TAGS_LIVRES_USUARIO, tipoTagsLivresUsuario);
		setAtributo(LISTA_TIPO_TAGS_LIVRES, listaTipoTags);
	}

	// ==================== ACTIONS ================================= //
	public void pesquisarTipoTagsAction(ActionEvent evt) {
		pesquisarTipoTags();
		atualizarSessao();
	}

	public void excluirModelo(ActionEvent evt) {
		excluirModelos();
		atualizarSessao();
	}

	public void limparTelaAction(ActionEvent evt) {
		limparTela();
		atualizarSessao();
	}

	public void alterarTipoTagsLivresAction(ActionEvent evt) {
		alterarTipoTags();
		atualizarSessao();
	}

	public void salvarTipoTagsLivresAction(ActionEvent evt) {
		salvarTipoTagsLivres();
		novoTipoTagLivres();
		atualizarSessao();
	}

	public void atualizarMarcacao(ActionEvent evt) {
		setListaTipoTags(listaTipoTags);
	}

	public void marcarTodosTextos(ActionEvent evt) {
		marcarOuDesmarcarTodas(listaTipoTags);
		setListaTipoTags(listaTipoTags);
	}

	// ###################### METHODS ############################# //

	/**
	 * Método responsável pela pesquisa dos modelos existentes.
	 */
	public void pesquisarTipoTags() {
		TagsLivresServiceLocal tagsLivresServiceLocal = getTagsLivresServiceLocal();
		List<TipoTagsLivresUsuario> tiposTagsLivresUsuario = Collections.emptyList();

		try {
			tiposTagsLivresUsuario = tagsLivresServiceLocal.pesquisarTiposTagsLivresUsuario(tipoTagsLivresUsuario);

			if (CollectionUtils.isVazia(tiposTagsLivresUsuario)) {
				reportarAviso("Nenhum tipo de tag encontrado.");
			} else {
				reportarInformacao(MessageFormat.format("Foi(ram) encontrado(s) {0} tipo(s) de tags.", tiposTagsLivresUsuario.size()));
			}
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao pesquisar os tipo de campos livres de preenchimento.", exception, LOG);
		}

		setListaTipoTags(getCheckableDataTableRowWrapperList(tiposTagsLivresUsuario));
	}

	/**
	 * Método para limpar os atributos da tela
	 */
	public void limparTela() {
		setTipoTagsLivresUsuario(new TipoTagsLivresUsuario());
		setListaTipoTags(null);
	}

	/**
	 * Método reponsável pela exclusão dos tipos
	 */
	public void excluirModelos() {
		TagsLivresServiceLocal tagsLivresServiceLocal = getTagsLivresServiceLocal();

		List<CheckableDataTableRowWrapper> selecionadosCheckable = retornarItensCheckableSelecionados(listaTipoTags);
		List<TipoTagsLivresUsuario> selecionados = retornarItensSelecionados(listaTipoTags);

		if (CollectionUtils.isVazia(selecionados)) {
			reportarAviso("Selecione pelo menos um tipo tag livre.");
		} else {
			try {
				tagsLivresServiceLocal.excluirTiposTagsLivresUsuario(selecionados);
				listaTipoTags.removeAll(selecionadosCheckable);
				setAtributo(LISTA_TIPO_TAGS_LIVRES, listaTipoTags);
				novoTipoTagLivres();
				reportarAviso("Tipo(s) removido(s) com sucesso!");
			} catch (RegraDeNegocioException exception) {
				reportarAviso(exception);
			} catch (ServiceLocalException exception) {
				reportarErro("Erro ao excluir tipos de tags.", exception, LOG);
			}
		}
	}

	public void alterarTipoTags() {
		tipoTagsLivresUsuario = (TipoTagsLivresUsuario) ((CheckableDataTableRowWrapper) tabelaTipoTags.getRowData()).getWrappedObject();
	}

	/**
	 * Método responsável em salvar o tipo de modelo.
	 */
	private void salvarTipoTagsLivres() {
		TagsLivresServiceLocal tagsLivresServiceLocal = getTagsLivresServiceLocal();

		try {
			tagsLivresServiceLocal.salvarTiposTagsLivresUsuario(tipoTagsLivresUsuario);
			reportarInformacao("Dados salvos com sucesso.");
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao salvar tipo de tags.", exception, LOG);
		}
	}

	private void novoTipoTagLivres() {
		setTipoTagsLivresUsuario(new TipoTagsLivresUsuario());
		setListaTipoTags(null);
	}

	// ********************* GET & SET ******************************** //

	public TipoTagsLivresUsuario getTipoTagsLivresUsuario() {
		return tipoTagsLivresUsuario;
	}

	public void setTipoTagsLivresUsuario(TipoTagsLivresUsuario tipoTagsLivresUsuario) {
		this.tipoTagsLivresUsuario = tipoTagsLivresUsuario;
	}

	public List<SelectItem> getItensTipoTags() {
		return itensTipoTags;
	}

	public void setItensTipoTags(List<SelectItem> itensTipoTags) {
		this.itensTipoTags = itensTipoTags;
	}

	public List<CheckableDataTableRowWrapper> getListaTipoTags() {
		return listaTipoTags;
	}

	public void setListaTipoTags(List<CheckableDataTableRowWrapper> listaTipoTags) {
		this.listaTipoTags = listaTipoTags;
	}

	public HtmlDataTable getTabelaTipoTags() {
		return tabelaTipoTags;
	}

	public void setTabelaTipoTags(HtmlDataTable tabelaTipoTags) {
		this.tabelaTipoTags = tabelaTipoTags;
	}
}
