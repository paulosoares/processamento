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
	<a4j:page pageTitle="::.. Relatório de Carga dos Autos ..::">

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
							<span class="Padrao">Autorizador/Autorizado:</span>
							<span>
								<h:inputText size="100" style="margin-left:7px;" 
									value="#{beanGerirAutosEmprestados.nomeJurisdicionado}"/>
							</span>	
							<span class="Padrao">Processo</span>
							<span>
								<h:inputText id="itProcesso" size="30"
								value="#{beanGerirAutosEmprestados.siglaNumeroProcesso}"
								onclick="if ( this.value!='' ) { #{rich:component('sbProcesso')}.callSuggestion(true) }" />
								<rich:hotKey selector="#itProcesso" key="return" handler="" />
								<rich:suggestionbox id="sbProcesso" height="200" width="200"
								for="itProcesso"
								suggestionAction="#{beanGerirAutosEmprestados.pesquisarIncidentesPrincipal}"
								var="oi" nothingLabel="Nenhum registro encontrado">
								<h:column>
									<h:outputText value="#{oi.identificacao}" />
								</h:column>
								<a4j:support ajaxSingle="true" event="onselect"
									reRender="objIncId">
									<f:setPropertyActionListener value="#{oi.id}"
										target="#{beanGerirAutosEmprestados.objetoIncidente}" />
								</a4j:support>
							</rich:suggestionbox>
							<h:inputHidden value="#{beanGerirAutosEmprestados.objetoIncidente}"
								id="objIncId" />
							</span>	
						</div>
						
						<div style="margin-top:10px;">	
							<span class="Padrao">Período</span>
							<span>
								<rich:calendar id="itDataInicio" 
									value="#{beanGerirAutosEmprestados.dataInicial}" 
									datePattern="dd/MM/yyyy" locale="pt_Br" />
								<h:outputText value="a" style="margin-left:5px; margin-right:5px;"/>
								<rich:calendar id="itDataFim" 
									value="#{beanGerirAutosEmprestados.dataFinal}" 
									datePattern="dd/MM/yyyy" locale="pt_Br" />	
							</span>	
							<span class="Padrao" style="margin-left: 10px;">Situação</span>
							<span>
								<h:selectOneMenu
									value="#{beanGerirAutosEmprestados.idSituacao}">
									<f:selectItems value="#{beanGerirAutosEmprestados.itensSituacaoAutos}"/>
								</h:selectOneMenu>	
							</span>
							<span>
								<a4j:commandButton styleClass="BotaoPadrao" value="Pesquisar" style="margin-left: 10px;"
									id="btnPesquisarJurisdicionado" actionListener="#{beanGerirAutosEmprestados.pesquisarAutosAction}"
									ignoreDupResponses="true"  onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" reRender="pnlCentral"/>
							</span>
						</div>
					</a4j:outputPanel>
					
					<a4j:outputPanel ajaxRendered="true" keepTransient="false"
						id="tabJurisdicionado" styleClass="MolduraInterna"
						rendered="#{not empty beanGerirAutosEmprestados.listaAutosEmprestados}">
						<c:if test="${beanGerirAutosEmprestados.renderizaTabelaCarga == true}">
							<hr color="red" align="left" size="1px" width="90%" />
							<rich:dataTable headerClass="DataTableDefaultHeader" id="tabelaAutos"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="tres, dezCenter, quinzeCenter, quinzeCenter, dezCenter, cincoCenter, cincoCenter, dezCenter, dezCenter, dezCenter, sete"
								value="#{beanGerirAutosEmprestados.listaAutosEmprestados}"
								var="wrappedJuris" width="100%"
								binding="#{beanGerirAutosEmprestados.tabelaAutosEmprestados}"
								rows="25">
								
								<rich:column>
									<f:facet name="header">
										<a4j:commandButton image="../../../images/setabaixo.gif"
											onclick="exibirMsgProcessando(true)"
											oncomplete="exibirMsgProcessando(false);"
											actionListener="#{beanGerirAutosEmprestados.marcarTodosAutos}" />
									</f:facet>
									<h:selectBooleanCheckbox
										onclick="document.getElementById('BotaoAtualizarMarcacao').click();"
										value="#{wrappedJuris.checked}" />
								</rich:column>
								
								<rich:column sortBy="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.deslocaRetirada.andamentoProcesso.objetoIncidente.identificacao}" >
									<f:facet name="header">
										<h:outputText value="Processo" />
									</f:facet>				
									<h:commandLink styleClass="PadraoLink" style="text-align:center;
										#{wrappedJuris.wrappedObject.situacaoEmprestimo.codigo == 1 ? 'color: red' : ''};"
										target="_blank"
										value="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.deslocaRetirada.andamentoProcesso.objetoIncidente.identificacao}"
										action="#{beanGerirAutosEmprestados.consultarProcessoDigital}">
										<f:setPropertyActionListener
											target="#{beanGerirAutosEmprestados.seqObjetoIncidente}"
											value="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.deslocaRetirada.andamentoProcesso.objetoIncidente.id}" />
									</h:commandLink>
								</rich:column>	
								
								<rich:column sortBy="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.nomeAutorizadorDaCarga}">
									<f:facet name="header">
										<h:outputText value="Autorizador" />
									</f:facet>
									<h:outputText value="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.nomeAutorizadorDaCarga}" 
										style="#{wrappedJuris.wrappedObject.situacaoEmprestimo.codigo == 1 ? 'color: red' : ''};"/>
								</rich:column>
								
								<rich:column sortBy="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.nomeAutorizadoDaCarga}">
									<f:facet name="header">
										<h:outputText value="Autorizado" />
									</f:facet>
									<h:outputText value="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.nomeAutorizadoDaCarga}" 
										style="#{wrappedJuris.wrappedObject.situacaoEmprestimo.codigo == 1 ? 'color: red' : ''};"/>
								</rich:column>
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Contatos" />
									</f:facet>				
									<h:outputText value="#{wrappedJuris.wrappedObject.contatosJurisdicionado}" 
										style="#{wrappedJuris.wrappedObject.situacaoEmprestimo.codigo == 1 ? 'color: red' : ''};"/>
								</rich:column>	
								
								<rich:column sortBy="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.papelJurisdicionado.jurisdicionado.oab}">
									<f:facet name="header">
										<h:outputText value="OAB" />
									</f:facet>				
									<h:outputText value="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.papelJurisdicionado.jurisdicionado.oab}" 
									style="width:50px;
										#{wrappedJuris.wrappedObject.situacaoEmprestimo.codigo == 1 ? 'color: red' : ''};"/>
								</rich:column>
								
								<rich:column sortBy="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.quantidadeCobrancaDevolucao}">
									<f:facet name="header">
										<h:outputText value="Cobrança" />
									</f:facet>				
									<h:outputText value="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.quantidadeCobrancaDevolucao}" 
										style="width:50px;
										#{wrappedJuris.wrappedObject.situacaoEmprestimo.codigo == 1 ? 'color: red' : ''};"/>
								</rich:column>
								
								<rich:column sortBy="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.dataEmprestimo}">
									<f:facet name="header">
										<h:outputText value="Data Carga" />
									</f:facet>				
									<h:outputText value="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.dataEmprestimo}" 
										style="width:50px;
										#{wrappedJuris.wrappedObject.situacaoEmprestimo.codigo == 1 ? 'color: red' : ''};"/>
								</rich:column>
								
								<rich:column sortBy="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.dataDevolucaoPrevista}">
									<f:facet name="header">
										<h:outputText value="Data Prevista" />
									</f:facet>				
									<h:outputText value="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.dataDevolucaoPrevista}" 
										style="width:50px;
										#{wrappedJuris.wrappedObject.situacaoEmprestimo.codigo == 1 ? 'color: red' : ''};"/>
								</rich:column>	
								
								<rich:column sortBy="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.deslocaDevolucao.dataRecebimento}">
									<f:facet name="header">
										<h:outputText value="Data devolução" />
									</f:facet>				
									<h:outputText value="#{wrappedJuris.wrappedObject.emprestimoAutosProcesso.deslocaDevolucao.dataRecebimento}" 
										style="width:50px;
										#{wrappedJuris.wrappedObject.situacaoEmprestimo.codigo == 1 ? 'color: red' : ''};">
										<f:convertDateTime pattern="dd/MM/yyyy"/> 
									</h:outputText>
								</rich:column>		
								
								<rich:column sortBy="#{wrappedJuris.wrappedObject.situacaoEmprestimo.descricao}">
									<f:facet name="header">
										<h:outputText value="Situação" />
									</f:facet>				
									<h:outputText value="#{wrappedJuris.wrappedObject.situacaoEmprestimo.descricao}" 
										style="width:50px;
										#{wrappedJuris.wrappedObject.situacaoEmprestimo.codigo == 1 ? 'color: red' : ''};"/>
								</rich:column>
								
							</rich:dataTable>
							<rich:datascroller id="dataScrollerCarga" for="tabelaAutos"
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
									
					<a4j:outputPanel>
						<div style="margin-top:10px; margin-bottom: 5px;">
							<span>
								<a4j:commandButton value="Cobrar" styleClass="BotaoPadrao" reRender="modalPanelObsCobranca"
									actionListener="#{beanGerirAutosEmprestados.exibeObservacaoCobrancaAction}" onclick="localizaAncora();"
									oncomplete="exibirMsgProcessando(false); Richfaces.showModalPanel('modalPanelObsCobranca');"/>
								<a4j:commandButton value="Invalidar Carga" styleClass="BotaoPadraoEstendido" reRender="pnlCentral"
									actionListener="#{beanGerirAutosEmprestados.invalidarCargaAction}"
									oncomplete="localizaAncora();"/>
								<a4j:commandButton value="Invalidar Cobrança" styleClass="BotaoPadraoEstendido" reRender="pnlCentral"
									actionListener="#{beanGerirAutosEmprestados.invalidarCobrancaAction}" onclick="localizaAncora();"/>										
								<a4j:commandButton value="Nova Carga dos Autos" styleClass="BotaoPadraoEstendido" reRender="pnlCentral"
									action="#{beanGerirAutosEmprestados.novaCargaAutos}"/>			
								<h:commandButton value="Imprimir" styleClass="BotaoPadrao" 
									actionListener="#{beanGerirAutosEmprestados.imprimirCargaAutosAction}"/>
								<a4j:commandButton styleClass="BotaoPadrao"
									style="margin-left:15px;" value="Limpar"
									id="btnLimparPesquisaJurisdicionado"
									actionListener="#{beanGerirAutosEmprestados.limparAutos}"
									ignoreDupResponses="true" onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" reRender="pnlCentral"/>	
							</span>
						</div>
					</a4j:outputPanel>
				</h:panelGrid>
			</h:panelGrid>
			<a4j:commandButton id="renderizaTelaID" reRender="pnlCentral" styleClass="BotaoOculto"/>
			<a4j:commandButton styleClass="BotaoOculto" id="BotaoAtualizarMarcacao"
				actionListener="#{beanGerirAutosEmprestados.atualizarMarcacao}" />
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
							value="#{beanGerirAutosEmprestados.obsInvalidarCobranca}"
							style="margin-left:40px;" />
					</span>
				</div>

				<div style="padding-top: 20px;">
					<span> 
						<a4j:commandButton styleClass="BotaoPadrao"
							actionListener="#{beanGerirAutosEmprestados.cobrarAutosAction}"
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