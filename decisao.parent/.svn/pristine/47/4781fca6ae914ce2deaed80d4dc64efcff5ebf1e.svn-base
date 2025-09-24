'use strict';

StfDecisao.viewCtrl('DetalheExpedientesViewCtrl', 'detalhe-expedientes', function(assinaturaModel, assinaturaService, flowService, msgService, loaderService, browserUtil) {
	var self = this;
	
	this.expediente = null;
	var seletorContainer = '#detalhe-expedientes-conteudo .pdf-container';
	var seletorContainerOut = '#detalhe-expedientes-conteudo .pdf-container-outer';
	var seletorContainerDesktop = '#detalhe-expedientes-conteudo .pdf-container-desktop';
	var SELETOR_PAINEL_DETALHES = '#detalhe-expedientes';
	var SELETOR_PAINEL_PDF = '#detalhe-expedientes-conteudo';
	var pageHeight = 850;
	
	function carregarConteudo(expediente) {
		var finalizarCarregamento = function() {
			flowService.ativarFullScreen(function() {
				$(SELETOR_PAINEL_DETALHES).show();
				$(SELETOR_PAINEL_PDF).hide();
				$(seletorContainer).hide();
				$(seletorContainer).find('iframe').remove();
			});
			$(SELETOR_PAINEL_DETALHES).hide();
			$(SELETOR_PAINEL_PDF).show();
			$(seletorContainer).show();
		};
		
		var ajustar = function() {
			if (browserUtil.isMobile()) {
				if (width > 700) {
					var adjustedWidth = width + 30;
				} else {
					var adjustedWidth = width;
				}
				
				console.log('body width', $('body').width());
				$(seletorContainer).css('width', adjustedWidth);
				
				$(seletorContainer).find('iframe').css('width', 610);
				if (adjustedWidth > 610) {
					var scale = adjustedWidth / 610;
					$(seletorContainer).find('iframe').css('-webkit-transform', 'scale(' + scale + ')');
				}
			}
			console.log('body width', $('body').width());
			finalizarCarregamento();
		}
		
		var totalHeight = expediente.paginasConteudo * pageHeight;
		console.log(totalHeight);
		$(seletorContainerOut).css('height', pageHeight);
		
		var urlPdf = '/edecisao/seam/resource/rest/assinador/expediente/' + expediente.id + '/pdf';
		
		var width = $('body').width();
		console.log('body width', width);
		var frame;
		if (browserUtil.isMobile()) {
			$(seletorContainerOut).show();
			frame = $('<iframe frameborder="0" src="about:blank" height="' + totalHeight + '"></iframe>');
			$(seletorContainer).css('height', totalHeight);
			$(seletorContainer).append(frame);
			frame.load(ajustar);
			frame.attr('src', urlPdf);
		} else {
			$(seletorContainerDesktop).show();
			frame = $('<iframe frameborder="0" src="' + urlPdf + '" height="' + pageHeight + '" width="' + width + '"></iframe>');
			$(seletorContainerDesktop).append(frame);
			finalizarCarregamento();
		}
	}
	
	this.detalhar = function(id){
		var deferred = $.Deferred();
		$(seletorContainer).hide();
		loaderService.loaderOn();
		
		return assinaturaService.getDetalhesExpediente(id).done(function(data) {
			$('#numero-processo-expediente').text(data.processo);
			$('#titulo-expediente').text(data.descricao);
			$('#expediente-data').text(data.dataFormatada);
			
			self.expediente = data;
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
		$('#btn-back-expediente').click(function(e) {
			e.preventDefault();
			flowService.irParaInicio(false);
		});
		
		$('#devolver-expediente').click(function(e) {
			e.preventDefault();
			assinaturaService.devolverExpediente(self.expediente.id).done(function() {
				msgService.addSucesso('O expediente foi devolvido com sucesso.');
				msgService.show();
				flowService.irParaInicio(true);
			}).fail(function(data) {
				msgService.addError(data.errors);
				msgService.show();
			});
		});
		$('#btn-assinar-este-expediente').click(function(e) {
			e.preventDefault();
			assinaturaModel.setDocumentosASeremAssinados([self.expediente]);
			flowService.irParaSenha();
		});
		$('#link-visualizar-expediente').click(function(e) {
			e.preventDefault();
			carregarConteudo(self.expediente);
		});
		
//		var prevLeft = 0;
		// TODO Verificou se isso adiantou alguma coisa
//		$(seletorContainerOut).scroll(function(e) {
//			console.log('scroll');
//			var currentLeft = $(this).scrollLeft();
//			if (prevLeft === currentLeft) {
//				console.log('vertical');
//				prevLeft = currentLeft;
//				e.preventDefault();
//				e.stopPropagation();
//			} else {
//				console.log('horizontal');
//				prevLeft = currentLeft;
//				
//			}
//		});
	};
	
	registerEvents();
});