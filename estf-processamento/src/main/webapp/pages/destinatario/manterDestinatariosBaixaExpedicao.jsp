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
	<a4j:page pageTitle="::.. Destinatário Baixa Expedição ..::"
		onload="document.getElementById('renderizaTelaID').click();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Destinatário Baixa e Expedição" />
			</jsp:include>
			<a name="ancora"></a>
		</h:form>

		<a4j:form id="form" prependId="false">
			<h:panelGrid styleClass="MolduraExterna" cellpadding="0" cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0" id="pnlCentral">
					<a4j:outputPanel id="pnlPesquisa" styleClass="MolduraInterna" rendered="#{beanManterDestinatariosBaixaExpedicao.renderizaTelaDePesquisa}">
						<div class="PainelTituloCriaTexto">
							<span> Pesquisa: </span>
						</div>
						
						<div style="margin-top:10px;">
							<span class="Padrao">Origem:</span>
							<span>
								<h:inputText id="itDescricaoOrigemP" size="100" value="#{beanManterDestinatariosBaixaExpedicao.nomeOrigem}"
										onchange="if ( this.value!='' ) { #{rich:component('sbOrigemC')}.callSuggestion(true) }"
										style="margin-left:30px;">
										<a4j:support event="onfocus" actionListener="#{beanManterDestinatariosBaixaExpedicao.carregarOrigensAtivasAction}" />
								</h:inputText>
								<rich:suggestionbox id="sbOrigemP" height="200" width="500" for="itDescricaoOrigemP" 
									suggestionAction="#{beanManterDestinatariosBaixaExpedicao.pesquisarOrigensPelaListaSelecionada}"
									var="origem" nothingLabel="Nenhum registro encontrado">
									<h:column>
										<h:outputText value="#{origem.id} - #{origem.descricao}" />
									</h:column>
				
									<a4j:support ajaxSingle="true" event="onselect" eventsQueue="ajaxQueue" ignoreDupResponses="true" reRender="idOrigemP">
										<f:setPropertyActionListener value="#{origem.id}" target="#{beanManterDestinatariosBaixaExpedicao.codigoOrigem}" />
									</a4j:support>
								</rich:suggestionbox>
								<h:inputHidden value="#{beanManterDestinatariosBaixaExpedicao.codigoOrigem}"
									id="idOrigemP" />		
							</span>	
						</div>
						
						<div style="margin-top:10px;">
							<span class="Padrao">Destinatário:</span>
							<span>
								<h:inputText id="itDescricaoDestinatarioP" size="100" value="#{beanManterDestinatariosBaixaExpedicao.nomeDestinatario}"
										onchange="if ( this.value!='' ) { #{rich:component('sbDestP')}.callSuggestion(true) }">
										<a4j:support event="onfocus" actionListener="#{beanManterDestinatariosBaixaExpedicao.carregarDestinatarioPelaOrigemAction}" />
								</h:inputText>
					
								<rich:suggestionbox id="sbDestP" height="200" width="500" for="itDescricaoDestinatarioP" 
									suggestionAction="#{beanManterDestinatariosBaixaExpedicao.pesquisarDestinatarioPelaListaSelecionada}"
									var="destin" nothingLabel="Nenhum registro encontrado">
									<h:column>
										<h:outputText value="#{destin.id} - #{destin.nomDestinatario}" />
									</h:column>
				
									<a4j:support ajaxSingle="true" event="onselect" eventsQueue="ajaxQueue" ignoreDupResponses="true" reRender="idDestP">
										<f:setPropertyActionListener value="#{destin.id}" target="#{beanManterDestinatariosBaixaExpedicao.codigoDestinatario}" />
									</a4j:support>
								</rich:suggestionbox>
								<h:inputHidden value="#{beanManterDestinatariosBaixaExpedicao.codigoDestinatario}"
									id="idDestP" />		
							</span>	
						</div>
						
						<div style="margin-top:10px;">
							<span>
								<a4j:commandButton styleClass="BotaoPadrao" value="Pesquisar"
									id="btnPesquisarDestinatario" actionListener="#{beanManterDestinatariosBaixaExpedicao.pesquisarDestinatarioAction}"
									ignoreDupResponses="true"  onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" reRender="pnlCentral"/>
								<a4j:commandButton styleClass="BotaoPadrao"
									style="margin-left:15px;" value="Limpar"
									id="btnLimparPesquisaDestinatario"
									actionListener="#{beanManterDestinatariosBaixaExpedicao.limparTelaAction}"
									ignoreDupResponses="true" 
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" 
									reRender="pnlCentral"/>	
								<a4j:commandButton styleClass="BotaoPadraoEstendido"
									style="margin-left:15px;" value="Cadastrar Novo"
									id="btnRenderizaTelaCadastro"
									actionListener="#{beanManterDestinatariosBaixaExpedicao.limparTelaCadastrarDestinatarioAction}"
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" 
									reRender="pnlCentral, pnlCentralCadastroDiv"/>		
							</span>
						</div>																
					</a4j:outputPanel>
					
						<a4j:outputPanel ajaxRendered="true" keepTransient="false"
							id="tabDestinatario" styleClass="MolduraInterna"
							rendered="#{not empty beanManterDestinatariosBaixaExpedicao.listaDestinatarios}">
							<c:if test="${beanManterDestinatariosBaixaExpedicao.renderizaTabelaDestinatario == true}">
								<hr color="red" align="left" size="1px" width="90%" />
								<rich:dataTable headerClass="DataTableDefaultHeader" id="tabelaDestinatarios"
									styleClass="DataTableDefault"
									footerClass="DataTableDefaultFooter"
									rowClasses="DataTableRow, DataTableRow2"
									columnClasses="trintaCenter, vinteCenter, trintaCenter, dezCenter, dezCenter"
									value="#{beanManterDestinatariosBaixaExpedicao.listaDestinatarios}"
									var="wrappedDest"
									binding="#{beanManterDestinatariosBaixaExpedicao.tabelaDestinatarios}">
									
									<rich:column>
										<f:facet name="header">
											<h:outputText value="Origem" />
										</f:facet>
										<h:outputText value="#{wrappedDest.wrappedObject.nomeOrigem}" />
														
									</rich:column>
									
									<rich:column>
										<f:facet name="header">
											<h:outputText value="Destinatário" />
										</f:facet>				
										<h:outputText value="#{wrappedDest.wrappedObject.nomeDestinatario}" />
									</rich:column>
									
									<rich:column>
										<f:facet name="header">
											<h:outputText value="Endereço" />
										</f:facet>				
										<h:outputText value="#{wrappedDest.wrappedObject.enderecoDestinatario}" />
									</rich:column>
									
									<rich:column>
										<f:facet name="header">
											<h:outputText value="Ativo" />
										</f:facet>				
										<h:outputText rendered="#{wrappedDest.wrappedObject.ativo}" value="Sim" />
										<h:outputText rendered="#{!(wrappedDest.wrappedObject.ativo)}" value="Não" />
									</rich:column>
									
									<rich:column>
										<f:facet name="header">
											<h:outputText value="Ações" />
										</f:facet>	
										<a4j:commandButton value="Alterar" styleClass="PadraoLink"
											actionListener="#{beanManterDestinatariosBaixaExpedicao.alterarDestinatarioAction}"
											reRender="pnlCentral, pnlCentralCadastroDiv" />
										<h:outputText value="/ "/>	
										<a4j:commandButton value="Excluir" styleClass="PadraoLink"
											actionListener="#{beanManterDestinatariosBaixaExpedicao.excluirDestinatarioAction}" 
											reRender="pnlCentral, pnlCentralCadastroDiv"/>	
									</rich:column>					
								</rich:dataTable>
							</c:if>
						</a4j:outputPanel>			
					<a4j:outputPanel styleClass="MolduraInterna" keepTransient="false"
							id="pnlCentralCadastroDiv" rendered="#{beanManterDestinatariosBaixaExpedicao.renderizaTelaCadastro}" >
						<div class="PainelTituloCriaTexto">
							<span> Cadastro: </span>
						</div>
						<div id="idOrigemPanel" style="padding: 5px;">
							<div style="margin-top:10px;">
								<span class="Padrao">* Origem:</span>
								<span>
									<h:inputText id="itDescricaoOrigemC" size="100" value="#{beanManterDestinatariosBaixaExpedicao.nomeOrigem}"
											onchange="if ( this.value!='' ) { #{rich:component('sbOrigemC')}.callSuggestion(true) }"
											style="margin-left:30px;">
											<a4j:support event="onfocus" actionListener="#{beanManterDestinatariosBaixaExpedicao.carregarOrigensAtivasAction}" />
									</h:inputText>
									<rich:suggestionbox id="sbOrigemC" height="200" width="500" for="itDescricaoOrigemC" 
										suggestionAction="#{beanManterDestinatariosBaixaExpedicao.pesquisarOrigensPelaListaSelecionada}"
										var="origem" nothingLabel="Nenhum registro encontrado">
										<h:column>
											<h:outputText value="#{origem.id} - #{origem.descricao}" />
										</h:column>
					
										<a4j:support ajaxSingle="true" event="onselect" eventsQueue="ajaxQueue" ignoreDupResponses="true" reRender="idOrigemC">
											<f:setPropertyActionListener value="#{origem.id}" target="#{beanManterDestinatariosBaixaExpedicao.codigoOrigem}" />
										</a4j:support>
									</rich:suggestionbox>
									<h:inputHidden value="#{beanManterDestinatariosBaixaExpedicao.codigoOrigem}"
										id="idOrigemC" />		
								</span>	
							</div>
							
							<div style="margin-top:10px;">
								<span class="Padrao">* Destinatário:</span>
								<span>
									<h:inputText id="itDescricaoDestinatarioC" size="100" 
										value="#{beanManterDestinatariosBaixaExpedicao.nomeDestinatario}" 
										
										onkeyup="caixaAlta(this);">
									</h:inputText>
								</span>	
							</div>
							
							<div style="margin-top:10px;">
								<span class="Padrao">Ativo: </span>
								<span>
									<h:selectOneMenu style="margin-left: 50px;"
										value="#{beanManterDestinatariosBaixaExpedicao.destinatario.ativo}">
										<f:selectItems value="#{beanManterDestinatariosBaixaExpedicao.itensAtivoDestinatario}"/>
									</h:selectOneMenu>	
								</span>
							</div>	
						</div>
						
						<div id="idDestinatarioPanel" style="padding: 5px;">
							<div style="padding: 5px;margin-top:15px;">
								<div class="SubTituloControleTexto">
									<span> Endereço Destinatário: </span>
								</div>
								
								<div style="margin-top:10px;">
									<span class="Padrao">CEP:</span>
									<span>
										<h:inputText style="margin-left: 60px;" size="9" maxlength="8"
											value="#{beanManterDestinatariosBaixaExpedicao.enderecoDestinatario.cep}"
											onchange="document.getElementById('bntCep').click();"
											onkeypress="return mascaraInputNumerico(this, event)">
										</h:inputText>
										<div id="dvAguarde" class="PadraoAguarde"></div>
									</span>
								</div>
							
								<div style="margin-top:10px;">
									<span class="Padrao">* Endereço:
										<h:inputText style="margin-left: 20px;" size="85" id="inputEndereco"
											value="#{beanManterDestinatariosBaixaExpedicao.enderecoDestinatario.logradouro}"
											onkeyup="caixaAlta(this);"/>
									</span>
								</div>	
								
								<div style="margin-top:10px;">
									<span class="Padrao">Complemento:
										<h:inputText style="margin-left: 5px;" size="50" id="inputComplemento"
											value="#{beanManterDestinatariosBaixaExpedicao.enderecoDestinatario.complemento}"
											onkeyup="caixaAlta(this);"/>
									</span>
									<span class="Padrao">* Número:
										<h:inputText style="margin-left: 5px;" size="5" maxlength="5"
											value="#{beanManterDestinatariosBaixaExpedicao.enderecoDestinatario.numeroLocalizacao}"/>
									</span>
								</div>
								
								<div style="margin-top:10px;">
									<span class="Padrao">Bairro:
										<h:inputText style="margin-left: 49px;" size="35" id="inputBairro"
											value="#{beanManterDestinatariosBaixaExpedicao.enderecoDestinatario.bairro}"
											onkeyup="caixaAlta(this);"/>
									</span>
									<span class="Padrao">* Cidade:
										<h:inputText style="margin-left: 5px;" size="35" id="inputCidade"
											value="#{beanManterDestinatariosBaixaExpedicao.enderecoDestinatario.municipio}"
											onkeyup="caixaAlta(this);"/>
									</span>
								</div>
									
								<div style="margin-top:10px;">
									<span class="Padrao">* UF:
										<h:selectOneMenu style="margin-left: 10px;"
											value="#{beanManterDestinatariosBaixaExpedicao.enderecoDestinatario.uf}">
											<f:selectItems value="#{beanManterDestinatariosBaixaExpedicao.itensUf}"/>
										</h:selectOneMenu>	
									</span>
									<span class="Padrao" style="margin-left:10px;">Ativo:
										<h:selectOneMenu style="margin-left: 10px;"
											value="#{beanManterDestinatariosBaixaExpedicao.enderecoDestinatario.ativo}">
											<f:selectItems value="#{beanManterDestinatariosBaixaExpedicao.itensAtivoDestinatario}"/>
										</h:selectOneMenu>	
									</span>
									<span style="margin-left:40px">
										<a4j:commandButton value="Adicionar Endereço" styleClass="botao"
											style="width: 130px;" rendered="#{!beanManterDestinatariosBaixaExpedicao.renderizaBotaoAlterarEndereco}"
											actionListener="#{beanManterDestinatariosBaixaExpedicao.adicionarEnderecoAction}" 
											reRender="pnlCentralCadastroDiv"/>
										<a4j:commandButton value="Alterar Endereço" styleClass="botao"
											style="width: 130px;" rendered="#{beanManterDestinatariosBaixaExpedicao.renderizaBotaoAlterarEndereco}"
											actionListener="#{beanManterDestinatariosBaixaExpedicao.adicionarEnderecoAction}" 
											reRender="pnlCentralCadastroDiv"/>
										<a4j:commandButton value="Novo Endereço" styleClass="botao"
											style="width: 130px;" rendered="#{beanManterDestinatariosBaixaExpedicao.renderizaBotaoAlterarEndereco}"
											actionListener="#{beanManterDestinatariosBaixaExpedicao.novoEnderecoAction}" 
											reRender="pnlCentralCadastroDiv"/>
									</span>
								</div>
							</div>
						</div>
						<a4j:outputPanel ajaxRendered="true" keepTransient="false"
							id="tabEndereco" styleClass="MolduraInterna"
							rendered="#{not empty beanManterDestinatariosBaixaExpedicao.listaEndereco}">
								<rich:dataTable headerClass="DataTableDefaultHeader" id="tabelaEndereco"
									styleClass="DataTableDefault"
									footerClass="DataTableDefaultFooter"
									rowClasses="DataTableRow, DataTableRow2"
									columnClasses="trintaCenter, vinteCenter, dezCenter, vinteCenter, dezCenter, dezCenter"
									value="#{beanManterDestinatariosBaixaExpedicao.listaEndereco}"
									var="wrappedEndereco"
									binding="#{beanManterDestinatariosBaixaExpedicao.tabelaEndereco}">
										
										<rich:column>
											<f:facet name="header">
												<h:outputText value="Destinatário" />
											</f:facet>
											<h:outputText value="#{wrappedEndereco.wrappedObject.destinatario.nomDestinatario}" />
										</rich:column>
										
										<rich:column>
											<f:facet name="header">
												<h:outputText value="Endereço" />
											</f:facet>
											<h:outputText value="#{wrappedEndereco.wrappedObject.logradouro} #{wrappedEndereco.wrappedObject.complemento} #{wrappedEndereco.wrappedObject.numeroLocalizacao} - #{wrappedEndereco.wrappedObject.bairro}" />
										</rich:column>
										
										<rich:column>
											<f:facet name="header">
												<h:outputText value="Cidade" />
											</f:facet>				
											<h:outputText value="#{wrappedEndereco.wrappedObject.municipio} - #{wrappedEndereco.wrappedObject.uf}" />
										</rich:column>
										
										<rich:column>
											<f:facet name="header">
												<h:outputText value="CEP" />
											</f:facet>				
											<h:outputText value="#{wrappedEndereco.wrappedObject.cep}" />
										</rich:column>
										
										<rich:column>
											<f:facet name="header">
												<h:outputText value="Ativo" />
											</f:facet>				
											<h:outputText rendered="#{wrappedEndereco.wrappedObject.ativo}" value="Sim" />
											<h:outputText rendered="#{!(wrappedEndereco.wrappedObject.ativo)}" value="Não" />
										</rich:column>
										
										<rich:column>
											<f:facet name="header">
												<h:outputText value="Ações" />
											</f:facet>
											<a4j:commandButton value="Alterar" styleClass="PadraoLink"
												actionListener="#{beanManterDestinatariosBaixaExpedicao.alterarEnderecoAction}" 
												reRender="pnlCentralCadastroDiv"/>
											<h:outputText value="/ "/>	
											<a4j:commandButton value="Excluir" styleClass="PadraoLink"
												actionListener="#{beanManterDestinatariosBaixaExpedicao.excluirEnderecoAction}" 
												reRender="pnlCentralCadastroDiv"/>	
										</rich:column>					
								</rich:dataTable>
							</a4j:outputPanel>							
							<div id="idDestinatarioContato" style="padding: 5px;">
								<div style="padding: 5px;margin-top:15px;">
									<div class="SubTituloControleTexto">
										<span> Contato: </span>
									</div>
								
									<div style="margin-top:10px;">
										<span class="Padrao">* Contato:
											<h:inputText style="margin-left: 5px;" size="55" id="inputNomeContato"
												value="#{beanManterDestinatariosBaixaExpedicao.contatoDestinatario.nomContato}"
												onkeyup="caixaAlta(this);"/>
										</span>
										<span class="Padrao">Telefone:
											<h:inputText style="margin-left: 5px;" size="2" maxlength="2" id="inputCodTel"
												value="#{beanManterDestinatariosBaixaExpedicao.contatoDestinatario.codigoAreaTelefone}"
												onkeypress="return mascaraInputNumerico(this, event)"/>
											<h:inputText style="margin-left: 2px;" size="11"  maxlength="10" id="inputTelefone" 
												onkeydown="mascaraTelefone(this);"
												value="#{beanManterDestinatariosBaixaExpedicao.telefoneDestHifen}">
											</h:inputText>
										</span>
									</div>
									
									<div style="margin-top:10px;">
										<span class="Padrao">Fax:
											<h:inputText style="margin-left: 5px;" size="2" maxlength="2" id="inputCodFax"
												onkeypress="return mascaraInputNumerico(this, event)"
												value="#{beanManterDestinatariosBaixaExpedicao.contatoDestinatario.codigoAreaFax}"/>
											<h:inputText style="margin-left: 2px;" size="11"  maxlength="10" id="inputFax"
												value="#{beanManterDestinatariosBaixaExpedicao.faxDestHifen}"
												onkeydown="mascaraTelefone(this);"/>	
										</span>
										<span class="Padrao">E-mail:
											<h:inputText style="margin-left: 5px;" size="55"
												value="#{beanManterDestinatariosBaixaExpedicao.contatoDestinatario.enderecoEmail}"/>
										</span>
										<span>
											<a4j:commandButton value="Adicionar Contato" styleClass="botao"
												style="width: 130px;" rendered="#{!beanManterDestinatariosBaixaExpedicao.renderizaBotaoAlterarContato}"
												actionListener="#{beanManterDestinatariosBaixaExpedicao.adicionarContatoAction}" 
												reRender="pnlCentralCadastroDiv"/>
											<a4j:commandButton value="Alterar Contato" styleClass="botao"
												style="width: 130px;" rendered="#{beanManterDestinatariosBaixaExpedicao.renderizaBotaoAlterarContato}"
												actionListener="#{beanManterDestinatariosBaixaExpedicao.adicionarContatoAction}" 
												reRender="pnlCentralCadastroDiv"/>	
											<a4j:commandButton value="Novo Contato" styleClass="botao"
												style="width: 130px;" rendered="#{beanManterDestinatariosBaixaExpedicao.renderizaBotaoAlterarContato}"
												actionListener="#{beanManterDestinatariosBaixaExpedicao.novoContatoAction}" 
												reRender="pnlCentralCadastroDiv"/>												
										</span>
									</div>
								</div>
							</div>
							<a4j:outputPanel ajaxRendered="true" keepTransient="false"
								id="tabContato" styleClass="MolduraInterna"
								rendered="#{not empty beanManterDestinatariosBaixaExpedicao.listaContatoDestinatario}">
								<rich:dataTable headerClass="DataTableDefaultHeader" id="tabelaContato"
									styleClass="DataTableDefault"
									footerClass="DataTableDefaultFooter"
									rowClasses="DataTableRow, DataTableRow2"
									columnClasses="quarentaCenter, dezCenter, dezCenter, trintaCenter, dezCenter"
									value="#{beanManterDestinatariosBaixaExpedicao.listaContatoDestinatario}"
									var="wrappedContato"
									binding="#{beanManterDestinatariosBaixaExpedicao.tabelaContatoDestinatario}">
									
									<rich:column>
										<f:facet name="header">
											<h:outputText value="Contato" />
										</f:facet>
										<h:outputText value="#{wrappedContato.wrappedObject.nomContato}" />
														
									</rich:column>
									
									<rich:column>
										<f:facet name="header">
											<h:outputText value="Telefone" />
										</f:facet>
										<h:outputText value="#{wrappedContato.wrappedObject.codigoAreaTelefone} #{wrappedContato.wrappedObject.numeroTelefone}" />
									</rich:column>
									
									<rich:column>
										<f:facet name="header">
											<h:outputText value="Fax" />
										</f:facet>				
										<h:outputText value="#{wrappedContato.wrappedObject.codigoAreaFax} #{wrappedContato.wrappedObject.numeroFax}" />
									</rich:column>
									
									<rich:column>
										<f:facet name="header">
											<h:outputText value="Email" />
										</f:facet>				
										<h:outputText value="#{wrappedContato.wrappedObject.enderecoEmail}" />
									</rich:column>
									
									<rich:column>
										<f:facet name="header">
											<h:outputText value="Ações" />
										</f:facet>	
										<a4j:commandButton value="Alterar" styleClass="PadraoLink"
											actionListener="#{beanManterDestinatariosBaixaExpedicao.alterarContatoAction}"
											reRender="pnlCentralCadastroDiv" />
										<h:outputText value="/ "/>	
										<a4j:commandButton value="Excluir" styleClass="PadraoLink"
											actionListener="#{beanManterDestinatariosBaixaExpedicao.excluirContatoAction}" 
											reRender="pnlCentralCadastroDiv"/>	
									</rich:column>					
								</rich:dataTable>
							</a4j:outputPanel>							
							<div style="margin-top:10px;">
								<span>
									<a4j:commandButton value="Salvar" styleClass="BotaoPadrao" reRender="pnlCentral"
										actionListener="#{beanManterDestinatariosBaixaExpedicao.salvarDestinatarioAction}"
										oncomplete="localizaAncora();"/>
									<a4j:commandButton value="Limpar" styleClass="BotaoPadrao" 
										actionListener="#{beanManterDestinatariosBaixaExpedicao.limparTelaCadastrarDestinatarioAction}"
										reRender="pnlCentral"/>
									<a4j:commandButton value="Fechar" styleClass="BotaoPadrao" 
										actionListener="#{beanManterDestinatariosBaixaExpedicao.fechaTelaCadastrarDestinatarioAction}"
										reRender="pnlCentral"/>
								</span>
							</div>
						</a4j:outputPanel>	
					</h:panelGrid>
			</h:panelGrid>
			<a4j:commandButton id="bntCep" actionListener="#{beanManterDestinatariosBaixaExpedicao.recuperarEnderecoCepAction}" styleClass="BotaoOculto" 
				reRender="pnlCentralCadastroDiv" oncomplete="aguarde(false, 'dvAguarde');" onclick="aguarde(true, 'dvAguarde');"/>	
			<a4j:commandButton id="incluiDestinatario" action="#{beanManterDestinatariosBaixaExpedicao.incluiDestinatario}" styleClass="BotaoOculto"/>	
			<a4j:commandButton id="renderizaTelaID" reRender="pnlCentral" styleClass="BotaoOculto"/>					
		</a4j:form>	

		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>