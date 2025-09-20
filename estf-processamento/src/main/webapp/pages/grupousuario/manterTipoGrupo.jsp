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

	function preencheValoresQuery(){
		$('textarea#textoQuery').click(
			function($e){
				$(this).text('WHERE');
			}
		);
	}

</script>

<f:view>
	<a4j:page pageTitle="::.. TipoGrupoo ..::"
		onload="document.getElementById('descricaoTipoGrupo').focus();">

		<h:form prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="TipoGrupo" />
			</jsp:include>
		</h:form>
		
		<a4j:outputPanel id="pnlTela" >
			<a4j:form id="form">

				<h:panelGrid styleClass="MolduraExterna" cellpadding="0"
					cellspacing="0" id="pnlPesquisa">

					<h:panelGrid columns="5" >
						<h:outputText styleClass="Padrao" value="Nome do grupo:" />
						<h:inputText id="descricaoTipoGrupo" size="100"
							value="#{beanManterTipoGrupo.descricaoDoGrupo}" />

						<a4j:commandButton id="botaoPesquisar" value="Pesquisar"
							styleClass="BotaoPesquisar"
							actionListener="#{beanManterTipoGrupo.pesquisarTipoGrupoControleAction}"
							onclick="exibirMsgProcessando(true)"
							oncomplete="exibirMsgProcessando(false);"
							reRender="pnlPesquisa" />
								
						<a4j:commandButton id="botaoLimpar" value="Limpar"
							styleClass="BotaoPadrao"
							actionListener="#{beanManterTipoGrupo.limparTelaAction}"
							onclick="exibirMsgProcessando(true)"
							oncomplete="exibirMsgProcessando(false);"
							reRender="pnlPesquisa" />	
								
						<a4j:commandButton id="botaoSalvar" value="Novo" styleClass="BotaoPadrao" 
							actionListener="#{beanManterTipoGrupo.renderizaTelaNovoGrupoAction}"
							reRender="pnlTela"/>
								
					</h:panelGrid>

					<h:panelGrid styleClass="MolduraInterna" id="pnlTabelaTipoGrupoControle"
						rendered="#{not empty beanManterTipoGrupo.listaTipoGrupoControle}">
							<h:dataTable headerClass="DataTableDefaultHeader"
								styleClass="DataTableDefault"
								footerClass="DataTableDefaultFooter"
								rowClasses="DataTableRow, DataTableRow2"
								columnClasses="tres,quarentaCenter, cinco, cinco"
								value="#{beanManterTipoGrupo.listaTipoGrupoControle}" var="wrappedTipo"
								binding="#{beanManterTipoGrupo.tabelaTipoGrupoControle}">
								<h:column>
									<f:facet name="header">
										<a4j:commandButton image="../../images/setabaixo.gif"
											onclick="exibirMsgProcessando(true)"
											oncomplete="exibirMsgProcessando(false);"
											actionListener="#{beanManterTipoGrupo.marcarTodosTextos}" />
									</f:facet>
									<h:selectBooleanCheckbox onclick="document.getElementById('BotaoAtualizarMarcacao').click();" 
										value="#{wrappedTipo.checked}" />
								</h:column>
								
								<h:column>
									<f:facet name="header">
										<h:outputText value="Descrição" styleClass="DataTableDocumentoTexto"/>
									</f:facet>
									<h:outputText styleClass="DataTableDocumentoTexto" style="text-align:center;"
										value="#{wrappedTipo.wrappedObject.dscTipoGrupoControle}" />
								</h:column>							
														
							</h:dataTable>
							
							<a4j:commandButton styleClass="BotaoOculto"
								id="BotaoAtualizarMarcacao"
								actionListener="#{beanManterTipoGrupo.atualizarMarcacao}"
								reRender="pnlTela" />
								
					</h:panelGrid>

					<a4j:outputPanel id="pnlAlteraTipoGrupoControle" styleClass="MolduraInterna"
						ajaxRendered="true" keepTransient="false">
						<c:if test="${beanManterTipoGrupo.renderedTelaNovoGrupoControle}">
							<hr color="red" align="left" size="1px" width="90%"/>
								<div class="PainelTituloCriaTexto">
									<span>
										Preencha os campos presentes na tela:
									</span>
								</div>
								<div style="margin-top: 7px">
									<span>
										<h:outputText styleClass="Padrao" value="Nome:" ></h:outputText>
										<h:inputText value="#{beanManterTipoGrupo.dscTipoGrupoCad}" style="margin-left: 90px"
											size="101" onkeyup="caixaAlta(this);">
										</h:inputText>
									</span>	
								</div>
								<div style="margin-top: 7px">
									<div>
									<span style="text-align: top; float: left;">
										<h:outputText styleClass="Padrao" value="Complemento Query:" ></h:outputText>
									</span>
									<span>
										<h:inputTextarea value="#{beanManterTipoGrupo.dscConsultaComplementoCad}" id="textoQuery" 
											cols="100" rows="5" style="margin-left: 5px"/>
									</span>
									
									</div>
								</div>
								
								<div style="margin-top: 10px"> 
									<a4j:commandButton value="Alterar" styleClass="BotaoPadrao" rendered="#{beanManterTipoGrupo.renderedBotaoAlteraTipoGrupoControle}"
										actionListener="#{beanManterTipoGrupo.salvaDadosAlteradosTipoGrupoControleAction}">
									</a4j:commandButton>
									<a4j:commandButton value="Salvar" styleClass="BotaoPadrao" rendered="#{!beanManterTipoGrupo.renderedBotaoAlteraTipoGrupoControle}"
										actionListener="#{beanManterTipoGrupo.salvaNovoTipoGrupoControleAction}">
									</a4j:commandButton>
									<a4j:commandButton styleClass="BotaoPadrao" reRender="pnlPesquisa"
										actionListener="#{beanManterTipoGrupo.fecharTelaAction}"
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