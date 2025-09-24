"use strict";

StfDecisao.service("VersaoService", function(mni, ajaxUtil){
	var self = this;
	
	var getVersaoMobile = function() {
		var deferred = $.Deferred();
		mni.recuperarVersaoMobile().done(function(data) {
			console.log(data);
			deferred.resolve(data.versao);
		}).fail(function() {
			deferred.resolve("Erro");
		});
		return deferred.promise();
	};
	
	var getVersaoServidor = function() {
		var deferred = $.Deferred();
		ajaxUtil.ajax({
			type: 'GET',
			url: '/edecisao/seam/resource/rest/assinador/versao',
			dataType: 'json'
		}).done(function(data) {
			console.log(data);
			deferred.resolve(data.versao + ' [' + data.ambiente + ']');
		}).fail(function() {
			deferred.resolve("Erro");
		});
		return deferred.promise();
	};
	
	this.getVersao = function() {
		var deferred = $.Deferred();
		getVersaoMobile().done(function(dataMobile) {
			console.log(dataMobile);
			var versaoMobile = dataMobile;
			getVersaoServidor().done(function(dataServidor) {
				console.log(dataMobile);
				var versaoServidor = dataServidor;
				deferred.resolve('Versão ' + versaoMobile + '/' + versaoServidor);
			});
		});
		return deferred.promise();
	};
	
});
