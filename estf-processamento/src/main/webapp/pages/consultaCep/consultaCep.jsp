<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<rich:modalPanel id="idModalPanelPesquisaCepPorEndereco"
	keepVisualState="true"
	style="overflow:auto; position: fixed; top: 50; left: 50;"
	autosized="true" moveable="false">	
	<f:subview id="viewMessagesPanelPesquisaCepPorEndereco">
		<a4j:outputPanel id="outputPanelMessagesPesquisaCepPorEndereco"
			ajaxRendered="true" keepTransient="false">
			<t:panelGrid id="pnlMessagesPesquisaCepPorEndereco" forceId="true"
				rendered="#{not empty facesContext.maximumSeverity}" cellpadding="0"
				cellspacing="0" columns="1" style="width: 100%; text-align: center;">
				<t:messages errorClass="ErrorMessage" style="text-align: left"
					infoClass="InfoMessage" warnClass="WarningMessage"
					showSummary="true" showDetail="true" layout="table" />
			</t:panelGrid>
		</a4j:outputPanel>
	</f:subview>

	<a4j:outputPanel id="idPanelPesquisaCepPorEndereco" ajaxRendered="true">
		<h:form id="ifFormFiltroPesquisaCepPorEndereco" prependId="false">
			<h:panelGrid id="idPanelTituloPesquisaCepPorEndereco"
				styleClass="PadraoTituloPanel" cellpadding="0" cellspacing="0">
				<h:outputText value="Pesquisa de CEP por endereço" />
			</h:panelGrid>

			<h:panelGrid id="idPanelFiltroPesquisaCepPorEndereco">
				<rich:panel bodyClass="inpanelBody">
					<f:facet name="header">
						<b>PESQUISA:</b>
					</f:facet>
					<h:inputText id="idCampoFiltroCep" size="60"
						value="#{beanPesquisaCepEndereco.filtro}" />
				</rich:panel>
			</h:panelGrid>

			<h:panelGrid id="idPanelBotoesPesquisaCepPorEndereco"
				styleClass="Moldura">
				<h:panelGrid columns="2">
					<a4j:commandButton id="btnPesquisarCep" styleClass="BotaoPesquisar"
						value="Pesquisar" process="idCampoFiltroCep"
						actionListener="#{beanPesquisaCepEndereco.buscarCep}" />
					<a4j:commandButton id="btnCancelarPesquisarCep"
						styleClass="BotaoPesquisar" value="Fechar" process=""
						oncomplete="Richfaces.hideModalPanel('idModalPanelPesquisaCepPorEndereco')"
						actionListener="#{beanPesquisaCepEndereco.cancelarPesquisa}" />
				</h:panelGrid>
			</h:panelGrid>
		</h:form>
	</a4j:outputPanel>
	
		<a4j:outputPanel id="idPanelResultadoPesquisaCepPorEndereco"
			ajaxRendered="true">
			<h:form id="ifFormResultadoPesquisaCepPorEndereco" prependId="false">
				<h:panelGrid id="idPanelQtdRegistrosResultadoPesquisaCepPorEndereco"
					styleClass="PadraoTituloPanel"
					rendered="#{beanPesquisaCepEndereco.resultadosObtidos}">
					<h:outputText
						value="Resultado da Pesquisa - Quant. de Registros: #{beanPesquisaCepEndereco.qtdRegistrosEncontrados}" />
				</h:panelGrid>
			<div style="overflow: auto; width: 700px; height: 500px">
				<rich:datascroller id="sc1PesquisaCepPorEndereco" align="left"
					for="idDataTableEnderecosEncontrados" maxPages="100"
					reRender="sc2PesquisaCepPorEndereco"
					rendered="#{beanPesquisaCepEndereco.resultadosObtidos}" />

				<rich:dataTable id="idDataTableEnderecosEncontrados" var="pesquisa"
					columnClasses="center" rowClasses="linha-par, linha-impar"
					rows="20" reRender="ds"
					value="#{beanPesquisaCepEndereco.enderecos}"
					rendered="#{beanPesquisaCepEndereco.resultadosObtidos}">

					<rich:column sortBy="#{pesquisa.cliente}" width="50px">
						<f:facet name="header">
							<h:outputText value="Cliente" />
						</f:facet>
						<h:outputText value="#{pesquisa.cliente}" />
					</rich:column>

					<rich:column sortBy="#{pesquisa.uf}" width="50px">
						<f:facet name="header">
							<h:outputText value="UF" />
						</f:facet>
						<h:outputText value="#{pesquisa.uf}" />
					</rich:column>

					<rich:column sortBy="#{pesquisa.municipio}" width="50px">
						<f:facet name="header">
							<h:outputText value="Município" />
						</f:facet>
						<h:outputText value="#{pesquisa.municipio}" />
					</rich:column>

					<rich:column sortBy="#{pesquisa.bairro}" width="50px">
						<f:facet name="header">
							<h:outputText value="Bairro" />
						</f:facet>
						<h:outputText value="#{pesquisa.bairro}" />
					</rich:column>

					<rich:column sortBy="#{pesquisa.logradouro}" width="50px">
						<f:facet name="header">
							<h:outputText value="Logradouro" />
						</f:facet>
						<h:outputText value="#{pesquisa.logradouro}" />
					</rich:column>

					<rich:column sortBy="#{pesquisa.cep}" width="50px">
						<f:facet name="header">
							<h:outputText value="Cep" />
						</f:facet>
						<h:outputText value="#{pesquisa.cep}" />
					</rich:column>

					<rich:column style="text-align: center;" width="5px">
						<f:facet name="header">
							<h:outputText value="Selecionar" />
						</f:facet>
						<a4j:commandLink id="idLinkSelecionarEnderecoCep"
							oncomplete="Richfaces.hideModalPanel('idModalPanelPesquisaCepPorEndereco')"
							action="#{beanPesquisaCepEndereco.selecionar}"
							reRender="#{beanPesquisaCepEndereco.nomeComponenteAtualizar}">
							<h:graphicImage url="/images/cruz.png" title="Selecionar" />
							<f:setPropertyActionListener value="#{pesquisa}"
								target="#{beanPesquisaCepEndereco.endereco}" />
						</a4j:commandLink>
					</rich:column>

					<f:facet name="footer">
						<rich:datascroller id="sc2PesquisaCepPorEndereco"
							for="idDataTableEnderecosEncontrados" maxPages="50"
							reRender="sc1PesquisaCepPorEndereco" align="left" />
					</f:facet>
				</rich:dataTable>
				</div>
			</h:form>
		</a4j:outputPanel>	
</rich:modalPanel>