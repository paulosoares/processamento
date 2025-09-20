<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />


<f:view>
	<a4j:page pageTitle="::.. Principal ..::">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Avisos Não Criados" />
			</jsp:include>
			<a name="ancora"></a>
		</h:form>
		<a4j:form id="form" prependId="false">
			<script type="text/javascript">
				function desabilitaSiglaNumero() {
					if (document.getElementById('processoOriginario').value != '') {
						document.getElementById('siglaProcesso').disabled = "disabled";
						document.getElementById('siglaProcesso').value = '';
						document.getElementById('numProcesso').disabled = "disabled";
						document.getElementById('numProcesso').value = '';
					} else {
						document.getElementById('siglaProcesso').disabled = false;
						document.getElementById('numProcesso').disabled = false;
					}
				}
				function desabilitaAndamentos() {
					if (document.getElementById('chkAndamentoExpedito').checked) {
						document.getElementById('andamento').disabled = "disabled";
						document.getElementById('andamento').value = '';
					} else {
						document.getElementById('andamento').disabled = false;
					}

				}
			</script>
			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid id="pnlCentral" styleClass="Moldura" cellpadding="0"
					cellspacing="0">
					<a4j:outputPanel id="pnlPesquisa" styleClass="MolduraInterna">
						<a4j:outputPanel ajaxRendered="true" keepTransient="false"
							id="pnlBuscaModelo">
							<div class="PainelTituloCriaTexto">
								<span>Pesquisar por Avisos Não Criados</span>
							</div>
						</a4j:outputPanel>
						<div style="padding-top: 10px;">
							<h:panelGrid columns="3">
								<t:panelGroup>
									<h:outputText value="Andamentos " styleClass="Padrao" />
									<h:selectOneMenu id="andamento"
										value="#{beanAndamentoProcesso.andamento}"
										style="margin-left:5px;">
										<f:selectItems
											value="#{beanAndamentoProcesso.listaAndamentos}" />
									</h:selectOneMenu>
								</t:panelGroup>
								<t:panelGroup>
									<h:outputText styleClass="Padrao"
										value="Pesquisar por processos recursais ('ARE', 'RE', 'AI'):" />
									<h:selectOneMenu onblur="desabilitaSiglaNumero();"
										id="processoOriginario"
										value="#{beanAndamentoProcesso.processoOriginario}"
										style="margin-left:5px;">
										<f:selectItems
											value="#{beanAndamentoProcesso.processosOriginarios}" />
									</h:selectOneMenu>
								</t:panelGroup>
								<t:panelGroup>
									<h:outputText styleClass="Padrao" value="Observação: " />
									<h:inputText id="observacao" style="margin-left:5px;" size="30"
										value="#{beanAndamentoProcesso.observacao}" />
								</t:panelGroup>
							</h:panelGrid>
							<h:panelGrid columns="6" id="painelPesquisa1">
								<t:panelGrid columns="2">
									<h:outputLabel styleClass="Padrao" value="Período: "></h:outputLabel>
									<h:panelGrid columns="1">
										<t:panelGroup>
											<rich:calendar id="itDataInicial"
												value="#{beanAndamentoProcesso.dataInicial}"
												datePattern="dd/MM/yyyy" locale="pt_Br" />
											<h:outputLabel styleClass="Padrao" value=" a "></h:outputLabel>
											<rich:calendar id="itDataFinal"
												value="#{beanAndamentoProcesso.dataFinal}"
												datePattern="dd/MM/yyyy" locale="pt_Br" />
										</t:panelGroup>
									</h:panelGrid>
								</t:panelGrid>
								<t:panelGroup>
									<h:outputText styleClass="Padrao" value="Sigla:"></h:outputText>
									<h:inputText id="siglaProcesso" size="4" maxlength="4"
										onkeyup="caixaAlta(this),converterClasse(this);"
										onchange="validarClasse(this);"
										onkeypress="return mascaraInputLetra(this,event);"
										style="margin-left:5px;"
										value="#{beanAndamentoProcesso.siglaProcesso}" />
								</t:panelGroup>
								<t:panelGroup>
									<h:outputText styleClass="Padrao" value="Número:"></h:outputText>
									<h:inputText id="numProcesso" size="9" style="margin-left:5px;"
										maxlength="9"
										onkeypress="return mascaraInputNumerico(this, event);"
										value="#{beanAndamentoProcesso.numProcesso}">
										<f:converter converterId="javax.faces.Integer" />
									</h:inputText>
								</t:panelGroup>
								<t:panelGroup>
									<h:outputText styleClass="Padrao"
										value="Pesquisar por processos que contenham andamentos de \"Expedidos\": "></h:outputText>
									<h:selectBooleanCheckbox id="chkAndamentoExpedito"
										onclick="desabilitaAndamentos();"
										value="#{beanAndamentoProcesso.andamentoExpedito}" />
								</t:panelGroup>
								<t:panelGroup>
									<a4j:commandButton styleClass="BotaoPesquisar"
										value="Pesquisar"
										action="#{beanAndamentoProcesso.pesquisarAction}"
										style="margin-left:15px;" ignoreDupResponses="true"
										onclick="exibirMsgProcessando(true);"
										oncomplete="exibirMsgProcessando(false);" />
								</t:panelGroup>
							</h:panelGrid>
						</div>
					</a4j:outputPanel>
				</h:panelGrid>
			</h:panelGrid>
			<a4j:outputPanel ajaxRendered="true" keepTransient="false"
				id="pnlResultadoPesquisa" styleClass="MolduraInterna">

				<c:if
					test="${not empty beanAndamentoProcesso.listaProcessoAndamento}">
					<rich:datascroller id="dataScrollerPecas" for="tableProcessos"
						fastControls="hide" maxPages="5" pageIndexVar="paginaAtual"
						pagesVar="paginas" eventsQueue="ajaxQueue"
						ignoreDupResponses="true" reRender="pnlResultadoPesquisa">
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

					<rich:dataTable headerClass="DataTableDefaultHeader"
						id="tableProcessos" styleClass="DataTableDefault"
						footerClass="DataTableDefaultFooter"
						rowClasses="DataTableRow, DataTableRow2"
						columnClasses="quinzeCenter" rows="20"
						value="#{beanAndamentoProcesso.listaProcessoAndamento}"
						var="wrappedProcesso"
						binding="#{beanAndamentoProcesso.tabelaProcessoAndamento}">

						<rich:column
							sortBy="#{wrappedProcesso.wrappedObject.identificacao}">
							<f:facet name="header">
								<h:outputText value="Processo" />
							</f:facet>
							<h:outputText styleClass="Padrao"
								value="#{wrappedProcesso.wrappedObject.identificacao}" />
						</rich:column>
						<rich:column
							sortBy="#{wrappedProcesso.wrappedObject.dataAndamento}">
							<f:facet name="header">
								<h:outputText value="Data do andamento" />
							</f:facet>
							<h:outputText styleClass="Padrao"
								value="#{wrappedProcesso.wrappedObject.dataAndamento}" />
						</rich:column>
						<rich:column
							sortBy="#{wrappedProcesso.wrappedObject.codigoAndamento}">
							<f:facet name="header">
								<h:outputText value="Andamento" />
							</f:facet>
							<h:outputText styleClass="Padrao"
								value="#{wrappedProcesso.wrappedObject.tipoAndamento.identificacao}" />
						</rich:column>
						<rich:column>
							<f:facet name="header">
								<h:outputText value="Observação" />
							</f:facet>
							<h:outputText styleClass="Padrao"
								value="#{wrappedProcesso.wrappedObject.descricaoObservacaoAndamento}" />
						</rich:column>
						<rich:column>
							<f:facet name="header">
								<h:outputText value="Ações:" />
							</f:facet>
							<a4j:commandLink action="#{beanAndamentoProcesso.abrirModal}"
								value="Reenviar" reRender="pnlCentral,modalReenviar"
								oncomplete="Richfaces.showModalPanel('modalReenviar')" />
						</rich:column>
					</rich:dataTable>

				</c:if>
			</a4j:outputPanel>
		</a4j:form>
		<rich:modalPanel id="modalReenviar" keepVisualState="true"
			autosized="true" width="1000">

			<f:facet name="header">
				<h:outputText value="Selec" />
			</f:facet>

			<a4j:form prependId="false">
				<a4j:outputPanel ajaxRendered="true" keepTransient="false"
					id="pnlModal" styleClass="MolduraInterna">
					<rich:tabPanel switchType="ajax">
						<rich:tab label="Selecionar deslocamento: "
							id="selecionarDeslocamento"
							disabled="#{beanAndamentoProcesso.editando}">
							<rich:dataTable headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								value="#{beanAndamentoProcesso.listaDeslocaProcessos}"
								id="idDeslocaProcessos" var="deslocaProcesso"
								binding="#{beanAndamentoProcesso.tabelaDeslocaProcessos}">

								<t:selectOneRadio id="idRadioDeslocaProcesso"
									value="#{beanAndamentoProcesso.modalDeslocaProcessoCheck}">
									<a4j:support ajaxSingle="true" event="onclick" />
									<f:selectItems
										value="#{beanAndamentoProcesso.listaDeslocaProcessoSelectItem}" />
								</t:selectOneRadio>

								<rich:column>
									<t:radio for="idRadioDeslocaProcesso"
										index="#{beanAndamentoProcesso.indexDeslocaProcesso}"></t:radio>
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Data Envio" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{deslocaProcesso.dataRecebimento}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Guia" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{deslocaProcesso.id.numeroGuia}/#{deslocaProcesso.id.anoGuia}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Origem" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{beanAndamentoProcesso.descricaoOrigem}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Destino" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{beanAndamentoProcesso.descricaoDestino}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Andamento " />
									</f:facet>
									<h:outputText styleClass="Padrao"
										rendered="#{not empty deslocaProcesso.andamentoProcesso}"
										value="#{deslocaProcesso.andamentoProcesso.codigoAndamento}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Integrada" />
									</f:facet>
									<h:outputText
										rendered="#{beanAndamentoProcesso.destinoEstaIntegrada}"
										value="Sim" style="color: green; font-weight: bold;" />
									<h:outputText
										rendered="#{!beanAndamentoProcesso.destinoEstaIntegrada}"
										value="Não" style="color: red; font-weight: bold;" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Número da Origem Está Cadastrada" />
									</f:facet>
									<h:outputText
										rendered="#{beanAndamentoProcesso.numeroDestinoEstaIntegrada}"
										value="Sim" style="color: green; font-weight: bold;" />
									<h:outputText
										rendered="#{!beanAndamentoProcesso.numeroDestinoEstaIntegrada}"
										value="Não" style="color: red; font-weight: bold;" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Ações" />
									</f:facet>
									<a4j:commandLink
										action="#{beanAndamentoProcesso.abrirBotaoEditar}"
										oncomplete="document.getElementById('selecionarOrigem_lbl').click()">
										<h:graphicImage url="../../images/editar_item.png"
											title="Editar" />
									</a4j:commandLink>
								</rich:column>
							</rich:dataTable>
							<div style="padding-top: 20px; text-align: center">
								<a4j:commandButton value="Confimar Baixa"
									styleClass="BotaoPadraoEstendido" reRender="tableProcessos"
									disabled="#{!beanAndamentoProcesso.confirmarBaixa}"
									onclick="Richfaces.hideModalPanel('modalReenviar'); exibirMsgProcessando(true);"
									action="#{beanAndamentoProcesso.deslocaProcessoAction}"
									oncomplete="exibirMsgProcessando(false);" />
								<a4j:commandButton
									oncomplete="Richfaces.hideModalPanel('modalReenviar');"
									action="#{beanAndamentoProcesso.limparSessaoModalAction }"
									styleClass="BotaoPadrao" value="Fechar"
									reRender="idTabelaOrigensCadastradas" id="idFechar" />
							</div>
						</rich:tab>
						<rich:tab label="Selecionar Origem" id="selecionarOrigem"
							disabled="#{!beanAndamentoProcesso.editando}">
							<t:div id="idPanelVerificarCodigoOrigem"
								style="padding-bottom: 10px; padding-top: 10px; padding-left: 10px; padding-right: 5px">
								<div>
									<fieldset>
										<legend>
											<h:outputLabel value="Incluir nova origem para devolução"
												styleClass="Padrao" />
										</legend>
										<h:panelGrid columns="2">
											<h:outputLabel value="Órgão:" styleClass="Padrao" />
											<h:selectOneMenu value="#{beanAndamentoProcesso.modalOrgao}"
												styleClass="Input" id="idComboOrgaos">
												<f:selectItems
													value="#{beanAndamentoProcesso.listaModalOrgao}" />
												<a4j:support ajaxSingle="true" event="onchange"
													reRender="idComboProcedencias" />
											</h:selectOneMenu>

											<h:outputLabel value="Procedência:" styleClass="Padrao" />
											<h:selectOneMenu
												value="#{beanAndamentoProcesso.modalProcedencia}"
												styleClass="Input" id="idComboProcedencias">
												<f:selectItems
													value="#{beanAndamentoProcesso.listaModalProcedencia}" />
												<a4j:support ajaxSingle="true" event="onchange"
													reRender="idComboOrigemDevolucao" />
											</h:selectOneMenu>

											<h:outputLabel value="Cidade/Complemento:"
												styleClass="Padrao" />
											<h:selectOneMenu value="#{beanAndamentoProcesso.modalOrigem}"
												styleClass="Input" id="idComboOrigemDevolucao">
												<f:selectItems
													value="#{beanAndamentoProcesso.listaModalOrigem}" />
												<a4j:support ajaxSingle="true" event="onchange" />
											</h:selectOneMenu>


											<h:outputLabel value="Sigla Origem:" styleClass="Padrao" />
											<h:inputText id="idSiglaOrigem"
												value="#{beanAndamentoProcesso.modalSiglaOrigem}"
												styleClass="Input" />

											<h:outputLabel value="Número Origem:" styleClass="Padrao" />
											<h:inputText id="idNumOrigem"
												value="#{beanAndamentoProcesso.modalNumeroOrigem}"
												styleClass="Input" />

											<h:outputLabel value="Número Único do Processo:"
												styleClass="Padrao" />
											<h:inputText id="idNumUnico"
												value="#{beanAndamentoProcesso.modalNumeroUnico}"
												styleClass="Input" />
										</h:panelGrid>

										<div style="padding-top: 20px; text-align: center">
											<a4j:commandButton styleClass="BotaoPadraoEstendido"
												action="#{beanAndamentoProcesso.inserirOrigemAction}"
												value="Inserir Origem"
												reRender="idTabelaOrigensCadastradas,idComboOrgaos,idComboProcedencias,idComboOrigemDevolucao,idNumUnico,idNumOrigem,idOrigem" />
										</div>
									</fieldset>

									<fieldset style="margin-top: 20px">
										<legend>
											<h:outputLabel value="Origens cadastradas pela autuação"
												styleClass="Padrao" />
										</legend>

										<rich:dataTable headerClass="DataTableDefaultHeader"
											styleClass="DataTableDefault"
											footerClass="DataTableDefaultFooter"
											rowClasses="DataTableRow, DataTableRow2"
											value="#{beanAndamentoProcesso.listaOrigens}"
											id="idTabelaOrigensCadastradas" var="historicoOrigem"
											binding="#{beanAndamentoProcesso.tabelaOrigens}">

											<t:selectOneRadio id="idRadiosOrigens"
												value="#{beanAndamentoProcesso.modalOrigemCheck}">
												<a4j:support ajaxSingle="true" event="onclick" />
												<f:selectItems
													value="#{beanAndamentoProcesso.listaOrigensSelectItens}" />
											</t:selectOneRadio>

											<rich:column>
												<t:radio for="idRadiosOrigens"
													index="#{beanAndamentoProcesso.indexOrigemCadastrada}"></t:radio>
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
													<h:outputText value="Numero Único" />
												</f:facet>
												<h:outputText styleClass="Padrao"
													value="#{historicoOrigem.numeroUnicoProcesso}" />
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
													<h:outputText value="Principal" />
												</f:facet>
												<h:outputText styleClass="Padrao" value="Sim"
													rendered="#{historicoOrigem.principal}" />
												<h:outputText styleClass="Padrao" value="Não"
													rendered="#{!historicoOrigem.principal}" />
												<a4j:commandLink value=" (Escolher principal)"
													action="#{beanAndamentoProcesso.escolherOrigemPrincipal }"
													reRender="idTabelaOrigensCadastradas"
													rendered="#{!historicoOrigem.principal }" />
											</rich:column>
											<rich:column>
												<f:facet name="header">
													<h:outputText value="Integrada" />
												</f:facet>
												<h:outputText
													rendered="#{beanAndamentoProcesso.isOrigemIntegrada}"
													styleClass="Padrao" value="Sim" />
												<h:outputText
													rendered="#{!beanAndamentoProcesso.isOrigemIntegrada}"
													styleClass="Padrao" value="Não" />
											</rich:column>
											<rich:column>
												<f:facet name="header">
													<h:outputText value="Ações" />
												</f:facet>
												<a4j:commandLink value="Excluir"
													action="#{beanAndamentoProcesso.excluirOrigemAction}"
													reRender="idTabelaOrigensCadastradas,pnlCentral" />
											</rich:column>
										</rich:dataTable>
									</fieldset>
								</div>
							</t:div>

							<div style="padding-top: 20px; text-align: center">
								<a4j:commandButton styleClass="BotaoPadraoEstendido"
									oncomplete="if(solicitaValidacao(#{beanAndamentoProcesso.temOrigemSelecionada},#{beanAndamentoProcesso.origemEstaIntegrada},#{beanAndamentoProcesso.verificaNumUnico},#{beanAndamentoProcesso.verificaNumOrigem})){ document.getElementById('idBotaoConfirmarEnvio').click() ;};"
									action="#" value="Confirmar Alteração"
									reRender="idTabelaOrigensCadastradas" />

								<a4j:commandButton
									oncomplete="document.getElementById('selecionarDeslocamento_lbl').click();"
									action="#{beanAndamentoProcesso.voltarAction }"
									styleClass="BotaoPadrao" value="Voltar" id="Voltar" />

								<a4j:commandButton styleClass="Botaooculto"
									id="idBotaoConfirmarEnvio"
									reRender="idTabelaOrigensCadastradas,idDeslocaProcessos"
									oncomplete="document.getElementById('selecionarDeslocamento_lbl').click();"
									action="#{beanAndamentoProcesso.alterarDeslocaProcessoAction }" />
							</div>
							<script type="text/javascript">
								function solicitaValidacao(
										temOrigemSelecionada,
										origemEstaIntegrada, verificaNumUnico,
										verificaNumOrigem) {
									if (!temOrigemSelecionada) {
										alert('Favor selecione uma origem cadastrada.');
										return false;
									}

									if (!origemEstaIntegrada) {
										if (!confirm('ATENÇÃO: A origem selecionada não está integrada. Deseja realmente baixar o processo para essa origem?')) {
											return false;
										}
									}

									if (!verificaNumUnico) {
										if (!confirm('Esse processo não possui o número único cadastrado. A inclusão dessa informação é importante para que a origem localize esse processo depois de devolvido eletronicamente. Deseja confirmar a baixa sem informar o número único?')) {
											return false;
										}
									}

									if (!verificaNumOrigem) {
										if (!confirm('Não há indicativo de sigla e número do processo de origem. Essa informação é importante para que a origem localize esse processo. Deseja confirmar a baixa sem informar o número de origem?')) {
											return false;
										}
									}

									return true;
								};
							</script>
						</rich:tab>
					</rich:tabPanel>
				</a4j:outputPanel>
			</a4j:form>
		</rich:modalPanel>
	</a4j:page>
</f:view>
