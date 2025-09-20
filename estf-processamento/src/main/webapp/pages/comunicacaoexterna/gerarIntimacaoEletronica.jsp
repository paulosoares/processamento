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
        if (document.getElementById('tipo').value == '') {
            alert('Por favor informe o "Tipo".');
            return false;
        } else if (document.getElementById('selecionar').value == '') {
            alert('Por favor selecione uma parte".');
            return false;
        }
        return true;
    }

</script>

<f:view>

	<t:saveState value="#{beanComunicacaoExterna}"></t:saveState>

	<a4j:page pageTitle="::.. Principal ..::"
		onload="document.getElementById('orgaoIntimado').focus();" id="pagina">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Gerar Intimação Eletrônica" />
			</jsp:include>
			<a name="ancora"></a>
		</h:form>

		<a4j:form id="form" prependId="false">

			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0"
					id="pnlCentral">
					<a4j:outputPanel id="painelPesquisaIntimacao"
						styleClass="MolduraInterna">
						
						<div style="padding-top: 8px;">
							<span class="Padrao" style="margin-left: 10px">Processo	Eletrônico: </span> 
							<span> 
								<h:inputText id="processo" size="28"
									style="margin-left: 63px; width:260px;"
									value="#{beanComunicacaoExterna.retornoProcesso}"
									onclick="if ( this.value!='' ) { #{rich:component('bProcesso')}.callSuggestion(true) }" />
								<rich:suggestionbox id="bProcesso" height="200" width="200"
									for="processo" fetchValue="#{oi.identificacao}"
									suggestionAction="#{beanComunicacaoExterna.pesquisarIncidentesPrincipal}"
									var="oi" nothingLabel="Nenhum registro encontrado">
									<h:column>										
										<h:outputText rendered="#{oi.eletronico}" value="e" style="color: red; font-weight: bold;" />	
										<h:outputText value="#{oi.identificacao}" />
									</h:column>
									<a4j:support ajaxSingle="true" event="onselect"
										reRender="dataTablePartes"
										oncomplete="pesquisarHandler('btnProcuraPecasVinculadasETagLivres');">
										<f:setPropertyActionListener value="#{oi.id}"
											target="#{beanComunicacaoExterna.idProcesso}" />
									</a4j:support>
								</rich:suggestionbox>
							</span>
						</div>

						<a4j:outputPanel ajaxRendered="true" keepTransient="false"
							id="pnlBuscaModelo">




						</a4j:outputPanel>

					</a4j:outputPanel>
				</h:panelGrid>
			</h:panelGrid>

			<rich:panel id="dataTablePartes">
				<h:inputHidden value="#{beanComunicacaoExterna.parteSelecionada}"
					id="selecionar"></h:inputHidden>
				<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
					cellspacing="0"
					rendered="#{beanComunicacaoExterna.idProcesso ne null}">
					<div class="PainelTituloCriaTexto">
						<span> Intimação Eletrônica - Criar Nova: </span>
					</div>

					<div style="padding-top: 8px;">
						<span class="Padrao" style="margin-left: 5px">Tipo * </span>
					</div>
					<h:selectOneMenu id="tipo"
						value="#{beanComunicacaoExterna.modeloComunicacaoEnum}"
						style="margin-left: 4px; width:260px;">
						<f:selectItem itemLabel="Selecione" />
						<f:selectItems value="#{beanComunicacaoExterna.tiposComunicacoesGeracao}" />
					</h:selectOneMenu>

					<div class="PainelTituloCriaTexto"
						style="margin-bottom: 10px; margin-top: 20px;">
						<span> Partes intimáveis </span>
					</div>
					<h:dataTable headerClass="DataTableDefaultHeader" id="tabelaPartes"
						styleClass="DataTableDefault" footerClass="DataTableDefaultFooter"
						rowClasses="DataTableRow, DataTableRow2"
						columnClasses="dois, cinco, vintecinco, cinco, cinco, cinco, cinco"
						value="#{beanComunicacaoExterna.listaPartesDTO}" var="result">


						<h:column id="columnCheckPartes">
							<f:facet name="header">
								<h:selectBooleanCheckbox
									value="#{beanComunicacaoExterna.selecionarTodasPartes}">
									<a4j:support event="onclick"
										actionListener="#{beanComunicacaoExterna.marcarDesmarcarPartes}"
										reRender="tabelaPartes, selecionar" />
								</h:selectBooleanCheckbox>
							</f:facet>
							<h:selectBooleanCheckbox value="#{result.checked}">
								<a4j:support event="onclick"
									actionListener="#{beanComunicacaoExterna.varificarSeParteFoiSelecionada}"
									reRender="selecionar" />
							</h:selectBooleanCheckbox>
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Qualificação" />
							</f:facet>
							<h:outputText value="#{result.qualificacao}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Nome" />
							</f:facet>
							<h:outputText value="#{result.nomeJurisdicionado}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Tipo" />
							</f:facet>
							<h:outputText value="#{result.tipoPessoa}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Código" />
							</f:facet>
							<h:outputText value="#{result.seqJurisdicionado}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Ratificada" />
							</f:facet>
							<h:outputText value="#{result.cadastroRatificado}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Prerrogativa de Intimação" />
							</f:facet>
							<h:outputText value="#{result.prerrogativaIntimacao}" />
						</h:column>


					</h:dataTable>

					<div class="PainelTituloCriaTexto"
						style="margin-bottom: 10px; margin-top: 10px;">
						<span> Partes não intimáveis </span>
					</div>
					<h:dataTable headerClass="DataTableDefaultHeader"
						styleClass="DataTableDefault" footerClass="DataTableDefaultFooter"
						rowClasses="DataTableRow, DataTableRow2"
						columnClasses="cinco, vintesete, cinco, cinco, cinco, cinco"
						value="#{beanComunicacaoExterna.listaPartesDTONaoIntimaveis}"
						var="result">



						<h:column>
							<f:facet name="header">
								<h:outputText value="Qualificação" />
							</f:facet>
							<h:outputText value="#{result.qualificacao}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Nome" />
							</f:facet>
							<h:outputText value="#{result.nomeJurisdicionado}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Tipo" />
							</f:facet>
							<h:outputText value="#{result.tipoPessoa}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Código" />
							</f:facet>
							<h:outputText value="#{result.seqJurisdicionado}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Ratificada" />
							</f:facet>
							<h:outputText value="#{result.cadastroRatificado}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Prerrogativa de Intimação" />
							</f:facet>
							<h:outputText value="#{result.prerrogativaIntimacao}" />
						</h:column>


					</h:dataTable>


					<div class="PainelTituloCriaTexto"
						style="margin-bottom: 10px; margin-top: 20px;">
						<span> Peças: </span>
					</div>
					<h:dataTable headerClass="DataTableDefaultHeader" id="tabelaPecas"
						styleClass="DataTableDefault" footerClass="DataTableDefaultFooter"
						rowClasses="DataTableRow, DataTableRow2"
						columnClasses="dois, cinco, vintecinco, cinco, cinco, cinco, cinco"
						value="#{beanComunicacaoExterna.listaPecasDTO}" var="result">


						<h:column id="columnCheckPecas">
							<f:facet name="header">
								<h:selectBooleanCheckbox
									value="#{beanComunicacaoExterna.selecionarTodasPecas}">
									<a4j:support event="onclick"
										actionListener="#{beanComunicacaoExterna.marcarDesmarcarPecas}"
										reRender="tabelaPecas" />
								</h:selectBooleanCheckbox>
							</f:facet>
							<h:selectBooleanCheckbox value="#{result.checked}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Nº da peça" />
							</f:facet>
							<h:outputText value="#{result.numeroOrdemPeca }" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Nome" />
							</f:facet>
							<h:outputText value="#{result.descricaoPeca }" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Tipo" />
							</f:facet>
							<h:outputText value="#{result.tipoPecaProcesso.descricao}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Dt de Inclusão" />
							</f:facet>
							<h:outputText value="#{result.dataInclusao}" >
								<f:convertDateTime  locale="pt_BR" type="date" pattern="dd/MM/yyyy"/>
							</h:outputText>
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Situação" />
							</f:facet>
							<h:outputText value="#{result.tipoSituacaoPeca.descricao }" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Documento" />
							</f:facet>
							<h:outputLink value="#{result.urlDownloadPeca}"
								rendered="#{result.urlDownloadPeca != null}">
								<h:graphicImage value="/images/pdf.png"></h:graphicImage>
							</h:outputLink>
						</h:column>
					</h:dataTable>

					<div class="PainelTituloCriaTexto"
						style="margin-bottom: 10px; margin-top: 20px;">
						<span> Andamentos: </span>
					</div>
					<h:dataTable headerClass="DataTableDefaultHeader"
						id="tabelaAndamentos" styleClass="DataTableDefault"
						footerClass="DataTableDefaultFooter"
						rowClasses="DataTableRow, DataTableRow2"
						columnClasses="dois, tres, tres, sete , dez , dez , dez"
						value="#{beanComunicacaoExterna.listaAndamentoDto}" var="result">


						<h:column id="columnCheckPecas">
							<f:facet name="header">
								<h:selectBooleanCheckbox
									value="#{beanComunicacaoExterna.selecionarTodosAndamentos}">
									<a4j:support event="onclick"
										actionListener="#{beanComunicacaoExterna.marcarDesmarcarAndamentos}"
										reRender="tabelaAndamentos" />
								</h:selectBooleanCheckbox>
							</f:facet>
							<h:selectBooleanCheckbox value="#{result.checked}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Data" />
							</f:facet>
							<h:outputText value="#{result.dataAndamento}" >
								<f:convertDateTime  locale="pt_BR" type="date" pattern="dd/MM/yyyy - HH:mm"/>
							</h:outputText>
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Cód." />
							</f:facet>
							<h:outputText value="#{result.codigoAndamento}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Descrição do Andamento" />
							</f:facet>
							<h:outputText value="#{result.tipoAndamento.descricao}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Órgão julgador" />
							</f:facet>
							<h:outputText value="#{result.origemAndamentoDecisao.descricao}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Observação" />
							</f:facet>
							<h:outputText value="#{result.descricaoObservacaoAndamento}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText value="Observação Interna" />
							</f:facet>
							<h:outputText value="#{result.descricaoObservacaoInterna}" />
						</h:column>

					</h:dataTable>



					<br />
					<rich:panel bodyClass="inpanelBody">
						<table>
							<tr>
								<td><a4j:commandButton id="btnEnviarComunicacao"
										rendered="#{beanComunicacaoExterna.statusBotao != false}"
										styleClass="BotaoPadraoEstendido" value="Enviar Comunicação"
										ignoreDupResponses="true" reRender="form"
										onclick="if(!validarCampos()){return;}else{exibirMsgProcessando(true);}"
										oncomplete="exibirMsgProcessando(false);"
										actionListener="#{beanComunicacaoExterna.enviarComunicacao}" /></td>
								<td><a4j:commandButton id="btnLimpar"
										styleClass="BotaoPadrao" style="margin-left:15px;"
										value="Limpar" ignoreDupResponses="true" reRender="form"
										onclick="exibirMsgProcessando(true)"
										oncomplete="exibirMsgProcessando(false);"
										actionListener="#{beanComunicacaoExterna.novaPesquisa}" /></td>
							</tr>
						</table>
					</rich:panel>


				</h:panelGrid>
			</rich:panel>
		</a4j:form>

		<jsp:include page="/pages/template/footer.jsp" flush="true" />
	</a4j:page>
</f:view>