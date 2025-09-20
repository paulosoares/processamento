<%@page
	import="br.gov.stf.estf.assinatura.visao.jsf.beans.consultardocumentosexterno.BeanConsultarPecas"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%
	Object seqObjetoIncidenteAttr = session
			.getAttribute(BeanConsultarPecas.SEQ_OBJETO_INCIDENTE_PROCESSO);

	Long seqObjetoIncidente = (Long) seqObjetoIncidenteAttr;

	response.sendRedirect(BeanConsultarPecas.VISUALIZADOR_PECAS + seqObjetoIncidente);
%>