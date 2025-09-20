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
<%@ page isErrorPage="true"%>

<html>
<header>
	<title>::.. Sessão Expirada ..::</title>
	<meta name="robots" content="noindex,nofollow,nosnippet,noodp,noarchive,noimageindex" />
</header>
<body>

	<%@include file="/pages/template/logo.jsp"%>
	<br/>
	<br/>
	<div style="text-align: center; width: 100%;">
		<div style="text-align: -webkit-center">
			<span class="ErrorMessage">
				Sua sessão expirou!<br /><br />
				<a href="<%=request.getContextPath()%>/pages/principal/principal.jsf">Clique aqui para voltar à página principal.</a>
			</span>
		</div>
	</div>
	<br/>
	<br/>
	<table cellspacing="0" cellpadding="0" border="0" class="PainelInferiorPagina">
		<tr>
			<td>
				<span>@ Supremo Tribunal Federal | Resolução mínima de 1024x768</span>
			</td>
		</tr>
	</table>
	
</body>
</html>