<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<jsp:include page="/pages/template/header.jsp" flush="true" />

<f:view>
	<a4j:page
		pageTitle="::.. Registrar Andamento para vários processos..::"
		onload="document.getElementById('idAndamento').focus();">

		<script type="text/javascript"
			src="<%=request.getContextPath()%>/scripts/andamento.js"></script>


		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina"
					value="Registrar Andamento para vários processos" />
			</jsp:include>
			<a name="ancora"></a>
		</h:form>
		<h:form id="form" prependId="false" onreset="">
			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0" id="pnlCentral">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">
					<a4j:outputPanel id="pnlPesquisaAndamento"
						style="padding-top: 5px;">

						<div class="PainelTituloCriaTexto">
							<span>Andamento</span>
						</div>

						<div class="Padrao edit">
							<h:outputLabel styleClass="form-label" value="Data:" />
							<h:outputText
								value="#{beanRegistrarAndamentoParaVariosProcessos.dataAtual}" />
						</div>
						<div class="Padrao edit">
							<h:outputLabel value="Código:" styleClass="form-label" />
							<h:inputText style="width:400px" id="idAndamento"
								binding="#{beanRegistrarAndamentoParaVariosProcessos.inputAndamento }"
								onkeypress="return noEnter(event);"
								value="#{beanRegistrarAndamentoParaVariosProcessos.descricaoAndamento }"
								onclick="exibirSuggestionBox(this,#{rich:component('sgPesquisaAndamento')});">
							</h:inputText>
							
							

							<rich:suggestionbox id="sgPesquisaAndamento"
								suggestionAction="#{beanRegistrarAndamentoParaVariosProcessos.pesquisaAndamentosSuggestionBox}"
								for="idAndamento" var="result"
								nothingLabel="Nenhum registro encontrado." width="400"
								ignoreDupResponses="true" eventsQueue="ajaxQueue" minChars="1">

								<a4j:support ajaxSingle="true" event="onselect"
									eventsQueue="ajaxQueue" ignoreDupResponses="true"
									reRender="idPanelVariaveis,modalPanelOrigemDecisao,pnlAssinatura, pnlFiltrosTabela, modalPanelConfirmacaoRegistro">
									<f:setPropertyActionListener value="#{result}"
										target="#{beanRegistrarAndamentoParaVariosProcessos.andamentoSelecionado}" />
								</a4j:support>

								<h:column>
									<h:outputText value="#{result.id}" styleClass="oiSuggestion" />
									<h:outputText value=" - " />
									<h:outputText value="#{result.descricao}" styleClass="green" />
								</h:column>
							</rich:suggestionbox>
						</div>
						
						<div class="Padrao edit">
							<div class="form-label">
								<h:outputLabel value="Observação:" />
								<br />
								<h:graphicImage
									title="O conteúdo desse campo será divulgado no Acompanhamento Processual do Portal do STF (Internet)"
									alt="Ajuda observação" value="/images/ajuda.gif" />
							</div>
							<h:inputTextarea style="width:400px" id="observacao"
								value="#{beanRegistrarAndamentoParaVariosProcessos.observacao }">
								<a4j:support ajaxSingle="true" event="onchange" />
							</h:inputTextarea>
						</div>
						<div class="Padrao edit">
							<h:outputLabel value="Obs. interna:" styleClass="form-label" />
							<h:inputTextarea style="width:400px" id="observacaoInterna"
								value="#{beanRegistrarAndamentoParaVariosProcessos.observacaoInterna }">
								<a4j:support ajaxSingle="true" event="onchange" />
							</h:inputTextarea>
						</div>

					</a4j:outputPanel>
				</h:panelGrid>
				<h:panelGroup id="pnlFiltrosTabela">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0" rendered="#{beanRegistrarAndamentoParaVariosProcessos.andamentoSelecionado != null }">


					<div class="PainelTituloCriaTexto">
						<span>Processos</span>
					</div>

					<div class="Padrao">
						<a4j:outputPanel id="pnlProcesso" style="padding-top: 5px;">
							<f:verbatim>
								<fieldset style="width: 10%; float: left;">
									<legend>Inclusão por</legend>
									<h:selectOneRadio
										value="#{beanRegistrarAndamentoParaVariosProcessos.tipoInclusao}"
										layout="pageDirection" id="tipoInclusao">
										<f:selectItem itemValue="PROCESSO" itemLabel="Processo" />
										<f:selectItem itemValue="GUIA" itemLabel="Guia" />
										<f:selectItem itemValue="IMPORTACAO" itemLabel="Importação" />
										<a4j:support event="onclick"
											reRender="pnlProcesso,pnlTabelaProcesso"
											onsubmit="if (!confirmarMudancaTipoInclusao('hdnExisteProcessoNaLista')) {return false;}"
											actionListener="#{beanRegistrarAndamentoParaVariosProcessos.atualizarTipoInclusao}" />
									</h:selectOneRadio>

								</fieldset>
							</f:verbatim>
							<h:panelGroup style="#{beanRegistrarAndamentoParaVariosProcessos.estiloVisibilidadeProcesso}">
								<div style="padding-top: 10px;">
									<h:panelGrid columns="4">
										<h:outputText styleClass="Padrao" value="Lista de Processos:"/>
	
										<h:inputText style="margin-left:10px;" id="idProcessos"  size="60"
											value="#{beanRegistrarAndamentoParaVariosProcessos.identificacaoProcessos}" />											
																		
										<rich:hotKey selector="#idProcessos" key="return" id="hotKeyidProcesso"
											handler="document.getElementById('btnIncluirDocumento').onclick()" />

										<a4j:commandButton styleClass="BotaoMais"
											id="btnIncluirDocumento"
											title="Inclui o processo digitado."
											onclick="exibirMsgProcessando(true); "
											reRender="hotKeyidProcesso,idProcessos,pnlTabelaProcesso,pnlCentral,pnlAssinatura,modalPanelConfirmacaoRegistro"
											oncomplete="document.getElementById('idProcesso').value = ''; exibirMsgProcessando(false); document.getElementById('idProcessos').focus(); verficaProcessoFindoLista();" />
									
										<h:outputText value=""/>
										<h:outputText  style="margin-left:10px;" value="Informar relação de processos separados por \";\""/>	
										<h:outputText value=""/>								
									</h:panelGrid>
																											
									<h:inputHidden id="idProcessoFindoLista" 
											value="#{beanRegistrarAndamentoParaVariosProcessos.processoFindoLista}"></h:inputHidden>
																							
								</div>
							</h:panelGroup>
							
							<h:panelGroup style="#{beanRegistrarAndamentoParaVariosProcessos.estiloVisibilidadeProcesso}">
								<div style="padding-top: 10px;">
									<h:outputText styleClass="Padrao" value="Processo:" />
		
									<h:inputText style="margin-left:10px;" id="idProcesso"
										onkeypress="return noEnter(event);" size="40" 
										value="#{beanRegistrarAndamentoParaVariosProcessos.identificacaoProcesso}"
										onclick="exibirSuggestionBoxPesquisaProcesso(this, #{rich:component('sgPesquisaProcesso')});">
									</h:inputText>
		
									<span style="padding-left: 5px;"> 
										<rich:suggestionbox
												id="sgPesquisaProcesso"
												suggestionAction="#{beanRegistrarAndamentoParaVariosProcessos.pesquisaSuggestionBox}"
												for="idProcesso" var="result" 
												fetchValue="#{result.identificacao}"
												nothingLabel="Nenhum registro encontrado." width="400"
												ignoreDupResponses="true" eventsQueue="ajaxQueue" minChars="1">
												<a4j:support ajaxSingle="true" event="onselect" onsubmit="exibirMsgProcessando(true);" oncomplete="exibirMsgProcessando(false);"
													eventsQueue="ajaxQueue" ignoreDupResponses="true" reRender="pnlTabelaProcesso,pnlCentral">
													<f:setPropertyActionListener value="#{result}" target="#{beanRegistrarAndamentoParaVariosProcessos.incidenteSelecionado}" />
												</a4j:support>
												<h:column>
													<h:outputText rendered="#{result.tipoObjetoIncidente.codigo == 'PR' && result.eletronico}" value="e"
															style="color: red; font-weight: bold;" />
													
													<h:outputText value="#{result.identificacao}"
														styleClass="oiSuggestion" />
													<h:outputText rendered="#{result.tipoObjetoIncidente.codigo == 'PR'}" value=" » " />
													<h:outputText rendered="#{result.tipoObjetoIncidente.codigo == 'PR'}" value="#{result.ministroRelatorAtual.nome}"
														styleClass="green" />
													
												</h:column>
										</rich:suggestionbox>
									</span>
								</div>							
							</h:panelGroup>
							
							<h:panelGroup
								style="#{beanRegistrarAndamentoParaVariosProcessos.estiloVisibilidadeGuia}">
								<div style="padding-top: 10px;">
									<h:outputText styleClass="Padrao" value="Guia Nº:" />

									<h:inputText style="margin-left:10px;" id="numeroGuia"
										value="#{beanRegistrarAndamentoParaVariosProcessos.numeroGuia}">
									</h:inputText>
									<span>/</span>
									<h:inputText style="margin-left:10px;" id="anoGuia"
										value="#{beanRegistrarAndamentoParaVariosProcessos.anoGuia}">
									</h:inputText>

									<a4j:commandButton id="btnImportarGuia" alt="Importar Guia"
										styleClass="BotaoPadrao" title="Importar" value="Importar"
										actionListener="#{beanRegistrarAndamentoParaVariosProcessos.importarGuia}"
										reRender="pnlTabelaProcesso, observacao" />

								</div>
							</h:panelGroup>

							<h:panelGroup
								style="#{beanRegistrarAndamentoParaVariosProcessos.estiloVisibilidadeImportacao}">
								<div style="padding-top: 10px;">
									<a4j:commandButton id="btnImportarListaEGab" alt="Importar lista exportada pelo eGab"
										styleClass="BotaoPadraoEstendido" title="Importar" value="Importar lista"
										oncomplete="exibirMsgProcessando(false);"
										onclick="exibirMsgProcessando(true);"
										reRender="pnlTabelaProcesso,pnlCentral"
										actionListener="#{beanRegistrarAndamentoParaVariosProcessos.processarImportacaoListaExportadaPeloEGab}" />

								</div>
							</h:panelGroup>
						</a4j:outputPanel>
						<a4j:outputPanel id="pnlTabelaProcesso">
							<h:inputHidden id="hdnExisteProcessoNaLista"
								value="#{beanRegistrarAndamentoParaVariosProcessos.existeProcessoNaLista}"></h:inputHidden>
							<rich:dataTable headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="dezCenter, vinteCenter, dez, dez, dez, dez"
								value="#{beanRegistrarAndamentoParaVariosProcessos.processosSelecionados}"
								var="processoSelecionado"
								binding="#{beanRegistrarAndamentoParaVariosProcessos.tabelaProcessos}">
								<!-- rows="10" -->
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Número Processo" />
									</f:facet>
									<h:outputLink target="_blank"
										value="http://www.stf.jus.br/portal/processo/verProcessoAndamento.asp?incidente=#{processoSelecionado.id}">
										<h:outputText rendered="#{processoSelecionado.eletronico}"
											value="e" style="color: red; font-weight: bold;" />
										<h:outputText value="#{beanRegistrarAndamentoParaVariosProcessos.identificacaoObjetoIncidente}" />
									</h:outputLink>

								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Relator" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{beanRegistrarAndamentoParaVariosProcessos.nomeMinistroRelator}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Destino da Baixa" />
									</f:facet>
									<h:outputText styleClass="Padrao" value="#{beanRegistrarAndamentoParaVariosProcessos.origemIdentificada}" />
								</rich:column>								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Qtd. Volumes" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{processoSelecionado.quantidadeVolumes}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Qtd. Apensos" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{processoSelecionado.quantidadeApensos}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Qtd. Juntadas" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{processoSelecionado.quantidadeJuntadasLinha}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Qtd. Vinculados" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{beanRegistrarAndamentoParaVariosProcessos.quantidadeVinculados}" />
								</rich:column>
								<rich:column rendered="#{beanRegistrarAndamentoParaVariosProcessos.andamentoSelecionadoImplicaEmDeslocamentoAutomatico}">
									<f:facet name="header">
										<h:outputText value="Deslocar automaticamente" />
									</f:facet>
									<h:selectBooleanCheckbox value="#{processoSelecionado.deslocarObjetoIncidente}" disabled="#{!processoSelecionado.objetoIncidentePodeSerDeslocado}"/>
								</rich:column>								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Excluir" />
									</f:facet>

									<a4j:commandLink
										onclick="if (!confirm('Gostaria de excluir o processo da lista?')) { return;}"
										actionListener="#{beanRegistrarAndamentoParaVariosProcessos.excluirProcesso}"
										reRender="pnlTabelaProcesso">
										<h:graphicImage url="/images/remove.gif"
											title="Excluir Processo" />
									</a4j:commandLink>
								</rich:column>

							</rich:dataTable>
							<h:outputText
								value="Total processos: #{beanRegistrarAndamentoParaVariosProcessos.numeroProcessosSelecionados }"
								styleClass="Padrao" style="padding-top:10px;" />

						</a4j:outputPanel>
					</div>

				</h:panelGrid>
				<a4j:outputPanel id="idPanelVariaveis">
					<h:inputHidden id="idSolicitarProcessoEObservacao"
						value="#{beanRegistrarAndamentoParaVariosProcessos.solicitarProcessoEObservacao}"
						binding="#{beanRegistrarAndamentoParaVariosProcessos.inputSolicitarProcessoEObservacao}"></h:inputHidden>
					<h:inputHidden id="idSolicitarProcessos"
						value="#{beanRegistrarAndamentoParaVariosProcessos.solicitarProcessos}"
						binding="#{beanRegistrarAndamentoParaVariosProcessos.inputSolicitarProcessos}"></h:inputHidden>
					<h:inputHidden id="idSolicitarPeticao"
						value="#{beanRegistrarAndamentoParaVariosProcessos.solicitarPeticao}"
						binding="#{beanRegistrarAndamentoParaVariosProcessos.inputSolicitarPeticao}"></h:inputHidden>
					<h:inputHidden id="idSolicitarOrigemDecisao"
						value="#{beanRegistrarAndamentoParaVariosProcessos.solicitarOrigemDecisao}"
						binding="#{beanRegistrarAndamentoParaVariosProcessos.inputSolicitarOrigemDecisao}"></h:inputHidden>
					<h:inputHidden id="idSolicitarPresidenteInterino"
						value="#{beanRegistrarAndamentoParaVariosProcessos.solicitarPresidenteInterino}"
						binding="#{beanRegistrarAndamentoParaVariosProcessos.inputSolicitarPresidenteInterino}"></h:inputHidden>
					<h:inputHidden id="idSolicitarTipoDevolucao"
						value="#{beanRegistrarAndamentoParaVariosProcessos.solicitarTipoDevolucao}"
						binding="#{beanRegistrarAndamentoParaVariosProcessos.inputSolicitarTipoDevolucao}"></h:inputHidden>
					
					<h:inputHidden id="idPrecisaConfirmacaoLancarAndamento"
						value="#{beanRegistrarAndamentoParaVariosProcessos.precisaConfirmacaoLancarAndamento}"></h:inputHidden>
					
					<h:inputHidden id="idPrecisaVerificarCodigoOrigem"
						value="#{beanRegistrarAndamentoParaVariosProcessos.precisaVerificarCodigoOrigem}"
						binding="#{beanRegistrarAndamentoParaVariosProcessos.inputPrecisaVerificarCodigoOrigem}"></h:inputHidden>
					<h:inputHidden id="idPrecisaListarDecisoes"
						value="#{beanRegistrarAndamentoParaVariosProcessos.precisaListarDecisoes}"
						binding="#{beanRegistrarAndamentoParaVariosProcessos.inputPrecisaListarDecisoes}"></h:inputHidden>						
					<h:inputHidden id="idMensagemConfirmacaoLancarAndamento"
						value="#{beanRegistrarAndamentoParaVariosProcessos.mensagemConfirmacaoLancarAndamento}"></h:inputHidden>
						
					<h:inputHidden id="idProcessoFindo"
						value="#{beanRegistrarAndamentoParaVariosProcessos.processoFindo}"
						binding="#{beanRegistrarAndamentoParaVariosProcessos.inputProcessoFindo}"></h:inputHidden>
					<h:inputHidden id="hidProcessoDifereSetorUsuario" 
						value="#{beanRegistrarAndamentoParaVariosProcessos.processoDifereSetorUsuario}"
						binding="#{beanRegistrarAndamentoParaVariosProcessos.inputProcessoDifereSetorUsuario}"></h:inputHidden>
					<h:inputHidden id="idSolicitarTema"
						value="#{beanRegistrarAndamentoParaVariosProcessos.solicitarTema}"
						binding="#{beanRegistrarAndamentoParaVariosProcessos.inputSolicitarTema}"></h:inputHidden>	
					<h:inputHidden id="qtdProcessosTemasSelecionados" 
					    value="#{beanRegistrarAndamentoParaVariosProcessos.quantidadeProcessosTemasSelecionados}" />												
				</a4j:outputPanel>
				</h:panelGroup>
			</h:panelGrid>
			
			<div style="padding-top: 15px">
				<span class="Padrao"> 
					<a4j:commandButton id="btnLimpar"
						alt="Limpar" title="Limpar" styleClass="BotaoPadrao"
						value="Limpar" reRender="pnlCentral" 
						actionListener="#{beanRegistrarAndamentoParaVariosProcessos.limpar}" />		
						
					<a4j:outputPanel id="pnlAssinatura">
						<a4j:commandButton id="btnConfirmar" alt="Registrar"
							reRender="pnlCentral,pnlAssinatura" rendered="#{!beanRegistrarAndamentoParaVariosProcessos.andamentoGeraDocumento}"
							styleClass="BotaoPadrao" title="Confirmar" value="Registrar"
							action="#{beanRegistrarAndamentoParaVariosProcessos.confirmarAndamento}" 
							onclick="exibirMsgProcessando(true); if (!verificaPendencias(#{beanRegistrarAndamentoParaVariosProcessos.mensagemDeRestricaoRegistroDeAndamento != null})) {return false;}"
							oncomplete="exibirMsgProcessando(false); "/>
							
						<a4j:commandButton id="btnConfirmarAssinar" alt="Registrar"
							reRender="pnlCentral" rendered="#{beanRegistrarAndamentoParaVariosProcessos.andamentoGeraDocumento}"
							styleClass="BotaoEstendido" title="Registrar e Assinar" value="Registrar e assinar"
							action="#{beanRegistrarAndamentoParaVariosProcessos.confirmarAssinarAndamento}" 
							onclick="exibirMsgProcessando(true); if (!verificaPendencias(#{beanRegistrarAndamentoParaVariosProcessos.mensagemDeRestricaoRegistroDeAndamento != null})) {return false;}"
							oncomplete="exibirMsgProcessando(false); acionaBotaoChamaAssinador();"/>												  
					</a4j:outputPanel>		
					
					<h:commandButton id="btnChamaAssinador" 						
						styleClass="BotaoOculto" 
						action="#{beanRegistrarAndamentoParaVariosProcessos.chamarAssinador}"/>
						
					<a4j:commandButton styleClass="BotaoOculto"
									   actionListener="#{beanRegistrarAndamentoParaVariosProcessos.verificarAndamento}"
								       reRender="idProcessoTemaTable" id="idBotaoRerenderModal"/>																				
									
				</span>

				<a4j:commandButton id="btSalvarAndamento" styleClass="BotaoOculto"
					reRender="pnlCentral"
					actionListener="#{beanRegistrarAndamentoParaVariosProcessos.confirmarAndamento}"
					oncomplete="exibirMsgProcessando(false);limparDadosModais(); " />
			</div>
		</h:form>

		<!-- Como não é possível modificar o bean dinamicamente, essa parte deverá ser copiada para a página registrarAndamentoParaVariosProcessos.jsp -->
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

						<h:outputText styleClass="Padrao" value="Quantidade de apensos:" />

						<h:inputText style="margin-left:10px; width:30px" id="idApensos"
							value="#{beanRegistrarAndamentoParaVariosProcessos.quantidadeApensos}"
							onkeypress="return noEnter(event);">
						</h:inputText>
					</h:panelGrid>

					<rich:suggestionbox id="sgBoxProcesso"
						suggestionAction="#{beanRegistrarAndamentoParaVariosProcessos.pesquisaSuggestionBoxProcesso}"
						for="identificacaoProcesso" var="result"
						fetchValue="#{result.identificacao}"
						nothingLabel="Nenhum registro encontrado." width="400"
						ignoreDupResponses="true" eventsQueue="ajaxQueue" minChars="1">

						<a4j:support ajaxSingle="true" event="onselect"
							eventsQueue="ajaxQueue" ignoreDupResponses="true"
							reRender="idApensos">
							<f:setPropertyActionListener value="#{result}"
								target="#{beanRegistrarAndamentoParaVariosProcessos.processoSelecionado}" />
						</a4j:support>

						<h:column>
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
						onclick="processoPrincipalOnClick()" reRender="pnlCentral"
						value="Cadastrar" />

					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelAnexarProcesso');"
						oncomplete="limparDadosModais();"
						reRender="pnlPesquisaAndamento,idPanelVariaveis"
						styleClass="BotaoPadrao" value="Cancelar" />
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
							onkeypress="return noEnter(event);"
							onclick="exibirSuggestionBoxPesquisaPeticao(this, #{rich:component('sgBoxPeticao')});">
						</h:inputText>
					</h:panelGrid>

					<rich:suggestionbox id="sgBoxPeticao"
						suggestionAction="#{beanRegistrarAndamentoParaVariosProcessos.pesquisaPeticaoSuggestionBox}"
						for="identificacaoPeticao" var="result"
						fetchValue="#{result.identificacao}"
						nothingLabel="Nenhum registro encontrado." width="400"
						ignoreDupResponses="true" eventsQueue="ajaxQueue" minChars="1">

						<a4j:support ajaxSingle="true" event="onselect"
							eventsQueue="ajaxQueue" ignoreDupResponses="true">
							<f:setPropertyActionListener value="#{result}"
								target="#{beanRegistrarAndamentoParaVariosProcessos.peticaoSelecionada}" />
						</a4j:support>

						<h:column>
							<h:outputText value="#{result.identificacao}"
								styleClass="oiSuggestion" />
						</h:column>
					</rich:suggestionbox>
				</div>

				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao"
						actionListener="#{beanRegistrarAndamentoParaVariosProcessos.registrarAndamentoComPeticaoAnexo}"
						oncomplete="limparDadosModais();"
						onclick="Richfaces.hideModalPanel('modalPanelAnexarPeticao'); if (!pedirInformacaoAdicional()) {document.getElementById('btSalvarAndamento').click();}"
						reRender="pnlCentral" value="Cadastrar" />

					<h:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelAnexarPeticao');"
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

						<h:selectOneMenu
							value="#{beanRegistrarAndamentoParaVariosProcessos.idOrigemDecisao}"
							styleClass="Input" id="idComboOrigemDecisao">
							<f:selectItems
								value="#{beanRegistrarAndamentoParaVariosProcessos.origensDecisao}" />
						</h:selectOneMenu>
					</h:panelGrid>
				</div>

				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao"
						actionListener="#{beanRegistrarAndamentoParaVariosProcessos.verificarPresidenteInterino}"
						oncomplete="Richfaces.hideModalPanel('modalPanelOrigemDecisao'); if (!pedirInformacaoAdicional()) {document.getElementById('btSalvarAndamento').click();}"
						onclick="if (document.getElementById('idComboOrigemDecisao').value == -1) { alert('Selecione uma origem de decisão.'); return; }"
						reRender="pnlPesquisaAndamento,idPanelVariaveis" value="Cadastrar" />

					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelOrigemDecisao');"
						oncomplete="limparDadosModais();" styleClass="BotaoPadrao"
						value="Cancelar" />

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

						<h:selectOneMenu
							value="#{beanRegistrarAndamentoParaVariosProcessos.idTipoDevolucao}"
							styleClass="Input" id="idComboDevolucao">
							<f:selectItems
								value="#{beanRegistrarAndamentoParaVariosProcessos.tiposDevolucao}" />
						</h:selectOneMenu>
					</h:panelGrid>
				</div>

				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao"
						onclick="if (document.getElementById('idComboDevolucao').value == -1) { alert('Selecione um tipo de devolução.'); return; } else {Richfaces.hideModalPanel('modalPanelDevolucao'); if (!pedirInformacaoAdicional()) {document.getElementById('btSalvarAndamento').click();}}"
						value="Cadastrar" reRender="pnlPesquisaAndamento" />

					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelDevolucao');"
						oncomplete="limparDadosModais();" styleClass="BotaoPadrao"
						value="Cancelar" />
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
							value="#{beanRegistrarAndamentoParaVariosProcessos.idPresidenteInterino}"
							styleClass="Input" id="idComboPresidenteInterino">
							<f:selectItems
								value="#{beanRegistrarAndamentoParaVariosProcessos.ministrosPresidenteInterino}" />
						</h:selectOneMenu>
					</h:panelGrid>
				</div>

				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao"
						onclick="if (document.getElementById('idComboPresidenteInterino').value == -1) { alert('Selecione o presidente.'); return; } else {Richfaces.hideModalPanel('modalPanelPresidenteInterino');}"
						actionListener="#{beanRegistrarAndamentoParaVariosProcessos.confirmarAndamento}"
						oncomplete="exibirMsgProcessando(false);limparDadosModais();" value="Ok" reRender="pnlCentral" />

					<h:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelPresidenteInterino');"
						styleClass="BotaoPadrao" value="Cancelar" />
				</div>
			</a4j:form>

		</rich:modalPanel>
		
		
		<rich:modalPanel id="modalPanelAnexarTema" width="300"
			height="" keepVisualState="true"
			onshow="identificacaoTema.value = ''; idObservacao.value = ''; identificacaoTema.focus();">

			<f:facet name="header">
				<h:outputText id="idMensagemTema"
						      value="Selecione o(s) tema(s)" />
			</f:facet>
			<a4j:form prependId="false">
				<div>
					<h:outputText styleClass="Padrao" value="Lance todos os temas indicados na decisão" />			
					<h:panelGrid>
						<h:panelGrid columns="2">
							<h:outputText styleClass="Padrao" value="Tema:" />
	
							<h:inputText style="margin-left:10px;" id="identificacaoTema"
								onkeypress="return noEnter(event);"
								onclick="exibirSuggestionBoxPesquisaProcesso(this, #{rich:component('sgBoxTema')});">
							</h:inputText>
						</h:panelGrid>
						
						<rich:dataTable id="idProcessoTemaTable" var="registro" headerClass="DataTableDefaultHeader"	styleClass="DataTableDefault"
							footerClass="DataTableDefaultFooter" rowClasses="DataTableRow, DataTableRow2" 
							binding="#{beanRegistrarAndamentoParaVariosProcessos.tabelaProcessoTema}"
							value="#{beanRegistrarAndamentoParaVariosProcessos.listaProcessoTema}">
							<rich:column>
								<f:facet name="header">
									<h:outputText value="Tema(s) selecionado(s)" />
								</f:facet>
								<h:outputText style="text-align:center;" value="Tema nº #{registro.tema.numeroSequenciaTema} - " styleClass="Padrao" />
								<h:outputText rendered="#{registro.objetoIncidente.eletronico}" value="e" style="color: red; font-weight: bold;" />
							    <h:outputText value="#{registro.identificacaoSimples}" styleClass="Padrao" />
							</rich:column>
							<rich:column>
								<a4j:commandLink
									reRender="idProcessoTemaTable" 									
									actionListener="#{beanRegistrarAndamentoParaVariosProcessos.removerTema}">
									<h:graphicImage
										url="../../images/remove.gif" 
										title="Remover tema"/>
								</a4j:commandLink>							
							</rich:column>							
						</rich:dataTable>						
						
					</h:panelGrid>

					<rich:suggestionbox id="sgBoxTema"
						suggestionAction="#{beanRegistrarAndamentoParaVariosProcessos.pesquisaSuggestionBoxTema}"
						for="identificacaoTema" var="result"
						fetchValue="#{result.tema.numeroSequenciaTema}"
						nothingLabel="Nenhum registro encontrado." width="300"
						ignoreDupResponses="true" eventsQueue="ajaxQueue" minChars="1">

						<a4j:support ajaxSingle="true" event="onselect" 
							eventsQueue="ajaxQueue" ignoreDupResponses="true"
							oncomplete="limparCampoIdentificacaoTema()"
							reRender="idProcessoTemaTable,idPanelVariaveis">
							<f:setPropertyActionListener value="#{result}" target="#{beanRegistrarAndamentoParaVariosProcessos.processoTemaSelecionado}" />
						</a4j:support>

						<h:column>
							<h:outputText value="Tema nº #{result.tema.numeroSequenciaTema} - " styleClass="oiSuggestion" />
							<h:outputText rendered="#{result.objetoIncidente.eletronico}" value="e" style="color: red; font-weight: bold;" />
							<h:outputText value="#{result.identificacaoSimples}" styleClass="oiSuggestion" />
						</h:column>
					</rich:suggestionbox>
				</div>

				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao" 
						onclick="processosTemasOnClick(); return;" value="Cadastrar" />

					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelAnexarTema');" 
						oncomplete="limparDadosModais();"
						actionListener="#{beanRegistrarAndamentoParaVariosProcessos.cancelarRegistroAndamento}" reRender="pnlCentral,idPanelVariaveis"
						styleClass="BotaoPadrao" value="Cancelar" />
						
				</div>
			</a4j:form>

		</rich:modalPanel>		
		
		
		<rich:modalPanel id="modalPanelImportacaoProcesso" width="360" height="125" keepVisualState="true">

			<f:facet name="header">
				<h:outputText value="Tela para confirmação de importação de lista de processos" />
			</f:facet>
			<a4j:form prependId="false">
				<div>
					<a4j:outputPanel id="pnlInformacaoImportacao">
						<h:panelGrid>
							<h:outputLabel value="Não existe processo a ser importado." 
							               rendered="#{beanRegistrarAndamentoParaVariosProcessos.quantidadeListaExportadaPeloEGab eq 0}"/>
							<h:outputLabel value="Confirma a importação de #{beanRegistrarAndamentoParaVariosProcessos.quantidadeListaExportadaPeloEGab} processo(s)?" 
							               rendered="#{beanRegistrarAndamentoParaVariosProcessos.quantidadeListaExportadaPeloEGab > 0}"/>
						</h:panelGrid>
					</a4j:outputPanel>
				</div>

				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao"
						onclick="Richfaces.hideModalPanel('modalPanelImportacaoProcesso'); exibirMsgProcessando(true); "
						actionListener="#{beanRegistrarAndamentoParaVariosProcessos.processarImportacaoListaExportadaPeloEGab}"
						reRender="pnlTabelaProcesso, observacao"
						oncomplete="exibirMsgProcessando(false);" value="Importar" />

					<h:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelImportacaoProcesso');"
						styleClass="BotaoPadrao" value="Cancelar" />
				</div>				
			</a4j:form>

		</rich:modalPanel>		

		<!--  -->
		
		<rich:modalPanel id="modalPanelConfirmacaoRegistro" keepVisualState="true" autosized="true" style="overflow:auto" width="600">
			<f:facet name="header">
				<h:outputText value="Confirmação" />
			</f:facet>
			<a4j:form prependId="false">
				<t:div id="idPanelConfirmacaoRegistro">
						<h:outputText escape="false" value="#{beanRegistrarAndamentoParaVariosProcessos.mensagemDeRestricaoRegistroDeAndamento}" style="white-space: pre-wrap;"></h:outputText>
				</t:div>
				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao" oncomplete="exibirMsgProcessando(false);"
						onclick="Richfaces.hideModalPanel('modalPanelConfirmacaoRegistro'); if (!confirmaLancamento()) {return false;}"
						value="Ok" reRender="pnlCentral,pnlAssinatura"
						rendered="#{!beanRegistrarAndamentoParaVariosProcessos.andamentoGeraDocumento}"
						action="#{beanRegistrarAndamentoParaVariosProcessos.confirmarAndamento}"/>
					<a4j:commandButton styleClass="BotaoPadrao" oncomplete="exibirMsgProcessando(false);" 
						onclick="Richfaces.hideModalPanel('modalPanelConfirmacaoRegistro'); if (!confirmaLancamento()) {return false;}"
						value="Ok" reRender="pnlCentral"
						rendered="#{beanRegistrarAndamentoParaVariosProcessos.andamentoGeraDocumento}"
						action="#{beanRegistrarAndamentoParaVariosProcessos.confirmarAssinarAndamento}"/>
					<a4j:commandButton oncomplete="exibirMsgProcessando(false);"
						onclick="Richfaces.hideModalPanel('modalPanelConfirmacaoRegistro');"
						styleClass="BotaoPadrao" value="Cancelar"/>
				</div>
			</a4j:form>

		</rich:modalPanel>

		<jsp:include page="/pages/template/footer.jsp" flush="true" />
		
		<script type="text/javascript">
		 function acionaBotaoChamaAssinador(){			 
			 document.getElementById("btnChamaAssinador").click();
		 }
		</script>		

	</a4j:page>
</f:view>
