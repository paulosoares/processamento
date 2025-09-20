<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@include file="/pages/template/header.jsp"%>

<f:view>
	<a4j:page pageTitle="::.. Principal ..::">

		<h:form id="form">
			<jsp:include page="/pages/template/logo.jsp" flush="true">
				<jsp:param name="nomePagina" value="Principal" />
			</jsp:include>
			
			<a4j:outputPanel id="pnlPrincipal" ajaxRendered="true">				

				<h:panelGrid styleClass="MolduraExterna">

					<h:panelGrid styleClass="Moldura">
						<h:panelGrid styleClass="PadraoTituloPanel">
							<h:outputText value="Template" />
						</h:panelGrid>
					</h:panelGrid>

				</h:panelGrid>

				<%@include file="/pages/template/footer.jsp"%>
				
			</a4j:outputPanel>

		</h:form>
	</a4j:page>
</f:view>


