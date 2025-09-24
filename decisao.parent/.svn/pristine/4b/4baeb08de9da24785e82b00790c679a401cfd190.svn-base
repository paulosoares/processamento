'use strict';

StfDecisao.util('AssinaturaProgressUtil', function() {
	
	this.registerProgress = function(viewCtrl, assinaturaModel, assinaturaService, msgService, flowService) {
		
		var terminarComEspera = function(callback) {
			setTimeout(function() {
				callback();
			}, 1000);
		};
		
		var progressButton = new ProgressButton($('#assinar-agora').get(0), {
			callback: function() {
				msgService.clear();
				var senha = $('#senha-assinatura').val();
				$('#senha-assinatura').val('');
				$("#desistir").attr("disabled", true);
				$('#assinar-agora .content').html("Assinando...");
				$('#assinando-docs').hide();
				$('#senha-assinatura').hide();
				var promise = assinaturaService.assinar(assinaturaModel.getDocumentosASeremAssinados(), senha);
				progressButton._setProgress(0);
				promise.done(function(dto) {
					if (dto.warnings.length > 0) {
						msgService.addWarning(dto.warnings);
					}
					var qtdeAssinados = dto.quantidadeTextosAssinados + dto.quantidadeComunicacoesAssinadas;
					if (qtdeAssinados > 0) {
						if (qtdeAssinados == 1) {
							msgService.addSucesso(qtdeAssinados + ' documento foi assinado com sucesso.');
						} else {
							if (dto.warnings.length == 0) {
								msgService.addSucesso('Todos os ' + qtdeAssinados + ' documentos foram assinados com sucesso.');
							} else {
								msgService.addSucesso(qtdeAssinados + ' documentos foram assinados com sucesso.');
							}
						}
					}
					progressButton._stop(1);
				}).fail(function(dto) {
					msgService.addError(dto.errors);
					msgService.addWarning(dto.warnings);
					progressButton._stop(-1);
				}).progress(function(obj) {
					progressButton._setProgress(obj.percentagem);
					$('#assinar-agora .content').html("Assinando... " + obj.indiceAtual + " de " + assinaturaModel.getDocumentosASeremAssinados().length);
				}).always(function(dto) {
					if (!dto.erroImpeditivo) {
						terminarComEspera(function() {
							flowService.irParaInicio(true);
							$("#desistir").removeAttr("disabled");
							$('#assinar-agora .content').html("Assinar Agora");
						});
					} else {
						if (dto.erroSenhaInvalida) { // Mantém na mesma página.
							terminarComEspera(function() {
								viewCtrl.viewIn();
								msgService.show();
								$("#desistir").removeAttr("disabled");
								$('#assinar-agora .content').html("Assinar Agora");
							});
						} else {
							if (dto.erroFatal) {
								terminarComEspera(function() {
									flowService.irParaInicio(false, true);
									$("#desistir").removeAttr("disabled");
									$('#assinar-agora .content').html("Assinar Agora");
								});
							} else {
								terminarComEspera(function() {
									flowService.irParaInicio(true);
									$("#desistir").removeAttr("disabled");
									$('#assinar-agora .content').html("Assinar Agora");
								});
							}
						}
					}
				});
			}
		});
		return progressButton;
	};
	
});