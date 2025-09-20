<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<script>
//verifica se dentro da string digitada, possui algum número, se tiver,
//exibe o suggestionBox
function exibirSuggestionBoxPesquisaProcesso(campoProcesso, suggestionBox) {
	var valor = campoProcesso.value;
	var possui = false;
	var i;
	for (i = 0; i < valor.length; i++) {
		// possui valor entre 0 e 9
		if (valor.charCodeAt(i) >= 48 && valor.charCodeAt(i) <= 57) {
			possui = true;
			break;
		}
	}

	if (possui) {
		suggestionBox.callSuggestion(true);
	} else {
		suggestionBox.hide();
	}
}

function exibeMensagemNoSubmit(){
	exibirMsgProcessando(true);
	return true;
}

</script>

<f:view>
	<a4j:page pageTitle="::.. Autorização para Baixa de Processo ..::"
		onload="document.getElementById('idProcesso').focus();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Autorizar Baixa de Processo" />
			</jsp:include>
			<a name="ancora"></a>
		</h:form>

		<a4j:form id="form" prependId="false" onreset="" >
		
			<h:panelGrid id="pnlCentral" style="padding-left:5px; padding-right:5px" >

					<a4j:outputPanel id="pnlPesquisaProcesso" >

						<div class="PainelTituloCriaTexto" style="width:100%" >
							<span>Pesquisa por Processo:</span>
						</div>

						<div style="padding-top: 10px;">
							<h:outputText styleClass="Padrao" value="Processo:" />

							<h:inputText style="margin-left:10px;" id="idProcesso"
								onkeypress="return noEnter(event);"
								value="#{beanAutorizarBaixaProcesso.identificacaoProcesso}"
								onclick="exibirSuggestionBoxPesquisaProcesso(this, #{rich:component('sgPesquisaProcesso')});">
							</h:inputText>

							<span style="padding-left: 5px;"></span> 
							<rich:suggestionbox
									id="sgPesquisaProcesso"
									suggestionAction="#{beanAutorizarBaixaProcesso.pesquisaSuggestionBox}"
									for="idProcesso" var="result"
									fetchValue="#{result.identificacao}"
									nothingLabel="Nenhum registro encontrado." width="400"
									ignoreDupResponses="true" eventsQueue="ajaxQueue" minChars="1">

									<a4j:support ajaxSingle="true" event="onselect" onsubmit="exibeMensagemNoSubmit()" oncomplete="exibirMsgProcessando(false);"
										eventsQueue="ajaxQueue" ignoreDupResponses="true"
										reRender="pnlCentral,chkAutorizadoBaixa">
										<f:setPropertyActionListener value="#{result}"
											target="#{beanAutorizarBaixaProcesso.processoSelecionado}" />
										<f:setPropertyActionListener value="#{result.identificacao}"
											target="#{beanAutorizarBaixaProcesso.identificacaoProcesso}" />
									</a4j:support>

									<h:column>
										
										<h:outputText rendered="#{result.eletronico}" value="e"
												style="color: red; font-weight: bold;" />
										
										<h:outputText value="#{result.identificacao}"
											styleClass="oiSuggestion" />
										<h:outputText value=" » " />
										<h:outputText value="#{result.ministroRelatorAtual.nome}"
											styleClass="green" />
										
									</h:column>
								</rich:suggestionbox>
						</div>
				</a4j:outputPanel>
				
				<t:panelGroup>
					<h:selectBooleanCheckbox id="chkAutorizadoBaixa" value="#{beanAutorizarBaixaProcesso.chkAutorizadoBaixa}" />
					<h:outputLabel styleClass="Padrao" value="Autorizado para Baixa" for="chkAutorizadoBaixa" />
				</t:panelGroup>
				
				<h:panelGrid>
					<a4j:commandButton value="Salvar" styleClass="BotaoPadrao" 
					     actionListener="#{beanAutorizarBaixaProcesso.salvarAutorizacao}" />
				</h:panelGrid>
				
			</h:panelGrid>
		</a4j:form>

		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>