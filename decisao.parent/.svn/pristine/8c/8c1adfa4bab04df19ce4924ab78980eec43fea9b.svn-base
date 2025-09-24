'use strict';

StfDecisao.viewCtrl('ConfirmarCertificadoSenhaViewCtrl', 'confirmar-cert-senha', function(assinaturaProgressUtil, assinaturaModel, assinaturaMobileService, msgService, flowService, mni, certificadoUtil) {
	var self = this;
	
	var SELETOR_SENHA_ASSINATURA = '#senha-assinatura';
	var SELETOR_QTDE_ASSINAR = '#qtde-docs-assinar-senha';
	var SELETOR_ASSINANDO_DOCS = '#assinando-docs';
	var SELETOR_DESISTIR = '#desistir';
	
	this.viewIn = function() {
		$(SELETOR_SENHA_ASSINATURA).val('');
		$(SELETOR_QTDE_ASSINAR).html(assinaturaModel.getDocumentosASeremAssinados().length);
		$(SELETOR_ASSINANDO_DOCS).show();
		$(SELETOR_SENHA_ASSINATURA).show();
		var promise = mni.recuperarDadosCertificado().done(function(data) {
	        $('#cert-assinando-cadeia').html(certificadoUtil.cadeiaInHtml(data));
		}).fail(function(data) {
			msgService.addError(data.errors);
			msgService.show();
			flowService.irParaInicio(false);
		});
	}
	
	this.progressButton = assinaturaProgressUtil.registerProgress(this, assinaturaModel, assinaturaMobileService, msgService, flowService);
	
	var registerEvents = function() {
		$(SELETOR_DESISTIR).click(function(e) {
			e.preventDefault();
			msgService.clear();
			flowService.irParaInicio(false);
		});
	};
	
	registerEvents();
});