package br.gov.stf.estf.assinatura.visao.jsf.beans.processamento.mantergrupousuario;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.entidade.usuario.TipoGrupoControle;
import br.gov.stf.estf.entidade.usuario.TipoGrupoControle.FlagTipoGrupoAtivo;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanManterTipoGrupo extends AssinadorBaseBean {

	private static final long serialVersionUID = 2978820334038817120L;

	// ################### VARIAVEIS DE SESSAO ###########################

	private static final Object TIPO_GRUPO_CONTROLE = new Object();
	private static final Object LISTA_TIPO_GRUPO_USUARIO = new Object();
	private static final Object RENDERED_BOTAO_ALTERA = new Object();
	private static final Object RENDERED_TELA_NOVO_GRUPO = new Object();

	// #################### DECLARAÇÃO VARIÁVEIS #########################

	private String descricaoDoGrupo;
	private String dscTipoGrupoCad;
	private String dscConsultaComplementoCad;
	private TipoGrupoControle tipoGrupoControle;
	private List<CheckableDataTableRowWrapper> listaTipoGrupoControle;
	private HtmlDataTable tabelaTipoGrupoControle;
	private Boolean renderedBotaoAlteraTipoGrupoControle;
	private Boolean renderedTelaNovoGrupoControle;

	// ####################### SESSÃO #################################

	public BeanManterTipoGrupo() {
		restaurarSessao();
	}

	public void restaurarSessao() {

		if (getAtributo(TIPO_GRUPO_CONTROLE) == null) {
			setAtributo(TIPO_GRUPO_CONTROLE, new TipoGrupoControle());
		}
		setTipoGrupoControle((TipoGrupoControle) getAtributo(TIPO_GRUPO_CONTROLE));

		if (getAtributo(RENDERED_TELA_NOVO_GRUPO) == null) {
			setAtributo(RENDERED_TELA_NOVO_GRUPO, false);
		}
		setRenderedTelaNovoGrupoControle((Boolean) getAtributo(RENDERED_TELA_NOVO_GRUPO));

		if (getAtributo(RENDERED_BOTAO_ALTERA) == null) {
			setAtributo(RENDERED_BOTAO_ALTERA, false);
		}
		setRenderedBotaoAlteraTipoGrupoControle((Boolean) getAtributo(RENDERED_BOTAO_ALTERA));

		setListaTipoGrupoControle((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_TIPO_GRUPO_USUARIO));
	}

	public void atualizarSessao() {
		setAtributo(TIPO_GRUPO_CONTROLE, tipoGrupoControle);
		setAtributo(LISTA_TIPO_GRUPO_USUARIO, listaTipoGrupoControle);
		setAtributo(RENDERED_TELA_NOVO_GRUPO, renderedTelaNovoGrupoControle);
		setAtributo(RENDERED_BOTAO_ALTERA, renderedBotaoAlteraTipoGrupoControle);
	}

	// #################### ACTIONS #######################################

	public void pesquisarTipoGrupoControleAction(ActionEvent evt) {
		pesquisarTipoGrupoControle();
		atualizarSessao();
	}

	public void renderizaTelaNovoGrupoAction(ActionEvent evt) {
		setRenderedTelaNovoGrupoControle(true);
		setRenderedBotaoAlteraTipoGrupoControle(false);
		setDscConsultaComplementoCad(null);
		setDscTipoGrupoCad(null);
		atualizarSessao();
	}

	public void limparTelaAction(ActionEvent evt) {
		limparTela();
		atualizarSessao();
	}

	public void fecharTelaAction(ActionEvent evt) {
		limparTela();
		atualizarSessao();
	}

	public void salvaDadosAlteradosTipoGrupoControleAction(ActionEvent evt) {
		salvaDadosAlteradosTipoGrupoControle();
		atualizarSessao();
	}

	public void salvaNovoTipoGrupoControleAction(ActionEvent evt) {
		salvaNovoTipoGrupoControle();
		atualizarSessao();
	}

	// #################### METHODS #######################################

	public void pesquisarTipoGrupoControle() {
		List<TipoGrupoControle> lista = new ArrayList<TipoGrupoControle>();

		try {
			lista = getTipoGrupoControleService().pesquisarTipoGrupoControle(descricaoDoGrupo);

		} catch (Exception e) {
			e.printStackTrace();
			reportarErro("Erro ao pesquisar os grupos.");
		}

		if (lista != null && lista.size() > 0) {
			setListaTipoGrupoControle(getCheckableDataTableRowWrapperList(lista));
		} else {
			reportarAviso("Nenhum registro encontrado para o parâmetro informado.");
		}

	}

	/**
	 * Altera os dados de um grupo já criado
	 */
	public void salvaDadosAlteradosTipoGrupoControle() {
		if ((dscTipoGrupoCad == null && dscTipoGrupoCad.trim().length() == 0) || dscConsultaComplementoCad == null
				&& dscConsultaComplementoCad.trim().length() == 0) {
			reportarAviso("Preecha o campos de nome e complemento Query.");
			return;
		} else {
			tipoGrupoControle.setDscTipoGrupoControle(dscTipoGrupoCad);
			tipoGrupoControle.setDscConsultaComplemento(dscConsultaComplementoCad);
			try {
				getTipoGrupoControleService().salvar(tipoGrupoControle);
			} catch (ServiceException e) {
				e.printStackTrace();
				reportarErro("Erro ao alterar os dados.");
				return;
			}
		}
		reportarAviso("Dados alterados com sucesso.");
		limparTela();
	}

	/**
	 * Salva um novo grupo de usuário
	 */
	public void salvaNovoTipoGrupoControle() {
		TipoGrupoControle tipoGrupoControleNovo = new TipoGrupoControle();
		if ((dscTipoGrupoCad == null && dscTipoGrupoCad.trim().length() == 0)
				|| (dscConsultaComplementoCad == null && dscConsultaComplementoCad.trim().length() == 0)) {
			reportarAviso("Descrição ou o complemento devem ser informados.");
			return;
		} else {
			try {
				tipoGrupoControleNovo.setFlagTipoGrupoControleAtivo(FlagTipoGrupoAtivo.S);
				tipoGrupoControleNovo.setDscConsultaComplemento(dscConsultaComplementoCad);
				tipoGrupoControleNovo.setDscTipoGrupoControle(dscTipoGrupoCad);
				getTipoGrupoControleService().salvar(tipoGrupoControleNovo);
			} catch (ServiceException e) {
				e.printStackTrace();
				reportarErro("Erro ao incluir o grupo de usuário");
				return;
			}
			reportarInformacao("Operação realizada com sucesso.");
			limparTela();
		}
		atualizarSessao();
	}

	public void limparTela() {
		setDescricaoDoGrupo(null);
		setListaTipoGrupoControle(null);
		setTipoGrupoControle(new TipoGrupoControle());
		setRenderedTelaNovoGrupoControle(false);
		setRenderedBotaoAlteraTipoGrupoControle(false);
	}

	// #################### GET AND SETS ##################################

	public TipoGrupoControle getTipoGrupoControle() {
		return tipoGrupoControle;
	}

	public void setTipoGrupoControle(TipoGrupoControle tipoGrupoControle) {
		this.tipoGrupoControle = tipoGrupoControle;
	}

	public List<CheckableDataTableRowWrapper> getListaTipoGrupoControle() {
		return listaTipoGrupoControle;
	}

	public void setListaTipoGrupoControle(List<CheckableDataTableRowWrapper> listaTipoGrupoControle) {
		this.listaTipoGrupoControle = listaTipoGrupoControle;
	}

	public HtmlDataTable getTabelaTipoGrupoControle() {
		return tabelaTipoGrupoControle;
	}

	public void setTabelaTipoGrupoControle(HtmlDataTable tabelaTipoGrupoControle) {
		this.tabelaTipoGrupoControle = tabelaTipoGrupoControle;
	}

	public Boolean getRenderedBotaoAlteraTipoGrupoControle() {
		return renderedBotaoAlteraTipoGrupoControle;
	}

	public void setRenderedBotaoAlteraTipoGrupoControle(Boolean renderedBotaoAlteraTipoGrupoControle) {
		this.renderedBotaoAlteraTipoGrupoControle = renderedBotaoAlteraTipoGrupoControle;
	}

	public Boolean getRenderedTelaNovoGrupoControle() {
		return renderedTelaNovoGrupoControle;
	}

	public void setRenderedTelaNovoGrupoControle(Boolean renderedTelaNovoGrupoControle) {
		this.renderedTelaNovoGrupoControle = renderedTelaNovoGrupoControle;
	}

	public String getDescricaoDoGrupo() {
		return descricaoDoGrupo;
	}

	public void setDescricaoDoGrupo(String descricaoDoGrupo) {
		this.descricaoDoGrupo = descricaoDoGrupo;
	}

	public String getDscTipoGrupoCad() {
		return dscTipoGrupoCad;
	}

	public void setDscTipoGrupoCad(String dscTipoGrupoCad) {
		this.dscTipoGrupoCad = dscTipoGrupoCad;
	}

	public String getDscConsultaComplementoCad() {
		return dscConsultaComplementoCad;
	}

	public void setDscConsultaComplementoCad(String dscConsultaComplementoCad) {
		this.dscConsultaComplementoCad = dscConsultaComplementoCad;
	}

}
