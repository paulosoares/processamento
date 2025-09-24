"use strict";

StfDecisao.service("SetorService", function(ajaxUtil){
	var self = this;
	
	this.getSetores = function() {
		var deferred = $.Deferred();
		ajaxUtil.ajax({
			type: 'GET',
			url: '/edecisao/seam/resource/rest/assinador/setores',
			dataType: 'json'
		}).done(function(data) {
			deferred.resolve(data);
		}).fail(function() {
			deferred.resolve("Erro");
		});
		return deferred.promise();
	};
	
	this.getSetorAtual = function() {
		var deferred = $.Deferred();
		ajaxUtil.ajax({
			type: 'GET',
			url: '/edecisao/seam/resource/rest/assinador/setor/atual',
			dataType: 'json'
		}).done(function(data) {
			deferred.resolve(data);
		}).fail(function() {
			deferred.resolve("Erro");
		});
		return deferred.promise();
	};
	
	this.mudarSetor = function(idNovoSetor) {
		var deferred = $.Deferred();
		ajaxUtil.ajax({
			type: 'POST',
			url: '/edecisao/seam/resource/rest/assinador/setor/mudar/' + idNovoSetor,
			dataType: 'json'
		}).done(function(data) {
			deferred.resolve(data);
		}).fail(function() {
			deferred.resolve("Erro");
		});
		return deferred.promise();
	};
	
});
