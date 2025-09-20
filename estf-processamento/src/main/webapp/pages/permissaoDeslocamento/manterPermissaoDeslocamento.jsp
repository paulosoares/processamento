<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

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
	<a4j:page pageTitle="::.. Manter permissões de deslocamento entre setores ..::"
		onload="document.getElementById('renderizaTelaID').click();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Manter Permissões de Deslocamento" />
			</jsp:include>
			<a name="ancora"></a>
		</h:form>

		<a4j:form id="form" prependId="false">
			<h:panelGrid styleClass="MolduraExterna" cellpadding="0" cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0" id="pnlCentral">
					<a4j:outputPanel id="pnlPesquisa" styleClass="MolduraInterna" rendered="#{beanManterPermissaoDeslocamento.renderizaTelaDePesquisa}">
						<div class="PainelTituloCriaTexto">
							<span> Pesquisa: </span>
						</div>
						
						<div style="margin-top:10px;">
							<span class="Padrao">Setor origem:</span>
							<span>
								<h:inputText id="itDescricaoOrigemP" size="100" value="#{beanManterPermissaoDeslocamento.nomeOrigem}"
										onchange="if ( this.value!='' ) { #{rich:component('sbOrigemC')}.callSuggestion(true) }">
										<a4j:support event="onfocus" actionListener="#{beanManterPermissaoDeslocamento.carregarOrigensAtivasAction}" />
								</h:inputText>
								<rich:suggestionbox id="sbOrigemP" height="200" width="500" for="itDescricaoOrigemP" 
									suggestionAction="#{beanManterPermissaoDeslocamento.pesquisarOrigensPelaListaSelecionada}"
									var="origem" nothingLabel="Nenhum registro encontrado">
									<h:column>
										<h:outputText value="#{origem.id} - #{origem.nome}" />
									</h:column>
				
									<a4j:support ajaxSingle="true" event="onselect" eventsQueue="ajaxQueue" ignoreDupResponses="true" reRender="idOrigemP">
										<f:setPropertyActionListener value="#{origem.id}" target="#{beanManterPermissaoDeslocamento.codigoOrigem}" />
									</a4j:support>
								</rich:suggestionbox>
								<h:inputHidden value="#{beanManterPermissaoDeslocamento.codigoOrigem}"
									id="idOrigemP" />		
							</span>	
						</div>
						
						<div style="margin-top:10px;">
							<span class="Padrao">Setor destino:</span>
							<span>
								<h:inputText id="itDescricaoDestinatarioP" size="100" value="#{beanManterPermissaoDeslocamento.nomeDestinatario}"
										onchange="if ( this.value!='' ) { #{rich:component('sbDestP')}.callSuggestion(true) }">
										<a4j:support event="onfocus" actionListener="#{beanManterPermissaoDeslocamento.carregarDestinatarioPelaOrigemAction}" />
								</h:inputText>
					
								<rich:suggestionbox id="sbDestP" height="200" width="500" for="itDescricaoDestinatarioP" 
									suggestionAction="#{beanManterPermissaoDeslocamento.pesquisarDestinatarioPelaListaSelecionada}"
									var="destin" nothingLabel="Nenhum registro encontrado">
									<h:column>
										<h:outputText value="#{destin.id} - #{destin.nome}" />
									</h:column>
				
									<a4j:support ajaxSingle="true" event="onselect" eventsQueue="ajaxQueue" ignoreDupResponses="true" reRender="idDestP">
										<f:setPropertyActionListener value="#{destin.id}" target="#{beanManterPermissaoDeslocamento.codigoDestinatario}" />
									</a4j:support>
								</rich:suggestionbox>
								<h:inputHidden value="#{beanManterPermissaoDeslocamento.codigoDestinatario}"
									id="idDestP" />		
							</span>	
						</div>
						
						<div style="margin-top:10px;">
							<span>
								<a4j:commandButton styleClass="BotaoPadrao" value="Pesquisar"
									id="btnPesquisarDestinatario" actionListener="#{beanManterPermissaoDeslocamento.pesquisarPermissaoDeslocamentoAction}"
									ignoreDupResponses="true"  onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" reRender="pnlCentral"/>
								<a4j:commandButton styleClass="BotaoPadrao"
									style="margin-left:15px;" value="Limpar"
									id="btnLimparPesquisaDestinatario"
									actionListener="#{beanManterPermissaoDeslocamento.limparTelaAction}"
									ignoreDupResponses="true" 
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" 
									reRender="pnlCentral"/>	
								<a4j:commandButton styleClass="BotaoPadraoEstendido"
									style="margin-left:15px;" value="Cadastrar Novo"
									id="btnRenderizaTelaCadastro"
									actionListener="#{beanManterPermissaoDeslocamento.limparTelaCadastrarPermissaoDeslocamentoAction}"
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" 
									reRender="pnlCentral, pnlCentralCadastroDiv"/>		
							</span>
						</div>																
					</a4j:outputPanel>
					
						<a4j:outputPanel ajaxRendered="true" keepTransient="false"
							id="tabDestinatario" styleClass="MolduraInterna"
							rendered="#{not empty beanManterPermissaoDeslocamento.listaPermissoes}">
							<c:if test="${beanManterPermissaoDeslocamento.renderizaTabelaDestinatario == true}">
								<hr color="red" align="left" size="1px" width="90%" />
								<rich:dataTable headerClass="DataTableDefaultHeader" id="tabelaDestinatarios"
									styleClass="DataTableDefault"
									footerClass="DataTableDefaultFooter"
									rowClasses="DataTableRow, DataTableRow2"
									columnClasses="trintaCenter, vinteCenter, trintaCenter, dezCenter, dezCenter"
									value="#{beanManterPermissaoDeslocamento.listaPermissoes}"
									var="wrappedDest"
									binding="#{beanManterPermissaoDeslocamento.tabelaDestinatarios}">
									
									<rich:column>
										<f:facet name="header">
											<h:outputText value="Origem" />
										</f:facet>
										<h:outputText value="#{wrappedDest.wrappedObject.setorOrigem.id} - #{wrappedDest.wrappedObject.setorOrigem.nome}" />
														
									</rich:column>
									
									<rich:column>
										<f:facet name="header">
											<h:outputText value="Destinatário" />
										</f:facet>				
										<h:outputText value="#{wrappedDest.wrappedObject.setorDestino.id} - #{wrappedDest.wrappedObject.setorDestino.nome}" />
									</rich:column>
									
									<rich:column>
										<f:facet name="header">
											<h:outputText value="Pode deslocar documento?" />
										</f:facet>				
										<h:outputText rendered="#{wrappedDest.wrappedObject.permissao}" value="Sim" />
										<h:outputText rendered="#{!(wrappedDest.wrappedObject.permissao)}" value="Não" />
									</rich:column>
									
									<rich:column>
										<f:facet name="header">
											<h:outputText value="Ações" />
										</f:facet>	
										<a4j:commandButton value="Alterar" styleClass="PadraoLink"
											actionListener="#{beanManterPermissaoDeslocamento.alterarPermissaoDeslocamentoAction}"
											reRender="pnlCentral, pnlCentralCadastroDiv" />
										<h:outputText value="/ "/>	
										<a4j:commandButton value="Excluir" styleClass="PadraoLink"
											actionListener="#{beanManterPermissaoDeslocamento.excluirPermissaoDeslocamentoAction}" 
											reRender="pnlCentral, pnlCentralCadastroDiv"/>	
									</rich:column>					
								</rich:dataTable>
							</c:if>
						</a4j:outputPanel>			
					<a4j:outputPanel styleClass="MolduraInterna" keepTransient="false"
							id="pnlCentralCadastroDiv" rendered="#{beanManterPermissaoDeslocamento.renderizaTelaCadastro}" >
						<div class="PainelTituloCriaTexto">
							<span> Cadastro: </span>
						</div>
						<div id="idOrigemPanel" style="padding: 5px;">
							<div style="margin-top:10px;">
								<span class="Padrao">* Setor Origem:</span>
								<span>
									<h:inputText id="itDescricaoOrigemC" size="100" value="#{beanManterPermissaoDeslocamento.nomeOrigem}"
											onchange="if ( this.value!='' ) { #{rich:component('sbOrigemC')}.callSuggestion(true) }">
											<a4j:support event="onfocus" actionListener="#{beanManterPermissaoDeslocamento.carregarOrigensAtivasAction}" />
									</h:inputText>
									<rich:suggestionbox id="sbOrigemC" height="200" width="500" for="itDescricaoOrigemC" 
										suggestionAction="#{beanManterPermissaoDeslocamento.pesquisarOrigensPelaListaSelecionada}"
										var="origem" nothingLabel="Nenhum registro encontrado">
										<h:column>
											<h:outputText value="#{origem.id} - #{origem.nome}" />
										</h:column>
					
										<a4j:support ajaxSingle="true" event="onselect" eventsQueue="ajaxQueue" ignoreDupResponses="true" reRender="idOrigemC">
											<f:setPropertyActionListener value="#{origem.id}" target="#{beanManterPermissaoDeslocamento.codigoOrigem}" />
										</a4j:support>
									</rich:suggestionbox>
									<h:inputHidden value="#{beanManterPermissaoDeslocamento.codigoOrigem}"
										id="idOrigemC" />		
								</span>	
							</div>
							
							<div style="margin-top:10px;">
								<span class="Padrao">* Setor destino:</span>
								<span>
									<h:inputText id="itDescricaoDestinatarioC" size="100" value="#{beanManterPermissaoDeslocamento.nomeDestinatario}"
											onchange="if ( this.value!='' ) { #{rich:component('sbDestP')}.callSuggestion(true) }">
											<a4j:support event="onfocus" actionListener="#{beanManterPermissaoDeslocamento.carregarDestinatarioPelaOrigemAction}" />
									</h:inputText>
						
									<rich:suggestionbox id="sbDestC" height="200" width="500" for="itDescricaoDestinatarioC" 
										suggestionAction="#{beanManterPermissaoDeslocamento.pesquisarDestinatarioPelaListaSelecionada}"
										var="destin" nothingLabel="Nenhum registro encontrado">
										<h:column>
											<h:outputText value="#{destin.id} - #{destin.nome}" />
										</h:column>
					
										<a4j:support ajaxSingle="true" event="onselect" eventsQueue="ajaxQueue" ignoreDupResponses="true" reRender="idDestC">
											<f:setPropertyActionListener value="#{destin.id}" target="#{beanManterPermissaoDeslocamento.codigoDestinatario}" />
										</a4j:support>
									</rich:suggestionbox>
									<h:inputHidden value="#{beanManterPermissaoDeslocamento.codigoDestinatario}"
										id="idDestC" />		
								</span>	
							</div>							
							
							<div style="margin-top:10px;">
								<span class="Padrao">Deslocamento permitido? </span>
								<span>
									<h:selectOneRadio value="#{beanManterPermissaoDeslocamento.permissaoDeslocamento.permissao}" layout="pageDirection">
										<f:selectItem itemLabel="Sim: somente o setor cadastrado na origem está autorizado a deslocar para o destino" itemValue="true"/>
										<f:selectItem itemLabel="Não: todos os setores estão autorizados a deslocar para o destino, exceto o cadastrado como origem" itemValue="false"/>																			
									</h:selectOneRadio>	
								</span>
							</div>	
						</div>
										
						<div style="margin-top:10px;">
							<span>
								<a4j:commandButton value="Salvar" styleClass="BotaoPadrao" reRender="pnlCentral"
									actionListener="#{beanManterPermissaoDeslocamento.salvarPermissaoDeslocamentoAction}"
									oncomplete="localizaAncora();"/>
								<a4j:commandButton value="Limpar" styleClass="BotaoPadrao" 
									actionListener="#{beanManterPermissaoDeslocamento.limparTelaCadastrarPermissaoDeslocamentoAction}"
									reRender="pnlCentral"/>
								<a4j:commandButton value="Fechar" styleClass="BotaoPadrao" 
									actionListener="#{beanManterPermissaoDeslocamento.fechaTelaCadastrarPermissaoDeslocamentoAction}"
									reRender="pnlCentral"/>
							</span>
						</div>
					</a4j:outputPanel>	
					</h:panelGrid>
			</h:panelGrid>
			<a4j:commandButton id="bntCep" actionListener="#{beanManterPermissaoDeslocamento.recuperarEnderecoCepAction}" styleClass="BotaoOculto" 
				reRender="pnlCentralCadastroDiv" oncomplete="aguarde(false, 'dvAguarde');" onclick="aguarde(true, 'dvAguarde');"/>	
			<a4j:commandButton id="incluiDestinatario" action="#{beanManterPermissaoDeslocamento.incluiDestinatario}" styleClass="BotaoOculto"/>	
			<a4j:commandButton id="renderizaTelaID" reRender="pnlCentral" styleClass="BotaoOculto"/>					
		</a4j:form>	

		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>