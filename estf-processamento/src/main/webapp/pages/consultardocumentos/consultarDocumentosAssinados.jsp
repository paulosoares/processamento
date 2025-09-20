<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />


<script type="text/javascript">
	function aguarde(mostrar, div){
	      if( mostrar == true ){
	            document.getElementById(div).innerHTML = '<img src="/processamento/images/loading.gif" /><font class="Padrao">&nbsp;&nbsp;Aguarde...</font>';
	      }
	}

	function pesquisar () {
		document.getElementById('botaoPesquisar').click();
	}
	
	function caixaAlta (campo) {
		campo.value = campo.value.toUpperCase();
	} 

		
</script>

<f:view>
	<a4j:page pageTitle="::.. Principal ..::">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Expedientes Assinados" />
			</jsp:include>
		</h:form>

		<a4j:form id="form" prependId="false">

			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">

					<a4j:outputPanel id="pnlPrincipalPesquisa" ajaxRendered="true">
						<div class="PainelTituloPesquisa">
							<span> Pesquisa: </span>
						</div>

						<div style="padding-top: 10px;">
							<h:panelGrid columns="4" id="painelPesquisa1">
								<t:panelGrid columns="2">
										<h:outputLabel styleClass="Padrao" value="*Período: "></h:outputLabel>
										<h:panelGrid columns="1">
											<t:panelGroup>
												<rich:calendar id="itDataInicial" value="#{beanConsultarDocumentosAssinados.dataInicial}" datePattern="dd/MM/yyyy" locale="pt_Br" />
												<h:outputLabel styleClass="Padrao" value=" a "></h:outputLabel>
												<rich:calendar id="itDataFinal" value="#{beanConsultarDocumentosAssinados.dataFinal}" datePattern="dd/MM/yyyy" locale="pt_Br" />
											</t:panelGroup>
										</h:panelGrid>
								</t:panelGrid>
							</h:panelGrid>
							
							<h:panelGrid columns="9" id="painelPesquisa2">
								<h:outputText styleClass="Padrao" value='*Setor:' />
								<h:selectOneMenu
									value="#{beanConsultarDocumentosAssinados.codigoSetorAtual}"
									style="margin-left:5px;">
									<f:selectItems
										value="#{beanConsultarDocumentosAssinados.itensSetores}" />
									<a4j:support ajaxSingle="false" event="onchange" reRender="comboUsuarios"
									actionListener="#{beanConsultarDocumentosAssinados.alterarSetor}"/>
								</h:selectOneMenu>
									
								<h:outputLabel styleClass="Padrao" value='Usuário:' />
								<h:selectOneMenu
									value="#{beanConsultarDocumentosAssinados.siglaUsuarioAtual}"
									style="margin-left:5px;" id="comboUsuarios">
									<f:selectItems value="#{beanConsultarDocumentosAssinados.itensUsuarios}"/>								
								</h:selectOneMenu>
							</h:panelGrid>
						
							<h:panelGrid columns="9" id="painelPesquisa3">
								<a4j:commandButton styleClass="BotaoPesquisar"
									style="margin-left:15px;" value="Pesquisar"
									id="btnPesquisarProcesso"
									actionListener="#{beanConsultarDocumentosAssinados.pesquisarDocumentosAction}"
									ignoreDupResponses="true" reRender="pnlResultadoPesquisa"
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" />
								<a4j:commandButton styleClass="BotaoPadrao"
									style="margin-left:15px;" value="Limpar"
									id="btnLimparProcesso"
									actionListener="#{beanConsultarDocumentosAssinados.limparSessaoAction}"
									ignoreDupResponses="true" 
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);"/>	
							</h:panelGrid>
						</div>
					</a4j:outputPanel>
					
					<a4j:outputPanel ajaxRendered="true" keepTransient="false"
						id="pnlResultadoPesquisa" styleClass="MolduraInterna">
						<c:if
							test="${not empty beanConsultarDocumentosAssinados.listaDocumentos}">
							<hr color="red" align="left" size="1px" width="90%" />
							<rich:dataTable headerClass="DataTableDefaultHeader"	styleClass="DataTableDefault"	footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="quinzeCenter, vinteCenter, cincoCenter, cincoCenter, dezCenter,  dezCenter, cincoCenter, dezCenter"
								value="#{beanConsultarDocumentosAssinados.listaDocumentos}"	var="wrappedDocumento"	binding="#{beanConsultarDocumentosAssinados.tabelaDocumentos}"
								rows="30" id="tabelaDocumento">

								<rich:column  sortBy="#{wrappedDocumento.wrappedObject.nomeProcesso}" >
									<f:facet name="header">
										<h:outputText value="Processo"	styleClass="DataTableDocumentoTexto" />
									</f:facet>
									<h:commandLink styleClass="DataTableDocumentoTexto"	target="_blank"
										value="#{wrappedDocumento.wrappedObject.nomeProcesso}"
										action="#{beanConsultarDocumentosAssinados.consultarProcessoDigital}">
										<f:setPropertyActionListener
											target="#{beanConsultarDocumentosAssinados.seqObjetoIncidente}"
											value="#{wrappedDocumento.wrappedObject.objetoIncidente}" />
									</h:commandLink>
								</rich:column>

								<rich:column sortBy="#{wrappedDocumento.wrappedObject.descricaoDocumento}">
									<f:facet name="header">
										<h:outputText value="Documento" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.descricaoDocumento}" />
								</rich:column>
								
								<rich:column sortBy="#{wrappedDocumento.wrappedObject.possuiPDF}">
									<f:facet name="header">
										<h:outputText value="PDF" />
									</f:facet>
									
									<h:commandLink 
											rendered="#{wrappedDocumento.wrappedObject.possuiPDF}"											
											actionListener="#{beanConsultarDocumentosAssinados.visualizarPDF}"										
											styleClass="PadraoLink">
											<h:graphicImage value="/images/pdf.png"></h:graphicImage>
									</h:commandLink> 
									
								</rich:column>
															
								<rich:column sortBy="#{wrappedDocumento.wrappedObject.dataAssinatura}">
									<f:facet name="header">
										<h:outputText value="Data Assinatura" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.dataAssinatura}" />
								</rich:column>			
								
								<rich:column sortBy="#{wrappedDocumento.wrappedObject.assinador}">
									<f:facet name="header">
										<h:outputText value="Assinador" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.assinador}" />
								</rich:column>	
								
								<rich:column sortBy="#{wrappedDocumento.wrappedObject.faseAtual}">
									<f:facet name="header">
										<h:outputText value="Situação Atual" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.faseAtual}" />
								</rich:column>			
								
								<rich:column sortBy="#{wrappedDocumento.wrappedObject.dataFase}">
									<f:facet name="header">
										<h:outputText value="Data Situação Atual" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.dataFase}" />
								</rich:column>
								
								<rich:column sortBy="#{wrappedDocumento.wrappedObject.ultimo}">
									<f:facet name="header">
										<h:outputText value="Usuário Situação Atual" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.ultimo}" />
								</rich:column>
								
									
							</rich:dataTable>
							
							<rich:datascroller for="tabelaDocumento" maxPages="10">
								<f:facet name="first">
									<h:outputText value="Primeira" />
								</f:facet>
								<f:facet name="last">
									<h:outputText value="Ultima"/>
								</f:facet>
							</rich:datascroller>
						</c:if>
					</a4j:outputPanel>
					
					<a4j:commandButton styleClass="BotaoOculto" id="btnAtualizaSessao"
						actionListener="#{beanConsultarDocumentosAssinados.atualizaSessaoAction}" />
					<a4j:commandButton id="botaoPesquisar" value="Pesquisar"
						styleClass="BotaoOculto"
						actionListener="#{beanConsultarDocumentosAssinados.pesquisarDocumentosAction}"
						reRender="pnlResultadoPesquisa"
						onclick="exibirMsgProcessando(true)"
						oncomplete="exibirMsgProcessando(false);" />
					
				</h:panelGrid>
				
			</h:panelGrid>
				
		</a4j:form>
		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>
