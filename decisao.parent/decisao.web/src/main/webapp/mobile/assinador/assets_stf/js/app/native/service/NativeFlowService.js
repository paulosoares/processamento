'use strict';

StfDecisao.service('NativeFlowService', function(flowService, views, serviceMsg, mni) {
	var self = this;
	
	this.reset = function() {
		serviceMsg.clear();
		flowService.reset();
	};
	
	this.irParaLogin = function() {
		flowService.irParaLogin();
	};
	
	this.irParaSistema = function() {
		flowService.irParaSistema();
	};
	
	this.irParaInicio = function(atualizarDocs, erroFatal) {
		flowService.irParaInicio(atualizarDocs, erroFatal);
	};
	
	this.irParaSenha = function() {
		flowService.irParaSenha();
	};
	
	this.irParaDetalheExpediente = function(id){
		flowService.irParaDetalheExpediente(id);
	};
	
	this.irParaDetalheTexto = function(id){
		flowService.irParaDetalheTexto(id);
	};
	
	this.ativarFullScreen = function(finishCallback) {
		flowService.ativarFullScreen(finishCallback);
	};
	
	this.desativarFullScreen = function() {
		flowService.desativarFullScreen();
	};
});