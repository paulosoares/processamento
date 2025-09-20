package br.gov.stf.estf.assinatura.visao.jsf.beans.autoridade;

import java.text.MessageFormat;
import java.util.List;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.gov.stf.estf.assinatura.service.TagsLivresServiceLocal;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.entidade.documento.TagsLivresUsuario;
import br.gov.stf.estf.intimacao.model.service.exception.ServiceLocalException;
import br.gov.stf.framework.exception.RegraDeNegocioException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanAdministrarAutoridade extends AssinadorBaseBean {

	private static final long serialVersionUID = -6146375904116905085L;
	private static final Log LOG = LogFactory.getLog(BeanAdministrarAutoridade.class);

	// ************************ VARIAVEIS DE SESSAO ************************* //

	private static final Object LISTA_AUTORIDADE = new Object();
	private static final Object TAGS_AUTORIDADE = new Object();
	private static final Object RENDERED_PERMISSAO_SALVAR = new Object();
	private static final Object RENDERED_BOTAO_ALTERAR = new Object();
	private static final Object RENDERED_TELA_SALVAR = new Object();
	private static final Object RENDERED_TELA_ALTERAR = new Object();

	// ---------------------- VARIÁVEIS ------------------------//

	private TagsLivresUsuario tagsAutoridades;
	private Boolean renderedPermissaoSalvar;
	private Boolean renderedTelaSalvaAutoridade;
	private Boolean renderedTelaAlterar;

	private List<SelectItem> itensAutoridade;

	private List<CheckableDataTableRowWrapper> listaAutoridade;
	private HtmlDataTable tabelaAutoridade;

	// ########################### SESSAO ################### //

	public BeanAdministrarAutoridade() {
		restaurarSessao();
	}

	@SuppressWarnings("unchecked")
	public void restaurarSessao() {

		if (getAtributo(TAGS_AUTORIDADE) == null) {
			setAtributo(TAGS_AUTORIDADE, new TagsLivresUsuario());
		}
		setTagsAutoridades((TagsLivresUsuario) getAtributo(TAGS_AUTORIDADE));

		if (isUsuarioMaster()) {
			setAtributo(RENDERED_PERMISSAO_SALVAR, true);
		} else {
			setAtributo(RENDERED_PERMISSAO_SALVAR, false);
		}
		setRenderedPermissaoSalvar((Boolean) getAtributo(RENDERED_PERMISSAO_SALVAR));

		if (getAtributo(RENDERED_TELA_ALTERAR) == null) {
			setAtributo(RENDERED_TELA_ALTERAR, false);
		}
		setRenderedTelaAlterar((Boolean) getAtributo(RENDERED_TELA_ALTERAR));

		if (getAtributo(RENDERED_TELA_SALVAR) == null) {
			setAtributo(RENDERED_TELA_SALVAR, false);
		}
		setRenderedTelaSalvaAutoridade((Boolean) getAtributo(RENDERED_TELA_SALVAR));

		setListaAutoridade((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_AUTORIDADE));
	}

	public void atualizarSessao() {
		setAtributo(TAGS_AUTORIDADE, tagsAutoridades);
		setAtributo(LISTA_AUTORIDADE, listaAutoridade);
		setAtributo(RENDERED_PERMISSAO_SALVAR, renderedPermissaoSalvar);
		setAtributo(RENDERED_TELA_SALVAR, renderedTelaSalvaAutoridade);
		setAtributo(RENDERED_TELA_ALTERAR, renderedTelaAlterar);
	}

	// ==================== ACTIONS ================================= //
	public void pesquisarAutoridadesAction(ActionEvent evt) {
		pesquisarAutoridade();
		atualizarSessao();
	}

	public void limparTelaAction(ActionEvent evt) {
		limparTela();
		atualizarSessao();
	}

	public void mostraDadosParaAlterarAutoridadeAction(ActionEvent evt) {
		mostraDadosParaAlterarAutoridade();
		atualizarSessao();
	}

	public void atualizarMarcacao(ActionEvent evt) {
		setListaAutoridade(listaAutoridade);
	}

	public void salvarAutoridadeAction(ActionEvent evt) {
		salvarAutoridade();
		novaAutoridade();
		atualizarSessao();
	}

	public void alterarAutoridadeAction(ActionEvent evt) {
		alterarAutoridade();
		novaAutoridade();
		atualizarSessao();
	}

	public void marcarTodosTextos(ActionEvent evt) {
		marcarOuDesmarcarTodas(listaAutoridade);
		setListaAutoridade(listaAutoridade);
	}

	public void abreTelaDeSalvarAction(ActionEvent evt) {
		setRenderedTelaSalvaAutoridade(true);
		atualizarSessao();
	}

	public void fecharTelaAction(ActionEvent evt) {
		setRenderedTelaSalvaAutoridade(false);
		setRenderedTelaAlterar(false);
		setTagsAutoridades(new TagsLivresUsuario());
		atualizarSessao();
	}

	// ###################### METHODS ############################# //

	/**
	 * Método responsável pela pesquisa dos modelos existentes.
	 */
	public void pesquisarAutoridade() {
		TagsLivresServiceLocal tagsLivresServiceLocal = getTagsLivresServiceLocal();
		List<TagsLivresUsuario> tagsAutoridades;

		try {
			tagsAutoridades = tagsLivresServiceLocal.pesquisarTagsAutoridades(getTagsAutoridades());

			if (CollectionUtils.isVazia(tagsAutoridades)) {
				reportarAviso("Nenhuma autoridade encontrada.");
			} else {
				reportarInformacao(MessageFormat.format("Foi(ram) encontrada(s) {0} autoridades.", tagsAutoridades.size()));
			}

			setListaAutoridade(getCheckableDataTableRowWrapperList(tagsAutoridades));
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao pesquisar tags de autoridades.", exception, LOG);
		}
	}

	/**
	 * Método para limpar os atributos da tela
	 */
	public void limparTela() {
		setTagsAutoridades(new TagsLivresUsuario());
		setListaAutoridade(null);
		setRenderedTelaSalvaAutoridade(false);
		setRenderedTelaAlterar(false);
	}

	public void mostraDadosParaAlterarAutoridade() {
		tagsAutoridades = (TagsLivresUsuario) ((CheckableDataTableRowWrapper) tabelaAutoridade.getRowData()).getWrappedObject();
		setRenderedTelaAlterar(true);
	}

	/**
	 * Método responsável em salvar o tipo de tag livres.
	 */
	private void salvarAutoridade() {
		TagsLivresServiceLocal tagsLivresServiceLocal = getTagsLivresServiceLocal();

		try {
			tagsLivresServiceLocal.salvarTagsAutoridades(tagsAutoridades);
			reportarInformacao("Operação realizada com sucesso.");
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao salvar tags de autoridades.", exception, LOG);
		}
	}

	private void alterarAutoridade() {
		TagsLivresServiceLocal tagsLivresServiceLocal = getTagsLivresServiceLocal();

		try {
			tagsLivresServiceLocal.salvarTagsAutoridades(tagsAutoridades);
			reportarAviso("Dados alterado com sucesso.");
		} catch (RegraDeNegocioException exception) {
			reportarAviso(exception);
		} catch (ServiceLocalException exception) {
			reportarErro("Erro ao salvar tags de autoridades.", exception, LOG);
		}
	}

	private void novaAutoridade() {
		setTagsAutoridades(new TagsLivresUsuario());
		setListaAutoridade(null);
		setRenderedTelaSalvaAutoridade(false);
		setRenderedTelaAlterar(false);
	}

	// ********************* GET & SET ******************************** //

	public List<SelectItem> getItensAutoridade() {
		return itensAutoridade;
	}

	public void setItensAutoridade(List<SelectItem> itensAutoridade) {
		this.itensAutoridade = itensAutoridade;
	}

	public List<CheckableDataTableRowWrapper> getListaAutoridade() {
		return listaAutoridade;
	}

	public void setListaAutoridade(List<CheckableDataTableRowWrapper> listaAutoridade) {
		this.listaAutoridade = listaAutoridade;
	}

	public HtmlDataTable getTabelaAutoridade() {
		return tabelaAutoridade;
	}

	public void setTabelaAutoridade(HtmlDataTable tabelaAutoridade) {
		this.tabelaAutoridade = tabelaAutoridade;
	}

	public TagsLivresUsuario getTagsAutoridades() {
		return tagsAutoridades;
	}

	public void setTagsAutoridades(TagsLivresUsuario tagsAutoridades) {
		this.tagsAutoridades = tagsAutoridades;
	}

	public Boolean getRenderedPermissaoSalvar() {
		return renderedPermissaoSalvar;
	}

	public void setRenderedPermissaoSalvar(Boolean renderedPermissaoSalvar) {
		this.renderedPermissaoSalvar = renderedPermissaoSalvar;
	}

	public Boolean getRenderedTelaSalvaAutoridade() {
		return renderedTelaSalvaAutoridade;
	}

	public void setRenderedTelaSalvaAutoridade(Boolean renderedTelaSalvaAutoridade) {
		this.renderedTelaSalvaAutoridade = renderedTelaSalvaAutoridade;
	}

	public Boolean getRenderedTelaAlterar() {
		return renderedTelaAlterar;
	}

	public void setRenderedTelaAlterar(Boolean renderedTelaAlterar) {
		this.renderedTelaAlterar = renderedTelaAlterar;
	}
}
