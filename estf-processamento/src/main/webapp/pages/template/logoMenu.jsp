<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<% 
	boolean isFrame = Boolean.parseBoolean(request.getParameter("frame"));
	if (!isFrame) {
%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td valign="top" width="80">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><img
					src="<%=request.getContextPath()%>/images/logoeSTFGabinete2.png" /></td>
			</tr>
		</table>
		</td>
		<td valign="top" width="100%">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td align="right" height="73" colspan="0"
					style="background-image: URL(<%= request.getContextPath() %>/images/fundo_logoeSTF.png ); background-repeat: repeat-x">
				<img
					src="<%=request.getContextPath()%>/images/detalhesFundoLogo.png" />
				</td>
			</tr>
			<tr>
				<td height="26" colspan="0"
					style="background-image: URL(<%= request.getContextPath() %>/images/fundo_menu.png ); background-repeat: repeat-x">
				<table>
					<tr>
						<td><input type="hidden" id="jscook_action"
							name="jscook_action" /></td>
						<td><jsp:include page="/pages/template/menu.jsp"/>			
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
		<td valign="top" width="80">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td><img
					src="<%=request.getContextPath()%>/images/brasaoENome.png"
					align="center" /></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<% } %>
<h:panelGrid styleClass="PainelSuperiorPagina" width="100%" columns="2" id="idPanelTituloPagina">
	<h:panelGrid styleClass="TituloPagina" width="100%" style="text-align: left">
		<h:outputText>eStf-Processamento - ${param.nomePagina}</h:outputText>
	</h:panelGrid>
	<h:panelGrid styleClass="TituloPagina" width="100%" style="text-align: right">	
		<h:outputText value="#{beanUsuario.usuario.name}@#{beanUsuario.usuario.setor.sigla}" />
	</h:panelGrid>
</h:panelGrid>

<f:subview id="viewHeader">

  <f:verbatim>
	  <%/* processando */%>
	  <div id="ProcessandoPopupDivBack" class="ProcessandoPopupDivBack">
	  </div>
	  <div id="ProcessandoPopupDiv" class="ProcessandoPopupDiv" style="background-image: URL(<%= request.getContextPath() %>/images/fundo_logoeSTF.png ); background-repeat: repeat-x">
	  <img src="<%=request.getContextPath()%>/images/ajax-loader.gif" /><br/>
	    <span>Processando...</span>
	  </div>
	  <iframe id="ProcessandoDivShim" class="ProcessandoDivShim" scrolling="no" frameborder="0">
	  </iframe>
	  <%/* processando */%>
	  
  </f:verbatim>
</f:subview>

<%@include file="/pages/template/messages.jsp" %>
