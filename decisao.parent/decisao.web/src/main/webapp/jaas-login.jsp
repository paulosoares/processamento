<%@page language="java" pageEncoding="ISO-8859-1" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
		<title>eSTF-Decisão - Login</title>
		<link href="common/styles/reset.css" rel="stylesheet" type="text/css"/>
		<link href="common/styles/richfaces.css" rel="stylesheet" type="text/css"/>
		<link href="common/styles/theme.css" rel="stylesheet" type="text/css"/>
		<script src="/common/scripts/jquery-1.4.2.min.js" type="text/javascript"></script>
		<script src="/common/scripts/global.js" type="text/javascript"></script>
		<script src="/common/scripts/jquery.bgiframe.js" type="text/javascript"></script>
		<script src="/common/scripts/styled-select.js" type="text/javascript"></script>
		<!--[if lt IE 8]>
			<link href="common/styles/theme-ie.css" rel="stylesheet" type="text/css" />
               <style media="screen" type="text/css">
               	/* Layout principal */
                   .root-wrapper       { height:100%; }
                   body				{ overflow-y: visible; }
               </style>
        <![endif]-->
        <style type="text/css">
        	label					{ display: inline-block; width: 80px; text-align: right; margin-right: 3px; margin-top: 3px; font-weight: bold; }
        	.input-field			{ margin-top: 4px;}
        	.login-form				{ width: 280px; position: absolute; left: 50%; top: 170px; margin-left: -150px; border: 1px solid #006699; padding: 5px 5px 15px 5px; 
        							 -moz-border-radius: 3px; -webkit-border-radius: 3px; border-radius: 3px;
        							 -moz-box-shadow: 2px 2px 3px rgba(0,0,0,0.2) }
        </style>
        
        <script type="text/javascript">
        //<![CDATA[
        	$j = jQuery.noConflict();
        	(function($) { 
		      	$(function() {
		       	$('select').styled();
		      	});
       		})(jQuery);
       	//]]>
        </script>
	</head>
	<body>
		<div class="root-wrapper">
			<div class="header">
				<table>
					<tbody>
						<tr>
							<td style="text-align: left; width: 115px;">
								<img src="common/images/stf-logo.png" />
							</td>
							<td style="text-align: left">
								<span style="margin-right: 5px;">eSTF-Decisão</span>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			

			<div class="body">
				<form method="post" action="j_security_check" id="formLogin" class="login-form">
					<div class="result-info" style="text-align: left">Por favor. Entre com seu usuário e senha de rede.</div>
					<div class="input-field">
						<label for="j_username" style="width: 30%">Usuário: </label><input type="text" name="j_username" style="width: 55%"/>
					</div>
					<div class="input-field">
						<label for="j_username" style="width: 30%">Senha: </label><input type="password" name="j_password" style="width: 55%"/>
					</div>
					<c:if test="${param.error != null}">
						<div class="input-field" style="text-align: right">
							<label style="margin-right: 43%; white-space: nowrap; color: red">Usuário ou Senha inválido(s)</label>
						</div>
					</c:if>
					<div class="input-field"  style="text-align: right">
						<input type="submit" name="ok" value="Entrar" style="margin-right: 14%"/>
					</div>
				</form>
			</div>
			<table class="footer">
				<tr>
					<td style="text-align: left; padding: 0px 5px;">© Supremo Tribunal Federal</td>
				</tr>
			</table>
		</div>
	</body>
</html>
