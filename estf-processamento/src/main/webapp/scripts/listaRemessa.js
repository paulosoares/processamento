
function confirmaFinalizacaoLista() {	
	var textoValidacao = document.getElementById('uploadForm:idMensagemValidacao').value;
	if (textoValidacao != null && textoValidacao.trim() != "") {
		if (confirm(textoValidacao + '\nDeseja continuar?')) {
    		return true;
		} else {
    		return false;
		}
	} else {
		return true;
	}
}