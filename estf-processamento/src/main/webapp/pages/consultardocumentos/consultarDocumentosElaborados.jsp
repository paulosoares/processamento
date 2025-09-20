<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />


<script type="text/javascript">
	function aguarde(mostrar, div){
	      if( mostrar == true ){
	            document.getElementById(div).innerHTML = '<img src="/processamento/images/loading.gif" /><font class="Padrao">&nbsp;&nbsp;Aguarde...</font>';
	      }
	}

	function pesquisar () {
		document.getElementById('botaoPesquisar').click();
	}
	
	function caixaAlta (campo) {
		campo.value = campo.value.toUpperCase();
	} 

		
</script>

<f:view>
	<a4j:page pageTitle="::.. Principal ..::">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Expedientes Elaborados" />
			</jsp:include>
		</h:form>

		<a4j:form id="form" prependId="false">

			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">

					<a4j:outputPanel id="pnlPrincipalPesquisa" ajaxRendered="true">
						<div class="PainelTituloPesquisa">
							<span> Pesquisa: </span>
						</div>

						<div style="padding-top: 10px;">
							<h:panelGrid columns="4" id="painelPesquisa1">
								<t:panelGroup>
									<h:outputText value="Classe Processo: " styleClass="Padrao" />
									<h:inputText size="4" maxlength="4" id="txtClassePesquisa"
										value="#{beanConsultarDocumentosElaborados.siglaProcesso}"
										onkeypress="return mascaraInputLetra(this,event)" />
								</t:panelGroup>
								
								<t:panelGroup>
									<h:outputText value="Número Processo:" styleClass="Padrao" />
									<h:inputText styleClass="Input" id="txtNumeroProcesso"
										onkeypress="return mascaraInputNumerico(this,event)"
										value="#{beanConsultarDocumentosElaborados.numeroProcesso}"
										maxlength="7" />
								</t:panelGroup>
								
								<t:panelGroup>
									<h:outputText styleClass="Padrao" value='Situação:' />
									<h:selectOneMenu
										value="#{beanConsultarDocumentosElaborados.codigoFaseDocumento}"
										style="margin-left:5px;">
										<f:selectItems
											value="#{beanConsultarDocumentosElaborados.itensFaseDocumento}" />
									</h:selectOneMenu>
								</t:panelGroup>
								<t:panelGrid columns="2">
										<h:outputLabel styleClass="Padrao" value="*Período: "></h:outputLabel>
										<h:panelGrid columns="1">
											<t:panelGroup>
												<rich:calendar id="itDataInicial" value="#{beanConsultarDocumentosElaborados.dataInicial}" datePattern="dd/MM/yyyy" locale="pt_Br" />
												<h:outputLabel styleClass="Padrao" value=" a "></h:outputLabel>
												<rich:calendar id="itDataFinal" value="#{beanConsultarDocumentosElaborados.dataFinal}" datePattern="dd/MM/yyyy" locale="pt_Br" />
											</t:panelGroup>
										</h:panelGrid>
								</t:panelGrid>
							</h:panelGrid>
							
							
							<div style="padding-top: 4px; padding-left: 3px; padding-bottom: 5px;" id="pesquisaNumeracaoUnica">
								<h:outputText styleClass="Padrao" value="Número Documento (Nº/Ano):" />
								<h:inputText size="10" maxlength="10" id="txtNumeracaoUnica" style="margin-left:10px; margin-right:0px;"
									value="#{beanConsultarDocumentosElaborados.numeracaoUnica}"/>
								<h:outputText styleClass="Padrao" value="/" style="margin-left:0px;" />	
								<h:inputText size="4" maxlength="4" id="txtAnoNumeracaoUnica"
									value="#{beanConsultarDocumentosElaborados.anoNumeracaoUnica}"/>	
							</div>
							
							<h:panelGrid columns="9" id="painelPesquisa2">
								<h:outputText styleClass="Padrao" value='Setor:' />
								<h:selectOneMenu
									value="#{beanConsultarDocumentosElaborados.codigoSetorAtual}"
									style="margin-left:5px;">
									<f:selectItems
										value="#{beanConsultarDocumentosElaborados.itensSetores}" />
								</h:selectOneMenu>

								<h:selectOneRadio
									value="#{beanConsultarDocumentosElaborados.opcaoLocal}">
									<f:selectItem itemValue="1" itemLabel="de deslocamento atual" />
									<f:selectItem itemValue="2" itemLabel="que gerou situação" />
								</h:selectOneRadio>

								<a4j:commandButton styleClass="BotaoPesquisar"
									style="margin-left:15px;" value="Pesquisar"
									id="btnPesquisarProcesso"
									actionListener="#{beanConsultarDocumentosElaborados.pesquisarDocumentosAction}"
									ignoreDupResponses="true" reRender="pnlResultadoPesquisa"
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" />
								<a4j:commandButton styleClass="BotaoPadrao"
									style="margin-left:15px;" value="Limpar"
									id="btnLimparProcesso"
									actionListener="#{beanConsultarDocumentosElaborados.limparSessaoAction}"
									ignoreDupResponses="true" 
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);"/>	
							</h:panelGrid>
						</div>
					</a4j:outputPanel>

					<a4j:outputPanel ajaxRendered="true" keepTransient="false"
						id="pnlResultadoPesquisa" styleClass="MolduraInterna">
						<c:if
							test="${not empty beanConsultarDocumentosElaborados.listaDocumentos}">
							<hr color="red" align="left" size="1px" width="90%" />
							<rich:dataTable headerClass="DataTableDefaultHeader"	styleClass="DataTableDefault"	footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="quinzeCenter, quinzeCenter, vinteLeft, dezCenter, cincoCenter, cincoCenter, vinteCenter, vinteCenter, vinteCenter, vinteCenter, dezCenter, dezCenter, dezCenter"
								value="#{beanConsultarDocumentosElaborados.listaDocumentos}"	var="wrappedDocumento"	binding="#{beanConsultarDocumentosElaborados.tabelaDocumentos}"
								rows="30" id="tabelaDocumento">

								<rich:column  sortBy="#{wrappedDocumento.wrappedObject.comunicacao.idObjetoIncidenteUnico}" >
									<f:facet name="header">
										<h:outputText value="Processo"	styleClass="DataTableDocumentoTexto" />
									</f:facet>
									<h:commandLink styleClass="DataTableDocumentoTexto"	target="_blank"
										value="#{wrappedDocumento.wrappedObject.comunicacao.identificacaoProcessual}"
										action="#{beanConsultarDocumentosElaborados.consultarProcessoDigital}">
										<f:setPropertyActionListener
											target="#{beanConsultarDocumentosElaborados.seqObjetoIncidente}"
											value="#{wrappedDocumento.wrappedObject.comunicacao.idObjetoIncidenteUnico}" />
									</h:commandLink>
								</rich:column>

								<rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.modeloComunicacao.tipoComunicacao.descricao}">
									<f:facet name="header">
										<h:outputText value="Tipo Modelo" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.comunicacao.modeloComunicacao.tipoComunicacao.descricao}" />
								</rich:column>

								<rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.dscNomeDocumento}">
									<f:facet name="header">
										<h:outputText value="Documento" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.comunicacao.dscNomeDocumento}" />
								</rich:column>

								<rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.concatenaAnoNumeracaoUnica}">
									<f:facet name="header">
										<h:outputText value="Nº Documento" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.comunicacao.concatenaAnoNumeracaoUnica}" />
								</rich:column>
								
								
							

								<rich:column sortBy="#{wrappedDocumento.wrappedObject.possuiPDF}">
									<f:facet name="header">
										<h:outputText value="PDF" />
									</f:facet>
									
									
											<h:outputLink
												rendered="#{wrappedDocumento.wrappedObject.possuiPDF && ((wrappedDocumento.wrappedObject.restrito && beanConsultarDocumentosElaborados.setorPermitidoRestrito) || !wrappedDocumento.wrappedObject.restrito )  }"
												value="#{wrappedDocumento.wrappedObject.linkPDF}">
												<h:graphicImage value="/images/pdf.png"></h:graphicImage>
											</h:outputLink>
								
								
											<h:outputLink title="Documento de acesso restrito"
												rendered="#{wrappedDocumento.wrappedObject.possuiPDF && ((wrappedDocumento.wrappedObject.restrito && !beanConsultarDocumentosElaborados.setorPermitidoRestrito) )  }"
												value="#">
												<h:graphicImage  value="/images/pdf-no.png"></h:graphicImage>
											</h:outputLink>
											
										<h:outputLink title ="Sem documento PDF"
												rendered="#{!wrappedDocumento.wrappedObject.possuiPDF}"
												value="#">
												<h:graphicImage value="/images/op-nao-permitida.png"></h:graphicImage>
											</h:outputLink>
									
									
								</rich:column>
								
								<rich:column  sortBy="#{wrappedDocumento.wrappedObject.documentoComunicacaoResult.listaPecasProcessoEletronicoComunicacao}">
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
														value="#{valor.situacaoPeca eq 3 ? valor.pecaProcessoEletronico.tipoPecaProcesso.descricao : 'Peça excluída'}"
														styleClass="Padrao" />
												</h:panelGrid>
											</h:column>
											<h:column>
												<f:facet name="header">
													<h:outputText value="PDF" />
												</f:facet>
												<h:commandLink action="#{assinadorBaseBean.report}" rendered="#{valor.situacaoPeca eq 3 }">
													<f:setPropertyActionListener value="#{valor}"
														target="#{assinadorBaseBean.valor}" />
													<h:graphicImage value="/images/pdf.png"></h:graphicImage>
												</h:commandLink>
											</h:column>
	
										</h:dataTable>
									</rich:toolTip>
								</rich:column>
								
								
								<rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.faseAtual}">
									<f:facet name="header">
										<h:outputText value="Situação atual" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.comunicacao.faseAtual}" title="#{wrappedDocumento.wrappedObject.comunicacao.dataFaseAtual}" />
								</rich:column>
								
								

								<c:if test="${beanConsultarDocumentosElaborados.exibirColunaSetorFase}">

							

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Setor que gerou a situação" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.comunicacao.nomeLocalSituacao}"
										rendered="#{not empty wrappedDocumento.wrappedObject.comunicacao.nomeLocalSituacao}" />
								</rich:column>
								
								</c:if>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Setor de deslocamento atual" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.comunicacao.deslocamentoAtual.setor.nome}"
										rendered="#{not empty wrappedDocumento.wrappedObject.comunicacao.deslocamentoAtual.setor.nome}" />

									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.comunicacao.setor.nome}"
										rendered="#{empty wrappedDocumento.wrappedObject.comunicacao.deslocamentoAtual.setor.nome}" />
								</rich:column>
								
								<rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.nomeUsuarioCriacao}">
									<f:facet name="header">
										<h:outputText value="Usuário" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.comunicacao.nomeUsuarioCriacao}" />
								</rich:column>

								<rich:column>
									<f:facet name="header">
										<h:outputText value="Data Entrada" />
									</f:facet>
									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.comunicacao.deslocamentoAtual.dataEntrada}"
										rendered="#{not empty wrappedDocumento.wrappedObject.comunicacao.deslocamentoAtual.dataEntrada}" />

									<h:outputText styleClass="Padrao"
										value="#{wrappedDocumento.wrappedObject.comunicacao.dataCriacao}"
										rendered="#{empty wrappedDocumento.wrappedObject.comunicacao.deslocamentoAtual.dataEntrada}" />
								</rich:column>
								
								
								<rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.obsComunicacao}">
									<f:facet name="header">
										<h:outputText value="Observacao" />
									</f:facet>
									<a4j:commandLink
										rendered="#{not empty wrappedDocumento.wrappedObject.comunicacao.obsComunicacao}">
										<h:graphicImage value="/images/icobs.png"></h:graphicImage>
									</a4j:commandLink>
									<rich:toolTip followMouse="false" direction="top-right"
										horizontalOffset="-5" verticalOffset="-5"
										styleClass="tooltipPrecedentes" hideDelay="20"
										showDelay="250"
										rendered="#{not empty wrappedDocumento.wrappedObject.comunicacao.obsComunicacao}">
										<h:outputText value="#{wrappedDocumento.wrappedObject.comunicacao.obsComunicacao}"/>
									</rich:toolTip>	
								</rich:column>
							</rich:dataTable>
							<rich:datascroller for="tabelaDocumento" maxPages="10">
								<f:facet name="first">
									<h:outputText value="Primeira" />
								</f:facet>
								<f:facet name="last">
									<h:outputText value="Ultima"/>
								</f:facet>
							</rich:datascroller>
						</c:if>
					</a4j:outputPanel>

					<a4j:commandButton styleClass="BotaoOculto" id="btnAtualizaSessao"
						actionListener="#{beanConsultarDocumentosElaborados.atualizaSessaoAction}" />
					<a4j:commandButton id="botaoPesquisar" value="Pesquisar"
						styleClass="BotaoOculto"
						actionListener="#{beanConsultarDocumentosElaborados.pesquisarDocumentosAction}"
						reRender="pnlResultadoPesquisa"
						onclick="exibirMsgProcessando(true)"
						oncomplete="exibirMsgProcessando(false);" />

				</h:panelGrid>
			</h:panelGrid>
				
		</a4j:form>
		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>
