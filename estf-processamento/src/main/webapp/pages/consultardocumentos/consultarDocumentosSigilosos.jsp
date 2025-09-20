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
				<jsp:param name="nomePagina" value="Expedientes Sigilosos" />
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
								<t:panelGroup>
									<h:outputText value="Classe Processo: " styleClass="Padrao" />
									<h:inputText size="4" maxlength="4" id="txtClassePesquisa"
										value="#{beanConsultarDocumentosSigilosos.siglaProcesso}"
										onkeypress="return mascaraInputLetra(this,event)" />
								</t:panelGroup>
								
								<t:panelGroup>
									<h:outputText value="Número Processo:" styleClass="Padrao" />
									<h:inputText styleClass="Input" id="txtNumeroProcesso"
										onkeypress="return mascaraInputNumerico(this,event)"
										value="#{beanConsultarDocumentosSigilosos.numeroProcesso}"
										maxlength="7" />
								</t:panelGroup>
								
								
								<t:panelGrid columns="2">
										<h:outputLabel styleClass="Padrao" value="*Período: "></h:outputLabel>
										<h:panelGrid columns="1">
											<t:panelGroup>
												<rich:calendar id="itDataInicial" value="#{beanConsultarDocumentosSigilosos.dataInicial}" datePattern="dd/MM/yyyy" locale="pt_Br" />
												<h:outputLabel styleClass="Padrao" value=" a "></h:outputLabel>
												<rich:calendar id="itDataFinal" value="#{beanConsultarDocumentosSigilosos.dataFinal}" datePattern="dd/MM/yyyy" locale="pt_Br" />
											</t:panelGroup>
										</h:panelGrid>
								</t:panelGrid>
							</h:panelGrid>
		
							
							<h:panelGrid columns="9" id="painelPesquisa2">
								


								<a4j:commandButton styleClass="BotaoPesquisar"
									style="margin-left:15px;" value="Pesquisar"
									id="btnPesquisarProcesso"
									actionListener="#{beanConsultarDocumentosSigilosos.pesquisarDocumentosAction}"
									ignoreDupResponses="true" reRender="pnlResultadoPesquisa"
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" />
								<a4j:commandButton styleClass="BotaoPadrao"
									style="margin-left:15px;" value="Limpar"
									id="btnLimparProcesso"
									actionListener="#{beanConsultarDocumentosSigilosos.limparSessaoAction}"
									ignoreDupResponses="true" 
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);"/>	
							</h:panelGrid>
						</div>
					</a4j:outputPanel>

					<a4j:outputPanel ajaxRendered="true" keepTransient="false"
						id="pnlResultadoPesquisa" styleClass="MolduraInterna">
						<c:if
							test="${not empty beanConsultarDocumentosSigilosos.listaDocumentos}">
							<hr color="red" align="left" size="1px" width="90%" />
							<rich:dataTable headerClass="DataTableDefaultHeader"	styleClass="DataTableDefault"	footerClass="DataTableDefaultFooter"  width="95%"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="quinzeCenter, quinzeCenter, vinteLeft, dezCenter, cincoCenter, cincoCenter, vinteCenter, vinteCenter, vinteCenter, vinteCenter, dezCenter, dezCenter, dezCenter"
								value="#{beanConsultarDocumentosSigilosos.listaDocumentos}"	var="wrappedDocumento"	binding="#{beanConsultarDocumentosSigilosos.tabelaDocumentos}"
								rows="30" id="tabelaDocumento">

								<rich:column style="width:3%" sortBy="#{wrappedDocumento.wrappedObject}" >
									<f:facet name="header">
										<h:outputText value="Classe Processual"	styleClass="DataTableDocumentoTexto" />
									</f:facet>
								<h:outputText value="#{wrappedDocumento.wrappedObject[0]}"	styleClass="DataTableDocumentoTexto" />
								</rich:column>

							
							
							<rich:column  style="width:3%" sortBy="#{wrappedDocumento.wrappedObject}" >
									<f:facet name="header">
										<h:outputText value="Processo"	styleClass="DataTableDocumentoTexto" />
									</f:facet>
										<h:outputText value="#{wrappedDocumento.wrappedObject[1]}"	styleClass="DataTableDocumentoTexto" />
								</rich:column>
								
								
								<rich:column  style="width:5%" sortBy="#{wrappedDocumento.wrappedObject}" >
									<f:facet name="header">
										<h:outputText value="Sigilo"	styleClass="DataTableDocumentoTexto" />
									</f:facet>
										<h:outputText value="#{wrappedDocumento.wrappedObject[2]}"	styleClass="DataTableDocumentoTexto" />
								</rich:column>
								
								<rich:column style="width:5%" sortBy="#{wrappedDocumento.wrappedObject}" >
									<f:facet name="header">
										<h:outputText value="Relator"	styleClass="DataTableDocumentoTexto" />
									</f:facet>
									<h:outputText value="#{wrappedDocumento.wrappedObject[3]}"	styleClass="DataTableDocumentoTexto" />
								</rich:column>
								
								
								<rich:column  style="width:15%" sortBy="#{wrappedDocumento.wrappedObject}" >
									<f:facet name="header">
										<h:outputText value="Setor"	styleClass="DataTableDocumentoTexto" />
									</f:facet>
									
									<h:outputText value="#{wrappedDocumento.wrappedObject[4]}"	styleClass="DataTableDocumentoTexto" />
									

								</rich:column>
								
								
								<rich:column style="width:5%" sortBy="#{wrappedDocumento.wrappedObject}" >
									<f:facet name="header">
										<h:outputText value="Data Entrada"	styleClass="DataTableDocumentoTexto" />
									</f:facet>
									<h:outputText value="#{wrappedDocumento.wrappedObject[5]}"	styleClass="DataTableDocumentoTexto" />
					
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


<!-- 
	<h:commandButton id="botaoImprimir" value="Imprimir"  disabled="#{not empty wrappedDocumento}" title="Gerar PDF com registros exibidos"
                                                   actionListener="#{beanConsultarDocumentosSigilosos.imprimirDocumentosSigilososAction}"
                                                   styleClass="BotaoPadrao" />
 -->

					<a4j:commandButton styleClass="BotaoOculto" id="btnAtualizaSessao"
						actionListener="#{beanConsultarDocumentosSigilosos.atualizaSessaoAction}" />
					<a4j:commandButton id="botaoPesquisar" value="Pesquisar"
						styleClass="BotaoOculto"
						actionListener="#{beanConsultarDocumentosSigilosos.pesquisarDocumentosAction}"
						reRender="pnlResultadoPesquisa"
						onclick="exibirMsgProcessando(true)"
						oncomplete="exibirMsgProcessando(false);" />

				</h:panelGrid>
			</h:panelGrid>
				
		</a4j:form>
		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>
