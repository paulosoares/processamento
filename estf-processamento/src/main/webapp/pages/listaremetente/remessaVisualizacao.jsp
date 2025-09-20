<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<script type="text/javascript">
	function validacaoNumeroListaRemessa(campo) {
		var digitos = "0123456789"
		var campoTemp

		if (campo.id == 'idCepListaRemessa') {
			if (campo.value == '') {
				document.getElementById("msgCEP").innerHTML = "";
			}
		}

		for (var i = 0; i < campo.value.length; i++) {
			campoTemp = campo.value.substring(i, i + 1)

			if (campo.id == 'idCepListaRemessa') {
				campo.value = campo.value.substring(0, 8);
				if (campo.value.length < 8) {
					document.getElementById("msgCEP").innerHTML = "<font color='red'>campo incompleto</font>";
				} else {
					document.getElementById("msgCEP").innerHTML = "<font color='green'>ok</font>";
				}
			}

			if (campo.id == 'idCodAreaTelefoneListaRemessa') {
				if(i > 1){
					campo.value = campo.value.substring(0, 2);
				}
			}

			if (campo.id == 'idNumeroTelefoneListaRemessa') {
				if(i > 9){
					campo.value = campo.value.substring(0, 10);
				}
			}

			if (campo.id == 'idCodAreaFaxListaRemessa') {
				if(i > 1){
					campo.value = campo.value.substring(0, 2);
				}
			}

			if (campo.id == 'idNumeroFaxListaRemessa') {
				if(i > 9){
					campo.value = campo.value.substring(0, 10);
				}
			}

			if (campo.id == 'idCodigoOrgaoListaRemessa') {
				if(i > 3){
					campo.value = campo.value.substring(0, 4);
				}
			}

			if (campo.id == 'idPesoGramasListaRemessa') {
				if(i > 9){
					campo.value = campo.value.substring(0, 10);
				}
			}

			if (digitos.indexOf(campoTemp) == -1) {
				campo.value = campo.value.substring(0, i);
			}
		}
	}

	function validacaoAlfaListaRemessa(campo) {
		for (var i = 0; i < campo.value.length; i++) {
			if (campo.id == 'idNumeroListaRemessa') {
				if(i > 4){
					campo.value = campo.value.substring(0, 5);
				}
			}

			if (campo.id == 'idDetalhePreListaRemessa') {
				if(i > 99){
					campo.value = campo.value.substring(0, 100);
				}
			}

			if (campo.id == 'idDetalhePrincipalListaRemessa') {
				if(i > 199){
					campo.value = campo.value.substring(0, 200);
				}
			}

			if (campo.id == 'idDetalhePosListaRemessa') {
				if(i > 99){
					campo.value = campo.value.substring(0, 100);
				}
			}

			if (campo.id == 'idBairro') {
				if(i > 199){
					campo.value = campo.value.substring(0, 200);
				}
			}

			if (campo.id == 'idLogradouroListaRemessa') {
				if(i > 249){
					campo.value = campo.value.substring(0, 250);
				}
			}

			if (campo.id == 'idComplementoListaRemessa') {
				if(i > 29){
					campo.value = campo.value.substring(0, 30);
				}
			}

			if (campo.id == 'idAgrupadorListaRemessa') {
				if(i > 9){
					campo.value = campo.value.substring(0, 10);
				}
			}

			if (campo.id == 'idEmailListaRemessa') {
				if (campo.value == '') {
					document.getElementById("msgEmailListaRemessa").innerHTML = "";
				}
			}

			if (campo.id == 'idEmailListaRemessa') {
				if(i > 49){
					usuario = campo.value.substring(0, campo.value.indexOf("@"));
					dominio = campo.value.substring(campo.value.indexOf("@") + 1,
							campo.value.length);
					if ((usuario.length >= 1) && (dominio.length >= 3)
							&& (usuario.search("@") == -1)
							&& (dominio.search("@") == -1)
							&& (usuario.search(" ") == -1)
							&& (dominio.search(" ") == -1)
							&& (dominio.search(".") != -1)
							&& (dominio.indexOf(".") >= 1)
							&& (dominio.lastIndexOf(".") < dominio.length - 1)) {
						document.getElementById("msgEmailListaRemessa").innerHTML = "";
					} else {
						document.getElementById("msgEmailListaRemessa").innerHTML = "<font color='red'>e-mail inválido</font>";
					}
					campo.value = campo.value.substring(0, 50);
				}
			}

			if (campo.id == 'idContatoListaRemessa') {
				if(i > 49){
					campo.value = campo.value.substring(0, 50);
				}
			}
		}
	}
</script>

<f:view>
	<a4j:page pageTitle="::.. Visualizar Lista de Remessa ..::">

		<h:form id="formVisualizarListaRemessa" prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Visualizar Lista de Remessa" />
			</jsp:include>

			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">
					<a4j:outputPanel id="pnlCadastrarRemessa" ajaxRendered="true">
						<h:panelGrid id="pnlFiltroCadastro" styleClass="PadraoTituloPanel" cellpadding="0" cellspacing="0">
							<h:outputText value="Cadastrar Lista de Remessa" />
						</h:panelGrid>
					</a4j:outputPanel>
				</h:panelGrid>
			</h:panelGrid>

			<h:panelGrid columns="1" id="panelVisualizacaoListaRemessa">
				<f:facet name="header">
					<b>Visualizar Lista de Remessa</b>
				</f:facet>
				<rich:panel id="panelCabecalhoVisualizacao" bodyClass="inpanelBody">
					<h:panelGrid id="painelPesquisaListaRemessaNumeroLista" columns="2">
						<h:outputText value="Número da Lista: " style="font-weight: bold;" />
						<h:outputText value="#{beanVisualizarListaRemessa.listaRemessa.numeroListaRemessaAnoFormato}"/>
					</h:panelGrid>
					<h:panelGrid id="painelPesquisaListaRemessa" columns="3">
						<h:panelGrid id="painelPesquisaListaRemessaTipoEntrega" columns="1" style="border-right: solid 1px;">
							<h:outputText value="Tipo de Entrega: " style="font-weight: bold;" />
							<h:outputText value="#{beanVisualizarListaRemessa.listaRemessa.tipoEntrega.descricao}"/>
						</h:panelGrid>
						<h:panelGrid id="painelPesquisaListaRemessaUnidadePostagem" columns="1" style="border-right: solid 1px;">
							<h:outputText value="Unidade de Postagem: " style="font-weight: bold;" />
							<h:outputText value="#{beanVisualizarListaRemessa.listaRemessa.unidadePostagem.nomeUnidadePostagem == null ? 'Sem Unidade' : beanVisualizarListaRemessa.listaRemessa.unidadePostagem.nomeUnidadePostagem}"/>
						</h:panelGrid>
						<h:panelGrid id="painelPesquisaListaRemessaServico" columns="1">
							<h:outputText value="Serviço: " style="font-weight: bold;" />
							<h:outputText value="#{beanVisualizarListaRemessa.listaRemessa.tipoServico.nome}"/>
						</h:panelGrid>
					</h:panelGrid>
				</rich:panel>

				<rich:panel id="panelRemessaVisualizacao" bodyClass="inpanelBody" style="width:1200px">
					<f:facet name="header">
						<b>Remessa</b>
					</f:facet>
					<table>
						<tr>
							<td><h:outputText value="Detalhe Pré: " style="font-weight: bold;" /></td>
							<td colspan="5"><h:outputText id="idDetalhePreListaRemessaVisualizacao"	value="#{beanVisualizarListaRemessa.remessa.descricaoAnterior}"/></td>
						</tr>
						<tr>
							<td><h:outputText value="Destinatário: " style="font-weight: bold;"/></td>
							<td colspan="5"><h:outputText id="idDetalhePrincipalListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.descricaoPrincipal}"/></td>
						</tr>
						<tr>
							<td><h:outputText value="Detalhe Pós: " style="font-weight: bold;"/></td>
							<td colspan="5"><h:outputText id="idDetalhePosListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.descricaoPosterior}" /></td>
						</tr>
						<tr>
							<td><h:outputText value="País: " style="font-weight: bold;"/></td>
							<td><h:outputText id="idPaisListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.pais}" /></td>
							<td><h:outputText value="UF: " style="font-weight: bold;"/></td>
							<td><h:outputText id="idUFXVisualizacao" value="#{beanVisualizarListaRemessa.remessa.uf}"	/></td>
							<td><h:outputText value="Cidade: " style="font-weight: bold;"/></td>
							<td><h:outputText id="idCidadeXVisualizacao" value="#{beanVisualizarListaRemessa.remessa.cidade}"/></td>
						</tr>
						<tr>
							<td><h:outputText value="Bairro: " style="font-weight: bold;"/></td>
							<td><h:outputText id="idBairroListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.bairro}" /></td>
							<td><h:outputText value="CEP: " style="font-weight: bold;"/></td>
							<td><h:outputText id="idCepListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.cep}"	/></td>
							<td colspan="2"></td>
						</tr>
						<tr>
							<td><h:outputText value="Logradouro: "style="font-weight: bold;" /></td>
							<td><h:outputText id="idLogradouroListaRemessaVisualizacao"	value="#{beanVisualizarListaRemessa.remessa.logradouro}"	/></td>
							<td><h:outputText value="Número: " style="font-weight: bold;"/></td>
							<td><h:outputText id="idNumeroListaRemessaVisualizacao"	value="#{beanVisualizarListaRemessa.remessa.numero}" /></td>
							<td><h:outputText value="Complemento: " style="font-weight: bold;"/></td>
							<td><h:outputText id="idComplementoListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.complemento}" /></td>
						</tr>
						<tr>
							<td><h:outputText value="Agrupador: " style="font-weight: bold;"/></td>
							<td><h:outputText id="idAgrupadorListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.agrupador}"/></td>
							<td><h:outputText value="Cód. Órgão: " style="font-weight: bold;"/></td>
							<td colspan="2"><h:outputText id="idCodigoOrgaoListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.codigoOrigem}" /></td>
						</tr>
						<tr>
							<td><h:outputText value="Telefone: " style="font-weight: bold;"/></td>
							<td>
								<h:outputText id="idCodAreaTelefoneListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.codigoAreaTelefone}" /> 
							    <h:outputText id="idNumeroTelefoneListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.numeroTelefone}"/>
						    </td>
							<td><h:outputText value="Fax: " style="font-weight: bold;"/></td>
							<td colspan="2">
								<h:outputText id="idCodAreaFaxListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.codigoAreaFax}"/> 
								<h:outputText id="idNumeroFaxListaRemessaVisualizacao"	value="#{beanVisualizarListaRemessa.remessa.numeroFax}"/>
							</td>
						</tr>
						<tr>
							<td><h:outputText value="E-mail: " style="font-weight: bold;"/></td>
							<td><h:outputText id="idEmailListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.email}" />
							</td>
							<td><h:outputText value="Contato: " style="font-weight: bold;"/></td>
							<td colspan="2"><h:outputText id="idContatoListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.nomeContato}" /></td>
						</tr>
					</table>

					<h:panelGroup id="panelTipoRemessa"  rendered="#{!beanVisualizarListaRemessa.exibirMalote}">
						<table>
							<tr>
								<td><h:outputText value="Tipo de Remessa:" style="font-weight: bold;"/></td>
								<td><h:outputText id="idTipoRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.tipoEmbalagem.nome}" /></td>
							</tr>
						</table>
					</h:panelGroup>

					<h:panelGroup id="panelTipoServicoAdicionalVisualizacao" rendered="#{beanVisualizarListaRemessa.exibirCampoServicoAdicional}">
						<table>
							<tr>
								<td>
									<h:outputText value="Serviços Adicionais:" style="font-weight: bold;"/>
								</td>
								<td>
									<a4j:repeat value="#{beanVisualizarListaRemessa.remessa.tiposServicoNaoObrigatorios}" var="objServicoAdicional" >
										<h:outputText value="#{objServicoAdicional.nome}" />
										<h:outputText value=";" />
										<rich:spacer width="5"/>
									</a4j:repeat>
								</td>
							</tr>
						</table>
					</h:panelGroup>

					<h:panelGroup id="panelQUantidadeVolumesVisualizacao" rendered="#{beanVisualizarListaRemessa.exibirCorreiosOuPortaria}">
						<table>
							<tr>
								<td colspan="4"><h:outputText value="Quant. de Volumes: " style="font-weight: bold;"/> 
								<h:outputText id="idQtdVolumesListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.quantidadeVolume}"></h:outputText></td>
							</tr>
							<tr>
								<td colspan="4">
									<rich:dataTable id="idTabelaVolumesVisualizacao"
													var="objVolume"
													value="#{beanVisualizarListaRemessa.remessa.volumes}">
										<rich:column style="text-align: center;" sortBy="#{objVolume.numeroVolume}">
											<f:facet name="header">
												<h:outputText value="Volume"/>
											</f:facet>
											<h:outputText value="#{objVolume.numeroVolume}" />
										</rich:column>
										<rich:column style="text-align: center;" sortBy="#{objVolume.pesoGramas}">
											<f:facet name="header">
												<h:outputText value="Peso"/>
											</f:facet>
											<h:outputText id="idPesoGramasListaRemessaVisualizacao"	value="#{objVolume.pesoGramas}"	/>gramas
										</rich:column>
										<rich:column style="text-align: center;">
											<f:facet name="header">
												<h:outputText value="Cód. Rastreio"/>
											</f:facet>
											<h:outputText value="#{objVolume.numeroEtiquetaCorreios != null ? objVolume.numeroEtiquetaCorreios : 'Sem código'}"/>
										</rich:column>
									</rich:dataTable>
								</td>
							</tr>
							<tr>
								<td><h:outputText value="Tipo de Documento: " style="font-weight: bold;"/></td>
								<td><h:outputText id="idTipoDocumentoVisualizacao" value="#{beanVisualizarListaRemessa.remessa.tipoComunicacao.descricao}" /></td>
								<td>
									<h:outputText value="Número de Documento: " rendered="#{beanVisualizarListaRemessa.verificarTipoDocumento}" style="font-weight: bold;"/> 
								</td>
								<td>
									<h:outputText id="idNumDocumentoListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.numeroComunicacao}" rendered="#{beanVisualizarListaRemessa.verificarTipoDocumento}"/>
								</td>
							</tr>
						</table>
						<table>
							<tr>
								<td><h:outputText value="Vínculo: " style="font-weight: bold;"/></td>
								<td><h:outputText id="idVinculoListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.vinculo}" /></td>
							</tr>
						</table>
					</h:panelGroup>

					<h:panelGroup id="panelMaloteVisualizacao" rendered="#{beanVisualizarListaRemessa.exibirMalote && (beanVisualizarListaRemessa.listaRemessa.tipoEntrega != 'ENTREGA_PORTARIA')}">
						<table>
							<tr>
								<td>
									<h:outputText value="Número do Malote: " style="font-weight: bold;"/> 
									<h:outputText id="idNumMaloteListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.malote}" />
								</td>
							</tr>
							<tr>
								<td>
									<h:outputText value="Número do Lacre: " style="font-weight: bold;"/> 
									<h:outputText id="idNumLacreListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.lacre}" />
								</td>
							</tr>
						</table>
					</h:panelGroup>

					<table>
						<tr>
							<td><h:outputText value="Guia de Deslocamento: " style="font-weight: bold;"/></td>
							<td><h:outputText id="idGuiaDeslocamentoListaRemessaVisualizacao" value="#{beanVisualizarListaRemessa.remessa.guiaDeslocamento}" />
							</td>
						</tr>
						<tr>
							<td valign="middle"><h:outputText value="Observação: " style="font-weight: bold;"/></td>
							<td><h:outputText id="idObservacaoListaRemessaVisualizacao" style="width:600px" value="#{beanVisualizarListaRemessa.remessa.observacao}" /></td>
						</tr>

					</table>
					<br>

				</rich:panel>

				<rich:dataTable id="idTabelaRemessasVisualizacao"
								border="1"
								var="objRemessa"
								value="#{beanVisualizarListaRemessa.consultaRemessa}"
								binding="#{beanVisualizarListaRemessa.tabelaRemessasCriadas}">
					<rich:column>
						<f:facet name="header">
							<h:outputText value="Det. Pré" />
						</f:facet>
						<h:outputText value="#{objRemessa.descricaoAnterior}" />
					</rich:column>
					<rich:column>
						<f:facet name="header">
							<h:outputText value="Destinatário" />
						</f:facet>
						<h:outputText value="#{objRemessa.descricaoPrincipal}" />
					</rich:column>
					<rich:column>
						<f:facet name="header">
							<h:outputText value="Det. Pós" />
						</f:facet>
						<h:outputText value="#{objRemessa.descricaoPosterior}" />
					</rich:column>
					<rich:column>
						<f:facet name="header">
							<h:outputText value="Agrupador" />
						</f:facet>
						<h:outputText value="#{objRemessa.agrupador}" />
					</rich:column>
					<rich:column>
						<f:facet name="header">
							<h:outputText value="Cód Órgão" />
						</f:facet>
						<h:outputText value="#{objRemessa.codigoOrigem}" />
					</rich:column>
					<rich:column>
						<f:facet name="header">
							<h:outputText value="Documento" />
						</f:facet>
						<h:outputText value="#{objRemessa.tipoComunicacao.descricao } #{objRemessa.numeroComunicacao}" />
					</rich:column>
					<rich:column>
						<f:facet name="header">
							<h:outputText value="Vínculo" />
						</f:facet>
						<h:outputText value="#{objRemessa.vinculo}" />
					</rich:column>
					<rich:column>
						<f:facet name="header">
							<h:outputText value="Lista(s) de Remessa" />
						</f:facet>
						<a4j:repeat value="#{objRemessa.listasEnviadas}" var="lista" >
							<h:commandLink value="#{lista.numeroListaRemessaAnoFormato}" action="#{beanVisualizarListaRemessa.visualizarListaRemessa}">
								<f:setPropertyActionListener value="#{lista}" target="#{beanVisualizarListaRemessa.listaRemessaVisualizar}" />
								<f:setPropertyActionListener value="#{beanListaRemessa}" target="#{beanVisualizarListaRemessa.pesquisaListaRemessa}" />
							</h:commandLink>
							<h:outputText value=";" />
							<rich:spacer width="5"/>
						</a4j:repeat>
					</rich:column>
					<rich:column>
						<f:facet name="header">
							<h:outputText value="Guia Deslocamento" />
						</f:facet>
						<h:outputText value="#{objRemessa.guiaDeslocamento}" />
					</rich:column>

					<rich:column style="text-align: center;" width="5px">
						<f:facet name="header">
							<h:outputText value="Visualizar" />
						</f:facet>
						<h:commandLink id="linkVisualizarListaRemessaVisualizacao"
										actionListener="#{beanVisualizarListaRemessa.visualizarRemessa}">
							<h:graphicImage value="/images/pesquisar.jpg" title="Visualizar" />
							<%-- <c:ajax render="panelVisualizacaoListaRemessa" onevent="click"/> --%>
						</h:commandLink>
					</rich:column>

					<rich:column style="text-align: center;">
						<f:facet name="header">
							<h:outputText value="Imprimir" />
						</f:facet>
						<h:commandLink id="linkImprimirEtiquetasIndividualVisualizacao"
										rendered="#{beanVisualizarListaRemessa.exibirImprimirEtiquetaIndividual and not beanVisualizarListaRemessa.malote}"
										actionListener="#{beanVisualizarListaRemessa.imprimirEtiquetasIndividual}">
							<h:graphicImage value="/images/printer1.png" title="Imprimir" />
						</h:commandLink>
					</rich:column>
					<rich:column style="text-align: center;">
						<f:facet name="header">
							<h:outputText value="Excluir" />
						</f:facet>
						<a4j:commandLink id="linkExcluirRemessaVisualizacao" 
											reRender="idTabelaRemessasVisualizacao" 
											actionListener="#{beanVisualizarListaRemessa.excluirRemessa}"
											rendered="#{not objRemessa.listaRemessa.finalizada}">
							<h:graphicImage url="/images/deletecell.png" title="Excluir" />
						</a4j:commandLink>
					</rich:column>
				</rich:dataTable>

				<h:panelGroup id="idPanelBotoesVisualizacao">
					<rich:panel bodyClass="inpanelBody">
						<table>
							<tr>
								<td colspan="5">
									<h:panelGroup id="idPanelPosicaoInicialVisualizacao">
										<table style="margin-bottom: 15px;">
											<tr>
												<td style="vertical-align: top;">Posição inicial impressão etiq.</td>
												<td style="vertical-align: top;">
													<rich:comboBox id="idComboPosicaoInicialEtiquetaVisualizacao"
																	value="#{beanVisualizarListaRemessa.selecaoQtdEtiquetas}"
																	suggestionValues="#{beanVisualizarListaRemessa.comboEtiquetas}"
																	directInputSuggestions="true" selectFirstOnUpdate="1"/>
												</td>
												<td style="vertical-align: top;">
													<a4j:commandLink reRender="idAjudaEtiquetasVisualizacao" oncomplete="Richfaces.showModalPanel('idAjudaEtiquetasVisualizacao')">
														<h:graphicImage value="/images/help.png" width="22" height="22" />
													</a4j:commandLink>
												</td>
											</tr>
										</table>
									</h:panelGroup>
								</td>
							</tr>
							<tr>
								<td>
									<h:commandButton id="btnSalvarListaVisualizacao"
													styleClass="BotaoPadrao"
													style="margin-left:15px;"
													value="Salvar Lista"
													action="#{beanVisualizarListaRemessa.salvarLista}" 
													rendered="#{not beanVisualizarListaRemessa.listaRemessa.finalizada}"/>
								</td>
								<td>
									<h:commandButton id="btnImprimirListaVisualizacao"
													styleClass="BotaoPadraoEstendido"
													style="margin-left:15px;"
													value="Imprimir Lista"
													actionListener="#{beanVisualizarListaRemessa.imprimirLista}" />
								</td>
								<td>
									<h:commandButton id="btnImprimirEtiquetasListaVisualizacao"
													styleClass="BotaoPadraoEstendido"
													style="margin-left:15px;"
													value="Imprimir Todas Etiquetas"
													rendered="#{beanVisualizarListaRemessa.listaRemessa.tipoEntrega != 'MALOTE'}"
													actionListener="#{beanVisualizarListaRemessa.imprimirEtiquetasLista}" />
								</td>
								<td>
									<h:commandButton id="btnCadastrarNovaListaVisualizacao"
														styleClass="BotaoPadraoEstendido"
														style="margin-left:15px;"
														value="Criar Nova Lista"
														actionListener="#{beanListaRemessa.novo}"
														action="#{beanMenu.remessaCadastro}" />
								</td>
								<td>
									<h:commandButton id="btnVoltarVisualizacao"
														styleClass="BotaoPadraoEstendido"
														style="margin-left:15px;"
														value="Voltar"
														rendered="#{beanVisualizarListaRemessa.exibirBotaoVoltar}"
														action="#{beanVisualizarListaRemessa.voltar}" />
								</td>
							</tr>
						</table>
					</rich:panel>
				</h:panelGroup>
			</h:panelGrid>
			<jsp:include page="/pages/template/footer.jsp" flush="true" />

		</h:form>

		<jsp:include page="/pages/destinatariolistaremetente/destinatarioPesquisaModal.jsp" />

		<!-- Ajuda Etiquetas -->
		<rich:modalPanel id="idAjudaEtiquetasVisualizacao" keepVisualState="true"
			style="overflow:auto; position: fixed; top: 50; left: 50;" autosized="true" width="300" moveable="false">

			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="Ajuda - Posição das Etiquetas" />
				</h:panelGroup>
			</f:facet>
			<f:facet name="controls">
				<h:panelGroup>
					<h:graphicImage value="/images/error.gif" styleClass="hidelink"
						id="esconderAjudaEtiquetas" />
					<rich:componentControl for="idAjudaEtiquetasVisualizacao"
						attachTo="esconderAjudaEtiquetas" operation="hide" event="onclick" />
				</h:panelGroup>
			</f:facet>

			<table>
				<tr>
					<td><h:panelGrid style="width: 135px; height: 100px; border: 2px solid #000000;">
							<center>ETQ 1</center>
						</h:panelGrid></td>
					<td><h:panelGrid style="width: 135px; height: 100px; border: 2px solid #000000;">
							<center>ETQ 2</center>
						</h:panelGrid></td>
				</tr>
				<tr>
					<td><h:panelGrid style="width: 135px; height: 100px; border: 2px solid #000000;">
							<center>ETQ 3</center>
						</h:panelGrid></td>
					<td><h:panelGrid style="width: 135px; height: 100px; border: 2px solid #000000;">
							<center>ETQ 4</center>
						</h:panelGrid></td>
				</tr>
				<tr>
					<td><h:panelGrid style="width: 135px; height: 100px; border: 2px solid #000000;" rendered="#{beanVisualizarListaRemessa.posicionamentoEtiquetas}">
							<center>ETQ 5</center>
						</h:panelGrid></td>
					<td><h:panelGrid style="width: 135px; height: 100px; border: 2px solid #000000;" rendered="#{beanVisualizarListaRemessa.posicionamentoEtiquetas}">
							<center>ETQ 6</center>
						</h:panelGrid></td>
				</tr>

				<tr>
					<td><h:panelGrid style="width: 135px; height: 100px; border: 2px solid #000000;" rendered="#{beanVisualizarListaRemessa.posicionamentoEtiquetas}">
							<center>ETQ 7</center>
						</h:panelGrid></td>
					<td><h:panelGrid style="width: 135px; height: 100px; border: 2px solid #000000;" rendered="#{beanVisualizarListaRemessa.posicionamentoEtiquetas}">
							<center>ETQ 8</center>
						</h:panelGrid></td>
				</tr>
			</table>

		</rich:modalPanel>
	</a4j:page>
</f:view>