'use strict';

StfDecisao.viewCtrl('ConfirmarAssinaturaComSenhaViewCtrl', 'confirmar-assinatura-com-senha',
		function(assinaturaProgressUtil, assinaturaModel, assinaturaService, msgService, flowService) {
	var self = this;
	
	var SELETOR_SENHA_ASSINATURA = '#senha-assinatura';
	var SELETOR_QTDE_ASSINAR = '#qtde-docs-assinar-senha';
	var SELETOR_ASSINANDO_DOCS = '#assinando-docs';
	var SELETOR_DESISTIR = '#desistir';
	
	this.viewIn = function() {
		$(SELETOR_SENHA_ASSINATURA).val('');
		msgService.hide();
		$(SELETOR_QTDE_ASSINAR).html(assinaturaModel.getDocumentosASeremAssinados().length);
		$(SELETOR_ASSINANDO_DOCS).show();
		$(SELETOR_SENHA_ASSINATURA).show();
	}
	
	this.progressButton = assinaturaProgressUtil.registerProgress(this, assinaturaModel, assinaturaService, msgService, flowService);
	
	var registerEvents = function() {
		$(SELETOR_DESISTIR).click(function(e) {
			e.preventDefault();
			msgService.clear();
			flowService.irParaInicio(false);
		});
	};
	
	registerEvents();
});