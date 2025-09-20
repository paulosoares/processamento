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

	function pesquisar() {
		jq1_10_2('#idBotaoInicializar').click();
	}

	function verifyLength(field, maxlength){
		if(field.value.length > maxlength)
			field.value = field.value.substring(0,maxlength);
	}
</script>

<f:view>
	<a4j:page pageTitle="::.. Principal ..::" onload="pesquisar();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Aguardando Revisão" />
			</jsp:include>
		</h:form>

		<a4j:form id="form" prependId="false" >
			<h:panelGrid styleClass="MolduraExterna" cellpadding="0" cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0" id="pnlCentral">
				
					<a4j:outputPanel ajaxRendered="true" keepTransient="false" id="pnlResultadoPesquisa" styleClass="MolduraInterna">
				
				<c:if test="${empty beanFinalizarRestritos.documentosAguadandoRevisao}">
									<div> </div>
											<div class="InfoMessage" >
												Nenhum Documento encontrado &nbsp;&nbsp;&nbsp;
											</div>
											</c:if> 

						<c:if test="${not empty beanRevisao.documentosAguadandoRevisao}">
							<div class="PainelTituloCriaTexto">
								<span> Pesquisa: </span>
							</div>
							
							<rich:dataTable id="tableDocumentosRevisao" headerClass="DataTableDefaultHeader" styleClass="DataTableDefault" footerClass="DataTableDefaultFooter"
 								rowClasses="DataTableRow, DataTableRow2" rows="30" var="row"
 								columnClasses="tres, dezCenter, dezCenter, vinteLeft, vinteLeft, vinteLeft, dezCenter, tres, tres, tres, cinco, sete"
 								value="#{beanRevisao.documentosAguadandoRevisao}"
 								binding="#{beanRevisao.tabelaAguardandoRevisao}">
								
								<rich:column>
									<f:facet name="header">
										<a4j:commandButton image="../../images/setabaixo.gif" action="#{beanRevisao.marcarTodosTextos}" />
									</f:facet>
									<h:selectBooleanCheckbox value="#{row.checked}" />
 								</rich:column>
								
								<rich:column sortBy="#{row.wrappedObject.comunicacao.deslocamentoAtual.dataEntrada}" sortOrder="ASCENDING">
									<f:facet name="header">
										<h:outputText value="Data de Entrada" styleClass="DataTableDocumentoTexto"/>
									</f:facet>

									<h:outputText styleClass="Padrao" value="#{row.wrappedObject.comunicacao.deslocamentoAtual.dataEntrada}" />

 								</rich:column>
								
								<rich:column sortBy="#{row.wrappedObject.comunicacao.identificacaoProcessual}">
									<f:facet name="header">
										<h:outputText value="Processo" 
 											styleClass="DataTableDocumentoTexto"/>
									</f:facet>

									<h:commandLink styleClass="PadraoLink" target="_blank"
 										value="#{row.wrappedObject.comunicacao.identificacaoProcessual}"
										action="#{beanRevisao.consultarProcessoDigital}">
										<f:setPropertyActionListener
 											target="#{beanRevisao.seqObjetoIncidente}"
											value="#{row.wrappedObject.comunicacao.objetoIncidenteUnico.principal.id}" />
 									</h:commandLink>

 								</rich:column>
								
								<rich:column sortBy="#{row.wrappedObject.comunicacao.concatenaAnoNumeracaoUnica}" >
									<f:facet name="header">
										<h:outputText value="Nº Documento" styleClass="DataTableDocumentoTexto"/>
 									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{row.wrappedObject.comunicacao.concatenaAnoNumeracaoUnica}" />
								</rich:column>

								<rich:column sortBy="#{row.wrappedObject.comunicacao.nomeMinistroRelatorAtual}">
									<f:facet name="header">
										<h:outputText value="Relator" styleClass="DataTableDocumentoTexto" />
									</f:facet>
									<h:outputText styleClass="Padrao"
 										value="#{row.wrappedObject.comunicacao.nomeMinistroRelatorAtual}" />
								</rich:column>

								<rich:column sortBy="#{row.wrappedObject.comunicacao.dscNomeDocumento}">
									<f:facet name="header">
										<h:outputText value="Nome Documento"
 											styleClass="DataTableDocumentoTexto" />
									</f:facet>
									<h:outputText styleClass="Padrao"
 										value="#{row.wrappedObject.comunicacao.dscNomeDocumento}" />
								</rich:column>

								<rich:column sortBy="#{row.wrappedObject.comunicacao.modeloComunicacao.tipoComunicacao.descricao}">
									<f:facet name="header">
										<h:outputText value="Tipo Documento" />
									</f:facet>
									<h:outputText styleClass="Padrao"
 										value="#{row.wrappedObject.comunicacao.modeloComunicacao.tipoComunicacao.descricao}" />
								</rich:column>

								<rich:column sortBy="#{row.wrappedObject.comunicacao.descricaoFaseAtual}">
									<f:facet name="header">
										<h:outputText value="Situação" />
									</f:facet>
									<h:outputText styleClass="Padrao"
 										value="#{row.wrappedObject.comunicacao.descricaoFaseAtual}" />
								</rich:column>
								
								<rich:column sortBy="#{row.wrappedObject.comunicacao.observacaoFaseAtual}">
									<f:facet name="header">
										<h:outputText value="Motivo" />
									</f:facet>
									<h:outputText styleClass="Padrao"
 										value="#{row.wrappedObject.comunicacao.observacaoFaseAtual}" />
								</rich:column>

								<rich:column sortBy="#{row.wrappedObject.comunicacao.nomeUsuarioCriacao}">
									<f:facet name="header">
										<h:outputText value="Usuário" />
									</f:facet>
									<h:outputText styleClass="Padrao"
 										value="#{row.wrappedObject.comunicacao.nomeUsuarioCriacao}" />
								</rich:column>

								<rich:column sortBy="#{row.wrappedObject.possuiPDF}">																		
									<f:facet name="header">
										<h:outputText value="PDF" />
									</f:facet>
									
									<h:commandLink
											rendered="#{row.wrappedObject.possuiPDF}"
											actionListener="#{beanRevisao.abrirPdf}"> 										
											<h:graphicImage value="/images/pdf.png"> 																		
											</h:graphicImage>																							
									</h:commandLink> 									
									<rich:toolTip   followMouse="true" direction="top-left" style="width:800px"		                                             
		                                             hideDelay="20"
		                                             showDelay="250">    		                                             
		                                      		<h:outputText value="#{beanRevisao.recuperarTextoComunicacaoAction}"/>
		                            </rich:toolTip>				
		                                                               															
								</rich:column>
								
                                <rich:column>
                                    <f:facet name="header">
                                        <h:outputText value="Editar" />
                                    </f:facet>
                                    <a4j:commandLink id="btnEditarCancelaDocumento"
                                                     rendered="#{row.wrappedObject.possuiPDF}"
                                                     reRender="frmEditarDocumento"
                                                     actionListener="#{beanRevisao.recuperaLinhaParaModalPanel}"
                                                     oncomplete="Richfaces.showModalPanel('modalPanelEditarDocumento')">
                                        <h:graphicImage url="../../images/btabrir.gif"
                                                        title="Editar Documento" />
                                        <a4j:status>
								            <f:facet name="start">
								                <h:graphicImage value="/images/ai.gif" alt="ai" />
								            </f:facet>
								        </a4j:status>
                                    </a4j:commandLink>
                                </rich:column>								

								<rich:column >
									<f:facet name="header">
										<h:outputText value="Cancelar" />
									</f:facet>
									<a4j:commandLink
 										oncomplete="Richfaces.showModalPanel('modalPanelMotivoCancelaDocumento');"
 										rendered="#{row.wrappedObject.possuiPDF}"
 										action="#{beanRevisao.atualizarDocumentoSelecionado}">
										<h:graphicImage url="../../images/close.gif" title="Cancelar" />
 									</a4j:commandLink>
								</rich:column>

                                <rich:column  sortBy="#{row.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}">
                                    <f:facet name="header">
                                        <h:outputText value="Peças" />
                                    </f:facet>
                                    <h:commandLink value="peças "
                                                   rendered="#{not empty row.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}" />
                                    <rich:toolTip followMouse="false" direction="top-right"
                                                  horizontalOffset="-5" verticalOffset="-5"
                                                  styleClass="tooltipPecasEletronicas" hideDelay="20"
                                                  showDelay="250"
                                                  rendered="#{not empty row.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}">
                                        <h:dataTable var="valor" id="tblValor"
                                                     value="#{row.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}"
                                                     headerClass="CabecalhoTabelaDados"
                                                     rowClasses="LinhaParTabelaDados"
                                                     columnClasses="ColunaTabelaDados"
                                                     styleClass="TabelaDadosPreview">
                                            <h:column>
                                                <f:facet name="header">
                                                    <h:outputText value="Processo" />
                                                </f:facet>
                                                <h:panelGrid style="text-align: left;" width="60%">
                                                    <h:outputText value="#{valor.pecaProcessoEletronico.objetoIncidente.identificacao}"
                                                                  styleClass="Padrao" />
                                                </h:panelGrid>
                                            </h:column>
                                            <h:column>
                                                <f:facet name="header">
                                                    <h:outputText value="Tipo Peça" />
                                                </f:facet>
                                                <h:panelGrid style="text-align: left;" width="60%">
                                                    <h:outputText value="#{valor.pecaProcessoEletronico.tipoPecaProcesso.descricao}"
                                                                  styleClass="Padrao" />
                                                </h:panelGrid>
                                            </h:column>
                                            <h:column>
                                                <f:facet name="header">
                                                    <h:outputText value="PDF" />
                                                </f:facet>
                                                <h:commandLink action="#{assinadorBaseBean.report}">
                                                    <f:setPropertyActionListener value="#{valor}"
                                                                                 target="#{assinadorBaseBean.valor}" />
                                                    <h:graphicImage value="/images/pdf.png"></h:graphicImage>
                                                </h:commandLink>
                                            </h:column>
                                        </h:dataTable>
                                    </rich:toolTip>
                                </rich:column>

                                <rich:column sortBy="#{row.wrappedObject.documentoComunicacaoResult.listaProcessoLoteVinculados}">
                                    <f:facet name="header">
                                        <h:outputText value="Lote" />
                                    </f:facet>
                                    <h:commandLink value="vinculados "
                                                   rendered="#{not empty row.wrappedObject.documentoComunicacaoResult.listaProcessoLoteVinculados}" />
                                    <rich:toolTip followMouse="false" direction="top-right"
                                                  horizontalOffset="-5" verticalOffset="-5"
                                                  styleClass="tooltipPrecedentes" hideDelay="20"
                                                  showDelay="250"
                                                  rendered="#{not empty row.wrappedObject.documentoComunicacaoResult.listaProcessoLoteVinculados}">

                                        <h:dataTable var="valorL" id="tblValorL"
                                                     value="#{row.wrappedObject.documentoComunicacaoResult.listaProcessoLoteVinculados}"
                                                     headerClass="CabecalhoTabelaDados"
                                                     rowClasses="LinhaParTabelaDados"
                                                     columnClasses="ColunaTabelaDados"
                                                     styleClass="TabelaDadosPreview">
                                            <h:column>
                                                <f:facet name="header">
                                                    <h:outputText value="Processo" />
                                                </f:facet>
                                                <h:panelGrid style="text-align: center;" width="60%">
                                                    <h:outputText
                                                        value="#{valorL}"
                                                        styleClass="Padrao" />
                                                </h:panelGrid>
                                            </h:column>
                                        </h:dataTable>
                                    </rich:toolTip>
                                </rich:column>

                                <rich:column sortBy="#{row.wrappedObject.comunicacao.obsComunicacao}">
                                    <f:facet name="header">
                                        <h:outputText value="Observação" />
                                    </f:facet>
                                    <a4j:commandLink
                                        rendered="#{not empty row.wrappedObject.comunicacao.obsComunicacao}">
                                        <h:graphicImage value="/images/icobs.png"></h:graphicImage>
                                    </a4j:commandLink>
                                    <rich:toolTip followMouse="false" direction="top-right"
                                                  horizontalOffset="-5" verticalOffset="-5"
                                                  styleClass="tooltipPrecedentes" hideDelay="20"
                                                  showDelay="250"
                                                  rendered="#{not empty row.wrappedObject.comunicacao.obsComunicacao}">
                                        <h:outputText value="#{row.wrappedObject.comunicacao.obsComunicacao}"/>
                                    </rich:toolTip>	
                                </rich:column>									
                            </rich:dataTable>

                            <rich:datascroller id="dataScrollerDocumentos" for="tableDocumentosRevisao"
                                               fastControls="hide" maxPages="10" pageIndexVar="paginaAtual"
                                               pagesVar="paginas" eventsQueue="ajaxQueue"
                                               ignoreDupResponses="true" reRender="pnlCentral">
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
														
							<h:commandButton id="botaoImprimir" value="Imprimir" 
                                                   actionListener="#{beanRevisao.imprimirDocumentosAction}"
                                                   styleClass="BotaoPadrao" />
                                                   
							<a4j:commandButton id="botaoFinalizarRevisao" value="Finalizar Revisão"
								action="#{beanRevisao.finalizarRevisao}"
								styleClass="BotaoEstendido"/>

						</c:if>
					</a4j:outputPanel>

					<a4j:commandButton styleClass="BotaoOculto" id="idBotaoInicializar"
 						action="#{beanRevisao.inicializar}"
 						reRender="pnlCentral" onclick="exibirMsgProcessando(true)"
 						oncomplete="exibirMsgProcessando(false);" />

				</h:panelGrid>
			</h:panelGrid>
			
		</a4j:form>

		<rich:modalPanel id="modalPanelMotivoCancelaDocumento" width="630"
			height="160" keepVisualState="true" onshow="jq1_10_2('#motivoInput').val('');">
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
						<h:inputTextarea cols="80" rows="5"  id="motivoInput" onkeyup="verifyLength(this, 1000)" value="#{beanRevisao.anotacaoCancelamento}" />
					</span>
				</div>
				<div style="padding-top: 15px;">
					<span class="Padrao">
						<a4j:commandButton
							oncomplete = "Richfaces.hideModalPanel('modalPanelMotivoCancelaDocumento');"
							value = "Salvar"
							action = "#{beanRevisao.retornarParaCorrecao}"/>
						<a4j:commandButton
							onclick="Richfaces.hideModalPanel('modalPanelMotivoCancelaDocumento');"
							value="Fechar" />
					</span>
				</div>
				<div id="dvAguardePanelCancelaPDFDocumento"></div>
			</a4j:form>
		</rich:modalPanel>
		
        <rich:modalPanel id="modalPanelEditarDocumento" width="630"
                         height="160">
            <f:facet name="header">
                <h:outputText value="Favor inserir o motivo do cancelamento" />
            </f:facet>
            <h:form id="frmEditarDocumento">
                <div>
                    <span class="Padrao"> <h:outputText styleClass="Padrao"
                                  value="Motivo:" /> </span>
                </div>
                <div style="padding-top: 3px;">
                    <span> 
                    	<h:inputTextarea cols="80" rows="5" value="#{beanRevisao.anotacaoCancelamento}" onkeyup="verifyLength(this, 1000)">
                    	</h:inputTextarea>
                    </span>
                </div>
                <div style="padding-top: 15px;">
                    <span> <h:commandButton
                            actionListener="#{beanRevisao.devolverDocumentosAction}"
                            onclick="Richfaces.hideModalPanel('modalPanelEditarDocumento');"
                            value="Encaminhar para Correção" /> <h:commandButton
                            onclick="Richfaces.hideModalPanel('modalPanelEditarDocumento');"
                            value="Fechar" /> <h:commandButton
                            action="#{beanRevisao.editarDocumentos}"
                            onclick="Richfaces.hideModalPanel('modalPanelEditarDocumento');"
                            value="Editar Documento" /> </span>
                </div>
            </h:form>
        </rich:modalPanel>		

		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>