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

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Pesquisar Guia de Deslocamento" />
			</jsp:include>
			<a name="ancora"></a>
			<script type="text/javascript">

				function caixaAlta(campo) {
					campo.value = campo.value.toUpperCase();
				}

				function limpar() {
					document.getElementById('btLimpar').click();
				}
				
				function limpaOrigem() {
					document.getElementById('itDescricaoOrig').value = '';
					document.getElementById('btnLimparSetorOrigem').onclick();
				}
				
				function limpaDestino() {
					document.getElementById('itDescricaoDestinatario').value = '';
					document.getElementById('btnLimparSetorDestino').onclick();
				}
				
				function trataCampoDestino() {
					 // impedir a digitação de novos destinos quando o checkbox do 'Utilizar meu setor' estiver marcado.
					 if (document.getElementById('chkDestinoLotacaoUsuario').checked) {
						 document.getElementById('itDescricaoDestinatario').disabled = true;
					 } else {
						 document.getElementById('itDescricaoDestinatario').disabled = false;
					 }
				}
				
				function trataCampoOrigem() {
					 // impedir a digitação de novos destinos quando o checkbox do 'Utilizar meu setor' estiver marcado.
					 if (document.getElementById('chkOrigemLotacaoUsuario').checked) {
						 document.getElementById('itDescricaoOrig').disabled = true;
					 } else {
						 document.getElementById('itDescricaoOrig').disabled = false;
					 }
				}
				
				function limparCampo(campo) {
					document.getElementById(campo).value = '';
				}

				function manterFoco(campo){
					document.getElementById(campo).focus();
				}
				
				function confirmaExclusaoGuia(){
					if (confirm("Deseja exlcuir esta guia?")) {
						document.getElementById('hidRespExcluirGuia').value="S";
						exibirMsgProcessando(true);
					} else {
						document.getElementById('hidRespExcluirGuia').value="N";
					}
				}

			</script>
		</h:form>

		<a4j:form id="form" prependId="false">
			<h:panelGrid columns="2" id="pnlPesquisa" columnClasses="PainelTop, PainelTop">

				<t:panelGroup>
					<h:outputLabel styleClass="Padrao" value="Número da Guia: "></h:outputLabel>
					<h:inputText id="itNumeroGuia" value="#{beanPesquisarGuia.numGuia}" size="15" 
						onblur="ga('send', 'event', 'Deslocamento - Pesquisa - #{beanUsuario.usuario.setor.sigla}', 'Número da guia', '#{beanUsuario.usuario.username}' )"
						/>
					<h:outputLabel styleClass="Padrao" value="Ano: "></h:outputLabel>
					<h:inputText id="itAnoGuia" value="#{beanPesquisarGuia.anoGuia}" 
						onblur="ga('send', 'event', 'Deslocamento - Pesquisa - #{beanUsuario.usuario.setor.sigla}', 'Ano', '#{beanUsuario.usuario.username}' )"
						requiredMessage="Digite apenas 4 números no campo 'Ano:' " size="10">
						<f:validateLength maximum="4" />
					</h:inputText>
				</t:panelGroup>

				<t:panelGroup>
					<h:outputLabel styleClass="Padrao" value="Data do envio: "></h:outputLabel>
					<rich:calendar id="itDataEnvio" value="#{beanPesquisarGuia.dataEnvio}" 
						ondateselect="ga('send', 'event', 'Deslocamento - Pesquisa - #{beanUsuario.usuario.setor.sigla}', 'Data do envio', '#{beanUsuario.usuario.username}' )"
						datePattern="dd/MM/yyyy" locale="pt_Br" />
				</t:panelGroup>

				<h:panelGrid columns="1">
					<t:panelGroup>
						<div style="padding-top: 10px;" id="divDescricaoTipoGuia">
							<h:outputText styleClass="Padrao" value="Processo/Petição:" id="descricaoTipoGuia" />

							<h:inputText style="margin-left:10px;" id="idProcessoPeticao"  
								onblur="ga('send', 'event', 'Deslocamento - Pesquisa - #{beanUsuario.usuario.setor.sigla}', 'Processo/Petição', '#{beanUsuario.usuario.username}' )"
								value="#{beanPesquisarGuia.identificacaoProcessoPeticao}"/>
						</div>
					</t:panelGroup>
				</h:panelGrid>
			</h:panelGrid>

			<h:panelGrid columns="1">

				<t:panelGroup>
					<h:panelGrid columns="4">
						<h:outputText styleClass="Padrao" value=" Origem:" />
						<h:inputText id="itDescricaoOrig" size="100" value="#{beanPesquisarGuia.descricaoOrigem}" 
							onblur="ga('send', 'event', 'Deslocamento - Pesquisa - #{beanUsuario.usuario.setor.sigla}', 'Origem', '#{beanUsuario.usuario.username}' )"
							onclick="javascript:this.value='';" 
							onchange="if ( this.value!='' ) { #{rich:component('sbOrigem')}.callSuggestion(true) }" />

						<rich:suggestionbox id="sbOrigem" height="200" width="500" for="itDescricaoOrig" 
							suggestionAction="#{beanPesquisarGuia.pesquisarOrigemDestino}" var="origem" nothingLabel="Nenhum registro encontrado." 
							ignoreDupResponses="true" eventsQueue="ajaxQueue" minChars="1" fetchValue="#{origem.origemDestino.id} - #{origem.origemDestino.descricao}">
							<h:column>
								<h:graphicImage value="#{origem.urlIcone}" id="iconeOrigem" style="vertical-align: middle;"/>
								<h:outputText value="#{origem.origemDestino.id} - #{origem.origemDestino.descricao}" />
							</h:column>
						</rich:suggestionbox>

						<t:panelGroup>
							<h:selectBooleanCheckbox id="chkOrigemLotacaoUsuario" 
								onblur="ga('send', 'event', 'Deslocamento - Pesquisa - #{beanUsuario.usuario.setor.sigla}', 'Checkbox - Utilizar origem do setor', '#{beanUsuario.usuario.username}' )"
								value="#{beanPesquisarGuia.chkOrigemLotacaoUsuario}">
								<a4j:support ajaxSingle="true" immediate="true" event="onclick" action="#{beanPesquisarGuia.atualizarOrigemSetorUsuario}"
									reRender="itDescricaoOrig,sbOrigem, itDescricaoDestinatario, chkDestinoLotacaoUsuario" oncomplete="trataCampoOrigem()" />
							</h:selectBooleanCheckbox>
							<h:outputLabel styleClass="Padrao" value="Utilizar meu setor" for="chkOrigemLotacaoUsuario" />
						</t:panelGroup>


						<h:outputLabel styleClass="Padrao" value="Destino: "/>
						<h:inputText id="itDescricaoDestinatario" size="100" value="#{beanPesquisarGuia.descricaoDestino}" 
							onblur="ga('send', 'event', 'Deslocamento - Pesquisa - #{beanUsuario.usuario.setor.sigla}', 'Destino', '#{beanUsuario.usuario.username}' )"
							onclick="javascript:this.value='';"
							onchange="if ( this.value!='' ) { #{rich:component('sbDestino')}.callSuggestion(true) }">
							<a4j:support ajaxSingle="true" event="onchange"/>
						</h:inputText>

						<rich:suggestionbox id="sbDestino" height="200" width="500" for="itDescricaoDestinatario" 
							suggestionAction="#{beanPesquisarGuia.pesquisarOrigemDestino}" var="destino" nothingLabel="Nenhum registro encontrado." 
							ignoreDupResponses="true" eventsQueue="ajaxQueue" minChars="1" fetchValue="#{destino.origemDestino.id} - #{destino.origemDestino.descricao}">
							<h:column>
								<h:graphicImage value="#{destino.urlIcone}" id="iconeDestino" style="vertical-align: middle;"/>
								<h:outputText value="#{destino.origemDestino.id} - #{destino.origemDestino.descricao}" />
							</h:column>
						</rich:suggestionbox>

						<t:panelGroup>
							<h:selectBooleanCheckbox id="chkDestinoLotacaoUsuario" 
								onblur="ga('send', 'event', 'Deslocamento - Pesquisa - #{beanUsuario.usuario.setor.sigla}', 'Checkbox - Utilizar destino do setor', '#{beanUsuario.usuario.username}' )"
								value="#{beanPesquisarGuia.chkDestinoLotacaoUsuario}">
								<a4j:support ajaxSingle="true" immediate="true" event="onclick" action="#{beanPesquisarGuia.atualizarDestinoSetorUsuario}"
									reRender="itDescricaoDestinatario,sbDestino, itDescricaoOrig, chkOrigemLotacaoUsuario" oncomplete="trataCampoDestino()" />
							</h:selectBooleanCheckbox>
							<h:outputLabel styleClass="Padrao" value="Utilizar meu setor" for="chkDestinoLotacaoUsuario" />
						</t:panelGroup>
						
					</h:panelGrid>
				</t:panelGroup>
			</h:panelGrid>

			<h:panelGrid columns="1">
				<t:panelGroup>
					<a4j:commandButton styleClass="BotaoPadrao" id="btnPesquisar" value="Pesquisar" actionListener="#{beanPesquisarGuia.pesquisarGuiaAction}"
					onclick="exibirMsgProcessando(true);" 
					oncomplete="exibirMsgProcessando(false); limparCampo(itDescricaoOrig);limparCampo(itDescricaoDestinatario);" 
					reRender="pnlPesquisa, pnlItemGuia" />
				</t:panelGroup>
			</h:panelGrid>

		</a4j:form>

		<a4j:form id="formResultado" prependId="false" >
			<!-- Listagem de guias com o resultado da pesquisa -->
			
			<a4j:outputPanel ajaxRendered="true" keepTransient="false"
				id="pnlItemGuia" styleClass="MolduraInterna">
				<c:if test="${not empty beanPesquisarGuia.listaGuias}">
					<hr color="red" align="left" size="1px" width="90%" />
					<rich:dataTable headerClass="DataTableDefaultHeader"
						styleClass="DataTableDefault" footerClass="DataTableDefaultFooter"
						rowClasses="DataTableRow, DataTableRow2"
						columnClasses="tres, cinco, cinco, tres, seteCenter, seteCenter, tres"
						sortMode="single" value="#{beanPesquisarGuia.listaGuias}"
						var="wrappedGuia" binding="#{beanPesquisarGuia.tabelaGuias}"
						rows="10" id="tabelaGuia">


						<rich:column>
							<f:facet name="header">
								<h:outputText value="Guia" />
							</f:facet>

							<a4j:commandLink styleClass="Padrao" style="text-align:center;"
								title="Clique para alterar a guia" onclick="exibirMsgProcessando(true);"
								value="#{wrappedGuia.wrappedObject.id.numeroGuia}  / #{wrappedGuia.wrappedObject.id.anoGuia}"
								actionListener="#{beanPesquisarGuia.prepararParaEdicaoAction}" reRender="tituloPopup, pnlEditarGuia"
								oncomplete="exibirMsgProcessando(false); location.href='#top'; Richfaces.showModalPanel('popupEditarGuia');" />
						</rich:column>
						
						<rich:column sortOrder="DESCENDING" sortBy="#{wrappedGuia.wrappedObject.dataInclusao}">
							<f:facet name="header">
								<h:outputText value="Criação"/>
							</f:facet>
							<h:outputText styleClass="Padrao" style="text-align:center;" value="#{wrappedGuia.wrappedObject.dataInclusao}">
								<f:convertDateTime pattern="dd/MM/yyyy HH:mm"/>  
							</h:outputText>
						</rich:column>	
						
						<rich:column>
							<f:facet name="header">
								<h:outputText value="Recebimento"/>
							</f:facet>
							<h:outputText styleClass="Padrao" style="text-align:center;"
								value="#{wrappedGuia.wrappedObject.dataRecebimento}" />
						</rich:column>							

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Qtde Itens" />
							</f:facet>
							<h:outputText styleClass="Padrao" style="text-align:center;"
								value="#{wrappedGuia.wrappedObject.quantidadeInternaProcesso} #{beanPesquisarGuia.descricaoQuantidadeItens}" />
						</rich:column>

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Origem" />
							</f:facet>
							<h:outputText styleClass="Padrao" style="text-align:center;"
								value="#{wrappedGuia.wrappedObject.descricaoOrigem}" />
						</rich:column>

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Destino" />
							</f:facet>
							<h:outputText styleClass="Padrao" style="text-align:center;"
								value="#{wrappedGuia.wrappedObject.descricaoDestino}" />
						</rich:column>

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Situação" />
							</f:facet>
							<h:outputText styleClass="Padrao" style="text-align:center;" 
							    value="#{wrappedGuia.wrappedObject.situacao}" />

						</rich:column>

						<rich:column>
							<f:facet name="header">
								<h:outputText value="Receber" />
							</f:facet>
							<a4j:commandButton image="../../images/receber.gif" actionListener="#{beanPesquisarGuia.receberGuia}" rendered="#{beanPesquisarGuia.podeReceberGuia}"
								reRender="pnlItemGuia" />
						</rich:column>

					</rich:dataTable>
					<rich:datascroller for="tabelaGuia" maxPages="10">
						<f:facet name="first">
							<h:outputText value="Primeira" />
						</f:facet>
						<f:facet name="last">
							<h:outputText value="Ultima" />
						</f:facet>
					</rich:datascroller>
				</c:if>
			</a4j:outputPanel>
			
			<a4j:commandButton styleClass="BotaoOculto" id="btnAtualizarSetorPesquisa" actionListener="#{beanPesquisarGuia.atualizarDestinoPesquisaAction}" />
			<a4j:commandButton styleClass="BotaoOculto" id="btnLimparSetorDestino" actionListener="#{beanPesquisarGuia.limparDestinoPesquisaAction}" reRender="itDescricaoOrig, chkOrigemLotacaoUsuario" />
			<a4j:commandButton styleClass="BotaoOculto" id="btnLimparSetorOrigem" actionListener="#{beanPesquisarGuia.limparOrigemPesquisaAction}" reRender="itDescricaoDestinatario, chkDestinoLotacaoUsuario" />

		</a4j:form>

		<jsp:include page="/pages/template/footer.jsp" flush="true" />
		
		<!-- janela para alteração/detalhamento de uma guia -->
		<rich:modalPanel id="popupEditarGuia" width="900" height="510" binding="#{beanPesquisarGuia.popupEditarGuia}" 
			keepVisualState="true" style="overflow:auto">
			<f:facet name="header">
				<h:outputText value="Guia de Deslocamento de #{beanPesquisarGuia.descricaoGuia}" id="tituloPopup"  />
			</f:facet>
			<a4j:form prependId="false" id="formPopup" >
				<h:inputHidden value="#{beanPesquisarGuia.respostaExcluirGuia}" id="hidRespExcluirGuia" />
				<h:panelGrid id="pnlEditarGuia" styleClass="MolduraInterna">
					<h:panelGrid id="mensagensPopup" styleClass="MolduraInterna">
						<h:panelGroup id="gridMessageAviso" rendered="#{beanPesquisarGuia.temMensagemAvisoPopup}" 
						             styleClass="WarningMessage" >
							<h:outputText id="lableMsgAviso" value="#{beanPesquisarGuia.mensagemAvisoPopup}" />
							<br/>
						</h:panelGroup>
						<h:panelGroup id="gridMessageInformacao" rendered="#{beanPesquisarGuia.temMensagemInfoPopup}" 
						             styleClass="InfoMessage" >
							<h:outputText id="lableMsgInformacao" value="#{beanPesquisarGuia.mensagemInfoPopup}" />
							<br/>
						</h:panelGroup>
						<h:panelGroup id="gridMessageErro" rendered="#{beanPesquisarGuia.temMensagemErroPopup}" 
						             styleClass="ErrorMessage" >
							<h:outputText id="lableMsgErro" value="#{beanPesquisarGuia.mensagemErroPopup}" />
							<br/>
						</h:panelGroup>
					</h:panelGrid>
					<t:panelGroup>
						<h:panelGrid columns="2"> 
							<h:outputText styleClass="Padrao" value="Guia: "/>
							<h:outputText style="color: #3F3FB6" styleClass="Padrao" value="#{beanPesquisarGuia.numeroGuiaPopup} / #{beanPesquisarGuia.anoGuiaPopup}"/>
						</h:panelGrid>
						<h:panelGrid columns="2"> 
							<h:outputText styleClass="Padrao" value="Situação: "/>
							<h:outputText style="color: #3F3FB6" styleClass="Padrao" value="#{beanPesquisarGuia.situacaoGuiaPopup}"/>
						</h:panelGrid>
						<h:panelGrid columns="2"> 
							<h:outputText styleClass="Padrao" value="Origem: "/>
							<h:outputText style="color: #3F3FB6" styleClass="Padrao" value="#{beanPesquisarGuia.codigoOrigemPopup} - #{beanPesquisarGuia.descricaoOrigemPopup}"/>
						</h:panelGrid>
						<h:panelGrid columns="2"> 
							<h:outputText styleClass="Padrao" value="Destino: "/>
							<h:outputText style="color: #3F3FB6" styleClass="Padrao" value="#{beanPesquisarGuia.codigoDestinoPopup} - #{beanPesquisarGuia.descricaoDestinoPopup}"/>
						</h:panelGrid>
						<h:panelGrid columns="2">
							<h:outputText styleClass="Padrao" value="Novo Destino: "/>
							<h:inputText id="itNovoDestinoPopup" size="100" value="#{beanPesquisarGuia.descricaoNovoDestinoPopup}" onclick="javascript:this.value = '' "
								onchange="if ( this.value!='' ) { #{rich:component('sbNovoDestinoGuia')}.callSuggestion(true) }" 
								disabled="#{beanPesquisarGuia.desabilitarAlteracaoDestino}"	/>
	
							<rich:suggestionbox id="sbNovoDestinoGuia" height="200" width="500"
								for="itNovoDestinoPopup"
								suggestionAction="#{beanPesquisarGuia.pesquisarDestinatario}"
								var="novoDestino" nothingLabel="Nenhum registro encontrado">
								<h:column>
									<h:graphicImage value="#{novoDestino.urlIcone}" id="icone"
										style="vertical-align: middle;" />
									<h:outputText
										value="#{novoDestino.origemDestino.id} - #{novoDestino.origemDestino.descricao}" />
								</h:column>
			
								<a4j:support ajaxSingle="true" event="onselect" eventsQueue="ajaxQueue" ignoreDupResponses="true">
									<f:setPropertyActionListener value="#{novoDestino.origemDestino.id}"
										target="#{beanPesquisarGuia.codigoNovoDestinoPopup}" />
									<f:setPropertyActionListener value="#{novoDestino.origemDestino.tipoOrigemDestino}"
										target="#{beanPesquisarGuia.tipoNovoDestinoPopup}" />
									<f:setPropertyActionListener value="#{novoDestino.origemDestino.descricao}"
										target="#{beanPesquisarGuia.descricaoNovoDestinoPopup}" />
								</a4j:support>
							</rich:suggestionbox>
							
						</h:panelGrid>
						<h:panelGrid columns="1" id="gridObservacao" columnClasses="PainelTop">
								<t:panelGroup>
									<h:outputLabel styleClass="Padrao" style="vertical-align:top" value="Observação: " id="labelEditarObservacao" for="itEditarObservacao" />
									<h:inputTextarea id="itEditarObservacao" rows="2" cols="100" 
									             value="#{beanPesquisarGuia.observacaoPopup}" />
								</t:panelGroup>
						</h:panelGrid>
						
						<h:panelGrid columns="1" id="botoesPopup">
							<t:panelGroup>
								<a4j:commandButton styleClass="BotaoPadrao" id="btnSalvar" value="Salvar" 
								    actionListener="#{beanPesquisarGuia.atualizarGuia}"
									onclick="exibirMsgProcessando(true);" oncomplete="exibirMsgProcessando(false);" reRender="formResultado, formPopup"/>
								
								<a4j:commandButton styleClass="BotaoOculto" id="btnExcluir" value="Excluir" 
								    actionListener="#{beanPesquisarGuia.excluirGuia}"  onclick="confirmaExclusaoGuia()"
									oncomplete="exibirMsgProcessando(false);" reRender="formResultado, formPopup"/>
								
								<a4j:commandButton styleClass="BotaoPadrao" id="btnFechar" value="Fechar" ajaxSingle="true" 
								    actionListener="#{beanPesquisarGuia.limparPopupAction}" 
								    oncomplete="Richfaces.hideModalPanel('popupEditarGuia');"/>&nbsp;&nbsp;
								    
								<h:commandButton styleClass="BotaoPadrao" id="btnImprimir" action="#{beanPesquisarGuia.imprimirGuia}" value="Imprimir"/>
								<span class="Padrao" style="padding-left: 5px;">
							        	<h:outputLabel value="Postal: " for="chkPostal"></h:outputLabel>
								        <h:selectBooleanCheckbox id="chkPostal" disabled="#{beanPesquisarGuia.desabilitarPostal}" value="#{beanPesquisarGuia.postal}"/>
								</span>
							</t:panelGroup>
						</h:panelGrid>
						
						<h:panelGrid columns="1" rendered="#{beanPesquisarGuia.temPermissaoAlterar}">
							<t:div>
								<span class="Padrao">
									<h:outputText styleClass="Padrao" value="#{beanPesquisarGuia.descricaoGuia}: " id="lableProcessoPeticao" />
									<h:inputText id="itDocumento" size="30" value="#{beanPesquisarGuia.idProcessoPeticaoPopup}" />
								</span>
								<a4j:commandButton styleClass="BotaoMais" id="btnIncluirDocumento"
								title="Inclui #{beanPesquisarGuia.descricaoGuia} na guia."
								actionListener="#{beanPesquisarGuia.inserirProcessoPeticaoAction}"
								onclick="exibirMsgProcessando(true);" 
								oncomplete="limparCampo('itDocumento');exibirMsgProcessando(false);manterFoco('itDocumento');"
								reRender="formPopup"/>
							</t:div>
						</h:panelGrid>
						
						<!--  listagem de processos/petições da guia -->
						<c:if test="${not empty beanPesquisarGuia.listaProcessosDaGuia}">
							<!-- Se for processo -->
							<rich:dataTable id="tabProcessos"
								value="#{beanPesquisarGuia.listaProcessosDaGuia}"
								var="deslocaProcesso" headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault" footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2" rows="10"
								binding="#{beanPesquisarGuia.tabelaProcessos}"
								columnClasses="tres, dezCenter, tres, tres, tres, tres"
								sortMode="single">

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Processo" />
									</f:facet>
									
									<h:graphicImage rendered="#{beanPesquisarGuia.isApenso}"
										value="../../images/seta.png" id="iconeApenso"
										style="vertical-align: middle;" />
										
									<h:outputText rendered="#{deslocaProcesso.id.processo.eletronico}" value="e"	style="color: red; font-weight: bold;" />
									<h:commandLink styleClass="PadraoLink"
										style="text-align:center;" target="_blank"
										value="#{deslocaProcesso.id.processo.siglaClasseProcessual} #{deslocaProcesso.id.processo.numeroProcessual}"
										action="#{beanPesquisarGuia.consultarObjetoIncidenteSupremo}">
										<f:setPropertyActionListener
											target="#{beanPesquisarGuia.seqObjetoIncidente}"
											value="#{deslocaProcesso.id.processo.id}" />
									</h:commandLink>
									<!--<h:outputText style="text-align:left;"
										value="#{deslocaProcesso.id.processo.siglaClasseProcessual} #{deslocaProcesso.id.processo.numeroProcessual}">
									</h:outputText> -->
								</rich:column>
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Ministro Relator" />
									</f:facet>
									<h:outputText
										value="#{deslocaProcesso.nomeMinistroRelatorAtual}"/>
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Qtde Volume" />
									</f:facet>
									<h:outputText
										value="#{deslocaProcesso.id.processo.quantidadeVolumes}">
									</h:outputText>
								</rich:column>
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Qtde Apensos" />
									</f:facet>
									<h:outputText
										value="#{deslocaProcesso.id.processo.quantidadeApensosFixo}">
									</h:outputText>
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Qtde Juntada por Linha" />
									</f:facet>
									<h:outputText
										value="#{deslocaProcesso.id.processo.quantidadeJuntadasLinha}">
									</h:outputText>
								</rich:column>
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Qtde Vinculado" />
									</f:facet>
									<h:outputText
										value="#{beanPesquisarGuia.quantidadeVinculados}">
									</h:outputText>
								</rich:column>
							
								<rich:column rendered="#{beanPesquisarGuia.temPermissaoAlterar}">
									<f:facet name="header">
										<h:outputText value="Ação" />
									</f:facet>
									<a4j:commandLink value="Remover" actionListener="#{beanPesquisarGuia.removerItemProcessoAction}"
										reRender="formPopup, formResultado" onclick="exibirMsgProcessando(true)"  
										oncomplete="exibirMsgProcessando(false)">
									</a4j:commandLink>
								</rich:column>
							</rich:dataTable>
							<rich:datascroller for="tabProcessos" maxPages="10">
								<f:facet name="first">
									<h:outputText value="Primeira" />
								</f:facet>
								<f:facet name="last">
									<h:outputText value="Última" />
								</f:facet>
							</rich:datascroller>
						</c:if>

						<!-- verifica se é peticao -->
						<c:if test="${not empty beanPesquisarGuia.listaPeticoesDaGuia}">
							<!-- peticao -->
							<rich:dataTable id="tbPeticoes"
								value="#{beanPesquisarGuia.listaPeticoesDaGuia}"
								var="deslocaPeticao" headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault" rows="10"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								binding="#{beanPesquisarGuia.tabelaPeticoes}"
								columnClasses="tres, tres, tres"
								sortMode="single">

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Petiçao" />
									</f:facet>
									<h:outputText
										value="#{deslocaPeticao.id.peticao.numeroPeticao} / #{deslocaPeticao.id.peticao.anoPeticao}"/>
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Processo" />
									</f:facet>
									<h:outputText value="#{deslocaPeticao.id.peticao.objetoIncidenteVinculado.principal.identificacao}"/>
								</rich:column>
								
								<rich:column rendered="#{beanPesquisarGuia.temPermissaoAlterar}">
									<f:facet name="header">
										<h:outputText value="Ação" />
									</f:facet>
									<a4j:commandLink value="Remover" actionListener="#{beanPesquisarGuia.removerItemPeticaoAction}" onclick="exibirMsgProcessando(true)" 
										oncomplete="exibirMsgProcessando(false)" reRender="formPopup, formResultado">
									</a4j:commandLink>
								</rich:column>
								
							</rich:dataTable>
							<rich:datascroller for="tbPeticoes" maxPages="10">
								<f:facet name="first">
									<h:outputText value="Primeira" />
								</f:facet>
								<f:facet name="last">
									<h:outputText value="Última" />
								</f:facet>
							</rich:datascroller>
						</c:if>
					</t:panelGroup>
				</h:panelGrid>
			</a4j:form>
		</rich:modalPanel>
	</a4j:page>
</f:view>