<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://stf" prefix="expedicao"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<style>
.tooltip {
	background-color: grey;
	border-width: 0px;
}

.tooltip-text {
	cursor: arrow;
	border-width: 0px;
	text-align: left;
	display: table-cell;
	vertical-align: middle;
}

.tooltipData {
	font-weight: bold;
}
</style>
<f:view>
	<a4j:page pageTitle="::.. Principal ..::">

		<h:form id="form" prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Pesquisar Lista de Remessa" />
			</jsp:include>

			<h:panelGrid styleClass="MolduraExterna" cellpadding="0" cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">
					<a4j:outputPanel id="pnlPrincipalPesquisa" ajaxRendered="true">
						<h:panelGrid id="pnlFiltro" styleClass="PadraoTituloPanel" cellpadding="0" cellspacing="0">
							<h:outputText value="Pesquisar Lista de Remessa" />
						</h:panelGrid>
					</a4j:outputPanel>
				</h:panelGrid>
			</h:panelGrid>

			<h:panelGrid id="painelPesquisaListaRemessa" columns="1">
				<rich:panel bodyClass="inpanelBody" id="painelCamposPesquisa">
					<f:facet name="header">
						<b>Pesquisa:</b>
					</f:facet>
					<div>
						<table>
							<tr>
								<td><h:outputText value="Número/Ano da Lista" /></td>
								<td><t:inputText id="idNumeroLista"
													size="20"
													value="#{beanListaRemessa.numeroAnoListaRemessa}"/>
									<rich:hotKey selector="#idNumeroLista"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								
								</td>
							</tr>
							<tr>
								<td><h:outputText value="Documento" /></td>
								<td><t:inputText id="idDocumento"
													size="60"
													value="#{beanListaRemessa.pesquisaListaRemessaDto.documento}" />
									<rich:hotKey selector="#idDocumento"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								</td>
							</tr>
							<tr>
								<td><h:outputText value="Vínculo" /></td>
								<td><t:inputText id="idVinculo"
													size="60"
													value="#{beanListaRemessa.pesquisaListaRemessaDto.vinculo}" />
									<rich:hotKey selector="#idVinculo"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
							</td>
							</tr>
							<tr>
								<td><h:outputText value="Lista(s) de Remessa vinculada(s)" /></td>
								<td><t:inputText id="idListaRemessaListaRemessa"
													size="60"
													value="#{beanListaRemessa.pesquisaListaRemessaDto.remessasListaRemessa}" />
									<rich:hotKey selector="#idListaRemessaListaRemessa"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								</td>
							</tr>								
							<tr>
								<td><h:outputText value="Guia de Deslocamento" /></td>
								<td><t:inputText id="idGuiaDeslocamento"
													size="60"
													value="#{beanListaRemessa.pesquisaListaRemessaDto.guiaDeslocamento}" />
									<rich:hotKey selector="#idGuiaDeslocamento"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								</td>
							</tr>
							<tr>
								<td><h:outputText value="Observação" /></td>
								<td><t:inputText id="idObservacao"
													size="60"
													value="#{beanListaRemessa.pesquisaListaRemessaDto.observacao}" />
									<rich:hotKey selector="#idObservacao"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								</td>
							</tr>

							<tr>
								<td><h:outputText value="Data de Criação" /></td>
								<td><rich:calendar id="idDtCriacaoInicial"
													datePattern="dd/MM/yyyy"
													locale="pt_Br"
													value="#{beanListaRemessa.pesquisaListaRemessaDto.dataCriacaoInicio}" />
									<h:outputLabel styleClass="Padrao" value=" e " />
									<rich:calendar id="idDtCriacaoFinal"
													datePattern="dd/MM/yyyy"
													locale="pt_Br"
													value="#{beanListaRemessa.pesquisaListaRemessaDto.dataCriacaoFim}" />
									<rich:hotKey selector="#idDtCriacaoInicial"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								</td>
							</tr>

							<tr>
								<td><h:outputText value="Data de Remessa" /></td>
								<td><rich:calendar id="idDtPostagemInicial"
													datePattern="dd/MM/yyyy"
													locale="pt_Br"
													value="#{beanListaRemessa.pesquisaListaRemessaDto.dataEnvioInicio}" />
									<h:outputLabel
										styleClass="Padrao" value=" e " />
									<rich:calendar id="idDtPostagemFinal"
													datePattern="dd/MM/yyyy"
													locale="pt_Br"
													value="#{beanListaRemessa.pesquisaListaRemessaDto.dataEnvioFim}" />
									<rich:hotKey selector="#idDtPostagemInicial"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								</td>
							</tr>					
							<tr>
								<td><h:outputText value="Destinatário" /></td>
								<td><t:inputText id="idDestinatario"
													size="60"
													value="#{beanListaRemessa.pesquisaListaRemessaDto.destinatario}" />
									<rich:hotKey selector="#idDestinatario"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								</td>
							</tr>
							<tr>
								<td><h:outputText value="Número do malote" /></td>
								<td>
									<t:inputText size="30" id="idNumMalote" value="#{beanListaRemessa.pesquisaListaRemessaDto.malote}" />
									<h:outputLabel styleClass="Padrao" value="       " />
									<h:outputText value="Número do Lacre" />
									<h:outputLabel styleClass="Padrao" value=" " />
									<t:inputText id="idNumLacre" size="30" value="#{beanListaRemessa.pesquisaListaRemessaDto.lacre}" />
									<rich:hotKey selector="#idNumMalote"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								</td>
							</tr>
						</table>
					</div>
				</rich:panel>

				<rich:panel bodyClass="inpanelBody">
					<table>
						<tr>
							<td>								
								<a4j:commandButton id="btnPesquisa"
													styleClass="BotaoPesquisar"
													style="margin-left:15px;"
													value="Pesquisar"
													ignoreDupResponses="true"
													reRender="pnlResultadoPesquisa"
													onclick="exibirMsgProcessando(true)"
													oncomplete="exibirMsgProcessando(false);"
													actionListener="#{beanListaRemessa.pesquisarRemessas}" />
							</td>
							<td>
								<a4j:commandButton id="btnLimpar"
													styleClass="BotaoPadrao"
													style="margin-left:15px;"
													value="Limpar"
													ignoreDupResponses="true"
													reRender="painelCamposPesquisa,pnlResultadoPesquisa"
													actionListener="#{beanListaRemessa.limparCampos}" />
							</td>
							<td>
								<h:commandButton id="idBtnCriarNovo"
													styleClass="BotaoPadrao"
													style="margin-left:15px;"
													value="Criar Nova"
													actionListener="#{beanListaRemessa.novo}"
													action="#{beanMenu.remessaCadastro}">
									<f:setPropertyActionListener value="" target="#{beanDestinatario.campoPesquisa}" />
								</h:commandButton>
							</td>
						</tr>
					</table>
				</rich:panel>
			</h:panelGrid>

			<a4j:outputPanel id="pnlResultadoPesquisa" ajaxRendered="true">
				<c:if test="${not empty beanListaRemessa.remessas}">
					<h:commandButton id="idBtnExportarExcel"
										styleClass="GerarXLS"
										style="float: right; margin-right: 20px; width: 260px;"
										value="EXPORTAR PLANILHA PARA O EXCEL"
										actionListener="#{beanListaRemessa.relatorioExcel}" />
					<br>
					<br>
					<h:panelGrid id="pnlResultadoTotal" styleClass="PadraoTituloPanel">
						<h:outputText value="Resultado da Pesquisa - Quant. de Registros: #{beanListaRemessa.qtdListas} lista(s), #{beanListaRemessa.qtdRegistros} remessa(s) e #{beanListaRemessa.qtdVolumes} volume(s)" />
					</h:panelGrid>
					<rich:datascroller id="sc1"
										align="left"
										for="tbPesquisaListaRemessa"
										maxPages="100"
										reRender="sc2" />
					<rich:dataTable id="tbPesquisaListaRemessa"
									var="pesquisa"
									columnClasses="center"
									rowClasses="linha-par, linha-impar"
									rows="50"								
									value="#{beanListaRemessa.consultaRemessasPorVolume}"
									binding="#{beanListaRemessa.remessaAManipular}">
						<rich:column sortBy="#{pesquisa.remessa.listaRemessa.numeroListaRemessa}" style="text-align: center; width: 10px;">
							<f:facet name="header">
								<h:outputText value="Nº/Ano Lista" />
							</f:facet>							
								<h:outputText value="#{pesquisa.remessa.listaRemessa.numeroListaRemessa}/#{pesquisa.remessa.listaRemessa.anoListaRemessa}" />								
								<rich:toolTip style="width: 500px; text-align: left;">
									<expedicao:hint value="#{pesquisa}"/>
								</rich:toolTip>
						</rich:column>
						<rich:column sortBy="#{pesquisa.volume.numeroEtiquetaCorreios}" style="text-align: center; width: 15px;">
							<f:facet name="header">
								<h:outputText value="Cód. Rastreio"/>
							</f:facet>
							<h:outputText value="#{pesquisa.volume.numeroEtiquetaCorreios}" />
							<rich:toolTip style="width: 500px; text-align: left;">
								<expedicao:hint value="#{pesquisa}"/>
							</rich:toolTip>
						</rich:column>
						<rich:column sortBy="#{pesquisa.volumeFormatado}" style="text-align: center; width: 10px;">
							<f:facet name="header">
								<h:outputText value="Volume"/>
							</f:facet>
							<h:outputText value="#{pesquisa.volumeFormatado}" />
							<rich:toolTip style="width: 500px; text-align: left;">
								<expedicao:hint value="#{pesquisa}"/>
							</rich:toolTip>	
						</rich:column>
						<rich:column sortBy="#{pesquisa.remessa.listaRemessa.dataCriacao.time}" style="text-align: center; width: 15px;">
							<f:facet name="header">
								<h:outputText value="Dt Criação" />
							</f:facet>							
								<h:outputText value="#{pesquisa.remessa.listaRemessa.dataCriacao.time}">
									<f:convertDateTime pattern="dd/MM/yyyy" />
								</h:outputText>
								<rich:toolTip style="width: 500px; text-align: left;">
									<expedicao:hint value="#{pesquisa}"/>
								</rich:toolTip>
						</rich:column>
						<rich:column sortBy="#{pesquisa.remessa.listaRemessa.dataEnvio.time}" style="text-align: center; width: 15px;">
							<f:facet name="header">
								<h:outputText value="Dt. Remessa" />
							</f:facet>							
							<h:outputText value="#{pesquisa.remessa.listaRemessa.dataEnvio.time}">
								<f:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>
							<rich:toolTip style="width: 500px; text-align: left;">
								<expedicao:hint value="#{pesquisa}"/>
							</rich:toolTip>
						</rich:column>
						<rich:column sortBy="#{pesquisa.remessa.listaRemessa.tipoServico.nome}" style="text-align: center; width: 20px;">
							<f:facet name="header">
								<h:outputText value="Tipo/Serviço" />
							</f:facet>							
							<h:outputText value="#{pesquisa.remessa.listaRemessa.descricaoTipoEntrega} - #{pesquisa.remessa.listaRemessa.tipoServico.nome}" />
							<rich:toolTip style="width: 500px; text-align: left;">
								<expedicao:hint value="#{pesquisa}"/>
							</rich:toolTip>
						</rich:column>
						<rich:column sortBy="#{pesquisa.remessa.descricaoAnterior}" width="50px">
							<f:facet name="header">
								<h:outputText value="Detalhe Pré" />
							</f:facet>							
								<h:outputText value="#{pesquisa.remessa.descricaoAnterior}" />
								<rich:toolTip style="width: 500px; text-align: left;">
									<expedicao:hint value="#{pesquisa}"/>
								</rich:toolTip>
						</rich:column>
						<rich:column sortBy="#{pesquisa.remessa.descricaoPrincipal}" width="200px">
							<f:facet name="header">
								<h:outputText value="Destinatário" />
							</f:facet>							
								<h:outputText value="#{pesquisa.remessa.descricaoPrincipal}" />
								<rich:toolTip style="width: 500px; text-align: left;">
									<expedicao:hint value="#{pesquisa}"/>
								</rich:toolTip>
						</rich:column>
						<rich:column sortBy="#{pesquisa.remessa.descricaoPosterior}" width="50px">
							<f:facet name="header">
								<h:outputText value="Detalhe Pós" />
							</f:facet>							
								<h:outputText value="#{pesquisa.remessa.descricaoPosterior}" />
								<rich:toolTip style="width: 500px; text-align: left;">
									<expedicao:hint value="#{pesquisa}"/>
								</rich:toolTip>
						</rich:column>
						<rich:column sortBy="#{pesquisa.remessa.numeroComunicacao}" width="50px">
							<f:facet name="header">
								<h:outputText value="Documento" />
							</f:facet>							
								<h:outputText value="#{pesquisa.remessa.tipoComunicacao.descricao}  #{pesquisa.remessa.numeroComunicacao}" />
								<rich:toolTip style="width: 500px; text-align: left;">
									<expedicao:hint value="#{pesquisa}"/>
								</rich:toolTip>
						</rich:column>
						<rich:column sortBy="#{pesquisa.remessa.vinculo}" width="70px">
							<f:facet name="header">
								<h:outputText value="Vínculo" />
							</f:facet>							
								<h:outputText value="#{pesquisa.remessa.vinculo}" />
								<rich:toolTip style="width: 500px; text-align: left;">
									<expedicao:hint value="#{pesquisa}"/>
								</rich:toolTip>
						</rich:column>
						
						<rich:column>
							<f:facet name="header">
								<h:outputText value="Lista(s) de Remessa" />
							</f:facet>
							<a4j:repeat value="#{pesquisa.remessa.listasEnviadas}" var="lista" >
								<h:outputLabel value="#{lista.numeroListaRemessaAnoFormato}"/>
							</a4j:repeat>
						</rich:column>												
						
						<rich:column sortBy="#{pesquisa.remessa.guiaDeslocamento}" width="50px">
							<f:facet name="header">
								<h:outputText value="Guia Deslocamento" />
							</f:facet>							
								<h:outputText value="#{pesquisa.remessa.guiaDeslocamento}" />
								<rich:toolTip style="width: 500px; text-align: left;">
									<expedicao:hint value="#{pesquisa}"/>
								</rich:toolTip>
						</rich:column>
						<rich:column sortBy="#{pesquisa.remessa.observacao}" width="50px">
							<f:facet name="header">
								<h:outputText value="Obs." />
							</f:facet>							
								<h:outputText value="#{pesquisa.remessa.observacao}" />
								<rich:toolTip style="width: 500px; text-align: left;">
									<expedicao:hint value="#{pesquisa}"/>
								</rich:toolTip>
						</rich:column>
						<rich:column style="text-align: center;" width="5px">
							<f:facet name="header">
								<h:outputText value="Visualizar" />
							</f:facet>
							<h:commandLink id="linkVisualizar"
											action="visualizarRemessa"
											actionListener="#{beanVisualizarListaRemessa.visualizarListaRemessa}">
								<h:graphicImage value="/images/pesquisar.jpg" title="Visualizar" />
								<f:setPropertyActionListener value="#{beanListaRemessa}" target="#{beanVisualizarListaRemessa.pesquisaListaRemessa}" />
							</h:commandLink>
						</rich:column>
						<rich:column style="text-align: center;" width="5px">
							<f:facet name="header">
								<h:outputText value="Ação" />
							</f:facet>
							<a4j:commandLink id="idBotaoModalExcluirRemessa"
												reRender="idPanelExcluir"
												oncomplete="Richfaces.showModalPanel('idPanelExcluir')"
												rendered="#{not pesquisa.remessa.listaRemessa.finalizada}">
								<h:graphicImage url="/images/deletecell.png" title="Excluir" />
								<f:setPropertyActionListener value="#{pesquisa.remessa}" target="#{beanListaRemessa.remessa}" />
							</a4j:commandLink>
							<a4j:commandLink id="idBotaoModalReabrirRemessa"
												reRender="idPanelReabrir"
												oncomplete="Richfaces.showModalPanel('idPanelReabrir')"
												rendered="#{pesquisa.remessa.listaRemessa.finalizada}">
								<h:graphicImage url="/images/back.png" title="Reabrir" />
								<f:setPropertyActionListener value="#{pesquisa.remessa}" target="#{beanListaRemessa.remessa}" />
							</a4j:commandLink>							
						</rich:column>
						<f:facet name="footer">
							<rich:datascroller id="sc2"
												align="left"
												for="tbPesquisaListaRemessa"
												maxPages="100"
												reRender="sc1" />
						</f:facet>
					</rich:dataTable>
				</c:if>
			</a4j:outputPanel>
			<jsp:include page="/pages/template/footer.jsp" flush="true" />
		</h:form>

		<rich:modalPanel id="idPanelExcluir"
							width="350"
							height="100"
							keepVisualState="true"
							style="overflow:auto; position: fixed; top: 50; left: 50;"
							autosized="true">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="Confirmar exclusão" />
				</h:panelGroup>
			</f:facet>
			<f:facet name="controls">
				<h:panelGroup>
					<h:graphicImage id="hidelink" value="/images/error.gif" styleClass="hidelink" />
					<rich:componentControl for="panelExcluir" attachTo="hidelink" operation="hide" event="onclick" />
				</h:panelGroup>
			</f:facet>
			<h:form>
				<h:outputText value="Deseja realizar a exclusão da Remessa?" />
				<div style="margin-top: 20px; margin-left: 100px;">
					<a4j:commandButton id="btnSim"
										styleClass="Excluir"
										value="  Sim"
										reRender="pnlResultadoPesquisa"
										oncomplete="Richfaces.hideModalPanel('idPanelExcluir')"
										actionListener="#{beanListaRemessa.excluirRemessaDaListaDeRemessas}" />
					<a4j:commandButton id="btnNao"
										styleClass="Cancelar"
										value=" Não"
										reRender="tbPesquisaDestinatario"
										oncomplete="Richfaces.hideModalPanel('idPanelExcluir')" />
				</div>
			</h:form>
		</rich:modalPanel>
		
		<rich:modalPanel id="idPanelReabrir"
							width="350"
							height="100"
							keepVisualState="true"
							style="overflow:auto; position: fixed; top: 50; left: 50;"
							autosized="true">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="Confirmar reabertura" />
				</h:panelGroup>
			</f:facet>
			<f:facet name="controls">
				<h:panelGroup>
					<h:graphicImage id="hidelink2" value="/images/error.gif" styleClass="hidelink" />
					<rich:componentControl for="panelReabrir" attachTo="hidelink2" operation="hide" event="onclick" />
				</h:panelGroup>
			</f:facet>
			<h:form>
				<h:outputText value="Deseja realizar a reabertura da Remessa?" />
				<div style="margin-top: 20px; margin-left: 100px;">
					<a4j:commandButton id="btnSim2"
										styleClass="Excluir"
										value="  Sim"
										reRender="pnlResultadoPesquisa"
										oncomplete="Richfaces.hideModalPanel('idPanelReabrir')"
										actionListener="#{beanListaRemessa.reabrirRemessaDaListaDeRemessas}" />
					<a4j:commandButton id="btnNao2"
										styleClass="Cancelar"
										value=" Não"
										reRender="tbPesquisaDestinatario"
										oncomplete="Richfaces.hideModalPanel('idPanelReabrir')" />
				</div>
			</h:form>
		</rich:modalPanel>		
	</a4j:page>
</f:view>