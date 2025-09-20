<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />

<script type="text/javascript">
	function pesquisarRecursos() {
		document.getElementById('botaoCarregarComboRecursos').click();
	}
	function aguarde(mostrar, div) {
		if (mostrar == true) {
			document.getElementById(div).innerHTML = '<img src="/processamento/images/loading.gif" /><font class="Padrao">&nbsp;&nbsp;Aguarde...</font>';
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
	<a4j:page pageTitle="::.. Principal ..::"
			onload="pesquisar();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Criar Tipo Modelos" />
			</jsp:include>
		</h:form>

		<a4j:outputPanel id="pnlTela" ajaxRendered="true">
			<a4j:form id="form" prependId="false">

				<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
					cellspacing="0" id="pnlPesquisa">
					<h:panelGrid columns="1">
						<a4j:commandButton id="botaoCriarNovo" value="Criar novo"
							styleClass="BotaoPadrao"
							actionListener="#{beanAdministrarTiposModelos.criarNovoTipoModelosAction}"
							oncomplete="exibirMsgProcessando(false); Richfaces.showModalPanel('modalPanelManterTipoDocumento');"
							reRender="modalPanelManterTipoDocumento" />
					</h:panelGrid>

					<h:panelGrid styleClass="MolduraInterna"
						id="pnlTabelaTipoDocumento"
						rendered="#{not empty beanAdministrarTiposModelos.listaTipoComunicacao}">
							<rich:dataTable
								 headerClass="DataTableDefaultHeader"
								 id="tableModelos" styleClass="DataTableDefault"
								 footerClass="DataTableDefaultFooter"
								 rowClasses="DataTableRow, DataTableRow2"
								 columnClasses="vinteLeft, dezCenter, vinte, cinco, tres"
								 value="#{beanAdministrarTiposModelos.listaTipoComunicacao}"
								 var="wrappedTipo"
								 binding="#{beanAdministrarTiposModelos.tabelaTipoComunicacao}">
							
							<rich:column sortBy="#{wrappedTipo.wrappedObject.descricao}">	
								<f:facet name="header">
									<h:outputText value="Descrição"
										styleClass="DataTableDocumentoTexto" />
								</f:facet>
								<h:outputText styleClass="DataTableDocumentoTexto"
									style="text-align:center;"
									value="#{wrappedTipo.wrappedObject.descricao}" />
							</rich:column>
							
							<rich:column>	
								<f:facet name="header">
									<h:outputText value="Número da Comunicação Anterior"
										styleClass="DataTableDocumentoTexto" />
								</f:facet>
								<h:outputText styleClass="DataTableDocumentoTexto"
									style="text-align:center;"
									value="#{wrappedTipo.wrappedObject.numeroComunicacaoAnterior}" />
							</rich:column>

							<rich:column>
								<a4j:commandButton value="Alterar" styleClass="PadraoLink"
									actionListener="#{beanAdministrarTiposModelos.alterarTipoModeloAction}"
									oncomplete="Richfaces.showModalPanel('modalPanelManterTipoDocumento');"
									reRender="modalPanelManterTipoDocumento" />
							</rich:column>

							<rich:column>
								<f:facet name="header">
									<h:outputText value="Excluir" />
								</f:facet>
								<a4j:commandLink
									action="#{beanAdministrarTiposModelos.removerTipoModelo}"
									reRender="pnlTabelaTipoDocumento">
									<h:graphicImage url="../../images/close.gif" title="Excluir" />
								</a4j:commandLink>
							</rich:column>
							</rich:dataTable>
					</h:panelGrid>
				</h:panelGrid>
				
				<a4j:commandButton id="botaoPesquisar" value="Pesquisar"
					styleClass="BotaoOculto"
					actionListener="#{beanAdministrarTiposModelos.pesquisarTipoModelosAction}"
					onclick="exibirMsgProcessando(true)"
					oncomplete="exibirMsgProcessando(false);"
					reRender="pnlTabelaTipoDocumento,pnlPesquisa" />
			</a4j:form>
		</a4j:outputPanel>
		
		<rich:modalPanel id="modalPanelManterTipoDocumento" width="630" 
			height="150" keepVisualState="true">
			<f:facet name="header">
				<h:outputText value="Dados tipo do modelo" />
			</f:facet>
			<a4j:form prependId="false">
				<div>
					<span class="Padrao"> 
						<h:outputText styleClass="Padrao" value="Tipo de Modelo:" />
						<h:inputText style="margin-left: 10px;" size="91"
							id="tipoDescricaoModelo"
							value="#{beanAdministrarTiposModelos.tipocomunicacao.descricao}" />
					</span>
				</div>
				<div>
					<span class="Padrao"> 
						<h:outputText styleClass="Padrao" value="Número da Comunicação Anterior:" />
						<h:inputText style="margin-left: 10px;" size="70"
							id="numeroComAnterior"
							value="#{beanAdministrarTiposModelos.tipocomunicacao.numeroComunicacaoAnterior}" />
					</span>
				</div>

				<div style="padding-top: 20px;">
					<span> 
						<a4j:commandButton styleClass="BotaoPadrao" rendered="#{beanAdministrarTiposModelos.renderedBotaoSalvar}"
							actionListener="#{beanAdministrarTiposModelos.persistirTipoTextoAction}"
							onclick="Richfaces.hideModalPanel('modalPanelManterTipoDocumento');"
							value="Salvar" />
						<a4j:commandButton styleClass="BotaoPadrao" rendered="#{beanAdministrarTiposModelos.renderedBotaoAlterar}"
							actionListener="#{beanAdministrarTiposModelos.persistirTipoTextoAction}"
							onclick="Richfaces.hideModalPanel('modalPanelManterTipoDocumento');"
							value="Alterar" />  
					</span> 
					<span> 
						<h:commandButton
							onclick="Richfaces.hideModalPanel('modalPanelManterTipoDocumento');"
							styleClass="BotaoPadrao" value="Fechar" /> 
					</span>
				</div>
			</a4j:form>
		</rich:modalPanel>

		<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>
