var startTime;
function start() {
	startTime = new Date().getTime();
}
function stop() {
	stopTime = new Date().getTime();
	var resultTime = ((stopTime - startTime) / 1000);
	document.getElementById('queryTime').innerHTML = '(' + resultTime + ' segundos)';
}
function showActionPanel(panelId, panelWidth, panelHeight) {
	Richfaces.hideModalPanel(panelId); 
	Richfaces.showModalPanel(panelId, {width: panelWidth, left: 'auto', height: panelHeight, top: 'auto'});
}
function abrirJanela(url) {
	if( url != null && url != '' ){
		window.open(url,null,'toolbar=no,copyhistory=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,top=0,left=0');
	}
}
function abrirAnexo(url) {
	if( url != null && url != '' ){
		window.open(url,"_self",'toolbar=no,copyhistory=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,top=0,left=0');
	}
}
function pesquisar(idBotao) {
	var tecla = event.which;
	if (tecla == null)
		tecla = event.keyCode;

	if (tecla && tecla == 13) {
		botao = document.getElementById(idBotao);
		if (botao != null) {
			botao.click();
		}
	}
}
function setFocus(elId) {
	var el = document.getElementById(elId);
	if (el && el.focus) {
		el.focus();
	}
}
function atualizaContadorCaracteres(idContador, campoContado, quantidadeMaximaCaracteres) {
	document.getElementById(idContador).innerHTML = quantidadeMaximaCaracteres - campoContado.value.length; 
}

function confirmarSair(){
	return confirm("Voc� ser� desconectado de todas as aplica��es em que estiver logado.\nDeseja realmente sair?\n(Para fechar somente esta tela, feche a aba do navegador.)");
}