
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

	function verificaCampo(campo){
		if(campo.getEntityValueById("itDescricaoDestinatario").empty){
			alert("É necessário incerir o campo de Localização Atual!")
		}
			
	} 
	

		
</script>

<f:view>
	<a4j:page pageTitle="::.. Relatório de Prescrição de Processos Criminais ..::">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Prescricao Processos Criminais" />
			</jsp:include>
		</h:form>

		<a4j:form id="form" prependId="false">
			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0"
					id="pnlCentral">
					<div class="PainelTituloCriaTexto">
						<span> Pesquisa: </span>
					</div>
					<h:panelGrid width="100%" id="pnlPesquisa">
						<h:panelGrid columns="7" id="painelPesquisa">
							<h:outputText styleClass="Padrao" value="Processo:" />
							<h:inputText id="itProcesso" size="30"
								value="#{beanConsultarPrescricao.siglaNumeroProcesso}"
								onclick="if ( this.value!='' ) {#{rich:component('sbProcesso')}.callSuggestion(true)}" />
							
							<rich:suggestionbox id="sbProcesso" height="200" width="200"
								for="itProcesso"
								suggestionAction="#{beanConsultarPrescricao.pesquisarIncidentesPrincipal}"
								var="oi" nothingLabel="Nenhum registro encontrado" >
								<h:column>
									<h:outputText value="#{oi.identificacao}" />
								</h:column>
								<a4j:support ajaxSingle="true" event="onselect"
									reRender="objIncId">
									<f:setPropertyActionListener value="#{oi.id}"
										target="#{beanConsultarPrescricao.objetoIncidente}" />
								</a4j:support>
							</rich:suggestionbox>
							<h:inputHidden value="#{beanConsultarPrescricao.objetoIncidente}"
								id="objIncId" />
						
							<h:outputText styleClass="Padrao" value="Relator Atual:" />
							<h:inputText id="itDescricaoOrig" size="80" value="#{beanConsultarPrescricao.descricaoMinistro}" 
								onchange="if ( this.value!='' ) { #{rich:component('sbOrigem')}.callSuggestion(true) }" />
	
							<rich:suggestionbox id="sbOrigem" height="200" width="500" for="itDescricaoOrig" suggestionAction="#{beanConsultarPrescricao.pesquisarMinistros}"
								var="ministro" nothingLabel="Nenhum registro encontrado." ignoreDupResponses="true" eventsQueue="ajaxQueue" minChars="1"
								fetchValue="#{ministro.nome}">
								<h:column>
									<h:outputText value="#{ministro.nome}" />
								</h:column>
								<a4j:support ajaxSingle="true" event="onselect" eventsQueue="ajaxQueue" ignoreDupResponses="true" reRender="idMinistro">
									<f:setPropertyActionListener value="#{ministro.id}" target="#{beanConsultarPrescricao.idMinistro}" />
								</a4j:support>
							</rich:suggestionbox>
							<h:inputHidden value="#{beanConsultarPrescricao.idMinistro}"
								id="idMinistro" />		
						</h:panelGrid>
						
						<h:panelGrid columns="2">
							<h:outputText styleClass="Padrao" value="* Localização Atual:"/>
						
							<h:inputText id="itDescricaoDestinatario" size="100" value="#{beanConsultarPrescricao.nomDestinatario}"
									onchange="if ( this.value!='' ) {#{rich:component('sbSetor')}.callSuggestion(true) }">
									<a4j:support event="onblur" actionListener="#{beanConsultarPrescricao.atualizaSessaoAction}" />
							</h:inputText>
				
							<rich:suggestionbox id="sbSetor" height="200" width="500" for="itDescricaoDestinatario" suggestionAction="#{beanConsultarPrescricao.pesquisarOrigensDestino}"
								var="destino" nothingLabel="Nenhum registro encontrado">
								<h:column>
									<h:graphicImage value="#{destino.urlIcone}" id="icone" style="vertical-align: middle;"/>
									<h:outputText value="  #{destino.origemDestino.id} - #{destino.origemDestino.descricao}" />
								</h:column>
			
								<a4j:support ajaxSingle="true" event="onselect" eventsQueue="ajaxQueue" ignoreDupResponses="true" reRender="idDestinatario,idTipoDestino,idDescDestino">
									<f:setPropertyActionListener value="#{destino.origemDestino.id}" target="#{beanConsultarPrescricao.codigoDestinatario}" />
									<f:setPropertyActionListener value="#{destino.origemDestino.tipoOrigemDestino}" target="#{beanConsultarPrescricao.tipoDestino}" />
									<f:setPropertyActionListener value="#{destino.origemDestino.descricao}" target="#{beanConsultarPrescricao.descDestino}" />
								</a4j:support>
							</rich:suggestionbox>
							<h:inputHidden value="#{beanConsultarPrescricao.codigoDestinatario}"
								id="idDestinatario" />		
							<h:inputHidden value="#{beanConsultarPrescricao.tipoDestino}"
								id="idTipoDestino" />		
							<h:inputHidden value="#{beanConsultarPrescricao.descDestino}"
								id="idDescDestino" />		
						</h:panelGrid>
						
						<h:panelGrid columns="10">
							<h:outputText styleClass="Padrao" value="Pena:" />
								<h:selectOneMenu value="#{beanConsultarPrescricao.codigoPena}" >
									<f:selectItems value="#{beanConsultarPrescricao.itensPena}" />
								</h:selectOneMenu>
							<h:outputText styleClass="Padrao" value="Prescrição:" />
							<t:panelGroup>
								<rich:calendar id="itDataInicial" value="#{beanConsultarPrescricao.dataInicial}" datePattern="dd/MM/yyyy" locale="pt_Br" />
								<h:outputLabel styleClass="Padrao" value=" a "></h:outputLabel>
								<rich:calendar id="itDataFinal" value="#{beanConsultarPrescricao.dataFinal}" datePattern="dd/MM/yyyy" locale="pt_Br" />
							</t:panelGroup>
							<h:selectBooleanCheckbox value="#{beanConsultarPrescricao.filtroEmTramitacao}"></h:selectBooleanCheckbox>
							<h:outputText styleClass="Padrao" value="Em tramitação" />
						</h:panelGrid>
						
						<h:panelGrid columns="2">
							<a4j:commandButton styleClass="BotaoPadrao"
								value="Pesquisar"
								id="btnPesquisarProcesso"
								actionListener="#{beanConsultarPrescricao.pesquisarProcessosPrescricaoAction}"
								ignoreDupResponses="true" 
								onclick="exibirMsgProcessando(true)"
								oncomplete="exibirMsgProcessando(false);" />
							<a4j:commandButton styleClass="BotaoPadrao"
								style="margin-left:15px;" value="Limpar"
								id="btnLimparProcesso"
								actionListener="#{beanConsultarPrescricao.limparPrescricaoAction}"
								ignoreDupResponses="true" 
								onclick="exibirMsgProcessando(true)"
								oncomplete="exibirMsgProcessando(false);" 
								reRender="pnlPesquisa"/>						
						</h:panelGrid>
					</h:panelGrid>
				
					<a4j:outputPanel ajaxRendered="true" keepTransient="false"
						id="pnlResultadoPesquisa" styleClass="MolduraInterna">
						<c:if
							test="${not empty beanConsultarPrescricao.listaProcessos}">
							<hr color="red" align="left" size="1px" width="90%" />
							<rich:dataTable headerClass="DataTableDefaultHeader" id="tabelaProcessosPrescricao"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="dezCenter, quinzeCenter, vinteCenter, vinteCenter, cincoCenter, quinzeCenter, quinzeCenter"
								value="#{beanConsultarPrescricao.listaProcessos}"
								var="wrappedDocumento"
								binding="#{beanConsultarPrescricao.tabelaProcessos}"
								rows="25">
								
								<rich:column sortBy="#{wrappedDocumento.wrappedObject.prescricaoReu.referenciaPrescricao.objetoIncidente.identificacao}">
									<f:facet name="header">
										<h:outputText value="Processo" />
									</f:facet>
									<h:outputText rendered="#{wrappedDocumento.wrappedObject.prescricaoReu.referenciaPrescricao.objetoIncidente.principal.eletronico}" value="e" style="color: red; font-weight: bold;" />
										<h:commandLink styleClass="DataTableDocumentoTexto"
										target="_blank"
										value="#{wrappedDocumento.wrappedObject.prescricaoReu.referenciaPrescricao.objetoIncidente.identificacao}"
										action="#{beanConsultarPrescricao.consultarProcessoAndamento}">
										<f:setPropertyActionListener
											target="#{beanConsultarPrescricao.seqObjetoIncidente}"
											value="#{wrappedDocumento.wrappedObject.prescricaoReu.referenciaPrescricao.objetoIncidente.principal.id}" />
									</h:commandLink>
 								</rich:column>
	
								<rich:column sortBy="#{wrappedDocumento.wrappedObject.prescricaoReu.referenciaPrescricao.nomeMinistroRelator}">
									<f:facet name="header">
										<h:outputText value="Ministro" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.prescricaoReu.referenciaPrescricao.nomeMinistroRelator}" />
								</rich:column>
	
								<rich:column sortBy="#{wrappedDocumento.wrappedObject.nomeUltimoSetorDeslocamento}">
									<f:facet name="header">
										<h:outputText value="Localização atual" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.nomeUltimoSetorDeslocamento}" />
								</rich:column>
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Tipo Pena" />
									</f:facet>
									<h:outputText styleClass="Padrao" value="#{wrappedDocumento.wrappedObject.tipoPena}"/>
								</rich:column>
								
								<rich:column sortBy = "#{wrappedDocumento.wrappedObject.dataPrescricaoPenaOrdenacao}">
									<f:facet name="header">
										<h:outputText value="Prescrição" />
									</f:facet>
										<h:outputText styleClass="Padrao" rendered="#{wrappedDocumento.wrappedObject.ehAbstrata}"
											value="#{wrappedDocumento.wrappedObject.prescricaoReu.dataPrescricaoPenaMinima}">
											<f:convertDateTime pattern="dd/MM/yyyy"/>
										</h:outputText>
										<h:outputText styleClass="Padrao" rendered="#{wrappedDocumento.wrappedObject.ehAbstrata}"
											value="  |  " />
										<h:outputText styleClass="Padrao" rendered="#{wrappedDocumento.wrappedObject.ehAbstrata}"
											value="#{wrappedDocumento.wrappedObject.prescricaoReu.dataPrescricaoPenaMaxima}">
											<f:convertDateTime pattern="dd/MM/yyyy"/>
										</h:outputText>
										<h:outputText styleClass="Padrao" rendered="#{wrappedDocumento.wrappedObject.ehConcreta}"
											value="#{wrappedDocumento.wrappedObject.prescricaoReu.dataPrescricao}"/>
								</rich:column>
								
								<rich:column sortBy = "#{wrappedDocumento.wrappedObject.tempoRestanteParaOrdenacao}" >
									<f:facet name="header">
										<h:outputText value="Tempo para prescrição" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.tempoRestante}" />
								</rich:column>	
							</rich:dataTable>
							<rich:datascroller id="dataScrollerPecas" for="tabelaProcessosPrescricao"
									fastControls="hide" maxPages="5" pageIndexVar="paginaAtual"
									pagesVar="paginas" eventsQueue="ajaxQueue"
									ignoreDupResponses="true" reRender="pnlCentral">
									<f:facet name="first">
										<h:outputText value="Primeira" />
									</f:facet>
									<f:facet name="first_disabled">
										<h:outputText value="Primeira" />
									</f:facet>
									<f:facet name="previous">
										<h:outputText value="Anterior" />
									</f:facet>
									<f:facet name="previous_disabled">
										<h:outputText value="Anterior" />
									</f:facet>
									<f:facet name="last">
										<h:outputText value="Última" />
									</f:facet>
									<f:facet name="last_disabled">
										<h:outputText value="Última" />
									</f:facet>
									<f:facet name="next">
										<h:outputText value="Próxima" />
									</f:facet>
									<f:facet name="next_disabled">
										<h:outputText value="Próxima" />
									</f:facet>
								</rich:datascroller>
						</c:if>
					</a4j:outputPanel>
				</h:panelGrid>
			</h:panelGrid>
		</a4j:form>
		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>
