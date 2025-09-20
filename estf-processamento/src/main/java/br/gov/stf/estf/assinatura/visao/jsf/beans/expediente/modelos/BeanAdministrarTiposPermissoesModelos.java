package br.gov.stf.estf.assinatura.visao.jsf.beans.expediente.modelos;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.documento.model.service.ModeloComunicacaoService;
import br.gov.stf.estf.documento.model.service.TipoPermissaoModeloComunicacaoService;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoPermissaoModeloComunicacao;
import br.gov.stf.estf.entidade.localizacao.Setor;
import br.gov.stf.estf.localizacao.model.service.SetorService;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanAdministrarTiposPermissoesModelos extends AssinadorBaseBean {

	private static final long serialVersionUID = 3790885446684121261L;

	// ----------------- ATRIBUTOS DE SESSÃO -------------------

	private static final Object PERMISSAO = new Object();
	private static final Object LISTA_PERMISSOES = new Object();
	private static final Object LISTA_SETORES = new Object();
	private static final Object PERMISSAO_TEMPORARIA = new Object();

	private static final Object RENDERED_TELA_ALTERAR = new Object();
	private static final Object RENDERED_TELA_SALVAR = new Object();

	// ----------------------- ATRIBUTOS -----------------------

	private String descricaoPermissaoPesquisa;

	private TipoPermissaoModeloComunicacao tipoPermissao;
	private String descricaoPermissao;
	private Long codigoSetor;

	private List<CheckableDataTableRowWrapper> listaPermissoesModelos;
	private HtmlDataTable tabelaPermissoesModelos;
	private CheckableDataTableRowWrapper tipoPermissaoTemporaria;

	private List<SelectItem> listaSetores;

	private Boolean renderedTelaSalvar;
	private Boolean renderedTelaAlterar;

	public BeanAdministrarTiposPermissoesModelos() {
		restaurarSessao();
	}

	// ------------------------- SESSÃO -------------------------

	@SuppressWarnings("unchecked")
	public void restaurarSessao() {
		if (getAtributo(PERMISSAO) == null) {
			setAtributo(PERMISSAO, new TipoPermissaoModeloComunicacao());
		}
		setTipoPermissao((TipoPermissaoModeloComunicacao) getAtributo(PERMISSAO));

		setTipoPermissaoTemporaria((CheckableDataTableRowWrapper) getAtributo(PERMISSAO_TEMPORARIA));

		if (getAtributo(LISTA_SETORES) == null) {
			setAtributo(LISTA_SETORES, carregarListaSetores());
		}
		setListaSetores((List<SelectItem>) getAtributo(LISTA_SETORES));

		if (getAtributo(RENDERED_TELA_ALTERAR) == null) {
			setAtributo(RENDERED_TELA_ALTERAR, Boolean.FALSE);
		}
		setRenderedTelaAlterar((Boolean) getAtributo(RENDERED_TELA_ALTERAR));

		if (getAtributo(RENDERED_TELA_SALVAR) == null) {
			setAtributo(RENDERED_TELA_SALVAR, Boolean.FALSE);
		}
		setRenderedTelaSalvar((Boolean) getAtributo(RENDERED_TELA_SALVAR));

		setListaPermissoesModelos((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_PERMISSOES));
	}

	public void atualizarSessao() {
		setAtributo(PERMISSAO, tipoPermissao);
		setAtributo(PERMISSAO_TEMPORARIA, tipoPermissaoTemporaria);
		setAtributo(LISTA_PERMISSOES, listaPermissoesModelos);
		setAtributo(LISTA_SETORES, listaSetores);
		setAtributo(RENDERED_TELA_ALTERAR, renderedTelaAlterar);
		setAtributo(RENDERED_TELA_SALVAR, renderedTelaSalvar);
	}

	private List<SelectItem> carregarListaSetores() {
		List<SelectItem> listaSetores = new ArrayList<SelectItem>();

		try {
			SetorService setorService = getSetorService();
			List<Setor> setores = setorService.pesquisarSetoresEGab();

			listaSetores.add(new SelectItem(null, ""));

			for (Setor setor : setores) {
				listaSetores.add(new SelectItem(setor.getId(), setor.getNome()
						+ " - " + setor.getId()));
			}
		} catch (ServiceException exception) {
			exception.printStackTrace();
			reportarErro("Erro ao recuperar setores.",
					exception.getLocalizedMessage());
		}

		return listaSetores;
	}

	// ------------------------- ACTIONS ------------------------

	public void pesquisarPermissoesAction(ActionEvent evento) {
		pesquisarPermissoes();
		atualizarSessao();
	}

	public void salvarPermissaoAction(ActionEvent evento) {
		persistirPermissaoModelos();
		limparCampos();
		atualizarSessao();
	}

	public void removerPermissaoAction(ActionEvent evento) {
		removerPermissao();
		limparCampos();
		atualizarSessao();
	}

	public void limparTelaAction(ActionEvent evento) {
		limparTela();
	}

	public void mostrarTelaAlteracaoAction() {
		alterarPermissao();
		// setListaPermissoesModelos(null);
		atualizarSessao();
	}

	public void mostrarTelaSalvarAction(ActionEvent event) {
		novaPermissao();
		atualizarSessao();
	}

	public void fecharTelaAlteracaoAction(ActionEvent event) {
		setRenderedTelaAlterar(Boolean.FALSE);
		setRenderedTelaSalvar(Boolean.FALSE);
		setTipoPermissao(new TipoPermissaoModeloComunicacao());
		atualizarSessao();
	}

	public void armazenarLinhaSelecionada() {
		tipoPermissaoTemporaria = (CheckableDataTableRowWrapper) tabelaPermissoesModelos
				.getRowData();
		atualizarSessao();
	}

	// ------------------------- MÉTODOS ------------------------

	private void limparTela() {
		setRenderedTelaAlterar(Boolean.FALSE);
		setRenderedTelaSalvar(Boolean.FALSE);
		limparCampos();
		setDescricaoPermissaoPesquisa("");
		setListaPermissoesModelos(null);
		atualizarSessao();
	}

	private void limparCampos() {
		setTipoPermissao(new TipoPermissaoModeloComunicacao());
		setDescricaoPermissao("");
		setCodigoSetor(null);
	}

	@SuppressWarnings({ "deprecation" })
	private void pesquisarPermissoes() {
		TipoPermissaoModeloComunicacaoService permissaoService = getTipoPermissaoModeloComunicacaoService();

		try {
			List<TipoPermissaoModeloComunicacao> permissoes = permissaoService
					.pesquisarPermissoes(getDescricaoPermissaoPesquisa(),
							Boolean.FALSE);

			if (CollectionUtils.isEmpty(permissoes)) {
				reportarAviso("Nenhuma permissão encontrada.");
				return;
			}

			setListaPermissoesModelos(getCheckableDataTableRowWrapperList(permissoes));
		} catch (ServiceException exception) {
			exception.printStackTrace();
			reportarErro("Erro ao pesquisar permissões.",
					exception.getLocalizedMessage());
		}
	}

	@SuppressWarnings("deprecation")
	private void persistirPermissaoModelos() {
		TipoPermissaoModeloComunicacaoService permissaoService = getTipoPermissaoModeloComunicacaoService();

		try {
			if (StringUtils.isEmpty(getDescricaoPermissao())) {
				reportarAviso("O nome da permissão deve ser especificado.");
			} else {
				tipoPermissao.setDescricao(getDescricaoPermissao());
				tipoPermissao.setSetor(carregarSetor());

				if (getRenderedTelaAlterar()) {
					// alterando
					permissaoService.alterar(tipoPermissao);
					reportarInformacao("Tipo de permissão alterado com sucesso.");
				} else {
					// criando
					List<TipoPermissaoModeloComunicacao> tiposExistentes = permissaoService
							.pesquisarPermissoes(tipoPermissao.getDescricao(),
									Boolean.TRUE);
					if (CollectionUtils.isEmpty(tiposExistentes)) {
						permissaoService.incluir(tipoPermissao);
						reportarInformacao("Tipo de permissão criado com sucesso.");
					} else {
						reportarErro("O nome informado já está sendo utilizado!");
					}
				}
			}
		} catch (ServiceException exception) {
			exception.printStackTrace();
			reportarErro("Erro ao criar permissão.",
					exception.getLocalizedMessage());
		}

	}

	@SuppressWarnings("deprecation")
	private void removerPermissao() {
		TipoPermissaoModeloComunicacaoService permissaoService = getTipoPermissaoModeloComunicacaoService();
		ModeloComunicacaoService modeloComunicacaoService = getModeloComunicacaoService();
		if (tipoPermissaoTemporaria == null) {
			reportarAviso("Selecione pelo menos uma permissão para ser excluída!");
			return;
		}

		try {
			TipoPermissaoModeloComunicacao tipoPermissaoSelecionada = (TipoPermissaoModeloComunicacao) tipoPermissaoTemporaria
					.getWrappedObject();

			// caso exista algum tipo de modelo com o tipo de permissão a ser
			// excluido o sistema deverá avisar que primeiro o usuário deverá
			// alterar os modelos de documentos para outro tipo de permissão
			// antes de remover o tipo selecionado
			List<ModeloComunicacao> listaModelos = modeloComunicacaoService
					.pesquisar(null, null, tipoPermissaoSelecionada.getId(), "S");

			if (!CollectionUtils.isEmpty(listaModelos)) {
				reportarAviso("Existe(m) tipo(s) de modelo(s) criado(s) com esta permissão. "
						+ "Será necessário primeiro alterá-lo(s) para então remover a permissão.");
				return;
			}

			permissaoService.excluir(tipoPermissaoSelecionada);
			listaPermissoesModelos.remove(tipoPermissaoTemporaria);

			reportarAviso("Permissão excluída com sucesso!");
		} catch (ServiceException exception) {
			exception.printStackTrace();
			reportarErro("Erro ao remover permissão.",
					exception.getLocalizedMessage());
		}
	}

	private void alterarPermissao() {
		setDescricaoPermissao(getTipoPermissao().getDescricao());
		setCodigoSetor(getTipoPermissao().getSetor() != null ? getTipoPermissao()
				.getSetor().getId() : null);

		setRenderedTelaAlterar(Boolean.TRUE);
		setRenderedTelaSalvar(Boolean.FALSE);
	}

	private void novaPermissao() {
		setTipoPermissao(new TipoPermissaoModeloComunicacao());
		setDescricaoPermissao(getDescricaoPermissaoPesquisa());
		setCodigoSetor(null);

		setRenderedTelaAlterar(Boolean.FALSE);
		setRenderedTelaSalvar(Boolean.TRUE);
	}

	private Setor carregarSetor() throws ServiceException {
		Setor setor = null;

		if (getCodigoSetor() != null) {
			SetorService setorService = getSetorService();
			setor = setorService.recuperarPorId(codigoSetor);
		}

		return setor;
	}

	// -------------------- GETTERS & SETTERS -------------------

	public List<CheckableDataTableRowWrapper> getListaPermissoesModelos() {
		return listaPermissoesModelos;
	}

	public void setListaPermissoesModelos(
			List<CheckableDataTableRowWrapper> listaPermissoesModelos) {
		this.listaPermissoesModelos = listaPermissoesModelos;
	}

	public String getDescricaoPermissaoPesquisa() {
		return descricaoPermissaoPesquisa;
	}

	public void setDescricaoPermissaoPesquisa(String descricaoPermissaoPesquisa) {
		this.descricaoPermissaoPesquisa = descricaoPermissaoPesquisa;
	}

	public TipoPermissaoModeloComunicacao getTipoPermissao() {
		return tipoPermissao;
	}

	public void setTipoPermissao(TipoPermissaoModeloComunicacao tipoPermissao) {
		this.tipoPermissao = tipoPermissao;
	}

	public String getDescricaoPermissao() {
		return descricaoPermissao;
	}

	public void setDescricaoPermissao(String descricaoPermissao) {
		this.descricaoPermissao = descricaoPermissao;
	}

	public Long getCodigoSetor() {
		return codigoSetor;
	}

	public void setCodigoSetor(Long codigoSetor) {
		this.codigoSetor = codigoSetor;
	}

	public Boolean getRenderedTelaSalvar() {
		return renderedTelaSalvar;
	}

	public void setRenderedTelaSalvar(Boolean renderedTelaSalvar) {
		this.renderedTelaSalvar = renderedTelaSalvar;
	}

	public Boolean getRenderedTelaAlterar() {
		return renderedTelaAlterar;
	}

	public void setRenderedTelaAlterar(Boolean renderedTelaAlterar) {
		this.renderedTelaAlterar = renderedTelaAlterar;
	}

	public HtmlDataTable getTabelaPermissoesModelos() {
		return tabelaPermissoesModelos;
	}

	public void setTabelaPermissoesModelos(HtmlDataTable tabelaPermissoesModelos) {
		this.tabelaPermissoesModelos = tabelaPermissoesModelos;
	}

	public CheckableDataTableRowWrapper getTipoPermissaoTemporaria() {
		return tipoPermissaoTemporaria;
	}

	public void setTipoPermissaoTemporaria(
			CheckableDataTableRowWrapper tipoPermissaoTemporaria) {
		this.tipoPermissaoTemporaria = tipoPermissaoTemporaria;
	}

	public List<SelectItem> getListaSetores() {
		return listaSetores;
	}

	public void setListaSetores(List<SelectItem> listaSetores) {
		this.listaSetores = listaSetores;
	}
}
