<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<f:view>
	<a4j:page pageTitle="::.. Principal ..::">

		<h:form id="form" prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Pesquisar Lista de Remessa" />
			</jsp:include>

			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">
					<a4j:outputPanel id="pnlPrincipalPesquisa" ajaxRendered="true">
						<h:panelGrid styleClass="PadraoTituloPanel" id="pnlFiltro"
							cellpadding="0" cellspacing="0">
							<h:outputText value="Pesquisar Lista de Remessa" />
						</h:panelGrid>
					</a4j:outputPanel>
				</h:panelGrid>
			</h:panelGrid>

			<h:panelGrid columns="1" id="painelPesquisaListaRemessa">
				<rich:panel bodyClass="inpanelBody" id="painelCamposPesquisa">
					<f:facet name="header">
						<b>Pesquisa:</b>
					</f:facet>
					<ul>
						<table>
							<tr>
								<td><h:outputText value="Número/Ano da Lista" /></td>
								<td><t:inputText size="20" id="idNumeroLista"
										value="#{beanListaRemessa.numeroAnoListaRemessa}" />
									<rich:hotKey selector="#idNumeroLista"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								</td>
							</tr>
							<tr>
								<td><h:outputText value="Documento" /></td>
								<td><t:inputText size="60" id="idDocumento"
										value="#{beanListaRemessa.pesquisaListaRemessaDto.documento}" />
									<rich:hotKey selector="#idDocumento"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />		
								</td>
							</tr>
							<tr>
								<td><h:outputText value="Vínculo" /></td>
								<td><t:inputText size="60" id="idVinculo"
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
								<td><t:inputText size="60" id="idGuiaDeslocamento"
										value="#{beanListaRemessa.pesquisaListaRemessaDto.guiaDeslocamento}" />
									<rich:hotKey selector="#idGuiaDeslocamento"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								</td>
							</tr>

							<tr>
								<td><h:outputText value="Cód. Rastreio" /></td>
								<td><t:inputText size="60" id="idCodRastreio"
										value="#{beanListaRemessa.pesquisaListaRemessaDto.codigoRastreio}" />
									<rich:hotKey selector="#idCodRastreio"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								</td>
							</tr>

							<tr>
								<td><h:outputText value="Observação" /></td>
								<td><t:inputText size="60" id="idObservacao"
										value="#{beanListaRemessa.pesquisaListaRemessaDto.observacao}" />
									<rich:hotKey selector="#idObservacao"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								</td>
							</tr>

							<tr>
								<td><h:outputText value="Data de Criação" /></td>
								<td><rich:calendar id="idDtCriacaoInicial"
										value="#{beanListaRemessa.pesquisaListaRemessaDto.dataCriacaoInicio}"
										datePattern="dd/MM/yyyy" locale="pt_Br" /> <h:outputLabel
										styleClass="Padrao" value=" e "></h:outputLabel> 
									<rich:calendar
										id="idDtCriacaoFinal"
										value="#{beanListaRemessa.pesquisaListaRemessaDto.dataCriacaoFim}"
										datePattern="dd/MM/yyyy" locale="pt_Br" />
									<rich:hotKey selector="#idDtCriacaoInicial"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
									<rich:hotKey selector="#idDtCriacaoFinal"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								</td>
							</tr>

							<tr>
								<td><h:outputText value="Data de Remessa" /></td>
								<td><rich:calendar id="idDtPostagemInicial"
										value="#{beanListaRemessa.pesquisaListaRemessaDto.dataEnvioInicio}"
										datePattern="dd/MM/yyyy" locale="pt_Br" /> <h:outputLabel
										styleClass="Padrao" value=" e "></h:outputLabel> 
									<rich:calendar
										id="idDtPostagemFinal"
										value="#{beanListaRemessa.pesquisaListaRemessaDto.dataEnvioFim}"
										datePattern="dd/MM/yyyy" locale="pt_Br" />
									<rich:hotKey selector="#idDtPostagemInicial"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
									<rich:hotKey selector="#idDtPostagemFinal"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								</td>
							</tr>
							<tr>
								<td><h:outputText value="Destinatário" /></td>
								<td><t:inputText size="60" id="idDestinatario"
										value="#{beanListaRemessa.pesquisaListaRemessaDto.destinatario}" />
									<rich:hotKey selector="#idDestinatario"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								</td>
							</tr>
							<tr>
								<td><h:outputText value="Número do malote" /></td>
								<td><t:inputText size="30" id="idNumMalote"
										value="#{beanListaRemessa.pesquisaListaRemessaDto.malote}" />
									<h:outputLabel styleClass="Padrao" value="       "></h:outputLabel>
									<h:outputText value="Número do Lacre" /> <h:outputLabel
										styleClass="Padrao" value=" "></h:outputLabel> <t:inputText
										size="30" id="idNumLacre"
										value="#{beanListaRemessa.pesquisaListaRemessaDto.lacre}" />
									<rich:hotKey selector="#idNumMalote"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
									<rich:hotKey selector="#idNumLacre"
												key="return"
												handler="document.getElementById('btnPesquisa').onclick()" />
								</td>
							</tr>
						</table>
					</ul>
				</rich:panel>

				<rich:panel bodyClass="inpanelBody">
					<table>
						<tr>
							<td><a4j:commandButton styleClass="BotaoPesquisar"
									style="margin-left:15px;" value="Pesquisar" id="btnPesquisa"
									actionListener="#{beanListaRemessa.pesquisarRemessasFinalizadas}"
									ignoreDupResponses="true" reRender="pnlResultadoPesquisa"
									onclick="exibirMsgProcessando(true)"
									oncomplete="exibirMsgProcessando(false);" /></td>
							<td><a4j:commandButton styleClass="BotaoPadrao"
									style="margin-left:15px;" value="Limpar" id="btnLimpar"
									actionListener="#{beanListaRemessa.limparCampos}"
									ignoreDupResponses="true"
									reRender="painelCamposPesquisa,pnlResultadoPesquisa" /></td>
						</tr>
					</table>
				</rich:panel>
			</h:panelGrid>

			<a4j:outputPanel id="pnlResultadoPesquisa" ajaxRendered="true">
				<c:if test="${not empty beanListaRemessa.remessas}">

					<h:commandButton
						actionListener="#{beanListaRemessa.relatorioExcel}"
						styleClass="GerarXLS"
						style="float: right; margin-right: 20px; width: 260px;"
						value="EXPORTAR PLANILHA PARA O EXCEL" />
					<br>
					<br>
					<h:panelGrid styleClass="PadraoTituloPanel" id="pnlResultadoTotal">
						<h:outputText
							value="Resultado da Pesquisa - Quant. de Registros: #{beanListaRemessa.qtdListas} lista(s), #{beanListaRemessa.qtdRegistros} remessa(s) e #{beanListaRemessa.qtdVolumes} volume(s)" />
					</h:panelGrid>
					<rich:datascroller align="left" for="tbPesquisaListaRemessa"
						maxPages="100" reRender="sc2" id="sc1" />
					<rich:dataTable id="tbPesquisaListaRemessa"
						value="#{beanListaRemessa.consultaRemessasPorVolume}"
						var="pesquisa" columnClasses="center"
						rowClasses="linha-par, linha-impar" rows="50" reRender="ds">
						<rich:column
							sortBy="#{pesquisa.remessa.listaRemessa.numeroListaRemessa}"
							width="20px" style="text-align: center;">
							<f:facet name="header">
								<h:outputText value="Nº Lista" />
							</f:facet>
							<h:outputText
								value="#{pesquisa.remessa.listaRemessa.numeroListaRemessa}" />
						</rich:column>
						<rich:column sortBy="#{pesquisa.remessa.numeroEtiquetaCorreios}"
							width="50px" style="text-align: center;">
							<f:facet name="header">
								<h:outputText value="Cód. Rastreio" />
							</f:facet>
							<h:outputText value="#{pesquisa.volume.numeroEtiquetaCorreios}" />
						</rich:column>
						<rich:column sortBy="#{pesquisa.volumeFormatado}" width="20px"
							style="text-align: center;">
							<f:facet name="header">
								<h:outputText value="Volume" />
							</f:facet>
							<h:outputText value="#{pesquisa.volumeFormatado}" />
						</rich:column>
						<rich:column
							sortBy="#{pesquisa.remessa.listaRemessa.dataCriacao.time}"
							width="50px" style="text-align: center;">
							<f:facet name="header">
								<h:outputText value="Dt Criação" />
							</f:facet>
							<h:outputText
								value="#{pesquisa.remessa.listaRemessa.dataCriacao.time}">
								<f:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>
						</rich:column>
						<rich:column
							sortBy="#{pesquisa.remessa.listaRemessa.dataEnvio.time}"
							width="50px" style="text-align: center;">
							<f:facet name="header">
								<h:outputText value="Dt. Remessa" />
							</f:facet>
							<h:outputText
								value="#{pesquisa.remessa.listaRemessa.dataEnvio.time}">
								<f:convertDateTime pattern="dd/MM/yyyy" />
							</h:outputText>
						</rich:column>
						<rich:column sortBy="#{pesquisa.remessa.descricaoAnterior}"
							width="50px">
							<f:facet name="header">
								<h:outputText value="Detalhe Pré" />
							</f:facet>
							<h:outputText value="#{pesquisa.remessa.descricaoAnterior}" />
						</rich:column>
						<rich:column sortBy="#{pesquisa.remessa.descricaoPrincipal}"
							width="200px">
							<f:facet name="header">
								<h:outputText value="Destinatário" />
							</f:facet>
							<h:outputText value="#{pesquisa.remessa.descricaoPrincipal}" />
						</rich:column>
						<rich:column sortBy="#{pesquisa.remessa.descricaoPosterior}"
							width="50px">
							<f:facet name="header">
								<h:outputText value="Detalhe Pós" />
							</f:facet>
							<h:outputText value="#{pesquisa.remessa.descricaoPosterior}" />
						</rich:column>
						<rich:column
							sortBy="#{pesquisa.remessa.listaRemessa.tipoServico.nome}"
							width="50px" style="text-align: center;">
							<f:facet name="header">
								<h:outputText value="Tipo/Serviço" />
							</f:facet>
							<h:outputText
								value="#{pesquisa.remessa.listaRemessa.descricaoTipoEntrega} - #{pesquisa.remessa.listaRemessa.tipoServico.nome}" />
						</rich:column>
						<rich:column sortBy="#{pesquisa.remessa.numeroComunicacao}"
							width="50px" style="text-align: center;">
							<f:facet name="header">
								<h:outputText value="Documento" />
							</f:facet>
							<h:outputText
								value="#{pesquisa.remessa.tipoComunicacao.descricao}  #{pesquisa.remessa.numeroComunicacao}" />
						</rich:column>
						<rich:column sortBy="#{pesquisa.remessa.vinculo}" width="70px">
							<f:facet name="header">
								<h:outputText value="Vínculo" />
							</f:facet>
							<h:outputText value="#{pesquisa.remessa.vinculo}" />
						</rich:column>
						
						<rich:column>
							<f:facet name="header">
								<h:outputText value="Lista(s) de Remessa" />
							</f:facet>
							<a4j:repeat value="#{pesquisa.remessa.listasEnviadas}" var="lista" >
								<h:outputLabel value="#{lista.numeroListaRemessaAnoFormato}"/>
							</a4j:repeat>
						</rich:column>						
						
						<rich:column sortBy="#{pesquisa.remessa.guiaDeslocamento}"
							width="50px">
							<f:facet name="header">
								<h:outputText value="Guia Deslocamento" />
							</f:facet>
							<h:outputText value="#{pesquisa.remessa.guiaDeslocamento}" />
						</rich:column>
						<rich:column style="text-align: center;" width="5px">
							<f:facet name="header">
								<h:outputText value="Visualizar (PDF)" />
							</f:facet>
							<h:commandLink actionListener="#{beanVisualizarListaRemessa.baixarImagem}"
								id="linkVisualizar">
								<h:graphicImage value="/images/pdf.png" title="Visualizar" />
							</h:commandLink>
						</rich:column>
						<f:facet name="footer">
							<rich:datascroller align="left" for="tbPesquisaListaRemessa"
								maxPages="100" id="sc2" reRender="sc1" />
						</f:facet>
					</rich:dataTable>
				</c:if>
			</a4j:outputPanel>
			<jsp:include page="/pages/template/footer.jsp" flush="true" />
		</h:form>
	</a4j:page>
</f:view>