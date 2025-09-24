var StfDecisao = StfDecisao || {};

$(document).ready(function() {
	StfDecisao.assinadorApp = StfDecisao.components.instanciar('AssinadorApp');
	for (var i in StfDecisao.initApp) {
		StfDecisao.initApp[i]();
	}
	StfDecisao.assinadorApp.init();
});