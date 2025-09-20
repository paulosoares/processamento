package br.gov.stf.estf.assinatura.visao.jsf.beans.expediente.modelos;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.richfaces.component.html.HtmlDataTable;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.assinatura.visao.util.commons.CollectionUtils;
import br.gov.stf.estf.assinatura.visao.util.commons.StringUtils;
import br.gov.stf.estf.documento.model.service.ModeloComunicacaoService;
import br.gov.stf.estf.documento.model.service.TipoComunicacaoService;
import br.gov.stf.estf.entidade.documento.ModeloComunicacao;
import br.gov.stf.estf.entidade.documento.TipoComunicacao;
import br.gov.stf.framework.model.entity.Flag;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanAdministrarTiposModelos extends AssinadorBaseBean {

	private static final long serialVersionUID = 8617916090095868937L;

	// ################### VARIAVEIS DE SESSAO ###########################

	private static final Object TIPO_COMUNICACAO = new Object();
	private static final Object RENDERED_BOTAO_SALVAR = new Object();
	private static final Object RENDERED_BOTAO_ALTERAR = new Object();
	private static final Object ITENS_TIPO_PERMISSAO = new Object();
	private static final Object LISTA_TIPO_COMUNICACAO = new Object();

	// ###################################################################

	// #################### DECLARAÇÃO VARIÁVEIS #########################

	private TipoComunicacao tipocomunicacao;
	private Boolean renderedBotaoSalvar;
	private Boolean renderedBotaoAlterar;
	private List<SelectItem> itensTipoPermissao;

	private List<CheckableDataTableRowWrapper> listaTipoComunicacao;
	private HtmlDataTable tabelaTipoComunicacao;

	// ###################################################################

	public BeanAdministrarTiposModelos() {
		restaurarSessao();
	}

	@SuppressWarnings("unchecked")
	public void restaurarSessao() {
		if (getAtributo(TIPO_COMUNICACAO) == null) {
			setAtributo(TIPO_COMUNICACAO, new TipoComunicacao());
		}
		setTipocomunicacao((TipoComunicacao) getAtributo(TIPO_COMUNICACAO));

		if (getAtributo(ITENS_TIPO_PERMISSAO) == null) {
			setAtributo(ITENS_TIPO_PERMISSAO, carregarComboTipoPermissao(false, false));
		}
		setItensTipoPermissao((List<SelectItem>) getAtributo(ITENS_TIPO_PERMISSAO));
		
		if (getAtributo(RENDERED_BOTAO_SALVAR) == null) {
			setAtributo(RENDERED_BOTAO_SALVAR, true);
		}
		setRenderedBotaoSalvar((Boolean) getAtributo(RENDERED_BOTAO_SALVAR));

		if (getAtributo(RENDERED_BOTAO_ALTERAR) == null) {
			setAtributo(RENDERED_BOTAO_ALTERAR, false);
		}
		setRenderedBotaoAlterar((Boolean) getAtributo(RENDERED_BOTAO_ALTERAR));
		
		setListaTipoComunicacao((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_TIPO_COMUNICACAO));
	}

	public void atualizarSessao() {
		setAtributo(TIPO_COMUNICACAO, tipocomunicacao);
		setAtributo(LISTA_TIPO_COMUNICACAO, listaTipoComunicacao);
		setAtributo(ITENS_TIPO_PERMISSAO, itensTipoPermissao);
		setAtributo(RENDERED_BOTAO_SALVAR, renderedBotaoSalvar);
		setAtributo(RENDERED_BOTAO_ALTERAR, renderedBotaoAlterar);
	}

	// #################### ACTIONS #######################################

	public void pesquisarTipoModelosAction(ActionEvent action) {
		pesquisarTipoModelos();
		atualizarSessao();
	}

	public void alterarTipoModeloAction(ActionEvent action) {
		setRenderedBotaoSalvar(false);
		setRenderedBotaoAlterar(true);
		alterarTipoModelo();
		atualizarSessao();
	}

	public void criarNovoTipoModelosAction(ActionEvent action) {
		setRenderedBotaoSalvar(true);
		setRenderedBotaoAlterar(false);
		tipocomunicacao = new TipoComunicacao();
		atualizarSessao();
	}
	
	public void persistirTipoTextoAction(ActionEvent action){
		persistirTipoTexto();
		atualizarSessao();
	}

	public void limparTela(ActionEvent evt) {
		novoTipoComunicacao();
		atualizarSessao();
	}

	// ######################### METHODS ##################################

	@SuppressWarnings("deprecation")
	public void pesquisarTipoModelos() {
		try {
			List<TipoComunicacao> lista = new ArrayList<TipoComunicacao>(); 
				try {

					lista = getTipoComunicacaoService().pesquisarListaTiposModelos(null);
				} catch (ServiceException e) {
					e.printStackTrace();
					reportarErro("Erro ao pesquisar os tipos de modelos.");
				}

			if (CollectionUtils.isVazia(lista)) {
				reportarAviso("Nenhum registro encontrado para o parâmetro informado.");
			}

			setListaTipoComunicacao(getCheckableDataTableRowWrapperList(lista));
		} catch (Exception e) {
			e.printStackTrace();
			reportarErro("Erro ao pesquisar", e.getMessage());
		}
	}

	public void alterarTipoModelo() {
		setRenderedBotaoSalvar(false);
		tipocomunicacao = (TipoComunicacao) ((CheckableDataTableRowWrapper) tabelaTipoComunicacao.getRowData()).getWrappedObject();
		atualizarSessao();
	}

	@SuppressWarnings("deprecation")
	public void removerTipoModelo() {
		TipoComunicacaoService tipoComunicacaoService = getTipoComunicacaoService();
		ModeloComunicacaoService modeloComunicacaoService = getModeloComunicacaoService();

		TipoComunicacao tipocomunicacao = (TipoComunicacao) ((CheckableDataTableRowWrapper) tabelaTipoComunicacao.getRowData()).getWrappedObject();

		if (tipocomunicacao == null) {
			reportarAviso("Selecione pelo menos um tipo de modelo.");
			return;
		}

		// caso exista algum modelo com o tipo de modelo a ser excluido o
		// sistema deverá avisar que primeiro o usuário deverá alterar os
		// modelos de documentos para outro tipo antes de remover o tipo de
		// modelo
		List<ModeloComunicacao> listaModelCom = new ArrayList<ModeloComunicacao>();

		try {
			listaModelCom = modeloComunicacaoService.pesquisar(null, tipocomunicacao.getId(), (Long) null, Flag.SIM);
		} catch (ServiceException e) {
			e.printStackTrace();
			reportarErro("Erro ao buscar os modelos com o(s) tipo(s) de modelo(s) selecionado(s).");
			return;
		}

		if (CollectionUtils.isNotVazia(listaModelCom)) {
			reportarAviso("Existe(m)modelo(s) criado(s) com este tipo. " + "Será necessário primeiro alterar o(s) modelo(s).");
			return;
		}

		try {
			tipoComunicacaoService.excluir(tipocomunicacao);

			listaTipoComunicacao.remove(new CheckableDataTableRowWrapper(tipocomunicacao));

			// novoTipoComunicacao();
			reportarAviso("Tipo(s) removido(s) com sucesso!");
		} catch (ServiceException se) {
			se.printStackTrace();
			reportarErro("Erro ao excluir o(s) tipo(s) de modelos.");
		}

		atualizarSessao();
	}

	private void novoTipoComunicacao() {
		setTipocomunicacao(new TipoComunicacao());
		setListaTipoComunicacao(null);
		setRenderedBotaoSalvar(true);
	}

	@SuppressWarnings("deprecation")
	public void persistirTipoTexto() {
		try {
			TipoComunicacaoService tipoComunicacaoService = getTipoComunicacaoService();

			if (StringUtils.isVazia(tipocomunicacao.getDescricao())) {
				reportarAviso("O nome do tipo de modelo deve ser informado");
			} else {

				if (tipocomunicacao.getId() == null) {
					List<TipoComunicacao> tiposExistentes = tipoComunicacaoService.pesquisarListaTiposModelos(tipocomunicacao.getDescricao());
					if (CollectionUtils.isNotVazia(tiposExistentes)) {
						reportarAviso("O tipo de modelo informado já existe!");
						return;
					} else {
						if (tipocomunicacao.getNumeroComunicacaoAnterior() == null){
							tipocomunicacao.setNumeroComunicacaoAnterior(0L);							
						}
						tipoComunicacaoService.incluir(tipocomunicacao);
					}
				} else {
					tipoComunicacaoService.alterar(tipocomunicacao);
				}

				novoTipoComunicacao();
				reportarInformacao("Operação realizada com sucesso.");
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			reportarErro("Erro ao salvar o tipo de modelo.", e.getLocalizedMessage());
		}
		pesquisarTipoModelos();
		atualizarSessao();
	}

	// #################### GET AND SETS ##################################

	public TipoComunicacao getTipocomunicacao() {
		return tipocomunicacao;
	}

	public void setTipocomunicacao(TipoComunicacao tipocomunicacao) {
		this.tipocomunicacao = tipocomunicacao;
	}

	public List<SelectItem> getItensTipoPermissao() {
		return itensTipoPermissao;
	}

	public void setItensTipoPermissao(List<SelectItem> itensTipoPermissao) {
		this.itensTipoPermissao = itensTipoPermissao;
	}

	public List<CheckableDataTableRowWrapper> getListaTipoComunicacao() {
		return listaTipoComunicacao;
	}

	public void setListaTipoComunicacao(List<CheckableDataTableRowWrapper> listaTipoComunicacao) {
		setAtributo(LISTA_TIPO_COMUNICACAO, listaTipoComunicacao);
		this.listaTipoComunicacao = listaTipoComunicacao;
	}

	public HtmlDataTable getTabelaTipoComunicacao() {
		return tabelaTipoComunicacao;
	}

	public void setTabelaTipoComunicacao(HtmlDataTable tabelaTipoComunicacao) {
		this.tabelaTipoComunicacao = tabelaTipoComunicacao;
	}

	public Boolean getRenderedBotaoSalvar() {
		return renderedBotaoSalvar;
	}

	public void setRenderedBotaoSalvar(Boolean renderedBotaoSalvar) {
		this.renderedBotaoSalvar = renderedBotaoSalvar;
	}

	public Boolean getRenderedBotaoAlterar() {
		return renderedBotaoAlterar;
	}

	public void setRenderedBotaoAlterar(Boolean renderedBotaoAlterar) {
		this.renderedBotaoAlterar = renderedBotaoAlterar;
	}
}
