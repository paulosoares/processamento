<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<rich:modalPanel id="idPnlModalPesquisaDestinatario"
					keepVisualState="true"
					style="overflow:auto; position: fixed; top: 50; left: 50; height:600px; width:870px;"
					autosized="true">
	<f:subview id="viewMessages2">
		<a4j:outputPanel id="outputPanelMessages2" ajaxRendered="true" keepTransient="false">
			<t:panelGrid id="pnlMessages2"
						forceId="true"
						rendered="#{not empty facesContext.maximumSeverity}"
						cellpadding="0"
						cellspacing="0"
						columns="1"
						style="width: 100%; text-align: center;">
				<t:messages errorClass="ErrorMessage"
							style="text-align: left"
							infoClass="InfoMessage"
							warnClass="WarningMessage"
							showSummary="true"
							showDetail="true"
							layout="table" />
			</t:panelGrid>
		</a4j:outputPanel>
	</f:subview>
	<f:facet name="controls">
		<h:panelGroup>
			<h:form id="formCancelaTela" prependId="false">
				<a4j:commandLink action="#{beanDestinatario.cancelar}"
								styleClass="hidelink"
								oncomplete="Richfaces.hideModalPanel('idPnlModalPesquisaDestinatario')">
					<h:graphicImage url="/images/error.gif" />
				</a4j:commandLink>
			</h:form>
		</h:panelGroup>
	</f:facet>
	<h:form id="formPesquisaDestinatario" prependId="false">
		<jsp:include page="/pages/destinatariolistaremetente/destinatarioPesquisaConteudo.jsp" />
	</h:form>
</rich:modalPanel>