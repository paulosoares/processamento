<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<a4j:outputPanel id="principalPesquisa" ajaxRendered="true">
	<h:panelGrid id="pnlFiltroConteudoDestinatario"
		styleClass="PadraoTituloPanel" cellpadding="0" cellspacing="0">
		<h:outputText value="#{beanDestinatario.titulo}" />
	</h:panelGrid>

	<!-- Pesquisa Simples -->
	<h:panelGrid rendered="#{beanDestinatario.pesquisaSimples}">
		<jsp:include
			page="/pages/destinatariolistaremetente/destinatarioPesquisaSimples.jsp" />
	</h:panelGrid>

	<!-- Pesquisa Avançada -->
	<h:panelGrid rendered="#{not beanDestinatario.pesquisaSimples}">
		<jsp:include
			page="/pages/destinatariolistaremetente/destinatarioPesquisaAvancada.jsp" />
	</h:panelGrid>

	<h:panelGrid styleClass="Moldura">
		<h:panelGrid columns="4">
			<a4j:commandButton id="btnPesquisaSimplesDestinatario"
				styleClass="BotaoPesquisar" value="Pesquisar"
				actionListener="#{beanDestinatario.executarPesquisaSimplesDestinatario}"
				rendered="#{beanDestinatario.pesquisaSimples}" />
			<a4j:commandButton id="btnPesquisaAvancadaDestinatario"
				styleClass="BotaoPesquisar" value="Pesquisar"
				actionListener="#{beanDestinatario.executarPesquisaAvancadaDestinatario}"
				rendered="#{not beanDestinatario.pesquisaSimples}" />

			<a4j:commandButton id="btnLimparPesquisarDestinatario"
				styleClass="BotaoPadrao" value="Limpar"
				actionListener="#{beanDestinatario.limparBean}" />

			<a4j:commandButton id="btnCadastrarDestinatario"
				styleClass="BotaoPadrao" value="Criar Novo"
				reRender="idPnlDestinatarioCadastro"
				oncomplete="Richfaces.showModalPanel('idPnlDestinatarioCadastro')"
				rendered="#{not beanDestinatario.flagModoPesquisaDialogo && beanDestinatario.usuarioComAcessoAlteracaoDados}"
				actionListener="#{beanDestinatario.inicializaCadastro}" />

			<a4j:commandButton id="btnPesquisaAvancada"
				styleClass="BotaoPadraoEstendido" value="Pesquisa Avançada"
				reRender="principalPesquisa"
				rendered="#{beanDestinatario.pesquisaSimples}"
				actionListener="#{beanDestinatario.inicializaPesquisaAvancada}">
				<a4j:actionparam assignTo="#{beanDestinatario.pesquisaSimples}"
					value="false" />
			</a4j:commandButton>
			<a4j:commandButton id="btnPesquisaSimples"
				styleClass="BotaoPadraoEstendido" value="Pesquisa Simples"
				reRender="principalPesquisa"
				rendered="#{not beanDestinatario.pesquisaSimples}">
				<a4j:actionparam assignTo="#{beanDestinatario.pesquisaSimples}"
					value="true" />
			</a4j:commandButton>
		</h:panelGrid>
	</h:panelGrid>
</a4j:outputPanel>

<a4j:outputPanel id="pnlPrincipalResultado" ajaxRendered="true">
	<h:commandButton actionListener="#{beanDestinatario.relatorioExcel}"
		styleClass="GerarXLS"
		style="float: right; margin-right: 20px; width: 260px;"
		value="EXPORTAR PLANILHA PARA O EXCEL"
		rendered="#{beanDestinatario.mostrarPesquisa && beanDestinatario.exibirBotaoExportarExcel}" />

	<br>
	<br>
	<h:panelGrid styleClass="PadraoTituloPanel" id="pnlResultadoTotal"
		rendered="#{beanDestinatario.mostrarPesquisa}">
		<h:outputText
			value="Resultado da Pesquisa - Quant. de Registros: #{beanDestinatario.qtdRegistros}" />
	</h:panelGrid>

	<rich:datascroller align="left" for="tbPesquisaDestinatario"
		maxPages="100" reRender="sc2" id="sc1"
		rendered="#{beanDestinatario.mostrarPesquisa}" />

	<rich:dataTable id="tbPesquisaDestinatario"
		value="#{beanDestinatario.listaDestinatarioListaRemessa}"
		var="pesquisa" columnClasses="center"
		rowClasses="linha-par, linha-impar" rows="50" reRender="ds"
		rendered="#{beanDestinatario.mostrarPesquisa}" >
       
		<rich:column sortBy="#{pesquisa.descricaoAnterior}" width="50px"  title="Observação: #{pesquisa.observacao}">
			<f:facet name="header">
				<h:outputText value="Detalhe Pré" />
			</f:facet>
			<h:outputText value="#{pesquisa.descricaoAnterior}" />
		</rich:column>

		<rich:column sortBy="#{pesquisa.descricaoPrincipal}" width="200px" title="Observação: #{pesquisa.observacao}">
			<f:facet name="header">
				<h:outputText value="Destinatario" />
			</f:facet>
			<h:outputText value="#{pesquisa.descricaoPrincipal}" />
			
		</rich:column>

		<rich:column sortBy="#{pesquisa.descricaoPosterior}" width="50px"  title="Observação: #{pesquisa.observacao}">
			<f:facet name="header">
				<h:outputText value="Detalhe Pós" />
			</f:facet>
			<h:outputText value="#{pesquisa.descricaoPosterior}" />
			
		</rich:column>

		<rich:column sortBy="#{pesquisa.municipio.siglaUf}" 
			style="text-align: center;" width="10px"  title="Observação: #{pesquisa.observacao}">
			<f:facet name="header">
				<h:outputText value="UF" />
			</f:facet>
			<h:outputText value="#{pesquisa.municipio.siglaUf}" />
			
		</rich:column>

		<rich:column sortBy="#{pesquisa.municipio.nome}" width="35px"  title="Observação: #{pesquisa.observacao}">
			<f:facet name="header">
				<h:outputText value="Cidade" />
			</f:facet>
			<h:outputText value="#{pesquisa.municipio.nome}" />
			
		</rich:column>

		<rich:column sortBy="#{pesquisa.logradouro}" width="300px"  title="Observação: #{pesquisa.observacao}">
			<f:facet name="header">
				<h:outputText value="Logadouro" />
			</f:facet>
			<h:outputText value="#{pesquisa.logradouro}" />
			
		</rich:column>
        
		<rich:column sortBy="#{pesquisa.cep}" width="25px"  title="Observação: #{pesquisa.observacao}"
			style="text-align: center;">
			<f:facet name="header">
				<h:outputText value="CEP" />
			</f:facet>
			<h:outputText value="#{pesquisa.cep}" />
			
		</rich:column>

		<rich:column sortBy="#{pesquisa.agrupador}" width="25px"  title="Observação: #{pesquisa.observacao}">
			<f:facet name="header">
				<h:outputText value="Agrupador" />
			</f:facet>
			<h:outputText value="#{pesquisa.agrupador}" />
			
		</rich:column>

		<rich:column sortBy="#{pesquisa.codigoOrigem}"  title="Observação: #{pesquisa.observacao}"
			style="text-align: center;" width="10px">
			<f:facet name="header">
				<h:outputText value="Cod. Órgão" />
			</f:facet>
			<h:outputText value="#{pesquisa.codigoOrigem}" />
			
		</rich:column>

		<rich:column style="text-align: center;" width="5px"
			rendered="#{not beanDestinatario.flagModoPesquisaDialogo}">
			<f:facet name="header">
				<h:outputText value="Visualizar" />
			</f:facet>
			<a4j:commandLink
				actionListener="#{beanDestinatario.abrirVisualizacao}"
				reRender="idPnlDestinatarioCadastro"
				oncomplete="Richfaces.showModalPanel('idPnlDestinatarioCadastro')">
				<h:graphicImage value="/images/icobs.png" title="Visualizar" />
				<f:setPropertyActionListener value="#{pesquisa}"
					target="#{beanDestinatario.bean}" />
			</a4j:commandLink>
		</rich:column>

		<rich:column style="text-align: center;" width="5px"
			rendered="#{not beanDestinatario.flagModoPesquisaDialogo}">
			<f:facet name="header">
				<h:outputText value="Copiar" />
			</f:facet>
			<a4j:commandLink actionListener="#{beanDestinatario.copiar}"
				disabled="#{not beanDestinatario.usuarioComAcessoAlteracaoDados}"
				reRender="idPnlDestinatarioCadastro"
				oncomplete="Richfaces.showModalPanel('idPnlDestinatarioCadastro')">
				<h:graphicImage value="/images/copiar.png" title="Copiar" />
				<f:param name="identificadorDestinatario" value="#{pesquisa.id}" />
				<f:setPropertyActionListener value="#{true}"
					target="#{beanDestinatario.copia}" />
			</a4j:commandLink>
		</rich:column>

		<rich:column style="text-align: center;" width="5px"
			rendered="#{not beanDestinatario.flagModoPesquisaDialogo}">
			<f:facet name="header">
				<h:outputText value="Alterar" />
			</f:facet>
			<a4j:commandLink actionListener="#{beanDestinatario.copiar}"
				disabled="#{not beanDestinatario.usuarioComAcessoAlteracaoDados}"
				reRender="idPnlDestinatarioCadastro"
				oncomplete="Richfaces.showModalPanel('idPnlDestinatarioCadastro')">
				<h:graphicImage value="/images/editar_item.png" title="Alterar" />
				<f:param name="identificadorDestinatario" value="#{pesquisa.id}" />
				<f:setPropertyActionListener value="#{false}"
					target="#{beanDestinatario.copia}" />
				<f:setPropertyActionListener value="#{pesquisa}"
					target="#{beanDestinatario.bean}" />
			</a4j:commandLink>
		</rich:column>

		<rich:column style="text-align: center;" width="5px"
			rendered="#{not beanDestinatario.flagModoPesquisaDialogo}">
			<f:facet name="header">
				<h:outputText value="Excluir" />
			</f:facet>
			<a4j:commandLink reRender="idPanelExcluir"
				oncomplete="Richfaces.showModalPanel('idPanelExcluir')">
				<h:graphicImage url="/images/deletecell.png" title="Excluir" />
				<f:setPropertyActionListener value="#{pesquisa}"
					target="#{beanDestinatario.bean}" />
			</a4j:commandLink>
		</rich:column>

		<rich:column style="text-align: center;" width="5px"
			rendered="#{beanDestinatario.flagModoPesquisaDialogo}">
			<f:facet name="header">
				<h:outputText value="Selecionar" />
			</f:facet>
			<a4j:commandLink action="#{beanDestinatario.selecionar}"
				reRender="#{beanDestinatario.selecionaBeanDestinatario.nomeComponenteRerenderizar}"
				oncomplete="Richfaces.hideModalPanel('idPnlModalPesquisaDestinatario')">
				<h:graphicImage url="/images/cruz.png" title="Selecionar" />
				<f:setPropertyActionListener value="#{pesquisa}"
					target="#{beanDestinatario.destinatarioSelecionado}" />
			</a4j:commandLink>
		</rich:column>

		<f:facet name="footer">
			<rich:datascroller align="left" for="tbPesquisaDestinatario"
				maxPages="100" id="sc2" reRender="sc1" />
		</f:facet>
	</rich:dataTable>
</a4j:outputPanel>