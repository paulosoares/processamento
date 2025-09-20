package br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.mantergrupousuario;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import br.gov.stf.estf.assinatura.security.UsuarioAssinatura;
import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.entidade.usuario.TipoGrupoControle;
import br.gov.stf.estf.entidade.usuario.TipoGrupoUsuarioControle;
import br.gov.stf.estf.entidade.usuario.TipoGrupoUsuarioControle.FlagGenericaGrupoUsuario;
import br.gov.stf.estf.entidade.usuario.UsuarioEGab;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.util.Constants;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanManterTipoGrupoUsuario extends AssinadorBaseBean {

	private static final long serialVersionUID = -436926252707233612L;

	// ################### VARIAVEIS DE SESSAO ###########################

	private static final Object LISTA_TODOS_GRUPOS = new Object();
	private static final Object LISTA_GRUPO_SELECIONADO = new Object();
	private static final Object LISTA_USUARIO = new Object();
	private static final Object NOME_USUARIO = new Object();
	private static final Object USUARIO_SELECIONADO = new Object();
	private static final Object RENDERED_MOSTRA_GRUPO_USUARIO = new Object();
	private static final Object RENDERED_TABELA_USUARIO = new Object();

	// #################### DECLARAÇÃO VARIÁVEIS #########################

	private String inputNomeUsuarioNovoAltera;
	private UsuarioAssinatura usuario;
	private UsuarioEGab usuarioU;

	private List<TipoGrupoControle> listaGrupoUsuarioSelecionado;
	private List<SelectItem> listaTodosGrupos;
	private List<CheckableDataTableRowWrapper> listaDeUsuarios;
	private HtmlDataTable tabelaDeUsuario;

	private Boolean renderedMostraGruposDoUsuario;
	private Boolean renderedTabelaUsuario;

	// ####################### SESSÃO #################################

	public BeanManterTipoGrupoUsuario() {
		restaurarSessao();
	}

	@SuppressWarnings("unchecked")
	public void restaurarSessao() {

		usuario = (UsuarioAssinatura) getUser();

		if (usuario.getAtivo()) {
			setSessionValue(Constants.SECURITY_ENABLED_KEY, Boolean.TRUE);
			setSessionValue(Constants.USER_KEY, usuario);
		}

		if (getAtributo(LISTA_TODOS_GRUPOS) == null) {
			setAtributo(LISTA_TODOS_GRUPOS, new ArrayList<SelectItem>());
		}
		setListaTodosGrupos((List<SelectItem>) getAtributo(LISTA_TODOS_GRUPOS));

		if (getAtributo(LISTA_GRUPO_SELECIONADO) == null) {
			setAtributo(LISTA_GRUPO_SELECIONADO, new ArrayList<TipoGrupoControle>());
		}
		setListaGrupoUsuarioSelecionado((List<TipoGrupoControle>) getAtributo(LISTA_GRUPO_SELECIONADO));

		if (getAtributo(USUARIO_SELECIONADO) == null) {
			setAtributo(USUARIO_SELECIONADO, new UsuarioEGab());
		}
		setUsuarioU((UsuarioEGab) getAtributo(USUARIO_SELECIONADO));

		if (getAtributo(NOME_USUARIO) == null) {
			setAtributo(NOME_USUARIO, "");
		}
		setInputNomeUsuarioNovoAltera((String) getAtributo(NOME_USUARIO));

		if (getAtributo(RENDERED_MOSTRA_GRUPO_USUARIO) == null) {
			setAtributo(RENDERED_MOSTRA_GRUPO_USUARIO, false);
		}
		setRenderedMostraGruposDoUsuario((Boolean) getAtributo(RENDERED_MOSTRA_GRUPO_USUARIO));

		if (getAtributo(RENDERED_TABELA_USUARIO) == null) {
			setAtributo(RENDERED_TABELA_USUARIO, false);
		}
		setRenderedTabelaUsuario((Boolean) getAtributo(RENDERED_TABELA_USUARIO));

		setListaDeUsuarios((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_USUARIO));
	}

	public void atualizarSessao() {
		setAtributo(LISTA_TODOS_GRUPOS, listaTodosGrupos);
		setAtributo(LISTA_USUARIO, listaDeUsuarios);
		setAtributo(LISTA_GRUPO_SELECIONADO, listaGrupoUsuarioSelecionado);
		setAtributo(RENDERED_MOSTRA_GRUPO_USUARIO, renderedMostraGruposDoUsuario);
		setAtributo(USUARIO_SELECIONADO, usuarioU);
		setAtributo(NOME_USUARIO, inputNomeUsuarioNovoAltera);
		setAtributo(RENDERED_TABELA_USUARIO, renderedTabelaUsuario);
	}

	// ####################### ACTIONS #################################

	public void limparTelaAction(ActionEvent evt) {
		limparTela();
		atualizarSessao();
	}

	public void fecharCaixaSelecaoGrupoUsuarioAction(ActionEvent evt) {
		limparTela();
		pesquisaUsuariosDoSetor();
		atualizarSessao();
	}

	// ####################### METHODS #################################

	public void pesquisaUsuariosDoSetor() {

		List<UsuarioEGab> listaU = new LinkedList<UsuarioEGab>();

		try {
			getUsuarioService().limparSessao();
			listaU = getUsuarioService().pesquisarUsuarios(usuario.getSetor().getId(), null, true);
		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar o usuário.");
			e.printStackTrace();
		}

		if (listaU == null || listaU.size() == 0) {
			reportarAviso("Nenhum usuário encontrado");
			return;
		}
		setRenderedTabelaUsuario(true);
		setListaDeUsuarios(getCheckableDataTableRowWrapperList(listaU));
		atualizarSessao();
	}

	/**
	 * Método responsável em alterar os dados do modelo
	 */
	public void mostraGruposDoUsuario() {

		usuarioU = (UsuarioEGab) ((CheckableDataTableRowWrapper) tabelaDeUsuario.getRowData()).getWrappedObject();
		setInputNomeUsuarioNovoAltera(usuarioU.getUsuario().getNome());

		if (usuario != null) {
			setRenderedMostraGruposDoUsuario(true);
		}

		try {
			listaGrupoUsuarioSelecionado = getTipoGrupoUsuarioControleService().buscaGruposDoUsuario(usuarioU.getUsuario().getId());
		} catch (ServiceException e) {
			reportarErro("Erro ao pesquisar os grupos do usuário.");
			e.printStackTrace();
		}

		listaTodosGrupos = carregaComboTipoGrupoControle();
		atualizarSessao();
	}

	/**
	 * Salva o grupo usuário.
	 */
	public void salvarGrupoDoUsuario() {

		List<TipoGrupoControle> listaGrupoControleInclusao = new LinkedList<TipoGrupoControle>();
		List<TipoGrupoControle> listaGrupoControlePickList = new LinkedList<TipoGrupoControle>();
		List<TipoGrupoControle> listaTransformaStringGrupoControle = new LinkedList<TipoGrupoControle>();

		if (listaGrupoUsuarioSelecionado != null && listaGrupoUsuarioSelecionado.size() > 0) {
			listaGrupoControlePickList.addAll(listaGrupoUsuarioSelecionado);
			for (int i = 0; i < listaGrupoControlePickList.size(); i++) {
				Object linhaControleInclusaoOb = null;
				linhaControleInclusaoOb = listaGrupoControlePickList.get(i);
				if (linhaControleInclusaoOb instanceof String) {
					int indice = 0;
					String linhaControleInclusaoString = null;
					String id = null;
					linhaControleInclusaoString = (String) linhaControleInclusaoOb;
					indice = linhaControleInclusaoString.lastIndexOf("id");
					id = linhaControleInclusaoString.substring(indice + 4, linhaControleInclusaoString.length());
					TipoGrupoControle tipo = new TipoGrupoControle();
					try {
						tipo = getTipoGrupoControleService().recuperarPorId(Long.parseLong(id));
					} catch (ServiceException e) {
						e.printStackTrace();
					}
					listaTransformaStringGrupoControle.add(tipo);
				}
			}
		}

		// foi criada a listaGrupoControleInclusao para não ocorrer erro no momento de
		// remover o item da lista no FOR da listaTransformaStringGrupoControle.
		listaGrupoControleInclusao.addAll(listaTransformaStringGrupoControle);

		if ((listaTransformaStringGrupoControle != null && listaTransformaStringGrupoControle.size() > 0)
				|| (usuarioU.getUsuario().getListaGrupoUsuarioControle() != null && usuarioU.getUsuario().getListaGrupoUsuarioControle().size() > 0)) {
			// varre todos os grupos encontrados na base para o usuário
			for (TipoGrupoUsuarioControle tipoGUC : usuarioU.getUsuario().getListaGrupoUsuarioControle()) {
				boolean ehIgual = false;
				// procura se existe no quadro de selecionados um item igual aos já armazenados na
				// base
				for (TipoGrupoControle tp : listaTransformaStringGrupoControle) {
					// caso exista será excluido da lista de itens que será armazenado na base,
					// significa que o
					// usuário acrescentou novos itens no quadro
					if (tipoGUC.getTipoGrupoControle().equals(tp)) {
						ehIgual = true;
						listaGrupoControleInclusao.remove(tp);
					}
				}
				// depois de percorrida a lista do quadro e não encontrar nenhum item, significa que
				// o
				// usuário retirou do quadro o grupo já cadastrado na base
				if (!ehIgual) {
					try {
						// agora é realizada a exclusão física do grupo
						getTipoGrupoUsuarioControleService().excluir(tipoGUC);
					} catch (Exception e) {
						e.printStackTrace();
						reportarErro("Erro ao apagar o grupo do usuário");
						return;
					}
				}
			}
			// se a lista de inclusão possuir itens, eles serão salvos na base
			if (listaGrupoControleInclusao != null && listaGrupoControleInclusao.size() > 0) {
				for (TipoGrupoControle tgc : listaGrupoControleInclusao) {
					TipoGrupoUsuarioControle grupoUsuario = new TipoGrupoUsuarioControle();
					grupoUsuario.setTipoGrupoControle(tgc);
					grupoUsuario.setUsuario(usuarioU.getUsuario());
					grupoUsuario.setFlagAtivo(FlagGenericaGrupoUsuario.S);
					try {
						getTipoGrupoUsuarioControleService().salvar(grupoUsuario);
					} catch (ServiceException e) {
						e.printStackTrace();
						reportarErro("Erro ao salvar o " + grupoUsuario.getTipoGrupoControle().getDscTipoGrupoControle());
						return;
					}
				}
				limparTela();
				pesquisaUsuariosDoSetor();
				reportarAviso("Grupo(s) salvo(s) com sucesso.");
			}
		} else {
			reportarAviso("Nenhum grupo selecionado.");
		}

	}

	/**
	 * Método responsável em limpar a tela do usuário
	 */
	public void limparTela() {
		setListaDeUsuarios(null);
		setRenderedMostraGruposDoUsuario(false);
		setRenderedTabelaUsuario(false);
		setListaGrupoUsuarioSelecionado(new ArrayList<TipoGrupoControle>());
		setListaTodosGrupos(new ArrayList<SelectItem>());
		setInputNomeUsuarioNovoAltera("");
	}

	// ####################### GET AND SETS #################################

	public UsuarioEGab getUsuarioU() {
		return usuarioU;
	}

	public void setUsuarioU(UsuarioEGab usuarioU) {
		this.usuarioU = usuarioU;
	}

	public HtmlDataTable getTabelaDeUsuario() {
		return tabelaDeUsuario;
	}

	public void setTabelaDeUsuario(HtmlDataTable tabelaDeUsuario) {
		this.tabelaDeUsuario = tabelaDeUsuario;
	}

	public List<SelectItem> getListaTodosGrupos() {
		return listaTodosGrupos;
	}

	public void setListaTodosGrupos(List<SelectItem> listaTodosGrupos) {
		this.listaTodosGrupos = listaTodosGrupos;
	}

	public Boolean getRenderedMostraGruposDoUsuario() {
		return renderedMostraGruposDoUsuario;
	}

	public void setRenderedMostraGruposDoUsuario(Boolean renderedMostraGruposDoUsuario) {
		this.renderedMostraGruposDoUsuario = renderedMostraGruposDoUsuario;
	}

	public String getInputNomeUsuarioNovoAltera() {
		return inputNomeUsuarioNovoAltera;
	}

	public void setInputNomeUsuarioNovoAltera(String inputNomeUsuarioNovoAltera) {
		this.inputNomeUsuarioNovoAltera = inputNomeUsuarioNovoAltera;
	}

	public List<CheckableDataTableRowWrapper> getListaDeUsuarios() {
		return listaDeUsuarios;
	}

	public void setListaDeUsuarios(List<CheckableDataTableRowWrapper> listaDeUsuarios) {
		this.listaDeUsuarios = listaDeUsuarios;
	}

	public List<TipoGrupoControle> getListaGrupoUsuarioSelecionado() {
		return listaGrupoUsuarioSelecionado;
	}

	public void setListaGrupoUsuarioSelecionado(List<TipoGrupoControle> listaGrupoUsuarioSelecionado) {
		this.listaGrupoUsuarioSelecionado = listaGrupoUsuarioSelecionado;
	}

	public Boolean getRenderedTabelaUsuario() {
		return renderedTabelaUsuario;
	}

	public void setRenderedTabelaUsuario(Boolean renderedTabelaUsuario) {
		this.renderedTabelaUsuario = renderedTabelaUsuario;
	}

	public UsuarioAssinatura getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioAssinatura usuario) {
		this.usuario = usuario;
	}

}
