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

	function verifyLength(field, maxlength) {
		if (field.value.length > maxlength)
			field.value = field.value.substring(0, maxlength);
	}
</script>

<f:view>
	<a4j:page pageTitle="::.. Principal ..::" onload="pesquisar();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Finalizar Restritos" />





			</jsp:include>
		</h:form>

		<a4j:form id="form" prependId="false">
			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="2"
					id="pnlCentral">

					<a4j:outputPanel ajaxRendered="true" keepTransient="false"
						id="pnlResultadoPesquisa" styleClass="MolduraInterna">

<p/>
						

<c:if test="${beanFinalizarRestritos.total == 0 }">
<p/>
<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">
					<a4j:outputPanel id="pnlEtiquetaProcesso" ajaxRendered="true">
						<h:panelGrid id="pnlFiltroCadastro" styleClass="PadraoTituloPanel"
							cellpadding="0" cellspacing="0">
							<h:outputLabel style="font-size: 10pt" value=" Nenhum registro encontrado." />
						</h:panelGrid>
					</a4j:outputPanel>
				</h:panelGrid>
			</h:panelGrid>
			
						</c:if>


<c:if test="${beanFinalizarRestritos.total > 0 }">
<p/>



		<h:panelGrid styleClass="MolduraExterna" cellpadding="2"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">
					<a4j:outputPanel id="pnlEtiquetaProcesso" ajaxRendered="true">
						<h:panelGrid id="pnlFiltroCadastro" styleClass="PadraoTituloPanel"
							cellpadding="0" cellspacing="0">
							<h:outputLabel style="font-size: 10pt" value="Resultado da Pesquisa - Quant. de Registros: #{beanFinalizarRestritos.total}" />
							
<a4j:commandButton styleClass="BotaoMaior"
							id="idBotaoInicializar102" value="Ver todos"
							onclick="exibirMsgProcessando(true)"
							action="#{beanFinalizarRestritos.setRegPagina}"
							reRender="pnlCentral" oncomplete="exibirMsgProcessando(false);" />
							
						</h:panelGrid>
					</a4j:outputPanel>
				</h:panelGrid>
			</h:panelGrid>
			 


						
						</c:if>
						<c:if
							test="${not empty beanFinalizarRestritos.documentosAguadandoRevisao}">
							<div class="PainelTituloCriaTexto">
								<span>  </span>
							</div>

							<rich:dataTable id="tableDocumentosRevisao"
								headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2" rows="#{beanFinalizarRestritos.regPagina}" var="row"
								columnClasses="tres, dezCenter, dezCenter, vinteLeft, vinteLeft, vinteLeft, dezCenter, tres, tres, tres, cinco, sete"
								value="#{beanFinalizarRestritos.documentosAguadandoRevisao}"
								binding="#{beanFinalizarRestritos.tabelaAguardandoRevisao}">

								<rich:column style="width:1%">
									<f:facet name="header">
										<a4j:commandButton image="../../images/setabaixo.gif"
											action="#{beanFinalizarRestritos.marcarTodosTextos}" />
									</f:facet>
									<h:selectBooleanCheckbox value="#{row.checked}" />
								</rich:column>

								<rich:column
									sortBy="#{row.wrappedObject.comunicacao.deslocamentoAtual.dataEntrada}"
									sortOrder="ASCENDING" style="width:1%">
									<f:facet name="header">
										<h:outputText value="Data de Entrada"
											styleClass="DataTableDocumentoTexto" />
									</f:facet>

									<h:outputText styleClass="Padrao"
										value="#{row.wrappedObject.comunicacao.deslocamentoAtual.dataEntrada}" />

								</rich:column>

								<rich:column
									sortBy="#{row.wrappedObject.comunicacao.identificacaoProcessual}"
									style="width:3%">
									<f:facet name="header">
										<h:outputText value="Processo"
											styleClass="DataTableDocumentoTexto" />
									</f:facet>

									<h:commandLink styleClass="PadraoLink" target="_blank"
										value="#{row.wrappedObject.comunicacao.identificacaoProcessual}"
										action="#{beanFinalizarRestritos.consultarProcessoDigital}">
										<f:setPropertyActionListener
											target="#{beanFinalizarRestritos.seqObjetoIncidente}"
											value="#{row.wrappedObject.comunicacao.objetoIncidenteUnico.principal.id}" />
									</h:commandLink>

								</rich:column>

								<rich:column
									sortBy="#{row.wrappedObject.comunicacao.concatenaAnoNumeracaoUnica}"
									style="width:1%">
									<f:facet name="header">
										<h:outputText value="Nº Documento"
											styleClass="DataTableDocumentoTexto" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{row.wrappedObject.comunicacao.concatenaAnoNumeracaoUnica}" />
								</rich:column>

								<rich:column
									sortBy="#{row.wrappedObject.comunicacao.nomeMinistroRelatorAtual}"
									style="width:10%">
									<f:facet name="header">
										<h:outputText value="Relator"
											styleClass="DataTableDocumentoTexto" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{row.wrappedObject.comunicacao.nomeMinistroRelatorAtual}" />
								</rich:column>

								<rich:column
									sortBy="#{row.wrappedObject.comunicacao.dscNomeDocumento}"
									style="width:15%">
									<f:facet name="header">
										<h:outputText value="Nome Documento"
											styleClass="DataTableDocumentoTexto" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{row.wrappedObject.comunicacao.dscNomeDocumento}" />
								</rich:column>

								<rich:column
									sortBy="#{row.wrappedObject.comunicacao.modeloComunicacao.tipoComunicacao.descricao}"
									style="width:5%">
									<f:facet name="header">
										<h:outputText value="Tipo Documento" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{row.wrappedObject.comunicacao.modeloComunicacao.tipoComunicacao.descricao}" />
								</rich:column>

								<rich:column
									sortBy="#{row.wrappedObject.comunicacao.descricaoFaseAtual}"
									style="width:3%">
									<f:facet name="header">
										<h:outputText value="Situação" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{row.wrappedObject.comunicacao.descricaoFaseAtual}" />
								</rich:column>

								<rich:column
									sortBy="#{row.wrappedObject.comunicacao.descricaoFaseAtual}"
									style="width:3%">
									<f:facet name="header">
										<h:outputText value="Localização" />
									</f:facet>
									<h:outputText styleClass="Padrao" title="#{row.wrappedObject.comunicacao.deslocamentoAtual.setor.nome}"
										value="#{row.wrappedObject.comunicacao.deslocamentoAtual.setor.sigla}" />
								</rich:column>


								<rich:column
									sortBy="#{row.wrappedObject.comunicacao.nomeUsuarioCriacao}"
									style="width:3%">
									<f:facet name="header">
										<h:outputText value="Usuário" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{row.wrappedObject.comunicacao.nomeUsuarioCriacao}" />
								</rich:column>

								<rich:column sortBy="#{row.wrappedObject.possuiPDF}"
									style="width:1%">
									<f:facet name="header">
										<h:outputText value="PDF" />
									</f:facet>

									<h:commandLink rendered="#{row.wrappedObject.possuiPDF}"
										actionListener="#{beanFinalizarRestritos.abrirPdf}">
										<h:graphicImage value="/images/pdf.png" title="Documento: #{row.wrappedObject.documentoComunicacaoResult.documentoComunicacao.documentoEletronicoView.id }">
										</h:graphicImage>
										
											<h:graphicImage width="16" height="16" title="Peça não encontrada" rendered="#{row.wrappedObject.documentoComunicacaoResult.descricaoSituaoPeca eq '0' }" value="/images/op-nao-permitida.gif">
										
										</h:graphicImage>
										
										<h:graphicImage title="Pendente de Juntada: #{row.wrappedObject.documentoComunicacaoResult.numeroOrdemPeca}" rendered="#{row.wrappedObject.documentoComunicacaoResult.descricaoSituaoPeca eq '2' }" value="/images/peca_pend.png">
										
										</h:graphicImage>
										
										<h:graphicImage title="Juntada: #{row.wrappedObject.documentoComunicacaoResult.numeroOrdemPeca}" rendered="#{row.wrappedObject.documentoComunicacaoResult.descricaoSituaoPeca eq '3' }" value="/images/peca_junt.png">
										</h:graphicImage>
										
									</h:commandLink>
								 

								</rich:column>

								<rich:column
									sortBy="#{row.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}"
									style="width:1%">
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
													<h:outputText
														value="#{valor.pecaProcessoEletronico.objetoIncidente.identificacao}"
														styleClass="Padrao" />
												</h:panelGrid>
											</h:column>
											<h:column>
												<f:facet name="header">
													<h:outputText value="Tipo Peça" />
												</f:facet>
												<h:panelGrid style="text-align: left;" width="60%">
													<h:outputText
														value="#{valor.pecaProcessoEletronico.tipoPecaProcesso.descricao}"
														styleClass="Padrao" />
												</h:panelGrid>
											</h:column>
											<h:column>
												<f:facet name="header">
													<h:outputText value="PDF XXX" />
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

								<rich:column
									sortBy="#{row.wrappedObject.comunicacao.obsComunicacao}"
									style="width:1%">
									<f:facet name="header">
										<h:outputText value="Obs" />
									</f:facet>
									<a4j:commandLink
										rendered="#{not empty row.wrappedObject.comunicacao.obsComunicacao}">
										<h:graphicImage value="/images/icobs.png"></h:graphicImage>
									</a4j:commandLink>
									<rich:toolTip followMouse="false" direction="top-right"
										horizontalOffset="-5" verticalOffset="-5"
										styleClass="tooltipPrecedentes" hideDelay="20" showDelay="250"
										rendered="#{not empty row.wrappedObject.comunicacao.obsComunicacao}">
										<h:outputText
											value="#{row.wrappedObject.comunicacao.obsComunicacao}" />
									</rich:toolTip>
								</rich:column>
							</rich:dataTable>

							<rich:datascroller id="dataScrollerDocumentos"
								for="tableDocumentosRevisao" fastControls="hide" maxPages="10"
								pageIndexVar="paginaAtual" pagesVar="paginas"
								eventsQueue="ajaxQueue" ignoreDupResponses="true"
								reRender="pnlCentral">
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

<p/>
						<a4j:commandButton styleClass="BotaoMaior"
							id="idBotaoInicializar180" value="Atualizar"
							onclick="exibirMsgProcessando(true)"
							action="#{beanFinalizarRestritos.inicializar}"
							reRender="pnlCentral" oncomplete="exibirMsgProcessando(false);" /> 
							
							<a4j:commandButton id="botaoFinalizarRevisao2"
								value="Finalizar e Juntar"
								title="Finaliza o expediente e junta a peça ao processo"
								action="#{beanFinalizarRestritos.finalizarJuntar}"
								onclick="exibirMsgProcessando(true)"
								oncomplete="exibirMsgProcessando(false);"
								styleClass="BotaoMaior" />

							<a4j:commandButton id="botaoFinalizarRevisao1"
								value="Finalizar s/Juntar"
								title="Finaliza o expediente e NÃO junta a peça ao processo."
								action="#{beanFinalizarRestritos.finalizarSemJuntar}"
								onclick="exibirMsgProcessando(true)"
								oncomplete="exibirMsgProcessando(false);"
								styleClass="BotaoMaior" />



							<a4j:commandButton id="botaoFinalizarRevisao3" value="Juntar"
								title="Apenas junta a peça ao processo."
								onclick="exibirMsgProcessando(true)"
								action="#{beanFinalizarRestritos.finalizarApenasJuntar}"
								oncomplete="exibirMsgProcessando(false)" styleClass="BotaoMaior" />

							<a4j:commandButton id="botaoFinalizarRevisao4"
								value="Desfazer Juntada"
								title="Apenas desjunta a peça do processo, ficando-a como 'Pendente'"
								onclick="exibirMsgProcessando(true)"
								action="#{beanFinalizarRestritos.finalizarApenasDesJuntar}"
								oncomplete="exibirMsgProcessando(false)" styleClass="BotaoMaior" />

						</c:if>
					</a4j:outputPanel>

					<a4j:commandButton styleClass="BotaoOculto" id="idBotaoInicializar"
						onclick="exibirMsgProcessando(true)"
						action="#{beanFinalizarRestritos.inicializar}"
						reRender="pnlCentral" oncomplete="exibirMsgProcessando(false);" />

					

				</h:panelGrid>
			</h:panelGrid>

		</a4j:form>

		<rich:modalPanel id="modalPanelMotivoCancelaDocumento" width="630"
			height="160" keepVisualState="true"
			onshow="jq1_10_2('#motivoInput').val('');">
			<f:facet name="header">
				<h:outputText value="Favor inserir o motivo do cancelamento" />
			</f:facet>
			<a4j:form prependId="false">
				<div>
					<span class="Padrao"> <h:outputText styleClass="Padrao"
							value="Motivo:" />
					</span>
				</div>
				<div style="padding-top: 3px;">
					<span> <h:inputTextarea cols="80" rows="5" id="motivoInput"
							onkeyup="verifyLength(this, 1000)"
							value="#{beanFinalizarRestritos.anotacaoCancelamento}" />
					</span>
				</div>
				<div style="padding-top: 15px;">
					<span class="Padrao"> <a4j:commandButton
							oncomplete="Richfaces.hideModalPanel('modalPanelMotivoCancelaDocumento');"
							value="Salvar"
							action="#{beanFinalizarRestritos.retornarParaCorrecao}" /> <a4j:commandButton
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
							value="Motivo:" />
					</span>
				</div>
				<div style="padding-top: 3px;">
					<span> <h:inputTextarea cols="80" rows="5"
							value="#{beanFinalizarRestritos.anotacaoCancelamento}"
							onkeyup="verifyLength(this, 1000)">
						</h:inputTextarea>
					</span>
				</div>
				
			</h:form>
		</rich:modalPanel>

		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>