<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<f:view>
	<a4j:page pageTitle="::.. Principal ..::">
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/andamento.js"></script>

		<h:form prependId="false" id="begin">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Receber Documento" />
			</jsp:include>
			<a name="ancora"></a>
			<script type="text/javascript">

				function limpaTela(){
					Richfaces.hideModalPanel('modalPanelGuia');
					document.getElementById('btnLimpaTela').onclick();
				}

				function caixaAlta(campo) {
					campo.value = campo.value.toUpperCase();
				}

				function limpar() {
					document.getElementById('btLimpar').click();
				}

				function verificaNumero(e) {
					if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
						return false;
					}
				}

				function verificaClasseDigitada(e){
					if(jq1_10_2("#idProcesso").val() == "pet"){
						var valor = "Pet";
						jq1_10_2("#idProcesso").attr('value', valor);
					}
				}

				function setarFocus(){
					document.getElementById('btnIncluirDocumento').onclick();
					document.getElementById('itDocumento').value = "";
					document.getElementById('itDocumento').focus();
				}

				jq1_10_2(document).ready(function() {
					jq1_10_2("#itAnoGuia").keypress(verificaNumero);
					jq1_10_2("#idProcesso").keypress(function(){
						verificaClasseDigitada();
					});					
					jq1_10_2("#itNumeroGuia").keyup(function() {
						verificaNumero();
						teste();
					});
				});
				
				function exibirPopup(){
					exibirMsgProcessando(false);
					if (document.getElementById('hidExibePopupGuia').value == 'S') {
						Richfaces.showModalPanel('modalPanelGuia');
					}
				}
				
				function focoOrigemOuProcessoPeticao(){
					if (document.getElementById('chkRemessa:0').checked){
						document.getElementById('itDescricaoOrig').focus();
					} else {
						document.getElementById('itDocumento').focus();
					} 
						
				}
			</script>
		</h:form>

		<a4j:form id="form" prependId="false">

			<h:inputHidden value="#{beanReceberExterno.sucesso}" id="hidSucesso" />
			<h:inputHidden value="#{beanReceberExterno.exibePopupGuia}" id="hidExibePopupGuia" />

			<h:panelGrid columns="2" id="pnlPesquisa">
				<t:panelGroup>
					<h:outputLabel styleClass="Padrao" value="Recebimento de: " />
					<h:panelGrid columns="1" styleClass="Moldura">
						<h:selectOneRadio value="#{beanReceberExterno.chkTipoOrigem}" layout="pageDirection" 
							onchange="ga('send', 'event', 'Deslocamento - Receber - #{beanUsuario.usuario.setor.sigla}', 'Setor do STF/Carga de autos/Orgao externo - Radiobutton', 'this.value' )"
							id="chkRemessa">
							<f:selectItem itemValue="SET" itemLabel="Setor do STF"/>
							<f:selectItem itemValue="ADV" itemLabel="Carga de autos" />
							<f:selectItem itemValue="ORG" itemLabel="Órgão externo" />
							<a4j:support event="onclick" actionListener="#{beanReceberExterno.atualizaDadosPesquisaAction}" reRender="panelPesquisar, pnlPesquisa, begin"
							  oncomplete="focoOrigemOuProcessoPeticao()" />
						</h:selectOneRadio>
					</h:panelGrid>
				</t:panelGroup>

				<h:panelGrid columns="1" id="panelPesquisar">
					<h:panelGrid columns="4" id="panelPesquisaOrigem">
						<h:outputText styleClass="Padrao" value="#{beanReceberExterno.descricaoOrigemRecebimento}:" />
						<h:inputText id="itDescricaoOrig" size="80" value="#{beanReceberExterno.descricaoOrigem}" 
							onblur="ga('send', 'event', 'Deslocamento - Receber - #{beanUsuario.usuario.setor.sigla}', 'Setor do STF - Setor do STF', '#{beanUsuario.usuario.username}' )"
							title="#{beanReceberExterno.hintDestino}" 
							onchange="if ( this.value!='' ) { #{rich:component('sbOrigem')}.callSuggestion(true) }" />

						<rich:suggestionbox id="sbOrigem" height="200" width="500" for="itDescricaoOrig" suggestionAction="#{beanReceberExterno.pesquisarDestinatario}"
							var="origem" nothingLabel="Nenhum registro encontrado." ignoreDupResponses="true" eventsQueue="ajaxQueue" minChars="1">
							<h:column>
								<h:graphicImage value="#{origem.urlIcone}" id="icone"
									style="vertical-align: middle;" />
								<h:outputText
									value="#{origem.origemDestino.id} - #{origem.origemDestino.descricao}" />
							</h:column>

							<a4j:support ajaxSingle="true" event="onselect" eventsQueue="ajaxQueue" ignoreDupResponses="true" oncomplete="document.getElementById('itDocumento').focus();">
								<f:setPropertyActionListener value="#{origem.origemDestino.id}" target="#{beanReceberExterno.codigoOrigem}" />
								<f:setPropertyActionListener value="#{origem.origemDestino.id} - #{origem.origemDestino.descricao}" target="#{beanReceberExterno.descricaoOrigem}"/>
							</a4j:support>
						</rich:suggestionbox>

						<a4j:commandButton styleClass="BotaoPadrao" id="btnPesquisar" size="100" value="Pesquisar" 
							onblur="ga('send', 'event', 'Deslocamento - Receber - #{beanUsuario.usuario.setor.sigla}', 'Setor do STF - Pesquisar setor (botão)', '#{beanUsuario.usuario.username}' )"
							actionListener="#{beanReceberExterno.pesquisarAction}"
							onclick="exibirMsgProcessando(true);" oncomplete="exibirMsgProcessando(false);" reRender="pnlTabelas,itDescricaoOrig, panelPesquisaOrigem"/>
					</h:panelGrid>

					<t:panelGroup rendered="#{beanReceberExterno.isSetorStf}" id="panelGuia">
						<h:outputLabel styleClass="Padrao" value="Número da Guia: " />
						<h:inputText id="itNumeroGuia" value="#{beanReceberExterno.numGuia}" size="15" maxlength="10"
							 onblur="ga('send', 'event', 'Deslocamento - Receber - #{beanUsuario.usuario.setor.sigla}', 'Setor do STF - Número de guia', '#{beanUsuario.usuario.username}' )"
							 />
						<h:outputLabel styleClass="Padrao" value="Ano: " />
						<h:inputText id="itAnoGuia" value="#{beanReceberExterno.anoGuia}" maxlength="4" size="5" 
							onblur="ga('send', 'event', 'Deslocamento - Receber - #{beanUsuario.usuario.setor.sigla}', 'Setor do STF - Número de guia', '#{beanUsuario.usuario.username}' )"
							/>
					</t:panelGroup>

					<h:panelGrid columns="4" id="panelPesquisaProcesso" rendered="#{beanReceberExterno.isOutrosSetores}">
						<h:outputText styleClass="Padrao" value="Processo/Petição:" id="descricaoTipoGuia" />
						<h:inputText style="margin-left:10px;" id="itDocumento" 
							onblur="ga('send', 'event', 'Deslocamento - Receber - #{beanUsuario.usuario.setor.sigla}', 'Carga de autos - Processo/Petição', '#{beanUsuario.usuario.username}' )"
							size="30" value="#{beanReceberExterno.identificacaoProcesso}" /> 
						<rich:hotKey selector="#itDocumento" key="return" handler="setarFocus();"/>
						
						<a4j:commandButton styleClass="BotaoMais" id="btnIncluirDocumento" 
							actionListener="#{beanReceberExterno.insereProcessoParaRecebimentoAction}" onclick="exibirMsgProcessando(true);"
							onblur="ga('send', 'event', 'Deslocamento - Receber - #{beanUsuario.usuario.setor.sigla}', 'Carga de autos - Botão +', '#{beanUsuario.usuario.username}' )"
							oncomplete="exibirMsgProcessando(false); document.getElementById('itDocumento').value = ''; if(#{beanReceberExterno.enableModalPanel}){#{rich:component('modalPanelLimpaListaProcesso')}.show()};
							            document.getElementById('itDocumento').select(); document.getElementById('itDocumento').focus();"
							reRender="itDescricaoOrig, pnlPesquisa, itDocumento" />
						<rich:toolTip for="itDocumento" style="width:350px; height:45px;" layout="block"
						              value="Formatos permitidos: [SIGLA]+[NUMERO] para processos ou [NUMERO]+[ANO] para petições"/>
					</h:panelGrid>
				</h:panelGrid>
			</h:panelGrid>
		</a4j:form>

		<rich:modalPanel id="modalPanelLimpaListaProcesso" width="300" height="120">
			<f:facet name="header">
				<h:outputText value="Receber Documento" />
			</f:facet>
			
			<a4j:form prependId="false">
					<h:panelGrid style="text-align: center;">
						<h:outputText style="text-align: center;" styleClass="Padrao" value="O processo informado não pertence a mesma origem que os demais, deseja limpar a lista de processos?" />
						<h:panelGrid columns="2" style="text-align: center;">
							<h:commandButton styleClass="BotaoPadrao" value="sim" action="#{beanReceberExterno.insereProcessoListaAlterada}">
							</h:commandButton>
							<h:commandButton styleClass="BotaoPadrao" value="não" actionListener="#{beanReceberExterno.visualizaTabelaProcesso}" onclick="Richfaces.hideModalPanel('modalPanelLimpaListaProcesso');"/>
						</h:panelGrid>
					</h:panelGrid>
				</a4j:form>
		</rich:modalPanel>

		<a4j:form id="formResultado">
		
			<!-- botão oculto limpar pesquisa -->
			<a4j:commandButton styleClass="BotaoOculto" id="btnLimpaTela" 
				    action="#{beanReceberExterno.limparPesquisa}" reRender="hidSucesso, form, pnlTabelas" />
			
			<a4j:outputPanel ajaxRendered="true" keepTransient="false" id="pnlTabelas" styleClass="MolduraInterna">
				<c:if test="${not empty beanReceberExterno.listaDocumentos}">
				
					<!--  os botões no topo da tabela somente serão apresentados quando a pesquisa retornar mais de 50 registros -->
					<!--  botão para recebimento -->
					<a4j:commandButton styleClass="BotaoEstendido" value="Receber Processos selecionados" id="btnRecebeProcessoTop"
					    actionListener="#{beanReceberExterno.salvarRecebimentoDocumentos}" onclick="exibirMsgProcessando(true);" 
						reRender="hidSucesso, pnlTabelas" size="50" oncomplete="exibirMsgProcessando(false); if(#{beanReceberExterno.enablePanelRecebimentoProcesso}){#{rich:component('modalPanelGuia')}.show()};"
						rendered="#{beanReceberExterno.totalLinha > 50}" />
					<!-- limpar pesquisa -->
					<a4j:commandButton styleClass="BotaoEstendido" value="Limpar pesquisa" id="btnLimpaTop" 
					    action="#{beanReceberExterno.limparPesquisa}" reRender="hidSucesso, form, pnlTabelas" rendered="#{beanReceberExterno.totalLinha > 50}" />

					<hr color="red" align="left" size="1px" width="100%" />
					<rich:dataTable headerClass="DataTableDefaultHeader" styleClass="DataTableDefault" footerClass="DataTableDefaultFooter"
						rowClasses="DataTableRow, DataTableRow2" columnClasses="tres, tres, seteCenter, seteCenter, tres" sortMode="single"
						value="#{beanReceberExterno.listaDocumentos}" var="wrappedGuia" binding="#{beanReceberExterno.tabelaDocumentos}" id="tabelaProcesso">

						<rich:column>
							<f:facet name="header">
								<a4j:commandButton image="../../images/setabaixo.gif" actionListener="#{beanReceberExterno.marcarTodosDocumentos}"
												   disabled="true" />
							</f:facet>
							<h:selectBooleanCheckbox value="#{wrappedGuia.checked}" disabled="true" />
						</rich:column>

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Processo" />
							</f:facet>

							<h:graphicImage rendered="#{beanReceberExterno.isApenso}"
										    value="../../images/seta.png" id="iconeApenso"
										    style="vertical-align: middle;" />
							
							<h:outputText value="#{wrappedGuia.wrappedObject.classeProcesso}" />
							<h:outputText value=" " />
							<h:outputText value="#{wrappedGuia.wrappedObject.numeroProcesso}">
							</h:outputText>
						</rich:column>

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Ministro Relator" />
							</f:facet>
							<h:outputText value="#{wrappedGuia.wrappedObject.id.processo.ministroRelatorAtual.nome}" />
						</rich:column>

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Qtde Volume" />
							</f:facet>
							<h:outputText value="#{wrappedGuia.wrappedObject.id.processo.quantidadeVolumes}">
							</h:outputText>
						</rich:column>

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Qtde Juntada por Linha" />
							</f:facet>
							<h:outputText value="#{wrappedGuia.wrappedObject.id.processo.quantidadeJuntadasLinha}">
							</h:outputText>
						</rich:column>

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Qtde Apensos" />
							</f:facet>
							<h:outputText value="#{wrappedGuia.wrappedObject.id.processo.quantidadeApensosFixo}">
							</h:outputText>
						</rich:column>

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Qtde Vinculado" />
							</f:facet>
							<h:outputText value="#{wrappedGuia.wrappedObject.quantidadeApensos}">
							</h:outputText>
						</rich:column>
						
						<rich:column>
							<f:facet name="header">
								<h:outputText value="Órgão Externo" />
							</f:facet>
								<h:outputText value="#{beanReceberExterno.descricaoOrigemExterna}">
							</h:outputText>
						</rich:column>						

					</rich:dataTable>
					<br />
					<a4j:commandButton styleClass="BotaoEstendido" value="Receber Processos selecionados"  id="btnReceberProcessosSelecionados"
					    actionListener="#{beanReceberExterno.salvarRecebimentoDeVariasOrigens}" onclick="exibirMsgProcessando(true);"
						reRender="hidSucesso,pnlTabelas" size="50" oncomplete="exibirMsgProcessando(false); if(#{beanReceberExterno.enablePanelRecebimentoProcesso}){#{rich:component('modalPanelGuia')}.show()};"/>
					<a4j:commandButton styleClass="BotaoEstendido" value="Limpar pesquisa" id="btnLimpaProcesso" 
					    action="#{beanReceberExterno.limparPesquisa}" reRender="hidSucesso, form, pnlTabelas" />
				</c:if>

				<c:if test="${not empty beanReceberExterno.listaGuia}">

					<!-- botões somente serão apresentados no início da tabela quando a pesquisas retornar mais de 50 linhas -->
					<!-- recebe guia -->
					<a4j:commandButton styleClass="BotaoPadraoEstendido" value="Receber Guia" actionListener="#{beanReceberExterno.salvarRecebimentoGuia}"
						reRender="hidSucesso, form" rendered="#{beanReceberExterno.totalLinha > 50}" />
					<!-- limpar pesquisa -->
					<a4j:commandButton styleClass="BotaoEstendido" value="Limpar pesquisa" id="btnLimpaGuiaTop" 
					    action="#{beanReceberExterno.limparPesquisa}" reRender="hidSucesso, form, pnlTabelas" rendered="#{beanReceberExterno.totalLinha > 50}"/>

					<hr color="red" align="left" size="1px" width="100%" />
					<rich:dataTable headerClass="DataTableDefaultHeader" styleClass="DataTableDefault" footerClass="DataTableDefaultFooter"
						rowClasses="DataTableRow, DataTableRow2" columnClasses="tres, tres, seteCenter, seteCenter, tres, tres, tres, tres, tres, tres" value="#{beanReceberExterno.listaGuia}"
						var="wrappedGuia" binding="#{beanReceberExterno.tabelaGuias}" id="tabelaGuia">

						<rich:column>
							<f:facet name="header">
								<a4j:commandButton image="../../images/setabaixo.gif" actionListener="#{beanReceberExterno.marcarTodosDocumentos}"
												   disabled="#{beanReceberExterno.desabilitarMarcarTodos}" />
							</f:facet>
							<h:selectBooleanCheckbox value="#{wrappedGuia.checked}" disabled="true" />
						</rich:column>

						<rich:column rendered="#{beanReceberExterno.pesquisaGuiaProcesso}">
							<f:facet name="header">
								<h:outputText value="Processo" />
							</f:facet>
							<h:outputText value="#{wrappedGuia.wrappedObject.classeProcesso}" />
							<h:outputText value=" " />
							<h:outputText value="#{wrappedGuia.wrappedObject.numeroProcesso}">
							</h:outputText>
						</rich:column>

						<rich:column rendered="#{beanReceberExterno.pesquisaGuiaProcesso}">
							<f:facet name="header">
								<h:outputText value="Ministro Relator" />
							</f:facet>
							<h:outputText value="#{wrappedGuia.wrappedObject.id.processo.ministroRelatorAtual.nome}" />
						</rich:column>

						<rich:column rendered="#{beanReceberExterno.pesquisaGuiaProcesso}">
							<f:facet name="header">
								<h:outputText value="Qtde Volume" />
							</f:facet>
							<h:outputText value="#{wrappedGuia.wrappedObject.id.processo.quantidadeVolumes}">
							</h:outputText>
						</rich:column>

						<rich:column rendered="#{beanReceberExterno.pesquisaGuiaProcesso}">
							<f:facet name="header">
								<h:outputText value="Qtde Juntada por Linha" />
							</f:facet>
							<h:outputText value="#{wrappedGuia.wrappedObject.id.processo.quantidadeJuntadasLinha}">
							</h:outputText>
						</rich:column>

						<rich:column rendered="#{beanReceberExterno.pesquisaGuiaProcesso}">
							<f:facet name="header">
								<h:outputText value="Qtde Apensos" />
							</f:facet>
							<h:outputText value="#{wrappedGuia.wrappedObject.id.processo.quantidadeApensosFixo}">
							</h:outputText>
						</rich:column>

						<rich:column rendered="#{beanReceberExterno.pesquisaGuiaProcesso}">
							<f:facet name="header">
								<h:outputText value="Qtde Vinculado" />
							</f:facet>
							<h:outputText value="#{wrappedGuia.wrappedObject.quantidadeApensos}">
							</h:outputText>
						</rich:column>


						<rich:column rendered="#{beanReceberExterno.pesquisaGuiaPeticao}">
							<f:facet name="header">
								<h:outputText value="Petição" />
							</f:facet>
							<h:outputText value="#{wrappedGuia.wrappedObject.numeroPeticao} / #{wrappedGuia.wrappedObject.anoPeticao}" />
						</rich:column>
						
						<rich:column rendered="#{beanReceberExterno.pesquisaGuiaPeticao}">
							<f:facet name="header">
								<h:outputText value="Processo" />
							</f:facet>
							<h:outputText value="#{beanReceberExterno.processoPrincipalGuiaPeticao}" />
						</rich:column>

						<rich:column sortOrder="DESCENDING" selfSorted="false" sortBy="#{wrappedGuia.wrappedObject.guia.descricaoDestino}" rendered="#{beanReceberExterno.pesquisaGuiaPeticao}">
							<f:facet name="header">
								<h:outputText value="Origem" />
							</f:facet>
							<h:outputText value="#{wrappedGuia.wrappedObject.guia.descricaoDestino}" />
						</rich:column>

					</rich:dataTable>
					<br />
					<a4j:commandButton styleClass="BotaoPadraoEstendido" value="Receber Guia" actionListener="#{beanReceberExterno.salvarRecebimentoGuia}" onclick="exibirMsgProcessando(true);"
						reRender="hidSucesso,form,hidExibePopupGuia" oncomplete="exibirPopup()"/>
					<a4j:commandButton styleClass="BotaoEstendido" value="Limpar pesquisa" id="btnLimpaGuiaFim" 
					    action="#{beanReceberExterno.limparPesquisa}" reRender="hidSucesso, form, pnlTabelas" />
				</c:if>
			</a4j:outputPanel>
			<a4j:commandButton styleClass="BotaoOculto" id="btnLimparSetorOrigem" actionListener="#{beanReceberDocumento.limparOrigemPesquisaAction}" />
		</a4j:form>
		
		<!-- popup para impressão da guia gerada no deslocamento -->

		<rich:modalPanel id="modalPanelGuia" width="350" height="100" keepVisualState="true" style="overflow:auto">
			<f:facet name="header">
				<h:outputText value="Visualizar guia(s) recebida(s)" />
			</f:facet>
			<a4j:form id="formImpressaoGuia" prependId="false">
				</br></br>
				<h:commandLink value="Visualizar Impressão" id="abrePdfGuia" 
				     		    action="#{beanReceberExterno.gerarPDFGuiaInLine}" target="_blank" />&nbsp;&nbsp;&nbsp;
				
				<a4j:commandButton value="Fechar" styleClass="BotaoPadrao" actionListener="#{beanReceberExterno.limparPesquisaAction}" 
								 onclick="Richfaces.hideModalPanel('modalPanelGuia');document.getElementById('itDocumento').focus();" 
								 id="popup_btnFecharImpressao" 
								 reRender="btnReceberProcessosSelecionados"/>
			</a4j:form>
		</rich:modalPanel>
		
		<jsp:include page="/pages/template/footer.jsp" flush="true" />
	</a4j:page>
</f:view>