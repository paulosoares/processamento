<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<%@page pageEncoding="ISO-8859-1"%>

<script type="text/javascript">

	function aguarde(mostrar, div){
	    if( mostrar == true ){
	          document.getElementById(div).innerHTML = '<img src="/processamento/images/loading.gif" /><font class="Padrao">&nbsp;&nbsp;Aguarde...</font>';
	    }
	}
	function localizaAncora(){
		location.hash="#ancora";
	}
	
</script>

<f:view>
	<a4j:page pageTitle="::.. Andamentos T ..::">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Relatório de Carga dos Autos" />
			</jsp:include>
			<div>
				<a name="ancora"></a>
			</div>
		</h:form>

		<a4j:form id="form" prependId="false">
			<h:panelGrid styleClass="MolduraExterna" cellpadding="0" cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0" id="pnlCentral">
					<a4j:outputPanel id="pnlPesquisa" styleClass="MolduraInterna">
						<div class="PainelTituloCriaTexto">
							<span> Relatório da Carga dos Autos </span>
						</div>
						
						<div style="margin-top:10px;">
							<span class="Padrao">Processo</span>
							<span>
								<h:inputText id="itProcesso" size="30"
									value="#{beanRegistrarAndamentoT.siglaNumeroProcesso}"
									onclick="if ( this.value!='' ) { #{rich:component('sbProcesso')}.callSuggestion(true) }" />
								<rich:hotKey selector="#itProcesso" key="return" handler="" />
								<rich:suggestionbox id="sbProcesso" height="200" width="200"
									for="itProcesso"
									suggestionAction="#{beanRegistrarAndamentoT.pesquisarIncidentesPrincipal}"
									var="oi" nothingLabel="Nenhum registro encontrado">
									<h:column>
										<h:outputText value="#{oi.identificacao}" />
									</h:column>
									<a4j:support ajaxSingle="true" event="onselect" action="#{beanRegistrarAndamentoT.procuraAndamentoProcessoPeloID}"
										reRender="objIncId, pnlCentral">
										<f:setPropertyActionListener value="#{oi.id}"
											target="#{beanRegistrarAndamentoT.objetoIncidente}" />
									</a4j:support>
							</rich:suggestionbox>
							<h:inputHidden value="#{beanRegistrarAndamentoT.objetoIncidente}"
								id="objIncId" />
							</span>	
						</div>
						
						<div>
							<a4j:commandButton styleClass="BotaoPadrao" id="BotaoAtualizarMarcacao" value="Limpar"
								actionListener="#{beanRegistrarAndamentoT.limparTelaAction}" 
								reRender="pnlCentral"/>
						</div>
					</a4j:outputPanel>
					
					<a4j:outputPanel ajaxRendered="true" keepTransient="false"
						id="tabJurisdicionado" styleClass="MolduraInterna"
						rendered="#{not empty beanRegistrarAndamentoT.listaAndamentoProcesso}">
							<hr color="red" align="left" size="1px" width="90%" />
							<rich:dataTable headerClass="DataTableDefaultHeader" id="tabelaAutos"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="dezesseteCenter, dezCenter, dezCenter, dezCenter, dezCenter, dezCenter, dezCenter, cinco, dezCenter"
								value="#{beanRegistrarAndamentoT.listaAndamentoProcesso}"
								var="andamentoProcessoT" width="100%"
								binding="#{beanRegistrarAndamentoT.tabelaProcessoAndamento}">
								
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Seq" />
									</f:facet>
									<h:outputText styleClass="Padrao"
									
										value="#{andamentoProcessoT.wrappedObject.numeroSequencia}" />
								</rich:column>


								<rich:column>
									<f:facet name="header">
										<h:outputText value="Cod." />
									</f:facet>
									<h:outputText styleClass="Padrao"
										
										value="#{andamentoProcessoT.wrappedObject.codigoAndamento}" />
								</rich:column>



								<rich:column>
									<f:facet name="header">
										<h:outputText value="Órgão Julgador" />
									</f:facet>
									<h:outputText styleClass="Padrao"
									
										value="#{andamentoProcessoT.wrappedObject.origemAndamentoDecisao.descricao}" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Observação" />
									</f:facet>
									<h:outputText styleClass="Padrao" id="idObservacao"
										
										value="#{andamentoProcessoT.wrappedObject.descricaoObservacaoAndamento}" escape="false"/>
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Observação Interna" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										
										value="#{andamentoProcessoT.wrappedObject.descricaoObservacaoInterna}" />
								</rich:column>
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Ações"
											styleClass="DataTableDocumentoTexto" />
									</f:facet>
									<a4j:commandLink
											oncomplete="Richfaces.showModalPanel('modalPanelObsCobranca');"
											reRender="modalPanelObsCobranca"
											actionListener="#{beanRegistrarAndamentoT.recuperaObservacaoSelecionada}">
										<h:graphicImage url="../../images/ok.png"
											title="DEscricao Observacao" />
									</a4j:commandLink>
								</rich:column>
								
							</rich:dataTable>
					</a4j:outputPanel>			
				</h:panelGrid>
			</h:panelGrid>
			<a4j:commandButton id="renderizaTelaID" reRender="pnlCentral" styleClass="BotaoOculto"/>
			
		</a4j:form>
		
		<rich:modalPanel id="modalPanelObsCobranca" width="650"
			height="150" keepVisualState="true">
			<f:facet name="header">
				<h:outputText value="Inserir observação" />
			</f:facet>
			<a4j:form prependId="false">

				<div>
					<span class="Padrao"> 
						<h:outputText styleClass="Padrao"
								value="Observação:" /> 
						<h:inputTextarea id="idTextObs" cols="100" rows="3"
							value="#{beanRegistrarAndamentoT.obsAndamento}"
							style="margin-left:40px;" />
					</span>
				</div>

				<div style="padding-top: 20px;">
					<span> 
						<a4j:commandButton styleClass="BotaoPadrao"
							actionListener="#{beanRegistrarAndamentoT.salvarObservacaoAction}"
							onclick="Richfaces.hideModalPanel('modalPanelObsCobranca');"
							value="OK" /> 
					</span> 
					<span> 
						<h:commandButton
							onclick="Richfaces.hideModalPanel('modalPanelObsCobranca');"
							styleClass="BotaoPadrao" value="Fechar" /> 
					</span>
				</div>
			</a4j:form>
		</rich:modalPanel>	
		
		<!--  "if (document.getElementById('idExisteCadastro').value = true) { alert('Para este CPF já existe uma pessoa cadastrada. Deseja alterá-la?.'); return; }" -->

	<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>