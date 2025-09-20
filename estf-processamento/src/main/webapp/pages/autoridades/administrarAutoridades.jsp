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
		onload="document.getElementById('dscAutoridade').focus();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="TipoModelos" />
			</jsp:include>
		</h:form>
		
		<a4j:outputPanel id="pnlTela" >
			<a4j:form id="form">
				<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
					cellspacing="0" id="pnlPesquisa">
					<h:panelGrid columns="6" >
						<h:outputText styleClass="Padrao" value="Autoridade:" />
						<h:inputText id="dscAutoridade" size="100"
							value="#{beanAdministrarAutoridade.tagsAutoridades.dscTagLivres}" />

						<a4j:commandButton id="botaoPesquisar" value="Pesquisar"
							styleClass="BotaoPesquisar"
							actionListener="#{beanAdministrarAutoridade.pesquisarAutoridadesAction}"
							onclick="exibirMsgProcessando(true)"
							oncomplete="exibirMsgProcessando(false);"
							reRender="pnlTabelaAutoridade,pnlPesquisa" />
								
						<a4j:commandButton id="botaoLimpar" value="Limpar"
							styleClass="BotaoPadrao"
							actionListener="#{beanAdministrarAutoridade.limparTelaAction}"
							onclick="exibirMsgProcessando(true)"
							oncomplete="exibirMsgProcessando(false);"
							reRender="pnlPesquisa" />	
								
						<a4j:commandButton id="botaoSalvar" value="Salvar" styleClass="BotaoPadrao" 
							actionListener="#{beanAdministrarAutoridade.abreTelaDeSalvarAction}"
							reRender="pnlTela" rendered="#{beanAdministrarAutoridade.renderedPermissaoSalvar}"
							/>
						
					</h:panelGrid>

					<h:panelGrid styleClass="MolduraInterna" id="pnlTabelaAutoridade"
						rendered="#{not empty beanAdministrarAutoridade.listaAutoridade}">
							<h:dataTable headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="tres, trintaCenter, trintaCenter, dez"
								value="#{beanAdministrarAutoridade.listaAutoridade}" var="wrappedAut"
								binding="#{beanAdministrarAutoridade.tabelaAutoridade}">
								<h:column>
									<f:facet name="header">
										<a4j:commandButton image="../../images/setabaixo.gif"
											onclick="exibirMsgProcessando(true)"
											oncomplete="exibirMsgProcessando(false);"
											actionListener="#{beanAdministrarAutoridade.marcarTodosTextos}" />
									</f:facet>
									<h:selectBooleanCheckbox onclick="document.getElementById('BotaoAtualizarMarcacao').click();" 
										value="#{wrappedAut.checked}" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText value="Descrição" styleClass="DataTableDocumentoTexto"/>
									</f:facet>
									<h:outputText styleClass="DataTableDocumentoTexto" style="text-align:center;"
										value="#{wrappedAut.wrappedObject.dscTagLivres}" />
								</h:column>						
								<h:column>
									<f:facet name="header">
										<h:outputText value="Código" styleClass="DataTableDocumentoTexto"/>
									</f:facet>
									<h:outputText styleClass="DataTableDocumentoTexto" style="text-align:center;"
										value="#{wrappedAut.wrappedObject.nomeRotulo}" />
								</h:column>									
								<h:column>
									<a4j:commandButton value="Alterar" styleClass="PadraoLink"
										actionListener="#{beanAdministrarAutoridade.mostraDadosParaAlterarAutoridadeAction}"
									 	reRender="pnlTela"/>
								</h:column>
							</h:dataTable>
						</h:panelGrid>
						<a4j:outputPanel id="pnlTelaAlteraAutoridade" styleClass="MolduraInterna"
							ajaxRendered="true" keepTransient="false">
							<c:if test="${beanAdministrarAutoridade.renderedTelaAlterar}">
								<hr color="red" align="left" size="1px" width="90%"/>
									<div class="PainelTituloCriaTexto">
										<span>
											Preencha os campos presentes:
										</span>
									</div>
									<div style="margin-top: 7px">
										<span>
											<h:outputText styleClass="Padrao" value="Descrição:" ></h:outputText>
											<h:inputText value="#{beanAdministrarAutoridade.tagsAutoridades.dscTagLivres}" style="margin-left: 35px"
												size="100"/>
										</span>
									</div>								
									
									<div style="margin-top: 10px"> 
										<a4j:commandButton id="botaoAlterar" value="Alterar" styleClass="BotaoPadrao" 
											actionListener="#{beanAdministrarAutoridade.alterarAutoridadeAction}"
											reRender="pnlTela"/>	
										<a4j:commandButton styleClass="BotaoPadrao"
											actionListener="#{beanAdministrarAutoridade.fecharTelaAction}"
											value="Fechar" reRender="pnlTela">
										</a4j:commandButton>
									</div>
								</c:if>
						</a4j:outputPanel>															
						<a4j:outputPanel id="pnlSalvaAutoridades" styleClass="MolduraInterna"
							ajaxRendered="true" keepTransient="false">
							<c:if test="${beanAdministrarAutoridade.renderedTelaSalvaAutoridade }">
								<hr color="red" align="left" size="1px" width="90%"/>
									<div class="PainelTituloCriaTexto">
										<span>
											Preencha os campos presentes:
										</span>
									</div>
									<div style="margin-top: 7px">
										<span>
											<h:outputText styleClass="Padrao" value="Descrição:" ></h:outputText>
											<h:inputText value="#{beanAdministrarAutoridade.tagsAutoridades.dscTagLivres}" style="margin-left: 35px"
												size="100"/>
										</span>
									</div>
									<div style="margin-top: 7px">
										<span>
											<h:outputText styleClass="Padrao" value="Texto do rótulo:" ></h:outputText>
											<h:inputText value="#{beanAdministrarAutoridade.tagsAutoridades.nomeRotulo}" style="margin-left: 5px"
												size="100" onkeyup="caixaAlta(this);">
											</h:inputText>
										</span>
									</div>									
									
									<div style="margin-top: 10px"> 
										<a4j:commandButton value="Gravar" styleClass="BotaoPadrao"
											actionListener="#{beanAdministrarAutoridade.salvarAutoridadeAction}"
											reRender="pnlTela">
										</a4j:commandButton>
										<a4j:commandButton value="Fechar" styleClass="BotaoPadrao"
											actionListener="#{beanAdministrarAutoridade.fecharTelaAction}"
											 reRender="pnlPesquisa">
										</a4j:commandButton>
									</div>
								</c:if>
						</a4j:outputPanel>
					<a4j:commandButton styleClass="BotaoOculto"
						id="BotaoAtualizarMarcacao"
						actionListener="#{beanAdministrarAutoridade.atualizarMarcacao}"
						reRender="pnlTela" />
				</h:panelGrid>	
			</a4j:form>
		</a4j:outputPanel>
	
	<jsp:include page="/pages/template/footer.jsp" flush="true" />

	</a4j:page>
</f:view>		