<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />


<f:view>
	<a4j:page pageTitle="::.. Principal ..::">
		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Copiar peças entre Processo" />
			</jsp:include>
			<a name="ancora"></a>
		</h:form>
	</a4j:page>
	<a4j:form id="form" prependId="false">
		<rich:panel style="width: 1000px;">
			<f:facet name="header">
				<h:outputText value="Copiar Peças" />
			</f:facet>
			<h:outputText styleClass="Padrao" value="Origem: " />
			<h:inputText id="itProcesso" size="30"
				value="#{beanCopiarPecas.siglaNumeroOrigem}"
				onkeyup="if ( this.value!='' ) { #{rich:component('sbProcesso')}.callSuggestion(true) }" />
			<rich:suggestionbox id="sbProcesso" height="200" width="200"
				for="itProcesso"
				suggestionAction="#{beanCopiarPecas.pesquisaSuggestionBox}"
				var="processo" nothingLabel="Nenhum registro encontrado">
				<h:column>
					<h:outputText
						value="#{processo.identificacao} #{processo.descricao}" />
				</h:column>
				<a4j:support ajaxSingle="true" event="onselect"
					reRender="listaAssociados">
					<f:setPropertyActionListener value="#{processo.id}"
						target="#{beanCopiarPecas.processo}" />
					<f:setPropertyActionListener
						value="#{processo.identificacao} #{processo.descricao}"
						target="#{beanCopiarPecas.siglaNumeroOrigem}" />
				</a4j:support>
			</rich:suggestionbox>
			<br>
			<h:outputText styleClass="Padrao" value="Destino: " />
			<h:inputText id="itObjetoIncidente" size="30"
				value="#{beanCopiarPecas.siglaNumeroDestino}"
				onkeyup="if ( this.value!='' ) { #{rich:component('sbObjetoIncidente')}.callSuggestion(true) }" />

			<rich:suggestionbox id="sbObjetoIncidente" for="itObjetoIncidente"
				height="200" width="600"
				suggestionAction="#{beanCopiarPecas.pesquisarObjetosIncidentesSuggestionBox}"
				var="objetoIncidente" nothingLabel="Nenhum registro encontrado"
				ignoreDupResponses="true" eventsQueue="ajaxQueue">
				<h:column>
					<h:outputText value="#{objetoIncidente.identificacao}" />
				</h:column>
				<a4j:support ajaxSingle="true" event="onselect"
					ignoreDupResponses="true">
					<f:setPropertyActionListener value="#{objetoIncidente.id}"
						target="#{beanCopiarPecas.objetoIncidente}" />
					<f:setPropertyActionListener
						value="#{objetoIncidente.identificacao}"
						target="#{beanCopiarPecas.siglaNumeroDestino}" />
				</a4j:support>
			</rich:suggestionbox>

			<rich:panel style="width: 350px">
				<f:facet name="header">
					<h:outputText value="Inserir as Peças" />
				</f:facet>
				<h:selectOneRadio id="numeroSequencia"
					value="#{beanCopiarPecas.numeroSequencia}">
					<f:selectItems value="#{beanCopiarPecas.escolherSequencia}" />
				</h:selectOneRadio>
			</rich:panel>

			<h:selectBooleanCheckbox id="apagarPecas"
				value="#{beanCopiarPecas.apagarPecas}" />
			<h:outputText styleClass="Padrao"
				value="Apagar as peças do processo origem." />
			<br>
			<h:selectBooleanCheckbox id="inserirInformacao"
				value="#{beanCopiarPecas.inserirInformacao}" />
			<h:outputText styleClass="Padrao"
				value="Inserir a informação de Sigla e Número do processo na Descrição da Peça." />

			<a4j:outputPanel ajaxRendered="true" keepTransient="false"
				id="listaAssociados" styleClass="MolduraInterna">
				<c:if test="${not empty beanCopiarPecas.listaAssociados}">
					<rich:scrollableDataTable height="150px" width="50%"
						value="#{beanCopiarPecas.listaAssociados}"
						id="listaAssociadosDataTable" var="wrappedAssociados">
						<rich:column  sortable="false" width="50px">
							<f:facet name="header">
								<a4j:commandButton image="../../images/setabaixo.gif"
									action="#{beanCopiarPecas.marcarTodosDocumentos}" />
							</f:facet>
							<div align="center">
								<h:selectBooleanCheckbox value="#{wrappedAssociados.checked}" />
							</div>
						</rich:column>
						<rich:column sortable="false" width="900px">
							<f:facet name="header">
								<h:outputText value="Descrição" />
							</f:facet>
							<h:outputText styleClass="Padrao"
								value="#{wrappedAssociados.wrappedObject.numeroOrdemPeca} - #{wrappedAssociados.wrappedObject.tipoPecaProcesso.descricao} - #{wrappedAssociados.wrappedObject.descricaoPeca}" />
						</rich:column>
					</rich:scrollableDataTable>
				</c:if>
			</a4j:outputPanel>

			<div align="center">
				<t:panelGrid columns="2">
					<t:panelGroup>
						<a4j:commandButton styleClass="BotaoPadraoEstendido"
							style="margin-left:15px;" value="Copiar Peças" id="copiarPecas"
							action="#{beanCopiarPecas.copiarPecasAction}"
							reRender="form,apagarPecas,inserirInformacao"
							ignoreDupResponses="true" onclick="exibirMsgProcessando(true);"
							oncomplete="exibirMsgProcessando(false);" />
					</t:panelGroup>
					<t:panelGroup>
						<a4j:commandButton styleClass="BotaoPadrao"
							style="margin-left:15px;" value="Limpar" id="limpar"
							action="#{beanCopiarPecas.limparAction}"
							reRender="form,apagarPecas,inserirInformacao"
							ignoreDupResponses="true" onclick="exibirMsgProcessando(true);"
							oncomplete="exibirMsgProcessando(false);" />
					</t:panelGroup>
				</t:panelGrid>
			</div>

		</rich:panel>
	</a4j:form>
</f:view>