<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<script>
// controla o radio button da grid
function dataTableSelectOneRadio(radio) {
    var radioId = radio.name.substring(radio.name.lastIndexOf(':'));

    for (var i = 0; i < radio.form.elements.length; i++) {
        var element = radio.form.elements[i];

        if (element.name.substring(element.name.lastIndexOf(':')) == radioId) {
            element.checked = false;
        }
    }

    radio.checked = true;
    document.getElementById('hidResponsavelSelecionado').value = radio.value;
}

/*
function tratarBotoesAutorizadoAutorizador() {
	if (document.getElementById('hidExisteVinculo').value == 'S') {
		alert('habilita autorizado');
	    document.getElementById('btnAddAutorizador').disabled = true;
	    document.getElementById('btnAddAutorizador').styleClass = "BotaoPadraoEstendidoInativo";
	    
	    document.getElementById('btnAddAutorizado').disabled = false;
	    document.getElementById('btnAddAutorizado').styleClass = "BotaoPadraoEstendido";
	} else {
		alert('habilita autorizador');
	    document.getElementById('btnAddAutorizado').disabled = true;
	    document.getElementById('btnAddAutorizado').styleClass = "BotaoPadraoEstendidoInativo";
	    
	    document.getElementById('btnAddAutorizador').disabled = false;
	    document.getElementById('btnAddAutorizador').styleClass = "BotaoPadraoEstendido";
	}
}
*/


function tratarAdicaoAutorizado(){
	
	if ( document.getElementById('hidTemMaisAutorizadores').value=='S' ) {
		Richfaces.showModalPanel('modalPanelSelectAutorizador');
	}
	document.getElementById('itResponsavel').value='';
}

function tratarPosCarga(){
	document.getElementById('btnNovaCarga').style="visibility:visible";
	if (document.getElementById('hidNumeroAnoGuia').value != '') {
		document.getElementById('btnImprimirGuia').style="visibility:visible";
	} else {
		document.getElementById('btnImprimirGuia').style="visibility:hidden";
	}
}

// utilizada no onsubmit, requer retorno true para o submit do componente
function exibeMensagem() {
	exibirMsgProcessando(true);
	return true;
}

function limitTextArea(element, limit) {

	if (element.value.length > limit) {
	   alert('Limite do campo foi excedido.');
	   element.value = element.value.substring(0, limit);
	}

} 

</script>

<f:view>
	<a4j:page pageTitle="::.. Autorização para Carga dos Autos ..::"
		onload="document.getElementById('idProcesso').focus();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Autorizar Carga dos Autos" />
			</jsp:include>
			<a name="ancora"></a>
		</h:form>

		
		<a4j:form id="form" prependId="false" onreset="" styleClass="Moldura" style="padding-top:10px" >
				<div class="PainelTituloCriaTexto">
					<span> Autorização para Carga dos Autos </span>
				</div>
			<h:panelGrid id="pnlCentral" style="padding-left:0px; padding-right:0px" >

				<a4j:outputPanel id="pnlPesquisaProcesso" style="padding-bottom: 15px;" >

						<h:panelGroup id="gridMessage" rendered="#{beanAutorizarCargaAutos.temMensagem}" 
						             styleClass="WarningMessage" >
							<h:outputText id="lableMsg" value="#{beanAutorizarCargaAutos.aviso}" />
							<br/>
						</h:panelGroup>

						<div style="padding-top: 10px; padding-bottom: 0px">
							<h:outputText styleClass="Padrao" value="Processo:" />
							<h:inputText style="margin-left:10px;" id="idProcesso" 
								value="#{beanAutorizarCargaAutos.classeNumeroProcesso}">
							</h:inputText>
							<rich:hotKey
								selector="#idProcesso" key="return"
								handler="document.getElementById('btnIncluirProcesso').onclick()" />
							
							<a4j:commandButton styleClass="BotaoMais" id="btnIncluirProcesso"
								title="Inclui o processo na lista."
								actionListener="#{beanAutorizarCargaAutos.incluirProcessoNaLista}"
								onclick="exibirMsgProcessando(true);"
								oncomplete="document.getElementById('idProcesso').value=''; exibirMsgProcessando(false);"
								reRender="pnlItemProcesso" />
						</div>
				</a4j:outputPanel>
				
				<!--  grid com os processos adicionados -->
				<a4j:outputPanel ajaxRendered="true" keepTransient="false" id="pnlItemProcesso" styleClass="MolduraInterna">
					<c:if test="${not empty beanAutorizarCargaAutos.listaProcessos}">
						<h:outputText value="Processo(s) para carga" styleClass="Padrao" />
						<hr color="red" align="left" size="1px" width="100%" />
						<rich:dataTable headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="tres, tres" sortMode="single"
								value="#{beanAutorizarCargaAutos.listaProcessos}"
								var="processoParaCarga"
								binding="#{beanAutorizarCargaAutos.tabelaProcessos}"
								id="tabProcesso">

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Processo" />
									</f:facet>
									<h:panelGrid columns="3">
										<h:graphicImage value="../../../images/apenso.png"
												style="vertical-align: middle;" rendered="#{beanAutorizarCargaAutos.isApenso}" />
										<h:outputText styleClass="Padrao" style="text-align:center;"
											value="#{processoParaCarga.wrappedObject.siglaClasseProcessual}  " />
										<h:outputText styleClass="Padrao" style="text-align:center;"
											value="#{processoParaCarga.wrappedObject.numeroProcessual}" />
									</h:panelGrid>
								</rich:column>
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Remover processo da carga" />
									</f:facet>
									<a4j:commandButton image="../../../images/remove.gif" style="border:0" 
									    rendered="#{not beanAutorizarCargaAutos.isApenso}"
										actionListener="#{beanAutorizarCargaAutos.removerProcessoAction}"
										disabled="#{beanAutorizarCargaAutos.sucesso}"
										reRender="pnlItemProcesso" />
								</rich:column>
						</rich:dataTable>
					</c:if>
				</a4j:outputPanel>
				
				<!-- seleciona o responsável (suggestion)  -->
				<div style="border: 1px solid #CCCCCC;">
				<t:panelGroup style="padding-top:0px; padding-left:0px" >
					<h:selectBooleanCheckbox id="chkEntidadeGovernamental" 
											 disabled="#{beanAutorizarCargaAutos.desabilitaChkEntidadesGovernamentais}"
					                         value="#{beanAutorizarCargaAutos.chkEntidadeGovernamental}">
						<a4j:support ajaxSingle="true" event="onclick" 
									 action="#{beanAutorizarCargaAutos.atualizarChkEntidadeGovernamental}" />
					</h:selectBooleanCheckbox>
					<h:outputLabel id="lblChkEntidade" styleClass="Padrao" value="Pesquisar em entidades governamentais" for="chkEntidadeGovernamental" />
				</t:panelGroup>
				<rich:toolTip for="chkEntidadeGovernamental" style="width:350px; height:80px;" layout="block"  
				    value="Quando marcado direciona a pesquisa do responsável para os registros relacionados ao(s) processo(s) e categorizados como entidades governamentais. 
				          Pode ser que alguns deles não estejam devidamente categorizados e a pesquisa não retorne nenhum registro. 
				          Se isso ocorrer o registro da entidade deverá ser atualizado pelo sistema Processamento Inicial." ></rich:toolTip>
				<rich:toolTip for="lblChkEntidade" style="width:350px; height:80px;" layout="block"  
				    value="Quando marcado direciona a pesquisa do responsável para os registros relacionados ao(s) processo(s) e categorizados como entidades governamentais. 
				          Pode ser que alguns deles não estejam devidamente categorizados e a pesquisa não retorne nenhum registro. 
				          Se isso ocorrer o registro da entidade deverá ser atualizado pelo sistema Processamento Inicial." ></rich:toolTip>
				<t:panelGroup id="grupoResponsavel">
					<h:outputText styleClass="Padrao" value="Responsável: "
						id="labelDestino"/>
					<h:inputText id="itResponsavel" size="100" 
					    value="#{beanAutorizarCargaAutos.nomeResponsavel}" onclick="javascript:this.value='';" 
						onchange="if ( this.value!='' ) { #{rich:component('sbResponsavel')}.callSuggestion(true) }">
						<a4j:support event="onblur"
							actionListener="#{beanAutorizarCargaAutos.atualizarSessao}" />
					</h:inputText>
					<a4j:commandButton id="btnAddAutorizador" 
					  	value="Adicionar Autorizador" 
					    styleClass="BotaoPadraoEstendidoInativo" 
					    disabled="true"
					    actionListener="#{beanAutorizarCargaAutos.adicionarAutorizador}"
					    oncomplete="document.getElementById('itResponsavel').value='';" 
					    reRender="pnlPesquisaProcesso, btnAddAutorizador, btnAddAutorizado, pnlItemResponsavel, grupoResponsavel, chkEntidadeGovernamental" />
					<a4j:commandButton id="btnAddAutorizado" 
						value="Vincular Autorizado" 
						styleClass="BotaoPadraoEstendidoInativo"
						disabled="true"
						actionListener="#{beanAutorizarCargaAutos.adicionarAutorizado}" 
					    oncomplete="tratarAdicaoAutorizado()" 
						reRender="pnlPesquisaProcesso, pnlItemResponsavel, grupoResponsavel, selectAutorizador" />
	
					<rich:suggestionbox id="sbResponsavel" height="200" width="500" 
						for="itResponsavel" 
						suggestionAction="#{beanAutorizarCargaAutos.pesquisarResponsavel}"  
						var="responsavel" nothingLabel="Nenhum registro encontrado">
						<h:column>
							<h:graphicImage value="#{responsavel.urlIcone}" id="icone"	style="vertical-align: middle;" />
							<h:outputText value="#{responsavel.id} - #{responsavel.nome} - #{responsavel.cpf} - #{responsavel.papel}" />
						</h:column>
	
						<a4j:support ajaxSingle="true" event="onselect" onsubmit="exibeMensagem()" oncomplete="exibirMsgProcessando(false);" 
						    reRender="pnlPesquisaProcesso, pnlItemResponsavel, hidExisteVinculo, hidTemMaisAutorizadores, btnAddAutorizado, btnAddAutorizador, chkEntidadeGovernamental" 
							eventsQueue="ajaxQueue" ignoreDupResponses="true" action="#{beanAutorizarCargaAutos.recuperarMembros}" >
							<f:setPropertyActionListener value="#{responsavel.id}"
								target="#{beanAutorizarCargaAutos.codigoResponsavel}" />
							<f:setPropertyActionListener value="#{responsavel.cpf}"
								target="#{beanAutorizarCargaAutos.cpfResponsavel}" />
							<f:setPropertyActionListener value="#{responsavel.papel}"
								target="#{beanAutorizarCargaAutos.papelResponsavel}" />
							<f:setPropertyActionListener value="#{responsavel.nome}"
								target="#{beanAutorizarCargaAutos.nomeResponsavel}" />
							<f:setPropertyActionListener value="#{responsavel.entidadeGovernamental}"
								target="#{beanAutorizarCargaAutos.chkEntidadeGovernamental}" />
						</a4j:support>
					</rich:suggestionbox>
				</t:panelGroup>
				</div>
				<rich:toolTip for="itResponsavel" style="width:350px; height:80px;" layout="block"  
				    value="Informe o CPF, código ou nome do responsável e selecione-o após a pesquisa. Para 'entidades governamentais' informe o código ou nome." ></rich:toolTip>
				
				<!-- grid com o responsável (autorizador) e seu(s) atorizado(s) -->
				<h:panelGrid id="pnlItemResponsavel" styleClass="MolduraInterna">
					<c:if test="${not empty beanAutorizarCargaAutos.listaResponsaveis}">
						<a4j:commandButton value="Limpar tabela" styleClass="BotaoPadraoEstendido" id="btnLimparGridResponsavel"
						                   actionListener="#{beanAutorizarCargaAutos.limparReponsaveis}" 
						                   reRender="pnlItemResponsavel,btnAddAutorizador,btnAddAutorizado,grupoResponsavel,chkEntidadeGovernamental"
						                   oncomplete="document.getElementById('itResponsavel').focus();" />
						<rich:dataTable headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="tresCenter, quatro, dezLeft, cinco, tres, tresCenter" sortMode="single"
								value="#{beanAutorizarCargaAutos.listaResponsaveis}"
								var="autorizadorAutorizado"
								binding="#{beanAutorizarCargaAutos.tabelaResponsaveis}"
								id="tabResponsavel">
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Selecionar" />
									</f:facet>
									
									<h:selectOneRadio onclick="dataTableSelectOneRadio(this);" value="#{beanAutorizarCargaAutos.codigoResponsavelSelecionado}" >
											<f:selectItem itemValue="#{autorizadorAutorizado.wrappedObject.idAutorizadoAutorizador}" />
									</h:selectOneRadio>
									
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Código responsável" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:left;" 
										value="#{autorizadorAutorizado.wrappedObject.id}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Nome" />
									</f:facet>
									<h:panelGrid columns="2" >
										<h:graphicImage value="../../../images/apensoEstendido.png"
											style="vertical-align: middle;" rendered="#{not autorizadorAutorizado.wrappedObject.autorizador}" />
										<h:outputText styleClass="Padrao" 
											value="#{autorizadorAutorizado.wrappedObject.nome}" />
									</h:panelGrid>
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="CPF/CNPJ" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{autorizadorAutorizado.wrappedObject.cpf}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Papel" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{autorizadorAutorizado.wrappedObject.papel}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Excluir vínculo" />
									</f:facet>
									<a4j:commandButton image="../../../images/remove.gif" 
										title="Exclui o vínculo entre autorizado e autorizador."
										actionListener="#{beanAutorizarCargaAutos.removerResponsavelAction}" 
										rendered="#{not autorizadorAutorizado.wrappedObject.autorizador}"
										reRender="btnAddAutorizador, btnAddAutorizado, pnlItemResponsavel, hidTemMaisAutorizadores" 
										style="border:0" />
								</rich:column>
						</rich:dataTable>
					</c:if>
					<h:panelGrid columns="2"  cellpadding="0" cellspacing="0">
						<h:outputLabel styleClass="Padrao" value="Observação:" style="padding-right:5px" />
						<h:inputTextarea id="itObsDeslocamento" value="#{beanAutorizarCargaAutos.obsDeslocamento}"
						             rows="2" cols="100" 
						             onkeyup="limitTextArea(this,1000);"
									 onkeydown="limitTextArea(this,1000);"/> 
					</h:panelGrid>
					<h:panelGrid columns="2"  cellpadding="0" cellspacing="0">
						<h:outputLabel styleClass="Padrao" value="Data para devolução:" style="padding-right:5px" />
						<rich:calendar id="itDataDevolucao" value="#{beanAutorizarCargaAutos.dataDevolucao}" 
									   datePattern="dd/MM/yyyy" locale="pt_Br" />
					</h:panelGrid>
					<h:panelGrid columns="6" id="botoesRodape">
						<a4j:commandButton id="btnSalvarCarga" value="Realizar carga" styleClass="BotaoPadraoEstendido" 
						                   actionListener="#{beanAutorizarCargaAutos.salvarCarga}" 
						                   reRender="form"
						                   onclick="exibirMsgProcessando(true);" 
						                   oncomplete="exibirMsgProcessando(false);" />
						<h:commandButton value="Imprimir Guia"
		            					 id="btnImprimirGuia"
					                     actionListener="#{beanAutorizarCargaAutos.imprimirGuia}" 
  									     styleClass="BotaoPadraoEstendidoInativo" disabled="true" />
  						<a4j:commandButton id="btnSalvarVinculo" value="Salvar vínculo" styleClass="BotaoPadraoEstendidoInativo"
						                   onclick="exibirMsgProcessando(true);" 
						                   oncomplete="exibirMsgProcessando(false);" 
  										   actionListener="#{beanAutorizarCargaAutos.salvarVinculo}" disabled="true" reRender="pnlItemResponsavel" />
						<a4j:commandButton id="btnNovaCarga" value="Nova carga" styleClass="BotaoPadraoInativo" disabled="true" 
						                   actionListener="#{beanAutorizarCargaAutos.novaCarga}" reRender="form" 
						                   oncomplete="document.getElementById('idProcesso').focus();" />
						<a4j:commandButton id="btnNovaPessoa" value="Novo autorizado" styleClass="BotaoPadraoEstendidoInativo" 
						                   disabled="true" actionListener="#{beanAutorizarCargaAutos.prepararPopupAutorizado}" reRender="formNovoAutorizado"
						                   oncomplete="Richfaces.showModalPanel('modalPanelNovoAutorizado');" /> 
						<rich:toolTip for="btnNovaPessoa" style="width:350px; height:80px;" layout="block" 
						    value="Clique aqui para incluir um novo responsável (advogado, preposto ou estagiário) somente se ele não foi localizado pela pesquisa. Dica: a pesquisa pelo CPF ou código é mais rápida e garante maior exatidão." />
					</h:panelGrid>
					<h:inputHidden id="hidResponsavelSelecionado" value="#{beanAutorizarCargaAutos.codigoResponsavelSelecionado}"/>
					<h:inputHidden id="hidExisteVinculo" value="#{beanAutorizarCargaAutos.existeVinculo}"/>
					<h:inputHidden id="hidTemMaisAutorizadores" value="#{beanAutorizarCargaAutos.temMaisAutorizadores}"/>
					<h:inputHidden id="hidNumeroAnoGuia" value="#{beanAutorizarCargaAutos.numeroAnoGuia}"/>
				</h:panelGrid>
			</h:panelGrid>
		</a4j:form>
		
		<!-- popup para seleção do autorizador quando houver mais de um na inclusão de um autorizado -->
		<rich:modalPanel id="modalPanelSelectAutorizador" width="630" height="150"
			keepVisualState="true" style="overflow:auto">
			<f:facet name="header">
				<h:outputText value="Selecionar autorizador" />
			</f:facet>
			<a4j:form>
				<t:panelGroup>
						<h:panelGrid columns="1">
							<h:outputText styleClass="Padrao" value="Existe mais de um autorizador incluído." />
						</h:panelGrid>
						<h:panelGrid columns="1">
							<h:outputText styleClass="Padrao" value="Selecione o autorizador desejado para o seu novo autorizado." />
						</h:panelGrid>
						<h:outputText styleClass="Padrao" value="Autorizador: " />
						<h:selectOneListbox id="selectAutorizador" value="#{beanAutorizarCargaAutos.codigoAutorizadorSelecionado}" style="margin-left:0px; width:100%" size="1">
							<f:selectItems value="#{beanAutorizarCargaAutos.listaAutorizadores}" />
							<a4j:support event="onchange" />
						</h:selectOneListbox>
				</t:panelGroup>
				<t:div style="margin-top:10px" >
				<a4j:commandButton value="Cancelar" styleClass="BotaoPadrao"
					onclick="Richfaces.hideModalPanel('modalPanelSelectAutorizador');"
					id="popup_btnCancelarSelectAutorizador" />
				<h:commandButton value="Confirmar" styleClass="BotaoPadrao"
					id="popup_btnConfirmarSelectAutorizador"
					actionListener="#{beanAutorizarCargaAutos.confirmarAutorizador}"
					onclick="Richfaces.hideModalPanel('modalPanelSelectAutorizador');" />
				</t:div>
			</a4j:form>
		</rich:modalPanel>
		
		<!-- popup para inclusão de novo autorizado a partir de um autorizador incluído-->
		<rich:modalPanel id="modalPanelNovoAutorizado" width="700" height="500"	keepVisualState="true" style="overflow:auto">
			<f:facet name="header">
				<h:outputText value="Novo autorizado" />
			</f:facet>
			<a4j:form id="formNovoAutorizado">
				<f:subview id="viewMessagesOrigem">
					<a4j:outputPanel id="outputPanelMessages" ajaxRendered="true"
						keepTransient="false">

						<t:panelGrid id="pnlMessagesOrigem" forceId="true"
							rendered="#{not empty facesContext.maximumSeverity}"
							cellpadding="0" cellspacing="0" columns="1"
							style="width: 100%; text-align: center;">

							<t:messages errorClass="ErrorMessage" style="text-align: left"
								infoClass="InfoMessage" warnClass="WarningMessage"
								showSummary="true" showDetail="true" layout="table" />
						</t:panelGrid>

					</a4j:outputPanel>
				</f:subview>
				<c:if test="${not empty beanAutorizarCargaAutos.listaAutorizadores}">
					<t:panelGroup>
							<h:panelGrid columns="1">
								<h:outputText styleClass="Padrao" value="Existe mais de um autorizador incluído." />
							</h:panelGrid>
							<h:panelGrid columns="1">
								<h:outputText styleClass="Padrao" value="Selecione o autorizador desejado para o seu novo autorizado." />
							</h:panelGrid>
							<h:outputText styleClass="Padrao" value="Autorizador: " />
							<h:selectOneListbox id="selectAutorizador" value="#{beanAutorizarCargaAutos.codigoAutorizadorSelecionado}" style="margin-left:0px; width:100%" size="1">
								<f:selectItems value="#{beanAutorizarCargaAutos.listaAutorizadores}" />
								<a4j:support event="onchange" />
							</h:selectOneListbox>
					</t:panelGroup>
				</c:if>
				<t:panelGroup>
					<a4j:outputPanel styleClass="MolduraInterna" keepTransient="false"	id="pnlNovoAutorizado">
						<div id="idOrigemPanel" style="padding: 5px;">
							<div style="margin-top:10px;">
								<span class="Padrao">Tipo do autorizado:</span>
								<span>
									<h:selectOneRadio value="#{beanAutorizarCargaAutos.tipoPapelNovoAutorizado}" 
										id="tipoPapelJurisdicionado">
										<f:selectItem itemValue="A" itemLabel="Advogado" />
										<f:selectItem itemValue="E" itemLabel="Estagiário" />
										<f:selectItem itemValue="P" itemLabel="Preposto" />
										<a4j:support ajaxSingle="true" event="onclick" reRender="obrigatorioOAB"
											eventsQueue="ajaxQueue" ignoreDupResponses="true" actionListener="#{beanAutorizarCargaAutos.atualizaLabelOAB}">
										</a4j:support>
									</h:selectOneRadio>
								</span>
							</div>
							<div style="margin-top:10px;">
								<span class="Padrao">* Nome:</span>
								<span>
									<h:inputText size="100" 
										value="#{beanAutorizarCargaAutos.novoAutorizado.nome}"
										onkeyup="caixaAlta(this);">
									</h:inputText>	
								</span>	
							</div>
							
							<div style="margin-top:10px;">
								<span class="Padrao"> Email:</span>
								<span>
									<h:inputText size="100" style="margin-left:11px"
										value="#{beanAutorizarCargaAutos.novoAutorizado.email}" >
									</h:inputText>	
								</span>	
							</div>
							
							<div style="margin-top:10px;">
								<span class="Padrao">* CPF:</span>
								<span>
									<h:inputText size="15" style="margin-left:10px;" 
										value="#{beanAutorizarCargaAutos.cpfNovoAutorizado}" maxlength="11" />
								</span>
								<span class="Padrao"> RG:</span>
								<span>
									<h:inputText size="15" 
										value="#{beanAutorizarCargaAutos.rgNovoAutorizado}" id="inputRgID"
										onkeypress="return mascaraInputNumerico(this, event)"/>
								</span>
								<!--  caracter '*' para OAB renderizado dinamicamente -->
								<h:outputText id="obrigatorioOAB" value="* " />
								<span class="Padrao">OAB</span>
								<span>
									<h:inputText size="15" 
										value="#{beanAutorizarCargaAutos.oabNovoAutorizado}"/>
									<h:selectOneMenu style="margin-left: 5px;"
										value="#{beanManterPessoaCarga.ufOAB}">
										<f:selectItems value="#{beanManterPessoaCarga.itensUfOAB}"/>
									</h:selectOneMenu>												
								</span>	
							</div>
							<div style="margin-top:10px;">
								<span class="Padrao">* Validade do Cadastro:</span>
								<span>
									<rich:calendar id="itDataValidadeJuris" 
										value="#{beanManterPessoaCarga.dataValidadeCadastro}" 
										datePattern="dd/MM/yyyy" locale="pt_Br" />										
								</span>	
							</div>
							<h:panelGrid id="panelObservacao">
								<h:outputText styleClass="Padrao" value="Observação:" />
								<h:inputTextarea id="itObservacao" rows="2" cols="100"   
									value="#{beanAutorizarCargaAutos.obsNovoAutorizado}" onkeyup="limitTextArea(this,1000);" 
									onkeydown="limitTextArea(this,1000);" />
							</h:panelGrid>
						</div>
					</a4j:outputPanel>

				</t:panelGroup>
				<t:div style="margin-top:10px" >
					<a4j:commandButton value="Salvar" styleClass="BotaoPadrao"
						id="popup_btnSalvarNovoAutorizado" reRender="pnlItemResponsavel" 
						actionListener="#{beanAutorizarCargaAutos.salvarAutorizado}"
						oncomplete="if(#{beanAutorizarCargaAutos.novoAutorizadoSalvo}) Richfaces.hideModalPanel('modalPanelNovoAutorizado');" />
					<a4j:commandButton value="Cancelar" styleClass="BotaoPadrao"
						onclick="Richfaces.hideModalPanel('modalPanelNovoAutorizado');"
						id="popup_btnCancelarNovoAutorizado" />
				</t:div>
				</a4j:form>
		</rich:modalPanel>

		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>