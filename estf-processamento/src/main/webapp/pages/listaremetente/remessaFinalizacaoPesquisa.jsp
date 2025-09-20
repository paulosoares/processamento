<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<f:view>
	<a4j:page pageTitle="::.. Principal ..::">

		<h:form id="form" prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Pesquisar Lista de Remessa" />
			</jsp:include>

			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">
					<a4j:outputPanel id="pnlPrincipalPesquisa" ajaxRendered="true">
						<h:panelGrid styleClass="PadraoTituloPanel" id="pnlFiltro"
							cellpadding="0" cellspacing="0">
							<h:outputText value="Pesquisar Lista de Remessa" />
						</h:panelGrid>
					</a4j:outputPanel>
				</h:panelGrid>
			</h:panelGrid>

			<h:panelGrid columns="1" id="painelPesquisaListaRemessa">
				<rich:panel bodyClass="inpanelBody" id="painelCamposPesquisa">
					<f:facet name="header">
						<b>Pesquisa:</b>
					</f:facet>
					<ul>
						<table>
							<tr>
								<td><h:outputText value="Número/Ano da lista" /></td>
								<td><t:inputText size="30" id="idNumeroLista"
										value="#{beanListaRemessa.numeroAnoListaRemessa}" /></td>
							</tr>
							<tr>
								<td><h:outputText value="Data de Criação" /></td>
								<td><rich:calendar id="idDtCriacaoInicial"
										value="#{beanListaRemessa.pesquisaListaRemessaDto.dataCriacaoInicio}"
										datePattern="dd/MM/yyyy" locale="pt_Br" /> <h:outputLabel
										styleClass="Padrao" value=" e "></h:outputLabel> <rich:calendar
										id="idDtCriacaoFinal"
										value="#{beanListaRemessa.pesquisaListaRemessaDto.dataCriacaoFim}"
										datePattern="dd/MM/yyyy" locale="pt_Br" /></td>
							</tr>
						</table>
					</ul>
				</rich:panel>

				<rich:panel bodyClass="inpanelBody">
					<table>
						<tr>
							<td><a4j:commandButton styleClass="BotaoPesquisar"
									style="margin-left:15px;" value="Pesquisar" id="btnPesquisa"
									actionListener="#{beanListaRemessa.pesquisarFinalizacao}"
									ignoreDupResponses="true" reRender="pnlResultadoPesquisa"
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" /></td>
							<td><a4j:commandButton styleClass="BotaoPadrao"
									style="margin-left:15px;" value="Limpar" id="btnLimpar"
									actionListener="#{beanListaRemessa.limparCampos}"
									ignoreDupResponses="true"
									reRender="painelCamposPesquisa,pnlResultadoPesquisa" /></td>
						</tr>
					</table>
				</rich:panel>
			</h:panelGrid>

			<a4j:outputPanel id="pnlResultadoPesquisa" ajaxRendered="true">
				<c:if test="${not empty beanListaRemessa.conjuntoRemessas}">
					<h:panelGrid styleClass="PadraoTituloPanel" id="pnlResultadoTotal">
						<h:outputText
							value="Resultado da Pesquisa - Quant. de Registros: #{beanListaRemessa.qtdRegistros}" />
					</h:panelGrid>
					<rich:datascroller align="left"
						for="tbPesquisaListaRemessaFinalizacao" maxPages="100"
						reRender="sc2" id="sc1" style="width: 550px;" />
					<rich:dataTable id="tbPesquisaListaRemessaFinalizacao"
						value="#{beanListaRemessa.conjuntoRemessas}" var="pesquisa"
						columnClasses="center" rowClasses="linha-par, linha-impar"
						rows="50" style="width: 550px;" reRender="ds">
						<rich:column style="text-align: center;" 
							sortBy="#{pesquisa.numeroListaRemessa}" width="50px">
							<f:facet name="header">
								<h:outputText value="Nº Lista" />
							</f:facet>
							<h:outputText value="#{pesquisa.numeroListaRemessa}/#{pesquisa.anoListaRemessa}" />
						</rich:column>
						<rich:column style="text-align: center;"
							sortBy="#{pesquisa.dataCriacao.time}" width="200px">
							<f:facet name="header">
								<h:outputText value="Dt Criação" />
							</f:facet>
							<h:outputText value="#{pesquisa.dataCriacao.time}">
								<f:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>
						</rich:column>
						<rich:column style="text-align: center;" 
							sortBy="#{pesquisa.tipoServico.nome}" width="50px">
							<f:facet name="header">
								<h:outputText value="Tipo/Serviço" />
							</f:facet>
							<h:outputText value="#{pesquisa.tipoServico.nome}" />
						</rich:column>
						<rich:column style="text-align: center;" width="5px">
							<f:facet name="header">
								<h:outputText value="Finalizar" />
							</f:facet>
							<h:commandLink action="#{beanListaRemessa.abrirFinalizacao}"
								rendered="#{not pesquisa.finalizada}">
								<h:graphicImage value="/images/setabaixo.gif" width="20"
									height="20"
									title="Finalizar Lista Nº #{pesquisa.numeroListaRemessa}" />
								<f:setPropertyActionListener value="#{pesquisa}"
									target="#{beanListaRemessa.listaRemessas}" />
							</h:commandLink>

						</rich:column>
						<f:facet name="footer">
							<rich:datascroller align="left"
								for="tbPesquisaListaRemessaFinalizacao" maxPages="100" id="sc2"
								reRender="sc1" />
						</f:facet>
					</rich:dataTable>
				</c:if>
			</a4j:outputPanel>
			<jsp:include page="/pages/template/footer.jsp" flush="true" />
		</h:form>
	</a4j:page>
</f:view>