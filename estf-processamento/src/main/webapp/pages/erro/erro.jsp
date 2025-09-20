<%@page import="br.gov.stf.estf.util.DataUtil"%>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/styles/buble-tooltip.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/styles/default.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/styles/dtree.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/styles/links.css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/styles/tree-view-0.1.0.css" />

<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/default.js"></script>
<script type="text/javascript" language="JavaScript" src="<%=request.getContextPath()%>/scripts/Etiqueta.js"></script>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ page import="java.util.Date, java.text.DateFormat, javax.faces.application.ViewExpiredException, 
	org.apache.commons.lang.exception.ExceptionUtils" isErrorPage="true"%>

<%
	//final String requestURL = request.getRequestURL().toString();
	//final String requestURI = request.getRequestURI();
	//final int index = requestURL.indexOf(requestURI);
	//final String retryURL = requestURL.substring(0, index) + request.getAttribute("javax.servlet.error.request_uri");

	if (exception instanceof ViewExpiredException) {
		response.sendRedirect(request.getContextPath() + "/pages/erro/sessaoExpirada.jsp");
	}
	
	final String mensagemErro = ExceptionUtils.getMessage(exception);
	final String mensagemErroRaiz = ExceptionUtils.getRootCauseMessage(exception);
%>

<html>
<header>
	<title>::.. Erro ..::</title>
	<meta name="robots" content="noindex,nofollow,nosnippet,noodp,noarchive,noimageindex" />
	
	<script type="text/javascript">
	<!-- //
		function mostrarOuEsconderDetalhes() {
			var tabela = document.getElementById("tblDetalhes");
			var botao = document.getElementById("btnMostrarDetalhes");
			
			if (tabela.style.display == "none") {
				tabela.style.display = "";
				botao.value = "Ocultar Detalhes"
			} else {
				tabela.style.display = "none";
				botao.value = "Mostrar Detalhes"
			}
		}
	// -->
	</script>

</header>
<body>

	<%@include file="/pages/template/logo.jsp"%>
	<br/>
	<br/>
	<div style="text-align: center; width: 100%;">
		<div style="text-align: -webkit-center;">
			<span class="ErrorMessage">
				Ocorreu um erro inesperado:<br /><br />
				<%=mensagemErroRaiz%><br /><br />
				Veja detalhes abaixo e entre em contato com o Atendimento da TI (Help Desk).
			</span>
		</div>	
		<br/>
		<br/>
		<input type="button" id="btnMostrarDetalhes" value="Mostrar Detalhes" class="BotaoPadraoEstendido" onclick="mostrarOuEsconderDetalhes();" />
	</div>


	<table id="tblDetalhes" cellspacing="5" cellpadding="2" border="0" style="display: none; text-align: left; width: 100%;">
	
		<tr>
			<td class="Padrao">
				<span class="PadraoDestaque">Data/Hora:</span> <%=DataUtil.date2String(new Date(), true)%>
				<br />
				<span class="PadraoDestaque">Servidor:</span> <%=request.getServerName()%>
				<br />
				<span class="PadraoDestaque">Código HTTP retornado pelo servidor:</span> <%=request.getAttribute("javax.servlet.error.status_code")%>
				<br />
			</td>
		</tr>
		
		<tr>
			<td class="Padrao">
				<%--<span class="PadraoDestaque">javax.servlet.error.request_uri:</span> <%=request.getAttribute("javax.servlet.error.request_uri")%>
				<br />--%>
				<span class="PadraoDestaque">Tipo de erro:</span> <%=request.getAttribute("javax.servlet.error.exception_type")%>
				<br />
				<span class="PadraoDestaque">Servlet onde o erro ocorreu:</span> <%=request.getAttribute("javax.servlet.error.servlet_name")%>
				<br />
				<span class="PadraoDestaque">Mensagem de erro retornada:</span> <%=mensagemErro%>
				<br />
				<span class="PadraoDestaque">Mensagem do erro-raíz:</span> <%=mensagemErroRaiz%>
				<br />
			</td>
		</tr>
		
		<tr>
			<td class="Padrao">
				<span class="PadraoDestaque">Stack Trace:</span><br />
				<textarea id="txtStackTrace" rows="20" readonly="readonly"  
					style="width: 100%"><%=ExceptionUtils.getFullStackTrace(exception)%></textarea><br />
			</td>
		</tr>
		
	</table>
	
	<br /><br />

	<div class="PadraoDestaque" style="text-align: center; width: 100%;">
		<div style="text-align: -webkit-center">
			<a href="<%=request.getContextPath()%>/pages/principal/principal.jsf">Clique aqui para voltar à página principal.</a>
		</div>
	</div>
	
	<br /><br />

	<table cellspacing="0" cellpadding="0" border="0" class="PainelInferiorPagina">
		<tr>
			<td>
				<span>@ Supremo Tribunal Federal | Resolução mínima de 1024x768</span>
			</td>
		</tr>
	</table>
	
</body>
</html>