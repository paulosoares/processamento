<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<jsp:include page="/pages/template/header.jsp" flush="true" />


<script type="text/javascript">

	function aguarde(mostrar, div){
	      if( mostrar == true ){
	            document.getElementById(div).innerHTML = '<img src="/processamento/images/loading.gif" /><font class="Padrao">&nbsp;&nbsp;Aguarde...</font>';
	      }
	}

	function pesquisar () {
		document.getElementById('botaoPesquisar').click();
	}

	function caixaAlta (campo) {
		campo.value = campo.value.toUpperCase();
	}

</script>

<f:view>
	<a4j:page pageTitle="::.. Principal ..::"
		onload="document.getElementById('dscTipoTags').focus();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="TipodeTags" />
			</jsp:include>
		</h:form>
		
		<a4j:outputPanel id="pnlTela" >
			<a4j:form id="form">

				<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
					cellspacing="0" id="pnlPesquisa">

					<h:panelGrid columns="5" >
						<h:outputText styleClass="Padrao" value="Tipo de campo livre:" />
						<h:inputText id="dscTipoTags" size="100"
							value="#{beanAdministrarTipoTagsLivres.tipoTagsLivresUsuario.dscTipoTagLivres}"
							onkeyup="caixaAlta(this);" />

						<a4j:commandButton id="botaoPesquisar" value="Pesquisar"
							styleClass="BotaoPesquisar"
							actionListener="#{beanAdministrarTipoTagsLivres.pesquisarTipoTagsAction}"
							onclick="exibirMsgProcessando(true)"
							oncomplete="exibirMsgProcessando(false);"
							reRender="pnlTabelaTipoTags,pnlPesquisa" />
								
						<a4j:commandButton id="botaoLimpar" value="Limpar"
							styleClass="BotaoPadrao"
							actionListener="#{beanAdministrarTipoTagsLivres.limparTelaAction}"
							onclick="exibirMsgProcessando(true)"
							oncomplete="exibirMsgProcessando(false);"
							reRender="pnlPesquisa" />	
								
						<a4j:commandButton id="botaoSalvar" value="Salvar" styleClass="BotaoPadrao" 
							actionListener="#{beanAdministrarTipoTagsLivres.salvarTipoTagsLivresAction}"
							reRender="pnlTela"/>
								
					</h:panelGrid>

					<h:panelGrid styleClass="MolduraInterna" id="pnlTabelaTipoTags"
						rendered="#{not empty beanAdministrarTipoTagsLivres.listaTipoTags}">
							<h:dataTable headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="tres, quarentacinco, dez"
								value="#{beanAdministrarTipoTagsLivres.listaTipoTags}" var="wrappedTipo"
								binding="#{beanAdministrarTipoTagsLivres.tabelaTipoTags}">
								<h:column>
									<f:facet name="header">
										<a4j:commandButton image="../../images/setabaixo.gif"
											onclick="exibirMsgProcessando(true)"
											oncomplete="exibirMsgProcessando(false);"
											actionListener="#{beanAdministrarTipoTagsLivres.marcarTodosTextos}" reRender="form" />
									</f:facet>
									<h:selectBooleanCheckbox onclick="document.getElementById('BotaoAtualizarMarcacao').click();" 
										value="#{wrappedTipo.checked}" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText value="Descrição" styleClass="DataTableDocumentoTexto"/>
									</f:facet>
									<h:outputText styleClass="DataTableDocumentoTexto" style="text-align:center;"
										value="#{wrappedTipo.wrappedObject.dscTipoTagLivres}" />
								</h:column>
								
								<h:column>
									<a4j:commandButton value="Alterar" styleClass="PadraoLink"
										actionListener="#{beanAdministrarTipoTagsLivres.alterarTipoTagsLivresAction}"
									 	reRender="pnlPesquisa"/>
								</h:column>
							</h:dataTable>
							
							<a4j:commandButton styleClass="BotaoOculto"
								id="BotaoAtualizarMarcacao"
								actionListener="#{beanAdministrarTipoTagsLivres.atualizarMarcacao}"
								reRender="pnlTela" />
								
					</h:panelGrid>
				</h:panelGrid>	
			</a4j:form>
		</a4j:outputPanel>
	
	<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>		