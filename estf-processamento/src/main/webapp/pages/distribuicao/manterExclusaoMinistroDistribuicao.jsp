<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<%@page pageEncoding="ISO-8859-1"%>

<f:view>
	<a4j:page pageTitle="::.. Manter Exclusão de Ministro da Distribuição ..::">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Manter Exclusão de Ministro da Distribuição" />
			</jsp:include>
			<a name="ancora"></a>
			<script type="text/javascript">
			
				function perguntaExclusao(){
					if (confirm("Confirma a exclusão do período?")){
						document.getElementById("hidRemocaoItemConfirmada").value = "S";
					} else {
						document.getElementById("hidRemocaoItemConfirmada").value = "N";
					}
				}
			
				function aguarde(mostrar, div) {
					if (mostrar == true) {
						document.getElementById(div).innerHTML = '<img src="/processamento/images/loading.gif" /><font class="Padrao">&nbsp;&nbsp;Aguarde...</font>';
					}
				}

				function caixaAlta(campo) {
					campo.value = campo.value.toUpperCase();
				}
				
			</script>
		</h:form>

		<a4j:form id="form" prependId="false">

			<h:panelGrid columns="1" id="pnlCriterioPeriodo">
				<t:panelGroup>
					<h:panelGrid columns="2">
						<t:panelGroup>
							<h:outputLabel styleClass="Padrao" value="Início do Período: "></h:outputLabel>
							<rich:calendar id="itDataInicial" value="#{beanExclusaoDistribuicao.inicioPeriodo}" 
								datePattern="dd/MM/yyyy HH:mm a" locale="pt_Br" />
						</t:panelGroup>
						<t:panelGroup>
							<h:outputLabel styleClass="Padrao" value="Fim do Período: "></h:outputLabel>
							<rich:calendar id="itDataFinal" value="#{beanExclusaoDistribuicao.fimPeriodo}" 
								datePattern="dd/MM/yyyy HH:mm" locale="pt_Br" defaultTime="23:59"/>
						</t:panelGroup>
					</h:panelGrid>
				</t:panelGroup>
			</h:panelGrid>
			
			<h:panelGrid columns="2" id="pnlCriterioTipoEministro">
				<t:panelGroup>
					<h:outputLabel styleClass="Padrao" value="Tipo de Exclusão: "></h:outputLabel>
					<h:selectOneListbox id="selectTipoExclusao" value="#{beanExclusaoDistribuicao.codigoTipoExclusao}" style="margin-left:10px;" size="1">
						<f:selectItem value="#{null}" />
						<f:selectItems value="#{beanExclusaoDistribuicao.listaTipoExclusao}" />
						<a4j:support event="onchange"/>
					</h:selectOneListbox>
				</t:panelGroup>
				<t:panelGroup>
					<h:outputLabel styleClass="Padrao" value="Ministro: "></h:outputLabel>
					<h:selectOneListbox id="selectMinistro" value="#{beanExclusaoDistribuicao.codigoMinistro}" style="margin-left:10px;" size="1">
						<f:selectItem value="#{null}" />
						<f:selectItems value="#{beanExclusaoDistribuicao.listaMinistro}" />
						<a4j:support event="onchange" />
					</h:selectOneListbox>
				</t:panelGroup>
			</h:panelGrid>

			<h:inputHidden value="#{beanExclusaoDistribuicao.alteracaoParcial}"	id="hidAlteracaoParcial" />
			
			<h:panelGrid columns="1">
				<t:panelGroup>
					<a4j:commandButton styleClass="BotaoPadrao" id="btnPesquisar" value="Pesquisar" actionListener="#{beanExclusaoDistribuicao.pesquisarAction}"
						reRender="pnlExclusoes" onclick="exibirMsgProcessando(true);" oncomplete="exibirMsgProcessando(false);" />
					<a4j:commandButton styleClass="BotaoPadrao" id="btnNovo" value="Novo" 
					    actionListener="#{beanExclusaoDistribuicao.novoAction}" oncomplete="Richfaces.showModalPanel('modalPanelEditarExclusao');" />
					<a4j:commandButton styleClass="BotaoPadrao" id="btnLimpar" value="Limpar" actionListener="#{beanExclusaoDistribuicao.limparAction}"
						reRender="form" />
				</t:panelGroup>
			</h:panelGrid>

			<!-- Listagem de exclusões de ministros da distribuição -->

			<a4j:outputPanel ajaxRendered="true" keepTransient="false" id="pnlExclusoes" styleClass="MolduraInterna">
				<c:if test="${not empty beanExclusaoDistribuicao.listaExclusao}">
					<hr color="red" align="left" size="1px" width="90%" />
					<rich:dataTable headerClass="DataTableDefaultHeader" styleClass="DataTableDefault" footerClass="DataTableDefaultFooter"
						rowClasses="DataTableRow, DataTableRow2" columnClasses="sete, sete, tres, tres, tres, tres, tres" sortMode="single"
						value="#{beanExclusaoDistribuicao.listaExclusao}" var="wrappedExclusao" binding="#{beanExclusaoDistribuicao.tabelaExclusao}" rows="10"
						id="tabExclusao">


						<rich:column sortOrder="#{columns.sortOrder}">
							<f:facet name="header">
								<h:outputText value="Ministro" />
							</f:facet>
							<h:outputText styleClass="Padrao" style="text-align:center;" value="#{wrappedExclusao.wrappedObject.ministro.nome}" />
						</rich:column>

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Tipo Exclusão" />
							</f:facet>
							<h:outputText styleClass="Padrao" style="text-align:center;" value="#{wrappedExclusao.wrappedObject.tipoExclusaoDistribuicao.descricao}" />
						</rich:column>

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Início do Período" />
							</f:facet>
							<h:outputText styleClass="Padrao" style="text-align:center;" value="#{wrappedExclusao.wrappedObject.dataInicioPeriodoFormatado}" />
						</rich:column>

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Fim do Período" />
							</f:facet>
							<h:outputText styleClass="Padrao" style="text-align:center;" value="#{wrappedExclusao.wrappedObject.dataFimPeriodoFormatado}" />
						</rich:column>

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Compensação" />
							</f:facet>
							<h:outputText styleClass="Padrao" style="text-align:center;" value="#{wrappedExclusao.wrappedObject.ajusteDistribuicaoFormatado}" />
						</rich:column>
						
						<rich:column>
							<f:facet name="header">
								<h:outputText value="Prevenção" />
							</f:facet>
							<h:outputText styleClass="Padrao" style="text-align:center;" value="#{wrappedExclusao.wrappedObject.distribuicaoPrevencaoFormatado}" />
						</rich:column>
						
						<rich:column>
							<f:facet name="header">
								<h:outputText value="Remover/Editar" />
							</f:facet>

							<a4j:commandButton image="#{beanExclusaoDistribuicao.iconeExclusao}" title="#{beanExclusaoDistribuicao.hintIconeExclusao}"
								actionListener="#{beanExclusaoDistribuicao.removerItemAction}"
								reRender="pnlExclusoes" onclick="perguntaExclusao()"
								disabled="#{beanExclusaoDistribuicao.desabilitaExclusao}" style="border:0" />
								
							<a4j:commandButton image="#{beanExclusaoDistribuicao.iconeAlteracao}" title="#{beanExclusaoDistribuicao.hintIconeAlteracao}"
								actionListener="#{beanExclusaoDistribuicao.atualizarParaEdicaoAction}"
								onclick="Richfaces.showModalPanel('modalPanelEditarExclusao');"
								disabled="#{beanExclusaoDistribuicao.desabilitaAlteracao}"
								reRender="pnlExclusoes, hidAlteracaoParcial" style="border:0" />

							<a4j:commandButton image="../../images/pesquisar.jpg" title="Consultar período de exclusão"
								actionListener="#{beanExclusaoDistribuicao.atualizarParaConsultaAction}"
								onclick="Richfaces.showModalPanel('modalPanelEditarExclusao');"
								style="border:0" />
								
						</rich:column>

					</rich:dataTable>
					<rich:datascroller for="tabExclusao" maxPages="10">
						<f:facet name="first">
							<h:outputText value="Primeira" />
						</f:facet>
						<f:facet name="last">
							<h:outputText value="Última" />
						</f:facet>
					</rich:datascroller>
				</c:if>
			</a4j:outputPanel>
			<h:inputHidden id="hidRemocaoItemConfirmada" value="#{beanExclusaoDistribuicao.remocaoItemConfirmada}"/>
		</a4j:form>
		
		<!-- popup para edição do registro -->
		<rich:modalPanel id="modalPanelEditarExclusao" width="810" height="400" binding="#{beanExclusaoDistribuicao.popupEdicaoExclusao}" 
			keepVisualState="true" style="overflow:auto">
			<f:facet name="header">
				<h:outputText value="Cadastro de Período de Exclusão de Ministro da Distribuição" />
			</f:facet>
			<a4j:form prependId="false">
				<a4j:outputPanel ajaxRendered="true" keepTransient="false" id="pnlEditarExclusao" styleClass="MolduraInterna">
					<h:panelGroup id="gridMessage" rendered="#{beanExclusaoDistribuicao.temMensagemPopup}" 
					             styleClass="InfoMessage" >
						<h:outputText id="lableMsg" value="#{beanExclusaoDistribuicao.mensagemPopup}" />
						<br/>
					</h:panelGroup>
					<t:panelGroup>
						<h:panelGrid columns="1" id="pnlEditarPeriodo">
							<t:panelGroup>
								<h:panelGrid columns="2">
									<t:panelGroup>
										<h:outputLabel styleClass="Padrao" value="* Início do Período: "/>
										<rich:calendar id="itEditarDataInicial" value="#{beanExclusaoDistribuicao.inicioPeriodo}"
													   showWeekDaysBar="false" showWeeksBar="false"
													   datePattern="dd/MM/yyyy HH:mm" locale="pt_Br" disabled="true"
													   showApplyButton="true" />
									</t:panelGroup>
									<t:panelGroup>
										<h:outputLabel styleClass="Padrao" value="* Fim do Período: "/>
										<rich:calendar id="itEditarDataFinal" value="#{beanExclusaoDistribuicao.fimPeriodo}" 
													   showWeekDaysBar="false" showWeeksBar="false"  	 
										               datePattern="dd/MM/yyyy HH:mm" locale="pt_Br" disabled="true" 
													   showApplyButton="true" />
									</t:panelGroup>
								</h:panelGrid>
							</t:panelGroup>
						</h:panelGrid>
						
						<h:panelGrid columns="2" id="pnlEditarTipoEministro">
							<t:panelGroup>
								<h:outputLabel styleClass="Padrao" value="* Tipo de Exclusão: "></h:outputLabel>
								<h:selectOneListbox id="selectEditarTipoExclusao" value="#{beanExclusaoDistribuicao.codigoTipoExclusao}" 
								                    style="margin-left:10px;" 
								                    size="1" 
								                    disabled="true" >
									<f:selectItem value="#{null}" />
									<f:selectItems value="#{beanExclusaoDistribuicao.listaTipoExclusao}" />
									<a4j:support event="onchange" 
									     actionListener="#{beanExclusaoDistribuicao.atualizarObservacaoAction}"
									     reRender="itEditarObservacao" />
								</h:selectOneListbox>
							</t:panelGroup>
							<t:panelGroup>
								<h:outputLabel styleClass="Padrao" value="* Ministro: "></h:outputLabel>
								<h:selectOneListbox id="selectEditarMinistro" value="#{beanExclusaoDistribuicao.codigoMinistro}" 
								                    style="margin-left:10px;" size="1"
								                    disabled="true">
									<f:selectItem value="#{null}" />
									<f:selectItems value="#{beanExclusaoDistribuicao.listaMinistro}" />
									<a4j:support event="onchange" />
								</h:selectOneListbox>
							</t:panelGroup>
						</h:panelGrid>
						
						<a4j:outputPanel id="pnlEditarCompensacaoPrevencao">
							<h:panelGrid columns="2" id="gridEditarCompensacaoPrevencao" columnClasses="PainelTop, PainelTop">
								<t:panelGroup>
									<span class="Padrao" style="padding-left: 1px;"> 
									    <h:panelGrid columns="2" styleClass="Moldura">
									    	<h:outputLabel value="* Ajustar contador da distribuição: " styleClass="Padrao"/>
											<div id="toolTC">
											<p style="font-family: Verdana, Arial, Helvetica, sans-serif;
		      									font-size: 11px; font-weight: bold; padding: 2px; color:#D2691E">?</p>
											</div>
											<rich:toolTip for="toolTC" style="width:350px; height:60px;" layout="block">A marcação 'Sim' gera ajuste automático dos contadores de distribuição do Ministro selecionado, 
													razão pela qual deve ser utilizada quando não deva haver compensação ao final do período de exclusão.
											</rich:toolTip>
											<div>
											<h:selectOneRadio value="#{beanExclusaoDistribuicao.compensacao}" 
															  layout="lineDirection" styleClass="Padrao" 
											                  id="rdoEditarCompensacao" disabled="true">
												<f:selectItem itemValue="S" itemLabel="Sim" />
												<f:selectItem itemValue="N" itemLabel="Não" />
												<a4j:support event="onclick" actionListener="#{beanExclusaoDistribuicao.tratarJustificativa}">
												</a4j:support>
											</h:selectOneRadio>
											</div>
										</h:panelGrid> 
									</span>
								</t:panelGroup>
								<t:panelGroup id="panelGroupPreventos" style="visibility:hidden" >
									<span class="Padrao" style="padding-left: 1px;"> 
									    <h:panelGrid columns="2" styleClass="Moldura">
									    	<h:outputLabel value="* Habilitar prevenção: " styleClass="Padrao"/>
										    	<div id="toolTP">
				      								<p style="font-family: Verdana, Arial, Helvetica, sans-serif;
				      									font-size: 11px; font-weight: bold; padding: 2px; color:#D2691E"> ?</p>
												</div>
												<rich:toolTip for="toolTP" style="width:350px; height:50px;" layout="block">A marcação deste campo com valor 'Sim' habilita a prevenção de
												    processos. 
												</rich:toolTip>
											<div>
												<h:selectOneRadio value="#{beanExclusaoDistribuicao.prevencao}" 
												                  layout="lineDirection" 
												                  id="rdoEditarPrevencao"
												                  disabled="true" styleClass="Padrao" >
													<f:selectItem itemValue="S" itemLabel="Sim" />
													<f:selectItem itemValue="N" itemLabel="Não" />
													<a4j:support event="onclick" actionListener="#{beanExclusaoDistribuicao.atualizarSessaoAction}">
													</a4j:support>
												</h:selectOneRadio>
											</div>
										</h:panelGrid> 
									</span>
								</t:panelGroup>
							</h:panelGrid>
			
							<a4j:outputPanel id="pnlEditarObservacao">
								<h:panelGrid columns="1" id="gridObservacao" columnClasses="PainelTop">
									<t:panelGroup>
										<h:outputLabel styleClass="Padrao" style="vertical-align:top" value="Observação: " id="labelEditarObservacao" for="itEditarObservacao" />
										<h:inputTextarea id="itEditarObservacao" rows="2" cols="100" 
										             value="#{beanExclusaoDistribuicao.observacao}"
										             disabled="true"/>
									</t:panelGroup>
								</h:panelGrid>
							</a4j:outputPanel>
							<!--  a justificativa é somente editável para alterações no campo compensação -->							
							<a4j:outputPanel id="pnlEditarJustificativa">
								<h:panelGrid columns="1" id="gridJustificativa" columnClasses="PainelTop">
									<t:panelGroup>
										<h:outputLabel styleClass="Padrao" style="vertical-align:top" value="Justificativa: " 
										               id="labelEditarJustificativa" for="itEditarJustificativa" />
										<h:inputTextarea id="itEditarJustificativa" rows="2" cols="100" 
										             value="#{beanExclusaoDistribuicao.justificativa}"
										             disabled="true"/>
									</t:panelGroup>
								</h:panelGrid>
							</a4j:outputPanel>
						</a4j:outputPanel>
						
						<h:panelGrid columns="1" id="botoesPopup">
							<t:panelGroup>
								<a4j:commandButton styleClass="BotaoPadrao" id="btnEditarSalvar" value="Salvar" 
								    actionListener="#{beanExclusaoDistribuicao.salvarPeriodoExclusaoAction}"
									onclick="exibirMsgProcessando(true);" oncomplete="exibirMsgProcessando(false);" reRender="botoesPopup"/>
								<a4j:commandButton styleClass="BotaoPadrao" id="btnEditarCancelar" value="Cancelar" 
								    onclick="Richfaces.hideModalPanel('modalPanelEditarExclusao');"/>
							</t:panelGroup>
						</h:panelGrid>
					
					</t:panelGroup>
				</a4j:outputPanel>
			</a4j:form>
		</rich:modalPanel>

		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>