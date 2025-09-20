<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<f:view>
	<script>		
			function abrirLink(id) {
			 	var url = '<%=request.getContextPath()%>/pages/visualizarpdf/visualizarPDF.jsp?id=' + id;			 	
			    window.open(url);
				return false;
			}
	</script>
	<a4j:page pageTitle="::.. Principal ..::">

		<h:form id="form" prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Assinar documentos" />
			</jsp:include>

			
				<h:panelGrid styleClass="MolduraExterna" cellpadding="0" cellspacing="0">
					<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">
						<a4j:outputPanel id="pnlPrincipalPesquisa" ajaxRendered="true">
							<c:if test="${beanAssinatura.isSetorPresidencia}">
								<h:panelGrid id="plnAbas" 
										style="padding: 0px 0px 0px 0px; border: 0px 0px 0px 0px;" 
										cellpadding="0" cellspacing="0" >										
									<h:panelGroup rendered="#{beanAssinatura.processoOuProtocolo == true}">
										<a4j:commandButton styleClass="BotaoAbaAtiva"  style="vertical-align: bottom; "
												value="Processo" id="btnPesquisaProcessoA" reRender="pnlPesquisa, pnlFiltro, plnAbas"
												actionListener="#{beanAssinatura.pesquisarPorDocumentosProcessos}" 
												ignoreDupResponses="true" onclick="exibirMsgProcessando(true)"
												oncomplete="exibirMsgProcessando(false);" />
										<a4j:commandButton styleClass="BotaoAbaInativa"  style="vertical-align: bottom;"
												value="Protocolo" id="btnPesquisaProtocoloI" reRender="pnlPesquisa, pnlFiltro, plnAbas"
												actionListener="#{beanAssinatura.pesquisarPorDocumentosProtocolos}"
												ignoreDupResponses="true" onclick="exibirMsgProcessando(true)"
												oncomplete="exibirMsgProcessando(false);" />
									</h:panelGroup>	
									<h:panelGroup rendered="#{beanAssinatura.processoOuProtocolo == false}" >
										<a4j:commandButton styleClass="BotaoAbaInativa"  style="vertical-align: bottom; "
												value="Processo" id="btnPesquisaProcessoI" reRender="pnlPesquisa, pnlFiltro, plnAbas"
												actionListener="#{beanAssinatura.pesquisarPorDocumentosProcessos}" 
												ignoreDupResponses="true" onclick="exibirMsgProcessando(true)"
												oncomplete="exibirMsgProcessando(false);" />
										<a4j:commandButton styleClass="BotaoAbaAtiva"  style="vertical-align: bottom;"
												value="Protocolo" id="btnPesquisaProtocoloA" reRender="pnlPesquisa, pnlFiltro, plnAbas"
												actionListener="#{beanAssinatura.pesquisarPorDocumentosProtocolos}"
												ignoreDupResponses="true" onclick="exibirMsgProcessando(true)"
												oncomplete="exibirMsgProcessando(false);" />
									</h:panelGroup>																																									
								</h:panelGrid>
								
							</c:if>
							
							<h:panelGrid styleClass="PadraoTituloPanel" id="pnlFiltro" cellpadding="0" cellspacing="0" >
								<h:outputText value="Filtros de pesquisa por #{beanAssinatura.textoTipoAssinatura}" />
							</h:panelGrid>
							<h:panelGrid styleClass="Moldura">							
								<h:panelGrid columns="2" id="pnlPesquisa">
									<c:if test="${beanAssinatura.processoOuProtocolo}">
										<h:outputText value="Classe:" />
										<t:inputText id="inputClasse" value="#{beanAssinatura.siglaClasse}" 
											onkeyup="converterClasse(this);"										
											onchange="validarClasse(this);" 
											onkeypress="return mascaraInputLetra(this,event)"
											onkeydown="return pesquisar(\"btnPesquisarProcesso\",event)"
											size="5" maxlength="4" forceId="true" />
										<h:outputText value="Número:" />
										<t:inputText value="#{beanAssinatura.numeroProcesso}"
											onkeydown="return pesquisar(\"btnPesquisarProcesso\",event)"
											onkeypress="return mascaraInputNumerico(this, event)" forceId="true" >
											<f:converter converterId="javax.faces.Long" />
										</t:inputText>
										<h:outputText value="Data Início:" />
										<t:inputCalendar value="#{beanAssinatura.dataInicio}"
											renderAsPopup="true" popupWeekString="Sem"
											popupTodayString="Hoje: "
											popupTodayDateFormat="EEEE, dd/MM/yyyy"
											popupDateFormat="dd/MM/yyyy"
											onkeydown="return pesquisar(\"btnPesquisarProcesso\",event)"
											onkeypress="return mascaraInputData(this, event)"
											onchange="validarData(this);" forceId="true" >
											<f:converter converterId="dataConverter" />
										</t:inputCalendar>
										<h:outputText value="Data Fim:" />
										<t:inputCalendar value="#{beanAssinatura.dataFim}"
											renderAsPopup="true" popupWeekString="Sem"
											popupTodayString="Hoje: "
											popupTodayDateFormat="EEEE, dd/MM/yyyy"
											popupDateFormat="dd/MM/yyyy"
											onkeydown="return pesquisar(\"btnPesquisarProcesso\",event)"
											onkeypress="return mascaraInputData(this, event)"
											onchange="validarData(this);" forceId="true" >
											<f:converter converterId="dataConverter" />
										</t:inputCalendar>
									</c:if>
									<c:if test="${not beanAssinatura.processoOuProtocolo}">
										<h:outputText value="Ano:" />
										<t:inputText id="inputAnoProtocolo" value="#{beanAssinatura.anoProtocolo}"
											onkeypress="return mascaraInputNumerico(this,event)"
											onkeydown="return pesquisar(\"btnPesquisarProtocolo\",event)"
											forceId="true"																	
											size="5" maxlength="4" >
											<f:converter converterId="javax.faces.Short" />
										</t:inputText>
										<h:outputText value="Número:" />
										<t:inputText value="#{beanAssinatura.numeroProtocolo}"
											onkeydown="return pesquisar(\"btnPesquisarProtocolo\",event)"
											onkeypress="return mascaraInputNumerico(this, event)"
											size="8" maxlength="7"
											id="inputNumeroProtocolo" forceId="true" >
											<f:converter converterId="javax.faces.Long" />
										</t:inputText>
										<h:outputText value="Data Início:" />
										<t:inputCalendar value="#{beanAssinatura.dataInicio}"
											renderAsPopup="true" popupWeekString="Sem"
											popupTodayString="Hoje: "
											popupTodayDateFormat="EEEE, dd/MM/yyyy"
											popupDateFormat="dd/MM/yyyy"
											onkeydown="return pesquisar(\"btnPesquisarProtocolo\",event)"
											onkeypress="return mascaraInputData(this, event)"
											onchange="validarData(this);" forceId="true" >
											<f:converter converterId="dataConverter" />
										</t:inputCalendar>
										<h:outputText value="Data Fim:" />
										<t:inputCalendar value="#{beanAssinatura.dataFim}"
											renderAsPopup="true" popupWeekString="Sem"
											popupTodayString="Hoje: "
											popupTodayDateFormat="EEEE, dd/MM/yyyy"
											popupDateFormat="dd/MM/yyyy"
											onkeydown="return pesquisar(\"btnPesquisarProtocolo\",event)"
											onkeypress="return mascaraInputData(this, event)"
											onchange="validarData(this);" forceId="true" >
											<f:converter converterId="dataConverter" />
										</t:inputCalendar>
									</c:if>
								</h:panelGrid>
								<h:panelGrid columns="2">
									<h:outputText value='Situação:' />
									<h:selectOneMenu immediate="true"
										value="#{beanAssinatura.filtroVisualizar}">
										<f:selectItem itemLabel="Assinados" itemValue="AS" />
										<f:selectItem itemLabel="Revisados" itemValue="RV" />
									</h:selectOneMenu>
								</h:panelGrid>
							</h:panelGrid>
	
							<c:if test="${beanAssinatura.processoOuProtocolo}">
								<a4j:commandButton styleClass="BotaoPesquisar"  
									value="Pesquisar" id="btnPesquisarProcesso"
									actionListener="#{beanAssinatura.pesquisarDocumentosProcessosSetor}"
									ignoreDupResponses="true" reRender="pnlPrincipalResultado"
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" />
								<a4j:push interval="2000"
									actionListener="#{beanAssinatura.pesquisarDocumentosProcessosSetor}"
									ignoreDupResponses="true" reRender="pnlPrincipalResultado"  
									eventProducer="#{beanAssinatura.refreshController.adicionarListenerRefresh}"									
									eventsQueue="ajaxQueue"
									id="pushPesquisaDocumentosProcessoSetor" />
							</c:if>
							<c:if test="${not beanAssinatura.processoOuProtocolo}">
								<a4j:commandButton styleClass="BotaoPesquisar"  
									value="Pesquisar" id="btnPesquisarProtocolo"
									actionListener="#{beanAssinatura.pesquisarDocumentosProtocolosSetor}"
									ignoreDupResponses="true" reRender="pnlPrincipalResultado"
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" />
								<a4j:push interval="2000"
									actionListener="#{beanAssinatura.pesquisarDocumentosProtocolosSetor}"
									ignoreDupResponses="true" reRender="pnlPrincipalResultado"  
									eventProducer="#{beanAssinatura.refreshController.adicionarListenerRefresh}"									
									eventsQueue="ajaxQueue"
									id="pushPesquisarDocumentosProtocolosSetor" />
									
							</c:if>	
						</a4j:outputPanel>
									
						<a4j:outputPanel id="pnlPrincipalResultado" ajaxRendered="true">						
							<h:panelGrid styleClass="PadraoTituloPanel" id="pnlResultadoTotal">							
								<c:if test="${beanAssinatura.processoOuProtocolo}">
									<h:outputText value="Assinar Documentos - Resultados : #{beanAssinatura.listaDocumentoTextoSize}" />
								</c:if>
								<c:if test="${not beanAssinatura.processoOuProtocolo}">
									<h:outputText value="Assinar Documentos - Resultados : #{beanAssinatura.listaDocumentoTextoProtocoloSize}" />
								</c:if>
							</h:panelGrid>
							
							<c:if test="${beanAssinatura.processoOuProtocolo}">						
								<h:dataTable headerClass="DataTableDefaultHeader"
									styleClass="DataTableDefault"
									footerClass="DataTableDefaultFooter"
									rowClasses="DataTableRow, DataTableRow2"																
									columnClasses="cinco, cinco, trintaLeft, trintaLeft, vinte, vinte, dez"
									value="#{beanAssinatura.listaDocumentoTexto}"
									var="tabelaProcesso"								
									binding="#{beanAssinatura.tabelaTextoProcessos}">
									<h:column>
										<f:facet name="header">
										</f:facet>
										<h:selectBooleanCheckbox value="#{tabelaProcesso.checked}" />
									</h:column>
									<h:column>
										<f:facet name="header">
										</f:facet>
										<h:graphicImage title="Assinado"
											value="/images/imgAssinado.png"
											rendered="#{tabelaProcesso.wrappedObject.tipoSituacaoDocumento.sigla == 'AS' }" />
										<h:graphicImage title="Revisado"
											value="/images/imgRevisado.png"
											rendered="#{tabelaProcesso.wrappedObject.tipoSituacaoDocumento.sigla == 'RV' }" />
									</h:column>
									<h:column>
										<f:facet name="header">
											<h:outputText value="Processo" />
										</f:facet>									
										<h:outputText styleClass="DataTableDocumentoTextoIndent" rendered="#{tabelaProcesso.indentar}" 
											value="#{tabelaProcesso.wrappedObject.texto.identificacao}" />
										<h:outputText styleClass="DataTableDocumentoTexto" rendered="#{!tabelaProcesso.indentar}"
											value="#{tabelaProcesso.wrappedObject.texto.identificacao}" />
									</h:column>
									<h:column>
										<f:facet name="header">
											<h:outputText value="Texto" />
										</f:facet>
										<h:commandLink value="#{tabelaProcesso.wrappedObject.texto.tipoTexto.descricao}   #{tabelaProcesso.wrappedObject.texto.observacao}"
											actionListener="#{beanAssinatura.visualizarPDFProcesso}"										
											styleClass="PadraoLink" />
										<%--<h:commandLink value="#{tabelaProcesso.wrappedObject.texto.tipoTexto.descricao} : #{tabelaProcesso.wrappedObject.texto.observacao}"
											onclick="abrirLink(#{tabelaProcesso.wrappedObject.documentoEletronicoView.id})"
											styleClass="PadraoLink" />--%>
									</h:column>
									<h:column>
										<f:facet name="header">
											<h:outputText value="Revisor" />
										</f:facet>
										<h:outputText
											value="#{tabelaProcesso.wrappedObject.usuarioRevisao}" />
									</h:column>
									<h:column rendered="#{beanAssinatura.filtroVisualizar == 'RV'}">
										<f:facet name="header">
											<h:outputText value="Data Revisão" />
										</f:facet>
										<h:outputText
											value="#{tabelaProcesso.wrappedObject.dataRevisao}">
											<f:converter converterId="dataConverter" />
										</h:outputText>
									</h:column>
									<h:column rendered="#{beanAssinatura.filtroVisualizar == 'AS'}">
										<f:facet name="header">
											<h:outputText value="Data Assinatura" />
										</f:facet>
										<h:outputText
											value="#{tabelaProcesso.wrappedObject.documentoEletronicoView.assinaturaDigitalView.dataCarimboTempo}">
											<f:converter converterId="dataConverter" />
										</h:outputText>
									</h:column>
									<%--<h:column>
										<f:facet name="header">
											<h:outputText value="Abrir" />
										</f:facet>
										<h:commandButton styleClass="BotaoAbrirDocumento" value="Abrir"
											onclick="abrirLink(#{tabelaProcesso.wrappedObject.documentoEletronico.id})"/>
									</h:column>--%>
								</h:dataTable>
							</c:if>
							
							<c:if test="${not beanAssinatura.processoOuProtocolo}">						
								<h:dataTable headerClass="DataTableDefaultHeader"
									styleClass="DataTableDefault"
									footerClass="DataTableDefaultFooter"
									rowClasses="DataTableRow, DataTableRow2"																
									columnClasses="cinco, cinco, trintaLeft, trintaLeft, vinte, vinte, dez"
									value="#{beanAssinatura.listaDocumentoTextoProtocolo}"
									var="tabelaProtocolo"								
									binding="#{beanAssinatura.tabelaTextoProtocolos}">
									<h:column>
										<f:facet name="header">
										</f:facet>
										<h:selectBooleanCheckbox value="#{tabelaProtocolo.checked}" />
									</h:column>
									<h:column>
										<f:facet name="header">
										</f:facet>
										<h:graphicImage title="Assinado"
											value="/images/imgAssinado.png"
											rendered="#{tabelaProtocolo.wrappedObject.tipoSituacaoDocumento.sigla == 'AS' }" />
										<h:graphicImage title="Revisado"
											value="/images/imgRevisado.png"
											rendered="#{tabelaProtocolo.wrappedObject.tipoSituacaoDocumento.sigla == 'RV' }" />
									</h:column>
									<h:column>
										<f:facet name="header">
											<h:outputText value="Protocolo" />
										</f:facet>									
										<h:outputText styleClass="DataTableDocumentoTextoIndent" rendered="#{tabelaProtocolo.indentar}" 
											value="#{tabelaProtocolo.wrappedObject.textoPeticao.ano}/#{tabelaProtocolo.wrappedObject.textoPeticao.numero}" />
										<h:outputText styleClass="DataTableDocumentoTexto" rendered="#{!tabelaProtocolo.indentar}"
											value="#{tabelaProtocolo.wrappedObject.textoPeticao.ano}/#{tabelaProtocolo.wrappedObject.textoPeticao.numero}" />
									</h:column>
									<h:column>
										<f:facet name="header">
											<h:outputText value="Texto" />
										</f:facet>
										<h:commandLink value="#{tabelaProtocolo.wrappedObject.textoPeticao.tipoTexto}"
											actionListener="#{beanAssinatura.visualizarPDFProtocolo}"										
											styleClass="PadraoLink" />
									</h:column>
									<h:column>
										<f:facet name="header">
											<h:outputText value="Revisor" />
										</f:facet>
										<h:outputText
											value="#{tabelaProtocolo.wrappedObject.usuarioRevisao}" />
									</h:column>
									<h:column rendered="#{beanAssinatura.filtroVisualizar == 'RV'}">
										<f:facet name="header">
											<h:outputText value="Data Revisão" />
										</f:facet>
										<h:outputText
											value="#{tabelaProtocolo.wrappedObject.dataRevisao}">
											<f:converter converterId="dataConverter" />
										</h:outputText>
									</h:column>
									<h:column rendered="#{beanAssinatura.filtroVisualizar == 'AS'}">
										<f:facet name="header">
											<h:outputText value="Data Assinatura" />
										</f:facet>
										<h:outputText
											value="#{tabelaProtocolo.wrappedObject.documentoEletronicoView.assinaturaDigitalView.dataCarimboTempo}">
											<f:converter converterId="dataConverter" />
										</h:outputText>
									</h:column>
								</h:dataTable>
							</c:if>
													
							<h:panelGrid columns="3">
								<input class="BotaoSelecionarTodos" value="Selecionar todos" name="selecionarTodos" type="button" onclick="selecionarCheckBoxesForm(document.forms[0]);"/>							
								<c:if test="${beanAssinatura.processoOuProtocolo}">	
										<h:commandButton styleClass="BotaoCancelarDocumento"
											value="Cancelar Documento"
											actionListener="#{beanAssinatura.cancelarDocumentosProcessosSelecionados}"
											onclick="return window.confirm('Deseja realmente cancelar os documentos selecionados?')" />
								</c:if>
								<c:if test="${not beanAssinatura.processoOuProtocolo}">	
										<h:commandButton styleClass="BotaoCancelarDocumento"
											value="Cancelar Documento"
											actionListener="#{beanAssinatura.cancelarDocumentosProtocolosSelecionados}"
											onclick="return window.confirm('Deseja realmente cancelar os documentos selecionados?')"
											 />
								</c:if>
								<c:if test="${beanAssinatura.processoOuProtocolo}">	
									<h:commandButton styleClass="BotaoAssinar" value="Assinar"
										rendered="#{beanAssinatura.filtroVisualizar == 'RV'}"
										action="#{beanAssinatura.assinarListaDocumentoTexto}" 
									/>
								</c:if>
								<c:if test="${not beanAssinatura.processoOuProtocolo}">	
									<h:commandButton styleClass="BotaoAssinar" value="Assinar"
										rendered="#{beanAssinatura.filtroVisualizar == 'RV'}"
										action="#{beanAssinatura.assinarListaDocumentoTextoPeticao}" />
								</c:if>
							</h:panelGrid>
						</a4j:outputPanel>
						<br/>						
						<h:outputText value="Legenda: " />
						<br/>
						<h:panelGrid styleClass="Moldura">
							<h:panelGrid columns="2">
								<h:outputText value="Revisado - " />
								<h:graphicImage title="Revisado" value="/images/imgRevisado.png"/>
								<h:outputText value="Assinado - " />
								<h:graphicImage title="Assinado" value="/images/imgAssinado.png"/>
							</h:panelGrid>
						</h:panelGrid>						
					</h:panelGrid>
				</h:panelGrid>
				<jsp:include page="/pages/template/footer.jsp" flush="true" />
			
		</h:form>
	</a4j:page>
</f:view>