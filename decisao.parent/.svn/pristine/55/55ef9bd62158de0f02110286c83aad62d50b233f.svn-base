'use strict';

StfDecisao.viewCtrl("MainViewCtrl", 'main-view', function(versaoService, certificadoService, certificadoUtil, flowService, msgService, setorService) {
	var self = this;
	
	var SELETOR_LINK_AJUDA = "#clicar-ajuda";
	var SELETOR_TEXTO_AJUDA = "#exibir-ajuda";
	
	var SELETOR_VERSAO_SISTEMA = ".versao-sistema";
	
	var SELETOR_LINK_EXIBIR_CERTIFICADO = ".clicar-certificado";
	var SELETOR_PAINEL_CERTIFICADO = "#exibir-certificado";
	var SELETOR_DADOS_CERTIFICADO = "#menu-dados-certificado";
	var SELETOR_LINK_EXCLUIR_CERTIFICADO = "#excluir-certificado";
	
	var SELETOR_INFORMACOES_ASSINADOR = '#sistema-container .profile-info';
	
	var SELETOR_BOTAO_VOLTAR_MAIN = '.btn-back-main';
	
	var SELETOR_SELECT_SETOR = '.select-setor';
	var SELETOR_LABEL_SETOR = '.label-setor';
	var SELETOR_SPAN_SETOR = SELETOR_LABEL_SETOR + ' .span-setor';
	
	this.viewIn = function (){
		versaoService.getVersao().done(function(data) {
			$(SELETOR_VERSAO_SISTEMA).text(data);
		});
		certificadoService.getCertificado().done(function(data) {
			$(SELETOR_DADOS_CERTIFICADO).html(certificadoUtil.cadeiaInHtml(data));
			$(SELETOR_LINK_EXCLUIR_CERTIFICADO).show();
		}).fail(function(data) {
			$(SELETOR_DADOS_CERTIFICADO).html("Nenhum certificado cadastrado.");
			$(SELETOR_LINK_EXCLUIR_CERTIFICADO).hide();
		});
		setorService.getSetores().done(function(setores) {
			setorService.getSetorAtual().done(function(setorAtual) {
				if (setores.length > 1) {
					$(SELETOR_LABEL_SETOR).hide();
					$(SELETOR_SELECT_SETOR).empty();
					for (var i in setores) {
						var option = {
							value: setores[i].id,
							text: setores[i].nome
						};
						if (setores[i].id == setorAtual.id) {
							option.selected = true;
						}
						
						$(SELETOR_SELECT_SETOR).append($('<option>', option));
					}
					$(SELETOR_SELECT_SETOR).show();
				} 
			});
		});
	};
	
	this.iniciarFullScreen = function() {
		$(SELETOR_BOTAO_VOLTAR_MAIN).css('visibility', 'visible');
		$(SELETOR_INFORMACOES_ASSINADOR).hide();
	};
	
	var registerEvents = function() {
		$(SELETOR_LINK_AJUDA).click(function(e){
			e.preventDefault();
			$(SELETOR_TEXTO_AJUDA).slideToggle();
		});
		$(SELETOR_LINK_EXIBIR_CERTIFICADO).click(function(e){
			e.preventDefault();
			$(SELETOR_PAINEL_CERTIFICADO).slideToggle();
		});
		$(SELETOR_LINK_EXCLUIR_CERTIFICADO).click(function(e) {
			e.preventDefault();
			var excluir = confirm("Esta operação irá excluir o certificado do dispositivo. Deseja continuar?");
			if (excluir) {
				var confirmacao = confirm("Não será possível assinar documentos sem um certificado no dispositivo, sendo necessária uma nova importação. Tem certeza que deseja excluir o certificado?");
				if (confirmacao) {
					msgService.clear();
					$(SELETOR_PAINEL_CERTIFICADO).slideToggle();
					certificadoService.excluirCertificado().done(function() {
						msgService.addSucesso("Certificado excluído com sucesso.");
					}).fail(function(data) {
						msgService.addError(data.errors);
					}).always(function() {
						msgService.show();
						self.viewIn();
						flowService.irParaInicio(false);
					});
				}
			}
		});
		$(SELETOR_BOTAO_VOLTAR_MAIN).click(function(e) {
			e.preventDefault();
			$(SELETOR_BOTAO_VOLTAR_MAIN).css('visibility', 'hidden');
			$(SELETOR_INFORMACOES_ASSINADOR).show();
			flowService.desativarFullScreen();
		});
		$(SELETOR_SELECT_SETOR).change(function(e) {
			var idNovoSetor = $(this).val();
			if (idNovoSetor) {
				setorService.mudarSetor($(this).val()).done(function() {
					msgService.clear();
					flowService.irParaInicio(true);
				});
			}
		});
	};
	
	registerEvents();
	
});