<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
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
	
	// utilizada no onsubmit, requer retorno true para o submit do componente
	function exibeMensagem() {
		exibirMsgProcessando(true);
		return true;
	}

		
</script>

<f:view>
	<a4j:page pageTitle="::.. Processo de Interesse ..::"
		onload="document.getElementById('itAdvogado').focus();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Processo de Interesse" />
			</jsp:include>
			<a name="ancora"></a>
		</h:form>

		
		<a4j:form id="form" prependId="false" onreset="" styleClass="Moldura" style="padding-top:10px" >
				<div class="PainelTituloCriaTexto">
					<span> Processo de Interesse </span>
				</div>
			<h:panelGrid id="pnlCentral" style="padding-left:0px; padding-right:0px" >

				
				<!-- seleciona o responsável (suggestion)  -->
				<t:panelGroup id="grupoAdvogado">
					<h:outputText styleClass="Padrao" value="Advogado: "
						id="labelDestino"/>
					<h:inputText id="itAdvogado" size="100" 
					    value="#{beanProcessoInteresse.nomeAdvogado}"
						onchange="if ( this.value!='' ) { #{rich:component('sbAdvogado')}.callSuggestion(true) }">
						<a4j:support event="onblur"
							actionListener="#{beanProcessoInteresse.atualizarSessao}" />
					</h:inputText>
	
					<rich:suggestionbox id="sbAdvogado" height="200" width="500" 
						for="itAdvogado" 
						suggestionAction="#{beanProcessoInteresse.pesquisarAdvogado}"  
						var="advogado" nothingLabel="Nenhum registro encontrado">
						<h:column>
							<h:outputText value="#{advogado.id} - #{advogado.nome} - #{advogado.cpf} - #{advogado.papel}" />
						</h:column>
	
						<a4j:support ajaxSingle="true" event="onselect" onsubmit="exibeMensagem()" oncomplete="exibirMsgProcessando(false);" 
						    reRender="pnlItemProcesso" action="#{beanProcessoInteresse.recuperarProcessosDeInteresse}"
							eventsQueue="ajaxQueue" ignoreDupResponses="true" >
							<f:setPropertyActionListener value="#{advogado.id}"
								target="#{beanProcessoInteresse.codigoAdvogado}" />
						</a4j:support>
					</rich:suggestionbox>
				</t:panelGroup>
				<rich:toolTip for="itAdvogado" value="Informe o CPF, código ou nome do advogado e selecione-o após a pesquisa" style="width:350px; height:80px;"></rich:toolTip>
				
				<a4j:outputPanel id="pnlPesquisaProcesso" style="padding-bottom: 15px;" >
						<div style="padding-top: 10px; padding-bottom: 0px">
							<h:outputText styleClass="Padrao" value="Processo:" />
							<h:inputText style="margin-left:10px;" id="idProcesso" 
								value="#{beanProcessoInteresse.classeNumeroProcesso}">
							</h:inputText>
							<rich:hotKey
								selector="#idProcesso" key="return"
								handler="document.getElementById('btnIncluirProcesso').onclick()" />
							
							<a4j:commandButton styleClass="BotaoMais" id="btnIncluirProcesso"
								title="Inclui o processo na lista."
								actionListener="#{beanProcessoInteresse.incluirProcessoNaLista}"
								onclick="exibirMsgProcessando(true);"
								oncomplete="document.getElementById('idProcesso').value=''; exibirMsgProcessando(false);"
								reRender="pnlItemProcesso" />
						</div>
				</a4j:outputPanel>
				
				<!--  grid com os processos adicionados -->
				<a4j:outputPanel ajaxRendered="true" keepTransient="false" id="pnlItemProcesso" styleClass="MolduraInterna">
					<c:if test="${not empty beanProcessoInteresse.listaProcessos}">
						<h:outputText value="Processo(s) para carga" styleClass="Padrao" />
						<hr color="red" align="left" size="1px" width="100%" />
						<rich:dataTable headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="tres, tres" sortMode="single"
								value="#{beanProcessoInteresse.listaProcessos}"
								var="processoParaCarga"
								binding="#{beanProcessoInteresse.tabelaProcessos}"
								rows="10"
								id="tabProcesso">

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Processo" />
									</f:facet>
									<h:panelGrid columns="3">
										<h:graphicImage value="../../images/apenso.png"
												style="vertical-align: middle;" rendered="#{beanProcessoInteresse.isApenso}" />
										<h:outputText styleClass="Padrao" style="text-align:center;"
											value="#{processoParaCarga.wrappedObject.siglaClasseProcessual}  " />
										<h:outputText styleClass="Padrao" style="text-align:center;"
											value="#{processoParaCarga.wrappedObject.numeroProcessual}" />
									</h:panelGrid>
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Remover" />
									</f:facet>
									<a4j:commandButton image="../../images/remove.gif" style="border:0" 
									    rendered="#{not beanProcessoInteresse.isApenso}"
										actionListener="#{beanProcessoInteresse.removerProcessoInteresseAction}"
										reRender="pnlItemProcesso" />
								</rich:column>
						</rich:dataTable>
						<rich:datascroller id="scrollProcInteresse" for="tabProcesso" maxPages="10">
							<f:facet name="first">
								<h:outputText value="Primeira" />
							</f:facet>
							<f:facet name="last">
								<h:outputText value="Ultima" />
							</f:facet>
						</rich:datascroller>
						
					</c:if>
				</a4j:outputPanel>
				<h:panelGrid columns="4">
					<h:commandButton styleClass="BotaoPadraoEstendido" id="btnImprimirMovimentada" 
					                 value="Imprimir Movimentada" actionListener="#{beanProcessoInteresse.imprimirMovimentada}" />
					<h:commandButton styleClass="BotaoEstendido" id="btnImprimirProcessoInteresse" 
					                 value="Imprimir Processo Interesse" actionListener="#{beanProcessoInteresse.imprimirProcessoInteresse}" />
					<a4j:commandButton styleClass="BotaoPadrao" id="btnLimpar" value="Limpar"
						actionListener="#{beanProcessoInteresse.limparTelaAction}" ignoreDupResponses="true" 
						onclick="exibirMsgProcessando(true)" oncomplete="exibirMsgProcessando(false);" 
						reRender="pnlCentral"/>		
				</h:panelGrid>
			</h:panelGrid>
		</a4j:form>

		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>