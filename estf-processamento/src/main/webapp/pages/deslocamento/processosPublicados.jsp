<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page pageEncoding="ISO-8859-1"%>

<jsp:include page="/pages/template/header.jsp" flush="true" />

<f:view>
	<a4j:page pageTitle="::.. Consulta de deslocamentos para a seção de acórdãos ..::">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Consultar deslocamentos de acórdãos" />
			</jsp:include>
			<a name="ancora"></a>
		</h:form>

		<a4j:form id="form" prependId="false" onreset="">

			<script type="text/javascript">

			</script>
			<h:panelGroup id="panelPesquisa" layout="block" style="margin-left: 10px;">
				<h:panelGrid columns="2">
					<h:outputText styleClass="Padrao"
						value="Consultar processos publicados: " />
					<h:selectOneRadio
						value="#{beanProcessoPublicado.deslocadoParaSecaoAcordao}"
						immediate="true"
						valueChangeListener="#{beanDesignarMinistroAcordao.atualizaOpcaoDeslocamentoParaAcordao}"
						styleClass="Padrao" layout="lineDirection">
						<f:selectItem itemValue="S"
							itemLabel="Deslocados para seção de acórdãos" />
						<f:selectItem itemValue="N"
							itemLabel="Não deslocados para seção de acórdãos" />
						<a4j:support event="onclick" ajaxSingle="true" reRender="motivo" />
					</h:selectOneRadio>

				</h:panelGrid>
				<h:panelGrid columns="2" id="pnlPesquisa" style="vertical-align:top"
					columnClasses="PainelTop, PainelTop">
					<h:outputText styleClass="Padrao" value="Para qual data de publicação? " />
					<rich:calendar id="dtPublicacao"
						value="#{beanProcessoPublicado.dataPublicacao}"
						datePattern="dd/MM/yyyy" locale="pt_Br" />
				</h:panelGrid>
				<h:panelGrid columns="2">
					<a4j:commandButton id="btnPesquisar"
						actionListener="#{beanProcessoPublicado.pesquisar}"
						styleClass="BotaoPadrao" value="Pesquisar"
						onclick="exibirMsgProcessando(true);"
						oncomplete="exibirMsgProcessando(false);" />
					<a4j:commandButton id="btnLimpar" styleClass="BotaoPadrao"
						value="Limpar" reRender="form"
						actionListener="#{beanProcessoPublicado.limpar}" />
				</h:panelGrid>
			</h:panelGroup>

			<!--  listagem de processos -->
			<a4j:outputPanel ajaxRendered="true" keepTransient="false" id="pnlProcessosPublicados" styleClass="MolduraInterna">
				<c:if test="${not empty beanProcessoPublicado.processosPublicados}">
				<br />
				<h:panelGroup style="margin-left: 10px; margin-right: 10px;" layout="block">
				<h:outputText styleClass="Padrao" 
					value="Total de processos: #{fn:length(beanProcessoPublicado.processosPublicados)}" />
					<rich:dataTable headerClass="DataTableDefaultHeader" styleClass="DataTableDefault" footerClass="DataTableDefaultFooter"
						rowClasses="DataTableRow, DataTableRow2" columnClasses="tres, sete, tres" sortMode="single"
						value="#{beanProcessoPublicado.processosPublicados}" var="listaResult" rows="10"
						id="tabProcessosPublicados">

						<rich:column sortOrder="#{columns.sortOrder}">
							<f:facet name="header">
								<h:outputText value="Processo" />
							</f:facet>
								
							<h:outputText styleClass="Padrao" style="text-align:center;" value="#{listaResult.wrappedObject.siglaProcessual}" />
							<h:outputText styleClass="Padrao" style="text-align:center;" value=" " />
							<h:outputText styleClass="Padrao" style="text-align:center;" value="#{listaResult.wrappedObject.numeroProcessual}" />
						</rich:column>

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Recurso" />
							</f:facet>
							<h:outputText styleClass="Padrao" style="text-align:center;" value="#{listaResult.wrappedObject.recurso}" />
						</rich:column>

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Tipo" />
							</f:facet>
							<h:outputText styleClass="Padrao" style="text-align:center;" value="#{listaResult.wrappedObject.tipoMeio}" />
						</rich:column>

					</rich:dataTable>
					<rich:datascroller for="tabProcessosPublicados" maxPages="10">
						<f:facet name="first">
							<h:outputText value="Primeira" />
						</f:facet>
						<f:facet name="last">
							<h:outputText value="Última" />
						</f:facet>
					</rich:datascroller>
					<br />
					<h:commandButton value="Imprimir" styleClass="BotaoPadrao" action="#{beanProcessoPublicado.imprimir}"></h:commandButton>
				</h:panelGroup>
				</c:if>
			</a4j:outputPanel>

		</a4j:form>

		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>