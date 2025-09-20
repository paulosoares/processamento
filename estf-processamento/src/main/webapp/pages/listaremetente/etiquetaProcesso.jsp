<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<script type="text/javascript">
	
</script>

<f:view>
	<a4j:page pageTitle="::.. Etiqueta Processo ..::">

		<h:form id="formEtiquetaProcesso" prependId="false">		
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Etiqueta Processo" />
			</jsp:include>
			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">
					<a4j:outputPanel id="pnlEtiquetaProcesso" ajaxRendered="true">
						<h:panelGrid id="pnlFiltroCadastro" styleClass="PadraoTituloPanel"
							cellpadding="0" cellspacing="0">
							<h:outputText value="Gerar Etiquetas do Processo" />
						</h:panelGrid>
					</a4j:outputPanel>
				</h:panelGrid>
			</h:panelGrid>
			<h:panelGrid columns="1" id="panelEtiquetasProcesso">
				<rich:panel id="panelEtiqueta" bodyClass="inpanelBody" style="width:1200px">
					<f:facet name="header">
						<b>Etiqueta</b>
					</f:facet>					
					<t:div style="display: table;">
						<t:div style="display: table-row;">	
							<t:div style="display: table-cell; text-align: left; vertical-align: middle; padding: 5px; width: 110px; float: left;">
								<h:outputText value="Assinador* " />
							</t:div> 
							<t:div style="display: table-cell; text-align: left; vertical-align: middle; padding: 5px; width: 200px;">
								<h:selectOneMenu id="idComboAssinador"
													styleClass="Input"
													value="#{beanGeracaoEtiquetaProcesso.codigoServidor}">
									<f:selectItems value="#{beanGeracaoEtiquetaProcesso.listaAssinadores}" />
								</h:selectOneMenu>
							</t:div>
						</t:div>
						<t:div style="display: table-row;">	
							<t:div style="display: table-cell; text-align: left; vertical-align: middle; padding: 5px; width: 110px; float: left;">
								<h:outputText value="Código Andamento* " />
							</t:div>
							<t:div style="display: table-cell; text-align: left; vertical-align: middle; padding: 5px; width: 200px;">
								<h:selectOneMenu id="idComboAndamento"
													styleClass="Input"
													value="#{beanGeracaoEtiquetaProcesso.codigoAndamento}">
									<f:selectItems value="#{beanGeracaoEtiquetaProcesso.listaAndamentos}" />
								</h:selectOneMenu>
							</t:div>
						</t:div>
					</t:div>
						
					<t:div style="display: table;">						
						<t:div style="display: table-row;">	
							<t:div style="display: table-cell; text-align: left; vertical-align: middle; padding: 5px; width: 110px; float: left;">
								<h:outputText value="Destinatário* " />
							</t:div>
							<t:div style="display: table-cell; text-align: left; vertical-align: middle; padding: 5px; width: 300px; float: left;">
								<t:inputText id="idDestinatario" size="50" value="#{beanDestinatario.campoPesquisa}" />
							</t:div>
							<t:div style="display: table-cell; text-align: left; vertical-align: middle; padding: 5px;">
								<a4j:commandButton id="btnBuscarDestinatario" 
													styleClass="BotaoPadraoEstendido"
													value="Buscar Destinatário" 
													reRender="#{beanGeracaoEtiquetaProcesso.nomeComponenteRerenderizar}"
													action="#{beanDestinatario.abrirModoPesquisaDialogo}">
									<f:setPropertyActionListener value="#{beanGeracaoEtiquetaProcesso}" target="#{beanDestinatario.selecionaBeanDestinatario}" />
								</a4j:commandButton>
								<h:panelGroup rendered="#{beanDestinatario.flagExibirModalPesquisa}">
									<script type="text/javascript">
										Richfaces.showModalPanel('idPnlModalPesquisaDestinatario')
									</script>
								</h:panelGroup>
							</t:div>
						</t:div>
						<t:div style="display: table-row;">							
							<t:div style="display: table-cell; text-align: center; vertical-align: padding: 5px; left;">
								<strong>								
									<h:outputText value="#{beanGeracaoEtiquetaProcesso.descricaoDestinatarioSelecionado}" />
								</strong>
							</t:div>
							<t:div style="display: table-cell; float: left;"></t:div>
						</t:div>
						</t:div>
						
						<t:div style="display: table;">	
						<t:div style="display: table-row;" id="idPanelPosicaoInicialVisualizacao">							
							<t:div style="display: table-cell; text-align: left; vertical-align: top; float: left;">
							<a4j:commandButton id="btnAdicionar" styleClass="BotaoPadrao"
												value="Adicionar"
												ignoreDupResponses="true" reRender="panelEtiquetasProcesso, idTabelaEtiquetas"
												actionListener="#{beanGeracaoEtiquetaProcesso.adicionarEtiqueta}" />
							</t:div>							
							<t:div style="display: table-cell; text-align: left; vertical-align: top;">
								<h:commandButton value="Limpar" styleClass="BotaoPadrao" actionListener="#{beanGeracaoEtiquetaProcesso.limpar}"/>
							</t:div>
																
						</t:div>
					</t:div>					
				</rich:panel>				
				<rich:panel id="idPanelTabelaEtiquetas" rendered="#{beanGeracaoEtiquetaProcesso.exibirLista}">
					<rich:dataTable id="idTabelaEtiquetas" border="1" var="objEtiqueta" value="#{beanGeracaoEtiquetaProcesso.lista}">
						<rich:column>
							<f:facet name="header">
								<h:outputText value="Assinador" />
							</f:facet>
							<h:outputText value="#{objEtiqueta.usuario.nomeFuncionario}" />
						</rich:column>
						<rich:column>
							<f:facet name="header">
								<h:outputText value="Código Andamento" />
							</f:facet>
							<h:outputText value="#{objEtiqueta.encaminhamento.codigoAndamento}" />
						</rich:column>
						<rich:column>
							<f:facet name="header">
								<h:outputText value="Destinatário" />
							</f:facet>
							<h:outputText value="#{objEtiqueta.destinatarioListaRemessa.descricaoPrincipal}" />
						</rich:column>					
						<rich:column style="text-align: center;">
							<f:facet name="header">
								<h:outputText value="Excluir" />
							</f:facet>
							<h:commandLink actionListener="#{beanGeracaoEtiquetaProcesso.excluirEtiqueta}">
								<h:graphicImage url="/images/deletecell.png" title="Excluir" />
								<a4j:support reRender="idTabelaEtiquetas, idPanelTabelaEtiquetas"/>				
							</h:commandLink>
						</rich:column>
					</rich:dataTable>
					<br>
					<t:div style="display: table;">
						<t:div style="display: table-row;">
							<t:div style="display: table-cell; text-align: center; vertical-align: top; float: left;">
								Posição inicial impressão etiq.
							</t:div>
							<t:div style="display: table-cell; text-align: left; vertical-align: top; width: 160px; float: left;">
								<rich:comboBox id="idComboPosicaoInicialEtiquetaVisualizacao"
												value="#{beanGeracaoEtiquetaProcesso.selecaoQtdEtiquetas}"
												suggestionValues="#{beanGeracaoEtiquetaProcesso.comboEtiquetas}"
												directInputSuggestions="false" />
								</t:div>
								<t:div style="display: table-cell; text-align: left; vertical-align: top; float: left;">
									<a4j:commandLink reRender="idAjudaEtiquetasVisualizacao" oncomplete="Richfaces.showModalPanel('idAjudaEtiquetas')">
										<h:graphicImage value="/images/help.png" width="22" height="22" />
									</a4j:commandLink>
								</t:div>					
							<t:div style="display: table-cell; text-align: left; vertical-align: top; float: left;">
								<h:commandButton actionListener="#{beanGeracaoEtiquetaProcesso.gerarEtiquetasPDF}"
													styleClass="BotaoPadraoEstendido"
													value="Gerar Etiquetas" 
													style="width: 150px;"/>
							</t:div>
						</t:div>
					</t:div>
				</rich:panel>
			</h:panelGrid>
			<jsp:include page="/pages/template/footer.jsp" flush="true" />
		</h:form>
		<jsp:include page="/pages/destinatariolistaremetente/destinatarioPesquisaModal.jsp" />
		
		<!-- Ajuda Etiquetas -->
		<rich:modalPanel id="idAjudaEtiquetas" 
							keepVisualState="true"
							style="overflow:auto; position: fixed; top: 50; left: 50;" 
							autosized="true" 
							width="240" 
							moveable="false">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="Ajuda - Posição das Etiquetas" />
				</h:panelGroup>
			</f:facet>
			<f:facet name="controls">
				<h:panelGroup>
					<h:graphicImage value="/images/error.gif" styleClass="hidelink"
						id="esconderAjudaEtiquetas" />
					<rich:componentControl for="idAjudaEtiquetas"
						attachTo="esconderAjudaEtiquetas" operation="hide" event="onclick" />
				</h:panelGroup>
			</f:facet>			
			<center>			
				<t:div style="display: table; border-collapse: separate; border-spacing: 5px;">
					<t:div style="display: table-row;">	
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 1
						</t:div>
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 1
						</t:div>
					</t:div>
					<t:div style="display: table-row;">	
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 2
						</t:div>
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 2
						</t:div>
					</t:div>		
					<t:div style="display: table-row;">	
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 3
						</t:div>
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 3
						</t:div>
					</t:div>
					<t:div style="display: table-row;">	
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 4
						</t:div>
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 4
						</t:div>
					</t:div>
					<t:div style="display: table-row;">	
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 5
						</t:div>
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 5
						</t:div>
					</t:div>
					<t:div style="display: table-row;">	
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 6
						</t:div>
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 6
						</t:div>
					</t:div>
					<t:div style="display: table-row;">	
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 7
						</t:div>
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 7
						</t:div>
					</t:div>
					<t:div style="display: table-row;">	
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 8
						</t:div>
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 8
						</t:div>
					</t:div>
					<t:div style="display: table-row;">	
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 9
						</t:div>
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 9
						</t:div>
					</t:div>
					<t:div style="display: table-row;">	
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 10
						</t:div>
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 10
						</t:div>
					</t:div>
					<t:div style="display: table-row;">	
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 11
						</t:div>
						<t:div style="display: table-cell; text-align: center; border: solid 1px; padding: 5px;">
							Etiqueta: LINHA 11
						</t:div>
					</t:div>
				</t:div>
			</center>
		</rich:modalPanel>
	</a4j:page>
</f:view>