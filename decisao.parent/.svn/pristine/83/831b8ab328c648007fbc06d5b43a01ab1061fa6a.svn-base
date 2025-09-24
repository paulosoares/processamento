var StfDecisao = StfDecisao || {};

StfDecisao.initApp.push(function() {
	
	StfDecisao.assinadorApp.addPreInitStep('carregarViews', function() {
		var SELETOR_SISTEMA_CONTAINER = '#sistema-container';
		return $.when().then(StfDecisao.carregarView("assets_stf/js/app/view/login/login.html"))
						.then(StfDecisao.carregarView("assets_stf/js/app/view/sistema/main.html"))
						.then(StfDecisao.carregarView("assets_stf/js/app/view/sistema/selecaoDocs.html", SELETOR_SISTEMA_CONTAINER))
						.then(StfDecisao.carregarView("assets_stf/js/app/view/sistema/detalheTextos.html", SELETOR_SISTEMA_CONTAINER))
						.then(StfDecisao.carregarView("assets_stf/js/app/view/sistema/detalheExpedientes.html", SELETOR_SISTEMA_CONTAINER));
	});
	
	StfDecisao.assinadorApp.addPreInitStep('carregarViewConfirmar', function() {
		var SELETOR_SISTEMA_CONTAINER = '#sistema-container';
		return $.when().then(StfDecisao.carregarView("assets_stf/js/app/view/sistema/confirmarSenha.html", SELETOR_SISTEMA_CONTAINER));
	});
	
	StfDecisao.assinadorApp.addInitStep('flowServiceDeps', function() {
		StfDecisao.views = {
			"loginViewCtrl": {
				"id": StfDecisao.viewCtrls.getId('LoginViewCtrl')
			},
			"selecaoDocsViewCtrl": {
				"id": StfDecisao.viewCtrls.getId('SelecaoDocumentosAssinaturaViewCtrl')
			},
			"confirmarSenhaViewCtrl": {
				"id": StfDecisao.viewCtrls.getId('ConfirmarAssinaturaComSenhaViewCtrl')
			},
			"detalheExpedientesViewCtrl": {
				"id": StfDecisao.viewCtrls.getId('DetalheExpedientesViewCtrl')
			},
			"detalheTextosViewCtrl": {
				"id": StfDecisao.viewCtrls.getId('DetalheTextosViewCtrl')
			},
			"mainViewCtrl": {
				"id": StfDecisao.viewCtrls.getId('MainViewCtrl')
			}
		};
		StfDecisao.msgService = StfDecisao.services.instanciar('MessageService');
	});

	/**
	 * BrowserUtil est� no pre-init, pois � utilizado para tomar decis�es na inicializa��o.
	 * 
	 */
	StfDecisao.assinadorApp.addPreInitStep('browserUtil', function() {
		StfDecisao.browserUtil = StfDecisao.utils.instanciar('BrowserUtil', [window]);
	});
	
	StfDecisao.assinadorApp.addPreInitStep('togglesUtil', function() {
		StfDecisao.togglesUtil = StfDecisao.utils.instanciar('TogglesUtil');
	});
	
	StfDecisao.assinadorApp.addPreInitStep('subFlowUtil', function() {
		StfDecisao.subFlowUtil = StfDecisao.utils.instanciar('SubFlowUtil', [StfDecisao.togglesUtil]);
	});
	
	StfDecisao.assinadorApp.addPreInitStep('ajaxUtil', function() {
		StfDecisao.ajaxUtil = StfDecisao.utils.instanciar('AjaxUtil', [window, navigator]);
	});
	
	StfDecisao.assinadorApp.addInitStep('flowService', function() {
		StfDecisao.flowService = StfDecisao.services.instanciar('FlowService', [StfDecisao.views, StfDecisao.msgService, StfDecisao.togglesUtil]);
	});
	
	StfDecisao.assinadorApp.addInitStep('loader', function() {
		StfDecisao.loaderService = StfDecisao.services.instanciar('LoaderService');
	});
	
	StfDecisao.assinadorApp.addInitStep('viewCtrlsDeps', function() {
		StfDecisao.acoes = StfDecisao.utils.instanciar('Acoes');
		StfDecisao.assinaturaService = StfDecisao.services.instanciar('AssinaturaService', [StfDecisao.acoes, StfDecisao.ajaxUtil]);
		StfDecisao.assinaturaModel = StfDecisao.models.instanciar('AssinaturaModel', [StfDecisao.acoes]);
		StfDecisao.assinaturaProgressUtil = StfDecisao.utils.instanciar('AssinaturaProgressUtil');
		StfDecisao.promiseUtil = StfDecisao.utils.instanciar('PromiseUtil');
		StfDecisao.datatableUtil = StfDecisao.utils.instanciar('DatatableUtil');
		StfDecisao.setorService = StfDecisao.services.instanciar('SetorService', [StfDecisao.ajaxUtil]);
	});
	
	StfDecisao.assinadorApp.addInitStep('views', function() {
		StfDecisao.loginService = StfDecisao.services.instanciar('LoginService', [StfDecisao.browserUtil]);
		StfDecisao.loginViewCtrl = StfDecisao.viewCtrls.instanciar('LoginViewCtrl', [StfDecisao.loginService, StfDecisao.flowService]);
		StfDecisao.views["loginViewCtrl"].instance = StfDecisao.loginViewCtrl;
		
		StfDecisao.selecaoDocsViewCtrl = StfDecisao.viewCtrls.instanciar('SelecaoDocumentosAssinaturaViewCtrl',
				[StfDecisao.assinaturaService, StfDecisao.msgService, StfDecisao.assinaturaModel, StfDecisao.flowService, StfDecisao.loaderService, StfDecisao.datatableUtil, StfDecisao.togglesUtil]);
		StfDecisao.views["selecaoDocsViewCtrl"].instance = StfDecisao.selecaoDocsViewCtrl;
		
		StfDecisao.detalheExpedientesViewCtrl = StfDecisao.viewCtrls.instanciar('DetalheExpedientesViewCtrl', [StfDecisao.assinaturaModel,
				StfDecisao.assinaturaService, StfDecisao.flowService, StfDecisao.msgService, StfDecisao.loaderService, StfDecisao.browserUtil]);
		StfDecisao.views["detalheExpedientesViewCtrl"].instance = StfDecisao.detalheExpedientesViewCtrl;
		
		StfDecisao.detalheTextosViewCtrl = StfDecisao.viewCtrls.instanciar('DetalheTextosViewCtrl', [StfDecisao.assinaturaModel,
				StfDecisao.assinaturaService, StfDecisao.flowService, StfDecisao.msgService, StfDecisao.loaderService]);
		StfDecisao.views["detalheTextosViewCtrl"].instance = StfDecisao.detalheTextosViewCtrl;
		
		StfDecisao.mainViewCtrl = StfDecisao.viewCtrls.instanciar('MainViewCtrl', [StfDecisao.versaoService, StfDecisao.certificadoService, StfDecisao.certificadoUtil,
		                                                                           StfDecisao.flowService, StfDecisao.msgService, StfDecisao.setorService]);
		StfDecisao.views["mainViewCtrl"].instance = StfDecisao.mainViewCtrl;
	});
	
	StfDecisao.assinadorApp.addInitStep('viewConfirmar', function() {
		StfDecisao.confirmarSenhaViewCtrl = StfDecisao.viewCtrls.instanciar('ConfirmarAssinaturaComSenhaViewCtrl', [StfDecisao.assinaturaProgressUtil,
		        StfDecisao.assinaturaModel, StfDecisao.assinaturaService, StfDecisao.msgService, StfDecisao.flowService]);
		StfDecisao.views["confirmarSenhaViewCtrl"].instance = StfDecisao.confirmarSenhaViewCtrl;
	});
	
	StfDecisao.assinadorApp.addInitStep('loginChecarLogado', function() {
		StfDecisao.loginService.checarSeEstaLogado(function(estaLogado) {
			if (estaLogado) {
				StfDecisao.flowService.irParaSistema();
			} else {
				StfDecisao.flowService.irParaLogin();
			}
		}, function() {
			StfDecisao.flowService.irParaLogin();
		});
	});
});