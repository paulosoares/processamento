<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<script type="text/javascript">
	function pesquisarRecursos() {
		document.getElementById('botaoCarregarComboRecursos').click();
	}
	function aguarde(mostrar, div){
	      if( mostrar == true ){
	            document.getElementById(div).innerHTML = '<img src="/processamento/images/loading.gif" /><font class="Padrao">&nbsp;&nbsp;Aguarde...</font>';
	      }
	}

	function pesquisar () {
		document.getElementById('botaoPesquisar').click();
	}

	function pesquisarHandler (valor) {
		document.getElementById(valor).click();
	}
	
	function caixaAlta (campo) {
		campo.value = campo.value.toUpperCase();
	}

	function focoComboBox(campo){
		document.getElementById(campo).focus();
	}

	function localizaAncora(){
		location.hash="#ancora";
	}

	function verifyLength(field, maxlength){
		if(field.value.length > maxlength)
			field.value = field.value.substring(0,maxlength);
	}

</script>

<f:view>
	<a4j:page pageTitle="::.. Principal ..::"
		onload="document.getElementById('idTipoModelo').focus();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Elaborar Documentos" />
			</jsp:include>
			<a name="ancora"></a>
		</h:form>

		<a4j:form id="form" prependId="false">
			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0"
					id="pnlCentral">
					<a4j:outputPanel id="pnlPesquisa" styleClass="MolduraInterna">
						<div class="PainelTituloCriaTexto">
							<span> Pesquisa: </span>
						</div>

						<h:panelGrid width="100%">
							<h:outputText styleClass="Padrao" value="Processo:" />
							<h:inputText id="itProcesso" size="30"
								value="#{beanManterDocumentos.siglaNumeroProcesso}"
								onclick="if ( this.value!='' ) { #{rich:component('sbProcesso')}.callSuggestion(true) }" />
							<rich:hotKey selector="#itProcesso" key="return" handler="" />

							<h:outputText styleClass="Padrao" value="Modelo:" />
							<h:selectOneMenu value="#{beanManterDocumentos.codigoModeloPesq}">
								<f:selectItems value="#{beanManterDocumentos.itensModelosPesq}" />
							</h:selectOneMenu>

							<h:panelGrid columns="3" id="painelPesquisa">
								<a4j:commandButton id="botaoPesquisar" value="Pesquisar"
									styleClass="BotaoPesquisar"
									actionListener="#{beanManterDocumentos.pesquisarDocumentosAction}"
									reRender="pnlCentral" onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" />
								<a4j:commandButton id="botaoLimpar" value="Limpar"
									styleClass="BotaoPadrao"
									actionListener="#{beanManterDocumentos.limparTelaAction}"
									reRender="pnlCentral" onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" />
								<a4j:push interval="2000"
									actionListener="#{beanManterDocumentos.pesquisarDocumentosAction}"
									ignoreDupResponses="true" reRender="pnlResultadoPesquisa"
									onbeforedomupdate="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);"
									eventProducer="#{beanManterDocumentos.refreshController.adicionarListenerRefresh}"
									eventsQueue="ajaxQueue" id="pushPesquisaDocumento" />
							</h:panelGrid>
							<rich:suggestionbox id="sbProcesso" height="200" width="200"
								for="itProcesso"
								suggestionAction="#{beanManterDocumentos.pesquisarIncidentesPrincipal}"
								var="oi" nothingLabel="Nenhum registro encontrado">
								<h:column>
									<h:outputText value="#{oi.identificacao}" />
								</h:column>
								<a4j:support ajaxSingle="true" event="onselect"
									reRender="objIncId">
									<f:setPropertyActionListener value="#{oi.id}"
										target="#{beanManterDocumentos.objetoIncidente}" />
								</a4j:support>
							</rich:suggestionbox>
							<h:inputHidden value="#{beanManterDocumentos.objetoIncidente}"
								id="objIncId" />
						</h:panelGrid>
					</a4j:outputPanel>

					<a4j:outputPanel id="pnlCriaTexto" styleClass="MolduraInterna"
						rendered="#{beanManterDocumentos.renderizaNovoTexto}">
						<hr color="red" align="left" size="1px" width="90%" />
						<a4j:outputPanel ajaxRendered="true" keepTransient="false"
							id="pnlBuscaModelo">
							<div class="PainelTituloCriaTexto">
								<span> Criar novo texto: </span>
							</div>

							<div style="padding-top: 12px;">
								<span class="Padrao"> Tipo de permissão: </span> <span> <h:selectOneMenu
										disabled="#{beanManterDocumentos.renderedDesabilitaMenuModelo}"
										id="filtroPermissaoModelos"
										style="width:220px; margin-left: 4px;"
										value="#{beanManterDocumentos.codigoTipoPermissao}">
										<f:selectItems
											value="#{beanManterDocumentos.itensTiposPermissoes}" />
										<a4j:support
											actionListener="#{beanManterDocumentos.procurarModelosPeloTipoPermissaoAction}"
											oncomplete="focoComboBox('idTipoModelo');" event="onchange" />
									</h:selectOneMenu> </span>
							</div>

							<div style="padding-top: 8px;">
								<span class="Padrao"> Tipo de modelo: </span> 
								<span> 
									<h:selectOneMenu
										disabled="#{beanManterDocumentos.renderedDesabilitaMenuModelo}"
										id="idTipoModelo" style="width:220px; margin-left: 20px;"
										value="#{beanManterDocumentos.codigoTipoModelo}">
										<f:selectItems
											value="#{beanManterDocumentos.itensTipoModelos}" />
										<a4j:support
											actionListener="#{beanManterDocumentos.procurarModelosPeloTipoDoModeloAction}"
											oncomplete="focoComboBox('codigoModelos');" event="onchange" />
										</h:selectOneMenu> 
								</span> 
								<span class="Padrao" style="margin-left: 10px"> Modelo: </span>
								<span style="margin-top: 20px; margin-left: 5px">
									<h:selectOneMenu id="codigoModelos"
										disabled="#{beanManterDocumentos.renderedDesabilitaMenuModelo}"
										value="#{beanManterDocumentos.codigoModelo}"
										style="margin-left: 4px;">
										<f:selectItems value="#{beanManterDocumentos.itensModelos}" />
										<a4j:support
											actionListener="#{beanManterDocumentos.atualizaSessaoAction}"
											oncomplete="focoComboBox('tProcesso');" event="onchange"
											reRender="pnlCentral" />
									</h:selectOneMenu> 
								</span>
							</div>
						</a4j:outputPanel>


						<div style="padding-top: 8px;">
							<span class="Padrao">Processo: </span> <span>
							<h:inputText
									id="tProcesso" size="28" style="margin-left: 53px"
									value="#{beanManterDocumentos.siglaNumeroProcesso}"
									onclick="if ( this.value!='' ) { #{rich:component('bProcesso')}.callSuggestion(true) }" />
								<rich:hotKey selector="#tProcesso" key="return" handler="" />
								<rich:suggestionbox id="bProcesso" height="200" width="200" 
									for="tProcesso"
									suggestionAction="#{beanManterDocumentos.pesquisarIncidentesPrincipal}"
									var="oi" nothingLabel="Nenhum registro encontrado">
									<h:column>
										<h:outputText value="#{oi.identificacao}" />
									</h:column>
									<a4j:support ajaxSingle="true" event="onselect" onsubmit="exibirMsgProcessando(true);"
										reRender="bjIncId"
										oncomplete="pesquisarHandler('btnProcuraPecasVinculadasETagLivres');">
										<f:setPropertyActionListener value="#{oi.id}"
											target="#{beanManterDocumentos.objetoIncidenteNovo}" />
									</a4j:support>
								</rich:suggestionbox> <h:inputHidden
									value="#{beanManterDocumentos.objetoIncidenteNovo}"
									id="bjIncId">
								</h:inputHidden> </span>

							<a4j:outputPanel
								rendered="#{beanManterDocumentos.renderedTelaProcessoLote}">
								<span class="Padrao" style="margin-left: 100px;">Processo
									Adicional: </span>
								<span>
								<rich:hotKey selector="#lProcesso" key="return" handler="" /> 
								<h:inputText id="lProcesso" size="28"
										style="margin-left: 5px;"
										value="#{beanManterDocumentos.siglaNumeroProcessoLote}"
										onclick="if ( this.value!='' ) { #{rich:component('slProcesso')}.callSuggestion(true) }" />
									<rich:suggestionbox id="slProcesso" height="200" width="200"
										for="lProcesso"
										suggestionAction="#{beanManterDocumentos.pesquisarIncidentesLotes}"
										var="oiLote" nothingLabel="Nenhum registro encontrado">
										<h:column>
											<h:outputText value="#{oiLote.identificacao}" />
										</h:column>
										<a4j:support ajaxSingle="true" event="onselect"
											reRender="ljIncId">
											<f:setPropertyActionListener value="#{oiLote.id}"
												target="#{beanManterDocumentos.objetoIncidenteLote}" />
										</a4j:support>
									</rich:suggestionbox> <h:inputHidden
										value="#{beanManterDocumentos.objetoIncidenteLote}"
										id="ljIncId">
									</h:inputHidden> </span>
								<span> <a4j:commandLink
										actionListener="#{beanManterDocumentos.adicionaListaProcessoAction}"
										value="Adicionar" reRender="pnlCriaTexto" /> </span>
								<c:if
									test="${beanManterDocumentos.renderedComponentesAlteracao}">
									<span class="Padrao" style="margin-left: 40px;">Lote: </span>
									<span> <h:selectOneListbox
											value="#{beanManterDocumentos.idProcessoLoteObjetoIncidente}"
											style="margin-left:10px;">
											<f:selectItems
												value="#{beanManterDocumentos.itensProcessoEmLote}" />
											<a4j:support event="onchange" />
										</h:selectOneListbox> <a4j:commandLink
											actionListener="#{beanManterDocumentos.retiraProcessoDaListaAction}"
											value="Retirar" style="margin-left:10px;"
											reRender="pnlCriaTexto" /> </span>
								</c:if>
							</a4j:outputPanel>
						</div>
						<div style="padding-top: 8px;">
							<span class="Padrao" style="height: 88px;"> Observação: </span> <span>
								<h:inputTextarea id="idTextObs" cols="100" rows="7"
									value="#{beanManterDocumentos.obsDocumento}"
									style="margin-left:40px;" /> </span>
						</div>
					</a4j:outputPanel>
					
					<a4j:outputPanel rendered="#{beanManterDocumentos.renderedAlteraObsAndamento}">
						<div style="padding-top: 8px;">
							<span class="Padrao" style="height: 65px;"> * Observação do andamento: </span> <span>
								<h:inputTextarea id="idTextObsAndamento" cols="89" rows="5"
									value="#{beanManterDocumentos.observacaoAndamento}"
									style="margin-left:10px;" /> </span>
						</div>
					</a4j:outputPanel>

					<a4j:outputPanel
						rendered="#{beanManterDocumentos.renderedTelaDePartesSelecionadas}"
						styleClass="MolduraInterna" id="idPainelPartesSelecionadas">
						<hr color="red" align="left" size="1px" width="90%"
							style="margin-left: 10px" />
						<div class="PainelTituloCriaTexto">
							<span> Selecione a(s) parte(s): </span>
						</div>
						<h:panelGrid
							rendered="#{beanManterDocumentos.renderedComboIntimandos}"
							columns="6" style="padding-top: 10px;">
							<h:outputText value="Intimandos:" styleClass="Padrao" />
							<h:selectOneMenu id="idItemIntimando"
								style="width:300px;margin-left: 5px;"
								value="#{beanManterDocumentos.nomeCategoriaIntimando}">
								<f:selectItems
									value="#{beanManterDocumentos.itensCategoriaIntimando}" />
							</h:selectOneMenu>
							<a4j:commandLink
								actionListener="#{beanManterDocumentos.renderizaCampoAdicionaIntimandoAction}"
								reRender="idPainelPartesSelecionadas"
								rendered="#{beanManterDocumentos.renderedBotaoAdicionarIntimando}">
								<h:graphicImage url="../../images/cruz.png" title="Adicionar" />
							</a4j:commandLink>
							<h:panelGrid
								rendered="#{beanManterDocumentos.renderedAdicionaIntimando}"
								columns="3">
								<h:outputText style="margin-left: 10px" styleClass="Padrao"
									value="Adicionar intimando:" />
								<h:inputText
									value="#{beanManterDocumentos.nomeIntimandoAdicionado}"
									style="margin-left:10px;" onkeyup="caixaAlta(this);" size="67" />
								<h:commandButton
									actionListener="#{beanManterDocumentos.adicionaNomeParaParteSelecionadaAction}"
									value="Adicionar" />
							</h:panelGrid>
						</h:panelGrid>

						<h:panelGrid
							rendered="#{beanManterDocumentos.renderedComboCitanda}"
							columns="6" style="padding-top: 10px;">
							<h:outputText value="Citandos:" styleClass="Padrao" />
							<h:selectOneMenu id="idItemCitanda"
								style="width:300px;margin-left: 22px;"
								value="#{beanManterDocumentos.nomeCategoriaCitanda}">
								<f:selectItems
									value="#{beanManterDocumentos.itensCategoriaCitanda}" />
							</h:selectOneMenu>
							<a4j:commandLink
								actionListener="#{beanManterDocumentos.renderizaCampoAdicionaCitandoAction}"
								reRender="idPainelPartesSelecionadas"
								rendered="#{beanManterDocumentos.renderedBotaoAdicionarCitando}">
								<h:graphicImage url="../../images/cruz.png" title="Adicionar" />
							</a4j:commandLink>
							<h:panelGrid
								rendered="#{beanManterDocumentos.renderedAdicionaCitando}"
								columns="3">
								<h:outputText style="margin-left: 10px" styleClass="Padrao"
									value="Adicionar citando:" />
								<h:inputText
									value="#{beanManterDocumentos.nomeCitandoAdicionado}"
									style="margin-left:10px;" onkeyup="caixaAlta(this);" size="67" />
								<h:commandButton
									actionListener="#{beanManterDocumentos.adicionaNomeParaParteSelecionadaAction}"
									value="Adicionar" />
							</h:panelGrid>
						</h:panelGrid>

						<h:panelGrid rendered="#{beanManterDocumentos.renderedComboReus}"
							columns="6" style="padding-top: 10px;">
							<h:outputText value="Réus:" styleClass="Padrao" />
							<h:selectOneMenu id="idItemReu"
								style="width:300px;margin-left: 43px;"
								value="#{beanManterDocumentos.nomeCategoriaReu}">
								<f:selectItems value="#{beanManterDocumentos.itensCategoriaReu}" />
							</h:selectOneMenu>
							<a4j:commandLink
								actionListener="#{beanManterDocumentos.renderizaCampoAdicionaReuAction}"
								reRender="idPainelPartesSelecionadas"
								rendered="#{beanManterDocumentos.renderedBotaoAdicionarReu}">
								<h:graphicImage url="../../images/cruz.png" title="Adicionar" />
							</a4j:commandLink>
							<h:panelGrid
								rendered="#{beanManterDocumentos.renderedAdicionaReu}"
								columns="3">
								<h:outputText style="margin-left: 10px" styleClass="Padrao"
									value="Adicionar réu:" />
								<h:inputText value="#{beanManterDocumentos.nomeReuAdicionado}"
									style="margin-left:10px;" onkeyup="caixaAlta(this);" size="67" />
								<h:commandButton
									actionListener="#{beanManterDocumentos.adicionaNomeParaParteSelecionadaAction}"
									value="Adicionar" />
							</h:panelGrid>
						</h:panelGrid>

						<div style="padding-top: 5px;">
							<c:if test="${beanManterDocumentos.renderedComboAutores}">
								<span class="Padrao" style="margin-left: 5px"> Autores: </span>
								<span style="margin-top: 20px; margin-left: 22px"> <h:selectOneMenu
										id="idItemAutor" style="width:300px;"
										value="#{beanManterDocumentos.nomeCategoriaAutor}">
										<f:selectItems
											value="#{beanManterDocumentos.itensCategoriaAutor}" />
									</h:selectOneMenu> </span>
							</c:if>
							<c:if test="${beanManterDocumentos.renderedComboAdvogados}">
								<span class="Padrao" style="margin-left: 5px"> Advogados:
								</span>
								<span style="margin-top: 20px; margin-left: 13px"> <h:selectOneMenu
										id="idItemAdvogado" style="width:300px;"
										value="#{beanManterDocumentos.nomeCategoriaAdvogado}">
										<f:selectItems
											value="#{beanManterDocumentos.itensCategoriaAdvogado}" />
									</h:selectOneMenu> </span>
							</c:if>
							<c:if test="${beanManterDocumentos.renderedComboParte}">
								<span class="Padrao" style="margin-left: 5px"> Partes: </span>
								<span style="margin-top: 20px; margin-left: 13px"> <h:selectOneMenu
										id="idItemParte" style="width:300px;"
										value="#{beanManterDocumentos.nomeCategoriaParte}">
										<f:selectItems
											value="#{beanManterDocumentos.itensCategoriaParte}" />
									</h:selectOneMenu> </span>
							</c:if>
						</div>
					</a4j:outputPanel>

					<a4j:outputPanel ajaxRendered="true" keepTransient="false"
						id="pnlResultadoPesquisa" styleClass="MolduraInterna"
						rendered="#{not empty beanManterDocumentos.listaDocumentos}">
						<hr color="red" align="left" size="1px" width="90%"
							style="margin-left: 10px" />
						<h:outputText styleClass="PainelTituloCriaTexto"
							style="margin-left: 10px;" value="Resultado:" />
						<div style="padding-top: 10px;"></div>
						<rich:dataTable headerClass="DataTableDefaultHeader"
							styleClass="DataTableDefault"
							footerClass="DataTableDefaultFooter"
							rowClasses="DataTableRow, DataTableRow2"
							columnClasses="tres, dezCenter, dezessete, dezesseteLeft, dezesseteLeft, dezCenter, tres, dezCenter, tres, tres, cinco, tres, cinco"
							value="#{beanManterDocumentos.listaDocumentos}"
							var="wrappedDocumento"
							binding="#{beanManterDocumentos.tabelaDocumentos}">
							<rich:column>
								<f:facet name="header">
									<a4j:commandButton image="../../images/setabaixo.gif"
										onclick="exibirMsgProcessando(true)"
										oncomplete="exibirMsgProcessando(false);"
										actionListener="#{beanManterDocumentos.marcarTodosTextos}" />
								</f:facet>
								<h:selectBooleanCheckbox
									onclick="document.getElementById('BotaoAtualizarMarcacao').click();"
									value="#{wrappedDocumento.checked}" />
							</rich:column>

							<rich:column
								sortBy="#{wrappedDocumento.wrappedObject.comunicacao.objetoIncidenteUnico.identificacao}">
								<f:facet name="header">
									<h:outputText value="Processo"
										styleClass="DataTableDocumentoTexto" />
								</f:facet>
								<h:commandLink styleClass="PadraoLink"
									style="text-align:center;" target="_blank"
									value="#{wrappedDocumento.wrappedObject.comunicacao.identificacaoProcessual}"
									action="#{beanManterDocumentos.consultarProcessoDigital}">
									<f:setPropertyActionListener
										target="#{beanManterDocumentos.seqObjetoIncidente}"
										value="#{wrappedDocumento.wrappedObject.comunicacao.objetoIncidenteUnico.principal.id}" />
								</h:commandLink>
							</rich:column>
							
							<rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.concatenaAnoNumeracaoUnica}" >
                                   <f:facet name="header">
                                       <h:outputText value="Nº Documento"
                                                     styleClass="DataTableDocumentoTexto" style="width: 101px;"/>
                                   </f:facet>
                                   <h:outputText styleClass="Padrao" style="text-align:left;"
                                                 value="#{wrappedDocumento.wrappedObject.comunicacao.concatenaAnoNumeracaoUnica}" />
                               </rich:column>
                               
							<rich:column
								sortBy="#{wrappedDocumento.wrappedObject.comunicacao.nomeMinistroRelatorAtual}">
								<f:facet name="header">
									<h:outputText value="Relator"
										styleClass="DataTableDocumentoTexto" />
								</f:facet>
								<h:outputText styleClass="Padrao" style="text-align:center;"
									value="#{wrappedDocumento.wrappedObject.comunicacao.nomeMinistroRelatorAtual}" />
							</rich:column>

							<rich:column
								sortBy="#{wrappedDocumento.wrappedObject.comunicacao.dscNomeDocumento}">
								<f:facet name="header">
									<h:outputText value="Nome Documento"
										styleClass="DataTableDocumentoTexto" />
								</f:facet>
								<h:outputText styleClass="Padrao" style="text-align:center;"
									value="#{wrappedDocumento.wrappedObject.comunicacao.dscNomeDocumento}" />
							</rich:column>

							<rich:column
								sortBy="#{wrappedDocumento.wrappedObject.comunicacao.modeloComunicacao.tipoComunicacao.descricao}">
								<f:facet name="header">
									<h:outputText value="Tipo Modelo" />
								</f:facet>
								<h:outputText styleClass="Padrao" style="text-align:center;"
									value="#{wrappedDocumento.wrappedObject.comunicacao.modeloComunicacao.tipoComunicacao.descricao}" />
							</rich:column>

							<rich:column
								sortBy="#{wrappedDocumento.wrappedObject.comunicacao.descricaoFaseAtual}">
								<f:facet name="header">
									<h:outputText value="Situação" />
								</f:facet>
								<h:outputText styleClass="Padrao"
									value="#{wrappedDocumento.wrappedObject.comunicacao.descricaoFaseAtual}" />
							</rich:column>
							
							<rich:column
								sortBy="#{wrappedDocumento.wrappedObject.comunicacao.observacaoFaseAtual}">
								<f:facet name="header">
									<h:outputText value="Motivo" />
								</f:facet>
								<h:outputText styleClass="Padrao"
									value="#{wrappedDocumento.wrappedObject.comunicacao.observacaoFaseAtual}"/>																	
							</rich:column>

							<rich:column sortBy="#{wrappedDocumento.wrappedObject.possuiPDF}">
								<f:facet name="header">
									<h:outputText value="PDF" />
								</f:facet>
								<h:outputLink
									rendered="#{wrappedDocumento.wrappedObject.possuiPDF}"
									value="#{wrappedDocumento.wrappedObject.linkPDF}">
									<h:graphicImage value="/images/pdf.png"></h:graphicImage>
								</h:outputLink>
								<rich:toolTip  followMouse="true" direction="top-left" style="width:800px"	
									rendered="#{wrappedDocumento.wrappedObject.possuiPDF}"	
										                                             
                                    hideDelay="20"
                                    showDelay="250">    		                                             
                             		<h:outputText value="#{beanManterDocumentos.recuperarTextoDocumento}"/>
	                            </rich:toolTip>				
							</rich:column>

							<rich:column
								sortBy="#{wrappedDocumento.wrappedObject.comunicacao.nomeUsuarioCriacao}">
								<f:facet name="header">
									<h:outputText value="Usuário" />
								</f:facet>
								<h:outputText styleClass="Padrao"
									value="#{wrappedDocumento.wrappedObject.comunicacao.nomeUsuarioCriacao}" />
							</rich:column>

							<rich:column
								sortBy="#{!wrappedDocumento.wrappedObject.possuiPDF}">
								<f:facet name="header">
									<h:outputText value="Editar" />
								</f:facet>
								<h:commandLink action="#{beanManterDocumentos.abrirDocumento}"
									rendered="#{!wrappedDocumento.wrappedObject.possuiPDF}">
									<h:graphicImage url="../../images/btabrir.gif"
										title="Editar Documento" />
								</h:commandLink>
							</rich:column>

							<rich:column sortBy="#{wrappedDocumento.wrappedObject.possuiPDF}">
								<f:facet name="header">
									<h:outputText value="Cancelar" />
								</f:facet>
								<a4j:commandLink
									oncomplete="Richfaces.showModalPanel('modalPanelMotivoCancelaDocumento');"
									rendered="#{wrappedDocumento.wrappedObject.possuiPDF}"
									actionListener="#{beanManterDocumentos.recuperaLinhaParaModalPanel}">
									<h:graphicImage url="../../images/close.gif" title="Cancelar" />
								</a4j:commandLink>
							</rich:column>

							<rich:column
								sortBy="#{not empty wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaProcessoLoteVinculados}">
								<f:facet name="header">
									<h:outputText value="Lote" />
								</f:facet>
								<h:commandLink value="vinculados "
									rendered="#{not empty wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaProcessoLoteVinculados}" />
								<rich:toolTip followMouse="false" direction="top-right"
									horizontalOffset="-5" verticalOffset="-5"
									styleClass="tooltipPrecedentes" hideDelay="20" showDelay="250"
									rendered="#{not empty wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaProcessoLoteVinculados}">

									<h:dataTable var="valorL" id="tblValor"
										value="#{wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaProcessoLoteVinculados}"
										headerClass="CabecalhoTabelaDados"
										rowClasses="LinhaParTabelaDados"
										columnClasses="ColunaTabelaDados"
										styleClass="TabelaDadosPreview">
										<h:column>
											<f:facet name="header">
												<h:outputText value="Processo" />
											</f:facet>
											<h:panelGrid style="text-align: center;" width="60%">
												<h:outputText value="#{valorL}" styleClass="Padrao" />
											</h:panelGrid>
										</h:column>
									</h:dataTable>
								</rich:toolTip>
							</rich:column>

							<rich:column
								sortBy="#{not empty wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}">
								<f:facet name="header">
									<h:outputText value="Peças" />
								</f:facet>
								<h:commandLink value="peças "
									rendered="#{not empty wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}" />
								<rich:toolTip followMouse="false" direction="top-right"
									horizontalOffset="-5" verticalOffset="-5"
									styleClass="tooltipPecasEletronicas" hideDelay="20"
									showDelay="250"
									rendered="#{not empty wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}">

									<h:dataTable var="valor" id="tbpValor"
										value="#{wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}"
										headerClass="CabecalhoTabelaDados"
										rowClasses="LinhaParTabelaDados"
										columnClasses="ColunaTabelaDados"
										styleClass="TabelaDadosPreview">
										<h:column>
											<f:facet name="header">
												<h:outputText value="Tipo Peça" />
											</f:facet>
											<h:panelGrid style="text-align: left;" width="60%">
												<h:outputText
													value="#{valor.excluida eq true ? 'Peça Dividida/Excluída' : valor.pecaProcessoEletronico.tipoPecaProcesso.descricao}"
													styleClass="Padrao" />
											</h:panelGrid>
										</h:column>
										<h:column>
											<f:facet name="header">
												<h:outputText value="PDF" />
											</f:facet>
											<h:commandLink action="#{assinadorBaseBean.report}" disabled="#{valor.excluida eq true ? true : false }">
												<f:setPropertyActionListener value="#{valor}"
													target="#{assinadorBaseBean.valor}" />
												<h:graphicImage value="/images/pdf.png"></h:graphicImage>
											</h:commandLink>
										</h:column>
									</h:dataTable>
								</rich:toolTip>
								<a4j:commandLink
									actionListener="#{beanManterDocumentos.alterarVinculoDePecasComunicacaoAction}"
									rendered="#{not empty wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}"
									value="alterar" reRender="pnlCentral"
									oncomplete="document.getElementById('btnSalvarVinculo').focus();">
								</a4j:commandLink>
							</rich:column>

							<rich:column>
								<f:facet name="header">
									<h:outputText value="Observacao" />
								</f:facet>
								<a4j:commandLink
									oncomplete="Richfaces.showModalPanel('modalPanelEditaObservacao');"
									actionListener="#{beanManterDocumentos.editaObservacaoComunicacaoAction}"
									reRender="modalPanelEditaObservacao">
									<h:graphicImage
										rendered="#{not empty wrappedDocumento.wrappedObject.comunicacao.obsComunicacao}"
										value="/images/icobs.png"></h:graphicImage>
									<h:graphicImage
										rendered="#{empty wrappedDocumento.wrappedObject.comunicacao.obsComunicacao}"
										value="/images/editar_item.png"></h:graphicImage>
								</a4j:commandLink>
								<rich:toolTip followMouse="false" direction="top-right"
									horizontalOffset="-5" verticalOffset="-5"
									styleClass="tooltipPrecedentes" hideDelay="20" showDelay="250"
									rendered="#{not empty wrappedDocumento.wrappedObject.comunicacao.obsComunicacao}">
									<h:outputText
										value="#{wrappedDocumento.wrappedObject.comunicacao.obsComunicacao}" />
								</rich:toolTip>
							</rich:column>
						</rich:dataTable>


						<a4j:commandButton styleClass="BotaoPadrao"
							actionListener="#{beanManterDocumentos.excluirDocumentosSelecionadosAction}"
							onclick="exibirMsgProcessando(true)"
							oncomplete="exibirMsgProcessando(false);" reRender="pnlCentral"
							value="Excluir" />

					</a4j:outputPanel>

					<h:panelGrid id="pnlPreenchimentoTagLivres"
						styleClass="MolduraInterna"
						rendered="#{beanManterDocumentos.renderedTelaDeTagsLivreDePreenchimento}">
						<hr color="red" align="left" size="1px" width="90%"
							style="margin-left: 10px" />
						<h:outputText styleClass="PainelTituloCriaTexto"
							style="margin-left: 10px"
							value="Preencha os campos presentes no documento:" />
						<h:panelGrid columns="2">
							<c:forEach var="tagsLivres"
								items="#{beanManterDocumentos.listaDeTags}">
								<h:outputText styleClass="Padrao" value="#{tagsLivres}:"
									style="margin-left: 10px"></h:outputText>
								<h:inputText
									value="#{beanManterDocumentos.conjuntoTags[tagsLivres]}"
									style="margin-left: 5px" size="60"></h:inputText>
							</c:forEach>
						</h:panelGrid>
					</h:panelGrid>
					
					<h:panelGrid id="pecaVinculadaDocumento"
						rendered="#{beanManterDocumentos.renderedVincularPecaAoDocumento}"
						styleClass="MolduraInterna">
						<hr color="red" align="left" size="1px" width="90%"
							style="margin-left: 10px" />
						<h:outputText styleClass="PainelTituloCriaTexto"
							id="lblVincularPecas" style="margin-left: 10px"
							value="Vincular Peças:" />
						<a4j:outputPanel ajaxRendered="true" keepTransient="false"
							id="pnlResultadoPesquisaPeca" styleClass="MolduraInterna">
							<c:if
								test="${not empty beanManterDocumentos.listaPecaProcessoEletronico}">
								<rich:dataTable headerClass="DataTableDefaultHeader" id="tabelaPecasProcesso"
									styleClass="DataTableDefault"
									footerClass="DataTableDefaultFooter"
									rowClasses="DataTableRow, DataTableRow2"
									columnClasses="tres, oitoCenter, cincoCenter, quinzeCenter, 
										quinzeCenter, oitoCenter, cincoCenter"
									value="#{beanManterDocumentos.listaPecaProcessoEletronico}"
									var="wrappedPecaEletr" rows="100"
									binding="#{beanManterDocumentos.tabelaPecaProcessoEletronico}">


									<rich:column sortBy="#{wrappedPecaEletr.checked}" >
										<f:facet name="header">
											<a4j:commandButton image="../../images/setabaixo.gif"
												onclick="exibirMsgProcessando(true)"
												oncomplete="exibirMsgProcessando(false);"
												actionListener="#{beanManterDocumentos.marcarTodasPecasVinculadas}" />
										</f:facet>
										<h:selectBooleanCheckbox
											onclick="document.getElementById('BotaoAtualizarMarcacao').click();"
											value="#{wrappedPecaEletr.checked}" />
									</rich:column>

									<rich:column
										sortBy="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.objetoIncidente.identificacao}">
										<f:facet name="header">
											<h:outputText value="Processo" />
										</f:facet>
										<h:outputText
											value="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.objetoIncidente.identificacao}" />
									</rich:column>

									<rich:column
										sortBy="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.numeroOrdemPeca}"
										sortOrder="ASCENDING">
										<f:facet name="header">
											<h:outputText value="Número da Peça" />
										</f:facet>
										<h:outputText
											value="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.numeroOrdemPeca}" />
									</rich:column>

									<rich:column
										sortBy="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.descricaoPeca}">
										<f:facet name="header">
											<h:outputText value="Nome da Peça" />
										</f:facet>
										<h:outputText
											value="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.descricaoPeca}" />
									</rich:column>

									<rich:column
										sortBy="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.tipoPecaProcesso.descricao}">
										<f:facet name="header">
											<h:outputText value="Tipo da Peça" />
										</f:facet>
										<h:outputText
											value="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.tipoPecaProcesso.descricao}" />
									</rich:column>

									<rich:column
										sortBy="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.tipoSituacaoPeca.descricao}">
										<f:facet name="header">
											<h:outputText value="Situação da Peça" />
										</f:facet>
										<h:outputText
											value="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.tipoSituacaoPeca.descricao}" />
									</rich:column>

									<rich:column>
										<f:facet name="header">
											<h:outputText value="Documento" />
										</f:facet>
										<h:outputLink
											value="#{beanManterDocumentos.documentoDownloadURL}">
											<h:graphicImage value="/images/pdf.png"></h:graphicImage>
										</h:outputLink>
									</rich:column>
								</rich:dataTable>

								<rich:datascroller id="scroll" for="tabelaPecasProcesso" maxPages="10" align="left" />

								<a4j:commandButton styleClass="BotaoPadrao"
									rendered="#{beanManterDocumentos.renderedBotaoVincularPeca}"
									actionListener="#{beanManterDocumentos.criaListaPecasAoDocumentoAction}"
									onclick="exibirMsgProcessando(true); localizaAncora();"
									oncomplete="exibirMsgProcessando(false);" reRender="pnlCentral"
									value="Vincular" />
								<a4j:commandButton styleClass="BotaoPadrao"
									rendered="#{beanManterDocumentos.renderedVincularPecaNovamenteDocumento}"
									actionListener="#{beanManterDocumentos.vincularPecasNovamenteAoDocumentoAction}"
									onclick="exibirMsgProcessando(true)" id="btnSalvarVinculo"
									oncomplete="exibirMsgProcessando(false);" reRender="pnlCentral"
									value="Salvar Vínculos" />
							</c:if>
						</a4j:outputPanel>
					</h:panelGrid>

					<h:panelGrid id="listaPecasVinculadas"
						rendered="#{beanManterDocumentos.renderedListaPecasVinculadas}"
						styleClass="MolduraInterna">
						<hr color="red" align="left" size="1px" width="90%" />
						<h:outputText styleClass="PainelTituloCriaTexto"
							style="margin-left: 10px;" value="Peças vinculadas:" />
						<div style="margin-top: 10px;"></div>
						<h:dataTable headerClass="DataTableDefaultHeader"
							styleClass="DataTableDefault"
							footerClass="DataTableDefaultFooter"
							rowClasses="DataTableRow, DataTableRow2"
							columnClasses="dezCenter, dezCenter, dezCenter"
							value="#{beanManterDocumentos.listaPecaProcessoEletronicoVinculadas}"
							var="wrappedPecaEletrVinc"
							binding="#{beanManterDocumentos.tabelaPecaProcessoEletronicoVinculadas}">

							<h:column>
								<f:facet name="header">
									<h:outputText value="Processo" />
								</f:facet>
								<h:outputText
									value="#{wrappedPecaEletrVinc.wrappedObject.pecaProcessoEletronico.objetoIncidente.identificacao}" />
							</h:column>

							<h:column>
								<f:facet name="header">
									<h:outputText value="Tipo da peça" />
								</f:facet>
								<h:outputText
									value="#{wrappedPecaEletrVinc.wrappedObject.pecaProcessoEletronico.tipoPecaProcesso.descricao}" />
							</h:column>

							<h:column>
								<f:facet name="header">
									<h:outputText value="Situação da peça" />
								</f:facet>
								<h:outputText
									value="#{wrappedPecaEletrVinc.wrappedObject.pecaProcessoEletronico.tipoSituacaoPeca.descricao}" />
							</h:column>
						</h:dataTable>
					</h:panelGrid>



					<a4j:outputPanel
						rendered="#{beanManterDocumentos.renderedBotaoNovoTexto}"
						styleClass="MolduraInterna" id="idPainelCriarNovoTexto">
						<hr color="red" align="left" size="1px" width="90%"
							style="margin-left: 10px" />
						<div class="PainelTituloCriaTexto">
							<span> Criar novo documento: </span>
						</div>
						<div style="padding-top: 8px;">
							<span> <h:commandButton styleClass="BotaoPadrao"
									style="margin-left: 5px"
									action="#{beanManterDocumentos.criarNovoTexto}" value="Criar">
								</h:commandButton> </span> <span> <a4j:commandButton styleClass="BotaoPadrao"
									value="Limpar"
									actionListener="#{beanManterDocumentos.limparTelaAction}"
									reRender="pnlCentral">
								</a4j:commandButton> </span>
						</div>
					</a4j:outputPanel>

					<a4j:commandButton styleClass="BotaoOculto"
						id="BotaoAtualizarMarcacao"
						actionListener="#{beanManterDocumentos.atualizarMarcacao}"
						reRender="pnlResultadoPesquisa,pnlResultadoPesquisaPeca" />

					<a4j:commandButton styleClass="BotaoOculto" id="btnProcuraModelos"
						actionListener="#{beanManterDocumentos.procurarModelosPeloTipoDoModeloAction}" />

					<a4j:commandButton styleClass="BotaoOculto"
						id="btnProcuraPecasVinculadasETagLivres"
						actionListener="#{beanManterDocumentos.verificaECarregaPecaVinculadaETagLivreAction}"
						reRender="pnlCentral" oncomplete="exibirMsgProcessando(false);" />

					<a4j:commandButton styleClass="BotaoOculto" id="btnAtualizaSessao"
						actionListener="#{beanManterDocumentos.atualizaSessaoAction}" />

					<a4j:commandButton styleClass="BotaoOculto"
						id="btnHabilitaPesquisaAutor"
						actionListener="#{beanManterDocumentos.pesquisaAutoresAction}"
						reRender="pnlCentral" />

				</h:panelGrid>
			</h:panelGrid>
		</a4j:form>

		<rich:modalPanel id="modalPanelMotivoCancelaDocumento" width="630"
			height="160" keepVisualState="true">
			<f:facet name="header">
				<h:outputText value="Favor inserir o motivo do cancelamento" />
			</f:facet>
			<a4j:form prependId="false">
				<div>
					<span class="Padrao"> <h:outputText styleClass="Padrao"
							value="Motivo:" /> </span>
				</div>
				<div style="padding-top: 3px;">
					<span> 
						<h:inputTextarea cols="80" rows="5" onkeyup="verifyLength(this, 1000)" value="#{beanManterDocumentos.anotacaoCancelamento}">							
						</h:inputTextarea>
					 
					</span>
				</div>
				<div style="padding-top: 15px;">
					<span class="Padrao"> <a4j:commandButton
							oncomplete="aguarde(false, 'dvAguardePanelAtualizarControleVotos');
								Richfaces.hideModalPanel('modalPanelMotivoCancelaDocumento');"
							value="Salvar"
							actionListener="#{beanManterDocumentos.cancelaPDFDocumentoAction}"
							onclick="aguarde(true, 'dvAguardePanelCancelaPDFDocumento');" />
						<a4j:commandButton
							onclick="Richfaces.hideModalPanel('modalPanelMotivoCancelaDocumento');"
							value="Fechar" /> </span>
				</div>
				<div id="dvAguardePanelCancelaPDFDocumento"></div>
			</a4j:form>
		</rich:modalPanel>

		<rich:modalPanel id="modalPanelEditaObservacao" width="530"
			height="160" keepVisualState="true">
			<f:facet name="header">
				<h:outputText value="Descrição da observação" />
			</f:facet>
			<a4j:form prependId="false">
				<div>
					<span class="Padrao"> <h:outputText styleClass="Padrao"
							value="Observação:" /> </span>
				</div>
				<div style="padding-top: 3px;">
					<span> <h:inputTextarea cols="80" rows="5"
							value="#{beanManterDocumentos.observacaoComunicacaoAlterada}" />
					</span>
				</div>
				<div style="padding-top: 15px;">
					<span class="Padrao"> <a4j:commandButton
							oncomplete="aguarde(false, 'dvAguardePanelAlteraObsComunicacao');
								Richfaces.hideModalPanel('modalPanelEditaObservacao');"
							value="Alterar"
							actionListener="#{beanManterDocumentos.alteraObsComunicacaoAction}"
							onclick="aguarde(true, 'modalPanelEditaObservacao');" /> <a4j:commandButton
							onclick="Richfaces.hideModalPanel('modalPanelEditaObservacao');"
							value="Fechar" /> </span>
				</div>
				<div id="dvAguardePanelAlteraObsComunicacao"></div>
			</a4j:form>
		</rich:modalPanel>

		<rich:modalPanel id="modalPanelEncaminharDocumento" width="630"
			height="100" keepVisualState="true">
			<f:facet name="header">
				<h:outputText value="Favor selecionar o setor de destino" />
			</f:facet>
			<a4j:form prependId="false">

				<div>
					<span class="Padrao"> <h:outputText styleClass="Padrao"
							value="Setor de Destino:" /> <h:selectOneMenu
							style="margin-left:10px;"
							value="#{beanManterDocumentos.idSetorDestino}" styleClass="Input">
							<f:selectItems
								value="#{beanManterDocumentos.itensSetoresDestino}" />
						</h:selectOneMenu> </span>
				</div>

				<div style="padding-top: 20px;">
					<span> <a4j:commandButton styleClass="BotaoPadrao"
							actionListener="#{beanManterDocumentos.encaminharDocumentosAction}"
							onclick="Richfaces.hideModalPanel('modalPanelEncaminharDocumento');"
							value="OK" /> </span> <span> <h:commandButton
							onclick="Richfaces.hideModalPanel('modalPanelEncaminharDocumento');"
							styleClass="BotaoPadrao" value="Fechar" /> </span>
				</div>
			</a4j:form>
		</rich:modalPanel>

		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>