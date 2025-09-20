<%@ page contentType="text/html;charset=windows-1252"
	import="br.gov.stf.estf.assinatura.visao.util.constantes.Constantes"%>

<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252"></meta>
<title>eSTF</title>
</head>
<body>

	<script>
		window.location='<%=request.getAttribute(Constantes.CHAVE_URL_CONSULTA_EXTERNA)%>';
	</script>

</body>
</html>