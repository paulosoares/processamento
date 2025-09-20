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
		onload="document.getElementById('tipoDescricaoModelo').focus();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="TipoModelos" />
			</jsp:include>
		</h:form>
		
		<a4j:outputPanel id="pnlTela" >
			<a4j:form id="form">

				<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
					cellspacing="0" id="pnlPesquisa">

					<h:panelGrid columns="5" >
						<h:outputText styleClass="Padrao" value="Campo Livre:" />
						<h:inputText id="descricaoTagsLivres" size="100"
							value="#{beanAdministrarTagLivres.tagsLivresUsuario.nomeRotulo}"
							onblur="retirarAcento(this);" />

						<a4j:commandButton id="botaoPesquisar" value="Pesquisar"
							styleClass="BotaoPesquisar"
							actionListener="#{beanAdministrarTagLivres.pesquisarTagsLivresAction}"
							onclick="exibirMsgProcessando(true)"
							oncomplete="exibirMsgProcessando(false);"
							reRender="pnlTabelaTagsLivres,pnlPesquisa" />
								
						<a4j:commandButton id="botaoLimpar" value="Limpar"
							styleClass="BotaoPadrao"
							actionListener="#{beanAdministrarTagLivres.limparTela}"
							onclick="exibirMsgProcessando(true)"
							oncomplete="exibirMsgProcessando(false);"
							reRender="pnlPesquisa" />	
								
						<a4j:commandButton id="botaoSalvar" value="Salvar" styleClass="BotaoPadrao" 
							actionListener="#{beanAdministrarTagLivres.criarNovoTagsLivresAction}"
							reRender="pnlTela"/>
								
					</h:panelGrid>

					<h:panelGrid styleClass="MolduraInterna" id="pnlTabelaTagsLivres"
						rendered="#{not empty beanAdministrarTagLivres.listaTagsLivres}">
							<h:dataTable headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="tres,trintaCenter, trintaCenter, quinze"
								value="#{beanAdministrarTagLivres.listaTagsLivres}" var="wrappedTipo"
								binding="#{beanAdministrarTagLivres.tabelaTagsLivres}">
								<h:column>
									<f:facet name="header">
										<a4j:commandButton image="../../images/setabaixo.gif"
											onclick="exibirMsgProcessando(true)"
											oncomplete="exibirMsgProcessando(false);"
											actionListener="#{beanAdministrarTagLivres.marcarTodosTextos}" reRender="form" />
									</f:facet>
									<h:selectBooleanCheckbox  
										value="#{wrappedTipo.checked}" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText value="Descrição" styleClass="DataTableDocumentoTexto"/>
									</f:facet>
									<h:outputText styleClass="DataTableDocumentoTexto" style="text-align:center;"
										value="#{wrappedTipo.wrappedObject.nomeRotulo}" />
								</h:column>

								<h:column>
									<f:facet name="header">
										<h:outputText value="Código" styleClass="DataTableDocumentoTexto"/>
									</f:facet>
									<h:outputText styleClass="DataTableDocumentoTexto" style="text-align:center;"
										value="#{wrappedTipo.wrappedObject.codigoRotulo}" />
								</h:column>								
								
								<h:column>
									<a4j:commandButton value="Alterar" styleClass="PadraoLink"
										actionListener="#{beanAdministrarTagLivres.alterarTagsLivresAction}"
									 	reRender="pnlPesquisa"/>
								</h:column>
								
								<h:column>
									<f:facet name="header">
										<h:outputText value="Excluir" />
									</f:facet>
										<h:commandLink action="#{beanAdministrarTagLivres.removerTagsLivres}" >
											<h:graphicImage url="../../images/close.gif" title="Excluir" />													
										</h:commandLink>
								</h:column>
														
							</h:dataTable>
							
							<a4j:commandButton styleClass="BotaoOculto"
								id="BotaoAtualizarMarcacao"
								actionListener="#{beanAdministrarTagLivres.atualizarMarcacao}"
								reRender="pnlTela" />
								
					</h:panelGrid>

					<a4j:outputPanel id="pnlAlteraTagLivres" styleClass="MolduraInterna"
						ajaxRendered="true" keepTransient="false">
						<c:if test="${beanAdministrarTagLivres.renderedTelaAlteraTagLivres }">
							<hr color="red" align="left" size="1px" width="90%"/>
								<div class="PainelTituloCriaTexto">
									<span>
										Preencha os campos presentes no documento:
									</span>
								</div>
								<div style="margin-top: 7px">
									<span>
										<h:outputText styleClass="Padrao" value="Descrição:" ></h:outputText>
										<h:inputText value="#{beanAdministrarTagLivres.tagsLivresUsuario.dscTagLivres}" style="margin-left: 5px"
											onkeyup="caixaAlta(this);">
										</h:inputText>
									</span>
								</div>
								
								<div style="margin-top: 10px"> 
									<a4j:commandButton value="Alterar" styleClass="BotaoPadrao"
										actionListener="#{beanAdministrarTagLivres.salvaDadosAlteradosTagAction}">
									</a4j:commandButton>
									<a4j:commandButton styleClass="BotaoPadrao"
										action="#{beanAdministrarTagLivres.fecharTelaAction}"
										value="Fechar">
									</a4j:commandButton>
								</div>
							</c:if>
						</a4j:outputPanel>								
				</h:panelGrid>	
			</a4j:form>
		</a4j:outputPanel>
	
	<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>		