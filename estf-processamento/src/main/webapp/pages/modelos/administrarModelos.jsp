<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<script type="text/javascript">
	function pesquisarRecursos() {
		document.getElementById('botaoCarregarComboRecursos').click();
	}
	function aguarde(mostrar, div) {
		if (mostrar == true) {
			document.getElementById(div).innerHTML = '<img src="/processamento/images/loading.gif" /><font class="Padrao">&nbsp;&nbsp;Aguarde...</font>';
		}
	}

	function pesquisar() {
		document.getElementById('botaoPesquisar').click();
	}

	function caixaAlta(campo) {
		campo.value = campo.value.toUpperCase();
	}

	function focoComboBox(campo) {
		document.getElementById(campo).focus();
	}

	function validarCampos(){
	  if (document.getElementById('nomeModeloComId').value == '') {
		  alert('Favor insira um "Nome".');
			return false;
	  }
	  if (document.getElementById('mnuTipoPermissaoNovo').value == '') {
		  alert('Favor selecione uma "Permissao".');
			return false;
	  }
	  if (document.getElementById('tipoModelo').value == '') {
		  alert('Favor selecione um "Modelo".');
			return false;
	  }
	  if (document.getElementById('idAndamento').value == '' && !document.getElementById('flagSemAndamento').checked ) {
		  alert('Favor selecione um "Andamento".');
			return false;
	  }
	  if(document.getElementById('flagAssintura').checked){
		 if(document.getElementById('setorSaida').value == ''){ 
		 	alert('Favor selecione um "Setor de Saida"');
		 	return false;
		 }
	  }
	  
	  Richfaces.hideModalPanel('modalEditaModelo');
	  return true;
	  
	}



</script>

<f:view>
	<a4j:page pageTitle="::.. Administrar Modelos ..::">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Elaborar Modelos" />
			</jsp:include>
		</h:form>
		<a4j:outputPanel id="pnlPrincipal" ajaxRendered="true">

			<a4j:form id="formId" prependId="false">
				<div style="text-align: left; width: 100%;">
					<rich:hotKey key="return" handler="pesquisar();" />
					<ul class="PadraoUl">
						<li class="PainelTituloCriaTexto"><span>Pesquisa</span></li>

						<li class="PadraoLi"><span>Nome do modelo: <h:inputText
									id="itNomeModelo"
									value="#{beanAdministrarModelos.nomeModeloProcura}"
									style="margin-left:10px;" size="67" onkeyup="caixaAlta(this);" />
						</span>
						</li>

						<li class="PadraoLi">
							<span>Tipo de Permissão: 
								<h:selectOneMenu
									id="mnuTipoPermissao" style="margin-left:3px;"
									value="#{beanAdministrarModelos.idTipoPermissao}"
									styleClass="Input">
									<f:selectItem itemValue="#{null}" />
									<f:selectItems
										value="#{beanAdministrarModelos.itensTiposPermissoes}" />
								</h:selectOneMenu> 
							</span>
						</li>

						<li class="PadraoLi"><span>Tipo de Modelo: <h:selectOneMenu
									id="mnuTipoModelo" style="margin-left:23px;"
									value="#{beanAdministrarModelos.idTipoComunicacao}"
									styleClass="Input">
									
									<f:selectItems
										value="#{beanAdministrarModelos.itensTiposModelos}" />
								</h:selectOneMenu> </span>
						</li>

						<li style="margin-top: 10px"><span> <a4j:commandButton
									id="botaoPesquisar" value="Pesquisar"
									styleClass="BotaoPesquisar"
									actionListener="#{beanAdministrarModelos.pesquisarModelosAction}"
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" /> <a4j:commandButton
									id="botaoLimpar" value="Limpar" styleClass="BotaoPadrao"
									actionListener="#{beanAdministrarModelos.limparTelaAction}"
									reRender="pnlPesquisa,pnlPrincipal"
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);"
								 /> 
									
								<a4j:commandButton
									reRender="pnlPrincipal,modalEditaModelo" 
									id="botaoNovo" value="Criar Novo" styleClass="BotaoEstendido"
									onclick="exibirMsgProcessando(true);"
									oncomplete="exibirMsgProcessando(false); Richfaces.showModalPanel('modalEditaModelo');"
									action="#{beanAdministrarModelos.renderizaTelaNovoModelo}"									
								/> 
							</span>
						</li>
					</ul>
					<h:panelGrid width="100%"
						rendered="#{not empty beanAdministrarModelos.listaModelos}">
						<rich:dataTable headerClass="DataTableDefaultHeader"
							styleClass="DataTableDefault"
							footerClass="DataTableDefaultFooter"
							rowClasses="DataTableRow, DataTableRow2"
							columnClasses="vinteCincoLeft, vinteCinco, dez, cinco, cinco, cinco, cinco, cinco, cinco, vinteCenter, cinco, cinco, cinco"
							value="#{beanAdministrarModelos.listaModelos}"
							var="wrappedModelo"
							binding="#{beanAdministrarModelos.tabelaModelos}">

							<rich:column sortBy="#{wrappedModelo.wrappedObject.dscModelo}"   style="width:20%">
								<f:facet name="header" >
									<h:outputText value="Nome" />
								</f:facet>
								<h:outputText style="text-align:left;"
									value="#{wrappedModelo.wrappedObject.dscModelo}" />
							</rich:column>

							<rich:column
								sortBy="#{wrappedModelo.wrappedObject.tipoPermissao.descricao}" style="width:10%">
								<f:facet name="header">
									<h:outputText value="Tipo de Permissão" />
								</f:facet><div align="left">
								<h:outputText style="text-align: left;"
									value="#{wrappedModelo.wrappedObject.tipoPermissao.descricao}" /></div>
							</rich:column>

							<rich:column
								sortBy="#{wrappedModelo.wrappedObject.tipoComunicacao.descricao}"  style="width:5%">
								<f:facet name="header">
									<h:outputText value="Tipo Modelo" />
								</f:facet>
								<h:outputText
									value="#{wrappedModelo.wrappedObject.tipoComunicacao.descricao}" />
							</rich:column>

							<rich:column
								sortBy="#{wrappedModelo.wrappedObject.flagTipoAcessoDocumentoPeca.descricao}"  style="width:5%">
								<f:facet name="header">
									<h:outputText value="Tipo de Acesso" />
								</f:facet>
								<h:outputText
									value="#{wrappedModelo.wrappedObject.flagTipoAcessoDocumentoPeca.descricao}" />
							</rich:column>

							<rich:column
								sortBy="#{wrappedModelo.wrappedObject.flagPartes.descricao}">
								<f:facet name="header">
									<h:outputText value="Partes" />
								</f:facet>
								<h:outputText style="text-align:center;"
									value="#{wrappedModelo.wrappedObject.flagPartes.descricao}" />
							</rich:column>

							<rich:column
								sortBy="#{wrappedModelo.wrappedObject.flagAssinaturaMinistro.descricao}"  style="width:5%">
								<f:facet name="header">
									<h:outputText value="Assinatura Ministro" />
								</f:facet>
								<h:outputText style="text-align:center;"
									value="#{wrappedModelo.wrappedObject.flagAssinaturaMinistro.descricao}" />
							</rich:column>

							<rich:column
								sortBy="#{wrappedModelo.wrappedObject.flagProcessoLote.descricao}"  style="width:5%">
								<f:facet name="header">
									<h:outputText value="Lote" />
								</f:facet>
								<h:outputText style="text-align:center;"
									value="#{wrappedModelo.wrappedObject.flagProcessoLote.descricao}" />
							</rich:column>

							<rich:column
								sortBy="#{wrappedModelo.wrappedObject.flagVinculoPecaProcessoElet.descricao}"  style="width:5%">
								<f:facet name="header">
									<h:outputText value="Peças" />
								</f:facet>
								<h:outputText style="text-align:center;"
									value="#{wrappedModelo.wrappedObject.flagVinculoPecaProcessoElet.descricao}" />
							</rich:column>

							<rich:column
								sortBy="#{wrappedModelo.wrappedObject.flagEncaminharParaDJe.descricao}"  style="width:5%">
								<f:facet name="header">
									<h:outputText value="Envia p/ DJe" />
								</f:facet>
								<h:outputText style="text-align:center;"
									value="#{wrappedModelo.wrappedObject.flagEncaminharParaDJe.descricao}" />
							</rich:column>

							<rich:column
								sortBy="#{wrappedModelo.wrappedObject.setorDestino.nome}"  style="width:5%">
								<f:facet name="header" >
									<h:outputText value="Setor de Saída" />
								</f:facet>
								<h:outputText style="text-align:left;"
									value="#{wrappedModelo.wrappedObject.setorDestino.sigla}" 
									title="#{wrappedModelo.wrappedObject.setorDestino.nome}" />
							</rich:column>

							<rich:column
								sortBy="#{wrappedModelo.wrappedObject.flagSemAndamento.descricao}"  style="width:5%">
								<f:facet name="header">
									<h:outputText value="Sem Andamento" />
								</f:facet>
								<h:outputText style="text-align:center;"
									value="#{wrappedModelo.wrappedObject.flagSemAndamento.descricao}" />
							</rich:column>
							
							<rich:column
								sortBy="#{wrappedModelo.wrappedObject.flagJuntadaPecaProc.descricao}"  style="width:5%">
								<f:facet name="header">
									<h:outputText value="Juntar" />
								</f:facet>
								<h:outputText style="text-align:center;"
									value="#{wrappedModelo.wrappedObject.flagJuntadaPecaProc.descricao}" />
							</rich:column>
							<rich:column  style="width:2%">
								<f:facet name="header">
									<h:outputText value="Editar Modelo" />
								</f:facet>
								<h:commandLink action="#{beanAdministrarModelos.editarModelo}">
									<h:graphicImage url="../../images/btabrir.gif"
										title="Editar o modelo de documento" />
								</h:commandLink>
							</rich:column>

							<rich:column  style="width:2%">
								<f:facet name="header">
									<h:outputText value="Alterar" />
								</f:facet><div align="center">
								<a4j:commandLink reRender="modalEditaModelo" style="float: center;" onclick="exibirMsgProcessando(true);"
									oncomplete="Richfaces.showModalPanel('modalEditaModelo');exibirMsgProcessando(false);"
									actionListener="#{beanAdministrarModelos.alterarModeloAction}">
									<h:graphicImage url="../../images/botaAlterarv.gif" style="text-align: rigth;"
										title="Alterar Modelo" />
								</a4j:commandLink></div>
							</rich:column>

							<rich:column  style="width:2%">
								<f:facet name="header">
									<h:outputText value="Excluir" />
								</f:facet>
								<h:commandLink
									actionListener="#{beanAdministrarModelos.excluirModelosAction}">
									<h:graphicImage url="../../images/close.gif" title="Excluir" />
								</h:commandLink>
							</rich:column>

						</rich:dataTable>
					</h:panelGrid>
				</div>
			</a4j:form>

			<jsp:include page="/pages/template/footer.jsp" flush="true" />

		</a4j:outputPanel>

		<rich:modalPanel id="modalEditaModelo" keepVisualState="true"
			style="overflow:auto" autosized="true" width="650">
			<f:facet name="header">
				<h:outputText rendered="#{!beanAdministrarModelos.renderedAlterarModelo}" value="Criar Modelo"/>
				<h:outputText rendered="#{beanAdministrarModelos.renderedAlterarModelo}" value="Alterar Modelo" />
				
			</f:facet>
			
			<h:form prependId="false">
				<div class="PainelTituloCriaTexto" style="width:100%">
					<span> Preencha os campos abaixo: </span>
				</div>
				
				<div style="margin-top: 5px">
					<h:outputText styleClass="Padrao" value="Nome*:"/>
					<h:inputText style="margin-left: 73px;" size="55" onkeyup="caixaAlta(this);"
						id="nomeModeloComId" value="#{beanAdministrarModelos.nomeModelo}" />
				</div>
				
				<div style="margin-top: 5px">
					<h:outputText styleClass="Padrao" value="Tipo de Permissão*:"></h:outputText>
					<h:selectOneMenu id="mnuTipoPermissaoNovo" style="margin-left:3px;"
						value="#{beanAdministrarModelos.idTipoPermissaoAlteracao}"
						styleClass="Input" >
						<f:selectItem itemValue="#{null}" />
						<f:selectItems
							value="#{beanAdministrarModelos.itensTiposPermissoesAlteracao}" />
						<a4j:support
							actionListener="#{beanAdministrarModelos.procurarTiposModelosPeloTipoPermissaoAction}"
							event="onchange"
							reRender="tipoModelo" />
					</h:selectOneMenu>
				</div>

				<div style="margin-top: 5px">
					<h:outputText styleClass="Padrao" value="Tipo de Modelo*:"></h:outputText>
					<h:selectOneMenu style="margin-left:24px;"
						value="#{beanAdministrarModelos.idTipoComunicacaoAlteracao}"
						styleClass="Input" id="tipoModelo">
						<f:selectItems
							value="#{beanAdministrarModelos.itensTiposModelosAlteracao}" />
					</h:selectOneMenu>
				</div>

				<div style="margin-top: 5px">
					<div> 
					<h:selectBooleanCheckbox
								value="#{beanAdministrarModelos.flagAssinaturaMinistro}"
								id="flagAssintura"
								onclick="document.getElementById('btnDesabilitarEncaminhamento').click();" />
						<h:outputText styleClass="Padrao"
							value="Assinatura do Ministro" /> 
								
					</div> 
					<div> 
						
							 <h:selectBooleanCheckbox
							value="#{beanAdministrarModelos.flagCampoLivre}"
							id="flagCampoLivre" /> 
							<h:outputText styleClass="Padrao"
							value="Tags livres" />
					</div> 
					<div> 
						
							<h:selectBooleanCheckbox
							value="#{beanAdministrarModelos.flagPartes}" id="flagPartes" />
							<h:outputText
							styleClass="Padrao" value="Listar Partes" /> 
					</div> 
					<div> 
						<h:selectBooleanCheckbox
						value="#{beanAdministrarModelos.flagPecas}" id="flagPecas" /> 
						<h:outputText styleClass="Padrao"
						value="Vincular Peças" /> 
					</div> 
					<div>
						
							<h:selectBooleanCheckbox
							value="#{beanAdministrarModelos.flagDuasAssinaturas}"
							id="flagDuasAssinaturas" /> 
							<h:outputText styleClass="Padrao" value="Duas assinaturas" /> 
					</div> 
					<div> 
						
							<h:selectBooleanCheckbox
							value="#{beanAdministrarModelos.flagProcessoLote}"
							id="flagProcessoLote" /> 
							<h:outputText
						styleClass="Padrao" value="Lote" /> 
					</div> 
					<div> 
						
							<h:selectBooleanCheckbox
							value="#{beanAdministrarModelos.flagRestringeAcessoPeca}"
							id="flagPecaPublica" /> 
							<h:outputText
						styleClass="Padrao" value="Restringe acesso peça" /> 
					</div> 
					<div> 
							<h:selectBooleanCheckbox
							value="#{beanAdministrarModelos.flagEncaminharParaDJe}"
							id="flagEncaminharParaDJe" /> 
							<h:outputText
						styleClass="Padrao" value="Encaminhar para o DJe" />
					</div>
					<div> 
							<h:selectBooleanCheckbox
							value="#{beanAdministrarModelos.flagAlterarObsAndamento}"
							id="flagAlterarObsAndamento" /> 
							<h:outputText
						styleClass="Padrao" value="Alterar observação de andamento" />
					</div>
					
					
					<div> 
							<h:selectBooleanCheckbox 
							value="#{beanAdministrarModelos.flagSemAndamento}"
							id="flagSemAndamento" onclick="document.getElementById('btnDesabilitarAndamento').click();"
							 />
							 <h:outputText
						styleClass="Padrao" value="Não lançar andamento" />
					</div>
				
					<div> 
							<h:selectBooleanCheckbox
							value="#{beanAdministrarModelos.flagJuntadaPecaProc}"
							id="flagPendenteJuntada" /> 
							<h:outputText
						styleClass="Padrao" value="Manter Peça Gerada como pendente de juntada" />
					</div>
					
					
				</div>
				<div style="margin-top: 5px">
					<h:outputText styleClass="Padrao"
						value="Andamento a ser lançado na assinatura do documento:"></h:outputText>
					<h:selectOneMenu style="margin-left:24px;"
					disabled ="#{beanAdministrarModelos.flagSemAndamento}"
						value="#{beanAdministrarModelos.idAndamentoModelo}"
						styleClass="Input"
						id="idAndamento">
						<f:selectItems
							value="#{beanAdministrarModelos.itensAndamentoModelo}" />
					</h:selectOneMenu>
				</div>
				<div style="margin-top: 5px">
					<h:outputText styleClass="Padrao" value="Setor de Saída:"></h:outputText>
					<h:selectOneMenu style="margin-left:24px;"
						disabled="#{beanAdministrarModelos.renderedDesabilitaMenu}"
						value="#{beanAdministrarModelos.idSetorDestino}"
						styleClass="Input"
						id="setorSaida">
						<f:selectItems
							value="#{beanAdministrarModelos.itensSetoresDestino}" />
					</h:selectOneMenu>
				</div>
				<div style="margin-top: 10px">
					<h:commandButton styleClass="BotaoPadrao" 
						rendered="#{beanAdministrarModelos.renderedAlterarModelo == false}"
						action="#{beanAdministrarModelos.criarNovoModelo}" value="Salvar"
						onclick="if(!validarCampos()){return;}else{Richfaces.showModalPanel('msg');}"
						>
					</h:commandButton>
					
					<a4j:commandButton styleClass="BotaoPadrao" limitToList="true"
						rendered="#{beanAdministrarModelos.renderedAlterarModelo == true}"
						onclick="exibirMsgProcessando(true);if(!validarCampos()){return;}"
						value="Alterar" reRender="formId"
						actionListener="#{beanAdministrarModelos.salvarModeloAlteradoAction}" 
						oncomplete="exibirMsgProcessando(false);">
					</a4j:commandButton>
					
					<a4j:commandButton styleClass="BotaoPadrao" value="Fechar"
						action="#{beanAdministrarModelos.fecharModelo}"
						onclick="Richfaces.hideModalPanel('modalEditaModelo');exibirMsgProcessando(true);"
						reRender="modalEditaModelo"
						oncomplete="Richfaces.hideModalPanel('modalEditaModelo'); exibirMsgProcessando(false); ">
					</a4j:commandButton>
				</div>
			
			<a4j:commandButton styleClass="BotaoOculto"
					id="btnDesabilitarEncaminhamento"
					onclick="exibirMsgProcessando(true)" reRender="setorSaida"
					oncomplete="exibirMsgProcessando(false); "
					actionListener="#{beanAdministrarModelos.desabilitarCampoDeEncaminhamentoAction}" />
					
			<a4j:commandButton styleClass="BotaoOculto"
					id="btnDesabilitarAndamento"
					onclick="exibirMsgProcessando(true)" reRender="idAndamento"
					oncomplete="exibirMsgProcessando(false); "
					actionListener="#{beanAdministrarModelos.alterarLancaAndamentoAction}" />				
			</h:form>
		</rich:modalPanel>
		<rich:modalPanel id="msg" height="125" width="400" >
			<f:facet name="header">
				<h:outputText value="Mensagem"/>
			</f:facet>
			<h:form >
				<h:outputText styleClass="Padrao" value="O modelo foi criado com sucesso. 
				Por favor, edite o modelo no STFOffice e retorne ao sistema."/>
				<div style="padding-top: 20px; text-align: center">	
					<a4j:commandButton styleClass="BotaoPadrao" value="OK"
				 	onclick="Richfaces.hideModalPanel('msg')">
					</a4j:commandButton>
				</div>
			</h:form>
		</rich:modalPanel>
	</a4j:page>
	 
	 
</f:view>