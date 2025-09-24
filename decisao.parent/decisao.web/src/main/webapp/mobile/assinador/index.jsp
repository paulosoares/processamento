<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="br.jus.stf.estf.decisao.mobile.assinatura.support.VersionUtil" %>
<%@ page import="java.util.Date" %>
<%
// 	long version = (new Date()).getTime(); // Utilizado para prevenir cache dos arquivos JS (Usar apenas para desenvolvimento)
	String version = VersionUtil.getVersion(getServletContext());
%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="content-type" content="text/html;charset=ISO-8859-1" />
		<meta charset="ISO-8859-1" />
		<title>STF - Assinador mobile</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
		<meta content="Solução de assinatura contingencial por dispositivos móveis para o STF" name="description" />
		<meta content="Leandro Rezende" name="author" />		
		<link href="assets/plugins/boostrapv3/css/bootstrap.min.css?<%= version %>" rel="stylesheet" type="text/css"/>
		<link href="assets/plugins/boostrapv3/css/bootstrap-theme.min.css?<%= version %>" rel="stylesheet" type="text/css"/>	
		<link href="assets/plugins/font-awesome/css/font-awesome.css?<%= version %>" rel="stylesheet" type="text/css"/>		
		<link href="assets/css/responsive.css?<%= version %>" rel="stylesheet" type="text/css"/>
		<link href="assets/css/custom-icon-set.css?<%= version %>" rel="stylesheet" type="text/css"/>		
		<link href="assets/css/style.css?<%= version %>" rel="stylesheet" type="text/css"/>
		<link href="assets/css/animate.min.css?<%= version %>" rel="stylesheet" type="text/css"/>
		<link href="assets/plugins/jquery-datatable/css/jquery.dataTables.css?<%= version %>" rel="stylesheet" type="text/css"/>
		<link href="assets_stf/css/custom.css?<%= version %>" rel="stylesheet" type="text/css"/>		
		<link href="assets/plugins/datatables-responsive/css/datatables.responsive.css?<%= version %>" rel="stylesheet" type="text/css"/>
		<link href="assets_stf/css/demo.css?<%= version %>" rel="stylesheet" type="text/css"/>
		<link href="assets_stf/css/component.css?<%= version %>" rel="stylesheet" type="text/css"/>
	</head>

<body style="display: none;">

	<!-- Views serão inseridas dinamicamente no body. -->
	
	<script src="assets_stf/js/modernizr.custom.js?<%= version %>" type="text/javascript"></script>
	<script src="assets/plugins/jquery-1.8.3.min.js?<%= version %>" type="text/javascript"></script>
	<script src="assets/plugins/boostrapv3/js/bootstrap.min.js?<%= version %>" type="text/javascript"></script>
	<script src="assets/plugins/jquery-slider/jquery.sidr.min.js?<%= version %>" type="text/javascript"></script> 
	<script src="assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js?<%= version %>" type="text/javascript"></script>
	<script src="assets/plugins/breakpoints/breakpoints.js?<%= version %>" type="text/javascript"></script> 
	<script src="assets/plugins/jquery-datatable/js/jquery.dataTables.min.js?<%= version %>" type="text/javascript"></script>
	<script src="assets/plugins/jquery-datatable/extra/js/TableTools.js?<%= version %>" type="text/javascript"></script>
	<script src="assets/plugins/data-tables/DT_bootstrap.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/datatable.pagination.swipe.js?<%= version %>" type="text/javascript"></script>
	<script src="assets/plugins/datatables-responsive/js/datatables.responsive.js?<%= version %>" type="text/javascript"></script> 
	<script src="assets/js/email_comman.js?<%= version %>" type="text/javascript"></script> 
	<script src="assets/js/core.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/classie.js?<%= version %>"></script>
	<script src="assets_stf/js/jquery.mobile.custom.min.js?<%= version %>"></script>
	<script src="assets_stf/js/progressButton.js?<%= version %>"></script>
	<script src="assets_stf/js/datatables-ptbr.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/StfDecisao.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/util/Toggles.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/util/Browser.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/util/AssinaturaProgress.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/util/SubFlow.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/util/Promise.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/util/Certificado.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/util/Ajax.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/service/AssinaturaService.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/service/MessageService.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/service/FlowService.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/service/SetorService.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/model/AssinaturaModel.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/controller/sistema/ConfirmarAssinaturaComSenhaViewCtrl.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/util/Datatable.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/controller/sistema/MainViewCtrl.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/controller/sistema/SelecaoDocumentosAssinaturaViewCtrl.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/service/LoaderService.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/service/LoginService.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/controller/login/LoginViewCtrl.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/controller/sistema/DetalheExpedientesViewCtrl.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/controller/sistema/DetalheTextosViewCtrl.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/app.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/app-bootstrap.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/native/MockMobileNativeInterfaceFacade.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/native/MobileNativeInterfaceFacade.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/native/controller/importacao/ImportarCertificadoViewCtrl.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/native/controller/sistema/ConfirmarCertificadoSenhaViewCtrl.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/native/service/AssinaturaMobileService.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/native/service/NativeFlowService.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/native/service/MessageServiceNative.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/native/service/CertificadoService.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/service/VersaoService.js?<%= version %>" type="text/javascript"></script>
	<script src="assets_stf/js/app/native/app-custom.js?<%= version %>" type="text/javascript"></script>
</body>
</html>
