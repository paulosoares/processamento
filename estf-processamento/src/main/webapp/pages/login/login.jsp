<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/buble-tooltip.css" />
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/default.css" />
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/dtree.css" />
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/links.css" />
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/styles/tree-view-0.1.0.css" />
	
<script type="text/javascript" language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/default.js"></script>

<script type="text/javascript" language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/Etiqueta.js"></script>
	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
	
<html>
	<head>
		<title>::.. Login ..::</title>
		<meta name="robots" content="noindex,nofollow,nosnippet,noodp,noarchive,noimageindex" />
	</head>
	<body onload="document.getElementById('txtUsuario').focus();" >
	
		<c:if test="${sessionScope.user ne null}">
			<c:redirect url="/pages/principal/principal.jsf" />
		</c:if>
		
		<%@include file="/pages/template/logo.jsp"%>
		
		<c:if test="${not empty param.login_error}">
			<table cellpadding="0" cellspacing="0" style="text-align: center;"
				width="100%">
				<tbody>
					<tr>
						<td>
							<table width="100%">
								<tr>
									<td align="center">
										<span class="ErrorMessage" style="text-align: left">Autenticação sem sucesso. Tente novamente.<BR></span>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
		</c:if>
		
		<form action="<c:url value='/j_spring_security_check'/>" method="post">
			<table cellspacing="3" cellpadding="0" border="0" style="width: 100%">
			
				<tr>
					<td class="Padrao" style="text-align: right; width: 40%"><span>Usuário:</span></td>
					<td><input type="text" name="j_username" id="txtUsuario" /></td>
				</tr>
				<tr>
					<td class="Padrao" style="text-align: right"><span>Senha:</span></td>
					<td><input type="password" name="j_password" autocomplete="on" /></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td colspan="2" style="text-align: left">
						<input type="submit" name="login_button" value="Autenticar" class="Botao" />
					</td>
				</tr>
			</table>
		</form>

			<table cellspacing="0" cellpadding="0" border="0" class="PainelInferiorPagina">
				<tr>
					<td>
						<span>@ Supremo Tribunal Federal | Resolução mínima de 1024x768</span>
					</td>
				</tr>
			</table>


	</body>
</html>