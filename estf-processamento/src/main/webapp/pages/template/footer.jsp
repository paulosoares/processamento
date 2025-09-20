<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<% 
	boolean isFrame = Boolean.parseBoolean(request.getParameter("frame"));
	if (!isFrame) {
%>
<f:subview id="viewFooter">
	<h:panelGrid style="width: 100%; text-align: center">
			<h:outputLink value="#{beanMenu.urlRelatorioTestes}" target="_blank" style="text-decoration:none">  
				<h:outputText styleClass="TituloApp" value="eSTF-Processamento Versão #{beanMenu.appVersion}" />
			</h:outputLink>
	</h:panelGrid>
	<h:panelGrid styleClass="PainelInferiorPagina">
		<h:outputText style="font-weight: bold; color: red"
			value="#{beanAmbiente.ambiente.descricao}" />
		<h:outputText
			value=" @ Supremo Tribunal Federal | Resolução mínima de 1024x768" />
	</h:panelGrid>
		<h:panelGroup rendered="#{not empty beanMensagemAtualizacao.mensagemAtualizacaoSistema}">
		<h:inputHidden value="#{beanMensagemAtualizacao.mostrarMensagemAtualizacaoSistema}" id="mensagemAtualizacaoSistema" />
		<script>
			if (document.getElementById('viewFooter:mensagemAtualizacaoSistema').value != ""){
				alert(document.getElementById('viewFooter:mensagemAtualizacaoSistema').value);
			}
		</script>		
	</h:panelGroup>
</f:subview>
<%
	}
%>
