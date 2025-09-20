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
	function aguarde(mostrar, div) {
		if (mostrar == true) {
			document.getElementById(div).innerHTML = '_$tag_________________________________________<font class="Padrao">&nbsp;&nbsp;Aguarde..._$tag__';
		}
	}

	function pesquisar() {
		document.getElementById('botaoPesquisar').click();
	}

	function caixaAlta(campo) {
		campo.value = campo.value.toUpperCase();
	}
</script>

<f:view>
	<a4j:page pageTitle="::.. Principal ..::">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Elaboração por Unidade" />
			</jsp:include>
		</h:form>

		<a4j:form id="form" prependId="false">

			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">

					<a4j:outputPanel id="pnlPrincipalPesquisa" ajaxRendered="true">
						<div class="PainelTituloPesquisa">
							<span> Pesquisa Expedientes Elaborados e Enviados para
								Assinatura: </span>
						</div>

						<div style="padding-top: 10px;">
							<h:panelGrid columns="8" id="painelPesquisa">

								<h:outputText value="Data" styleClass="Padrao" />									
								<t:inputCalendar value="#{beanConsultarDocumentosUnidade.dataPesquisa}"
									renderAsPopup="true" popupWeekString="Sem"
									popupTodayString="Hoje: "
									popupTodayDateFormat="EEEE, dd/MM/yyyy"
									popupDateFormat="dd/MM/yyyy"
									onkeydown="if (event.keyCode == 13) { return pesquisar(\"btnPesquisarProcesso\"); }"
									onkeypress="return mascaraInputData(this, event)"
									onchange="validarData(this);" forceId="true">
									<f:converter converterId="dataConverter" />
								</t:inputCalendar>

								<a4j:commandButton styleClass="BotaoPesquisar"
									style="margin-left:15px;" value="Pesquisar"
									id="btnPesquisarProcesso"
									actionListener="#{beanConsultarDocumentosUnidade.pesquisarDocumentosAction}"
									ignoreDupResponses="true" reRender="form"
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" />
							</h:panelGrid>
						</div>
					</a4j:outputPanel>

					<a4j:outputPanel ajaxRendered="true" keepTransient="false"
						id="pnlResultadoPesquisa" styleClass="MolduraInterna"
						rendered="#{not empty beanConsultarDocumentosUnidade.listaDocumentos}">
						<hr color="red" align="left" size="1px" width="90%" />
						<rich:dataTable headerClass="DataTableDefaultHeader"
							styleClass="DataTableDefault"
							footerClass="DataTableDefaultFooter"
							rowClasses="DataTableRow, DataTableRow2"
							columnClasses="dezCenter, vinteCenter, vinteCenter, quinzeCenter, dezCenter, quinzeCenter, quinzeCenter"
							value="#{beanConsultarDocumentosUnidade.listaDocumentos}"
							var="wrappedDocumento"
							binding="#{beanConsultarDocumentosUnidade.tabelaDocumentos}">

							<rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.objetoIncidenteUnico.identificacao}">
								<f:facet name="header">
									<h:outputText value="Processo"
										styleClass="DataTableDocumentoTexto" />
								</f:facet>
								<h:commandLink styleClass="DataTableDocumentoTexto"
									target="_blank"
									value="#{wrappedDocumento.wrappedObject.comunicacao.identificacaoProcessual}"
									action="#{beanConsultarDocumentosUnidade.consultarProcessoDigital}">
									<f:setPropertyActionListener
										target="#{beanConsultarDocumentosUnidade.seqObjetoIncidente}"
										value="#{wrappedDocumento.wrappedObject.comunicacao.objetoIncidenteUnico.principal.id}" />
								</h:commandLink>

							</rich:column>

							<rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.modeloComunicacao.tipoComunicacao.descricao}">
								<f:facet name="header">
									<h:outputText value="Tipo Modelo" />
								</f:facet>
								<h:outputText styleClass="DataTableDocumentoTexto"
									value="#{wrappedDocumento.wrappedObject.comunicacao.modeloComunicacao.tipoComunicacao.descricao}" />
							</rich:column>

							<rich:column sortBy="#{wrappedDocumento.wrappedObject.comunicacao.dscNomeDocumento}">
								<f:facet name="header">
									<h:outputText value="Documento"
										styleClass="DataTableDocumentoTexto" />
								</f:facet>
								<h:outputText styleClass="DataTableDocumentoTexto"
									value="#{wrappedDocumento.wrappedObject.comunicacao.dscNomeDocumento}" />
							</rich:column>

							<rich:column sortBy="#{wrappedDocumento.wrappedObject.acaoRealizadaUnidade}">
								<f:facet name="header">
									<h:outputText value="Ação" />
								</f:facet>
								<h:outputText styleClass="Padrão"
									value="#{wrappedDocumento.wrappedObject.acaoRealizadaUnidade}" />
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
									<h:outputText value="Setor Atual" />
								</f:facet>
								<h:outputText styleClass="DataTableDocumentoTexto"
									value="#{wrappedDocumento.wrappedObject.comunicacao.deslocamentoAtual.setor.nome}"
									rendered="#{not empty wrappedDocumento.wrappedObject.comunicacao.deslocamentoAtual.setor.nome}" />

								<h:outputText styleClass="DataTableDocumentoTexto"
									value="#{wrappedDocumento.wrappedObject.comunicacao.setor.nome}"
									rendered="#{empty wrappedDocumento.wrappedObject.comunicacao.deslocamentoAtual.setor.nome}" />
							</rich:column>

							<rich:column sortBy="#{wrappedDocumento.wrappedObject.localizacaoAtualProcesso}">
								<f:facet name="header">
									<h:outputText value="Setor Atual do Processo" />
								</f:facet>
								<h:outputText styleClass="DataTableDocumentoTexto"
									value="#{wrappedDocumento.wrappedObject.localizacaoAtualProcesso}" />
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

					</a4j:outputPanel>

					<a4j:commandButton styleClass="BotaoOculto" id="btnAtualizaSessao"
						actionListener="#{beanConsultarDocumentosUnidade.atualizaSessaoAction}" />
					<a4j:commandButton id="botaoPesquisar" value="Pesquisar"
						styleClass="BotaoOculto"
						actionListener="#{beanConsultarDocumentosUnidade.pesquisarDocumentosAction}"
						reRender="pnlResultadoPesquisa"
						onclick="exibirMsgProcessando(true)"
						oncomplete="exibirMsgProcessando(false);" />

				</h:panelGrid>
			</h:panelGrid>
		</a4j:form>
		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>