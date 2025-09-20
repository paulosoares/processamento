package br.gov.stf.estf.assinatura.visao.jsf.beans.distribuicao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import br.gov.stf.estf.assinatura.visao.jsf.beans.AssinadorBaseBean;
import br.gov.stf.estf.entidade.ministro.ExclusaoDistribuicao;
import br.gov.stf.estf.entidade.ministro.Ministro;
import br.gov.stf.estf.entidade.processostf.TipoExclusaoDistribuicao;
import br.gov.stf.framework.model.service.ServiceException;
import br.gov.stf.framework.view.jsf.util.CheckableDataTableRowWrapper;

public class BeanExclusaoDistribuicao extends AssinadorBaseBean {
	
	public BeanExclusaoDistribuicao() throws ServiceException{
		preencherListaMinistro();
		preencherListaTipoExclusao();
		restaurarSessao();
	}

	/**
	 * Classe respons�vel pela interface de Exclus�o de Ministros da Distribui��o 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date inicioPeriodo;
	private Date fimPeriodo;
	private List<SelectItem> listaTipoExclusao;
	private Long codigoTipoExclusao; // c�digo do tipo de exclus�o selecionado pelo usu�rio.
	private List<SelectItem> listaMinistro;
	private Long codigoMinistro; // c�digo do ministro selecionado pelo usu�rio.
	private String compensacao;
	private String compensacaoAnterior; // armazena valor antes da altera��o.
	private String prevencao;
	private String observacao;
	private String justificativa;
	private List<CheckableDataTableRowWrapper> listaExclusao;
	private org.richfaces.component.html.HtmlDataTable tabelaExclusao;
	private org.richfaces.component.html.HtmlModalPanel popupEdicaoExclusao;
	private Long idExclusao;
	private Boolean alteracaoParcial;
	private Boolean alteracaoPermitida;
	private Boolean exclusaoPermitida;
	private String iconeAlteracao;
	private String iconeExclusao;
	private String hintIconeExclusao;
	private String hintIconeAlteracao;
	private String mensagemPopup;
	private String remocaoItemConfirmada;
	
	// vari�veis de sess�o
	private static final Object INICIO_PERIODO = new Object();
	private static final Object FIM_PERIODO = new Object();
	private static final Object COMPENSACAO = new Object();
	private static final Object COMPENSACAO_ANTERIOR = new Object();
	private static final Object PREVENCAO = new Object();
	private static final Object OBSERVACAO = new Object();
	private static final Object JUSTIFICATIVA = new Object();
	private static final Object LISTA_TIPO_EXCLUSAO = new Object();
	private static final Object LISTA_MINISTRO = new Object();
	private static final Object LISTA_EXCLUSAO = new Object();
	private static final Object CODIGO_MINISTRO = new Object();
	private static final Object CODIGO_TIPO_EXCLUSAO = new Object();
	private static final Object ID_EXCLUSAO = new Object();
	private static final Object ALTERACAO_PARCIAL = new Object();
	private static final Object ALTERACAO_PERMITIDA = new Object();
	private static final Object EXCLUSAO_PERMITIDA = new Object();
	private static final Object MENSAGEM_POPUP = new Object();

	@SuppressWarnings("unchecked")
	private void restaurarSessao(){
		setMensagemPopup((String) getAtributo(MENSAGEM_POPUP));
		setInicioPeriodo((Date) getAtributo(INICIO_PERIODO));
		setInicioPeriodo((Date) getAtributo(FIM_PERIODO));
		if (getAtributo(COMPENSACAO) == null) {
			setCompensacao("S");
		} else {
			setCompensacao((String) getAtributo(COMPENSACAO));
		}
		if (getAtributo(PREVENCAO) == null) {
			setPrevencao("S");
		} else {
			setPrevencao((String) getAtributo(PREVENCAO));
		}
		setCompensacaoAnterior((String) getAtributo(COMPENSACAO_ANTERIOR));
		setObservacao((String) getAtributo(OBSERVACAO));
		setObservacao((String) getAtributo(JUSTIFICATIVA));
		setCodigoMinistro((Long) getAtributo(CODIGO_MINISTRO));
		setCodigoTipoExclusao((Long) getAtributo(CODIGO_TIPO_EXCLUSAO));
		setListaTipoExclusao((List<SelectItem>) getAtributo(LISTA_TIPO_EXCLUSAO));
		setListaMinistro((List<SelectItem>) getAtributo(LISTA_MINISTRO));
		setListaExclusao((List<CheckableDataTableRowWrapper>) getAtributo(LISTA_EXCLUSAO));
		setIdExclusao((Long) getAtributo(ID_EXCLUSAO));
		if (getAtributo(ALTERACAO_PARCIAL) == null) {
			setAlteracaoParcial(false);
		} else {
			setAlteracaoParcial((Boolean) getAtributo(ALTERACAO_PARCIAL));
		}
		if (getAtributo(ALTERACAO_PERMITIDA) == null) {	
			setAlteracaoPermitida(false);
		} else {
			setAlteracaoPermitida((Boolean) getAtributo(ALTERACAO_PERMITIDA));
		}
		if (getAtributo(EXCLUSAO_PERMITIDA) == null) {
			setExclusaoPermitida(false);
		} else {
			setExclusaoPermitida((Boolean) getAtributo(EXCLUSAO_PERMITIDA));
		}
	}
	
	private void atualizarSessao() {
		setAtributo(MENSAGEM_POPUP, mensagemPopup);
		setAtributo(INICIO_PERIODO, inicioPeriodo);
		setAtributo(FIM_PERIODO, fimPeriodo);
		setAtributo(COMPENSACAO, compensacao);
		setAtributo(COMPENSACAO_ANTERIOR, compensacaoAnterior);
		setAtributo(PREVENCAO, prevencao);
		setAtributo(OBSERVACAO, observacao);
		setAtributo(JUSTIFICATIVA, justificativa);
		setAtributo(LISTA_TIPO_EXCLUSAO, listaTipoExclusao);
		setAtributo(LISTA_MINISTRO, listaMinistro);
		setAtributo(LISTA_EXCLUSAO, listaExclusao);
		setAtributo(CODIGO_MINISTRO, codigoMinistro);
		setAtributo(CODIGO_TIPO_EXCLUSAO, codigoTipoExclusao);
		setAtributo(ID_EXCLUSAO, idExclusao);
		setAtributo(ALTERACAO_PARCIAL, alteracaoParcial);
		setAtributo(ALTERACAO_PERMITIDA, alteracaoPermitida);
		setAtributo(EXCLUSAO_PERMITIDA, alteracaoPermitida);
	}
	
	// preenche a lista de tipos de exclus�o utilizada na combo
	public void preencherListaTipoExclusao() throws ServiceException {
		List<TipoExclusaoDistribuicao> tiposExclusoes = new ArrayList<TipoExclusaoDistribuicao>();
		
		// percorre pelos c�digos de tipos de exclus�o predefinidos no banco: 1, 2, 3 e 4.
		for (int i=1; i<6; i++) {
			TipoExclusaoDistribuicao tipoExclusao = TipoExclusaoDistribuicao.valueOf((long) i);
			tiposExclusoes.add(tipoExclusao);
		}
		tiposExclusoes.remove(TipoExclusaoDistribuicao.PRESIDENTECNJ);
		
		if (listaTipoExclusao == null) {
			listaTipoExclusao = new ArrayList<SelectItem>();
		}
		for (TipoExclusaoDistribuicao tipo : tiposExclusoes) {
			SelectItem selectItem = new SelectItem(tipo.getCodigo(), tipo.getDescricao());
			listaTipoExclusao.add(selectItem);
		}
	}
	
	// preenche a lista de ministros utilizada na combo
	public void preencherListaMinistro() throws ServiceException {
		List<Ministro> ministros = getMinistroService().pesquisarMinistrosAtivos();
		// acrescentado a ordena��o do List
		//OrdenacaoUtils.ordenarListaClasseProcessualId(classes, TipoOrdenacao.ASCENDENTE);
		if (listaMinistro == null) {
			listaMinistro = new ArrayList<SelectItem>();
		}
		for (Ministro m : ministros) {
			SelectItem selectItem = new SelectItem(m.getId(), m.getNomeMinistroCapsulado(true, true, true));
			listaMinistro.add(selectItem);
		}
	}

	
	// Actions
	public void pesquisarAction(ActionEvent evt) throws ServiceException {
		ExclusaoDistribuicao exclusaoDistribuicao = new ExclusaoDistribuicao();
		if (fimPeriodo != null) {
			Calendar fimPeriodoCalendar = Calendar.getInstance();
			fimPeriodoCalendar.setTime(fimPeriodo);
			fimPeriodoCalendar.set(Calendar.HOUR_OF_DAY, 23);
			fimPeriodoCalendar.set(Calendar.MINUTE, 59);
			fimPeriodoCalendar.set(Calendar.SECOND, 59);
			fimPeriodoCalendar.set(Calendar.MILLISECOND, 999);
			
			exclusaoDistribuicao.setDataFimPeriodo(fimPeriodoCalendar.getTime());
		}
		if (inicioPeriodo != null) {
			exclusaoDistribuicao.setDataInicioPeriodo(inicioPeriodo);
		}
		if (codigoMinistro != null) {
			exclusaoDistribuicao.setMinistro(getMinistroService().recuperarPorId(codigoMinistro));
		}
		if (codigoTipoExclusao != null) {
			exclusaoDistribuicao.setTipoExclusaoDistribuicao(TipoExclusaoDistribuicao.valueOf(codigoTipoExclusao));
		}
		 
		if (inicioPeriodo != null && fimPeriodo != null && fimPeriodo.before(inicioPeriodo)) {
			setListaExclusao(null);
			reportarInformacao("Data Final dever� ser superior � Data Inicial informada");		
		} else {
			List<ExclusaoDistribuicao> resultExclusao = getExclusaoDistribuicaoService().recuperarExclusao(exclusaoDistribuicao);
			if (resultExclusao != null && resultExclusao.size() > 0) {
				setListaExclusao(getCheckableDataTableRowWrapperList(resultExclusao));
			} else {
				setListaExclusao(null);
				reportarInformacao("Nenhum registro encontrado para o(s) par�metro(s) informado(s).");
			}	
		}		
		
		atualizarSessao();
	}
	
	
	public void limpar(){
		setInicioPeriodo(null);
		setFimPeriodo(null);
		setCompensacao("S");
		setPrevencao("S");
		setObservacao(null);
		setListaTipoExclusao(null);
		setListaMinistro(null);
		setListaExclusao(null);
		setIdExclusao(null);
		setCodigoMinistro(null);
		setCodigoTipoExclusao(null);
		setMensagemPopup("");
		atualizarSessao();
	}
	
	public void limparAction(ActionEvent evt) {
		limpar();
	}
	public void removerItemAction(ActionEvent evt) {
		
		try {
			if (getRemocaoItemConfirmada().equals("S")) {
				CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaExclusao.getRowData();
				ExclusaoDistribuicao exclusaoSelecionada =  (ExclusaoDistribuicao) chkDataTable.getWrappedObject();
				getExclusaoDistribuicaoService().excluir(exclusaoSelecionada);
				listaExclusao.remove(chkDataTable);
				atualizarSessao();
				reportarInformacao("Per�odo de exclus�o de ministro removido com sucesso!");
			}
		} catch (Exception e) {
			reportarErro("N�o foi poss�vel remover o per�odo selecionado: " + e.getMessage());
		}
	}
	
	public void atualizarSessaoAction(ActionEvent evt) {
		atualizarSessao();
	}
	
	// antes da inclus�o
	public void novoAction (ActionEvent evt) {
		limpar();
		UIComponent form = popupEdicaoExclusao.getChildren().get(0);
		form.findComponent("btnEditarCancelar").getAttributes().put("value", "Cancelar");
		form.findComponent("btnEditarSalvar").getAttributes().put("disabled", false);
		form.findComponent("btnEditarSalvar").getAttributes().put("styleClass", "BotaoPadrao");
		form.findComponent("itEditarDataInicial").getAttributes().put("disabled", false);
		form.findComponent("itEditarDataFinal").getAttributes().put("disabled", false);
		form.findComponent("selectEditarTipoExclusao").getAttributes().put("disabled", false);
		form.findComponent("selectEditarMinistro").getAttributes().put("disabled", false);
		form.findComponent("rdoEditarCompensacao").getAttributes().put("disabled", false);
		form.findComponent("rdoEditarPrevencao").getAttributes().put("disabled", false);
		form.findComponent("itEditarObservacao").getAttributes().put("disabled", false);
		form.findComponent("pnlEditarJustificativa").getAttributes().put("style", "visibility:visible");
		form.findComponent("itEditarJustificativa").getAttributes().put("disabled", false);
		// mostra o campo "considerar os processos preventos" na inclus�o (depois a visibilidade ser� tratada pelo m�todo tratarJustificativa)
		form.findComponent("panelGroupPreventos").getAttributes().put("style", "visibility:visible");
		// setar as datas para os valores default quando abre a popup
		setInicioPeriodo(getInicioPeriodoDefault());
		setFimPeriodo(getFimPeriodoDefault());
		tratarJustificativa();
	}
	
	public Boolean getDesabilitaExclusao() throws ParseException{
		if (tabelaExclusao == null) {return false;}
		if (tabelaExclusao.getRowCount() == 0) {return false;}
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaExclusao.getRowData();
		ExclusaoDistribuicao exclusao = (ExclusaoDistribuicao) chkDataTable.getWrappedObject();
		
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date dataAtual = new Date();
		dataAtual = sd.parse(sd.format(dataAtual));
		
		Date dataInicioExclusao = exclusao.getDataInicioPeriodo();
		dataInicioExclusao = sd.parse(sd.format(dataInicioExclusao));
		
		if ( (dataInicioExclusao.after(dataAtual) || dataInicioExclusao.equals(dataAtual)) && (exclusao.getCompensado() == false) ) {
			setExclusaoPermitida(true);
			setAlteracaoPermitida(true);
			setIconeExclusao("/images/remove.gif");
			setIconeAlteracao("/images/editar_item.png");
			setHintIconeExclusao("Remover per�odo de exclus�o");
			setHintIconeAlteracao("Editar per�odo de exclus�o");
		} else {
			setExclusaoPermitida(false);
			setAlteracaoPermitida(false);
			setIconeExclusao("/images/remove_disabled.gif");
			setIconeAlteracao("/images/botaAlterarv_disabled.gif");
			setHintIconeExclusao("Per�odo de exclus�o finalizado n�o pode ser removido");
			setHintIconeAlteracao("Per�odo de exclus�o finalizado n�o pode ser editado");
		}
		
		atualizarSessao();
		return getExclusaoPermitida()?false:true;
	}
	
	
	public Boolean getDesabilitaAlteracao(){
		return getAlteracaoPermitida()?false:true;
	}
	
	public Boolean getTemMensagemPopup() {
		if ((getMensagemPopup() == null) || (getMensagemPopup().equals(""))) {
			return false;
		} else {
			return true;
		}
	}

	// antes da consulta
	public void atualizarParaConsultaAction(ActionEvent evt) {
		setMensagemPopup("");
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaExclusao.getRowData();
		ExclusaoDistribuicao exclusao = (ExclusaoDistribuicao) chkDataTable.getWrappedObject();
		this.idExclusao = exclusao.getId();
		this.codigoMinistro = exclusao.getMinistro().getId();
		this.codigoTipoExclusao = exclusao.getTipoExclusaoDistribuicao().getCodigo();
		if (exclusao.getAjusteDistribuicao() == null) {
			this.compensacao = "N";
		} else {
			this.compensacao = exclusao.getAjusteDistribuicao()?"S":"N";
		}
		this.compensacaoAnterior = this.compensacao;
		this.fimPeriodo = exclusao.getDataFimPeriodo();
		this.inicioPeriodo = exclusao.getDataInicioPeriodo();
		this.observacao = exclusao.getObservacao();
		this.justificativa = exclusao.getJustificativa();
		if (exclusao.getDistribuicaoPrevencao() == null) {
			this.prevencao = "N";
		} else {
			this.prevencao = exclusao.getDistribuicaoPrevencao()?"S":"N";
		}
		UIComponent form = popupEdicaoExclusao.getChildren().get(0);
		form.findComponent("btnEditarCancelar").getAttributes().put("value", "Fechar"); // muda label do bot�o cancelar
		form.findComponent("btnEditarSalvar").getAttributes().put("disabled", true); // desabilita o bot�o salvar
		form.findComponent("btnEditarSalvar").getAttributes().put("styleClass", "BotaoPadraoInativo");
		form.findComponent("pnlEditarJustificativa").getAttributes().put("style", "visibility:visible");
		form.findComponent("labelEditarJustificativa").getAttributes().put("value", "Justificativa: "); // mostra label como n�o obrigat�rio
		// desabilitar os campos para consulta
		form.findComponent("itEditarDataInicial").getAttributes().put("disabled", true);
		form.findComponent("itEditarDataFinal").getAttributes().put("disabled", true);
		form.findComponent("selectEditarTipoExclusao").getAttributes().put("disabled", true);
		form.findComponent("selectEditarMinistro").getAttributes().put("disabled", true);
		form.findComponent("rdoEditarCompensacao").getAttributes().put("disabled", true);
		form.findComponent("rdoEditarPrevencao").getAttributes().put("disabled", true);
		form.findComponent("itEditarObservacao").getAttributes().put("disabled", true);
		form.findComponent("itEditarJustificativa").getAttributes().put("disabled", true);
		// mostra o campo "considerar os processos preventos" na consulta
		form.findComponent("panelGroupPreventos").getAttributes().put("style", "visibility:visible"); 
		atualizarSessao();
	}
	
	
	// antes da altera��o
	public void atualizarParaEdicaoAction(ActionEvent evt) {
		setMensagemPopup("");
		CheckableDataTableRowWrapper chkDataTable = (CheckableDataTableRowWrapper) tabelaExclusao.getRowData();
		ExclusaoDistribuicao exclusao = (ExclusaoDistribuicao) chkDataTable.getWrappedObject();
		this.idExclusao = exclusao.getId();
		this.codigoMinistro = exclusao.getMinistro().getId();
		this.codigoTipoExclusao = exclusao.getTipoExclusaoDistribuicao().getCodigo();
		//this.compensacao = exclusao.getCompensado()?"S":"N";
		if (exclusao.getAjusteDistribuicao() == null) {
			this.compensacao = "N";
		} else {
			this.compensacao = exclusao.getAjusteDistribuicao()?"S":"N";
		}
		this.compensacaoAnterior = this.compensacao;
		this.fimPeriodo = exclusao.getDataFimPeriodo();
		this.inicioPeriodo = exclusao.getDataInicioPeriodo();
		this.observacao = exclusao.getObservacao();
		this.justificativa = exclusao.getJustificativa();
		if (exclusao.getDistribuicaoPrevencao() == null) {
			this.prevencao = "N";
		} else {
			this.prevencao = exclusao.getDistribuicaoPrevencao()?"S":"N";
		}
		UIComponent form = popupEdicaoExclusao.getChildren().get(0);
		form.findComponent("btnEditarCancelar").getAttributes().put("value", "Cancelar");
		form.findComponent("btnEditarSalvar").getAttributes().put("disabled", false); // habilita o bot�o salvar
		form.findComponent("btnEditarSalvar").getAttributes().put("styleClass", "BotaoPadrao");
		form.findComponent("pnlEditarJustificativa").getAttributes().put("style", "visibility:visible");
		form.findComponent("labelEditarJustificativa").getAttributes().put("value", "Justificativa: "); // mostra label como n�o obrigat�rio
		// per�odo em que a data de hoje ainda n�o entrou - todos os campos podem ser alterados
		if ( exclusao.getDataInicioPeriodo().after(new Date()) && exclusao.getDataFimPeriodo().after(new Date()) ) {
			form.findComponent("itEditarDataInicial").getAttributes().put("disabled", false);
			form.findComponent("itEditarDataFinal").getAttributes().put("disabled", false);
			form.findComponent("selectEditarTipoExclusao").getAttributes().put("disabled", false);
			form.findComponent("selectEditarMinistro").getAttributes().put("disabled", false);
			form.findComponent("rdoEditarCompensacao").getAttributes().put("disabled", false);
			form.findComponent("rdoEditarPrevencao").getAttributes().put("disabled", false);
			form.findComponent("itEditarObservacao").getAttributes().put("disabled", false);
			form.findComponent("itEditarJustificativa").getAttributes().put("disabled", false);
		// per�odo em que a data de hoje entrou, mas o per�odo n�o acabou - habilitar somente data final e observa��o
		} else if ( exclusao.getDataInicioPeriodo().before(new Date()) && exclusao.getDataFimPeriodo().after(new Date()) ) {
			form.findComponent("itEditarDataInicial").getAttributes().put("disabled", true);
			form.findComponent("itEditarDataFinal").getAttributes().put("disabled", false);
			form.findComponent("selectEditarTipoExclusao").getAttributes().put("disabled", true);
			form.findComponent("selectEditarMinistro").getAttributes().put("disabled", true);
			form.findComponent("rdoEditarCompensacao").getAttributes().put("disabled", true);
			form.findComponent("rdoEditarPrevencao").getAttributes().put("disabled", true);
			form.findComponent("itEditarObservacao").getAttributes().put("disabled", false);
			form.findComponent("itEditarJustificativa").getAttributes().put("disabled", true);
		}
		// mostra o campo "considerar os processos preventos" na edi��o de acordo com o valor de compensacao
		if (this.compensacao.equals("S")) {
			form.findComponent("panelGroupPreventos").getAttributes().put("style", "visibility:visible");
		} else {
			form.findComponent("panelGroupPreventos").getAttributes().put("style", "visibility:hidden");
		}
		tratarJustificativa();
		atualizarSessao();
	}
	
	public void salvarPeriodoExclusaoAction(ActionEvent evt) {
		try {
			setMensagemPopup("");
			if (!criticar()) {
				return;
			}
			ExclusaoDistribuicao novaExclusao = new ExclusaoDistribuicao();
			novaExclusao.setCompensado(false);
			novaExclusao.setAjusteDistribuicao(compensacao.equals("S")?true:false);
			novaExclusao.setDataFimPeriodo(fimPeriodo);
			novaExclusao.setDataInicioPeriodo(inicioPeriodo);
			novaExclusao.setDistribuicaoPrevencao(prevencao.equals("S")?true:false);
			novaExclusao.setMinistro(getMinistroService().recuperarPorId(codigoMinistro));
			novaExclusao.setObservacao(observacao);
			novaExclusao.setJustificativa(justificativa);
			novaExclusao.setTipoExclusaoDistribuicao(TipoExclusaoDistribuicao.valueOf(codigoTipoExclusao));
			if (idExclusao != null && idExclusao > 0) {
				novaExclusao.setId(idExclusao);
			}
			getExclusaoDistribuicaoService().salvar(novaExclusao);
			// desabilita a bot�o salvar e os campos da popup ap�s registro salvo
			UIComponent form = popupEdicaoExclusao.getChildren().get(0);
			form.findComponent("btnEditarCancelar").getAttributes().put("value", "Fechar");
			form.findComponent("btnEditarSalvar").getAttributes().put("disabled", true);
			form.findComponent("btnEditarSalvar").getAttributes().put("styleClass", "BotaoPadraoInativo");
			form.findComponent("itEditarDataInicial").getAttributes().put("disabled", true);
			form.findComponent("itEditarDataFinal").getAttributes().put("disabled", true);
			form.findComponent("selectEditarTipoExclusao").getAttributes().put("disabled", true);
			form.findComponent("selectEditarMinistro").getAttributes().put("disabled", true);
			form.findComponent("rdoEditarCompensacao").getAttributes().put("disabled", true);
			form.findComponent("rdoEditarPrevencao").getAttributes().put("disabled", true);
			form.findComponent("itEditarObservacao").getAttributes().put("disabled", true);
			form.findComponent("itEditarJustificativa").getAttributes().put("disabled", true);
			if (idExclusao != null && idExclusao > 0) {
				setMensagemPopup("Per�odo de exclus�o de ministro alterado com sucesso!");
			} else {
				setMensagemPopup("Per�odo de exclus�o de ministro inclu�do com sucesso!");
			}
			
		} catch (Exception e) {
			setMensagemPopup("N�o foi poss�vel salvar o per�odo de exclus�o: " + e.getMessage());
		}
	}
	
	/**
	 * 
	 * 	1	PRESIDENTE DO STF -> Art. 67 - � 2� RISTF
		2	PRESIDENTE DO TSE -> Art. 67 - � 5� RISTF
		3	AUS�NCIA - ART 67 -> Art. 67 - � 5� RISTF
		4	PRESIDENTE DO CNJ
	 * 
	 */
		
	public void atualizarObservacaoAction(ActionEvent evt){
		if (codigoTipoExclusao.equals(new Long(1))) {
			setObservacao("Art. 67 - �� 2� e 12� RISTF");
			setCompensacao("N");
			setPrevencao("S");
			habilitaRadioButtons(true);
		} else if (codigoTipoExclusao.equals(new Long(2))) {
			setObservacao("Art. 67 - � 5� RISTF");
			setCompensacao("N");
			setPrevencao("N");
			habilitaRadioButtons(false);
		} else if ( codigoTipoExclusao.equals(new Long(3))){
			setObservacao("Art. 67 - � 1� RISTF � Ministro licenciado por mais de 30 dias � ato: " +
					" ou Art. 67 - � 1� RISTF � Ministro em miss�o oficial por mais de trinta dias � ato: " +
					" ou Resolu��o n� 449/2011 � Miss�o oficial � ato: ");
			setCompensacao("N");
			setPrevencao("S");
			habilitaRadioButtons(true);
		} else if ( codigoTipoExclusao.equals(new Long(5))){
			setObservacao("Art. 67 - � 13� RISTF");
			setCompensacao("N");
			setPrevencao("S");
		}
		
		tratarJustificativa();
		atualizarSessao();
	}

	private void habilitaRadioButtons(boolean habilitar) {
		UIComponent form = popupEdicaoExclusao.getChildren().get(0);
		form.findComponent("rdoEditarCompensacao").getAttributes().put("disabled", !habilitar);
		form.findComponent("rdoEditarPrevencao").getAttributes().put("disabled", !habilitar);
	}
	
	private void tratarJustificativa() {
		UIComponent form = popupEdicaoExclusao.getChildren().get(0);
		if (getCompensacao().equals("N")) { // se ocorreu a mudan�a para N o campo justificativa n�o fica obrigat�rio
			form.findComponent("labelEditarJustificativa").getAttributes().put("value", "Justificativa: ");
		} else {
			form.findComponent("labelEditarJustificativa").getAttributes().put("value", "* Justificativa: "); // mostra label obrigat�rio
		}
	}
	
	/**
	 * A justificativa ser� habilitada somente quando ocorrer uma mudan�a no campo compensa��o
	 */
	public void tratarJustificativa(ActionEvent evt) {
		tratarJustificativa();
		// mostra os controles de preventos somente quando existir o ajuste
//		if (getCompensacao().equals("S")) {
//			form.findComponent("panelGroupPreventos").getAttributes().put("style", "visibility:visible"); 
//		} else {
//			setPrevencao("N");
//			form.findComponent("panelGroupPreventos").getAttributes().put("style", "visibility:hidden"); 
//		}
		atualizarSessao();
	}
	
	/**
	 *  Regras definidas na PROCESSAMENTO-1163
	 * 	1. Na inclus�o ou altera��o todos os campos, exceto a observa��o, s�o obrigat�rios;
		2. A data inicial do per�odo n�o deve ser menor que a data atual; Sugest�o de mensagem: "Data de in�cio inferior � data atual"
		3. A data final n�o deve ser menor que data inicial do per�odo; Sugest�o de mensagem: "Data final superior � data inicial"
		4. Um ministro n�o pode ter um per�odo de exclus�o inserido duas vezes para um mesmo tipo de exclus�o;
		5. Um registro s� poder� ser excluido quando a data inicial do per�odo for maior que a data atual;
		6. Um per�odo de exclus�o somente poder� ser alterado se a data final do per�odo for maior que a data atual. 
		   Nesse caso apenas os campos OBSERVA��O e data final poder�o ser alterados. 
		   Se a data inicial e final do per�odo forem maiores que a data atual TODOS os campos poder�o ser alterados;
		7. Os tipos de exclus�o "PRESIDENTE DO STF", "PRESIDENTE TSE" e "PRESIDENTE DO CNJ" n�o poder�o ser cadastrados duas vezes no mesmo per�odo;
		8. Caso haja uma exclus�o j� cadastrada para o per�odo informado, 
		   o sistema deve mostrar a mensagem: "Ministro j� exclu�do da distribui��o ou tipo de exclus�o j� utilizado no per�odo informado";
		9. Data inicial deve ser maior que a data atual. Regra alterada: agora � poss�vel inserir um per�odo come�ando na mesmo dia.
	 * 
	 */
	public Boolean criticar() {
		try {
			// Regra 1
			if ((inicioPeriodo == null) || (fimPeriodo == null) || 
				(compensacao == null) || (prevencao == null) || 
				(codigoMinistro == null) || (codigoTipoExclusao == null)) {
				setMensagemPopup("Campos obrigat�rios: 'In�cio do Per�odo', 'Fim do Per�odo', 'Tipo de Exclus�o', 'Ministro', 'Compensa��o' e 'Preven��o'");
				return false;
			}
			// se a compensa��o foi alterada o campo justificativa � obrigat�rio (� verificado somente na alera��o getIdExclusao != null
//			if ( ( getIdExclusao() != null && !getIdExclusao().equals(0) ) && ( getCompensacao().equals("N") && getCompensacaoAnterior().equals("S") ) ) {
			if ( getCompensacao().equals("S") ) {
				if (getJustificativa() == null || getJustificativa().equals("")){
					setMensagemPopup("O campo 'Justificativa' tamb�m � obrigat�rio quando h� ajuste da distribui��o");
					return false;
				}
			}
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date inicio = sd.parse(sd.format(inicioPeriodo));
			Date fim = sd.parse(sd.format(fimPeriodo));
			
			Date dataAtual = new Date();
			dataAtual = sd.parse(sd.format(dataAtual));
			
			// Regra 2
			if (inicio.before(dataAtual)) {
				setMensagemPopup("Data e hora de in�cio inferior � data e hora atual");
				return false;
			}
			// Regra 3
			if (fim.before(inicioPeriodo)) {
				setMensagemPopup("Data e hora final inferior � data e hora inicial");
				return false;
			}
			// Regra 4 -- somente para inclus�o - idExclusao == 0
			if (getIdExclusao() == null || getIdExclusao().equals((long)0)) {
				if (getExclusaoDistribuicaoService().existePeriodoParaMinistro(inicio, fim, codigoMinistro)) {
					setMensagemPopup("Ministro j� exclu�do da distribui��o ou tipo de exclus�o j� utilizado no per�odo informado");
					return false;
				}
			}
			// Regra 5 redundante com a regra 2
			// Regra 6 impelementada em outros m�todos que atualizam as propriedades exclusaoPermitida, alteracaoPermitida e alteracaoParcial
			// Regra 7: tipo 1,2 e 4 (presidente STF, TSE e CNJ)
			if (codigoTipoExclusao.equals((long)1) || codigoTipoExclusao.equals((long)2) || codigoTipoExclusao.equals((long)4)) {
				if (getExclusaoDistribuicaoService().existeAusenciaPorPresidenteNoPeriodo(inicio, fim)) { 
					setMensagemPopup("J� existe per�odo de exclus�o para o tipo informado");
					return false;
				}
			}
			// Regra 9
			if ( inicio.before(dataAtual) ) {
				setMensagemPopup("Data e hora inicial menor que a data e hora atual");
				return false;
			}
			
		} catch (Exception e) {
			setMensagemPopup("Erro na checagem dos dados:" + e.getMessage());
			return false;
		}
		return true;
	}
	
	public Date getInicioPeriodoDefault() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		calendar.set(Calendar.MILLISECOND, 000);
		return calendar.getTime();
	}
	public Date getFimPeriodoDefault() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
	
	// getters/setters
	public void setInicioPeriodo(Date inicioPeriodo) {
		this.inicioPeriodo = inicioPeriodo;
	}
	public Date getInicioPeriodo() throws ParseException {
		return inicioPeriodo;
	}
	public void setFimPeriodo(Date fimPeriodo) {
		this.fimPeriodo = fimPeriodo;
	}
	public Date getFimPeriodo() {
		return fimPeriodo;
	}
	public void setListaTipoExclusao(List<SelectItem> listaTipoExclusao) {
		this.listaTipoExclusao = listaTipoExclusao;
	}
	public List<SelectItem> getListaTipoExclusao() throws ServiceException {
		if (listaTipoExclusao == null) {
			preencherListaTipoExclusao();
		}
		return listaTipoExclusao;
	}
	public void setListaMinistro(List<SelectItem> listaMinistro) {
		this.listaMinistro = listaMinistro;
	}
	public List<SelectItem> getListaMinistro() throws ServiceException {
		if (listaMinistro == null) {
			preencherListaMinistro();
		}
		return listaMinistro;
	}
	public void setCompensacao(String compensacao) {
		this.compensacao = compensacao;
	}
	public String getCompensacao() {
		return compensacao;
	}
	public void setPrevencao(String prevencao) {
		this.prevencao = prevencao;
	}
	public String getPrevencao() {
		return prevencao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setListaExclusao(List<CheckableDataTableRowWrapper> listaExclusao) {
		this.listaExclusao = listaExclusao;
	}
	public List<CheckableDataTableRowWrapper> getListaExclusao() {
		return listaExclusao;
	}

	public void setCodigoTipoExclusao(Long codigoTipoExclusao) {
		this.codigoTipoExclusao = codigoTipoExclusao;
	}

	public Long getCodigoTipoExclusao() {
		return codigoTipoExclusao;
	}

	public void setCodigoMinistro(Long codigoMinistro) {
		this.codigoMinistro = codigoMinistro;
	}

	public Long getCodigoMinistro() {
		return codigoMinistro;
	}

	public void setTabelaExclusao(org.richfaces.component.html.HtmlDataTable tabelaExclusao) {
		this.tabelaExclusao = tabelaExclusao;
	}

	public org.richfaces.component.html.HtmlDataTable getTabelaExclusao() {
		return tabelaExclusao;
	}

	public Long getIdExclusao() {
		return idExclusao;
	}

	public void setIdExclusao(Long idExclusao) {
		this.idExclusao = idExclusao;
	}

	public void setAlteracaoParcial(Boolean alteracaoParcial) {
		this.alteracaoParcial = alteracaoParcial;
	}

	public Boolean getAlteracaoParcial() {
		return alteracaoParcial;
	}

	public void setAlteracaoPermitida(Boolean alteracaoPermitida) {
		this.alteracaoPermitida = alteracaoPermitida;
	}

	public Boolean getAlteracaoPermitida() {
		return alteracaoPermitida;
	}

	public void setExclusaoPermitida(Boolean exclusaoPermitida) {
		this.exclusaoPermitida = exclusaoPermitida;
	}

	public Boolean getExclusaoPermitida() {
		return exclusaoPermitida;
	}

	public void setPopupEdicaoExclusao(org.richfaces.component.html.HtmlModalPanel popupEdicaoExclusao) {
		this.popupEdicaoExclusao = popupEdicaoExclusao;
	}

	public org.richfaces.component.html.HtmlModalPanel getPopupEdicaoExclusao() {
		return popupEdicaoExclusao;
	}

	public void setIconeAlteracao(String iconeAlteracao) {
		this.iconeAlteracao = iconeAlteracao;
	}

	public String getIconeAlteracao() {
		return iconeAlteracao;
	}

	public void setIconeExclusao(String iconeExclusao) {
		this.iconeExclusao = iconeExclusao;
	}

	public String getIconeExclusao() {
		return iconeExclusao;
	}

	public void setHintIconeExclusao(String hintIconeExclusao) {
		this.hintIconeExclusao = hintIconeExclusao;
	}

	public String getHintIconeExclusao() {
		return hintIconeExclusao;
	}

	public void setHintIconeAlteracao(String hintIconeAlteracao) {
		this.hintIconeAlteracao = hintIconeAlteracao;
	}

	public String getHintIconeAlteracao() {
		return hintIconeAlteracao;
	}

	public void setMensagemPopup(String mensagemPopup) {
		setAtributo(MENSAGEM_POPUP, mensagemPopup);
		this.mensagemPopup = mensagemPopup;
		
	}

	public String getMensagemPopup() {
		return mensagemPopup;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setCompensacaoAnterior(String compensacaoAnterior) {
		this.compensacaoAnterior = compensacaoAnterior;
	}

	public String getCompensacaoAnterior() {
		return compensacaoAnterior;
	}

	public void setRemocaoItemConfirmada(String remocaoItemConfirmada) {
		this.remocaoItemConfirmada = remocaoItemConfirmada;
	}

	public String getRemocaoItemConfirmada() {
		if (remocaoItemConfirmada == null || remocaoItemConfirmada.equals("")) {
			remocaoItemConfirmada = "N"; 
		}
		return remocaoItemConfirmada;
	}
	

}
