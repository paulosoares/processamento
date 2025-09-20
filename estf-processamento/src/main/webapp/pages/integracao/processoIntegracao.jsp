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
				<jsp:param name="nomePagina" value="Avisos de Comunicacao" />
			</jsp:include>
			<a name="ancora"></a>

		</h:form>
		<a4j:form id="form" prependId="false">
			<script type="text/javascript">
				function validaInserirOrgao(bolOrigem) {
					var confirma;
					if (document.getElementById('idComboOrgaos').selectedIndex <= 0) {
						alert('Favor selecionar um órgão.');
						return false;
					}
					if (document.getElementById('idComboProcedencias').selectedIndex <= 0) {
						alert('Favor selecionar uma procedência.');
						return false;
					}
					if (document.getElementById('idComboOrigemDevolucao').selectedIndex <= 0) {
						alert('Favor selecionar uma cidade.');
						return false;
					}

					if (document.getElementById('idNumUnico').value == '') {
						if (!confirm('O processo não possui número único cadastrado. A inclusão do número único facilita o relacionamento do processo do STF com o tribunal de origem. Deseja continuar?')) {
							return false;
						}
					}
					if (document.getElementById('idSiglaOrigem').value == '') {
						if (!confirm('Não há informação de qual o número de processo para a origem selecionada.A falta dessa informação dificulta a localização desse processo pelo tribunal de origem. Deseja continuar?')) {
							return false;
						}
					}
					if (!bolOrigem) {
						if (!confirm('O Tribunal escolhido não está integrado. Deseja realmente enviar a notificação desse processo para esse tribunal/órgão?')) {
							return false;
						}
					}
					return confirm;
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
								<span>Pesquisar por Avisos de Comunicação</span>
							</div>
							<div style="padding-top: 10px;">
								<h:panelGrid columns="3">
									<t:panelGroup>
										<h:outputText value="Usuários Externos: " styleClass="Padrao" />
										<h:selectOneMenu id="usuarioExterno"
											value="#{beanProcessoIntegracao.usuarioExterno}"
											style="margin-left:5px;">
											<f:selectItems
												value="#{beanProcessoIntegracao.listaUsuariosExternos}" />
										</h:selectOneMenu>
									</t:panelGroup>

									<t:panelGroup>
										<h:outputText value="Situação: " styleClass="Padrao" />
										<h:selectOneMenu id="tipoProcessoIntegracao"
											value="#{beanProcessoIntegracao.tipoProcessoIntegracao}"
											style="margin-left:5px;">
											<f:selectItems
												value="#{beanProcessoIntegracao.listaTipoProcessoIntegracao}" />
										</h:selectOneMenu>
									</t:panelGroup>
									<t:panelGroup>
										<h:outputText value="Tipo: " styleClass="Padrao" />
										<h:selectOneMenu id="tipoComunicacao" 
											value="#{beanProcessoIntegracao.tipoComunicacao}"
											style="margin-left:5px;">
											<f:selectItems
												value="#{beanProcessoIntegracao.listaTipoComunicacao}" />
										</h:selectOneMenu>
									</t:panelGroup>
								</h:panelGrid>
								<h:panelGrid columns="4" id="painelPesquisa1">
									<t:panelGrid columns="2">
										<h:outputLabel styleClass="Padrao" value="Período: "></h:outputLabel>
										<h:panelGrid columns="1">
											<t:panelGroup>
												<rich:calendar id="itDataInicial"
													value="#{beanProcessoIntegracao.dataInicial}"
													datePattern="dd/MM/yyyy" locale="pt_Br" />
												<h:outputLabel styleClass="Padrao" value=" a "></h:outputLabel>
												<rich:calendar id="itDataFinal"
													value="#{beanProcessoIntegracao.dataFinal}"
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
											value="#{beanProcessoIntegracao.siglaProcesso}" />
									</t:panelGroup>
									<t:panelGroup>
										<h:outputText styleClass="Padrao" value="Número:"></h:outputText>
										<h:inputText id="numProcesso" size="9"
											style="margin-left:5px;" maxlength="9"
											onkeypress="return mascaraInputNumerico(this, event);"
											value="#{beanProcessoIntegracao.numProcesso}">
											<f:converter converterId="javax.faces.Integer" />
										</h:inputText>

										<a4j:commandButton styleClass="BotaoPesquisar"
											style="margin-left:15px;" value="Pesquisar"
											id="btnPesquisarProcesso"
											action="#{beanProcessoIntegracao.pesquisarAction}"
											ignoreDupResponses="true"
											onclick="exibirMsgProcessando(true);"
											oncomplete="exibirMsgProcessando(false);" />
										<a4j:commandButton styleClass="BotaoPadrao" value="Limpar"
											id="btnLimparProcesso" style="margin-left:15px; inline;"
											ignoreDupResponses="true"
											onclick="exibirMsgProcessando(true);"
											oncomplete="exibirMsgProcessando(false);"
											action="#{beanProcessoIntegracao.limparSessaoAction}" />
									</t:panelGroup>
								</h:panelGrid>
							</div>
						</a4j:outputPanel>
						<a4j:outputPanel ajaxRendered="true" keepTransient="false"
							id="pnlResultadoPesquisa" styleClass="MolduraInterna">
							<c:if test="${not empty beanProcessoIntegracao.listaProcessos}">
								<rich:datascroller id="dataScrollerPecas" for="tableProcessos"
									fastControls="hide" maxPages="25" pageIndexVar="paginaAtual"
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
									columnClasses="tres, dezCenter, vinte, sete, quinzeCenter, vinte, seteCenter, seteCenter"
									rows="13" value="#{beanProcessoIntegracao.listaProcessos}"
									var="wrappedProcesso"
									binding="#{beanProcessoIntegracao.tabelaProcessos}">
									<rich:column>
										<f:facet name="header">
											<a4j:commandButton image="../../images/setabaixo.gif"
												actionListener="#{beanProcessoIntegracao.marcarTodosProcessosIntegracao}" />
										</f:facet>
										<h:selectBooleanCheckbox
											onclick="document.getElementById('BotaoAtualizarMarcacao').click();"
											value="#{wrappedProcesso.checked}"
											disabled="#{wrappedProcesso.wrappedObject.enviado == 'E' ? false : true}" />
									</rich:column>
									<rich:column 
										sortBy="#{wrappedProcesso.wrappedObject.dataCriacao}">
										<f:facet name="header">
											<h:outputText value="Data da Comunicação" />
										</f:facet>
										<h:outputText styleClass="Padrao" value="#{wrappedProcesso.wrappedObject.dataCriacao}">
											<f:convertDateTime pattern="dd/MM/yyyy" /> 
										</h:outputText>
									</rich:column>									
									<rich:column
										sortBy="#{wrappedProcesso.wrappedObject.identificacao}">
										<f:facet name="header">
											<h:outputText value="Processo" />
										</f:facet>
										<h:outputLink
											value="http://www.stf.jus.br/portal/processo/verProcessoAndamento.asp?incidente=#{wrappedProcesso.wrappedObject.processo.id}"
											target="_blank">
											<h:outputText styleClass="PadraoLink"
												value="#{wrappedProcesso.wrappedObject.identificacao}" />
										</h:outputLink>
									</rich:column>
									<rich:column
										sortBy="#{wrappedProcesso.wrappedObject.andamentoProcesso.tipoAndamento.identificacao}">
										<f:facet name="header">
											<h:outputText value="Andamento" />
										</f:facet>
										<h:outputText styleClass="Padrao"
											value="#{wrappedProcesso.wrappedObject.andamentoProcesso.tipoAndamento.identificacao}" />
									</rich:column>
									<rich:column sortBy="#{wrappedProcesso.wrappedObject.enviado}">
										<f:facet name="header">
											<h:outputText value="Situação" />
										</f:facet>
										<h:outputText style="Padrao"
											rendered="#{wrappedProcesso.wrappedObject.enviado}"
											value="Lido" />
										<h:outputText style="Padrao"
											rendered="#{!wrappedProcesso.wrappedObject.enviado}"
											value="Não Lido" />
									</rich:column>
									<rich:column
										sortBy="#{wrappedProcesso.wrappedObject.origem.descricao}">
										<f:facet name="header">
											<h:outputText value="Orgão" />
										</f:facet>
										<h:outputText styleClass="Padrao"
											value="#{wrappedProcesso.wrappedObject.origem.descricao}" />
									</rich:column>
									<rich:column
										sortBy="#{wrappedProcesso.wrappedObject.tipoComunicacao.identificacao}">
										<f:facet name="header">
											<h:outputText value="Tipo da Comunicação" />
										</f:facet>
										<h:outputText styleClass="Padrao"
											value="#{wrappedProcesso.wrappedObject.tipoComunicacao.identificacao}" />
									</rich:column>
									<rich:column>
										<f:facet name="header">
											<h:outputText value="Ação" />
										</f:facet>
										<h:panelGrid
											rendered="#{!wrappedProcesso.wrappedObject.enviado}"
											columns="1">
											<t:panelGroup>
												<a4j:commandLink
													action="#{beanProcessoIntegracao.abrirModal}"
													value="Reenviar" reRender="pnlCentral,modalReenviar"
													oncomplete="Richfaces.showModalPanel('modalReenviar')">
												</a4j:commandLink>
											</t:panelGroup>
										</h:panelGrid>
									</rich:column>
								</rich:dataTable>
								<a4j:commandButton value="Marcar como Lidos"
									styleClass="BotaoPadraoEstendido"
									onclick="if(confirm('Deseja marcar como lido todos os registros selecionados? Essa ação não pode ser desfeita.')){retorna();}; exibirMsgProcessando(true);" />
								<a4j:jsFunction name="retorna" ajaxSingle="true"
									action="#{beanProcessoIntegracao.marcarComoLidos}"
									oncomplete="exibirMsgProcessando(false);"
									reRender="tableProcessos" />
								<a4j:commandButton styleClass="BotaoOculto"
									id="BotaoAtualizarMarcacao"
									actionListener="#{beanProcessoIntegracao.atualizarMarcacao}" />
							</c:if>
						</a4j:outputPanel>
					</a4j:outputPanel>
				</h:panelGrid>
			</h:panelGrid>
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
							disabled="#{beanProcessoIntegracao.editando}">
							<rich:dataTable headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								value="#{beanProcessoIntegracao.listaDeslocaProcessos}"
								id="idDeslocaProcessos" var="deslocaProcesso"
								binding="#{beanProcessoIntegracao.tabelaDeslocaProcessos}">

								<t:selectOneRadio id="idRadioDeslocaProcesso"
									value="#{beanProcessoIntegracao.modalDeslocaProcessoCheck}">
									<a4j:support ajaxSingle="true" event="onclick" />
									<f:selectItems
										value="#{beanProcessoIntegracao.listaDeslocaProcessoSelectItem}" />
								</t:selectOneRadio>

								<rich:column>
									<t:radio for="idRadioDeslocaProcesso"
										index="#{beanProcessoIntegracao.indexDeslocaProcesso}"></t:radio>
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
										value="#{beanProcessoIntegracao.descricaoOrigem}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Destino" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{beanProcessoIntegracao.descricaoDestino}" />
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
										rendered="#{beanProcessoIntegracao.destinoEstaIntegrada}"
										value="Sim" style="color: green; font-weight: bold;" />
									<h:outputText
										rendered="#{!beanProcessoIntegracao.destinoEstaIntegrada}"
										value="Não" style="color: red; font-weight: bold;" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Ações" />
									</f:facet>
									<a4j:commandLink
										action="#{beanProcessoIntegracao.abrirBotaoEditar}"
										oncomplete="document.getElementById('selecionarOrigem_lbl').click()">
										<h:graphicImage url="../../images/editar_item.png"
											title="Editar" />
									</a4j:commandLink>
								</rich:column>
							</rich:dataTable>
							<div style="padding-top: 20px; text-align: center">
								<a4j:commandButton value="Confimar Baixa"
									styleClass="BotaoPadraoEstendido" reRender="tableProcessos"
									disabled="#{!beanProcessoIntegracao.confirmarBaixa}"
									onclick="Richfaces.hideModalPanel('modalReenviar'); exibirMsgProcessando(true);"
									action="#{beanProcessoIntegracao.deslocaProcessoAction}"
									oncomplete="exibirMsgProcessando(false);" />
								<%--<a4j:commandButton value="Marcar Enviado Por Midia"
									styleClass="BotaoPadraoEstendido" reRender="tableProcessos"
									disabled="#{!beanProcessoIntegracao.confirmarMarcadoPorMidia}"
									onclick="Richfaces.hideModalPanel('modalReenviar'); exibirMsgProcessando(true);"
									action="#{beanProcessoIntegracao.marcarEnviadoPorMidiaAction}"
									oncomplete="exibirMsgProcessando(false);" /> --%>
								<a4j:commandButton
									oncomplete="Richfaces.hideModalPanel('modalReenviar');"
									action="#{beanProcessoIntegracao.limparSessaoModalAction }"
									styleClass="BotaoPadrao" value="Fechar"
									reRender="idTabelaOrigensCadastradas" id="idFechar" />
							</div>
						</rich:tab>
						<rich:tab label="Selecionar Origem" id="selecionarOrigem"
							disabled="#{!beanProcessoIntegracao.editando}">
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
											<h:selectOneMenu value="#{beanProcessoIntegracao.modalOrgao}"
												styleClass="Input" id="idComboOrgaos">
												<f:selectItems
													value="#{beanProcessoIntegracao.listaModalOrgao}" />
												<a4j:support ajaxSingle="true" event="onchange"
													reRender="idComboProcedencias" />
											</h:selectOneMenu>

											<h:outputLabel value="Procedência:" styleClass="Padrao" />
											<h:selectOneMenu
												value="#{beanProcessoIntegracao.modalProcedencia}"
												styleClass="Input" id="idComboProcedencias">
												<f:selectItems
													value="#{beanProcessoIntegracao.listaModalProcedencia}" />
												<a4j:support ajaxSingle="true" event="onchange"
													reRender="idComboOrigemDevolucao" />
											</h:selectOneMenu>

											<h:outputLabel value="Cidade/Complemento:"
												styleClass="Padrao" />
											<h:selectOneMenu
												value="#{beanProcessoIntegracao.modalOrigem}"
												styleClass="Input" id="idComboOrigemDevolucao">
												<f:selectItems
													value="#{beanProcessoIntegracao.listaModalOrigem}" />
												<a4j:support ajaxSingle="true" event="onchange" />
											</h:selectOneMenu>

											<h:outputLabel value="Sigla Origem:" styleClass="Padrao" />
											<h:inputText id="idSiglaOrigem"
												value="#{beanProcessoIntegracao.modalSiglaOrigem}"
												styleClass="Input" />

											<h:outputLabel value="Número Origem:" styleClass="Padrao" />
											<h:inputText id="idNumOrigem"
												value="#{beanProcessoIntegracao.modalNumeroOrigem}"
												styleClass="Input" />

											<h:outputLabel value="Número Único do Processo:"
												styleClass="Padrao" />
											<h:inputText id="idNumUnico"
												value="#{beanProcessoIntegracao.modalNumeroUnico}"
												styleClass="Input" />
										</h:panelGrid>

										<div style="padding-top: 20px; text-align: center">
											<a4j:commandButton styleClass="BotaoPadraoEstendido"
												action="#{beanProcessoIntegracao.inserirOrigemAction}"
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
											value="#{beanProcessoIntegracao.listaOrigens}"
											id="idTabelaOrigensCadastradas" var="historicoOrigem"
											binding="#{beanProcessoIntegracao.tabelaOrigens}">

											<t:selectOneRadio id="idRadiosOrigens"
												value="#{beanProcessoIntegracao.modalOrigemCheck}">
												<a4j:support ajaxSingle="true" event="onclick" />
												<f:selectItems
													value="#{beanProcessoIntegracao.listaOrigensSelectItens}" />
											</t:selectOneRadio>

											<rich:column>
												<t:radio for="idRadiosOrigens"
													index="#{beanProcessoIntegracao.indexOrigemCadastrada}"></t:radio>
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
													action="#{beanProcessoIntegracao.escolherOrigemPrincipal }"
													reRender="idTabelaOrigensCadastradas"
													rendered="#{!historicoOrigem.principal }" />
											</rich:column>
											<rich:column>
												<f:facet name="header">
													<h:outputText value="Integrada" />
												</f:facet>
												<h:outputText
													rendered="#{beanProcessoIntegracao.isOrigemIntegrada}"
													styleClass="Padrao" value="Sim" />
												<h:outputText
													rendered="#{!beanProcessoIntegracao.isOrigemIntegrada}"
													styleClass="Padrao" value="Não" />
											</rich:column>
											<rich:column>
												<f:facet name="header">
													<h:outputText value="Ações" />
												</f:facet>
												<a4j:commandLink value="Excluir"
													action="#{beanProcessoIntegracao.excluirOrigemAction}"
													reRender="idTabelaOrigensCadastradas,pnlCentral" />
											</rich:column>
										</rich:dataTable>
									</fieldset>
								</div>
							</t:div>

							<div style="padding-top: 20px; text-align: center">
								<a4j:commandButton styleClass="BotaoPadraoEstendido"
									oncomplete="if(solicitaValidacao(#{beanProcessoIntegracao.temOrigemSelecionada},#{beanProcessoIntegracao.origemEstaIntegrada},#{beanProcessoIntegracao.verificaNumUnico},#{beanProcessoIntegracao.verificaNumOrigem})){ document.getElementById('idBotaoConfirmarEnvio').click() ;};"
									action="#" value="Confirmar Alteração"
									reRender="idTabelaOrigensCadastradas" />

								<a4j:commandButton
									oncomplete="document.getElementById('selecionarDeslocamento_lbl').click();"
									action="#{beanProcessoIntegracao.voltarAction }"
									styleClass="BotaoPadrao" value="Voltar" id="Voltar" />

								<a4j:commandButton styleClass="Botaooculto"
									id="idBotaoConfirmarEnvio"
									reRender="idTabelaOrigensCadastradas,idDeslocaProcessos"
									oncomplete="document.getElementById('selecionarDeslocamento_lbl').click();"
									action="#{beanProcessoIntegracao.alterarDeslocaProcessoAction }" />
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