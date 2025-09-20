<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<jsp:include page="/pages/template/header.jsp" flush="true" />
<style type="text/css">
<!--
//
CSS
.linha-par {
	background-color: #FCFFFE;
}

.linha-impar {
	background-color: #ECF3FE;
}
-->
</style>
<f:view>
	<a4j:page pageTitle="::.. Principal ..::">

		<h:form id="formPesquisaContratoPostagem" prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Gerenciar Contrato Postagem" />
			</jsp:include>
			<a4j:outputPanel id="principalPesquisa" ajaxRendered="true">
			    <rich:panel bodyClass="inpanelBody">
			        <f:facet name="header">
			            <b>Contrato Vigente:</b>
			        </f:facet>
			        <ul>
			            <table>
			                <tr>
			                    <td><h:outputText value="Número do Contrato" /></td>
			                    <td>
			                        <t:inputText id="idNumeroContrato"
			                                     size="60"
			                                     value="#{beanContratoPostagem.contratoPostagem.numero}"
			                                     readonly="true" />
			                    </td>
			                </tr>
			                <tr>
			                    <td><h:outputText value="Número do Cartão" /></td>
			                    <td>
			                        <t:inputText id="idNumeroCartao"
			                                     size="60"
			                                     value="#{beanContratoPostagem.contratoPostagem.cartao}"
			                                     readonly="true" />
			                    </td>
			                </tr>
			                <tr>
			                    <td><h:outputText value="Código Administrativo" /></td>
			                    <td>
			                        <t:inputText id="idCodigoAdministrativo"
			                                     size="60"
			                                     value="#{beanContratoPostagem.contratoPostagem.codigoAdministrativo}"
			                                     readonly="true" />
			                    </td>
			                </tr>
			                <tr>
			                    <td><h:outputText value="Número Diretoria Regional" /></td>
			                    <td>
			                        <t:inputText id="idNumeroDiretoriaRegional"
			                                     size="60"
			                                     value="#{beanContratoPostagem.contratoPostagem.numeroDiretoriaRegional}"
			                                     readonly="true" />
			                    </td>
			                </tr>
			                <tr>
			                    <td><h:outputText value="Data de Início" /></td>
			                    <td>
			                    	<rich:calendar id="idDataInicio"
			                    					value="#{beanContratoPostagem.contratoPostagem.dataVigenciaInicial}"
			                    					datePattern="dd/MM/yyyy"
			                    					locale="pt_Br"
			                                     	readonly="true" />
			                    </td>
			                </tr>
			                <tr>
			                    <td><h:outputText value="Login Web Service *" /></td>
			                    <td>
			                        <t:inputText id="idLoginWebService"
			                                     size="60"
			                                     value="#{beanContratoPostagem.contratoPostagem.usuarioAutenticacaoWS}" />
			                    </td>
			                </tr>
			                <tr>
			                    <td><h:outputText value="Senha Web Service *" /></td>
			                    <td>
			                        <t:inputText id="idSenhaWebService"
			                                     size="60"
			                                     value="#{beanContratoPostagem.contratoPostagem.senhaAutenticacaoWS}" />
			                    </td>
			                </tr>
			            </table>
			        </ul>
			    </rich:panel>
			    <rich:panel bodyClass="inpanelBody">
					<table>
						<tr>
							<td>
								<a4j:commandButton id="btnCriarNovo"
													styleClass="BotaoPadrao"
													value="Criar Novo"
													reRender="idPnlContratoPostagemCadastro"
													oncomplete="Richfaces.showModalPanel('idPnlContratoPostagemCadastro')" />
							</td>
							<td>
								<a4j:commandButton id="btnAlterar"
													styleClass="BotaoPadraoEstendido"
													value="Alterar Contrato Vigente"
													actionListener="#{beanContratoPostagem.alterarContratoVigente}"
													rendered="#{beanContratoPostagem.existeContratoVigente}" />
							</td>
						</tr>
					</table>
				</rich:panel>
			    <a4j:outputPanel id="pnlResultadoPesquisa" ajaxRendered="true">
			    	<h:panelGrid id="pnlResultadoTotal"
			    					styleClass="PadraoTituloPanel">
						<h:outputText value="Resultado da Pesquisa - Quant. de Registros: #{beanContratoPostagem.qtdRegistros}" />
						<rich:datascroller id="sc1"
											align="left"
											for="tbPesquisaContratoPostagem"
											maxPages="100"
											reRender="sc2" />
						<rich:dataTable id="tbPesquisaContratoPostagem"
										value="#{beanContratoPostagem.contratosEncerrados}"
										var="pesquisa"
										columnClasses="center"
										rowClasses="linha-par, linha-impar"
										rows="50"
										reRender="ds">
							<rich:column sortBy="#{pesquisa.numero}" width="10px">
								<f:facet name="header">
									<h:outputText value="Número do Contrato" />
								</f:facet>
								<h:outputText value="#{pesquisa.numero}" />
							</rich:column>

							<rich:column sortBy="#{pesquisa.cartao}" width="10px">
								<f:facet name="header">
									<h:outputText value="Número do Cartão" />
								</f:facet>
								<h:outputText value="#{pesquisa.cartao}" />
							</rich:column>

							<rich:column sortBy="#{pesquisa.codigoAdministrativo}" width="10px">
								<f:facet name="header">
									<h:outputText value="Código Administrativo" />
								</f:facet>
								<h:outputText value="#{pesquisa.codigoAdministrativo}" />
							</rich:column>

							<rich:column sortBy="#{pesquisa.numeroDiretoriaRegional}" width="10px">
								<f:facet name="header">
									<h:outputText value="Número Diretoria Regional" />
								</f:facet>
								<h:outputText value="#{pesquisa.numeroDiretoriaRegional}" />
							</rich:column>

							<rich:column sortBy="#{pesquisa.dataVigenciaInicial}" width="10px">
								<f:facet name="header">
									<h:outputText value="Dt Início" />
								</f:facet>
								<h:outputText value="#{pesquisa.dataVigenciaInicial.time}" >
									<f:convertDateTime pattern="dd/MM/yyyy" />
								</h:outputText>
							</rich:column>

							<rich:column sortBy="#{pesquisa.dataVigenciaFinal}" width="10px">
								<f:facet name="header">
									<h:outputText value="Dt Fim" />
								</f:facet>
								<h:outputText value="#{pesquisa.dataVigenciaFinal.time}" >
									<f:convertDateTime pattern="dd/MM/yyyy" />
								</h:outputText>
							</rich:column>

							<rich:column sortBy="#{pesquisa.usuarioAutenticacaoWS}" width="10px">
								<f:facet name="header">
									<h:outputText value="Login Web Service" />
								</f:facet>
								<h:outputText value="#{pesquisa.usuarioAutenticacaoWS}" />
							</rich:column>

							<f:facet name="footer">
								<rich:datascroller align="left" for="tbPesquisaContratoPostagem" maxPages="100" id="sc2" reRender="sc1" />
							</f:facet>
						</rich:dataTable>
					</h:panelGrid>
			    </a4j:outputPanel>
			</a4j:outputPanel>
		</h:form>

		<!-- Cadastro de destinatário -->
		<rich:modalPanel id="idPnlContratoPostagemCadastro"							
							keepVisualState="true"
							style="position: fixed; top: 50; left: 50;"
							autosized="true"
							moveable="false"
							trimOverlayedElements="false">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="Cadastrar novo Contrato com os Correios" />
				</h:panelGroup>
			</f:facet>
			<h:form id="cadastro">
				<jsp:include page="/pages/contratoPostagem/contratoPostagemCadastro.jsp" />
			</h:form>
		</rich:modalPanel>

		<jsp:include page="/pages/template/footer.jsp" flush="true" />
	</a4j:page>
</f:view>