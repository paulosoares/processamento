<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />




<f:view>
	<a4j:page pageTitle="::.. Registrar Andamento ..::"
		onload="document.getElementById('idProcesso').focus();">

		<script type="text/javascript"
			src="<%=request.getContextPath()%>/scripts/andamento.js"></script>

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Registrar Andamento" />
			</jsp:include>
			<a name=ancora"></a>
		</h:form>

		<a4j:form id="form" prependId="false" onreset="" >
		
			<h:panelGrid id="pnlCentral"
				style="padding-left:5px; padding-right:5px">

					<a4j:outputPanel id="pnlPesquisaProcesso">

						<div class="PainelTituloCriaTexto" style="width:100%">
							<span>Pesquisa por Processo:</span>
						</div>

						<div style="padding-top: 10px;">
							<h:outputText styleClass="Padrao" value="Processo:" />

							<h:inputText style="margin-left:10px;" id="idProcesso"
								onkeypress="return noEnter(event);"
								value="#{beanRegistrarAndamento.identificacaoProcesso}"
								onclick="exibirSuggestionBoxPesquisaProcesso(this, #{rich:component('sgPesquisaProcesso')});"/>

						<span style="padding-left: 5px;"> <rich:suggestionbox
										id="sgPesquisaProcesso"
										suggestionAction="#{beanRegistrarAndamento.pesquisaSuggestionBox}"
										for="idProcesso" var="result"
										fetchValue="#{result.identificacao}"
										nothingLabel="Nenhum registro encontrado." width="400"
										ignoreDupResponses="true" eventsQueue="ajaxQueue" minChars="1">
								<a4j:support ajaxSingle="true" event="onselect"
									onsubmit="exibirMsgProcessando(true);"
										oncomplete="if(#{beanRegistrarAndamento.lancamentoIndevidoException}) {exibirMsgProcessando(false);document.getElementById('idProcesso').focus();} else {exibirMsgProcessando(false);document.getElementById('idAndamento').focus();}"
									eventsQueue="ajaxQueue" ignoreDupResponses="true"
									reRender="pnlCentral,idPanelPartes">
											<f:setPropertyActionListener value="#{result}"
												target="#{beanRegistrarAndamento.incidenteSelecionado}" />
										</a4j:support>
										<h:column>
									<h:outputText
										rendered="#{result.tipoObjetoIncidente.codigo == 'PR' && result.eletronico}"
										value="e" style="color: red; font-weight: bold;" />
											
											<h:outputText value="#{result.identificacao}"
												styleClass="oiSuggestion" />
									<h:outputText
										rendered="#{result.tipoObjetoIncidente.codigo == 'PR'}"
										value=" » " />
									<h:outputText
										rendered="#{result.tipoObjetoIncidente.codigo == 'PR'}"
										value="#{result.ministroRelatorAtual.nome}" styleClass="green" />
											
										</h:column>
								</rich:suggestionbox>
							</span>
						</div>
						
						<h:outputText 
										value="" styleClass="green" />
						
						<h:outputLabel value="Processo em setor diverso" 
									   rendered="#{beanRegistrarAndamento.mostrarMensagemProcessoEmSetorDiverso}"
									   style="color: red;"/>

						<a4j:commandButton id="btLimpar" styleClass="BotaoOculto"
							actionListener="#{beanRegistrarAndamento.limpar}"
							reRender="pnlCentral" />
					</a4j:outputPanel>


					<c:if
						test="${beanRegistrarAndamento.processo ne null && beanRegistrarAndamento.permiteAndamento}">
						<a4j:outputPanel id="pnlInformacoesProcesso" ajaxRendered="true"
							keepTransient="false">
							
															<h:panelGrid columns="2">
								<t:div>
									<span class="Padrao"> <h:outputLabel value="Relator(a): "></h:outputLabel></span>
										<span class="Padrao" style="padding-right: 25px; color: black">
										<h:outputLabel  styleClass="font-color:red"
											value="#{beanRegistrarAndamento.processo.ministroRelator.nome}"></h:outputLabel>
									</span>
								</t:div>
							</h:panelGrid>
							<h:panelGrid columns="2">
								<t:div>
									<span class="Padrao"> <h:outputLabel value="Relator(a) do último incidente: "></h:outputLabel></span>
										<span class="Padrao" style="padding-right: 25px; color: black">
										<h:outputLabel  styleClass="font-color:red"
											value="#{beanRegistrarAndamento.processo.ministroRelatorAtual.nome}"></h:outputLabel>
									</span>
								</t:div>
							</h:panelGrid>
							
							<h:panelGrid columns="6">

							

								<t:div>
								<span class="Padrao" style="padding-left: 25px;"> <a4j:commandLink
										styleClass="Padrao" value="Partes"
											onclick="Richfaces.showModalPanel('modalPanelPartes');"/>
									</span>
								</t:div>

								<t:div>
									<span class="Padrao" style="padding-left: 25px;"> <h:outputLabel
											value="Procedência: "></h:outputLabel> <h:outputLabel
											value="#{beanRegistrarAndamento.procedencia}"></h:outputLabel>
									</span>
								</t:div>

								<c:if test="${beanRegistrarAndamento.temApensos}">
									<t:div>
									<span class="Padrao" style="padding-left: 25px;"> <a4j:commandLink
											styleClass="Padrao"
												value="#{beanRegistrarAndamento.processo.quantidadeApensos} apenso(s)" 
												oncomplete="Richfaces.showModalPanel('modalPanelApensos');"
												reRender="idPanelApensos"
												actionListener="#{beanRegistrarAndamento.recuperarApensosAction}" />
										</span>
									</t:div>
								</c:if>

								<c:if test="${not beanRegistrarAndamento.temApensos}">
									<t:div>
										<span class="Padrao" style="padding-left: 25px;"> <h:outputLabel
											value="Sem apensos"></h:outputLabel>
									</span>
									</t:div>
								</c:if>
								
								<t:div></t:div>

								<t:div>
								<span class="Padrao" style="padding-left: 25px;"> <h:outputLabel
										value="Preferências: #{beanRegistrarAndamento.preferencias}"
										title="#{beanRegistrarAndamento.titlePreferencias}" />
									</span>
								</t:div>

								<t:div>
									<span class="Padrao" style="color: red"> <h:outputLabel
											value="#{beanRegistrarAndamento.processo.nivelSigilo}"></h:outputLabel>
									</span>
								</t:div>

								<t:div>
									<span class="Padrao" style="padding-left: 25px; color: red">
										<h:outputLabel value="#{beanRegistrarAndamento.midia}"></h:outputLabel>
									</span>
								</t:div>
	<t:div>
									<span class="Padrao" style="padding-left: 25px;"> <h:commandLink
											value="Acompanhamento Processual (STF Digital)" target="_blank"
											action="#{beanConsultarDocumentoExterno.consultarProcessoDigital}">
											<f:setPropertyActionListener
												target="#{beanConsultarDocumentoExterno.seqObjetoIncidente}"
												value="#{beanRegistrarAndamento.processo.id}" />
									</h:commandLink>
								</span>
								</t:div>
									<c:if test="${beanRegistrarAndamento.temPecas}">
									<t:div>
										<span class="Padrao" style="padding-left: 25px;"> <h:commandLink
											value="Peças Eletrônicas (STF Digital)" target="_blank"
											action="#{beanConsultarDocumentoExterno.consultarProcessoDigitalPecas}">
											<f:setPropertyActionListener
												target="#{beanConsultarDocumentoExterno.seqObjetoIncidente}"
												value="#{beanRegistrarAndamento.processo.id}" />
									</h:commandLink>
								</span>
									</t:div>
									
								</c:if>

								<c:if test="${not beanRegistrarAndamento.temPecas}">
									<t:div>
										<span class="Padrao" style="padding-left: 25px;"> <h:outputLabel value="Peças Eletrônicas (0 peças)"></h:outputLabel>
									</span>
									</t:div>
								</c:if>

								

							</h:panelGrid>
						</a4j:outputPanel>

					<a4j:outputPanel id="pnlPesquisaAndamento"
						style="padding-top: 5px;">
						
							<h:panelGrid columns="1">						
							<t:div>
								<span class="Padrao"> <h:outputLabel value="Código:"
										style="padding-right:54px" /> <h:inputText style="width:400px"
										id="idAndamento"
										binding="#{beanRegistrarAndamento.inputAndamento }"
										onkeypress="return noEnter(event);"
										onclick="exibirSuggestionBox(this,#{rich:component('sgPesquisaAndamento')});">
									</h:inputText>
									
									
									<a4j:outputPanel id="pnlEditarDestinoBaixa">
										<c:if test="${beanRegistrarAndamento.processo.isEletronico}" >
											<h:selectBooleanCheckbox value="#{beanRegistrarAndamento.precisaVerificarCodigoOrigem}" id="checkBoxEditarDestinoBaixa" 
												disabled="#{beanRegistrarAndamento.precisaVerificarCodigoOrigem}" rendered="#{beanRegistrarAndamento.renderCheckBoxEditarDestinoBaixa}">
												<a4j:support ajaxSingle="true" immediate="true" action="#{beanRegistrarAndamento.alterarPrecisaVerificarCodigoOrigem}" event="onchange" reRender="idPrecisaVerificarCodigoOrigem, modalPanelVerificarCodigoOrigem, tooltipPrecisaVerificarCodigoOrigem"
												onsubmit="exibirMsgProcessando(true);" oncomplete="exibirMsgProcessando(false);"/>
											</h:selectBooleanCheckbox>
											<t:popup displayAtDistanceX="20" displayAtDistanceY="5"  id="tooltipPrecisaVerificarCodigoOrigem">
												<h:outputLabel value="Editar destino de baixa/remessa" styleClass="Padrao" rendered="#{beanRegistrarAndamento.renderCheckBoxEditarDestinoBaixa}"/>
													<f:facet name="popup">
														<t:panelGroup styleClass="LinhaParTabelaDadosPreview"  rendered="#{beanRegistrarAndamento.precisaVerificarCodigoOrigem}">
															<t:outputText value="Processo sem destino de baixa/remessa cadastrado" styleClass="Padrao"/>
														</t:panelGroup>
													</f:facet>
											</t:popup>
										</c:if>
									</a4j:outputPanel>
									
									
									
									
									<rich:suggestionbox id="sgPesquisaAndamento"
										suggestionAction="#{beanRegistrarAndamento.pesquisaAndamentosSuggestionBox}"
										for="idAndamento" var="result"
										nothingLabel="Nenhum registro encontrado." width="400"
										ignoreDupResponses="true" eventsQueue="ajaxQueue" minChars="1">

										<a4j:support ajaxSingle="true" event="onselect"
											eventsQueue="ajaxQueue" ignoreDupResponses="true" 
											reRender="pnlDeslocar,idPanelVariaveis,modalPanelOrigemDecisao,idMensagemConfirmacaoLancarAndamento,pnlAssinatura,idPanelConfirmacaoRegistro,modalPanelConfirmacaoRegistro,idPrecisaVerificarCodigoOrigem,pnlEditarDestinoBaixa">
											<f:setPropertyActionListener value="#{result}"
												target="#{beanRegistrarAndamento.andamentoSelecionado}" />
										</a4j:support>

										<h:column>
											<h:outputText value="#{result.id}" styleClass="oiSuggestion" />
											<h:outputText value=" - " />
											<h:outputText value="#{result.descricao}" styleClass="green" />
										</h:column>
									</rich:suggestionbox> <h:panelGroup layout="horizontal" id="pnlDeslocar">
										<h:panelGroup
											rendered="#{beanRegistrarAndamento.selecionadoAndamento8204ou8238 && beanRegistrarAndamento.podeDeslocarAutomaticamente}">
											<h:outputText styleClass="Padrao"
												value="Deslocar para o gabinete do Ministro: " />
											<h:selectOneMenu
												value="#{beanRegistrarAndamento.idMinistroSelecionado}"
															 styleClass="Input"  id="idComboMinistroRevisor" >
												<f:selectItems value="#{beanRegistrarAndamento.ministros}" />
											</h:selectOneMenu>
										</h:panelGroup>
										<h:panelGroup
											rendered="#{beanRegistrarAndamento.podeDeslocarAutomaticamente}">
											<h:selectBooleanCheckbox
												value="#{beanRegistrarAndamento.deslocarAutomaticamente}" />
											<h:outputText value="Deslocar automaticamente"/>
										</h:panelGroup>
									</h:panelGroup>
								</span>
							</t:div>

							<t:div>
								<span class="Padrao"> <h:outputLabel value="Observação:"
										styleClass="topAligned">
										<h:graphicImage style="padding-left:5px; padding-right:5px"
											title="O conteúdo desse campo será divulgado no Acompanhamento Processual do Portal do STF (Internet)"
											alt="Ajuda observação" value="/images/ajuda.gif" />
									</h:outputLabel> <h:inputTextarea style="width:400px" id="observacao"
										value="#{beanRegistrarAndamento.observacao }">
										<a4j:support ajaxSingle="true" event="onchange"
											onsubmit="if (!validarTamanhoObservacoes('observacao','observacaoInterna')) {return false;};" />
									</h:inputTextarea> <h:outputLabel value="Obs. interna:" styleClass="topAligned"
										style="padding-left:10px" /> <h:inputTextarea
										style="width:400px; margin-left:5px" id="observacaoInterna"
										value="#{beanRegistrarAndamento.observacaoInterna }">
										<a4j:support ajaxSingle="true" event="onchange" onsubmit="if (!validarTamanhoObservacoes('observacao','observacaoInterna')) {return false;};"/></h:inputTextarea>
									
									<a4j:outputPanel id="pnlAssinatura">
										<a4j:commandButton id="btNovoAndamento" styleClass="BotaoPadrao" 
											value="Registrar"  rendered="#{!beanRegistrarAndamento.andamentoGeraDocumento}"
											disabled="#{beanRegistrarAndamento.disabledNovoAndamento}"
											alt="#{beanRegistrarAndamento.altBotaoNovoAndamento}"
											title="#{beanRegistrarAndamento.titleBotaoNovoAndamento}"
											style="#{beanRegistrarAndamento.styleBotaoNovoAndamento}; margin-bottom: 8px; margin-left: 5px;"
											onclick="executarRegistrarAndamento(#{beanRegistrarAndamento.mensagemDeRestricaoRegistroDeAndamento != null},#{beanRegistrarAndamento.alertaApensos}, #{beanRegistrarAndamento.validarDadosBaixa})"
											action="#"/>
										<a4j:commandButton styleClass="BotaoOculto" reRender="pnlCentral" 
												actionListener="#{beanRegistrarAndamento.registrarAndamento}" id="idBotaoRegistrarAndamento"/>
												
										<a4j:commandButton id="btNovoAndamentoAssinando" styleClass="BotaoEstendido" reRender="pnlCentral"
											value="Registrar e Assinar" 
											disabled="#{beanRegistrarAndamento.disabledNovoAndamento}"
											rendered="#{beanRegistrarAndamento.andamentoGeraDocumento }"
											alt="#{beanRegistrarAndamento.altBotaoNovoAndamento}"
											title="#{beanRegistrarAndamento.titleBotaoNovoAndamento}"
											style="#{beanRegistrarAndamento.styleBotaoNovoAndamento}; margin-bottom: 8px; margin-left: 5px;"
											actionListener="#{beanRegistrarAndamento.registrarAndamentoEAssinar}"
											onclick="registrarAndamento( #{beanRegistrarAndamento.mensagemDeRestricaoRegistroDeAndamento != null}, #{beanRegistrarAndamento.alertaApensos}, #{beanRegistrarAndamento.validarDadosBaixa} );"
											oncomplete="acionaBotaoChamaAssinador();"/>		
										<h:inputHidden id="mensagemApensos" value="#{beanRegistrarAndamento.mensagemApensos}" />							

									</a4j:outputPanel> <a4j:commandButton id="btSalvarAndamento"
										styleClass="BotaoOculto"
										reRender="pnlCentral,idTabelaProcessos"
										actionListener="#{beanRegistrarAndamento.registrarAndamento}"
										oncomplete="limparDadosModais();" /> <a4j:commandButton
										id="btAbrirTelaConfirmacaoOrigem" styleClass="BotaoOculto"
										reRender="idPanelVerificarCodigoOrigem"
										actionListener="#{beanRegistrarAndamento.vazio}"
										oncomplete="Richfaces.showModalPanel('modalPanelVerificarCodigoOrigem');" />
										
									<a4j:commandButton id="btAbrirTelaListarDecisoes"
										styleClass="BotaoOculto" reRender="idPanelListarDecisoes"
										actionListener="#{beanRegistrarAndamento.vazio}"
										oncomplete="Richfaces.showModalPanel('modalPanelListarDecisoes');" />
										
									<a4j:commandButton id="btConfirmarDeslocamento"
										styleClass="BotaoOculto"
										actionListener="#{beanRegistrarAndamento.confirmarDeslocamento}"/>
										
									<h:commandButton id="btnChamaAssinador"
										styleClass="BotaoOculto"
										action="#{beanRegistrarAndamento.chamarAssinador}" /> <a4j:commandButton
										styleClass="BotaoOculto"
									    actionListener="#{beanRegistrarAndamento.verificarAndamento}"
										reRender="idProcessoTemaTable" id="idBotaoRerenderModal"/>																				
								</span>
								
							</t:div>
							</h:panelGrid>
						</a4j:outputPanel>
					</c:if>

				<a4j:outputPanel ajaxRendered="true" keepTransient="false"
					id="pnlAndamentosProcesso">

						<c:if test="${beanRegistrarAndamento.andamentosProcesso ne null}">
							<hr color="red" align="left" size="1px" width="90%"
								style="margin-left: 10px" />

							<t:div style="padding-bottom:5px">
							<h:outputText styleClass="PainelTituloCriaTexto"
								style="margin-left: 10px; margin-right:5px"
								value="Andamento Processual" />

								<s:span>
									<a4j:commandLink styleClass="Padrao"  
										value="#{beanRegistrarAndamento.textoExpandirRetrairAndamentosProcesso}" 
										reRender="pnlAndamentosProcesso"
										actionListener="#{beanRegistrarAndamento.expandirRetrairAndamentosProcesso}" />
								</s:span>
							</t:div>
						<rich:dataTable headerClass="DataTableDefaultHeader"
							id="tableAndamentoProcesso" styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="dois, cinco, dois, dezLeft, dez, dezLeft, dezLeft, cinco, cinco, cinco, dois"
								value="#{beanRegistrarAndamento.andamentosProcesso}"
								var="andamentoProcesso" rows="30"
							binding="#{beanRegistrarAndamento.tabelaAndamentos}">

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Seq" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										style=" #{beanRegistrarAndamento.styleLancamentoIndevido}"
										value="#{andamentoProcesso.numeroSequencia}" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Data" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										style="text-align:center; #{beanRegistrarAndamento.styleLancamentoIndevido}"
										value="#{andamentoProcesso.dataAndamento}">
										<f:convertDateTime pattern="dd/MM/yyyy"/>
									</h:outputText>
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Cod." />
									</f:facet>
									<h:outputText styleClass="Padrao"
										style="text-align:center; #{beanRegistrarAndamento.styleLancamentoIndevido}"
										value="#{andamentoProcesso.codigoAndamento}" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Descrição do Andamento*" />
									</f:facet>
									<t:popup displayAtDistanceX="20" displayAtDistanceY="5">

										<t:outputText styleClass="Padrao"
											style="text-align:center; #{beanRegistrarAndamento.styleLancamentoIndevido}"
											value="#{andamentoProcesso.tipoAndamento.descricao}" />

										<f:facet name="popup">
											<t:panelGroup>
												<t:panelGrid cellpadding="0" cellspacing="0"
													rowClasses="LinhaParTabelaDadosPreview"
													columnClasses="ColunaTabelaDadosComAlinhamentoEsquerda"
													columns="2">
													
													<h:panelGrid columns="2" >
														<h:outputText value="Incluído por:" styleClass="Padrao" />
														<h:outputText value="#{andamentoProcesso.usuarioInclusao}"
															styleClass="Padrao" />
													</h:panelGrid>
													
													<h:panelGrid columns="2">
														<t:outputText value=" em:" styleClass="Padrao" />
														<t:outputText value="#{andamentoProcesso.dataInclusao}"
															styleClass="Padrao">
															<f:convertDateTime pattern="dd/MM/yyyy HH:mm" />
														</t:outputText>
													</h:panelGrid>
													
													<h:panelGrid columns="2">
														<t:outputText value="Alterado por: " styleClass="Padrao" />
														<t:outputText value="#{andamentoProcesso.usuarioAlteracao}"
														styleClass="Padrao" />
													</h:panelGrid>
													
													<h:panelGrid columns="2">
														<t:outputText value="em:" styleClass="Padrao" />
														<t:outputText value="#{andamentoProcesso.dataAlteracao}"
															styleClass="Padrao">
															<f:convertDateTime pattern="dd/MM/yyyy HH:mm" />
														</t:outputText>
													</h:panelGrid>

												</t:panelGrid>
											</t:panelGroup>
										</f:facet>
									</t:popup>
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Órgão Julgador" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										style="text-align:center; #{beanRegistrarAndamento.styleLancamentoIndevido}"
										value="#{andamentoProcesso.origemAndamentoDecisao.descricao}" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Observação" />
									</f:facet>
									<h:outputText styleClass="Padrao" id="idObservacao"
										style="text-align:center; #{beanRegistrarAndamento.styleLancamentoIndevido}" 
									value="#{beanRegistrarAndamento.descricaoObservacaoAndamento}"
									escape="false" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Observação Interna" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										style="text-align:center; color: red; #{beanRegistrarAndamento.styleLancamentoIndevido}"
										value="#{andamentoProcesso.descricaoObservacaoInterna}" />
								</rich:column>
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Peça" />
									</f:facet>
								<a4j:commandLink value="peças"
									actionListener="#{beanRegistrarAndamento.recuperarPecasAndamento}"
									ajaxSingle="true"
									rendered="#{andamentoProcesso.possuiSeqDocumento and  !andamentoProcesso.lancamentoIndevido}"
									oncomplete="Richfaces.showModalPanel('modalPanelPecasAndamento');"
									reRender="modalPanelPecasAndamento" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Ações" />
									</f:facet>
									<h:panelGrid columns="2">

									<c:if
										test="${beanRegistrarAndamento.usuarioEditarAndamentoProcesso}">
									  
										<a4j:commandLink reRender="divEditarAndamento"
											disabled="#{!beanRegistrarAndamento.podeLancarAndamentoIndevido}"
											oncomplete="Richfaces.showModalPanel('modalPanelEditarAndamento');"
											actionListener="#{beanRegistrarAndamento.atualizarAndamentoSelecionado}">
											<h:graphicImage
												url="#{beanRegistrarAndamento.urlImagemEditarAndamento}" 
												title="#{beanRegistrarAndamento.titleEditarLancamentoIndevido}"/>
										</a4j:commandLink>
	
										</c:if>
										
										<a4j:commandLink											
											disabled="#{!beanRegistrarAndamento.podeLancarAndamentoIndevido}"
											actionListener="#{beanRegistrarAndamento.atualizarAndamentoSelecionadoParaLancamentoIndevido}"
											oncomplete="if(#{beanRegistrarAndamento.lancamentoIndevidoException}) {alert('#{beanRegistrarAndamento.msgLancamentoIndevidoException}')} else {document.getElementById('idBotaoLancamentoIndevido').onclick();}"
											reRender="pnlAndamentosProcesso,pnlCentral,idPanelVariaveis,idPanelInfoAndamentoIndevido">
											<h:graphicImage
												url="#{beanRegistrarAndamento.urlImagemLancamentoIndevido}"
												title="#{beanRegistrarAndamento.titleLancamentoIndevido}" />
										</a4j:commandLink>
										
									</h:panelGrid>
								</rich:column>
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Identificação" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										style="text-align:center; #{beanRegistrarAndamento.styleLancamentoIndevido}"
										value="#{beanRegistrarAndamento.identificacao}" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Tipo Devolução" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										style="text-align:center; #{beanRegistrarAndamento.styleLancamentoIndevido}"
										value="#{andamentoProcesso.tipoDevolucao.descricao}" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Presidente" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										style="text-align:center; #{beanRegistrarAndamento.styleLancamentoIndevido}"
										value="#{andamentoProcesso.presidenteInterino.nome}" />
								</rich:column>
							</rich:dataTable>
						<rich:datascroller id="scroll" for="tableAndamentoProcesso"
							maxPages="10" align="left" />
						<a4j:commandButton id="idBotaoLancamentoIndevido"
							style="visibility:hidden;"
								onclick="if (!lancarAndamentoIndevidoOnClick()) {return;}"
								actionListener="#{beanRegistrarAndamento.lancarOuCancelarAndamentoIndevido}"
								oncomplete="if(#{beanRegistrarAndamento.lancamentoIndevidoException}) {alert('#{beanRegistrarAndamento.msgLancamentoIndevidoException}')};"
								reRender="pnlAndamentosProcesso,pnlCentral"/>

						</c:if>
					</a4j:outputPanel>

				<a4j:outputPanel id="idPanelVariaveis">
					<h:inputHidden id="idSolicitarProcessoEObservacao"
						value="#{beanRegistrarAndamento.solicitarProcessoEObservacao}"
						binding="#{beanRegistrarAndamento.inputSolicitarProcessoEObservacao}"></h:inputHidden>
					<h:inputHidden id="idSolicitarProcessos"
						value="#{beanRegistrarAndamento.solicitarProcessos}"
						binding="#{beanRegistrarAndamento.inputSolicitarProcessos}"></h:inputHidden>
					<h:inputHidden id="idSolicitarPeticao"
						value="#{beanRegistrarAndamento.solicitarPeticao}"
						binding="#{beanRegistrarAndamento.inputSolicitarPeticao}"></h:inputHidden>
					<h:inputHidden id="idSolicitarOrigemDecisao"
						value="#{beanRegistrarAndamento.solicitarOrigemDecisao}"
						binding="#{beanRegistrarAndamento.inputSolicitarOrigemDecisao}"></h:inputHidden>
					<h:inputHidden id="idSolicitarPresidenteInterino"
						value="#{beanRegistrarAndamento.solicitarPresidenteInterino}"
						binding="#{beanRegistrarAndamento.inputSolicitarPresidenteInterino}"></h:inputHidden>
					<h:inputHidden id="idSolicitarTipoDevolucao"
						value="#{beanRegistrarAndamento.solicitarTipoDevolucao}"
						binding="#{beanRegistrarAndamento.inputSolicitarTipoDevolucao}"></h:inputHidden>
					<h:inputHidden id="idPrecisaConfirmacaoLancarAndamento"
						value="#{beanRegistrarAndamento.precisaConfirmacaoLancarAndamento}"></h:inputHidden>
					<h:inputHidden id="idPrecisaVerificarCodigoOrigem"
						value="#{beanRegistrarAndamento.precisaVerificarCodigoOrigem}"
                        binding="#{beanRegistrarAndamento.inputPrecisaVerificarCodigoOrigem}"></h:inputHidden>
					<h:inputHidden id="idPrecisaListarDecisoes"
						value="#{beanRegistrarAndamento.precisaListarDecisoes}"
						binding="#{beanRegistrarAndamento.inputPrecisaListarDecisoes}"></h:inputHidden>
					<h:inputHidden id="idMensagemConfirmacaoLancarAndamento"
						value="#{beanRegistrarAndamento.mensagemConfirmacaoLancarAndamento}"></h:inputHidden>
					<h:inputHidden id="idCancelarAndamentoIndevido"
						value="#{beanRegistrarAndamento.cancelarLancamentoIndevido}"></h:inputHidden>
					<h:inputHidden id="idProcessoFindo"
						value="#{beanRegistrarAndamento.processoFindo}"
						binding="#{beanRegistrarAndamento.inputProcessoFindo}"></h:inputHidden>
					<h:inputHidden id="hidCodigoAndamento" 
						value="#{beanRegistrarAndamento.codigoAndamentoSelecionado}"></h:inputHidden>	
					<h:inputHidden id="hidSucesso" 
						value="#{beanRegistrarAndamento.sucesso}"
						binding="#{beanRegistrarAndamento.inputSucesso}"></h:inputHidden>
					<h:inputHidden id="hidTemCertidao" 
						value="#{beanRegistrarAndamento.temCertidao}"
						binding="#{beanRegistrarAndamento.inputTemCertidao}"></h:inputHidden>
					<h:inputHidden id="hidProcessoDifereSetorUsuario" 
						value="#{beanRegistrarAndamento.processoDifereSetorUsuario}"
						binding="#{beanRegistrarAndamento.inputProcessoDifereSetorUsuario}"></h:inputHidden>
					<h:inputHidden id="idSolicitarTema"
						value="#{beanRegistrarAndamento.solicitarTema}"
						binding="#{beanRegistrarAndamento.inputSolicitarTema}"></h:inputHidden>	
					<h:inputHidden id="qtdProcessosTemasSelecionados" 
					    value="#{beanRegistrarAndamento.quantidadeProcessosTemasSelecionados}" />						

				</a4j:outputPanel>

			</h:panelGrid>
			<a4j:outputPanel id="idPanelVerificacaoProcesso">
				<script type="text/javascript">
				verificarConfirmacaoSelecaoProcesso();
			</script>
				<h:inputHidden id="idConfirmarSelecaoProcesso"
					value="#{beanRegistrarAndamento.confirmacaoAndamento}" />
				<h:inputHidden id="idMensagemConfirmacaoProcesso"
					value="#{beanRegistrarAndamento.msgConfirmacaoAndamento}" />
			</a4j:outputPanel>
		
		</a4j:form>
		
		<rich:modalPanel id="modalPanelPecasAndamento" keepVisualState="true">
			<f:facet name="header">
				<h:outputText value="Peças do andamento" />
			</f:facet>
			<c:if
				test="${not empty beanRegistrarAndamento.listaPecasProcessoEletronico}">
			<a4j:form prependId="false">
					<rich:dataTable id="tabelaPecasAndamento" var="valor"
						headerClass="DataTableDefaultHeader" styleClass="DataTableDefault"
						footerClass="DataTableDefaultFooter"
						rowClasses="DataTableRow, DataTableRow2"
					value="#{beanRegistrarAndamento.listaPecasProcessoEletronico}">
					<rich:column>
						<f:facet name="header">
							<h:outputText value="Descrição Peça" />
						</f:facet>
							<h:outputText style="text-align:center;"
								value="#{valor.descricaoPeca}" styleClass="Padrao" />

					</rich:column>
					<rich:column>
						<f:facet name="header">
							<h:outputText value="PDF" />
						</f:facet>
						<h:dataTable value="#{valor.documentos}" var="value">
							<h:column>
								<h:outputLink
										value="#{facesContext.externalContext.request.contextPath}/verPDFServlet?seqDocumentoEletronico=#{value.documentoEletronico.id}&nomeDocumento=#{valor.descricaoPeca}">
										<h:graphicImage style="text-align:center;"
											value="/images/pdf.png" />
								</h:outputLink>
							</h:column>
						</h:dataTable>
					</rich:column>
				</rich:dataTable>
			</a4j:form>
			</c:if>
			<c:if
				test="${empty beanRegistrarAndamento.listaPecasProcessoEletronico}">
				<h:outputText value="Nenhum registro encontrado."
					styleClass="Padrao" />
			</c:if>
			</br>
			<a4j:commandButton
				onclick="Richfaces.hideModalPanel('modalPanelPecasAndamento');"
				styleClass="BotaoPadrao" value="Fechar" />
		</rich:modalPanel>

		<rich:modalPanel id="modalPanelAnexarProcesso" width="400"
			height="135" keepVisualState="true"
			onshow="identificacaoProcesso.value = ''; idObservacao.value = ''; idApensos.value = ''; identificacaoProcesso.focus();">

			<f:facet name="header">
				<h:outputText id="idMensagemProcesso"
					value="Selecione o processo principal" />
			</f:facet>
			<a4j:form prependId="false">
				<div>
					<h:panelGrid columns="2">
						<h:outputText styleClass="Padrao" value="Processo:" />

						<h:inputText style="margin-left:10px;" id="identificacaoProcesso"
							onkeypress="return noEnter(event);"
							onclick="exibirSuggestionBoxPesquisaProcesso(this, #{rich:component('sgBoxProcesso')});">
						</h:inputText>

						<h:outputText styleClass="Padrao" value="Quantidade de apensos:"/>

						<h:inputText style="margin-left:10px; width:30px" id="idApensos"
							value="#{beanRegistrarAndamento.quantidadeApensos}"
							onkeypress="return noEnter(event);">
						</h:inputText>
					</h:panelGrid>

					<rich:suggestionbox id="sgBoxProcesso"
						suggestionAction="#{beanRegistrarAndamento.pesquisaSuggestionBox}"
						for="identificacaoProcesso" var="result"
						fetchValue="#{result.identificacao}"
						nothingLabel="Nenhum registro encontrado." width="400"
						ignoreDupResponses="true" eventsQueue="ajaxQueue" minChars="1">

						<a4j:support ajaxSingle="true" event="onselect"
							eventsQueue="ajaxQueue" ignoreDupResponses="true"
							reRender="idApensos">
							<f:setPropertyActionListener value="#{result}"
								target="#{beanRegistrarAndamento.processoSelecionado}" />
						</a4j:support>

						<h:column rendered="#{result.tipoObjetoIncidente.codigo == 'PR'}">
							<h:outputText rendered="#{result.eletronico}" value="e"
								style="color: red; font-weight: bold;" />
							<h:outputText value="#{result.identificacao}"
								styleClass="oiSuggestion" />
							<h:outputText value=" » " />
							<h:outputText value="#{result.ministroRelatorAtual.nome}"
								styleClass="green" />
						</h:column>
					</rich:suggestionbox>
				</div>

				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao"
						onclick="processoPrincipalOnClick(); return;" value="Cadastrar" />

					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelAnexarProcesso');" 
						oncomplete="limparDadosModais();"
						actionListener="#{beanRegistrarAndamento.cancelarRegistroAndamento}"
						reRender="pnlCentral,idPanelVariaveis" styleClass="BotaoPadrao"
						value="Cancelar" />
				</div>
			</a4j:form>

		</rich:modalPanel>

		<rich:modalPanel id="modalPanelAnexarProcessos" width="400"
			autosized="true" keepVisualState="true"
			onshow="identificacaoProcessos.value = ''; idObservacao.value = ''; identificacaoProcessos.focus();">

			<f:facet name="header">
				<h:outputText id="idMensagemProcessos"
					value="Selecione os processos principais" />
			</f:facet>
			<a4j:form prependId="false">
				<div>
					<h:panelGrid columns="2">
						<h:outputText styleClass="Padrao" value="Processo:" />

						<h:inputText style="margin-left:10px;" id="identificacaoProcessos"
							onkeypress="return noEnter(event);"
							onclick="exibirSuggestionBoxPesquisaProcesso(this, #{rich:component('sgBoxProcessos')});">
						</h:inputText>

						<h:outputText styleClass="Padrao" value="Quantidade de apensos:"/>
	
						<h:inputText style="margin-left:10px; width:30px"
							id="idApensosProcessos"
							value="#{beanRegistrarAndamento.quantidadeApensos}"
							onkeypress="return noEnter(event);">
							<a4j:support ajaxSingle="true" event="onchange"/>
						</h:inputText>
					</h:panelGrid>

					<div style="padding-top: 20px; text-align: center">
						<a4j:commandButton
							onclick="if (!adicionarProcessoPrincipaisOnClick()) {return;};"
							oncomplete="exibirMsgProcessando(false); document.getElementById('identificacaoProcessos').value = '';"
							actionListener="#{beanRegistrarAndamento.adicionarProcessoSelecionado}"
							reRender="idTabelaProcessos,idApensosProcessos"
							styleClass="BotaoPadrao" value="Adicionar" />
					</div>

					<rich:suggestionbox id="sgBoxProcessos"
						suggestionAction="#{beanRegistrarAndamento.pesquisaSuggestionBox}"
						for="identificacaoProcessos" var="result"
						fetchValue="#{result.identificacao}"
						nothingLabel="Nenhum registro encontrado." width="400"
						ignoreDupResponses="true" eventsQueue="ajaxQueue" minChars="1">

						<a4j:support ajaxSingle="true" event="onselect" 
							eventsQueue="ajaxQueue" ignoreDupResponses="true"
							reRender="idApensosProcessos">
							<f:setPropertyActionListener value="#{result}"
								target="#{beanRegistrarAndamento.adicionarProcessoSelecionado}" />
						</a4j:support>

						<h:column rendered="#{result.tipoObjetoIncidente.codigo == 'PR'}">
							<h:outputText rendered="#{result.eletronico}" value="e"
								style="color: red; font-weight: bold;" />
							<h:outputText value="#{result.identificacao}"
								styleClass="oiSuggestion" />
							<h:outputText value=" » " />
							<h:outputText value="#{result.ministroRelatorAtual.nome}"
								styleClass="green" />
						</h:column>
					</rich:suggestionbox>
 
 					<fieldset style="margin-top:20px">
						<legend>
							<h:outputLabel value="Processos" styleClass="Padrao" />
						</legend>
						<rich:dataTable headerClass="DataTableDefaultHeader"
							styleClass="DataTableDefault"
							footerClass="DataTableDefaultFooter"
							rowClasses="DataTableRow, DataTableRow2"
							value="#{beanRegistrarAndamento.processosSelecionados}"
							id="idTabelaProcessos" var="processos"
							binding="#{beanRegistrarAndamento.tabelaProcessosSelecionados}">
							
							<rich:column>
								<f:facet name="header">
									<h:outputText value="Processo" />
								</f:facet>
								<h:outputText styleClass="Padrao"
									value="#{processos.identificacao}" />
							</rich:column>
							
							<rich:column>
								<f:facet name="header">
									<h:outputText value="Apensos" />
								</f:facet>
								<h:outputText styleClass="Padrao"
									value="#{processos.quantidadeApensosFixo}" />
							</rich:column>

							<rich:column>
								<f:facet name="header">
									<h:outputText value="Ministro" />
								</f:facet>
								<h:outputText styleClass="Padrao"
									value="#{processos.ministroRelatorAtual.nome}" />
							</rich:column>
							
							<rich:column>
								<f:facet name="header">
									<h:outputText value="Ações" />
								</f:facet>
								<a4j:commandLink value="Excluir"
									actionListener="#{beanRegistrarAndamento.excluirProcessoSelecionado}"
									reRender="idTabelaProcessos" />
							</rich:column>
						</rich:dataTable>
					</fieldset>

				</div>

				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao"
						onclick="processosPrincipaisOnClick(); return;" value="Cadastrar" />

					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelAnexarProcessos');" 
						oncomplete="limparDadosModais();"
						actionListener="#{beanRegistrarAndamento.cancelarRegistroAndamento}"
						reRender="pnlCentral,idPanelVariaveis,idTabelaProcessos"
						styleClass="BotaoPadrao" value="Cancelar" />
				</div>
			</a4j:form>

		</rich:modalPanel>
		
		
		<rich:modalPanel id="modalPanelAnexarTema" width="300" height=""
			keepVisualState="true"
			onshow="identificacaoTema.value = ''; idObservacao.value = ''; idApensosTema.value = ''; identificacaoTema.focus();">

			<f:facet name="header">
				<h:outputText id="idMensagemTema" value="Selecione o(s) tema(s)" />
			</f:facet>
			<a4j:form prependId="false">
				<div>
					<h:outputText styleClass="Padrao"
						value="Lance todos os temas indicados na decisão" />
					<h:panelGrid>
						<h:panelGrid columns="2">
							<h:outputText styleClass="Padrao" value="Tema:" />
	
							<h:inputText style="margin-left:10px;" id="identificacaoTema"
								onkeypress="return noEnter(event);"
								onclick="exibirSuggestionBoxPesquisaProcesso(this, #{rich:component('sgBoxTema')});">
							</h:inputText>
						</h:panelGrid>
						
						<rich:dataTable id="idProcessoTemaTable" var="registro"
							headerClass="DataTableDefaultHeader"
							styleClass="DataTableDefault"
							footerClass="DataTableDefaultFooter"
							rowClasses="DataTableRow, DataTableRow2"
							binding="#{beanRegistrarAndamento.tabelaProcessoTema}"
							value="#{beanRegistrarAndamento.listaProcessoTema}">
							<rich:column>
								<f:facet name="header">
									<h:outputText value="Tema(s) selecionado(s)" />
								</f:facet>								
								<h:outputText style="text-align:center;"
									value="Tema nº #{registro.tema.numeroSequenciaTema} - "
									styleClass="Padrao" />
								<h:outputText rendered="#{registro.objetoIncidente.eletronico}"
									value="e" style="color: red; font-weight: bold;" />
								<h:outputText value="#{registro.identificacaoSimples}"
									styleClass="Padrao" />
							</rich:column>
							<rich:column>
								<a4j:commandLink reRender="idProcessoTemaTable"
									actionListener="#{beanRegistrarAndamento.removerTema}">
									<h:graphicImage url="../../images/remove.gif"
										title="Remover tema"/>
								</a4j:commandLink>							
							</rich:column>
						</rich:dataTable>						
						
					</h:panelGrid>

					<rich:suggestionbox id="sgBoxTema"
						suggestionAction="#{beanRegistrarAndamento.pesquisaSuggestionBoxTema}"
						for="identificacaoTema" var="result"
						fetchValue="#{result.tema.numeroSequenciaTema}"
						nothingLabel="Nenhum registro encontrado." width="300"
						ignoreDupResponses="true" eventsQueue="ajaxQueue" minChars="1">

						<a4j:support ajaxSingle="true" event="onselect" 
							eventsQueue="ajaxQueue" ignoreDupResponses="true"
							oncomplete="limparCampoIdentificacaoTema()"
							reRender="idProcessoTemaTable,idPanelVariaveis">
							<f:setPropertyActionListener value="#{result}"
								target="#{beanRegistrarAndamento.processoTemaSelecionado}" />
						</a4j:support>

						<h:column>
							<h:outputText
								value="Tema nº #{result.tema.numeroSequenciaTema} - "
								styleClass="oiSuggestion" />
							<h:outputText rendered="#{result.objetoIncidente.eletronico}"
								value="e" style="color: red; font-weight: bold;" />
							<h:outputText value="#{result.identificacaoSimples}"
								styleClass="oiSuggestion" />
						</h:column>
					</rich:suggestionbox>
				</div>

				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao"
						onclick="processoTemaOnClick(); return;" value="Cadastrar" />

					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelAnexarTema');" 
						oncomplete="limparDadosModais();"
						actionListener="#{beanRegistrarAndamento.cancelarRegistroAndamento}"
						reRender="pnlCentral,idPanelVariaveis" styleClass="BotaoPadrao"
						value="Cancelar" />
						
				</div>
			</a4j:form>
		</rich:modalPanel>				

		<rich:modalPanel id="modalPanelAnexarPeticao" width="300" height="120"
			keepVisualState="true"
			onshow="document.getElementById('identificacaoPeticao').setValue(''); document.getElementById('identificacaoPeticao').focus();">

			<f:facet name="header">
				<h:outputText id="idMensagemPeticao" value="Selecione a petição" />
			</f:facet>
			<a4j:form prependId="false">
				<div>
					<h:panelGrid columns="2">
						<h:outputText styleClass="Padrao" value="Petição:" />

						<h:inputText style="margin-left:10px;" id="identificacaoPeticao"
							value="#{beanRegistrarAndamento.peticaoSelecionada}">
						</h:inputText>
					</h:panelGrid>
				</div>

				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao"
						actionListener="#{beanRegistrarAndamento.registrarAndamento}"
						onclick="Richfaces.hideModalPanel('modalPanelAnexarPeticao');"
						reRender="pnlPesquisaAndamento,idPanelVariaveis" value="Cadastrar" />

					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelAnexarPeticao');"
						oncomplete="limparDadosModais();"
						actionListener="#{beanRegistrarAndamento.cancelarRegistroAndamento}"
						reRender="pnlPesquisaAndamento,idPanelVariaveis"
						styleClass="BotaoPadrao" value="Cancelar" />
				</div>
			</a4j:form>

		</rich:modalPanel>

		<rich:modalPanel id="modalPanelOrigemDecisao" width="370" height="125"
			keepVisualState="true">

			<f:facet name="header">
				<h:outputText value="Selecione a origem da decisão" />
			</f:facet>
			<a4j:form prependId="false">
				<div>
					<h:panelGrid columns="2">
						<h:outputText styleClass="Padrao" value="Origem da decisão:" />

						<h:selectOneMenu value="#{beanRegistrarAndamento.idOrigemDecisao}"
							styleClass="Input" id="idComboOrigemDecisao">
							<f:selectItems value="#{beanRegistrarAndamento.origensDecisao}" />
						</h:selectOneMenu>
					</h:panelGrid>
				</div>

				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao"
						actionListener="#{beanRegistrarAndamento.verificarPresidenteInterino}"
						oncomplete="Richfaces.hideModalPanel('modalPanelOrigemDecisao'); if (!pedirInformacaoAdicional()) {document.getElementById('btSalvarAndamento').click();}"
						onclick="if (document.getElementById('idComboOrigemDecisao').value == -1) { alert('Selecione uma origem de decisão.'); return; }"
						reRender="pnlPesquisaAndamento,idPanelVariaveis" value="Salvar" />

					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelOrigemDecisao');"
						oncomplete="limparDadosModais();"
						actionListener="#{beanRegistrarAndamento.cancelarRegistroAndamento}"
						reRender="pnlPesquisaAndamento,idPanelVariaveis"
						styleClass="BotaoPadrao" value="Cancelar" />
				</div>
			</a4j:form>

		</rich:modalPanel>

		<rich:modalPanel id="modalPanelDevolucao" width="300" height="125"
			keepVisualState="true">

			<f:facet name="header">
				<h:outputText value="Selecione o tipo da devolução" />
			</f:facet>
			<a4j:form prependId="false">
				<div>
					<h:panelGrid columns="2">
						<h:outputText styleClass="Padrao" value="Tipo da Devolução:" />
						<h:selectOneMenu value="#{beanRegistrarAndamento.idTipoDevolucao}"
							styleClass="Input" id="idComboDevolucao" >
							<f:selectItems value="#{beanRegistrarAndamento.tiposDevolucao}" />
						</h:selectOneMenu>
					</h:panelGrid>
				</div>

				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao"
						onclick="if (document.getElementById('idComboDevolucao').value == -1) { alert('Selecione um tipo de devolução.'); return; } else {Richfaces.hideModalPanel('modalPanelDevolucao'); if (!pedirInformacaoAdicional()) {document.getElementById('btSalvarAndamento').click();}}"
						value="Cadastrar" reRender="pnlPesquisaAndamento,idPanelVariaveis" />

					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelDevolucao');"
						oncomplete="limparDadosModais();"
						actionListener="#{beanRegistrarAndamento.cancelarRegistroAndamento}"
						reRender="pnlPesquisaAndamento,idPanelVariaveis"
						styleClass="BotaoPadrao" value="Cancelar" />
				</div>
			</a4j:form>

		</rich:modalPanel>

		<rich:modalPanel id="modalPanelPresidenteInterino" width="360"
			height="125" keepVisualState="true">

			<f:facet name="header">
				<h:outputText value="Selecione o Presidente" />
			</f:facet>
			<a4j:form prependId="false">
				<div>
					<h:panelGrid columns="2">
						<h:outputText styleClass="Padrao" value="Presidente:" />

						<h:selectOneMenu
							value="#{beanRegistrarAndamento.idPresidenteInterino}"
							styleClass="Input" id="idComboPresidenteInterino">
							<f:selectItems
								value="#{beanRegistrarAndamento.ministrosPresidenteInterino}" />
						</h:selectOneMenu>
					</h:panelGrid>
				</div>

				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao"
						actionListener="#{beanRegistrarAndamento.registrarAndamento}"
						onclick="if (document.getElementById('idComboPresidenteInterino').value == -1) { alert('Selecione o presidente.'); return; } else {Richfaces.hideModalPanel('modalPanelPresidenteInterino');}"
						value="Ok" reRender="pnlPesquisaAndamento,idPanelVariaveis" />

					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelPresidenteInterino');"
						oncomplete="limparDadosModais();"
						actionListener="#{beanRegistrarAndamento.cancelarRegistroAndamento}"
						reRender="pnlPesquisaAndamento,idPanelVariaveis"
						styleClass="BotaoPadrao" value="Cancelar" />
				</div>
			</a4j:form>

		</rich:modalPanel>

		<rich:modalPanel id="modalPanelListarDecisoes" keepVisualState="true"
			autosized="true" width="900">

			<f:facet name="header">
				<h:outputText value="Despacho / Decisão" />
			</f:facet>
			<a4j:form prependId="false" id="formModalDecisoes">
				<t:div id="idPanelListarDecisoes"
					style="padding-bottom: 10px; padding-top: 10px; padding-left: 10px; padding-right: 5px">
					
					<div style="width: 900px; height: 300px; overflow-y: scroll;">
						<h:panelGrid columns="3">
							<h:outputLabel value="Número Único do Processo:"
								styleClass="Padrao" />
							<h:inputText
								value="#{beanRegistrarAndamento.numeroUnicoProcesso}"
								styleClass="Input" />

						</h:panelGrid>


						<fieldset style="margin-top: 20px">
							<legend>
								<h:outputLabel value="Selecione o despacho/decisão:"
									styleClass="Padrao" />
							</legend>


							<rich:dataTable headerClass="DataTableDefaultHeader"
								id="tabelaPecas" styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="dois, cinco, cinco, dez, cinco, cinco"
								value="#{beanRegistrarAndamento.listaPecasDTO}" var="result"
								binding="#{beanRegistrarAndamento.tabelaPecasSelecionadas}">
								
								<t:selectOneRadio id="idRadiosPeca" value="#{beanRegistrarAndamento.pecaSelecionada}">
 									<%--<a4j:support ajaxSingle="true" event="onchange"
 										action="#{beanRegistrarAndamento.atualizaPecaSelecionada}" />--%> 
									<f:selectItems value="#{beanRegistrarAndamento.pecasSelectItens}" />	
								</t:selectOneRadio>
								
								<rich:column>
									<t:radio for="idRadiosPeca" index="#{beanRegistrarAndamento.indexPeca}" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Data da juntada" />
									</f:facet>
									<h:outputText value="#{result.dataInclusao}">
										<f:convertDateTime locale="pt_BR" type="date"
											pattern="dd/MM/yyyy" />
									</h:outputText>
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Nº da peça" />
									</f:facet>
									<h:outputText value="#{result.numeroOrdemPeca }" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Tipo" />
									</f:facet>
									<h:outputText value="#{result.tipoPecaProcesso.descricao}" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Já utilizada em Vista?" />
									</f:facet>
									<h:outputText value="#{result.isUtilizadaEmVista}" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Documento" />
									</f:facet>
									<h:outputLink value="#{result.urlDownloadPeca}"
										rendered="#{result.urlDownloadPeca != null}">
										<h:graphicImage value="/images/pdf.png"></h:graphicImage>
									</h:outputLink>
								</rich:column>
										
							</rich:dataTable>
							<br />
							<div style="text-align: left">
								<a4j:commandButton id="expandirPecas"
									actionListener="#{beanRegistrarAndamento.expandirListaPecas}"
									reRender="tabelaPecas" value="Expandir Lista"
									rendered="#{beanRegistrarAndamento.exibirExpandirListaPecas}"
									oncomplete="$('expandirPecas').style.display='none'"
									style="width: 100px;" />
							</div>

						</fieldset>
					</div>
				</t:div>

				<div style="padding-top: 20px; text-align: center">

					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelListarDecisoes');exibirMsgProcessando(true);"
						oncomplete="if(!gerarCertidaoBaixa()){exibirMsgProcessando(false);}"
						actionListener="#{beanRegistrarAndamento.registrarAndamento}"
						reRender="pnlPesquisaAndamento,idPanelVariaveis"
						id="idBotaoConfirmarLisaDecisoes" styleClass="BotaoPadraoEstendido"
						value="Registrar Andamento" />

					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelListarDecisoes');"
						oncomplete="limparDadosModais();"
						actionListener="#{beanRegistrarAndamento.cancelarRegistroAndamento}"
						reRender="pnlPesquisaAndamento,idPanelVariaveis"
						styleClass="BotaoPadrao" value="Cancelar" />


				</div>
			</a4j:form>

		</rich:modalPanel>

		<rich:modalPanel id="modalPanelVerificarCodigoOrigem"
			keepVisualState="true" autosized="true" width="1200">

			<f:facet name="header">
				<h:outputText value="Verificação das Origens" />
			</f:facet>
			<a4j:form prependId="false" id="formModal">
				<f:subview id="viewMessagesOrigem">
					<a4j:outputPanel id="outputPanelMessages" ajaxRendered="true"
						keepTransient="false">

						<t:panelGrid id="pnlMessagesOrigem" forceId="true"
							rendered="#{not empty facesContext.maximumSeverity}"
							cellpadding="0" cellspacing="0" columns="1"
							style="width: 100%; text-align: center;">

							<t:messages errorClass="ErrorMessage" style="text-align: left"
								infoClass="InfoMessage" warnClass="WarningMessage"
								showSummary="true" showDetail="true" layout="table" />
						</t:panelGrid>

					</a4j:outputPanel>

				</f:subview>
				<t:div id="idPanelVerificarCodigoOrigem"
					style="padding-bottom: 10px; padding-top: 10px; padding-left: 10px; padding-right: 5px">
				<div>
						<h:panelGrid columns="3">
							<h:outputLabel value="Número Único do Processo:"
								styleClass="Padrao" />
							<h:inputText
								value="#{beanRegistrarAndamento.numeroUnicoProcesso}"
								styleClass="Input" />
							<a4j:commandButton action="#"
							oncomplete="if(!#{beanRegistrarAndamento.verificarNumeroUnico}) {if (confirm('O número único desse processo está incorreto. Verifique se o número se encontra no seguinte formato XXXXXXX-DD.AAAA.J.TR.OOOO. Gostaria de continuar?')) {document.getElementById('idBotaoSalvarNumeroUnico').click();}} else {document.getElementById('idBotaoSalvarNumeroUnico').click();}"
							styleClass="BotaoPadraoEstendido" value="Salvar Número Único" />
							
							<a4j:commandButton styleClass="BotaoOculto"
								reRender="idTabelaOrigensCadastradas"
								actionListener="#{beanRegistrarAndamento.salvarNumeroUnicoProcesso}"
								id="idBotaoSalvarNumeroUnico" />
						</h:panelGrid>
							
					<fieldset>
							<legend>
								<h:outputLabel value="Incluir nova origem para devolução"
									styleClass="Padrao" />
							</legend>
							
						<h:panelGrid columns="2">
							
							
					
							
							
							
							<h:outputLabel value="Origem:" styleClass="Padrao"/>
							
				
				<h:inputText id="identificacaoOrigem" size="100" value=""
							onkeypress="return noEnter(event);"
							onclick="exibirSuggestionBoxPesquisaProcesso(this, #{rich:component('sgBoxOrigem')});">
				</h:inputText>
						
					<rich:suggestionbox id="sgBoxOrigem"
						suggestionAction="#{beanRegistrarAndamento.pesquisarDestinatarioAction}"
						for="identificacaoOrigem" var="result"
					
						nothingLabel="Nenhum registro encontrado." width="600"
						ignoreDupResponses="true" eventsQueue="ajaxQueue" minChars="1">
						
						<h:column>
						<h:outputText
							value="#{result.origemDestino.id} - #{result.origemDestino.descricao}" />
					</h:column>
						<a4j:support ajaxSingle="true" event="onselect"
							eventsQueue="ajaxQueue" ignoreDupResponses="true"
							reRender="idComboProcedencias">
							<f:setPropertyActionListener value="#{result.origemDestino.id}"
								target="#{beanRegistrarAndamento.idOrigemDevolucao}" />	
						</a4j:support>
					</rich:suggestionbox>
							
							</h:panelGrid>
				
						<div style="padding-top: 20px; text-align: center">
								<a4j:commandButton styleClass="BotaoPadraoEstendido"
									value="Inserir Origem" 
								actionListener="#{beanRegistrarAndamento.inserirOrigem}"
								onclick="document.getElementById('identificacaoOrigem').value = ''"
								reRender="identificacaoOrigem,itDescricaoDestinatario,idPanelVerificarCodigoOrigem,identificacaoOrigem"/>
						</div>
					</fieldset>
					

								<a4j:jsFunction name="setOrigemSelecionada" action="#{beanRegistrarAndamento.verificaOrigemModificadaParaSTJ}" reRender="idBotaoValidaBaixa"  > 
										<a4j:actionparam name="origemSelecionada" assignTo="#{beanRegistrarAndamento.origemSelecionada}" /> 
 							   </a4j:jsFunction> 
						
						<fieldset style="margin-top:20px">
							<legend>
								<h:outputLabel value="Origens cadastradas pela autuação"
									styleClass="Padrao" />
							</legend>
				
							<rich:dataTable headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								value="#{beanRegistrarAndamento.origensCadastradas}"
								id="idTabelaOrigensCadastradas"
								var="historicoOrigem"
								rendered="#{beanRegistrarAndamento.precisaVerificarCodigoOrigem}"
								binding="#{beanRegistrarAndamento.tabelaOrigensCadastradas}">
								
								<t:selectOneRadio id="idRadiosOrigens" value="#{beanRegistrarAndamento.origemSelecionada}">
									<f:selectItems value="#{beanRegistrarAndamento.origensCadastradasSelectItens}" />
									<a4j:support ajaxSingle="true" event="onclick" reRender="idBotaoValidaBaixa" oncomplete="submitCheckedRadio()"/>
							    </t:selectOneRadio>

								<rich:column>
									<t:radio for="idRadiosOrigens" index="#{beanRegistrarAndamento.indexOrigemCadastrada}"></t:radio> 
								</rich:column>
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Origem" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{historicoOrigem.origem.descricao}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Sigla Classe Or." />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{historicoOrigem.siglaClasseOrigem}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Numero Or." />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{historicoOrigem.numeroProcessoOrigem}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Número Único" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{beanRegistrarAndamento.processo.numeroUnicoProcesso}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Procedência" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{historicoOrigem.procedencia.siglaProcedencia}" />
								</rich:column>
								<rich:column width="150px">
									<f:facet name="header">
										<h:outputText value="Destinatário" />
									</f:facet>
									<h:outputText style="#{beanRegistrarAndamento.styleSimNao}"
										styleClass="Padrao"
										value="#{beanRegistrarAndamento.isOrigemDestinatario}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Integrada" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{beanRegistrarAndamento.isOrigemIntegrada}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Gera comunicação"/>
									</f:facet>
									<rich:toolTip followMouse="false" direction="top-right"
										rendered="#{historicoOrigem.origem.pessoa.id != null}"
										horizontalOffset="5" verticalOffset="5" hideDelay="20"
										showDelay="250" styleClass="Padrao">
										<h:outputText value="Gera Comunicação Eletronica"
											styleClass="Padrao" />
									</rich:toolTip>
									<h:outputText styleClass="Padrao"
										value="#{historicoOrigem.origem.pessoa.id != null ? 'Sim' : 'Não'}"
										style="color: #{historicoOrigem.origem.pessoa.id != null ? 'red' : ''}"/>
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Ações" />
									</f:facet>
									<a4j:commandLink value="Excluir"
										actionListener="#{beanRegistrarAndamento.excluirOrigem}"
										reRender="idTabelaOrigensCadastradas" />
								</rich:column>
								
							</rich:dataTable>
					</fieldset>
				</div>
				</t:div>

				<div style="padding-top: 20px; text-align: center">
					
					<a4j:commandButton
						oncomplete="if(solicitaValidacao(#{beanRegistrarAndamento.temOrigemSelecionada},#{beanRegistrarAndamento.origemEstaIntegrada},#{beanRegistrarAndamento.verificaNumUnico},#{beanRegistrarAndamento.verificaNumOrigem},#{beanRegistrarAndamento.unicaBaixa})){document.getElementById('idBotaoConfirmarBaixa').click();};"
						action="#" id="idBotaoValidaBaixa"
						styleClass="BotaoPadraoEstendido" value="Confirmar Baixa"/>
						
					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelVerificarCodigoOrigem');"
						oncomplete="limparDadosModais();"
						actionListener="#{beanRegistrarAndamento.cancelarRegistroAndamento}"
						reRender="pnlPesquisaAndamento,idPanelVariaveis"
						styleClass="BotaoPadrao" value="Cancelar" />

					<a4j:commandButton styleClass="BotaoOculto"
						onclick="Richfaces.hideModalPanel('modalPanelVerificarCodigoOrigem');exibirMsgProcessando(true);"
						oncomplete="if(!gerarCertidaoBaixa()){exibirMsgProcessando(false);}"
						actionListener="#{beanRegistrarAndamento.registrarAndamento}"
						reRender="pnlPesquisaAndamento,idPanelVariaveis"
						id="idBotaoConfirmarBaixa" />
						
					<a4j:commandButton styleClass="BotaoOculto"
						id="abrePdfCertidaoBaixa"
						oncomplete="exibirMsgProcessando(false);document.getElementById('imprimirPdfCertidaoBaixa').click();"
						action="#{beanRegistrarAndamento.gerarAndamentoCertidaoBaixa}"/>						
					
					<h:commandLink id="imprimirPdfCertidaoBaixa"
						action="#{beanRegistrarAndamento.imprimirCertidao}"
						target="_blank" styleClass="BotaoOculto" />
				</div>
			</a4j:form>

		</rich:modalPanel>

		<rich:modalPanel id="modalPanelEditarAndamento" keepVisualState="true"
			autosized="true">

			<f:facet name="header">
				<h:outputText value="Edite o Andamento" />
			</f:facet>
			<a4j:form prependId="false">
				<t:div id="divEditarAndamento">
					<h:panelGrid style="margin-bottom:5px">
						<h:outputLabel
							value="A informação do campo 'Observação' é editável até o dia seguinte ao cadastramento do andamento."
								styleClass="Padrao"/>
						<h:outputLabel
							value="O campo 'Observação Interna' foi atualizado por outro usuário."
							style="color:red"
							rendered="#{beanRegistrarAndamento.obsInternaAtualizadaPorOutroUsuario}" />
					</h:panelGrid>
					<h:panelGrid columns="2" columnClasses="topAligned">
						<h:outputLabel value="Observação:" styleClass="Padrao"
							style="#{beanRegistrarAndamento.styleEditarObservacao }" />
						<h:inputTextarea
							style="margin-left:10px; width:300px; #{beanRegistrarAndamento.styleEditarObservacao }"
							id="idObservacao" value="#{beanRegistrarAndamento.observacao}"
							disabled="#{beanRegistrarAndamento.disabledEditarAndamento}" />

						<h:outputLabel value="Observação Interna:" styleClass="Padrao"/>
						<h:inputTextarea style="margin-left:10px; width:300px"
							id="idObservacaoInterna"
							value="#{beanRegistrarAndamento.observacaoInterna}" />
					</h:panelGrid>
				</t:div>

				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao" 
						actionListener="#{beanRegistrarAndamento.atualizarAndamento}"
						onclick="if (validarTamanhoObservacoes('idObservacao','idObservacaoInterna')) { Richfaces.hideModalPanel('modalPanelEditarAndamento');} else {return;}"
						value="Ok" reRender="pnlPesquisaAndamento" />

					<a4j:commandButton
						onclick="document.getElementById('idObservacaoInterna').value = ''; document.getElementById('idObservacao').value = ''; Richfaces.hideModalPanel('modalPanelEditarAndamento');"
						styleClass="BotaoPadrao" value="Cancelar"/>
				</div>
			</a4j:form>

		</rich:modalPanel>

		<rich:modalPanel id="modalPanelApensos" keepVisualState="true"
			style="overflow:auto" autosized="true" width="580">
			<f:facet name="header">
				<h:outputText value="Apenso(s) relacionados ao Processo" />
			</f:facet>

			<t:div id="idPanelApensos"
				style="padding-bottom: 10px; padding-top: 10px; padding-left: 10px; padding-right: 5px">
			<h:panelGrid columns="2" columnClasses="topAligned">
			
				<h:panelGrid>
					<t:div style="padding-bottom: 5px;">
							<span class="Padrao"> <h:outputText
									value="O processo #{beanRegistrarAndamento.processo.identificacao} contém os apensos:" />
						</span>
					</t:div>
					<t:div>
							<span class="Padrao"> <rich:dataTable
									headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
									value="#{beanRegistrarAndamento.urlExternas}" var="apensos">
			
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Processo" />
									</f:facet>
										<h:outputLink styleClass="Padrao" style="text-align:center;"
											value="#{apensos.consultaUrlExterna}" target="_blank">
											<h:outputText
												value="#{apensos.processoDependencia.classeProcesso} #{apensos.processoDependencia.numeroProcesso}"></h:outputText>
									</h:outputLink>
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Data" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{apensos.processoDependencia.dataInicioDependencia}" />
								</rich:column>
								
							</rich:dataTable>
						</span>
					</t:div>
				</h:panelGrid>

				<h:panelGrid style="padding-left: 5px">			
					<t:div style="padding-bottom: 5px;">
							<span class="Padrao"> <h:outputText
									value="O processo #{beanRegistrarAndamento.processo.identificacao} está apensado aos processos:" />
						</span>
					</t:div>
					<t:div>
							<span class="Padrao"> <rich:dataTable
									headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
									value="#{beanRegistrarAndamento.urlExternasAo}" var="apensos">
			
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Processo" />
									</f:facet>
										<h:outputLink styleClass="Padrao" style="text-align:center;"
											value="#{apensos.consultaUrlExterna}" target="_blank">
											<h:outputText
												value="#{apensos.processoDependencia.classeProcessoVinculador} #{apensos.processoDependencia.numeroProcessoVinculador}"></h:outputText>
									</h:outputLink>
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Data" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{apensos.processoDependencia.dataInicioDependencia}" />
								</rich:column>
								
							</rich:dataTable>
						</span>
					</t:div>
				</h:panelGrid>
			</h:panelGrid>
			</t:div>
			
			<div style="text-align: center">
				<h:commandButton
					onclick="Richfaces.hideModalPanel('modalPanelApensos');"
					styleClass="BotaoPadrao" value="Ok" />
			</div>
		</rich:modalPanel>


		<rich:modalPanel id="modalPanelPartes" keepVisualState="true"
			style="overflow:auto" autosized="true" width="500">
			<f:facet name="header">
				<h:outputText value="Partes" />
			</f:facet>

			<t:div id="idPanelPartes"
				style="padding-bottom: 10px; padding-top: 10px; padding-left: 10px; padding-right: 5px">
			
					<t:div>
					<span class="Padrao"> <rich:dataTable
							headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
							value="#{beanRegistrarAndamento.listaSpecPartes}" var="partes">
			
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Tipo" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{partes.tipo}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Nome" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{partes.nome}" />
								</rich:column>
								
							</rich:dataTable>
						</span>
					</t:div>
			</t:div>
			
			<div style="text-align: center">
				<h:commandButton
					onclick="Richfaces.hideModalPanel('modalPanelPartes');"
					styleClass="BotaoPadrao" value="Ok" />
			</div>
		</rich:modalPanel>
		
		<rich:modalPanel id="modalPanelInformacoesLancamentoIndevido"
			keepVisualState="true" autosized="true">

			<f:facet name="header">
				<h:outputText
					value="Informações adicionais para o lançamento indevido" />
			</f:facet>
			<a4j:form prependId="false">
				<t:div id="idPanelInfoAndamentoIndevido">
					<h:panelGrid columns="2" columnClasses="topAligned">
						<h:outputLabel value="Observação:" styleClass="Padrao">
							<font color="red">*</font>
						</h:outputLabel>
						<h:inputTextarea style="margin-left:10px; width:300px;"
							id="idObservacaoIndevido"
							value="#{beanRegistrarAndamento.observacao}" />

						<h:outputLabel value="Observação Interna:" styleClass="Padrao"/>
						<h:inputTextarea style="margin-left:10px; width:300px"
							id="idObservacaoInternaIndevido"
							value="#{beanRegistrarAndamento.observacaoInterna}" />
					</h:panelGrid>
				</t:div>

				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao" 
						actionListener="#{beanRegistrarAndamento.lancarOuCancelarAndamentoIndevido}"
						oncomplete="exibirMsgProcessando(false); if(#{beanRegistrarAndamento.lancamentoIndevidoException}) {alert('#{beanRegistrarAndamento.msgLancamentoIndevidoException}')};"
						onclick="exibirMsgProcessando(true); if (!lancarAndamentoIndevidoModalOnClick()) {return;}"
						value="Ok" reRender="pnlPesquisaAndamento,pnlCentral" />

					<a4j:commandButton
						onclick="document.getElementById('idObservacaoInternaIndevido').value = ''; document.getElementById('idObservacaoIndevido').value = ''; Richfaces.hideModalPanel('modalPanelInformacoesLancamentoIndevido');"
						styleClass="BotaoPadrao" value="Cancelar"/>
				</div>
			</a4j:form>

		</rich:modalPanel>
		
		<rich:modalPanel id="modalPanelConfirmacaoRegistro" keepVisualState="true" autosized="true" style="overflow:auto" width="600">
			<f:facet name="header">
				<h:outputText value="Confirmação" />
			</f:facet>
			<a4j:form prependId="false">
				<t:div id="idPanelConfirmacaoRegistro">
						<h:outputText escape="false" value="#{beanRegistrarAndamento.mensagemDeRestricaoRegistroDeAndamento}" style="white-space: pre-wrap;"></h:outputText>
				</t:div>
				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao" 
						onclick="Richfaces.hideModalPanel('modalPanelConfirmacaoRegistro'); if (!confirmaLancamento()) {return false;}"
						value="Sim" reRender="pnlCentral"
						rendered="#{!beanRegistrarAndamento.andamentoGeraDocumento}"
						actionListener="#{beanRegistrarAndamento.registrarAndamento}"/>
					<a4j:commandButton styleClass="BotaoPadrao" 
						onclick="Richfaces.hideModalPanel('modalPanelConfirmacaoRegistro'); if (!confirmaLancamento()) {return false;}"
						value="Sim" reRender="pnlCentral"
						rendered="#{beanRegistrarAndamento.andamentoGeraDocumento}"
						actionListener="#{beanRegistrarAndamento.registrarAndamentoEAssinar}"
						oncomplete="acionaBotaoChamaAssinador();"/>
					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelConfirmacaoRegistro');"
						styleClass="BotaoPadrao" value="Não"/>
				</div>
			</a4j:form>

		</rich:modalPanel>		
		
		<rich:modalPanel id="modalPanelConfirmacaoAndamento" keepVisualState="true" autosized="true" style="overflow:auto" width="600">
			<f:facet name="header">
				<h:outputText value="Confirmação" />
			</f:facet>
			<a4j:form prependId="false">
				<div>
						<h:outputText value="O andamento 8558 (Autos Requisitados ao AGU) desloca, de imediato, o processo ao STF e não é passível de invalidação posterior. Deseja continuar?"></h:outputText>
				<div>
				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao" 
						onclick="Richfaces.hideModalPanel('modalPanelConfirmacaoAndamento');"
						value="Sim" reRender="pnlCentral"
						actionListener="#{beanRegistrarAndamento.registrarAndamento}"/>
					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelConfirmacaoAndamento');"
						styleClass="BotaoPadrao" value="Não"/>
				</div>
			</a4j:form>

		</rich:modalPanel>

		<jsp:include page="/pages/template/footer.jsp" flush="true" />
		
		<script type="text/javascript">
			 function acionaBotaoChamaAssinador(){			 
				 document.getElementById("btnChamaAssinador").click();
			 }

			 function verificaConfirmacaoProcessoComApensos(possuiApensos){
				alert('passei 1 + possuiApensos: ' + possuiApensos);
				if (!possuiApensos)
					return true;
				if(confirm('Este processo possui apenso(s). Deseja continuar?')) 
					return true;
				else 
					return false;
			 }
		</script>

	</a4j:page>
</f:view>