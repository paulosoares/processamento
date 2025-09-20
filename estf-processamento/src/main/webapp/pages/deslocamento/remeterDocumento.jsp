<%@ page contentType="text/html;charset=ISO-8859-1"%>
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
				<jsp:param name="nomePagina" value="Remeter Documento(s)" />
			</jsp:include>
			<a name="ancora"></a>
		</h:form>

		<a4j:form id="form" prependId="false" onreset="">

			<script type="text/javascript">

				function desabilitarNovoDeslocamento(){
					 if (document.getElementById("hidSucesso").value == "S") {
						 tratarPosDeslocamento();
					 } else {
						 limparTela();
					 }
				}
			
				function aguarde(mostrar, div){
				      if( mostrar == true ){
				            document.getElementById(div).innerHTML = '<img src="/processamento/images/loading.gif" /><font class="Padrao">&nbsp;&nbsp;Aguarde...</font>';
				      }
				}
				
				function caixaAlta (campo) {
					campo.value = campo.value.toUpperCase();
				}
			
				function limpar () {
					document.getElementById('btLimpar').click();
				}

				function limparCampo (campo) {
					document.getElementById(campo).value = '';
				}
			
				// função chamada pelo botão "Novo deslocamento" quando se deseja um novo deslocamento
				function limparTela() {
					
				 	document.getElementById('itNumeroGuia').value = '';
					document.getElementById('itAnoGuia').value = '';
					document.getElementById('chkPostal').checked = 0; 
					document.getElementById('itDescricaoDestinatario').value = '';
					document.getElementById('itDocumento').value = '';

					document.getElementById("chkPostal").disabled = true;
				 	document.getElementById("btnImprimirGuia").disabled = true;
				 	document.getElementById("btnImprimirGuia").className = 'BotaoPadraoEstendidoInativo';

				 	document.getElementById("btnDeslocar").className = 'BotaoPadrao';
				 	document.getElementById("btnDeslocar").disabled = false;

				 	document.getElementById("btnRemoverItem").className = 'BotaoPadrao';
				 	document.getElementById("btnRemoverItem").disabled = false;

				 	document.getElementById("itObservacao").value = '';

				 	// desabilitar o botão novo deslocamento, após clicá-lo
				 	document.getElementById("btnNovoDeslocamento").className = 'BotaoPadraoEstendidoInativo';
				 	document.getElementById("btnNovoDeslocamento").disabled = true;
					exibeEscondeGuia(false);
				}
				
                function focoInsercao() {
					document.getElementById('itDocumento').value = '';
					document.getElementById('itDocumento').focus();
                }
                
                function finalizaInsercao() {
					document.getElementById('itDocumento').value = '';
					document.getElementById('itDocumento').focus();
               	 	exibirMsgProcessando(false);
                }

 				 // tratar o comportamento da tela após o deslocamento
				 function tratarPosDeslocamento(){
					 if (document.getElementById("hidSucesso").value == "S") {
						// se a remessa a setores do STF estiver marcada não deverá habilitar o Postal 
					 	if (document.getElementById('hidTipoDestino').value == 'SET') {
						 	document.getElementById("chkPostal").disabled = true;
						} else {
						 	document.getElementById("chkPostal").disabled = false;
						}
					 	document.getElementById("btnImprimirGuia").disabled = false;
					 	document.getElementById("btnImprimirGuia").className = 'BotaoPadraoEstendido';

					 	document.getElementById("btnDeslocar").className = 'BotaoPadraoInativo';
					 	document.getElementById("btnDeslocar").disabled = true;

					 	Richfaces.showModalPanel('modalPanelGuia');
						// habilitar o botão "Novo Deslocamento" após um deslocamento					 						 	
					 	document.getElementById("btnNovoDeslocamento").className = 'BotaoPadraoEstendido';
					 	document.getElementById("btnNovoDeslocamento").disabled = false;
					 	exibeEscondeGuia(true);
					 }
				 }

                 function limpaDocumento() {
					 document.getElementById('itDocumento').value = '';
                 }
                 
                 function finalizaDeslocamento() {
					 document.getElementById('itDocumento').value = '';
					 document.getElementById("btnDeslocarPrincipal").disabled = false;
                	 exibirMsgProcessando(false);
                 }

                 function criticaDestinoDifereRelator(){
					if (document.getElementById('hidDestinoDiferenteRelator').value == 'S') {
						var processosCriticados = document.getElementById('hidProcessosCriticaRelator').value; 
			    		var processos = processosCriticados.split(',');
			    		processosCriticados='';
			    		for (i=0;i<processos.length;i++)
			    		  {processosCriticados = processosCriticados + processos[i] + '\n';}
					    if (!confirm("O(s) seguinte(s) processo(s) possue(m) relatoria diferente do Gabinete escolhido para deslocamento:"+ processosCriticados +"Deseja continuar assim mesmo?")) { 
							return false;		
				    	} else {
							return true;
						}
					} else {
						return true;
					}	
                 }
                 
				 function criticaPetPendenteTratamento(){
					if (document.getElementById('hidPeticoesPendentesDeTratamento').value == 'S') {
						var processosCriticados = document.getElementById('hidProcessosCriticaPetPendente').value; 
				    	var processos = processosCriticados.split(',');
				    	processosCriticados='';
				    	for (i=0;i<processos.length;i++)
				    	  {processosCriticados = processosCriticados + processos[i] + '\n';}
					    if (!confirm("O(s) seguinte(s) processo(s) possue(m) peti\u00e7\u00f5es vinculadas pendentes de tratamento no eSTF-Gabinetes:"+ processosCriticados +
					    		document.getElementById('hidMensagemConfirmacao').value)) { 
							return false;		
				    	} else {
							return true;
						}
					} else {
						return true;
					}	
				 }
				 
				 function criticaPetNaoJuntada() {
					if (document.getElementById('hidPeticoesNaoJuntadas').value == 'S') {
						var processosCriticados = document.getElementById('hidProcessosCriticaNaoJuntadoPet').value; 
				    	var processos = processosCriticados.split(',');
				    	processosCriticados='';
				    	for (i=0;i<processos.length;i++)
				    	  {processosCriticados = processosCriticados + processos[i] + '\n';}
					    if (!confirm("Existem peti\u00e7\u00f5es vinculadas a este(s) processo(s) que n\u00e3o foram juntadas ou n\u00e3o possuem andamento de interposi\u00e7\u00e3o:"+ processosCriticados +
					    		document.getElementById('hidMensagemConfirmacao').value)) { 
							return false;		
				    	} else {
							return true;
						}
					} else {
						return true;
					}	
				 }

				 function criticaDeslocamento() {
					 var critica1 = criticaDestinoDifereRelator();
					 var critica2 = criticaPetPendenteTratamento();
					 var critica3 = criticaPetNaoJuntada();
					 if (critica1 && critica2 && critica3) {
						 return true;
					 } else {
						 return false;
					 }
				 }

				 // função ação para o botão 'deslocar'. Dispara o actionListener do botão btnDeslocarOculto. 
				 function deslocar() {
					 if (criticaDeslocamento() == true) {
						 	if (document.getElementById("btnDeslocarPrincipal").disabled == false) {
								document.getElementById('btnDeslocarOculto').onclick();
						 	}
						 	document.getElementById("btnDeslocarPrincipal").disabled = true;
					  } else {
							document.getElementById("btnDeslocarPrincipal").disabled = false;
						 	exibirMsgProcessando(false);
					  }
				 }
				 
				 function verificaPendencias(param) {
					if (param) {
						 Richfaces.showModalPanel('modalPanelConfirmacaoRegistro')
						 return true;
					}
					document.getElementById("btnDeslocarPrincipal").disabled = false;
					return false;
				}
				 
				 // mostrar/esconder agrupador guia
				 function exibeEscondeGuia(exibir){
					 if (exibir) {
						 document.getElementById('panelGuiaPostal').style.visibility = 'visible';
					 } else {
						 document.getElementById('panelGuiaPostal').style.visibility = 'hidden';
					 }
				 }
				 
				 function insereItem(e) {
					if (e.keyCode=='13') {
//						document.getElementById('btnOcultoInserirDocumento').onclick();
						document.getElementById('btnIncluirDocumento').onclick();
					}
					
				 }
				 
				// funções que tratam do endereço para baixa 
				function atualizaMensagem() {
					// se tem um endereço selecionado, então será apresentado
 					document.getElementById('labelEndereco').style.paddingBottom="0cm";
 					document.getElementById('labelEndereco').style.margin="0px 0px";
					document.getElementById('labelEndereco').style.visibility = 'visible'; 

					document.getElementById('botaoRemoverEndereco').style.visibility = 'visible';
					document.getElementById('linkEndereco').style.visibility = 'visible';
					document.getElementById('botaoRemoverEndereco').style.visibility = 'visible';

					Richfaces.hideModalPanel('modalPanelSelecaoEndereco');
				}
				
				function cancelaSelecaoEndereco() {
					Richfaces.hideModalPanel('modalPanelSelecaoEndereco');
				}
				
				function visibilidadeEndereco(){
					 if (document.getElementById('hidTipoDestino').value == 'ORG') {
						 document.getElementById('linkEndereco').style.visibility = 'visible';
					 } else {
						 document.getElementById('linkEndereco').style.visibility = 'hidden';
					 }
					 focoInsercao();
				}
				
				function limitTextArea(element, limit) {

					if (element.value.length > limit) {
					   alert('Limite do campo foi excedido.');
					   element.value = element.value.substring(0, limit);
					}

				}

			</script>


			<!-- corpo principal da página -->

			<div style="width:100%">
				<h:outputText id="labelDescricaoGuia" 
				              styleClass="PainelTituloCriaTexto" 
				              value="Remeter: #{beanRemeterDocumento.exibeLabelGuia}" /> 
			</div>
			
			<h:panelGrid columns="1" id="pnlPesquisa" style="vertical-align:top" columnClasses="PainelTop, PainelTop">
				<t:panelGroup id="panelGuiaPostal" style="visibility:hidden;">
					<h:outputText styleClass="Padrao" value="Guia:" />
					<h:panelGrid columns="2" styleClass="Moldura">
						<h:panelGrid columns="3">
							<h:inputText id="itNumeroGuia" size="20" disabled="true"
								value="#{beanRemeterDocumento.numGuia}" />
							<h:outputText styleClass="Padrao" value="/" />
							<h:inputText id="itAnoGuia" size="10" disabled="true"
								value="#{beanRemeterDocumento.anoGuia}" />
						</h:panelGrid>

						<h:panelGrid columns="4">
							<h:outputLabel styleClass="Padrao" value="Postal: "
								for="chkPostal" />
							<h:selectBooleanCheckbox id="chkPostal"
								disabled="#{beanRemeterDocumento.habilitarPostal}"
								value="#{beanRemeterDocumento.postal}" />
							<a4j:commandButton disabled="false" value="Visualizar impressão"
								styleClass="BotaoPadraoEstendido"
								id="btnImprimirGuia" 
								onclick="document.getElementById('abrePdf').click()" />

							<h:commandLink value="Abrir em nova aba" id="abrePdf" styleClass="BotaoOculto" 
								action="#{beanRemeterDocumento.gerarPDFGuiaInLine}" target="_blank" />
							
						</h:panelGrid>
					</h:panelGrid>
				</t:panelGroup>

			</h:panelGrid>

			<h:panelGrid columns="4" id="panelDestino">
				<h:outputText styleClass="Padrao" value="* Destino:"
					id="labelDestino" />
				<h:inputText id="itDescricaoDestinatario" size="100" 
					onblur="ga('send', 'event', 'Deslocamento - Remeter - #{beanUsuario.usuario.name}', 'Destino', '#{beanUsuario.usuario.setor.sigla}' )"
					disabled="#{beanRemeterDocumento.habilitaAdicao}"
					value="#{beanRemeterDocumento.nomDestinatario}"
					onchange="if ( this.value!='' ) { #{rich:component('sbSetor')}.callSuggestion(true) }">
					<a4j:support event="onblur"
						actionListener="#{beanRemeterDocumento.atualizarAction}" />
				</h:inputText>
				
				<!--  link para selecionar o endereço no caso de Baixa -->
				<h:panelGrid id="linkEndereco" style="visibility:hidden;"> 
					<a4j:commandLink id="btnEndereco" actionListener="#{beanRemeterDocumento.limparPopupEnderecoAction}" 
									 reRender="modalPanelSelecaoEndereco"
									 disabled="#{beanRemeterDocumento.habilitaAdicao}" 
					                 oncomplete="Richfaces.showModalPanel('modalPanelSelecaoEndereco')" 
					                 value="Selecionar endereço"/>
				</h:panelGrid>

				<rich:suggestionbox id="sbSetor" height="200" width="500"
					for="itDescricaoDestinatario"
					suggestionAction="#{beanRemeterDocumento.pesquisarOrigem}"
					var="destino" nothingLabel="Nenhum registro encontrado">
					<h:column>
						<h:graphicImage value="#{destino.urlIcone}" id="icone" style="vertical-align: middle;" />
						<h:outputText
							value="#{destino.origemDestino.id} - #{destino.origemDestino.descricao}" />
					</h:column>

					<a4j:support ajaxSingle="true" event="onselect" reRender="linkEndereco, hidTipoDestino" oncomplete="visibilidadeEndereco()"
						eventsQueue="ajaxQueue" ignoreDupResponses="true">
						<f:setPropertyActionListener value="#{destino.origemDestino.id}"
							target="#{beanRemeterDocumento.codigoDestinatario}" />
						<f:setPropertyActionListener
							value="#{destino.origemDestino.tipoOrigemDestino}"
							target="#{beanRemeterDocumento.tipoDestino}" />
						<f:setPropertyActionListener
							value="#{destino.origemDestino.descricao}"
							target="#{beanRemeterDocumento.descDestino}" />
						<f:setPropertyActionListener
							value="#{destino.origemDestino.descricao}"
							target="#{beanRemeterDocumento.nomDestinatario}" />
					</a4j:support>
				</rich:suggestionbox>

			</h:panelGrid>
			
			<!--  label com o endereço selecionado no caso de Baixa e Expedição-->
			<h:panelGrid id="labelEndereco" style="visibility:hidden; height:0; margin:0px 0px 0px;" columns="3">
				<rich:panel header="#{beanRemeterDocumento.panelHeader}" 
				            styleClass="Padrao" id="panelDetalheDestinatario" >
				        <h:panelGrid id="gridEndereco" rendered="#{beanRemeterDocumento.temEndereco}" styleClass="Padrao">
				        	<h:panelGrid columns="2">    
								<h:outputText value="#{beanRemeterDocumento.panelLogradouro}, " styleClass="Padrao" style="line-height: 7px" />
								<h:outputText value="#{beanRemeterDocumento.panelNumLocalizacao}" styleClass="Padrao" style="line-height: 7px"/>
							</h:panelGrid>
							<h:panelGrid columns="1">
								<h:outputText value="#{beanRemeterDocumento.panelBairro}" styleClass="Padrao" style="line-height: 7px"/>
							</h:panelGrid>
				        	<h:panelGrid columns="2">    
								<h:outputText value="#{beanRemeterDocumento.panelMunicipio} - " styleClass="Padrao" style="line-height: 7px"/>
								<h:outputText value="#{beanRemeterDocumento.panelUf}" styleClass="Padrao" style="line-height: 7px"/>
							</h:panelGrid>
							<h:panelGrid columns="1">
								<h:outputText value="#{beanRemeterDocumento.panelCep}" styleClass="Padrao" style="line-height: 7px"/>
							</h:panelGrid>
						</h:panelGrid>
						<h:outputText value="Nenhum endereço selecionado para o destinatário." styleClass="Padrao" id="semEndereco" 
						              rendered="#{not beanRemeterDocumento.temEndereco}" />
				</rich:panel>
				<a4j:commandButton id="botaoRemoverEndereco" image="../../images/remove.gif"
									title="Remove o endereço selecionado"
									actionListener="#{beanRemeterDocumento.limpaEnderecoSelecionado}"
									oncomplete="document.getElementById('botaoRemoverEndereco').style.visibility = 'hidden'"
									reRender="panelDetalheDestinatario"
									style="visibility:hidden; border:0" disabled="#{beanRemeterDocumento.habilitaAdicao}" />
			</h:panelGrid>

		</a4j:form>

		<!-- tela para registrar os itens do deslocamento -->

		<h:panelGrid columns="1" id="pnlDeslocamento" style="width:100%; margin:0px; 0px; padding-top: 0px;">


			<a4j:form prependId="false">

				<!--  campos escondidos -->
				<h:inputHidden value="#{beanRemeterDocumento.tipoRemessa}"	id="hidTipoDestino" />
				<h:inputHidden value="#{beanRemeterDocumento.sucesso}"	id="hidSucesso" />
				<!--  relação de processos criticados por cada crítica -->
				<h:inputHidden value="#{beanRemeterDocumento.processosCriticaRelator}"	id="hidProcessosCriticaRelator" />
				<h:inputHidden value="#{beanRemeterDocumento.processosCriticaPetPendente}"	id="hidProcessosCriticaPetPendente" />
				<h:inputHidden value="#{beanRemeterDocumento.processosCriticaNaoJuntadoPet}"	id="hidProcessosCriticaNaoJuntadoPet" />
				<!--  flags (S/N) que indicam se ocorreu a crítica -->
				<h:inputHidden value="#{beanRemeterDocumento.destinoDiferenteRelator}"	id="hidDestinoDiferenteRelator" />
				<h:inputHidden value="#{beanRemeterDocumento.peticoesPendentesDeTratamento}"	id="hidPeticoesPendentesDeTratamento" />
				<h:inputHidden value="#{beanRemeterDocumento.peticoesNaoJuntadas}"	id="hidPeticoesNaoJuntadas" />
				<!--  mensagem apropiada à critica -->
				<h:inputHidden value="#{beanRemeterDocumento.mensagemConfirmacao}"	id="hidMensagemConfirmacao" />


				<a4j:outputPanel id="pnlProcesso" style="padding-top: 5px;">
					<f:verbatim>
						<fieldset style="width: 10%; float: left;">
							<legend>Inclusão por</legend>
							<h:selectOneRadio 
								onchange="ga('send', 'event', 'Deslocamento - Remeter - #{beanUsuario.usuario.setor.sigla}', 'Inclusão por Processo/Importação - Radiobutton', 'this.value' )"
								value="#{beanRemeterDocumento.tipoInclusao}"
								layout="pageDirection" id="tipoInclusao">
								<f:selectItem itemValue="PROCESSO" itemLabel="Processo" />
								<f:selectItem itemValue="IMPORTACAO" itemLabel="Importação" />
								<a4j:support event="onclick"
									reRender="pnlProcesso"
									actionListener="#{beanRemeterDocumento.atualizarTipoInclusao}" />
							</h:selectOneRadio>

						</fieldset>
					</f:verbatim>
					
					<h:panelGrid columns="2"  style="#{beanRemeterDocumento.estiloVisibilidadeProcesso}">
						<t:div>
							<span class="Padrao" style="margin:0px"> 
								<h:outputText styleClass="Padrao"
									value="* Processo/Petição: " id="labelDocumento" /> 
								
								<!--  caixa de texto para o documento (processo ou petição) -->
								<h:inputText id="itDocumento" size="30"
									onblur="ga('send', 'event', 'Deslocamento - Remeter - #{beanUsuario.usuario.username}', 'Inclusão por Processo - Processo/Petição', '#{beanUsuario.usuario.setor.sigla}' )"
									disabled="#{beanRemeterDocumento.habilitaAdicao}"
									value="#{beanRemeterDocumento.idDocumento}" />
								<rich:hotKey
									selector="#itDocumento" key="return"
									handler="document.getElementById('btnIncluirDocumento').onclick()" />
							</span>
						</t:div>
						<a4j:commandButton styleClass="BotaoMais" id="btnIncluirDocumento"
							onblur="ga('send', 'event', 'Deslocamento - Remeter - #{beanUsuario.usuario.username}', 'Inclusão por Processo - Botão +', '#{beanUsuario.usuario.setor.sigla}')"
							title="Inclui o processo ou petição digitado."
							actionListener="#{beanRemeterDocumento.insereItemDocumentoAction}"
							onclick="exibirMsgProcessando(true);"
							disabled="#{ beanRemeterDocumento.habilitaAdicao }"
							oncomplete="finalizaInsercao()" reRender="labelDescricaoGuia, pnlItemDeslocamento, modalPanelConfirmacaoRegistro, btnDeslocar" />
						<rich:spacer height="30px"/>
					</h:panelGrid>													
					
					<h:panelGrid style="#{beanRemeterDocumento.estiloVisibilidadeImportacao}">
					
						<a4j:commandButton styleClass="BotaoPadraoEstendido" id="btnImportarLista"
							onblur="ga('send', 'event', 'Deslocamento - Remeter - #{beanUsuario.usuario.username}', 'Inclusão por Importação - Importar lista', '#{beanUsuario.usuario.setor.sigla}' )"
							title="Importar lista exportada pelo eGab." value="Importar lista"
							actionListener="#{beanRemeterDocumento.processarImportacaoListaExportadaPeloEGab}"
							disabled="#{ beanRemeterDocumento.habilitaAdicao }"		
							onclick="exibirMsgProcessando(true);"				
							oncomplete="finalizaInsercao(); exibirMsgProcessando(false);"
							reRender="pnlInformacaoImportacao" />
						<rich:spacer height="28px"/>							
					</h:panelGrid>							
					
				</a4j:outputPanel>				

				<!-- tabelas populadas com os itens para deslocamento -->

				<a4j:outputPanel ajaxRendered="true" keepTransient="false"
					id="pnlItemDeslocamento" styleClass="MolduraInterna">
					<c:if test="${not empty beanRemeterDocumento.listaDocumento}">
						<hr color="red" align="left" size="1px" width="100%" />

						<!--  se for processo desenha a tabela para lista de processos -->
						<c:if
							test="${beanRemeterDocumento.tipoGuia == 'PRO' or beanRemeterDocumento.tipoGuia == 'PRE'}">

							<rich:dataTable headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="#{beanRemeterDocumento.rowClasses}"
								columnClasses="tres, tres, dezCenter, tres, tres, tres, tres"
								sortMode="single" value="#{beanRemeterDocumento.listaDocumento}"
								var="wrappedDocumento"
								binding="#{beanRemeterDocumento.tabelaDocumentos}"
								id="tabelaProcesso" onRowMouseOver="" onRowMouseOut="">
								<f:facet name="caption">
									<h:outputText
										value="Guia de Deslocamento de #{beanRemeterDocumento.descricaoGuia}"
										styleClass="Padrao" />
								</f:facet>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Excluir item" />
									</f:facet>

									<a4j:commandButton image="../../images/remove.gif"
										actionListener="#{beanRemeterDocumento.removerItemAction}"
										rendered="#{not beanRemeterDocumento.isApenso}"
										reRender="pnlItemDeslocamento, labelDescricaoGuia"
										disabled="#{beanRemeterDocumento.habilitaAdicao}" style="border:0" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Processo" />
									</f:facet>


									<h:graphicImage rendered="#{beanRemeterDocumento.isApenso}"
										value="../../images/seta.png" id="iconeApenso"
										style="vertical-align: middle;" />

									<h:outputText
										rendered="#{wrappedDocumento.wrappedObject.principal.eletronico}"
										value="e" style="color: red; font-weight: bold;" />
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{wrappedDocumento.wrappedObject.siglaClasseProcessual}" />
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value=" " />
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{wrappedDocumento.wrappedObject.numeroProcessual}" />

									<a4j:commandLink styleClass="Padrao" style="text-align:center;"
										rendered="#{not empty beanRemeterDocumento.totalVinculos}"
										actionListener="#{beanRemeterDocumento.insereApensosNaLista}"
										reRender="tabelaProcesso">
										<h:graphicImage
											value="#{beanRemeterDocumento.imagemMaisMenos}" id="icone"
											style="vertical-align: middle; padding-left: 10px;" />
									</a4j:commandLink>

								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Relator" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{wrappedDocumento.wrappedObject.ministroRelatorAtual.nome}" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Qtd Volume" />
									</f:facet>

									<h:outputText styleClass="Padrao" style="text-align:center;"
										rendered="#{not empty wrappedDocumento.wrappedObject.quantidadeVolumes}"
										value="#{wrappedDocumento.wrappedObject.quantidadeVolumes}" />
									<h:outputText styleClass="Padrao" style="text-align:center;"
										rendered="#{empty wrappedDocumento.wrappedObject.quantidadeVolumes}"
										value="0" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Qtd Apenso" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
										rendered="#{not empty wrappedDocumento.wrappedObject.quantidadeApensosFixo}"
										value="#{wrappedDocumento.wrappedObject.quantidadeApensosFixo}" />
									<h:outputText styleClass="Padrao" style="text-align:center;"
										rendered="#{empty wrappedDocumento.wrappedObject.quantidadeApensosFixo}"
										value="0" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Qtd Juntada por Linha" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
										rendered="#{not empty  wrappedDocumento.wrappedObject.quantidadeJuntadasLinha}"
										value="#{wrappedDocumento.wrappedObject.quantidadeJuntadasLinha}" />
									<h:outputText styleClass="Padrao" style="text-align:center;"
										rendered="#{empty  wrappedDocumento.wrappedObject.quantidadeJuntadasLinha}"
										value="0" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Qtde Vinculado" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{beanRemeterDocumento.quantidadeVinculados}" />
								</rich:column>

							</rich:dataTable>
						</c:if>

						<!--  se for petição desenha a tabela para lista de petições -->
						<c:if test="${beanRemeterDocumento.tipoGuia == 'PET' or beanRemeterDocumento.tipoGuia == 'PEE'}">
							<rich:dataTable headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="tres, tres, tres" sortMode="single"
								value="#{beanRemeterDocumento.listaDocumento}"
								var="wrappedDocumento"
								binding="#{beanRemeterDocumento.tabelaDocumentos}"
								id="tabelaPeticao">
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Excluir item" />
									</f:facet>
									<a4j:commandButton image="../../images/remove.gif"
										actionListener="#{beanRemeterDocumento.removerItemAction}"
										reRender="pnlItemDeslocamento,labelDescricaoGuia"
										disabled="#{beanRemeterDocumento.habilitaAdicao}" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Petição" />
									</f:facet>
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{wrappedDocumento.wrappedObject.identificacao}" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Processo/Petição vinculado(a)" />
									</f:facet>
									<h:outputText
										rendered="#{beanRemeterDocumento.isProcessoVinculadoEletronico}"
										value="e" style="color: red; font-weight: bold;" />
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{beanRemeterDocumento.processoVinculadoPeticao}" />
									<h:outputText styleClass="Padrao" style="text-align:center;"
										value="#{beanRemeterDocumento.peticaoVinculadaPeticao}" />
								</rich:column>

							</rich:dataTable>
						</c:if>
					</c:if>
				</a4j:outputPanel>

				<!-- campo observação -->
				<a4j:outputPanel>
					<h:panelGrid id="panelObservacao">
						<h:outputText styleClass="Padrao" value="Observação:" />
						<h:inputTextarea id="itObservacao" rows="2" cols="100"   
							onblur="ga('send', 'event', 'Deslocamento - Remeter - #{beanUsuario.usuario.setor.sigla}', 'Observação', 'this.value' )"
							value="#{beanRemeterDocumento.observacao}" onkeyup="limitTextArea(this,1000);" disabled="#{beanRemeterDocumento.habilitaAdicao}"
							onkeydown="limitTextArea(this,1000);" />
					</h:panelGrid>
				</a4j:outputPanel>
				
				<div style="padding-top: 15px;" id="idDivBotoes">
					<span class="Padrao"> 
						<a4j:commandButton
							styleClass="BotaoPadrao" id="btnDeslocar" value="Deslocar"
							onclick="if (verificaPendencias(#{beanRemeterDocumento.mensagemDeRestricaoRegistroDeAndamento!=null})) {return false};"
							actionListener="#{beanRemeterDocumento.validarAction}"
							reRender="hidDestinoDiferenteRelator, hidProcessosCriticaRelator,hidProcessosCriticaPetPendente,
							          hidProcessosCriticaNaoJuntadoPet, hidPeticoesPendentesDeTratamento, hidPeticoesNaoJuntadas, 
							          hidMensagemConfirmacao"
							oncomplete="deslocar();"/> 							
						<a4j:commandButton
							styleClass="BotaoPadraoEstendidoInativo" id="btnNovoDeslocamento"
							actionListener="#{beanRemeterDocumento.limpaSessaoAction}"
							oncomplete="limparTela()" reRender="form, pnlDeslocamento"
							value="Novo Deslocamento" /> 
						<a4j:commandButton
							styleClass="BotaoOculto"
							actionListener="#{beanRemeterDocumento.limpaSessaoAction}"
							id="btnLimpaSessao" /> 
						<a4j:commandButton
							styleClass="BotaoOculto" id="btnDeslocarOculto" value="Deslocar"
							actionListener="#{beanRemeterDocumento.salvarDeslocamentoAction}"
							reRender="pnlPesquisa, hidSucesso, panelGuiaPostal, hidTipoDestino, 
							         itDocumento, itDescricaoDestinatario, botaoRemoverEndereco, btnEndereco, itObservacao"
							oncomplete="exibirMsgProcessando(false); tratarPosDeslocamento();" />
						<a4j:commandButton styleClass="BotaoOculto"
							id="btnOcultoInserirDocumento"
							actionListener="#{beanRemeterDocumento.insereItemDocumentoAction}"
							onclick="exibirMsgProcessando(true);"
							oncomplete="finalizaDeslocamento();" /> 
					</span>
				</div>
				

			</a4j:form>
		</h:panelGrid>

		<!-- popup para impressão da guia gerada no deslocamento -->

		<rich:modalPanel id="modalPanelGuia" width="500" height="200"
			keepVisualState="true" style="overflow:auto">
			<f:facet name="header">
				<h:outputText value="Imprimir Guia de Deslocamento" />
			</f:facet>
			<f:facet name="controls">  
				<h:panelGroup>  
					<h:graphicImage value="../../images/close.gif" styleClass="hidelink" id="hidelink"/>  
					<rich:componentControl for="modalPanelGuia" attachTo="hidelink" operation="hide" event="onclick"/>  
				</h:panelGroup>  
			</f:facet>  
			<a4j:form>
				<a4j:outputPanel ajaxRendered="true" keepTransient="false"
					id="pnlImprimirGuia" styleClass="MolduraInterna">
					<c:if test="${not empty beanRemeterDocumento.numGuia}">
						<t:panelGroup>
							<h:outputText styleClass="Padrao" value="Guia:" />
							<h:panelGrid columns="1" styleClass="Moldura">
								<t:div>
									<h:outputText styleClass="Padrao" value="Número:" />
									<h:inputText id="popup_itNumeroGuia" size="20" disabled="true"
										value="#{beanRemeterDocumento.numGuia}" />
									<h:outputText styleClass="Padrao" value="Ano:" />
									<h:inputText id="popup_itAnoGuia" size="10" disabled="true"
										value="#{beanRemeterDocumento.anoGuia}" />
								</t:div>
							</h:panelGrid>
							<h:panelGrid columns="1" styleClass="Moldura">
								<t:div>
									<span class="Padrao" style="padding-left: 25px;" id="popup_spnPostal"> 
										<h:outputLabel value="Postal: "	for="popup_chkPostal"/> 
										<h:selectBooleanCheckbox value="#{beanRemeterDocumento.postal}" id="popup_chkPostal"
												disabled="#{beanRemeterDocumento.habilitarPostal}" /> 
									</span>
									<h:commandLink value="Visualizar Impressão" id="abrePdfGuia" 
									    action="#{beanRemeterDocumento.gerarPDFGuiaInLine}" target="_blank" />
									<rich:toolTip for="abrePdfGuia" style="width:180px; height:60px;" layout="block"  
									    value="Clique aqui para visualizar a guia. Dica: com o documento em tela utilize
									     as teclas Ctrl+P para imprimi-lo." />
								</t:div>
							</h:panelGrid>
						</t:panelGroup>
					</c:if>
				</a4j:outputPanel>
				<h:panelGrid columns="3">
					<a4j:commandButton value="Fechar" styleClass="BotaoPadrao"
						onclick="Richfaces.hideModalPanel('modalPanelGuia');"
						id="popup_btnFecharImpressao" />
				</h:panelGrid>
			</a4j:form>
		</rich:modalPanel>


		<!-- popup para seleção de endreço -->
		<rich:modalPanel id="modalPanelSelecaoEndereco" width="1000" height="250"
			keepVisualState="true" style="overflow:auto">
			<f:facet name="header">
				<h:outputText value="Selecionar endereço de envio" />
			</f:facet>
			<a4j:form prependId="false" id="formEndereco" >
				<h:panelGrid columns="2" >
					<h:panelGroup>
						<h:outputText styleClass="Padrao" value="Destinatário para baixa e expedição: "
								id="labelDestinoBaixaExpedicao" />
						<h:inputText id="itDescricaoDestinatarioBaixaExpedicao" size="100"
								value="#{beanRemeterDocumento.nomDestinatarioBaixaExpedicao}"
								onchange="if ( this.value!='' ) { #{rich:component('sbDestinatarioBaixaExpedicao')}.callSuggestion(true) }">
							<a4j:support event="onblur"
								actionListener="#{beanRemeterDocumento.atualizarAction}" />
							<a4j:support event="onchange"
								actionListener="#{beanRemeterDocumento.limpaEnderecos}" reRender="tabelaEndereco" />
					    </h:inputText>
				    </h:panelGroup>
				    
				                       
					<rich:suggestionbox id="sbDestinatarioBaixaExpedicao" height="200" width="500"
							for="itDescricaoDestinatarioBaixaExpedicao"
							suggestionAction="#{beanRemeterDocumento.pesquisarDestinatarioBaixaExpedicaoAction}"
							var="destinoBaixaExpedicao" nothingLabel="Nenhum registro encontrado">
						<h:column>
							<h:outputText value="#{destinoBaixaExpedicao.nomDestinatario}" />
						</h:column>
		
						<a4j:support event="onselect" action="#{beanRemeterDocumento.recuperarEnderecos}"  
							eventsQueue="ajaxQueue" ignoreDupResponses="true" reRender="gridMessage, tabelaEndereco">
							<f:setPropertyActionListener 
								value="#{destinoBaixaExpedicao.id}"
								target="#{beanRemeterDocumento.codigoDestinatarioBaixaExpedicao}" />
						</a4j:support>
							
					</rich:suggestionbox>
				</h:panelGrid>
				<!-- tabela contendo os endereços para seleção -->
				<h:panelGrid id="gridMessage">
					<h:outputText value="Não existe endereço cadastrado para o destinatário." style="color:red" rendered="#{beanRemeterDocumento.naoEncontrouEndereco}" />
				</h:panelGrid>
			    <rich:dataTable id="tabelaEndereco" var="item" value="#{beanRemeterDocumento.listaEnderecos}"
								headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								binding="#{beanRemeterDocumento.tabelaEnderecos}"
								sortMode="single">  
			        <rich:column>  
			            <f:facet name="header">
							<h:outputText value="Logradouro" />
			            </f:facet>
						<!-- mostra o logradouro na forma de link se estiver ativo -->				            
		            	<a4j:commandLink value="#{item.logradouro}"  
                          	actionListener="#{beanRemeterDocumento.selecionaEnderecoAction}" 
			            	oncomplete="atualizaMensagem();focoInsercao()" 
			            	reRender="linkEndereco, modalPanelSelecaoEndereco, panelDetalheDestinatario" rendered="#{item.ativo}" />
						<!-- mostra o logradouro sem link se não estiver ativo -->				            
						<h:outputText styleClass="Padrao" style="text-align:center;" value="#{item.logradouro}" rendered="#{not item.ativo}" />
			        </rich:column>  
			        <rich:column>  
			            <f:facet name="header">
							<h:outputText value="CEP" />
			            </f:facet>
						<h:outputText styleClass="Padrao" style="text-align:center;" value="#{item.cep}" />
			        </rich:column>
			        <rich:column>  
			            <f:facet name="header">
							<h:outputText value="Bairro" />
			            </f:facet>
						<h:outputText styleClass="Padrao" style="text-align:center;" value="#{item.bairro}" />
			        </rich:column>
			        <rich:column>
			            <f:facet name="header">
							<h:outputText value="Municipio" />
			            </f:facet>
						<h:outputText styleClass="Padrao" style="text-align:center;" value="#{item.municipio}" />
			        </rich:column>
			        <rich:column>
			            <f:facet name="header">
							<h:outputText value="UF" />
			            </f:facet>
						<h:outputText styleClass="Padrao" style="text-align:center;" value="#{item.uf}" />
			        </rich:column>
			        <rich:column>
			            <f:facet name="header">
							<h:outputText value="Situação" />
			            </f:facet>
						<h:outputText styleClass="Padrao" style="text-align:center;" value="Ativo" rendered="#{item.ativo}" />
						<h:outputText styleClass="Padrao" style="text-align:center;" value="Inativo" rendered="#{not item.ativo}" />
			        </rich:column>

			    </rich:dataTable>
				<h:panelGrid columns="1">
					<a4j:commandButton value="Cancelar" styleClass="BotaoPadrao"					
						id="popup_btnConfirmarSelecao" oncomplete="cancelaSelecaoEndereco()" />
				</h:panelGrid>
			</a4j:form>
		</rich:modalPanel>
		
		<rich:modalPanel id="modalPanelImportacaoProcesso" width="360" height="125" keepVisualState="true">

			<f:facet name="header">
				<h:outputText value="Tela para confirmação de importação de lista de processos" />
			</f:facet>
			<a4j:form prependId="false">
				<a4j:outputPanel id="pnlInformacaoImportacao">
					<div>
						
							<h:panelGrid>
								<h:outputLabel value="Não existe processo a ser importado." 
								               rendered="#{beanRemeterDocumento.quantidadeListaExportadaPeloEGab eq 0}"/>
								<h:outputLabel value="Confirma a importação de #{beanRemeterDocumento.quantidadeListaExportadaPeloEGab} processo(s)?" 
								               rendered="#{beanRemeterDocumento.quantidadeListaExportadaPeloEGab > 0}"/>
							</h:panelGrid>
						
					</div>
	
					<div style="padding-top: 20px; text-align: center">
 						<a4j:commandButton styleClass="BotaoPadrao"
							onclick="Richfaces.hideModalPanel('modalPanelImportacaoProcesso'); exibirMsgProcessando(true); "
							actionListener="#{beanRemeterDocumento.processarImportacaoListaExportadaPeloEGab}"
							reRender="labelDescricaoGuia, pnlItemDeslocamento"
							oncomplete="finalizaInsercao(); exibirMsgProcessando(false);" value="Importar" />	 
	
						<h:commandButton
							onclick="Richfaces.hideModalPanel('modalPanelImportacaoProcesso');"
							styleClass="BotaoPadrao" value="Cancelar" />
					</div>
				</a4j:outputPanel>
			</a4j:form>

		</rich:modalPanel>
		
		<rich:modalPanel id="modalPanelConfirmacaoRegistro" keepVisualState="true" autosized="true" style="overflow:auto" width="600">
			<f:facet name="header">
				<h:outputText value="Confirmação" />
			</f:facet>
			<a4j:form prependId="false">
				<t:div id="idPanelConfirmacaoRegistro">
						<h:outputText escape="false" value="#{beanRemeterDocumento.mensagemDeRestricaoRegistroDeAndamento}" style="white-space: pre-wrap;"></h:outputText>
				</t:div>
				<div style="padding-top: 20px; text-align: center">
					<a4j:commandButton styleClass="BotaoPadrao" id="btnDeslocarPrincipal"
						onclick="Richfaces.hideModalPanel('modalPanelConfirmacaoRegistro');"
						value="Ok" reRender="pnlCentral,pnlAssinatura"
						actionListener="#{beanRemeterDocumento.validarAction}"
						oncomplete="deslocar();"
						/>
					<a4j:commandButton
						onclick="Richfaces.hideModalPanel('modalPanelConfirmacaoRegistro');"
						styleClass="BotaoPadrao" value="Cancelar"/>
				</div>
			</a4j:form>

		</rich:modalPanel>		

		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>