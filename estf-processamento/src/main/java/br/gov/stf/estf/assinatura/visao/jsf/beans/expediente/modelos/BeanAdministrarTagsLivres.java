package br.gov.stf.estf.assinatura.visao.jsf.beans.expediente.modelos;

import java.util.List;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.documento.model.service.TagsLivresUsuarioService;
import br.gov.stf.estf.documento.model.service.TipoTagsLivresUsuarioService;
import br.gov.stf.estf.entidade.documento.TagsLivresUsuario;
import br.gov.stf.estf.entidade.documento.TipoTagsLivresUsuario;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanAdministrarTagsLivres extends AssinadorBaseBean {

	private static final long serialVersionUID = 8617916090095868937L;

	// ################### VARIAVEIS DE SESSAO ###########################

	private static final Object TAG_LIVRES = new Object();
	private static final Object LISTA_TAGS_LIVRES = new Object();
	private static final Object RENDENRED_ALTERA_TAG = new Object();

	// ###################################################################

	// #################### DECLARAÇÃO VARIÁVEIS #########################

	private TagsLivresUsuario tagsLivresUsuario;
	private List<CheckableDataTableRowWrapper> listaTagsLivres;
	private HtmlDataTable tabelaTagsLivres;
	private Boolean renderedTelaAlteraTagLivres;

	// ###################################################################

	public BeanAdministrarTagsLivres() {
		restaurarSessao();
	}

	@SuppressWarnings("unchecked")
	public void restaurarSessao() {

		if (getAtributo(TAG_LIVRES) == null) {
			setAtributo(TAG_LIVRES, new TagsLivresUsuario());
		}
		setTagsLivresUsuario((TagsLivresUsuario) getAtributo(TAG_LIVRES));

		if (getAtributo(RENDENRED_ALTERA_TAG) == null) {
			setAtributo(RENDENRED_ALTERA_TAG, false);
		}
		setRenderedTelaAlteraTagLivres((Boolean) getAtributo(RENDENRED_ALTERA_TAG));

		setListaTagsLivres((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_TAGS_LIVRES));
	}

	public void atualizarSessao() {
		setAtributo(TAG_LIVRES, tagsLivresUsuario);
		setAtributo(LISTA_TAGS_LIVRES, listaTagsLivres);
		setAtributo(RENDENRED_ALTERA_TAG, renderedTelaAlteraTagLivres);
	}

	// #################### ACTIONS #######################################

	public void pesquisarTagsLivresAction(ActionEvent action) {
		pesquisarTagsLivres();
		atualizarSessao();
	}

	public void alterarTagsLivresAction(ActionEvent action) {
		alterarTagsLivres();
		atualizarSessao();
	}

	public void criarNovoTagsLivresAction(ActionEvent action) {
		persistirTagsLivres();
		atualizarSessao();
	}

	public void limparTela(ActionEvent evt) {
		setTagsLivresUsuario(new TagsLivresUsuario());
		setListaTagsLivres(null);
		atualizarSessao();
		setRenderedTelaAlteraTagLivres(false);
	}

	public void fecharTelaAction(ActionEvent evt) {
		fecharTela();
		atualizarSessao();
	}

	public void salvaDadosAlteradosTagAction(ActionEvent evt) {
		salvarDadosAlteradosTag();
		atualizarSessao();
	}

	public void marcarTodosTextos(ActionEvent evt) {
		marcarOuDesmarcarTodas(listaTagsLivres);
		setListaTagsLivres(listaTagsLivres);
	}

	public void atualizarMarcacao(ActionEvent evt) {
		setListaTagsLivres(listaTagsLivres);
	}

	// ######################### METHODS ##################################

	public void pesquisarTagsLivres() {
		setListaTagsLivres(pesquisarTags(tagsLivresUsuario.getNomeRotulo()));
	}

	@SuppressWarnings("deprecation")
	public List<CheckableDataTableRowWrapper> pesquisarTags(String nomeRotulo) {

		TagsLivresUsuarioService tagsLivresService = getTagsLivresUsuarioService();

		try {
			List<TagsLivresUsuario> lista = tagsLivresService.pesquisarNomeRotuloOuDescricao(nomeRotulo, 1L, null);

			if (CollectionUtils.isVazia(lista)) {
				reportarAviso("Nenhum registro encontrado para o parâmetro informado.");
			}
			return getCheckableDataTableRowWrapperList(lista);

		} catch (Exception e) {
			e.printStackTrace();
			reportarErro("Erro ao pesquisar", e.getMessage());
		}
		return null;
	}

	public void alterarTagsLivres() {

		tagsLivresUsuario = (TagsLivresUsuario) ((CheckableDataTableRowWrapper) tabelaTagsLivres.getRowData()).getWrappedObject();
		setRenderedTelaAlteraTagLivres(true);

	}

	/**
	 * Ação do botão alterar. Somente é alterado a descrição do objeto. O código
	 * será sempre alterado pelo sistema a fim de que não ocorram problemas.
	 */
	@SuppressWarnings("deprecation")
	public void salvarDadosAlteradosTag() {

		TagsLivresUsuarioService tagsLivresService = getTagsLivresUsuarioService();

		if (tagsLivresUsuario.getNomeRotulo() != null && tagsLivresUsuario.getNomeRotulo().length() > 0) {
			String tagConcatenada = "@@" + tagsLivresUsuario.getNomeRotulo() + "@@";
			tagsLivresUsuario.setCodigoRotulo(tagConcatenada);
		} else {
			reportarAviso("Favor digitar o valor no campo descrição.");
			return;
		}

		try {
			tagsLivresService.salvar(tagsLivresUsuario);
		} catch (ServiceException e) {
			e.printStackTrace();
			reportarErro("Erro ao salvar os dados.");
		}
		reportarAviso("Dados alterado com sucesso.");
		novoTagLivres();
	}

	@SuppressWarnings({ "deprecation", "unlikely-arg-type" })
	public void removerTagsLivres() {

		TagsLivresUsuarioService tagsLivresService = getTagsLivresUsuarioService();

		List<TagsLivresUsuario> selecionados = retornarItensSelecionados(listaTagsLivres);

		if (CollectionUtils.isVazia(selecionados)) {
			reportarAviso("Selecione pelo menos uma tag livre.");
			return;
		}

		try {
			for (TagsLivresUsuario tc : selecionados) {

				tagsLivresService.excluir(tc);
			}
			listaTagsLivres.removeAll(selecionados);
			setAtributo(LISTA_TAGS_LIVRES, listaTagsLivres);
			novoTagLivres();
			reportarAviso("Tag(s) removida(s) com sucesso!");
		} catch (ServiceException se) {
			se.printStackTrace();
			reportarErro("Erro ao excluir a(s) tag(s).");
		}
	}

	private void novoTagLivres() {
		setTagsLivresUsuario(new TagsLivresUsuario());
		setListaTagsLivres(null);
		setRenderedTelaAlteraTagLivres(false);
	}

	@SuppressWarnings("deprecation")
	public void persistirTagsLivres() {
		TagsLivresUsuarioService tagsLivresService = getTagsLivresUsuarioService();
		TipoTagsLivresUsuarioService tipoTagsService = getTipoTagsLivresUsuarioService();

		if (tagsLivresUsuario.getNomeRotulo() == null && tagsLivresUsuario.getNomeRotulo().trim().length() == 0) {
			reportarAviso("A tag deve ser informada.");
			return;
		} else {
			String codigoTag = tagsLivresUsuario.getNomeRotulo();
			String semEspacos = StringUtils.substituiEspacosBrancoPorUnderline(codigoTag);
			String tagConcatenada = "@@" + semEspacos + "@@";
			tagsLivresUsuario.setCodigoRotulo(tagConcatenada);
			tagsLivresUsuario.setDscTagLivres(tagsLivresUsuario.getDscTagLivres());
			try {
				// recupera somente o tipo com o tipo "campo livre"
				TipoTagsLivresUsuario tipoTags = tipoTagsService.recuperarPorId(1L);
				tagsLivresUsuario.setTipoTagsLivres(tipoTags);
			} catch (ServiceException e) {
				e.printStackTrace();
				reportarErro("Erro ao pesquisar o tipo de campo igual a 'campo livre'. ");
			}

			try {
				tagsLivresService.incluir(tagsLivresUsuario);
				novoTagLivres();
			} catch (ServiceException e) {
				e.printStackTrace();
				reportarErro("Erro ao incluir o campo livre de preenchimento");
			}
			reportarInformacao("Operação realizada com sucesso.");
		}
		atualizarSessao();
	}

	public void fecharTela() {
		setRenderedTelaAlteraTagLivres(false);
	}

	// #################### GET AND SETS ##################################

	public TagsLivresUsuario getTagsLivresUsuario() {
		return tagsLivresUsuario;
	}

	public void setTagsLivresUsuario(TagsLivresUsuario tagsLivresUsuario) {
		this.tagsLivresUsuario = tagsLivresUsuario;
	}

	public List<CheckableDataTableRowWrapper> getListaTagsLivres() {
		return listaTagsLivres;
	}

	public void setListaTagsLivres(List<CheckableDataTableRowWrapper> listaTagsLivres) {
		this.listaTagsLivres = listaTagsLivres;
	}

	public HtmlDataTable getTabelaTagsLivres() {
		return tabelaTagsLivres;
	}

	public void setTabelaTagsLivres(HtmlDataTable tabelaTagsLivres) {
		this.tabelaTagsLivres = tabelaTagsLivres;
	}

	public Boolean getRenderedTelaAlteraTagLivres() {
		return renderedTelaAlteraTagLivres;
	}

	public void setRenderedTelaAlteraTagLivres(Boolean renderedTelaAlteraTagLivres) {
		this.renderedTelaAlteraTagLivres = renderedTelaAlteraTagLivres;
	}

}
