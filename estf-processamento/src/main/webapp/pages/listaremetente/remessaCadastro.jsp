<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
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

			if(campo.id == 'idQtdVolumesListaRemessa'){
				if(i > 3){
					campo.value = campo.value.substring(0, 4);
				}
			}
			
			if (campo.id == 'idTabelaVolumes:0:idPesoGramasListaRemessa') {
				if(i > 9){
					campo.value = campo.value.substring(0, 10);
				}
			}

			if(campo.id == 'idNumMaloteListaRemessa'){
				if(i > 4){
					campo.value = campo.value.substring(0, 4);
				}
			}
			
			if(campo.id == 'idNumLacreListaRemessa'){
				if(i > 12){
					campo.value = campo.value.substring(0, 13);
				}
			}
			
			if (digitos.indexOf(campoTemp) == -1) {
				campo.value = campo.value.substring(0, i);
			}
		}
	}

	function onclickCampoPeso(campo){
		if(campo.value == '0'){
			campo.value = '';
		}
	}
	
	function onblurCampoPeso(campo){
		if(campo.value == ''){
			campo.value = '0';
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
				if(i > 99){
					campo.value = campo.value.substring(0, 100);
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
			if (campo.id == 'idObservacaoDestinatarioListaRemessa'){
				if(i > 999){
					campo.value = campo.value.substring(0, 1000);
				}
			}
			
			if (campo.id == 'idObservacaoListaRemessa') {
				if(i > 999){
					campo.value = campo.value.substring(0, 1000);
				}
			}
			
			if(campo.id == 'idNumDocumentoListaRemessa'){
				if(i > 199){
					campo.value = campo.value.substring(0, 200);
				}
				campo.value = campo.value.replace(";","/");
			}
			
			if(campo.id == 'idVinculoListaRemessa'){
				if(i > 419){
					campo.value = campo.value.substring(0, 420);
				}
			}
			
			if(campo.id == 'idGuiaDeslocamentoListaRemessa'){
				if(i > 119){
					campo.value = campo.value.substring(0, 120);
				}
			}
			
			
		}
	}

	function copiarConteudoCampos(campoOrigem, campoDestino) {
		campoDestino.value = campoOrigem.value;
	}

	function setFoco(campo) {
		document.getElementById(campo).focus();
	}
</script>

<f:view>
	<a4j:page pageTitle="::.. Cadastrar Lista de Remessa ..::">

		<h:form id="formCadastroListaRemessa" prependId="false">		
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Cadastrar Lista de Remessa" />
			</jsp:include>
		
			<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
				cellspacing="0">
				<h:panelGrid styleClass="Moldura" cellpadding="0" cellspacing="0">
					<a4j:outputPanel id="pnlCadastrarRemessa" ajaxRendered="true">
						<h:panelGrid id="pnlFiltroCadastro" styleClass="PadraoTituloPanel"
							cellpadding="0" cellspacing="0">
							<h:outputText value="Cadastrar Lista de Remessa" />
						</h:panelGrid>
					</a4j:outputPanel>
				</h:panelGrid>
			</h:panelGrid>

			<h:panelGrid columns="1" id="panelListaRemessa">
				<f:facet name="header">
					<b><h:outputText id="outputTitulo" value="#{beanListaRemessa.tituloLista}" /></b>
				</f:facet>
				<rich:panel id="panelCabecalho" bodyClass="inpanelBody">
					<h:panelGroup id="panelTipoEntrega">
						<h:outputText value="Tipo de Entrega* " />						
						<h:selectOneRadio id="radioTipoEntrega"
											disabled="#{beanListaRemessa.existeRemessa}"
											value="#{beanListaRemessa.tipoEntrega}"
											layout="lineDirection">
								<f:selectItem itemValue="CORREIOS" itemLabel="Correios" />
								<f:selectItem itemValue="ENTREGA_PORTARIA" itemLabel="Entrega Portaria" />
								<f:selectItem itemValue="MALOTE" itemLabel="Malote" />
								<a4j:support event="onclick" reRender="panelListaRemessa" />
						</h:selectOneRadio>						
					</h:panelGroup>
					
					<h:panelGroup id="panelUnidadePostagem" rendered="#{beanListaRemessa.exibirCampoUnidadePostagem}">
						<h:outputText value="Unidade de Postagem* " />
						<h:selectOneMenu disabled="#{beanListaRemessa.existeRemessa}"
											styleClass="Input"
											style="margin-left:10px;"
											value="#{beanListaRemessa.codigoUnidadePostagem}">
								<f:selectItems value="#{beanListaRemessa.comboUnidadeDePostagem}" />
						</h:selectOneMenu>
						
					</h:panelGroup>
					<h:panelGroup id="panelTipoServico"
						rendered="#{beanListaRemessa.exibirCampoTipoServico}">
						<h:outputText value="Serviço* " />
						<h:selectOneMenu disabled="#{beanListaRemessa.existeRemessa}"
											styleClass="Input"
											style="margin-left:10px;"
											value="#{beanListaRemessa.codigoTipoServico}">
							<f:selectItems value="#{beanListaRemessa.comboTipoServico}" />
							<a4j:support event="onchange"
											actionListener="#{beanListaRemessa.selecionarServico}"
											reRender="panelListaRemessa,idComboPosicaoInicialEtiqueta" />
						</h:selectOneMenu>
						
					</h:panelGroup>
				</rich:panel>

				<rich:panel id="panelRemessa"
							rendered="#{beanListaRemessa.exibirBotaoAdicionar}"
							bodyClass="inpanelBody" style="width:1200px">
					<f:facet name="header">
						<b>Remessa</b>
					</f:facet>

					<table>
						<tr>
							<td><h:outputText value="Destinatário " /></td>
							<td>
								<t:inputText id="idDestinatarioListaRemessa"
												size="50"
												onkeyup="javascript:validacaoAlfaListaRemessa(this);"
												value="#{beanDestinatario.campoPesquisa}" />
								<h:inputHidden id="idDestinatarioListaRemessaId"
												value="#{beanListaRemessa.idDestinatario}" />
								<rich:hotKey selector="#idDestinatarioListaRemessa"
												key="return"
												handler="document.getElementById('btnBuscarDestinatarioCodigoBarras').onclick()" />
							</td>
							<td>
								<a4j:commandButton id="btnBuscarDestinatario"
													styleClass="BotaoPadraoEstendido"
													value="Buscar Destinatário"
													onclick="exibirMsgProcessando(true)"
													oncomplete="exibirMsgProcessando(false);"
													reRender="#{beanListaRemessa.nomeComponenteRerenderizar}"
													action="#{beanDestinatario.abrirModoPesquisaDialogo}">
									<f:setPropertyActionListener value="#{beanListaRemessa}" target="#{beanDestinatario.selecionaBeanDestinatario}" />
								</a4j:commandButton>
							</td>
							<td>
								<a4j:commandButton id="btnBuscarDestinatarioCodigoBarras"
													styleClass="BotaoPadraoEstendido"
													value="Buscar Código de Barras"
													process="idDestinatarioListaRemessa"
													onclick="javascript:copiarConteudoCampos(idDestinatarioListaRemessa, idDestinatarioListaRemessaId);"
													reRender="#{beanListaRemessa.nomeComponenteRerenderizar}"
													actionListener="#{beanListaRemessa.pesquisarDestinatarioPeloId}" />
							</td>
						</tr>
						<tr>
							<td><h:outputText value="Detalhe Pré " /></td>
							<td colspan="5"><t:inputText id="idDetalhePreListaRemessa"
									size="60" value="#{beanListaRemessa.remessa.descricaoAnterior}"
									onkeyup="javascript:validacaoAlfaListaRemessa(this);" /></td>
						</tr>
						<tr>
							<td><h:outputText value="Destinatário* " /></td>
							<td colspan="5"><t:inputText
									id="idDetalhePrincipalListaRemessa" size="60"
									value="#{beanListaRemessa.remessa.descricaoPrincipal}"
									onkeyup="javascript:validacaoAlfaListaRemessa(this);" /></td>
						</tr>
						<tr>
							<td><h:outputText value="Detalhe Pós " /></td>
							<td colspan="5"><t:inputText id="idDetalhePosListaRemessa"
									size="60"
									value="#{beanListaRemessa.remessa.descricaoPosterior}"
									onkeyup="javascript:validacaoAlfaListaRemessa(this);" /></td>
						</tr>
						<tr>
							<td><h:outputText value="CEP* " /></td>
							<td>
								<t:inputText id="idCepListaRemessa"
												size="20"
												onkeyup="javascript:validacaoNumeroListaRemessa(this);"
												value="#{beanListaRemessa.remessa.cep}" />
								<div id="msgCEP"></div></td>
							<td colspan="3">
								<a4j:commandButton id="btnPesquisarCEPListaDestinatario"
													value="Pesquisar CEP"
													styleClass="BotaoPadraoEstendido"
													ignoreDupResponses="true"
													reRender="panelListaRemessa,idPanelExibirModalPesquisaCepPorEndereco"
													actionListener="#{beanListaRemessa.pesquisarCEP}">
									<f:setPropertyActionListener value="#{beanListaRemessa}" target="#{beanPesquisaCepEndereco.selecionaCep}" />
								</a4j:commandButton>
								<h:panelGroup id="idPanelExibirModalPesquisaCepPorEndereco">
									<h:panelGroup rendered="#{beanListaRemessa.flagExibirModalPesquisaCep}">
										<script type="text/javascript">
											Richfaces.showModalPanel('idModalPanelPesquisaCepPorEndereco')
										</script>
									</h:panelGroup>
								</h:panelGroup>
							</td>
						</tr>
						<tr>
							<td><h:outputText value="País* " /></td>
							<td colspan="5"><t:inputText id="idPaisListaRemessa"
									size="10" value="#{beanListaRemessa.pais}" disabled="true" /></td>
						</tr>
						<tr>
							<td><h:outputText value="UF* " /></td>
							<td><rich:comboBox id="idUfListaRemessa"
									value="#{beanListaRemessa.remessa.uf}"
									suggestionValues="#{beanListaRemessa.comboUF}"
									directInputSuggestions="true" defaultLabel="Digite a UF">
									<a4j:support ajaxSingle="true" event="onselect"
										actionListener="#{beanListaRemessa.configurarMunicipio}"
										reRender="idComboCidadeRemessa" />
								</rich:comboBox></td>

							<td><h:outputText value="Cidade* " /></td>
							<td><h:panelGroup id="panelCidadeListaRemessa">
									<rich:comboBox id="idComboCidadeRemessa"
										value="#{beanListaRemessa.remessa.cidade}"
										suggestionValues="#{beanListaRemessa.listaCidade}"
										directInputSuggestions="true" defaultLabel="Digite a Cidade" />
								</h:panelGroup></td>
						</tr>
						<tr>
							<td><h:outputText value="Bairro* " /></td>
							<td colspan="5"><t:inputText id="idBairroListaRemessa"
									size="50" value="#{beanListaRemessa.remessa.bairro}"
									onkeyup="javascript:validacaoAlfaListaRemessa(this);" /></td>
						</tr>
						<tr>
							<td><h:outputText value="Logradouro* " /></td>
							<td colspan="5"><t:inputText id="idLogradouroListaRemessa"
									size="50" value="#{beanListaRemessa.remessa.logradouro}"
									onkeyup="javascript:validacaoAlfaListaRemessa(this);" /></td>
						</tr>
						<tr>
							<td><h:outputText value="Complemento " /></td>
							<td><t:inputText id="idComplementoListaRemessa" size="50"
									value="#{beanListaRemessa.remessa.complemento}"
									onkeyup="javascript:validacaoAlfaListaRemessa(this);" /></td>
							<td><h:outputText value="Número* " /></td>
							<td colspan="2"><t:inputText id="idNumeroListaRemessa"
									size="10" value="#{beanListaRemessa.remessa.numero}"
									onkeyup="javascript:validacaoAlfaListaRemessa(this);" /></td>
						</tr>
						<tr>
							<td><h:outputText value="Agrupador " /></td>
							<td><t:inputText id="idAgrupadorListaRemessa" size="50"
									value="#{beanListaRemessa.remessa.agrupador}"
									onkeyup="javascript:validacaoAlfaListaRemessa(this);" /></td>
							<td><h:outputText value="Cód. Órgão " /></td>
							<td colspan="2"><t:inputText id="idCodigoOrgaoListaRemessa"
									size="10" value="#{beanListaRemessa.remessa.codigoOrigem}"
									onkeyup="javascript:validacaoNumeroListaRemessa(this);" /></td>
						</tr>
						<tr>
							<td><h:outputText value="Telefone " /></td>
							<td><t:inputText id="idCodAreaTelefoneListaRemessa" size="3"
									value="#{beanListaRemessa.remessa.codigoAreaTelefone}"
									onkeyup="javascript:validacaoNumeroListaRemessa(this);" /> <t:inputText
									id="idNumeroTelefoneListaRemessa" size="15"
									value="#{beanListaRemessa.remessa.numeroTelefone}"
									onkeyup="javascript:validacaoNumeroListaRemessa(this);" /></td>
							<td><h:outputText value="Fax " /></td>
							<td colspan="2"><t:inputText id="idCodAreaFaxListaRemessa"
									size="3" value="#{beanListaRemessa.remessa.codigoAreaFax}"
									onkeyup="javascript:validacaoNumeroListaRemessa(this);" /> <t:inputText
									id="idNumeroFaxListaRemessa" size="15"
									value="#{beanListaRemessa.remessa.numeroFax}"
									onkeyup="javascript:validacaoNumeroListaRemessa(this);" /></td>
						</tr>
						<tr>
							<td><h:outputText value="E-mail " /></td>
							<td><t:inputText id="idEmailListaRemessa" size="50"
									value="#{beanListaRemessa.remessa.email}"
									onkeyup="javascript:validacaoAlfaListaRemessa(this);" />
								<div id="msgEmailListaRemessa"></div></td>
							<td><h:outputText value="Contato " /></td>
							<td colspan="2"><t:inputText id="idContatoListaRemessa"
									size="20" value="#{beanListaRemessa.remessa.nomeContato}"
									onkeyup="javascript:validacaoAlfaListaRemessa(this);" /></td>
						</tr>
						<tr>
							<td valign="middle"><h:outputText value="Observação Destinatario" /></td>
							<td><h:inputTextarea id="idObservacaoDestinatarioListaRemessa"
													value="#{beanListaRemessa.observacaoDestinatario}" 
													onkeyup="javascript:validacaoAlfaListaRemessa(this);"
													cols="48" rows="2" /></td>
						</tr>
					</table>
					<h:panelGroup id="panelTipoRemessa"
						rendered="#{beanListaRemessa.exibirCampoTipoRemessa}">
						<table>
							<tr>
								<td><h:outputText value="Tipo de Remessa* " /></td>
								<td><h:selectOneRadio id="chkTipoRemessaListaRemessa"
										value="#{beanListaRemessa.tipoEmbalagem}"
										layout="lineDirection">
										<f:selectItems value="#{beanListaRemessa.tiposRemessa}" />
										<a4j:support event="onclick"
											actionListener="#{beanListaRemessa.limparCamposCadastroRemessa}"
											reRender="panelListaRemessa" />
									</h:selectOneRadio></td>
							</tr>
						</table>
					</h:panelGroup>

					<h:panelGroup id="panelTipoEmbalagem"
						rendered="#{beanListaRemessa.exibirCampoTipoEmbalagem}">
						<table>
							<tr>
								<td><h:selectOneRadio id="chkTipoEmbalagemListaRemessa"
										layout="lineDirection"
										value="#{beanListaRemessa.codigoTipoEmbalagem}">
										<f:selectItems value="#{beanListaRemessa.radioTipoEmbalagem}" />
									</h:selectOneRadio></td>
							</tr>
						</table>
					</h:panelGroup>

					<a4j:outputPanel id="panelTipoServicoAdicional"
						rendered="#{beanListaRemessa.exibirCampoServicoAdicional}">
						<table>
							<tr>
								<td><h:selectManyCheckbox
										id="chkTipoServicoAdicionalListaRemessa"
										value="#{beanListaRemessa.codigoTipoServicoAdiconal}"
										layout="lineDirection">
										<f:selectItems
											value="#{beanListaRemessa.checkTipoServicoAdicionais}" />
										<a4j:support event="onclick"
											actionListener="#{beanListaRemessa.marcarServicosAdicionaisDependentes}"
											reRender="panelTipoServicoAdicional" />
									</h:selectManyCheckbox></td>
							</tr>
						</table>
					</a4j:outputPanel>

					<h:panelGroup id="panelQUantidadeVolumes"
						rendered="#{beanListaRemessa.exibirCorreiosOuPortaria}">
						<table>
							<tr>
								<td><h:outputText value="Quant. de Volumes* " /> <t:inputText
										id="idQtdVolumesListaRemessa" size="3"
										value="#{beanListaRemessa.qtdVolumes}" onkeydown="javascript:validacaoNumeroListaRemessa(this);">
										<a4j:support event="onkeyup"
											actionListener="#{beanListaRemessa.geraListaVolumes}"
											reRender="idTabelaVolumes" />
									</t:inputText></td>
							</tr>
							<tr>
								<td><rich:dataTable id="idTabelaVolumes"
										style="#{beanListaRemessa.exibirCorreiosOuPortaria}"
										value="#{beanListaRemessa.listaVolumes}" var="objVolume">
										<rich:column>
											<h:outputText value="Peso* #{objVolume.numeroVolume}" />
										</rich:column>
										<rich:column>
											<t:inputText id="idPesoGramasListaRemessa" size="5"
												value="#{objVolume.pesoGramas}"
												onkeyup="javascript:validacaoNumeroListaRemessa(this);"
												onclick="javascript:onclickCampoPeso(this);"
												onblur="javascript:onblurCampoPeso(this);"/>gramas
										</rich:column>
									</rich:dataTable></td>
							</tr>
							<tr>
								<td>
									<h:outputText value="Tipo de Documento " />
									<h:selectOneMenu styleClass="Input"
														style="margin-left:10px;"
														value="#{beanListaRemessa.codigoTipoDocumento}">
										<f:selectItems value="#{beanListaRemessa.comboTipoDocumento}" />
										<a4j:support event="onchange" reRender="panelListaRemessa" />
									</h:selectOneMenu></td>
							</tr>
						</table>

						<h:panelGroup id="panelNumeroDocumento"
							rendered="#{beanListaRemessa.verificarTipoDocumento}">
							<table>
								<tr>
									<td>
										<h:outputText value="Número de Documento* " />
										<%-- verificar se o erro está sendo causado pelo a4j dentro do t --%>
										<h:inputText id="idNumDocumentoListaRemessa"
														size="60" 
														onkeyup="javascript:validacaoAlfaListaRemessa(this);"
														value="#{beanListaRemessa.remessa.numeroComunicacao}" >
											<a4j:support event="onchange" id="a4jChangeNumDoc"
														 actionListener="#{beanListaRemessa.recuperarStringListaProcessosDoDocumento}" 
														 reRender="panelVinculo" />
											<a4j:support event="onblur" id="a4jBlurNumDoc"
														 actionListener="#{beanListaRemessa.recuperarStringListaProcessosDoDocumento}" 
														 reRender="panelVinculo" />
										</h:inputText>
									</td>
								</tr>
							</table>
						</h:panelGroup>
						<h:panelGroup id="panelVinculo">
							<table>
								<tr>
									<td><h:outputText value="Vínculo " style="padding-right: 88px !important;"/></td>
									<td>
										<h:inputTextarea id="idVinculoListaRemessa"
														style="width:600px"
														onkeyup="javascript:validacaoAlfaListaRemessa(this);"
														value="#{beanListaRemessa.remessa.vinculo}" />																
									</td>
								</tr>
							</table>
						</h:panelGroup>
					</h:panelGroup>

					<h:panelGroup id="panelMalote" rendered="#{beanListaRemessa.exibirMalote}">
						<table>
							<tr>
								<td>
									<h:outputText value="#{beanListaRemessa.labelNumeroMalote} " />
									<t:inputText id="idNumMaloteListaRemessa"
													size="50"
													onkeyup="javascript:validacaoNumeroListaRemessa(this);"
													value="#{beanListaRemessa.remessa.malote}"/>
								</td>
							</tr>
							<tr>
								<td>
									<h:outputText value="#{beanListaRemessa.labelNumeroLacre} " />
									<t:inputText id="idNumLacreListaRemessa"
													size="50"
													onkeyup="javascript:validacaoNumeroListaRemessa(this);"
													value="#{beanListaRemessa.remessa.lacre}"/>
								</td>
							</tr>
						</table>
					</h:panelGroup>

					<table>
						<tr>
							<td><h:outputText value="Lista(s) de Remessa " /></td>
							<td><t:inputText id="idListaRemessaListaRemessa"
												size="60"
												value="#{beanListaRemessa.listasRemessa}" /></td>
						</tr>
						<tr>
							<td><h:outputText value="#{beanListaRemessa.labelGuiaDeslocamento} " /></td>
							<td><t:inputText id="idGuiaDeslocamentoListaRemessa"
												size="50"
												onkeyup="javascript:validacaoAlfaListaRemessa(this);"
												value="#{beanListaRemessa.remessa.guiaDeslocamento}" />
							</td>
						</tr>
						<tr>
							<td valign="middle"><h:outputText value="Observação " /></td>
							<td><h:inputTextarea id="idObservacaoListaRemessa"
													style="width:600px"
													onkeyup="javascript:validacaoAlfaListaRemessa(this);"
													value="#{beanListaRemessa.remessa.observacao }" /></td>
						 </tr>
					</table>
					<br>
					<br>
					<br>
					<a4j:commandButton id="btnAdicionar"
										styleClass="BotaoPadrao"
										style="margin-left:15px;"
										value="Adicionar"
										ignoreDupResponses="true"
										reRender="panelListaRemessa"
										oncomplete="javascript:setFoco('idDestinatarioListaRemessa');"
										actionListener="#{beanListaRemessa.adicionar}">
						<f:setPropertyActionListener value="" target="#{beanDestinatario.campoPesquisa}" />
					</a4j:commandButton>
					<a4j:commandButton id="idBtnLimpar"
										styleClass="BotaoPadrao"
										style="margin-left:15px;"
										value="Limpar"
										reRender="panelListaRemessa"
										actionListener="#{beanListaRemessa.limparTelaParaNovo}">
						<f:setPropertyActionListener value="" target="#{beanDestinatario.campoPesquisa}" />
					</a4j:commandButton>
					<h:panelGroup rendered="#{beanDestinatario.flagExibirModalPesquisa}">
						<script type="text/javascript">
							Richfaces.showModalPanel('idPnlModalPesquisaDestinatario')
						</script>
					</h:panelGroup>
				</rich:panel>
				<br>
				<br>
				<br>
				<rich:dataTable id="idTabelaRemessas"
								border="1"
								var="objRemessa"
								value="#{beanListaRemessa.remessas}"
								binding="#{beanListaRemessa.remessaAManipular}">					
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
						<h:outputText value="#{objRemessa.numeroComunicacao}" />
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
							<h:outputText value="#{lista.numeroListaRemessaAnoFormato}"/>							
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
					<rich:column style="text-align: center;">
						<f:facet name="header">
							<h:outputText value="Imprimir" />
						</f:facet>
					</rich:column>
					<rich:column style="text-align: center;">
						<f:facet name="header">
							<h:outputText value="Excluir" />
						</f:facet>
						<a4j:commandLink reRender="formCadastroListaRemessa" actionListener="#{beanListaRemessa.excluirRemessa}">
							<h:graphicImage url="/images/deletecell.png" title="Excluir" />
						</a4j:commandLink>
					</rich:column>
				</rich:dataTable>

				<h:panelGroup id="idPanelBotoes">
					<rich:panel bodyClass="inpanelBody">
						<table>
							<tr>
								<td>
									<h:commandButton id="btnSalvarLista"
														styleClass="BotaoPadrao"
														style="margin-left:15px;"
														value="Salvar Lista"
														disabled="#{not beanListaRemessa.existeRemessa}"
														action="#{beanListaRemessa.salvarLista}">
										<a4j:support event="oncomplete" 
														reRender="idPanelBotoes,idPanelPosicaoInicial,outputTitulo,idTabelaRemessas"
														ignoreDupResponses="true"/>
										<f:setPropertyActionListener value="#{beanListaRemessa}" target="#{beanVisualizarListaRemessa.pesquisaListaRemessa}" />
									</h:commandButton>
								</td>
							</tr>
						</table>
					</rich:panel>
				</h:panelGroup>
			</h:panelGrid>
			<jsp:include page="/pages/template/footer.jsp" flush="true" />

		</h:form>

		<jsp:include page="/pages/consultaCep/consultaCep.jsp" />
		<jsp:include page="/pages/destinatariolistaremetente/destinatarioPesquisaModal.jsp" />
	</a4j:page>
</f:view>