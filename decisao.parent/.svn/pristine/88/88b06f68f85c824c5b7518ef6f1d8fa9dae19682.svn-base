'use strict';

StfDecisao.viewCtrl('DetalheTextosViewCtrl', 'detalhe-textos', function(assinaturaModel, assinaturaService, flowService, msgService, loaderService) {
	var self = this;
	this.texto = null;
	var seletorContainer = '#detalhe-textos .email-body';
	
	function carregarConteudo(id) {
		var frame = $('<iframe frameborder="0" src="about:blank"></iframe>');
		$(seletorContainer).css('height', 0);
		$(seletorContainer).find('iframe').remove();
		$(seletorContainer).append(frame);
		
		function ajustarAltura() {
			var height = frame.contents().find('html').height();
			$('#detalhe-textos .email-body').css('height', height);
		}
		
		frame.load(ajustarAltura);
		frame.attr('src', '/edecisao/seam/resource/rest/assinador/texto/' + id + '/html');
	}
	
	this.detalhar = function(id){
		var deferred = $.Deferred();
		loaderService.loaderOn();
		$('#qtde-suspender').text(assinaturaModel.getDocumentosComListaTextosIguais(id).length);
		assinaturaService.getDetalhesTexto(id).done(function(data) {
			self.texto = data;
			$('#numero-processo-texto').text(data.processo);
			$('#descricao').text(data.descricao);
			if (data.responsavel) {
				$('#responsavel-texto').text(data.responsavel);
			} else {
				$('#responsavel-texto').text("Não possui responsável");
			}
			$('#texto-data').text(data.dataFormatada);
			carregarConteudo(id);
		}).fail(function(data) {
			msgService.addError(data.errors);
			msgService.show();
			flowService.irParaInicio(false);
		}).always(function() {
			loaderService.loaderOff();
			deferred.resolve();
		});
		return deferred.promise();
	}
	
	this.update = function() {
		
	}
	
	
	var registerEvents = function() {
		$('#suspender-liberacao').click(function (e){
			e.preventDefault();
			msgService.clear();
			assinaturaService.suspenderLiberacaoTexto(self.texto.id).done(function () {
				msgService.addSucesso('A liberação do texto foi suspensa com sucesso.');
				msgService.show();
				flowService.irParaInicio(true);
			}).fail(function(data) {
				msgService.addError(data.errors);
				msgService.show();
			});
		});
		$('#btn-back-texto').click(function(e) {
			e.preventDefault();
			flowService.irParaInicio(false);
		});
		$('#btn-assinar-este-texto').click(function(e) {
			e.preventDefault();
			assinaturaModel.setDocumentosASeremAssinados([self.texto]);
			flowService.irParaSenha();
		});
	};
	
	registerEvents();
});