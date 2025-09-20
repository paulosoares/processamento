<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<script type="text/javascript">
    function pesquisar() {
        document.getElementById('botaoPesquisar').click();
    }

    function focoComboBox(campo) {
        document.getElementById(campo).focus();
    }
</script>

<f:view>
	<t:saveState value="#{beanComunicacaoExterna}"></t:saveState>

	<a4j:page pageTitle="::.. Principal ..::"
		onload="document.getElementById('orgaoIntimado').focus();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Pesquisar Intimação" />
			</jsp:include>
			<a name="ancora"></a>
		</h:form>
		<div class="PainelTituloCriaTexto">
			<span> Intimação Eletrônica - Pesquisa: </span>
		</div>
		<a4j:form id="form" prependId="false">
			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0"
					id="pnlCentral">
					<a4j:outputPanel id="painelPesquisaIntimacao"
						styleClass="MolduraInterna">

						<div style="padding-top: 8px;">
							<span class="Padrao">Processo: </span> <span> <h:inputText
									id="processo" size="28" style="margin-left: 53px"
									value="#{beanComunicacaoExterna.retornoProcesso}"
									onclick="if ( this.value!='' ) { #{rich:component('bProcesso')}.callSuggestion(true) }" />
								<rich:suggestionbox id="bProcesso" height="200" width="200"
									for="processo" fetchValue="#{oi.identificacao}"
									suggestionAction="#{beanComunicacaoExterna.pesquisarIncidentesPrincipal}"
									var="oi" nothingLabel="Nenhum registro encontrado">
									<h:column>
										<h:outputText rendered="#{oi.eletronico}" value="e"
											style="color: red; font-weight: bold;" />
										<h:outputText value="#{oi.identificacao}" />
									</h:column>
									<a4j:support ajaxSingle="true" event="onselect"
										oncomplete="pesquisarHandler('btnProcuraPecasVinculadasETagLivres');">
										<f:setPropertyActionListener value="#{oi.id}"
											target="#{beanComunicacaoExterna.idProcesso}" />
									</a4j:support>
								</rich:suggestionbox>
							</span>
						</div>

						<a4j:outputPanel ajaxRendered="true" keepTransient="false"
							id="pnlBuscaModelo">

							<div style="padding-top: 12px;">
								<span class="Padrao" style="margin-right: 10px;">
									Preferências: </span> <span> <h:selectOneMenu id="preferencias"
										style="width:529px; margin-left: 21px;"
										value="#{beanComunicacaoExterna.tipoIncidentePreferencia}">
										<f:selectItem itemLabel="Todos" />
										<f:selectItems
											value="#{beanComunicacaoExterna.incidentePreferencia}" />
									</h:selectOneMenu>
								</span>
							</div>

							<div style="padding-top: 12px;">
								<span class="Padrao" style="margin-right: 10px"> Parte
									Intimada: </span> <span> <h:selectOneMenu id="orgaoIntimado"
										style="width:534px; margin-left: 4px;"
										value="#{beanComunicacaoExterna.orgaoIntimado}">
										<f:selectItems
											value="#{beanComunicacaoExterna.listaOrgaoIntimado}" />
									</h:selectOneMenu>
								</span>
							</div>

							<div style="padding-top: 8px;">
								<span class="Padrao" style="margin-right: 34px">Situação:</span>
								<span> <h:selectOneMenu id="situacao"
										style="width:220px; margin-left: 20px;"
										value="#{beanComunicacaoExterna.tipoSituacao}">
										<f:selectItems value="#{beanComunicacaoExterna.listaSituacao}" />
									</h:selectOneMenu>
								</span>
							</div>

							<div style="padding-top: 8px;">
								<span class="Padrao" style="margin-right: 10px"> Tipo: </span> <span
									style="margin-left: 5px"> <h:selectOneMenu id="tipo2"
										value="#{beanComunicacaoExterna.descricaoTipoComunicacao}"
										style="margin-left: 4px; width:260px;">
										<f:selectItem itemLabel="Todos" />
										<f:selectItems
											value="#{beanComunicacaoExterna.descricoesTipoComunicacao}" />
										<a4j:support event="onchange" reRender="modelo" />
									</h:selectOneMenu>
								</span> <span class="Padrao" style="margin-left: 10px"> Modelo:
								</span> <span style="margin-left: 5px"> <h:selectOneMenu
										id="modelo"
										value="#{beanComunicacaoExterna.modeloComunicacaoEnum}"
										style="margin-left: 4px; width:260px;">
										<f:selectItem itemLabel="Todos" />
										<f:selectItems
											value="#{beanComunicacaoExterna.tiposComunicacao}" />
									</h:selectOneMenu>
								</span>
							</div>

							<div style="margin-top: 8px">
								<span class="Padrao" style="margin-right: 65px">Período
									(Dt de envio)</span>
								<rich:calendar id="dtInicio" datePattern="dd/MM/yyyy"
									locale="pt_Br" value="#{beanComunicacaoExterna.periodoInicio}">
								</rich:calendar>
								<span class="Padrao" style="margin-right: 5px"> à </span>
								<rich:calendar id="dtFim" datePattern="dd/MM/yyyy"
									locale="pt_Br" value="#{beanComunicacaoExterna.periodoFim}">
								</rich:calendar>
								<rich:hotKey selector="#dtInicio" key="return"
									handler="document.getElementById('btnPesquisa').onclick()">
								</rich:hotKey>
							</div>
						</a4j:outputPanel>

					</a4j:outputPanel>
				</h:panelGrid>
			</h:panelGrid>
			<rich:panel bodyClass="inpanelBody">
				<table>
					<tr>
						<td><a4j:commandButton id="btnPesquisa"
								styleClass="BotaoPesquisar" style="margin-left:15px;"
								value="Pesquisar" ignoreDupResponses="true"
								reRender="painelPesquisaIntimacao,pnlTableResultado"
								onclick="exibirMsgProcessando(true)"
								oncomplete="exibirMsgProcessando(false);"
								actionListener="#{beanComunicacaoExterna.pesquisar}" /></td>
						<td><a4j:commandButton id="btnLimpar"
								styleClass="BotaoPadrao" style="margin-left:15px;"
								value="Limpar" ignoreDupResponses="true"
								reRender="form,tableResultado"
								actionListener="#{beanComunicacaoExterna.novaPesquisa}" /></td>
						<td><h:commandButton id="idBtnCriarNovo"
								styleClass="BotaoPadrao" style="margin-left:15px;"
								value="Criar Nova" action="#{beanMenu.gerarIntimacaoEletronica}"
								actionListener="#{beanComunicacaoExterna.novaPesquisa}">
							</h:commandButton></td>
					</tr>
				</table>
			</rich:panel>

			<a4j:outputPanel id="pnlTableResultado">
				<rich:dataTable headerClass="" styleClass="" footerClass=""
					rowClasses=""></rich:dataTable>
				<h:dataTable headerClass="DataTableDefaultHeader"
					styleClass="DataTableDefault" footerClass="DataTableDefaultFooter"
					rowClasses="DataTableRow, DataTableRow2" style="height:45px;"
					value="#{beanComunicacaoExterna.listaComunicacaoExterna}"
					var="result"
					rendered="#{!empty beanComunicacaoExterna.listaComunicacaoExterna}"
					id="tableResultado"
					binding="#{beanComunicacaoExterna.dataTableListaComunicacao}">

					<h:column>
						<f:facet name="header">
							<h:outputText value="Tipo" />
						</f:facet>
						<h:outputText value="#{result.descricaoTipoComunicacao}" />
					</h:column>

					<h:column>
						<f:facet name="header">
							<h:outputText value="Modelo" />
						</f:facet>
						<h:outputText value="#{result.modelo}" />
					</h:column>

					<h:column>
						<f:facet name="header">
							<h:outputText value="Processo" />
						</f:facet>
						<h:outputText value="#{result.processo}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Preferências" />
						</f:facet>
						<h:dataTable value="#{result.tiposPreferencias}"
							var="preferencias">
							<h:column>
								<h:outputText value="#{preferencias.sigla}"
									title="#{preferencias.descricao}" />
							</h:column>
						</h:dataTable>


					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText value="Envio" />
						</f:facet>
						<h:outputText value="#{result.dataEnvio}">

							<f:convertDateTime pattern="dd/MM/yyyy - HH:mm" />
						</h:outputText>
					</h:column>

					<h:column>
						<f:facet name="header">
							<h:outputText value="Leitura" />
						</f:facet>
						<h:outputText value="#{result.dataRecebimento}">
							<f:convertDateTime pattern="dd/MM/yyyy - HH:mm" />
						</h:outputText>
					</h:column>

					<h:column>
						<f:facet name="header">
							<h:outputText value="Situação" />
						</f:facet>
						<h:outputText value="#{result.situacao}" />
					</h:column>

					<h:column>
						<f:facet name="header">
							<h:outputText value="Usuário que Leu a comunicação" />
						</f:facet>
						<h:outputText value="#{result.nomePessoaRecebeuComunicacao}" />
						<h:outputText value="(#{result.usuarioRecebeuComunicacao})" rendered="#{result.usuarioRecebeuComunicacao != ''}"/>
					</h:column>


					<h:column>
						<f:facet name="header">
							<h:outputText value="Parte Intimada" />
						</f:facet>
						<h:outputText value="#{result.orgaoIntimado}" />
					</h:column>

					<h:column>
						<f:facet name="header">
							<h:outputText value="Ações" />
						</f:facet>
						<a4j:commandLink
							rendered="#{result.situacao == 'Não Lida' and result.situacao != 'Cancelada'}"
							oncomplete="Richfaces.showModalPanel('modalCancelaIntimacao');">

							<f:setPropertyActionListener value="#{result.idComunicacao}"
								target="#{beanComunicacaoExterna.idComunicacao}" />

							<f:setPropertyActionListener
								value="#{beanComunicacaoExterna.dataTableListaComunicacao.rowIndex}"
								target="#{beanComunicacaoExterna.rowIndexDataTableResultado}" />

							<h:graphicImage url="../../images/close.gif" title="Cancelar" />
						</a4j:commandLink>

					</h:column>

				</h:dataTable>


			</a4j:outputPanel>
		</a4j:form>
		<rich:modalPanel id="modalCancelaIntimacao" width="200" height="70"
			keepVisualState="true">
			<f:facet name="header">
				<h:outputText value="Deseja cancelar a intimação?" />
			</f:facet>
			<a4j:form prependId="false">
				<div style="padding-top: 15px;">
					<span class="Padrao"> <a4j:commandButton
							oncomplete="Richfaces.hideModalPanel('modalCancelaIntimacao');"
							value="Sim"
							actionListener="#{beanComunicacaoExterna.cancelarIntimacao}"
							reRender="tableResultado" /> <a4j:commandButton
							onclick="Richfaces.hideModalPanel('modalCancelaIntimacao');"
							value="Não" />
					</span>
				</div>

			</a4j:form>
		</rich:modalPanel>
		<jsp:include page="/pages/template/footer.jsp" flush="true" />
	</a4j:page>
</f:view>