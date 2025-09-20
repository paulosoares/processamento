<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<jsp:include page="/pages/template/header.jsp" flush="true" />
<style type="text/css">
<!--
//
CSS
.linha-par {
	background-color: #FCFFFE;
}

.linha-impar {
	background-color: #ECF3FE;
}
-->
</style>
<f:view>
	<a4j:page pageTitle="::.. Principal ..::">
		<a4j:keepAlive beanName="beanDestinatario"/>

		<h:form id="formPesquisaDestinatario" prependId="false">
			<jsp:include page="/pages/template/logoMenu.jsp" flush="true">
				<jsp:param name="nomePagina" value="Pesquisar Destinatário" />
			</jsp:include>
			<jsp:include page="/pages/destinatariolistaremetente/destinatarioPesquisaConteudo.jsp" />
		</h:form>

		<!-- Confirmar exclusão -->
		<rich:modalPanel id="idPanelExcluir" 
			width="350" 
			height="100" 
			keepVisualState="true" 
			style="overflow:auto; position: fixed; top: 50; left: 50;" 
			autosized="true">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="Confirmar exclusão" />
				</h:panelGroup>
			</f:facet>
			<h:form>
				Deseja realizar a exclusão do destinatário<br><br>
				<b><h:outputText value="#{beanDestinatario.bean.descricaoPrincipal}" /></b>?
				
				<div style="margin-top: 20px; margin-left: 100px;">
					<a4j:commandButton id="btnSim"
										styleClass="Excluir" 
										value="  Sim" 
										actionListener="#{beanDestinatario.excluir}" 
										reRender="tbPesquisaDestinatario" 
										oncomplete="Richfaces.hideModalPanel('idPanelExcluir')"/>
					<a4j:commandButton id="btnNao"
										styleClass="Cancelar"
										value=" Não"
										reRender="tbPesquisaDestinatario"
										oncomplete="Richfaces.hideModalPanel('idPanelExcluir')"/>
				</div>
			</h:form>
		</rich:modalPanel>
		
		<!-- Cadastro de destinatário -->
		<rich:modalPanel id="idPnlDestinatarioCadastro" 
							keepVisualState="true"
							style="overflow:auto; position: fixed; top: 50; left: 50;"
							autosized="true">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="Cadastrar Destinatário - Novo/Alterar" />
				</h:panelGroup>
			</f:facet>
			<h:form id="cadastro">
				<jsp:include page="/pages/destinatariolistaremetente/destinatarioCadastro.jsp" />
			</h:form>
		</rich:modalPanel>
		<jsp:include page="/pages/consultaCep/consultaCep.jsp" />

		<jsp:include page="/pages/template/footer.jsp" flush="true" />
	</a4j:page>
</f:view>