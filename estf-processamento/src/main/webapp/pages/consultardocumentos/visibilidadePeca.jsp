<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<script type="text/javascript">
	function aguarde(mostrar, div) {
		if (mostrar == true) {
			document.getElementById(div).innerHTML = '<img src="/processamento/images/loading.gif" /><font class="Padrao">&nbsp;&nbsp;Aguarde...</font>';
		}
	}

	function caixaAlta(campo) {
		campo.value = campo.value.toUpperCase();
	}

	function verificaOpcoesPesquisa() {
		var desabilitar = document.getElementById('cbProcessoGabinete').checked;
		document.getElementById('inputClasse').disabled = desabilitar;
		document.getElementById('inputProcesso').disabled = desabilitar;

		if (desabilitar) {
			document.getElementById('lbClasse').setAttribute('style',
					'opacity:0.4');
			document.getElementById('lbProcesso').setAttribute('style',
					'opacity:0.4');

			document.getElementById('inputClasse').setAttribute('style',
					'margin-left: 36px; opacity:0.4');
			document.getElementById('inputProcesso').setAttribute('style',
					'margin-left: 20px; opacity:0.4');
		} else {
			document.getElementById('lbClasse').setAttribute('style',
					'opacity:1');
			document.getElementById('lbProcesso').setAttribute('style',
					'opacity:1');

			document.getElementById('inputClasse').setAttribute('style',
					'margin-left: 36px; opacity:1');
			document.getElementById('inputProcesso').setAttribute('style',
					'margin-left: 20px; opacity:1');
		}
	}
</script>

<f:view>
	<a4j:page pageTitle="::.. Principal ..::">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Visibilidade de Peça" />
			</jsp:include>
			<a name="ancora"></a>
		</h:form>

		<a4j:form id="form" prependId="false"
			onreset="verificaOpcoesPesquisa();">
			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0"
					id="pnlCentral">


					<a4j:outputPanel id="pnlCriaTexto" styleClass="MolduraInterna">
						<a4j:outputPanel ajaxRendered="true" keepTransient="false"
							id="pnlBuscaModelo">
							<div class="PainelTituloCriaTexto">
								<span>Pesquisa por Processo:</span>
							</div>
						</a4j:outputPanel>

						<rich:hotKey key="return" handler="pesquisar();" />

						<div style="padding-top: 8px;">
							<span class="Padrao">Visibilidade:</span>
							<span>
								<h:selectOneMenu immediate="true"
									value="#{beanVisibilidadePeca.filtroVisualizar}"
									style="margin-left: 9px">
									<f:selectItems
										value="#{beanVisibilidadePeca.tiposSituacaoPeca}" />
								</h:selectOneMenu>
							</span>
						</div>

						<div style="padding-top: 5px;">
							<span class="Padrao">
								<h:outputLabel value="Classe: "
									id="lbClasse"></h:outputLabel>
							</span>
							<span>
								<h:inputText size="28" style="margin-left: 36px"
									id="inputClasse" onkeydown="pesquisar('btPesquisar');"
									value="#{beanVisibilidadePeca.siglaClasse}" />
							</span>
						</div>
						
						<div style="padding-top: 5px;">
							<span class="Padrao">
								<h:outputLabel value="Processo: "
									id="lbProcesso">
								</h:outputLabel>
							</span>
							<span>
								<h:inputText size="28" style="margin-left: 20px"
									id="inputProcesso" onkeydown="pesquisar('btPesquisar');"
									value="#{beanVisibilidadePeca.numeroProcesso}" />
							</span>
						</div>
						
				
						<div style="padding-top: 8px;">
							<span style="padding-left: 5px;">
								<a4j:commandButton id="btPesquisar" value="Pesquisar" styleClass="BotaoPesquisar"
									actionListener="#{beanVisibilidadePeca.pesquisarPecasAction}"
									reRender="pnlCentral" onclick="exibirMsgProcessando(true);"
									oncomplete="exibirMsgProcessando(false); verificaOpcoesPesquisa();" />
							</span>
						</div>
					

					</a4j:outputPanel>

						<h:panelGrid id="pecasPanel" styleClass="MolduraInterna" 
							rendered="#{not empty beanVisibilidadePeca.listaPecaProcessoEletronico}">
							<hr color="red" align="left" size="1px" width="90%"
								style="margin-left: 10px" />
							<h:outputText styleClass="PainelTituloCriaTexto"
								style="margin-left: 10px" value="Peças:" />
							
							<a4j:outputPanel ajaxRendered="true" keepTransient="false"
								id="pnlResultadoPesquisaPeca" styleClass="MolduraInterna">
								
								<rich:datascroller id="scroll" for="tablePecas" maxPages="20" />
								<rich:dataTable headerClass="DataTableDefaultHeader"
									id="tablePecas" styleClass="DataTableDefault"
									footerClass="DataTableDefaultFooter"
									rows="15"
									rowClasses="DataTableRow, DataTableRow2"
									value="#{beanVisibilidadePeca.listaPecaProcessoEletronico}"
									var="wrappedPecaEletr"
									binding="#{beanVisibilidadePeca.tabelaPecaProcessoEletronico}">

									<rich:column sortBy="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.objetoIncidente.principal.identificacao}">
										<f:facet name="header">
											<h:outputText value="Processo" />
										</f:facet>
										<h:outputText
											value="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.objetoIncidente.principal.identificacao}" />
									</rich:column>

									<rich:column >
										<f:facet name="header">
											<h:outputText value="Relator" />
										</f:facet>
										<h:outputText value="#{beanVisibilidadePeca.relator}" />
									</rich:column>

									<rich:column sortBy="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.tipoPecaProcesso.descricao}">
										<f:facet name="header">
											<h:outputText value="Tipo da Peça" />
										</f:facet>
										<h:outputText
											value="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.tipoPecaProcesso.descricao}" />
									</rich:column>

									<rich:column sortBy="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.descricaoPeca}">
										<f:facet name="header">
											<h:outputText value="Nome da Peça" />
										</f:facet>
										<h:outputText
											value="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.descricaoPeca}" />
									</rich:column>

									<rich:column sortBy="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.tipoPecaProcesso.descricao}">
										<f:facet name="header">
											<h:outputText value="Situação da Peça" />
										</f:facet>
										<h:outputText value="#{beanVisibilidadePeca.situacaoPeca}" />
									</rich:column>

									<rich:column sortBy="#{wrappedPecaEletr.wrappedObject.pecaProcessoEletronico.dataInclusao}" sortOrder="ASCENDING">
										<f:facet name="header">
											<h:outputText value="Data de Inclusão da Peça" />
										</f:facet>
										<h:outputText value="#{beanVisibilidadePeca.dataInclusaoPeca}" />
									</rich:column>

									<rich:column>
										<f:facet name="header">
											<h:outputText value="Documento" />
										</f:facet>
										<h:outputLink
											value="#{beanVisibilidadePeca.documentoDownloadURL}">
											<h:graphicImage value="/images/pdf.png"></h:graphicImage>
										</h:outputLink>
									</rich:column>

									<rich:column>
										<f:facet name="header">
											<h:outputText value="Pend -> Pub" />
										</f:facet>
										<a4j:commandButton value="Pend -> Pub"
											styleClass="BotaoPadrao"
											style="#{beanVisibilidadePeca.estiloBotaoPendentePublicoTabela}"
											disabled="#{beanVisibilidadePeca.disabledBotaoPendentePublicoTabela}"
											actionListener="#{beanVisibilidadePeca.pendenteParaPublicoAction}"
											title="#{beanVisibilidadePeca.titleBotaoPendentePublicoTabela}"
											reRender="pnlCentral" />
									</rich:column>

									<rich:column>
										<f:facet name="header">
											<h:outputText value="Pub -> Pend" />
										</f:facet>
										<a4j:commandButton value="Pub -> Pend"
											styleClass="BotaoPadrao"
											style="#{beanVisibilidadePeca.estiloBotaoPublicoPendenteTabela}"
											disabled="#{beanVisibilidadePeca.disabledBotaoPublicoPendenteTabela}"
											actionListener="#{beanVisibilidadePeca.publicoParaPendenteAction}"
											title="#{beanVisibilidadePeca.titleBotaoPublicoPendenteTabela}"
											reRender="pnlCentral" />
									</rich:column>

								</rich:dataTable>
						
								<rich:datascroller id="dataScrollerPecas" for="tablePecas"
									fastControls="hide" maxPages="5" pageIndexVar="paginaAtual"
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
							</a4j:outputPanel>
						</h:panelGrid>

				</h:panelGrid>
			</h:panelGrid>
		</a4j:form>

		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>

	<script type="text/javascript">
		verificaOpcoesPesquisa();
	</script>

</f:view>