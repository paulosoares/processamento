<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<script type="text/javascript">
	function aguarde(mostrar, div) {
		if (mostrar == true) {
			document.getElementById(div).innerHTML = '<img src="/processamento/images/loading.gif" /><font class="Padrao">&nbsp;&nbsp;Aguarde...</font>';
		}
	}

	function pesquisar() {
		document.getElementById('form:botaoPesquisar').click();
	}
</script>

<f:view>
	<a4j:page pageTitle="::.. Administrar Permissões de Modelos ..::"
		onload="document.getElementById('nomePermissao').focus();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Permissões de Modelos" />
			</jsp:include>
		</h:form>

		<a4j:outputPanel id="pnlTela">
			<a4j:form id="form">
				<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
					cellspacing="0" id="pnlPesquisa">
					<h:panelGrid columns="6">
						<h:outputText styleClass="Padrao" value="Nome da Permissão:" />
						<h:inputText id="nomePermissao" size="100"
							value="#{beanAdministrarTiposPermissoesModelos.descricaoPermissao}" />

						<a4j:commandButton id="botaoPesquisar" value="Pesquisar"
							styleClass="BotaoPesquisar"
							actionListener="#{beanAdministrarTiposPermissoesModelos.pesquisarPermissoesAction}"
							onclick="exibirMsgProcessando(true)"
							oncomplete="exibirMsgProcessando(false);"
							reRender="pnlTabelaPermissoes,pnlPesquisa" />

						<a4j:commandButton id="botaoLimpar" value="Limpar"
							styleClass="BotaoPadrao"
							actionListener="#{beanAdministrarTiposPermissoesModelos.limparTelaAction}"
							onclick="exibirMsgProcessando(true)"
							oncomplete="exibirMsgProcessando(false);" reRender="pnlPesquisa" />

						<a4j:commandButton id="botaoSalvar" value="Novo"
							styleClass="BotaoPadrao" onclick="exibirMsgProcessando(true);"
							oncomplete="exibirMsgProcessando(false);"
							actionListener="#{beanAdministrarTiposPermissoesModelos.mostrarTelaSalvarAction}"
							reRender="pnlTela" />

					</h:panelGrid>

					<h:panelGrid styleClass="MolduraInterna" id="pnlTabelaPermissoes"
						rendered="#{not empty beanAdministrarTiposPermissoesModelos.listaPermissoesModelos}">
						<h:dataTable headerClass="DataTableDefaultHeader"
							styleClass="DataTableDefault"
							footerClass="DataTableDefaultFooter"
							rowClasses="DataTableRow, DataTableRow2"
							columnClasses="trintaCenter, trintaCenter, tres, tres"
							value="#{beanAdministrarTiposPermissoesModelos.listaPermissoesModelos}"
							var="wrappedPermissao"
							binding="#{beanAdministrarTiposPermissoesModelos.tabelaPermissoesModelos}">
							<h:column>
								<f:facet name="header">
									<h:outputText value="Nome" styleClass="DataTableDocumentoTexto" />
								</f:facet>
								<h:outputText styleClass="DataTableDocumentoTexto"
									style="text-align:center;"
									value="#{wrappedPermissao.wrappedObject.descricao}" />
							</h:column>
							<h:column>
								<f:facet name="header">
									<h:outputText value="Setor"
										styleClass="DataTableDocumentoTexto" />
								</f:facet>
								<h:outputText styleClass="DataTableDocumentoTexto"
									style="text-align:center;"
									value="#{wrappedPermissao.wrappedObject.setor.nome}"
									rendered="#{wrappedPermissao.wrappedObject.setor != null}" />
							</h:column>

							<h:column>
								<f:facet name="header">
									<h:outputText value="Alterar" />
								</f:facet>
								<a4j:commandLink
									action="#{beanAdministrarTiposPermissoesModelos.mostrarTelaAlteracaoAction}"
									reRender="pnlTela">
									<h:graphicImage url="../../images/botaAlterarv.gif"
										title="Alterar Permissão" />
									<f:setPropertyActionListener
										target="#{beanAdministrarTiposPermissoesModelos.tipoPermissao}"
										value="#{wrappedPermissao.wrappedObject}" />
								</a4j:commandLink>
							</h:column>

							<h:column>
								<f:facet name="header">
									<h:outputText value="Excluir" />
								</f:facet>
								<a4j:commandLink
									action="#{beanAdministrarTiposPermissoesModelos.removerPermissaoAction}"
									reRender="pnlTabelaPermissoes">
									<h:graphicImage url="../../images/close.gif" title="Excluir" />
									<f:setPropertyActionListener
										target="#{beanAdministrarTiposPermissoesModelos.tipoPermissao}"
										value="#{wrappedPermissao.wrappedObject}" />
								</a4j:commandLink>
							</h:column>
						</h:dataTable>
					</h:panelGrid>

					<a4j:outputPanel id="pnlTelaAlterarPermissao"
						styleClass="MolduraInterna" ajaxRendered="true"
						keepTransient="false">
						<c:if
							test="${beanAdministrarTiposPermissoesModelos.renderedTelaAlterar}">
							<hr color="red" align="left" size="1px" width="90%" />
							<div class="PainelTituloCriaTexto">
								<span> Preencha os campos presentes: </span>
							</div>
							<div style="margin-top: 5px">
								<span> <h:outputText styleClass="Padrao" value="Nome:" />
									<h:inputText id="descricaoA"
										value="#{beanAdministrarTiposPermissoesModelos.descricaoPermissao}"
										style="margin-left: 24px" size="55" /> </span>
							</div>
							<div style="margin-top: 5px">
								<span> <h:outputText styleClass="Padrao" value="Setor:" />
									<h:selectOneMenu style="margin-left:24px;"
										value="#{beanAdministrarTiposPermissoesModelos.codigoSetor}"
										id="setorA">
										<f:selectItems
											value="#{beanAdministrarTiposPermissoesModelos.listaSetores}" />
									</h:selectOneMenu> </span>
							</div>

							<div style="margin-top: 10px">
								<a4j:commandButton id="botaoAlterar" value="Alterar"
									actionListener="#{beanAdministrarTiposPermissoesModelos.salvarPermissaoAction}"
									reRender="pnlTela" styleClass="BotaoPadrao" />
								<a4j:commandButton id="botaoFecharTelaAlterar" value="Fechar"
									actionListener="#{beanAdministrarTiposPermissoesModelos.fecharTelaAlteracaoAction}"
									reRender="pnlTela" styleClass="BotaoPadrao">
								</a4j:commandButton>
							</div>
						</c:if>
					</a4j:outputPanel>

					<a4j:outputPanel id="pnlTelaSalvarPermissao"
						styleClass="MolduraInterna" ajaxRendered="true"
						keepTransient="false">
						<c:if
							test="${beanAdministrarTiposPermissoesModelos.renderedTelaSalvar}">
							<hr color="red" align="left" size="1px" width="90%" />
							<div class="PainelTituloCriaTexto">
								<span> Preencha os campos presentes: </span>
							</div>
							<div style="margin-top: 5px">
								<h:outputText styleClass="Padrao" value="Nome:" />
								<h:inputText id="descricaoS"
									value="#{beanAdministrarTiposPermissoesModelos.descricaoPermissao}"
									style="margin-left: 24px;" size="55" />
							</div>
							<div style="margin-top: 5px">
								<h:outputText styleClass="Padrao" value="Setor:" />
								<h:selectOneMenu style="margin-left:24px;" id="setorS"
									value="#{beanAdministrarTiposPermissoesModelos.codigoSetor}"
									immediate="true">
									<f:selectItems
										value="#{beanAdministrarTiposPermissoesModelos.listaSetores}" />
								</h:selectOneMenu>
							</div>

							<div style="margin-top: 10px">
								<a4j:commandButton id="botaoGravar" value="Gravar"
									actionListener="#{beanAdministrarTiposPermissoesModelos.salvarPermissaoAction}"
									reRender="pnlTela" styleClass="BotaoPadrao">
								</a4j:commandButton>
								<a4j:commandButton id="botaoFecharTelaGravar" value="Fechar"
									actionListener="#{beanAdministrarTiposPermissoesModelos.fecharTelaAlteracaoAction}"
									reRender="pnlPesquisa" styleClass="BotaoPadrao">
								</a4j:commandButton>
							</div>
						</c:if>
					</a4j:outputPanel>
				</h:panelGrid>
			</a4j:form>
		</a4j:outputPanel>

		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>