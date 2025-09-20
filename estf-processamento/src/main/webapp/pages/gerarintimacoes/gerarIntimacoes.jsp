<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<script type="text/javascript">
	
	function validarCampos(){
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

</script>

<f:view>

	<a4j:page pageTitle="::.. Principal ..::">

		<h:form id="form" prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Gerar documentos" />
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
							<h:panelGrid columns="2" id="pnlPesquisa">

								<h:outputText value="Data:" />
								<t:inputCalendar
									value="#{beanPartesGerarIntimacao.dataPublicacao}"
									renderAsPopup="true" popupWeekString="Sem"
									popupTodayString="Hoje: "
									popupTodayDateFormat="EEEE, dd/MM/yyyy"
									popupDateFormat="dd/MM/yyyy"
									onkeydown="return pesquisar(\"btnPesquisarPartes\",event)"
									onkeypress="return mascaraInputData(this, event)"
									onchange="validarData(this);" forceId="true">
									<f:converter converterId="dataConverter" />
								</t:inputCalendar>


							</h:panelGrid>
						</h:panelGrid>

						<a4j:commandButton styleClass="BotaoPesquisar" value="Pesquisar"
							id="btnPesquisarPartes" ignoreDupResponses="true"
							reRender="pnlPrincipalResultado,pnlInput,gerarIntimacoes,tableResultado,labelResponsavel,responsavel,labelCargo,cargoResponsavel"
							onclick="exibirMsgProcessando(true)"
							oncomplete="exibirMsgProcessando(false);"
							action="#{beanPartesGerarIntimacao.pesquisarPartes}" />
					</a4j:outputPanel>

					<br />
					<br />
					<t:saveState value="#{beanPartesGerarIntimacao.partes}"></t:saveState>
					<a4j:outputPanel id="pnlPrincipalResultado">
						<h:panelGrid styleClass="PadraoTituloPanel" id="pnlResultadoTotal">
							<h:outputText value="Lista Partes" />
						</h:panelGrid>

						<div style="height: 250px; overflow: auto;">
							<h:dataTable headerClass="DataTableDefaultHeader"  rendered="#{!empty beanPartesGerarIntimacao.partes}" id="tableResultado" 
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="dois, trintaLeft"
								binding="#{beanPartesGerarIntimacao.tabelaPartes}"
								value="#{beanPartesGerarIntimacao.listaPartes}" var="partes">
								<h:column id="columnCheck">
									<f:facet name="header">
									</f:facet>
									<h:selectBooleanCheckbox
										value="#{partes.wrappedObject.selected}" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText value="Cod Parte" />
									</f:facet>
									<h:outputText value="#{partes.wrappedObject.seqParteProcessual}" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText value="Partes" />
									</f:facet>
									<h:outputText value="#{partes.wrappedObject.nomeParte}" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText value="Processo" />
									</f:facet>
									<h:outputText value="#{partes.wrappedObject.numeroProcesso}" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText value="Documento" />
									</f:facet>
									<h:outputLink
										value="#{beanPartesGerarIntimacao.documentoDownloadURL}">
										<h:graphicImage value="/images/pdf.png"></h:graphicImage>
									</h:outputLink>
								</h:column>
							</h:dataTable>
						</div>
					</a4j:outputPanel>

					<h:panelGrid id="pnlInput" >
						<div style="padding-top: 8px;">
							
							<h:outputLabel value="Responsável* :" styleClass="Padrao" id="labelResponsavel"  rendered="#{!empty beanPartesGerarIntimacao.partes}"/>
							<span class="Padrao" style="margin-left: 10px">
								<h:inputText id="responsavel" rendered="#{!empty beanPartesGerarIntimacao.partes}"
									value="#{beanPartesGerarIntimacao.responsavelAssinatura}"
									size="60" />
							</span>
							
							<h:outputLabel value="Cargo* :" styleClass="Padrao" id="labelCargo"  rendered="#{!empty beanPartesGerarIntimacao.partes}"/>
							<span class="Padrao" style="margin-left: 10px"> <h:inputText
									id="cargoResponsavel"
									value="#{beanPartesGerarIntimacao.cargoResponsavelAssinatura}"  rendered="#{!empty beanPartesGerarIntimacao.partes}"
									size="60" />
							</span>
						</div>
						<br />
						<br />
						<h:panelGrid columns="3">
							<a4j:commandButton styleClass="BotaoSelecionarTodos" rendered="#{!empty beanPartesGerarIntimacao.partes}"
								value="Gerar Intimações" id="gerarIntimacoes"
								action="#{beanPartesGerarIntimacao.gerar}"
								ignoreDupResponses="true" reRender="pnlMessages"
								onclick="if(!validarCampos()){return;}else{exibirMsgProcessando(true);}"
								oncomplete="exibirMsgProcessando(false);"/>
						</h:panelGrid>
						
					</h:panelGrid>

				</h:panelGrid>
			</h:panelGrid>


			<jsp:include page="/pages/template/footer.jsp" flush="true" />

		</h:form>
	</a4j:page>
</f:view>