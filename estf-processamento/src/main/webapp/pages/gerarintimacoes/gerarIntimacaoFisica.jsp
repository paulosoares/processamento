
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<script type="text/javascript">
	function validarCampos() {
		if (document.getElementById('responsavel').value == '') {
			alert('Favor informe o "Responsável".');
			return false;
		}
		if (document.getElementById('cargoResponsavel').value == '') {
			alert('Favor informe o "Cargo".');
			return false;
		}

		return true;

	}

	function marcarDesmarcarCheckboxes(elementoPai, valor) {
		for (var indice = 0; indice < $(elementoPai).getElementsByTagName(
				'input').length; indice++) {
			var input = $(elementoPai).getElementsByTagName('input')[indice];
			if (input.type == "checkbox") {
				input.checked = valor;
			}
		}
	}
</script>

<f:view>

	<a4j:page pageTitle="::.. Principal ..::">

		<h:form id="form" prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Gerar Intimações" />
			</jsp:include>

			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">
					<a4j:outputPanel id="pnlPrincipalPesquisa" ajaxRendered="true">
						<h:panelGrid styleClass="PadraoTituloPanel" id="pnlFiltro"
							cellpadding="0" cellspacing="0">
							<h:outputText value="Filtros de pesquisa por data" />
						</h:panelGrid>
						<h:panelGrid styleClass="Moldura">
							<h:panelGrid columns="6" id="pnlPesquisa">
								<h:outputText value="Data de Divulgação do DJe:" />
								<rich:calendar id="idDataIntimacao" datePattern="dd/MM/yyyy"
									locale="pt_Br"
									value="#{beanPartesGerarIntimacao.dataPublicacao}" />

								<h:outputText value="Tipo Intimação:" />
								<h:selectOneMenu id="tipoIntimacao"
									style="width:90px; margin-left: 4px;"
									value="#{beanPartesGerarIntimacao.tipoMeioComunicacaoEnum}">
									<f:selectItems
										value="#{beanPartesGerarIntimacao.listaTipoIntimacao}" />
								</h:selectOneMenu>
							</h:panelGrid>
						</h:panelGrid>

						<a4j:commandButton styleClass="BotaoPesquisar" value="Pesquisar"
							id="btnPesquisarPartes" ignoreDupResponses="true"
							reRender="pnlPrincipalResultado,pnlInput,gerarIntimacoes,labelResponsavel,responsavel,labelCargo,cargoResponsavel"
							onclick="exibirMsgProcessando(true)"
							oncomplete="exibirMsgProcessando(false);"
							action="#{beanPartesGerarIntimacao.pesquisarPartes}" />
					</a4j:outputPanel>

					<br />
					<a4j:outputPanel id="panelProcessosNaoIntimaveis"
						ajaxRendered="true">
						<h:panelGrid style="text-align: center; font-size:12px;">
							<h:outputLabel styleClass="PadraoTituloPanel"
								style="color: #B22222;"
								value="#{beanPartesGerarIntimacao.processosSemSetor}"
								rendered="#{beanPartesGerarIntimacao.qntProcessosSemSetor > 0}" />
						</h:panelGrid>
					</a4j:outputPanel>

					<br />
					
					<h:panelGrid id="pnlInput">
						<div>
							<h:panelGroup>
								<h:outputLabel value="Responsável* :" styleClass="Padrao"
									id="labelResponsavel"
									rendered="#{!empty beanPartesGerarIntimacao.partes}" />
								<span class="Padrao" style="margin-left: 10px"> <h:inputText
										id="responsavel"
										rendered="#{!empty beanPartesGerarIntimacao.partes}"
										value="#{beanPartesGerarIntimacao.responsavelAssinatura}"
										size="60" />
								</span>
							</h:panelGroup>
							<h:panelGroup>
								<h:outputLabel value="Cargo* :" styleClass="Padrao"
									id="labelCargo"
									rendered="#{!empty beanPartesGerarIntimacao.partes}" />
								<span class="Padrao" style="margin-left: 49px"> 
								<h:inputText
										id="cargoResponsavel"
										value="#{beanPartesGerarIntimacao.cargoResponsavelAssinatura}"
										rendered="#{!empty beanPartesGerarIntimacao.partes}" size="60" />
								</span>
								<span class="Padrao" style="margin-left: 10px"> 
								
								<a4j:commandButton styleClass="BotaoSelecionarTodos"
								rendered="#{!empty beanPartesGerarIntimacao.partes}"
								value="Gerar Intimações" id="gerarIntimacoes"
								action="#{beanPartesGerarIntimacao.gerar}"
								ignoreDupResponses="true" reRender="pnlPrincipalResultado, pnlMessages"
								onclick="if(!validarCampos()){return;}else{exibirMsgProcessando(true);}"
								oncomplete="exibirMsgProcessando(false);" />
							</h:panelGroup>
						</div>
					</h:panelGrid>					
					
					<br />
					<br />					
					
					<t:saveState value="#{beanPartesGerarIntimacao.partes}"></t:saveState>
					<a4j:outputPanel id="pnlPrincipalResultado">
						<h:panelGrid styleClass="PadraoTituloPanel" id="pnlResultadoTotal"
							rendered="#{!empty beanPartesGerarIntimacao.partes}">
							<h:outputText value="Lista Partes" />
						</h:panelGrid>

						<div>
							<rich:dataTable headerClass="DataTableDefaultHeader"
								rendered="#{!empty beanPartesGerarIntimacao.partes}"
								id="tabelaResultado" styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="tres, oitoLeft,oitoLeft,tres,vinte"
								value="#{beanPartesGerarIntimacao.partes}" var="parte">
								<rich:column id="columnCheck">
									<f:facet name="header">
										<h:selectBooleanCheckbox
											value="#{beanPartesGerarIntimacao.todasPartesSelecionadas}"
											onclick="marcarDesmarcarCheckboxes('tabelaResultado', this.checked)" />
									</f:facet>
									<h:selectBooleanCheckbox id="checkboxParte"
										value="#{parte.selected}">
										<a4j:support reRender="tabelaResultado" />
									</h:selectBooleanCheckbox>
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Partes" />
									</f:facet>
									<h:outputText value="(#{parte.seqPessoa}) " />
									<h:outputText value="#{parte.nomeParte}" />
								</rich:column>
								<rich:column>
									<f:facet name="header">
										<h:outputText value="Preferência <br/> de Intimação"
											escape="false" />
									</f:facet>
									<h:outputText value="#{parte.descricaoMeioIntimacao}" />
								</rich:column>
								<rich:column style="width:250px;">
									<f:facet name="header">
										<h:outputText value="Processo(s)" />
									</f:facet>

									<h:dataTable value="#{parte.processosFisicosIntimados}"
										var="processo"
										rendered="#{!empty parte.processosFisicosIntimados}">
										<h:column>
											<f:facet name="header">
												<h:outputText value="Processos Físicos Intimados"
													style="float: left;" />
											</f:facet>
											<a4j:commandLink
												value="#{processo.siglaClasseProcesso} #{processo.numeroProcesso} (#{processo.qtdPecasIntimadas})">

												<rich:toolTip followMouse="false" direction="top-right"
													horizontalOffset="-5" verticalOffset="-5"
													styleClass="tooltipPecasEletronicas" hideDelay="20"
													showDelay="250">

													<h:dataTable value="#{processo.pecasIntimadas}" var="peca"
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
																	value="#{peca.descricaoTipoPecaProcessual}"
																	styleClass="Padrao" />
															</h:panelGrid>
														</h:column>


														<h:column>
															<f:facet name="header">
																<h:outputText value="PDF" />
															</f:facet>
															<h:commandLink
																action="#{assinadorBaseBean.obterPecaArquivoPDF}">
																<f:setPropertyActionListener
																	value="#{peca.tipoPecaProcesso.descricao}"
																	target="#{assinadorBaseBean.nomeDocumentoDownload}" />
																<f:setPropertyActionListener
																	value="#{peca.seqDocumento}"
																	target="#{assinadorBaseBean.seqDocumentoPeca}" />
																<h:graphicImage value="/images/pdf.png"></h:graphicImage>
															</h:commandLink>

														</h:column>

													</h:dataTable>
												</rich:toolTip>

											</a4j:commandLink>

											<h:graphicImage url="../../images/ok.png" title="Intimação já cadastrada"/>
											
										</h:column>
									</h:dataTable>

									<h:dataTable value="#{parte.processosFisicosNaoIntimados}"
										var="processo"
										rendered="#{!empty parte.processosFisicosNaoIntimados}">
										<h:column>
											<f:facet name="header">
												<h:outputText value="Processos Físicos Não Intimados"
													style="float: left;" />
											</f:facet>
											<a4j:commandLink
												value="#{processo.siglaClasseProcesso} #{processo.numeroProcesso} (#{processo.qtdPecasNaoIntimadas})">

												<rich:toolTip followMouse="false" direction="top-right"
													horizontalOffset="-5" verticalOffset="-5"
													rendered="#{!empty processo.pecasNaoIntimadas}"
													styleClass="tooltipPecasEletronicas" hideDelay="20"
													showDelay="250">

													<h:dataTable value="#{processo.pecasNaoIntimadas}"
														var="peca" headerClass="CabecalhoTabelaDados"
														rowClasses="LinhaParTabelaDados"
														columnClasses="ColunaTabelaDados"
														styleClass="TabelaDadosPreview">

														<h:column>
															<f:facet name="header">
																<h:outputText value="Processo" />
															</f:facet>
															<h:panelGrid style="text-align: left;" width="60%">
																<h:outputText
																	value="#{peca.descricaoTipoPecaProcessual}"
																	styleClass="Padrao" />
															</h:panelGrid>
														</h:column>



														<h:column>
															<f:facet name="header">
																<h:outputText value="PDF" />
															</f:facet>
															<h:commandLink
																action="#{assinadorBaseBean.obterPecaArquivoPDF}">
																<f:setPropertyActionListener
																	value="#{peca.tipoPecaProcesso.descricao}"
																	target="#{assinadorBaseBean.nomeDocumentoDownload}" />
																<f:setPropertyActionListener
																	value="#{peca.seqDocumento}"
																	target="#{assinadorBaseBean.seqDocumentoPeca}" />
															</h:commandLink>

														</h:column>

													</h:dataTable>
												</rich:toolTip>

											</a4j:commandLink>
											
											<h:selectBooleanCheckbox id="checkboxPecaFisicaNaoIntimada" value="#{processo.selected}"/>											
											
										</h:column>
									</h:dataTable>

									<!--  -->
									<h:dataTable value="#{parte.processosEletronicosIntimados}"
										var="processo"
										rendered="#{!empty parte.processosEletronicosIntimados}">
										<h:column>
											<f:facet name="header">
												<h:outputText value="Processos Eletrônicos Intimados"
													style="float: left;" />
											</f:facet>
											<a4j:commandLink
												value="#{processo.siglaClasseProcesso} #{processo.numeroProcesso} (#{processo.qtdPecasIntimadas})">

												<rich:toolTip followMouse="false" direction="top-right"
													horizontalOffset="-5" verticalOffset="-5"
													rendered="#{!empty processo.pecasIntimadas}"
													styleClass="tooltipPecasEletronicas" hideDelay="20"
													showDelay="250">

													<h:dataTable value="#{processo.pecasIntimadas}" var="peca"
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
																	value="#{peca.descricaoTipoPecaProcessual}"
																	styleClass="Padrao" />
															</h:panelGrid>
														</h:column>



														<h:column>
															<f:facet name="header">
																<h:outputText value="PDF" />
															</f:facet>
															<h:commandLink
																action="#{assinadorBaseBean.obterPecaArquivoPDF}">
																<f:setPropertyActionListener
																	value="#{peca.tipoPecaProcesso.descricao}"
																	target="#{assinadorBaseBean.nomeDocumentoDownload}" />
																<f:setPropertyActionListener
																	value="#{peca.seqDocumento}"
																	target="#{assinadorBaseBean.seqDocumentoPeca}" />
																<h:graphicImage value="/images/pdf.png"></h:graphicImage>
															</h:commandLink>

														</h:column>

													</h:dataTable>
												</rich:toolTip>

											</a4j:commandLink>
											
											<h:graphicImage url="../../images/ok.png" title="Intimação já cadastrada"/>
											
										</h:column>
									</h:dataTable>

									<h:dataTable value="#{parte.processosEletronicosNaoIntimados}"
										var="processo"
										rendered="#{!empty parte.processosEletronicosNaoIntimados}">
										<h:column>
											<f:facet name="header">
												<h:outputText value="Processos Eletrônicos Não Intimados"
													style="float: left;" />
											</f:facet>
											<a4j:commandLink
												value="#{processo.siglaClasseProcesso} #{processo.numeroProcesso} (#{processo.qtdPecasNaoIntimadas})">

												<rich:toolTip followMouse="false" direction="top-right"
													horizontalOffset="-5" verticalOffset="-5"
													styleClass="tooltipPecasEletronicas" hideDelay="20"
													showDelay="250">

													<h:dataTable value="#{processo.pecasNaoIntimadas}"
														var="peca" headerClass="CabecalhoTabelaDados"
														rowClasses="LinhaParTabelaDados"
														columnClasses="ColunaTabelaDados"
														styleClass="TabelaDadosPreview">

														<h:column>
															<f:facet name="header">
																<h:outputText value="Processo" />
															</f:facet>
															<h:panelGrid style="text-align: left;" width="60%">
																<h:outputText
																	value="#{peca.descricaoTipoPecaProcessual}"
																	styleClass="Padrao" />
															</h:panelGrid>
														</h:column>


														<h:column>
															<f:facet name="header">
																<h:outputText value="PDF" />
															</f:facet>
															<h:commandLink
																action="#{assinadorBaseBean.obterPecaArquivoPDF}">
																<f:setPropertyActionListener
																	value="#{peca.tipoPecaProcesso.descricao}"
																	target="#{assinadorBaseBean.nomeDocumentoDownload}" />
																<f:setPropertyActionListener
																	value="#{peca.seqDocumento}"
																	target="#{assinadorBaseBean.seqDocumentoPeca}" />
																<h:graphicImage value="/images/pdf.png"></h:graphicImage>
															</h:commandLink>

														</h:column>

													</h:dataTable>
												</rich:toolTip>

											</a4j:commandLink>

											<h:selectBooleanCheckbox id="checkboxPecaEletronicaNaoIntimada" value="#{processo.selected}"/>
											
										</h:column>
									</h:dataTable>


								</rich:column>

							</rich:dataTable>
						</div>

					</a4j:outputPanel>



				</h:panelGrid>
			</h:panelGrid>

			<jsp:include page="/pages/template/footer.jsp" flush="true" />
		</h:form>
	</a4j:page>

</f:view>
