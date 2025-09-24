'use strict';

StfDecisao.facade('MockMobileNativeInterfaceFacade', function() {
	this.assinarDigitalmente = function(hash, idCertificado, senha) {
		var deferred = $.Deferred();
		
		deferred.reject({errors: ["Opera��o n�o dispon�vel"]});
		
		return deferred.promise();
	};
	this.recuperarCadeia = function(idCertificado) {
	    var deferred = $.Deferred();
	    
	    deferred.reject({errors: ["Opera��o n�o dispon�vel"]});
	    
	    return deferred.promise();
	};
	this.testarAssinatura = function(idCertificado, senha) {
	    var deferred = $.Deferred();

	    deferred.reject({errors: ["Opera��o n�o dispon�vel"]});
	    
	    return deferred.promise();
	};
	this.existeCertificadoParaImportacao = function() {
	    var deferred = $.Deferred();

	    deferred.reject({errors: ["Opera��o n�o dispon�vel"]});
	    
	    return deferred.promise();
	};
	this.recuperarDadosImportacao = function(senha) {
	    var deferred = $.Deferred();

	    deferred.reject({errors: ["Opera��o n�o dispon�vel"]});
	    
	    return deferred.promise();
	};
	this.setCallbackIniciarImportacaoCertificado = function(callback) {
		StfDecisao.callbackIniciarImportacaoCertificadoMock = callback;
	};
	this.importarCertificado = function(senha) {
	    var deferred = $.Deferred();

	    deferred.reject({errors: ["Opera��o n�o dispon�vel"]});
	    
	    return deferred.promise();
	};
	this.recuperarDadosCertificado = function() {
	    var deferred = $.Deferred();

	    deferred.reject({errors: ["Opera��o n�o dispon�vel"]});
	    
	    return deferred.promise();
	};
	this.zerarImportacao = function() {
		var deferred = $.Deferred();

		deferred.reject({errors: ["Opera��o n�o dispon�vel"]});
	    
	    return deferred.promise();
	};
	this.excluirCertificado = function(idCertificado) {
		var deferred = $.Deferred();

		deferred.reject({errors: ["Opera��o n�o dispon�vel"]});
	    
	    return deferred.promise();
	};
	this.recuperarVersaoMobile = function() {
		var deferred = $.Deferred();
		
		deferred.reject({errors: ["Opera��o n�o dispon�vel"]});
		
		return deferred.promise();
	};
});