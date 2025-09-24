'use strict';

StfDecisao.service('AssinaturaService', function(acoes, ajaxUtil) {
	var self = this;
	var buildDocAssinarUrl = function(doc) {
		if (doc.tipo == "texto") {
			return acoes.ASSINAR_TEXTOS.buildUrl(doc.id);
		} else if (doc.tipo == "comunicacao") {
			return acoes.ASSINAR_COMUNICACOES.buildUrl(doc.id);
		}
	};
	
	this.assinarComCustomBackend = function(docs, senha, customBackendCall) {
		var deferred = $.Deferred();
		
		if (docs.length > 0) {
			var idsIndex = 0;
			var idsSize = docs.length;
			var error = false;
			var erroImpeditivo = false;
			var finalDto = {
				quantidadeTextosAssinados: 0,
				quantidadeComunicacoesAssinadas: 0,
				warnings: [],
				errors: []
			};
			var composeFinalDto = function(dto) {
				finalDto.quantidadeTextosAssinados += dto.quantidadeTextosAssinados;
				finalDto.quantidadeComunicacoesAssinadas += dto.quantidadeComunicacoesAssinadas;
				finalDto.erroImpeditivo = dto.erroImpeditivo;
				finalDto.erroSenhaInvalida = dto.erroSenhaInvalida;
				finalDto.erroFatal = dto.erroFatal;
				finalDto.erroProvavelConexaoPerdida = dto.erroProvavelConexaoPerdida;
				for (var i in dto.warnings) {
					finalDto.warnings.push(dto.warnings[i]);
				}
				for (var i in dto.errors) {
					if ($.inArray(dto.errors[i], finalDto.errors) == -1) {
						finalDto.errors.push(dto.errors[i]);
					}
				}
			};
			
			var doneCallback = function(dto) {
				composeFinalDto(dto);
			}
			
			var provavelErroConexaoPerdida = function() {
				error = true;
				var dto = {'erroProvavelConexaoPerdida': true, 'errors': ['Alguns documentos não foram assinados devido a instabilidade da conexão com a Internet. Tente assiná-los novamente com uma conexão mais estável.']};
				composeFinalDto(dto);
			};
			
			var failCallback = function(dto) {
				try {
					if (dto) {
						composeFinalDto(dto);
						error = true;
						if (dto.erroImpeditivo) {
							erroImpeditivo = true;
						}
					} else {
						provavelErroConexaoPerdida();
					}
				} catch (err) {
					provavelErroConexaoPerdida()
				}
			}
			
			var alwaysCallback = function(dto) {
				if (erroImpeditivo) {
					deferred.reject(finalDto);
					return;
				}
				idsIndex++;
				if (idsIndex < idsSize) {
					deferred.notify({'percentagem': (idsIndex + 1)/ idsSize, 'indiceAtual': idsIndex+1});
					customBackendCall(docs[idsIndex]).done(doneCallback).fail(failCallback).always(alwaysCallback);
				} else {
					if (!error) {
						deferred.resolve(finalDto)
					} else {
						deferred.reject(finalDto);
					}
				}
			}
			deferred.notify({'percentagem': (idsIndex + 1)/ idsSize, 'indiceAtual': idsIndex+1});
			customBackendCall(docs[idsIndex]).done(doneCallback).fail(failCallback).always(alwaysCallback);
		} else {
			deferred.reject();
		}
		
		return deferred;
	}
	
	this.assinar = function(docs, senha) {
		var callAjax = function(paramDoc) {
			var deferred = $.Deferred();
			$.ajax({
				type: 'POST',
				url: '/edecisao/seam/resource/rest/' + buildDocAssinarUrl(paramDoc),
				data: {'senha': senha},
				dataType: 'json'
			}).done(function(data) {
				deferred.resolve(data);
			}).fail(function(xhr) {
				var dto = $.parseJSON(xhr.responseText);
				deferred.reject(dto);
			});
			
			return deferred.promise();
		}
		return self.assinarComCustomBackend(docs, senha, callAjax);
	};
	this.getDocumentosParaAssinar = function(){
		return ajaxUtil.ajax({
			type:'GET',
			url: '/edecisao/seam/resource/rest/assinador/documentos',
			dataType: 'json'
		});
	};
	this.getMinistro = function(){
		return ajaxUtil.ajax({
			type: 'GET',
			url: '/edecisao/seam/resource/rest/assinador/ministro',
			dataType: 'json'
		});
	};
	this.getDetalhesTexto = function(id){
		return ajaxUtil.ajax({
			type: 'GET',
			url: '/edecisao/seam/resource/rest/assinador/texto/' + id + '/detalhes',
			dataType: 'json'
		});
	};
	this.getDetalhesExpediente = function(id){
		return ajaxUtil.ajax({
			type: 'GET',
			url: '/edecisao/seam/resource/rest/assinador/expediente/' + id + '/detalhes',
			dataType: 'json'
		});
	};
	
	this.suspenderLiberacaoTexto = function(id){
		return ajaxUtil.ajax({
			type: 'POST',
			url: '/edecisao/seam/resource/rest/assinador/texto/' + id + '/suspender',
			dataType: 'json'
		});
	};
	
	this.devolverExpediente = function(id){
		return ajaxUtil.ajax({
			type: 'POST',
			url: '/edecisao/seam/resource/rest/assinador/comunicacao/' + id + '/devolver',
			dataType: 'json'
		});
	};
});

StfDecisao.util('Acoes', function() {
	this.ASSINAR_TEXTOS = {
		buildUrl: function(id) {
			return 'assinador/texto/' + id + '/assinar';
		}
	};
	
	this.ASSINAR_COMUNICACOES = {
		buildUrl: function(id) {
			return 'assinador/comunicacao/' + id + '/assinar';
		}
	};
	this.ASSINAR_DOCUMENTOS = {
		buildUrl: function(id) {
			return 'assinador/documento/' + id + '/assinar';
		}
	};
});