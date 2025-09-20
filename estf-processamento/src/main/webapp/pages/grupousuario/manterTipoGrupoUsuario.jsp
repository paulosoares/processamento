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
</script>

<f:view>
	<a4j:page pageTitle="::.. Manter Grupo Usuário ..::"
		onload="document.getElementById('botaoPesquisar').click();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="GrupoUsuario" />
			</jsp:include>
		</h:form>
		<a4j:outputPanel id="pnlPrincipal" ajaxRendered="true">

			<a4j:form id="formId" prependId="false">
				<div class="PadraoDiv" style="text-align: left; width: 100%;">
					<h:panelGrid width="100%"
						rendered="#{beanManterTipoGrupoUsuario.renderedTabelaUsuario}">
						<h:dataTable headerClass="DataTableDefaultHeader"
							styleClass="DataTableDefault"
							footerClass="DataTableDefaultFooter"
							rowClasses="DataTableRow, DataTableRow2"
							columnClasses="vinteCincoLeft, vinteCincoLeft,vinteCenter, cinco, cinco"
							value="#{beanManterTipoGrupoUsuario.listaDeUsuarios}"
							var="wrappedGrupoUsuario"
							binding="#{beanManterTipoGrupoUsuario.tabelaDeUsuario}">

							<h:column>
								<f:facet name="header">
									<h:outputText value="Sigla Usuário" />
								</f:facet>
								<h:outputText styleClass="Padrao" value="#{wrappedGrupoUsuario.wrappedObject.usuario.id}" /> 
							</h:column>

							<h:column>
								<f:facet name="header">
									<h:outputText value="Nome" />
								</f:facet>
								<h:outputText styleClass="Padrao" value="#{wrappedGrupoUsuario.wrappedObject.usuario.nome}" /> 
							</h:column>

							<h:column>
								<f:facet name="header">
									<h:outputText value="Grupos" />
								</f:facet>
								<h:outputText styleClass="Padrao" value="#{wrappedGrupoUsuario.wrappedObject.usuario.listaDeGruposConcatenados}" />
							</h:column>
							
							<h:column>
								<f:facet name="header">
									<h:outputText value="Alterar"
										styleClass="DataTableDocumentoTexto" />
								</f:facet>
								<a4j:commandLink action="#{beanManterTipoGrupoUsuario.mostraGruposDoUsuario}" reRender="pnlEditaGrupoUsuario">
									<h:graphicImage url="../../images/botaAlterarv.gif"
										title="Alterar Grupo" />
								</a4j:commandLink>
							</h:column>							
						</h:dataTable>
					</h:panelGrid>
				</div>

				<a4j:outputPanel id="pnlEditaGrupoUsuario" styleClass="MolduraInterna" ajaxRendered="true" keepTransient="false" 
					rendered="#{beanManterTipoGrupoUsuario.renderedMostraGruposDoUsuario}">
					<div class="PainelTituloCriaTexto">
						<span> Preencha os campos abaixo: </span>
					</div>
					<div style="margin-top: 5px">
					<div style="margin-top: 15px; margin-bottom: 15px;">
						<h:outputText style="color:red;font-weight: bold;font-family : verdana, Geneva, Arial, Helvetica, sans-serif;" 
						value="#{beanManterTipoGrupoUsuario.inputNomeUsuarioNovoAltera}"/>
					</div>
						<rich:pickList id="pickGrupos" value="#{beanManterTipoGrupoUsuario.listaGrupoUsuarioSelecionado}" showButtonsLabel="false" 
							sourceListWidth="200px" targetListWidth="200px"> 	
							<f:selectItems value="#{beanManterTipoGrupoUsuario.listaTodosGrupos}"/> 
						</rich:pickList> 
					</div>
					<div style="margin-top: 5px">
						<h:commandButton value="Salvar" styleClass="BotaoPadrao" style="margin-left:5px;"
							action="#{beanManterTipoGrupoUsuario.salvarGrupoDoUsuario}"/>
						<h:commandButton value="Fechar" styleClass="BotaoPadrao" style="margin-left:5px;"
							actionListener="#{beanManterTipoGrupoUsuario.fecharCaixaSelecaoGrupoUsuarioAction}"/>
					</div>
				</a4j:outputPanel>
				
				<a4j:commandButton id="botaoPesquisar" value="Pesquisar"
					styleClass="BotaoOculto"
					action="#{beanManterTipoGrupoUsuario.pesquisaUsuariosDoSetor}"
					onclick="exibirMsgProcessando(true)"
					oncomplete="exibirMsgProcessando(false);" /> 

			</a4j:form>

			<jsp:include page="/pages/template/footer.jsp" flush="true" />

		</a4j:outputPanel>
	</a4j:page>
</f:view>