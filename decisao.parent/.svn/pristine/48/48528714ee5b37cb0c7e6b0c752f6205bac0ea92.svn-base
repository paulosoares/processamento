'use strict';

StfDecisao.service('AssinaturaMobileService', function(assinaturaService, mni, acoes, promiseUtil, ajaxUtil) {
	var self = this;
	
	var buildDocAssinarUrl = function(doc) {
		if (doc.tipo == "texto") {
			return acoes.ASSINAR_TEXTOS.buildUrl(doc.id);
		} else if (doc.tipo == "comunicacao") {
			return acoes.ASSINAR_COMUNICACOES.buildUrl(doc.id);
		}
	};
	
	var buildDocAssinarUrlPos = function(doc) {
		if (doc.tipo == "texto") {
			return acoes.ASSINAR_TEXTOS.buildUrlPos(doc.id);
		} else if (doc.tipo == "comunicacao") {
			return acoes.ASSINAR_COMUNICACOES.buildUrlPos(doc.id);
		}
	};
	
	var mobileRecuperarCadeia = function() {
		return function() {
			return mni.recuperarCadeia();
		};
	};
	
	var serverPreAssinar = function(doc, cadeia) {
		return function() {
			return ajaxUtil.ajax({
				type: 'POST',
				contentType: 'application/json',
				url: '/edecisao/seam/resource/rest/' + buildDocAssinarUrl(doc),
				data: JSON.stringify(cadeia),
				dataType: 'json'
			});
		};
	};
	
	var mobileAssinar = function(senha) {
		return function(data) {
			var deferred = $.Deferred();
			mni.assinarDigitalmente(data.hash, null, senha).done(function(assinatura) {
				deferred.resolve({"idContexto": data.id, "assinatura": assinatura});
			}).fail(function(data) {
				if (data.errors[0] == 'Senha inválida') {
					data.erroImpeditivo = true;
					data.erroSenhaInvalida = true;
				}
				deferred.reject(data);
			});
			return deferred.promise();
		};
	};
	
	var serverPosAssinar = function(doc, deferred) {
		return function(data) {
			return ajaxUtil.ajax({
				type: 'POST',
				url: '/edecisao/seam/resource/rest/' + buildDocAssinarUrlPos(doc),
				data: {'idContexto': data.idContexto, 'assinatura' : data.assinatura},
				dataType: 'json'
			});
		}
	};
	
	var assinarComCustomBackend = function(docs, senha) { 
		return function(cadeia) {
			return assinaturaService.assinarComCustomBackend(docs, senha, function(paramDoc) {
				var promise = promiseUtil.encadear(
					serverPreAssinar(paramDoc, cadeia),
					mobileAssinar(senha),
					serverPosAssinar(paramDoc)
				);
				return promise;
			});
		}
	};
	
	this.assinar = function(docs, senha) {
		var promise = promiseUtil.encadear(
			mobileRecuperarCadeia(), assinarComCustomBackend(docs, senha)
		);
		return promise;
	};
});


StfDecisao.util('AcoesNative', function() {
	this.ASSINAR_TEXTOS = {
		buildUrl: function(id) {
			return 'assinador/externo/texto/' + id + '/pre-assinar';
		},
		buildUrlPos: function(id) {
			return 'assinador/externo/texto/' + id + '/pos-assinar';
		}
	};
	
	this.ASSINAR_COMUNICACOES = {
		buildUrl: function(id) {
			return 'assinador/externo/comunicacao/' + id + '/pre-assinar';
		},
		buildUrlPos: function(id) {
			return 'assinador/externo/comunicacao/' + id + '/pos-assinar';
		}
	};
});