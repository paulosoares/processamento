var StfDecisao = StfDecisao || {};

StfDecisao.initApp.push(function() {
	var mostrarApp = function() {
		$('body').show();
	};
	
	/**
	 * Workaround que corrige o problema do usuário clicar fora do campo de senha
	 * e não conseguir mais continuar digitando a senha.
	 * 
	 * Adaptado de http://stackoverflow.com/questions/13124340/cant-type-into-html-input-fields-on-ios-after-clicking-twice .
	 */
	var fixIpadTouch = function() {
		var elementosInput = $('input[type=text], input[type=password]');
		elementosInput.on('keydown', function() {
			window.focus();
			$(this).focus();
		});
	};
	
	StfDecisao.assinadorApp.addPreInitStep('carregarViews', function() {
		return $.when().then(StfDecisao.carregarView("assets_stf/js/app/native/view/importacao/importacao.html"));
	});
	
	/**
	 * Override do carregamento da view de confirmar.
	 */
	StfDecisao.assinadorApp.addPreInitStep('carregarViewConfirmar', function() {
		var SELETOR_SISTEMA_CONTAINER = '#sistema-container';
		return $.when().then(StfDecisao.carregarView("assets_stf/js/app/native/view/sistema/confirmacao-cert-senha.html", SELETOR_SISTEMA_CONTAINER));
	}, true);
	
	StfDecisao.assinadorApp.addPreInitStep('initFacade', function() {
		if (StfDecisao.browserUtil.isMobile()) {
			StfDecisao.mni = StfDecisao.facades.instanciar('MobileNativeInterfaceFacade');
		} else {
			StfDecisao.mni = StfDecisao.facades.instanciar('MockMobileNativeInterfaceFacade');
		}
	});
	
	StfDecisao.assinadorApp.addPreInitStep('versaoService', function() {
		StfDecisao.versaoService = StfDecisao.services.instanciar("VersaoService", [StfDecisao.mni, StfDecisao.ajaxUtil]);
	});
	
	StfDecisao.assinadorApp.addPreInitStep('certificadoService', function() {
		StfDecisao.certificadoService = StfDecisao.services.instanciar("CertificadoService", [StfDecisao.mni]);
	});
	
	StfDecisao.assinadorApp.addPreInitStep('flowServiceResolver', function() {
		StfDecisao.flowServiceResolver = {};
	});
	
	StfDecisao.assinadorApp.addPreInitStep('certificadoUtil', function() {
		StfDecisao.certificadoUtil = StfDecisao.utils.instanciar('CertificadoUtil');
	});
	
	StfDecisao.assinadorApp.addPreInitStep('msgServiceNative', function() {
		StfDecisao.msgServiceNative = StfDecisao.services.instanciar('MessageServiceNative');
	});
	
	StfDecisao.assinadorApp.addInitStepAfterHook('flowServiceDeps', function() {
		StfDecisao.views['confirmarSenhaViewCtrl'] = {
				"id": StfDecisao.viewCtrls.getId('ConfirmarCertificadoSenhaViewCtrl')
		};
	});
	
	StfDecisao.assinadorApp.addInitStepAfterHook('flowService', function() {
		StfDecisao.flowService = StfDecisao.services.instanciar('NativeFlowService', [StfDecisao.flowService, StfDecisao.views, StfDecisao.msgService, StfDecisao.mni]);
		StfDecisao.flowServiceResolver.instance = StfDecisao.flowService;
	});
	
	StfDecisao.assinadorApp.addPreInitStep('importacaoView', function() {
		StfDecisao.importarCertificadoViewCtrl = StfDecisao.viewCtrls.instanciar('ImportarCertificadoViewCtrl', [StfDecisao.subFlowUtil, StfDecisao.mni, StfDecisao.flowServiceResolver, StfDecisao.msgServiceNative, StfDecisao.certificadoUtil]);
	});
	
	StfDecisao.assinadorApp.addInitStep('viewConfirmar', function() {
		StfDecisao.confirmarCertificadoSenhaViewCtrl = StfDecisao.viewCtrls.instanciar('ConfirmarCertificadoSenhaViewCtrl', [StfDecisao.assinaturaProgressUtil,
			StfDecisao.assinaturaModel, StfDecisao.assinaturaMobileService, StfDecisao.msgService, StfDecisao.flowService, StfDecisao.mni, StfDecisao.certificadoUtil]);
		StfDecisao.views["confirmarSenhaViewCtrl"].instance = StfDecisao.confirmarCertificadoSenhaViewCtrl;
	}, true);
	
	StfDecisao.assinadorApp.addPreInitStep('importacao', function() {
		var deferred = $.Deferred();
		StfDecisao.mni.existeCertificadoParaImportacao().done(function(exists) {
			console.log(exists);
			if (exists) {
				mostrarApp(); // Rotina de importação.
				StfDecisao.importarCertificadoViewCtrl.setFinishedCallback(function() {
					deferred.resolve();
				});
				StfDecisao.importarCertificadoViewCtrl.show();
			} else {
				deferred.resolve();
			}
		}).fail(function() {
			deferred.resolve();
		});
		return deferred.promise();
	});
	
	StfDecisao.assinadorApp.addInitStepAfterHook('viewCtrlsDeps', function() {
		StfDecisao.acoesNative = StfDecisao.utils.instanciar('AcoesNative');
		StfDecisao.assinaturaMobileService = StfDecisao.services.instanciar('AssinaturaMobileService', [StfDecisao.assinaturaService, StfDecisao.mni, StfDecisao.acoesNative, StfDecisao.promiseUtil, StfDecisao.ajaxUtil]);
	});
	
	StfDecisao.assinadorApp.addInitStep('final', function() {
		fixIpadTouch();
		mostrarApp(); // Passo final. Mostrar app.
	});
});