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
	
	function checkEnableSubmit() {
		  if (document.getElementById("inputOabId").value != null) 
		    {document.getElementById("btnPesquisarJurisdicionado").disabled = false;}
		  else 
		    {document.getElementById("btnPesquisarJurisdicionado").disabled = true;}
	}
	
	function desabilitaBotaoPesquisar() {
		    document.getElementById("btnPesquisarJurisdicionado").disabled = true;
	}
	
	function foco(campo){
		document.getElementById(campo).focus();
	}
	
	function limitTextArea(element, limit) {

		if (element.value.length > limit) {
		   alert('Limite do campo foi excedido.');
		   element.value = element.value.substring(0, limit);
		}

	}

	
</script>


<f:view>
	<a4j:page pageTitle="::.. Cadastro de Pessoa para Carga ..::"
	onload="desabilitaBotaoPesquisar();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Cadastro de Pessoa para Carga" />
			</jsp:include>
			<a name="ancora"></a>
		</h:form>

		<a4j:form id="form" prependId="false">
			<h:panelGrid styleClass="MolduraExterna" cellpadding="0" cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0" id="pnlCentral">
					<a4j:outputPanel id="pnlPesquisa" styleClass="MolduraInterna" rendered="#{beanManterPessoaCarga.renderizaTelaDePesquisa}">
						<div class="PainelTituloCriaTexto">
							<span> Pesquisa: </span>
						</div>

						<h:panelGrid columns="4" id="panelDestino" style="margin-top: 10px;">
							<h:outputText styleClass="Padrao" value="Código/Nome/CPF:"
								id="labelDestino" />
							<h:inputText id="itDescricaoDestinatario" size="100" onkeyup="caixaAlta(this);"
								value="#{beanManterPessoaCarga.nomeJurisdicionado}"
								onchange="if ( this.value!='' ) { #{rich:component('sbSetor')}.callSuggestion(true) }">
							</h:inputText>
							<rich:hotKey selector="#itDescricaoDestinatario" key="return" handler="" />
		
							<rich:suggestionbox id="sbSetor" height="200" width="500" 
								for="itDescricaoDestinatario"
								suggestionAction="#{beanManterPessoaCarga.carregarJurisdicionadosPeloIdNome}"
								var="jurisResult" nothingLabel="Nenhum registro encontrado">
								<h:column>
										<h:outputText value="#{jurisResult.id} - #{jurisResult.nome} - #{jurisResult.cpf}" />
									</h:column>
			
								<a4j:support ajaxSingle="true" event="onselect" reRender="pnlCentral"
									eventsQueue="ajaxQueue" ignoreDupResponses="true" action="#{beanManterPessoaCarga.setaValorIdJurisdicionado}">
									<f:setPropertyActionListener value="#{jurisResult.id}" target="#{beanManterPessoaCarga.idJurisdicionado}" />
								</a4j:support>
							</rich:suggestionbox>
						</h:panelGrid>
						
						<h:panelGrid columns="4">
							<h:outputText styleClass="Padrao" value="OAB:"/>
							<h:inputText size="15" style="margin-left:7px;" id="inputOabId"
								value="#{beanManterPessoaCarga.oabJurisdicionado}"
								onkeypress="checkEnableSubmit();"/>
							<h:selectOneMenu style="margin-left: 10px;"
								value="#{beanManterPessoaCarga.idUf}">
								<f:selectItems value="#{beanManterPessoaCarga.itensUf}"/>
							</h:selectOneMenu>	
						</h:panelGrid>
						
						<h:panelGrid columns="3" style="margin-top:10px;">
								<a4j:commandButton styleClass="BotaoPadrao" value="Pesquisar" disabled="#{beanManterPessoaCarga.desabilitarPesquisa}"
									id="btnPesquisarJurisdicionado" actionListener="#{beanManterPessoaCarga.pesquisarJurisdicionadoAction}"
									ignoreDupResponses="true"  onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" reRender="pnlCentral"/>
								<a4j:commandButton styleClass="BotaoPadrao"
									style="margin-left:15px;" value="Limpar"
									id="btnLimparPesquisaJurisdicionado"
									actionListener="#{beanManterPessoaCarga.limparTelaAction}"
									ignoreDupResponses="true" 
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);desabilitaBotaoPesquisar();" 
									reRender="pnlCentral"/>	
								<a4j:commandButton styleClass="BotaoPadraoEstendido"
									style="margin-left:15px;" value="Nova Pessoa"
									id="btnRenderizaTelaCadastro"
									actionListener="#{beanManterPessoaCarga.limparTelaCadastrarJurisdicionadoAction}"
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" 
									reRender="pnlCentral, pnlCentralCadastroDiv"/>		
						</h:panelGrid>															
					</a4j:outputPanel>
					
					<a4j:outputPanel ajaxRendered="true" keepTransient="false"
						id="tabJurisdicionado" styleClass="MolduraInterna"
						rendered="#{not empty beanManterPessoaCarga.listaJurisdicionado}">
						<c:if test="${beanManterPessoaCarga.renderizaTabelaJurisdicionado == true}">
							<hr color="red" align="left" size="1px" width="100%" />
							<rich:dataTable headerClass="DataTableDefaultHeader" id="tabelaJurisdicionado"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="dezCenter, trintaCenter, dezCenter, dezCenter, trintaCenter, seteCenter"
								value="#{beanManterPessoaCarga.listaJurisdicionado}"
								var="wrappedJuris"
								binding="#{beanManterPessoaCarga.tabelaJurisdicionado}">
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Código" />
									</f:facet>
									<h:outputText value="#{wrappedJuris.wrappedObject.id}" />
								</rich:column>
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Nome" />
									</f:facet>
									<h:outputText value="#{wrappedJuris.wrappedObject.nome}" />
								</rich:column>
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="CPF" />
									</f:facet>				
									<h:outputText value="#{wrappedJuris.wrappedObject.cpf}" />
								</rich:column>
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="OAB" />
									</f:facet>				
									<h:outputText value="#{wrappedJuris.wrappedObject.oab}" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Observação" />
									</f:facet>				
									<h:outputText id="colObs" value="#{beanManterPessoaCarga.colunaTruncadaObs}" />
								    <rich:toolTip for="colObs" style="width:350px; height:210px;" layout="block" value="#{wrappedJuris.wrappedObject.observacao}"
								                  disabled="#{beanManterPessoaCarga.desabilitaHintObs}"  />
								</rich:column>
								
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Ações" />
									</f:facet>	
									<a4j:commandButton value="Alterar" styleClass="PadraoLink" id="btnAlterarJurisdicionadoId"
										actionListener="#{beanManterPessoaCarga.alterarJurisdicionadoAction}"
										reRender="pnlCentral, pnlCentralCadastroDiv" />
								</rich:column>					
							</rich:dataTable>
						</c:if>
					</a4j:outputPanel>			
					<a4j:outputPanel styleClass="MolduraInterna" keepTransient="false"
							id="pnlCentralCadastroDiv" rendered="#{beanManterPessoaCarga.renderizaTelaCadastro}" >
						<div class="PainelTituloCriaTexto">
							<span> Cadastro: </span>
						</div>
						<div id="idOrigemPanel" style="padding: 5px;">
							<div style="margin-top:10px;">
								<span>
									<h:selectOneRadio disabled="#{beanManterPessoaCarga.desabilitaRadioPapel}"
										value="#{beanManterPessoaCarga.tipoPapelJurisdicionado}" 
										id="tipoPapelJurisdicionado">
										<f:selectItem itemValue="A" itemLabel="Advogado" />
										<f:selectItem itemValue="E" itemLabel="Estagiário" />
										<f:selectItem itemValue="P" itemLabel="Preposto" />
										<a4j:support ajaxSingle="true" event="onclick" reRender="obrigatorioOAB"
											eventsQueue="ajaxQueue" ignoreDupResponses="true" actionListener="#{beanManterPessoaCarga.atualizaLabelOAB}">
										</a4j:support>
										
									</h:selectOneRadio>
								</span>
							</div>
							<div style="margin-top:10px;">
								<span class="Padrao">* Nome:</span>
								<span>
									<h:inputText size="100" disabled="#{beanManterPessoaCarga.desabilitaSeForAlteracao}"
										value="#{beanManterPessoaCarga.jurisdicionado.nome}"
										onkeyup="caixaAlta(this);">
									</h:inputText>	
								</span>	
							</div>
							
							<div style="margin-top:10px;">
								<span class="Padrao"> Email:</span>
								<span>
									<h:inputText size="100" style="margin-left:11px"
										value="#{beanManterPessoaCarga.jurisdicionado.email}" >
									</h:inputText>	
								</span>	
							</div>
							
							<div style="margin-top:10px;">
								<span class="Padrao">* CPF:</span>
								<span>
									<h:inputText size="15" style="margin-left:10px;" disabled="#{beanManterPessoaCarga.desabilitaSeForAlteracao}"
										value="#{beanManterPessoaCarga.cpfJurisdicionado}" maxlength="11" onkeypress="return mascaraInputNumerico(this, event)"
										onblur="document.getElementById('bntCPF').click();" />
									<h:inputHidden value="#{beanManterPessoaCarga.existeCadastro}"
										binding="#{beanManterPessoaCarga.inputVerificaCadastroPessoa}" id="idExisteCadastro" />
								</span>
								<span class="Padrao"> RG:</span>
								<span>
									<h:inputText size="15" 
										value="#{beanManterPessoaCarga.rgJurisdicionado}" id="inputRgID"
										onkeypress="return mascaraInputNumerico(this, event)"/>
								</span>
								<!--  caracter '*' para OAB renderizado dinamicamente -->
								<h:outputText id="obrigatorioOAB" value="* " />
								<span class="Padrao">OAB</span>
								<span>
									<h:inputText size="15" 
										value="#{beanManterPessoaCarga.oabJurisdicionado}"/>
									<h:selectOneMenu style="margin-left: 5px;"
										value="#{beanManterPessoaCarga.ufOAB}">
										<f:selectItems value="#{beanManterPessoaCarga.itensUfOAB}"/>
									</h:selectOneMenu>												
								</span>	
							</div>
							<div style="margin-top:10px;">
								<span class="Padrao">* Validade do Cadastro:</span>
								<span>
									<rich:calendar id="itDataValidadeJuris" 
										value="#{beanManterPessoaCarga.dataValidadeCadastro}" 
										datePattern="dd/MM/yyyy" locale="pt_Br" />										
								</span>	
							</div>
							<h:panelGrid id="panelObservacao">
								<h:outputText styleClass="Padrao" value="Observação:" />
								<h:inputTextarea id="itObservacao" rows="2" cols="100"   
									value="#{beanManterPessoaCarga.observacao}" onkeyup="limitTextArea(this,1000);" 
									onkeydown="limitTextArea(this,1000);" />
							</h:panelGrid>
						</div>
						
						<rich:panel bodyClass="rich-laguna-panel-no-header" style="background-color: #E8E8E8;">
							<div id="idDestinatarioPanel" style="padding: 5px;">
								<div>
									<div class="SubTituloControleTexto">
										<span> Endereço Jurisdicionado: </span>
									</div>
									
									<div style="margin-top:10px;">
										<span class="Padrao">CEP:</span>
										<span>
											<h:inputText style="margin-left: 60px;" size="9" maxlength="8"
												value="#{beanManterPessoaCarga.enderecoJurisdicionado.cep}"
												onchange="document.getElementById('bntCep').click();"
												onkeypress="return mascaraInputNumerico(this, event)">
											</h:inputText>
											<div id="dvAguarde" class="PadraoAguarde"></div>
										</span>
									</div>
								
									<div style="margin-top:10px;">
										<span class="Padrao">* Endereço:
											<h:inputText style="margin-left: 20px;" size="85" id="inputEndereco"
												value="#{beanManterPessoaCarga.enderecoJurisdicionado.logradouro}"
												onkeyup="caixaAlta(this);"/>
										</span>
									</div>	
									
									<div style="margin-top:10px;">
										<span class="Padrao">Complemento:
											<h:inputText style="margin-left: 5px;" size="50" id="inputComplemento"
												value="#{beanManterPessoaCarga.enderecoJurisdicionado.complemento}"
												onkeyup="caixaAlta(this);"/>
										</span>
										<span class="Padrao">* Número:
											<h:inputText style="margin-left: 5px;" size="5" maxlength="5"
												value="#{beanManterPessoaCarga.enderecoJurisdicionado.numero}"/>
										</span>
									</div>
									
									<div style="margin-top:10px;">
										<span class="Padrao">Bairro:
											<h:inputText style="margin-left: 49px;" size="35" id="inputBairro"
												value="#{beanManterPessoaCarga.enderecoJurisdicionado.bairro}"
												onkeyup="caixaAlta(this);"/>
										</span>
										<span class="Padrao">* Cidade:
											<h:inputText style="margin-left: 5px;" size="35" id="inputCidade"
												value="#{beanManterPessoaCarga.enderecoJurisdicionado.municipio}"
												onkeyup="caixaAlta(this);"/>
										</span>
									</div>
										
									<div style="margin-top:10px;">
										<span class="Padrao">* UF:
											<h:selectOneMenu style="margin-left: 10px;"
												value="#{beanManterPessoaCarga.enderecoJurisdicionado.UF}">
												<f:selectItems value="#{beanManterPessoaCarga.itensUf}"/>
											</h:selectOneMenu>	
										</span>
										<span style="margin-left:40px">
											<a4j:commandButton value="Adicionar Endereço" styleClass="botao"
												style="width: 130px;" rendered="#{!beanManterPessoaCarga.renderizaBotaoAlterarEndereco}"
												actionListener="#{beanManterPessoaCarga.adicionarEnderecoAction}" 
												reRender="pnlCentralCadastroDiv"/>
											<a4j:commandButton value="Alterar Endereço" styleClass="botao"
												style="width: 130px;" rendered="#{beanManterPessoaCarga.renderizaBotaoAlterarEndereco}"
												actionListener="#{beanManterPessoaCarga.adicionarEnderecoAction}" 
												reRender="pnlCentralCadastroDiv"/>
											<a4j:commandButton value="Novo Endereço" styleClass="botao"
												style="width: 130px;" rendered="#{beanManterPessoaCarga.renderizaBotaoAlterarEndereco}"
												actionListener="#{beanManterPessoaCarga.novoEnderecoAction}" 
												reRender="pnlCentralCadastroDiv"/>
										</span>
									</div>
								</div>
							</div>
							<a4j:outputPanel ajaxRendered="true" keepTransient="false"
								id="tabEndereco" styleClass="MolduraInterna"
								rendered="#{not empty beanManterPessoaCarga.listaEndereco}">
									<rich:dataTable headerClass="DataTableDefaultHeader" id="tabelaEndereco"
										styleClass="DataTableDefault"
										footerClass="DataTableDefaultFooter"
										rowClasses="DataTableRow, DataTableRow2"
										columnClasses="trintaCenter, vinteCenter, dezCenter, vinteCenter, dezCenter, dezCenter"
										value="#{beanManterPessoaCarga.listaEndereco}"
										var="wrappedEndereco"
										binding="#{beanManterPessoaCarga.tabelaEndereco}">
											
											<rich:column>
												<f:facet name="header">
													<h:outputText value="Nome" />
												</f:facet>
												<h:outputText value="#{wrappedEndereco.wrappedObject.jurisdicionado.nome}" />
											</rich:column>
											
											<rich:column>
												<f:facet name="header">
													<h:outputText value="Endereço" />
												</f:facet>
												<h:outputText value="#{wrappedEndereco.wrappedObject.logradouro} #{wrappedEndereco.wrappedObject.complemento} #{wrappedEndereco.wrappedObject.numero} - #{wrappedEndereco.wrappedObject.bairro}" />
											</rich:column>
											
											<rich:column>
												<f:facet name="header">
													<h:outputText value="Cidade" />
												</f:facet>				
												<h:outputText value="#{wrappedEndereco.wrappedObject.municipio} - #{wrappedEndereco.wrappedObject.UF}" />
											</rich:column>
											
											<rich:column>
												<f:facet name="header">
													<h:outputText value="CEP" />
												</f:facet>				
												<h:outputText value="#{wrappedEndereco.wrappedObject.cep}" />
											</rich:column>
											
											<rich:column>
												<f:facet name="header">
													<h:outputText value="Ações" />
												</f:facet>
												<a4j:commandButton value="Alterar" styleClass="PadraoLink"
													actionListener="#{beanManterPessoaCarga.alterarEnderecoAction}" 
													reRender="pnlCentralCadastroDiv"/>
												<h:outputText value="/ "/>	
												<a4j:commandButton value="Excluir" styleClass="PadraoLink"
													actionListener="#{beanManterPessoaCarga.excluirEnderecoAction}" 
													reRender="pnlCentralCadastroDiv"/>	
											</rich:column>					
									</rich:dataTable>
								</a4j:outputPanel>	
							</rich:panel>
							 <rich:spacer width="1" height="5"/>
							<rich:panel bodyClass="rich-laguna-panel-no-header" style="background-color: #E8E8E8;">						
								<div id="idDestinatarioContato" style="padding: 5px;">
									<div>
										<div class="SubTituloControleTexto">
											<span> Telefone: </span>
										</div>
									
										<div style="margin-top:10px;">
											<span class="Padrao">* Tipo de telefone:
												<h:selectOneMenu style="margin-left: 10px;"
													value="#{beanManterPessoaCarga.telefoneJurisdicionado.tipoTelefone}">
													<f:selectItems value="#{beanManterPessoaCarga.itensTipoTelefone}"/>
												</h:selectOneMenu>	
											</span>
											<span class="Padrao">Telefone:
												<h:inputText style="margin-left: 5px;" size="2" maxlength="2" id="inputCodTel"
													value="#{beanManterPessoaCarga.telefoneJurisdicionado.DDD}"
													onkeypress="return mascaraInputNumerico(this, event)"/>
												<h:inputText style="margin-left: 2px;" size="11"  maxlength="10" id="inputTelefone" 
													onkeydown="mascaraTelefone(this);"
													value="#{beanManterPessoaCarga.telefoneJusHifen}">
												</h:inputText>
											</span>
											<span>
												<a4j:commandButton value="Adicionar Telefone" styleClass="botao"
													style="width: 130px;" rendered="#{!beanManterPessoaCarga.renderizaBotaoAlterarTelefone}"
													actionListener="#{beanManterPessoaCarga.adicionarTelefoneAction}" 
													reRender="pnlCentralCadastroDiv"/>
												<a4j:commandButton value="Alterar Telefone" styleClass="botao"
													style="width: 130px;" rendered="#{beanManterPessoaCarga.renderizaBotaoAlterarTelefone}"
													actionListener="#{beanManterPessoaCarga.adicionarTelefoneAction}" 
													reRender="pnlCentralCadastroDiv"/>	
												<a4j:commandButton value="Novo Telefone" styleClass="botao"
													style="width: 130px;" rendered="#{beanManterPessoaCarga.renderizaBotaoAlterarTelefone}"
													actionListener="#{beanManterPessoaCarga.novoTelefoneAction}" 
													reRender="pnlCentralCadastroDiv"/>												
											</span>
										</div>
									</div>
								</div>
								<a4j:outputPanel ajaxRendered="true" keepTransient="false"
									id="tabContato" styleClass="MolduraInterna"
									rendered="#{not empty beanManterPessoaCarga.listaTelefone}">
									<rich:dataTable headerClass="DataTableDefaultHeader" id="tabelaTelefone"
										styleClass="DataTableDefault" width="70%"
										footerClass="DataTableDefaultFooter"
										rowClasses="DataTableRow, DataTableRow2"
										columnClasses="trintaCenter, dezCenter, vinteCenter, dezCenter"
										value="#{beanManterPessoaCarga.listaTelefone}"
										var="wrappedTelefone"
										binding="#{beanManterPessoaCarga.tabelaTelefone}">
										
										<rich:column>
											<f:facet name="header">
												<h:outputText value="Tipo" />
											</f:facet>
											<h:outputText value="#{wrappedTelefone.wrappedObject.tipoTelefone.descricao}" />
															
										</rich:column>
										
										<rich:column>
											<f:facet name="header">
												<h:outputText value="DDD" />
											</f:facet>
											<h:outputText value="#{wrappedTelefone.wrappedObject.DDD}" />
										</rich:column>
										
										<rich:column>
											<f:facet name="header">
												<h:outputText value="Telefone" />
											</f:facet>
											<h:outputText value="#{wrappedTelefone.wrappedObject.numero}" />
										</rich:column>
										
										<rich:column>
											<f:facet name="header">
												<h:outputText value="Ações" />
											</f:facet>	
											<a4j:commandButton value="Alterar" styleClass="PadraoLink"
												actionListener="#{beanManterPessoaCarga.alterarTelefoneAction}"
												reRender="pnlCentralCadastroDiv" />
											<h:outputText value="/ "/>	
											<a4j:commandButton value="Excluir" styleClass="PadraoLink"
												actionListener="#{beanManterPessoaCarga.excluirTelefoneAction}" 
												reRender="pnlCentralCadastroDiv"/>	
										</rich:column>					
									</rich:dataTable>
								</a4j:outputPanel>
							</rich:panel>							
							<div style="margin-top:10px;">
								<span>
									<a4j:commandButton value="Salvar" styleClass="BotaoPadrao" reRender="pnlCentral"
										rendered="#{!beanManterPessoaCarga.renderizaBotaoAlterarJurisdicionado}"
										actionListener="#{beanManterPessoaCarga.salvarJurisdicionadoAction}"
										oncomplete="localizaAncora();"/>
									<a4j:commandButton value="Alterar" styleClass="BotaoPadrao" reRender="pnlCentral"
										rendered="#{beanManterPessoaCarga.renderizaBotaoAlterarJurisdicionado}"
										actionListener="#{beanManterPessoaCarga.salvarJurisdicionadoAction}"
										onclick="exibirMsgProcessando(true);" oncomplete="exibirMsgProcessando(false);"/>	
									<a4j:commandButton value="Limpar" styleClass="BotaoPadrao" 
										actionListener="#{beanManterPessoaCarga.limparTelaCadastrarJurisdicionadoAction}"
										reRender="pnlCentral"/>
									<a4j:commandButton value="Fechar" styleClass="BotaoPadrao" 
										actionListener="#{beanManterPessoaCarga.fechaTelaCadastrarJurisdicionadoAction}"
										reRender="pnlCentral" oncomplete="desabilitaBotaoPesquisar();"/>
								</span>
							</div>
						</a4j:outputPanel>	
					</h:panelGrid>
			</h:panelGrid>
			<a4j:commandButton id="bntCep" actionListener="#{beanManterPessoaCarga.recuperarEnderecoCepAction}" styleClass="BotaoOculto" 
				reRender="pnlCentralCadastroDiv" oncomplete="aguarde(false, 'dvAguarde');" onclick="aguarde(true, 'dvAguarde');"/>	
			<a4j:commandButton id="bntCPF" actionListener="#{beanManterPessoaCarga.verificaSeExisteJurisdicionadoAction}" 
				onchange="if(#{beanManterPessoaCarga.existeCadastro}) {if (confirm('Este CPF já está cadastrado na base. Deseja alterar o cadastro?.')) 
					{document.getElementById('alterarCadastroJurisId').click();} else {document.getElementById('limparTelaCadastroID').click();}} else {return;}"
				styleClass="BotaoOculto" reRender="pnlCentralCadastroDiv, idExisteCadastro" oncomplete="document.getElementById('inputRgID').focus();"/>
			<a4j:commandButton id="alterarCadastroJurisId" reRender="pnlCentral" styleClass="BotaoOculto" 
				actionListener="#{beanManterPessoaCarga.alterarJurisdicionadoAction}"/>	
			<a4j:commandButton id="limparTelaCadastroID" reRender="pnlCentral" styleClass="BotaoOculto" 
				actionListener="#{beanManterPessoaCarga.limparTelaCadastrarJurisdicionadoAction}"/>							
		</a4j:form>	
		
		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>