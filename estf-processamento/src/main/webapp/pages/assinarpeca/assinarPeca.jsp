<title>::.. Principal ..::</title>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<script type="text/javascript">
	
	function abrirLink(id) {
	 	var url = '<%=request.getContextPath()%>/pages/visualizarpdf/visualizarPDF.jsp?id=' + id;			 	
	    window.open(url);
		return false;
	}
</script>


<f:view>
	<a4j:page>

		<a4j:status id="status"
			onstart="Richfaces.showModalPanel('ajaxLoadingModalBox',{width:100,top:200})"
			onstop="Richfaces.hideModalPanel('ajaxLoadingModalBox')" />

		<rich:modalPanel id="ajaxLoadingModalBox" minHeight="10" minWidth="20"
			height="60" width="300" zindex="2000" moveable="false"
			resizeable="false">
			<h:outputText value="Aguarde. Processando..."
				style="font-size: 12px; font-weight: bold; color: #82c0e7; text-align: center; vertical-align: middle;" />
		</rich:modalPanel>

		<h:form id="form" prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Assinar Peça" />
			</jsp:include>

			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="PadraoTituloPanel">Pesquisar peças eletrônicas</td>
				</tr>

				<tr>
					<td>
						<table>
							<tr>
								<td class="Padrao">Sigla do processo:</td>
								<td>
									<h:inputText
										value="#{beanAssinarPeca.siglaClasseProcesso}"
										onkeyup="converterClasse(this);"
										onchange="validarClasse(this);"
										onkeypress="return mascaraInputLetra(this,event)" size="5"
										maxlength="4"
										onkeydown="return pesquisar(\"btnPesquisarPeca\")" />
								</td>
								<td class="Padrao">Número do processo:</td>
								<td>
									<h:inputText value="#{beanAssinarPeca.numeroProcesso}"
										onkeypress="return mascaraInputNumerico(this, event)"
										onkeydown="return pesquisar(\"btnPesquisarPeca\")">
										<f:converter converterId="javax.faces.Long" />
									</h:inputText>
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<h:panelGrid columns="2">
										<h:outputText styleClass="Padrao" value="Lista de Processos:"/>
	
										<h:inputText style="margin-left:10px;" id="idProcessos"  size="50"
											value="#{beanAssinarPeca.identificacaoProcessos}" />											
										<!-- 								
										<rich:hotKey selector="#idProcessos" key="return" id="hotKeyidProcesso"
											handler="document.getElementById('btnIncluirDocumento').onclick()" />

										<a4j:commandButton styleClass="BotaoMais"
											id="btnIncluirDocumento"
											title="Inclui o processo digitado."
											onclick="exibirMsgProcessando(true); "
											reRender="hotKeyidProcesso,idProcessos,pnlPrincipal"
											oncomplete="document.getElementById('idProcessos').value = ''; exibirMsgProcessando(false); document.getElementById('idProcessos').focus(); " />
									 -->
										<h:outputText value=""/>
										<h:outputText  style="margin-left:10px;" value="Informar relação de processos separados por \";\""/>	
								
									</h:panelGrid>								
								</td>
							</tr>
							<tr>
								<td class="Padrao">Número do protocolo:</td>
								<td>
									<h:inputText value="#{beanAssinarPeca.numeroProtocolo}"
										onkeypress="return mascaraInputNumerico(this, event)"
										onkeydown="return pesquisar(\"btnPesquisarPeca\")" size="8"
										maxlength="7">
										<f:converter converterId="javax.faces.Long" />
									</h:inputText>
								</td>
								<td class="Padrao">Ano do protocolo:</td>
								<td>
									<h:inputText value="#{beanAssinarPeca.anoProtocolo}"
										onkeypress="return mascaraInputNumerico(this, event)" size="5"
										maxlength="4"
										onkeydown="return pesquisar(\"btnPesquisarPeca\")">
										<f:converter converterId="javax.faces.Short" />
									</h:inputText>
								</td>
							</tr>
							<tr>
								<td class="Padrao">Situação do documento:</td>
								<td>
									<h:selectOneMenu
										id="idSituacaoDocumento"
										value="#{beanAssinarPeca.apenasAssinados}">
										<f:selectItems
											value="#{beanAssinarPeca.itensApenasDocumentosAssinados}" />
									</h:selectOneMenu>
								</td>
							</tr>
							<br/>
							<tr>
								<td>
									<a4j:commandButton styleClass="BotaoPesquisar"
										value="Pesquisar" id="btnPesquisarPeca"
										actionListener="#{beanAssinarPeca.pesquisarPecasEletronicas}"
										ignoreDupResponses="true" limitToList="false" 
										onclick="exibirMsgProcessando(true); "
										reRender="pnlPrincipal"
										oncomplete="exibirMsgProcessando(false);" />
										<!-- Componente que faz o refresh após a assinatura das peças -->
									<a4j:push interval="2000"
										actionListener="#{beanAssinarPeca.pesquisarPecasEletronicas}"
										ignoreDupResponses="true"  
										eventProducer="#{beanAssinarPeca.refreshController.adicionarListenerRefresh}"									
										eventsQueue="ajaxQueue"
										id="pushPesquisaTextoProcesso" />
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>

			<a4j:outputPanel id="pnlPrincipal" ajaxRendered="true">
				<table width="100%" cellpadding="0" cellspacing="0">
					<c:if
						test="${not empty beanAssinarPeca.arquivoProcessoEletronicoList}">
						<tr>
							<td>
								<table width="100%" cellpadding="0" cellspacing="0">
									<tr>
										<td><rich:dataTable headerClass="DataTableDefaultHeader"
												styleClass="DataTableDefault"
												footerClass="DataTableDefaultFooter"
												rowClasses="DataTableRow, DataTableRow2"
												columnClasses="cinco, dez, dez, dez, trinta, quinze, cinco"
												value="#{beanAssinarPeca.arquivoProcessoEletronicoList}"
												var="peca"
												binding="#{beanAssinarPeca.tabelaArquivoProcessoEletronicoList}">

												<rich:column>
													<f:facet name="header">
													</f:facet>
													<h:selectBooleanCheckbox value="#{peca.checked}"
														disabled="#{peca.wrappedObject.documentoEletronicoView.descricaoStatusDocumento == 'ASS'}" />
												</rich:column>

												<rich:column>
													<f:facet name="header">
														<h:outputText value="Protocolo" />
													</f:facet>
													<h:outputText
														value="#{peca.wrappedObject.pecaProcessoEletronico.objetoIncidente.identificacao}" />
												</rich:column>

												<rich:column>
													<f:facet name="header">
														<h:outputText value="Documento" />
													</f:facet>
													<h:commandLink
														value="#{peca.wrappedObject.documentoEletronicoView.id}"
														actionListener="#{beanAssinarPeca.visualizarPDFPecaProcessual}" />
													<h:outputLink
														value="#{beanAssinarPeca.documentoDownloadURL}">
														<h:graphicImage value="/images/pdf.png"></h:graphicImage>
													</h:outputLink>
												</rich:column>
																	
												<rich:column
													sortBy="#{peca.wrappedObject.pecaProcessoEletronico.dataInclusao}">
													<f:facet name="header">
														<h:outputText value="Data" />
													</f:facet>
													<h:outputText value="#{peca.wrappedObject.pecaProcessoEletronico.dataInclusao}">
														<f:converter converterId="dataConverter" />
													</h:outputText>
												</rich:column>

												<rich:column>
													<f:facet name="header">
														<h:outputText value="Tipo da peça" />
													</f:facet>
													<h:outputText
														value="#{peca.wrappedObject.pecaProcessoEletronico.tipoPecaProcesso.descricao}" />
												</rich:column>

												<rich:column>
													<f:facet name="header">
														<h:outputText value="Situação da peça" />
													</f:facet>
													<h:outputText
														value="#{peca.wrappedObject.pecaProcessoEletronico.tipoSituacaoPeca.descricao}" />
												</rich:column>

												<rich:column>
													<f:facet name="header">
														<h:outputText value="Situação do documento" />
													</f:facet>
													<h:outputText
														value="#{peca.wrappedObject.documentoEletronicoView.descricaoStatusDocumento}" />
												</rich:column>

											</rich:dataTable>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<table width="155px" cellpadding="0" cellspacing="0">
									<tr>
										<td><a4j:commandButton value="Selecionar todos"
												styleClass="BotaoSelecionarTodos"
												actionListener="#{beanAssinarPeca.marcarTodos}" />
										</td>
										<td><h:commandButton styleClass="BotaoAssinar"
												value="Assinar"
												action="#{beanAssinarPeca.assinarListaArquivoProcessoEletronico}" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</c:if>

				</table>
			</a4j:outputPanel>

			<jsp:include page="/pages/template/footer.jsp" flush="true" />
		</h:form>
	</a4j:page>
</f:view>


