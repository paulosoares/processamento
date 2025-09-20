<%@page
	import="br.gov.stf.estf.assinatura.visao.jsf.beans.consultardocumentosexterno.BeanConsultarDocumentoExterno"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%
	Object seqObjetoIncidenteAttr = session
			.getAttribute(BeanConsultarDocumentoExterno.SEQ_OBJETO_INCIDENTE_CONSULTA_EXTERNA);

	Long seqObjetoIncidente = (Long) seqObjetoIncidenteAttr;

	response.sendRedirect(BeanConsultarDocumentoExterno.PORTAL_STF + seqObjetoIncidente);
%>