'use strict';

StfDecisao.service('FlowService', function(views, msgService, togglesUtil, win) {
	var self = this;

	var mainViewCtrl = views.mainViewCtrl; // N�o faz parte do toggle, pois � sempre vis�vel nesse flow.
	
	var loginViewCtrl = views.loginViewCtrl;
	var selecaoDocsViewCtrl = views.selecaoDocsViewCtrl;
	var confirmarSenhaViewCtrl = views.confirmarSenhaViewCtrl;
	var detalheExpedientesViewCtrl = views.detalheExpedientesViewCtrl;
	var detalheTextosViewCtrl = views.detalheTextosViewCtrl;
	var viewsToggle = togglesUtil.viewsToggle([loginViewCtrl.id, selecaoDocsViewCtrl.id,
	                                   confirmarSenhaViewCtrl.id, detalheExpedientesViewCtrl.id, detalheTextosViewCtrl.id]);

	this.getViewsToggle = function() {
		return viewsToggle;
	};
	
	this.reset = function() {
		msgService.clear();
		viewsToggle.toggle();
	};
	
	this.irParaLogin = function() {
		msgService.clear();
		loginViewCtrl.instance.viewIn();
		viewsToggle.toggle(loginViewCtrl.id);
	};
	
	this.irParaSistema = function() {
		mainViewCtrl.instance.viewIn();
		$('#' + mainViewCtrl.id).show();
		self.irParaInicio(true);
	};
	
	this.irParaInicio = function(atualizarDocs, erroFatal) {
		loginViewCtrl.instance.viewOut();
		$('#' + selecaoDocsViewCtrl.id).hide();
		var deferred = $.Deferred();
		selecaoDocsViewCtrl.instance.update(atualizarDocs, function(){
			deferred.resolve();
		}, erroFatal);
		viewsToggle.toggle(selecaoDocsViewCtrl.id, deferred.promise());
	};
	
	this.irParaSenha = function() {
		msgService.clear();
		confirmarSenhaViewCtrl.instance.viewIn();
		viewsToggle.toggle(confirmarSenhaViewCtrl.id);
	};
	
	this.irParaDetalheExpediente = function(id){
		msgService.clear();
		var promise = detalheExpedientesViewCtrl.instance.detalhar(id);
		viewsToggle.toggle(detalheExpedientesViewCtrl.id, promise);
	};
	
	this.irParaDetalheTexto = function(id){
		msgService.clear();
		var promise = detalheTextosViewCtrl.instance.detalhar(id);
		viewsToggle.toggle(detalheTextosViewCtrl.id, promise);
	};
	
	var finishCallback;
	this.ativarFullScreen = function(fc) {
		finishCallback = fc;
		mainViewCtrl.instance.iniciarFullScreen();
	};
	this.desativarFullScreen = function() {
		if (finishCallback) {
			finishCallback();
		}
	};
});